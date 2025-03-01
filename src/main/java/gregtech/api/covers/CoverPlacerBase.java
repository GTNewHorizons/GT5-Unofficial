package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;

public class CoverPlacerBase implements CoverPlacer {

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable tileEntity) {
        return true;
    }

    @Override
    public boolean isSimpleCover() {
        return false;
    }

    @Override
    public boolean isGuiClickable() {
        return true;
    }

    /**
     * sets the Cover upon placement.
     */
    @Override
    public final void placeCover(EntityPlayer player, ItemStack coverItem, ICoverable tileEntity, ForgeDirection side) {
        Cover cover = CoverRegistry.getRegistration(coverItem)
            .buildCover(side, tileEntity, coverItem);
        tileEntity.attachCover(cover, side);
        cover.onPlayerAttach(player, coverItem);
    }
}
