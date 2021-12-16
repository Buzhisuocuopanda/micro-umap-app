package com.mkst.umap.app.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.domain.Petition;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.service.IReplyService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.ReportMapper;
import com.mkst.umap.app.admin.domain.Report;
import com.mkst.umap.app.admin.service.IReportService;
import com.mkst.mini.systemplus.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 随手拍/公益举报 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-27
 */
@Service
public class ReportServiceImpl implements IReportService 
{
	@Autowired
	private ReportMapper reportMapper;

	@Autowired
    private IReplyService replyService;

	/**
     * 查询随手拍/公益举报信息
     * 
     * @param id 随手拍/公益举报ID
     * @return 随手拍/公益举报信息
     */
    @Override
	public Report selectReportById(Long id)
	{
	    return reportMapper.selectReportById(id);
	}
	
	/**
     * 查询随手拍/公益举报列表
     * 
     * @param report 随手拍/公益举报信息
     * @return 随手拍/公益举报集合
     */
	@Override
	public List<Report> selectReportList(Report report)
	{
	    return reportMapper.selectReportList(report);
	}
	
    /**
     * 新增随手拍/公益举报
     * 
     * @param report 随手拍/公益举报信息
     * @return 结果
     */
	@Override
	public int insertReport(Report report)
	{
	    return reportMapper.insertReport(report);
	}
	
	/**
     * 修改随手拍/公益举报
     * 
     * @param report 随手拍/公益举报信息
     * @return 结果
     */
	@Override
	public int updateReport(Report report)
	{
	    return reportMapper.updateReport(report);
	}

	/**
     * 删除随手拍/公益举报对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteReportByIds(String ids)
	{
		return reportMapper.deleteReportByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<String> getFileBind(Long id) {
		ArrayList<String> resultList = new ArrayList<>();

		Report report = this.selectReportById(id);
		if (report == null){
			return resultList;
		}

		List<SysFileUpload> uploadList = FileUploadExtendUtils.findFileUpload(id.toString(), BusinessTypeEnum.UMAP_REPORT.getValue());

		if (CollectionUtil.isEmpty(uploadList)){
			return resultList;
		}

		uploadList.stream().forEach(fileUpload ->{
			fileUpload.getFileEntityList().stream().forEach(file -> {
				resultList.add(file.getFilePath());
			});
		});

		return resultList;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reply(Long id, String content, String loginName) {

        Report updateReport = new Report();
        updateReport.setId(id);
        updateReport.setHasReplied("1");
        reportMapper.updateReport(updateReport);

        Reply insertReply = new Reply();
        insertReply.setObjectId(id.toString());
        insertReply.setContent(content);
        insertReply.setBusinessType(BusinessTypeEnum.UMAP_REPORT.getValue());
        insertReply.setCreateBy(loginName);

        return replyService.insertReply(insertReply);
    }
}
