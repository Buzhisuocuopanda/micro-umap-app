package com.mkst.umap.app.admin.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysRoleMapper;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.service.IApplyInfoService;
import com.mkst.umap.app.common.enums.RoleKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时发送清洁消息
 * @author 吴泽昊
 * @data 2022/7/28.
 */
@Component("sendCleanSmsTask")
public class SendCleanSmsTask {

    @Autowired
    private IApplyInfoService applyInfoService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    public void sendCleanMsg() {
        Date date = new Date();
        StringBuilder msg = new StringBuilder();
        msg.append("【龙华区人民检察院】【门禁预约】您好，昨晚");
        date = DateUtil.offsetDay(date, -1);
        date = DateUtil.parse(DateUtil.format(date,"yyyy-MM-dd"),"yyyy-MM-dd");


        SysUser selectUser = new SysUser();
        SysRole sysRole = getSysRole();
        selectUser.setRoleIds(new Long[]{sysRole.getRoleId()});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        List<String> roomStrs = applyInfoService.selectApplyUseRoomList(date);
        if (CollectionUtil.isNotEmpty(roomStrs)) {
            SmsMsgContent msgContent = new SmsMsgContent();
            msgContent.setTitle("备勤间预约成功通知");
            roomStrs.forEach(str ->{
                msg.append("【" + str + "】");
            });
            msg.append("房间已使用，请立即进行打扫清洁，谢谢");
            msgContent.setContent(msg.toString());
            Date finalDate = date;
            sysUsers.forEach(user ->{
                MsgPushUtils.push(msgContent, StrUtil.toString(DateUtil.dayOfMonth(finalDate)), "umap_backup_clean", "[CODE]"+user.getPhonenumber());
            });
            MsgPushUtils.getMsgPushTask().execute();
        }

    }

    private SysRole getSysRole(){
        return sysRoleMapper.checkRoleKeyUnique(RoleKeyEnum.CLEAN_PERSON.getValue());
    }
}
