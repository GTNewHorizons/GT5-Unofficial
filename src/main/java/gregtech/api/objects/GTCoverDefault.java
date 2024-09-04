package gregtech.api.objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;

public class GTCoverDefault extends CoverBehavior {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public GTCoverDefault() {
        super();
    }

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = ((aCoverVariable + 1) & 15);
        GTUtility.sendChatToPlayer(
            aPlayer,
            ((aCoverVariable & 1) != 0 ? GTUtility.trans("128.1", "Redstone ") : "")
                + ((aCoverVariable & 2) != 0 ? GTUtility.trans("129.1", "Energy ") : "")
                + ((aCoverVariable & 4) != 0 ? GTUtility.trans("130.1", "Fluids ") : "")
                + ((aCoverVariable & 8) != 0 ? GTUtility.trans("131.1", "Items ") : ""));
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 1) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 1) != 0;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 2) != 0;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return (aCoverVariable & 2) != 0;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable & 4) != 0;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable & 4) != 0;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable & 8) != 0;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable & 8) != 0;
    }
}
