package com.erzbir.numeron.console;

import com.erzbir.numeron.boot.Starter;
import com.erzbir.numeron.console.plugin.Plugin;
import com.erzbir.numeron.console.plugin.PluginManager;
import com.erzbir.numeron.core.context.AppContext;
import com.erzbir.numeron.core.entity.NumeronBot;

import java.util.List;

public class NumeronBotApplication {
    private static final String packageName = "com.erzbir.numeron";

    static {
        NumeronBot numeronBot = NumeronBot.INSTANCE;  // 这里是为了提前初始化, 不然会出现空指针异常
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        AppContext instance = AppContext.INSTANCE;
        PluginManager pluginManager = PluginManager.INSTANCE;
        List<Plugin> plugins = pluginManager.getPlugins();
        plugins.forEach(plugin -> PluginManager.INSTANCE.enable(plugin));
        Starter starter = new Starter(packageName, NumeronBotApplication.class.getClassLoader());
        starter.boot();  // 调用boot方法启动
        l = System.currentTimeMillis() - l;
        NumeronBot.INSTANCE.login();
        System.out.println("""
                | \\ | |_   _ _ __ ___   ___ _ __ ___  _ __ \s
                |  \\| | | | | '_ ` _ \\ / _ \\ '__/ _ \\| '_ \\\s
                | |\\  | |_| | | | | | |  __/ | | (_) | | | |
                |_| \\_|\\__,_|_| |_| |_|\\___|_|  \\___/|_| |_|""".indent(1));
        System.out.println("欢迎使用Numeron!!!");
        System.out.println("启动耗时: " + l + "ms");
        NumeronBot.INSTANCE.getBot().join();
    }
}

/*
 | \ | |_   _ _ __ ___   ___ _ __ ___  _ __
 |  \| | | | | '_ ` _ \ / _ \ '__/ _ \| '_ \
 | |\  | |_| | | | | | |  __/ | | (_) | | | |
 |_| \_|\__,_|_| |_| |_|\___|_|  \___/|_| |_|
 */
