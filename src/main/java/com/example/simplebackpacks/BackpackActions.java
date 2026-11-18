package com.example.simplebackpacks;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;

/** Server-side logic: open, wear and take off backpacks. */
public final class BackpackActions {
    private BackpackActions() {}

    /** Backpack in hand: opens it. Contents are stored in the very ItemStack that is in the hand. */
    public static void openFromHand(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof BackpackItem backpack)) {
            return;
        }
        BackpackContainer container = new BackpackContainer(
                stack,
                backpack.getRows() * 9,
                p -> p.getItemInHand(hand) == stack,
                saved -> {});
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, p) -> new BackpackMenu(id, inventory, container, backpack.getRows()),
                stack.getHoverName()));
    }

    /** Opens the backpack the player is wearing. */
    public static void openWorn(ServerPlayer player) {
        ItemStack worn = player.getAttached(ModAttachments.WORN);
        if (worn == null || !(worn.getItem() instanceof BackpackItem backpack)) {
            return;
        }
        ItemStack working = worn.copy();
        BackpackContainer container = new BackpackContainer(
                working,
                backpack.getRows() * 9,
                p -> {
                    ItemStack now = p.getAttached(ModAttachments.WORN);
                    return now != null && now.getItem() == working.getItem();
                },
                // setAttached with a fresh copy so the change is saved and synced to clients
                saved -> player.setAttached(ModAttachments.WORN, saved.copy()));
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, p) -> new BackpackMenu(id, inventory, container, backpack.getRows()),
                working.getHoverName()));
    }

    /** Puts the backpack from the hand on the back. A backpack that was already worn goes to the hand. */
    public static void wear(ServerPlayer player, InteractionHand hand) {
        ItemStack inHand = player.getItemInHand(hand);
        if (!(inHand.getItem() instanceof BackpackItem)) {
            return;
        }
        ItemStack previous = player.getAttached(ModAttachments.WORN);
        player.setAttached(ModAttachments.WORN, inHand.copy());
        player.setItemInHand(hand, previous == null ? ItemStack.EMPTY : previous);
    }

    /** Takes the worn backpack off and puts it into the inventory. */
    public static void takeOff(ServerPlayer player) {
        ItemStack worn = player.getAttached(ModAttachments.WORN);
        if (worn == null || worn.isEmpty()) {
            return;
        }
        if (player.getInventory().add(worn)) {
            player.removeAttached(ModAttachments.WORN);
        } else {
            player.displayClientMessage(Component.translatable("message.simplebackpacks.inventory_full"), true);
        }
    }

    public static void handle(ServerPlayer player, int action) {
        if (action == BackpackActionPayload.TAKE_OFF) {
            takeOff(player);
        } else {
            openWorn(player);
        }
    }
}
