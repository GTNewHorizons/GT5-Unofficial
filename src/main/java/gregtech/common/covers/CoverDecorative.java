package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.util.GTUtility;

public class CoverDecorative extends CoverLegacyData {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public CoverDecorative(CoverContext context) {
        super(context);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = ((this.coverData + 1) & 15);
        GTUtility.sendChatToPlayer(
            aPlayer,
            ((this.coverData & 1) != 0 ? GTUtility.trans("128.1", "Redstone ") : "")
                + ((this.coverData & 2) != 0 ? GTUtility.trans("129.1", "Energy ") : "")
                + ((this.coverData & 4) != 0 ? GTUtility.trans("130.1", "Fluids ") : "")
                + ((this.coverData & 8) != 0 ? GTUtility.trans("131.1", "Items ") : ""));
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return (coverData & 1) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return (coverData & 1) != 0;
    }

    @Override
    public boolean letsEnergyIn() {
        return (coverData & 2) != 0;
    }

    @Override
    public boolean letsEnergyOut() {
        return (coverData & 2) != 0;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return (coverData & 4) != 0;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return (coverData & 4) != 0;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return (coverData & 8) != 0;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return (coverData & 8) != 0;
    }
}
