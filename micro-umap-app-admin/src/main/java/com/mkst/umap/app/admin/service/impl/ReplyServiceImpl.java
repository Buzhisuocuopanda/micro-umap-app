package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.mapper.ReplyMapper;
import com.mkst.umap.app.admin.service.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回复 服务层实现
 *
 * @author wangyong
 * @date 2020-08-12
 */
@Service
public class ReplyServiceImpl implements IReplyService {
	@Autowired
	private ReplyMapper replyMapper;

	/**
	 * 查询回复信息
	 *
	 * @param id 回复ID
	 * @return 回复信息
	 */
	@Override
	public Reply selectReplyById(Long id) {
		return replyMapper.selectReplyById(id);
	}

	/**
	 * 查询回复列表
	 *
	 * @param reply 回复信息
	 * @return 回复集合
	 */
	@Override
	public List<Reply> selectReplyList(Reply reply) {
		return replyMapper.selectReplyList(reply);
	}

	/**
	 * 新增回复
	 *
	 * @param reply 回复信息
	 * @return 结果
	 */
	@Override
	public int insertReply(Reply reply) {
		return replyMapper.insertReply(reply);
	}

	/**
	 * 修改回复
	 *
	 * @param reply 回复信息
	 * @return 结果
	 */
	@Override
	public int updateReply(Reply reply) {
		return replyMapper.updateReply(reply);
	}

	/**
	 * 删除回复对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteReplyByIds(String ids) {
		return replyMapper.deleteReplyByIds(Convert.toStrArray(ids));
	}

}
