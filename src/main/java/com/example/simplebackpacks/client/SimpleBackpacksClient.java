package com.example.simplebackpacks.client;

import com.example.simplebackpacks.BackpackActionPayload;
import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public class SimpleBackpacksClient implements ClientModInitializer {
    private static KeyMapping backpackKey;

    @Override
    public void onInitializeClient() {
        // B: open the worn backpack. Shift + B: take it off.
        backpackKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.simplebackpacks.backpack",
                InputConstants.KEY_B,
                KeyMapping.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (backpackKey.consumeClick()) {
                if (client.player == null) {
                    continue;
                }
                int action = client.player.isShiftKeyDown() ? BackpackActionPayload.TAKE_OFF : BackpackActionPayload.OPEN;
                ClientPlayNetworking.send(new BackpackActionPayload(action));
            }
        });

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, renderer, helper, context) -> {
            if (renderer instanceof AvatarRenderer<?> avatarRenderer) {
                helper.register(new BackpackLayer<>(avatarRenderer));
            }
        });
    }
}
