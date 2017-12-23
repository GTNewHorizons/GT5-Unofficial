package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.Vec3pos;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.CommonValues.*;
import static com.github.technus.tectech.Util.*;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;

/**
 * Created by danie_000 on 27.10.2016.
 */
public abstract class GT_MetaTileEntity_MultiblockBase_EM extends GT_MetaTileEntity_MultiBlockBase {
    protected final static Map<String, Method> adderMethodMap = new HashMap<>();
    private static Method adderMethod;

    protected cElementalInstanceStackMap[] outputEM;

    public final static ItemStack[] nothingI = new ItemStack[0];
    public final static FluidStack[] nothingF = new FluidStack[0];

    protected static Textures.BlockIcons.CustomIcon ScreenOFF;
    protected static Textures.BlockIcons.CustomIcon ScreenON;

    protected ArrayList<GT_MetaTileEntity_Hatch_InputElemental> eInputHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_OutputElemental> eOutputHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_MufflerElemental> eMufflerHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_Param> eParamHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_Uncertainty> eUncertainHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> eEnergyMulti = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> eDynamoMulti = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_InputData> eInputData = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_OutputData> eOutputData = new ArrayList<>();

    // 0 and 10 are from first parametrizer
    // 1 and 11 are from second etc...

    private final int[] iParamsIn = new int[20];//number I from parametrizers
    private final int[] iParamsOut = new int[20];//number O to parametrizers
    private final boolean[] bParamsAreFloats = new boolean[10];

    final byte[] eParamsInStatus = new byte[20];//LED status for I
    final byte[] eParamsOutStatus = new byte[20];//LED status for O
    public final static byte STATUS_UNUSED = 0,
            STATUS_TOO_LOW = 1,  STATUS_LOW = 2,
            STATUS_WRONG = 3,    STATUS_OK = 4,
            STATUS_TOO_HIGH = 5, STATUS_HIGH = 6;
    // 0,2,4,6 - ok
    //  1,3,5  - nok

    //TO ENABLE this change value in <init> to false and/or other than 0, can also be added in recipe check or whatever
    protected boolean eParameters = true, ePowerPass = false, eSafeVoid = false, eDismantleBoom = false;
    protected byte eCertainMode = 0, eCertainStatus = 0, minRepairStatus = 3;

    protected long eMaxAmpereFlow = 0;//don't modify! unless YOU REALLY HAVE TO
    private long maxEUinputMin = 0, maxEUinputMax = 0;

    public long eAmpereFlow = 1; // analogue of EU/t but for amperes used (so eu/t is actually eu*A/t) USE ONLY POSITIVE NUMBERS!

    protected long eRequiredData = 0; // data required to operate
    protected long eAvailableData = 0; // data being available

    private boolean explodedThisTick=false;

    //init param states in constructor, or implement it in checkrecipe/outputfunction

    //METHODS TO OVERRIDE

    //if you want to add checks that run periodically when machine works then make onRunningTick better
    //if you want to add checks that run periodically when machine is built then use check params

    public boolean checkRecipe_EM(ItemStack itemStack) {
        return false;
    }
    // My code handles AMPS, if you want overclocking just modify mEUt and mMaxProgressTime, leave amps as usual!
    // Set mEUt, Efficiencies, required computation, time, check input etc.

    protected void parametersInRead_EM(){

    }

    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
    }
    // update status of parameters in guis and "machine state"
    // Called before check recipe, before outputting, and every second the machine is active

    public void outputAfterRecipe_EM() {
    }
    // based on "machine state" do output,
    // this must move to outputEM to EM output hatches
    // and can also modify output items/fluids/EM, remaining EM is NOT overflowed.
    // (Well it can be overflowed if machine didn't finished, soft-hammered/disabled/not enough EU)
    // Setting available data processing

    protected void hatchInit_EM(boolean mMachine) {
    }//For extra types of hatches initiation, LOOK HOW IT IS CALLED! onPostTick

    protected void extraExplosions_EM() {
    }//For that extra hatches explosions, and maybe some MOORE EXPLOSIONS

    //machine structure check
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return false;
    }

    //Get Available data, Override only on data producers should return mAvailableData that is set in check recipe
    protected long getAvailableData_EM() {
        long result = 0;
        final Vec3pos pos = new Vec3pos(getBaseMetaTileEntity());
        for (GT_MetaTileEntity_Hatch_InputData in : eInputData)
            if (in.q != null) result += in.q.computationIfNotContained(pos);
        return result;
    }

    //Extra hook on cyclic updates (not really needed for machines smaller than 1 chunk)
    //BUT NEEDED WHEN - machine blocks are not touching each other ot they don't implement IMachineBlockUpdateable (ex. air)
    protected boolean cyclicUpdate_EM() {
        return mUpdate <= -1000;//set to false to disable cyclic update
        //default is once per 50s; mUpdate is decremented every tick
    }

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    public int getPollutionPerTick_EM(ItemStack itemStack) {
        return 0;
    }

    //helper method so i don't have to set that params to nothing at all times
    protected void afterRecipeCheckFailed(){
        if (outputEM != null) {
            float mass = 0;
            for (cElementalInstanceStackMap tree : outputEM)
                mass += tree.getMass();
            if (mass > 0) {
                if (eMufflerHatches.size() < 1) explodeMultiblock();
                else {
                    mass /= eMufflerHatches.size();
                    for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches)
                        if (dump.addOverflowMatter(mass)) explodeMultiblock();
                }
            }
            outputEM = null;
        }

        for (GT_MetaTileEntity_Hatch_OutputData data : eOutputData)
            data.q = null;

        mOutputItems = null;
        mOutputFluids = null;
        mEfficiency = 0;
        mEfficiencyIncrease = 0;
        mProgresstime = 0;
        //mMaxProgresstime = 0; //Done after this - cos it is VITAL!
        eAvailableData = 0;
        //getBaseMetaTileEntity().disableWorking();
        //hatchesStatusUpdate_EM(); //called always after recipe checks
    }

    //triggered if machine is not allowed to work after completing a recipe, override to make it not shutdown for instance (like turbines).
    protected void notAllowedToWork_stopMachine_EM(){
        stopMachine();
    }

    //Override if needed but usually call super method at start!
    //On machine stop - NOT called directly when softhammered to offline state! - it SHOULD cause a full stop like power failure does
    @Override
    public void stopMachine() {
        if (outputEM != null) {
            float mass = 0;
            for (cElementalInstanceStackMap tree : outputEM)
                mass += tree.getMass();
            if (mass > 0) {
                if (eMufflerHatches.size() < 1) explodeMultiblock();
                else {
                    mass /= eMufflerHatches.size();
                    for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches)
                        if (dump.addOverflowMatter(mass)) explodeMultiblock();
                }
            }
            outputEM = null;
        }

        for (GT_MetaTileEntity_Hatch_OutputData data : eOutputData)
            data.q = null;

        mOutputItems = null;
        mOutputFluids = null;
        mEfficiency = 0;
        mEfficiencyIncrease = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        eAvailableData = 0;
        hatchesStatusUpdate_EM();
        getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                "Nothing special just override me."
        };
    }

    //RATHER LEAVE ALONE Section
    protected boolean areChunksAroundLoaded_EM(){
        if(isValidMetaTileEntity(this) && getBaseMetaTileEntity().isServerSide()){
            IGregTechTileEntity base=getBaseMetaTileEntity();
            return base.getWorld().doChunksNearChunkExist(base.getXCoord(),base.getYCoord(),base.getZCoord(),3);
            //todo check if it is actually checking if chunks are loaded
        }else return false;
    }

    public GT_MetaTileEntity_MultiblockBase_EM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        parametersLoadDefault_EM();
    }

    public GT_MetaTileEntity_MultiblockBase_EM(String aName) {
        super(aName);
        parametersLoadDefault_EM();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "EMDisplay.png");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4]};
    }

    @Override
    public byte getTileEntityBaseType() {
        return 3;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setLong("eMaxEUmin", maxEUinputMin);
        aNBT.setLong("eMaxEUmax", maxEUinputMax);
        aNBT.setLong("eRating", eAmpereFlow);
        aNBT.setLong("eMaxA", eMaxAmpereFlow);
        aNBT.setLong("eDataR", eRequiredData);
        aNBT.setLong("eDataA", eAvailableData);
        aNBT.setByte("eCertainM", eCertainMode);
        aNBT.setByte("eCertainS", eCertainStatus);
        aNBT.setByte("eMinRepair", minRepairStatus);
        aNBT.setBoolean("eParam", eParameters);
        aNBT.setBoolean("ePass", ePowerPass);
        aNBT.setBoolean("eVoid", eSafeVoid);
        aNBT.setBoolean("eBoom", eDismantleBoom);
        aNBT.setBoolean("eOK", mMachine);

        //Ensures compatibility
        if (mOutputItems != null) {
            aNBT.setInteger("mOutputItemsLength", mOutputItems.length);
            for (int i = 0; i < mOutputItems.length; i++)
                if (mOutputItems[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    mOutputItems[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputItem" + i, tNBT);
                }
        }

        //Ensures compatibility
        if (mOutputFluids != null) {
            aNBT.setInteger("mOutputFluidsLength", mOutputFluids.length);
            for (int i = 0; i < mOutputFluids.length; i++)
                if (mOutputFluids[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    mOutputFluids[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputFluids" + i, tNBT);
                }
        }

        if (outputEM != null) {
            aNBT.setInteger("outputStackCount", outputEM.length);
            NBTTagCompound output = new NBTTagCompound();
            for (int i = 0; i < outputEM.length; i++)
                output.setTag(Integer.toString(i), outputEM[i].toNBT());
            aNBT.setTag("outputEM", output);
        } else {
            aNBT.setInteger("outputStackCount", 0);
            aNBT.removeTag("outputEM");
        }

        NBTTagCompound paramI = new NBTTagCompound();
        for (int i = 0; i < iParamsIn.length; i++)
            paramI.setInteger(Integer.toString(i), iParamsIn[i]);
        aNBT.setTag("eParamsIn", paramI);

        NBTTagCompound paramO = new NBTTagCompound();
        for (int i = 0; i < iParamsOut.length; i++)
            paramO.setInteger(Integer.toString(i), iParamsOut[i]);
        aNBT.setTag("eParamsOut", paramO);

        NBTTagCompound paramB = new NBTTagCompound();
        for (int i = 0; i < bParamsAreFloats.length; i++)
            paramB.setBoolean(Integer.toString(i), bParamsAreFloats[i]);
        aNBT.setTag("eParamsB", paramB);

        NBTTagCompound paramIs = new NBTTagCompound();
        for (int i = 0; i < eParamsInStatus.length; i++)
            paramIs.setByte(Integer.toString(i), eParamsInStatus[i]);
        aNBT.setTag("eParamsInS", paramIs);

        NBTTagCompound paramOs = new NBTTagCompound();
        for (int i = 0; i < eParamsOutStatus.length; i++)
            paramOs.setByte(Integer.toString(i), eParamsOutStatus[i]);
        aNBT.setTag("eParamsOutS", paramOs);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        maxEUinputMin = aNBT.getLong("eMaxEUmin");
        maxEUinputMax = aNBT.getLong("eMaxEUmax");
        eAmpereFlow = aNBT.getLong("eRating");
        eMaxAmpereFlow = aNBT.getLong("eMaxA");
        eRequiredData = aNBT.getLong("eDataR");
        eAvailableData = aNBT.getLong("eDataA");
        eCertainMode = aNBT.getByte("eCertainM");
        eCertainStatus = aNBT.getByte("eCertainS");
        minRepairStatus = aNBT.getByte("eMinRepair");
        eParameters = aNBT.getBoolean("eParam");
        ePowerPass = aNBT.getBoolean("ePass");
        eSafeVoid = aNBT.getBoolean("eVoid");
        eDismantleBoom = aNBT.getBoolean("eBoom");
        mMachine = aNBT.getBoolean("eOK");

        //Ensures compatibility
        int aOutputItemsLength = aNBT.getInteger("mOutputItemsLength");
        if (aOutputItemsLength > 0) {
            mOutputItems = new ItemStack[aOutputItemsLength];
            for (int i = 0; i < mOutputItems.length; i++)
                mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
        }

        //Ensures compatibility
        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++)
                mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
        }

        final int outputLen = aNBT.getInteger("outputStackCount");
        if (outputLen > 0) {
            outputEM = new cElementalInstanceStackMap[outputLen];
            for (int i = 0; i < outputEM.length; i++)
                try {
                    outputEM[i] = cElementalInstanceStackMap.fromNBT(
                            aNBT.getCompoundTag("outputEM").getCompoundTag(Integer.toString(i)));
                } catch (tElementalException e) {
                    if (DEBUG_MODE) e.printStackTrace();
                    outputEM[i] = new cElementalInstanceStackMap();
                }
        } else outputEM = new cElementalInstanceStackMap[0];

        NBTTagCompound paramI = aNBT.getCompoundTag("eParamsIn");
        for (int i = 0; i < iParamsIn.length; i++)
            iParamsIn[i] = paramI.getInteger(Integer.toString(i));

        NBTTagCompound paramO = aNBT.getCompoundTag("eParamsOut");
        for (int i = 0; i < iParamsOut.length; i++)
            iParamsOut[i] = paramO.getInteger(Integer.toString(i));

        NBTTagCompound paramB = aNBT.getCompoundTag("eParamsB");
        for (int i = 0; i < bParamsAreFloats.length; i++)
            bParamsAreFloats[i] = paramB.getBoolean(Integer.toString(i));

        NBTTagCompound paramIs = aNBT.getCompoundTag("eParamsInS");
        for (int i = 0; i < eParamsInStatus.length; i++)
            eParamsInStatus[i] = paramIs.getByte(Integer.toString(i));

        NBTTagCompound paramOs = aNBT.getCompoundTag("eParamsOutS");
        for (int i = 0; i < eParamsOutStatus.length; i++)
            eParamsOutStatus[i] = paramOs.getByte(Integer.toString(i));
    }

    private boolean cyclicUpdate() {
        if (cyclicUpdate_EM()) {
            mUpdate = 0;
            return true;
        }
        return false;
    }

    //Param methods
    //called in creator
    protected void parametersLoadDefault_EM(){
        //load default parameters with setParameterPairIn_ClearOut
    }

    public final boolean setParameterPairIn_ClearOut(int hatchNo, boolean usesFloats, double value0, double value1) {
        if (mMaxProgresstime > 0) return false;
        bParamsAreFloats[hatchNo] = usesFloats;
        if (usesFloats) {
            iParamsIn[hatchNo] = Float.floatToIntBits((float) value0);
            iParamsIn[hatchNo + 10] = Float.floatToIntBits((float) value1);
        } else {
            iParamsIn[hatchNo] = (int) value0;
            iParamsIn[hatchNo + 10] = (int) value1;
        }
        iParamsOut[hatchNo] = 0;
        iParamsOut[hatchNo + 10] = 0;
        return true;
    }

    public final boolean isParametrizerUsingFloat(int hatchNo){
        return bParamsAreFloats[hatchNo];
    }

    public final double getParameterIn(int hatchNo, int paramID){
        return bParamsAreFloats[hatchNo]?Float.intBitsToFloat(iParamsIn[hatchNo+10*paramID]):iParamsIn[hatchNo+10*paramID];
    }

    public final int getParameterInInt(int hatchNo, int paramID){
        return iParamsIn[hatchNo+10*paramID];
    }

    public final float getParameterInFloat(int hatchNo, int paramID){
        return Float.intBitsToFloat(iParamsIn[hatchNo+10*paramID]);
    }

    public final void setParameterOut(int hatchNo, int paramID, double value){
        if(bParamsAreFloats[hatchNo]) {
            iParamsOut[hatchNo+10*paramID]=Float.floatToIntBits((float) value);
        }else{
            iParamsOut[hatchNo+10*paramID]=(int)value;
        }
    }

    public final boolean setParameterOutInt(int hatchNo, int paramID, int value){
        if(bParamsAreFloats[hatchNo]) return false;
        iParamsOut[hatchNo+10*paramID]=value;
        return true;
    }

    public final boolean setParameterOutFloat(int hatchNo, int paramID, float value){
        if(bParamsAreFloats[hatchNo]) {
            iParamsOut[hatchNo + 10 * paramID] = Float.floatToIntBits(value);
            return true;
        }
        return false;
    }

    public final byte getStatusOfParameterIn(int hatchNo, int paramID){
        return eParamsInStatus[hatchNo+10*paramID];
    }

    public final void setStatusOfParameterIn(int hatchNo, int paramID, byte status){
        eParamsInStatus[hatchNo+10*paramID]=status;
    }

    public final byte getStatusOfParameterOut(int hatchNo, int paramID){
        return eParamsOutStatus[hatchNo+10*paramID];
    }

    public final void setStatusOfParameterOut(int hatchNo, int paramID, byte status){
        eParamsOutStatus[hatchNo+10*paramID]=status;
    }

    @Override
    public final boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return checkMachine_EM(iGregTechTileEntity, itemStack);
    }

    //can be used to check structures of multi-blocks larger than one chunk, but...
    //ALL THE HATCHES AND THE CONTROLLER SHOULD BE IN ONE CHUNK OR IN LOADED CHUNKS
    @Deprecated
    public final boolean structureCheck_EM(
            String[][] structure,//0-9 casing, +- air no air, a-z ignore
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset) {
        return StructureChecker(structure, blockType, blockMeta,
                horizontalOffset, verticalOffset, depthOffset, getBaseMetaTileEntity(), !mMachine);
    }

    public final boolean structureCheck_EM(
            String[][] structure,//0-9 casing, +- air no air, a-z ignore
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            String[] addingMethods,
            short[] casingTextures,
            Block[] blockTypeFallback,//use numbers 0-9 for casing types
            byte[] blockMetaFallback,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset) {
        return StructureCheckerAdvanced(structure, blockType, blockMeta,
                adderMethod, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback,
                horizontalOffset, verticalOffset, depthOffset, getBaseMetaTileEntity(), !mMachine);
    }

    //CAREFUL!!! it calls most of the callbacks
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            explodedThisTick=false;
            if (mEfficiency < 0) mEfficiency = 0;

            if (--mUpdate == 0 || --mStartUpCheck == 0 || cyclicUpdate() || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                mInputHatches.clear();
                mInputBusses.clear();
                mOutputHatches.clear();
                mOutputBusses.clear();
                mDynamoHatches.clear();
                mEnergyHatches.clear();
                mMufflerHatches.clear();
                mMaintenanceHatches.clear();

                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches)
                    if (isValidMetaTileEntity(hatch_elemental)) hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches)
                    if (isValidMetaTileEntity(hatch_elemental)) hatch_elemental.id = -1;

                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eOutputData)
                    if (isValidMetaTileEntity(hatch_data)) hatch_data.id = -1;
                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eInputData)
                    if (isValidMetaTileEntity(hatch_data)) hatch_data.id = -1;

                for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                    if (isValidMetaTileEntity(hatch)) hatch.getBaseMetaTileEntity().setActive(false);
                for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                    if (isValidMetaTileEntity(hatch)) hatch.getBaseMetaTileEntity().setActive(false);

                eUncertainHatches.clear();
                eEnergyMulti.clear();
                eInputHatches.clear();
                eOutputHatches.clear();
                eParamHatches.clear();
                eMufflerHatches.clear();
                eDynamoMulti.clear();
                eOutputData.clear();
                eInputData.clear();

                if ((getBaseMetaTileEntity() instanceof BaseTileEntity))
                    ((BaseTileEntity) getBaseMetaTileEntity()).ignoreUnloadedChunks = mMachine;
                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);

                if (!mMachine) {
                    if ((ePowerPass && getEUVar() > V[3]) || (eDismantleBoom && mMaxProgresstime > 0 && areChunksAroundLoaded_EM()))
                        explodeMultiblock();
                    if (outputEM != null)
                        for (cElementalInstanceStackMap tree : outputEM)
                            if (tree.hasStacks()) explodeMultiblock();
                }

                if (eUncertainHatches.size() > 1) mMachine = false;

                if (mMachine) {
                    short id = 1;
                    for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches)
                        if (isValidMetaTileEntity(hatch_elemental)) hatch_elemental.id = id++;
                    id = 1;
                    for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches)
                        if (isValidMetaTileEntity(hatch_elemental)) hatch_elemental.id = id++;

                    id = 1;
                    for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eOutputData)
                        if (isValidMetaTileEntity(hatch_data)) hatch_data.id = id++;
                    id = 1;
                    for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eInputData)
                        if (isValidMetaTileEntity(hatch_data)) hatch_data.id = id++;

                    if (mEnergyHatches.size() > 0 || eEnergyMulti.size() > 0) {
                        maxEUinputMin = V[15];
                        maxEUinputMax = V[0];
                        for (GT_MetaTileEntity_Hatch_Energy hatch : mEnergyHatches)
                            if (isValidMetaTileEntity(hatch)) {
                                if (hatch.maxEUInput() < maxEUinputMin) maxEUinputMin = hatch.maxEUInput();
                                if (hatch.maxEUInput() > maxEUinputMax) maxEUinputMax = hatch.maxEUInput();
                            }
                        for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : eEnergyMulti)
                            if (isValidMetaTileEntity(hatch)) {
                                if (hatch.maxEUInput() < maxEUinputMin) maxEUinputMin = hatch.maxEUInput();
                                if (hatch.maxEUInput() > maxEUinputMax) maxEUinputMax = hatch.maxEUInput();
                            }
                        eMaxAmpereFlow = 0;
                        //counts only full amps
                        for (GT_MetaTileEntity_Hatch_Energy hatch : mEnergyHatches)
                            if (isValidMetaTileEntity(hatch)) eMaxAmpereFlow += hatch.maxEUInput() / maxEUinputMin;
                        for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : eEnergyMulti)
                            if (isValidMetaTileEntity(hatch))
                                eMaxAmpereFlow += (hatch.maxEUInput() / maxEUinputMin) * hatch.Amperes;
                        if (this.getEUVar() > maxEUStore()) this.setEUVar(this.maxEUStore());
                    } else {
                        maxEUinputMin = 0;
                        maxEUinputMax = 0;
                        eMaxAmpereFlow = 0;
                        this.setEUVar(0);
                    }

                    for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                        if (isValidMetaTileEntity(hatch)) hatch.getBaseMetaTileEntity().setActive(true);
                    for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches){
                        if (isValidMetaTileEntity(hatch)) {
                            hatch.getBaseMetaTileEntity().setActive(true);
                            if(hatch.param>=0) bParamsAreFloats[hatch.param]=hatch.isUsingFloats();
                        }
                    }
                } else {
                    maxEUinputMin = 0;
                    maxEUinputMax = 0;
                    eMaxAmpereFlow = 0;
                    this.setEUVar(0);
                }
                hatchInit_EM(mMachine);
            }

            if (mStartUpCheck < 0) {//E
                if (mMachine) {//S
                    final byte Tick = (byte) (aTick % 20);
                    if (MULTI_PURGE_1_AT == Tick || MULTI_PURGE_2_AT == Tick)
                        purgeAllOverflowEM_EM();
                    else if (MULTI_CHECK_AT == Tick)
                        for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
                            if (isValidMetaTileEntity(tHatch)) {
                                if (disableMaintenance) {
                                    mWrench = true;
                                    mScrewdriver = true;
                                    mSoftHammer = true;
                                    mHardHammer = true;
                                    mSolderingTool = true;
                                    mCrowbar = true;
                                } else {
                                    if (tHatch.mAuto && !(mWrench && mScrewdriver && mSoftHammer && mHardHammer && mSolderingTool && mCrowbar))
                                        tHatch.autoMaintainance();
                                    if (tHatch.mWrench) mWrench = true;
                                    if (tHatch.mScrewdriver) mScrewdriver = true;
                                    if (tHatch.mSoftHammer) mSoftHammer = true;
                                    if (tHatch.mHardHammer) mHardHammer = true;
                                    if (tHatch.mSolderingTool) mSolderingTool = true;
                                    if (tHatch.mCrowbar) mCrowbar = true;

                                    tHatch.mWrench = false;
                                    tHatch.mScrewdriver = false;
                                    tHatch.mSoftHammer = false;
                                    tHatch.mHardHammer = false;
                                    tHatch.mSolderingTool = false;
                                    tHatch.mCrowbar = false;
                                }
                            }
                        }
                    else if (MOVE_AT == Tick && eSafeVoid) {
                        for (GT_MetaTileEntity_Hatch_MufflerElemental voider : eMufflerHatches) {
                            if (voider.overflowMax < voider.getOverflowMatter()) continue;
                            float remaining = voider.overflowMax - voider.getOverflowMatter();
                            for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
                                for (cElementalInstanceStack instance : in.getContainerHandler().values()) {
                                    long qty = (long) Math.floor(remaining / instance.definition.getMass());
                                    if (qty > 0) {
                                        qty = Math.min(qty, instance.amount);
                                        if (voider.addOverflowMatter(instance.definition.getMass() * qty))
                                            voider.setOverflowMatter(voider.overflowMax);
                                        in.getContainerHandler().removeAmount(false, new cElementalDefinitionStack(instance.definition, qty));
                                    }
                                }
                            }
                            for (GT_MetaTileEntity_Hatch_OutputElemental out : eOutputHatches) {
                                for (cElementalInstanceStack instance : out.getContainerHandler().values()) {
                                    long qty = (long)Math.floor(remaining / instance.definition.getMass());
                                    if (qty > 0) {
                                        qty = Math.min(qty, instance.amount);
                                        if (voider.addOverflowMatter(instance.definition.getMass() * qty))
                                            voider.setOverflowMatter(voider.overflowMax);
                                        out.getContainerHandler().removeAmount(false, new cElementalDefinitionStack(instance.definition, qty));
                                    }
                                }
                            }
                        }
                    }

                    if (getRepairStatus() >= minRepairStatus) {//S
                        if (MULTI_CHECK_AT == Tick)
                            hatchesStatusUpdate_EM();

                        //region power pass and controller charging
                        {//DO
                            long euVar;
                            for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
                                if (this.getEUVar() > this.getMinimumStoredEU()) break;
                                if (isValidMetaTileEntity(tHatch)) {
                                    euVar = tHatch.maxEUInput();
                                    if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euVar, false))
                                        this.setEUVar(this.getEUVar() + euVar);
                                }
                            }
                            for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
                                if (this.getEUVar() > this.getMinimumStoredEU()) break;
                                if (isValidMetaTileEntity(tHatch)) {
                                    euVar = tHatch.maxEUInput() * tHatch.Amperes;
                                    if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euVar, false))
                                        this.setEUVar(this.getEUVar() + euVar);
                                }
                            }
                            if (ePowerPass) {
                                for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
                                    if (isValidMetaTileEntity(tHatch)) {
                                        euVar = tHatch.maxEUOutput();
                                        if (tHatch.getBaseMetaTileEntity().getStoredEU() <= (tHatch.maxEUStore() - euVar) &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + Math.min(euVar >> 7,1), false))
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                    }
                                }
                                for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
                                    if (isValidMetaTileEntity(tHatch)) {
                                        euVar = tHatch.maxEUOutput() * tHatch.Amperes;
                                        if (tHatch.getBaseMetaTileEntity().getStoredEU() <= tHatch.maxEUStore() - euVar &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + Math.min(euVar >> 7,tHatch.Amperes), false))
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                    }
                                }
                            }
                        }
                        //endregion

                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {//Start
                            if (onRunningTick(mInventory[1])) {//Compute EU
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) stopMachine();
                                if (!polluteEnvironment_EM(getPollutionPerTick_EM(mInventory[1]))) stopMachine();

                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime && RECIPE_AT == Tick) {//progress increase and done
                                    hatchesStatusUpdate_EM();

                                    outputAfterRecipe_EM();
                                    cleanOutputEM_EM();

                                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems)
                                        if (tStack != null) addOutput(tStack);
                                    mOutputItems = null;

                                    if (mOutputFluids != null) {
                                        if (mOutputFluids.length == 1) {
                                            for (FluidStack tStack : mOutputFluids)
                                                if (tStack != null) addOutput(tStack);
                                        } else if (mOutputFluids.length > 1) addFluidOutputs(mOutputFluids);
                                    }
                                    mOutputFluids = null;

                                    updateSlots();
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                                        if (checkRecipe(mInventory[1])) {
                                            mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                        }else {
                                            afterRecipeCheckFailed();
                                            mMaxProgresstime=0;//Just to be sure...
                                        }
                                        updateSlots();
                                    } else notAllowedToWork_stopMachine_EM();
                                }
                            }// else {//failed to consume power/resources - inside on running tick
                            //    stopMachine();
                            //}
                        } else if (RECIPE_AT == Tick || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe(mInventory[1])) {
                                    mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                }else {
                                    afterRecipeCheckFailed();
                                    mMaxProgresstime=0;//Just to be sure...
                                }
                                updateSlots();
                            } //else notAllowedToWork_stopMachine_EM(); //it is already stopped here
                        }
                    } else {//not repaired
                        stopMachine();
                    }
                } else {//not complete
                    stopMachine();
                }
            }

            aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & -512) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8) | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64) | ((eCertainStatus == 0) ? 0 : 128) | (eParameters ? 0 : 256));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            boolean active = aBaseMetaTileEntity.isActive() && mPollution > 0;
            for (GT_MetaTileEntity_Hatch_Muffler aMuffler : mMufflerHatches)
                aMuffler.getBaseMetaTileEntity().setActive(active);
        }
    }

    public boolean polluteEnvironment_EM(int aPollutionLevel) {
        return true; //TODO
    }

    @Deprecated
    public final int getAmountOfOutputs() {
        throw new NoSuchMethodError("Deprecated Do not use");
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids) {
        int min=mOutputFluids.length>mOutputHatches.size()?mOutputHatches.size():mOutputFluids.length;
        for (int i = 0; i < min; ++i) {
            if (this.mOutputHatches.get(i) != null && mOutputFluids[i] != null && isValidMetaTileEntity(this.mOutputHatches.get(i))) {
                this.mOutputHatches.get(i).fill(mOutputFluids[i], true);
            }
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getIdealStatus() {
        return super.getIdealStatus() + 2;
    }

    @Override
    public int getRepairStatus() {
        return super.getRepairStatus() + ((eCertainStatus == 0) ? 1 : 0) + (this.eParameters ? 1 : 0);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (eRequiredData > eAvailableData) {
            if(energyFlowOnRunningTick(aStack,false))
                stopMachine();
            return false;
        }
        return energyFlowOnRunningTick(aStack,true);
    }

    //new method
    private boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        long euFlow = mEUt * eAmpereFlow;//quick scope sign
        if (allowProduction && euFlow > 0) {
            this.addEnergyOutput_EM((long) mEUt * (long) mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
        } else if (euFlow < 0) {
            if (!this.drainEnergyInput_EM(mEUt,(long) mEUt * getMaxEfficiency(aStack) / Math.max(1000L, this.mEfficiency), eAmpereFlow)) {
                stopMachine();
                return false;
            }
        }
        return true;
    }

    //region energy
    //public final boolean energyFlowWithoutEffieciencyComputation(int eu,long ampere) {
    //    long temp = eu * ampere;//quick scope sign
    //    if (temp > 0) {
    //        this.addEnergyOutput_EM(eu, ampere);
    //    } else if (temp < 0) {
    //        if (!this.drainEnergyInput_EM(eu,eu, ampere)) {
    //            stopMachine();
    //            return false;
    //        }
    //    }
    //    return true;
    //}

    @Override
    public final long maxEUStore() {
        return (maxEUinputMin * eMaxAmpereFlow) << 3;
    }

    @Override
    public final long getMinimumStoredEU() {
        return maxEUStore() >> 1;
    }

    @Override
    public final long maxAmperesIn() {
        return 0L;
    }

    @Override
    public final long maxAmperesOut() {
        return 0L;
    }

    @Deprecated
    @Override
    public final boolean addEnergyOutput(long EU) {
        if (EU <= 0L) return true;
        for (GT_MetaTileEntity_Hatch tHatch : eDynamoMulti)
            if (isValidMetaTileEntity(tHatch)){
                if(tHatch.maxEUOutput()<EU) explodeMultiblock();
                if(tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU, false))
                    return true;
            }
        for (GT_MetaTileEntity_Hatch tHatch : mDynamoHatches)
            if (isValidMetaTileEntity(tHatch)){
                if(tHatch.maxEUOutput()<EU) explodeMultiblock();
                if(tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU, false))
                    return true;
            }
        return false;
    }

    private boolean addEnergyOutput_EM(long EU, long Amperes) {
        if(EU<0) EU=-EU;
        if(Amperes<0) Amperes=-Amperes;
        long euVar = EU * Amperes;
        long diff;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                if (tHatch.maxEUOutput() < EU) explodeMultiblock();
                diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity().getStoredEU();
                if (diff > 0) {
                    if (euVar > diff) {
                        tHatch.setEUVar(tHatch.maxEUStore());
                        euVar -= diff;
                    } else if (euVar <= diff) {
                        tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                        return true;
                    }
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                if (tHatch.maxEUOutput() < EU) explodeMultiblock();
                diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity().getStoredEU();
                if (diff > 0) {
                    if (euVar > diff) {
                        tHatch.setEUVar(tHatch.maxEUStore());
                        euVar -= diff;
                    } else if (euVar <= diff) {
                        tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public final boolean drainEnergyInput(long EU) {
        if (EU <= 0L) return true;
        for (GT_MetaTileEntity_Hatch tHatch : eEnergyMulti)
            if (isValidMetaTileEntity(tHatch) && tHatch.maxEUInput()>EU && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU, false))
                return true;
        for (GT_MetaTileEntity_Hatch tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch) && tHatch.maxEUInput()>EU && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU, false))
                return true;
        return false;
    }

    private boolean drainEnergyInput_EM(long EUtTierVoltage, long EUtEffective, long Amperes) {
        if(EUtTierVoltage<0) EUtTierVoltage=-EUtTierVoltage;
        if(EUtEffective<0) EUtEffective=-EUtEffective;
        if(Amperes<0) Amperes=-Amperes;
        long EUuse = EUtEffective * Amperes;
        if (EUuse > getEUVar() || //not enough power
                EUtTierVoltage > maxEUinputMax || //TIER IS BASED ON BEST HATCH! not total EUtEffective input
                ((EUtTierVoltage*Amperes) - 1) / maxEUinputMin + 1 > eMaxAmpereFlow) {// EUuse==0? --> (EUuse - 1) / maxEUinputMin + 1 = 1! //if not too much A
            if (DEBUG_MODE) {
                TecTech.Logger.debug("L1 " + EUuse + " " + getEUVar() + " " + (EUuse > getEUVar()));
                TecTech.Logger.debug("L2 " + EUtEffective + " " + maxEUinputMax + " " + (EUtEffective > maxEUinputMax));
                TecTech.Logger.debug("L3 " + EUuse + " " + eMaxAmpereFlow);
                TecTech.Logger.debug("L4 " + ((EUuse - 1) / maxEUinputMin + 1) + " " + eMaxAmpereFlow + " " + ((EUuse - 1) / maxEUinputMin + 1 > eMaxAmpereFlow));
            }
            return false;
        }
        //sub eu
        setEUVar(getEUVar() - EUuse);
        return true;
    }

    //new method
    public final boolean overclockAndPutValuesIn_EM(long EU, int time) {//TODO revise
        if (EU == 0L) {
            mEUt = 0;
            mMaxProgresstime = time;
            return true;
        }
        long tempEUt = EU < V[1] ? V[1] : EU;
        long tempTier = maxEUinputMax >> 2;
        while (tempEUt < tempTier) {
            tempEUt <<= 2;
            time >>= 1;
            EU = time == 0 ? EU >> 1 : EU << 2;//U know, if the time is less than 1 tick make the machine use less power
        }
        if (EU > Integer.MAX_VALUE || EU < Integer.MIN_VALUE) {
            mEUt = Integer.MAX_VALUE - 1;
            mMaxProgresstime = Integer.MAX_VALUE - 1;
            return false;
        }
        mEUt = (int) EU;
        mMaxProgresstime = time == 0 ? 1 : time;
        return true;
    }//Use in EM check recipe return statement if you want overclocking

    @Override // same as gt sum of all hatches
    public final long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.maxEUInput();
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.maxEUInput();
        return rVoltage;
    }

    //new Method
    public final long getMaxInputEnergy(){
        long energy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) energy += tHatch.maxEUInput();
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
            if (isValidMetaTileEntity(tHatch)) energy += tHatch.maxEUInput() * tHatch.Amperes;
        return energy;
    }

    //new Method
    public final int getMaxEnergyInputTier_EM() {
        return Util.getTier(maxEUinputMax);
    }

    //new Method
    public final int getMinEnergyInputTier_EM() {
        return Util.getTier(maxEUinputMin);
    }

    public final long getMaxAmpereFlowAtMinTierOfEnergyHatches(){
        return eAmpereFlow;
    }

    //endregion

    //new Method
    public final cElementalInstanceStackMap getInputsClone_EM(){
        cElementalInstanceStackMap in=new cElementalInstanceStackMap();
        for(GT_MetaTileEntity_Hatch_ElementalContainer hatch:eInputHatches){
            in.putUnifyAll(hatch.getContainerHandler());
        }
        return in.hasStacks()?in:null;
    }

    //new Method
    public final cElementalInstanceStackMap getOutputsClone_EM(){
        cElementalInstanceStackMap out=new cElementalInstanceStackMap();
        for(GT_MetaTileEntity_Hatch_ElementalContainer hatch:eOutputHatches){
            out.putUnifyAll(hatch.getContainerHandler());
        }
        return out.hasStacks()?out:null;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        purgeAllOverflowEM_EM();
    }

    //region em cleaning
    private void purgeAllOverflowEM_EM() {
        float mass = 0;
        for (GT_MetaTileEntity_Hatch_InputElemental tHatch : eInputHatches) {
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
            mass += tHatch.overflowMatter;
            tHatch.overflowMatter = 0;
        }
        for (GT_MetaTileEntity_Hatch_OutputElemental tHatch : eOutputHatches) {
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
            mass += tHatch.overflowMatter;
            tHatch.overflowMatter = 0;
        }
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches)
                if (dump.addOverflowMatter(mass)) explodeMultiblock();
        }
    }

    public void cleanHatchContentEM_EM(GT_MetaTileEntity_Hatch_ElementalContainer target) {
        if (target == null) return;
        float mass = target.getContainerHandler().getMass();
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches)
                if (dump.addOverflowMatter(mass)) explodeMultiblock();
        }
    }

    public void cleanStackEM_EM(cElementalInstanceStack target) {
        if (target == null) return;
        cleanMassEM_EM(target.getMass());
    }

    public void cleanMassEM_EM(float mass) {
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches)
                if (dump.addOverflowMatter(mass)) explodeMultiblock();
        }
    }

    private void cleanOutputEM_EM() {
        if (outputEM == null) return;
        float mass = 0;
        for (cElementalInstanceStackMap map : outputEM)
            mass += map.getMass();

        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches)
                if (dump.addOverflowMatter(mass)) explodeMultiblock();
        }
        outputEM = null;
    }
    //endregion

    @Override
    public final boolean checkRecipe(ItemStack itemStack) {//do recipe checks, based on "machine content and state"
        hatchesStatusUpdate_EM();
        boolean result= checkRecipe_EM(itemStack);//if had no - set default params
        hatchesStatusUpdate_EM();
        return result;
    }

    private void hatchesStatusUpdate_EM() {
        boolean busy=mMaxProgresstime>0;
        if (busy) {//write from buffer to hatches only
            for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                if (!isValidMetaTileEntity(hatch) || hatch.param < 0) continue;
                final int paramID = hatch.param;
                if(bParamsAreFloats[hatch.param] == hatch.isUsingFloats()){
                    hatch.input0i = iParamsOut[paramID];
                    hatch.input1i = iParamsOut[paramID + 10];
                }else if(hatch.isUsingFloats()){
                    hatch.input0i = Float.floatToIntBits((float)iParamsOut[paramID]);
                    hatch.input1i = Float.floatToIntBits((float)iParamsOut[paramID + 10]);
                }else {
                    hatch.input0i = (int)Float.intBitsToFloat(iParamsOut[paramID]);
                    hatch.input1i = (int)Float.intBitsToFloat(iParamsOut[paramID + 10]);
                }
            }
            parametersInRead_EM();
        } else {//if has nothing to do update all
            for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                if (!isValidMetaTileEntity(hatch) || hatch.param < 0) continue;
                final int paramID = hatch.param;
                bParamsAreFloats[hatch.param] = hatch.isUsingFloats();
                iParamsIn[paramID] = hatch.value0i;
                iParamsIn[paramID + 10] = hatch.value1i;
                hatch.input0i = iParamsOut[paramID];
                hatch.input1i = iParamsOut[paramID + 10];
            }
        }
        for (GT_MetaTileEntity_Hatch_Uncertainty uncertainty : eUncertainHatches)
            eCertainStatus = uncertainty.update(eCertainMode);
        eAvailableData = getAvailableData_EM();
        parametersOutAndStatusesWrite_EM(busy);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public final void explodeMultiblock() {
        if(explodedThisTick)return;
        explodedThisTick=true;
        if (!TecTech.ModConfig.BOOM_ENABLE) {
            TecTech.proxy.broadcast("Multi Explode BOOM! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            TecTech.proxy.broadcast("Multi Explode BOOM! " + ste[2].toString());
            return;
        }
        extraExplosions_EM();
        GT_Pollution.addPollution(getBaseMetaTileEntity(), 600000);
        mInventory[1] = null;
        for (MetaTileEntity tTileEntity : mInputBusses) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mOutputBusses) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mInputHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mOutputHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mDynamoHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : mMufflerHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mEnergyHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : mMaintenanceHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eParamHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eInputHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eOutputHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eMufflerHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eEnergyMulti) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eUncertainHatches) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eDynamoMulti) tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eInputData) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eOutputData) tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        getBaseMetaTileEntity().doExplosion(V[15]);
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        explodeMultiblock();
        super.doExplosion(aExplosionPower);
    }//Redirecting to explodemultiblock

    @Override
    public void onRemoval() {
        try {
            if (eOutputHatches != null) {
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches)
                    hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches)
                    hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_OutputData hatch_data : eOutputData) {
                    hatch_data.id = -1;
                    hatch_data.q = null;
                }
                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eInputData)
                    hatch_data.id = -1;
                for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
                for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
            }
            if ((ePowerPass && getEUVar()>V[3]) || (eDismantleBoom && mMaxProgresstime > 0 && areChunksAroundLoaded_EM()))
                explodeMultiblock();
            if (outputEM != null)
                for (cElementalInstanceStackMap output : outputEM)
                    if (output.hasStacks()) explodeMultiblock();
        } catch (Exception e) {
            if (DEBUG_MODE) e.printStackTrace();
        }
    }

    //region adder methods
    @Override
    public final boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental)
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param)
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty)
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental)
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_MufflerElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti)
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputData)
            return eInputData.add((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputData)
            return eOutputData.add((GT_MetaTileEntity_Hatch_OutputData) aMetaTileEntity);
        return false;
    }

    public final boolean addClassicToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param)
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty)
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti)
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputData)
            return eInputData.add((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputData)
            return eOutputData.add((GT_MetaTileEntity_Hatch_OutputData) aMetaTileEntity);
        return false;
    }

    public final boolean addElementalToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental)
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental)
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental)
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_MufflerElemental) aMetaTileEntity);
        return false;
    }

    public final boolean addClassicMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addElementalMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_MufflerElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_MufflerElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            //((GT_MetaTileEntity_Hatch_Elemental) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        return false;
    }

    @Deprecated
    @Override
    public final boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        return false;
    }

    @Deprecated
    @Override
    public final boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        return false;
    }

    //New Method
    public final boolean addEnergyIOToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addElementalInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            //((GT_MetaTileEntity_Hatch_Elemental) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addElementalOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addParametrizerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addUncertainToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addDataConnectorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputData) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eInputData.add((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputData) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eOutputData.add((GT_MetaTileEntity_Hatch_OutputData) aMetaTileEntity);
        }
        return false;
    }
    //endregion

    public static void run() {
        try {
            adderMethodMap.put("addToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addClassicToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addClassicToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addElementalToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addElementalToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addMufflerToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addMufflerToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addClassicMufflerToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addClassicMufflerToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addElementalMufflerToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addElementalMufflerToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addInputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addInputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addOutputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addOutputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addEnergyInputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addEnergyInputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addDynamoToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addDynamoToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addEnergyIOToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addEnergyIOToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addElementalInputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addElementalInputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addElementalOutputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addElementalOutputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addClassicInputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addClassicInputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addClassicOutputToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addClassicOutputToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addParametrizerToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addParametrizerToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addUncertainToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addUncertainToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addMaintenanceToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addMaintenanceToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addClassicMaintenanceToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addClassicMaintenanceToMachineList", IGregTechTileEntity.class, int.class));
            adderMethodMap.put("addDataConnectorToMachineList", GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addDataConnectorToMachineList", IGregTechTileEntity.class, int.class));
            adderMethod = GT_MetaTileEntity_MultiblockBase_EM.class.getMethod("addThing", String.class, IGregTechTileEntity.class, int.class);
        } catch (NoSuchMethodException e) {
            if (DEBUG_MODE) e.printStackTrace();
        }
    }

    //CALLBACK
    public boolean addThing(String methodName, IGregTechTileEntity igt, int casing) {
        try {
            return (boolean) adderMethodMap.get(methodName).invoke(this, igt, casing);
        } catch (InvocationTargetException | IllegalAccessException e) {
            if (DEBUG_MODE) e.printStackTrace();
        }
        return false;
    }

    @Override
    public String[] getInfoData() {//TODO Do it
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                "Progress:",
                EnumChatFormatting.GREEN + Integer.toString(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + Integer.toString(mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
                "Energy Hatches:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET + " EU",
                (mEUt <= 0 ? "Probably uses: " : "Probably makes: ") +
                        EnumChatFormatting.RED + Integer.toString(Math.abs(mEUt)) + EnumChatFormatting.RESET + " EU/t at " +
                        EnumChatFormatting.RED + eAmpereFlow + EnumChatFormatting.RESET + " A",
                "Tier Rating: " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier_EM()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier_EM()] + EnumChatFormatting.RESET +
                        " Amp Rating: " + EnumChatFormatting.GREEN + eMaxAmpereFlow + EnumChatFormatting.RESET + " A",
                "Problems: " + EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " Efficiency: " + EnumChatFormatting.YELLOW + Float.toString(mEfficiency / 100.0F) + EnumChatFormatting.RESET + " %",
                "PowerPass: " + EnumChatFormatting.BLUE + ePowerPass + EnumChatFormatting.RESET +
                        " SafeVoid: " + EnumChatFormatting.BLUE + eSafeVoid,
                "Computation: " + EnumChatFormatting.GREEN + eAvailableData + EnumChatFormatting.RESET + " / " + EnumChatFormatting.YELLOW + eRequiredData + EnumChatFormatting.RESET
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }
}
