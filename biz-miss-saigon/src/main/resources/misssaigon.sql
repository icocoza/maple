
CREATE TABLE `user` (
  `userId` varchar(64) NOT NULL,
  `userName` varchar(64) NOT NULL,
  `anonymous` tinyint(1) DEFAULT '0',
  `osType` varchar(16) DEFAULT NULL,
  `osVersion` varchar(8) DEFAULT NULL,
  `appVersion` varchar(8) DEFAULT NULL,
  `inAppcode` varchar(16) DEFAULT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `leftAt` datetime DEFAULT NULL,
  `lastAt` datetime DEFAULT NULL,
  `likes` int(11) DEFAULT '0',
  `dislikes` int(11) DEFAULT '0',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `userAuth` (
  `userId` varchar(64) NOT NULL,
  `uid` varchar(64) DEFAULT NULL,
  `email` varchar(64) DEFAULT '',
  `phone` varchar(64) DEFAULT '',
  `pw` varchar(64) DEFAULT '',
  `emailCode` varchar(32) DEFAULT '',
  `smsCode` varchar(6) DEFAULT '',
  `registeredAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `leftAt` datetime DEFAULT NULL,
  `authType` varchar(12) NOT NULL,
  PRIMARY KEY (`userId`),
  KEY `idx_uid` (`uid`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `userToken` (
  `userId` varchar(64) NOT NULL,
  `uuid` varchar(128) DEFAULT NULL,
  `tokenId` varchar(64) NOT NULL,
  `token` varchar(128) NOT NULL,
  `createdAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `expiredAt` datetime DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`userId`,`tokenId`),
  KEY `idx_uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
