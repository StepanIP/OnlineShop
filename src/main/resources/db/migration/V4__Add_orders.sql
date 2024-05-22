INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (1, 'Wall Street1', 'New York', '2021-04-07', 'test123@test.com', 'John', 'Doe', '1234567890', 1234567890, 840, 2);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (2, 'Wall Street1', 'New York', '2021-04-07', 'test123@test.com', 'John', 'Doe', '1234567890', 1234567890, 240, 2);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (3, 'Tverskaya street 1', 'Moscow', '2021-04-07', 'ivan123@test.com', 'Ivan', 'Ivanov', '1234567890', 1234567890, 163, 3);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (4, 'Tverskaya street 1', 'Moscow', '2021-04-07', 'ivan123@test.com', 'Ivan', 'Ivanov', '1234567890', 1234567890, 780, 3);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (5, 'Tverskaya street 1', 'Moscow', '2021-04-07', 'ivan123@test.com', 'Ivan', 'Ivanov', '1234567890', 1234567890, 196, 3);

INSERT INTO orders_phones (order_id, phones_id) VALUES (1, 1);
INSERT INTO orders_phones (order_id, phones_id) VALUES (1, 2);
INSERT INTO orders_phones (order_id, phones_id) VALUES (2, 3);
INSERT INTO orders_phones (order_id, phones_id) VALUES (2, 4);
INSERT INTO orders_phones (order_id, phones_id) VALUES (3, 5);
INSERT INTO orders_phones (order_id, phones_id) VALUES (3, 6);
INSERT INTO orders_phones (order_id, phones_id) VALUES (3, 7);
INSERT INTO orders_phones (order_id, phones_id) VALUES (4, 8);
INSERT INTO orders_phones (order_id, phones_id) VALUES (4, 8);
INSERT INTO orders_phones (order_id, phones_id) VALUES (5, 9);
INSERT INTO orders_phones (order_id, phones_id) VALUES (5, 10);
