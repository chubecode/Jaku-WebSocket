package com.jaku.websocket.core;

import com.jaku.core.JakuResponseData;

public class JakuWebSocketResponse {
	
	private Object jakuResponseData;
	
	public JakuWebSocketResponse(Object jakuResponseData) {
		this.jakuResponseData = jakuResponseData;
	}

	public Object getResponseData() {
		return jakuResponseData;
	}

	public void setResponseData(JakuResponseData jakuResponseData) {
		this.jakuResponseData = jakuResponseData;
	}
}
