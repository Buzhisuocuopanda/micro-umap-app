package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.Reply;

import java.util.List;

/**
 * 回复 数据层
 *
 * @author wangyong
 * @date 2020-08-12
 */
public interface ReplyMapper {
    /**
     * 查询回复信息
     *
     * @param id 回复ID
     * @return 回复信息
     */
    public Reply selectReplyById(Long id);

    /**
     * 查询回复列表
     *
     * @param reply 回复信息
     * @return 回复集合
     */
    public List<Reply> selectReplyList(Reply reply);

    /**
     * 新增回复
     *
     * @param reply 回复信息
     * @return 结果
     */
    public int insertReply(Reply reply);

    /**
     * 修改回复
     *
     * @param reply 回复信息
     * @return 结果
     */
    public int updateReply(Reply reply);

    /**
     * 删除回复
     *
     * @param id 回复ID
     * @return 结果
     */
    public int deleteReplyById(Long id);

    /**
     * 批量删除回复
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteReplyByIds(String[] ids);

}