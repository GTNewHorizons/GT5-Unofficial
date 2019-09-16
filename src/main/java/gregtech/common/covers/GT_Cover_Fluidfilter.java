package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;
import static gregtech.api.enums.GT_Values.E;

public class GT_Cover_Fluidfilter extends GT_CoverBehavior {

    // Uses the lower 3 bits of the cover variable, so we have 8 options to work with (0-7)
    private final int FILTER_INPUT_DENY_OUTPUT = 0;
    private final int INVERT_INPUT_DENY_OUTPUT = 1;
    private final int FILTER_INPUT_ANY_OUTPUT = 2;
    private final int INVERT_INPUT_ANY_OUTPUT = 3;
    private final int DENY_INPUT_FILTER_OUTPUT = 4;
    private final int DENY_INPUT_INVERT_OUTPUT = 5;
    private final int ANY_INPUT_FILTER_OUTPUT = 6;
    private final int ANY_INPUT_INVERT_OUTPUT = 7;

    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        int aFilterMode = aCoverVariable & 7;
        int aFilterFluid = aCoverVariable >>> 3;
        final Fluid fluid = FluidRegistry.getFluid(aFilterFluid);
        if(fluid == null) return E;
        
        final FluidStack sFluid = new FluidStack(fluid, 1000);
        return(String.format("Filtering Fluid: %s  Mode: %s", sFluid.getLocalizedName(), getFilterMode(aFilterMode)));
    }


    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    public String getFilterMode(int aFilterMode) {
        switch(aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT: return(trans("043", "Filter input, Deny output"));
            case INVERT_INPUT_DENY_OUTPUT: return(trans("044", "Invert input, Deny output"));
            case FILTER_INPUT_ANY_OUTPUT: return(trans("045", "Filter input, Permit any output"));
            case INVERT_INPUT_ANY_OUTPUT: return(trans("046", "Invert input, Permit any output"));
            case DENY_INPUT_FILTER_OUTPUT: return(trans("219", "Deny input, Filter output"));
            case DENY_INPUT_INVERT_OUTPUT: return(trans("220", "Deny input, Invert output"));
            case ANY_INPUT_FILTER_OUTPUT: return(trans("221", "Permit any input, Filter output"));
            case ANY_INPUT_INVERT_OUTPUT: return(trans("222", "Permit any input, Invert output"));
            default: return("UNKNOWN");
        }

    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int aFilterMode = aCoverVariable & 7;
        aCoverVariable ^= aFilterMode;
        aFilterMode = (aFilterMode + (aPlayer.isSneaking()? -1 : 1)) % 8;
        if (aFilterMode < 0) {
            aFilterMode = 7;
        }

        GT_Utility.sendChatToPlayer(aPlayer, getFilterMode(aFilterMode));

        aCoverVariable|=aFilterMode;

        return aCoverVariable;
    }

    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        //GT_FML_LOGGER.info("rightclick");
        if (
                ((aX > 0.375D) && (aX < 0.625D)) ||
                ((aSide > 3) && ((aY > 0.375D) && (aY < 0.625D))) ||
                ((aSide < 2) && ((aZ > 0.375D) && (aZ < 0.625D))) ||
                (aSide == 2) ||
                (aSide == 3)
        ) {
            ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack == null) return true;

            FluidStack tFluid = FluidContainerRegistry.getFluidForFilledItem(tStack);
            if (tFluid != null) {
                int aFluid = tFluid.getFluidID();
                aCoverVariable = (aCoverVariable & 7) | (aFluid << 3);
                aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                GT_Utility.sendChatToPlayer(aPlayer, trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            } else if (tStack.getItem() instanceof IFluidContainerItem) {
                IFluidContainerItem tContainer = (IFluidContainerItem) tStack.getItem();
                if (tContainer.getFluid(tStack) != null) {
                    int aFluid = tContainer.getFluid(tStack).getFluidID();
                    aCoverVariable = (aCoverVariable & 7) | (aFluid << 3);
                    aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                    FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                    GT_Utility.sendChatToPlayer(aPlayer, trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable & 7;
        int aFilterFluid = aCoverVariable >>> 3;

        if (aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == DENY_INPUT_INVERT_OUTPUT)
            return false;
        else if (aFilterMode == ANY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT)
            return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == FILTER_INPUT_ANY_OUTPUT;
        else
            return aFilterMode == INVERT_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT;

    }
    
    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable & 7;
        int aFilterFluid = aCoverVariable >>> 3;

        if (aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_DENY_OUTPUT)
            return false;
        else if (aFilterMode == FILTER_INPUT_ANY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT)
            return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_FILTER_OUTPUT;
        else
            return aFilterMode == DENY_INPUT_INVERT_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT;

    }
    
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }
}
