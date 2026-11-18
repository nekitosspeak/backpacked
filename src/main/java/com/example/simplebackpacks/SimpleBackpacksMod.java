package com.example.simplebackpacks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class SimpleBackpacksMod implements ModInitializer {
    public static final String MOD_ID = "simplebackpacks";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModAttachments.initialize();

        PayloadTypeRegistry.serverboundPlay().register(BackpackActionPayload.TYPE, BackpackActionPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(BackpackActionPayload.TYPE,
                (payload, context) -> BackpackActions.handle(context.player(), payload.action()));
    }
}
