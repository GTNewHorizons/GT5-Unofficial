package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;

public interface CoverPlacer {

    boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable coverable);

    boolean isSimpleCover();

    void placeCover(EntityPlayer player, ItemStack cover, ICoverable tileEntity, ForgeDirection side);
}
