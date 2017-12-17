package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static gregtech.api.GregTech_API.sBlockCasings1;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_transformer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable{
    //region Structure
    private static final String[][] shape = new String[][]{
            {"   "," . ","   ",},
            {"   "," 0 ","   ",},
            {"   ","   ","   ",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasings1};
    private static final byte[] blockMeta = new byte[]{15};
    private static final String[] addingMethods = new String[]{"addEnergyIOToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Energy IO Hatches or High Power Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_transformer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
        eDismantleBoom=true;
    }

    public GT_MetaTileEntity_EM_transformer(String aName) {
        super(aName);
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
        eDismantleBoom=true;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_transformer(this.mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,1, 1, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity,true,false,false);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "EMDisplay.png",true,false,false);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][0], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][0]};
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                "Power substation",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "All the transformation!",
                EnumChatFormatting.BLUE + "Only 0.78125% power loss, HAYO!",
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack, boolean noParametrizers) {
        if (ePowerPass) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
        } else {
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
        }
        eAmpereFlow = 0;
        mEUt = 0;
        return ePowerPass;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((aTick & 31) == 31) {
            ePowerPass = aBaseMetaTileEntity.isAllowedToWork();
        }
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }
}
