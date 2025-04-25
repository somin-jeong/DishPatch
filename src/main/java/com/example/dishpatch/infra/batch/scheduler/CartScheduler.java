package com.example.dishpatch.infra.batch.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.cart.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartScheduler {

	private final CartRepository cartRepository;

	@Scheduled(cron = "0 0 0 * * ?")
	public void clearExpiredCarts() {
		LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);
		cartRepository.deleteAllExpired(expiredTime);
		System.out.println("하루 지난 장바구니 삭제 완료");
	}
}
