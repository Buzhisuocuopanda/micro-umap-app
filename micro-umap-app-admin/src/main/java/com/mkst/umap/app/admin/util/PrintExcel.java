package com.mkst.umap.app.admin.util;


import com.spire.xls.Workbook;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.print.PrintService;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName PrintExcel
 * @Description PrintExcel
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-25 15:55
 */
@Slf4j
public class PrintExcel {
    public static void main(String[] args) throws IOException, InvalidFormatException, PrinterException {
        String pdfFile = "D:\\视频提审人员名单 (11).xls";// 文件路径
        //加载excel工作表
        Workbook workbook = new Workbook();
        workbook.loadFromFile("E:\\resources\\umap\\视频提审人员名单.xls");
        //创建 PrinterJob对象
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        //指定打印机
        PrintService myPrintService = findPrintService("Brother MFC-7880DN Printer");
        printerJob.setPrintService(myPrintService);
        //指定打印页面为默认大小和方向
        PageFormat pageFormat = printerJob.defaultPage();
        //设置相关打印选项
        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
        pageFormat.setPaper(paper);
        printerJob.setCopies(1);
        printerJob.setPrintable(workbook, pageFormat);
        //执行打印
        try {
            printerJob.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public static void ExcelPrint(File file, String printerName) throws FileNotFoundException, PrinterException {
        if (file == null) {
            log.error("缺少打印文件");
            return;
        }
        // 文件路径
        String pdfFile = file.getPath();
        //加载excel工作表
        Workbook workbook = new Workbook();
        workbook.loadFromFile(pdfFile);
        //创建 PrinterJob对象
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        //指定打印机
        PrintService myPrintService = findPrintService(printerName);
        if (myPrintService == null){
            log.error("未发现打印机：" + printerName);
            return;
        }
        printerJob.setPrintService(myPrintService);
        //指定打印页面为默认大小和方向
        PageFormat pageFormat = printerJob.defaultPage();
        //设置相关打印选项
        Paper paper = pageFormat.getPaper();
        paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());
        pageFormat.setPaper(paper);
        printerJob.setCopies(1);
        printerJob.setPrintable(workbook, pageFormat);
        printerJob.print();

    }

/*    public static String excel2Pdf(File file){
        String filePath = file.getAbsolutePath();
        //加载Excel文档
        Workbook wb = new Workbook();
        //调用方法保存为PDF格式
        wb.loadFromFile(filePath);
        String pdfName = file.getParentFile().getAbsolutePath() + "pdf.pdf";
        wb.saveToFile(pdfName, FileFormat.PDF);

        return pdfName;
    }*/

    //通过打印机名称获取打印服务


    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        for (PrintService printService : printServices) {
            if (printService.getName().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
}
