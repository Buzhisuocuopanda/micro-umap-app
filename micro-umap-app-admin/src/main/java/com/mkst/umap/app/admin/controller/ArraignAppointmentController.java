package com.mkst.umap.app.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.exception.BusinessException;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.system.domain.SysDept;
import com.mkst.mini.systemplus.system.domain.SysDictData;
import com.mkst.mini.systemplus.system.service.ISysDeptService;
import com.mkst.mini.systemplus.system.service.ISysDictDataService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.domain.ArraignAppointment;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.vo.ArraignAppointmentTotalVo;
import com.mkst.umap.app.admin.dto.arraign.*;
import com.mkst.umap.app.admin.service.IArraignAppointmentService;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.util.ArraignAppointmentExcel;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.base.core.util.UmapDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 提审预约 信息操作处理
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Controller
@Slf4j
@RequestMapping("/admin/arraignAppointment")
public class ArraignAppointmentController extends BaseController {
    private static Integer HOURS = 6 * 10;
    private static String CRIMINAL_TYPE = "criminal_type";
    private String prefix = "app/arraign/arraignAppointment";
    @Autowired
    private IArraignAppointmentService arraignAppointmentService;
    @Autowired
    private IArraignRoomService arraignRoomService;
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IAuditRecordService auditRecordService;

    @RequiresPermissions("admin:arraignAppointment:view")
    @GetMapping()
    public String arraignAppointment() {
        return prefix + "/arraignAppointment";
    }

    @RequiresPermissions("admin:arraignAppointment:view")
    @GetMapping("/total")
    public String total() {
        return "app/arraign/total/total";
    }

    @RequiresPermissions("admin:arraignAppointment:view")
    @GetMapping("/audit")
    public String audit() {
        return prefix + "/audit";
    }

    @GetMapping({"/selectDeptTree/{deptId}"})
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        mmap.put("dept", this.deptService.selectDeptById(deptId));
        return "app/arraign/report/tree";
    }

    @RequiresPermissions({"dmin:arraignAppointment:view"})
    @GetMapping({"/reportPage"})
    public String reportPage(String reportType) {
        return "app/arraign/report/" + reportType;
    }

    @RequiresPermissions("admin:arraignAppointment:view")
    @GetMapping("/exportMenu")
    public String exportMenu() {
        return prefix + "/export";
    }

    @GetMapping({"/treeData"})
    @ResponseBody
    public List<Map<String, Object>> treeData() {
        List<Map<String, Object>> tree = deptService.selectDeptTree(new SysDept());
        for (Map<String, Object> map : tree) {
            if ((Long) map.get("id") == 100L) {
                tree.remove(map);
                break;
            }
        }
        return tree;
    }

    /**
     * 统计提审预约信息
     */
    @RequiresPermissions("admin:arraignAppointment:view")
    @PostMapping("/totalList")
    @ResponseBody
    public AjaxResult totalList(ReportDto dto) throws ParseException {
        if (!"admin".equals(ShiroUtils.getLoginName())) {
            dto.setCreateBy(ShiroUtils.getLoginName());
        }
        ArraignAppointmentTotalVo arraignAppointmentTotalVo = new ArraignAppointmentTotalVo();
        List<String> criminalTypeAndDeptData = new LinkedList<>();
        List<String> criminalTypeList = new LinkedList<>();
        List<String> timeIntervals = new LinkedList<>();
        Map<String, List<Integer>> map = new HashMap();
        List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType(CRIMINAL_TYPE);
        for (SysDictData dictData : dictDataList) {
            criminalTypeAndDeptData.add(dictData.getDictLabel());
            criminalTypeList.add(dictData.getDictValue());
        }
        dto.setCriminalTypeList(criminalTypeList);

        String regEx = "[^0-9]";
        List<CriminalTypeResultDto> list = new ArrayList<>();
        if ("season".equals(dto.getTotalType())) {
            list = arraignAppointmentService.selectSeasonGroupByCriminalType(dto);
            for (CriminalTypeResultDto r : list) {
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(r.getCDateType());
                String s = m.replaceAll("").trim().substring(0, 4);
                String season = m.replaceAll("").trim().substring(4, 5);
                r.setCDateType(s + "年第" + season + "季");
            }
        } else {
            if ("today".equals(dto.getTotalType())) {
                dto.setDateType("%Y年%m月%d日");
            } else if ("week".equals(dto.getTotalType())) {
                dto.setDateType("%Y年第%u周");
            } else if ("month".equals(dto.getTotalType())) {
                dto.setDateType("%Y年%m月");
            } else if ("year".equals(dto.getTotalType())) {
                dto.setDateType("%Y年");
            } else {
                dto.setDateType("%Y年%m月%d日");
            }
            list = arraignAppointmentService.selectGroupByCriminalType(dto);
        }

        List<Integer> timeTotals1 = new LinkedList<>();
        List<Integer> timeTotals2 = new LinkedList<>();
        List<Integer> timeTotals3 = new LinkedList<>();
        List<Integer> timeTotals4 = new LinkedList<>();
        List<Integer> timeTotals5 = new LinkedList<>();
        List<Integer> timeTotals6 = new LinkedList<>();
        List<Integer> timeTotals7 = new LinkedList<>();
        List<Integer> timeTotals8 = new LinkedList<>();
        List<Integer> timeTotals9 = new LinkedList<>();
        List<Integer> timeTotals10 = new LinkedList<>();
        List<Integer> timeTotals11 = new LinkedList<>();
        List<Integer> timeTotals12 = new LinkedList<>();
        List<Integer> timeTotals13 = new LinkedList<>();
        List<Integer> timeTotals14 = new LinkedList<>();
        List<Integer> timeTotals15 = new LinkedList<>();
        List<Integer> timeTotals16 = new LinkedList<>();
        List<Integer> timeTotals17 = new LinkedList<>();
        List<Integer> timeTotals18 = new LinkedList<>();
        List<Integer> timeTotals19 = new LinkedList<>();
        List<Integer> timeTotals20 = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            timeIntervals.add(list.get(i).getCDateType());
            timeTotals1.add(list.get(i).getType0());
            timeTotals2.add(list.get(i).getType1());
            timeTotals3.add(list.get(i).getType2());
            timeTotals4.add(list.get(i).getType3());
            timeTotals5.add(list.get(i).getType4());
            timeTotals6.add(list.get(i).getType5());
            timeTotals7.add(list.get(i).getType6());
            timeTotals8.add(list.get(i).getType7());
            timeTotals9.add(list.get(i).getType8());
            timeTotals10.add(list.get(i).getType9());
            timeTotals11.add(list.get(i).getType10());
            timeTotals12.add(list.get(i).getType11());
            timeTotals13.add(list.get(i).getType12());
            timeTotals14.add(list.get(i).getType13());
            timeTotals15.add(list.get(i).getType14());
            timeTotals16.add(list.get(i).getType15());
            timeTotals17.add(list.get(i).getType16());
            timeTotals18.add(list.get(i).getType17());
            timeTotals19.add(list.get(i).getType18());
            timeTotals20.add(list.get(i).getType19());
        }

        map.put(criminalTypeAndDeptData.get(0), timeTotals1);
        map.put(criminalTypeAndDeptData.get(1), timeTotals2);
        map.put(criminalTypeAndDeptData.get(2), timeTotals3);
        map.put(criminalTypeAndDeptData.get(3), timeTotals4);
        map.put(criminalTypeAndDeptData.get(4), timeTotals5);
        map.put(criminalTypeAndDeptData.get(5), timeTotals6);
        map.put(criminalTypeAndDeptData.get(6), timeTotals7);
        map.put(criminalTypeAndDeptData.get(7), timeTotals8);
        map.put(criminalTypeAndDeptData.get(8), timeTotals9);
        map.put(criminalTypeAndDeptData.get(9), timeTotals10);
        map.put(criminalTypeAndDeptData.get(10), timeTotals11);
        map.put(criminalTypeAndDeptData.get(11), timeTotals12);
        map.put(criminalTypeAndDeptData.get(12), timeTotals13);
        map.put(criminalTypeAndDeptData.get(13), timeTotals14);
        map.put(criminalTypeAndDeptData.get(14), timeTotals15);
        map.put(criminalTypeAndDeptData.get(15), timeTotals16);
        map.put(criminalTypeAndDeptData.get(16), timeTotals17);
        map.put(criminalTypeAndDeptData.get(17), timeTotals18);
        map.put(criminalTypeAndDeptData.get(18), timeTotals19);
        map.put(criminalTypeAndDeptData.get(19), timeTotals20);

        arraignAppointmentTotalVo.setTimeIntervals(timeIntervals);

        arraignAppointmentTotalVo.setMapList(map);
        arraignAppointmentTotalVo.setCriminalTypeAndDeptData(criminalTypeAndDeptData);
        return AjaxResult.success(arraignAppointmentTotalVo);
    }


    @PostMapping("/arraignAppointment30Day")
    @ResponseBody
    public AjaxResult arraignAppointment30Day(ReportDto dto) throws ParseException {
        if (!"admin".equals(ShiroUtils.getLoginName())) {
            dto.setCreateBy(ShiroUtils.getLoginName());
        }
        ArraignAppointmentTotalVo arraignAppointmentTotalVo = new ArraignAppointmentTotalVo();

        List<String> criminalTypeAndDictData = new LinkedList<>();
        List<String> criminalTypeList = new LinkedList<>();
        List<String> timeIntervals = new LinkedList<>();
        Map<String, List<Integer>> map = new HashMap();
        List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType(CRIMINAL_TYPE);
        for (SysDictData dictData : dictDataList) {
            criminalTypeAndDictData.add(dictData.getDictLabel());
            criminalTypeList.add(dictData.getDictValue());
        }
        dto.setCriminalTypeList(criminalTypeList);
        List<String> dayList = DateUtils.getDays(DateUtils.getThirtyDay(), DateUtils.getDate());
        List<CriminalTypeResultDto> list = new ArrayList<>();
        dto.setDateType("%Y-%m-%d");
        list = arraignAppointmentService.select30Day(dto);

        Map<String, CriminalTypeResultDto> mapList = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            mapList.put(list.get(i).getCDateType(), list.get(i));
        }
        arraignAppointmentTotalVo = assembleTotalVo(dayList, mapList, dayList, criminalTypeAndDictData, map, arraignAppointmentTotalVo);
        return AjaxResult.success(arraignAppointmentTotalVo);
    }

    /**
     * 查询提审预约列表
     */
    @RequiresPermissions("admin:arraignAppointment:list")
    @PostMapping("/dtoList")
    @ResponseBody
    public TableDataInfo list(ArraignAppointmentTotalDto dto) {
        startPage();
        if (!"admin".equals(ShiroUtils.getLoginName())) {
            dto.setCreateBy(ShiroUtils.getLoginName());
        }
        List<ArraignAppointment> list = arraignAppointmentService.selectArraignAppointmentListByDto(dto);
        return getDataTable(list);
    }

    /**
     * 查询提审预约列表
     */
    @RequiresPermissions("admin:arraignAppointment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ArraignAppointment arraignAppointment) {
        AppointmentDetailDto appointmentDetailDto = new AppointmentDetailDto();
        BeanUtil.copyProperties(arraignAppointment, appointmentDetailDto);
        startPage();
        List<AppointmentDetailDto> list = arraignAppointmentService.getAppointmentDetailList(appointmentDetailDto);
        return getDataTable(list);
    }

    /**
     * 查询提审预约审核列表
     */
    @RequiresPermissions("admin:arraignAppointment:list")
    @PostMapping("/auditList")
    @ResponseBody
    public TableDataInfo auditList(ArraignAuditDto arraignAuditDto) {
        startPage();
        arraignAuditDto.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
        List<ArraignAuditDto> list = arraignAppointmentService.selectArraignAppointmentAuditList(arraignAuditDto);
        return getDataTable(list);
    }

    /**
     * 提交审核
     */
    @PostMapping("/doAudit/{id}/{status}/{reason}")
    @RequiresPermissions("admin:arraignAppointment:audit")
    @Log(title = "提审预约审核", businessType = BusinessType.UPDATE)
    @ResponseBody
    public AjaxResult doAudit(@PathVariable Long id, @PathVariable String status, @PathVariable String reason) {
        return toAjax(arraignAppointmentService.audit(id, status, reason));
    }

    /**
     * 周月年季报
     */
    @RequiresPermissions("admin:arraignAppointment:list")
    @PostMapping("/reportList")
    @ResponseBody
    public TableDataInfo reportList(ReportDto dto) {
        startPage();
        if (!"admin".equals(ShiroUtils.getLoginName())) {
            dto.setCreateBy(ShiroUtils.getLoginName());
        }
        String regEx = "[^0-9]";
        List<ReportDto> list = typeChoose(dto, regEx);
        return getDataTable(list);
    }


    /**
     * 导出提审预约列表
     */
    @RequiresPermissions("admin:arraignAppointment:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ArraignAppointmentTotalDto dto) throws Exception {
        //dto.setCreateBy(ShiroUtils.getLoginName());
        List<ArraignAppointment> list = arraignAppointmentService.selectArraignAppointmentListByDto(dto);
        ExcelUtil<ArraignAppointment> util = new ExcelUtil<ArraignAppointment>(ArraignAppointment.class);
        StringBuilder str = new StringBuilder("-");
        str = reflect(dto, str);
        str.append("提审统计表");
        return util.exportExcel(list, str.toString());
    }

    public StringBuilder reflect(ArraignAppointmentTotalDto e, StringBuilder str) throws Exception {
        Class cls = e.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            if ("serialVersionUID".equals(f.getName())) {
                continue;
            }
            //System.out.println("属性名:" + f.getName() + " 属性值:" + f.get(e));
            if (f.get(e) != null && f.get(e) != "") {
                if ("class java.util.Date".equals(f.getGenericType().toString())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    str.append(sdf.format(f.get(e))).append("-");
                } else {
                    str.append(f.get(e).toString()).append("-");
                }
            }
        }
        return str;
    }

    /**
     * 导出 周 月 季 年 的统计报表Excel
     *
     * @param response
     * @param dto
     */
    @RequestMapping("/statisticalReports")
    public void statisticalReports(HttpServletResponse response, ReportDto dto) {
        String filename = encodingFilename("统计报表");
        ArraignAppointmentExcel excel = new ArraignAppointmentExcel();
        HSSFWorkbook workbook;
        String regEx = "[^0-9]";
        List<ReportDto> list = typeChoose(dto, regEx);
        for (ReportDto r : list) {
            String criminalType = sysDictDataService.selectDictLabel(CRIMINAL_TYPE, r.getCriminalType());
            r.setCriminalType(criminalType);
        }
        workbook = excel.Excels(filename, list);
        writeStream(response, filename, workbook);
    }

    /**
     * 通过类型 给日期类型赋不同的日期格式用于sql语句
     *
     * @param dto
     * @param regEx
     * @return
     */
    public List<ReportDto> typeChoose(ReportDto dto, String regEx) {
        List<ReportDto> list;
        if ("season".equals(dto.getTotalType())) {
            list = arraignAppointmentService.selectSeasonReport(dto);
            for (ReportDto r : list) {
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(r.getDateFormat());
                String s = m.replaceAll("").trim().substring(0, 4);
                String season = m.replaceAll("").trim().substring(4, 5);
                r.setDateFormat(s + "年第" + season + "季");
            }
        } else {
            if ("today".equals(dto.getTotalType())) {
                dto.setDateType("%Y年%m月%d日");
            } else if ("week".equals(dto.getTotalType())) {
                dto.setDateType("%Y年第%u周");
            } else if ("month".equals(dto.getTotalType())) {
                dto.setDateType("%Y年%m月");
            } else if ("year".equals(dto.getTotalType())) {
                dto.setDateType("%Y年");
            } else {
                dto.setDateType("%Y年%m月%d日");
            }
            list = arraignAppointmentService.selectReport(dto);
        }
        return list;
    }

    /**
     * 导出指定模板Excel接口
     *
     * @param response
     */
    @RequiresPermissions("admin:arraignAppointment:export")
    @RequestMapping("/exportTotal")
    public void exportTotal(HttpServletResponse response, ArraignAppointment arraignAppointment) {
        String filename = encodingFilename("视频提审人员名单");
        HSSFWorkbook workbook = null;

        //拿出数据组装成list
        //ArraignAppointment arraignAppointment = new ArraignAppointment();
        arraignAppointment.setDelFlag("0");
//        if (!"admin".equals(ShiroUtils.getLoginName())) {
//            arraignAppointment.setCreateBy(ShiroUtils.getLoginName());
//        }
        if ("undefined".equals(arraignAppointment.getStatus())){
            arraignAppointment.setStatus("");
        }
        if ("undefined".equals(arraignAppointment.getProsecutorId())){
            arraignAppointment.setProsecutorId("");
        }
        List<ArraignAppointment> list = arraignAppointmentService.selectArraignAppointmentList(arraignAppointment);
        workbook = AssembleExcel(list);
        writeStream(response, filename, workbook);
    }

    /**
     * 配置response配置
     * 将excel写入到输出流
     *
     * @param response
     * @param filename
     * @param workbook
     */
    public void writeStream(HttpServletResponse response, String filename, HSSFWorkbook workbook) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
    }

    /**
     * 通过list数据 组装excel
     *
     * @param list
     * @return
     */
    public HSSFWorkbook AssembleExcel(List<ArraignAppointment> list) {
        try {
            List<Map> afternoon = new ArrayList<Map>();
            List<Map> morning = new ArrayList<Map>();
            Map<String, List<Map>> mapList = new HashMap<>();
            int morningIndex = 1;

            String[] headNames = {"序号", "犯罪嫌疑人", "出生日期", "案由", "检察官", "检察官证件号", "阶段", "认罪认罚"};
            String[] keys = {"id", "criminalName", "criminalBirth", "criminalType", "prosecutorName", "prosecutorId", "stage", "criminalAdmit"};
            for (ArraignAppointment appointment : list) {
                LinkedHashMap<String, Object> e = new LinkedHashMap<String, Object>();
                SimpleDateFormat df = new SimpleDateFormat("HH");

                e.put("criminalName", appointment.getCriminalName());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String criminalBirth = null;
                if (appointment.getCriminalBirth() != null) {
                    criminalBirth = format.format(appointment.getCriminalBirth());
                }
                e.put("criminalBirth", criminalBirth);


                e.put("criminalType", sysDictDataService.selectDictLabel(CRIMINAL_TYPE, appointment.getCriminalType()));
                e.put("prosecutorName", appointment.getProsecutorName());
                e.put("prosecutorId", appointment.getProsecutorId());

                String stage = sysDictDataService.selectDictLabel("case_stage", appointment.getStage());
                e.put("stage", stage);

                String criminalAdmit = sysDictDataService.selectDictLabel("sys_yes_no", appointment.getCriminalAdmit());
                e.put("criminalAdmit", criminalAdmit);
                int time = Integer.parseInt(df.format(appointment.getStartTime()));
                if (time >= 0 && time <= 12) {
                    e.put("id", morningIndex++);
                    morning.add(e);
                } else {
                    afternoon.add(e);
                }
            }
            int afternoonIndex = morning.size() + 1;
            for (int j = 0; j < afternoon.size(); j++) {
                afternoon.get(j).put("id", afternoonIndex);
                afternoonIndex++;
            }
            mapList.put("morning", morning);
            mapList.put("afternoon", afternoon);
            int colWidths[] = {40, 100, 100, 85, 120, 150, 60, 80};

            //将数据生成输入流
            ArraignAppointmentExcel arraignAppointmentExcel = new ArraignAppointmentExcel();
            return arraignAppointmentExcel.getExcelFile(mapList, "视频提审人员名单", headNames, keys, colWidths, DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
    }


    public String encodingFilename(String filename) {
        filename = filename + ".xls";
        return filename;
    }

    /**
     * 新增提审预约
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存提审预约
     */
    @RequiresPermissions("admin:arraignAppointment:add")
    @Log(title = "提审预约", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ArraignAppointment arraignAppointment) {
        return toAjax(arraignAppointmentService.insertArraignAppointment(arraignAppointment));
    }

    /**
     * 修改提审预约
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        ArraignAppointment arraignAppointment = arraignAppointmentService.selectArraignAppointmentById(id);
        mmap.put("arraignAppointment", arraignAppointment);
        return prefix + "/edit";
    }

    /**
     * @return
     * @Author wangyong
     * @Description shoe detail
     * @Date 18:06 2020-06-30
     * @Param
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        AppointmentDetailDto appointmentDetail = arraignAppointmentService.getAppointmentDetailById(id);
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setApplyId(id);
        auditRecord.setApplyType(AuditRecordTypeEnum.ArraignAudit.getValue());
        List<AuditRecord> auditRecords = auditRecordService.selectAuditRecordList(auditRecord);
        mmap.put("auditRecords", auditRecords);
        mmap.put("arraignAppointment", appointmentDetail);
        return prefix + "/detail";
    }

    /**
     * 修改保存提审预约
     */
    @RequiresPermissions("admin:arraignAppointment:edit")
    @Log(title = "提审预约", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ArraignAppointment arraignAppointment) {
        return toAjax(arraignAppointmentService.updateArraignAppointment(arraignAppointment));
    }

    /**
     * 删除提审预约
     */
    @RequiresPermissions("admin:arraignAppointment:remove")
    @Log(title = "提审预约", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(arraignAppointmentService.deleteArraignAppointmentByIds(ids));
    }

    /**
     * 查询近30天前五部门提审数量
     */
    @PostMapping("/selectDeptFive30Day")
    @ResponseBody
    public TableDataInfo selectDeptFive30Day() {
        startPage();
        List<CountDto> list = arraignAppointmentService.selectDeptFive30Day();
        list = list.subList(0, 5);
        return getDataTable(list);
    }


    @PostMapping("/selectActiveUsers")
    @ResponseBody
    public AjaxResult selectActiveUsers(Integer dateType) {
        List<String> dayList = new ArrayList<>();
        if (dateType.equals(1)) {
            dayList = DateUtils.getDays(UmapDateUtils.getSevenDay(), DateUtils.getDate());

        } else {
            dayList = DateUtils.getDays(DateUtils.getThirtyDay(), DateUtils.getDate());
        }
        List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();

        for (String date : dayList) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<String> list = arraignAppointmentService.selectActiveUsers(date);
            list = list.stream().distinct().collect(Collectors.toList());
            map.put("date", date);
            map.put("total", list.size());
            dataMapList.add(map);
        }
        return AjaxResult.success(dataMapList);
    }

    @PostMapping("/newArraignAppointmentReport")
    @ResponseBody
    public AjaxResult newArraignAppointmentReport(ReportDto dto) throws ParseException {
        if (!"admin".equals(ShiroUtils.getLoginName())) {
            dto.setCreateBy(ShiroUtils.getLoginName());
        }
        ArraignAppointmentTotalVo arraignAppointmentTotalVo = new ArraignAppointmentTotalVo();
        List<String> criminalTypeAndDeptData = new LinkedList<>();
        List<String> criminalTypeList = new LinkedList<>();
        List<String> timeIntervals = new LinkedList<>();
        Map<String, List<Integer>> map = new HashMap();
        List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType(CRIMINAL_TYPE);
        for (SysDictData dictData : dictDataList) {
            criminalTypeAndDeptData.add(dictData.getDictLabel());
            criminalTypeList.add(dictData.getDictValue());
        }
        dto.setCriminalTypeList(criminalTypeList);

        String regEx = "[^0-9]";
        List<CriminalTypeResultDto> list = new ArrayList<>();
        if ("season".equals(dto.getTotalType())) {
            list = arraignAppointmentService.selectSeasonGroupByCriminalType(dto);
            for (CriminalTypeResultDto r : list) {
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(r.getCDateType());
                String s = m.replaceAll("").trim().substring(0, 4);
                String season = m.replaceAll("").trim().substring(4, 5);
                r.setCDateType(s + "年第" + season + "季");
            }
        } else {
            if ("today".equals(dto.getTotalType())) {
                dto.setDateType("%Y年%m月%d日");
            } else if ("week".equals(dto.getTotalType())) {
                dto.setDateType("%Y年第%u周");
            } else if ("month".equals(dto.getTotalType())) {
                timeIntervals = UmapDateUtils.getTwelveMonth(6);
                dto.setDateType("%Y年%m月");
            } else if ("year".equals(dto.getTotalType())) {
                timeIntervals = UmapDateUtils.getYear(6);
                dto.setDateType("%Y年");
            } else {
                dto.setDateType("%Y年%m月%d日");
            }
            list = arraignAppointmentService.selectGroupByCriminalType(dto);
        }

        Map<String, CriminalTypeResultDto> mapList = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            mapList.put(list.get(i).getCDateType(), list.get(i));
        }
        arraignAppointmentTotalVo = assembleTotalVo(timeIntervals, mapList, timeIntervals, criminalTypeAndDeptData, map, arraignAppointmentTotalVo);
        return AjaxResult.success(arraignAppointmentTotalVo);
    }

    private ArraignAppointmentTotalVo assembleTotalVo(List<String> dayList, Map<String, CriminalTypeResultDto> mapList, List<String> timeIntervals,
                                                      List<String> criminalTypeAndDictData, Map<String, List<Integer>> map,
                                                      ArraignAppointmentTotalVo arraignAppointmentTotalVo) {
        List<Integer> timeTotals1 = new LinkedList<>();
        List<Integer> timeTotals2 = new LinkedList<>();
        List<Integer> timeTotals3 = new LinkedList<>();
        List<Integer> timeTotals4 = new LinkedList<>();
        List<Integer> timeTotals5 = new LinkedList<>();
        List<Integer> timeTotals6 = new LinkedList<>();
        List<Integer> timeTotals7 = new LinkedList<>();
        List<Integer> timeTotals8 = new LinkedList<>();
        List<Integer> timeTotals9 = new LinkedList<>();
        List<Integer> timeTotals10 = new LinkedList<>();
        List<Integer> timeTotals11 = new LinkedList<>();
        List<Integer> timeTotals12 = new LinkedList<>();
        List<Integer> timeTotals13 = new LinkedList<>();
        List<Integer> timeTotals14 = new LinkedList<>();
        List<Integer> timeTotals15 = new LinkedList<>();
        List<Integer> timeTotals16 = new LinkedList<>();
        List<Integer> timeTotals17 = new LinkedList<>();
        List<Integer> timeTotals18 = new LinkedList<>();
        List<Integer> timeTotals19 = new LinkedList<>();
        List<Integer> timeTotals20 = new LinkedList<>();
        for (String day : timeIntervals) {
            if (mapList.get(day) != null) {
                timeTotals1.add(mapList.get(day).getType0());
                timeTotals2.add(mapList.get(day).getType1());
                timeTotals3.add(mapList.get(day).getType2());
                timeTotals4.add(mapList.get(day).getType3());
                timeTotals5.add(mapList.get(day).getType4());
                timeTotals6.add(mapList.get(day).getType5());
                timeTotals7.add(mapList.get(day).getType6());
                timeTotals8.add(mapList.get(day).getType7());
                timeTotals9.add(mapList.get(day).getType8());
                timeTotals10.add(mapList.get(day).getType9());
                timeTotals11.add(mapList.get(day).getType10());
                timeTotals12.add(mapList.get(day).getType11());
                timeTotals13.add(mapList.get(day).getType12());
                timeTotals14.add(mapList.get(day).getType13());
                timeTotals15.add(mapList.get(day).getType14());
                timeTotals16.add(mapList.get(day).getType15());
                timeTotals17.add(mapList.get(day).getType16());
                timeTotals18.add(mapList.get(day).getType17());
                timeTotals19.add(mapList.get(day).getType18());
                timeTotals20.add(mapList.get(day).getType19());
            } else {
                //timeIntervals.add(day);
                timeTotals1.add(0);
                timeTotals2.add(0);
                timeTotals3.add(0);
                timeTotals4.add(0);
                timeTotals5.add(0);
                timeTotals6.add(0);
                timeTotals7.add(0);
                timeTotals8.add(0);
                timeTotals9.add(0);
                timeTotals10.add(0);
                timeTotals11.add(0);
                timeTotals12.add(0);
                timeTotals13.add(0);
                timeTotals14.add(0);
                timeTotals15.add(0);
                timeTotals16.add(0);
                timeTotals17.add(0);
                timeTotals18.add(0);
                timeTotals19.add(0);
                timeTotals20.add(0);
            }
        }

        map.put(criminalTypeAndDictData.get(0), timeTotals1);
        map.put(criminalTypeAndDictData.get(1), timeTotals2);
        map.put(criminalTypeAndDictData.get(2), timeTotals3);
        map.put(criminalTypeAndDictData.get(3), timeTotals4);
        map.put(criminalTypeAndDictData.get(4), timeTotals5);
        map.put(criminalTypeAndDictData.get(5), timeTotals6);
        map.put(criminalTypeAndDictData.get(6), timeTotals7);
        map.put(criminalTypeAndDictData.get(7), timeTotals8);
        map.put(criminalTypeAndDictData.get(8), timeTotals9);
        map.put(criminalTypeAndDictData.get(9), timeTotals10);
        map.put(criminalTypeAndDictData.get(10), timeTotals11);
        map.put(criminalTypeAndDictData.get(11), timeTotals12);
        map.put(criminalTypeAndDictData.get(12), timeTotals13);
        map.put(criminalTypeAndDictData.get(13), timeTotals14);
        map.put(criminalTypeAndDictData.get(14), timeTotals15);
        map.put(criminalTypeAndDictData.get(15), timeTotals16);
        map.put(criminalTypeAndDictData.get(16), timeTotals17);
        map.put(criminalTypeAndDictData.get(17), timeTotals18);
        map.put(criminalTypeAndDictData.get(18), timeTotals19);
        map.put(criminalTypeAndDictData.get(19), timeTotals20);


        arraignAppointmentTotalVo.setTimeIntervals(dayList);
        arraignAppointmentTotalVo.setMapList(map);
        arraignAppointmentTotalVo.setCriminalTypeAndDeptData(criminalTypeAndDictData);
        return arraignAppointmentTotalVo;
    }

}
