package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_Container_BuckConverter;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_GUIContainer_BuckConverter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_POWER_TT;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;

public class GT_MetaTileEntity_BuckConverter extends GT_MetaTileEntity_TieredMachineBlock {
    private static GT_RenderedTexture BUCK,BUCK_ACTIVE;
    public int EUT=0,AMP=0;
    public boolean enabled = true;

    public GT_MetaTileEntity_BuckConverter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Power from nothing");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_BuckConverter(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BuckConverter(mName, mTier, mDescription, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        BUCK = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/BUCK"));
        BUCK_ACTIVE = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/BUCK_ACTIVE"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{MACHINE_CASINGS[mTier][aColorIndex + 1], aSide == aFacing ? (aActive?BUCK_ACTIVE:BUCK) :
                (aSide==GT_Utility.getOppositeSide(aFacing)?
                        OVERLAYS_ENERGY_IN_POWER_TT[mTier]:
                        (aActive ? OVERLAYS_ENERGY_OUT_POWER_TT[mTier] : OVERLAYS_ENERGY_IN_POWER_TT[mTier]))};
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_BuckConverter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BuckConverter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eEUT",EUT);
        aNBT.setInteger("eAMP",AMP);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        EUT=aNBT.getInteger("eEUT");
        AMP=aNBT.getInteger("eAMP");
        enabled = (long)AMP*EUT >=0;
        getBaseMetaTileEntity().setActive(enabled);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && (aTick & 0xF) == 0) {
            if(aBaseMetaTileEntity.isActive()) {
                aBaseMetaTileEntity.decreaseStoredEnergyUnits(CommonValues.V[mTier] >> 2, true);
            }
            aBaseMetaTileEntity.setActive(enabled);
        }
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
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL, mDescription,
                EnumChatFormatting.BLUE + "Adjustable step down transformer",
                EnumChatFormatting.BLUE + "Switching power supply..."
        };
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return enabled && aSide != getBaseMetaTileEntity().getFrontFacing() && aSide != getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long maxAmperesOut() {
        return enabled?Math.min(Math.abs(AMP),64):0;
    }

    @Override
    public long maxEUInput() {
        return CommonValues.V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return enabled?Math.min(Math.abs(EUT),maxEUInput()):0;
    }

    @Override
    public long maxEUStore() {
        return CommonValues.V[mTier]<<4;
    }

    @Override
    public long getMinimumStoredEU() {
        return CommonValues.V[mTier]<<2;
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }
}
