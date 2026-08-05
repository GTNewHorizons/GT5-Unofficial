package gregtech.common.tileentities.machines.multi;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;

/**
 * An abstract hatch class that can emit directional redstone signals. Extend to add textures/description/data.
 */
public abstract class MTEHatchRedstoneBase extends MTEHatch {

    private final byte[] redstoneSignal = { 0, 0, 0, 0, 0, 0 };

    public MTEHatchRedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatchRedstoneBase(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    protected static byte redstoneSignalFromOn(boolean on) {
        return (byte) (on ? 15 : 0);
    }

    /**
     * This should be the only method that directly changes redstoneSignal, override for invert logic etc.
     */
    public void setRedstoneSignalOnFace(int facing, byte signal, boolean turnOtherFacesOff) {
        if (facing < 0 || facing > ForgeDirection.VALID_DIRECTIONS.length) return;
        if (!turnOtherFacesOff) {
            redstoneSignal[facing] = signal;
            return;
        }
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            redstoneSignal[i] = (i == facing) ? signal : 0;
        }
    }

    public void setRedstoneSignalOnFace(int facing, boolean on, boolean turnOtherFacesOff) {
        setRedstoneSignalOnFace(facing, redstoneSignalFromOn(on), turnOtherFacesOff);
    }

    public void setAllFacesRedstoneSignal(byte signal) {
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            setRedstoneSignalOnFace(i, signal, false);
        }
    }

    public void setAllFacesRedstoneSignal(boolean on) {
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            setRedstoneSignalOnFace(i, on, false);
        }
    }

    public void setFacingSideRedstoneSignal(byte signal, boolean turnOtherFacesOff) {
        if (this.getBaseMetaTileEntity() == null) return;
        this.setRedstoneSignalOnFace(
            getBaseMetaTileEntity().getFrontFacing()
                .ordinal(),
            signal,
            turnOtherFacesOff);
    }

    public void setFacingSideRedstoneSignal(boolean on, boolean turnOtherFacesOff) {
        setFacingSideRedstoneSignal(redstoneSignalFromOn(on), turnOtherFacesOff);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, redstoneSignal[side.ordinal()]);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection Side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            redstoneSignal[i] = aNBT.getByte("signal" + i);
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            aNBT.setByte("signal" + i, redstoneSignal[i]);
        }
    }
}
