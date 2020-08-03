package com.jaku.websocket.api;

import java.io.IOException;
import java.util.List;

import com.jaku.core.JakuRequest;
import com.jaku.core.JakuResponse;
import com.jaku.model.Channel;
import com.jaku.parser.AppsParser;
import com.jaku.request.QueryAppsRequest;

public class WebSocketRequests {

	private WebSocketRequests() {
		
	}
	
	@SuppressWarnings("unchecked")
	public static final List<Channel> queryAppsRequest(String url) throws IOException {
		QueryAppsRequest queryAppsRequest = new QueryAppsRequest(url);
		
		JakuRequest request = new JakuRequest(queryAppsRequest, new AppsParser());
		JakuResponse response = request.send();
		
		return (List<Channel>) response.getResponseData();
	}
}
