package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject.LegacyCoverData;

@SuppressWarnings("unused") // Legacy from GT4. TODO: Consider re-enable registration
public class CoverRedstoneConductor extends CoverBehavior {

    CoverRedstoneConductor(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        int coverDataValue = coverData.get();
        if (coverDataValue == 0) {
            coverable.setOutputRedstoneSignal(coverSide, coverable.getStrongestRedstone());
        } else if (coverDataValue < 7) {
            coverable.setOutputRedstoneSignal(
                coverSide,
                coverable.getInternalInputRedstoneSignal(ForgeDirection.getOrientation((coverDataValue - 1))));
        }
        return LegacyCoverData.of(coverDataValue);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + (aPlayer.isSneaking() ? -1 : 1)) % 7;
        if (coverDataValue < 0) {
            coverDataValue = 6;
        }
        switch (coverDataValue) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("071", "Conducts strongest Input"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("072", "Conducts from bottom Input"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("073", "Conducts from top Input"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("074", "Conducts from north Input"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("075", "Conducts from south Input"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("076", "Conducts from west Input"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("077", "Conducts from east Input"));
        }
        coverData.set(coverDataValue);
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }
}
