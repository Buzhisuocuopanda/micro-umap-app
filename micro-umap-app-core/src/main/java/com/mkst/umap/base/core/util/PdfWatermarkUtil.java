package com.mkst.umap.base.core.util;

import java.awt.FontMetrics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.swing.JLabel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mkst.mini.systemplus.common.utils.file.FileUtils;

/**
 * 给pdf添加水印工具类
 */
public class PdfWatermarkUtil {
	private static int interval = 5; 
	
	/**
	 * 需要添加水印的pdf文件
	 * 
	 * @param inputPdf      需要加水印的pdf路径
	 * @param outputPdf     加完水印后的pdf文件路径
	 * @param textWatermark 水印文字
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void addPngWatermark(String inputPdf, String outputPdf, String textWatermark, String userPassWord,
			String ownerPassWord) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(inputPdf);

		File file = new File(outputPdf);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}

		BaseFont baseFont = BaseFont.createFont(getChineseFont(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		// 设置字体大小
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(file));
		// 设置密码
		stamper.setEncryption(userPassWord.getBytes(), ownerPassWord.getBytes(), PdfWriter.ALLOW_PRINTING,
				PdfWriter.STANDARD_ENCRYPTION_128);
		// transparency
		Rectangle pageRect = null;
		PdfGState gs = new PdfGState();
		gs.setFillOpacity(0.1f);
		// 页数
		int total = reader.getNumberOfPages() + 1;

		JLabel label = new JLabel();
		FontMetrics metrics;
		int textH = 0;
		int textW = 0;
		label.setText(textWatermark);
		metrics = label.getFontMetrics(label.getFont());
		textH = metrics.getHeight();
		textW = metrics.stringWidth(label.getText());

		PdfContentByte under;
		for (int i = 1; i < total; i++) {
			pageRect = reader.getPageSizeWithRotation(i);
			under = stamper.getOverContent(i);
			under.saveState();
			under.setGState(gs);
			under.beginText();
			under.setFontAndSize(baseFont, 30);

			// 水印文字成30度角倾斜
			// 你可以随心所欲的改你自己想要的角度
			for (int height = interval + textH; height < pageRect.getHeight(); height = height + textH * 10) {
				for (int width = interval + textW; width < pageRect.getWidth() + textW; width = width + textW * 2) {
					under.showTextAligned(Element.ALIGN_LEFT, textWatermark, width - textW, height - textH, 45);
				}
			}
			// 添加水印文字
			under.endText();
		}

		stamper.close();
		reader.close();
	}

	/**
	 * 报表pdf生成
	 * @param filePath  文件地址
	 * @param map
	 */
	public static void reportCreatePdf(String filePath, Map<String,String> map){
		try{
			Document document = new Document(PageSize.A4,40f,40f,100f,20f);

			File file = new File(filePath);
			File fileParent = file.getParentFile();
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			//添加标题
			//BaseFont b1 = BaseFont.createFont("D:\\profile\\webcase\\SimHei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			BaseFont b1 = BaseFont.createFont(getChineseFont(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			Font title = new Font(b1,24,Font.ITALIC);
			Paragraph titleParagraph = new Paragraph("强制报告表",title);
			titleParagraph.setAlignment(Element.TITLE);
			document.add(titleParagraph);

			Font fontNormal = new Font(b1,12);

			PdfPTable baseTable = new PdfPTable(2);//表格为两例
			baseTable.setSpacingBefore(30);//表格前间距
			baseTable.setSpacingAfter(30);//表格后间距
			baseTable.setHorizontalAlignment(Element.ALIGN_CENTER); //表格水平对齐方式
			baseTable.setWidthPercentage(90);//宽度为80%页面
			baseTable.setWidths(new int[]{1,4}); //两列宽度为1：4
			baseTable.setPaddingTop(200);
			//baseTable.getDefaultCell().setMinimumHeight(30);//单元格最小高度
			baseTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);//单元格中文字垂直对齐方式
			baseTable.getDefaultCell().setExtraParagraphSpace(5);//段落文字与表格之间的距离,底部距离
			baseTable.getDefaultCell().setLeading(15, 0);//设置行间距

			//被侵害人员
			PdfPCell cell1 = getCell(50F,new Paragraph(map.get("cell1"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell1);
			PdfPCell cell2 = getCell(50F,new Paragraph(map.get("cell2"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell2);

			//报告主体发现时间
			PdfPCell cell3 = getCell(60F,new Paragraph(map.get("cell3"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell3);
			PdfPCell cell4 = getCell(60F,new Paragraph(map.get("cell4"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell4);

			//报告单位及人员
			PdfPCell cell5 = getCell(60F,new Paragraph(map.get("cell5"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell5);
			PdfPCell cell6 = getCell(60F,new Paragraph(map.get("cell6"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell6);

			//报告时间
			PdfPCell cell7 = getCell(50F,new Paragraph(map.get("cell7"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell7);
			PdfPCell cell8 = getCell(50F,new Paragraph(map.get("cell8"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell8);

			//报告详情
			PdfPCell cell9 = getCell(200F,new Paragraph(map.get("cell9"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell9);
			PdfPCell cell10 = getCell(200F,new Paragraph(map.get("cell10"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell10);

			//接报单位
			PdfPCell cell11 = getCell(50F,new Paragraph(map.get("cell11"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell11);
			PdfPCell cell12 = getCell(50F,new Paragraph(map.get("cell12"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell12);

			//后续处理
			PdfPCell cell13 = getCell(50F,new Paragraph(map.get("cell13"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell13);
			PdfPCell cell14 = getCell(50F,new Paragraph(map.get("cell14"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell14);

			document.add(baseTable);
			document.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * 报表pdf生成
	 * @param filePath  文件地址
	 * @param map
	 */
	public static void inductionQueryCreatePdf(String filePath, Map<String,String> map){
		try{
			Document document = new Document(PageSize.A4,40f,40f,100f,20f);

			File file = new File(filePath);
			File fileParent = file.getParentFile();
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();

			//添加标题
			//BaseFont b1 = BaseFont.createFont("D:\\profile\\webcase\\SimHei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			BaseFont b1 = BaseFont.createFont(getChineseFont(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			Font title = new Font(b1,24,Font.ITALIC);
			Paragraph titleParagraph = new Paragraph("入职申请表",title);
			titleParagraph.setAlignment(Element.TITLE);
			document.add(titleParagraph);

			Font fontNormal = new Font(b1,12);

			PdfPTable baseTable = new PdfPTable(2);//表格为两例
			baseTable.setSpacingBefore(30);//表格前间距
			baseTable.setSpacingAfter(30);//表格后间距
			baseTable.setHorizontalAlignment(Element.ALIGN_CENTER); //表格水平对齐方式
			baseTable.setWidthPercentage(90);//宽度为80%页面
			baseTable.setWidths(new int[]{1,4}); //两列宽度为1：4
			baseTable.setPaddingTop(200);
			//baseTable.getDefaultCell().setMinimumHeight(30);//单元格最小高度
			baseTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);//单元格中文字垂直对齐方式
			baseTable.getDefaultCell().setExtraParagraphSpace(5);//段落文字与表格之间的距离,底部距离
			baseTable.getDefaultCell().setLeading(15, 0);//设置行间距

			//查询人（用人单位）
			PdfPCell cell1 = getCell(50F,new Paragraph(map.get("cell1"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell1);
			PdfPCell cell2 = getCell(50F,new Paragraph(map.get("cell2"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell2);

			//被查询人（拟入职人员）基本信息
			PdfPCell cell3 = getCell(80F,new Paragraph(map.get("cell3"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell3);
			PdfPCell cell4 = getCell(80F,new Paragraph(map.get("cell4"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell4);

			//拟入职岗位
			PdfPCell cell5 = getCell(30F,new Paragraph(map.get("cell5"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell5);
			PdfPCell cell6 = getCell(30F,new Paragraph(map.get("cell6"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell6);

			//主管单位
			PdfPCell cell7 = getCell(30F,new Paragraph(map.get("cell7"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell7);
			PdfPCell cell8 = getCell(30F,new Paragraph(map.get("cell8"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell8);


			//查询时间
			PdfPCell cell9 = getCell(30F,new Paragraph(map.get("cell9"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell9);
			PdfPCell cell10 = getCell(30F,new Paragraph(map.get("cell10"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell10);

			//查询内容
			PdfPCell cell11 = getCell(70F,new Paragraph(map.get("cell11"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell11);
			PdfPCell cell12 = getCell(70F,new Paragraph(map.get("cell12"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell12);


			//查询结果
			PdfPCell cell13 = getCell(150F,new Paragraph(map.get("cell13"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell13);
			PdfPCell cell14 = getCell(150F,new Paragraph(map.get("cell14"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell14);

			//查询审核
			PdfPCell cell15 = getCell(30F,new Paragraph(map.get("cell15"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell15);
			PdfPCell cell16 = getCell(30F,new Paragraph(map.get("cell16"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell16);

			//是否属于从业限制人员
			PdfPCell cell17 = getCell(50F,new Paragraph(map.get("cell17"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell17);
			PdfPCell cell18 = getCell(50F,new Paragraph(map.get("cell18"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell18);

			//其他备注事项
			PdfPCell cell19 = getCell(40F,new Paragraph(map.get("cell19"),fontNormal),PdfPCell.ALIGN_LEFT,PdfPCell.ALIGN_TOP);
			baseTable.addCell(cell19);
			PdfPCell cell20 = getCell(40F,new Paragraph(map.get("cell20"),fontNormal),PdfPCell.ALIGN_CENTER,PdfPCell.ALIGN_MIDDLE);
			baseTable.addCell(cell20);

			document.add(baseTable);
			document.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取字体
	 * 
	 * @return
	 */
	private static String getChineseFont() {
		return PdfWatermarkUtil.class.getClassLoader().getResource("simhei.ttf").getPath();
	}

	/**
	 * 设置居中表格
	 * @param fixedHeight 表格高度
	 * @param phrase 表格内容字体
	 * @return
	 */
	private static PdfPCell getCell(float fixedHeight,Phrase phrase,int horizontal, int vertical){
		PdfPCell pathCell = new PdfPCell(phrase);
		pathCell.setHorizontalAlignment(horizontal);//水平居中
		pathCell.setUseAscender(true);//垂直居中
		pathCell.setVerticalAlignment(vertical);//垂直居中
		pathCell.setFixedHeight(fixedHeight);
		return pathCell;
	}

}
