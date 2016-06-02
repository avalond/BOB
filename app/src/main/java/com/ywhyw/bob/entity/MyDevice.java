package com.ywhyw.bob.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "device")
public class MyDevice extends BaseEntity{
	@Column(name = "id",isId = true,autoGen = true)
	private int id;
	@Column(name = "deviceName")
	private String deviceName;
	@Column(name = "deviceAddress")
	private String deviceAddress;
	@Column(name = "isConnected")
	private boolean isConnected;
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "MyDevice [id=" + id + ", deviceName=" + deviceName + ", deviceAddress=" + deviceAddress
				+ ", isConnected=" + isConnected + "]";
	}
	
}
