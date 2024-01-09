package com.meteor.mckook.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import snw.jkook.message.component.card.CardComponent;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.kookbc.impl.entity.builder.CardBuilder;

import java.util.Map;

public class TextComponentHelper {

    private static final Gson gson = new Gson();

    /**
     * json转换为卡片消息
     * @return
     */
    public static MultipleCardComponent jsonToCardComponent(String json, Map<String,String> params){
        for (String key : params.keySet()) {
            json = json.replace("{"+key+"}",params.get(key));
        }
        JsonArray asJsonArray = JsonParser.parseString(json).getAsJsonArray();
        return CardBuilder.buildCard(asJsonArray);
    }

}
