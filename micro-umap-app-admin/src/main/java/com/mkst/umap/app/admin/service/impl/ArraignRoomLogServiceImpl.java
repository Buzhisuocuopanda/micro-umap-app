package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.impl.SysUserServiceImpl;
import com.mkst.umap.app.admin.domain.ArraignRoomLog;
import com.mkst.umap.app.admin.mapper.ArraignRoomLogMapper;
import com.mkst.umap.app.admin.service.IArraignRoomLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 提审室操作日志 服务层实现
 * 
 * @author wangyong
 * @date 2020-09-21
 */
@Service
public class ArraignRoomLogServiceImpl implements IArraignRoomLogService 
{
	@Autowired
	private ArraignRoomLogMapper arraignRoomLogMapper;
	@Autowired
	SysUserServiceImpl sysUserService;

	/**
     * 查询提审室操作日志信息
     * 
     * @param id 提审室操作日志ID
     * @return 提审室操作日志信息
     */
    @Override
	public ArraignRoomLog selectArraignRoomLogById(Long id)
	{
	    return arraignRoomLogMapper.selectArraignRoomLogById(id);
	}
	
	/**
     * 查询提审室操作日志列表
     * 
     * @param arraignRoomLog 提审室操作日志信息
     * @return 提审室操作日志集合
     */
	@Override
	public List<ArraignRoomLog> selectArraignRoomLogList(ArraignRoomLog arraignRoomLog)
	{
	    return arraignRoomLogMapper.selectArraignRoomLogList(arraignRoomLog);
	}
	
    /**
     * 新增提审室操作日志
     * 
     * @param arraignRoomLog 提审室操作日志信息
     * @return 结果
     */
	@Override
	public int insertArraignRoomLog(ArraignRoomLog arraignRoomLog)
	{
	    return arraignRoomLogMapper.insertArraignRoomLog(arraignRoomLog);
	}
	
	/**
     * 修改提审室操作日志
     * 
     * @param arraignRoomLog 提审室操作日志信息
     * @return 结果
     */
	@Override
	public int updateArraignRoomLog(ArraignRoomLog arraignRoomLog)
	{
	    return arraignRoomLogMapper.updateArraignRoomLog(arraignRoomLog);
	}

	/**
     * 删除提审室操作日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteArraignRoomLogByIds(String ids)
	{
		return arraignRoomLogMapper.deleteArraignRoomLogByIds(Convert.toStrArray(ids));
	}


	@Override
	@Transactional
	public String importArraignRoomLog(String id,List<ArraignRoomLog> arraignRoomLogList) {

		if (StringUtils.isNull(arraignRoomLogList) || arraignRoomLogList.size() == 0)
		{
			throw new RuntimeException("导入日志数据不能为空！");
		}
		//成功数量
		int successNum = 0;
		//失败数量
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		for (ArraignRoomLog arraignRoomLog : arraignRoomLogList) {
			if(StringUtils.isBlank(arraignRoomLog.getName())){
				continue;
			}
			SysUser sysUser = new SysUser();
			try {
				sysUser = sysUserService.selectUserByUserName(StringUtils.trim(arraignRoomLog.getName()));
			}catch (Exception e){
				e.printStackTrace();
			}
			if(sysUser == null || StringUtils.isEmpty(sysUser.getLoginName())){
				failureNum++;
				failureMsg.append("<br/>" + failureNum + "、检察官 " + arraignRoomLog.getName() + " 不存在");
				throw new RuntimeException(failureMsg.toString());
			}
			arraignRoomLog.setCreateTime(new Date());
			arraignRoomLog.setRoomId(id);
			arraignRoomLog.setCreateBy(ShiroUtils.getLoginName());
			arraignRoomLogMapper.insertArraignRoomLog(arraignRoomLog);
			successNum++;
			successMsg.append("<br/>" + successNum + "用户："  + arraignRoomLog.getName()+" 开始时间： "+ arraignRoomLog.getStartTime() + " 日志导入成功");
		}
		if (failureNum > 0)
		{
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new RuntimeException(failureMsg.toString());
		}
		else
		{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}
	
}
