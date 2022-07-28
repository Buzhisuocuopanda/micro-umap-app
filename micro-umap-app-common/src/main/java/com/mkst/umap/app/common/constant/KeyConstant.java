package com.mkst.umap.app.common.constant;

/**
 * @author Baldwin
 * @描述 字典键值常量类
 * @创建时间 2020-06-19
 * @修改人和其它信息
 */
public interface KeyConstant {

    String EVENT_ZERO = "0";

    /** 资源状态 */
    /**
     * 可用
     */
    String RESOURCES_STATUS_AVAILABLE = "0";
    /**
     * 不可用
     */
    String RESOURCES_STATUS_DISAVAILABLE = "1";

    /**  */


    /** 提审室当前状态键值 */
    /**
     * 空闲的
     */
    String RESOURCES_STATUS_NOW_FREE = "0";
    /**
     * 使用中
     */
    String RESOURCES_STATUS_NOW_BUSY = "1";

    /** 提审申请预约状态 */
    /**
     * 待审核
     */
    String EVENT_AUDIT_STATUS_WAIT = "0";
    /**
     * 已审核
     */
    String EVENT_AUDIT_STATUS_PASS = "1";
    /**
     * 未通过
     */
    String EVENT_AUDIT_STATUS_FAIL = "2";

    String IS_CAN_AUDIT = "1";

    /**
     * 事件状态
     * 0:未开始
     * 1:进行中
     * 2:已结束
     */
    String EVENT_PROGRESS_STATUS_WAITING = "0";
    String EVENT_PROGRESS_STATUS_ONGOING = "1";
    String EVENT_PROGRESS_STATUS_FINISHED = "2";
    String EVENT_ALL = "3";

    /**
     * 事件状态
     * 0:正常
     * 1:已取消
     */
    String EVENT_IS_CANCEL_FALSE = "0";
    String EVENT_IS_CANCEL_TRUE = "1";

    /**
     * 全部提审室已经满
     * 0:不是
     * 1:是
     */
    String ARRAIGN_ROOM_FULL_NO = "0";
    String ARRAIGN_ROOM_FULL_YES = "1";

    /**
     * 场所资源类型
     * 0：提审室 1：接待室
     */
    String ARENA_TYPE_ARRAIGN_ROOM = "0";
    String ARENA_TYPE_RECEPTION_RECEPTION_ROOM = "1";
    String ARENA_TYPE_MEETING_ROOM = "5";
    /**
     * 发送短信类型
     * 1：注册 2：重置密码
     */
    String SMS_SEND_REGISTERED = "1";
    String SMS_SEND_RESET_PASSWORD = "2";

    String LAWYER_CODE = "lawyer";

    String SEARCH_MODE_YEAR = "year";


    String ROLES = "roles";
    String LOGIN_NAME = "loginName";

    String ARRAIGN_DATA_FILE_NAME = "arraignData.json";

    /**
     * 提审预约放号时间天数
     */
    String ARRAIGN_APPOINTMENT_LIMIT_DAY = "arraign_appointment_limit_day";

    /**
     * 提升预约放号时间key
     */
    String ARRAIGN_APPOINTMENT_OPEN_TIME_KEY = "arraign_appointment_open_number_time";

    /**
     * 提审预约天数是否忽略假日
     */
    String ARRAIGN_APPOINTMENT_IGNORE_HOLIDAY_KEY = "arraign_appointment_ignore_holiday";
    /**
     * 备勤间预约放号时间天数
     */
    String BACKUPROOM_APPOINTMENT_LIMIT_DAY = "backuproom_appointment_limit_day";
    
    /**
     * 备勤间预约放号时间key
     */
    String BACKUPROOM_APPOINTMENT_OPEN_TIME_KEY = "backuproom_appointment_open_number_time";
    
    /**
     * 备勤间预约天数是否忽略假日
     */
    String BACKUPROOM_APPOINTMENT_IGNORE_HOLIDAY_KEY = "backuproom_appointment_ignore_holiday";

    /**
     * 备勤间预约放号第二天结束时间
     */
    String BACKUPROOM_APPOINTMENT_LIMIT_HOUR = "backuproom_appointment_limit_hour";
}
