package com.baidu.brpc.protocol.pbrpc;

import com.baidu.brpc.protocol.standard.Echo;

public class EchoServiceImpl implements EchoService {

    @Override
    public Echo.EchoResponse echo(Echo.EchoRequest request) {
        String message = request.getMessage();
        Echo.EchoResponse response = Echo.EchoResponse.newBuilder()
                .setMessage(message).build();
        return response;
    }
}
