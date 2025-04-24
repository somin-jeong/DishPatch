package com.example.dishpatch.domain.order.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.order.request.OrderRequestDto;
import com.example.dishpatch.api.order.request.OrderStatusRequestDto;
import com.example.dishpatch.api.order.response.MenuOptionDetailResponseDto;
import com.example.dishpatch.api.order.response.OrderDetailResponseDto;
import com.example.dishpatch.api.order.response.OrderItemDetailResponseDto;
import com.example.dishpatch.api.order.response.OrderResponseDto;
import com.example.dishpatch.domain.coupon.exception.CouponErrorCode;
import com.example.dishpatch.domain.coupon.service.CouponService;
import com.example.dishpatch.domain.order.exception.OrderErrorCode;
import com.example.dishpatch.domain.pointHistory.exception.PointErrorCode;
import com.example.dishpatch.domain.pointHistory.service.PointHistoryService;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.domain.store.service.StoreService;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import com.example.dishpatch.infra.db.coupon.entity.CouponType;
import com.example.dishpatch.infra.db.order.aop.LogOrderCreation;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.entity.OrderItem;
import com.example.dishpatch.infra.db.order.entity.OrderStatus;
import com.example.dishpatch.infra.db.order.repository.OrderItemRepository;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartService cartService;
	private final CouponService couponService;
	private final PointHistoryService pointHistoryService;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final OrderItemService orderItemService;
	private final OrderItemRepository orderItemRepository;
	private final StoreService storeService;

	@LogOrderCreation
	public OrderResponseDto createOrder(OrderRequestDto requestDto, Long userId) {

		Coupon coupon = null;

		if (requestDto.couponId() != null) {
			coupon = couponService.getCoupon(requestDto.couponId());
		}

		Integer remainingPoint = 0;

		if (requestDto.point() != null) {
			remainingPoint = pointHistoryService.getRemainingPoint(userId);
			if (remainingPoint == null) {
				throw new BizException(PointErrorCode.NO_POINT);
			}

			if (requestDto.point() > remainingPoint) {
				throw new BizException(PointErrorCode.INSUFFICIENT_POINT);
			}
		}

		List<CartResponseDto> cartResponseDtoList = cartService.findCarts(userId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));
		Store store = storeRepository.findById(cartResponseDtoList.get(0).storeId())
			.orElseThrow(() -> new BizException(StoreErrorCode.STORE_NOT_FOUND));

		int totalPrice = 0;

		for (CartResponseDto cartResponseDto : cartResponseDtoList) {
			totalPrice +=
				cartResponseDto.qunatity() * (cartResponseDto.menuPrice() + cartResponseDto.menuOptionPrice());
		}

		verifyStore(store, totalPrice);

		totalPrice = checkCoupon(totalPrice, coupon);

		if (totalPrice < remainingPoint) {
			throw new BizException(PointErrorCode.EXCEEDING_POINT_AMOUNT);
		}

		totalPrice -= remainingPoint;

		totalPrice += store.getDeliveryFee();

		Order order = new Order(totalPrice, user, store);

		Order savedOrder = orderRepository.save(order);

		List<Long> orderItemIds = orderItemService.addOrderItem(savedOrder.getId(), cartResponseDtoList);

		if (coupon != null) {
			couponService.useCoupon(coupon);
		}

		if (requestDto.point() != null) {
			pointHistoryService.usePoint(userId, requestDto.point());

		}

		return new OrderResponseDto(
			savedOrder.getId(),
			userId,
			cartResponseDtoList.get(0).getStoreId(),
			orderItemIds,
			totalPrice,
			savedOrder.getStatus()
		);
	}

	private void verifyStore(Store store, int totalPrice) {

		LocalTime now = LocalTime.now();

		if (totalPrice < store.getMinDeliveryPrice()) {
			throw new BizException(OrderErrorCode.UNDER_MINIMUM_ORDER_PRICE);
		}
		if (store.getOpenTime().isAfter(now) || store.getCloseTime().isBefore(now)) {
			throw new BizException(OrderErrorCode.STORE_CLOSED);
		}
	}

	private int checkCoupon(int totalPrice, Coupon coupon) {
		if (coupon == null) {
			return totalPrice;
		}

		if (coupon.getCoupontype() == CouponType.A) {
			if (coupon.getMaxDiscount() < totalPrice * (100 - coupon.getDeductedPrice()) / 100) {
				totalPrice -= coupon.getMaxDiscount();
			} else {
				totalPrice -= totalPrice * (100 - coupon.getDeductedPrice()) / 100;
			}
		} else if (coupon.getCoupontype() == CouponType.B) {
			if (coupon.getDeductedPrice() > totalPrice) {
				throw new BizException(CouponErrorCode.COUPON_EXCEEDS_TOTAL);
			} else {
				totalPrice -= coupon.getDeductedPrice();
			}
		}

		return totalPrice;
	}

	@Transactional
	public OrderResponseDto updateOrder(Long userId, Long orderId, @Valid OrderStatusRequestDto requestDto) {

		User user = validateUser(userId);

		Order order = validateOrder(user, orderId);

		if (!order.getStatus().equals(requestDto.getOrderStatus())) {
			throw new BizException(OrderErrorCode.INVALID_ORDER_STATUS);
		}

		if (order.getStatus() == OrderStatus.CHECKING) {
			order.updateStatus(OrderStatus.COOKING);
		} else if (order.getStatus() == OrderStatus.COOKING) {
			order.updateStatus(OrderStatus.DELIVERING);
		} else if (order.getStatus() == OrderStatus.DELIVERING) {
			order.updateStatus(OrderStatus.FINISHED);
			pointHistoryService.getPoint(userId, order.getTotalPrice());
		}

		List<Long> menuIds = orderItemService.getOrderItems(orderId);

		return new OrderResponseDto(
			order.getId(),
			userId,
			order.getStore().getId(),
			menuIds,
			order.getTotalPrice(),
			order.getStatus()
		);

	}

	private Order validateOrder(User user, Long orderId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BizException(OrderErrorCode.ORDER_NOT_FOUND));

		if (!Objects.equals(order.getStore().getUser().getId(), user.getId())) {
			throw new BizException(StoreErrorCode.STORE_OWNER_MISMATCH);
		}

		if (order.getStatus() == OrderStatus.FINISHED) {
			throw new BizException(OrderErrorCode.ORDER_ALREADY_FINISHED);
		}

		return order;
	}

	private User validateUser(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		return user;
	}

	@Transactional
	public void refuseOrder(Long userId, Long orderId) {

		User user = validateUser(userId);

		Order order = validateOrder(user, orderId);

		if (!order.getStatus().equals(OrderStatus.CHECKING)) {
			throw new BizException(OrderErrorCode.INVALID_ORDER_REJECTION);
		}

		order.updateStatus(OrderStatus.REFUSED);
	}

	public List<OrderResponseDto> findAllOrders(Long userId) {

		User user = validateUser(userId);

		List<Order> orders = new ArrayList<>();

		if (user.getRole().equals(UserRole.USER)) {
			orders = orderRepository.findByUser(user);
		} else if (user.getRole().equals(UserRole.CEO)) {
			List<Long> storeIds = storeRepository.findIdByUser(user);
			orders = orderRepository.findByStoreIds(storeIds);
		}

		return orders.stream()
			.map(order -> new OrderResponseDto(
				order.getId(),
				order.getUser().getId(),
				order.getStore().getId(),
				orderItemService.getOrderItems(order.getId()),
				order.getTotalPrice(),
				order.getStatus()
			))
			.collect(Collectors.toList());
	}

	public OrderDetailResponseDto findOrderDetails(Long userId, Long orderId) {

		User user = validateUser(userId);

		Order order = validateDetailOrder(user, orderId);

		List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

		List<OrderItemDetailResponseDto> orderItemDtos = orderItems.stream()
			.map(orderItem -> new OrderItemDetailResponseDto(
				orderItem.getMenu().getId(),
				orderItem.getMenu().getName(),
				orderItem.getMenuOption() != null ?
					List.of(new MenuOptionDetailResponseDto(
						orderItem.getMenuOption().getId(),
						orderItem.getMenuOption().getName()
					)) : List.of(),
				orderItem.getQuantity()
			))
			.toList();

		return new OrderDetailResponseDto(
			order.getId(),
			order.getUser().getId(),
			order.getStore().getId(),
			orderItemDtos,
			order.getTotalPrice(),
			order.getStatus(),
			order.getCreatedDate()
		);
	}

	private Order validateDetailOrder(User user, Long orderId) {

		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BizException(OrderErrorCode.ORDER_NOT_FOUND));

		if (order.getUser().getId().equals(user.getId())) {
			return order;
		}

		if (order.getStore().getUser().getId().equals(user.getId())) {
			return order;
		}

		throw new BizException(OrderErrorCode.INACCESSIBLE_ORDER);
	}
}
