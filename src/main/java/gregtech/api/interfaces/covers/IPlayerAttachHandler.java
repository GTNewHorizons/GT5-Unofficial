package gregtech.api.interfaces.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;

/** Allows covers to perform special actions when they are attached to stuff. */
public interface IPlayerAttachHandler {

    void onPlayerAttach(EntityPlayer player, ItemStack aCover, ICoverable aTileEntity, ForgeDirection side);
}
