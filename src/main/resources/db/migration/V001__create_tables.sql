CREATE TABLE persons (
                         id UUID NOT NULL,
                         person_type VARCHAR(20) NOT NULL,
                         document_value VARCHAR(20) NOT NULL,
                         name VARCHAR(100) NOT NULL,
                         last_name VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT pk_persons PRIMARY KEY (id),

                         CONSTRAINT uk_persons_document_value UNIQUE (document_value),
                         CONSTRAINT uk_persons_email UNIQUE (email)
);
CREATE TABLE daily_transaction_limit (
                            id UUID NOT NULL,
                            person_id UUID NOT NULL,
                            currency VARCHAR(3) NOT NULL,
                            amount DECIMAL(19, 2) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT pk_daily_transaction_limit PRIMARY KEY (id),
                            CONSTRAINT uk_daily_transaction_limit_person_currency UNIQUE (person_id, currency),
                            CONSTRAINT fk_daily_transaction_limit_persons FOREIGN KEY (person_id)
                                REFERENCES persons (id) ON DELETE CASCADE
);

CREATE TABLE accounts (
                          id UUID NOT NULL,
                          person_id UUID NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT pk_accounts PRIMARY KEY (id),
                          CONSTRAINT uk_accounts_person UNIQUE (person_id),
                          CONSTRAINT fk_accounts_persons FOREIGN KEY (person_id)
                              REFERENCES persons (id) ON DELETE RESTRICT
);

CREATE TABLE wallets (
                         id UUID NOT NULL,
                         currency VARCHAR(3) NOT NULL,
                         balance NUMERIC(19, 2) NOT NULL,
                         account_id UUID NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT pk_wallets PRIMARY KEY (id),
                         CONSTRAINT fk_wallets_accounts FOREIGN KEY (account_id)
                             REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE transactions (
                             id UUID NOT NULL,
                             wallet_source_id UUID,
                             wallet_destiny_id UUID,
                             amount DECIMAL(19, 2) NOT NULL,
                             status VARCHAR(30) NOT NULL,
                             type VARCHAR(30) NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT pk_transactions PRIMARY KEY (id),
                             CONSTRAINT fk_transactions_wallet_source FOREIGN KEY (wallet_source_id)
                                 REFERENCES wallets (id) ON DELETE CASCADE,
                             CONSTRAINT fk_transactions_wallet_destiny FOREIGN KEY (wallet_destiny_id)
                                 REFERENCES wallets (id) ON DELETE CASCADE
);

CREATE TABLE remittances (
                             id UUID NOT NULL,
                             transaction_id UUID NOT NULL,
                             converted_currency_amount DECIMAL(19, 2) NOT NULL,
                             exchange_rate DECIMAL(19, 8) NOT NULL,
                             created_at DATE NOT NULL,
                             updated_at DATE NOT NULL,

                             CONSTRAINT pk_remittances PRIMARY KEY (id),
                             CONSTRAINT fk_remittances_transactions FOREIGN KEY (transaction_id)
                                 REFERENCES transactions (id) ON DELETE CASCADE
);

