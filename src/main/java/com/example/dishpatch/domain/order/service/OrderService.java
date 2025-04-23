package com.example.dishpatch.domain.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.order.request.OrderRequestDto;
import com.example.dishpatch.api.order.response.OrderResponseDto;
import com.example.dishpatch.domain.coupon.service.CouponService;
import com.example.dishpatch.domain.pointHistory.service.PointHistoryService;
import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import com.example.dishpatch.infra.db.coupon.entity.CouponType;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

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

	public OrderResponseDto createOrder(OrderRequestDto requestDto, Long userId) {

		Coupon coupon = null;

		if (requestDto.couponId() != null) {
			coupon = couponService.getCoupon(requestDto.couponId());
		}

		Integer remainingPoint = 0;

		if (requestDto.point() != null) {
			remainingPoint = pointHistoryService.getRemainingPoint(userId);
			if (remainingPoint == null) {
				throw new RuntimeException("포인트가 없습니다.");
			}

			if (requestDto.point() > remainingPoint) {
				throw new RuntimeException("포인트가 부족합니다.");
			}
		}

		List<CartResponseDto> cartResponseDtoList = cartService.findCarts(userId);

		int totalPrice = 0;

		for (CartResponseDto cartResponseDto : cartResponseDtoList) {
			totalPrice +=
				cartResponseDto.qunatity() * (cartResponseDto.menuPrice() + cartResponseDto.menuOptionPrice());
		}

		totalPrice = applyCoupon(totalPrice, coupon);

		if (totalPrice < remainingPoint) {
			throw new RuntimeException("사용하려는 포인트가 금액보다 더 높습니다.");
		}

		totalPrice -= remainingPoint;

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
		Store store = storeRepository.findById(cartResponseDtoList.get(0).storeId())
			.orElseThrow(() -> new RuntimeException("존재하지 않는 매장입니다."));

		Order order = new Order(totalPrice, user, store);

		Order savedOrder = orderRepository.save(order);

		List<Long> orderItemIds = orderItemService.addMenu(savedOrder.getId(), cartResponseDtoList);

		pointHistoryService.usePoint(userId, requestDto.point());

		return new OrderResponseDto(
			savedOrder.getId(),
			userId,
			cartResponseDtoList.get(0).getStoreId(),
			orderItemIds,
			totalPrice,
			savedOrder.getStatus()
		);
	}

	private int applyCoupon(int totalPrice, Coupon coupon) {
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
				throw new RuntimeException("쿠폰이 금액보다 큽니다.");
			} else {
				totalPrice -= coupon.getDeductedPrice();
			}
		}

		return totalPrice;
	}
}
