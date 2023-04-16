package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;

@SuppressWarnings("unused") // TODO: Consider re-registering this
public class GT_Cover_RedstoneSignalizer extends GT_CoverBehavior {

    GT_Cover_RedstoneSignalizer(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + 1) % 48;
        switch (aCoverVariable / 16) {
            case 0 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("078", "Signal = ") + (aCoverVariable & 0xF));
            case 1 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("079", "Conditional Signal = ") + (aCoverVariable & 0xF));
            case 2 -> GT_Utility.sendChatToPlayer(
                aPlayer,
                GT_Utility.trans("080", "Inverted Conditional Signal = ") + (aCoverVariable & 0xF));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public byte getRedstoneInput(ForgeDirection aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        if (aCoverVariable < 16) {
            return (byte) (aCoverVariable & 0xF);
        }
        if ((aTileEntity instanceof IMachineProgress)) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork()) {
                if (aCoverVariable / 16 == 1) {
                    return (byte) (aCoverVariable & 0xF);
                }
            } else if (aCoverVariable / 16 == 2) {
                return (byte) (aCoverVariable & 0xF);
            }
            return 0;
        }
        return (byte) (aCoverVariable & 0xF);
    }
}
