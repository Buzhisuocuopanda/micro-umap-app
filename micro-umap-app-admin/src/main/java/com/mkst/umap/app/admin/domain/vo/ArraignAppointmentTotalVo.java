package com.mkst.umap.app.admin.domain.vo;

import com.mkst.umap.app.admin.dto.arraign.ArraignAppointmentTotalDto;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ArraignAppointmentTotalVo {

    private List<ArraignAppointmentTotalDto> roomList;
    private List<ArraignAppointmentTotalDto> criminalTypeList;

    //折线图  时间段的list  和 对应时间段 提审预约数
    private List<String> timeIntervals;
    private List<Integer> timeTotals;
    private List<String> criminalTypeAndDeptData;
    private Map<String,List<Integer>> mapList;

    //柱形图  犯罪类型list  和 对应的 总数
    private List<Integer> criminalTypeTotals;
    private List<String> criminalTypeNames;

    //饼图  提审室list 和对应总数
    private List<Integer> roomTotals;
    private List<String> roomNames;

}
