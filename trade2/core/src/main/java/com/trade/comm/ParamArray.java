package com.trade.comm;

import com.trade.model.Fadmin;
import com.trade.model.Fuser;
import org.springframework.web.multipart.MultipartFile;

public class ParamArray {
	private Fadmin fadmin;
	private MultipartFile filedata;
	private Fuser fuser ;

	public Fadmin getFadmin() {
		return fadmin;
	}

	public void setFadmin(Fadmin fadmin) {
		this.fadmin = fadmin;
	}

	public MultipartFile getFiledata() {
		return filedata;
	}

	public void setFiledata(MultipartFile filedata) {
		this.filedata = filedata;
	}

	public Fuser getFuser() {
		return fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

}
