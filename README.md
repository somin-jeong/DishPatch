
![header](https://capsule-render.vercel.app/api?type=waving&&color=E680E6&height=300&section=header&text=DishPatch&fontSize=90&desc)


## 🤔 개요
- DishPatch는 고객의 주문활동과 사장님의 매장관리를 지원하는 통합 음식 배달 플랫폼입니다. 

## 🌳 개발환경 
언어 : ![Static Badge](https://img.shields.io/badge/Java-red?style=flat-square)

JDK : ![Static Badge](https://img.shields.io/badge/JDK-17-yellow?style=flat-square)

프레임워크 : ![Static Badge](https://img.shields.io/badge/SpringBoot-%23FFFF00?logo=springboot)

DB : ![Static Badge](https://img.shields.io/badge/MySql-%23FFFFFF?style=flat&logo=mysql) ![Static Badge](https://img.shields.io/badge/Redis-green?logo=redis) 

ORM : ![Static Badge](https://img.shields.io/badge/JPA-FFA500?style=flat) ![Static Badge](https://img.shields.io/badge/QueryDSL-ivory) 


그외 : ![Static Badge](https://img.shields.io/badge/WEB-green?label=Spring)
![Static Badge](https://img.shields.io/badge/Batch-purple?label=Spring)
![Static Badge](https://img.shields.io/badge/Security-blue?logo=springsecurity&label=Spring) ![Static Badge](https://img.shields.io/badge/JWT-chocolate?logo=jsonwebtokens) ![Static Badge](https://img.shields.io/badge/Oauth2-%2336454F?logo=auth0) ![Static Badge](https://img.shields.io/badge/Junit5-%23F08080?logo=junit5) 
![Static Badge](https://img.shields.io/badge/AWSS3-%23FFFFFF?logo=amazons3)
![Static Badge](https://img.shields.io/badge/Docker-%2387CEEB?logo=docker) ![Static Badge](https://img.shields.io/badge/Mockito-%23FFD700)


## 🛠 기능 엿보기   
1. [📅 ERD  ](#-ERD)
2. [📘 통합 API 명세서](#-통합-API-명세서)
3. [ 📋 기능 요약](#-기능-요약)
4. [ ✅ 디렉토리 구조](#-디렉토리-구조)

# 📅 ERD

![image](https://github.com/user-attachments/assets/cc40a7ed-2ed2-40a6-99f5-51c90153fd1c)


# 📘 통합 API 명세서

https://documenter.getpostman.com/view/13356404/2sB2j1jD2x

# 📋 기능 요약

| 구분 | 기능 | 설명 |
|:---|:---|:---|
| **회원** | 회원가입 | 이메일/비밀번호/권한 선택, 비밀번호는 Bcrypt로 인코딩 |
|  | 로그인 | 가입한 아이디(이메일)와 비밀번호로 로그인 |
|  | 소셜 로그인 | 네이버, 카카오 OAuth2 로그인 지원 |
|  | 회원탈퇴 | 비밀번호 확인 후 탈퇴, 탈퇴 아이디 재사용 불가 |
|  | 권한 부여 | USER(일반), OWNER(사장님) 선택 가입 |
|  | 고객 등급 관리 | 고마운분/귀한분/천생연분 등, 매월 1일 쿠폰 지급 |
| **가게** | 가게 생성/수정 | 사장님만 가능, 최대 3개 운영 가능, 오픈/마감시간 설정 |
|  | 가게 폐업 | 폐업 상태로 변경, 폐업 후 추가 등록 가능 |
|  | 가게 조회(다건) | 가게 목록 조회 (메뉴 제외) |
|  | 가게 조회(단건) | 가게 + 메뉴 상세 조회 가능 |
|  | 가게 카테고리 조회 | 카테고리별 가게 필터링 지원 |
| **메뉴** | 메뉴 생성/수정 | 사장님만 본인 가게에 메뉴 등록/수정 가능 |
|  | 메뉴 삭제 | 삭제 상태로 변경, 가게 조회 시 미표시, 주문내역 조회 시 표시 |
|  | 메뉴 옵션 CRUD | 메뉴에 옵션 추가/수정/삭제 가능 |
| **주문** | 주문 생성 | 고객이 메뉴 주문 가능 |
|  | 주문 상태 변경 | 사장님이 주문 수락/조리중/배달중/완료 상태 변경 |
|  | AOP 주문로그 기록 | 주문 요청/상태 변경 시 로그 (시각, 가게ID, 주문ID) 기록 |
| **포인트/쿠폰** | 포인트 적립 | 배달 완료 시 결제금액 3% 적립 |
|  | 포인트 사용 | 주문 시 포인트 사용 가능 |
|  | 쿠폰 사용 | 퍼센트 할인 또는 금액 할인, 만료일 관리 |
| **구독 서비스** | 배달팁 무료 구독 | 구독 시 배달팁 무료 제공 |
| **리뷰** | 리뷰 작성 | 배달 완료된 주문 건에 대해 별점+리뷰 작성 가능 |
|  | 사장님 대댓글 | 사장님만 리뷰에 대댓글 작성 가능 |
|  | 리뷰 조회 | 가게별 리뷰 다건 조회, 최신순/별점범위 필터링 가능 |
| **찜/즐겨찾기** | 가게 찜하기 | 고객이 가게를 찜 추가/해제 가능 |
| **통합검색** | 인기 검색어 조회 | 매시간 Top 10 인기 검색어 제공 |
|  | 가게 추천 | 찜 수 기준으로 가게 추천 노출 |
| **장바구니** | 장바구니 관리 | 한 가게의 메뉴만 담기 가능, 하루 유지, 가게 변경시 초기화 |
| **대시보드** | 가게 통계 조회 | 사장님용 일간/월간 주문 통계 제공 |
|  | 전체 통계 조회 | 관리자용 전체 주문 통계 제공 |
| **이미지 업로드** | 이미지 저장 | 가게/메뉴/프로필/리뷰용 이미지 업로드 (jpg, png, jpeg 지원) |

# ✅ 디렉토리 구조

```
C:.
│  .gitattributes
│  .gitignore
│  build.gradle
│  gradlew
│  gradlew.bat
│  settings.gradle
│
├─.github
│      pull_request_template.md
│
├─.gradle
│  │  file-system.probe
│  │
│  ├─8.13
│  │  │  gc.properties
│  │  │
│  │  ├─checksums
│  │  │      checksums.lock
│  │  │      md5-checksums.bin
│  │  │      sha1-checksums.bin
│  │  │
│  │  ├─executionHistory
│  │  │      executionHistory.bin
│  │  │      executionHistory.lock
│  │  │
│  │  ├─expanded
│  │  ├─fileChanges
│  │  │      last-build.bin
│  │  │
│  │  ├─fileHashes
│  │  │      fileHashes.bin
│  │  │      fileHashes.lock
│  │  │      resourceHashesCache.bin
│  │  │
│  │  └─vcsMetadata
│  ├─buildOutputCleanup
│  │      buildOutputCleanup.lock
│  │      cache.properties
│  │      outputFiles.bin
│  │
│  └─vcs-1
│          gc.properties
│
├─.idea
│  │  .gitignore
│  │  checkstyle-idea.xml
│  │  compiler.xml
│  │  dataSources.local.xml
│  │  dataSources.xml
│  │  gradle.xml
│  │  misc.xml
│  │  modules.xml
│  │  vcs.xml
│  │  workspace.xml
│  │
│  ├─dataSources
│  │  │  9de12494-27a8-4e2a-aa40-bd0c94283b79.xml
│  │  │  cdc74133-0041-4f88-8cd6-2652e38a41ce.xml
│  │  │
│  │  └─cdc74133-0041-4f88-8cd6-2652e38a41ce
│  │      └─storage_v2
│  │          └─_src_
│  │              └─schema
│  │                      dishpatch.7oJTAQ.meta
│  │                      information_schema.FNRwLQ.meta
│  │                      performance_schema.kIw0nw.meta
│  │
│  └─modules
│          DishPatch.main.iml
│
├─build
│  ├─classes
│  │  └─java
│  │      ├─main
│  │      │  └─com
│  │      │      └─example
│  │      │          └─dishpatch
│  │      │              │  DishPatchApplication.class
│  │      │              │
│  │      │              ├─api
│  │      │              │  ├─admin
│  │      │              │  │  ├─controller
│  │      │              │  │  │      AdminStoreStatController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      StoreOrderStatPeriodType.class
│  │      │              │  │  │      StoreOrderStatRequest.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          StoreOrderStatItem.class
│  │      │              │  │          StoreOrderStatResponse.class
│  │      │              │  │
│  │      │              │  ├─cart
│  │      │              │  │  ├─controller
│  │      │              │  │  │      CartController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      CartCreateRequest.class
│  │      │              │  │  │      CartUpdateRequest.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          CartCreateResponse.class
│  │      │              │  │          CartItemResponse.class
│  │      │              │  │          CartResponseDto.class
│  │      │              │  │
│  │      │              │  ├─menu
│  │      │              │  │  ├─controller
│  │      │              │  │  │      MenuController.class
│  │      │              │  │  │      MenuOptionController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      MenuCreateRequest.class
│  │      │              │  │  │      MenuOptionAddRequest.class
│  │      │              │  │  │      MenuOptionUpdateRequest.class
│  │      │              │  │  │      MenuUpdateRequest.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          MenuCreateResponse.class
│  │      │              │  │          MenuOptionAddResponse.class
│  │      │              │  │          MenuOptionResponse.class
│  │      │              │  │          MenuResponse.class
│  │      │              │  │          StoreMenuListResponse.class
│  │      │              │  │
│  │      │              │  ├─order
│  │      │              │  │  ├─controller
│  │      │              │  │  │      OrderController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      OrderRequestDto.class
│  │      │              │  │  │      OrderStatusRequestDto.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          MenuOptionDetailResponseDto.class
│  │      │              │  │          OrderDetailResponseDto.class
│  │      │              │  │          OrderItemDetailResponseDto.class
│  │      │              │  │          OrderResponseDto.class
│  │      │              │  │
│  │      │              │  ├─review
│  │      │              │  │  ├─controller
│  │      │              │  │  │      CeoReviewController.class
│  │      │              │  │  │      ReviewController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      CeoReviewCreateRequest.class
│  │      │              │  │  │      CeoReviewUpdateRequest.class
│  │      │              │  │  │      ReviewCreateRequest.class
│  │      │              │  │  │      ReviewUpdateRequest.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          CeoReviewResponse.class
│  │      │              │  │          ReviewPageResponse.class
│  │      │              │  │          ReviewResponse.class
│  │      │              │  │
│  │      │              │  ├─statistics
│  │      │              │  │  ├─controller
│  │      │              │  │  │      StoreStatController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      StoreOrderStatPeriodType.class
│  │      │              │  │  │      StoreOrderStatRequest.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          StoreOrderStatItem.class
│  │      │              │  │          StoreOrderStatResponse.class
│  │      │              │  │
│  │      │              │  ├─store
│  │      │              │  │  ├─controller
│  │      │              │  │  │      StoreController.class
│  │      │              │  │  │
│  │      │              │  │  ├─request
│  │      │              │  │  │      StoreCreateRequest.class
│  │      │              │  │  │      StoreUpdateRequest.class
│  │      │              │  │  │
│  │      │              │  │  └─response
│  │      │              │  │          KeywordRank.class
│  │      │              │  │          PopularKeywordsResponse.class
│  │      │              │  │          StoreCreateResponse.class
│  │      │              │  │          StoreInfoResponse.class
│  │      │              │  │          StoreResponse.class
│  │      │              │  │          StoreSearchResponse.class
│  │      │              │  │
│  │      │              │  └─user
│  │      │              │      ├─controller
│  │      │              │      │      UserController.class
│  │      │              │      │
│  │      │              │      ├─request
│  │      │              │      │      UserDeleteRequest.class
│  │      │              │      │      UserLoginRequest.class
│  │      │              │      │      UserSignupRequest.class
│  │      │              │      │      UserUpdateRequest.class
│  │      │              │      │
│  │      │              │      └─response
│  │      │              │              UserLoginResponse.class
│  │      │              │              UserProfileResponse.class
│  │      │              │              UserSignupResponse.class
│  │      │              │              UserUpdateResponse.class
│  │      │              │
│  │      │              ├─domain
│  │      │              │  ├─admin
│  │      │              │  │  ├─exception
│  │      │              │  │  │      StatErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │      │  AdminStoreStatService.class
│  │      │              │  │      │
│  │      │              │  │      └─strategy
│  │      │              │  │              AdminDailyOrderStatStrategy.class
│  │      │              │  │              AdminMonthlyOrderStatStrategy.class
│  │      │              │  │              AdminStoreOrderStatStrategy.class
│  │      │              │  │
│  │      │              │  ├─cart
│  │      │              │  │  ├─exception
│  │      │              │  │  │      CartErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          CartService.class
│  │      │              │  │
│  │      │              │  ├─coupon
│  │      │              │  │  ├─exception
│  │      │              │  │  │      CouponErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          CouponService.class
│  │      │              │  │
│  │      │              │  ├─menu
│  │      │              │  │  ├─exception
│  │      │              │  │  │      MenuErrorCode.class
│  │      │              │  │  │      MenuOptionErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          MenuOptionService.class
│  │      │              │  │          MenuService.class
│  │      │              │  │
│  │      │              │  ├─order
│  │      │              │  │  ├─exception
│  │      │              │  │  │      OrderErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          OrderItemService.class
│  │      │              │  │          OrderService.class
│  │      │              │  │
│  │      │              │  ├─pointHistory
│  │      │              │  │  ├─exception
│  │      │              │  │  │      PointErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          PointHistoryService.class
│  │      │              │  │          PointUseHistoryService.class
│  │      │              │  │
│  │      │              │  ├─review
│  │      │              │  │  ├─exception
│  │      │              │  │  │      CeoReviewErrorCode.class
│  │      │              │  │  │      ReviewErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          CeoReviewService.class
│  │      │              │  │          ReviewService.class
│  │      │              │  │
│  │      │              │  ├─statistics
│  │      │              │  │  ├─exception
│  │      │              │  │  │      StatErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │      │  StoreOrderStatService.class
│  │      │              │  │      │
│  │      │              │  │      └─strategy
│  │      │              │  │              DailyOrderStatStrategy.class
│  │      │              │  │              MonthlyOrderStatStrategy.class
│  │      │              │  │              StoreOrderStatStrategy.class
│  │      │              │  │
│  │      │              │  ├─store
│  │      │              │  │  ├─exception
│  │      │              │  │  │      StoreErrorCode.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          StoreService.class
│  │      │              │  │
│  │      │              │  └─user
│  │      │              │      ├─exception
│  │      │              │      │      UserErrorCode.class
│  │      │              │      │
│  │      │              │      └─service
│  │      │              │              UserGradeScheduler.class
│  │      │              │              UserService.class
│  │      │              │              UserServiceImpl.class
│  │      │              │
│  │      │              ├─global
│  │      │              │  ├─config
│  │      │              │  │      CacheConfig.class
│  │      │              │  │      S3Config.class
│  │      │              │  │      SecurityConfig.class
│  │      │              │  │
│  │      │              │  ├─exception
│  │      │              │  │      BizException.class
│  │      │              │  │      CommonErrorCode.class
│  │      │              │  │      ErrorCode.class
│  │      │              │  │      GlobalExceptionHandler.class
│  │      │              │  │
│  │      │              │  ├─oauth2
│  │      │              │  │  ├─handler
│  │      │              │  │  │      OAuth2SuccessHandler.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          CustomOAuth2UserService.class
│  │      │              │  │
│  │      │              │  ├─response
│  │      │              │  │  │  ErrorResponse$ErrorResponseBuilder.class
│  │      │              │  │  │  ErrorResponse$FieldError.class
│  │      │              │  │  │  ErrorResponse.class
│  │      │              │  │  │
│  │      │              │  │  └─pagination
│  │      │              │  │          CursorSupport.class
│  │      │              │  │          SliceResponse.class
│  │      │              │  │
│  │      │              │  ├─S3
│  │      │              │  │  ├─controller
│  │      │              │  │  │      S3ConrollerTemplate.class
│  │      │              │  │  │
│  │      │              │  │  └─service
│  │      │              │  │          S3Service.class
│  │      │              │  │
│  │      │              │  └─security
│  │      │              │          JwtUtil.class
│  │      │              │          SecurityFilter.class
│  │      │              │          UserAuth.class
│  │      │              │
│  │      │              └─infra
│  │      │                  ├─batch
│  │      │                  │  ├─config
│  │      │                  │  │      FirstDayOfMonthDecider.class
│  │      │                  │  │      StoreOrderStatBatchConfig.class
│  │      │                  │  │
│  │      │                  │  ├─processor
│  │      │                  │  │      AdminStoreOrderStatDailyProcessor.class
│  │      │                  │  │      AdminStoreOrderStatMonthlyProcessor.class
│  │      │                  │  │      StoreOrderStatDailyProcessor.class
│  │      │                  │  │      StoreOrderStatMonthlyProcessor.class
│  │      │                  │  │
│  │      │                  │  ├─reader
│  │      │                  │  │      AdminStoreOrderStatDailyReader.class
│  │      │                  │  │      AdminStoreOrderStatMonthlyReader.class
│  │      │                  │  │      StoreOrderStatDailyReader.class
│  │      │                  │  │      StoreOrderStatMonthlyReader.class
│  │      │                  │  │
│  │      │                  │  ├─scheduler
│  │      │                  │  │      CartScheduler.class
│  │      │                  │  │      StoreOrderStatJobScheduler.class
│  │      │                  │  │
│  │      │                  │  ├─service
│  │      │                  │  │      StoreOrderStatBatchService.class
│  │      │                  │  │
│  │      │                  │  └─writer
│  │      │                  │          AdminStoreOrderDailyWriter.class
│  │      │                  │          AdminStoreOrderMonthlyWriter.class
│  │      │                  │          StoreOrderDailyWriter.class
│  │      │                  │          StoreOrderMonthlyWriter.class
│  │      │                  │
│  │      │                  ├─config
│  │      │                  │      BatchConfig.class
│  │      │                  │      JpaConfig.class
│  │      │                  │      QueryDslConfig.class
│  │      │                  │      SchedulingConfig.class
│  │      │                  │
│  │      │                  ├─db
│  │      │                  │  ├─admin
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      AdminStoreOrderStatDaily.class
│  │      │                  │  │  │      AdminStoreOrderStatMonthly.class
│  │      │                  │  │  │      AdminStoreStat.class
│  │      │                  │  │  │      QAdminStoreOrderStatDaily.class
│  │      │                  │  │  │      QAdminStoreOrderStatMonthly.class
│  │      │                  │  │  │      QAdminStoreStat.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          AbstractAdminStoreOrderStatQueryRepository.class
│  │      │                  │  │          AdminStoreOrderStatDailyQueryRepository.class
│  │      │                  │  │          AdminStoreOrderStatDailyQueryRepositoryImpl.class
│  │      │                  │  │          AdminStoreOrderStatDailyRepository.class
│  │      │                  │  │          AdminStoreOrderStatMonthlyQueryRepository.class
│  │      │                  │  │          AdminStoreOrderStatMonthlyQueryRepositoryImpl.class
│  │      │                  │  │          AdminStoreOrderStatMonthlyRepository.class
│  │      │                  │  │
│  │      │                  │  ├─cart
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      Cart.class
│  │      │                  │  │  │      QCart.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          CartRepository.class
│  │      │                  │  │
│  │      │                  │  ├─common
│  │      │                  │  │      BaseEntity.class
│  │      │                  │  │      QBaseEntity.class
│  │      │                  │  │      QSoftDeletableEntity.class
│  │      │                  │  │      SoftDeletableEntity.class
│  │      │                  │  │      StatConvertible.class
│  │      │                  │  │
│  │      │                  │  ├─coupon
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      Coupon$CouponBuilder.class
│  │      │                  │  │  │      Coupon.class
│  │      │                  │  │  │      CouponType.class
│  │      │                  │  │  │      CouponUsed.class
│  │      │                  │  │  │      QCoupon.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          CouponRepository.class
│  │      │                  │  │
│  │      │                  │  ├─menu
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      Menu$MenuBuilder.class
│  │      │                  │  │  │      Menu.class
│  │      │                  │  │  │      MenuOption$MenuOptionBuilder.class
│  │      │                  │  │  │      MenuOption.class
│  │      │                  │  │  │      QMenu.class
│  │      │                  │  │  │      QMenuOption.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          MenuOptionQueryRepository.class
│  │      │                  │  │          MenuOptionQueryRepositoryImpl.class
│  │      │                  │  │          MenuOptionRepository.class
│  │      │                  │  │          MenuQueryRepository.class
│  │      │                  │  │          MenuQueryRepositoryImpl.class
│  │      │                  │  │          MenuRepository.class
│  │      │                  │  │
│  │      │                  │  ├─order
│  │      │                  │  │  ├─aop
│  │      │                  │  │  │      LogOrderCreation.class
│  │      │                  │  │  │      OrderLoggingAspect.class
│  │      │                  │  │  │
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      Order.class
│  │      │                  │  │  │      OrderItem.class
│  │      │                  │  │  │      OrderStatus.class
│  │      │                  │  │  │      QOrder.class
│  │      │                  │  │  │      QOrderItem.class
│  │      │                  │  │  │      
│  │      │                  │  │  └─repository
│  │      │                  │  │          OrderItemRepository.class
│  │      │                  │  │          OrderRepository.class
│  │      │                  │  │
│  │      │                  │  ├─pointHistory
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      PointHistory.class
│  │      │                  │  │  │      PointUsed.class
│  │      │                  │  │  │      PointUseHistory.class
│  │      │                  │  │  │      QPointHistory.class
│  │      │                  │  │  │      QPointUseHistory.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          CustomPointHistoryRepository.class
│  │      │                  │  │          CustomPointHistoryRepositoryImpl.class
│  │      │                  │  │          PointHistoryRepository.class
│  │      │                  │  │          PointUseHistoryRepository.class
│  │      │                  │  │
│  │      │                  │  ├─review
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      CeoReview.class
│  │      │                  │  │  │      QCeoReview.class
│  │      │                  │  │  │      QReview.class
│  │      │                  │  │  │      Review.class
│  │      │                  │  │  │      ReviewStatus.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          CeoReviewRepository.class
│  │      │                  │  │          ReviewRepository.class
│  │      │                  │  │
│  │      │                  │  ├─statistics
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      QStoreOrderStatDaily.class
│  │      │                  │  │  │      QStoreOrderStatId.class
│  │      │                  │  │  │      QStoreOrderStatMonthly.class
│  │      │                  │  │  │      StoreOrderStatDaily.class
│  │      │                  │  │  │      StoreOrderStatId.class
│  │      │                  │  │  │      StoreOrderStatMonthly.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          StoreOrderStatDailyRepository.class
│  │      │                  │  │          StoreOrderStatMonthlyRepository.class
│  │      │                  │  │
│  │      │                  │  ├─store
│  │      │                  │  │  ├─entity
│  │      │                  │  │  │      Category.class
│  │      │                  │  │  │      Dib$DibBuilder.class
│  │      │                  │  │  │      Dib.class
│  │      │                  │  │  │      QCategory.class
│  │      │                  │  │  │      QDib.class
│  │      │                  │  │  │      QStore.class
│  │      │                  │  │  │      Store$StoreBuilder.class
│  │      │                  │  │  │      Store.class
│  │      │                  │  │  │
│  │      │                  │  │  ├─enums
│  │      │                  │  │  │      SortType.class
│  │      │                  │  │  │
│  │      │                  │  │  └─repository
│  │      │                  │  │          CategoryRepository.class
│  │      │                  │  │          DibRepository.class
│  │      │                  │  │          KeywordRedisRepository.class
│  │      │                  │  │          StoreQueryRepository.class
│  │      │                  │  │          StoreQueryRepositoryImpl.class
│  │      │                  │  │          StoreRepository.class
│  │      │                  │  │
│  │      │                  │  └─user
│  │      │                  │      ├─entity
│  │      │                  │      │      QUser.class
│  │      │                  │      │      User.class
│  │      │                  │      │      UserGrade.class
│  │      │                  │      │      UserProvider.class
│  │      │                  │      │      UserRole.class
│  │      │                  │      │
│  │      │                  │      └─repository
│  │      │                  │              RedisRepository.class
│  │      │                  │              UserRepository.class
│  │      │                  │
│  │      │                  └─redis
│  │      │                      └─user
│  │      │                              RedisConfig.class
│  │      │
│  │      └─test
│  │          └─com
│  │              └─example
│  │                  └─dishpatch
│  │                      │  DishPatchApplicationTests.class
│  │                      │
│  │                      └─domain
│  │                          ├─cart
│  │                          │  └─service
│  │                          │          CartServiceTest.class
│  │                          │
│  │                          ├─menu
│  │                          │  └─service
│  │                          │          MenuOptionServiceTest.class
│  │                          │          MenuServiceTest.class
│  │                          │
│  │                          ├─review
│  │                          │  └─service
│  │                          │          CeoReviewServiceTest.class
│  │                          │          ReviewServiceTest.class
│  │                          │
│  │                          ├─store
│  │                          │  └─service
│  │                          │          StoreServiceTest.class
│  │                          │
│  │                          └─user
│  │                              └─service
│  │                                      UserServiceImplTest.class
│  │
│  ├─generated
│  │  └─sources
│  │      ├─annotationProcessor
│  │      │  └─java
│  │      │      ├─main
│  │      │      │  └─com
│  │      │      │      └─example
│  │      │      │          └─dishpatch
│  │      │      │              └─infra
│  │      │      │                  └─db
│  │      │      │                      ├─admin
│  │      │      │                      │  └─entity
│  │      │      │                      │          QAdminStoreOrderStatDaily.java
│  │      │      │                      │          QAdminStoreOrderStatMonthly.java
│  │      │      │                      │          QAdminStoreStat.java
│  │      │      │                      │
│  │      │      │                      ├─cart
│  │      │      │                      │  └─entity
│  │      │      │                      │          QCart.java
│  │      │      │                      │
│  │      │      │                      ├─common
│  │      │      │                      │      QBaseEntity.java
│  │      │      │                      │      QSoftDeletableEntity.java
│  │      │      │                      │
│  │      │      │                      ├─coupon
│  │      │      │                      │  └─entity
│  │      │      │                      │          QCoupon.java
│  │      │      │                      │
│  │      │      │                      ├─menu
│  │      │      │                      │  └─entity
│  │      │      │                      │          QMenu.java
│  │      │      │                      │          QMenuOption.java
│  │      │      │                      │
│  │      │      │                      ├─order
│  │      │      │                      │  └─entity
│  │      │      │                      │          QOrder.java
│  │      │      │                      │          QOrderItem.java
│  │      │      │                      │
│  │      │      │                      ├─pointHistory
│  │      │      │                      │  └─entity
│  │      │      │                      │          QPointHistory.java
│  │      │      │                      │          QPointUseHistory.java
│  │      │      │                      │
│  │      │      │                      ├─review
│  │      │      │                      │  └─entity
│  │      │      │                      │          QCeoReview.java
│  │      │      │                      │          QReview.java
│  │      │      │                      │
│  │      │      │                      ├─statistics
│  │      │      │                      │  └─entity
│  │      │      │                      │          QStoreOrderStatDaily.java
│  │      │      │                      │          QStoreOrderStatId.java
│  │      │      │                      │          QStoreOrderStatMonthly.java
│  │      │      │                      │
│  │      │      │                      ├─store
│  │      │      │                      │  └─entity
│  │      │      │                      │          QCategory.java
│  │      │      │                      │          QDib.java
│  │      │      │                      │          QStore.java
│  │      │      │                      │
│  │      │      │                      └─user
│  │      │      │                          └─entity
│  │      │      │                                  QUser.java
│  │      │      │
│  │      │      └─test
│  │      └─headers
│  │          └─java
│  │              ├─main
│  │              └─test
│  ├─reports
│  │  ├─problems
│  │  │      problems-report.html
│  │  │
│  │  └─tests
│  │      └─test
│  │          │  index.html
│  │          │
│  │          ├─classes
│  │          │      Gradle#20Test#20Executor#202.html
│  │          │
│  │          ├─css
│  │          │      base-style.css
│  │          │      style.css
│  │          │
│  │          ├─js
│  │          │      report.js
│  │          │
│  │          └─packages
│  │                  default-package.html
│  │
│  ├─resources
│  │  └─main
│  │          application.yml
│  │
│  ├─test-results
│  │  └─test
│  │      │  TEST-Gradle#20Test#20Executor#202.xml
│  │      │
│  │      └─binary
│  │              output.bin
│  │              output.bin.idx
│  │              results.bin
│  │
│  └─tmp
│      ├─compileJava
│      │  │  previous-compilation-data.bin
│      │  │
│      │  └─compileTransaction
│      │      ├─backup-dir
│      │      └─stash-dir
│      │              JwtUtil.class.uniqueId3
│      │              OAuth2SuccessHandler.class.uniqueId6
│      │              SecurityConfig.class.uniqueId2
│      │              SecurityFilter.class.uniqueId1
│      │              UserController.class.uniqueId4
│      │              UserService.class.uniqueId0
│      │              UserServiceImpl.class.uniqueId5
│      │
│      ├─compileTestJava
│      │  │  previous-compilation-data.bin
│      │  │
│      │  └─compileTransaction
│      │      ├─backup-dir
│      │      └─stash-dir
│      │              UserServiceImplTest.class.uniqueId0
│      │
│      └─test
├─gradle
│  └─wrapper
│          gradle-wrapper.jar
│          gradle-wrapper.properties
│
├─infra
│  │  docker-compose.yml
│  │
│  └─redis
│      ├─conf
│      │  └─redis.conf
│      └─data
│              dump.rdb
│
└─src
    ├─main
    │  ├─java
    │  │  └─com
    │  │      └─example
    │  │          └─dishpatch
    │  │              │  DishPatchApplication.java
    │  │              │
    │  │              ├─api
    │  │              │  ├─admin
    │  │              │  │  ├─controller
    │  │              │  │  │      AdminStoreStatController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      StoreOrderStatPeriodType.java
    │  │              │  │  │      StoreOrderStatRequest.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          StoreOrderStatItem.java
    │  │              │  │          StoreOrderStatResponse.java
    │  │              │  │
    │  │              │  ├─cart
    │  │              │  │  ├─controller
    │  │              │  │  │      CartController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      CartCreateRequest.java
    │  │              │  │  │      CartUpdateRequest.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          CartCreateResponse.java
    │  │              │  │          CartItemResponse.java
    │  │              │  │          CartResponseDto.java
    │  │              │  │
    │  │              │  ├─coupon
    │  │              │  │  ├─controller
    │  │              │  │  │      .gitkeep
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      .gitkeep
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          .gitkeep
    │  │              │  │
    │  │              │  ├─menu
    │  │              │  │  ├─controller
    │  │              │  │  │      MenuController.java
    │  │              │  │  │      MenuOptionController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      MenuCreateRequest.java
    │  │              │  │  │      MenuOptionAddRequest.java
    │  │              │  │  │      MenuOptionUpdateRequest.java
    │  │              │  │  │      MenuUpdateRequest.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          MenuCreateResponse.java
    │  │              │  │          MenuOptionAddResponse.java
    │  │              │  │          MenuOptionResponse.java
    │  │              │  │          MenuResponse.java
    │  │              │  │          StoreMenuListResponse.java
    │  │              │  │
    │  │              │  ├─order
    │  │              │  │  ├─controller
    │  │              │  │  │      OrderController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      OrderRequestDto.java
    │  │              │  │  │      OrderStatusRequestDto.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          MenuOptionDetailResponseDto.java
    │  │              │  │          OrderDetailResponseDto.java
    │  │              │  │          OrderItemDetailResponseDto.java
    │  │              │  │          OrderResponseDto.java
    │  │              │  │
    │  │              │  ├─pointHistory
    │  │              │  │  ├─controller
    │  │              │  │  │      .gitkeep
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      .gitkeep
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          .gitkeep
    │  │              │  │
    │  │              │  ├─review
    │  │              │  │  ├─controller
    │  │              │  │  │      CeoReviewController.java
    │  │              │  │  │      ReviewController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      CeoReviewCreateRequest.java
    │  │              │  │  │      CeoReviewUpdateRequest.java
    │  │              │  │  │      ReviewCreateRequest.java
    │  │              │  │  │      ReviewUpdateRequest.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          CeoReviewResponse.java
    │  │              │  │          ReviewPageResponse.java
    │  │              │  │          ReviewResponse.java
    │  │              │  │
    │  │              │  ├─statistics
    │  │              │  │  ├─controller
    │  │              │  │  │      StoreStatController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      StoreOrderStatPeriodType.java
    │  │              │  │  │      StoreOrderStatRequest.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          StoreOrderStatItem.java
    │  │              │  │          StoreOrderStatResponse.java
    │  │              │  │
    │  │              │  ├─store
    │  │              │  │  ├─controller
    │  │              │  │  │      StoreController.java
    │  │              │  │  │
    │  │              │  │  ├─request
    │  │              │  │  │      StoreCreateRequest.java
    │  │              │  │  │      StoreUpdateRequest.java
    │  │              │  │  │
    │  │              │  │  └─response
    │  │              │  │          KeywordRank.java
    │  │              │  │          PopularKeywordsResponse.java
    │  │              │  │          StoreCreateResponse.java
    │  │              │  │          StoreInfoResponse.java
    │  │              │  │          StoreResponse.java
    │  │              │  │          StoreSearchResponse.java
    │  │              │  │
    │  │              │  └─user
    │  │              │      ├─controller
    │  │              │      │      .gitkeep
    │  │              │      │      UserController.java
    │  │              │      │
    │  │              │      ├─request
    │  │              │      │      .gitkeep
    │  │              │      │      UserDeleteRequest.java
    │  │              │      │      UserLoginRequest.java
    │  │              │      │      UserSignupRequest.java
    │  │              │      │      UserUpdateRequest.java
    │  │              │      │
    │  │              │      └─response
    │  │              │              .gitkeep
    │  │              │              UserLoginResponse.java
    │  │              │              UserProfileResponse.java
    │  │              │              UserSignupResponse.java
    │  │              │              UserUpdateResponse.java
    │  │              │
    │  │              ├─domain
    │  │              │  ├─admin
    │  │              │  │  ├─exception
    │  │              │  │  │      StatErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │      │  AdminStoreStatService.java
    │  │              │  │      │
    │  │              │  │      └─strategy
    │  │              │  │              AdminDailyOrderStatStrategy.java
    │  │              │  │              AdminMonthlyOrderStatStrategy.java
    │  │              │  │              AdminStoreOrderStatStrategy.java
    │  │              │  │
    │  │              │  ├─cart
    │  │              │  │  ├─exception
    │  │              │  │  │      CartErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          CartService.java
    │  │              │  │
    │  │              │  ├─coupon
    │  │              │  │  ├─exception
    │  │              │  │  │      .gitkeep
    │  │              │  │  │      CouponErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          .gitkeep
    │  │              │  │          CouponService.java
    │  │              │  │
    │  │              │  ├─menu
    │  │              │  │  ├─exception
    │  │              │  │  │      MenuErrorCode.java
    │  │              │  │  │      MenuOptionErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          MenuOptionService.java
    │  │              │  │          MenuService.java
    │  │              │  │
    │  │              │  ├─order
    │  │              │  │  ├─exception
    │  │              │  │  │      .gitkeep
    │  │              │  │  │      OrderErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          .gitkeep
    │  │              │  │          OrderItemService.java
    │  │              │  │          OrderService.java
    │  │              │  │
    │  │              │  ├─pointHistory
    │  │              │  │  ├─exception
    │  │              │  │  │      .gitkeep
    │  │              │  │  │      PointErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          .gitkeep
    │  │              │  │          PointHistoryService.java
    │  │              │  │          PointUseHistoryService.java
    │  │              │  │
    │  │              │  ├─review
    │  │              │  │  ├─exception
    │  │              │  │  │      CeoReviewErrorCode.java
    │  │              │  │  │      ReviewErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          CeoReviewService.java
    │  │              │  │          ReviewService.java
    │  │              │  │
    │  │              │  ├─statistics
    │  │              │  │  ├─exception
    │  │              │  │  │      StatErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │      │  StoreOrderStatService.java
    │  │              │  │      │
    │  │              │  │      └─strategy
    │  │              │  │              DailyOrderStatStrategy.java
    │  │              │  │              MonthlyOrderStatStrategy.java
    │  │              │  │              StoreOrderStatStrategy.java
    │  │              │  │
    │  │              │  ├─store
    │  │              │  │  ├─exception
    │  │              │  │  │      StoreErrorCode.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          StoreService.java
    │  │              │  │
    │  │              │  └─user
    │  │              │      ├─exception
    │  │              │      │      UserErrorCode.java
    │  │              │      │
    │  │              │      └─service
    │  │              │              UserGradeScheduler.java
    │  │              │              UserService.java
    │  │              │              UserServiceImpl.java
    │  │              │
    │  │              ├─global
    │  │              │  ├─config
    │  │              │  │      CacheConfig.java
    │  │              │  │      S3Config.java
    │  │              │  │      SecurityConfig.java
    │  │              │  │
    │  │              │  ├─exception
    │  │              │  │      .gitkeep
    │  │              │  │      BizException.java
    │  │              │  │      CommonErrorCode.java
    │  │              │  │      ErrorCode.java
    │  │              │  │      GlobalExceptionHandler.java
    │  │              │  │
    │  │              │  ├─oauth2
    │  │              │  │  ├─handler
    │  │              │  │  │      OAuth2SuccessHandler.java
    │  │              │  │  │
    │  │              │  │  └─service
    │  │              │  │          CustomOAuth2UserService.java
    │  │              │  │
    │  │              │  ├─response
    │  │              │  │  │  ErrorResponse.java
    │  │              │  │  │
    │  │              │  │  └─pagination
    │  │              │  │          CursorSupport.java
    │  │              │  │          SliceResponse.java
    │  │              │  │
    │  │              │  ├─S3
    │  │              │  │  ├─controller
    │  │              │  │  │      S3ConrollerTemplate.java
    │  │              │  │  │      
    │  │              │  │  └─service
    │  │              │  │          S3Service.java
    │  │              │  │
    │  │              │  └─security
    │  │              │          JwtUtil.java
    │  │              │          SecurityFilter.java
    │  │              │          UserAuth.java
    │  │              │
    │  │              └─infra
    │  │                  ├─batch
    │  │                  │  ├─config
    │  │                  │  │      FirstDayOfMonthDecider.java
    │  │                  │  │      StoreOrderStatBatchConfig.java
    │  │                  │  │
    │  │                  │  ├─processor
    │  │                  │  │      AdminStoreOrderStatDailyProcessor.java
    │  │                  │  │      AdminStoreOrderStatMonthlyProcessor.java
    │  │                  │  │      StoreOrderStatDailyProcessor.java
    │  │                  │  │      StoreOrderStatMonthlyProcessor.java
    │  │                  │  │
    │  │                  │  ├─reader
    │  │                  │  │      AdminStoreOrderStatDailyReader.java
    │  │                  │  │      AdminStoreOrderStatMonthlyReader.java
    │  │                  │  │      StoreOrderStatDailyReader.java
    │  │                  │  │      StoreOrderStatMonthlyReader.java
    │  │                  │  │
    │  │                  │  ├─scheduler
    │  │                  │  │      CartScheduler.java
    │  │                  │  │      StoreOrderStatJobScheduler.java
    │  │                  │  │
    │  │                  │  ├─service
    │  │                  │  │      StoreOrderStatBatchService.java
    │  │                  │  │
    │  │                  │  └─writer
    │  │                  │          AdminStoreOrderDailyWriter.java
    │  │                  │          AdminStoreOrderMonthlyWriter.java
    │  │                  │          StoreOrderDailyWriter.java
    │  │                  │          StoreOrderMonthlyWriter.java
    │  │                  │
    │  │                  ├─config
    │  │                  │      BatchConfig.java
    │  │                  │      JpaConfig.java
    │  │                  │      QueryDslConfig.java
    │  │                  │      SchedulingConfig.java
    │  │                  │
    │  │                  ├─db
    │  │                  │  ├─admin
    │  │                  │  │  ├─entity
    │  │                  │  │  │      AdminStoreOrderStatDaily.java
    │  │                  │  │  │      AdminStoreOrderStatMonthly.java
    │  │                  │  │  │      AdminStoreStat.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          AbstractAdminStoreOrderStatQueryRepository.java
    │  │                  │  │          AdminStoreOrderStatDailyQueryRepository.java
    │  │                  │  │          AdminStoreOrderStatDailyQueryRepositoryImpl.java
    │  │                  │  │          AdminStoreOrderStatDailyRepository.java
    │  │                  │  │          AdminStoreOrderStatMonthlyQueryRepository.java
    │  │                  │  │          AdminStoreOrderStatMonthlyQueryRepositoryImpl.java
    │  │                  │  │          AdminStoreOrderStatMonthlyRepository.java
    │  │                  │  │
    │  │                  │  ├─cart
    │  │                  │  │  ├─entity
    │  │                  │  │  │      Cart.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          CartRepository.java
    │  │                  │  │
    │  │                  │  ├─common
    │  │                  │  │      BaseEntity.java
    │  │                  │  │      SoftDeletableEntity.java
    │  │                  │  │      StatConvertible.java
    │  │                  │  │
    │  │                  │  ├─coupon
    │  │                  │  │  ├─entity
    │  │                  │  │  │      .gitkeep
    │  │                  │  │  │      Coupon.java
    │  │                  │  │  │      CouponType.java
    │  │                  │  │  │      CouponUsed.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          CouponRepository.java
    │  │                  │  │
    │  │                  │  ├─menu
    │  │                  │  │  ├─entity
    │  │                  │  │  │      Menu.java
    │  │                  │  │  │      MenuOption.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          MenuOptionQueryRepository.java
    │  │                  │  │          MenuOptionQueryRepositoryImpl.java
    │  │                  │  │          MenuOptionRepository.java
    │  │                  │  │          MenuQueryRepository.java
    │  │                  │  │          MenuQueryRepositoryImpl.java
    │  │                  │  │          MenuRepository.java
    │  │                  │  │
    │  │                  │  ├─order
    │  │                  │  │  ├─aop
    │  │                  │  │  │      LogOrderCreation.java
    │  │                  │  │  │      OrderLoggingAspect.java
    │  │                  │  │  │
    │  │                  │  │  ├─entity
    │  │                  │  │  │      Order.java
    │  │                  │  │  │      OrderItem.java
    │  │                  │  │  │      OrderStatus.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          .gitkeep
    │  │                  │  │          OrderItemRepository.java
    │  │                  │  │          OrderRepository.java
    │  │                  │  │
    │  │                  │  ├─pointHistory
    │  │                  │  │  ├─entity
    │  │                  │  │  │      .gitkeep
    │  │                  │  │  │      PointHistory.java
    │  │                  │  │  │      PointUsed.java
    │  │                  │  │  │      PointUseHistory.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          .gitkeep
    │  │                  │  │          CustomPointHistoryRepository.java
    │  │                  │  │          CustomPointHistoryRepositoryImpl.java
    │  │                  │  │          PointHistoryRepository.java
    │  │                  │  │          PointUseHistoryRepository.java
    │  │                  │  │
    │  │                  │  ├─review
    │  │                  │  │  ├─entity
    │  │                  │  │  │      CeoReview.java
    │  │                  │  │  │      Review.java
    │  │                  │  │  │      ReviewStatus.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          CeoReviewRepository.java
    │  │                  │  │          ReviewRepository.java
    │  │                  │  │
    │  │                  │  ├─statistics
    │  │                  │  │  ├─entity
    │  │                  │  │  │      StoreOrderStatDaily.java
    │  │                  │  │  │      StoreOrderStatId.java
    │  │                  │  │  │      StoreOrderStatMonthly.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          StoreOrderStatDailyRepository.java
    │  │                  │  │          StoreOrderStatMonthlyRepository.java
    │  │                  │  │
    │  │                  │  ├─store
    │  │                  │  │  ├─entity
    │  │                  │  │  │      Category.java
    │  │                  │  │  │      Dib.java
    │  │                  │  │  │      Store.java
    │  │                  │  │  │
    │  │                  │  │  ├─enums
    │  │                  │  │  │      SortType.java
    │  │                  │  │  │
    │  │                  │  │  └─repository
    │  │                  │  │          CategoryRepository.java
    │  │                  │  │          DibRepository.java
    │  │                  │  │          KeywordRedisRepository.java
    │  │                  │  │          StoreQueryRepository.java
    │  │                  │  │          StoreQueryRepositoryImpl.java
    │  │                  │  │          StoreRepository.java
    │  │                  │  │
    │  │                  │  └─user
    │  │                  │      ├─entity
    │  │                  │      │      .gitkeep
    │  │                  │      │      User.java
    │  │                  │      │      UserGrade.java
    │  │                  │      │      UserProvider.java
    │  │                  │      │      UserRole.java
    │  │                  │      │
    │  │                  │      └─repository
    │  │                  │              .gitkeep
    │  │                  │              RedisRepository.java
    │  │                  │              UserRepository.java
    │  │                  │
    │  │                  └─redis
    │  │                      └─user
    │  │                              RedisConfig.java
    │  │
    │  └─resources
    │          application.yml
    │
    └─test
        └─java
            └─com
                └─example
                    └─dishpatch
                        │  DishPatchApplicationTests.java
                        │
                        └─domain
                            ├─cart
                            │  └─service
                            │          CartServiceTest.java
                            │
                            ├─menu
                            │  └─service
                            │          MenuOptionServiceTest.java
                            │          MenuServiceTest.java
                            │
                            ├─review
                            │  └─service
                            │          CeoReviewServiceTest.java
                            │          ReviewServiceTest.java
                            │
                            ├─store
                            │  └─service
                            │          StoreServiceTest.java
                            │
                            └─user
                                └─service
                                        UserServiceImplTest.java

```
