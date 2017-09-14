package com.home.app.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.home.app.web.rest.BarcodeReqResource;

public class BarcodeReqFileValidator implements Runnable {

    private final Logger log = LoggerFactory.getLogger(BarcodeReqResource.class);
	@Override
	public void run() {
		try {
			/////////////////////////////////////////
			//TODO  Replace with real validation call
			//
			for(int i=0; i<=50; i++){
			Thread.sleep(500);
			log.debug("Simulating slow process; STEP:"+i);
			}
			/////////////////////////////////////////
		} catch (InterruptedException e) {
			log.debug("AN EXCEPTION in FILE VALIDATOR!!!:\n"+e.toString());
		}
	}

}
