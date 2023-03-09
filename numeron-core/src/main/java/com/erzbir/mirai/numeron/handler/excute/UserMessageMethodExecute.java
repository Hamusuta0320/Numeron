package com.erzbir.mirai.numeron.handler.excute;

import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;

import java.lang.reflect.Method;

/**
 * @author Erzbir
 * @Date: 2022/11/28 10:31
 * <p>联系人消息处理方法</p>
 */
public class UserMessageMethodExecute implements MethodExecute {
    public static final UserMessageMethodExecute INSTANCE = new UserMessageMethodExecute();

    private UserMessageMethodExecute() {

    }

    @Override
    public void execute(Method method, Object bean, EventChannel<BotEvent> channel) {
        RegisterEventHandle.register(channel, method, bean);
    }
}
