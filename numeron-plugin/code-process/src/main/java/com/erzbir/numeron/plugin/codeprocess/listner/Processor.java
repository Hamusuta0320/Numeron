package com.erzbir.numeron.plugin.codeprocess.listner;

import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.filter.rule.FilterRule;
import com.erzbir.numeron.core.handler.Command;
import com.erzbir.numeron.core.handler.Message;
import com.erzbir.numeron.core.listener.Listener;
import com.erzbir.numeron.menu.Menu;
import com.erzbir.numeron.plugin.codeprocess.runner.CodeRunner;
import com.erzbir.numeron.plugin.codeprocess.runway.RunJs;
import com.erzbir.numeron.plugin.codeprocess.runway.RunPy;
import com.erzbir.numeron.plugin.codeprocess.runway.RunShell;
import net.mamoe.mirai.event.events.MessageEvent;

import java.io.IOException;

/**
 * @author Erzbir
 * @Date: 2022/11/30 09:34
 */
@Listener
@Menu(name = "代码执行")
@SuppressWarnings("unused")
public class Processor {
    private final CodeRunner codeRunner = CodeRunner.getInstance();

    @Command(
            name = "指令执行",
            dec = "执行py代码",
            help = "py\nprint(1)",
            permission = PermissionType.MASTER
    )
    @Message(
            text = "py\n",
            messageRule = MessageRule.BEGIN_WITH,
            filterRule = FilterRule.NONE,
            permission = PermissionType.MASTER
    )
    private void runPy(MessageEvent event) throws IOException {
        codeRunner.setRunCode(RunPy.getInstance());
        event.getSubject().sendMessage(codeRunner.getRunCode().execute(event.getMessage().contentToString().replaceFirst("py\\s+", "")));
    }

    @Command(
            name = "指令执行",
            dec = "执行js代码",
            help = "py\nconsole.log(1)",
            permission = PermissionType.MASTER
    )
    @Message(text = "js\n",
            messageRule = MessageRule.BEGIN_WITH,
            filterRule = FilterRule.NONE,
            permission = PermissionType.MASTER
    )
    private void runJs(MessageEvent event) throws IOException {
        codeRunner.setRunCode(RunJs.getInstance());
        event.getSubject().sendMessage(codeRunner.getRunCode().execute(event.getMessage().contentToString().replaceFirst("js\\s+", "")));
    }

    @Command(
            name = "指令执行",
            dec = "执行shell代码",
            help = "sh\necho 1",
            permission = PermissionType.MASTER
    )
    @Message(
            text = "sh\n",
            messageRule = MessageRule.BEGIN_WITH,
            filterRule = FilterRule.NONE,
            permission = PermissionType.MASTER
    )
    private void runShell(MessageEvent event) throws IOException {
        codeRunner.setRunCode(RunShell.getInstance());
        event.getSubject().sendMessage(codeRunner.getRunCode().execute(event.getMessage().contentToString().replaceFirst("sh\\s+", "")));
    }
}
