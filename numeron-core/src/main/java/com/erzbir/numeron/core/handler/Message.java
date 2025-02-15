package com.erzbir.numeron.core.handler;

import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.filter.rule.FilterRule;
import net.mamoe.mirai.event.ConcurrencyKind;
import net.mamoe.mirai.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Erzbir
 * @Date: 2022/11/18 14:32
 * <p>标记所有消息事件</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Message {
    FilterRule filterRule() default FilterRule.NONE;

    MessageRule messageRule() default MessageRule.EQUAL;

    PermissionType permission();

    String text();

    EventPriority priority() default EventPriority.NORMAL;

    ConcurrencyKind concurrency() default ConcurrencyKind.CONCURRENT;
}
