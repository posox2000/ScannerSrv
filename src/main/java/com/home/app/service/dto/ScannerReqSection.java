package com.home.app.service.dto;

import java.util.List;

public class ScannerReqSection {
	private String sectionRPC;
	private List<ScannerReqFile> files;
	public String getSectionRPC() {
		return sectionRPC;
	}
	public void setSectionRPC(String sectionRPC) {
		this.sectionRPC = sectionRPC;
	}
	public List<ScannerReqFile> getFiles() {
		return files;
	}
	public void setFiles(List<ScannerReqFile> files) {
		this.files = files;
	}

}
