package gregtech.api.gui;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_GtTileEntityGuiRequest;

@Deprecated
public abstract class GT_GUICover extends GT_GUIScreen {

    public final ICoverable tile;
    public int parentGuiId = -1;

    public GT_GUICover(ICoverable tile, int width, int height, ItemStack cover) {
        super(width, height, cover == null ? "" : cover.getDisplayName());
        this.tile = tile;
        headerIcon.setItem(cover);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!tile.isUseableByPlayer(mc.thePlayer)) {
            closeScreen();
        }
    }

    /**
     * The parent GUI to exit to. -1 is ignored.
     * 
     * @param parentGuiId
     */
    public void setParentGuiId(int parentGuiId) {
        this.parentGuiId = parentGuiId;
    }

    @Override
    public void closeScreen() {
        // If this cover was given a guiId, tell the server to open it for us when this GUI closes.
        if (parentGuiId != -1 && tile.isUseableByPlayer(mc.thePlayer)) {
            GT_Values.NW.sendToServer(
                new GT_Packet_GtTileEntityGuiRequest(
                    tile.getXCoord(),
                    tile.getYCoord(),
                    tile.getZCoord(),
                    parentGuiId,
                    tile.getWorld().provider.dimensionId,
                    mc.thePlayer.getEntityId()));
        } else {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
    }
}
