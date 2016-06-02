package com.ywhyw.bob.entity;

public class PTStatus {
	private boolean statusZhenJiuIsOpen;
	private int statusZhenJiuIntensity;
	private boolean statusZhenJiuIsClock;
	private int statusZhenJiuClockTime;
	
	private boolean statusAnMoIsOpen;
	private int statusAnMoIntensity;
	private boolean statusAnMoIsClock;
	private int statusAnMoClockTime;
	
	private boolean statusLiLiaoIsOpen;
	private int statusLiLiaoIntensity;
	private boolean statusLiLiaoIsClock;
	private int statusLiLiaoClockTime;
	
	private boolean statusYueLiaoIsOpen;
	private int statusYueLiaoIntensity;
	private int statusYueLiaoFrequency;
	private boolean statusYueLiaoIsClock;
	private int statusYueLiaoClockTime;
	
	
	public boolean isStatusZhenJiuIsOpen() {
		return statusZhenJiuIsOpen;
	}
	public void setStatusZhenJiuIsOpen(boolean statusZhenJiuIsOpen) {
		this.statusZhenJiuIsOpen = statusZhenJiuIsOpen;
	}
	public int getStatusZhenJiuIntensity() {
		return statusZhenJiuIntensity;
	}
	public void setStatusZhenJiuIntensity(int statusZhenJiuIntensity) {
		this.statusZhenJiuIntensity = statusZhenJiuIntensity;
	}
	public boolean isStatusZhenJiuIsClock() {
		return statusZhenJiuIsClock;
	}
	public void setStatusZhenJiuIsClock(boolean statusZhenJiuIsClock) {
		this.statusZhenJiuIsClock = statusZhenJiuIsClock;
	}
	public int getStatusZhenJiuClockTime() {
		return statusZhenJiuClockTime;
	}
	public void setStatusZhenJiuClockTime(int statusZhenJiuClockTime) {
		this.statusZhenJiuClockTime = statusZhenJiuClockTime;
	}
	public boolean isStatusAnMoIsOpen() {
		return statusAnMoIsOpen;
	}
	public void setStatusAnMoIsOpen(boolean statusAnMoIsOpen) {
		this.statusAnMoIsOpen = statusAnMoIsOpen;
	}
	public int getStatusAnMoIntensity() {
		return statusAnMoIntensity;
	}
	public void setStatusAnMoIntensity(int statusAnMoIntensity) {
		this.statusAnMoIntensity = statusAnMoIntensity;
	}
	public boolean isStatusAnMoIsClock() {
		return statusAnMoIsClock;
	}
	public void setStatusAnMoIsClock(boolean statusAnMoIsClock) {
		this.statusAnMoIsClock = statusAnMoIsClock;
	}
	public int getStatusAnMoClockTime() {
		return statusAnMoClockTime;
	}
	public void setStatusAnMoClockTime(int statusAnMoClockTime) {
		this.statusAnMoClockTime = statusAnMoClockTime;
	}
	public boolean isStatusLiLiaoIsOpen() {
		return statusLiLiaoIsOpen;
	}
	public void setStatusLiLiaoIsOpen(boolean statusLiLiaoIsOpen) {
		this.statusLiLiaoIsOpen = statusLiLiaoIsOpen;
	}
	public int getStatusLiLiaoIntensity() {
		return statusLiLiaoIntensity;
	}
	public void setStatusLiLiaoIntensity(int statusLiLiaoIntensity) {
		this.statusLiLiaoIntensity = statusLiLiaoIntensity;
	}
	public boolean isStatusLiLiaoIsClock() {
		return statusLiLiaoIsClock;
	}
	public void setStatusLiLiaoIsClock(boolean statusLiLiaoIsClock) {
		this.statusLiLiaoIsClock = statusLiLiaoIsClock;
	}
	public int getStatusLiLiaoClockTime() {
		return statusLiLiaoClockTime;
	}
	public void setStatusLiLiaoClockTime(int statusLiLiaoClockTime) {
		this.statusLiLiaoClockTime = statusLiLiaoClockTime;
	}
	public boolean isStatusYueLiaoIsOpen() {
		return statusYueLiaoIsOpen;
	}
	public void setStatusYueLiaoIsOpen(boolean statusYueLiaoIsOpen) {
		this.statusYueLiaoIsOpen = statusYueLiaoIsOpen;
	}
	public int getStatusYueLiaoIntensity() {
		return statusYueLiaoIntensity;
	}
	public void setStatusYueLiaoIntensity(int statusYueLiaoIntensity) {
		this.statusYueLiaoIntensity = statusYueLiaoIntensity;
	}
	public int getStatusYueLiaoFrequency() {
		return statusYueLiaoFrequency;
	}
	public void setStatusYueLiaoFrequency(int statusYueLiaoFrequency) {
		this.statusYueLiaoFrequency = statusYueLiaoFrequency;
	}
	public boolean isStatusYueLiaoIsClock() {
		return statusYueLiaoIsClock;
	}
	public void setStatusYueLiaoIsClock(boolean statusYueLiaoIsClock) {
		this.statusYueLiaoIsClock = statusYueLiaoIsClock;
	}
	public int getStatusYueLiaoClockTime() {
		return statusYueLiaoClockTime;
	}
	public void setStatusYueLiaoClockTime(int statusYueLiaoClockTime) {
		this.statusYueLiaoClockTime = statusYueLiaoClockTime;
	}
	@Override
	public String toString() {
		return "PTStatus [statusZhenJiuIsOpen=" + statusZhenJiuIsOpen
				+ ", statusZhenJiuIntensity=" + statusZhenJiuIntensity
				+ ", statusZhenJiuIsClock=" + statusZhenJiuIsClock
				+ ", statusZhenJiuClockTime=" + statusZhenJiuClockTime
				+ ", statusAnMoIsOpen=" + statusAnMoIsOpen
				+ ", statusAnMoIntensity=" + statusAnMoIntensity
				+ ", statusAnMoIsClock=" + statusAnMoIsClock
				+ ", statusAnMoClockTime=" + statusAnMoClockTime
				+ ", statusLiLiaoIsOpen=" + statusLiLiaoIsOpen
				+ ", statusLiLiaoIntensity=" + statusLiLiaoIntensity
				+ ", statusLiLiaoIsClock=" + statusLiLiaoIsClock
				+ ", statusLiLiaoClockTime=" + statusLiLiaoClockTime
				+ ", statusYueLiaoIsOpen=" + statusYueLiaoIsOpen
				+ ", statusYueLiaoIntensity=" + statusYueLiaoIntensity
				+ ", statusYueLiaoFrequency=" + statusYueLiaoFrequency
				+ ", statusYueLiaoIsClock=" + statusYueLiaoIsClock
				+ ", statusYueLiaoClockTime=" + statusYueLiaoClockTime + "]";
	}
	
	
}
