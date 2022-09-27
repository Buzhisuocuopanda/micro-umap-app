ALTER TABLE `umap_user_spend` 
ADD COLUMN `user_name` varchar(100) NULL COMMENT '用户姓名' AFTER `user_id`;