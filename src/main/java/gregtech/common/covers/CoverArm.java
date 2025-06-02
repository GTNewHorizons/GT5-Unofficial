package gregtech.common.covers;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.ArmUIFactory;

public class CoverArm extends CoverLegacyData {

    public final int mTickRate;
    // msb converted, 2nd : direction (1=export)
    // right 14 bits: internalSlot, next 14 bits adjSlot, 0 = all, slot = -1
    public static final int EXPORT_MASK = 0x40000000;
    public static final int SLOT_ID_MASK = 0x3FFF;
    protected static final int SLOT_ID_MIN = 0;
    public static final int CONVERTED_BIT = 0x80000000;

    public CoverArm(CoverContext context, int aTickRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTickRate = aTickRate;
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null || (((coverable instanceof IMachineProgress machine)) && (!machine.isAllowedToWork()))
            || !(coverable instanceof TileEntity tileEntity)) {
            return;
        }

        final TileEntity toTile;
        final TileEntity fromTile;
        final int toSlot;
        final int fromSlot;

        if ((this.coverData & EXPORT_MASK) > 0) {
            fromTile = tileEntity;
            toTile = coverable.getTileEntityAtSide(coverSide);
            fromSlot = this.coverData & SLOT_ID_MASK;
            toSlot = (this.coverData >> 14) & SLOT_ID_MASK;
        } else {
            fromTile = coverable.getTileEntityAtSide(coverSide);
            toTile = tileEntity;
            fromSlot = (this.coverData >> 14) & SLOT_ID_MASK;
            toSlot = this.coverData & SLOT_ID_MASK;
        }

        if (fromSlot > 0 && toSlot > 0) {
            if (fromTile instanceof IInventory fromInventory && toTile instanceof IInventory toInventory)
                GTUtility.moveFromSlotToSlot(
                    fromInventory,
                    toInventory,
                    fromSlot - 1,
                    toSlot - 1,
                    null,
                    false,
                    (byte) 64,
                    (byte) 1,
                    (byte) 64,
                    (byte) 1);
        } else if (toSlot > 0) {
            final ForgeDirection toSide;
            if ((this.coverData & EXPORT_MASK) > 0) toSide = coverSide;
            else toSide = coverSide.getOpposite();
            GTUtility.moveOneItemStackIntoSlot(
                fromTile,
                toTile,
                toSide,
                toSlot - 1,
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1);
        } else if (fromSlot > 0) {
            final ForgeDirection toSide;
            if ((this.coverData & EXPORT_MASK) > 0) toSide = coverSide;
            else toSide = coverSide.getOpposite();
            if (fromTile instanceof IInventory fromInventory) GTUtility.moveFromSlotToSide(
                fromInventory,
                toTile,
                fromSlot - 1,
                toSide,
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1);
        } else {
            final ForgeDirection fromSide;
            final ForgeDirection toSide;
            if ((this.coverData & EXPORT_MASK) > 0) {
                fromSide = coverSide;
                toSide = coverSide.getOpposite();
            } else {
                fromSide = coverSide.getOpposite();
                toSide = coverSide;
            }
            GTUtility.moveOneItemStack(
                fromTile,
                toTile,
                fromSide,
                toSide,
                null,
                false,
                (byte) 64,
                (byte) 1,
                (byte) 64,
                (byte) 1);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = 0;
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            step += aPlayer.isSneaking() ? 256 : 16;
        } else {
            step -= aPlayer.isSneaking() ? 256 : 16;
        }
        int newCoverData = getNewVar(getVariable(), step);
        sendMessageToPlayer(aPlayer, newCoverData);
        coverData = newCoverData;
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        int tCoverVariable = getNewVar(getVariable(), step);
        sendMessageToPlayer(aPlayer, tCoverVariable);
        coverData = tCoverVariable;
        return true;
    }

    private void sendMessageToPlayer(EntityPlayer aPlayer, int var) {
        if ((var & EXPORT_MASK) != 0) GTUtility.sendChatToPlayer(
            aPlayer,
            translateToLocal("gt.interact.desc.out_slot") + (((var >> 14) & SLOT_ID_MASK) - 1));
        else GTUtility
            .sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.in_slot") + ((var & SLOT_ID_MASK) - 1));
    }

    private int getNewVar(int var, int step) {
        int intSlot = (var & SLOT_ID_MASK);
        int adjSlot = (var >> 14) & SLOT_ID_MASK;
        if ((var & EXPORT_MASK) == 0) {
            int x = (intSlot + step);
            if (x > SLOT_ID_MASK) return createVar(0, SLOT_ID_MASK, 0);
            else if (x < 1) return createVar(-step - intSlot + 1, 0, EXPORT_MASK);
            else return createVar(0, x, 0);
        } else {
            int x = (adjSlot - step);
            if (x > SLOT_ID_MASK) return createVar(SLOT_ID_MASK, 0, EXPORT_MASK);
            else if (x < 1) return createVar(0, step - adjSlot + 1, 0);
            else return createVar(x, 0, EXPORT_MASK);
        }
    }

    private int createVar(int adjSlot, int intSlot, int export) {
        return CONVERTED_BIT | export | ((adjSlot & SLOT_ID_MASK) << 14) | (intSlot & SLOT_ID_MASK);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

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
    public boolean letsFluidIn(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int slot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int slot) {
        return true;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return this.mTickRate;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ArmUIFactory(buildContext).createWindow();
    }

}
