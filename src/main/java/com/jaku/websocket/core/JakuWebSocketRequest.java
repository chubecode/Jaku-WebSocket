package com.jaku.websocket.core;

import com.jaku.websocket.request.JakuWebSocketRequestData;

public class JakuWebSocketRequest {

	private JakuWebSocketRequestData jakuRequestData;
	private Class<?> clz;
	
	public JakuWebSocketRequest(JakuWebSocketRequestData jakuRequestData, Class<?> clz) {
		this.jakuRequestData = jakuRequestData;
		this.clz = clz;
	}
	
	public JakuWebSocketRequestData getRequestData() {
		return jakuRequestData;
	}
	
	public Class<?> getResponseClass() {
		return clz;
	}	
}

