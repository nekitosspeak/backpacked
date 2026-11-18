package com.example.simplebackpacks;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public final class ModAttachments {
    /** The backpack a player is wearing. Saved with the player and synced to every client that sees them. */
    public static final AttachmentType<ItemStack> WORN = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(SimpleBackpacksMod.MOD_ID, "worn_backpack"),
            builder -> builder
                    .persistent(ItemStack.OPTIONAL_CODEC)
                    .copyOnDeath()
                    .syncWith(ItemStack.OPTIONAL_STREAM_CODEC, AttachmentSyncPredicate.all()));

    private ModAttachments() {}

    public static void initialize() {
        // loading the class registers the attachment
    }
}
