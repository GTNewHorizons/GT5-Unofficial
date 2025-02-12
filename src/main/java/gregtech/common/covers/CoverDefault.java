package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject.LegacyCoverData;

public class CoverDefault extends CoverBehavior {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public CoverDefault(CoverContext context) {
        super(context);
    }

    @Override
    public LegacyCoverData onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int coverDataValue = coverData.get();
        coverDataValue = ((coverDataValue + 1) & 15);
        GTUtility.sendChatToPlayer(
            aPlayer,
            ((coverDataValue & 1) != 0 ? GTUtility.trans("128.1", "Redstone ") : "")
                + ((coverDataValue & 2) != 0 ? GTUtility.trans("129.1", "Energy ") : "")
                + ((coverDataValue & 4) != 0 ? GTUtility.trans("130.1", "Fluids ") : "")
                + ((coverDataValue & 8) != 0 ? GTUtility.trans("131.1", "Items ") : ""));
        return LegacyCoverData.of(coverDataValue);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return (coverData.get() & 1) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return (coverData.get() & 1) != 0;
    }

    @Override
    public boolean letsEnergyIn() {
        return (coverData.get() & 2) != 0;
    }

    @Override
    public boolean letsEnergyOut() {
        return (coverData.get() & 2) != 0;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return (coverData.get() & 4) != 0;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return (coverData.get() & 4) != 0;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return (coverData.get() & 8) != 0;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return (coverData.get() & 8) != 0;
    }
}
