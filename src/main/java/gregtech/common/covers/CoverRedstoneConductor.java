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

    @Override
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
            case 0 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.strongest");
            case 1 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.bottom");
            case 2 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.top");
            case 3 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.north");
            case 4 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.south");
            case 5 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.west");
            case 6 -> GTUtility.sendChatTrans(aPlayer, "GT5U.chat.cover.redstone_conductor.input.east");
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
