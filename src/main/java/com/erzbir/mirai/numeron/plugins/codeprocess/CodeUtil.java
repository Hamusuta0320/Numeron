package com.erzbir.mirai.numeron.plugins.codeprocess;

import com.erzbir.mirai.numeron.configs.GlobalConfig;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Erzbir
 * @Date: 2022/11/30 09:13
 */
public class CodeUtil {
    private static Charset getCharset() {
        if (GlobalConfig.OS.startsWith("Windows")) {
            return Charset.forName("GBK");
        }
        return StandardCharsets.UTF_8;
    }

    private static StringBuilder readResultS(BufferedReader in) {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try (in) {
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            stringBuilder.append(e.getMessage()).append("\n");
        }
        return stringBuilder;
    }


    private static String readResult(Process process) throws Exception {
        Charset use = getCharset();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder errBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), use));
        BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream(), use));
        try {
            stringBuilder = readResultS(in);
            errBuilder = readResultS(err);
        } catch (Exception e) {
            errBuilder.append(e.getMessage()).append("\n");
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
        // 这里不能超过5000
        if (!errBuilder.isEmpty()) {
            throw new Exception(errBuilder.toString().trim());
        } else if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString().trim();
        }
        return "该命令没有输出";
    }

    public static String execute(String type, String filename, File file) {
        String result;
        try {
            result = CodeUtil.readResult(Runtime.getRuntime().exec(type + " " + filename, null, file));
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            new Thread(file::delete).start();
        }
        return result;
    }

    public static File createCodeFile(String dir, String filename, String code) {
        File file;
        if (!(file = new File(dir)).exists()) {
            file.mkdirs();
        }
        try (FileWriter fileWriter = new FileWriter(Path.of(dir, filename).toFile())) {
            fileWriter.write(code);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
