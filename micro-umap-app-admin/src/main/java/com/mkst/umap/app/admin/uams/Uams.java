package com.mkst.umap.app.admin.uams;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @ClassName Uams
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/16 0016 上午 9:41
 */
@Data
@Slf4j
public class Uams {

    private String amServerUrl = "http://10.148.26.132:8001/am";

    public Uams(){}

    public Uams(String amServerUrl){
        this.amServerUrl = amServerUrl;
    }

    public String loginByPwd(String username , String pwd ){
        String url = StrUtil.format("{}/identity/authenticate?username={}&password={}&uri=service=initService",
                this.getAmServerUrl(),username , pwd);
        String result = HttpUtil.get(url);
        log.info("url:{},result:{}",url,result);
        System.out.println(StrUtil.format("url:{},result:{}",url,result));
        if(StrUtil.isBlank(result)){
            return null;
        }
        String token = result.trim().split("=")[1];
        return token;
    }

    public void loginOut(String token){
        String url = StrUtil.format("{}/identity/logout?subjectid=" +
                "{}",this.getAmServerUrl(),token);
        String result = HttpUtil.get(url);
        log.info("url:{},result:{}",url,result);
    }

    public String getRandom(String token){
        String url = StrUtil.format("{}/bjca/random/generate?" +
                "tokenId={}",this.getAmServerUrl() , token);
        String result = HttpUtil.get(url);
        log.info("url:{},result:{}",url,result);
        System.out.println(StrUtil.format("url:{},result:{}",url,result));
        JSONObject json = JSONObject.parseObject(result);
        return json.getString("random");
    }

    public JSONObject getAllUserAttribute(String token , String random){
        if(StrUtil.isBlank(random)){
            random = this.getRandom(token);
        }
        String url = StrUtil.format("{}/bjca/random/getAllUserAttributes?" +
                "tokenId={}&random={}",this.getAmServerUrl(),token,random);
        String r = HttpUtil.get(url);
        log.info("url:{},result:{}",url,r);
        System.out.println(StrUtil.format("url:{},result:{}",url,r));
        JSONObject jsonObject = JSONObject.parseObject(r);
        return jsonObject;
    }

    public String getUserPhone(String token , String random){
        JSONObject obj = getAllUserAttribute(token , random);
        JSONArray userPhone = obj.getJSONArray("telephonenumber");
        return userPhone.get(0).toString();
    }

    public Map<String, Object> QueryUserInfoDetail(String userIdCode){
        String url = StrUtil.format("http://10.148.26.132:8001/idm/rest/QueryUserInfoDetail?userIdCode={}&authId=93ce5274575740e994a0c458488ce9db",userIdCode);
        String r = HttpUtil.get(url);
        System.out.println(StrUtil.format("QueryUserInfoDetail url:{} ,  result:{}",url,r));
        JSONObject json = JSONObject.parseObject(r);
        //{"status":101123,"info":{"errorCode":"101123","errorMsg":"该用户已不存在"}}
        if(0 == json.getInteger("status")){
            //获取成功
            return json.getJSONObject("info").getInnerMap();
        }else{
            return null;
        }
    }

    public static void main(String[] args) {
        String token = "{\n" +
                "\t\"status\": 0,\n" +
                "\t\"info\": {\n" +
                "\t\t\"createTime\": {\n" +
                "\t\t\t\"time\": 1578554248000,\n" +
                "\t\t\t\"minutes\": 17,\n" +
                "\t\t\t\"seconds\": 28,\n" +
                "\t\t\t\"hours\": 15,\n" +
                "\t\t\t\"month\": 0,\n" +
                "\t\t\t\"year\": 120,\n" +
                "\t\t\t\"timezoneOffset\": -480,\n" +
                "\t\t\t\"day\": 4,\n" +
                "\t\t\t\"date\": 9\n" +
                "\t\t},\n" +
                "\t\t\"baseInfoCreList\": [],\n" +
                "\t\t\"cloudSignMsspId\": \"\",\n" +
                "\t\t\"userIdcardNum\": \"110101198001010379\",\n" +
                "\t\t\"userCredenceInfoEmail\": null,\n" +
                "\t\t\"appUserList\": [],\n" +
                "\t\t\"rootName\": \"\",\n" +
                "\t\t\"userEmail\": \"\",\n" +
                "\t\t\"policyBaseInfos\": [],\n" +
                "\t\t\"type\": \"\",\n" +
                "\t\t\"userExtList\": [],\n" +
                "\t\t\"userType\": 1,\n" +
                "\t\t\"uniqueId\": \"3b4a1a805cd9415bb99f3d0c40c5d448\",\n" +
                "\t\t\"userDefault4\": \"\",\n" +
                "\t\t\"userCredenceInfoVOCert\": null,\n" +
                "\t\t\"userDefault2\": \"\",\n" +
                "\t\t\"userDefault3\": \"\",\n" +
                "\t\t\"userDefault1\": \"\",\n" +
                "\t\t\"userName\": \"张文2\",\n" +
                "\t\t\"userPhone\": \"13300000000\",\n" +
                "\t\t\"userIdCode\": \"3b4a1a805cd9415bb99f3d0c40c5d448\",\n" +
                "\t\t\"duties\": [],\n" +
                "\t\t\"asName\": \"\",\n" +
                "\t\t\"groups\": [],\n" +
                "\t\t\"checkState\": \"2\",\n" +
                "\t\t\"unitCode\": \"\",\n" +
                "\t\t\"userSortnum\": 9999,\n" +
                "\t\t\"userModPwdFlag\": 1,\n" +
                "\t\t\"userCredenceInfoChineseName\": null,\n" +
                "\t\t\"checkDescribe\": \"\",\n" +
                "\t\t\"organizations\": [{\n" +
                "\t\t\t\"orgType\": null,\n" +
                "\t\t\t\"orgUpCode\": \"1001\",\n" +
                "\t\t\t\"orgDescript\": \"测试专用\",\n" +
                "\t\t\t\"orgFlowno\": \"0f119656d0834000873fb56caf8f54c1\",\n" +
                "\t\t\t\"orgCode\": \"10011001\",\n" +
                "\t\t\t\"orgNumber\": \"defaultOrg\",\n" +
                "\t\t\t\"orgStatus\": \"0\",\n" +
                "\t\t\t\"orgDefault1\": \"Y\",\n" +
                "\t\t\t\"orgName\": \"龙华区政府\",\n" +
                "\t\t\t\"orgSortNum\": 9999,\n" +
                "\t\t\t\"orgDefault2\": \"\",\n" +
                "\t\t\t\"orgDefault3\": \"\"\n" +
                "\t\t}],\n" +
                "\t\t\"userCredenceInfoPhone\": null,\n" +
                "\t\t\"titleVOList\": [],\n" +
                "\t\t\"userLoginType\": \"0\",\n" +
                "\t\t\"pbiList\": [],\n" +
                "\t\t\"certList\": [],\n" +
                "\t\t\"regSource\": \"1\",\n" +
                "\t\t\"userIdType\": \"SF\",\n" +
                "\t\t\"userCredenceInfoCard\": null,\n" +
                "\t\t\"userAttrList\": [{\n" +
                "\t\t\t\"aiDefaultValue\": \"\",\n" +
                "\t\t\t\"isPermitUpdate\": 1,\n" +
                "\t\t\t\"aiTypeComplex\": \"\",\n" +
                "\t\t\t\"aiType\": \"text\",\n" +
                "\t\t\t\"aiIsComplex\": 0,\n" +
                "\t\t\t\"valueList\": [],\n" +
                "\t\t\t\"aiEname\": \"zhudept\",\n" +
                "\t\t\t\"aiName\": \"主部门标识\",\n" +
                "\t\t\t\"asCode\": \"\",\n" +
                "\t\t\t\"isEmpty\": 0,\n" +
                "\t\t\t\"aiTypeSimply\": \"\",\n" +
                "\t\t\t\"valueVOList\": [],\n" +
                "\t\t\t\"aiFlowno\": \"e6bbc7b8fb5d4bb3a026a67e7fcb1c2d\",\n" +
                "\t\t\t\"aiDescribe\": \"\",\n" +
                "\t\t\t\"aiDefault2\": \"\",\n" +
                "\t\t\t\"aiDefault1\": \"\",\n" +
                "\t\t\t\"aiDefault3\": \"\",\n" +
                "\t\t\t\"asName\": \"\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"aiDefaultValue\": \"\",\n" +
                "\t\t\t\"isPermitUpdate\": 1,\n" +
                "\t\t\t\"aiTypeComplex\": \"\",\n" +
                "\t\t\t\"aiType\": \"text\",\n" +
                "\t\t\t\"aiIsComplex\": 0,\n" +
                "\t\t\t\"valueList\": [],\n" +
                "\t\t\t\"aiEname\": \"duty\",\n" +
                "\t\t\t\"aiName\": \"职务\",\n" +
                "\t\t\t\"asCode\": \"\",\n" +
                "\t\t\t\"isEmpty\": 0,\n" +
                "\t\t\t\"aiTypeSimply\": \"\",\n" +
                "\t\t\t\"valueVOList\": [],\n" +
                "\t\t\t\"aiFlowno\": \"4ea2bf53ec1b433b92c2d2b1c9ddcb57\",\n" +
                "\t\t\t\"aiDescribe\": \"\",\n" +
                "\t\t\t\"aiDefault2\": \"\",\n" +
                "\t\t\t\"aiDefault1\": \"\",\n" +
                "\t\t\t\"aiDefault3\": \"\",\n" +
                "\t\t\t\"asName\": \"\"\n" +
                "\t\t}],\n" +
                "\t\t\"cloudSignFlag\": 0,\n" +
                "\t\t\"orglist\": [{\n" +
                "\t\t\t\"orgFlowno\": \"0f119656d0834000873fb56caf8f54c1\",\n" +
                "\t\t\t\"orgCode\": \"10011001\",\n" +
                "\t\t\t\"orgNumber\": \"defaultOrg\",\n" +
                "\t\t\t\"utoFlowno\": \"35791870e99f456c9c6a1e7f55c8dbdd\",\n" +
                "\t\t\t\"orgName\": \"龙华区政府\",\n" +
                "\t\t\t\"uniqueId\": \"3b4a1a805cd9415bb99f3d0c40c5d448\",\n" +
                "\t\t\t\"userType\": 1\n" +
                "\t\t}],\n" +
                "\t\t\"unitName\": \"\",\n" +
                "\t\t\"roles\": [],\n" +
                "\t\t\"userStatus\": 1,\n" +
                "\t\t\"credenceList\": [{\n" +
                "\t\t\t\"certNumber\": \"\",\n" +
                "\t\t\t\"certUniqueid\": \"\",\n" +
                "\t\t\t\"userInfoVO\": null,\n" +
                "\t\t\t\"creatTime\": \"\",\n" +
                "\t\t\t\"pwdModifiedDate\": \"2020-01-10 12:28:51\",\n" +
                "\t\t\t\"userType\": 1,\n" +
                "\t\t\t\"credenceState\": 1,\n" +
                "\t\t\t\"uniqueid\": \"3b4a1a805cd9415bb99f3d0c40c5d448\",\n" +
                "\t\t\t\"uciDefault2\": \"4KGv472eka8NxTAnJTAQsw==\",\n" +
                "\t\t\t\"credenceFlowno\": \"21cf3587a32d4df5adbc3676dcd807bc\",\n" +
                "\t\t\t\"pwdLevel\": \"N\",\n" +
                "\t\t\t\"uciDefault3\": \"\",\n" +
                "\t\t\t\"credenceType\": \"\",\n" +
                "\t\t\t\"uciDefault1\": \"0\",\n" +
                "\t\t\t\"chineseLoginName\": \"\",\n" +
                "\t\t\t\"credenceClass\": \"Credence_002\",\n" +
                "\t\t\t\"certStatus\": \"\",\n" +
                "\t\t\t\"credenceAppend\": \"{SSHA256}AHMhUMqLT8CjJcnYIiZaCbyRrzQ2KbXkZyw/BDg/jt54Q2tQK2ZWNXBkNThCWkhkR05CNExRPT0=\",\n" +
                "\t\t\t\"loginName\": \"zhangwen2\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"certNumber\": \"\",\n" +
                "\t\t\t\"certUniqueid\": \"\",\n" +
                "\t\t\t\"userInfoVO\": null,\n" +
                "\t\t\t\"creatTime\": \"\",\n" +
                "\t\t\t\"pwdModifiedDate\": \"2020-01-10 12:28:51\",\n" +
                "\t\t\t\"userType\": 1,\n" +
                "\t\t\t\"credenceState\": 1,\n" +
                "\t\t\t\"uniqueid\": \"3b4a1a805cd9415bb99f3d0c40c5d448\",\n" +
                "\t\t\t\"uciDefault2\": \"4KGv472eka8NxTAnJTAQsw==\",\n" +
                "\t\t\t\"credenceFlowno\": \"96440ab2cef4496bbb9364ec26867dab\",\n" +
                "\t\t\t\"pwdLevel\": \"N\",\n" +
                "\t\t\t\"uciDefault3\": \"\",\n" +
                "\t\t\t\"credenceType\": \"\",\n" +
                "\t\t\t\"uciDefault1\": \"\",\n" +
                "\t\t\t\"chineseLoginName\": \"\",\n" +
                "\t\t\t\"credenceClass\": \"Credence_006\",\n" +
                "\t\t\t\"certStatus\": \"\",\n" +
                "\t\t\t\"credenceAppend\": \"{SSHA256}AHMhUMqLT8CjJcnYIiZaCbyRrzQ2KbXkZyw/BDg/jt54Q2tQK2ZWNXBkNThCWkhkR05CNExRPT0=\",\n" +
                "\t\t\t\"loginName\": \"张文2\"\n" +
                "\t\t}],\n" +
                "\t\t\"userCredenceInfoVOPwd\": null\n" +
                "\t}\n" +
                "}";

        Map<String, Object> map = JSONObject.parseObject(token).getJSONObject("info").getInnerMap();
        System.out.println(map.get("userIdCode"));
        System.out.println(map.get("userName"));
        System.out.println(map.get("userPhone"));


    }
}