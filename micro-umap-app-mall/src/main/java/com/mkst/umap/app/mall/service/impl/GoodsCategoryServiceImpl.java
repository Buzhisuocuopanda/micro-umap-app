/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.GoodsCategory;
import com.mkst.umap.app.mall.common.entity.GoodsCategoryTree;
import com.mkst.umap.app.mall.common.util.TreeUtil;
import com.mkst.umap.app.mall.mapper.GoodsCategoryMapper;
import com.mkst.umap.app.mall.service.GoodsCategoryService;

import cn.hutool.core.bean.BeanUtil;

/**
 * 商品类目
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-12 11:46:28
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {

	@Override
	public List<Map<String, Object>> selectTree(GoodsCategory goodsCategory) {
		return getTrees(this.list(Wrappers.lambdaQuery(goodsCategory)), false, null);
	}

	/**
	 * 对象转树
	 *
	 * @param treeList 分组列表
	 * @param isCheck   是否需要选中
	 * @param checkList 已存在分组列表
	 * @return
	 */
	public List<Map<String, Object>> getTrees(List<GoodsCategory> treeList, boolean isCheck, List<String> checkList) {

		List<Map<String, Object>> trees = new ArrayList<Map<String, Object>>();
		for (GoodsCategory tree : treeList) {
			Map<String, Object> deptMap = new HashMap<String, Object>();
			deptMap.put("id", tree.getId());
			deptMap.put("pId", tree.getParentId());
			deptMap.put("name", tree.getName());
			deptMap.put("title", tree.getName());
			if (isCheck) {
				deptMap.put("checked", checkList.contains(tree.getId() + tree.getName()));
			} else {
				deptMap.put("checked", false);
			}
			trees.add(deptMap);
		}
		return trees;
	}
	
	/**
	 * 构建树
	 *
	 * @param entitys
	 * @return
	 */
	private List<GoodsCategoryTree> getTree(List<GoodsCategory> entitys) {
		List<GoodsCategoryTree> treeList = entitys.stream()
				.filter(entity -> !entity.getId().equals(entity.getParentId()))
				.sorted(Comparator.comparingInt(GoodsCategory::getSort))
				.map(entity -> {
					GoodsCategoryTree node = new GoodsCategoryTree();
					BeanUtil.copyProperties(entity,node);
					return node;
				}).collect(Collectors.toList());
		return TreeUtil.build(treeList, MallConstants.PARENT_ID);
	}

	@Override
	public boolean removeById(Serializable id) {
		super.removeById(id);
		remove(Wrappers.<GoodsCategory>query()
				.lambda().eq(GoodsCategory::getParentId, id));
		return true;
	}
}
