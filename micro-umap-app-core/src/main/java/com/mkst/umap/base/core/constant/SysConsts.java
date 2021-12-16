package com.mkst.umap.base.core.constant;

/**
 * <pre>
 * 	预约阅卷系统常量
 *  Created by gin.lee on 2019-04-22.
 * </pre>
 */
public class SysConsts {
	
	public static final String COOKIE_NAME_OPENID = "openid";
	public static final String COOKIE_NAME_SOURCE = "source";

	/**
	 *	用户来源</br>
	 *	WELINK：华为Welink，MP：微信公众号
	 */
	public enum UserSource {
		WELINK, MP;

		@Override
        public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 *	短信验证码类型</br>
	 * 	USER_CERTIFICATION：用户认证
	 */
	public enum SmsValidateCodeType {
		USER_CERTIFICATION;
		
		@Override
        public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	/**
	 *	序列类型</br>
	 */
	public enum GenerationType {
		USER_CODE("1", "用户编号"), NOTARY_CODE("2", "公证编号");
		
		private String code;
		private String name;

		GenerationType(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public static String getName(String code) {
			for (GenerationType obj : GenerationType.values()) {
				if (obj.getCode().equals(code)) {
					return obj.name;
				}
			}
			return null;
		}
		
		@Override
        public String toString(){
			return super.toString().toLowerCase() ;
		}
	}
	
	
}
