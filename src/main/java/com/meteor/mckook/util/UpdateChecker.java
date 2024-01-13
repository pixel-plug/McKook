package com.meteor.mckook.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meteor.mckook.McKook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 更新检测
 */
public class UpdateChecker {

    private static final String GITHUB_API = "https://api.github.com/repos/meteorOSS/McKook/releases/latest";

    public static void checkForUpdates(McKook plugin) {
        try {
            plugin.getLogger().info("正在检测更新");
            URL url = new URL(GITHUB_API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            String latestVersion = jsonObject.get("tag_name").getAsString()+"-SNAPSHOT";
            String releaseNotes = jsonObject.get("body").getAsString();
            String currentVersion = plugin.getDescription().getVersion();

            if (!latestVersion.equals(currentVersion)) {
                plugin.getLogger().info("检测到新版本: "+latestVersion);
                plugin.getLogger().info("更新内容: ");
                System.out.println(releaseNotes);
                plugin.getLogger().info("当前版本: "+currentVersion);
                plugin.getLogger().info("前往 https://github.com/meteorOSS/McKook/releases 获取新发布版本");
            } else {
                plugin.getLogger().info("当前已经是最新版本!");
            }

        } catch (Exception e) {
            plugin.getLogger().info("检测更新失败，请前往 https://github.com/meteorOSS/McKook/releases 查看更新");
        }
    }


}
