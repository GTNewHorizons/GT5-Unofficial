package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_Conveyor extends GT_CoverBehavior {
    public final int mTickRate;
    private final int mMaxStacks;

    public GT_Cover_Conveyor(int aTickRate) {
        this.mTickRate = aTickRate;
        this.mMaxStacks = 1;
    }

    public GT_Cover_Conveyor(int aTickRate, int maxStacks) {
        this.mTickRate = aTickRate;
        this.mMaxStacks = maxStacks;
    }

    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((aCoverVariable % 6 > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return aCoverVariable;
            }
        }
        TileEntity tTileEntity = aTileEntity.getTileEntityAtSide(aSide);
        Object fromEntity = aCoverVariable % 2 == 0 ? aTileEntity : tTileEntity,
                 toEntity = aCoverVariable % 2 != 0 ? aTileEntity : tTileEntity;
        byte fromSide = aCoverVariable % 2 != 0 ? GT_Utility.getOppositeSide(aSide) : aSide,
               toSide = aCoverVariable % 2 == 0 ? GT_Utility.getOppositeSide(aSide) : aSide;
        boolean costsEnergy = ((aCoverVariable % 2 == 0) || (aSide != 1)) && ((aCoverVariable % 2 != 0) || (aSide != 0)) && (aTileEntity.getUniversalEnergyCapacity() >= 128L);
        byte moved;

        for(int i=0 ; i < this.mMaxStacks ; i++) {
            // Costs energy but we don't have enough, bail
            if ((costsEnergy && !aTileEntity.isUniversalEnergyStored(256L)))
                break;

            moved = GT_Utility.moveOneItemStack(fromEntity, toEntity, fromSide , toSide, null, false, (byte) 64, (byte) 1, (byte) 64, (byte) 1);

            if(moved == 0)
                break;

            if (costsEnergy)
                aTileEntity.decreaseStoredEnergyUnits(4 * moved, true);
        }

        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking()? -1 : 1)) % 12;
        if(aCoverVariable <0){aCoverVariable = 11;}
        switch(aCoverVariable) {
            case 0: GT_Utility.sendChatToPlayer(aPlayer, trans("006", "Export")); break;
            case 1: GT_Utility.sendChatToPlayer(aPlayer, trans("007", "Import")); break;
            case 2: GT_Utility.sendChatToPlayer(aPlayer, trans("008", "Export (conditional)")); break;
            case 3: GT_Utility.sendChatToPlayer(aPlayer, trans("009", "Import (conditional)")); break;
            case 4: GT_Utility.sendChatToPlayer(aPlayer, trans("010", "Export (invert cond)")); break;
            case 5: GT_Utility.sendChatToPlayer(aPlayer, trans("011", "Import (invert cond)")); break;
            case 6: GT_Utility.sendChatToPlayer(aPlayer, trans("012", "Export allow Input")); break;
            case 7: GT_Utility.sendChatToPlayer(aPlayer, trans("013", "Import allow Output")); break;
            case 8: GT_Utility.sendChatToPlayer(aPlayer, trans("014", "Export allow Input (conditional)")); break;
            case 9: GT_Utility.sendChatToPlayer(aPlayer, trans("015", "Import allow Output (conditional)")); break;
            case 10: GT_Utility.sendChatToPlayer(aPlayer, trans("016", "Export allow Input (invert cond)")); break;
            case 11: GT_Utility.sendChatToPlayer(aPlayer, trans("017", "Import allow Output (invert cond)")); break;
        }
        return aCoverVariable;
    }

    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
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
        return (aCoverVariable >= 6) || (aCoverVariable % 2 != 0);
    }

    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return (aCoverVariable >= 6) || (aCoverVariable % 2 == 0);
    }

    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return this.mTickRate;
    }
}
