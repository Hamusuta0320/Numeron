package com.erzbir.mirai.numeron.filter;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/16 22:18
 * <p>
 * 滤器接口
 * </p>
 */
public interface ChannelFilterInter {

    /**
     * @param event 消息事件
     * @param text  注解中的text()值
     * @return Boolean
     */
    Boolean filter(MessageEvent event, String text);
}