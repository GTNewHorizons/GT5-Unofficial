package gregtech.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IWirelessCharger {

    IGregTechTileEntity getChargerTE();

    boolean canChargeItems(EntityPlayer player);

    void chargeItems(ItemStack[] stack, EntityPlayer player);
}
