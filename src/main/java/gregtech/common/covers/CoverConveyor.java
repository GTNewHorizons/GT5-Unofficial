package gregtech.common.covers;

import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTItemTransfer;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.base.CoverIOBaseGui;
import gregtech.common.gui.mui1.cover.ConveyorUIFactory;

public class CoverConveyor extends CoverIOBase {

    public final int tickRate;
    private final int stacksPerTransfer;
    private final int itemsPerStack;

    public CoverConveyor(CoverContext context, int aTickRate, int maxStacks, int itemsPerStack, ITexture coverTexture) {
        super(context, coverTexture);
        this.tickRate = aTickRate;
        this.stacksPerTransfer = maxStacks;
        this.itemsPerStack = itemsPerStack;
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    protected void doTransfer(ICoverable coverable) {
        GTItemTransfer transfer = new GTItemTransfer();

        switch (getIOMode()) {
            case EXPORT -> {
                transfer.push(coverable, coverSide, coverable.getTileEntityAtSide(coverSide));
            }
            case IMPORT -> {
                transfer.pull(coverable, coverSide, coverable.getTileEntityAtSide(coverSide));
            }
        }

        transfer.setStacksToTransfer(stacksPerTransfer);
        transfer.setMaxItemsPerTransfer(itemsPerStack);
        transfer.dropItems(coverable, coverSide);

        transfer.transfer();
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
        return this.tickRate;
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
