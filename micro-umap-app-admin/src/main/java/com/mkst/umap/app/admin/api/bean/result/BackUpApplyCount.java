package com.mkst.umap.app.admin.api.bean.result;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;

@Data
public class BackUpApplyCount implements Serializable {

	private static final long serialVersionUID = 1L;

	private int totalNumber;
	private int dayNumber;
	
	private String userName;
	private String deptName;
	private int applyNumber;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
