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

    private boolean export = false;
    private int internalSlotId = 1;
    private int externalSlotId = 1;
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

        if (export) {
            fromTile = tileEntity;
            toTile = coverable.getTileEntityAtSide(coverSide);
            fromSlot = internalSlotId;
            toSlot = externalSlotId;
        } else {
            fromTile = coverable.getTileEntityAtSide(coverSide);
            toTile = tileEntity;
            fromSlot = externalSlotId;
            toSlot = internalSlotId;
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
            final ForgeDirection toSide = export ? coverSide : coverSide.getOpposite();
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
            final ForgeDirection toSide = export ? coverSide : coverSide.getOpposite();
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
            final ForgeDirection fromSide = export ? coverSide : coverSide.getOpposite();
            final ForgeDirection toSide = fromSide.getOpposite();
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
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            internalSlotId += aPlayer.isSneaking() ? 16 : 1;
        } else {
            internalSlotId = Math.max(0, internalSlotId - (aPlayer.isSneaking() ? 16 : 1));
        }
        sendMessageToPlayer(aPlayer);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        internalSlotId = Math.max(0, internalSlotId + step);
        sendMessageToPlayer(aPlayer);
        return true;
    }

    private void sendMessageToPlayer(EntityPlayer aPlayer) {
        if (export) GTUtility.sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.out_slot") + externalSlotId);
        else GTUtility.sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.in_slot") + internalSlotId);
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
