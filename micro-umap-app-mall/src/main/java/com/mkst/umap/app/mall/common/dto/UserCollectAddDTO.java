/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author www.joolun.com
 * @date 2019-08-12 16:25:10
 */
@Data
public class UserCollectAddDTO implements Serializable {
	private static final long serialVersionUID = 1L;

    private String type;
    private List<String> relationIds;

}
