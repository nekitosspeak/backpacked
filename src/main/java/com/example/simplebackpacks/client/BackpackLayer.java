package com.example.simplebackpacks.client;

import com.example.simplebackpacks.ModAttachments;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Draws the worn backpack on the player's back (the item model is drawn as a flat 3D sprite). */
public class BackpackLayer<M extends EntityModel<AvatarRenderState>> extends RenderLayer<AvatarRenderState, M> {
    // Tweak these if the backpack sits wrong. Layer space: +y is DOWN, +z is BEHIND the player.
    private static final float SCALE = 0.6F;
    private static final float OFFSET_Y = 0.40F;  // body spans y = 0 .. 0.75
    private static final float OFFSET_Z = 0.15F;  // back of the body is at z = 0.125

    public BackpackLayer(RenderLayerParent<AvatarRenderState, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
                       AvatarRenderState state, float yRot, float xRot) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        Entity entity = minecraft.level.getEntity(state.id);
        if (!(entity instanceof LivingEntity living)) {
            return;
        }
        ItemStack worn = entity.getAttached(ModAttachments.WORN);
        if (worn == null || worn.isEmpty()) {
            return;
        }

        ItemStackRenderState itemState = new ItemStackRenderState();
        minecraft.getItemModelResolver().updateForLiving(itemState, worn, ItemDisplayContext.NONE, living);
        if (itemState.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.0F, OFFSET_Y, OFFSET_Z);
        poseStack.scale(-SCALE, -SCALE, SCALE); // item space (y up) -> layer space (y down)
        itemState.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
