package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT;
import static com.github.technus.tectech.util.CommonValues.V;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.util.TT_Utility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DataReader extends GT_MetaTileEntity_BasicMachine {

    public static GT_RenderedTexture READER_ONLINE, READER_OFFLINE;

    public GT_MetaTileEntity_DataReader(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "", 1, 1, "dataReader.png", "");
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_DataReader(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, "dataReader.png", "");
        TT_Utility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DataReader(mName, mTier, mDescription, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        READER_ONLINE = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/READER_ONLINE"));
        READER_OFFLINE = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/READER_OFFLINE"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aBaseMetaTileEntity.getWorld() == null) {
            if (aSide == aFacing) {
                return new ITexture[] { MACHINE_CASINGS_TT[mTier][aColorIndex + 1],
                        aActive ? READER_ONLINE : READER_OFFLINE };
            }
            return new ITexture[] { MACHINE_CASINGS_TT[mTier][aColorIndex + 1] };
        }
        if (aSide == mMainFacing) {
            return new ITexture[] { MACHINE_CASINGS_TT[mTier][aColorIndex + 1],
                    aActive ? READER_ONLINE : READER_OFFLINE };
        } else if (aSide == aFacing) {
            return new ITexture[] { MACHINE_CASINGS_TT[mTier][aColorIndex + 1],
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
        }
        return new ITexture[] { MACHINE_CASINGS_TT[mTier][aColorIndex + 1] };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public int checkRecipe() {
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        aBaseMetaTileEntity.setActive(getOutputAt(0) != null || mMaxProgresstime > 0);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.DARK_RED + "Deprecated" };
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return maxEUInput() * 16L;
    }

    @Override
    public long getMinimumStoredEU() {
        return maxEUInput() * 4L;
    }
}
