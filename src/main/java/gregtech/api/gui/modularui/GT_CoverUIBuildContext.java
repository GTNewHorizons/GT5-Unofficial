package gregtech.api.gui.modularui;

import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import net.minecraft.entity.player.EntityPlayer;

public class GT_CoverUIBuildContext extends UIBuildContext {

    // cover data is not synced to client, while ID is
    private final int coverID;
    private final byte side;
    private final ICoverable tile;
    private boolean anotherWindow;

    public GT_CoverUIBuildContext(EntityPlayer player, int coverID, byte side, ICoverable tile, boolean anotherWindow) {
        super(player);
        this.coverID = coverID;
        this.side = side;
        this.tile = tile;
        this.anotherWindow = anotherWindow;
    }

    public int getCoverID() {
        return coverID;
    }

    public byte getCoverSide() {
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
}
