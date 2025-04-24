package gregtech.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IWirelessCharger {

    IGregTechTileEntity getChargerTE();

    boolean canChargePlayerItems(EntityPlayer player);

    void chargePlayerItems(ItemStack[] stack, EntityPlayer player);
}
