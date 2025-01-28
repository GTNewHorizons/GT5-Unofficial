package gregtech.api.gui.modularui2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.SidedPosGuiData;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;

/**
 * Holds data regarding the situation in which the cover UI panel gets opened. Also provides some convenient methods.
 */
public class CoverGuiData extends SidedPosGuiData {

    private final int coverID;
    private final boolean anotherWindow;

    /**
     * @param player        Player opened this UI
     * @param coverID       ID of the cover
     * @param x             X position of the tile this cover is attached to
     * @param y             Y position of the tile this cover is attached to
     * @param z             Z position of the tile this cover is attached to
     * @param side          Side this cover is attached to
     * @param anotherWindow If cover UI is shown on top of another window
     */
    public CoverGuiData(EntityPlayer player, int coverID, int x, int y, int z, ForgeDirection side,
        boolean anotherWindow) {
        super(player, x, y, z, side);
        this.coverID = coverID;
        this.anotherWindow = anotherWindow;
    }

    public int getCoverID() {
        return coverID;
    }

    /**
     * If cover GUI is shown in opened on top of another window.
     */
    public boolean isAnotherWindow() {
        return anotherWindow;
    }

    public ICoverable getCoverable() {
        return (ICoverable) getTileEntity();
    }

    public CoverBehaviorBase<?> getCoverBehavior() {
        return getCoverable().getCoverBehaviorAtSideNew(getSide());
    }

    @NotNull
    public ISerializableObject getCoverData() {
        return getCoverable().getComplexCoverDataAtSide(getSide());
    }

    public void setCoverData(ISerializableObject coverData) {
        getCoverable().receiveCoverData(
            getSide(),
            coverID,
            coverData,
            getPlayer() instanceof EntityPlayerMP entityPlayerMP ? entityPlayerMP : null);
    }
}
