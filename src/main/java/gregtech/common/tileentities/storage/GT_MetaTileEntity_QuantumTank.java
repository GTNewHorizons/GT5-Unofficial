package gregtech.common.tileentities.storage;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

public class GT_MetaTileEntity_QuantumTank
        extends GT_MetaTileEntity_BasicTank {
    public GT_MetaTileEntity_QuantumTank(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3, "Stores " + CommonSizeCompute(aTier) + "L of fluid");
    }

    public GT_MetaTileEntity_QuantumTank(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_QuantumTank(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return aSide == 1 ? new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_QTANK)} : new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public final byte getUpdateData() {
        return 0x00;
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public String[] getInfoData() {

        if (mFluid == null) {
            return new String[]{
                    EnumChatFormatting.BLUE + "Quantum Tank"+ EnumChatFormatting.RESET,
                    "Stored Fluid:",
                    EnumChatFormatting.GOLD + "No Fluid"+ EnumChatFormatting.RESET,
                    EnumChatFormatting.GREEN + Integer.toString(0) + " L"+ EnumChatFormatting.RESET+" "+
                    EnumChatFormatting.YELLOW + Integer.toString(getCapacity()) + " L"+ EnumChatFormatting.RESET
            };
        }
        return new String[]{
                EnumChatFormatting.BLUE + "Quantum Tank"+ EnumChatFormatting.RESET,
                "Stored Fluid:",
                EnumChatFormatting.GOLD + mFluid.getLocalizedName()+ EnumChatFormatting.RESET,
                EnumChatFormatting.GREEN + Integer.toString(mFluid.amount) + " L"+ EnumChatFormatting.RESET+" "+
                EnumChatFormatting.YELLOW+ Integer.toString(getCapacity()) + " L"+ EnumChatFormatting.RESET
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_QuantumTank(mName, mTier, mDescriptionArray, mTextures);
    }

    private static int CommonSizeCompute(int tier){
        switch(tier){
            case 6:
                return  128000000;
            case 7:
                return  256000000;
            case 8:
                return  512000000;
            case 9:
                return 1024000000;
            case 10:
                return 2147483640;
            default:
                return 0;
        }
    }

    @Override
    public int getCapacity() {
        return CommonSizeCompute(mTier);
    }

    @Override
    public int getTankPressure() {
        return 100;
    }

}
