package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 地图位置表 umap_map_location
 * 
 * @author wangyong
 * @date 2020-07-20
 */
public class MapLocation extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 位置id */
	private Long locationId;
	/** 位置名称 */
	private String locationName;
	/** 经度 */
	private Integer longitude;
	/** 纬度 */
	private Integer latitude;

	public void setLocationId(Long locationId) 
	{
		this.locationId = locationId;
	}

	public Long getLocationId() 
	{
		return locationId;
	}
	public void setLocationName(String locationName) 
	{
		this.locationName = locationName;
	}

	public String getLocationName() 
	{
		return locationName;
	}
	public void setLongitude(Integer longitude) 
	{
		this.longitude = longitude;
	}

	public Integer getLongitude() 
	{
		return longitude;
	}
	public void setLatitude(Integer latitude) 
	{
		this.latitude = latitude;
	}

	public Integer getLatitude() 
	{
		return latitude;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("locationId", getLocationId())
            .append("locationName", getLocationName())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .toString();
    }
}
