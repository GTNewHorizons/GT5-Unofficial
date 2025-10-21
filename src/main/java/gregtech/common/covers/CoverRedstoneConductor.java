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
            case 0 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_strongest_in"));
            case 1 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_bottom_in"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_top_in"));
            case 3 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_north_in"));
            case 4 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_south_in"));
            case 5 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_west_in"));
            case 6 -> GTUtility
                .sendChatToPlayer(aPlayer, GTUtility.translate("gt.chat.interact.desc.conducts_east_in"));
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
