package com.erzbir.numeron.plugin.qqmanage.command;

import com.erzbir.numeron.api.model.GroupService;
import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.filter.rule.FilterRule;
import com.erzbir.numeron.core.handler.Command;
import com.erzbir.numeron.core.handler.Message;
import com.erzbir.numeron.core.listener.Listener;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Erzbir
 * @Date: 2022/11/27 22:46
 */
@Listener
@SuppressWarnings("unused")
public class MuteCommands {

    @Command(
            name = "禁言操作",
            dec = "禁言一个人",
            help = "/mute [@user] [time] 或者 /mute [qq] [time]",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "/mute\\s+?@?\\d+?\\s+?\\d+",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ADMIN
    )
    private void muteSingle(MessageEvent event) {
        mute(event, true);
    }

    @Command(
            name = "禁言操作",
            dec = "解禁一个人",
            help = "/unmute [@user] [time] 或者 /unmute [qq] [time]",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "/unmute\\s+?@?\\d+?",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ADMIN
    )
    private void unmuteSingle(MessageEvent event) {
        mute(event, false);
    }

    private void mute(MessageEvent event, boolean bool) {
        String[] s = event.getMessage().contentToString().split("\\s+");
        long id;
        int time;
        s[1] = s[1].replaceAll("@", "");
        id = Long.parseLong(s[1]);
        time = Integer.parseInt(s[2]);
        if (event instanceof GroupMessageEvent event1) {
            Objects.requireNonNull(event1.getGroup().get(id)).mute(time);
        } else {
            AtomicReference<NormalMember> member = new AtomicReference<>();
            GroupService.INSTANCE.getEnableGroupList().forEach(v -> member.set(Objects.requireNonNull(event.getBot().getGroup(v)).get(id)));
            if (member.get().getPermission().getLevel() < 1) {
                if (!bool && member.get().isMuted()) {
                    member.get().unmute();
                } else if (bool && !member.get().isMuted()) {
                    member.get().mute(time);
                }
            }
        }
    }

    @Command(
            name = "禁言操作",
            dec = "禁言群",
            help = "/mute group [id]",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "/mute group\\s+?\\d+",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ADMIN)
    private void muteGroup(MessageEvent event) {
        String[] split = event.getMessage().contentToString().split("\\s+");
        long id = Long.parseLong(split[2]);
        Objects.requireNonNull(event.getBot().getGroup(id)).getSettings().setMuteAll(true);
    }

    @Command(
            name = "禁言操作",
            dec = "解禁群",
            help = "/unmute [id]",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "/unmute group\\s+?\\d+",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ADMIN
    )
    private void unmuteGroup(MessageEvent event) {
        String[] split = event.getMessage().contentToString().split("\\s+");
        long id = Long.parseLong(split[2]);
        Objects.requireNonNull(event.getBot().getGroup(id)).getSettings().setMuteAll(false);
    }
}
