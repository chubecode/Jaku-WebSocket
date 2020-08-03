Jaku-WebSocket
============================

Overview
--------

Jaku is a Java wrapper/client for the [Roku External Control API](https://sdkdocs.roku.com/display/sdkdoc/External+Control+API/) using WebSockets.

Key Features:
--------

Jaku-WebSocket includes helper functions to:

#### Query
* Query the state of the current audio on a device

#### Control
* Set the audio output on a device

Install
--------

TBA


Usage
------------

Sample code:

##### Query the state of the current audio on a device

    WebSocketConnection conn = new WebSocketConnection(ROKU_DEVICE_IP_ADDRESS);
    conn.openConnection();

    QueryAudioDeviceRequest request = new QueryAudioDeviceRequest();
    JakuWebSocketResponse response = conn.send(new JakuWebSocketRequest(request, AudioDevice.class));
    AudioDevice audioDevice = (com.jaku.websocket.model.AudioDevice) response.getResponseData();

##### Set the audio output on a device

    WebSocketConnection conn = new WebSocketConnection(ROKU_DEVICE_IP_ADDRESS);
    conn.openConnection();

    SetAudioOutputRequest request = new SetAudioOutputRequest(ROKU_DEVICE_HOSTNAME + ":6970:97:960:0:10");
    conn.send(new JakuWebSocketRequest(request, null));

License
------------

```
Jaku-WebSocket: A Java wrapper/client for the Roku External Control API
using WebSockets.

Copyright 2020 William Seemann

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
