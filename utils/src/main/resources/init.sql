create table transactions(
                             id serial not null primary key,
                             msg_length varchar not null,
                             msg_type int not null,
                             date date not null,
                             username varchar not null,
                             item_id varchar not null,
                             amount varchar not null,
                             price double precision not null,
                             market_name varchar not null,
                             check_sum varchar not null
);

drop table transactions;

CREATE TABLE IF NOT EXISTS transactions(id serial not null primary key,
                                        msg_length varchar not null,
                                        msg_type varchar not null,
                                        date varchar not null,
                                        username varchar not null,
                                        item_id varchar not null,
                                        amount varchar not null,
                                        price varchar not null,
                                        market_name varchar not null,
                                        check_sum varchar not null);