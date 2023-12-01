# Money-api

### 바로가기
- [요구 사항](#요구-사항)
- [프로젝트 구조](#프로젝트-구조)
- [고려 사항](#고려-사항)

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

### 고려 사항
```
```
