package gregtech.api.objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;

public class GT_Cover_Default extends GT_CoverBehavior {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public GT_Cover_Default() {
        super();
    }

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = ((aCoverVariable + 1) & 15);
        GT_Utility.sendChatToPlayer(
            aPlayer,
            ((aCoverVariable & 1) != 0 ? GT_Utility.trans("128.1", "Redstone ") : "")
                + ((aCoverVariable & 2) != 0 ? GT_Utility.trans("129.1", "Energy ") : "")
                + ((aCoverVariable & 4) != 0 ? GT_Utility.trans("130.1", "Fluids ") : "")
                + ((aCoverVariable & 8) != 0 ? GT_Utility.trans("131.1", "Items ") : ""));
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 1) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 1) != 0;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 2) != 0;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 2) != 0;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable & 4) != 0;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable & 4) != 0;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable & 8) != 0;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable & 8) != 0;
    }
}
