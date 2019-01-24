package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_Container_DebugStructureWriter;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_GUIContainer_DebugStructureWriter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureWriter;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DebugStructureWriter extends GT_MetaTileEntity_TieredMachineBlock {
    private static GT_RenderedTexture MARK;
    public short[] numbers = new short[6];
    public boolean size = false;
    public String[] result = new String[]{"Undefined"};

    public GT_MetaTileEntity_DebugStructureWriter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Scans Blocks Around");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_DebugStructureWriter(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DebugStructureWriter(mName, mTier, mDescription, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        MARK = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/MARK"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], aSide != aFacing ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE) : MARK};
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DebugStructureWriter(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DebugStructureWriter(aPlayerInventory, aBaseMetaTileEntity);
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
        for (int i = 0; i < numbers.length; i++) {
            aNBT.setShort("eData" + i, numbers[i]);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = aNBT.getShort("eData" + i);
        }
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isAllowedToWork()) {
            result = StructureWriter(getBaseMetaTileEntity(), numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5], false);
            for (String s : result) {
                TecTech.LOGGER.info(s);
            }
            aBaseMetaTileEntity.disableWorking();
        }
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        result = StructureWriter(getBaseMetaTileEntity(), numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5], true);
        for (String s : result) {
            TecTech.LOGGER.info(s);
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        //if (TecTechConfig.DEBUG_MODE && aPlayer.getHeldItem() != null)
        //    TecTech.LOGGER.info("UnlocalizedName: " + getUniqueIdentifier(aPlayer.getHeldItem()));
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL, mDescription,
                EnumChatFormatting.BLUE + "Prints Multiblock NonTE structure check code",
                EnumChatFormatting.BLUE + "ABC axises aligned to machine front"
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return result;
    }
}
