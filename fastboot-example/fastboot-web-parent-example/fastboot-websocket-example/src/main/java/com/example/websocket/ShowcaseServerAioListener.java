/**
 * 
 */
package com.example.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Tio;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsServerAioListener;

/**
 * @author Stylesmile
 * 用户根据情况来完成该类的实现
 */
public class ShowcaseServerAioListener extends WsServerAioListener {
	private static Logger log = LoggerFactory.getLogger(ShowcaseServerAioListener.class);

	public static final ShowcaseServerAioListener me = new ShowcaseServerAioListener();

	private ShowcaseServerAioListener() {

	}

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
		super.onAfterConnected(channelContext, isConnected, isReconnect);
		if (log.isInfoEnabled()) {
			log.info("onAfterConnected\r\n{}", channelContext);
		}

	}

	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
		super.onAfterSent(channelContext, packet, isSentSuccess);
		if (log.isInfoEnabled()) {
			log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
		super.onBeforeClose(channelContext, throwable, remark, isRemove);
		if (log.isInfoEnabled()) {
			log.info("onBeforeClose\r\n{}", channelContext);
		}

		WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();

		if (wsSessionContext != null && wsSessionContext.isHandshaked()) {
			
			int count = Tio.getAll(channelContext.tioConfig).getObj().size();

			String msg = channelContext.getClientNode().toString() + " 离开了，现在共有【" + count + "】人在线";
			//用tio-websocket，服务器发送到客户端的Packet都是WsResponse
			WsResponse wsResponse = WsResponse.fromText(msg, ConstantConfig.CHARSET);
			//群发
			Tio.sendToGroup(channelContext.tioConfig, ConstantConfig.GROUP_ID, wsResponse);
		}
	}

	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
		super.onAfterDecoded(channelContext, packet, packetSize);
		if (log.isInfoEnabled()) {
			log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}

	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
		super.onAfterReceivedBytes(channelContext, receivedBytes);
		if (log.isInfoEnabled()) {
			log.info("onAfterReceivedBytes\r\n{}", channelContext);
		}
	}

	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
		super.onAfterHandled(channelContext, packet, cost);
		if (log.isInfoEnabled()) {
			log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}

}
