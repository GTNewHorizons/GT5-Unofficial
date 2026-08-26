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

    private boolean directional = true;
    protected boolean inverted = false;

    public MTEHatchRedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatchRedstoneBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatchRedstoneBase(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public boolean supportsInvertedSignal() {
        return true;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem) {
        return false;
    }

    /**
     * This method changes the redstone signal of the hatch. If directional is true, it only affects the side it's
     * facing while turning all other faces off, but if directional is false, it affects all faces.
     *
     * @param on Whether to turn the hatch on/off
     */
    public void setRedstoneSignal(boolean on) {
        if (supportsInvertedSignal()) {
            on = on ^ inverted;
        }
        byte signal = (byte) (on ? 15 : 0);
        setRedstoneSignal(signal);
    }

    public void setRedstoneSignal(byte signal) {
        if (this.getBaseMetaTileEntity() == null) return;
        ForgeDirection facingSide = getBaseMetaTileEntity().getFrontFacing();
        for (ForgeDirection forgeDirection : ForgeDirection.VALID_DIRECTIONS) {
            getBaseMetaTileEntity().setStrongOutputRedstoneSignal(
                forgeDirection,
                directional ? (forgeDirection == facingSide ? signal : 0) : signal);
        }
    }

    @Override
    public void onFacingChange() {
        if (getBaseMetaTileEntity() == null) return;
        ForgeDirection facingSide = getBaseMetaTileEntity().getFrontFacing();
        byte signal = getBaseMetaTileEntity().getStrongOutputRedstoneSignal(facingSide);
        setRedstoneSignal(signal);
    }

    @Override
    protected boolean useMui2() {
        return true;
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

    public boolean isDirectional() {
        return directional;
    }

    public void setDirectional(boolean directional) {
        this.directional = directional;
        if (getBaseMetaTileEntity() == null) return;
        ForgeDirection facingSide = getBaseMetaTileEntity().getFrontFacing();
        byte signal = getBaseMetaTileEntity().getStrongOutputRedstoneSignal(facingSide);
        setRedstoneSignal(signal);
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        directional = aNBT.getBoolean("directional");
        if (supportsInvertedSignal()) {
            inverted = aNBT.getBoolean("inverted");
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("directional", directional);
        if (supportsInvertedSignal()) {
            aNBT.setBoolean("inverted", inverted);
        }
    }
}
