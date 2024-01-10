package com.meteor.mckook.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.entity.builder.CardBuilder;

import java.util.Map;

public class TextComponentHelper {

    private static final Gson gson = new Gson();

    /**
     * json转换为卡片消息
     */
    public static MultipleCardComponent json2CardComponent(String json,Map<String,String> contextMap){

        // 替换消息中占位符
        json = contextMap.entrySet().stream()
                .reduce(json, (str, entry) -> str.replace("{" + entry.getKey() + "}", entry.getValue()), (s1, s2) -> s1);

        MultipleCardComponent multipleCardComponent = CardBuilder.buildCard(gson.fromJson(json, JsonArray.class));
        return multipleCardComponent;

    }



}
