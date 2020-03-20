SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '收件员');
INSERT INTO `role` VALUES ('2', '客服');
INSERT INTO `role` VALUES ('3', '资料检查员');
INSERT INTO `role` VALUES ('4', '签证处理员');
INSERT INTO `role` VALUES ('5', '管理员');

-- ----------------------------
-- Table structure for static_page
-- ----------------------------
DROP TABLE IF EXISTS `static_page`;
CREATE TABLE `static_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_name` varchar(255) DEFAULT NULL,
  `page_url` varchar(255) DEFAULT NULL,
  `parent_node` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of static_page
-- ----------------------------
INSERT INTO `static_page` VALUES ('1', '快件签收', '/ExpressReceipt.html', '1');
INSERT INTO `static_page` VALUES ('2', '收件管理', '/ReceiptManagement.html', '1');
INSERT INTO `static_page` VALUES ('3', '异常快件', '/AbnormalExpress.html', '1');
INSERT INTO `static_page` VALUES ('4', '护照信息录入', '/PassportInformationEntry.html', '2');
INSERT INTO `static_page` VALUES ('5', '签证信息审核', '/VisaInformationCheck.html', '2');
INSERT INTO `static_page` VALUES ('6', '待补充签证', '/VisaToBeSupplemented.html', '2');
INSERT INTO `static_page` VALUES ('7', '签证处理进度', '/VisapProcessing.html', '2');
INSERT INTO `static_page` VALUES ('8', '签证处理记录', '/VisaProcessingRecord.html', '2');
INSERT INTO `static_page` VALUES ('9', '电商订单查询', '/ElectricitySupplierOrder.html', '3');
INSERT INTO `static_page` VALUES ('10', '角色分配', '/user.html', '4');
INSERT INTO `static_page` VALUES ('11', '订单状态详情', '/OrderStatusDetails.html', '2');

-- ----------------------------
-- Table structure for method_path
-- ----------------------------
DROP TABLE IF EXISTS `method_path`;
CREATE TABLE `method_path` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `page_id` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of method_path
-- ----------------------------
INSERT INTO `method_path` VALUES ('1', '添加快件', '1', '/backExpressReceipt/expressReceipt');
INSERT INTO `method_path` VALUES ('2', '快件备注', '1', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('3', '收件管理', '2', '/backExpressReceipt/getExpressReceiptByCondition');
INSERT INTO `method_path` VALUES ('4', '添加异常', '2', '/backExpressReceipt/addUnusual');
INSERT INTO `method_path` VALUES ('5', '更正', '2', '/backExpressReceipt/updateExpressReceipt');
INSERT INTO `method_path` VALUES ('6', '快件备注', '2', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('7', '查询异常快件', '3', '/backExpressReceipt/getUnusualExpressReceiptByCondition');
INSERT INTO `method_path` VALUES ('8', '更正', '3', '/backExpressReceipt/updateExpressReceipt');
INSERT INTO `method_path` VALUES ('9', '快件备注', '3', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('10', '查询快件备注信息（异常', '2', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('11', '查询快件备注信息（添加', '1', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('12', '查询快件备注信息（收件管理', '3', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('13', '护照信息录入查询', '4', '/backExpressReceipt/getExpressReceiptByCondition');
INSERT INTO `method_path` VALUES ('14', '查看护照', '4', '/visaHandle/getPassportByExpressReceiptId');
INSERT INTO `method_path` VALUES ('15', '修改护照', '4', '/visaHandle/updatePassport');
INSERT INTO `method_path` VALUES ('16', '根据护照id查询护照信息', '4', '/visaHandle/getPassportByPassId');
INSERT INTO `method_path` VALUES ('17', '导入excel表格', '4', '/visaHandle/savePassPortByExcel/');
INSERT INTO `method_path` VALUES ('18', '上传照片', '4', '/visaHandle/localUploadPics/');
INSERT INTO `method_path` VALUES ('19', '批量提交护照', '4', '/visaHandle/addPassPortByBatch');
INSERT INTO `method_path` VALUES ('20', '备注（护照', '4', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('21', '查看备注（护照', '4', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('22', '查看签证审核', '5', '/visaHandle/getPassPortByStatus');
INSERT INTO `method_path` VALUES ('23', '审核', '5', '/visaHandle/checkPassportById');
INSERT INTO `method_path` VALUES ('24', '查看备注（审核', '5', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('25', '备注（审核', '5', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('26', '查看待补充签证', '6', '/visaHandle/getProblemPassport');
INSERT INTO `method_path` VALUES ('27', '修改护照（待补充', '6', '/visaHandle/updatePassport');
INSERT INTO `method_path` VALUES ('28', '发回重审', '6', '/visaHandle/aRetrialPassPort');
INSERT INTO `method_path` VALUES ('29', '查看备注（待补充', '6', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('30', '备注（待补充', '6', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('31', '签证处理进度查询', '7', '/visaHandle/getPassPortByPass');
INSERT INTO `method_path` VALUES ('32', '送签', '7', '/visaHandle/sendVisa/');
INSERT INTO `method_path` VALUES ('33', '拒签出签', '7', '/visaHandle/outAndRefuseVisa/');
INSERT INTO `method_path` VALUES ('34', '寄回', '7', '/visaHandle/sendBackVisa');
INSERT INTO `method_path` VALUES ('35', '一同寄回', '7', '/visaHandle/sendBackVisaTogether');
INSERT INTO `method_path` VALUES ('36', '备注（进度', '7', '/backExpressReceipt/expressReceiptRemark');
INSERT INTO `method_path` VALUES ('37', '查看备注（进度', '7', '/backExpressReceipt/getExpressReceiptRemark');
INSERT INTO `method_path` VALUES ('38', '签证处理记录', '8', '/backRoleConsole/getVisaOperationRecordByCondition');
INSERT INTO `method_path` VALUES ('39', '新建账户', '10', '/backRoleConsole/registBackUser');
INSERT INTO `method_path` VALUES ('40', '重置密码', '10', '/backRoleConsole/updateBackUserPwd2');
INSERT INTO `method_path` VALUES ('41', '分配角色', '10', '/backRoleConsole/saveUserRole');
INSERT INTO `method_path` VALUES ('42', '查询全部角色', '10', '/backRoleConsole/getAllRole');
INSERT INTO `method_path` VALUES ('43', '查询全部后台人员', '10', '/backRoleConsole/getAllBackuserByCondition');
INSERT INTO `method_path` VALUES ('44', '移除角色', '10', '/backRoleConsole/removeUserRole');



-- ----------------------------
-- Table structure for urlrelation
-- ----------------------------
DROP TABLE IF EXISTS `urlrelation`;
CREATE TABLE `urlrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` varchar(255) DEFAULT NULL,
  `urlid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of urlrelation
-- ----------------------------
INSERT INTO `urlrelation` VALUES ('1', '1', '1');
INSERT INTO `urlrelation` VALUES ('2', '1', '2');
INSERT INTO `urlrelation` VALUES ('3', '1', '9');
INSERT INTO `urlrelation` VALUES ('4', '2', '3');
INSERT INTO `urlrelation` VALUES ('5', '2', '9');
INSERT INTO `urlrelation` VALUES ('6', '2', '6');
INSERT INTO `urlrelation` VALUES ('7', '3', '5');
INSERT INTO `urlrelation` VALUES ('8', '3', '4');
INSERT INTO `urlrelation` VALUES ('9', '4', '7');
INSERT INTO `urlrelation` VALUES ('10', '3', '11');
INSERT INTO `urlrelation` VALUES ('11', '4', '11');
