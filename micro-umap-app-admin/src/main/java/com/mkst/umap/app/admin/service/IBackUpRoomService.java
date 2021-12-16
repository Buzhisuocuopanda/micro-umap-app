package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.BackUpRoom;
import com.mkst.umap.app.admin.dto.apply.BackUpRoomDto;

import java.util.List;

/**
 * 备勤间 服务层
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
public interface IBackUpRoomService
{
	/**
     * 查询备勤间信息
     * 
     * @param roomId 备勤间ID
     * @return 备勤间信息
     */
	public BackUpRoom selectBackUpRoomById(Integer roomId);
	
	/**
     * 查询备勤间列表
     * 
     * @param backUpRoom 备勤间信息
     * @return 备勤间集合
     */
	public List<BackUpRoom> selectBackUpRoomList(BackUpRoom backUpRoom);

	/**
	 * 通过集合id查询房间集合信息
	 * @param ids
	 * @return
	 */
	List<BackUpRoom> selectRoomListByIds(List<Long> ids);

	/**
	 * 通过日期获取当天备勤间使用状态
	 * @param backUpRoomDto
	 * @return
	 */
	List<BackUpRoom> selectBackUpRoomByDate(BackUpRoomDto backUpRoomDto);
	
	/**
     * 新增备勤间
     * 
     * @param backUpRoom 备勤间信息
     * @return 结果
     */
	public int insertBackUpRoom(BackUpRoom backUpRoom);
	
	/**
     * 修改备勤间
     * 
     * @param backUpRoom 备勤间信息
     * @return 结果
     */
	public int updateBackUpRoom(BackUpRoom backUpRoom);
		
	/**
     * 删除备勤间信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBackUpRoomByIds(String ids);

	/**
	 * 逻辑删除备勤间信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteBackUpRoomByIdsUpdateDelFlag(String ids);

	/**
	 * 校验房间号是否唯一
	 *
	 * @param
	 * @return 结果
	 */
	public String checkRoomNumUnique(String roomNum);
	
}
