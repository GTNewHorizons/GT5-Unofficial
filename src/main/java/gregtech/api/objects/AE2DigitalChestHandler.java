package gregtech.api.objects;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;

public class AE2DigitalChestHandler implements appeng.api.storage.IExternalStorageHandler {

    @Override
    public boolean canHandle(final TileEntity te, final ForgeDirection d, final appeng.api.storage.StorageChannel chan,
            final appeng.api.networking.security.BaseActionSource mySrc) {
        return chan == appeng.api.storage.StorageChannel.ITEMS && te instanceof BaseMetaTileEntity
                && ((BaseMetaTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase;
    }

    @Override
    public appeng.api.storage.IMEInventory<?> getInventory(final TileEntity te, final ForgeDirection d,
            final appeng.api.storage.StorageChannel chan, final appeng.api.networking.security.BaseActionSource src) {
        if (chan == appeng.api.storage.StorageChannel.ITEMS) {
            return ((GT_MetaTileEntity_DigitalChestBase) (((BaseMetaTileEntity) te).getMetaTileEntity()));
        }
        return null;
    }
}
