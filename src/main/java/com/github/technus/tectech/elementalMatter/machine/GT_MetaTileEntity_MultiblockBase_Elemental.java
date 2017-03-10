package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackTree;
import com.github.technus.tectech.elementalMatter.classes.tElementalException;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.gui.GT_Container_MultiMachineEM;
import com.github.technus.tectech.elementalMatter.gui.GT_GUIContainer_MultiMachineEM;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
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
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static com.github.technus.tectech.elementalMatter.commonValues.*;
import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 27.10.2016.
 */
public abstract class GT_MetaTileEntity_MultiblockBase_Elemental extends GT_MetaTileEntity_MultiBlockBase {
    protected cElementalInstanceStackTree[] outputEM=new cElementalInstanceStackTree[0];
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    public ArrayList<GT_MetaTileEntity_Hatch_InputElemental> eInputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputElemental> eOutputHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_MufflerElemental> eMufflerHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Param> eParamHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_Uncertainty> eUncertainHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> eEnergyMulti = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> eDynamoMulti = new ArrayList<>();

    public final float[] eParamsIn=new float[20];
    public final float[] eParamsOut=new float[20];
    public final byte[] eParamsInStatus =new byte[20];
    public final byte[] eParamsOutStatus=new byte[20];
    protected final byte PARAM_UNUSED=0, PARAM_OK=1, PARAM_TOO_LOW=2, PARAM_LOW=3, PARAM_TOO_HIGH=4, PARAM_HIGH=5, PARAM_WRONG=6;

    //TO ENABLE this change value in <init> to false and/or other than 0, can also be added in recipe check or whatever
    public boolean eParameters=true,ePowerPass=false,eSafeVoid=false;
    public byte eCertainMode=0,eCertainStatus=0;
    private int maxAmps=0;
    public int ampereRating=1,minRepairStatus=2;

    //init param states in constructor, or implement it in checkrecipe/outputfunction

    //METHODS TO OVERRIDE

    //if you want to add checks that run periodically when machine works then make onRunningTick better
    //if you want to add checks that run periodically when machine is built then use check params

    public boolean EM_checkRecipe(ItemStack itemStack){
        return false;
    }

    public void EM_checkParams(){
    }//update status of parameters in guis and "machine state"

    public void EM_outputFunction(){
        //it can also be here to do the last param check.
    }
    // based on "machine state" do output,
    // this must move to output EM things and can also modify output items/fluids

    //RATHER LEAVE ALONE
    public GT_MetaTileEntity_MultiblockBase_Elemental(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiblockBase_Elemental(String aName) {
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
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[99], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[99]};
    }

    @Override
    public final byte getTileEntityBaseType() {
        return 3;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        //Fix supermethod shit.
        if(mOutputItems!=null)
            aNBT.setInteger("eItemsOut",mOutputItems.length);
        if(mOutputFluids!=null)
            aNBT.setInteger("eFluidsOut",mOutputFluids.length);

        aNBT.setInteger("eRating",ampereRating);
        aNBT.setByte("eCertainM",eCertainMode);
        aNBT.setByte("eCertainS",eCertainStatus);
        aNBT.setBoolean("eParam",!eParameters);
        aNBT.setBoolean("ePass",ePowerPass);
        aNBT.setBoolean("eVoid",eSafeVoid);

        if (outputEM!=null) {
            aNBT.setInteger("outputStackCount", outputEM.length);
            NBTTagCompound output = new NBTTagCompound();
            for (int i = 0; i < outputEM.length; i++)
                output.setTag(Integer.toString(i), outputEM[i].toNBT());
            aNBT.setTag("outputEM", output);
        } else {
            aNBT.setInteger("outputStackCount", 0);
            aNBT.removeTag("outputEM");
        }

        NBTTagCompound paramI=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramI.setFloat(Integer.toString(i),eParamsIn[i]);
        aNBT.setTag("eParamsIn",paramI);

        NBTTagCompound paramO=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramO.setFloat(Integer.toString(i),eParamsOut[i]);
        aNBT.setTag("eParamsOut",paramO);

        NBTTagCompound paramIs=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramIs.setByte(Integer.toString(i),eParamsInStatus[i]);
        aNBT.setTag("eParamsInS",paramIs);

        NBTTagCompound paramOs=new NBTTagCompound();
        for(int i=0;i<20;i++)
            paramOs.setByte(Integer.toString(i),eParamsOutStatus[i]);
        aNBT.setTag("eParamsOutS",paramOs);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        ampereRating=aNBT.getInteger("eRating");
        eCertainMode=aNBT.getByte("eCertainM");
        eCertainStatus=aNBT.getByte("eCertainS");
        eParameters=!aNBT.getBoolean("eParam");
        ePowerPass=aNBT.getBoolean("ePass");
        eSafeVoid=aNBT.getBoolean("eVoid");

        //Fix supermethod shit.
        mOutputItems = new ItemStack[aNBT.getInteger("eItemsOut")];
        for (int i = 0; i < mOutputItems.length; i++)
            mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
        mOutputFluids = new FluidStack[aNBT.getInteger("eFluidsOut")];
        for (int i = 0; i < mOutputFluids.length; i++)
            mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);

        final int outputLen=aNBT.getInteger("outputStackCount");
        if(outputLen>0){
            outputEM=new cElementalInstanceStackTree[outputLen];
            for(int i=0;i<outputEM.length;i++)
                try {
                    outputEM[i] = cElementalInstanceStackTree.fromNBT(
                            aNBT.getCompoundTag("outputEM").getCompoundTag(Integer.toString(i)));
                }catch (tElementalException e){
                    if(DEBUGMODE)e.printStackTrace();
                    outputEM[i] = new cElementalInstanceStackTree();
                }
        }else outputEM=new cElementalInstanceStackTree[0];

        NBTTagCompound paramI=aNBT.getCompoundTag("eParamsIn");
        for(int i=0;i<eParamsIn.length;i++)
            eParamsIn[i]=paramI.getFloat(Integer.toString(i));

        NBTTagCompound paramO=aNBT.getCompoundTag("eParamsOut");
        for(int i=0;i<eParamsOut.length;i++)
            eParamsOut[i]=paramO.getFloat(Integer.toString(i));

        NBTTagCompound paramIs=aNBT.getCompoundTag("eParamsInS");
        for(int i=0;i<eParamsInStatus.length;i++)
            eParamsInStatus[i]=paramIs.getByte(Integer.toString(i));

        NBTTagCompound paramOs=aNBT.getCompoundTag("eParamsOutS");
        for(int i=0;i<eParamsOutStatus.length;i++)
            eParamsOutStatus[i]=paramOs.getByte(Integer.toString(i));
    }

    @Override
    public final long maxEUStore() {
        return getMaxInputVoltage()*maxAmps<<3;
    }

    @Override
    public final long getMinimumStoredEU() {
        return maxEUStore()>>1;
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

    @Override
    public final void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (--mUpdate == 0 || --mStartUpCheck == 0) {

                mInputHatches.clear();
                mInputBusses.clear();
                mOutputHatches.clear();
                mOutputBusses.clear();
                mDynamoHatches.clear();
                mEnergyHatches.clear();
                mMufflerHatches.clear();
                mMaintenanceHatches.clear();

                for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eOutputHatches)
                    hatch_elemental.id=-1;
                for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eInputHatches)
                    hatch_elemental.id=-1;
                for(GT_MetaTileEntity_Hatch_Uncertainty hatch:eUncertainHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
                for(GT_MetaTileEntity_Hatch_Param hatch:eParamHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);

                eUncertainHatches.clear();
                eEnergyMulti.clear();
                eInputHatches.clear();
                eOutputHatches.clear();
                eParamHatches.clear();
                eMufflerHatches.clear();
                eDynamoMulti.clear();

                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);
                if(eUncertainHatches.size()>1) mMachine=false;

                if(mMachine) {
                    short id=1;
                    for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eOutputHatches)
                        hatch_elemental.id=id++;
                    id=1;
                    for(GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental:eInputHatches)
                        hatch_elemental.id=id++;

                    maxAmps=mEnergyHatches.size();
                    for(GT_MetaTileEntity_Hatch_EnergyMulti hatch:eEnergyMulti)
                        maxAmps+=hatch.Amperes;

                    for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                        hatch.getBaseMetaTileEntity().setActive(true);
                    for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                        hatch.getBaseMetaTileEntity().setActive(true);
                }else{
                    maxAmps=0;
                }
            }

            if (mStartUpCheck < 0 ) {//E
                if (mMachine) {//S
                    final byte Tick=(byte)(aTick%20);
                    if (multiPurge1At==Tick || multiPurge2At==Tick)
                        purgeAll();
                    else if (multiCheckAt==Tick)
                        for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
                            if (isValidMetaTileEntity(tHatch)) {
                                if (disableMaintenance){
                                    mWrench = true;
                                    mScrewdriver = true;
                                    mSoftHammer = true;
                                    mHardHammer = true;
                                    mSolderingTool = true;
                                    mCrowbar = true;
                                } else {
                                    if (tHatch.mAuto && !(mWrench&&mScrewdriver&&mSoftHammer&&mHardHammer&&mSolderingTool&&mCrowbar))tHatch.isRecipeInputEqual(true);
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
                    else if(moveAt==Tick && eSafeVoid) {
                        for(GT_MetaTileEntity_Hatch_MufflerElemental voider:eMufflerHatches) {
                            if(voider.overflowMax<voider.overflowMatter) continue;
                            float remaining=voider.overflowMax-voider.overflowMatter;
                            for(GT_MetaTileEntity_Hatch_InputElemental in:eInputHatches){
                                for(cElementalInstanceStack instance:in.getContainerHandler().values()){
                                    int qty=(int)Math.floor(remaining/instance.definition.getMass());
                                    if(qty>0){
                                        qty=Math.min(qty,instance.amount);
                                        voider.overflowMatter+=instance.definition.getMass()*qty;
                                        in.getContainerHandler().removeAmount(false,new cElementalDefinitionStack(instance.definition,qty));
                                    }
                                }
                            }
                            for(GT_MetaTileEntity_Hatch_OutputElemental out:eOutputHatches){
                                for(cElementalInstanceStack instance:out.getContainerHandler().values()){
                                    int qty=(int)Math.floor(remaining/instance.definition.getMass());
                                    if(qty>0){
                                        qty=Math.min(qty,instance.amount);
                                        voider.overflowMatter+=instance.definition.getMass()*qty;
                                        out.getContainerHandler().removeAmount(false,new cElementalDefinitionStack(instance.definition,qty));
                                    }
                                }
                            }
                            //in case some weird shit happened here, it will still be safe
                            if(voider.overflowMatter>voider.overflowMax)voider.overflowMatter=voider.overflowMax;
                        }
                    }

                    if(ePowerPass && (mDynamoHatches.size()>0 || eDynamoMulti.size()>0)){
                        IGregTechTileEntity base=this.getBaseMetaTileEntity();
                        for(GT_MetaTileEntity_Hatch_Energy tHatch:mEnergyHatches){
                            if(this.getEUVar()>this.getMinimumStoredEU()) break;
                            if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tHatch.maxEUInput(),false))
                                this.setEUVar(this.getEUVar()+tHatch.maxEUInput());
                        }
                        for(GT_MetaTileEntity_Hatch_EnergyMulti tHatch:eEnergyMulti){
                            if(this.getEUVar()>this.getMinimumStoredEU()) break;
                            if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(tHatch.maxEUInput()*tHatch.Amperes,false))
                                this.setEUVar(this.getEUVar()+tHatch.maxEUInput()*tHatch.Amperes);
                        }
                        for(GT_MetaTileEntity_Hatch_Dynamo tHatch:mDynamoHatches)
                            if(     isValidMetaTileEntity(tHatch) &&
                                    tHatch.getBaseMetaTileEntity().getStoredEU()<=(tHatch.maxEUStore()-tHatch.maxEUOutput()) &&
                                    base.decreaseStoredEnergyUnits(tHatch.maxEUOutput(),false))
                                tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU()+tHatch.maxEUOutput());
                        for(GT_MetaTileEntity_Hatch_DynamoMulti tHatch:eDynamoMulti)
                            if(isValidMetaTileEntity(tHatch) &&
                                    tHatch.getBaseMetaTileEntity().getStoredEU()<=(tHatch.maxEUStore()-tHatch.maxEUOutput()*tHatch.Amperes) &&
                                    base.decreaseStoredEnergyUnits(tHatch.maxEUOutput()*tHatch.Amperes,false))
                                tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU()+tHatch.maxEUOutput()*tHatch.Amperes);
                    }

                    if (getRepairStatus() >= minRepairStatus) {//S
                        if (multiCheckAt==Tick)
                            paramsUpdate();

                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {//Start
                            if (onRunningTick(mInventory[1])) {//Compute EU
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1])))
                                    stopMachine();

                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime && recipeAt==Tick ) {//progress increase and done
                                    paramsUpdate();
                                    EM_outputFunction();
                                    purgeAll();

                                    if (mOutputItems != null) for (ItemStack tStack : mOutputItems)
                                        if (tStack != null)
                                            addOutput(tStack);

                                    if (mOutputFluids != null && mOutputFluids.length == 1)
                                        for (FluidStack tStack : mOutputFluids)
                                            if (tStack != null)
                                                addOutput(tStack);
                                    else if (mOutputFluids != null && mOutputFluids.length > 1)
                                        addFluidOutputs(mOutputFluids);

                                    mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                                    mOutputItems = null;
                                    mOutputFluids = null;
                                    outputEM=null;
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe(mInventory[1]);
                                }
                            }
                        } else {
                            if (recipeAt==Tick) {
                                if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe(mInventory[1]);
                                if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
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
        for(int i = 0; i < mOutputFluids2.length; ++i) {
            if(this.mOutputHatches.size() > i && this.mOutputHatches.get(i) != null && mOutputFluids2[i] != null && isValidMetaTileEntity((MetaTileEntity)this.mOutputHatches.get(i))) {
                ((GT_MetaTileEntity_Hatch_Output)this.mOutputHatches.get(i)).fill(mOutputFluids2[i], true);
            }
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getIdealStatus() {
        return 8;
    }

    @Override
    public int getRepairStatus() {
        return super.getRepairStatus() + ((eCertainStatus==0)?1:0) + (this.eParameters?1:0);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(ampereRating<=0) return true;
        else if(this.mEUt > 0) {
            this.addEnergyOutput((long)this.mEUt * (long) ampereRating * (long)this.mEfficiency / getMaxEfficiency(aStack));
            return true;
        } else if(this.mEUt < 0 && !this.drainEnergyInput((long)(-this.mEUt) * (long) ampereRating * getMaxEfficiency(aStack) / (long)Math.max(1000, this.mEfficiency))) {
            this.stopMachine();
            return false;
        } else return true;
    }

    @Override
    public final boolean addEnergyOutput(long aEU) {
        if(aEU <= 0L) {
            return true;
        } else {
            for(GT_MetaTileEntity_Hatch tHatch:eDynamoMulti)
                if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(aEU,false))
                    return true;
            for(GT_MetaTileEntity_Hatch tHatch:mDynamoHatches)
                if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(aEU,false))
                    return true;
            return false;
        }
    }

    @Override
    public final boolean drainEnergyInput(long aEU) {
        if(aEU <= 0L) {
            return true;
        } else {
            for(GT_MetaTileEntity_Hatch tHatch:eEnergyMulti)
                if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU,false))
                    return true;
            for(GT_MetaTileEntity_Hatch tHatch:mEnergyHatches)
                if(isValidMetaTileEntity(tHatch) && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(aEU,false))
                    return true;
            return false;
        }
    }

    @Override
    public final long getMaxInputVoltage() {
        long rVoltage = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti)
            if (isValidMetaTileEntity(tHatch)) rVoltage += tHatch.getBaseMetaTileEntity().getInputVoltage();
        return rVoltage;
    }

    @Override
    public final void stopMachine() {
        super.stopMachine();
        float mass=0;
        if(outputEM==null) return;
        for(cElementalInstanceStackTree tree:outputEM)
            mass+=tree.getMass();
        if(mass>0) {
            if (eMufflerHatches.size()<1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
        outputEM = null;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    private void purgeAll(){
        float mass=0;
        for(GT_MetaTileEntity_Hatch_InputElemental tHatch: eInputHatches) {
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
            mass+=tHatch.overflowMatter;
            tHatch.overflowMatter=0;
        }
        for(GT_MetaTileEntity_Hatch_OutputElemental tHatch: eOutputHatches) {
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
            mass+=tHatch.overflowMatter;
            tHatch.overflowMatter=0;
        }
        if(mass>0) {
            if (eMufflerHatches.size()<1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    public void cleanHatchContent(GT_MetaTileEntity_Hatch_ElementalContainer target){
        float mass=target.getContainerHandler().getMass();
        if(mass>0) {
            if (eMufflerHatches.size()<1) explodeMultiblock();
            mass /= eMufflerHatches.size();
            for (GT_MetaTileEntity_Hatch_MufflerElemental dump : eMufflerHatches) {
                dump.overflowMatter += mass;
                if (dump.overflowMatter > dump.overflowMax) explodeMultiblock();
            }
        }
    }

    @Override
    public final boolean checkRecipe(ItemStack itemStack){//do recipe checks, based on "machine content and state"
        paramsUpdate();
        return EM_checkRecipe(itemStack);
    }

    private void paramsUpdate(){
        for(GT_MetaTileEntity_Hatch_Param param:eParamHatches){
            int paramID=param.param;
            if(paramID<0)continue;
            eParamsIn[paramID]=param.value1f;
            eParamsIn[paramID+10]=param.value2f;
            param.input1f=eParamsOut[paramID];
            param.input2f=eParamsOut[paramID+10];
        }
        EM_checkParams();
        for(GT_MetaTileEntity_Hatch_Uncertainty uncertainty:eUncertainHatches){
            eCertainStatus=uncertainty.update(eCertainMode);
        }
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
        GT_Pollution.addPollution(new ChunkPosition(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()), 600000);
        mInventory[1] = null;
        for (MetaTileEntity tTileEntity : mInputBusses)         tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mOutputBusses)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mInputHatches)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mOutputHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mDynamoHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : mMufflerHatches)      tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : mEnergyHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : mMaintenanceHatches)  tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eParamHatches)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eInputHatches)        tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eOutputHatches)       tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eMufflerHatches)      tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eEnergyMulti)         tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        for (MetaTileEntity tTileEntity : eUncertainHatches)    tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        for (MetaTileEntity tTileEntity : eDynamoMulti)         tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
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
    public final boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
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
    public final boolean addClassicMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex){
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).mMachineBlock = (byte) aBaseCasingIndex;
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public String[] getInfoData() {//TODO Do it
        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for(GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                "Progress:",
                EnumChatFormatting.GREEN + Integer.toString(mProgresstime/20) + EnumChatFormatting.RESET +" s / "+
                        EnumChatFormatting.YELLOW + Integer.toString(mMaxProgresstime/20) + EnumChatFormatting.RESET +" s",
                "Stored Energy:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU",
                "Probably uses: "+
                        EnumChatFormatting.RED + Integer.toString(-mEUt) + EnumChatFormatting.RESET + " EU/t",
                "Maximum total power (to all Energy Hatches, not single ones): ",
                EnumChatFormatting.YELLOW+Long.toString(getMaxInputVoltage())+EnumChatFormatting.RESET+ " EU/t * 2A",
                "Problems: "+
                        EnumChatFormatting.RED+ (getIdealStatus() - getRepairStatus())+EnumChatFormatting.RESET+
                        " Efficiency: "+
                        EnumChatFormatting.YELLOW+Float.toString(mEfficiency / 100.0F)+EnumChatFormatting.RESET + " %",
                "PowerPass(tm): "+EnumChatFormatting.BLUE+ePowerPass,
                "SafeVoid(tm): "+EnumChatFormatting.BLUE+eSafeVoid
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    //Check Machine Structure based on string array array, ond offset of the controller
    public static boolean stuctureCheck(String[][] structure,//0-9 casing, +- air no air, a-z ignore
                                        Block[] blockType,//use numbers 0-9 for casing types
                                        byte[] blockMeta,//use numbers 0-9 for casing types
                                        int horizontalOffset, int verticalOffset, int depthOffset,
                                        IGregTechTileEntity aBaseMetaTileEntity){
        //TE Rotation
        byte facing=aBaseMetaTileEntity.getFrontFacing();

        int x,y,z,a,c;//b is y no matter what

        //perform your duties
        c=-depthOffset;
        for (String[] _structure:structure) {//front to back
            y=verticalOffset;
            for (String __structure : _structure) {//top to bottom
                a=-horizontalOffset;
                for (char block : __structure.toCharArray()) {//left to right
                    if(block>'`'){//small characters allow to skip check a-1 skip, b-2 skips etc.
                        a+=block-'`';
                    } else {
                        //get x y z from rotation
                        switch (facing) {//translation
                            case 4: x =  c; z =  a; break;
                            case 3: x =  a; z = -c; break;
                            case 5: x = -c; z = -a; break;
                            case 2: x = -a; z =  c; break;
                            default: return false;
                        }
                        //Check block
                        switch (block) {
                            case '-'://must be air
                                if (!aBaseMetaTileEntity.getAirOffset(x, y, z)) return false;
                                break;
                            case '+'://must not be air
                                if (aBaseMetaTileEntity.getAirOffset(x, y, z)) return false;
                                break;
                            default: {//check for block (countable)
                                int pointer = block - '0';
                                //countable air -> net.minecraft.block.BlockAir
                                if (aBaseMetaTileEntity.getBlockOffset(x, y, z) != blockType[pointer])  return false;
                                    //System.out.println("Keked1:"+x+" "+y+" "+z+"/"+a+" "+c+"/"+aBaseMetaTileEntity.getBlockOffset (x,y,z)+" "+blockType[pointer]);
                                if (aBaseMetaTileEntity.getMetaIDOffset(x, y, z) != blockMeta[pointer]) return false;
                                    //System.out.println("Keked2:"+x+" "+y+" "+z+"/"+a+" "+c+"/"+aBaseMetaTileEntity.getMetaIDOffset(x,y,z)+" "+blockMeta[pointer]);
                            }
                        }
                        a++;//block in horizontal layer
                    }
                }
                y--;//horizontal layer
            }
            c++;//depth
        }
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Nothing special just override me."
        };
    }

    @Override
    public void onRemoval() {
        try {
            if(eOutputHatches!=null) {
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches)
                    hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches)
                    hatch_elemental.id = -1;
                for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
                for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches)
                    hatch.getBaseMetaTileEntity().setActive(false);
            }
            if(outputEM!=null) {
                for (cElementalInstanceStackTree output : outputEM) {
                    if (output.hasStacks()) {
                        getBaseMetaTileEntity().doExplosion(V[15]);
                        return;
                    }
                }
            }
        }catch (Exception e){
            if(DEBUGMODE) e.printStackTrace();
        }
    }
}
