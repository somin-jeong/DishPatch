package com.example.dishpatch.domain.user.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import com.example.dishpatch.infra.db.coupon.entity.CouponType;
import com.example.dishpatch.infra.db.coupon.entity.CouponUsed;
import com.example.dishpatch.infra.db.coupon.repository.CouponRepository;
import com.example.dishpatch.infra.db.order.repository.OrderRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserGradeScheduler {

	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final CouponRepository couponRepository;

	@Transactional
	@Scheduled(cron = "0 0 0 1 * *")
	public void updateUserGrades(){

		List<User> users = userRepository.findAll();
		// User user = new User();

		for(User user : users){
			Long total = orderRepository.findTotalPriceByUserId(user.getId());

			if(total == null){
				total = 0L;
			}

			UserGrade userGrade = calculateGrade(total);

			if(user.getGrade()!=userGrade){
				user.updateGrade(userGrade);
			}

			provideCouponForGrade(user,userGrade);
		}
	}

	private UserGrade calculateGrade(Long total){
		if(total > 1000000){
			return UserGrade.A;
		} else if(total > 500000){
			return UserGrade.B;
		} else if(total > 200000){
			return UserGrade.C;
		} else {
			return UserGrade.D;
		}
	}

	private void provideCouponForGrade(User user, UserGrade grade){
		Coupon coupon = Coupon.builder()
			.name(grade.name() + "등급 쿠폰")
			.coupontype(CouponType.B)
			.deductedPrice(getDeductedPriceByGrade(grade))
			.maxDiscount(null)
			.status(CouponUsed.B)
			.user(user)
			.build();

		couponRepository.save(coupon);
	}

	private int getDeductedPriceByGrade(UserGrade grade){
		if (grade == UserGrade.A) {
			return 10000;
		} else if (grade == UserGrade.B) {
			return 5000;
		} else if (grade == UserGrade.C) {
			return 3000;
		} else {
			return 1000;
		}
	}
}
