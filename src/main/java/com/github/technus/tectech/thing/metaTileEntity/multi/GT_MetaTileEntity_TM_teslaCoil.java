package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static gregtech.api.GregTech_API.sBlockCasings2;
import static gregtech.api.GregTech_API.sBlockCasings5;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_TM_teslaCoil extends GT_MetaTileEntity_MultiblockBase_EM  implements IConstructable {
    private int powerSetting = 1000;
    private int timerSetting = 0;
    private int timerValue = 0;
    private boolean hasBeenPausedThiscycle=false;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {E,"00000",},
            {"A000","00000","A000","\u0003","A . ",},
            {"A000","00100","A010","B1","B1","B1","B1","A 1 ",},
            {"A000","00000","A000","\u0003","A   ",},
            {E,"00000",}
    };
    private static final Block[] blockType = new Block[]{sBlockCasings5,sBlockCasings2};
    private static final byte[] blockMeta = new byte[]{0,0};


    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final short[] casingTextures = new short[]{16};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasings5};
    private static final byte[] blockMetaFallback = new byte[]{0};
    //endregion

    public GT_MetaTileEntity_TM_teslaCoil(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_teslaCoil(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_teslaCoil(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 7, 1);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,2, 7, 1, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return new String[0];
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.bassMark,
                "High Frequency Oven",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "From live to done in seconds!",
                EnumChatFormatting.BLUE + "I said nuke the chinese, I meant teslaCoil supper!",
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        hasBeenPausedThiscycle=false;
        if(powerSetting<=300 || eParamsInStatus[0] == PARAM_TOO_HIGH || timerSetting<=0 || timerSetting>3000) return false;
        if (timerValue <= 0) {
            timerValue=timerSetting;
        }
        mEUt = -(powerSetting >> 1);
        eAmpereFlow = 1;
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {

    }


    @Override
    public void updateParameters_EM() {
        if (eParamsIn[0] <= 300)
            eParamsInStatus[0] = PARAM_TOO_LOW;
        else if (eParamsIn[0] < 1000)
            eParamsInStatus[0] = PARAM_LOW;
        else if (eParamsIn[0] == 1000)
            eParamsInStatus[0] = PARAM_OK;
        else if (eParamsIn[0] <= Integer.MAX_VALUE)
            eParamsInStatus[0] = PARAM_HIGH;
        else eParamsInStatus[0] = PARAM_TOO_HIGH;

        if (eParamsIn[10] <= 1)
            eParamsInStatus[10] = PARAM_TOO_LOW;
        else if (eParamsIn[10] <= 3000)
            eParamsInStatus[10] = PARAM_OK;
        else eParamsInStatus[10] = PARAM_TOO_HIGH;

        powerSetting = (int)eParamsIn[0];
        timerSetting = (int)eParamsIn[10];

        eParamsOut[0] = timerValue;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(eSafeVoid) hasBeenPausedThiscycle=true;
        return hasBeenPausedThiscycle || super.onRunningTick(aStack);//consume eu and other resources if not paused
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        timerValue=0;
    }

    @Override
    protected void workGotDisabled_EM() {
        timerValue=0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("eTimerVal", timerValue);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        timerValue = aNBT.getInteger("eTimerVal");
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        explodeMultiblock();
    }//Redirecting to explodemultiblock
}
