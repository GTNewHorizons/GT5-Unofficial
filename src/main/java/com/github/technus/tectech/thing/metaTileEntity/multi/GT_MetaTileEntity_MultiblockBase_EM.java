package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.classes.tElementalException;
import com.github.technus.tectech.thing.machineTT;
import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.gui.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.gui.GT_GUIContainer_MultiMachineEM;
import com.github.technus.tectech.vec3pos;
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
import static com.github.technus.tectech.Util.StructureChecker;
import static com.github.technus.tectech.Util.StructureCheckerAdvanced;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;

/**
 * Created by danie_000 on 27.10.2016.
 */
public abstract class GT_MetaTileEntity_MultiblockBase_EM extends GT_MetaTileEntity_MultiBlockBase implements machineTT {
    protected final static Map<String, Method> adderMethodMap = new HashMap<>();
    public static Method adderMethod;

    protected cElementalInstanceStackMap[] outputEM;

    public final static ItemStack[] nothingI = new ItemStack[0];
    public final static FluidStack[] nothingF = new FluidStack[0];

    protected static Textures.BlockIcons.CustomIcon ScreenOFF;
    protected static Textures.BlockIcons.CustomIcon ScreenON;
    protected final static int textureOffset = 96;

    public ArrayList<GT_MetaTileEntity_Hatch_InputElemental> eInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputElemental> eOutputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_MufflerElemental> eMufflerHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Param> eParamHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Uncertainty> eUncertainHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> eEnergyMulti = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> eDynamoMulti = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_InputData> eInputData = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputData> eOutputData = new ArrayList<>();

    public final float[] eParamsIn = new float[20];
    public final float[] eParamsOut = new float[20];
    public final byte[] eParamsInStatus = new byte[20];
    public final byte[] eParamsOutStatus = new byte[20];
    public final static byte PARAM_UNUSED = 0, PARAM_OK = 1, PARAM_TOO_LOW = 2, PARAM_LOW = 3, PARAM_TOO_HIGH = 4, PARAM_HIGH = 5, PARAM_WRONG = 6;

    //TO ENABLE this change value in <init> to false and/or other than 0, can also be added in recipe check or whatever
    public boolean eParameters = true, ePowerPass = false, eSafeVoid = false, eDismatleBoom = false;
    public byte eCertainMode = 0, eCertainStatus = 0, minRepairStatus = 3;

    private long eMaxAmpereFlow = 0;
    private long maxEUinputMin = 0, maxEUinputMax = 0;
    public long eAmpereFlow = 1;
    public long eRequiredData = 0;
    protected long eAvailableData = 0;

    //init param states in constructor, or implement it in checkrecipe/outputfunction

    //METHODS TO OVERRIDE

    //if you want to add checks that run periodically when machine works then make onRunningTick better
    //if you want to add checks that run periodically when machine is built then use check params

    public boolean EM_checkRecipe(ItemStack itemStack) {
        return false;
    }
    // My code handles AMPS, if you want overclocking just modify mEUt and mMaxProgressTime, leave amps as usual!
    // Set mEUt, Efficiencies, required computation, time, check input etc.

    public void EM_checkParams() {
    }
    // update status of parameters in guis and "machine state"
    // Called before check recipe, before outputting, and every second the machine is active

    public void EM_outputFunction() {
    }
    // based on "machine state" do output,
    // this must move to outputEM to EM output hatches
    // and can also modify output items/fluids/EM, remaining EM is NOT overflowed.
    // (Well it can be overflowed if machine didn't finished, soft-hammered/disabled/not enough EU)
    // Setting available data processing

    protected void EM_extraHatchInitHook() {
    }//For extra types of hatches initiation, LOOK HOW IT IS CALLED! onPostTick

    protected void EM_extraExplosions() {
    }//For that extra hatches explosions, and maybe some MOORE EXPLOSIONS

    protected void EM_stopMachine() {
    }//On machine stop

    //machine structure check
    protected boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return false;
    }

    //Get Available data, Override only on data producers should return mAvailableData that is set in check recipe
    protected long EM_getAvailableData() {
        long result = 0;
        final vec3pos pos = new vec3pos(getBaseMetaTileEntity());
        for (GT_MetaTileEntity_Hatch_InputData in : eInputData)
            if (in.q != null) result += in.q.computationIfNotContained(pos);
        return result;
    }

    //Extra hook on cyclic updates (not really needed for machines smaller than 1 chunk)
    //BUT NEEDED WHEN - machine blocks are not touching each other ot they don't implement IMachineBlockUpdateable (ex. air)
    protected boolean EM_cyclicUpdate() {
        return mUpdate <= -1000;//set to false to disable cyclic update
        //default is once per 50s; mUpdate is decremented every tick
    }


    //RATHER LEAVE ALONE Section

    public GT_MetaTileEntity_MultiblockBase_EM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiblockBase_EM(String aName) {
        super(aName);
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
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 4], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[textureOffset + 4]};
    }

    @Override
    public final byte getTileEntityBaseType() {
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
        aNBT.setBoolean("eBoom", eDismatleBoom);
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
        for (int i = 0; i < 20; i++)
            paramI.setFloat(Integer.toString(i), eParamsIn[i]);
        aNBT.setTag("eParamsIn", paramI);

        NBTTagCompound paramO = new NBTTagCompound();
        for (int i = 0; i < 20; i++)
            paramO.setFloat(Integer.toString(i), eParamsOut[i]);
        aNBT.setTag("eParamsOut", paramO);

        NBTTagCompound paramIs = new NBTTagCompound();
        for (int i = 0; i < 20; i++)
            paramIs.setByte(Integer.toString(i), eParamsInStatus[i]);
        aNBT.setTag("eParamsInS", paramIs);

        NBTTagCompound paramOs = new NBTTagCompound();
        for (int i = 0; i < 20; i++)
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
        eDismatleBoom = aNBT.getBoolean("eBoom");
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
                    if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
                    outputEM[i] = new cElementalInstanceStackMap();
                }
        } else outputEM = new cElementalInstanceStackMap[0];

        NBTTagCompound paramI = aNBT.getCompoundTag("eParamsIn");
        for (int i = 0; i < eParamsIn.length; i++)
            eParamsIn[i] = paramI.getFloat(Integer.toString(i));

        NBTTagCompound paramO = aNBT.getCompoundTag("eParamsOut");
        for (int i = 0; i < eParamsOut.length; i++)
            eParamsOut[i] = paramO.getFloat(Integer.toString(i));

        NBTTagCompound paramIs = aNBT.getCompoundTag("eParamsInS");
        for (int i = 0; i < eParamsInStatus.length; i++)
            eParamsInStatus[i] = paramIs.getByte(Integer.toString(i));

        NBTTagCompound paramOs = aNBT.getCompoundTag("eParamsOutS");
        for (int i = 0; i < eParamsOutStatus.length; i++)
            eParamsOutStatus[i] = paramOs.getByte(Integer.toString(i));
    }

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

    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    private boolean cyclicUpdate() {
        if (EM_cyclicUpdate()) {
            mUpdate = 0;
            return true;
        }
        return false;
    }

    @Override
    public final boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_checkMachine(iGregTechTileEntity, itemStack);
    }

    @Override
    public final void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
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

                if (!mMachine)
                    if (eDismatleBoom && mMaxProgresstime > 0) explodeMultiblock();
                    else if (outputEM != null)
                        for (cElementalInstanceStackMap tree : outputEM)
                            if (tree.hasStacks()) {
                                explodeMultiblock();
                                break;
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
                    for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                        if (isValidMetaTileEntity(hatch)) hatch.getBaseMetaTileEntity().setActive(true);
                } else {
                    maxEUinputMin = 0;
                    maxEUinputMax = 0;
                    eMaxAmpereFlow = 0;
                    this.setEUVar(0);
                }
                EM_extraHatchInitHook();
            }

            if (mStartUpCheck < 0) {//E
                if (mMachine) {//S
                    final byte Tick = (byte) (aTick % 20);
                    if (multiPurge1At == Tick || multiPurge2At == Tick)
                        purgeAll();
                    else if (multiCheckAt == Tick)
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
                                        tHatch.isRecipeInputEqual(true);
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
                    else if (moveAt == Tick && eSafeVoid) {
                        for (GT_MetaTileEntity_Hatch_MufflerElemental voider : eMufflerHatches) {
                            if (voider.overflowMax < voider.overflowMatter) continue;
                            float remaining = voider.overflowMax - voider.overflowMatter;
                            for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
                                for (cElementalInstanceStack instance : in.getContainerHandler().values()) {
                                    int qty = (int) Math.floor(remaining / instance.definition.getMass());
                                    if (qty > 0) {
                                        qty = Math.min(qty, instance.amount);
                                        voider.overflowMatter += instance.definition.getMass() * qty;
                                        in.getContainerHandler().removeAmount(false, new cElementalDefinitionStack(instance.definition, qty));
                                    }
                                }
                            }
                            for (GT_MetaTileEntity_Hatch_OutputElemental out : eOutputHatches) {
                                for (cElementalInstanceStack instance : out.getContainerHandler().values()) {
                                    int qty = (int) Math.floor(remaining / instance.definition.getMass());
                                    if (qty > 0) {
                                        qty = Math.min(qty, instance.amount);
                                        voider.overflowMatter += instance.definition.getMass() * qty;
                                        out.getContainerHandler().removeAmount(false, new cElementalDefinitionStack(instance.definition, qty));
                                    }
                                }
                            }
                            //in case some weird shit happened here, it will still be safe
                            if (voider.overflowMatter > voider.overflowMax) voider.overflowMatter = voider.overflowMax;
                        }
                    }

                    if (getRepairStatus() >= minRepairStatus) {//S
                        if (multiCheckAt == Tick)
                            hatchesStatusUpdate();

                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {//Start
                            if (onRunningTick(mInventory[1])) {//Compute EU
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1])))
                                    stopMachine();

                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime && recipeAt == Tick) {//progress increase and done
                                    hatchesStatusUpdate();
                                    EM_outputFunction();
                                    cleanOutputEM();
                                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems)
                                        if (tStack != null)
                                            addOutput(tStack);

                                    if (mOutputFluids != null && mOutputFluids.length == 1)
                                        for (FluidStack tStack : mOutputFluids)
                                            if (tStack != null)
                                                addOutput(tStack);
                                            else if (mOutputFluids != null && mOutputFluids.length > 1)
                                                addFluidOutputs(mOutputFluids);
                                    updateSlots();
                                    mOutputItems = null;
                                    mOutputFluids = null;
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                                        if (checkRecipe(mInventory[1])) {
                                            mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                        }
                                        updateSlots();
                                    } else stopMachine();
                                }
                            }
                        } else {
                            if (recipeAt == Tick || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                                if (aBaseMetaTileEntity.isAllowedToWork()) {
                                    if (checkRecipe(mInventory[1])) {
                                        mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                    }
                                    updateSlots();
                                } else stopMachine();
                            }
                        }

                        {//DO ONCE
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
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + (euVar >> 7), false))
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                    }
                                }
                                for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
                                    if (isValidMetaTileEntity(tHatch)) {
                                        euVar = tHatch.maxEUOutput() * tHatch.Amperes;
                                        if (tHatch.getBaseMetaTileEntity().getStoredEU() <= tHatch.maxEUStore() - euVar &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + (euVar >> 7), false))
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                    }
                                }
                            }
                        }
                    } else {//not repaired
                        stopMachine();
                    }
                } else {//not machine
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

    @Deprecated
    @Override
    public final int getAmountOfOutputs() {
        return 0;
    }

    private void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length; ++i) {
            if (this.mOutputHatches.size() > i && this.mOutputHatches.get(i) != null && mOutputFluids2[i] != null && isValidMetaTileEntity((MetaTileEntity) this.mOutputHatches.get(i))) {
                this.mOutputHatches.get(i).fill(mOutputFluids2[i], true);
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
        if (eRequiredData > 0 && eRequiredData > eAvailableData) return false;
        if (this.mEUt > 0) {
            this.EMaddEnergyOutput((long) mEUt * (long) mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
            return true;
        } else if (this.mEUt < 0 && !this.EMdrainEnergyInput((long) (-this.mEUt) * getMaxEfficiency(aStack) / (long) Math.max(1000, this.mEfficiency), eAmpereFlow)) {
            this.stopMachine();
            return false;
        } else return true;
    }

    @Deprecated
    @Override
    public final boolean addEnergyOutput(long EU) {
        if (EU <= 0L) return true;
        for (GT_MetaTileEntity_Hatch tHatch : eDynamoMulti)
            if (isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU, false))
                return true;
        for (GT_MetaTileEntity_Hatch tHatch : mDynamoHatches)
            if (isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU, false))
                return true;
        return false;
    }

    //new method
    public final boolean EMaddEnergyOutput(long EU, long Amperes) {
        if (EU <= 0L || Amperes <= 0) return true;
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
            if (isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU, false))
                return true;
        for (GT_MetaTileEntity_Hatch tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU, false))
                return true;
        return false;
    }

    //new method
    public final boolean EMdrainEnergyInput(long EU, long Amperes) {
        if (EU <= 0L || Amperes <= 0) return true;
        long euVar = EU * Amperes;
        if (euVar > getEUVar() ||
                EU > maxEUinputMax ||
                (euVar - 1) / maxEUinputMin + 1 > eMaxAmpereFlow) {// euVar==0? --> (euVar - 1) / maxEUinputMin + 1 = 1!
            if (TecTechConfig.DEBUG_MODE) {
                TecTech.Logger.debug("OMG1 " + euVar + " " + getEUVar() + " " + (euVar > getEUVar()));
                TecTech.Logger.debug("OMG2 " + EU + " " + maxEUinputMax + " " + (EU > maxEUinputMax));
                TecTech.Logger.debug("OMG3 " + euVar + " " + eMaxAmpereFlow);
                TecTech.Logger.debug("OMG4 " + ((euVar - 1) / maxEUinputMin + 1) + " " + eMaxAmpereFlow + " " + ((euVar - 1) / maxEUinputMin + 1 > eMaxAmpereFlow));
            }
            return false;
        }
        //sub eu
        setEUVar(getEUVar() - euVar);
        return true;
    }

    //new method
    public final boolean EMoverclockAndPutValuesIn(long EU, int time) {
        if (EU == 0) {
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

    @Override
    public final long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.maxEUInput();
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.maxEUInput();
        return rVoltage;
    }

    //new Method
    public final int getMaxEnergyInputTier() {
        return GT_Utility.getTier(maxEUinputMax);
    }

    //new Method
    public final int getMinEnergyInputTier() {
        return GT_Utility.getTier(maxEUinputMin);
    }

    @Override
    public final void stopMachine() {
        mOutputItems = null;
        mOutputFluids = null;
        //mEUt = 0;
        mEfficiency = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        mEfficiencyIncrease = 0;
        getBaseMetaTileEntity().disableWorking();

        for (GT_MetaTileEntity_Hatch_OutputData data : eOutputData) {
            data.q = null;
        }

        float mass = 0;
        if (outputEM == null) return;
        for (cElementalInstanceStackMap tree : outputEM)
            mass += tree.getMass();
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
        outputEM = null;

        hatchesStatusUpdate();

        EM_stopMachine();
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        purgeAll();
    }

    private void purgeAll() {
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
            if (eMufflerHatches.size() < 1) {
                explodeMultiblock();
                return;
            }
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    public void cleanHatchContent(GT_MetaTileEntity_Hatch_ElementalContainer target) {
        if (target == null) return;
        float mass = target.getContainerHandler().getMass();
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    public void cleanInstanceStack(cElementalInstanceStack target) {
        if (target == null) return;
        float mass = target.getMass();
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    private void cleanOutputEM() {
        if (outputEM == null) return;
        float mass = 0;
        for (cElementalInstanceStackMap map : outputEM)
            mass = map.removeOverflow(0, 0);

        if (mass > 0) {
            if (eMufflerHatches.size() < 1) {
                explodeMultiblock();
                return;
            }
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
        outputEM = null;
    }

    @Override
    public final boolean checkRecipe(ItemStack itemStack) {//do recipe checks, based on "machine content and state"
        hatchesStatusUpdate();
        return EM_checkRecipe(itemStack);
    }

    private void hatchesStatusUpdate() {
        for (GT_MetaTileEntity_Hatch_Param param : eParamHatches) {
            final int paramID = param.param;
            if (paramID < 0) continue;
            eParamsIn[paramID] = param.value1f;
            eParamsIn[paramID + 10] = param.value2f;
            param.input1f = eParamsOut[paramID];
            param.input2f = eParamsOut[paramID + 10];
        }
        EM_checkParams();

        eAvailableData = EM_getAvailableData();

        for (GT_MetaTileEntity_Hatch_Uncertainty uncertainty : eUncertainHatches)
            eCertainStatus = uncertainty.update(eCertainMode);
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
    public void explodeMultiblock() {//BEST METHOD EVER!!!
        if (!TecTech.ModConfig.BOOM_ENABLE) {
            TecTech.proxy.broadcast("Multi Explode BOOM! " + getBaseMetaTileEntity().getXCoord() + " " + getBaseMetaTileEntity().getYCoord() + " " + getBaseMetaTileEntity().getZCoord());
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            TecTech.proxy.broadcast("Multi Explode BOOM! " + ste[2].toString());
            return;
        }
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
        EM_extraExplosions();
        getBaseMetaTileEntity().doExplosion(V[15]);
    }

    @Override
    public final boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addElementalMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MufflerElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            ((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            ((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
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
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eInputData.add((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputData) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return eOutputData.add((GT_MetaTileEntity_Hatch_OutputData) aMetaTileEntity);
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
                "Tier Rating: " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier()] + EnumChatFormatting.RESET +
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

    //can be used to check structures of multi-blocks larger than one chunk, but...
    //ALL THE HATCHES AND THE CONTROLLER SHOULD BE IN ONE CHUNK OR IN LOADED CHUNKS
    public final boolean EM_StructureCheck(
            String[][] structure,//0-9 casing, +- air no air, a-z ignore
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset) {
        return StructureChecker(structure, blockType, blockMeta, horizontalOffset, verticalOffset, depthOffset, getBaseMetaTileEntity(), !mMachine);
    }

    public final boolean EM_StructureCheckAdvanced(
            String[][] structure,//0-9 casing, +- air no air, a-z ignore
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            String[] addingMethods,
            byte[] casingTextures,
            Block[] blockTypeFallback,//use numbers 0-9 for casing types
            byte[] blockMetaFallback,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset) {
        return StructureCheckerAdvanced(structure, blockType, blockMeta,
                adderMethod, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback,
                horizontalOffset, verticalOffset, depthOffset, getBaseMetaTileEntity(), !mMachine);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Nothing special just override me."
        };
    }

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
            if (eDismatleBoom && mMaxProgresstime > 0) explodeMultiblock();
            else if (outputEM != null)
                for (cElementalInstanceStackMap output : outputEM)
                    if (output.hasStacks()) {
                        explodeMultiblock();
                        return;
                    }
        } catch (Exception e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
        }
    }

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
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
        }
    }

    //CALLBACK
    public final boolean addThing(String methodName, IGregTechTileEntity igt, int casing) {
        try {
            return (boolean) adderMethodMap.get(methodName).invoke(this, igt, casing);
        } catch (InvocationTargetException | IllegalAccessException e) {
            if (TecTechConfig.DEBUG_MODE) e.printStackTrace();
        }
        return false;
    }
}
