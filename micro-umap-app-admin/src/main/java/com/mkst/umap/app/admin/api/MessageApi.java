package com.mkst.umap.app.admin.api;

import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用内即时通知接口
 *
 * @author liyifan
 * @since 2022-02-09 09:24:37
 */
@Slf4j
@Api("应用内即时通知接口")
@RestController
@RequestMapping("/api/message")
public class MessageApi extends BaseApi {
    /**
     * @param content 消息
     * @return 操作结果（发送成功/失败）
     */
    @PostMapping("/sendAppMsg")
    @ApiOperation("发送即时通知")
    @ResponseBody
    @Login
    public AjaxResult sendMessage(MessageApi.MsgContent content) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("bizKey", content.getBusinessId().toString());
            params.put("bizType", BusinessTypeEnum.valueOf(content.getBusinessType()).getValue());

            AppMsgContent msgContent = new AppMsgContent();
            msgContent.setTitle(content.getTitle());
            msgContent.setContent(content.getContent());
            msgContent.setParams(params);
            MsgPushUtils.push(msgContent, params.get("bizKey"), params.get("bizType"), content.getTargetUserName());
            MsgPushUtils.getMsgPushTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("服务器错误");
        }
        return AjaxResult.success("信息发送成功");
    }

    @Data
    static class MsgContent {
        /**
         * 目标用户名
         */
        private String targetUserName;
        /**
         * 通知事件Id
         */
        private Long businessId;
        /**
         * 通知事件类型
         */
        private String businessType;
        /**
         * 通知消息标题
         */
        private String title;
        /**
         * 通知消息内容
         */
        private String content;
    }
}