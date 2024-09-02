package gtPlusPlus.xmod.gregtech.common.tileentities.redstone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.objects.GTItemStack;
import gtPlusPlus.core.lib.GTPPCore;

public abstract class MTERedstoneBase extends MTETieredMachineBlock {

    protected int mOpenerCount;

    public MTERedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTERedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTERedstoneBase(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public final boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aStack) {
        return side != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public final boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public abstract void saveNBTData(NBTTagCompound aNBT);

    @Override
    public abstract void loadNBTData(NBTTagCompound aNBT);

    @Override
    public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public final void onOpenGUI() {
        super.onOpenGUI();
        mOpenerCount++;
    }

    @Override
    public final void onCloseGUI() {
        super.onCloseGUI();
        mOpenerCount--;
    }

    public boolean hasRedstoneSignal() {
        if (getBaseMetaTileEntity().getStrongestRedstone() > 0) {
            return true;
        }
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (getBaseMetaTileEntity().getOutputRedstoneSignal(side) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, GTPPCore.GT_Tooltip.get());
    }
}
