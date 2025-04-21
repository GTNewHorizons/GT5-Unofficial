package gregtech.api.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.common.covers.Cover;

public final class CoverNone extends Cover {

    /**
     * Explicitly package-private so the reference is held in CoverRegistry
     */
    static CoverNone instance = new CoverNone();

    /**
     * This is the Dummy, if there is no Cover.
     */
    private CoverNone() {
        super(new CoverContext(null, ForgeDirection.UNKNOWN, null), null);
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
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean onCoverShiftRightClick(EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public byte getRedstoneInput(byte aInputRedstone) {
        return aInputRedstone;
    }

    @Override
    public ItemStack asItemStack() {
        return null;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        // Cancel opening the UI.
        return null;
    }

    @Override
    public boolean allowsCopyPasteTool() {
        return false;
    }
}
