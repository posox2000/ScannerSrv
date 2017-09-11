package com.home.app.service.dto;

import java.time.LocalDate;
import java.util.List;

import com.home.app.service.dto.ScannerReqReader;

public class ScannerReqDTO {
	private String batch;
	private String userId;
	private String officeId;
	private LocalDate date;
	private List<ScannerReqReader> readers;
	
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public List<ScannerReqReader> getReaders() {
		return readers;
	}
	public void setReaders(List<ScannerReqReader> readers) {
		this.readers = readers;
	}
}
