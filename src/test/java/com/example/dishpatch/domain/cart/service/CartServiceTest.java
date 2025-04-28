package com.example.dishpatch.domain.cart.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.dishpatch.api.cart.request.CartCreateRequest;
import com.example.dishpatch.api.cart.request.CartUpdateRequest;
import com.example.dishpatch.api.cart.response.CartCreateResponse;
import com.example.dishpatch.api.cart.response.CartResponseDto;
import com.example.dishpatch.domain.menu.exception.MenuErrorCode;
import com.example.dishpatch.domain.menu.exception.MenuOptionErrorCode;
import com.example.dishpatch.domain.store.exception.StoreErrorCode;
import com.example.dishpatch.domain.user.exception.UserErrorCode;
import com.example.dishpatch.global.exception.BizException;
import com.example.dishpatch.global.security.UserAuth;
import com.example.dishpatch.infra.db.cart.entity.Cart;
import com.example.dishpatch.infra.db.cart.repository.CartRepository;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

@Nested
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@Mock
	private CartRepository cartRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuOptionRepository menuOptionRepository;
	@InjectMocks
	private CartService cartService;

	private UserAuth userAuth;
	private User user;
	private User storeUser;
	private Store store;
	private Menu menu;
	private MenuOption menuOption;
	private Category category;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		UserAuth userAuth = new UserAuth(1L, UserRole.USER);

		User user = new User("test@test.com", "passWord123!", "01012345678", "name",
			UserProvider.LOCAL, UserGrade.A, UserRole.USER, "address");
		ReflectionTestUtils.setField(user, "id", 1L);

		User storeUser = new User("test1@test.com", "passWord123!!", "01011112222", "ceoName",
			UserProvider.LOCAL, UserGrade.A, UserRole.CEO, "address123");
		ReflectionTestUtils.setField(storeUser, "id", 2L);

		Category category = new Category();
		ReflectionTestUtils.setField(category, "id", 1L);
		ReflectionTestUtils.setField(category, "name", "한식");

		Store store = new Store("storeName", "storeAddress", "imageAddress", "023334444", 3000,
			"introduction", 1500, true, LocalTime.NOON, LocalTime.MIDNIGHT, category, storeUser);
		ReflectionTestUtils.setField(store, "id", 1L);

		Menu menu = Menu.builder()
			.name("menuName")
			.price(10000)
			.imageUrl("menuImage")
			.soldOut(false)
			.store(store)
			.options(List.of())
			.build();
		ReflectionTestUtils.setField(menu, "id", 1L);

		MenuOption menuOption = MenuOption.builder()
			.name("optionName")
			.price(2000)
			.soldOut(false)
			.menu(menu)
			.build();
		ReflectionTestUtils.setField(menuOption, "id", 1L);
	}

	@Test
	@DisplayName("작성자가 존재 하지 않는다.")
	void user_not_found() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 3);

		given(userRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class, () -> cartService.createCart(request, userAuth));

		assertEquals(UserErrorCode.INVALID_ID, exception.getErrorCode());
	}

	@Test
	@DisplayName("가게가 존재 하지 않는다.")
	void store_not_found() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 3);

		given(storeRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class, () -> cartService.createCart(request, userAuth));

		assertEquals(StoreErrorCode.STORE_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("메뉴가 존재 하지 않는다.")
	void menu_not_found() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 3);

		given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class, () -> cartService.createCart(request, userAuth));

		assertEquals(MenuErrorCode.MENU_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("메뉴 옵션이 존재 하지 않는다.")
	void menu_option_not_found() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 3);

		given(menuOptionRepository.findById(anyLong())).willReturn(Optional.empty());

		// when & then
		BizException exception = assertThrows(BizException.class, () -> cartService.createCart(request, userAuth));

		assertEquals(MenuOptionErrorCode.MENU_OPTION_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("기존 장바구니가 다른 가게였다면 초기화")
	void create_cart_different_store_deletes_existing() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 0);

		Store oldStore = new Store("oldStore", "oldAddress", "oldImage", "0212345678", 2000,
			"oldIntroduction", 1000, true, LocalTime.NOON, LocalTime.MIDNIGHT, category, storeUser);
		ReflectionTestUtils.setField(oldStore, "id", 99L);

		Cart existingCarts = new Cart(user, oldStore, menu, menuOption, 1);
		ReflectionTestUtils.setField(existingCarts, "id", 10L);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
		given(menuOptionRepository.findById(anyLong())).willReturn(Optional.of(menuOption));
		given(cartRepository.findByUserId(anyLong())).willReturn(List.of(existingCarts));
		given(cartRepository.findByUserId(anyLong())).willReturn(List.of());
		given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

		// when
		CartCreateResponse response = cartService.createCart(request, userAuth);

		// then
		assertThat(response).isNotNull();
		then(cartRepository).should(times(1)).deleteAll(anyList());
	}

	@Test
	@DisplayName("수량이 0 이하이면 예외 발생")
	void create_cart_invalid_quantity() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 0);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
		given(menuOptionRepository.findById(anyLong())).willReturn(Optional.of(menuOption));
		given(cartRepository.findByUserId(anyLong())).willReturn(List.of());

		// when & then
		assertThrows(BizException.class, () -> cartService.createCart(request, userAuth));
	}

	@Test
	@DisplayName("장바구니 생성에 성공한다")
	void create_cart_success() {
		// given
		CartCreateRequest request = new CartCreateRequest(1L, 1L, 1L, 3);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
		given(menuOptionRepository.findById(anyLong())).willReturn(Optional.of(menuOption));
		given(cartRepository.findByUserId(anyLong())).willReturn(List.of());
		given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));

		// when
		CartCreateResponse response = cartService.createCart(request, userAuth);

		// then
		assertThat(response).isNotNull();
		assertThat(response.menuId()).isEqualTo(menu.getId());
		assertThat(response.menuOptionId()).isEqualTo(menuOption.getId());
		assertThat(response.storeId()).isEqualTo(store.getId());
	}

	@Test
	@DisplayName("성공적으로 장바구니 조회")
	void find_carts_success() {
		// given
		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(cartRepository.findByUserId(anyLong())).willReturn(List.of());

		// when
		CartResponseDto response = cartService.findCarts(userAuth);

		// then
		assertThat(response).isNotNull();
	}

	@Test
	@DisplayName("성공적으로 장바구니 수정")
	void update_cart_success() {
		// given
		CartUpdateRequest request = new CartUpdateRequest(1L, 1L, 2);

		Cart cart = new Cart(user, store, menu, menuOption, 3);
		ReflectionTestUtils.setField(cart, "id", 1L);

		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
		given(menuOptionRepository.findById(anyLong())).willReturn(Optional.of(menuOption));
		given(cartRepository.findById(anyLong())).willReturn(Optional.of(cart));
		given(cartRepository.findByUserId(anyLong())).willReturn(List.of(cart));

		// when
		CartResponseDto response = cartService.updateCart(1L, request, userAuth);

		// then
		assertThat(response).isNotNull();
	}

	@Test
	@DisplayName("다른 사용자의 장바구니 삭제 시 예외 발생")
	void delete_cart_author_mismatch() {
		// given
		User otherUser = new User("other@test.com", "passWord456!", "01087654321", "otherName",
			UserProvider.LOCAL, UserGrade.B, UserRole.USER, "otherAddress");
		ReflectionTestUtils.setField(otherUser, "id", 999L);

		Cart cart = new Cart(otherUser, store, menu, menuOption, 2);
		ReflectionTestUtils.setField(cart, "id", 1L);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(cartRepository.findById(anyLong())).willReturn(Optional.of(cart));

		// when & then
		assertThrows(BizException.class, () -> cartService.deleteCart(1L, userAuth));
	}

	@Test
	@DisplayName("성공적으로 장바구니 삭제")
	void delete_cart_success() {
		// given
		Cart cart = new Cart(user, store, menu, menuOption, 3);
		ReflectionTestUtils.setField(cart, "id", 1L);

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(cartRepository.findById(anyLong())).willReturn(Optional.of(cart));

		// when
		cartService.deleteCart(1L, userAuth);

		// then
		then(cartRepository).should(times(1)).deleteAllById(anyLong());
	}
}