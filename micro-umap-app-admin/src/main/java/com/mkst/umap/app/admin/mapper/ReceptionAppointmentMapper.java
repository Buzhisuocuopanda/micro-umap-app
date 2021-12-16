package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionDetailResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult;
import com.mkst.umap.app.admin.domain.ReceptionAppointment;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 接待预约 数据层
 *
 * @author wangyong
 * @date 2020-07-08
 */
public interface ReceptionAppointmentMapper {
    /**
     * 查询接待预约信息
     *
     * @param id 接待预约ID
     * @return 接待预约信息
     */
    public ReceptionAppointment selectReceptionAppointmentById(Integer id);

    /**
     * 查询接待预约列表
     *
     * @param receptionAppointment 接待预约信息
     * @return 接待预约集合
     */
    public List<ReceptionAppointment> selectReceptionAppointmentList(ReceptionAppointment receptionAppointment);

    /**
     * 新增接待预约
     *
     * @param receptionAppointment 接待预约信息
     * @return 结果
     */
    public int insertReceptionAppointment(ReceptionAppointment receptionAppointment);

    /**
     * 修改接待预约
     *
     * @param receptionAppointment 接待预约信息
     * @return 结果
     */
    public int updateReceptionAppointment(ReceptionAppointment receptionAppointment);

    /**
     * 删除接待预约
     *
     * @param id 接待预约ID
     * @return 结果
     */
    public int deleteReceptionAppointmentById(Integer id);

    /**
     * 批量删除接待预约
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteReceptionAppointmentByIds(String[] ids);

    LinkedList<ReceptionInfoResult> getReceptionInfoResultList(ReceptionParam receptionParam);

    ReceptionDetailResult getReceptionDetailById(Long id);

    Date getNextTime(ReceptionParam receptionParam);

    int selectIsOccupied(ReceptionParam receptionParam);

    List<NameCountResult> selectDayCount(ReceptionParam param);

    List<TimeApplyResult> selectTimeApplyList(ReceptionParam param);
}