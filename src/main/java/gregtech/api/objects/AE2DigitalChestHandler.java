package gregtech.api.objects;

import cpw.mods.fml.common.Optional;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

@Optional.Interface(iface = "appeng.api.storage.IExternalStorageHandler", modid = "appliedenergistics2", striprefs = true)
public class AE2DigitalChestHandler implements appeng.api.storage.IExternalStorageHandler {

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public boolean canHandle(final TileEntity te, final ForgeDirection d, final appeng.api.storage.StorageChannel chan, final appeng.api.networking.security.BaseActionSource mySrc) {
        return chan == appeng.api.storage.StorageChannel.ITEMS && te instanceof BaseMetaTileEntity && ((BaseMetaTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public appeng.api.storage.IMEInventory getInventory(final TileEntity te, final ForgeDirection d, final appeng.api.storage.StorageChannel chan, final appeng.api.networking.security.BaseActionSource src) {
        if (chan == appeng.api.storage.StorageChannel.ITEMS) {
            return ((GT_MetaTileEntity_DigitalChestBase) (((BaseMetaTileEntity) te).getMetaTileEntity()));
        }
        return null;
    }
}
