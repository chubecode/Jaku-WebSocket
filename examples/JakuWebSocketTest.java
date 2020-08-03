import java.io.IOException;

import com.jaku.websocket.WebSocketConnection;
import com.jaku.websocket.core.JakuWebSocketRequest;
import com.jaku.websocket.core.JakuWebSocketResponse;
import com.jaku.websocket.model.AudioDevice;
import com.jaku.websocket.request.QueryAudioDeviceRequest;
import com.jaku.websocket.request.SetAudioOutputRequest;

public class JakuWebSocketTest {

	private static final String ROKU_DEVICE_IP_ADDRESS = "<device ip address>";

	private static WebSocketConnection conn;
	
	public static void main(String [] args) {		
		try {
			conn = new WebSocketConnection(ROKU_DEVICE_IP_ADDRESS);
			conn.openConnection();
			
			testQueryAudioDevice();
			testSetAudioOutput();	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.closeConnection();
			}
		}
	}
	
	private static void testQueryAudioDevice() throws Exception {		
		QueryAudioDeviceRequest request = new QueryAudioDeviceRequest();
		JakuWebSocketResponse response = conn.send(new JakuWebSocketRequest(request, AudioDevice.class));
		AudioDevice audioDevice = (com.jaku.websocket.model.AudioDevice) response.getResponseData();
	}
	
	private static void testSetAudioOutput() throws Exception {
		SetAudioOutputRequest request = new SetAudioOutputRequest("192.168.1.18:6970:97:960:0:10");
		conn.send(new JakuWebSocketRequest(request, null));
	}
}
