-- 用户表插入语句 (5条)

INSERT INTO `user` (id_, account_, user_name, phone_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (1, 'zhangsan', '张三', '13800138000', '普通用户', 1, 1, NOW(), 1, NOW());

INSERT INTO `user` (id_, account_, user_name, phone_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (2, 'lisi', '李四', '13900139000', 'VIP会员', 1, 1, NOW(), 1, NOW());

INSERT INTO `user` (id_, account_, user_name, phone_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (3, 'wangwu', '王五', '13700137000', '商务客户', 1, 1, NOW(), 1, NOW());

INSERT INTO `user` (id_, account_, user_name, phone_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (4, 'zhaoliu', '赵六', '13600136000', '常旅客', 1, 1, NOW(), 1, NOW());

INSERT INTO `user` (id_, account_, user_name, phone_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (5, 'sunqi', '孙七', '13500135000', '新注册用户', 1, 1, NOW(), 1, NOW());

-- 预定信息表插入语句 (5条)

INSERT INTO `booking` (id_, user_id, booking_num, booking_date, from_, to_, status_, level_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (1, 1, 'booking20240101001', '2024-01-20 10:00:00', '北京', '上海', 1, 1, '靠窗座位', 1, 1, NOW(), 1, NOW());

INSERT INTO `booking` (id_, user_id, booking_num, booking_date, from_, to_, status_, level_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (2, 2, 'booking20240101002', '2024-01-21 14:30:00', '广州', '深圳', 1, 2, '靠过道', 1, 1, NOW(), 1, NOW());

INSERT INTO `booking` (id_, user_id, booking_num, booking_date, from_, to_, status_, level_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (3, 3, 'booking20240101003', '2024-01-22 09:00:00', '上海', '杭州', 1, 3, '商务舱', 1, 1, NOW(), 1, NOW());

INSERT INTO `booking` (id_, user_id, booking_num, booking_date, from_, to_, status_, level_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (4, 4, 'booking20240101004', '2024-01-23 16:00:00', '成都', '重庆', 1, 1, '', 1, 1, NOW(), 1, NOW());

INSERT INTO `booking` (id_, user_id, booking_num, booking_date, from_, to_, status_, level_, remark_, enable_, create_by, create_time, update_by, update_time)
VALUES (5, 5, 'booking20240101005', '2024-01-25 08:00:00', '武汉', '长沙', 1, 2, '靠窗', 1, 1, NOW(), 1, NOW());
