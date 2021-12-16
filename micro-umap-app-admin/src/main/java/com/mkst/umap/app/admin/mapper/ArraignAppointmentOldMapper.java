package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.ArraignAppointmentOld;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName ArraignAppointmentOld
 * @Description 提审预约老系统关联 数据层
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 上午 9:06
 */
@Repository
public interface ArraignAppointmentOldMapper {

    public ArraignAppointmentOld selectArraignAppointmentOldById(Long id);

    public List<ArraignAppointmentOld> selectAll();

    public void insert(ArraignAppointmentOld old);

    public void updateStatus(ArraignAppointmentOld old);

}
