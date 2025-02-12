package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.CoverInfo;

public class CoverPlacerBase implements CoverPlacer {

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack cover, ICoverable tileEntity) {
        return true;
    }

    @Override
    public boolean isSimpleCover() {
        return false;
    }

    /**
     * sets the Cover upon placement.
     */
    @Override
    public final void placeCover(EntityPlayer player, ItemStack cover, ICoverable tileEntity, ForgeDirection side) {
        CoverInfo coverInfo = CoverRegistry.getRegistration(cover)
            .buildCover(side, tileEntity, cover);
        tileEntity.attachCover(coverInfo, side);
        coverInfo.onPlayerAttach(player, cover);
    }
}
