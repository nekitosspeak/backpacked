package com.example.simplebackpacks;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BackpackItem extends Item {
    private final int rows;

    public BackpackItem(Properties properties, int rows) {
        super(properties);
        this.rows = rows;
    }

    public int getRows() {
        return rows;
    }

    /** Right click: open. Shift + right click: put on the back. */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                BackpackActions.wear(serverPlayer, hand);
            } else {
                BackpackActions.openFromHand(serverPlayer, hand);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
