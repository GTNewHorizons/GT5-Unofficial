package gregtech.common.tileentities.machines.multi;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * An abstract hatch class that can emit directional redstone signals. Extend to add textures and data, and implement redstone logic on multi side.
 */
public abstract class MTEDetectorHatchBase extends MTEHatch {

    private final byte[] redstoneSignal={0,0,0,0,0,0};

    public MTEDetectorHatchBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEDetectorHatchBase(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    private static byte redstoneSignalFromOn(boolean on){
        return (byte) (on? 15:0);
    }

    public void setRedstoneSignal(int facing, byte signal){
        if(facing<0||facing>ForgeDirection.VALID_DIRECTIONS.length) return;
        redstoneSignal[facing]=signal;
    }

    public void setRedstoneSignal(int facing, boolean on){
        setRedstoneSignal(facing,redstoneSignalFromOn(on));
    }

    public void setAllFacesRedstoneSignal(byte signal){
        for(int i=0;i<ForgeDirection.VALID_DIRECTIONS.length;i++) {
            setRedstoneSignal(i,signal);
        }
    }

    public void setAllFacesRedstoneSignal(boolean on){
        for(int i=0;i<ForgeDirection.VALID_DIRECTIONS.length;i++) {
            setRedstoneSignal(i,on);
        }
    }

    public void setFacingSideRedstoneSignal(byte signal){
        setRedstoneSignal(getBaseMetaTileEntity().getFrontFacing().ordinal(),signal);
    }

    public void setFacingSideRedstoneSignal(boolean on){
        setRedstoneSignal(getBaseMetaTileEntity().getFrontFacing().ordinal(),on);
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
    public void loadNBTData(NBTTagCompound aNBT){
        for(int i=0;i<ForgeDirection.VALID_DIRECTIONS.length;i++){
            redstoneSignal[i]= aNBT.getByte("signal"+i);
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT){
        for(int i=0;i<ForgeDirection.VALID_DIRECTIONS.length;i++){
            aNBT.setByte("signal"+i,redstoneSignal[i]);
        }
    }
}
