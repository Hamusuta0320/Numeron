package com.erzbir.numeron.plugin.qqmanage.action;

import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.handler.Command;
import com.erzbir.numeron.core.handler.Event;
import com.erzbir.numeron.core.handler.Message;
import com.erzbir.numeron.core.listener.Listener;
import com.erzbir.numeron.menu.Menu;
import com.erzbir.numeron.plugin.qqmanage.NetUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Erzbir
 * @Date: 2022/12/2 16:57
 */
@Listener
@Menu(name = "入群欢迎")
@SuppressWarnings("unused")
public class MemberInAction {
    private final HashMap<Long, Boolean> isOn = new HashMap<>();

    @Event
    public void register(MemberJoinEvent event) {
        long groupId = event.getGroupId();
        isOn.putIfAbsent(groupId, false);
        if (isOn.get(groupId)) {
            NormalMember member = event.getMember();
            Group group = event.getGroup();
            String avatarUrl = member.getAvatarUrl();
            MessageChain messages = new MessageChainBuilder().build();
            Image image = null;
            try {
                image = NetUtil.getImage(group, avatarUrl);
            } catch (IOException e) {
                event.getGroup().sendMessage(e.getMessage());
            }
            if (image != null) {
                messages.plus(image).plus("\n");
            }
            messages.plus("昵称: ").plus(member.getNick()).plus("\n")
                    .plus("欢迎进群");
        }
    }

    @Command(
            name = "入群欢迎",
            dec = "开关入群欢迎",
            help = "/welcome [true|false]",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "/welcome\\s+?(true|false)",
            permission = PermissionType.WHITE,
            messageRule = MessageRule.REGEX
    )
    private void onOff(GroupMessageEvent event) {
        String[] s = event.getMessage().contentToString().split("\\s+?");
        isOn.put(event.getGroup().getId(), Boolean.parseBoolean(s[2]));
        event.getSubject().sendMessage(isOn.toString());
    }
}
