package com.jaku.websocket.request;

import org.json.JSONObject;

public class QueryAudioDeviceRequest extends JakuWebSocketRequestData {

	public QueryAudioDeviceRequest() {
		
	}
	
	@Override
	public JSONObject getPayload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("request", "query-audio-device");
		jsonObject.put("content-type", "text/xml");
		return jsonObject;
	}
}
