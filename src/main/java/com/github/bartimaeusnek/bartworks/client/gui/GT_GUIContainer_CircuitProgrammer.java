package com.github.bartimaeusnek.bartworks.client.gui;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.server.container.GT_Container_CircuitProgrammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GT_GUIContainer_CircuitProgrammer extends GuiContainer {

    public GT_GUIContainer_CircuitProgrammer(InventoryPlayer p_i1072_1_) {
        super(new GT_Container_CircuitProgrammer(p_i1072_1_));
    }
    public static final ResourceLocation texture = new ResourceLocation(MainMod.modID, "textures/GUI/GUI_CircuitP.png");
    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(guiLeft-79, guiTop, 0, 0, 256, 165);
    }
}
