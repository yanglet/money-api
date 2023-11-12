CREATE TABLE `money`.`MEMBER`
(
    `member_no`       INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '회원 번호',
    `status`          VARCHAR(10) NOT NULL COMMENT '회원 상태 (탈퇴, 활동)',
    `insert_date`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '등록일자',
    `insert_operator` VARCHAR(50) NOT NULL COMMENT '등록자',
    `update_date`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '수정일자',
    `update_operator` VARCHAR(50) NOT NULL COMMENT '수정자',
    PRIMARY KEY (`member_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE='utf8mb4_general_ci' COMMENT='회원';


CREATE TABLE `money`.`WALLET`
(
    `wallet_no`       INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '지갑 번호',
    `member_no`       INT UNSIGNED NOT NULL COMMENT '회원 번호',
    `balance`         INT UNSIGNED NOT NULL COMMENT '보유 금액',
    `maximum_balance` INT UNSIGNED NOT NULL COMMENT '보유 금액 최대 한도',
    `insert_date`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '등록일자',
    `insert_operator` VARCHAR(50) NOT NULL COMMENT '등록자',
    `update_date`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '수정일자',
    `update_operator` VARCHAR(50) NOT NULL COMMENT '수정자',
    PRIMARY KEY (`wallet_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE='utf8mb4_general_ci' COMMENT='지갑';

CREATE INDEX idx_member_no ON `money`.`WALLET` ( member_no );


CREATE TABLE `money`.`REMITTANCE`
(
    `remittance_no`   INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '송금 번호',
    `to_member_no`    INT UNSIGNED NOT NULL COMMENT '송금 받는 회원 번호',
    `from_member_no`  INT UNSIGNED NOT NULL COMMENT '송금 하는 회원 번호',
    `to_balance`      INT UNSIGNED NOT NULL COMMENT '송금 받는 회원의 보유 금액 (거래 후)',
    `from_balance`    INT UNSIGNED NOT NULL COMMENT '송금 하는 회원의 보유 금액 (거래 후)',
    `amount`          INT UNSIGNED NOT NULL COMMENT '송금 금액',
    `status`          VARCHAR(10) NOT NULL COMMENT '송금 상태 (실패, 성공)',
    `reason`          VARCHAR(255) COMMENT '송금 상태에 따른 사유',
    `insert_date`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '등록일자',
    `insert_operator` VARCHAR(50) NOT NULL COMMENT '등록자',
    `update_date`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) COMMENT '수정일자',
    `update_operator` VARCHAR(50) NOT NULL COMMENT '수정자',
    PRIMARY KEY (`remittance_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE='utf8mb4_general_ci' COMMENT='송금';

CREATE INDEX idx_from_member_no ON `money`.`REMITTANCE` ( from_member_no );