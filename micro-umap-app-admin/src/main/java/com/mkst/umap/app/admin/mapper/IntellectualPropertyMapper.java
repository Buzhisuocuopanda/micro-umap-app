package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.IntellectualProperty;
import com.mkst.umap.app.admin.dto.intellectualproperty.IntelProInfoWebDto;

import java.util.List;

/**
 * 知识产权 数据层
 *
 * @author wangyong
 * @date 2020-08-12
 */
public interface IntellectualPropertyMapper {
    /**
     * 查询知识产权信息
     *
     * @param id 知识产权ID
     * @return 知识产权信息
     */
    public IntellectualProperty selectIntellectualPropertyById(Long id);

    /**
     * 查询知识产权列表
     *
     * @param intellectualProperty 知识产权信息
     * @return 知识产权集合
     */
    public List<IntellectualProperty> selectIntellectualPropertyList(IntellectualProperty intellectualProperty);

    /**
     * 新增知识产权
     *
     * @param intellectualProperty 知识产权信息
     * @return 结果
     */
    public int insertIntellectualProperty(IntellectualProperty intellectualProperty);

    /**
     * 修改知识产权
     *
     * @param intellectualProperty 知识产权信息
     * @return 结果
     */
    public int updateIntellectualProperty(IntellectualProperty intellectualProperty);

    /**
     * 删除知识产权
     *
     * @param id 知识产权ID
     * @return 结果
     */
    public int deleteIntellectualPropertyById(Long id);

    /**
     * 批量删除知识产权
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIntellectualPropertyByIds(String[] ids);

    List<IntelProInfoWebDto> selectWebInfo(IntellectualProperty intellectualProperty);
}