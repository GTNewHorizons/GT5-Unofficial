package gregtech.api.gui;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.entity.player.InventoryPlayer;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

@Deprecated
public class GT_GUIContainer_1by1 extends GT_GUIContainerMetaTile_Machine {

    private final String mName;
    private final int textColor = this.getTextColorOrDefault("title", 0x404040);

    public GT_GUIContainer_1by1(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(
                new GT_Container_1by1(aInventoryPlayer, aTileEntity),
                GregTech.getResourcePath("textures", "gui", "1by1.png"));
        mName = aName;
    }

    public GT_GUIContainer_1by1(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
            String aBackground) {
        super(
                new GT_Container_1by1(aInventoryPlayer, aTileEntity),
                GregTech.getResourcePath("textures", "gui", aBackground + "1by1.png"));
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(mName, 8, 4, textColor);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
