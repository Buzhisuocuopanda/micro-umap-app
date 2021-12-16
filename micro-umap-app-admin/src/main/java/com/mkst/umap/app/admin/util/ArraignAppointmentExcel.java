package com.mkst.umap.app.admin.util;

import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.framework.util.SpringUtils;
import com.mkst.mini.systemplus.spider.util.DateUtil;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.mini.systemplus.system.service.impl.SysConfigServiceImpl;
import com.mkst.umap.app.admin.dto.arraign.ReportDto;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ArraignAppointmentExcel {

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private String bDate;
    private int year;

    private ISysConfigService configService = SpringUtils.getBean(SysConfigServiceImpl.class);

    /**
     * 创建行元素
     * @param style    样式
     * @param height   行高
     * @param value    行显示的内容
     * @param row1     起始行
     * @param row2     结束行
     * @param col1     起始列
     * @param col2     结束列
     */
    public void createRow(HSSFCellStyle style, int height, String value, int row1, int row2, int col1, int col2){

        sheet.addMergedRegion(new CellRangeAddress(row1, row2, col1, col2));  //设置从第row1行合并到第row2行，第col1列合并到col2列
        HSSFRow rows = sheet.createRow(row1);        //设置第几行
        rows.setHeight((short) height);//设置行高
        HSSFCell cell = rows.createCell(col1);       //设置内容开始的列
        style.setLocked(true);
        cell.setCellStyle(style);                    //设置样式
        cell.setCellValue(new HSSFRichTextString(value));                    //设置该行的值
    }

    /**
     * 创建样式
     * @param fontSize   字体大小
     * @param align  水平位置  左右居中2 居右3 默认居左 垂直均为居中
     * @param bold   是否加粗
     * @return
     */
    public HSSFCellStyle getStyle(int fontSize,int align,boolean bold,boolean border){
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) fontSize);// 字体大小
        if (bold){
            font.setBold(true);
        }
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);                         //设置字体
        style.setAlignment(HorizontalAlignment.forInt((short) align));          // 左右居中2 居右3 默认居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中1
        if (border){
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setLocked(true);
        }
        return style;
    }

    /**
     * 根据数据集生成Excel，并返回Excel文件流
     * @param mapList 数据集
     * @param sheetName Excel中sheet单元名称
     * @param headNames 列表头名称数组
     * @param colKeys 列key,数据集根据该key进行按顺序取值
     * @return
     * @throws IOException
     */
    public HSSFWorkbook getExcelFile(Map<String,List<Map>> mapList, String sheetName, String[] headNames,
                                    String[] colKeys, int colWidths[],String bDate) throws IOException {
        this.bDate = bDate;
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
        // 创建表头 startRow代表表体开始的行
        int startRow = createHeadCell( headNames, colWidths);

        // 创建表体数据
        // 建立新的cell样式
        HSSFCellStyle cellStyle = getStyle(12,2,false,true);
        setCellData(mapList, cellStyle, startRow, colKeys,headNames.length);

        //创建表尾
        List<Map> morning = mapList.get("morning");
        List<Map> afternoon = mapList.get("afternoon");
        createTailCell((morning.size()+afternoon.size())+3,headNames.length);
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        byte[] ba = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        return bais;*/
        return workbook;
    }

    /**
     * 创建表头
     *
     * @param headNames
     * @param colWidths
     */
    public int createHeadCell( String[] headNames, int colWidths[]) {
        // 表头标题
        HSSFCellStyle titleStyle = getStyle(22,2,true,false);//样式
        createRow(titleStyle,0x549,"深圳市龙华区人民检察院视频提审人员名单",0,0,0,headNames.length-1);
        //第二行
       /* HSSFCellStyle unitStyle = getStyle(12,2,true,false);
        createRow(unitStyle,0x190,"单位: 深圳市龙华区人民检察院",1,1,0,headNames.length-1);*/

        //第三行左边部分
        /*year = Integer.parseInt(bDate.substring(0,4));
        String month = bDate.substring(4,6);
        int m = Integer.parseInt(month)-1;
        Calendar   cal   =   Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,m);//从0开始 0代表一月 11代表12月
        int   maxDate   =   cal.getActualMaximum(Calendar.DATE);

        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));*/
        HSSFRow row = sheet.createRow(1);
        row.setHeight((short) 0x190);
        /*HSSFCell cell = row.createCell(0);
        cell.setCellStyle(getStyle(12,1,true,false));
        cell.setCellValue("时间:"+year+"年"+month+"月"+"01日至"+year+"年"+month+"月"+maxDate+"日");*/

        //第三行右边部分
        Date date = DateUtil.parse(bDate, UmapDateUtils.YYYY_MM_DD);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, headNames.length-1));
        HSSFCell cell2 = row.createCell(3);
        cell2.setCellStyle(getStyle(12,3,true,false));
        cell2.setCellValue("日期: "+sdf.format(date));

        //第四行表头
        boolean b = (headNames != null && headNames.length > 0);
        if (b) {
            HSSFRow row2 = sheet.createRow(2);
            row2.setHeight((short) 0x289);
            HSSFCell fcell = null;
            HSSFCellStyle cellStyle = getStyle(12,2,true,true); // 建立新的cell样式
            for (int i = 0; i < headNames.length; i++) {
                fcell = row2.createCell(i);
                fcell.setCellStyle(cellStyle);
                fcell.setCellValue(headNames[i]);
                if (colWidths != null && i < colWidths.length) {
                    sheet.setColumnWidth(i, 32 * colWidths[i]);
                }
            }
        }
        return b ? 3 : 2;  //从哪一行开始渲染表体
    }

    /**
     * 创建表体数据
     * @param mapList           表体数据
     * @param cellStyle      样式
     * @param startRow       开始行
     * @param colKeys        值对应map的key
     */
    public void setCellData(Map<String,List<Map>> mapList, HSSFCellStyle cellStyle, int startRow,
                            String[] colKeys,int length) {
        // 创建数据
        HSSFRow row = null;
        HSSFCell cell = null;
        int i = startRow;
        List<Map> morning = mapList.get("morning");
        List<Map> afternoon = mapList.get("afternoon");

        if (morning != null && morning.size() > 0) {
            //DecimalFormat df = new DecimalFormat("#0.00");
            for (Map<String, Object> rowData : morning) {
                row = sheet.createRow(i);
                row.setHeight((short) 0x279);
                int j = 0;
                for (String key : colKeys) {
                    Object colValue = rowData.get(key);
                    cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (colValue != null) {
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(colValue.toString());
                    }
                    j++;
                }
                i++;
            }
            HSSFCellStyle remarkStyle2 = getStyle(14,2,false,false);
            createRow(remarkStyle2,0x160,"上午/下午",i,i,0,length-1);

            i=i+1;
            for (Map<String, Object> rowData : afternoon) {
                row = sheet.createRow(i);
                row.setHeight((short) 0x279);
                int j = 0;
                for (String key : colKeys) {
                    Object colValue = rowData.get(key);
                    cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (colValue != null) {
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(colValue.toString());
                    }
                    j++;
                }
                i++;
            }
        }
    }

    /**
     * 创建表体数据
     * @param data           表体数据
     * @param cellStyle      样式
     * @param startRow       开始行
     * @param colKeys        值对应map的key
     */
    public void setCellData(List<Map> data, HSSFCellStyle cellStyle, int startRow,
                             String[] colKeys,int length) {
        // 创建数据
        HSSFRow row = null;
        HSSFCell cell = null;
        int i = startRow;

        if (data != null && data.size() > 0) {
            //DecimalFormat df = new DecimalFormat("#0.00");
            for (Map<String, Object> rowData : data) {
                row = sheet.createRow(i);
                row.setHeight((short) 0x279);
                int j = 0;
                for (String key : colKeys) {
                    Object colValue = rowData.get(key);
                    /*if (key.equalsIgnoreCase("CITYNAME")){
                        colValue = colValue+"XX科技有限公司";
                    }else if (key.equalsIgnoreCase("ORDERSUM")||key.equalsIgnoreCase("TRANSFEE")||key.equalsIgnoreCase("ORDREALSUM")){
                        colValue = df.format(colValue);
                    }*/
                    cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                    if (colValue != null) {
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cell.setCellValue(colValue.toString());
                    }
                    j++;
                    /*if(j>= (data.size()/2)){
                        HSSFCellStyle remarkStyle2 = getStyle(12,2,false,false);
                        createRow(remarkStyle2,0x160,"上午/下午",j,j+1,0,length-1);
                    }*/
                }
                i++;
            }
        }
    }

    /**
     * 创建表尾
     * @param size
     * @param length
     */
    public void createTailCell(int size, int length) {
        /*HSSFCellStyle remarkStyle1 = getStyle(11,1,false,false);
        createRow(remarkStyle1,0x190,"经核对，确认以上数据真实无误。",size,size,0,length-2);*/

        HSSFCellStyle remarkStyle2 = getStyle(12,1,false,false);
        remarkStyle2.setWrapText(true);
        /*String text = "备注：" +"\r\n"+
                "协助提审的工作人员:" +"\r\n"+
                "书记员：王一辰，工作证号：09L003，身份证号：500103199305112614，手机号：18328585194；" +"\r\n"+
                "书记员：蔡地佛，工作证号：09F006，身份证号：36073119920327485X，手机号：13122266908；" +"\r\n"+
                "书记员：郭晓哲，工作证号：09F015，身份证号：231003199606021337，手机号：15889798445；";*/
        String text = "备注：" + "\r\n" + configService.selectConfigByKey("arraign_appointment_remark");
        createRow(remarkStyle2,0x160,text,size+1,size+3,0,length-1);

        /*HSSFRow row3 = sheet.createRow(size+2);
        row3.setHeight((short) 0x379);

        sheet.addMergedRegion(new CellRangeAddress(size+3, size+3, 0, 1));
        HSSFRow row4 = sheet.createRow(size+3);
        row4.setHeight((short) 0x190);
        HSSFCell cell4 = row4.createCell(0);
        cell4.setCellStyle(getStyle(11,1,false,false));
        cell4.setCellValue("单位核对人：");

        sheet.addMergedRegion(new CellRangeAddress(size+3, size+3, 2, 4));
        HSSFCell cell15 = row4.createCell(2);
        cell15.setCellStyle(getStyle(11,1,false,false));
        cell15.setCellValue("单位制表人：");

        HSSFCellStyle dateStyle = getStyle(10,3,false,false);
        createRow(dateStyle,0x150,"公司公章                     ",size+8,size+8,0,length-2);

        createRow(dateStyle,0x150,year+"年  月   日",size+9,size+9,0,length-2);*/

    }


    public HSSFWorkbook Excels(String tableName, List<ReportDto> list) {
        //创建excel
        workbook = new HSSFWorkbook();
        if (list.size() != 0) {
            //创建一个工作表(表名)
            sheet = workbook.createSheet(tableName);
        }
        //设置样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setAlignment(HorizontalAlignment.LEFT);//设置单元格水平方向对齐方式  居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置单元格垂直方向对齐方式  居中
        // 设置字体样式
        HSSFCellStyle cellStyle4 = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();//设置字体样式
        font.setFontName("微软雅黑");

        font.setFontHeightInPoints((short) 12);//设置字号
        cellStyle4.setFont(font);//字体风格放入
        // 创建第一行标题
        HSSFRow row = sheet.createRow(0);


        //合拼单元格

        //sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
        HSSFCell cell = null;
        String[] headLists = new String[]{"日期","部门名称","犯罪类型","总数"};
        for (int i = 0; i < headLists.length; i++) {
            cell = row.createCell(i);

            cell.setCellValue(headLists[i]);

        }
        for (int i = 0; i < list.size(); i++) {
            //内容区与标题距离行数
            HSSFRow row1 = sheet.createRow(i + 1);
            //设置宽度
            sheet.setColumnWidth(i, 5000);
            //赋值
            row1.createCell(0).setCellValue(list.get(i).getDateFormat());
            if(StrUtil.isEmpty(list.get(i).getDeptName())) {
                row1.createCell(1).setCellValue("未分配部门");
            } else {
                row1.createCell(1).setCellValue(list.get(i).getDeptName());
            }
            row1.createCell(2).setCellValue(list.get(i).getCriminalType());
            row1.createCell(3).setCellValue(list.get(i).getCount());
        }
        return workbook;
    }



    // 往字符串数组追加新数据
    private String[] insert(String[] arr, String... str) {
        int size = arr.length; // 获取原数组长度
        int newSize = size + str.length; // 原数组长度加上追加的数据的总长度

        // 新建临时字符串数组
        String[] tmp = new String[newSize];
        // 先遍历将原来的字符串数组数据添加到临时字符串数组
        for (int i = 0; i < size; i++) {
            tmp[i] = arr[i];
        }
        // 在末尾添加上需要追加的数据
        for (int i = size; i < newSize; i++) {
            tmp[i] = str[i - size];
        }
        return tmp; // 返回拼接完成的字符串数组

    }


    // 测试
    public static void main(String[] args) throws IOException {
        ArraignAppointmentExcel arraignAppointmentExcel = new ArraignAppointmentExcel();
        List<Map> data = new ArrayList<Map>();

        LinkedHashMap<String, Object> e = new LinkedHashMap<String, Object>();

        e.put("CITYNAME", "北京");
        e.put("ORDERCOUNT", "65");
        e.put("ORDERSUM", 930.38);
        e.put("TRANSFEE", 2.28);
        e.put("ORDREALSUM", 928.10);
        e.put("REMARK", "通过1");
        data.add(e);

        e = new LinkedHashMap<String, Object>();
        e.put("CITYNAME", "上海");
        e.put("ORDERCOUNT", "50");
        e.put("ORDERSUM", 850.34);
        e.put("TRANSFEE", 2.08);
        e.put("ORDREALSUM", 848.26);
        e.put("REMARK", "通过2");
        data.add(e);

        e = new LinkedHashMap<String, Object>();
        e.put("CITYNAME", "苏州");
        e.put("ORDERCOUNT", "10");
        e.put("ORDERSUM", 112.20);
        e.put("TRANSFEE", 2.20);
        e.put("ORDREALSUM", 55.00);
        e.put("REMARK", "通过3");
        data.add(e);

        e = new LinkedHashMap<String, Object>();
        e.put("CITYNAME", "南京");
        e.put("ORDERCOUNT", "26");
        e.put("ORDERSUM", 210.12);
        e.put("TRANSFEE", 0.51);
        e.put("ORDREALSUM", 2409.61);
        e.put("REMARK", "通过4");
        data.add(e);

        String[] headNames = { "单位名称", "收入笔数", "收入金额", "手续费(2.45‰)", "实际金额","备注" };
        String[] keys = { "CITYNAME",  "ORDERCOUNT", "ORDERSUM","TRANSFEE","ORDREALSUM","REMARK"};
        int colWidths[] = { 300, 200, 200, 200, 200,300 };

        String bDate = "201708";
        /*InputStream input = (excel.getExcelFile(data, "单位", headNames, keys, colWidths,bDate));

        File f = new File("f:\\excel.xls");
        if (f.exists())
            f.delete();
        f.createNewFile();
        FileOutputStream out = new FileOutputStream(f);
        HSSFWorkbook book = new HSSFWorkbook(input);
        book.write(out);
        out.flush();
        out.close();*/
    }

}

