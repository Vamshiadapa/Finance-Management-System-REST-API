create database finance_db;
use finance_db;

create table customers (
    customer_id bigint auto_increment primary key,
    customer_name varchar(100) not null,
    email varchar(150) unique not null,
    phone varchar(15) unique not null,
    address varchar(255),
    date_of_birth date not null,
    created_at timestamp default current_timestamp
);

create table accounts (
    account_id bigint auto_increment primary key,
    account_number varchar(20) unique not null,
    account_type varchar(20) not null,
    balance decimal(15,2) not null default 0.00,
    customer_id bigint not null,
    created_at timestamp default current_timestamp,
    constraint fk_account_customer foreign key (customer_id)
        references customers(customer_id)
);

create table transactions (
    transaction_id bigint auto_increment primary key,
    transaction_type varchar(20) not null,
    amount decimal(15,2) not null,
    transaction_date timestamp default current_timestamp,
    description varchar(255),
    account_id bigint not null,
    target_account_id bigint,
    constraint fk_transaction_account foreign key (account_id)
        references accounts(account_id),
    constraint fk_transaction_target foreign key (target_account_id)
        references accounts(account_id)
);

create table loans (
    loan_id bigint auto_increment primary key,
    loan_type varchar(30) not null,
    loan_amount decimal(15,2) not null,
    interest_rate decimal(5,2) not null,
    duration int not null,
    total_payable decimal(15,2) not null,
    amount_paid decimal(15,2) default 0.00,
    status varchar(20) default 'ACTIVE',
    start_date date not null,
    customer_id bigint not null,
    constraint fk_loan_customer foreign key (customer_id)
        references customers(customer_id)
);

create table investments (
    investment_id bigint auto_increment primary key,
    investment_type varchar(30) not null,
    amount decimal(15,2) not null,
    current_value decimal(15,2) not null,
    performance decimal(7,2) default 0.00,
    invested_date date not null,
    maturity_date date not null,
    status varchar(20) default 'ACTIVE',
    customer_id bigint not null,
    constraint fk_investment_customer foreign key (customer_id)
        references customers(customer_id)
);

create table loan_documents (
    document_id bigint auto_increment primary key,
    document_type varchar(30) not null,
    file_name varchar(255) not null,
    stored_name varchar(255) not null,
    file_type varchar(100) not null,
    file_size bigint not null,
    file_path varchar(500) not null,
    uploaded_date timestamp default current_timestamp,
    loan_id bigint not null,
    constraint fk_document_loan foreign key (loan_id)
        references loans(loan_id)
        on delete cascade
);

show tables;

insert into customers
(customer_name, email, phone, address, date_of_birth)
values
('Vamshi', 'vamshi@gmail.com', '9876543210', 'Hyderabad', '2000-05-15'),
('Krishna', 'krishna@gmail.com', '9876543211', 'Bangalore', '1999-08-20');

insert into accounts
(account_number, account_type, balance, customer_id)
values
('ACC1001', 'SAVINGS', 25000.00, 1),
('ACC1002', 'CURRENT', 50000.00, 2);

insert into loans
(loan_type, loan_amount, interest_rate, duration, total_payable, amount_paid, status, start_date, customer_id)
values
('HOME', 500000.00, 8.50, 120, 925000.00, 100000.00, 'ACTIVE', '2026-01-10', 1),
('CAR', 300000.00, 9.00, 60, 435000.00, 50000.00, 'ACTIVE', '2026-02-15', 2);

insert into investments
(investment_type, amount, current_value, performance, invested_date, maturity_date, status, customer_id)
values
('MUTUAL_FUND', 100000.00, 110000.00, 10.00, '2026-01-01', '2030-01-01', 'ACTIVE', 1),
('FD', 50000.00, 54000.00, 8.00, '2026-03-01', '2027-03-01', 'ACTIVE', 2);