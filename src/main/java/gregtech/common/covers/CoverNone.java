package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;

public class CoverNone extends Cover {

    /**
     * This is the Dummy, if there is no Cover
     */
    public CoverNone(CoverContext context) {
        super(context, null);
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
