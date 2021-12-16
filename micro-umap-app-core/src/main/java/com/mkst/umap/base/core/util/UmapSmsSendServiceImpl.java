package com.mkst.umap.base.core.util;

import com.mkst.mini.systemplus.basic.domain.MsgPush;
import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import com.mkst.mini.systemplus.basic.send.MsgSendService;
import com.mkst.mini.systemplus.common.utils.Exceptions;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 发送短信服务实现
 *
 * @author liuzhiping
 * @date 2020年11月11日
 */
@Service
public class UmapSmsSendServiceImpl implements MsgSendService {

    private static final Logger logger = LoggerFactory.getLogger(UmapSmsSendServiceImpl.class);

    @Override
    public void sendMessage(MsgPush msgPush) {
        try {
            logger.info("发送短信");
            // 发送短信
            SmsMsgContent content = msgPush.parseMsgContent(SmsMsgContent.class);
            YxtSmsConfig.getYxtSmsService().sendMsg(msgPush.getReceiveCode(), content.getContent());
            //发送成功
            msgPush.setPushDate(new Date());
            msgPush.setReadStatus(MsgPush.READ_STATUS_READ);
            msgPush.setPushStatus(MsgPush.PUSH_STATUS_SUCCESS);
            msgPush.addPushReturnContent("发送成功");
        } catch (Exception ex) {
            logger.error("发送短信失败！ ", ex);
            msgPush.setPushDate(new Date());
            msgPush.setReadStatus(MsgPush.READ_STATUS_NONE);
            msgPush.setPushStatus(MsgPush.PUSH_STATUS_FAIL);
            msgPush.addPushReturnContent(Exceptions.getStackTraceAsString(ex));
        }
    }
}
