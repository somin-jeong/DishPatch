package com.example.dishpatch.domain.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.domain.cart.service.CartService;
import com.example.dishpatch.domain.coupon.service.CouponService;
import com.example.dishpatch.domain.pointHistory.service.PointHistoryService;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.infra.db.order.entity.Order;
import com.example.dishpatch.infra.db.order.entity.OrderStatus;
import com.example.dishpatch.infra.db.order.repository.OrderItemRepository;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private CartService cartService;

	@Mock
	private CouponService couponService;

	@Mock
	private PointHistoryService pointHistoryService;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OrderItemService orderItemService;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Test
	@DisplayName("refuseOrder 성공 테스트")
	void refuseOrder_shouldSucceed() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		// User 객체 생성
		User user = new User(
			"test@example.com",
			"password",
			"010-1234-5678",
			"홍길동",
			UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO,
			"서울시 어딘가"
		);
		ReflectionTestUtils.setField(user, "id", userId);

		Category category = Mockito.mock(Category.class);
		Store store = Store.builder()
			.name("맛집")
			.address("서울시 맛동네")
			.image(null)
			.phone("02-123-4567")
			.deliveryFee(3000)
			.introduction("최고의 맛")
			.minDeliveryPrice(10000)
			.isAdvertised(false)
			.openTime(LocalTime.of(9, 0))
			.closeTime(LocalTime.of(22, 0))
			.category(category)
			.user(user)
			.build();
		ReflectionTestUtils.setField(store, "id", 1L);

		Order order = new Order(13000, user, store);
		ReflectionTestUtils.setField(order, "id", orderId);

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

		// when
		orderService.refuseOrder(userId, orderId);

		// then
		assertEquals(OrderStatus.REFUSED, order.getStatus());
	}

	@Test
	void refuseOrder_whenOrderStatusInvalid_shouldThrowException() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		// User 객체 생성
		User user = new User(
			"test@example.com",
			"password",
			"010-1234-5678",
			"홍길동",
			UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO,
			"서울시 어딘가"
		);
		ReflectionTestUtils.setField(user, "id", userId);

		Category category = Mockito.mock(Category.class);
		Store store = Store.builder()
			.name("맛집")
			.address("서울시 맛동네")
			.image(null)
			.phone("02-123-4567")
			.deliveryFee(3000)
			.introduction("최고의 맛")
			.minDeliveryPrice(10000)
			.isAdvertised(false)
			.openTime(LocalTime.of(9, 0))
			.closeTime(LocalTime.of(22, 0))
			.category(category)
			.user(user)
			.build();
		ReflectionTestUtils.setField(store, "id", 1L);

		Order order = new Order(13000, user, store);
		ReflectionTestUtils.setField(order, "id", orderId);
		ReflectionTestUtils.setField(order, "status", OrderStatus.REFUSED);

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

		// when and then
		assertThrows(BizException.class, () -> orderService.refuseOrder(userId, orderId),
			"거절할 수 없는 상태입니다.");
	}

	@Test
	void refuseOrder_whenOrderStatusFinished_shouldThrowException() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		// User 객체 생성
		User user = new User(
			"test@example.com",
			"password",
			"010-1234-5678",
			"홍길동",
			UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO,
			"서울시 어딘가"
		);
		ReflectionTestUtils.setField(user, "id", userId);

		Category category = Mockito.mock(Category.class);
		Store store = Store.builder()
			.name("맛집")
			.address("서울시 맛동네")
			.image(null)
			.phone("02-123-4567")
			.deliveryFee(3000)
			.introduction("최고의 맛")
			.minDeliveryPrice(10000)
			.isAdvertised(false)
			.openTime(LocalTime.of(9, 0))
			.closeTime(LocalTime.of(22, 0))
			.category(category)
			.user(user)
			.build();
		ReflectionTestUtils.setField(store, "id", 1L);

		Order order = new Order(13000, user, store);
		ReflectionTestUtils.setField(order, "id", orderId);
		ReflectionTestUtils.setField(order, "status", OrderStatus.FINISHED);

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

		// when and then
		assertThrows(BizException.class, () -> orderService.refuseOrder(userId, orderId),
			"이미 완료된 주문입니다.");
	}

	@Test
	void refuseOrder_whenOrderStoreMismatch_shouldThrowException() {
		// given
		Long userId = 1L;
		Long orderId = 1L;

		// User 객체 생성
		User user = new User(
			"test@example.com",
			"password",
			"010-1234-5678",
			"홍길동",
			UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO,
			"서울시 어딘가"
		);
		ReflectionTestUtils.setField(user, "id", userId);

		User user2 = new User(
			"test2@example.com",
			"password",
			"010-1234-5678",
			"전우치",
			UserProvider.LOCAL,
			UserGrade.A,
			UserRole.CEO,
			"서울시 어딘가"
		);
		ReflectionTestUtils.setField(user2, "id", 2L);

		Category category = Mockito.mock(Category.class);
		Store correctStore = Store.builder()
			.name("맛집")
			.address("서울시 맛동네")
			.image(null)
			.phone("02-123-4567")
			.deliveryFee(3000)
			.introduction("최고의 맛")
			.minDeliveryPrice(10000)
			.isAdvertised(false)
			.openTime(LocalTime.of(9, 0))
			.closeTime(LocalTime.of(22, 0))
			.category(category)
			.user(user2)
			.build();
		ReflectionTestUtils.setField(correctStore, "id", 1L);

		Order order = new Order(13000, user, correctStore);
		ReflectionTestUtils.setField(order, "id", orderId);
		ReflectionTestUtils.setField(order, "status", OrderStatus.CHECKING);

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

		// when and then
		assertThrows(BizException.class, () -> orderService.refuseOrder(userId, orderId),
			"가게에 대한 권한이 없습니다.");
	}
}
