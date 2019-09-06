package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_ItemMeter
        extends GT_CoverBehavior {
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        int[] tSlots;
        if (aCoverVariable < 2) {
            tSlots = aTileEntity.getAccessibleSlotsFromSide(aSide);
        } else {
            tSlots = new int[]{aCoverVariable - 2};
        }
        int tMax = 0;
        int tUsed = 0;
        for (int i : tSlots) {
            if (i >= 0 && i < aTileEntity.getSizeInventory()) {
                tMax+=64;
                ItemStack tStack = aTileEntity.getStackInSlot(i);
                if (tStack != null)
                    tUsed += (tStack.stackSize<<6)/tStack.getMaxStackSize();
            }
        }
        if(tUsed==0)//nothing
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 1 ? 15 : 0));
        else if(tUsed >= tMax)//full
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 1 ? 0 : 15));
        else//1-14 range
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 1 ? 14-((14*tUsed)/tMax) : 1+((14*tUsed)/tMax)) );
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking()? -1 : 1)) % (2 + aTileEntity.getSizeInventory());
        switch(aCoverVariable) {
            case 0: GT_Utility.sendChatToPlayer(aPlayer, trans("051", "Normal")); break;
            case 1: GT_Utility.sendChatToPlayer(aPlayer, trans("052", "Inverted")); break;
            default: GT_Utility.sendChatToPlayer(aPlayer, trans("053", "Slot: ") + (aCoverVariable - 2)); break;
        }
        return aCoverVariable;
    }

    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }
}
