package com.erzbir.numeron.plugin.cardsearch.listner;

import com.erzbir.numeron.core.filter.message.MessageRule;
import com.erzbir.numeron.core.filter.permission.PermissionType;
import com.erzbir.numeron.core.filter.rule.FilterRule;
import com.erzbir.numeron.core.handler.Command;
import com.erzbir.numeron.core.handler.Message;
import com.erzbir.numeron.core.listener.Listener;
import com.erzbir.numeron.menu.Menu;
import com.erzbir.numeron.plugin.cardsearch.Card;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Erzbir
 * @Date: 2023/3/5 16:08
 */

@Listener
@Menu(name = "游戏王查卡")
@SuppressWarnings("unused")
public class Search {
    private static final String API = "https://ygocdb.com/api/v0/?search=";
    private static final String PIC_URL = "https://cdn.233.momobako.com/ygopro/pics/";

    @Command(name = "游戏王查卡", dec = "sr [cardname]", help = "sr 青眼白龙", permission = PermissionType.ALL)
    @Message(
            text = "^sr\\s+\\S+",
            filterRule = FilterRule.BLACK,
            messageRule = MessageRule.REGEX,
            permission = PermissionType.ALL
    )
    private void search(MessageEvent event) throws IOException {
        String s = event.getMessage().contentToString().replaceFirst("^sr\\s+", "");
        String string = sendRequest(s);
        if (string == null) {
            return;
        }
        List<Card> cards = pareResult(string);
        Card result = getResult(cards);
        Image pic = getPic(event.getSubject(), result.getId());
        StringBuilder sb = new StringBuilder();
        cards.forEach(t -> sb.append(t.getCn_name()).append("   "));
        sb.append("\n");
        event.getSubject().sendMessage(new PlainText("共找到" + cards.size() + "张卡\n" + sb).plus(creatMessage(result, pic)));
    }

    private Card getResult(List<Card> cards) {
        int max = 0;
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getWeight() < cards.get(i + 1).getWeight()) {
                max = i + 1;
            }
        }
        return cards.get(max);
    }

    private List<Card> pareResult(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray result = jsonObject.getAsJsonArray("result");
        List<Card> cards = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < result.size(); i++) {
            JsonObject temp = result.get(i).getAsJsonObject();
            Card card = gson.fromJson(temp.toString(), Card.class);
            cards.add(card);
        }
        return cards;
    }

    private Image getPic(Contact contact, long id) throws IOException {
        return Contact.uploadImage(contact, new URL(PIC_URL + id + ".jpg").openStream());
    }

    private MessageChain creatMessage(Card card, Image image) {
        return new MessageChainBuilder().append(image).append(card.toString()).build();
    }

    private String sendRequest(String s) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
        Request request = new Request.Builder().url(API + s).build();
        try (Response execute = okHttpClient.newCall(request).execute()) {
            int code = execute.code();
            if (code != 200) {
                return null;
            }
            return Objects.requireNonNull(execute.body()).string();
        }
    }
}
