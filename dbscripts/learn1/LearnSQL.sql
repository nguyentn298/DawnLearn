DROP TABLE IF EXISTS menu_item_temp;

CREATE TABLE menu_item_temp (
  `PARENT_ID` INT(11) DEFAULT NULL,
  `NAME` VARCHAR(100) NOT NULL,
  `TITLE` VARCHAR(100) NOT NULL,
  `LOCATION` VARCHAR(256) NOT NULL,
  `IMAGE` VARCHAR(100) DEFAULT NULL,
  `IMAGE_FOCUS` VARCHAR(100) DEFAULT NULL,
  `SORT_ORDER` INT(11) NOT NULL DEFAULT 0
) ENGINE=INNODB DEFAULT CHARSET=utf8;

SET @parentId = (SELECT MENU_ITEM_ID FROM menu_item WHERE NAME = 'Utilities' limit 1);
INSERT INTO `menu_item_temp`(`PARENT_ID`,`NAME`,`TITLE`,`LOCATION`,`IMAGE`,`IMAGE_FOCUS`,`SORT_ORDER`) 
VALUE (@parentId,'ViewLog','View Log','utilities/viewLog.sp',NULL,NULL,10);

INSERT INTO `menu_item`(`PARENT_ID`,`NAME`,`TITLE`,`LOCATION`,`IMAGE`,`IMAGE_FOCUS`,`SORT_ORDER`)
SELECT `PARENT_ID`,`NAME`,`TITLE`,`LOCATION`,`IMAGE`,`IMAGE_FOCUS`,`SORT_ORDER` 
FROM menu_item_temp
WHERE `NAME` NOT IN (SELECT `NAME` FROM menu_item);	

DROP TABLE IF EXISTS menu_item_temp;

/* Find a column of every table in specific db */

SELECT DISTINCT TABLE_NAME 
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE COLUMN_NAME IN ('LOAN_NUMBER')
        AND TABLE_SCHEMA='aklero_idea_db_2_17_1';
