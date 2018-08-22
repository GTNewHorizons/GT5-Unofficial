package gregtech.api.metatileentity.implementations;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class GT_MetaTileEntity_Hatch extends GT_MetaTileEntity_BasicTank {
    @Deprecated //Use texture changing method
    public byte mMachineBlock = 0;
    private byte mTexturePage = 0;
    private byte actualTexture = 0;

    public GT_MetaTileEntity_Hatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch(String aName, int aTier, int aInvSlotCount, String []aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return aTier < 1 ? 1 : aTier == 1 ? 4 : aTier == 2 ? 9 : 16;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

    public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        int textureIndex=actualTexture|(mTexturePage<<7);//Shift seven since one page is 128 textures!
        int texturePointer=(byte)(actualTexture&0x7F);//just to be sure, from my testing the 8th bit cannot be set clientside
        return aSide != aFacing ?
                textureIndex > 0 ?
                        new ITexture[]{Textures.BlockIcons.casingTexturePages[mTexturePage][texturePointer]} :
                        new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]} :
                textureIndex > 0 ?
                        aActive ?
                                getTexturesActive(Textures.BlockIcons.casingTexturePages[mTexturePage][texturePointer]) :
                                getTexturesInactive(Textures.BlockIcons.casingTexturePages[mTexturePage][texturePointer]) :
                        aActive ?
                                getTexturesActive(Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]) :
                                getTexturesInactive(Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mMachineBlock", actualTexture);
        aNBT.setByte("mTexturePage", mTexturePage);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        actualTexture=aNBT.getByte("mMachineBlock");
        mTexturePage=aNBT.getByte("mTexturePage");

        if(mTexturePage!=0 && GT_Values.GT.isServerSide())
            actualTexture|=0x80;//<- lets just hope no one needs the correct value for that on server
        mMachineBlock=actualTexture;
    }

    /**
     * 
     * @param textureIndex
     * Index between 0-127.
     * Add 128 per page, if texture index is not on first page.
     * 
     */
    
    public final void updateTexture(int textureIndex){
        onValueUpdate((byte) textureIndex);
        onTexturePageUpdate((byte) (textureIndex>>7));
    }

    public final void updateTexture(byte texturePage, byte machineBlock){
        onValueUpdate(machineBlock);
        onTexturePageUpdate(texturePage);
    }

    @Override
    public final void onValueUpdate(byte aValue) {
        actualTexture=(byte)(aValue & 0x7F);
        mMachineBlock=actualTexture;
        mTexturePage=0;
    }

    @Override
    public final byte getUpdateData() {
        return (byte)(actualTexture & 0x7F);
    }

    public final void onTexturePageUpdate(byte aValue) {
        mTexturePage = (byte)(aValue & 0x7F);
        if(mTexturePage!=0 && getBaseMetaTileEntity().isServerSide()) {//just to be sure
            mMachineBlock|=0x80;//<- lets just hope no one needs the correct value for that on server
            actualTexture=mMachineBlock;
        }
        //set last bit to allow working of the page reset-er to 0 in rare case when texture id is the same but page changes to 0
    }

    public final byte getTexturePage() {
        return (byte)(mTexturePage & 0x7F);
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean displaysItemStack() {
        return false;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {//in that method since it is usually not overriden, especially for hatches.
        if(actualTexture!=mMachineBlock){//revert to page 0 on edition of the field - old code way
            actualTexture=(byte)(mMachineBlock & 0x7F);
            mMachineBlock=actualTexture;//clear last bit in mMachineBlock since now we are at page 0 after the direct field change
            mTexturePage=0;//assuming old code only supports page 0
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    //To change to other page -> use the setter method -> updateTexture
}
