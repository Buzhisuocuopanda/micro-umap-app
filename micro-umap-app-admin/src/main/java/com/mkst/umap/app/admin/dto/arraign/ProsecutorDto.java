package com.mkst.umap.app.admin.dto.arraign;

import com.mkst.mini.systemplus.api.web.dto.SysDeptDto;
import com.mkst.mini.systemplus.api.web.dto.SysRoleDto;
import com.mkst.mini.systemplus.system.domain.SysUserInfo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ProsecutorDto
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/5/26 0026 下午 5:39
 */
@Data
public class ProsecutorDto {

    private Long userId;
    private Long deptId;
    private String loginName;
    private String userName;
    private String userNamePinyin;
    private SysDeptDto dept;
    private List<SysRoleDto> roles;
    private SysUserInfo userInfo;

}
