-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `psd16` DEFAULT CHARACTER SET utf8 ;
USE `psd16` ;

-- -----------------------------------------------------
-- Table `mydb`.`Client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Client` (
  `Username` VARCHAR(20) NOT NULL,
  `AccountBalance` FLOAT NOT NULL,
  PRIMARY KEY (`Username`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Accoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `psd16`.`Accoes` (
  `idAccoes` INT NOT NULL AUTO_INCREMENT,
  `NomeEmpresa` VARCHAR(45) NOT NULL,
  `Quantidade` INT NOT NULL,
  `User` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`idAccoes`),
  INDEX `fk_Accoes_Client_idx` (`User` ASC),
  CONSTRAINT `fk_Accoes_Client`
    FOREIGN KEY (`User`)
    REFERENCES `psd16`.`Client` (`Username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
