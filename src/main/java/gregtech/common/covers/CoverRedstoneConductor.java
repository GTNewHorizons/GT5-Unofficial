package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;

@SuppressWarnings("unused") // Legacy from GT4. TODO: Consider re-enable registration
public class CoverRedstoneConductor extends CoverLegacyData {

    CoverRedstoneConductor(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if (this.coverData == 0) {
            coverable.setOutputRedstoneSignal(coverSide, coverable.getStrongestRedstone());
        } else if (this.coverData < 7) {
            coverable.setOutputRedstoneSignal(
                coverSide,
                coverable.getInternalInputRedstoneSignal(ForgeDirection.getOrientation((this.coverData - 1))));
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 7;
        if (this.coverData < 0) {
            this.coverData = 6;
        }
        switch (this.coverData) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("071", "Conducts strongest Input"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("072", "Conducts from bottom Input"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("073", "Conducts from top Input"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("074", "Conducts from north Input"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("075", "Conducts from south Input"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("076", "Conducts from west Input"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("077", "Conducts from east Input"));
        }
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
