package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;

public interface CoverPlacer {

    boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable coverable);

    boolean allowOnPrimitiveBlock();

    /**
     * If it lets you rightclick the Machine normally
     */
    boolean isGuiClickable();

    void placeCover(EntityPlayer player, ItemStack coverItem, ICoverable tileEntity, ForgeDirection side);
}
