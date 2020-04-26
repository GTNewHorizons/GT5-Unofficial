package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.alignment.AlignmentLimits;
import com.github.technus.tectech.mechanics.alignment.AlignmentMessage;
import com.github.technus.tectech.mechanics.alignment.IAlignment;
import com.github.technus.tectech.mechanics.alignment.IAlignmentLimits;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.alignment.enumerable.Flip;
import com.github.technus.tectech.mechanics.alignment.enumerable.Rotation;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.tElementalException;
import com.github.technus.tectech.mechanics.structure.IHatchAdder;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.Structure;
import com.github.technus.tectech.thing.metaTileEntity.hatch.*;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.Util;
import com.github.technus.tectech.util.Vec3Impl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.util.CommonValues.*;
import static com.github.technus.tectech.util.Util.getTier;

/**
 * Created by danie_000 on 27.10.2016.
 */
public abstract class GT_MetaTileEntity_MultiblockBase_EM extends GT_MetaTileEntity_MultiBlockBase implements IAlignment {
    //region Client side variables (static - one per class)

    //Front icon holders - static so it is default one for my blocks
    //just add new static ones in your class and and override getTexture
    protected static Textures.BlockIcons.CustomIcon ScreenOFF;
    protected static Textures.BlockIcons.CustomIcon ScreenON;

    //Sound resource - same as with screen but override getActivitySound
    public final static ResourceLocation activitySound = new ResourceLocation(Reference.MODID + ":fx_lo_freq");
    @SideOnly(Side.CLIENT)
    private SoundLoop activitySoundLoop;
    //endregion

    //region HATCHES ARRAYS - they hold info about found hatches, add hatches to them... (auto structure magic does it tho)

    //HATCHES!!!, should be added and removed in check machine

    //EM in/out
    protected ArrayList<GT_MetaTileEntity_Hatch_InputElemental> eInputHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_OutputElemental> eOutputHatches = new ArrayList<>();
    //EM overflow output
    protected ArrayList<GT_MetaTileEntity_Hatch_OverflowElemental> eMufflerHatches = new ArrayList<>();
    //extra hatches
    protected ArrayList<GT_MetaTileEntity_Hatch_Param> eParamHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_Uncertainty> eUncertainHatches = new ArrayList<>();
    //multi amp hatches in/out
    protected ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> eEnergyMulti = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> eDynamoMulti = new ArrayList<>();
    //data hatches
    protected ArrayList<GT_MetaTileEntity_Hatch_InputData> eInputData = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_OutputData> eOutputData = new ArrayList<>();

    //region parameters
    public final Parameters parametrization;
    //endregion

    //region Control variables

    //should explode on dismatle?, set it in constructor, if true machine will explode if invalidated structure while active
    protected boolean eDismantleBoom = false;

    //what is the amount of A required
    public long eAmpereFlow = 1; // analogue of EU/t but for amperes used (so eu/t is actually eu*A/t) USE ONLY POSITIVE NUMBERS!

    //set to what you need it to be in check recipe
    //data required to operate
    protected long eRequiredData = 0;

    //storage for output EM that will be auto handled in case of failure to finish recipe
    //if you succed to use a recipe - be sure to output EM from outputEM to hatches in the output method
    protected cElementalInstanceStackMap[] outputEM;

    //are parameters correct - change in check recipe/output/update params etc. (maintenance status boolean)
    protected boolean eParameters = true;

    //what type of certainty inconvenience is used - can be used as in Computer - more info in uncertainty hatch
    protected byte eCertainMode = 0, eCertainStatus = 0;

    //minimal repair status to make the machine even usable (how much unfixed fixed stuff is needed)
    //if u need to force some things to be fixed - u might need to override doRandomMaintenanceDamage
    protected byte minRepairStatus = 3;

    //functionality toggles - changed by buttons in gui also
    public boolean ePowerPass = false, eSafeVoid = false;

    //endregion

    //region READ ONLY unless u really need to change it

    //max amperes machine can take in after computing it to the lowest tier (exchange packets to min tier count)
    protected long eMaxAmpereFlow = 0, eMaxAmpereGen = 0;

    //What is the max and minimal tier of eu hatches installed
    private long maxEUinputMin = 0, maxEUinputMax = 0, maxEUoutputMin = 0, maxEUoutputMax = 0;

    //read only unless you are making computation generator - read computer class
    protected long eAvailableData = 0; // data being available

    //just some info - private so hidden
    private boolean explodedThisTick = false;

    //front rotation val
    private IAlignmentLimits alignmentLimits = AlignmentLimits.UNLIMITED;
    private ExtendedFacing extendedFacing = ExtendedFacing.DEFAULT;
    //endregion

    protected GT_MetaTileEntity_MultiblockBase_EM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        parametrization = new Parameters(this);
        parametersInstantiation_EM();
        parametrization.setToDefaults(true, true);
    }

    protected GT_MetaTileEntity_MultiblockBase_EM(String aName) {
        super(aName);
        parametrization = new Parameters(this);
        parametersInstantiation_EM();
        parametrization.setToDefaults(true, true);
    }

    //region SUPER STRUCT
    @Override
    public ExtendedFacing getExtendedFacing() {
        return extendedFacing;
    }

    @Override
    public void setExtendedFacing(ExtendedFacing alignment) {
        if(extendedFacing!=alignment){
            extendedFacing=alignment;
            IGregTechTileEntity base = getBaseMetaTileEntity();
            if (getBaseMetaTileEntity().isServerSide()) {
                NetworkDispatcher.INSTANCE.sendToAllAround(new AlignmentMessage.AlignmentData(this),
                        base.getWorld().provider.dimensionId,
                        base.getXCoord(), base.getYCoord(), base.getZCoord(), 512);
            }else{
                base.issueTextureUpdate();
            }
        }
    }

    @Override
    public IAlignmentLimits getAlignmentLimits() {
        return alignmentLimits;
    }

    @Override
    public void setAlignmentLimits(IAlignmentLimits limits) {
        alignmentLimits=limits;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return canSetToDirectionAny(ForgeDirection.getOrientation(aFacing));
    }

    @Override
    public void onFacingChange() {
        toolSetDirection(ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()));
    }

    /**
     * Gets structure
     * @return STATIC INSTANCE OF STRUCTURE
     */
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM(){
        throw new NoSuchMethodError("Implement it as STATIC INSTANCE");
    }

    @SuppressWarnings("unchecked")
    private IStructureDefinition<GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM_Internal(){
        return (IStructureDefinition<GT_MetaTileEntity_MultiblockBase_EM>)getStructure_EM();
    }

    public final boolean structureCheck_EM(String piece,int horizontalOffset, int verticalOffset, int depthOffset) {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        return getStructure_EM_Internal().check(this,piece, baseMetaTileEntity.getWorld(),getExtendedFacing(),
                baseMetaTileEntity.getXCoord(),baseMetaTileEntity.getYCoord(),baseMetaTileEntity.getZCoord(),
                horizontalOffset,verticalOffset,depthOffset,!mMachine);
    }

    public final boolean structureBuild_EM(String piece,int horizontalOffset, int verticalOffset, int depthOffset,boolean hintsOnly) {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        return getStructure_EM_Internal().buildOrHints(this,piece, baseMetaTileEntity.getWorld(),getExtendedFacing(),
                baseMetaTileEntity.getXCoord(),baseMetaTileEntity.getYCoord(),baseMetaTileEntity.getZCoord(),
                horizontalOffset,verticalOffset,depthOffset,hintsOnly);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public final <T extends GT_MetaTileEntity_MultiblockBase_EM> boolean structureCheck_EM(
            String[][] structure,//0-9 casing, +- air no air, a-z ignore
            Block[] blockType,//use numbers 0-9 for casing types
            byte[] blockMeta,//use numbers 0-9 for casing types
            IHatchAdder<T>[] addingMethods,
            short[] casingTextures,
            Block[] blockTypeFallback,//use numbers 0-9 for casing types
            byte[] blockMetaFallback,//use numbers 0-9 for casing types
            int horizontalOffset, int verticalOffset, int depthOffset) {
        return Structure.checker(structure, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback,
                horizontalOffset, verticalOffset, depthOffset, (T)this, getExtendedFacing(), !mMachine);
    }

    //endregion

    //region METHODS TO OVERRIDE - general functionality, recipe check, output

    /**
     * Check structure here, also add hatches
     *
     * @param iGregTechTileEntity - the tile entity
     * @param itemStack           - what is in the controller input slot
     * @return is structure valid
     */
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return false;
    }

    /**
     * Checks Recipes (when all machine is complete and can work)
     * <p>
     * can get/set Parameters here also
     *
     * @param itemStack item in the controller
     * @return is recipe is valid
     */
    public boolean checkRecipe_EM(ItemStack itemStack) {
        return false;
    }

    /**
     * Put EM stuff from outputEM into EM output hatches here
     * or do other stuff - it is basically on recipe succeded
     * <p>
     * based on "machine state" do output,
     * this must move to outputEM to EM output hatches
     * and can also modify output items/fluids/EM, remaining EM is NOT overflowed.
     * (Well it can be overflowed if machine didn't finished, soft-hammered/disabled/not enough EU)
     * Setting available data processing
     */
    public void outputAfterRecipe_EM() {
    }

    /**
     * to add fluids into hatches
     *
     * @param mOutputFluids
     */
    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids) {
        int min = mOutputFluids.length > mOutputHatches.size() ? mOutputHatches.size() : mOutputFluids.length;
        for (int i = 0; i < min; ++i) {
            if (mOutputHatches.get(i) != null && mOutputFluids[i] != null && GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(mOutputHatches.get(i))) {
                mOutputHatches.get(i).fill(mOutputFluids[i], true);
            }
        }
    }
    //endregion

    //region tooltip and scanner result

    /**
     * @param hatchNo
     * @param paramID
     * @return
     */
    public ArrayList<String> getFullLedDescriptionIn(int hatchNo, int paramID) {
        ArrayList<String> list = new ArrayList<>();
        list.add(EnumChatFormatting.WHITE + "ID: " +
                EnumChatFormatting.AQUA + hatchNo +
                EnumChatFormatting.YELLOW + ":" +
                EnumChatFormatting.AQUA + paramID +
                EnumChatFormatting.YELLOW + ":" +
                EnumChatFormatting.AQUA + "I  " + parametrization.getStatusIn(hatchNo, paramID).name.get());
        list.add(EnumChatFormatting.WHITE + "Value: " +
                EnumChatFormatting.AQUA + Util.doubleToString(parametrization.getIn(hatchNo, paramID)));
        try {
            list.add(parametrization.groups[hatchNo].parameterIn[paramID].getBrief());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            list.add("Unused");
        }
        return list;
    }

    /**
     * @param hatchNo
     * @param paramID
     * @return
     */
    public ArrayList<String> getFullLedDescriptionOut(int hatchNo, int paramID) {
        ArrayList<String> list = new ArrayList<>();
        list.add(EnumChatFormatting.WHITE + "ID: " +
                EnumChatFormatting.AQUA + hatchNo +
                EnumChatFormatting.YELLOW + ":" +
                EnumChatFormatting.AQUA + paramID +
                EnumChatFormatting.YELLOW + ":" +
                EnumChatFormatting.AQUA + "O " + parametrization.getStatusOut(hatchNo, paramID).name.get());
        list.add(EnumChatFormatting.WHITE + "Value: " +
                EnumChatFormatting.AQUA + Util.doubleToString(parametrization.getOut(hatchNo, paramID)));
        try {
            list.add(parametrization.groups[hatchNo].parameterOut[paramID].getBrief());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            list.add("Unused");
        }
        return list;
    }

    /**
     * TOOLTIP
     *
     * @return strings in tooltip
     */
    @Override
    public String[] getDescription() {
        return new String[]{
                TEC_MARK_GENERAL,
                "Nothing special just override me."
        };
    }

    /**
     * scanner gives it
     *
     * @return
     */
    @Override
    public String[] getInfoData() {//TODO Do it
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                "Progress:",
                EnumChatFormatting.GREEN + Integer.toString(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + mMaxProgresstime / 20 + EnumChatFormatting.RESET + " s",
                "Energy Hatches:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + maxEnergy + EnumChatFormatting.RESET + " EU",
                (mEUt * eAmpereFlow <= 0 ? "Probably uses: " : "Probably makes: ") +
                        EnumChatFormatting.RED + Math.abs(mEUt) + EnumChatFormatting.RESET + " EU/t at " +
                        EnumChatFormatting.RED + eAmpereFlow + EnumChatFormatting.RESET + " A",
                "Tier Rating: " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier_EM()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier_EM()] + EnumChatFormatting.RESET +
                        " Amp Rating: " + EnumChatFormatting.GREEN + eMaxAmpereFlow + EnumChatFormatting.RESET + " A",
                "Problems: " + EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " Efficiency: " + EnumChatFormatting.YELLOW + mEfficiency / 100.0F + EnumChatFormatting.RESET + " %",
                "PowerPass: " + EnumChatFormatting.BLUE + ePowerPass + EnumChatFormatting.RESET +
                        " SafeVoid: " + EnumChatFormatting.BLUE + eSafeVoid,
                "Computation: " + EnumChatFormatting.GREEN + eAvailableData + EnumChatFormatting.RESET + " / " + EnumChatFormatting.YELLOW + eRequiredData + EnumChatFormatting.RESET
        };
    }

    /**
     * should it work with scanner? HELL YES
     *
     * @return
     */
    @Override
    public boolean isGivingInformation() {
        return true;
    }

    //endregion

    //region GUI/SOUND/RENDER

    /**
     * Server side container
     *
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     * @return
     */
    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity);
    }

    /**
     * Client side gui
     *
     * @param aID
     * @param aPlayerInventory
     * @param aBaseMetaTileEntity
     * @return
     */
    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png");
    }


    /**
     * add more textures
     *
     * @param aBlockIconRegister
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_CONTROLLER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    /**
     * actually use textures
     *
     * @param aBaseMetaTileEntity
     * @param aSide
     * @param aFacing
     * @param aColorIndex
     * @param aActive
     * @param aRedstone
     * @return
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4]};
    }

    /**
     * should return your activity sound
     *
     * @return
     */
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return activitySound;
    }

    /**
     * plays the sounds auto magically
     *
     * @param activitySound
     */
    @SideOnly(Side.CLIENT)
    protected void soundMagic(ResourceLocation activitySound) {
        if (getBaseMetaTileEntity().isActive()) {
            if (activitySoundLoop == null) {
                activitySoundLoop = new SoundLoop(activitySound, getBaseMetaTileEntity(), false, true);
                Minecraft.getMinecraft().getSoundHandler().playSound(activitySoundLoop);
            }
        } else {
            if (activitySoundLoop != null) {
                activitySoundLoop = null;
            }
        }
    }

    //endregion

    //region Methods to maybe override (if u implement certain stuff)

    /**
     * is the thing inside controller a valid item to make the machine work
     *
     * @param itemStack
     * @return
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    /**
     * how much damage to apply to thing in controller - not sure how it does it
     *
     * @param itemStack
     * @return
     */
    @Override
    public int getDamageToComponent(ItemStack itemStack) {
        return 0;
    }

    /**
     * called when removing from map - not when unloading? //todo check
     */
    @Override
    public void onRemoval() {
        try {
            if (eOutputHatches != null) {
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches) {
                    hatch_elemental.id = -1;
                }
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches) {
                    hatch_elemental.id = -1;
                }
                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eOutputData) {
                    hatch_data.id = -1;
                    hatch_data.q = null;
                }
                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eInputData) {
                    hatch_data.id = -1;
                }
                for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches) {
                    hatch.getBaseMetaTileEntity().setActive(false);
                }
                for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                    hatch.getBaseMetaTileEntity().setActive(false);
                }
            }
            cleanOutputEM_EM();
            if (ePowerPass && getEUVar() > V[3] || eDismantleBoom && mMaxProgresstime > 0 && areChunksAroundLoaded_EM()) {
                explodeMultiblock();
            }
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }

    /**
     * prevents spontaneous explosions when the chunks unloading would cause them
     * should cover 3 chunks radius
     *
     * @return
     */
    protected boolean areChunksAroundLoaded_EM() {
        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(this) && getBaseMetaTileEntity().isServerSide()) {
            IGregTechTileEntity base = getBaseMetaTileEntity();
            return base.getWorld().doChunksNearChunkExist(base.getXCoord(), base.getYCoord(), base.getZCoord(), 3);
            //todo check if it is actually checking if chunks are loaded
        } else {
            return false;
        }
    }

    /**
     * instantiate parameters in CONSTRUCTOR! CALLED ONCE on creation, don't call it in your classes
     */
    protected void parametersInstantiation_EM() {
    }

    /**
     * It is automatically called OFTEN
     * update status of parameters in guis (and "machine state" if u wish)
     * Called before check recipe, before outputting, and every second the machine is complete
     * <p>
     * good place to update parameter statuses, default implementation handles it well
     *
     * @param machineBusy is machine doing SHIT
     */
    protected void parametersStatusesWrite_EM(boolean machineBusy) {//todo unimplement?
        if (!machineBusy) {
            for (Parameters.Group.ParameterIn parameterIn : parametrization.parameterInArrayList) {
                if (parameterIn != null) {
                    parameterIn.updateStatus();
                }
            }
        } else {
            for (Parameters.Group hatch : parametrization.groups) {
                if (hatch != null && hatch.updateWhileRunning) {
                    for (Parameters.Group.ParameterIn in : hatch.parameterIn) {
                        if (in != null) {
                            in.updateStatus();
                        }
                    }
                }
            }
        }
        for (Parameters.Group.ParameterOut parameterOut : parametrization.parameterOutArrayList) {
            if (parameterOut != null) {
                parameterOut.updateStatus();
            }
        }
    }

    /**
     * For extra types of hatches initiation, LOOK HOW IT IS CALLED! in onPostTick
     *
     * @param mMachine was the machine considered complete at that point in onPostTick
     */
    protected void hatchInit_EM(boolean mMachine) {
    }

    /**
     * called when the multiblock is exploding - if u want to add more EXPLOSIONS, for ex. new types of hatches also have to explode
     */
    protected void extraExplosions_EM() {
    }//For that extra hatches explosions, and maybe some MOORE EXPLOSIONS

    /**
     * Get Available data, Override only on data outputters should return mAvailableData that is set in check recipe
     *
     * @return available data
     */
    protected long getAvailableData_EM() {
        long result = 0;
        Vec3Impl pos = new Vec3Impl(getBaseMetaTileEntity());
        for (GT_MetaTileEntity_Hatch_InputData in : eInputData) {
            if (in.q != null) {
                Long value = in.q.contentIfNotInTrace(pos);
                if (value != null) {
                    result += value;
                }
            }
        }
        return result;
    }

    /**
     * Extra hook on cyclic updates (not really needed for machines smaller than 1 chunk)
     * BUT NEEDED WHEN - machine blocks are not touching each other or they don't implement IMachineBlockUpdateable (ex. air,stone,weird TE's)
     */
    protected boolean cyclicUpdate_EM() {
        return mUpdate <= -1000;//set to false to disable cyclic update
        //default is once per 50s; mUpdate is decremented every tick
    }

    /**
     * get pollution per tick
     *
     * @param itemStack what is in controller
     * @return how much pollution is produced
     */
    @Override
    public int getPollutionPerTick(ItemStack itemStack) {
        return 0;
    }

    /**
     * EM pollution per tick
     *
     * @param itemStack - item in controller
     * @return how much excess matter is there
     */
    public float getExcessMassPerTick_EM(ItemStack itemStack) {
        return 0f;
    }

    /**
     * triggered if machine is not allowed to work after completing a recipe, override to make it not shutdown for instance (like turbines).
     * bu just replacing it with blank - active transformer is doing it
     * <p>
     * CALLED DIRECTLY when soft hammered to offline state - usually should stop the machine unless some other mechanics should do it
     */
    protected void notAllowedToWork_stopMachine_EM() {
        stopMachine();
    }

    /**
     * store data
     *
     * @param aNBT
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eMaxGenEUmin", maxEUoutputMin);
        aNBT.setLong("eMaxGenEUmax", maxEUoutputMax);
        aNBT.setLong("eGenRating", eMaxAmpereGen);
        aNBT.setLong("eMaxEUmin", maxEUinputMin);
        aNBT.setLong("eMaxEUmax", maxEUinputMax);
        aNBT.setLong("eRating", eAmpereFlow);
        aNBT.setLong("eMaxA", eMaxAmpereFlow);
        aNBT.setLong("eDataR", eRequiredData);
        aNBT.setLong("eDataA", eAvailableData);
        aNBT.setByte("eCertainM", eCertainMode);
        aNBT.setByte("eCertainS", eCertainStatus);
        aNBT.setByte("eMinRepair", minRepairStatus);
        aNBT.setByte("eRotation", (byte)extendedFacing.getRotation().getIndex());
        aNBT.setByte("eFlip", (byte)extendedFacing.getFlip().getIndex());
        aNBT.setBoolean("eParam", eParameters);
        aNBT.setBoolean("ePass", ePowerPass);
        aNBT.setBoolean("eVoid", eSafeVoid);
        aNBT.setBoolean("eBoom", eDismantleBoom);
        aNBT.setBoolean("eOK", mMachine);

        //Ensures compatibility
        if (mOutputItems != null) {
            aNBT.setInteger("mOutputItemsLength", mOutputItems.length);
            for (int i = 0; i < mOutputItems.length; i++) {
                if (mOutputItems[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    mOutputItems[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputItem" + i, tNBT);
                }
            }
        }

        //Ensures compatibility
        if (mOutputFluids != null) {
            aNBT.setInteger("mOutputFluidsLength", mOutputFluids.length);
            for (int i = 0; i < mOutputFluids.length; i++) {
                if (mOutputFluids[i] != null) {
                    NBTTagCompound tNBT = new NBTTagCompound();
                    mOutputFluids[i].writeToNBT(tNBT);
                    aNBT.setTag("mOutputFluids" + i, tNBT);
                }
            }
        }

        if (outputEM != null) {
            aNBT.setInteger("eOutputStackCount", outputEM.length);
            NBTTagCompound output = new NBTTagCompound();
            for (int i = 0; i < outputEM.length; i++) {
                if (outputEM[i] != null) {
                    output.setTag(Integer.toString(i), outputEM[i].toNBT());
                }
            }
            aNBT.setTag("outputEM", output);
        } else {
            aNBT.setInteger("eOutputStackCount", 0);
            aNBT.removeTag("outputEM");
        }

        NBTTagCompound paramI = new NBTTagCompound();
        for (int i = 0; i < parametrization.iParamsIn.length; i++) {
            paramI.setDouble(Integer.toString(i), parametrization.iParamsIn[i]);
        }
        aNBT.setTag("eParamsInD", paramI);

        NBTTagCompound paramO = new NBTTagCompound();
        for (int i = 0; i < parametrization.iParamsOut.length; i++) {
            paramO.setDouble(Integer.toString(i), parametrization.iParamsOut[i]);
        }
        aNBT.setTag("eParamsOutD", paramO);

        NBTTagCompound paramIs = new NBTTagCompound();
        for (int i = 0; i < parametrization.eParamsInStatus.length; i++) {
            paramIs.setByte(Integer.toString(i), parametrization.eParamsInStatus[i].getOrdinalByte());
        }
        aNBT.setTag("eParamsInS", paramIs);

        NBTTagCompound paramOs = new NBTTagCompound();
        for (int i = 0; i < parametrization.eParamsOutStatus.length; i++) {
            paramOs.setByte(Integer.toString(i), parametrization.eParamsOutStatus[i].getOrdinalByte());
        }
        aNBT.setTag("eParamsOutS", paramOs);
    }

    /**
     * load data
     *
     * @param aNBT
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        maxEUoutputMin = aNBT.getLong("eMaxGenEUmin");
        maxEUoutputMax = aNBT.getLong("eMaxGenEUmax");
        eMaxAmpereGen = aNBT.getLong("eGenRating");
        maxEUinputMin = aNBT.getLong("eMaxEUmin");
        maxEUinputMax = aNBT.getLong("eMaxEUmax");
        eAmpereFlow = aNBT.getLong("eRating");
        eMaxAmpereFlow = aNBT.getLong("eMaxA");
        eRequiredData = aNBT.getLong("eDataR");
        eAvailableData = aNBT.getLong("eDataA");
        eCertainMode = aNBT.getByte("eCertainM");
        eCertainStatus = aNBT.getByte("eCertainS");
        minRepairStatus = aNBT.getByte("eMinRepair");
        extendedFacing=ExtendedFacing.of(
                ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()),
                Rotation.byIndex(aNBT.getByte("eRotation")),
                Flip.byIndex(aNBT.getByte("eFlip")));
        eParameters = aNBT.getBoolean("eParam");
        ePowerPass = aNBT.getBoolean("ePass");
        eSafeVoid = aNBT.getBoolean("eVoid");
        eDismantleBoom = aNBT.getBoolean("eBoom");
        mMachine = aNBT.getBoolean("eOK");

        //Ensures compatibility
        int aOutputItemsLength = aNBT.getInteger("mOutputItemsLength");
        if (aOutputItemsLength > 0) {
            mOutputItems = new ItemStack[aOutputItemsLength];
            for (int i = 0; i < mOutputItems.length; i++) {
                mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
            }
        }

        //Ensures compatibility
        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++) {
                mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
            }
        }

        int outputLen = aNBT.getInteger("eOutputStackCount");
        if (outputLen > 0) {
            outputEM = new cElementalInstanceStackMap[outputLen];
            NBTTagCompound compound = aNBT.getCompoundTag("outputEM");
            for (int i = 0; i < outputEM.length; i++) {
                if (compound.hasKey(Integer.toString(i))) {
                    try {
                        outputEM[i] = cElementalInstanceStackMap.fromNBT(compound.getCompoundTag(Integer.toString(i)));
                    } catch (tElementalException e) {
                        if (DEBUG_MODE) {
                            e.printStackTrace();
                        }
                        outputEM[i] = null;
                    }
                }
            }
        } else {
            outputEM = null;
        }

        if (aNBT.hasKey("eParamsIn") && aNBT.hasKey("eParamsOut") && aNBT.hasKey("eParamsB")) {
            NBTTagCompound paramI = aNBT.getCompoundTag("eParamsIn");
            NBTTagCompound paramO = aNBT.getCompoundTag("eParamsOut");
            NBTTagCompound paramB = aNBT.getCompoundTag("eParamsB");
            for (int i = 0; i < 10; i++) {
                if (paramB.getBoolean(Integer.toString(i))) {
                    parametrization.iParamsIn[i] = Float.intBitsToFloat(paramI.getInteger(Integer.toString(i)));
                    parametrization.iParamsOut[i] = Float.intBitsToFloat(paramO.getInteger(Integer.toString(i)));
                } else {
                    parametrization.iParamsIn[i] = paramI.getInteger(Integer.toString(i));
                    parametrization.iParamsOut[i] = paramO.getInteger(Integer.toString(i));
                }
            }
        } else {
            NBTTagCompound paramI = aNBT.getCompoundTag("eParamsInD");
            for (int i = 0; i < parametrization.iParamsIn.length; i++) {
                parametrization.iParamsIn[i] = paramI.getDouble(Integer.toString(i));
            }
            NBTTagCompound paramO = aNBT.getCompoundTag("eParamsOutD");
            for (int i = 0; i < parametrization.iParamsOut.length; i++) {
                parametrization.iParamsOut[i] = paramO.getDouble(Integer.toString(i));
            }
        }

        NBTTagCompound paramIs = aNBT.getCompoundTag("eParamsInS");
        for (int i = 0; i < parametrization.eParamsInStatus.length; i++) {
            parametrization.eParamsInStatus[i] = LedStatus.getStatus(paramIs.getByte(Integer.toString(i)));
        }

        NBTTagCompound paramOs = aNBT.getCompoundTag("eParamsOutS");
        for (int i = 0; i < parametrization.eParamsOutStatus.length; i++) {
            parametrization.eParamsOutStatus[i] = LedStatus.getStatus(paramOs.getByte(Integer.toString(i)));
        }
    }

    /**
     * if u want to use gt recipe maps...
     *
     * @return
     */
    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    /**
     * does some validation and cleaning, dont touch i think
     */
    @Override
    public void updateSlots() {
        super.updateSlots();
        purgeAllOverflowEM_EM();
    }

    //endregion

    //region RATHER LEAVE ALONE Section

    /**
     * Override if needed but usually call super method at start!
     * On machine stop - NOT called directly when soft hammered to offline state! - it SHOULD cause a full stop like power failure does
     */
    @Override
    public void stopMachine() {
        cleanOrExplode();

        for (GT_MetaTileEntity_Hatch_OutputData data : eOutputData) {
            data.q = null;
        }

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

    /**
     * After recipe check failed
     * helper method so i don't have to set that params to nothing at all times
     */
    protected void afterRecipeCheckFailed() {
        cleanOrExplode();

        for (GT_MetaTileEntity_Hatch_OutputData data : eOutputData) {
            data.q = null;
        }

        mOutputItems = null;
        mOutputFluids = null;
        mEfficiency = 0;
        mEfficiencyIncrease = 0;
        mProgresstime = 0;
        mMaxProgresstime = 0;
        eAvailableData = 0;
        //getBaseMetaTileEntity().disableWorking(); //can add in override
        //hatchesStatusUpdate_EM(); //called always after recipe checks
    }

    private void cleanOrExplode() {
        if (outputEM != null) {
            float mass = 0;
            for (cElementalInstanceStackMap tree : outputEM) {
                if (tree != null) {
                    mass += tree.getMass();
                }
            }
            if (mass > 0) {
                if (eMufflerHatches.size() < 1) {
                    explodeMultiblock();
                } else {
                    mass /= eMufflerHatches.size();
                    for (GT_MetaTileEntity_Hatch_OverflowElemental dump : eMufflerHatches) {
                        if (dump.addOverflowMatter(mass)) {
                            explodeMultiblock();
                        }
                    }
                }
            }
            outputEM = null;
        }
    }

    /**
     * cyclic check even when not working, called LESS frequently
     *
     * @return
     */
    private boolean cyclicUpdate() {
        if (cyclicUpdate_EM()) {
            mUpdate = 0;
            return true;
        }
        return false;
    }

    /**
     * mining level...
     *
     * @return
     */
    @Override
    public byte getTileEntityBaseType() {
        return 3;
    }

    //endregion

    //region internal

    /**
     * internal check machine
     *
     * @param iGregTechTileEntity
     * @param itemStack
     * @return
     */
    @Override
    public final boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return checkMachine_EM(iGregTechTileEntity, itemStack);
    }

    /**
     * internal check recipe
     *
     * @param itemStack
     * @return
     */
    @Override
    public final boolean checkRecipe(ItemStack itemStack) {//do recipe checks, based on "machine content and state"
        hatchesStatusUpdate_EM();
        boolean result = checkRecipe_EM(itemStack);//if had no - set default params
        hatchesStatusUpdate_EM();
        return result;
    }

    /**
     * callback for updating parameters and new hatches
     */
    protected void hatchesStatusUpdate_EM() {
        if (getBaseMetaTileEntity().isClientSide()) {
            return;
        }
        boolean busy = mMaxProgresstime > 0;
        if (busy) {//write from buffer to hatches only
            for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch) || hatch.param < 0) {
                    continue;
                }
                int hatchId = hatch.param;
                if (parametrization.groups[hatchId] != null && parametrization.groups[hatchId].updateWhileRunning) {
                    parametrization.iParamsIn[hatchId] = hatch.value0D;
                    parametrization.iParamsIn[hatchId + 10] = hatch.value1D;
                }
                hatch.input0D = parametrization.iParamsOut[hatchId];
                hatch.input1D = parametrization.iParamsOut[hatchId + 10];
            }
        } else {//if has nothing to do update all
            for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                if (!GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch) || hatch.param < 0) {
                    continue;
                }
                int hatchId = hatch.param;
                parametrization.iParamsIn[hatchId] = hatch.value0D;
                parametrization.iParamsIn[hatchId + 10] = hatch.value1D;
                hatch.input0D = parametrization.iParamsOut[hatchId];
                hatch.input1D = parametrization.iParamsOut[hatchId + 10];
            }
        }
        for (GT_MetaTileEntity_Hatch_Uncertainty uncertainty : eUncertainHatches) {
            eCertainStatus = uncertainty.update(eCertainMode);
        }
        eAvailableData = getAvailableData_EM();
        parametersStatusesWrite_EM(busy);
    }

    @Deprecated
    public final int getAmountOfOutputs() {
        throw new NoSuchMethodError("Deprecated Do not use");
    }
    //endregion

    //region TICKING functions

    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
    }

    @Override
    public final void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        isFacingValid(aBaseMetaTileEntity.getFrontFacing());
        if (getBaseMetaTileEntity().isClientSide()) {
            NetworkDispatcher.INSTANCE.sendToServer(new AlignmentMessage.AlignmentQuery(this));
        }
        onFirstTick_EM(aBaseMetaTileEntity);
    }

    /**
     * called every tick the machines is active
     *
     * @param aStack
     * @return
     */
    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (eRequiredData > eAvailableData) {
            if (energyFlowOnRunningTick(aStack, false)) {
                stopMachine();
            }
            return false;
        }
        return energyFlowOnRunningTick(aStack, true);
    }

    /**
     * CAREFUL!!! it calls most of the callbacks, like everything else in here
     */
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            explodedThisTick = false;
            if (mEfficiency < 0) {
                mEfficiency = 0;
            }

            if (--mUpdate == 0 || --mStartUpCheck == 0 || cyclicUpdate() || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                mInputHatches.clear();
                mInputBusses.clear();
                mOutputHatches.clear();
                mOutputBusses.clear();
                mDynamoHatches.clear();
                mEnergyHatches.clear();
                mMufflerHatches.clear();
                mMaintenanceHatches.clear();

                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches) {
                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_elemental)) {
                        hatch_elemental.id = -1;
                    }
                }
                for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches) {
                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_elemental)) {
                        hatch_elemental.id = -1;
                    }
                }

                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eOutputData) {
                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_data)) {
                        hatch_data.id = -1;
                    }
                }
                for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eInputData) {
                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_data)) {
                        hatch_data.id = -1;
                    }
                }

                for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches) {
                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                        hatch.getBaseMetaTileEntity().setActive(false);
                    }
                }
                for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                        hatch.getBaseMetaTileEntity().setActive(false);
                    }
                }

                eUncertainHatches.clear();
                eEnergyMulti.clear();
                eInputHatches.clear();
                eOutputHatches.clear();
                eParamHatches.clear();
                eMufflerHatches.clear();
                eDynamoMulti.clear();
                eOutputData.clear();
                eInputData.clear();

                if (aBaseMetaTileEntity instanceof BaseTileEntity) {
                    ((BaseTileEntity) aBaseMetaTileEntity).ignoreUnloadedChunks = mMachine;
                }
                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);

                if (!mMachine) {
                    if (ePowerPass && getEUVar() > V[3] || eDismantleBoom && mMaxProgresstime > 0 && areChunksAroundLoaded_EM()) {
                        explodeMultiblock();
                    }
                    if (outputEM != null) {
                        for (cElementalInstanceStackMap tree : outputEM) {
                            if (tree != null && tree.hasStacks()) {
                                explodeMultiblock();
                            }
                        }
                    }
                }

                if (eUncertainHatches.size() > 1) {
                    mMachine = false;
                }

                if (mMachine) {
                    short id = 1;
                    for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eOutputHatches) {
                        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_elemental)) {
                            hatch_elemental.id = id++;
                        }
                    }
                    id = 1;
                    for (GT_MetaTileEntity_Hatch_ElementalContainer hatch_elemental : eInputHatches) {
                        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_elemental)) {
                            hatch_elemental.id = id++;
                        }
                    }

                    id = 1;
                    for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eOutputData) {
                        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_data)) {
                            hatch_data.id = id++;
                        }
                    }
                    id = 1;
                    for (GT_MetaTileEntity_Hatch_DataConnector hatch_data : eInputData) {
                        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch_data)) {
                            hatch_data.id = id++;
                        }
                    }

                    if (!mEnergyHatches.isEmpty() || !eEnergyMulti.isEmpty() || !mDynamoHatches.isEmpty() || !eDynamoMulti.isEmpty()) {
                        maxEUinputMin = V[15];
                        maxEUinputMax = V[0];
                        maxEUoutputMin = V[15];
                        maxEUoutputMax = V[0];
                        for (GT_MetaTileEntity_Hatch_Energy hatch : mEnergyHatches) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                if (hatch.maxEUInput() < maxEUinputMin) {
                                    maxEUinputMin = hatch.maxEUInput();
                                }
                                if (hatch.maxEUInput() > maxEUinputMax) {
                                    maxEUinputMax = hatch.maxEUInput();
                                }
                            }
                        }
                        for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : eEnergyMulti) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                if (hatch.maxEUInput() < maxEUinputMin) {
                                    maxEUinputMin = hatch.maxEUInput();
                                }
                                if (hatch.maxEUInput() > maxEUinputMax) {
                                    maxEUinputMax = hatch.maxEUInput();
                                }
                            }
                        }
                        for (GT_MetaTileEntity_Hatch_Dynamo hatch : mDynamoHatches) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                if (hatch.maxEUOutput() < maxEUoutputMin) {
                                    maxEUoutputMin = hatch.maxEUOutput();
                                }
                                if (hatch.maxEUOutput() > maxEUoutputMax) {
                                    maxEUoutputMax = hatch.maxEUOutput();
                                }
                            }
                        }
                        for (GT_MetaTileEntity_Hatch_DynamoMulti hatch : eDynamoMulti) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                if (hatch.maxEUOutput() < maxEUoutputMin) {
                                    maxEUoutputMin = hatch.maxEUOutput();
                                }
                                if (hatch.maxEUOutput() > maxEUoutputMax) {
                                    maxEUoutputMax = hatch.maxEUOutput();
                                }
                            }
                        }
                        eMaxAmpereFlow = 0;
                        eMaxAmpereGen = 0;
                        //counts only full amps
                        for (GT_MetaTileEntity_Hatch_Energy hatch : mEnergyHatches) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                eMaxAmpereFlow += hatch.maxEUInput() / maxEUinputMin;
                            }
                        }
                        for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : eEnergyMulti) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                eMaxAmpereFlow += hatch.maxEUInput() / maxEUinputMin * hatch.Amperes;
                            }
                        }
                        for (GT_MetaTileEntity_Hatch_Dynamo hatch : mDynamoHatches) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                eMaxAmpereGen += hatch.maxEUOutput() / maxEUoutputMin;
                            }
                        }
                        for (GT_MetaTileEntity_Hatch_DynamoMulti hatch : eDynamoMulti) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                                eMaxAmpereGen += hatch.maxEUOutput() / maxEUoutputMin * hatch.Amperes;
                            }
                        }
                    } else {
                        maxEUinputMin = 0;
                        maxEUinputMax = 0;
                        eMaxAmpereFlow = 0;
                        maxEUoutputMin = 0;
                        maxEUoutputMax = 0;
                        eMaxAmpereGen = 0;
                    }
                    if (getEUVar() > maxEUStore()) {
                        setEUVar(maxEUStore());
                    }

                    for (GT_MetaTileEntity_Hatch_Uncertainty hatch : eUncertainHatches) {
                        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                            hatch.getBaseMetaTileEntity().setActive(true);
                        }
                    }
                    for (GT_MetaTileEntity_Hatch_Param hatch : eParamHatches) {
                        if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(hatch)) {
                            hatch.getBaseMetaTileEntity().setActive(true);
                        }
                    }
                } else {
                    maxEUinputMin = 0;
                    maxEUinputMax = 0;
                    eMaxAmpereFlow = 0;
                    setEUVar(0);
                }
                hatchInit_EM(mMachine);
            }

            if (mStartUpCheck < 0) {//E
                if (mMachine) {//S
                    byte Tick = (byte) (aTick % 20);
                    if (MULTI_PURGE_1_AT == Tick || MULTI_PURGE_2_AT == Tick) {
                        purgeAllOverflowEM_EM();
                    } else if (MULTI_CHECK_AT == Tick) {
                        for (GT_MetaTileEntity_Hatch_Maintenance tHatch : mMaintenanceHatches) {
                            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                                if (GT_MetaTileEntity_MultiBlockBase.disableMaintenance) {
                                    mWrench = true;
                                    mScrewdriver = true;
                                    mSoftHammer = true;
                                    mHardHammer = true;
                                    mSolderingTool = true;
                                    mCrowbar = true;
                                } else {
                                    if (tHatch.mAuto && !(mWrench && mScrewdriver && mSoftHammer && mHardHammer && mSolderingTool && mCrowbar)) {
                                        tHatch.autoMaintainance();
                                    }
                                    if (tHatch.mWrench) {
                                        mWrench = true;
                                    }
                                    if (tHatch.mScrewdriver) {
                                        mScrewdriver = true;
                                    }
                                    if (tHatch.mSoftHammer) {
                                        mSoftHammer = true;
                                    }
                                    if (tHatch.mHardHammer) {
                                        mHardHammer = true;
                                    }
                                    if (tHatch.mSolderingTool) {
                                        mSolderingTool = true;
                                    }
                                    if (tHatch.mCrowbar) {
                                        mCrowbar = true;
                                    }

                                    tHatch.mWrench = false;
                                    tHatch.mScrewdriver = false;
                                    tHatch.mSoftHammer = false;
                                    tHatch.mHardHammer = false;
                                    tHatch.mSolderingTool = false;
                                    tHatch.mCrowbar = false;
                                }
                            }
                        }
                    } else if (MOVE_AT == Tick && eSafeVoid) {
                        for (GT_MetaTileEntity_Hatch_OverflowElemental voider : eMufflerHatches) {
                            if (voider.overflowMax < voider.getOverflowMatter()) {
                                continue;
                            }
                            float remaining = voider.overflowMax - voider.getOverflowMatter();
                            for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
                                for (cElementalInstanceStack instance : in.getContainerHandler().values()) {
                                    long qty = (long) Math.floor(remaining / instance.definition.getMass());
                                    if (qty > 0) {
                                        qty = Math.min(qty, instance.amount);
                                        if (voider.addOverflowMatter(instance.definition.getMass() * qty)) {
                                            voider.setOverflowMatter(voider.overflowMax);
                                        }
                                        in.getContainerHandler().removeAmount(false, new cElementalDefinitionStack(instance.definition, qty));
                                    }
                                }
                            }
                            for (GT_MetaTileEntity_Hatch_OutputElemental out : eOutputHatches) {
                                for (cElementalInstanceStack instance : out.getContainerHandler().values()) {
                                    long qty = (long) Math.floor(remaining / instance.definition.getMass());
                                    if (qty > 0) {
                                        qty = Math.min(qty, instance.amount);
                                        if (voider.addOverflowMatter(instance.definition.getMass() * qty)) {
                                            voider.setOverflowMatter(voider.overflowMax);
                                        }
                                        out.getContainerHandler().removeAmount(false, new cElementalDefinitionStack(instance.definition, qty));
                                    }
                                }
                            }
                        }
                    }

                    if (getRepairStatus() >= minRepairStatus) {//S
                        if (MULTI_CHECK_AT == Tick) {
                            hatchesStatusUpdate_EM();
                        }

                        //region power pass and controller charging
                        {//DO
                            long euVar;
                            for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
                                if (getEUVar() > getMinimumStoredEU()) {
                                    break;
                                }
                                if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                                    euVar = tHatch.maxEUInput();
                                    if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euVar, false)) {
                                        setEUVar(getEUVar() + euVar);
                                    }
                                }
                            }
                            for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
                                if (getEUVar() > getMinimumStoredEU()) {
                                    break;
                                }
                                if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                                    euVar = tHatch.maxEUInput() * tHatch.Amperes;
                                    if (tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(euVar, false)) {
                                        setEUVar(getEUVar() + euVar);
                                    }
                                }
                            }
                            if (ePowerPass && getEUVar() > getMinimumStoredEU()) {
                                for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
                                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                                        euVar = tHatch.maxEUOutput();
                                        if (tHatch.getBaseMetaTileEntity().getStoredEU() <= tHatch.maxEUStore() - euVar &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + Math.min(euVar >> 7, 1), false)) {
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                        }
                                    }
                                }
                                for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : eDynamoMulti) {
                                    if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                                        euVar = tHatch.maxEUOutput() * tHatch.Amperes;
                                        if (tHatch.getBaseMetaTileEntity().getStoredEU() <= tHatch.maxEUStore() - euVar &&
                                                aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + Math.min(euVar >> 7, tHatch.Amperes), false)) {
                                            tHatch.setEUVar(tHatch.getBaseMetaTileEntity().getStoredEU() + euVar);
                                        }
                                    }
                                }
                            }
                        }
                        //endregion

                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {//Start
                            if (onRunningTick(mInventory[1])) {//Compute EU
                                cleanMassEM_EM(getExcessMassPerTick_EM(mInventory[1]));
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
                                    stopMachine();
                                }

                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime && RECIPE_AT == Tick) {//progress increase and done
                                    hatchesStatusUpdate_EM();

                                    outputAfterRecipe_EM();
                                    cleanOutputEM_EM();

                                    if (mOutputItems != null) {
                                        for (ItemStack tStack : mOutputItems) {
                                            if (tStack != null) {
                                                addOutput(tStack);
                                            }
                                        }
                                    }
                                    mOutputItems = null;

                                    if (mOutputFluids != null) {
                                        if (mOutputFluids.length == 1) {
                                            for (FluidStack tStack : mOutputFluids) {
                                                if (tStack != null) {
                                                    addOutput(tStack);
                                                }
                                            }
                                        } else if (mOutputFluids.length > 1) {
                                            addFluidOutputs(mOutputFluids);
                                        }
                                    }
                                    mOutputFluids = null;

                                    updateSlots();
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;
                                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                                        if (checkRecipe(mInventory[1])) {
                                            mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - (getIdealStatus() - getRepairStatus()) * 1000));
                                        } else {
                                            afterRecipeCheckFailed();
                                        }
                                        updateSlots();
                                    } else {
                                        notAllowedToWork_stopMachine_EM();
                                    }
                                }
                            }// else {//failed to consume power/resources - inside on running tick
                            //    stopMachine();
                            //}
                        } else if (RECIPE_AT == Tick || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe(mInventory[1])) {
                                    mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - (getIdealStatus() - getRepairStatus()) * 1000));
                                } else {
                                    afterRecipeCheckFailed();
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

            aBaseMetaTileEntity.setErrorDisplayID(aBaseMetaTileEntity.getErrorDisplayID() & -512 | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8) | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64) | (eCertainStatus == 0 ? 0 : 128) | (eParameters ? 0 : 256));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            boolean active = aBaseMetaTileEntity.isActive() && mPollution > 0;
            for (GT_MetaTileEntity_Hatch_Muffler aMuffler : mMufflerHatches) {
                aMuffler.getBaseMetaTileEntity().setActive(active);
            }
        } else {
            soundMagic(getActivitySound());
        }
    }

    //endregion

    //region EFFICIENCY AND FIXING LIMITS

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
        return super.getRepairStatus() + (eCertainStatus == 0 ? 1 : 0) + (eParameters ? 1 : 0);
    }

    //endregion

    //region ENERGY!!!!

    //new method
    private boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        long euFlow = mEUt * eAmpereFlow;//quick scope sign
        if (allowProduction && euFlow > 0) {
            addEnergyOutput_EM((long) mEUt * (long) mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
        } else if (euFlow < 0) {
            if (!drainEnergyInput_EM(mEUt, (long) mEUt * getMaxEfficiency(aStack) / Math.max(1000L, mEfficiency), eAmpereFlow)) {
                stopMachine();
                return false;
            }
        }
        return true;
    }

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
    public long maxEUStore() {
        return Math.max(maxEUinputMin * (eMaxAmpereFlow << 3), maxEUoutputMin * (eMaxAmpereGen << 3));
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
        if (EU <= 0L) {
            return true;
        }
        for (GT_MetaTileEntity_Hatch tHatch : eDynamoMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                if (tHatch.maxEUOutput() < EU) {
                    explodeMultiblock();
                }
                if (tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU, false)) {
                    return true;
                }
            }
        }
        for (GT_MetaTileEntity_Hatch tHatch : mDynamoHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                if (tHatch.maxEUOutput() < EU) {
                    explodeMultiblock();
                }
                if (tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(EU, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param EU
     * @param Amperes
     * @return if was able to put inside the hatches
     */
    private boolean addEnergyOutput_EM(long EU, long Amperes) {
        if (EU < 0) {
            EU = -EU;
        }
        if (Amperes < 0) {
            Amperes = -Amperes;
        }
        long euVar = EU * Amperes;
        long diff;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                if (tHatch.maxEUOutput() < EU) {
                    explodeMultiblock();
                }
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
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                if (tHatch.maxEUOutput() < EU) {
                    explodeMultiblock();
                }
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
        setEUVar(Math.min(getEUVar() + euVar, maxEUStore()));
        return false;
    }

    @Deprecated
    @Override
    public final boolean drainEnergyInput(long EU) {
        if (EU <= 0L) {
            return true;
        }
        for (GT_MetaTileEntity_Hatch tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch) && tHatch.maxEUInput() > EU && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU, false)) {
                return true;
            }
        }
        for (GT_MetaTileEntity_Hatch tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch) && tHatch.maxEUInput() > EU && tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(EU, false)) {
                return true;
            }
        }
        return false;
    }

    private boolean drainEnergyInput_EM(long EUtTierVoltage, long EUtEffective, long Amperes) {
        if (EUtTierVoltage < 0) {
            EUtTierVoltage = -EUtTierVoltage;
        }
        if (EUtEffective < 0) {
            EUtEffective = -EUtEffective;
        }
        if (Amperes < 0) {
            Amperes = -Amperes;
        }
        long EUuse = EUtEffective * Amperes;
        if (EUuse > getEUVar() || //not enough power
                EUtTierVoltage > maxEUinputMax || //TIER IS BASED ON BEST HATCH! not total EUtEffective input
                (EUtTierVoltage * Amperes - 1) / maxEUinputMin + 1 > eMaxAmpereFlow) {// EUuse==0? --> (EUuse - 1) / maxEUinputMin + 1 = 1! //if not too much A
            if (DEBUG_MODE) {
                TecTech.LOGGER.debug("L1 " + EUuse + ' ' + getEUVar() + ' ' + (EUuse > getEUVar()));
                TecTech.LOGGER.debug("L2 " + EUtEffective + ' ' + maxEUinputMax + ' ' + (EUtEffective > maxEUinputMax));
                TecTech.LOGGER.debug("L3 " + EUuse + ' ' + eMaxAmpereFlow);
                TecTech.LOGGER.debug("L4 " + ((EUuse - 1) / maxEUinputMin + 1) + ' ' + eMaxAmpereFlow + ' ' + ((EUuse - 1) / maxEUinputMin + 1 > eMaxAmpereFlow));
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
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage += tHatch.maxEUInput();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                rVoltage += tHatch.maxEUInput();
            }
        }
        return rVoltage;
    }

    //new Method
    public final long getMaxInputEnergy() {
        long energy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                energy += tHatch.maxEUInput();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                energy += tHatch.maxEUInput() * tHatch.Amperes;
            }
        }
        return energy;
    }

    //new Method
    public final int getMaxEnergyInputTier_EM() {
        return getTier(maxEUinputMax);
    }

    //new Method
    public final int getMinEnergyInputTier_EM() {
        return getTier(maxEUinputMin);
    }

    public final long getMaxAmpereFlowAtMinTierOfEnergyHatches() {
        return eAmpereFlow;
    }

    //endregion

    //region convenience copies input and output EM
    //new Method
    public final cElementalInstanceStackMap getInputsClone_EM() {
        cElementalInstanceStackMap in = new cElementalInstanceStackMap();
        for (GT_MetaTileEntity_Hatch_ElementalContainer hatch : eInputHatches) {
            in.putUnifyAll(hatch.getContainerHandler());
        }
        return in.hasStacks() ? in : null;
    }

    //new Method
    public final cElementalInstanceStackMap getOutputsClone_EM() {
        cElementalInstanceStackMap out = new cElementalInstanceStackMap();
        for (GT_MetaTileEntity_Hatch_ElementalContainer hatch : eOutputHatches) {
            out.putUnifyAll(hatch.getContainerHandler());
        }
        return out.hasStacks() ? out : null;
    }
    //endregion

    //region em cleaning
    private void purgeAllOverflowEM_EM() {
        float mass = 0;
        for (GT_MetaTileEntity_Hatch_InputElemental tHatch : eInputHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                tHatch.updateSlots();
            }
            mass += tHatch.overflowMatter;
            tHatch.overflowMatter = 0;
        }
        for (GT_MetaTileEntity_Hatch_OutputElemental tHatch : eOutputHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                tHatch.updateSlots();
            }
            mass += tHatch.overflowMatter;
            tHatch.overflowMatter = 0;
        }
        cleanMassEM_EM(mass);
    }

    public void cleanHatchContentEM_EM(GT_MetaTileEntity_Hatch_ElementalContainer target) {
        if (target == null) {
            return;
        }
        cleanMassEM_EM(target.getContainerHandler().getMass());
    }

    public void cleanStackEM_EM(cElementalInstanceStack target) {
        if (target == null) {
            return;
        }
        cleanMassEM_EM(target.getMass());
    }

    public void cleanMassEM_EM(float mass) {
        if (mass > 0) {
            if (eMufflerHatches.size() < 1) {
                TecTech.anomalyHandler.addAnomaly(getBaseMetaTileEntity(), mass);
                explodeMultiblock();
                return;
            }
            mass /= eMufflerHatches.size();
            boolean shouldExplode = false;
            for (GT_MetaTileEntity_Hatch_OverflowElemental dump : eMufflerHatches) {
                if (dump.addOverflowMatter(mass)) {
                    shouldExplode = true;
                }
            }
            if (shouldExplode) {
                explodeMultiblock();
            }
        }
    }

    private void cleanOutputEM_EM() {
        if (outputEM == null) {
            return;
        }
        float mass = 0;
        for (cElementalInstanceStackMap map : outputEM) {
            if (map != null) {
                mass += map.getMass();
            }
        }
        outputEM = null;
        cleanMassEM_EM(mass);
    }
    //endregion

    //region EXPLOSIONS

    @Override
    public boolean explodesOnComponentBreak(ItemStack itemStack) {
        return false;
    }

    @Override
    public final void explodeMultiblock() {
        if (explodedThisTick) {
            return;
        }
        explodedThisTick = true;
        if (!TecTech.configTecTech.BOOM_ENABLE) {
            TecTech.proxy.broadcast("Multi Explode BOOM! " + getBaseMetaTileEntity().getXCoord() + ' ' + getBaseMetaTileEntity().getYCoord() + ' ' + getBaseMetaTileEntity().getZCoord());
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            TecTech.proxy.broadcast("Multi Explode BOOM! " + ste[2].toString());
            return;
        }
        extraExplosions_EM();
        GT_Pollution.addPollution(getBaseMetaTileEntity(), 600000);
        mInventory[1] = null;
        for (MetaTileEntity tTileEntity : mInputBusses) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : mOutputBusses) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : mInputHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : mOutputHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : mDynamoHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : mMufflerHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : mEnergyHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : mMaintenanceHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : eParamHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : eInputHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : eOutputHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : eMufflerHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : eEnergyMulti) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : eUncertainHatches) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : eDynamoMulti) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[14]);
        }
        for (MetaTileEntity tTileEntity : eInputData) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        for (MetaTileEntity tTileEntity : eOutputData) {
            tTileEntity.getBaseMetaTileEntity().doExplosion(V[9]);
        }
        getBaseMetaTileEntity().doExplosion(V[15]);
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        explodeMultiblock();
        if (!TecTech.configTecTech.BOOM_ENABLE) {
            TecTech.proxy.broadcast("Multi DoExplosion BOOM! " + getBaseMetaTileEntity().getXCoord() + ' ' + getBaseMetaTileEntity().getYCoord() + ' ' + getBaseMetaTileEntity().getZCoord());
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            TecTech.proxy.broadcast("Multi DoExplosion BOOM! " + ste[2].toString());
            return;
        }
        super.doExplosion(aExplosionPower);
    }//Redirecting to explodemultiblock
    //endregion

    //region adder methods
    @Override
    public final boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OverflowElemental) {
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_OverflowElemental) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputData) {
            return eInputData.add((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputData) {
            return eOutputData.add((GT_MetaTileEntity_Hatch_OutputData) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addClassicToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            return mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Param) {
            return eParamHatches.add((GT_MetaTileEntity_Hatch_Param) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            return eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti) {
            return eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputData) {
            return eInputData.add((GT_MetaTileEntity_Hatch_InputData) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputData) {
            return eOutputData.add((GT_MetaTileEntity_Hatch_OutputData) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addElementalToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OverflowElemental) {
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_OverflowElemental) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addClassicMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        return false;
    }

    public final boolean addElementalMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OverflowElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_OverflowElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OverflowElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eMufflerHatches.add((GT_MetaTileEntity_Hatch_OverflowElemental) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
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

    //New Method
    public final boolean addEnergyIOToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            //((GT_MetaTileEntity_Hatch_Elemental) aMetaTileEntity).mRecipeMap = getRecipeMap();
            return eInputHatches.add((GT_MetaTileEntity_Hatch_InputElemental) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addElementalOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputElemental) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eOutputHatches.add((GT_MetaTileEntity_Hatch_OutputElemental) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addClassicInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Uncertainty) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eUncertainHatches.add((GT_MetaTileEntity_Hatch_Uncertainty) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public final boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        return false;
    }

    //NEW METHOD
    public final boolean addDataConnectorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
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
}