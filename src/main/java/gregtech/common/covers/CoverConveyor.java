package gregtech.common.covers;

import static gregtech.api.util.GTUtility.moveMultipleItemStacks;
import static gregtech.api.util.GTUtility.sendChatToPlayer;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.common.gui.mui1.cover.ConveyorUIFactory;

public class CoverConveyor extends CoverLegacyData {

    public final int mTickRate;
    private final int mMaxStacks;

    public CoverConveyor(CoverContext context, int aTickRate, int maxStacks, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTickRate = aTickRate;
        this.mMaxStacks = maxStacks;
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
        if ((this.coverData % 6 > 1) && ((coverable instanceof IMachineProgress machine))) {
            if (machine.isAllowedToWork() != this.coverData % 6 < 4) {
                return;
            }
        }
        final TileEntity tTileEntity = coverable.getTileEntityAtSide(coverSide);
        final Object fromEntity = this.coverData % 2 == 0 ? coverable : tTileEntity;
        final Object toEntity = this.coverData % 2 != 0 ? coverable : tTileEntity;
        final ForgeDirection fromSide = this.coverData % 2 != 0 ? coverSide.getOpposite() : coverSide;
        final ForgeDirection toSide = this.coverData % 2 == 0 ? coverSide.getOpposite() : coverSide;

        moveMultipleItemStacks(
            fromEntity,
            toEntity,
            fromSide,
            toSide,
            null,
            false,
            (byte) 64,
            (byte) 1,
            (byte) 64,
            (byte) 1,
            this.mMaxStacks);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        coverData = (coverData + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (coverData < 0) {
            coverData = 11;
        }
        switch (coverData) {
            case 0 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.o"));
            case 1 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.i"));
            case 2 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.o_c"));
            case 3 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.i_c"));
            case 4 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.o_c_i"));
            case 5 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.i_c_i"));
            case 6 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.i2o"));
            case 7 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.o2i"));
            case 8 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.i2o_c"));
            case 9 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.o2i_c"));
            case 10 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.i2o_c_i"));
            case 11 -> sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.o2i_c_i"));
        }
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
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
        return (this.coverData >= 6) || (this.coverData % 2 != 0);
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return (this.coverData >= 6) || (this.coverData % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return this.mTickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ConveyorUIFactory(buildContext).createWindow();
    }

}
