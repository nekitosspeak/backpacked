package com.example.simplebackpacks;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Client -> server: the backpack key was pressed. */
public record BackpackActionPayload(int action) implements CustomPacketPayload {
    public static final int OPEN = 0;
    public static final int TAKE_OFF = 1;

    public static final CustomPacketPayload.Type<BackpackActionPayload> TYPE = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(SimpleBackpacksMod.MOD_ID, "backpack_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BackpackActionPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, BackpackActionPayload::action, BackpackActionPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
