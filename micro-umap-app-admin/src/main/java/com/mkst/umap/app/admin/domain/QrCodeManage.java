package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 二维码管理表 umap_qr_code_manage
 * 
 * @author wangyong
 * @date 2020-07-13
 */
@Data
public class QrCodeManage extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 二维码id */
	private Long qrCodeId;
	/** 业务id */
	private Long businessId;
	/** 业务事项 */
	private String businessMatter;
	/** 跳转url */
	private String jumpUrl;
	/** 是否需要跳转（'0' 否  '1' 1是） */
	private Boolean jumpWhether;

	/** 是否加密（'0' 否  '1' 是） */
	private Boolean encryptWhether;

	/** 二维码地址 */
	private String qrCodeAddress;

	/** 加密数据区 */
	private String encryptDataArea;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新人 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 删除标识 */
	private String delFlag;
	/** 备注 */
	private String remark;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("qrCodeId", getQrCodeId())
            .append("businessId", getBusinessId())
            .append("businessMatter", getBusinessMatter())
            .append("jumpUrl", getJumpUrl())
            .append("jumpStatus", getJumpWhether())
				.append("encryptWhether", getEncryptWhether())
				.append("jumpStatus", getQrCodeAddress())
            .append("encryptDataArea", getEncryptDataArea())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .toString();
    }
}
