package com.example.dishpatch.infra.db.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatDaily;
import com.example.dishpatch.infra.db.admin.entity.AdminStoreOrderStatMonthly;
import com.example.dishpatch.infra.db.admin.repository.AdminStoreOrderStatDailyRepository;
import com.example.dishpatch.infra.db.admin.repository.AdminStoreOrderStatMonthlyRepository;
import com.example.dishpatch.infra.db.coupon.entity.Coupon;
import com.example.dishpatch.infra.db.coupon.entity.CouponType;
import com.example.dishpatch.infra.db.coupon.entity.CouponUsed;
import com.example.dishpatch.infra.db.coupon.repository.CouponRepository;
import com.example.dishpatch.infra.db.menu.entity.Menu;
import com.example.dishpatch.infra.db.menu.entity.MenuOption;
import com.example.dishpatch.infra.db.menu.repository.MenuOptionRepository;
import com.example.dishpatch.infra.db.menu.repository.MenuRepository;
import com.example.dishpatch.infra.db.pointHistory.entity.PointHistory;
import com.example.dishpatch.infra.db.pointHistory.repository.PointHistoryRepository;
import com.example.dishpatch.infra.db.review.entity.CeoReview;
import com.example.dishpatch.infra.db.review.entity.Review;
import com.example.dishpatch.infra.db.review.entity.ReviewStatus;
import com.example.dishpatch.infra.db.review.repository.CeoReviewRepository;
import com.example.dishpatch.infra.db.review.repository.ReviewRepository;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatDaily;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatId;
import com.example.dishpatch.infra.db.statistics.entity.StoreOrderStatMonthly;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatDailyRepository;
import com.example.dishpatch.infra.db.statistics.repository.StoreOrderStatMonthlyRepository;
import com.example.dishpatch.infra.db.store.entity.Category;
import com.example.dishpatch.infra.db.store.entity.Store;
import com.example.dishpatch.infra.db.store.repository.CategoryRepository;
import com.example.dishpatch.infra.db.store.repository.StoreRepository;
import com.example.dishpatch.infra.db.user.entity.User;
import com.example.dishpatch.infra.db.user.entity.UserGrade;
import com.example.dishpatch.infra.db.user.entity.UserProvider;
import com.example.dishpatch.infra.db.user.entity.UserRole;
import com.example.dishpatch.infra.db.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MenuOptionRepository menuOptionRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CeoReviewRepository ceoReviewRepository;

	@Autowired
	private PointHistoryRepository pointHistoryRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private StoreOrderStatDailyRepository storeOrderStatDailyRepository;

	@Autowired
	private StoreOrderStatMonthlyRepository storeOrderStatMonthlyRepository;

	@Autowired
	private AdminStoreOrderStatDailyRepository adminStoreOrderStatDailyRepository;

	@Autowired
	private AdminStoreOrderStatMonthlyRepository adminStoreOrderStatMonthlyRepository;

	@Override
	public void run(String... args) throws Exception {

		// Sample User
		String encodedPassword = passwordEncoder.encode("!Aa123456");
		User user1 = new User("user1@example.com", encodedPassword, "010-1234-5678", "user1", UserProvider.LOCAL,
			UserGrade.D, UserRole.USER, "Seoul");
		User user2 = new User("user2@example.com", encodedPassword, "010-1234-5678", "user1", UserProvider.LOCAL,
			UserGrade.D, UserRole.USER, "Seoul");
		User ceo1 = new User("ceo1@example.com", encodedPassword, "010-3456-7890", "ceo1", UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO, "Seoul");
		User ceo2 = new User("ceo2@example.com", encodedPassword, "010-4567-8901", "ceo2", UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO, "Seoul");
		User ceo3 = new User("ceo3@example.com", encodedPassword, "010-5678-9012", "ceo3", UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO, "Seoul");
		User ceo4 = new User("ceo4@example.com", encodedPassword, "010-6789-0123", "ceo4", UserProvider.LOCAL,
			UserGrade.D,
			UserRole.CEO, "Seoul");
		User admin1 = new User("admin1@example.com", encodedPassword, "010-7890-1234", "admin1", UserProvider.LOCAL,
			UserGrade.D, UserRole.ADMIN, "Seoul");
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(ceo1);
		userRepository.save(ceo2);
		userRepository.save(ceo3);
		userRepository.save(ceo4);
		userRepository.save(admin1);

		// Sample Category
		Category category1 = new Category("한식");
		Category category2 = new Category("분식");
		Category category3 = new Category("일식");
		Category category4 = new Category("양식");
		Category category5 = new Category("중식");
		categoryRepository.save(category1);
		categoryRepository.save(category2);
		categoryRepository.save(category3);
		categoryRepository.save(category4);
		categoryRepository.save(category5);

		Store store1 = Store.builder()
			.name("Store 1")
			.address("123 Main St")
			.phone("010-1234-5678")
			.deliveryFee(3000)
			.minDeliveryPrice(15000)
			.isAdvertised(true)
			.openTime(LocalTime.of(9, 0))
			.closeTime(LocalTime.of(22, 0))
			.category(category1)
			.user(ceo1)
			.build();

		Store store2 = Store.builder()
			.name("Store 2")
			.address("456 Oak St")
			.phone("010-2345-6789")
			.deliveryFee(2500)
			.minDeliveryPrice(10000)
			.isAdvertised(false)
			.openTime(LocalTime.of(10, 0))
			.closeTime(LocalTime.of(20, 0))
			.category(category2)
			.user(ceo1)
			.build();

		Store store3 = Store.builder()
			.name("Store 3")
			.address("789 Pine St")
			.phone("010-3456-7890")
			.deliveryFee(3500)
			.minDeliveryPrice(20000)
			.isAdvertised(true)
			.openTime(LocalTime.of(8, 30))
			.closeTime(LocalTime.of(23, 0))
			.category(category3)
			.user(ceo1)
			.build();

		Store store4 = Store.builder()
			.name("Store 4")
			.address("101 Maple St")
			.phone("010-4567-8901")
			.deliveryFee(2000)
			.minDeliveryPrice(12000)
			.isAdvertised(false)
			.openTime(LocalTime.of(11, 0))
			.closeTime(LocalTime.of(19, 0))
			.category(category4)
			.user(ceo2)
			.build();

		Store store5 = Store.builder()
			.name("Store 5")
			.address("202 Birch St")
			.phone("010-5678-9012")
			.deliveryFee(4000)
			.minDeliveryPrice(25000)
			.isAdvertised(true)
			.openTime(LocalTime.of(9, 30))
			.closeTime(LocalTime.of(21, 0))
			.category(category5)
			.user(ceo2)
			.build();

		Store store6 = Store.builder()
			.name("Store 6")
			.address("303 Cedar St")
			.phone("010-6789-0123")
			.deliveryFee(3200)
			.minDeliveryPrice(18000)
			.isAdvertised(true)
			.openTime(LocalTime.of(9, 0))
			.closeTime(LocalTime.of(22, 30))
			.category(category1)
			.user(ceo2)
			.build();

		Store store7 = Store.builder()
			.name("Store 7")
			.address("404 Elm St")
			.phone("010-7890-1234")
			.deliveryFee(2700)
			.minDeliveryPrice(11000)
			.isAdvertised(false)
			.openTime(LocalTime.of(10, 30))
			.closeTime(LocalTime.of(21, 0))
			.category(category2)
			.user(ceo3)
			.build();

		Store store8 = Store.builder()
			.name("Store 8")
			.address("505 Walnut St")
			.phone("010-8901-2345")
			.deliveryFee(2900)
			.minDeliveryPrice(16000)
			.isAdvertised(true)
			.openTime(LocalTime.of(9, 15))
			.closeTime(LocalTime.of(22, 0))
			.category(category3)
			.user(ceo3)
			.build();

		Store store9 = Store.builder()
			.name("Store 9")
			.address("606 Pineapple St")
			.phone("010-9012-3456")
			.deliveryFee(3100)
			.minDeliveryPrice(13000)
			.isAdvertised(false)
			.openTime(LocalTime.of(9, 30))
			.closeTime(LocalTime.of(20, 30))
			.category(category4)
			.user(ceo3)
			.build();

		Store store10 = Store.builder()
			.name("Store 10")
			.address("707 Strawberry St")
			.phone("010-0123-4567")
			.deliveryFee(3300)
			.minDeliveryPrice(20000)
			.isAdvertised(true)
			.openTime(LocalTime.of(8, 45))
			.closeTime(LocalTime.of(23, 30))
			.category(category5)
			.user(ceo4)
			.build();

		storeRepository.saveAll(
			List.of(store1, store2, store3, store4, store5, store6, store7, store8, store9, store10));

		Menu menu1 = Menu.builder()
			.name("Menu 1")
			.price(10000)
			.soldOut(false)
			.store(store1)
			.build();

		Menu menu2 = Menu.builder()
			.name("Menu 2")
			.price(15000)
			.soldOut(false)
			.store(store1)
			.build();

		Menu menu3 = Menu.builder()
			.name("Menu 3")
			.price(12000)
			.soldOut(false)
			.store(store2)
			.build();

		Menu menu4 = Menu.builder()
			.name("Menu 4")
			.price(17000)
			.soldOut(false)
			.store(store2)
			.build();

		Menu menu5 = Menu.builder()
			.name("Menu 5")
			.price(13000)
			.soldOut(false)
			.store(store3)
			.build();

		Menu menu6 = Menu.builder()
			.name("Menu 6")
			.price(18000)
			.soldOut(false)
			.store(store3)
			.build();

		Menu menu7 = Menu.builder()
			.name("Menu 7")
			.price(11000)
			.soldOut(false)
			.store(store4)
			.build();

		Menu menu8 = Menu.builder()
			.name("Menu 8")
			.price(16000)
			.soldOut(false)
			.store(store4)
			.build();

		Menu menu9 = Menu.builder()
			.name("Menu 9")
			.price(14000)
			.soldOut(false)
			.store(store5)
			.build();

		Menu menu10 = Menu.builder()
			.name("Menu 10")
			.price(19000)
			.soldOut(false)
			.store(store5)
			.build();

		Menu menu11 = Menu.builder()
			.name("Menu 11")
			.price(12500)
			.soldOut(false)
			.store(store6)
			.build();

		Menu menu12 = Menu.builder()
			.name("Menu 12")
			.price(13500)
			.soldOut(false)
			.store(store6)
			.build();

		Menu menu13 = Menu.builder()
			.name("Menu 13")
			.price(14500)
			.soldOut(false)
			.store(store7)
			.build();

		Menu menu14 = Menu.builder()
			.name("Menu 14")
			.price(15500)
			.soldOut(false)
			.store(store7)
			.build();

		Menu menu15 = Menu.builder()
			.name("Menu 15")
			.price(16500)
			.soldOut(false)
			.store(store8)
			.build();

		Menu menu16 = Menu.builder()
			.name("Menu 16")
			.price(17500)
			.soldOut(false)
			.store(store8)
			.build();

		Menu menu17 = Menu.builder()
			.name("Menu 17")
			.price(18500)
			.soldOut(false)
			.store(store9)
			.build();

		Menu menu18 = Menu.builder()
			.name("Menu 18")
			.price(19500)
			.soldOut(false)
			.store(store9)
			.build();

		Menu menu19 = Menu.builder()
			.name("Menu 19")
			.price(20000)
			.soldOut(false)
			.store(store10)
			.build();

		Menu menu20 = Menu.builder()
			.name("Menu 20")
			.price(21000)
			.soldOut(false)
			.store(store10)
			.build();

		menuRepository.saveAll(List.of(menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8, menu9, menu10,
			menu11, menu12, menu13, menu14, menu15, menu16, menu17, menu18, menu19, menu20));

		MenuOption option1 = MenuOption.builder()
			.name("Option 1")
			.price(2000)
			.soldOut(false)
			.menu(menu1)
			.build();
		menuOptionRepository.save(option1);

		MenuOption option2 = MenuOption.builder()
			.name("Option 2")
			.price(2500)
			.soldOut(false)
			.menu(menu2)
			.build();
		menuOptionRepository.save(option2);

		MenuOption option3 = MenuOption.builder()
			.name("Option 3")
			.price(2200)
			.soldOut(false)
			.menu(menu3)
			.build();
		menuOptionRepository.save(option3);

		MenuOption option4 = MenuOption.builder()
			.name("Option 4")
			.price(2700)
			.soldOut(false)
			.menu(menu4)
			.build();
		menuOptionRepository.save(option4);

		MenuOption option5 = MenuOption.builder()
			.name("Option 5")
			.price(2300)
			.soldOut(false)
			.menu(menu5)
			.build();
		menuOptionRepository.save(option5);

		MenuOption option6 = MenuOption.builder()
			.name("Option 6")
			.price(2800)
			.soldOut(false)
			.menu(menu6)
			.build();
		menuOptionRepository.save(option6);

		MenuOption option7 = MenuOption.builder()
			.name("Option 7")
			.price(2400)
			.soldOut(false)
			.menu(menu7)
			.build();
		menuOptionRepository.save(option7);

		MenuOption option8 = MenuOption.builder()
			.name("Option 8")
			.price(2900)
			.soldOut(false)
			.menu(menu8)
			.build();
		menuOptionRepository.save(option8);

		MenuOption option9 = MenuOption.builder()
			.name("Option 9")
			.price(2500)
			.soldOut(false)
			.menu(menu9)
			.build();
		menuOptionRepository.save(option9);

		MenuOption option10 = MenuOption.builder()
			.name("Option 10")
			.price(3000)
			.soldOut(false)
			.menu(menu10)
			.build();
		menuOptionRepository.save(option10);

		MenuOption option11 = MenuOption.builder()
			.name("Option 11")
			.price(2700)
			.soldOut(false)
			.menu(menu11)
			.build();
		menuOptionRepository.save(option11);

		MenuOption option12 = MenuOption.builder()
			.name("Option 12")
			.price(3200)
			.soldOut(false)
			.menu(menu12)
			.build();
		menuOptionRepository.save(option12);

		MenuOption option13 = MenuOption.builder()
			.name("Option 13")
			.price(2800)
			.soldOut(false)
			.menu(menu13)
			.build();
		menuOptionRepository.save(option13);

		MenuOption option14 = MenuOption.builder()
			.name("Option 14")
			.price(3300)
			.soldOut(false)
			.menu(menu14)
			.build();
		menuOptionRepository.save(option14);

		MenuOption option15 = MenuOption.builder()
			.name("Option 15")
			.price(2900)
			.soldOut(false)
			.menu(menu15)
			.build();
		menuOptionRepository.save(option15);

		MenuOption option16 = MenuOption.builder()
			.name("Option 16")
			.price(3400)
			.soldOut(false)
			.menu(menu16)
			.build();
		menuOptionRepository.save(option16);

		MenuOption option17 = MenuOption.builder()
			.name("Option 17")
			.price(3000)
			.soldOut(false)
			.menu(menu17)
			.build();
		menuOptionRepository.save(option17);

		MenuOption option18 = MenuOption.builder()
			.name("Option 18")
			.price(3500)
			.soldOut(false)
			.menu(menu18)
			.build();
		menuOptionRepository.save(option18);

		MenuOption option19 = MenuOption.builder()
			.name("Option 19")
			.price(3100)
			.soldOut(false)
			.menu(menu19)
			.build();
		menuOptionRepository.save(option19);

		MenuOption option20 = MenuOption.builder()
			.name("Option 20")
			.price(3600)
			.soldOut(false)
			.menu(menu20)
			.build();
		menuOptionRepository.save(option20);

		Coupon coupon1 = Coupon.builder()
			.name("10% Off Coupon")
			.coupontype(CouponType.A)
			.deductedPrice(10)
			.maxDiscount(5000)
			.status(CouponUsed.B)
			.user(user1)
			.build();

		Coupon coupon2 = Coupon.builder()
			.name("1000 Sale Coupon")
			.coupontype(CouponType.B)
			.deductedPrice(1000)
			.maxDiscount(0)
			.status(CouponUsed.B)
			.user(user1)
			.build();
		couponRepository.save(coupon1);
		couponRepository.save(coupon2);

		Review review1 = new Review(user1, store1, menu1, null, 5, "Great food!", "https://example.com/image.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review1);

		Review review2 = new Review(user1, store1, menu1, null, 4, "Very good, but could be better.",
			"https://example.com/image2.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review2);

		Review review3 = new Review(user1, store1, menu1, null, 3, "It's okay, not bad.",
			"https://example.com/image3.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review3);

		Review review4 = new Review(user1, store1, menu1, null, 2, "Not great, didn't meet expectations.",
			"https://example.com/image4.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review4);

		Review review5 = new Review(user1, store1, menu1, null, 1, "Very disappointed with the food.",
			"https://example.com/image5.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review5);

		Review review6 = new Review(user1, store1, menu2, null, 5, "Delicious, highly recommend!",
			"https://example.com/image6.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review6);

		Review review7 = new Review(user1, store1, menu2, null, 4, "Good, but a bit too salty.",
			"https://example.com/image7.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review7);

		Review review8 = new Review(user1, store1, menu2, null, 3, "Average taste.", "https://example.com/image8.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review8);

		Review review9 = new Review(user1, store1, menu2, null, 2, "Not really my taste.",
			"https://example.com/image9.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review9);

		Review review10 = new Review(user1, store1, menu2, null, 1, "I couldn't finish it, not good.",
			"https://example.com/image10.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review10);

		Review review11 = new Review(user1, store2, menu3, null, 5, "Absolutely amazing!",
			"https://example.com/image11.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review11);

		Review review12 = new Review(user1, store2, menu3, null, 4, "Very tasty, would come back.",
			"https://example.com/image12.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review12);

		Review review13 = new Review(user1, store2, menu3, null, 3, "It was fine, but nothing special.",
			"https://example.com/image13.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review13);

		Review review14 = new Review(user1, store2, menu3, null, 2, "Did not enjoy it as much as expected.",
			"https://example.com/image14.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review14);

		Review review15 = new Review(user1, store2, menu3, null, 1, "Terrible, wouldn't recommend.",
			"https://example.com/image15.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review15);

		Review review16 = new Review(user1, store2, menu4, null, 5, "Perfect, loved every bite!",
			"https://example.com/image16.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review16);

		Review review17 = new Review(user1, store2, menu4, null, 4, "Good, but not my favorite.",
			"https://example.com/image17.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review17);

		Review review18 = new Review(user1, store2, menu4, null, 3, "Okay, could use a little improvement.",
			"https://example.com/image18.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review18);

		Review review19 = new Review(user1, store2, menu4, null, 2, "Not what I expected, needs work.",
			"https://example.com/image19.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review19);

		Review review20 = new Review(user1, store2, menu4, null, 1, "Worst meal ever, I won't return.",
			"https://example.com/image20.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review20);

		Review review21 = new Review(user1, store3, menu5, null, 5, "Excellent meal, worth every penny!",
			"https://example.com/image21.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review21);

		Review review22 = new Review(user1, store3, menu5, null, 4, "Very good, but could be a bit cheaper.",
			"https://example.com/image22.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review22);

		Review review23 = new Review(user1, store3, menu5, null, 3, "Average food, not bad.",
			"https://example.com/image23.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review23);

		Review review24 = new Review(user1, store3, menu5, null, 2, "Not great, would prefer other options.",
			"https://example.com/image24.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review24);

		Review review25 = new Review(user1, store3, menu5, null, 1, "Extremely disappointed.",
			"https://example.com/image25.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review25);

		Review review26 = new Review(user1, store3, menu6, null, 5, "Fantastic, I will definitely come again!",
			"https://example.com/image26.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review26);

		Review review27 = new Review(user1, store3, menu6, null, 4, "Really good, just a bit overpriced.",
			"https://example.com/image27.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review27);

		Review review28 = new Review(user1, store3, menu6, null, 3, "It was okay, nothing exceptional.",
			"https://example.com/image28.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review28);

		Review review29 = new Review(user1, store3, menu6, null, 2, "Not my taste, wouldn't order again.",
			"https://example.com/image29.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review29);

		Review review30 = new Review(user1, store3, menu6, null, 1, "Waste of money, wouldn't recommend.",
			"https://example.com/image30.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review30);

		Review review31 = new Review(user1, store4, menu7, null, 5, "Amazing taste, highly recommend!",
			"https://example.com/image31.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review31);

		Review review32 = new Review(user1, store4, menu7, null, 4, "Good, but a bit spicy for my liking.",
			"https://example.com/image32.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review32);

		Review review33 = new Review(user1, store4, menu7, null, 3, "It was okay, not amazing.",
			"https://example.com/image33.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review33);

		Review review34 = new Review(user1, store4, menu7, null, 2, "Not very flavorful.",
			"https://example.com/image34.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review34);

		Review review35 = new Review(user1, store4, menu7, null, 1, "I didn't enjoy it at all.",
			"https://example.com/image35.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review35);

		Review review36 = new Review(user1, store4, menu8, null, 5, "The best I've had in a long time!",
			"https://example.com/image36.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review36);

		Review review37 = new Review(user1, store4, menu8, null, 4, "Very good, I would eat this again.",
			"https://example.com/image37.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review37);

		Review review38 = new Review(user1, store4, menu8, null, 3, "It was fine, but I expected more.",
			"https://example.com/image38.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review38);

		Review review39 = new Review(user1, store4, menu8, null, 2, "Not impressed, could use more seasoning.",
			"https://example.com/image39.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review39);

		Review review40 = new Review(user1, store4, menu8, null, 1, "Terrible, I couldn't finish it.",
			"https://example.com/image40.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review40);

		Review review41 = new Review(user1, store5, menu9, null, 5, "Absolutely delicious, would order again!",
			"https://example.com/image41.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review41);

		Review review42 = new Review(user1, store5, menu9, null, 4, "Very tasty, but a bit too sweet.",
			"https://example.com/image42.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review42);

		Review review43 = new Review(user1, store5, menu9, null, 3, "Good, but I expected it to be better.",
			"https://example.com/image43.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review43);

		Review review44 = new Review(user1, store5, menu9, null, 2, "Disappointing, lacked flavor.",
			"https://example.com/image44.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review44);

		Review review45 = new Review(user1, store5, menu9, null, 1, "Worst meal, wouldn't recommend.",
			"https://example.com/image45.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review45);

		Review review46 = new Review(user1, store5, menu10, null, 5, "Perfect, would definitely come back!",
			"https://example.com/image46.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review46);

		Review review47 = new Review(user1, store5, menu10, null, 4, "Very good, but could be cheaper.",
			"https://example.com/image47.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review47);

		Review review48 = new Review(user1, store5, menu10, null, 3, "It was fine, not great.",
			"https://example.com/image48.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review48);

		Review review49 = new Review(user1, store5, menu10, null, 2, "Not great, I wouldn't order it again.",
			"https://example.com/image49.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review49);

		Review review50 = new Review(user1, store5, menu10, null, 1, "Extremely disappointing, waste of money.",
			"https://example.com/image50.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review50);

		Review review51 = new Review(user1, store6, menu11, null, 5, "Outstanding, loved every bite!",
			"https://example.com/image51.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review51);

		Review review52 = new Review(user1, store6, menu11, null, 4, "Very good, but a bit too greasy.",
			"https://example.com/image52.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review52);

		Review review53 = new Review(user1, store6, menu11, null, 3, "It was okay, but not memorable.",
			"https://example.com/image53.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review53);

		Review review54 = new Review(user1, store6, menu11, null, 2, "Not my favorite, would not recommend.",
			"https://example.com/image54.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review54);

		Review review55 = new Review(user1, store6, menu11, null, 1, "Didn't like it at all, very disappointed.",
			"https://example.com/image55.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review55);

		Review review56 = new Review(user1, store6, menu12, null, 5, "The best pizza ever!",
			"https://example.com/image56.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review56);

		Review review57 = new Review(user1, store6, menu12, null, 4, "Really good, but the crust could be better.",
			"https://example.com/image57.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review57);

		Review review58 = new Review(user1, store6, menu12, null, 3, "It was decent, but I've had better.",
			"https://example.com/image58.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review58);

		Review review59 = new Review(user1, store6, menu12, null, 2, "It was too oily for my taste.",
			"https://example.com/image59.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review59);

		Review review60 = new Review(user1, store6, menu12, null, 1, "Didn't enjoy it at all, very disappointing.",
			"https://example.com/image60.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review60);

		Review review61 = new Review(user1, store7, menu13, null, 5, "Best burger in town, so juicy!",
			"https://example.com/image61.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review61);

		Review review62 = new Review(user1, store7, menu13, null, 4, "Really good, but a little too salty.",
			"https://example.com/image62.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review62);

		Review review63 = new Review(user1, store7, menu13, null, 3, "It was alright, nothing special.",
			"https://example.com/image63.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review63);

		Review review64 = new Review(user1, store7, menu13, null, 2, "Not great, I wouldn't order it again.",
			"https://example.com/image64.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review64);

		Review review65 = new Review(user1, store7, menu13, null, 1, "Worst burger I've ever had.",
			"https://example.com/image65.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review65);

		Review review66 = new Review(user1, store7, menu14, null, 5, "This pasta is incredible, full of flavor!",
			"https://example.com/image66.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review66);

		Review review67 = new Review(user1, store7, menu14, null, 4,
			"Very good pasta, but it was a bit too spicy for me.", "https://example.com/image67.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review67);

		Review review68 = new Review(user1, store7, menu14, null, 3, "It was okay, but I prefer other types of pasta.",
			"https://example.com/image68.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review68);

		Review review69 = new Review(user1, store7, menu14, null, 2, "It lacked flavor, didn't enjoy it much.",
			"https://example.com/image69.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review69);

		Review review70 = new Review(user1, store7, menu14, null, 1, "Horrible pasta, couldn't even finish it.",
			"https://example.com/image70.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review70);

		Review review71 = new Review(user1, store8, menu15, null, 5, "Amazing sushi, tasted so fresh!",
			"https://example.com/image71.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review71);

		Review review72 = new Review(user1, store8, menu15, null, 4,
			"Really good, but I wish the portions were bigger.", "https://example.com/image72.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review72);

		Review review73 = new Review(user1, store8, menu15, null, 3, "It was okay, but not the best sushi I've had.",
			"https://example.com/image73.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review73);

		Review review74 = new Review(user1, store8, menu15, null, 2, "Not very good, felt dry.",
			"https://example.com/image74.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review74);

		Review review75 = new Review(user1, store8, menu15, null, 1, "Very disappointing sushi, wouldn't come back.",
			"https://example.com/image75.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review75);

		Review review76 = new Review(user1, store8, menu16, null, 5, "Delicious fried chicken, perfectly crispy!",
			"https://example.com/image76.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review76);

		Review review77 = new Review(user1, store8, menu16, null, 4,
			"Really good, though I wish there was more seasoning.", "https://example.com/image77.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review77);

		Review review78 = new Review(user1, store8, menu16, null, 3, "It was decent, but a bit too oily for my taste.",
			"https://example.com/image78.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review78);

		Review review79 = new Review(user1, store8, menu16, null, 2, "Not very tasty, the batter was too thick.",
			"https://example.com/image79.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review79);

		Review review80 = new Review(user1, store8, menu16, null, 1, "Disappointing, the chicken was soggy.",
			"https://example.com/image80.jpg", ReviewStatus.PRIVATE);
		reviewRepository.save(review80);

		Review review81 = new Review(user1, store9, menu17, null, 5, "The best sandwich I've ever had, full of flavor!",
			"https://example.com/image81.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review81);

		Review review82 = new Review(user1, store9, menu17, null, 4,
			"Really good sandwich, but a little too much mustard.", "https://example.com/image82.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review82);

		Review review83 = new Review(user1, store9, menu17, null, 3, "It was okay, but could have been more balanced.",
			"https://example.com/image83.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review83);

		Review review84 = new Review(user1, store9, menu17, null, 2, "The sandwich was a bit too dry for my liking.",
			"https://example.com/image84.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review84);

		Review review85 = new Review(user1, store9, menu17, null, 1, "Very bland, didn't enjoy it.",
			"https://example.com/image85.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review85);

		Review review86 = new Review(user1, store9, menu18, null, 5, "Fantastic ramen, the broth was perfect!",
			"https://example.com/image86.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review86);

		Review review87 = new Review(user1, store9, menu18, null, 4,
			"Good ramen, but the noodles could be a little firmer.", "https://example.com/image87.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review87);

		Review review88 = new Review(user1, store9, menu18, null, 3, "It was okay, but I've had better ramen.",
			"https://example.com/image88.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review88);

		Review review89 = new Review(user1, store9, menu18, null, 2, "The ramen was too salty for me.",
			"https://example.com/image89.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review89);

		Review review90 = new Review(user1, store9, menu18, null, 1,
			"Did not enjoy it at all, the broth was very bland.", "https://example.com/image90.jpg",
			ReviewStatus.PRIVATE);
		reviewRepository.save(review90);

		Review review91 = new Review(user1, store10, menu19, null, 5, "Incredible pizza, the crust was so crispy!",
			"https://example.com/image91.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review91);

		Review review92 = new Review(user1, store10, menu19, null, 4,
			"Great pizza, but I would have preferred more cheese.", "https://example.com/image92.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review92);

		Review review93 = new Review(user1, store10, menu19, null, 3, "It was okay, but could use a bit more sauce.",
			"https://example.com/image93.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review93);

		Review review94 = new Review(user1, store10, menu19, null, 2, "The pizza was too doughy for my taste.",
			"https://example.com/image94.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review94);

		Review review95 = new Review(user1, store10, menu19, null, 1, "Not a fan of the pizza, it was too dry.",
			"https://example.com/image95.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review95);

		Review review96 = new Review(user1, store10, menu20, null, 5, "Amazing sushi, so fresh and delicious!",
			"https://example.com/image96.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review96);

		Review review97 = new Review(user1, store10, menu20, null, 4,
			"The sushi was good, but the rice could be stickier.", "https://example.com/image97.jpg",
			ReviewStatus.PUBLIC);
		reviewRepository.save(review97);

		Review review98 = new Review(user1, store10, menu20, null, 3, "It was decent, but not the best sushi I've had.",
			"https://example.com/image98.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review98);

		Review review99 = new Review(user1, store10, menu20, null, 2, "The fish was a bit too chewy for my liking.",
			"https://example.com/image99.jpg", ReviewStatus.PUBLIC);
		reviewRepository.save(review99);

		Review review100 = new Review(user1, store10, menu20, null, 1,
			"I didn't enjoy the sushi, it was bland and soggy.", "https://example.com/image100.jpg",
			ReviewStatus.PRIVATE);
		reviewRepository.save(review100);

		CeoReview ceoReview1 = new CeoReview(ceo1,
			"This is a detailed CEO review about the product quality and customer service.", ReviewStatus.PUBLIC,
			review1);
		ceoReviewRepository.save(ceoReview1);

		CeoReview ceoReview2 = new CeoReview(ceo1,
			"Great feedback on the menu variety, but some items were out of stock.", ReviewStatus.PUBLIC, review2);
		ceoReviewRepository.save(ceoReview2);

		CeoReview ceoReview3 = new CeoReview(ceo1, "Customer was happy with the delivery time, but food was cold.",
			ReviewStatus.PUBLIC, review3);
		ceoReviewRepository.save(ceoReview3);

		CeoReview ceoReview4 = new CeoReview(ceo1,
			"Feedback indicates a need for improved packaging and presentation.", ReviewStatus.PUBLIC, review4);
		ceoReviewRepository.save(ceoReview4);

		CeoReview ceoReview5 = new CeoReview(ceo1,
			"Positive reviews overall, with minor complaints about portion size.", ReviewStatus.PUBLIC, review5);
		ceoReviewRepository.save(ceoReview5);

		CeoReview ceoReview6 = new CeoReview(ceo1,
			"Customer appreciated the quick resolution to their complaint. Great service!", ReviewStatus.PUBLIC,
			review6);
		ceoReviewRepository.save(ceoReview6);

		CeoReview ceoReview7 = new CeoReview(ceo1,
			"The food quality was excellent, but the delivery took longer than expected.", ReviewStatus.PUBLIC,
			review7);
		ceoReviewRepository.save(ceoReview7);

		CeoReview ceoReview8 = new CeoReview(ceo1,
			"Positive feedback on the taste, but more variety is needed in the menu.", ReviewStatus.PUBLIC, review8);
		ceoReviewRepository.save(ceoReview8);

		CeoReview ceoReview9 = new CeoReview(ceo1,
			"Customer noted that the online ordering process was smooth and user-friendly.", ReviewStatus.PUBLIC,
			review9);
		ceoReviewRepository.save(ceoReview9);

		CeoReview ceoReview10 = new CeoReview(ceo1,
			"Customer was satisfied overall, but would like to see more healthier options.", ReviewStatus.PUBLIC,
			review10);
		ceoReviewRepository.save(ceoReview10);

		// Sample PointHistory for User 1
		PointHistory pointHistory1 = new PointHistory(1000, 0, user1);
		pointHistoryRepository.save(pointHistory1);

		// Sample PointHistory for User 1
		PointHistory pointHistory2 = new PointHistory(500, 250, user1);
		pointHistoryRepository.save(pointHistory2);

		// Sample PointHistory for User 1
		PointHistory pointHistory3 = new PointHistory(1500, 1500, user1);
		pointHistoryRepository.save(pointHistory3);

		// Sample PointHistory for User 1
		PointHistory pointHistory4 = new PointHistory(2000, 2000, user1);
		pointHistoryRepository.save(pointHistory4);

		// Sample PointHistory for User 1
		PointHistory pointHistory5 = new PointHistory(3000, 3000, user1);
		pointHistoryRepository.save(pointHistory5);

		List<StoreOrderStatDaily> dailyStats = List.of(
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 1)), 100, 1000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 2)), 200, 2000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 3)), 300, 3000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 4)), 400, 4000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 5)), 500, 5000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 1)), 100, 1000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 2)), 200, 2000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 3)), 300, 3000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 4)), 400, 4000000L),
			StoreOrderStatDaily.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 5)), 500, 5000000L)
		);
		storeOrderStatDailyRepository.saveAll(dailyStats);

		List<StoreOrderStatMonthly> monthlyStats = List.of(
			StoreOrderStatMonthly.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 3, 1)), 200, 2000000L),
			StoreOrderStatMonthly.of(StoreOrderStatId.of(2L, LocalDate.of(2025, 3, 1)), 200, 2000000L),
			StoreOrderStatMonthly.of(StoreOrderStatId.of(1L, LocalDate.of(2025, 4, 1)), 500, 5000000L),
			StoreOrderStatMonthly.of(StoreOrderStatId.of(2L, LocalDate.of(2025, 4, 1)), 500, 5000000L)
		);
		storeOrderStatMonthlyRepository.saveAll(monthlyStats);

		List<AdminStoreOrderStatDaily> adminDailyStats = List.of(
			new AdminStoreOrderStatDaily(LocalDate.of(2025, 4, 1), 100, 1000000L),
			new AdminStoreOrderStatDaily(LocalDate.of(2025, 4, 2), 100, 1000000L),
			new AdminStoreOrderStatDaily(LocalDate.of(2025, 4, 3), 100, 1000000L),
			new AdminStoreOrderStatDaily(LocalDate.of(2025, 4, 4), 100, 1000000L),
			new AdminStoreOrderStatDaily(LocalDate.of(2025, 4, 5), 100, 1000000L)
		);
		adminStoreOrderStatDailyRepository.saveAll(adminDailyStats);

		List<AdminStoreOrderStatMonthly> adminMonthlyStats = List.of(
			new AdminStoreOrderStatMonthly(LocalDate.of(2025, 3, 1), 100, 1000000L),
			new AdminStoreOrderStatMonthly(LocalDate.of(2025, 4, 1), 100, 1000000L)
		);
		adminStoreOrderStatMonthlyRepository.saveAll(adminMonthlyStats);

	}
}