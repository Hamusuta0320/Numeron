package com.erzbir.mirai.numeron;

import com.erzbir.mirai.numeron.boot.Starter;

public class NumeronBotApplication {
    private static final String packageName = "com.erzbir.mirai.numeron";

    public static void main(String[] args) {
        Starter starter = new Starter(packageName);
        starter.boot();  // 调用boot方法启动
        System.out.println("""
                | \\ | |_   _ _ __ ___   ___ _ __ ___  _ __ \s
                |  \\| | | | | '_ ` _ \\ / _ \\ '__/ _ \\| '_ \\\s
                | |\\  | |_| | | | | | |  __/ | | (_) | | | |
                |_| \\_|\\__,_|_| |_| |_|\\___|_|  \\___/|_| |_|""".indent(1));
        System.out.println("欢迎使用Numeron!!!");
    }
}

/*
 | \ | |_   _ _ __ ___   ___ _ __ ___  _ __
 |  \| | | | | '_ ` _ \ / _ \ '__/ _ \| '_ \
 | |\  | |_| | | | | | |  __/ | | (_) | | | |
 |_| \_|\__,_|_| |_| |_|\___|_|  \___/|_| |_|
 */
