package com.erzbir.numeron.plugin.help;

import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.handler.Message;
import com.erzbir.numeron.core.listener.Listener;
import com.erzbir.numeron.menu.MenuStatic;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.HashSet;
import java.util.Set;

import static com.erzbir.numeron.menu.IOUtils.bufferedImageToInputStream;
import static com.erzbir.numeron.menu.MenuDrawUtil.drawMenu;


@Listener
@SuppressWarnings("unused")
public class MenuController {
    @Message(
            text = "^#OpenMenu\\s\\w*$",
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ADMIN
    )
    private void openMenu(MessageEvent e) {
        String[] ary = e.getMessage().contentToString().split("\\s+");
        if (MenuStatic.menuList.contains(ary[1]) && MenuStatic.closeMenuGroups.get(ary[1]) != null) {
            MenuStatic.closeMenuGroups.get(ary[1]).remove(e.getSubject().getId());
            e.getSubject().sendMessage(Contact.uploadImage(e.getSubject(), bufferedImageToInputStream(drawMenu(e.getSubject().getId()), "PNG")));
        } else if (MenuStatic.menuList.contains(ary[1])) {
            e.getSubject().sendMessage("该功能已开启");
        } else {
            e.getSubject().sendMessage("未找到该功能");
        }
    }

    @Message(
            text = "^#CloseMenu\\s\\w*$",
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ADMIN
    )
    private void closeMenu(MessageEvent e) {
        String[] ary = e.getMessage().contentToString().split("\\s+");
        if (MenuStatic.menuList.contains(ary[1])) {
            if (MenuStatic.closeMenuGroups.get(ary[1]) != null)
                MenuStatic.closeMenuGroups.get(ary[1]).add(e.getSubject().getId());
            else {
                Set<Long> groups = new HashSet<>();
                groups.add(e.getSubject().getId());
                MenuStatic.closeMenuGroups.put(ary[1], groups);
            }
            e.getSubject().sendMessage(Contact.uploadImage(e.getSubject(), bufferedImageToInputStream(drawMenu(e.getSubject().getId()), "PNG")));
        } else {
            e.getSubject().sendMessage("未找到该功能");
        }
    }
}
