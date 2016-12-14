-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema psd16
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema psd16
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `psd16` DEFAULT CHARACTER SET utf8 ;
USE `psd16` ;

-- -----------------------------------------------------
-- Table `psd16`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Users` (
  `Username` VARCHAR(40) NOT NULL,
  `Password` VARCHAR(45) NOT NULL,
  `AccountNumber` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `psd16`.`Sells`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Sells` (
  `idSell` INT NOT NULL AUTO_INCREMENT,
  `Company` VARCHAR(45) NOT NULL,
  `Quantidade` INT NOT NULL,
  `Price` FLOAT NOT NULL,
  `Users_Username` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`idSell`),
  INDEX `fk_Sells_Users_idx` (`Users_Username` ASC),
  CONSTRAINT `fk_Sells_Users`
    FOREIGN KEY (`Users_Username`)
    REFERENCES `psd16`.`Users` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `psd16`.`Buys`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Buys` (
  `idBuy` INT NOT NULL AUTO_INCREMENT,
  `Company` VARCHAR(20) NOT NULL,
  `Quantidade` INT NOT NULL,
  `Price` FLOAT NOT NULL,
  `Users_Username` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`idBuy`),
  INDEX `fk_Buys_Users1_idx` (`Users_Username` ASC),
  CONSTRAINT `fk_Buys_Users1`
    FOREIGN KEY (`Users_Username`)
    REFERENCES `psd16`.`Users` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `psd16`.`Request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Request` (
  `idRequest` INT NOT NULL AUTO_INCREMENT,
  `Accoes` INT NOT NULL,
  `Company` VARCHAR(45) NOT NULL,
  `User1` VARCHAR(40) NOT NULL,
  `User2` VARCHAR(40) NOT NULL,
  `AcNR1` VARCHAR(30) NOT NULL,
  `AcNR2` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`idRequest`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `psd16`.`Actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Actions` (
  `idActions` INT NOT NULL AUTO_INCREMENT,
  `Company` VARCHAR(45) NOT NULL,
  `Ammount` INT NOT NULL,
  `Users_Username` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`idActions`),
  INDEX `fk_Actions_Users1_idx` (`Users_Username` ASC),
  CONSTRAINT `fk_Actions_Users1`
    FOREIGN KEY (`Users_Username`)
    REFERENCES `psd16`.`Users` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `psd16`.`Bank`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Bank` (
  `AccountNumber` VARCHAR(30) NOT NULL,
  `Balance` FLOAT NOT NULL,
  PRIMARY KEY (`AccountNumber`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
