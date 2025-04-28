package com.example.dishpatch.domain.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dishpatch.api.cart.request.CartCreateRequest;
import com.example.dishpatch.api.cart.request.CartUpdateRequest;
import com.example.dishpatch.api.cart.response.CartCreateResponse;
import com.example.dishpatch.api.cart.response.CartResponseDto;
import com.example.dishpatch.domain.cart.exception.CartErrorCode;
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
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	private final MenuOptionRepository menuOptionRepository;

	public CartCreateResponse createCart(CartCreateRequest request, UserAuth userAuth) {
		User user = userRepository.findById(userAuth.getId())
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		Store store = storeRepository.findById(request.storeId())
			.orElseThrow(() -> new BizException(StoreErrorCode.STORE_NOT_FOUND));

		Menu menu = menuRepository.findById(request.menuId())
			.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));

		MenuOption menuOption = menuOptionRepository.findById(request.menuOptionId())
			.orElseThrow(() -> new BizException(MenuOptionErrorCode.MENU_OPTION_NOT_FOUND));

		//기존 장바구니 조회
		List<Cart> existingCarts = cartRepository.findByUserId(userAuth.getId());

		//기존 장바구니가 다른 가게였다면 초기화
		if (!existingCarts.isEmpty() &&
			!existingCarts.get(0).getStore().getId().equals(store.getId())) {
			cartRepository.deleteAll(existingCarts);
		}

		//수량 예외처리
		if (request.quantity() <= 0) {
			throw new BizException(CartErrorCode.INVALID_QUANTITY);
		}

		Cart cart = new Cart(user, store, menu, menuOption, request.quantity());
		Cart saved = cartRepository.save(cart);

		return CartCreateResponse.from(saved);
	}

	public CartResponseDto findCarts(UserAuth userAuth) {
		User user = userRepository.findById(userAuth.getId())
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		List<Cart> cartList = cartRepository.findByUserId(user.getId());

		return CartResponseDto.from(cartList);
	}

	public CartResponseDto updateCart(Long cartId, CartUpdateRequest request, UserAuth userAuth) {
		Menu menu = menuRepository.findById(request.menuId())
			.orElseThrow(() -> new BizException(MenuErrorCode.MENU_NOT_FOUND));

		MenuOption menuOption = menuOptionRepository.findById(request.menuOptionId())
			.orElseThrow(() -> new BizException(MenuOptionErrorCode.MENU_OPTION_NOT_FOUND));

		Cart cart = cartRepository.findById(cartId)
			.orElseThrow(() -> new BizException(CartErrorCode.CART_NOT_FOUND));

		cart.updateCart(menu, menuOption, request.quantity());

		cartRepository.save(cart);

		List<Cart> cartList = cartRepository.findByUserId(userAuth.getId());

		return CartResponseDto.from(cartList);
	}

	public void deleteCart(Long cartId, UserAuth userAuth) {
		User user = userRepository.findById(userAuth.getId())
			.orElseThrow(() -> new BizException(UserErrorCode.INVALID_ID));

		Cart cart = cartRepository.findById(cartId)
			.orElseThrow(() -> new BizException(CartErrorCode.CART_NOT_FOUND));

		if (!user.getId().equals(cart.getUser().getId())) {
			throw new BizException(CartErrorCode.CART_AUTHOR_MISMATCH);
		}

		cartRepository.deleteById(cart.getId());
	}

}
