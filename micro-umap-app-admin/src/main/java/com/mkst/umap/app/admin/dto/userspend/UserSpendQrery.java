package com.mkst.umap.app.admin.dto.userspend;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserSpendQrery {

	private Long userId;
	private Long deptId;
	/**
	 * 交易月份
	 */
	private String transactionMonth;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getTransactionMonth() {
		return transactionMonth;
	}

	public void setTransactionMonth(String transactionMonth) {
		this.transactionMonth = transactionMonth;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
