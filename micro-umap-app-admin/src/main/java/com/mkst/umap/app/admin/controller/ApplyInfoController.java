package com.mkst.umap.app.admin.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.system.domain.SysArea;
import com.mkst.mini.systemplus.system.domain.SysDictData;
import com.mkst.mini.systemplus.system.service.ISysAreaService;
import com.mkst.mini.systemplus.system.service.ISysDeptService;
import com.mkst.mini.systemplus.system.service.ISysDictDataService;
import com.mkst.umap.app.admin.domain.LhWs;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.ILhWsService;
import com.mkst.umap.base.core.util.UmapDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.service.IApplyInfoService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 备勤间申请 信息操作处理
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Controller
@RequestMapping("/admin/applyInfo")
public class ApplyInfoController extends BaseController
{
    private String prefix = "app/apply/applyInfo";
	
	@Autowired
	private IApplyInfoService applyInfoService;
	@Autowired
	private IAuditRecordService auditRecordService;

	@Autowired
	private ISysDeptService deptService;
	
	@RequiresPermissions("admin:applyInfo:view")
	@GetMapping()
	public String applyInfo()
	{
	    return prefix + "/applyInfo";
	}
	
	/**
	 * 查询备勤间申请列表
	 */
	@RequiresPermissions("admin:applyInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ApplyInfo applyInfo)
	{
		startPage();
        List<ApplyInfo> list = applyInfoService.selectApplyInfoList(applyInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出备勤间申请列表
	 */
	@RequiresPermissions("admin:applyInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ApplyInfo applyInfo)
    {
    	List<ApplyInfo> list = applyInfoService.selectApplyInfoList(applyInfo);
        ExcelUtil<ApplyInfo> util = new ExcelUtil<ApplyInfo>(ApplyInfo.class);
        return util.exportExcel(list, "applyInfo");
    }
	
	/**
	 * 新增备勤间申请
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存备勤间申请
	 */
	@RequiresPermissions("admin:applyInfo:add")
	@Log(title = "备勤间申请", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ApplyInfo applyInfo)
	{
		applyInfo.setApplicantId(ShiroUtils.getUserId());
		applyInfo.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(applyInfoService.insertApplyInfoWithGuests(applyInfo));
	}

	/**
	 * 修改备勤间申请
	 */
	@GetMapping("/edit/{applyId}")
	public String edit(@PathVariable("applyId") Long applyId, ModelMap mmap)
	{
		ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyId);
		mmap.put("applyInfo", applyInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存备勤间申请
	 */
	@RequiresPermissions("admin:applyInfo:edit")
	@Log(title = "备勤间申请", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ApplyInfo applyInfo)
	{		
		return toAjax(applyInfoService.updateApplyInfo(applyInfo));
	}

	/**
	 * 提交审核
	 */
	@PostMapping("/doAudit/{id}/{status}/{reason}")
	@RequiresPermissions("admin:applyInfo:audit")
	@Log(title = "备勤间申请审核", businessType = BusinessType.UPDATE)
	@ResponseBody
	public AjaxResult doAudit(@PathVariable Long id, @PathVariable String status, @PathVariable String reason) {
		return toAjax(applyInfoService.audit(id, status, reason));
	}
	
	/**
	 * 删除备勤间申请
	 */
	@RequiresPermissions("admin:applyInfo:remove")
	@Log(title = "备勤间申请", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(applyInfoService.deleteApplyInfoByIds(ids));
	}


	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		applyInfoService.detail(id,mmap);
		return prefix + "/detail";
	}

    /**
     * @ClassName BackUpApplyTask
     * @Description 备勤间定时任务
     * @Author ltt
     * @Modified By:
     * @Date 2020-08-11 11:12
     */
    @Component("SelectLHBookTask")
    @Slf4j
    public static class SelectLHBookTask {

        @Value("${lh.searchWsIdUrl}")
        private String searchWsIdUrl;
        @Value("${lh.searchWsUrl}")
        private String searchWsUrl;
        @Value("${lh.searchWsIdIncrementUrl}")
        private String searchWsIdIncrementUrl;
        private static String CRIMINAL_TYPE = "lh_crime_type_select";
        private static String LH_WS_AREA = "lh_ws_area";

        @Autowired
        private ISysDictDataService sysDictDataService;
        @Autowired
        private ISysAreaService sysAreaService;
        @Autowired
        private ILhWsService lhWsService;

        //执行一次 获取全部
        public void SelectLHBookTask() {
            try{
                List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType(CRIMINAL_TYPE);
                SysArea sysArea = new SysArea();
                // 440300 深圳市code 查询深圳市下面的所有区
                sysArea.setParentCode("440300");
                List<SysArea> areaList = sysAreaService.selectAreaList(sysArea);
                for(SysDictData sysDictData : dictDataList){
                    JSONObject json = new JSONObject();
                    json.put("crimeName", sysDictData.getDictLabel());
                    log.info("\n【请求地址】: {}\n【请求参数】：{}", searchWsIdUrl, json);
                    String post = HttpUtil.createPost(searchWsIdUrl).body(String.valueOf(json), "application/json").execute().body();
                    log.info("\n【响应数据】：{}", post);
                    JSONObject responseJson = JSONObject.parseObject(post);
                    if(responseJson.getJSONArray("data") != null && responseJson.getJSONArray("data").size() >0){
                        List<LhWs> list = JSONObject.parseArray(responseJson.getJSONArray("data").toJSONString(), LhWs.class);
                        list.forEach(o ->{
                            try{
                                JSONObject object = new JSONObject();
                                object.put("wsId", o.getWsId());

                                log.info("\n【请求地址】: {}\n【请求参数】：{}", searchWsUrl, object);
                                String result = HttpUtil.createPost(searchWsUrl).body(String.valueOf(object), "application/json").execute().body();
                                log.info("\n【响应数据】：{}", result);
                                String data = JSONObject.parseObject(result).getString("data");
                                String content = JSONObject.parseObject(data).get("content").toString();
                                String area = null;
                                if(StrUtil.isNotBlank(content)) {
                                    //area = subString(content,"公诉机关","人民检察院");
                                    area = patternStr(content,"公诉机关([\\s\\S]{1,10})人民检察院");
                                    if(StrUtil.isNotBlank(area)) {
                                        area = area.split("深圳市")[1];
                                    }
                                }
                                String finalArea = area;
                                List<SysArea> filterList = areaList.stream().filter(a -> a.getAreaName().equals(finalArea)).collect(Collectors.toList());
                                if(filterList.size()>0) {
                                    area = filterList.get(0).getAreaCode();
                                } else {
                                    area = "0";
                                }
                                String subContent = content.substring(18, 40);
                                //String pushYear = subString(subContent, "（", "）");
                                String yearReg = "[(（]([\\d]*)[）)]";
                                String pushYear = patternStr(subContent,yearReg);

                                LhWs old = new LhWs();
                                old.setWsId(o.getWsId());
                                List<LhWs> lhWsList = lhWsService.selectLhWsList(old);
                                if(CollUtil.isEmpty(lhWsList)){
                                    o.setArea(area);
                                    o.setContent(content);
                                    o.setType(sysDictData.getDictValue());
                                    o.setSyncTime(UmapDateUtils.getNowDate());
                                    o.setPublishTime(pushYear);
                                    lhWsService.insertLhWs(o);
                                }

                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                            }
                        });
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //增量定时器
        public void SelectLHBookIncrementTask() {
            try{
                List<SysDictData> dictDataList = sysDictDataService.selectDictDataByType(CRIMINAL_TYPE);
                SysArea sysArea = new SysArea();
                // 440300 深圳市code 查询深圳市下面的所有区
                sysArea.setParentCode("440300");
                List<SysArea> areaList = sysAreaService.selectAreaList(sysArea);
                for(SysDictData sysDictData : dictDataList){
                    JSONObject json = new JSONObject();
                    json.put("crimeName", sysDictData.getDictLabel());
                    json.put("date", DateUtil.today());
                    log.info("\n【请求地址】: {}\n【请求参数】：{}", searchWsIdIncrementUrl, json);
                    String post = HttpUtil.createPost(searchWsIdIncrementUrl).body(String.valueOf(json), "application/json").execute().body();
                    log.info("\n【响应数据】：{}", post);
                    JSONObject responseJson = JSONObject.parseObject(post);
                    if(responseJson.getJSONArray("data") != null && responseJson.getJSONArray("data").size() >0){
                        List<LhWs> list = JSONObject.parseArray(responseJson.getJSONArray("data").toJSONString(), LhWs.class);
                        list.forEach(o ->{
                            try{
                                JSONObject object = new JSONObject();
                                object.put("wsId", o.getWsId());
                                log.info("\n【请求地址】: {}\n【请求参数】：{}", searchWsUrl, object);
                                String result = HttpUtil.createPost(searchWsUrl).body(String.valueOf(object), "application/json").execute().body();
                                log.info("\n【响应数据】：{}", result);
                                String data = JSONObject.parseObject(result).getString("data");
                                String content = JSONObject.parseObject(data).get("content").toString();
                                String area = null;
                                if(StrUtil.isNotBlank(content)) {
                                    //area = subString(content,"公诉机关","人民检察院");
                                    area = patternStr(content,"公诉机关([\\s\\S]{1,10})人民检察院");
                                    if(StrUtil.isNotBlank(area)) {
                                        area = area.split("深圳市")[1];
                                    }
                                }
                                String finalArea = area;
                                List<SysArea> filterList = areaList.stream().filter(a -> a.getAreaName().equals(finalArea)).collect(Collectors.toList());
                                if(filterList.size()>0) {
                                    area = filterList.get(0).getAreaCode();
                                } else {
                                    area = "0";
                                }
                                //通过文书id判断是否存在  存在则修改，否则新增
                                LhWs old = new LhWs();
                                old.setWsId(o.getWsId());
                                List<LhWs> lhWsList = lhWsService.selectLhWsList(old);
                                if(CollUtil.isNotEmpty(lhWsList)){
                                    lhWsList.get(0).setContent(content);
                                    lhWsList.get(0).setType(sysDictData.getDictValue());
                                    lhWsList.get(0).setArea(area);
                                    lhWsService.updateLhWs(lhWsList.get(0));
                                }else {
                                    o.setContent(content);
                                    o.setType(sysDictData.getDictValue());
                                    o.setArea(area);
                                    lhWsService.insertLhWs(o);
                                }
                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                            }
                        });
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public static String subString(String str, String strStart, String strEnd) {

            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = str.indexOf(strStart);
            int strEndIndex = str.indexOf(strEnd);

            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strStartIndex < 0) {
                return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
            }
            if (strEndIndex < 0) {
                return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
            }
            /* 开始截取 */
            String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
            return result;
        }

        public static String patternStr(String str , String reg){
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }

        public static void main(String[] args) {

            String str = "被告人黄燕玲，女，，捕前暂住广东省\n" +
                    "广东省深圳市龙华区人民法院\n" +
                    "刑 事 判 决 书\n" +
                    "（2019）粤0309刑初1245号\n" +
                    "公诉机关深圳市龙华区人民检察院。\n" +
                    "被告人黄燕玲，女，，捕前暂住广东省深圳市龙华区，曾因犯诈骗罪于2015年3月31日被福建省厦门市思明区人民法院判处有期徒刑八个月，2015年5月1日刑满释放。现因涉嫌犯诈骗罪于2018年10月26日被传唤到案，次日被刑事拘留，同年11月23日被逮捕。现羁押于深圳市宝安区看守所。\n" +
                    "指定辩护人孙婷，广东联建律师事务所律师。\n" +
                    "深圳市龙华区人民检察院以深龙华检刑诉〔2019〕1247号起诉书指控被告人黄燕玲犯诈骗罪一案，于2019年7月10日向本院提起公诉。本院受理后，依法组成合议庭，于2019年9月18日公开开庭审理了本案。深圳市龙华区人民检察院指派检察员姚睿敏出庭支持公诉。被告人黄燕玲及其辩护人孙婷到庭参加诉讼，现已审理终结。\n" +
                    "公诉机关指控，被告人黄燕玲与被害人班某于2016年9月相识并成为朋友。黄燕玲自2018年4月至2018年8月多次以投资、介绍工作等为由骗取班某钱款。其中2018年4月15日黄燕玲以投资理财为由骗取班某人民币5,000元；2018年6月黄燕玲以班某思介绍工作和购买电脑为由骗取人民币6,400元；2018年6月24日黄燕玲以合作投资为由骗取班某人民币24,400元。\n" +
                    "2017年被告人黄燕玲在深圳市龙华区民治街道塘水围宇通大厦1楼店铺消费时，认识在此工作的被害人王某。2018年9月至10月，黄燕玲以合作低价购买苹果手机并转卖、向银行交税等为由，先后多次骗取王某钱款共计人民币63,500元。2018年10月10日，王某发现黄燕玲将其微信删除并失联于是报警。2018年10月26日，黄燕玲与王某来到深圳市龙华区民治派出所协商还款时被公安机关传唤。\n" +
                    "公诉机关为支持其指控，当庭出示并宣读的证据有：1．书证：微信及支付宝转账记录截图、微信聊天记录截图、被告人身份信息、三面照、前科信息表、刑事判决书复印件；2.证人证言：证人钟某、叶某的证言：3.被害人陈述：被害人王某、班某的陈述；4.被告人的供述与辩解：被告人黄燕玲的供述与辩解；5.勘验、检查、辨认笔录：现场勘验检查笔录、方位示意图及照片，被害人对被告人的辨认；6.电子数据：被告人所用微信的后台支付数据。\n" +
                    "公诉机关认为，被告人黄燕玲以非法占有为目的，骗取他人财物，数额较大，其行为构成诈骗罪。诉请本院依法惩处。\n" +
                    "被告人黄燕玲当庭表示称其没有骗班某的钱，其有骗王某的钱。案发前，班某一直和其生活在一起直至其去派出所投案，班某给其的转账系生活费和买手机的费用。\n" +
                    "被告人黄燕玲的辩护人提出，1.班某报案称被骗证据不足，班某与黄燕玲一直生活在一起，存在财物混同情况；2.黄燕玲有自首情节，主动到派出所并如实供述犯罪情节；3.被告人黄燕玲存在还钱的意愿，主观恶性小，请求法院轻轻触发。\n" +
                    "经审理查明，2017年被告人黄燕玲在深圳市龙华区民治街道塘水围宇通大厦1楼店铺消费时，认识在此工作的被害人王某。2018年9月至10月，黄燕玲以合作低价购买苹果手机并转卖、向银行交税等为由，先后多次骗取王某钱款共计人民币63,500元。2018年10月10日，王某发现黄燕玲将其微信删除并失联于是报警。2018年10月26日，黄燕玲与王某来到深圳市龙华区民治派出所协商还款时被公安机关传唤。被告人黄燕玲对诈骗王某的行为供认不讳。\n" +
                    "公诉机关出示的证据均经开庭质证，本院予以确认。\n" +
                    "本院认为，被告人黄燕玲以非法占有为目的，骗取他人财物，数额较大，其行为构成诈骗罪。公诉机关指控被告人黄燕玲诈骗被害人王某事实清楚，证据确实、充分，指控罪名成立，但指控被告人黄燕玲诈骗被害人班某的证据，仅有班某一人陈述，且对于诈骗数额陈述不一，亦无其他证据佐证，而被告人黄燕玲对于和被害人班某之间款项来往、案发前一同居住的辩解与酒店住宿等实际情况吻合，被告人黄燕玲的辩解在无其他证据反证的情况下，存在合理性，本院予以采纳。被告人黄燕玲在明知被害人王某报警的情况下，主动到派出所投案并如实供述了相关犯罪事实，系自首，依法可以从轻或减轻处罚。辩护人的相关辩护理由本院予以采纳。\n" +
                    "综上，根据被告人黄燕玲的犯罪事实，归案后的认罪态度、自首等量刑情节，尚未返还诈骗款等情节，依照《中华人民共和国刑法》第二百六十六条、第六十七条第一款、第五十二条、第五十三条、第六十四条之规定，判决如下：\n" +
                    "一、被告人黄燕玲犯诈骗罪，判处有期徒刑一年六个月，并处罚金人民币一万元。（刑期从判决执行之日起计算，判决执行之前先行羁押的，羁押一日折抵刑期一日，即自2018年10月26日起至2020年4月25日止。罚金限于本判决生效后一个月内缴纳。）\n" +
                    "二、对被告人黄燕玲诈骗款项63,500元，继续追缴，并依法返还被害人王某。\n" +
                    "如不服本判决，可在接到判决书的第二日起十日内，通过本院或者直接向深圳市中级人民法院提出上诉。书面上诉的，应当提交上诉状正本一份，副本二份。\n" +
                    "审　判　长　邹　　　　鹏\n" +
                    "人民陪审员　廖　 小　 龙\n" +
                    "人民陪审员　张　　　　瑞\n" +
                    "二〇一九年十月十四日\n" +
                    "书　记　员　张洪源（兼）\n" +
                    "书　记　员　吴　 思　 男\n" +
                    "附相关法条：\n" +
                    "《中华人民共和国刑法》\n" +
                    "第五十二条判处罚金，应当根据犯罪情节决定罚金数额。\n" +
                    "第五十三条罚金在判决指定的期限内一次或者分期缴纳。期满不缴纳的，强制缴纳。对于不能全部缴纳罚金的，人民法院在任何时候发现被执行人有可以执行的财产，应当随时追缴。\n" +
                    "由于遭遇不能抗拒的灾祸等原因缴纳确实有困难的，经人民法院裁定，可以延期缴纳、酌情减少或者免除。\n" +
                    "第六十四条犯罪分子违法所得的一切财物，应当予以追缴或者责令退赔；对被害人的合法财产，应当及时返还；违禁品和供犯罪所用的本人财物，应当予以没收。没收的财物和罚金，一律上缴国库，不得挪用和自行处理。\n" +
                    "第六十七条犯罪以后自动投案，如实供述自己的罪行的，是自首。对于自首的犯罪分子，可以从轻或者减轻处罚。其中，犯罪较轻的，可以免除处罚。\n" +
                    "被采取强制措施的犯罪嫌疑人、被告人和正在服刑的罪犯，如实供述司法机关还未掌握的本人其他罪行的，以自首论。\n" +
                    "犯罪嫌疑人虽不具有前两款规定的自首情节，但是如实供述自己罪行的，可以从轻处罚；因其如实供述自己罪行，避免特别严重后果发生的，可以减轻处罚。\n" +
                    "第二百六十六条诈骗公私财物，数额较大的，处三年以下有期徒刑、拘役或者管制，并处或者单处罚金；数额巨大或者有其他严重情节的，处三年以上十年以下有期徒刑，并处罚金；数额特别巨大或者有其他特别严重情节的，处十年以上有期徒刑或者无期徒刑，并处罚金或者没收财产。本法另有规定的，依照规定。\n";

            String reg = "公诉机关([\\s\\S]{1,10})人民检察院";

            System.out.println(patternStr(str,reg));




        }

    }

}
