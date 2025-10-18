package gregtech.common.covers;

import static gregtech.api.util.GTUtility.moveMultipleItemStacks;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.base.CoverIOBaseGui;
import gregtech.common.gui.mui1.cover.ConveyorUIFactory;

public class CoverConveyor extends CoverIOBase {

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
    protected void doTransfer(ICoverable coverable) {
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
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverIOBaseGui(this, "cover.conveyor");
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ConveyorUIFactory(buildContext).createWindow();
    }

}
