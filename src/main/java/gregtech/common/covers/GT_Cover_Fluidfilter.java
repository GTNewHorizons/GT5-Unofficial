package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;


public class GT_Cover_Fluidfilter
        extends GT_CoverBehavior {
            
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int aFilterMode = aCoverVariable & 7;
        aCoverVariable ^=aFilterMode;
        aFilterMode = (aFilterMode + (aPlayer.isSneaking()? -1 : 1)) % 4;
        if(aFilterMode < 0){aFilterMode = 3;}
        switch(aFilterMode) {
            case 0: GT_Utility.sendChatToPlayer(aPlayer, trans("043", "Allow input, no output")); break;
            case 1: GT_Utility.sendChatToPlayer(aPlayer, trans("044", "Deny input, no output")); break;
            case 2: GT_Utility.sendChatToPlayer(aPlayer, trans("045", "Allow input, permit any output")); break;
            case 3: GT_Utility.sendChatToPlayer(aPlayer, trans("046", "Deny input, permit any output")); break;
        }
        aCoverVariable|=aFilterMode;
        return aCoverVariable;
    }
    
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
       //System.out.println("rightclick");
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide > 3) && (((aY > 0.375D) && (aY < 0.625D)) || ((aSide < 2) && (((aZ > 0.375D) && (aZ < 0.625D)) || (aSide == 2) || (aSide == 3)))))) {
            ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if(tStack!=null){
            FluidStack tFluid = FluidContainerRegistry.getFluidForFilledItem(tStack);
            if(tFluid!=null){
                //System.out.println(tFluid.getLocalizedName()+" "+tFluid.getFluidID());
                int aFluid = tFluid.getFluidID();
                aCoverVariable = (aCoverVariable & 7) | (aFluid << 3);
                aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid),1000);
                GT_Utility.sendChatToPlayer(aPlayer, trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            }else if(tStack.getItem() instanceof IFluidContainerItem){
                IFluidContainerItem tContainer = (IFluidContainerItem)tStack.getItem();
                if(tContainer.getFluid(tStack) != null) {
                    int aFluid = tContainer.getFluid(tStack).getFluidID();
                    aCoverVariable = (aCoverVariable & 7) | (aFluid << 3);
                    aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                    FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid),1000);
                    GT_Utility.sendChatToPlayer(aPlayer, trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
                }
            }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if(aFluid==null){return true;}
        int aFilterMode = aCoverVariable & 7;
        int aFilterFluid = aCoverVariable >>> 3;
        return aFluid.getID() == aFilterFluid ? aFilterMode == 0 || aFilterMode == 2 : aFilterMode == 1 || aFilterMode == 3;
    }
    
    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        int aFilterMode = aCoverVariable & 7;
        return aFilterMode != 0 && aFilterMode != 1;
    }
    
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }
}
