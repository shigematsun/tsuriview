CREATE TABLE usertbl (
  id VARCHAR(100),
  password VARCHAR(100) NOT NULL,
  role VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE m_prefecture (
  id VARCHAR(2),
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE m_place (
  id SERIAL,
  name VARCHAR(100) NOT NULL,
  prefecture VARCHAR(2) NOT NULL,
  tide_pc VARCHAR(2),
  tide_hc VARCHAR(2),
  map_url VARCHAR(1000),
  PRIMARY KEY(id)
);

CREATE TABLE m_fish (
  id SERIAL,
  name VARCHAR(100) NOT NULL,
  image_key VARCHAR(1000),
  PRIMARY KEY(id)
);

CREATE TABLE m_method (
  id SERIAL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE image (
  id SERIAL,
  key VARCHAR(1000) NOT NULL,
  entry_id INTEGER,
  entry_index INTEGER,
  created_datetime TIMESTAMP NOT NULL,
  updated_datetime TIMESTAMP NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE entry (
  id SERIAL,
  date DATE NOT NULL,
  month INTEGER NOT NULL,
  start_time VARCHAR(5) NOT NULL,
  end_time VARCHAR(5) NOT NULL,
  prefecture INTEGER NOT NULL,
  place INTEGER NOT NULL,
  memo VARCHAR(1000),
  created_datetime TIMESTAMP NOT NULL,
  updated_datetime TIMESTAMP NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE entry_fish (
  entry_id INTEGER NOT NULL,
  index INTEGER NOT NULL,
  fish INTEGER NOT NULL,
  count INTEGER NOT NULL,
  min INTEGER NOT NULL,
  max INTEGER,
  method INTEGER NOT NULL,
  time_hour VARCHAR(2) NOT NULL,
  time_minute VARCHAR(2) NOT NULL,
  created_datetime TIMESTAMP NOT NULL,
  updated_datetime TIMESTAMP NOT NULL,
  PRIMARY KEY(entry_id, index)
);
