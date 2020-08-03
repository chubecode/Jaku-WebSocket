package com.jaku.websocket.request;

import org.json.JSONObject;

public class SetAudioOutputRequest extends JakuWebSocketRequestData {
	
	private String deviceName;
	
	public SetAudioOutputRequest(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@Override
	public JSONObject getPayload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("request", "set-audio-output");
		jsonObject.put("param-audio-output", "datagram");
		jsonObject.put("param-devname", deviceName);
		return jsonObject;
	}
}
