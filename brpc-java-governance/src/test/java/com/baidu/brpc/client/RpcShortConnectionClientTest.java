/*
 * Copyright (c) 2018 Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.brpc.client;

import static org.junit.Assert.assertEquals;


import com.baidu.brpc.protocol.standard.Echo;
import org.junit.Before;
import org.junit.Test;

import com.baidu.brpc.client.channel.ChannelType;
import com.baidu.brpc.client.channel.Endpoint;
import com.baidu.brpc.loadbalance.LoadBalanceStrategy;
import com.baidu.brpc.protocol.Options;
import com.baidu.brpc.protocol.standard.EchoService;
import com.baidu.brpc.protocol.standard.EchoServiceImpl;
import com.baidu.brpc.RpcOptionsUtils;
import com.baidu.brpc.server.RpcServer;
import com.baidu.brpc.server.RpcServerOptions;
import com.baidu.brpc.server.ServiceManager;

public class RpcShortConnectionClientTest {

    @Before
    public void init() {
        if (ServiceManager.getInstance() != null) {
            ServiceManager.getInstance().getServiceMap().clear();
        }
    }

    @Test
    public void testBasic() {
        RpcServer rpcServer = new RpcServer(8001, RpcOptionsUtils.getRpcServerOptions());
        rpcServer.registerService(new EchoServiceImpl());
        rpcServer.start();

        Endpoint endPoint = new Endpoint("127.0.0.1", 8001);
        RpcClientOptions rpcClientOptions = RpcOptionsUtils.getRpcClientOptions();
        rpcClientOptions.setLoadBalanceType(LoadBalanceStrategy.LOAD_BALANCE_RANDOM);
        rpcClientOptions.setChannelType(ChannelType.SHORT_CONNECTION);
        RpcClient rpcClient = new RpcClient(endPoint, rpcClientOptions);
        EchoService echoService = BrpcProxy.getProxy(rpcClient, EchoService.class);
        Echo.EchoRequest request = Echo.EchoRequest.newBuilder().setMessage("hello").build();
        Echo.EchoResponse response = echoService.echo(request);
        assertEquals("hello", response.getMessage());
        rpcClient.stop();
        rpcServer.shutdown();
    }

    @Test
    public void testHttpProto() {
        RpcServerOptions serverOptions = RpcOptionsUtils.getRpcServerOptions();
        serverOptions.setProtocolType(Options.ProtocolType.PROTOCOL_HTTP_PROTOBUF_VALUE);
        RpcServer rpcServer = new RpcServer(8001, serverOptions);
        rpcServer.registerService(new EchoServiceImpl());
        rpcServer.start();

        RpcClientOptions clientOptions = RpcOptionsUtils.getRpcClientOptions();
        clientOptions.setProtocolType(Options.ProtocolType.PROTOCOL_HTTP_PROTOBUF_VALUE);
        clientOptions.setLoadBalanceType(LoadBalanceStrategy.LOAD_BALANCE_RANDOM);
        clientOptions.setChannelType(ChannelType.SHORT_CONNECTION);

        Endpoint endPoint = new Endpoint("127.0.0.1", 8001);
        RpcClient rpcClient = new RpcClient(endPoint, clientOptions);
        EchoService echoService = BrpcProxy.getProxy(rpcClient, EchoService.class);
        Echo.EchoRequest request = Echo.EchoRequest.newBuilder().setMessage("hello").build();
        Echo.EchoResponse response = echoService.echo(request);
        assertEquals("hello", response.getMessage());

        rpcClient.stop();
        rpcServer.shutdown();
    }

    @Test
    public void testNsheadProto() {
        RpcServerOptions serverOptions = RpcOptionsUtils.getRpcServerOptions();
        serverOptions.setProtocolType(Options.ProtocolType.PROTOCOL_NSHEAD_PROTOBUF_VALUE);
        RpcServer rpcServer = new RpcServer(8001, serverOptions);
        rpcServer.registerService(new EchoServiceImpl());
        rpcServer.start();

        RpcClientOptions clientOptions = RpcOptionsUtils.getRpcClientOptions();
        clientOptions.setProtocolType(Options.ProtocolType.PROTOCOL_NSHEAD_PROTOBUF_VALUE);
        clientOptions.setLoadBalanceType(LoadBalanceStrategy.LOAD_BALANCE_RANDOM);
        clientOptions.setChannelType(ChannelType.SHORT_CONNECTION);
        Endpoint endPoint = new Endpoint("127.0.0.1", 8001);

        RpcClient rpcClient = new RpcClient(endPoint, clientOptions);
        EchoService echoService = BrpcProxy.getProxy(rpcClient, EchoService.class);
        Echo.EchoRequest request = Echo.EchoRequest.newBuilder().setMessage("hello").build();
        Echo.EchoResponse response = echoService.echo(request);
        assertEquals("hello", response.getMessage());

        rpcClient.stop();
        rpcServer.shutdown();
    }
}
