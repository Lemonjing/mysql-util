package com.ryan.pojo;

/**
 * Baidu地址信息
 *
 * @author nothankyou
 * @date 2017-02-03 22:12:38
 */
public class BaiduPlace implements Comparable<BaiduPlace> {
	// 名称
	private String name;

	// 详细地址
	private String address;

	// 纬度
	private String lat;

	// 经度
	private String lng;

	// 联系电话
	private String telephone;

	// 距离
	private int distance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(BaiduPlace baiduPlace) {
		return this.distance - baiduPlace.getDistance();
	}
}
