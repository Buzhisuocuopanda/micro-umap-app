package com.mkst.umap.app.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.domain.IntellectualProperty;
import com.mkst.umap.app.admin.dto.intellectualproperty.IntelProDetailDto;
import com.mkst.umap.app.admin.dto.intellectualproperty.IntelProInfoWebDto;
import com.mkst.umap.app.admin.mapper.IntellectualPropertyMapper;
import com.mkst.umap.app.admin.service.IIntellectualPropertyService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识产权 服务层实现
 *
 * @author wangyong
 * @date 2020-08-12
 */
@Service
public class IntellectualPropertyServiceImpl implements IIntellectualPropertyService {

	@Autowired
	private IntellectualPropertyMapper intellectualPropertyMapper;

	/**
	 * 查询知识产权信息
	 *
	 * @param id 知识产权ID
	 * @return 知识产权信息
	 */
	@Override
	public IntellectualProperty selectIntellectualPropertyById(Long id) {
		return intellectualPropertyMapper.selectIntellectualPropertyById(id);
	}

	/**
	 * 查询知识产权列表
	 *
	 * @param intellectualProperty 知识产权信息
	 * @return 知识产权集合
	 */
	@Override
	public List<IntellectualProperty> selectIntellectualPropertyList(IntellectualProperty intellectualProperty) {
		return intellectualPropertyMapper.selectIntellectualPropertyList(intellectualProperty);
	}

	@Override
	public List<IntelProInfoWebDto> selectIntellectualPropertyWebList(IntellectualProperty intellectualProperty) {
		return intellectualPropertyMapper.selectWebInfo(intellectualProperty);
	}

	/**
	 * 新增知识产权
	 *
	 * @param intellectualProperty 知识产权信息
	 * @return 结果
	 */
	@Override
	public int insertIntellectualProperty(IntellectualProperty intellectualProperty) {
		return intellectualPropertyMapper.insertIntellectualProperty(intellectualProperty);
	}

	/**
	 * 修改知识产权
	 *
	 * @param intellectualProperty 知识产权信息
	 * @return 结果
	 */
	@Override
	public int updateIntellectualProperty(IntellectualProperty intellectualProperty) {
		return intellectualPropertyMapper.updateIntellectualProperty(intellectualProperty);
	}

	/**
	 * 删除知识产权对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteIntellectualPropertyByIds(String ids) {
		return intellectualPropertyMapper.deleteIntellectualPropertyByIds(Convert.toStrArray(ids));
	}

	@Override
	public IntelProDetailDto selectIntelProDetailById(Long id) {

		IntelProDetailDto result = transObject(this.selectIntellectualPropertyById(id), IntelProDetailDto.class);

		List<SysFileUpload> uploadList = FileUploadExtendUtils.findFileUpload(id.toString(), BusinessTypeEnum.UMAP_INTELLECTUAL_PROPERTY.getValue());

		if (uploadList == null || uploadList.isEmpty()){
			result.setFileList(new ArrayList<>());
		}else {
			ArrayList<String> fileList = new ArrayList<>();
			uploadList.stream().forEach(fileUpload ->{
				fileUpload.getFileEntityList().stream().forEach(file -> {
					fileList.add(file.getFilePath());
				});
			});
			result.setFileList(fileList);
		}
		return result;
	}

	public <T> T transObject(Object ob, Class<T> clazz) {
		String oldOb = JSON.toJSONString(ob);
		return JSON.parseObject(oldOb, clazz);
	}

}
