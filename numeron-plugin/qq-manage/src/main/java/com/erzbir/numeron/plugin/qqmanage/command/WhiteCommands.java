package com.erzbir.numeron.plugin.qqmanage.command;

import com.erzbir.numeron.api.model.WhiteService;
import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.filter.rule.FilterRule;
import com.erzbir.numeron.core.handler.Command;
import com.erzbir.numeron.core.handler.Message;
import com.erzbir.numeron.core.listener.Listener;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/27 22:50
 * <p>
 * 白名单相关命令
 * </p>
 */
@Listener
@SuppressWarnings("unused")
public class WhiteCommands {

    @Command(
            name = "白名单操作",
            dec = "添加白名单",
            help = "/permit user [@user] 或者 /permit user [qq]",
            permission = PermissionType.MASTER
    )
    @Message(
            text = "^/permit\\s+?user\\s+?@*\\d+",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.MASTER
    )
    private void permit2(MessageEvent event) {
        long id = Long.parseLong(event.getMessage().contentToString().replaceFirst("^/permit\\s+?user\\s+?@*", ""));
        if (WhiteService.INSTANCE.addWhite(id, event.getSender().getId())) {
            event.getSubject().sendMessage(id + " 已添加到白名单");
        }
    }

    @Command(
            name = "白名单操作",
            dec = "移出白名单",
            help = "/unpermit user [@user] 或者 /unpermit user [qq]",
            permission = PermissionType.MASTER
    )
    @Message(
            text = "^/unpermit\\s+?user\\s+?@*\\d+",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.MASTER
    )
    private void noPermit(MessageEvent event) {
        long id = Long.parseLong(event.getMessage().contentToString().replaceFirst("^/unpermit\\s+?user\\s+?@*", ""));
        if (WhiteService.INSTANCE.removeWhite(id)) {
            event.getSubject().sendMessage(id + " 已移出白名单");
        }
    }

    @Command(
            name = "白名单操作",
            dec = "查询白名单",
            help = "/query white [id]",
            permission = PermissionType.MASTER
    )
    @Message(
            text = "^/query\\s+?white\\s+?\\d+",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.MASTER
    )
    private void query(MessageEvent event) {
        long l = Long.parseLong(event.getMessage().contentToString().replaceFirst("/query\\s+?white\\s+?", ""));
        if (l == 0) {
            event.getSubject().sendMessage(WhiteService.INSTANCE.getAdminList().toString());
        } else {
            event.getSubject().sendMessage(String.valueOf(WhiteService.INSTANCE.exist(l)));
        }
    }
}
