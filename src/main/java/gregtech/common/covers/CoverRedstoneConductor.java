package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;

@SuppressWarnings("unused") // Legacy from GT4. TODO: Consider re-enable registration
public class CoverRedstoneConductor extends CoverBehavior {

    CoverRedstoneConductor(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable == 0) {
            aTileEntity.setOutputRedstoneSignal(side, aTileEntity.getStrongestRedstone());
        } else if (aCoverVariable < 7) {
            aTileEntity.setOutputRedstoneSignal(
                side,
                aTileEntity.getInternalInputRedstoneSignal(ForgeDirection.getOrientation((aCoverVariable - 1))));
        }
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 7;
        if (aCoverVariable < 0) {
            aCoverVariable = 6;
        }
        switch (aCoverVariable) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("071", "Conducts strongest Input"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("072", "Conducts from bottom Input"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("073", "Conducts from top Input"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("074", "Conducts from north Input"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("075", "Conducts from south Input"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("076", "Conducts from west Input"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("077", "Conducts from east Input"));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }
}
