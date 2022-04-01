package com.mkst.umap.app.admin.task;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mkst.mini.systemplus.common.constant.UserConstants;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.system.domain.SysConfig;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysConfigMapper;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.util.Hex;


@Component("UserSyncTask")
public class UserSyncTask {
    private static final String username = "platform";
    private static final String password = "platform@123456";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

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
    @Value("${userSync.mini}")
    private String miniJDBC;

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
        databaseMap.put("mini",miniJDBC);

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
                    	String mobile = user.getPhonenumber();
                    	String email = user.getEmail();
                    	String userType = user.getUserType();
                    	if("mini".equals(m.getKey())) {
                    		mobile = encryptData(mobile);
                    		email = encryptData(email);
                    		userType = UserConstants.REGISTER_USER_TYPE.equals(userType) ? "01" : userType;
                    	}
                        stmt.setLong(1, user.getUserId());
                        stmt.setObject(2, user.getDeptId());
                        stmt.setString(3, user.getLoginName());
                        stmt.setString(4, user.getUserName());
                        stmt.setString(5, userType);
                        stmt.setString(6, email);
                        stmt.setString(7, mobile);
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
                
                if("mini".equals(m.getKey())) {
                	String userInfoSql = "INSERT INTO sys_user_info(user_id, id_card_type, id_card) VALUES (?, ?, ?)";
                	stmt = con.prepareStatement(userInfoSql);
                	for (SysUser user : sysUsers) {
                        try {
                        	String idcard = user.getUserInfo() != null ? user.getUserInfo().getIdCard() : null;
                        	if(idcard != null) {
                        		idcard = encryptData(idcard);
                        	}
                            stmt.setLong(1, user.getUserId());
                            stmt.setString(2, "10");
                            stmt.setString(3, idcard);
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
    
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException{
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //AES 要求密钥长度为 128
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kg.init(128, random);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }
    
    private String encryptData(String value) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
		IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKey("micro12sdf3211034adff32ddf9993system"));
		byte[] content = value.getBytes("UTF-8");
		byte[] encryptData = cipher.doFinal(content);
		return Hex.bytesToHexString(encryptData);
	}
}
