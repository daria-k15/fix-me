DROP TABLE IF EXISTS transactions;

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
