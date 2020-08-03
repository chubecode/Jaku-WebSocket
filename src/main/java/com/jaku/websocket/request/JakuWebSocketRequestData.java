package com.jaku.websocket.request;

import org.json.JSONObject;

public abstract class JakuWebSocketRequestData implements WebSocketRequestParameters {

	protected JakuWebSocketRequestData() {
		super();		
	}
	
	public JSONObject getPayload() {
		return null;
	}
}
