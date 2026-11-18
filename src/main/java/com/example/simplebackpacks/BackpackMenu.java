package com.example.simplebackpacks;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Vanilla chest GUI (no custom client code) with protection against putting a backpack into itself. */
public class BackpackMenu extends ChestMenu {
    private final BackpackContainer backpack;
    private final int backpackSlots;

    public BackpackMenu(int containerId, Inventory inventory, BackpackContainer container, int rows) {
        super(typeFor(rows), containerId, inventory, container, rows);
        this.backpack = container;
        this.backpackSlots = rows * 9;
    }

    private static MenuType<ChestMenu> typeFor(int rows) {
        return switch (rows) {
            case 1 -> MenuType.GENERIC_9x1;
            case 2 -> MenuType.GENERIC_9x2;
            case 3 -> MenuType.GENERIC_9x3;
            case 4 -> MenuType.GENERIC_9x4;
            case 5 -> MenuType.GENERIC_9x5;
            default -> MenuType.GENERIC_9x6;
        };
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (isForbidden(slotId, button, clickType, player)) {
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    private boolean isInBackpack(int slotId) {
        return slotId >= 0 && slotId < backpackSlots;
    }

    private boolean isForbidden(int slotId, int button, ClickType clickType, Player player) {
        ItemStack open = backpack.getBackpackStack();

        if (clickType == ClickType.SWAP) {
            Inventory inv = player.getInventory();
            ItemStack swapped = (button >= 0 && button < inv.getContainerSize()) ? inv.getItem(button) : ItemStack.EMPTY;
            if (swapped == open) {
                return true;
            }
            if (isInBackpack(slotId) && swapped.getItem() instanceof BackpackItem) {
                return true;
            }
        }

        if (slotId >= 0 && slotId < this.slots.size()) {
            Slot slot = this.slots.get(slotId);
            ItemStack clicked = slot.getItem();
            if (clicked == open) {
                return true; // the backpack that is currently open can't be moved
            }
            if (isInBackpack(slotId)) {
                if (getCarried().getItem() instanceof BackpackItem) {
                    return true; // no backpacks inside backpacks
                }
            } else if (clickType == ClickType.QUICK_MOVE && clicked.getItem() instanceof BackpackItem) {
                return true;
            }
        }
        return false;
    }
}
