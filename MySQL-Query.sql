CREATE DATABASE mowbiedb;
DROP DATABASE mowbiedb;
USE mowbiedb;

SELECT * FROM tb_users;
SELECT * FROM tb_categories;
SELECT * FROM tb_products;
SELECT * FROM tb_orders;
SELECT * FROM tb_order_items;
SELECT * FROM tb_payments;
SELECT * FROM tb_reviews;
SELECT * FROM tb_carts;

-- Xoá dữ liệu các bảng! Reset AUTO_INCREMENT về 1
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE tb_users;
TRUNCATE TABLE tb_categories;
TRUNCATE TABLE tb_products;
TRUNCATE TABLE tb_orders;
TRUNCATE TABLE tb_order_items;
TRUNCATE TABLE tb_payments;
TRUNCATE TABLE tb_reviews;
TRUNCATE TABLE tb_carts;
SET FOREIGN_KEY_CHECKS=1;

-- Khởi tạo database
CREATE TABLE tb_users (
                          user_id INT PRIMARY KEY AUTO_INCREMENT,
                          username VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          phone_number VARCHAR(10) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          user_role ENUM('customer', 'admin') DEFAULT 'customer'
);

CREATE TABLE tb_categories (
                               category_id INT PRIMARY KEY AUTO_INCREMENT,
                               category_name VARCHAR(100) NOT NULL UNIQUE,
                               category_description TEXT
);

CREATE TABLE tb_products (
                             product_id INT PRIMARY KEY AUTO_INCREMENT,
                             product_name VARCHAR(255) NOT NULL,
                             product_description TEXT,
                             product_image_path VARCHAR(255),
                             product_price DECIMAL(14,2) NOT NULL,
                             category_id INT NOT NULL DEFAULT 1,
                             FOREIGN KEY (category_id) REFERENCES tb_categories(category_id)
);

-- Trigger này khi một danh mục bị xóa, tất cả sản phẩm thuộc danh mục đó sẽ có category_id = 1.
DELIMITER //
CREATE TRIGGER before_category_delete
    BEFORE DELETE ON tb_categories
    FOR EACH ROW
BEGIN
    UPDATE tb_products SET category_id = 1 WHERE category_id = OLD.category_id;
END;
//
DELIMITER ;

CREATE TABLE tb_orders (
                           order_id INT PRIMARY KEY AUTO_INCREMENT,
                           user_id INT,
                           total_price DECIMAL(10,2) NOT NULL,
                           order_status ENUM('pending', 'processing', 'completed', 'canceled') DEFAULT 'pending',
                           order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES tb_users(user_id) ON DELETE CASCADE
);

CREATE TABLE tb_order_items (
                                order_item_id INT PRIMARY KEY AUTO_INCREMENT,
                                order_id INT,
                                product_id INT,
                                order_item_quantity INT NOT NULL,
                                order_item_price DECIMAL(10,2) NOT NULL,
                                FOREIGN KEY (order_id) REFERENCES tb_orders(order_id) ON DELETE CASCADE,
                                FOREIGN KEY (product_id) REFERENCES tb_products(product_id) ON DELETE CASCADE
);

CREATE TABLE tb_payments (
                             payment_id INT PRIMARY KEY AUTO_INCREMENT,
                             order_id INT,
                             payment_amount DECIMAL(10,2) NOT NULL,
                             payment_method ENUM('cash', 'bank_transfer') NOT NULL,
                             payment_status ENUM('pending', 'paid', 'failed') DEFAULT 'pending',
                             payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (order_id) REFERENCES tb_orders(order_id) ON DELETE CASCADE
);

CREATE TABLE tb_reviews (
                            review_id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT,
                            product_id INT,
                            review_rating INT CHECK (review_rating BETWEEN 1 AND 5),
                            review_comment TEXT,
                            FOREIGN KEY (user_id) REFERENCES tb_users(user_id) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES tb_products(product_id) ON DELETE CASCADE
);

CREATE TABLE tb_cart (
                         cart_id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL,
                         product_id INT NOT NULL,
                         quantity INT NOT NULL DEFAULT 1,
                         added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES tb_users(user_id) ON DELETE CASCADE,
                         FOREIGN KEY (product_id) REFERENCES tb_products(product_id) ON DELETE CASCADE
);

-- Dữ liệu mẫu cho tb_users
INSERT INTO tb_users (username, email, phone_number, password, user_role) VALUES
-- Password: Hoang@123
('Nguyễn Minh Hoàng', 'hoang.nguyen78@huflit.com', '0986257260', 'gnsTUdrIRR4b0A/+xc8/DESKFwszKypusRKogTr6EKU=', 'customer'),

-- Password: HangTran@456
('Trần Thị Thu Hằng', 'hang.tran92@huflit.vn', '0338454190', 'Qf3q0HCKIJebhWtWg1AK/MoOPx/V5J/BXD8v6gMi7GQ=', 'customer'),

-- Password: LeHuy@789
('Lê Quang Huy', 'huy.le88@huflit.net', '0917882758', 'KkLI4tDHFZ1AX1j/N6Jtdas0Ajw5xI52qJRYYmKCK1A=', 'customer'),

-- Password: DungPham!2024
('Phạm Văn Dũng', 'dung.pham01@huflit.org', '0330281062', 'UBM6SiWldWylaLOMC2HV4tmvoF9WnNkvLMQlcQMpFH0=', 'customer'),

-- Password: TrucHT@321
('Hoàng Thanh Trúc', 'truc.hoang95@huflit.edu.vn', '0325846465', '3gzttNJoLjvc/1SSWqAMR/ZGICI0QyAdRK+DNcQ3ig8=', 'customer'),

-- Password: NamDo#852
('Đỗ Hoài Nam', 'nam.do76@huflit.io', '0351243526', '3GxfJr0EJ1jZ/lR7D0A1nXgZxY4NP5tuQOJJGVxB0LE=', 'customer'),

-- Password: BuiLan$963
('Bùi Thị Lan', 'lan.bui89@huflit.com', '0978836935', 'Y1Wxjj6w0QK4TCsjC3KtbC+XYbeqOVwFTYLAcDCThtI=', 'customer'),

-- Password: HieuNgo@741
('Ngô Minh Hiếu', 'hieu.ngo11@huflit.vn', '0771664009', '5XRachp5CdXj2c0Gi9/bfMfKV6ktk8Ol0mtZvrhMNaU=', 'customer'),

-- Password: DuongHa!159
('Dương Thu Hà', 'ha.duong25@huflit.net', '0937193677', 'VhIZ6nARWthaPy9qCipcyqr//f6wk5mkoNHlsS2tR28=', 'customer'),

-- Password: VuLam@357
('Vũ Văn Lâm', 'lam.vu54@huflit.org', '0380559175', 'gD3z9NlYa9+mfReSdqP9f4nvsJ+v9Ryk210r3YIV1hM=', 'customer'),

-- Password: LyKhanh@753
('Lý Khánh An', 'an.ly72@huflit.io', '0810967201', 'rJ3f53C4AbRJUlqvXuFnC1Fp+VuAm8Gk0IPP249snJ8=', 'customer'),

-- Password: ToBich#468
('Tô Ngọc Bích', 'bich.to39@huflit.com', '0841992987', 'JlvAq+Wttc4pp3daXjRdWBVxyzOQXs7G1OFrUR4xAgg=', 'customer'),

-- Password: CaoKhang@124
('Cao Hoàng Khang', 'khang.cao66@huflit.vn', '0327005146', '4ykewa+EcJNX/c4cejxdLV/yzBj3vKdDC1n6V/JfuZo=', 'customer'),

-- Password: NgocChau!986
('Châu Bảo Ngọc', 'ngoc.chau87@huflit.net', '0966106038', 'obIcYiFcOXLZXmM/h19IXornrZuus/wTrPVn8PkbrEY=', 'customer'),

-- Password: VoQuan@529
('Võ Văn Quân', 'quan.vo91@huflit.org', '0918275605', 'ZHOJKrVf9nbkrOcsWFplNieUcI3jAWoNhMOG9SdUrZo=', 'customer'),

-- Password: TaLinh#748
('Tạ Nhật Linh', 'linh.ta07@huflit.io', '0969391619', 'Y8o/uaQVwS+Njcc39w5MozuVudBPVXKgsIzt5vRtRto=', 'customer'),

-- Password: MaiPhuong@639
('Mai Thị Phương', 'phuong.mai36@huflit.com', '0751255052', 'BMnta4TqCmnHCwbPEDghl4tXNV9V2mwl+rgfVoRsfqw=', 'customer'),

-- Password: ThachDuc@852
('Thạch Minh Đức', 'duc.thach45@huflit.vn', '0790025783', 'YL3iZhQgfK36MaToPZ5qkx5MpEpWDwhxHsPQWewHJUs=', 'customer'),

-- Password: HuynhKieu!731
('Huỳnh Thị Kiều', 'kieu.huynh99@huflit.net', '0750956890', 'SICBNXz9CHUegO7uwT+m4Z+BGRnDQnOgjBrWstKSft4=', 'customer'),

-- Password: DinhThanh#951
('Đinh Công Thành', 'thanh.dinh22@huflit.org', '0859803101', 'rT7ju72OPc6nN36GcZ4rFlKCj7HLPXAsgC388VfUEhU=', 'customer'),

-- ADMIN - Password: Admin@Secure2024
('Nguyễn Văn Quản Trị', 'admin@huflit.edu.vn', '0363450237', 'BEOG0NL8ELkftPH+lo/24wbmecKlcfK1NdkwHO8z99M=', 'admin');

-- Dữ liệu mẫu cho tb_categories
INSERT INTO tb_categories (category_name, category_description) VALUES
('Khác', 'Danh mục dành cho các hãng điện thoại không có trong danh sách.'),
('Apple', 'Thương hiệu iPhone nổi tiếng đến từ Mỹ, chuyên sản xuất điện thoại cao cấp.'),
('Samsung', 'Hãng công nghệ Hàn Quốc với các dòng Galaxy phổ biến từ giá rẻ đến cao cấp.'),
('Xiaomi', 'Thương hiệu Trung Quốc nổi bật với cấu hình mạnh, giá thành hợp lý.'),
('Oppo', 'Hãng điện thoại Trung Quốc chuyên về camera selfie và thiết kế đẹp.'),
('Vivo', 'Công ty con của BBK Electronics, nổi bật với camera và âm thanh chất lượng.'),
('OnePlus', 'Thương hiệu con của Oppo, tập trung vào điện thoại cao cấp với hiệu năng mạnh.'),
('Nokia', 'Hãng điện thoại lâu đời, nổi bật với độ bền và thương hiệu lâu năm.');

-- Dữ liệu mẫu cho tb_products
INSERT INTO tb_products (product_name, product_description, product_image_path, product_price, category_id) VALUES
                                                                                                                ('Tai nghe Bluetooth', 'Phụ kiện cần thiết giúp nâng cao trải nghiệm sử dụng điện thoại.', '', 37990000, 1),
                                                                                                                ('Tai nghe có dây', 'Sản phẩm tiện ích hỗ trợ sạc, bảo vệ và kết nối thiết bị.', '', 28090000, 1),
                                                                                                                ('Sạc nhanh 65W', 'Thiết bị hỗ trợ tối ưu, bền bỉ, dễ dàng mang theo.', '', 34090000, 1),
                                                                                                                ('Sạc không dây', 'Phụ kiện cần thiết giúp nâng cao trải nghiệm sử dụng điện thoại.', '', 26990000, 1),
                                                                                                                ('Thẻ nhớ microSD 128GB', 'Thiết bị hỗ trợ tối ưu, bền bỉ, dễ dàng mang theo.', '', 37090000, 1),
                                                                                                                ('Thẻ nhớ microSD 256GB', 'Sản phẩm tiện ích hỗ trợ sạc, bảo vệ và kết nối thiết bị.', '', 43090000, 1),
                                                                                                                ('Cáp sạc USB Type-C', 'Thiết bị hỗ trợ tối ưu, bền bỉ, dễ dàng mang theo.', '', 34090000, 1),
                                                                                                                ('Cáp Lightning', 'Sản phẩm tiện ích hỗ trợ sạc, bảo vệ và kết nối thiết bị.', '', 42090000, 1),
                                                                                                                ('Giá đỡ điện thoại', 'Phụ kiện cần thiết giúp nâng cao trải nghiệm sử dụng điện thoại.', '', 39090000, 1),
                                                                                                                ('Ốp lưng chống sốc', 'Sản phẩm tiện ích hỗ trợ sạc, bảo vệ và kết nối thiết bị.', '', 29090000, 1),
                                                                                                                ('iPhone 14 Pro', 'Sản phẩm mới nhất từ Apple, thiết kế sang trọng, hiệu năng mạnh mẽ.', '', 35090000, 2),
                                                                                                                ('iPhone 15 Pro Max', 'Apple tiếp tục nâng tầm với sản phẩm mới, trang bị công nghệ hiện đại.', '', 33090000, 2),
                                                                                                                ('iPhone 15 Pro', 'Sản phẩm mới nhất từ Apple, thiết kế sang trọng, hiệu năng mạnh mẽ.', '', 24990000, 2),
                                                                                                                ('iPhone 14 Pro Max', 'Apple tiếp tục nâng tầm với sản phẩm mới, trang bị công nghệ hiện đại.', '', 39090000, 2),
                                                                                                                ('Galaxy Z Flip5', 'Dòng Galaxy cao cấp với màn hình chất lượng và camera AI thông minh.', '', 18090000, 3),
                                                                                                                ('Galaxy S23 Ultra', 'Điện thoại Samsung mới nhất, trải nghiệm mượt mà và bền bỉ.', '', 10990000, 3),
                                                                                                                ('Galaxy S22 Ultra', 'Điện thoại Samsung mới nhất, trải nghiệm mượt mà và bền bỉ.', '', 19090000, 3),
                                                                                                                ('Galaxy S23', 'Điện thoại Samsung mới nhất, trải nghiệm mượt mà và bền bỉ.', '', 27090000, 3),
                                                                                                                ('Xiaomi 12', 'Điện thoại Xiaomi thế hệ mới với pin trâu, màn hình đẹp.', '', 6090000, 4),
                                                                                                                ('Xiaomi 13 Ultra', 'Điện thoại Xiaomi thế hệ mới với pin trâu, màn hình đẹp.', '', 12990000, 4),
                                                                                                                ('Poco X4 Pro', 'Xiaomi tiếp tục khẳng định vị thế với cấu hình mạnh, giá hợp lý.', '', 4990000, 4),
                                                                                                                ('Redmi Note 12', 'Sản phẩm Xiaomi với công nghệ tiên tiến, camera sắc nét.', '', 11990000, 4),
                                                                                                                ('Oppo Reno8', 'Điện thoại Oppo với màn hình AMOLED, chụp ảnh chân dung ấn tượng.', '', 12090000, 5),
                                                                                                                ('Oppo Find X6 Pro', 'Oppo thế hệ mới, hiệu năng mạnh, phù hợp với giới trẻ.', '', 11990000, 5),
                                                                                                                ('Vivo X80', 'Dòng sản phẩm Vivo mang đến hiệu năng tốt, màn hình sắc nét.', '', 11090000, 6),
                                                                                                                ('Vivo X80 Pro', 'Dòng sản phẩm Vivo mang đến hiệu năng tốt, màn hình sắc nét.', '', 13990000, 6),
                                                                                                                ('OnePlus Nord CE 3', 'Dòng flagship OnePlus mạnh mẽ, sạc siêu nhanh.', '', 22090000, 7),
                                                                                                                ('OnePlus 11', 'Smartphone OnePlus với OxygenOS mượt mà, hiệu năng đỉnh cao.', '', 24990000, 7),
                                                                                                                ('Nokia G60', 'Điện thoại Nokia thế hệ mới với thiết kế hiện đại, cấu hình tốt.', '', 7090000, 8),
                                                                                                                ('Nokia G21', 'Sản phẩm Nokia đáng tin cậy, đơn giản mà mạnh mẽ.', '', 2090000, 8),
                                                                                                                ('Nothing Phone 1', 'Sản phẩm nổi bật từ nhiều thương hiệu, tối ưu cho game thủ.', '', 23090000, 8),
                                                                                                                ('Nothing Phone 2', 'Dòng sản phẩm đặc biệt, thiết kế ấn tượng, hiệu suất vượt trội.', '', 22090000, 8),
                                                                                                                ('Asus ROG Phone 7', 'Dòng sản phẩm đặc biệt, thiết kế ấn tượng, hiệu suất vượt trội.', '', 24090000, 8);

-- Dữ liệu mẫu cho tb_reviews
INSERT INTO tb_reviews (user_id, product_id, review_rating, review_comment) VALUES
                                                                                (13, 1, 4, 'Giá hơi cao nhưng đáng tiền.'),
                                                                                (6, 1, 1, 'Hài lòng với chất lượng, đáng để mua.'),
                                                                                (8, 1, 1, 'Không xứng đáng với giá tiền, thất vọng.'),
                                                                                (7, 1, 2, 'Sản phẩm chất lượng tốt, rất hài lòng.'),
                                                                                (1, 1, 4, 'Giá hơi cao nhưng đáng tiền.'),
                                                                                (2, 2, 5, 'Hiệu năng mạnh, chạy ứng dụng mượt.'),
                                                                                (7, 2, 2, 'Hài lòng với chất lượng, đáng để mua.'),
                                                                                (5, 3, 5, 'Không xứng đáng với giá tiền, thất vọng.'),
                                                                                (3, 3, 5, 'Sản phẩm trung bình, không quá đặc sắc.'),
                                                                                (2, 3, 2, 'Sản phẩm không tốt như mong đợi.'),
                                                                                (18, 3, 4, 'Có một số lỗi nhỏ nhưng vẫn chấp nhận được.'),
                                                                                (16, 4, 3, 'Hiệu năng mạnh, chạy ứng dụng mượt.'),
                                                                                (18, 4, 1, 'Không xứng đáng với giá tiền, thất vọng.'),
                                                                                (1, 4, 4, 'Mình nghĩ sản phẩm này có thể cải thiện thêm.'),
                                                                                (14, 4, 1, 'Hiệu năng mạnh, chạy ứng dụng mượt.'),
                                                                                (5, 5, 5, 'Mình nghĩ sản phẩm này có thể cải thiện thêm.'),
                                                                                (18, 5, 4, 'Thiết kế đẹp, cảm giác cầm nắm thoải mái.'),
                                                                                (17, 5, 5, 'Hiệu năng mạnh, chạy ứng dụng mượt.'),
                                                                                (9, 5, 1, 'Có một số lỗi nhỏ nhưng vẫn chấp nhận được.'),
                                                                                (17, 6, 1, 'Không xứng đáng với giá tiền, thất vọng.'),
                                                                                (3, 6, 3, 'Mình nghĩ sản phẩm này có thể cải thiện thêm.'),
                                                                                (7, 6, 5, 'Sản phẩm chất lượng tốt, rất hài lòng.'),
                                                                                (5, 6, 3, 'Thiết kế đẹp, cảm giác cầm nắm thoải mái.'),
                                                                                (11, 7, 4, 'Không xứng đáng với giá tiền, thất vọng.'),
                                                                                (19, 7, 5, 'Mình nghĩ sản phẩm này có thể cải thiện thêm.'),
                                                                                (8, 7, 3, 'Có một số lỗi nhỏ nhưng vẫn chấp nhận được.'),
                                                                                (5, 7, 5, 'Thiết kế đẹp, cảm giác cầm nắm thoải mái.'),
                                                                                (18, 8, 3, 'Chất lượng hoàn thiện tốt, dùng ổn định.'),
                                                                                (16, 8, 4, 'Thiết kế đẹp, cảm giác cầm nắm thoải mái.'),
                                                                                (20, 8, 2, 'Sản phẩm chất lượng tốt, rất hài lòng.'),
                                                                                (3, 8, 2, 'Sản phẩm không tốt như mong đợi.'),
                                                                                (2, 8, 3, 'Chất lượng tạm ổn, chưa thực sự ấn tượng.');







