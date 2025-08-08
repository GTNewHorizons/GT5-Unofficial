package tectech.thing.cover;

import static ic2.api.info.Info.DMG_ELECTRIC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverContext;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.interfaces.tileentity.ICoverable;
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
        if (coverable != null && !coverable.isClientSide()) {
            ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove(
                new TeslaCoverConnection(
                    coverable.getIGregTechTileEntityOffset(0, 0, 0),
                    getTeslaReceptionCapability()));
        }
    }

    @Override
    public void onCoverRemoval() {
        ICoverable coverable = coveredTile.get();
        if (coverable != null) ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove(
            new TeslaCoverConnection(coverable.getIGregTechTileEntityOffset(0, 0, 0), getTeslaReceptionCapability()));
    }

    @Override
    public void onBaseTEDestroyed() {
        ICoverable coverable = coveredTile.get();
        if (coverable != null) ITeslaConnectable.TeslaUtil.teslaSimpleNodeSetRemove(
            new TeslaCoverConnection(coverable.getIGregTechTileEntityOffset(0, 0, 0), getTeslaReceptionCapability()));
    }

    @Override
    public String getDescription() {
        return "Do not attempt to use screwdriver!"; // TODO Translation support
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
