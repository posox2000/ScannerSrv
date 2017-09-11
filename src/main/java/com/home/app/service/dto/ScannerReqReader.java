package com.home.app.service.dto;

import java.util.List;

public class ScannerReqReader {
	private String readerId;
	private List<ScannerReqSection> sectionRPCs;
	public String getReaderId() {
		return readerId;
	}
	public void setReaderId(String readerId) {
		this.readerId = readerId;
	}
	public List<ScannerReqSection> getSectionRPCs() {
		return sectionRPCs;
	}
	public void setSectionRPCs(List<ScannerReqSection> sectionRPCs) {
		this.sectionRPCs = sectionRPCs;
	}
}
