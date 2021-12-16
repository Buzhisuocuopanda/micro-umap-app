package com.mkst.umap.app.common.enums;

/**
 * @author wangyong
 * @description 会议审核进度
 * @date 11:45 2020-11-09
 */
public enum MeetingAuditProgressEnum {
    FINISHED(-1,"","结束"),
    DEFAULT(0,"","提交"),
    INTERNAL(1,"nqry","内勤人员"),
    AFFAIRS(2,"hwfzr","会务负责人");

    private Integer progressCode;
    private String roleCode;
    private String progressName;

    MeetingAuditProgressEnum(Integer progressCode, String roleCode, String progressName) {
        this.progressCode = progressCode;
        this.roleCode = roleCode;
        this.progressName = progressName;
    }

    public static String getRoleName(Integer progressCode){
        for (MeetingAuditProgressEnum progress : MeetingAuditProgressEnum.values()) {
            if (progress.getProgressCode().equals(progressCode)) {
                return progress.getProgressName();
            }
        }
        return null;
    }

    public static String getRoleCodeByProgressCode(Integer progressCode){
        for (MeetingAuditProgressEnum progress : MeetingAuditProgressEnum.values()) {
            if (progress.getProgressCode().equals(progressCode)) {
                return progress.getRoleCode();
            }
        }
        return null;
    }

    public static String getNextRoleCode(Integer progressCode){
        return getRoleCodeByProgressCode(progressCode + 1);
    }


    public Integer getProgressCode() {
        return progressCode;
    }

    public void setProgressCode(Integer progressCode) {
        this.progressCode = progressCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getProgressName() {
        return progressName;
    }

    public void setProgressName(String progressName) {
        this.progressName = progressName;
    }
}
