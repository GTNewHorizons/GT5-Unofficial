package com.detrav.gui;

import com.detrav.gui.containers.DetravPortableAnvilContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * Created by Detrav on 30.10.2016.
 */
public class DetravPortableAnvilGui  extends GuiContainer {
    public static final int GUI_ID = 40;

    private static final ResourceLocation field_147093_u = new ResourceLocation("textures/gui/container/anvil.png");

    public DetravPortableAnvilGui(InventoryPlayer inventory, World world, ItemStack currentEquippedItem) {
        super(new DetravPortableAnvilContainer(inventory,world,currentEquippedItem));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147093_u);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
