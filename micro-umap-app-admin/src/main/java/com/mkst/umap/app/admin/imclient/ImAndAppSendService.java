package com.mkst.umap.app.admin.imclient;

import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.basic.api.dto.MsgPushDto;
import com.mkst.mini.systemplus.basic.domain.MsgPush;
import com.mkst.mini.systemplus.basic.send.Impl.AppSendService;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.domain.ImNoticeUser;
import com.mkst.umap.app.admin.imclient.domain.ChatBody;
import com.mkst.umap.app.admin.service.IImNoticeUserService;
import com.mkst.umap.app.imclient.ImClientChannelContext;
import com.mkst.umap.app.imclient.JimClient;
import com.mkst.umap.app.imclient.JimClientAPI;
import com.mkst.umap.app.imclient.config.ImClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.jim.core.packets.ChatType;
import org.jim.core.packets.Command;
import org.jim.core.packets.LoginReqBody;
import org.jim.core.tcp.TcpPacket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.client.ReconnConf;
import org.tio.core.Node;

/**
 * @ClassName ImSendService
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/4/8 0008 下午 3:02
 */
@Component("imAndAppSendService")
@Slf4j
public class ImAndAppSendService extends AppSendService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private IImNoticeUserService imNoticeUserService;

    private ImClientChannelContext imClientChannelContext = null;

    private String imServerIp = SysConfigUtil.getKey("im_server_ip");

    private int imServerPort = Integer.parseInt(SysConfigUtil.getKey("im_server_port"));

    public ImAndAppSendService(){
        init();
    }

    private void init(){
        log.info("初始化IM客户端。。。");
        //Node serverNode = new Node("localhost", 36060);
        Node serverNode = new Node(imServerIp, imServerPort);
        //构建客户端配置信息
        ImClientConfig imClientConfig = ImClientConfig.newBuilder()
                //客户端业务回调器,不可以为NULL
                .clientHandler(new HelloImClientHandler())
                //客户端事件监听器，可以为null，但建议自己实现该接口
                .clientListener(new HelloImClientListener())
                //心跳时长不设置，就不发送心跳包
                //.heartbeatTimeout(5000)
                //断链后自动连接的，不想自动连接请设为null
                .reConnConf(new ReconnConf(5000L))
                .build();
        //生成客户端对象;
        JimClient jimClient = new JimClient(imClientConfig);
        //连接服务端
        try {
            imClientChannelContext = jimClient.connect(serverNode);
            log.info("IM客户端初始化成功");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public void sendMessage(MsgPush msgPush) {
        //查看是否发送过
        boolean needSendIm = true;
        if("2".equals(msgPush.getPushStatus())){
            needSendIm = false;
        }
        super.sendMessage(msgPush);
        //发送失败一般是app推送  im不再处理
        if(!needSendIm){
            return;
        }
        try {
            sendIm(msgPush);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    protected void sendIm(MsgPush msgPush) throws Exception {
        MsgPushDto dto = new MsgPushDto();
        BeanUtils.copyProperties(msgPush , dto);
        SysUser toUser = userService.selectUserByLoginName(msgPush.getReceiveUserCode());
        if(toUser == null){
            log.info("IM接受者未找到，接受者代码："+ msgPush.getReceiveUserCode());
            return;
        }
        ImNoticeUser imNoticeUser = imNoticeUserService.selectImNoticeUserByTypeKey(msgPush.getBizType());
        if(imNoticeUser == null){
            log.info("IM发送者未找到，发送者业务类型："+ msgPush.getBizType());
            return;
        }
        SysUser sendUser = userService.selectUserById(imNoticeUser.getUserId().longValue());
        if(sendUser == null){
            log.info("IM发送者未找到，发送者ID："+ imNoticeUser.getUserId());
            return;
        }
        send(dto,toUser.getUserId(),imNoticeUser,sendUser);
    }

    private void send(MsgPushDto dto , Long to , ImNoticeUser imNoticeUser , SysUser sendUser ){
        ChatBody chatBody = ChatBody.newBuilder()
                .from(imNoticeUser.getUserId()+"")
                .to(to+"")
                .msgType(7)
                .chatType(ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .content(JSONObject.toJSONString(dto))
                .isTop(imNoticeUser.getIsTop())
                .topOrder(imNoticeUser.getTopOrder())
                .fromNick(sendUser.getUserName())
                .avatar(imNoticeUser.getAvatar())
                .build();
        log.info("send im notice info:{}",JSONObject.toJSON(chatBody));
        TcpPacket chatPacket = new TcpPacket(Command.COMMAND_CHAT_REQ,chatBody.toByte());
        JimClientAPI.send(imClientChannelContext, chatPacket);
    }
}
