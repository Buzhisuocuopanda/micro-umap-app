package com.mkst.umap.app.mall.common.mybatis;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.mall.common.constant.MallConstants;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonFieldHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		// 插入方法实体填充
		Object createTime = metaObject.getValue("createTime");
		if (createTime == null) {
			this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
		}

		Object tenantId = metaObject.getValue("tenantId");
		if (tenantId == null) {
			this.strictInsertFill(metaObject, "tenantId", String.class, MallConstants.SYS_DEFAULT_TENANT_ID);
		}
		try {
			SysUser user = ShiroUtils.getSysUser();
			if (user != null) {
				Object createBy = metaObject.getValue("createBy");
				if (createBy == null) {
					this.strictInsertFill(metaObject, "createBy", String.class, Convert.toStr(user.getUserId()));
				}

				Object deptId = metaObject.getValue("deptId");
				if (deptId == null) {
					this.strictInsertFill(metaObject, "deptId", String.class, Convert.toStr(user.getDeptId()));
				}
			}
		} catch (Exception e) {
			log.warn("插入方法实体填充失败");
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 更新方法实体填充
		Object updateTime = metaObject.getValue("updateTime");
		if (updateTime == null) {
			this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
		}

		try {
			SysUser user = ShiroUtils.getSysUser();
			if (user != null) {
				Object updateBy = metaObject.getValue("updateBy");
				if (updateBy == null) {
					this.strictUpdateFill(metaObject, "updateBy", String.class, Convert.toStr(user.getUserId()));
				}
			}
		} catch (Exception e) {
			log.warn("更新方法实体填充失败");
		}
	}
}
