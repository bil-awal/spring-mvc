use spring_tutorial;

-- BEGIN: Danger! Don't execute
DROP TABLE users;
-- END: Danger! Don't execute

-- BEGIN: Users
CREATE TABLE users(
    username        VARCHAR(30)     NOT NULL,
    password        VARCHAR(999)    NOT NULL,
    name            VARCHAR(30)     NULL,
    token           VARCHAR(999)    NULL,
    token_expire    BIGINT,

    PRIMARY KEY (username),
    UNIQUE (token)

) ENGINE InnoDB;
-- END: Users

-- BEGIN: Contacts
CREATE TABLE contacts(
    id          VARCHAR(100)    NOT NULL,
    user_id     VARCHAR(30)     NOT NULL,
    first_name  VARCHAR(20)     NOT NULL,
    last_name   VARCHAR(20)     NULL,
    phone       BIGINT          NULL,
    email       VARCHAR(100)    NULL,

    PRIMARY KEY (id),
    FOREIGN KEY fk_users_contacts(user_id) REFERENCES users(username)

) ENGINE InnoDB;
-- END: Contacts

-- BEGIN: Address
CREATE TABLE addresses(
    id          VARCHAR(100)    NOT NULL,
    contact_id  VARCHAR(100)    NOT NULL,
    street      VARCHAR(250)    NULL,
    city        VARCHAR(50)     NULL,
    province    VARCHAR(50)     NULL,
    country     VARCHAR(50)     NOT NULL,
    zip_code    INT             NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY fk_contacts_addresses(contact_id) REFERENCES contacts(id)
) ENGINE InnoDB;

DESC addresses;
-- END: Address