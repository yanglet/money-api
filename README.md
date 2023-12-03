# Money-api

### 바로가기
- [요구 사항](#요구-사항)
- [프로젝트 구조](#프로젝트-구조)
- [구현 사항](#구현-사항)
- [의문](#의문)
- [컨벤션](#컨벤션)

### 요구 사항
- 머니 충전 API
- 송금 API
- 회원에 따른 송금 목록 조회 API
```
송금의 취소는 발생하지않는다.
회원은 보유할 수 있는 최대 한도가 있다.
```

### 프로젝트 구조
```
├── main
│   ├── kotlin
│   │   └── com
│   │       └── money
│   │           ├── MoneyApplication.kt
│   │           ├── adapter
│   │           │   ├── in
│   │           │   │   └── web
│   │           │   │       ├── RemittanceController.kt
│   │           │   │       ├── WalletController.kt
│   │           │   │       └── dto
│   │           │   │           ├── DepositRequest.kt
│   │           │   │           ├── ReadRemittancesResponse.kt
│   │           │   │           └── RemitRequest.kt
│   │           │   └── out
│   │           │       ├── cache
│   │           │       │   └── redis
│   │           │       │       └── configuration
│   │           │       │           ├── CacheConfiguration.kt
│   │           │       │           └── RedisConfiguration.kt
│   │           │       └── persistence
│   │           │           ├── MemberPersistenceAdapter.kt
│   │           │           ├── RemittancePersistenceAdapter.kt
│   │           │           ├── WalletPersistenceAdapter.kt
│   │           │           └── jpa
│   │           │               ├── MemberJpaEntity.kt
│   │           │               ├── MemberJpaRepository.kt
│   │           │               ├── RemittanceJpaEntity.kt
│   │           │               ├── RemittanceJpaRepository.kt
│   │           │               ├── WalletJpaEntity.kt
│   │           │               ├── WalletJpaRepository.kt
│   │           │               ├── common
│   │           │               │   └── BaseEntity.kt
│   │           │               └── configuration
│   │           │                   ├── AuditingConfiguration.kt
│   │           │                   └── CustomAuditorAware.kt
│   │           ├── application
│   │           │   ├── domain
│   │           │   │   ├── exception
│   │           │   │   │   ├── BalanceInsufficientException.kt
│   │           │   │   │   └── BalanceMaxedOutException.kt
│   │           │   │   ├── model
│   │           │   │   │   ├── Member.kt
│   │           │   │   │   ├── MemberStatus.kt
│   │           │   │   │   ├── Money.kt
│   │           │   │   │   ├── Remittance.kt
│   │           │   │   │   ├── RemittanceStatus.kt
│   │           │   │   │   └── Wallet.kt
│   │           │   │   └── usecase
│   │           │   │       ├── DepositService.kt
│   │           │   │       ├── ReadRemittancesService.kt
│   │           │   │       └── RemitService.kt
│   │           │   └── port
│   │           │       ├── in
│   │           │       │   ├── DepositUseCase.kt
│   │           │       │   ├── ReadRemittancesUseCase.kt
│   │           │       │   ├── RemitUseCase.kt
│   │           │       │   └── dto
│   │           │       │       ├── DepositCommand.kt
│   │           │       │       └── RemitCommand.kt
│   │           │       └── out
│   │           │           ├── CreateRemittancePort.kt
│   │           │           ├── LoadMemberPort.kt
│   │           │           ├── LoadRemittancesPort.kt
│   │           │           ├── LoadWalletLockPort.kt
│   │           │           └── UpdateWalletPort.kt
│   │           └── common
│   │               ├── PersistenceAdapter.kt
│   │               ├── UseCase.kt
│   │               ├── WebAdapter.kt
│   │               └── exception
│   │                   └── DataNotFoundException.kt
│   └── resources
│       ├── application-local.yml
│       ├── application-test.yml
│       ├── application.yml
│       └── db
│           └── schema.sql
└── test
    └── kotlin
        └── com
            └── money
                ├── adapter
                │   └── out
                │       └── persistence
                │           └── WalletPersistenceAdapterJunit.kt
                └── application
                    └── domain
                        ├── MemberFreeSpec.kt
                        ├── MoneyAnnotationSpec.kt
                        ├── WalletBehaviorSpec.kt
                        └── usecase
                            └── integration
                                ├── DepositConcurrencyBehaviorSpec.kt
                                └── RemitBehaviorSpec.kt
```

### 헥사고날 아키텍처 (Hexagonal Architecture)
```
```

### 구현 사항
```
- 크리티컬한 비즈니스이므로 비관적락을 통해 동시성 제어 ( Redisson pub/sub 을 통한 분산락 -> 비관적락으로 변경 )
  * Redis 분산락을 건 서버가 장애날 경우
  -> Redisson 의 SpinLock형태를 이용해서 default timeout 이 발생하거나 영구적으로 해당 유저는 Redis 키가 삭제될때 까지 트랜잭션이 실패하는 이슈가 발생
  -> 그렇다고 여러 대 두기엔 오베 엔지니어링일 수 있다.
  * redisson pub/sub 은 lettuce 의 spin lock 보다 개선된 것은 맞지만 spin lock 로직이 내부에 일부 있다.
  * Redis에 작업을 건 프로세스가 컨텍스트 스위칭이나 특정 블럭킹으로 시간이 오래걸리면 Key 가 Expire 되면서 중복 작업이 발생할 수 있다.

- Redis 캐시를 사용했으나 인프라가 구축되어 있지 않고 트래픽이 많지 않으면 로컬 캐시를 고려할 것 같다.
  캐시 동기화를 위해 변경될 때 CacheEvict 해준다.

- Cache stampede (TTL 만료 시 요청이 몰려 부하가 발생하는 이슈)
  -> 특정 회원에 따른 송금 목록 조회이므로 비즈니스상 많은 부하가 몰릴 가능성이 적다고 판단하여 고려하지 않았다.
  -> 만일 고려해야한다면 TTL 전에 캐시 갱신, 키 전략을 좀 더 상세하게 가져가기, 마지막 요청만 처리하기 등의 해결책이 있겠다.

- 테스트 코드를 작성해 신뢰성을 높이고자 했다.
```

### 의문
```
- response dto 로의 변환은 어디서 가져가는게 좋을까? (service? controller?)

- domain model 을 어떻게 만들면 좋을까? (data class 로 private var 했더니 getter 를 만들어줘야하는데 ,,)
```

### 컨벤션
```
- 클래스의 시작과 끝 부분은 줄바꿈한다. (인터페이스 등 제외)

- return 문 전에 줄바꿈

- 책임을 가지는 필드는 객체로 만든다. (ex. Money)

- 가독성을 위해 named argument 를 적극 사용한다.

- service dto 의 네이밍은 usecase 이름 + command or query, controller dto 의 네이밍은 usecase 이름 + request or response
```