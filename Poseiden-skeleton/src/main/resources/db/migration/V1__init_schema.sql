CREATE TABLE bidlist (
  bid_list_id INT NOT NULL AUTO_INCREMENT,
  account      VARCHAR(30) NOT NULL,
  type         VARCHAR(30) NOT NULL,
  bid_quantity DECIMAL(19,4),
  PRIMARY KEY (bid_list_id)
);

CREATE TABLE trade (
  trade_id     INT NOT NULL AUTO_INCREMENT,
  account      VARCHAR(30) NOT NULL,
  type         VARCHAR(30) NOT NULL,
  buy_quantity DECIMAL(19,4),
  PRIMARY KEY (trade_id)
);

CREATE TABLE curvepoint (
  id INT(4) NOT NULL AUTO_INCREMENT,
  curve_id TINYINT,
  as_of_date TIMESTAMP,
  term DOUBLE,
  value DOUBLE,
  creation_date TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE rating (
  id INT(4) NOT NULL AUTO_INCREMENT,
  moodys_rating VARCHAR(125),
  sandp_rating VARCHAR(125),
  fitch_rating VARCHAR(125),
  order_number TINYINT,
  PRIMARY KEY (id)
);

CREATE TABLE rulename (
  id INT(4) NOT NULL AUTO_INCREMENT,
  name VARCHAR(125),
  description VARCHAR(125),
  json VARCHAR(125),
  template VARCHAR(512),
  sql_str VARCHAR(125),
  sql_part VARCHAR(125),
  PRIMARY KEY (id)
);

CREATE TABLE users (
  id INT(4) NOT NULL AUTO_INCREMENT,
  username VARCHAR(125) NOT NULL UNIQUE,
  password VARCHAR(125),
  fullname VARCHAR(125),
  role VARCHAR(125),
  PRIMARY KEY (id)
);