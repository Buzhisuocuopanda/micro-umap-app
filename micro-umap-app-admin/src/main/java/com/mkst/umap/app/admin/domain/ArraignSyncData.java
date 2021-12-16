package com.mkst.umap.app.admin.domain;

import lombok.Data;

/**
 * @ClassName ArraignSyncData
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/3 0003 下午 6:02
 */
@Data
public class ArraignSyncData {

    private Long applyer;
    private Long applytime;
    private Long approver;
    private String birthday;
    private String certificateId;
    private String crimetype;
    private String criminal;
    private String descr;
    private int roomtype;
    private int status;
    private Long time1;
    private Long time2;
    private Long id;
    private String undertaker;
    private String prosecuteName;

    public int hashCode(){
        if(id == null){
            return 0;
        }
        return id.hashCode();
    }

    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof ArraignSyncData){
            ArraignSyncData asd = (ArraignSyncData)obj;
            if(this.getId() != null){
                return this.getId().equals(asd.getId());
            }
        }
        return false;
    }

}
