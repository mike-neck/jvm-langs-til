CREATE TABLE `speedment-orm`.`hare` (
  `id`    INT(11)     NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(45) NOT NULL,
  `color` VARCHAR(45) NOT NULL,
  `age`   INT(11)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 100;

CREATE TABLE IF NOT EXISTS `speedment-orm`.`carrot` (
  `id`    INT(11)     NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(45) NOT NULL,
  `owner` INT(11)     NOT NULL,
  `rival` INT(11),
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 100;

CREATE TABLE IF NOT EXISTS `speedment-orm`.`human` (
  `id`   INT(11)     NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 100;

CREATE TABLE `speedment-orm`.`friend` (
  `hare`  INT(11) NOT NULL,
  `human` INT(11) NOT NULL,
  PRIMARY KEY (`hare`, `human`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE `speedment-orm`.`carrot`
  ADD CONSTRAINT `carrot_owner_to_hare_id` FOREIGN KEY (`owner`) REFERENCES `hare` (`id`);
ALTER TABLE `speedment-orm`.`carrot`
  ADD CONSTRAINT `carrot_rival_to_hare_id` FOREIGN KEY (`rival`) REFERENCES `hare` (`id`);

ALTER TABLE `speedment-orm`.`friend`
  ADD CONSTRAINT `friend_hare_to_hare_id` FOREIGN KEY (`hare`) REFERENCES `hare` (`id`);
ALTER TABLE `speedment-orm`.`friend`
  ADD CONSTRAINT `friend_human_to_human_id` FOREIGN KEY (`human`) REFERENCES `human` (`id`);
