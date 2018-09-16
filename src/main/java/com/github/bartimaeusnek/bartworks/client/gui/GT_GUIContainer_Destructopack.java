package com.github.bartimaeusnek.bartworks.client.gui;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_Item_Destructopack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GT_GUIContainer_Destructopack extends GuiContainer
{
    public GT_GUIContainer_Destructopack(InventoryPlayer inventory) {
        super(new GT_Container_Item_Destructopack(inventory));
    }
    public static final ResourceLocation texture = new ResourceLocation(MainMod.modID, "textures/GT2/gui/Destructopack.png");

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 175, 165);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    }


}
