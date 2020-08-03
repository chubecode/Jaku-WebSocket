package com.jaku.websocket;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.jaku.websocket.core.JakuWebSocketRequest;
import com.jaku.websocket.core.JakuWebSocketResponse;
import com.jaku.websocket.request.JakuWebSocketRequestData;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketConnection extends WebSocketListener {

	private static final String TAG = "WebSocketConnection";
	
	private WebSocket websocket;
	private String url;
	private Integer requestId = 0;

	private HashMap<Integer, String> responses = new HashMap<Integer, String>();
	
	public WebSocketConnection(final String url) {
		super();
		this.url = url;
	}
	
	public void openConnection() {
		Request myrequest = new Request.Builder().url(url + "/ecp-session")
                .addHeader("Sec-WebSocket-Origin", "Android")
                .addHeader("Sec-WebSocket-Protocol", "ecp-2")
                .addHeader("Upgrade", "websocket")
                .addHeader("Connection", "Upgrade")
                .addHeader("Sec-WebSocket-Key", "<Enter Key Here>")
                .addHeader("Sec-WebSocket-Version", "13")
                .addHeader("Host", url.replace("http://", ""))
                .addHeader("Accept-Encoding", "gzip")
                .addHeader("User-Agent", "okhttp/3.11.0")
                .build();
		
        OkHttpClient client = new OkHttpClient();
        websocket = client.newWebSocket(myrequest, this);
	}
	
	public JakuWebSocketResponse send(final JakuWebSocketRequest jakuWebSocketRequest) throws Exception {
		if (websocket == null) {
			return null;
		}
		
		JakuWebSocketResponse jakuWebSocketResponse = null;
		
		JakuWebSocketRequestData webSocketRequestData = jakuWebSocketRequest.getRequestData();
		JSONObject payload = webSocketRequestData.getPayload();
		
		Integer currentRequestId = requestId;
		
		if (payload != null) {
			payload.put("request-id", String.valueOf(currentRequestId));
			requestId++;
		}
		
		websocket.send(payload.toString());
		
		while (responses.get(currentRequestId) == null) {
			Thread.sleep(50);
		}
		
		if (responses.get(currentRequestId).equals("")) {
			return null;
		}
		
		System.out.println("Request response: " + responses.get(currentRequestId));
		
		jakuWebSocketResponse = new JakuWebSocketResponse(generateResponseData(responses.get(currentRequestId), jakuWebSocketRequest.getResponseClass()));
		
		return jakuWebSocketResponse;
	}
	
	private Object generateResponseData(String response, Class<?> clz) throws Exception {
		Serializer serializer = new Persister();
		return serializer.read(clz, response);
	}
	
	public void closeConnection() {
		if (websocket != null) {
			websocket.close(1000, null);
		}
		
		responses.clear();
	}
	
	@Override
	public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
		super.onClosed(webSocket, code, reason);
		Log("onClosed: " + code);
	}

	@Override
	public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
		super.onClosing(webSocket, code, reason);
		Log("onClosing: " + code);
	}

	@Override
	public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
		super.onFailure(webSocket, t, response);
		Log("open: " + t.getMessage());
		if (response != null) {
			Log("open: " + response.message());
		}
	}

	@Override
	public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
		super.onMessage(webSocket, text);
		Log("onMessage: " + text);

		try {
			JSONObject response = new JSONObject(text);

			if (!response.optString("notify").equals("")) {
				StringBuilder sb = new StringBuilder();
				sb.append(response.getString("param-challenge"));
				sb.append(generateHash("95E610D0-7C29-44EF-FB0F-97F1FCE4C297", 9));

				byte[] bytes = sb.toString().getBytes();

				MessageDigest instance = MessageDigest.getInstance("SHA-1");
				instance.update(bytes, 0, bytes.length);
				String hash = Base64.getEncoder().encodeToString(instance.digest());

				Log("Hash: " + hash);

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("request", "authenticate");
				jsonObject.put("request-id", "48");
				jsonObject.put("param-response", hash);

				Log("Resp: " + jsonObject.toString());

				webSocket.send(jsonObject.toString());
			} else if (!response.optString("content-data").equals("") &&
					!response.optString("response-id").equals("") &&
					response.optString("status").equals("200") &&
					response.optString("status-msg").equals("OK")) {
				String decodedResponse = new String(Base64.getDecoder().decode(response.getString("content-data").getBytes()));
				responses.put(Integer.valueOf(response.optString("response-id")), decodedResponse);		
			} else if (!response.optString("response-id").equals("")) {
				responses.put(Integer.valueOf(response.optString("response-id")), "");
			}
		} catch (Exception ex) {
			Log(ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
		super.onMessage(webSocket, bytes);
		Log("onMessage: " + bytes.toString());
	}

	@Override
	public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
		super.onOpen(webSocket, response);
		Log("open: " + response.message());
	}

	private String generateHash(String str, int i) {
		StringBuilder sb = new StringBuilder();
		for (int i2 = 0; i2 < str.length(); i2++) {
			sb.append(hash(str.charAt(i2), i));
		}
		return sb.toString();
	}

	private char hash(char c, int i) {
		int i2 = (c < '0' || c > '9') ? (c < 'A' || c > 'F') ? -1 : (c - 'A') + 10 : c - '0';
		if (i2 < 0) {
			return c;
		}
		int i3 = ((15 - i2) + i) & 15;
		return (char) (i3 < 10 ? i3 + 48 : (i3 + 65) - 10);
	}


	private void Log(String message) {
		System.out.println(TAG + " " + message);
	}
}
