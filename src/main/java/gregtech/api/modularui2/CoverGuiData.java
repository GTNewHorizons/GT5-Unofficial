package gregtech.api.modularui2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.SidedPosGuiData;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;

/**
 * Holds data regarding the situation in which the cover UI panel gets opened. Also provides some convenient methods.
 */
public class CoverGuiData extends SidedPosGuiData {

    private final int coverID;

    /**
     * @param player  Player opened this UI
     * @param coverID ID of the cover
     * @param x       X position of the tile this cover is attached to
     * @param y       Y position of the tile this cover is attached to
     * @param z       Z position of the tile this cover is attached to
     * @param side    Side this cover is attached to
     */
    public CoverGuiData(EntityPlayer player, int coverID, int x, int y, int z, ForgeDirection side) {
        super(player, x, y, z, side);
        this.coverID = coverID;
    }

    public int getCoverID() {
        return coverID;
    }

    public ICoverable getCoverable() {
        return (ICoverable) getTileEntity();
    }

    @NotNull
    public Cover getCover() {
        return getCoverable().getCoverAtSide(getSide());
    }
}
