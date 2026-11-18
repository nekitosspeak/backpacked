package com.example.simplebackpacks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
    // rows * 9 slots: 18 / 36 / 54
    public static final BackpackItem LEATHER_BACKPACK = register("leather_backpack", 2);
    public static final BackpackItem IRON_BACKPACK = register("iron_backpack", 4);
    public static final BackpackItem DIAMOND_BACKPACK = register("diamond_backpack", 6);

    private ModItems() {}

    private static BackpackItem register(String name, int rows) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(SimpleBackpacksMod.MOD_ID, name));
        BackpackItem item = new BackpackItem(new Item.Properties().setId(key).stacksTo(1), rows);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(LEATHER_BACKPACK);
            entries.accept(IRON_BACKPACK);
            entries.accept(DIAMOND_BACKPACK);
        });
    }
}
