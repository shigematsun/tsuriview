INSERT INTO usertbl
  (id, password, role)
VALUES
  ('user1', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', 'admin'),
  ('user2', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', 'general');

INSERT INTO m_prefecture
  (id, name)
VALUES
  ('14', '神奈川県'),
  ('22', '静岡県');

INSERT INTO m_place
  (id, name, prefecture, tide_pc, tide_hc, map_url)
VALUES
  (1, 'ふれーゆ裏', '14', '14', '2', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d1366.0459382278968!2d139.6880372716315!3d35.47897232435631!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x60185d8b256af70d%3A0x459357c6e263a1f4!2z44G144KM44O844KG6KOP!5e0!3m2!1sja!2sjp!4v1646442115779!5m2!1sja!2sjp'),
  (2, '川崎新堤', '14', '14', '2', 'https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d12993.610231505152!2d139.780149!3d35.494323!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xe59b9dcc0eec5a47!2z5p2x5omH5bO26Ziy5rOi5aCk!5e0!3m2!1sja!2sjp!4v1646867247593!5m2!1sja!2sjp'),
  (3, '伊東港', '22', '22', '2', 'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3269.3943448781674!2d139.1033993155374!3d34.971785876158314!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x6019c3e6ed91548b%3A0xb8e490d1e75030e4!2z5LyK5p2x5riv!5e0!3m2!1sja!2sjp!4v1645612117689!5m2!1sja!2sjp');

INSERT INTO m_fish
  (id, name, image_key)
VALUES
  (1, 'アジ', '20220206135922497/IMG_20210523_073312.jpg'),
  (2, 'カサゴ', '20220206135922497/IMG_20210523_052855.jpg');

INSERT INTO m_method
  (id, name)
VALUES
  (1, '投げ（アオイソメ）'),
  (2, 'メタルジグ');

INSERT INTO image
  (id, key, entry_id, entry_index, created_datetime, updated_datetime)
VALUES
  (1, '20220206135922497/IMG_20210523_082747.jpg', 1, 1, '2022-01-01 15:38:51.55', '2022-01-01 15:38:51.55'),
  (2, '20220206135922497/IMG_20210523_073312.jpg', 1, 2, '2022-01-01 15:38:51.55', '2022-01-01 15:38:51.55'),
  (3, '20220206135922497/IMG_20210523_052855.jpg', 2, 1,'2022-01-01 15:38:51.55', '2022-01-01 15:38:51.55'),
  (4, '20220206135922497/IMG_20210523_052855.jpg', null, null,'2022-01-01 15:38:51.55', '2022-01-01 15:38:51.55');

INSERT INTO entry
  (id, date, month, start_time, end_time, prefecture, place, memo, created_datetime, updated_datetime)
VALUES
  (1, '2022-02-06', 2, '06:05', '12:25', '14', 1, 'めも', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319'),
  (2, '2022-02-02', 2, '02:02', '22:22', '14', 1, 'めも2', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319'),
  (3, '2022-02-03', 2, '03:03', '13:33', '14', 1, 'めも3', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319'),
  (4, '2021-01-04', 1, '04:04', '14:44', '14', 1, 'めも4', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319'),
  (5, '2021-02-04', 2, '04:04', '14:44', '14', 2, 'めも5', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319');

INSERT INTO entry_fish
  (entry_id, index, fish, count, min, max, method, time_hour, time_minute, created_datetime, updated_datetime)
VALUES
  (1, 1, 2, 1, 10, null, 1, '07', '09', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319'),
  (1, 2, 1, 4, 12, 25, 2, '10', '11', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319'),
  (2, 2, 1, 4, 12, 25, 2, '10', '11', '2022-02-06 14:10:21.319', '2022-02-06 14:10:21.319');

  
