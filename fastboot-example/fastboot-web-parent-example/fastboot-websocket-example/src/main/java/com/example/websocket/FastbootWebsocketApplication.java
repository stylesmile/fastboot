package com.example.websocket;

import io.github.stylesmile.app.App;

/**
 * @author Stylesmile
 */
public class FastbootWebsocketApplication {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		//启动http server，这个步骤不是必须的，但是为了用页面演示websocket，所以先启动http
		App.start(FastbootWebsocketApplication.class,args);
		//启动websocket服务器
		WebsocketStarter.start();
	}
}
