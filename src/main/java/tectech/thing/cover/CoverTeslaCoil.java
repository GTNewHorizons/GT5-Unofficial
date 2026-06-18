package tectech.thing.cover;

import static ic2.api.info.Info.DMG_ELECTRIC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverContext;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.covers.Cover;
import tectech.mechanics.tesla.ITeslaConnectable;
import tectech.mechanics.tesla.TeslaCoverConnection;

public class CoverTeslaCoil extends Cover {

    public CoverTeslaCoil(CoverContext context) {
        super(context, null);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        // Only do stuff if we're on top and have power
        if (coverable != null && coverSide == ForgeDirection.UP || coverable.getEUCapacity() > 0) {
            // Makes sure we're on the list
            ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetAdd(
                new TeslaCoverConnection(
                    coverable.getIGregTechTileEntityOffset(0, 0, 0),
                    getTeslaReceptionCapability()));
        }
    }

    @Override
    public void onCoverUnload() {
        ICoverable coverable = coveredTile.get();
        if (coverable instanceof IGregTechTileEntity IGT && !coverable.isClientSide()) {
            ITeslaConnectable.TeslaUtil
                .teslaSimpleNodeSetRemove(new TeslaCoverConnection(IGT, getTeslaReceptionCapability()));
        }
    }

    @Override
    public void onCoverRemoval() {
        ICoverable coverable = coveredTile.get();
        if (coverable instanceof IGregTechTileEntity IGT) ITeslaConnectable.TeslaUtil
            .teslaSimpleNodeSetRemove(new TeslaCoverConnection(IGT, getTeslaReceptionCapability()));
    }

    @Override
    public void onBaseTEDestroyed() {
        ICoverable coverable = coveredTile.get();
        if (coverable instanceof IGregTechTileEntity IGT) ITeslaConnectable.TeslaUtil
            .teslaSimpleNodeSetRemove(new TeslaCoverConnection(IGT, getTeslaReceptionCapability()));
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("gt.interact.desc.ban_screwdriver");
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ICoverable coverable = coveredTile.get();
        // Shock a non-hazmat player if they dare stuff a screwdriver into one of these
        if (coverable != null && coverable.getStoredEU() > 0 && !HazardProtection.isWearingFullElectroHazmat(aPlayer)) {
            aPlayer.attackEntityFrom(DMG_ELECTRIC, 20);
        }
    }

    @Override
    public int getMinimumTickRate() {
        // It updates once every 10 ticks, so once every 0.5 second
        return 10;
    }

    public byte getTeslaReceptionCapability() {
        return 2;
    }
}
