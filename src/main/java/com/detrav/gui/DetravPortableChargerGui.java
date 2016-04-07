package com.detrav.gui;

import com.detrav.gui.containers.DetravPortableChargerContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 07.04.2016.
 */
public class DetravPortableChargerGui extends GuiContainer {
    public static final int GUI_ID = 30;
    public DetravPortableChargerGui(InventoryPlayer player, World aWorld) {
        super(new DetravPortableChargerContainer(player, aWorld));

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //draw text and stuff here
        //the parameters for drawString are: string, x, y, color
        fontRendererObj.drawString("Tiny", 8, 6, 4210752);
        //draws "Inventory" or your regional equivalent
        fontRendererObj.drawString("container.inventory", 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2,
                                                   int par3) {
        //draw your Gui here, only thing you need to change is the path
        //int texture = mc.renderEngine.getTexture("/gui/trap.png");
        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //this.mc.renderEngine.bindTexture(texture);
        //int x = (width - xSize) / 2;
        //int y = (height - ySize) / 2;
        //this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

}
