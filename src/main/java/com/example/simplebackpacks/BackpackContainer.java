package com.example.simplebackpacks;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/** Inventory that stores its contents inside the backpack ItemStack (CONTAINER component). */
public class BackpackContainer extends SimpleContainer {
    private final ItemStack stack;
    private final Predicate<Player> stillValid;
    private final Consumer<ItemStack> afterSave;
    private boolean loaded = false;

    public BackpackContainer(ItemStack stack, int size, Predicate<Player> stillValid, Consumer<ItemStack> afterSave) {
        super(size);
        this.stack = stack;
        this.stillValid = stillValid;
        this.afterSave = afterSave;

        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
        for (int i = 0; i < size; i++) {
            setItem(i, items.get(i));
        }
        this.loaded = true;
    }

    public ItemStack getBackpackStack() {
        return stack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (!loaded) {
            return;
        }
        NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < getContainerSize(); i++) {
            items.set(i, getItem(i));
        }
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
        afterSave.accept(stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return !stack.isEmpty() && stillValid.test(player);
    }
}
