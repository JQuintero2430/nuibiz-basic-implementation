CREATE TABLE IF NOT EXISTS `crud_demo`.`commerce` (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                         `name` VARCHAR(45) NOT NULL,
                                         `username` VARCHAR(45) NOT NULL,
                                         `password` VARCHAR(45) NOT NULL,
                                         PRIMARY KEY (`id`),
                                         UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
                                         UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);