package gregtech.api.gui.modularui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverUIBuildContext extends UIBuildContext {

    // cover data is not synced to client, while ID is
    private final int coverID;
    private final ForgeDirection side;
    private final ICoverable tile;
    private final boolean anotherWindow;
    private final int guiColorization;

    /**
     * @param player          Player opened this UI
     * @param coverID         See {@link ICoverable#getCoverIDAtSide}
     * @param side            Side this cover is attached to
     * @param tile            Tile this cover is attached to
     * @param anotherWindow   If cover UI is shown on top of another window
     * @param guiColorization The color used to render machine's GUI
     */
    public CoverUIBuildContext(EntityPlayer player, int coverID, ForgeDirection side, ICoverable tile,
        boolean anotherWindow, int guiColorization) {
        super(player);
        this.coverID = coverID;
        this.side = side;
        this.tile = tile;
        this.anotherWindow = anotherWindow;
        this.guiColorization = guiColorization;
    }

    /**
     * @param player        Player opened this UI
     * @param coverID       See {@link ICoverable#getCoverIDAtSide}
     * @param side          Side this cover is attached to
     * @param tile          Tile this cover is attached to
     * @param anotherWindow If cover GUI is shown in opened on top of another window
     */
    public CoverUIBuildContext(EntityPlayer player, int coverID, ForgeDirection side, ICoverable tile,
        boolean anotherWindow) {
        this(player, coverID, side, tile, anotherWindow, tile.getGUIColorization());
    }

    public int getCoverID() {
        return coverID;
    }

    public ForgeDirection getCoverSide() {
        return side;
    }

    /**
     * Note that this will return different object between client v.s. server side on SP.
     */
    public ICoverable getTile() {
        return tile;
    }

    /**
     * If cover GUI is shown in opened on top of another window.
     */
    public boolean isAnotherWindow() {
        return anotherWindow;
    }

    public int getGuiColorization() {
        return guiColorization;
    }
}
