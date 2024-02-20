package io.github.stylesmile.websocket;

import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.intf.Packet;

/**
 * @author Stylesmile
 */
public class WebsocketUtil {
    /**
     * 绑定用户
     *
     * @param channelContext 上下文
     * @param userid         用户id
     */
    public static void bindUser(ChannelContext channelContext, String userid) {
        Tio.bindUser(channelContext, userid);
    }

    /**
     * 发送个人消息
     *
     * @param tioConfig 配置
     * @param userid    用户id
     * @param packet    消息
     */
    public static void sendToUser(TioConfig tioConfig, String userid, Packet packet) {
        Tio.sendToUser(tioConfig, userid, packet);
    }

    /**
     * 发送个人消息
     *
     * @param tioConfig 配置
     * @param groupId   用户组id
     * @param packet    消息
     */
    public static void sendToGroup(TioConfig tioConfig, String groupId, Packet packet) {
        Tio.sendToGroup(tioConfig, groupId, packet);
    }


}
