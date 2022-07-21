ALTER TABLE `umap_back_up_room` 
ADD COLUMN `door_lock_id` varchar(64) NULL COMMENT '门锁ID' AFTER `status`,
ADD COLUMN `use_count` int(11) NOT NULL DEFAULT 0 COMMENT '使用次数' AFTER `door_lock_id`;