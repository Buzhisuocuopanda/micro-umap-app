package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.SysUserOld;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SysUserOldMapper
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/6 0006 下午 11:50
 */
@Repository
public interface SysUserOldMapper {

    public SysUserOld selectById(Long id);

}
