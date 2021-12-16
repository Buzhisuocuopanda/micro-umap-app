package com.mkst.umap.app.admin.task;

import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.system.domain.SysConfig;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysConfigMapper;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component("UserSyncTask")
public class UserSyncTask {
    private static final String username = "platform";
    private static final String password = "platform@123456";


    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysConfigMapper configMapper;

    @Value("${userSync.im}")
    private String imJDBC;

    @Value("${userSync.webcase}")
    private String webcaseJDBC;

    @Value("${userSync.webcard}")
    private String webcardJDBC;

    public void syncUser() throws Exception {

        SysConfig config = new SysConfig();
        config.setConfigKey("sync_user");
        SysConfig retConfig = configMapper.selectConfig(config);

        String dateVaule = DateUtils.datePath();

        SysUser sysUser = new SysUser();
        Map<String, Object> map = new HashMap<>();
        map.put("beginTime",retConfig.getConfigValue());
        sysUser.setParams(map);

        List<SysUser> sysUsers = sysUserService.selectUserList(sysUser);

        Map<String,String> databaseMap = new HashMap<>();
        databaseMap.put("im",imJDBC);
        databaseMap.put("webcase",webcaseJDBC);
        databaseMap.put("webcard",webcardJDBC);

        for (Map.Entry<String, String> m : databaseMap.entrySet()) {
            Connection con = null;
            PreparedStatement stmt = null;
            try {
                con = getConnection(m.getValue());
                String sql = "insert into sys_user(user_id,dept_id,login_name,user_name" +
                        ",user_type,email,phonenumber,sex,avatar,password" +
                        ",salt,create_by,remark,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);

                for (SysUser user : sysUsers) {
                    try {
                        stmt.setLong(1, user.getUserId());
                        stmt.setObject(2, user.getDeptId());
                        stmt.setString(3, user.getLoginName());
                        stmt.setString(4, user.getUserName());
                        stmt.setString(5, user.getUserType());
                        stmt.setString(6, user.getEmail());
                        stmt.setString(7, user.getPhonenumber());
                        stmt.setString(8, user.getSex());
                        stmt.setString(9, user.getAvatar());
                        stmt.setString(10, user.getPassword());
                        stmt.setString(11, user.getSalt());
                        stmt.setString(12, user.getCreateBy());
                        stmt.setString(13, user.getRemark());
                        stmt.setObject(14, user.getCreateTime());
                        stmt.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }

                if("im".equals(m.getKey())) {
                    String sqlIm = "insert into tb_im_user(user_id,avatar,nick,status,email," +
                            "password,create_by,dept_id,login_name) values(?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sqlIm);
                    for (SysUser user : sysUsers) {
                        try {
                            stmt.setLong(1, user.getUserId());
                            stmt.setString(2, user.getAvatar());
                            stmt.setString(3, user.getUserName());
                            stmt.setString(4, user.getStatus());
                            stmt.setString(5, user.getEmail());
                            stmt.setString(6, user.getPassword());
                            stmt.setString(7, user.getCreateBy());
                            stmt.setObject(8, user.getDeptId());
                            stmt.setString(9, user.getLoginName());
                            stmt.executeUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(stmt, con);
            }
        }
        retConfig.setConfigValue(dateVaule);
        configMapper.updateConfig(retConfig);
    }
    //创建数据库的连接
    public static Connection getConnection(String connectionURL) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return   DriverManager.getConnection(connectionURL,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //关闭数据库的连接
    public static void close(Statement stmt, Connection con) throws SQLException {
        if(stmt!=null){stmt.close();}
        if(con!=null){ con.close();}
    }
}
