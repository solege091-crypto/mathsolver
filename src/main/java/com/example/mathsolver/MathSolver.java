package com.example.mathsolver;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.util.Random;

public class MathSolver implements ClientModInitializer {

    private static String pending = null;
    private static String pendingKey = "";
    private static int delay = 0;
    private static String lastKey = "";
    private static long lastSent = 0;
    private static final Random RND = new Random();

    @Override
    public void onInitializeClient() {
        // Sohbet ve sistem mesajlari (action bar dahil)
        ClientReceiveMessageEvents.GAME.register((msg, overlay) -> handle(msg.getString(), !overlay));
        ClientReceiveMessageEvents.CHAT.register((msg, signed, sender, params, ts) -> handle(msg.getString(), true));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (pending != null && --delay <= 0) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatMessage(pending);
                    lastKey = pendingKey;
                    lastSent = System.currentTimeMillis();
                }
                pending = null;
            }
        });
    }

    /** strict=true: chat mesajlari (islemden sonra = veya ? sart). false: ekrandaki baslik yazilari. */
    public static void handle(String text, boolean strict) {
        Solver.Result r;
        try {
            r = Solver.solve(text, strict);
        } catch (Exception e) {
            return;
        }
        if (r == null) return;

        long now = System.currentTimeMillis();
        if (pending != null && r.key.equals(pendingKey)) return;
        if (r.key.equals(lastKey) && now - lastSent < 3000) return;

        pending = r.answer;
        pendingKey = r.key;
        delay = 6 + RND.nextInt(10); // 0.3 - 0.8 saniye
    }
}
