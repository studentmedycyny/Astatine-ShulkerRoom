package net.ernest.shulkerroom.helper;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatHelper {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    private static final String[] TO_REPLACE = { ">>", "<<" };
    private static final String[] REPLACED = { "\u00BB", "\u00AB" };

    public static List<String> fixColors(List<String> contents) {
        return contents.stream()
                .map(ChatHelper::fixColors)
                .collect(Collectors.toList());
    }
    public static String fixColors(String message) {
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, String.valueOf(ChatColor.of(color)));
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', StringUtils.replaceEach(message, TO_REPLACE, REPLACED));
    }
}
