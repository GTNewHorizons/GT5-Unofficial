package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.loader.TecTechConfig.POWERLESS_MODE;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.util.CommonValues.MULTI_CHECK_AT;
import static com.github.technus.tectech.util.CommonValues.RECIPE_AT;
import static com.github.technus.tectech.util.CommonValues.TEC_MARK_GENERAL;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.CommonValues.VN;
import static com.github.technus.tectech.util.TT_Utility.getTier;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DataConnector;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputData;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Uncertainty;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.google.common.collect.Iterables;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IBindPlayerInventoryUI;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.GT_Pollution;
import gregtech.common.tileentities.machines.IDualInputHatch;

/**
 * Created by danie_000 on 27.10.2016.
 */
public abstract class GT_MetaTileEntity_MultiblockBase_EM
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_MultiblockBase_EM>
    implements IAlignment, IBindPlayerInventoryUI {
    // region Client side variables (static - one per class)

    // Front icon holders - static so it is default one for my blocks
    // just add new static ones in your class and and override getTexture
    protected static Textures.BlockIcons.CustomIcon ScreenOFF;
    protected static Textures.BlockIcons.CustomIcon ScreenON;

    /** Base ID for the LED window popup. LED 1 I0 will have ID 100, LED 1 I1 101... */
    protected static int LED_WINDOW_BASE_ID = 100;

    // Sound resource - same as with screen but override getActivitySound
    public static final ResourceLocation activitySound = new ResourceLocation(Reference.MODID + ":fx_lo_freq");

    @SideOnly(Side.CLIENT)
    private SoundLoop activitySoundLoop;
    // endregion

    // region HATCHES ARRAYS - they hold info about found hatches, add hatches to them... (auto structure magic does it
    // tho)

    // HATCHES!!!, should be added and removed in check machine
    protected ArrayList<GT_MetaTileEntity_Hatch_Param> eParamHatches = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_Uncertainty> eUncertainHatches = new ArrayList<>();
    // multi amp hatches in/out
    protected ArrayList<GT_MetaTileEntity_Hatch_EnergyMulti> eEnergyMulti = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_DynamoMulti> eDynamoMulti = new ArrayList<>();
    // data hatches
    protected ArrayList<GT_MetaTileEntity_Hatch_InputData> eInputData = new ArrayList<>();
    protected ArrayList<GT_MetaTileEntity_Hatch_OutputData> eOutputData = new ArrayList<>();

    // endregion

    // region parameters
    public final Parameters parametrization;
    // endregion

    // region Control variables

    // should explode on dismatle?, set it in constructor, if true machine will explode if invalidated structure while
    // active
    protected boolean eDismantleBoom = false;

    // what is the amount of A required
    public long eAmpereFlow = 1; // analogue of EU/t but for amperes used (so eu/t is actually eu*A/t) USE ONLY POSITIVE
                                 // NUMBERS!

    // set to what you need it to be in check recipe
    // data required to operate
    protected long eRequiredData = 0;

    // Counter for the computation timeout. Will be initialized one to the max time and then only decreased.
    protected int eComputationTimeout = MAX_COMPUTATION_TIMEOUT;

    // Max timeout of computation in ticks
    protected static int MAX_COMPUTATION_TIMEOUT = 100;

    // are parameters correct - change in check recipe/output/update params etc. (maintenance status boolean)
    protected boolean eParameters = true;

    // what type of certainty inconvenience is used - can be used as in Computer - more info in uncertainty hatch
    protected byte eCertainMode = 0, eCertainStatus = 0;

    // minimal repair status to make the machine even usable (how much unfixed fixed stuff is needed)
    // if u need to force some things to be fixed - u might need to override doRandomMaintenanceDamage
    protected byte minRepairStatus = 3;

    // whether there is a maintenance hatch in the multi and whether checks are necessary (for now only used in a
    // transformer)
    protected boolean hasMaintenanceChecks = true;

    // is power pass cover present
    public boolean ePowerPassCover = false;

    // functionality toggles - changed by buttons in gui also
    public boolean ePowerPass = false, eSafeVoid = false;

    // endregion

    // region READ ONLY unless u really need to change it

    // max amperes machine can take in after computing it to the lowest tier (exchange packets to min tier count)
    protected long eMaxAmpereFlow = 0, eMaxAmpereGen = 0;

    // What is the max and minimal tier of eu hatches installed
    private long maxEUinputMin = 0, maxEUinputMax = 0, maxEUoutputMin = 0, maxEUoutputMax = 0;

    // read only unless you are making computation generator - read computer class
    protected long eAvailableData = 0; // data being available

    // just some info - private so hidden
    private boolean explodedThisTick = false;

    /** Flag if the new long power variable should be used */
    protected boolean useLongPower = false;

    // Locale-aware formatting of numbers.
    protected static NumberFormatMUI numberFormat;
    static {
        numberFormat = new NumberFormatMUI();
        numberFormat.setMaximumFractionDigits(8);
    }

    // endregion

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

    // region SUPER STRUCT

    /**
     * Gets structure
     *
     * @return STATIC INSTANCE OF STRUCTURE
     */
    public abstract IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM();

    @SuppressWarnings("unchecked")
    private IStructureDefinition<GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM_Internal() {
        return (IStructureDefinition<GT_MetaTileEntity_MultiblockBase_EM>) getStructure_EM();
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiblockBase_EM> getStructureDefinition() {
        return getStructure_EM_Internal();
    }

    public final boolean structureCheck_EM(String piece, int horizontalOffset, int verticalOffset, int depthOffset) {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        return getStructure_EM_Internal().check(
            this,
            piece,
            baseMetaTileEntity.getWorld(),
            getExtendedFacing(),
            baseMetaTileEntity.getXCoord(),
            baseMetaTileEntity.getYCoord(),
            baseMetaTileEntity.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            !mMachine);
    }

    public final boolean structureBuild_EM(String piece, int horizontalOffset, int verticalOffset, int depthOffset,
        ItemStack trigger, boolean hintsOnly) {
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        return getStructure_EM_Internal().buildOrHints(
            this,
            trigger,
            piece,
            baseMetaTileEntity.getWorld(),
            getExtendedFacing(),
            baseMetaTileEntity.getXCoord(),
            baseMetaTileEntity.getYCoord(),
            baseMetaTileEntity.getZCoord(),
            horizontalOffset,
            verticalOffset,
            depthOffset,
            hintsOnly);
    }
    // endregion

    // region METHODS TO OVERRIDE - general functionality, recipe check, output

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
     * @deprecated Use {@link #createProcessingLogic()} ()} or {@link #checkProcessing_EM()}
     *
     * @param itemStack item in the controller
     * @return is recipe is valid
     */
    @Deprecated
    public boolean checkRecipe_EM(ItemStack itemStack) {
        return false;
    }

    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        if (processingLogic == null) {
            return checkRecipe_EM(getControllerSlot()) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }
        return super.checkProcessing();
    }

    /**
     * Put EM stuff from outputEM into EM output hatches here or do other stuff - it is basically on recipe succeded
     * <p>
     * based on "machine state" do output, this must move to outputEM to EM output hatches and can also modify output
     * items/fluids/EM, remaining EM is NOT overflowed. (Well it can be overflowed if machine didn't finished,
     * soft-hammered/disabled/not enough EU) Setting available data processing
     */
    public void outputAfterRecipe_EM() {}
    // endregion

    // region tooltip and scanner result

    public ArrayList<String> getFullLedDescriptionIn(int hatchNo, int paramID) {
        ArrayList<String> list = new ArrayList<>();
        list.add(
            EnumChatFormatting.WHITE + "ID: "
                + EnumChatFormatting.AQUA
                + hatchNo
                + EnumChatFormatting.YELLOW
                + ":"
                + EnumChatFormatting.AQUA
                + paramID
                + EnumChatFormatting.YELLOW
                + ":"
                + EnumChatFormatting.AQUA
                + "I  "
                + parametrization.getStatusIn(hatchNo, paramID).name.get());
        list.add(
            EnumChatFormatting.WHITE + "Value: "
                + EnumChatFormatting.AQUA
                + numberFormat.format(parametrization.getIn(hatchNo, paramID)));
        try {
            list.add(parametrization.groups[hatchNo].parameterIn[paramID].getBrief());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            list.add("Unused");
        }
        return list;
    }

    public ArrayList<String> getFullLedDescriptionOut(int hatchNo, int paramID) {
        ArrayList<String> list = new ArrayList<>();
        list.add(
            EnumChatFormatting.WHITE + "ID: "
                + EnumChatFormatting.AQUA
                + hatchNo
                + EnumChatFormatting.YELLOW
                + ":"
                + EnumChatFormatting.AQUA
                + paramID
                + EnumChatFormatting.YELLOW
                + ":"
                + EnumChatFormatting.AQUA
                + "O "
                + parametrization.getStatusOut(hatchNo, paramID).name.get());
        list.add(
            EnumChatFormatting.WHITE + "Value: "
                + EnumChatFormatting.AQUA
                + numberFormat.format(parametrization.getOut(hatchNo, paramID)));
        try {
            list.add(parametrization.groups[hatchNo].parameterOut[paramID].getBrief());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            list.add("Unused");
        }
        return list;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addInfo("Nothing special just override me")
            .toolTipFinisher(TEC_MARK_GENERAL);
        return tt;
    }

    /**
     * scanner gives it
     */
    @Override
    public String[] getInfoData() { // TODO Do it
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : filterValidMTEs(eEnergyMulti)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] { "Progress:",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            "Energy Hatches:",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            (getPowerFlow() * eAmpereFlow <= 0 ? "Probably uses: " : "Probably makes: ") + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(Math.abs(getPowerFlow()))
                + EnumChatFormatting.RESET
                + " EU/t at "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(eAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            "Tier Rating: " + EnumChatFormatting.YELLOW
                + VN[getMaxEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.GREEN
                + VN[getMinEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " Amp Rating: "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(eMaxAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            "Problems: " + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " Efficiency: "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            "PowerPass: " + EnumChatFormatting.BLUE
                + ePowerPass
                + EnumChatFormatting.RESET
                + " SafeVoid: "
                + EnumChatFormatting.BLUE
                + eSafeVoid,
            "Computation: " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(eAvailableData)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(eRequiredData)
                + EnumChatFormatting.RESET };
    }

    /**
     * should it work with scanner? HELL YES
     */
    @Override
    public boolean isGivingInformation() {
        return true;
    }

    // endregion

    // region GUI/SOUND/RENDER

    /**
     * add more textures
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
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][4],
                new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][4] };
    }

    /**
     * should return your activity sound
     */
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return activitySound;
    }

    /**
     * plays the sounds auto magically
     */
    @SideOnly(Side.CLIENT)
    protected void soundMagic(ResourceLocation activitySound) {
        if (getBaseMetaTileEntity().isActive()) {
            if (activitySoundLoop == null) {
                activitySoundLoop = new SoundLoop(activitySound, getBaseMetaTileEntity(), false, true);
                Minecraft.getMinecraft()
                    .getSoundHandler()
                    .playSound(activitySoundLoop);
            }
        } else {
            if (activitySoundLoop != null) {
                activitySoundLoop = null;
            }
        }
    }

    // endregion

    // region Methods to maybe override (if u implement certain stuff)

    /**
     * is the thing inside controller a valid item to make the machine work
     */
    @Override
    public boolean isCorrectMachinePart(ItemStack itemStack) {
        return true;
    }

    /**
     * how much damage to apply to thing in controller - not sure how it does it
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
            if (ePowerPass && getEUVar() > V[3]
                || eDismantleBoom && mMaxProgresstime > 0 && areChunksAroundLoaded_EM()) {
                explodeMultiblock();
            }
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }

    /**
     * prevents spontaneous explosions when the chunks unloading would cause them should cover 3 chunks radius
     */
    protected boolean areChunksAroundLoaded_EM() {
        if (this.isValid() && getBaseMetaTileEntity().isServerSide()) {
            IGregTechTileEntity base = getBaseMetaTileEntity();
            return base.getWorld()
                .doChunksNearChunkExist(base.getXCoord(), base.getYCoord(), base.getZCoord(), 3);
            // todo check if it is actually checking if chunks are loaded
        } else {
            return false;
        }
    }

    /**
     * instantiate parameters in CONSTRUCTOR! CALLED ONCE on creation, don't call it in your classes
     */
    protected void parametersInstantiation_EM() {}

    /**
     * It is automatically called OFTEN update status of parameters in guis (and "machine state" if u wish) Called
     * before check recipe, before outputting, and every second the machine is complete
     * <p>
     * good place to update parameter statuses, default implementation handles it well
     *
     * @param machineBusy is machine doing stuff
     */
    protected void parametersStatusesWrite_EM(boolean machineBusy) { // todo unimplement?
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
    protected void hatchInit_EM(boolean mMachine) {}

    /**
     * called when the multiblock is exploding - if u want to add more EXPLOSIONS, for ex. new types of hatches also
     * have to explode
     */
    protected void extraExplosions_EM() {} // For that extra hatches explosions, and maybe some MOORE EXPLOSIONS

    /**
     * Get Available data, Override only on data outputters should return mAvailableData that is set in check recipe
     *
     * @return available data
     */
    protected long getAvailableData_EM() {
        long result = 0;
        IGregTechTileEntity baseMetaTileEntity = getBaseMetaTileEntity();
        Vec3Impl pos = new Vec3Impl(
            baseMetaTileEntity.getXCoord(),
            baseMetaTileEntity.getYCoord(),
            baseMetaTileEntity.getZCoord());
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

    protected long getPowerFlow() {
        return useLongPower ? lEUt : mEUt;
    }

    protected void setPowerFlow(long lEUt) {
        if (useLongPower) {
            this.lEUt = lEUt;
        } else {
            mEUt = (int) Math.min(Integer.MAX_VALUE, lEUt);
        }
    }

    @Override
    protected long getActualEnergyUsage() {
        return -(useLongPower ? lEUt : mEUt) * eAmpereFlow * 10_000 / Math.max(1_000, mEfficiency);
    }

    /**
     * Extra hook on cyclic updates (not really needed for machines smaller than 1 chunk) BUT NEEDED WHEN - machine
     * blocks are not touching each other or they don't implement IMachineBlockUpdateable (ex. air,stone,weird TE's)
     */
    protected boolean cyclicUpdate_EM() {
        return mUpdate <= -1000; // set to false to disable cyclic update
        // default is once per 50s; mUpdate is decremented every tick
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
     * triggered if machine is not allowed to work after completing a recipe, override to make it not shutdown for
     * instance (like turbines). bu just replacing it with blank - active transformer is doing it
     * <p>
     * CALLED DIRECTLY when soft hammered to offline state - usually should stop the machine unless some other mechanics
     * should do it
     */
    protected void notAllowedToWork_stopMachine_EM() {
        stopMachine();
    }

    /**
     * store data
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
        aNBT.setBoolean("eParam", eParameters);
        aNBT.setBoolean("ePass", ePowerPass);
        aNBT.setBoolean("ePowerPassCover", ePowerPassCover);
        aNBT.setBoolean("eVoid", eSafeVoid);
        aNBT.setBoolean("eBoom", eDismantleBoom);
        aNBT.setBoolean("eOK", mMachine);
        // Ensures compatibility
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

        // Ensures compatibility
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

        aNBT.setInteger("eOutputStackCount", 0);
        aNBT.removeTag("outputEM");

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
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        maxEUoutputMin = aNBT.getLong("eMaxGenEUmin");
        maxEUoutputMax = aNBT.getLong("eMaxGenEUmax");
        eMaxAmpereGen = aNBT.getLong("eGenRating");
        maxEUinputMin = aNBT.getLong("eMaxEUmin");
        maxEUinputMax = aNBT.getLong("eMaxEUmax");
        eAmpereFlow = aNBT.hasKey("eRating") ? aNBT.getLong("eRating") : 1;
        eMaxAmpereFlow = aNBT.getLong("eMaxA");
        eRequiredData = aNBT.getLong("eDataR");
        eAvailableData = aNBT.getLong("eDataA");
        eCertainMode = aNBT.getByte("eCertainM");
        eCertainStatus = aNBT.getByte("eCertainS");
        minRepairStatus = aNBT.hasKey("eMinRepair") ? aNBT.getByte("eMinRepair") : 3;
        eParameters = !aNBT.hasKey("eParam") || aNBT.getBoolean("eParam");
        ePowerPass = aNBT.getBoolean("ePass");
        ePowerPassCover = aNBT.getBoolean("ePowerPassCover");
        eSafeVoid = aNBT.getBoolean("eVoid");
        eDismantleBoom = aNBT.getBoolean("eBoom");
        mMachine = aNBT.getBoolean("eOK");

        // Ensures compatibility
        int aOutputItemsLength = aNBT.getInteger("mOutputItemsLength");
        if (aOutputItemsLength > 0) {
            mOutputItems = new ItemStack[aOutputItemsLength];
            for (int i = 0; i < mOutputItems.length; i++) {
                mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
            }
        }

        // Ensures compatibility
        int aOutputFluidsLength = aNBT.getInteger("mOutputFluidsLength");
        if (aOutputFluidsLength > 0) {
            mOutputFluids = new FluidStack[aOutputFluidsLength];
            for (int i = 0; i < mOutputFluids.length; i++) {
                mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
            }
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
     * Override if needed but usually call super method at start! On machine stop - NOT called directly when soft
     * hammered to offline state! - it SHOULD cause a full stop like power failure does
     */
    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        if (!ShutDownReasonRegistry.isRegistered(reason.getID())) {
            throw new RuntimeException(String.format("Reason %s is not registered for registry", reason.getID()));
        }
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
        getBaseMetaTileEntity().setShutDownReason(reason);
        getBaseMetaTileEntity().setShutdownStatus(true);
        if (reason.wasCritical()) {
            sendSound(INTERRUPT_SOUND_INDEX);
        }
    }

    /**
     * After recipe check failed helper method so i don't have to set that params to nothing at all times
     */
    protected void afterRecipeCheckFailed() {

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
    }

    /**
     * cyclic check even when not working, called LESS frequently
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
     */
    @Override
    public byte getTileEntityBaseType() {
        return 3;
    }

    // endregion

    // region internal

    /**
     * internal check machine
     */
    @Override
    public final boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return checkMachine_EM(iGregTechTileEntity, itemStack);
    }

    /**
     * internal check recipe
     */
    @Override
    public final boolean checkRecipe(ItemStack itemStack) { // do recipe checks, based on "machine content and state"
        hatchesStatusUpdate_EM();
        startRecipeProcessing();
        boolean result = checkRecipe_EM(itemStack); // if had no - set default params
        endRecipeProcessing();
        hatchesStatusUpdate_EM();
        return result;
    }

    @NotNull
    @Override
    public final CheckRecipeResult checkProcessing() {
        hatchesStatusUpdate_EM();
        CheckRecipeResult result = checkProcessing_EM();
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
        if (busy) { // write from buffer to hatches only
            for (GT_MetaTileEntity_Hatch_Param hatch : filterValidMTEs(eParamHatches)) {
                if (hatch.param < 0) {
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
        } else { // if has nothing to do update all
            for (GT_MetaTileEntity_Hatch_Param hatch : filterValidMTEs(eParamHatches)) {
                if (hatch.param < 0) {
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
    // endregion

    // region TICKING functions

    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {}

    @Override
    public final void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        isFacingValid(aBaseMetaTileEntity.getFrontFacing());
        if (getBaseMetaTileEntity().isClientSide()) {
            StructureLibAPI.queryAlignment((IAlignmentProvider) aBaseMetaTileEntity);
        }
        onFirstTick_EM(aBaseMetaTileEntity);
    }

    /**
     * called every tick the machines is active
     */
    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return onRunningTickCheck(aStack);
    }

    public boolean onRunningTickCheck_EM(ItemStack aStack) {
        if (eRequiredData > eAvailableData) {
            if (!checkComputationTimeout()) {
                if (energyFlowOnRunningTick_EM(aStack, false)) {
                    stopMachine(SimpleShutDownReason.ofCritical("computation_loss"));
                }
                return false;
            }
        }
        return energyFlowOnRunningTick_EM(aStack, true);
    }

    public boolean onRunningTickCheck(ItemStack aStack) {
        if (eRequiredData > eAvailableData) {
            if (!checkComputationTimeout()) {
                if (energyFlowOnRunningTick(aStack, false)) {
                    stopMachine(SimpleShutDownReason.ofCritical("computation_loss"));
                }
                return false;
            }
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

            if (--mUpdate == 0 || --mStartUpCheck == 0
                || cyclicUpdate()
                || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                clearHatches_EM();

                if (aBaseMetaTileEntity instanceof BaseTileEntity) {
                    ((BaseTileEntity) aBaseMetaTileEntity).ignoreUnloadedChunks = mMachine;
                }
                mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);

                if (!mMachine) {
                    if (ePowerPass && getEUVar() > V[3]
                        || eDismantleBoom && mMaxProgresstime > 0 && areChunksAroundLoaded_EM()) {
                        explodeMultiblock();
                    }
                }

                if (eUncertainHatches.size() > 1) {
                    mMachine = false;
                }

                if (mMachine) {
                    setupHatches_EM();

                    setupEnergyHatchesVariables_EM();

                    if (getEUVar() > maxEUStore()) {
                        setEUVar(maxEUStore());
                    }
                } else {
                    maxEUinputMin = 0;
                    maxEUinputMax = 0;
                    eMaxAmpereFlow = 0;
                    setEUVar(0);
                }
                hatchInit_EM(mMachine);
            }

            if (mStartUpCheck < 0) { // E
                if (mMachine) { // S
                    byte Tick = (byte) (aTick % 20);
                    if (MULTI_CHECK_AT == Tick) {
                        checkMaintenance();
                    }

                    if (getRepairStatus() >= minRepairStatus) { // S
                        if (MULTI_CHECK_AT == Tick) {
                            hatchesStatusUpdate_EM();
                        }

                        dischargeController_EM(aBaseMetaTileEntity);
                        chargeController_EM(aBaseMetaTileEntity);

                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) { // Start
                            if (onRunningTick(mInventory[1])) { // Compute EU
                                if (!polluteEnvironment(getPollutionPerTick(mInventory[1]))) {
                                    stopMachine(ShutDownReasonRegistry.POLLUTION_FAIL);
                                }

                                if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) { // progress increase
                                                                                                   // and done
                                    hatchesStatusUpdate_EM();

                                    outputAfterRecipe_EM();

                                    addClassicOutputs_EM();

                                    updateSlots();
                                    mProgresstime = 0;
                                    mMaxProgresstime = 0;
                                    mEfficiencyIncrease = 0;

                                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                                        if (checkRecipe()) {
                                            mEfficiency = Math.max(
                                                0,
                                                min(
                                                    mEfficiency + mEfficiencyIncrease,
                                                    getMaxEfficiency(mInventory[1])
                                                        - (getIdealStatus() - getRepairStatus()) * 1000));
                                        } else {
                                            afterRecipeCheckFailed();
                                        }
                                        updateSlots();
                                    } else {
                                        notAllowedToWork_stopMachine_EM();
                                    }
                                }
                            } // else {//failed to consume power/resources - inside on running tick
                              // stopMachine();
                              // }
                        } else if (RECIPE_AT == Tick || aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
                            if (aBaseMetaTileEntity.isAllowedToWork()) {
                                if (checkRecipe()) {
                                    mEfficiency = Math.max(
                                        0,
                                        min(
                                            mEfficiency + mEfficiencyIncrease,
                                            getMaxEfficiency(mInventory[1])
                                                - (getIdealStatus() - getRepairStatus()) * 1000));
                                } else {
                                    afterRecipeCheckFailed();
                                }
                                updateSlots();
                            } // else notAllowedToWork_stopMachine_EM(); //it is already stopped here
                        }
                    } else { // not repaired
                        stopMachine(ShutDownReasonRegistry.NO_REPAIR);
                    }
                } else { // not complete
                    stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
                }
            }

            aBaseMetaTileEntity.setErrorDisplayID(
                aBaseMetaTileEntity.getErrorDisplayID() & -512 | (mWrench ? 0 : 1)
                    | (mScrewdriver ? 0 : 2)
                    | (mSoftHammer ? 0 : 4)
                    | (mHardHammer ? 0 : 8)
                    | (mSolderingTool ? 0 : 16)
                    | (mCrowbar ? 0 : 32)
                    | (mMachine ? 0 : 64)
                    | (eCertainStatus == 0 ? 0 : 128)
                    | (eParameters ? 0 : 256));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
            boolean active = aBaseMetaTileEntity.isActive() && mPollution > 0;
            setMufflers(active);
        } else {
            soundMagic(getActivitySound());
        }
    }

    protected void addClassicOutputs_EM() {
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
    }

    protected void clearHatches_EM() {
        mDualInputHatches.clear();
        mInputHatches.clear();
        mInputBusses.clear();
        mOutputHatches.clear();
        mOutputBusses.clear();
        mDynamoHatches.clear();
        mEnergyHatches.clear();
        mMufflerHatches.clear();
        mMaintenanceHatches.clear();

        for (GT_MetaTileEntity_Hatch_DataConnector<?> hatch_data : filterValidMTEs(eOutputData)) {
            hatch_data.id = -1;
        }
        for (GT_MetaTileEntity_Hatch_DataConnector<?> hatch_data : filterValidMTEs(eInputData)) {
            hatch_data.id = -1;
        }

        for (GT_MetaTileEntity_Hatch_Uncertainty hatch : filterValidMTEs(eUncertainHatches)) {
            hatch.getBaseMetaTileEntity()
                .setActive(false);
        }
        for (GT_MetaTileEntity_Hatch_Param hatch : filterValidMTEs(eParamHatches)) {
            hatch.getBaseMetaTileEntity()
                .setActive(false);
        }

        eUncertainHatches.clear();
        eEnergyMulti.clear();
        eParamHatches.clear();
        eDynamoMulti.clear();
        eOutputData.clear();
        eInputData.clear();
    }

    protected void setupHatches_EM() {
        short id = 1;

        for (GT_MetaTileEntity_Hatch_DataConnector<?> hatch_data : filterValidMTEs(eOutputData)) {
            hatch_data.id = id++;
        }
        id = 1;
        for (GT_MetaTileEntity_Hatch_DataConnector<?> hatch_data : filterValidMTEs(eInputData)) {
            hatch_data.id = id++;
        }

        for (GT_MetaTileEntity_Hatch_Uncertainty hatch : filterValidMTEs(eUncertainHatches)) {
            hatch.getBaseMetaTileEntity()
                .setActive(true);
        }
        for (GT_MetaTileEntity_Hatch_Param hatch : filterValidMTEs(eParamHatches)) {
            hatch.getBaseMetaTileEntity()
                .setActive(true);
        }
    }

    protected void setupEnergyHatchesVariables_EM() {
        if (!mEnergyHatches.isEmpty() || !eEnergyMulti.isEmpty()
            || !mDynamoHatches.isEmpty()
            || !eDynamoMulti.isEmpty()) {
            maxEUinputMin = V[15];
            maxEUinputMax = V[0];
            maxEUoutputMin = V[15];
            maxEUoutputMax = V[0];
            for (GT_MetaTileEntity_Hatch_Energy hatch : filterValidMTEs(mEnergyHatches)) {
                if (hatch.maxEUInput() < maxEUinputMin) {
                    maxEUinputMin = hatch.maxEUInput();
                }
                if (hatch.maxEUInput() > maxEUinputMax) {
                    maxEUinputMax = hatch.maxEUInput();
                }
            }
            for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : filterValidMTEs(eEnergyMulti)) {
                if (hatch.maxEUInput() < maxEUinputMin) {
                    maxEUinputMin = hatch.maxEUInput();
                }
                if (hatch.maxEUInput() > maxEUinputMax) {
                    maxEUinputMax = hatch.maxEUInput();
                }
            }
            for (GT_MetaTileEntity_Hatch_Dynamo hatch : filterValidMTEs(mDynamoHatches)) {
                if (hatch.maxEUOutput() < maxEUoutputMin) {
                    maxEUoutputMin = hatch.maxEUOutput();
                }
                if (hatch.maxEUOutput() > maxEUoutputMax) {
                    maxEUoutputMax = hatch.maxEUOutput();
                }
            }
            for (GT_MetaTileEntity_Hatch_DynamoMulti hatch : filterValidMTEs(eDynamoMulti)) {
                if (hatch.maxEUOutput() < maxEUoutputMin) {
                    maxEUoutputMin = hatch.maxEUOutput();
                }
                if (hatch.maxEUOutput() > maxEUoutputMax) {
                    maxEUoutputMax = hatch.maxEUOutput();
                }
            }
            eMaxAmpereFlow = 0;
            eMaxAmpereGen = 0;
            // counts only full amps
            for (GT_MetaTileEntity_Hatch_Energy hatch : filterValidMTEs(mEnergyHatches)) {
                eMaxAmpereFlow += hatch.maxEUInput() / maxEUinputMin;
            }
            for (GT_MetaTileEntity_Hatch_EnergyMulti hatch : filterValidMTEs(eEnergyMulti)) {
                eMaxAmpereFlow += hatch.maxEUInput() / maxEUinputMin * hatch.Amperes;
            }
            for (GT_MetaTileEntity_Hatch_Dynamo hatch : filterValidMTEs(mDynamoHatches)) {
                eMaxAmpereGen += hatch.maxEUOutput() / maxEUoutputMin;
            }
            for (GT_MetaTileEntity_Hatch_DynamoMulti hatch : filterValidMTEs(eDynamoMulti)) {
                eMaxAmpereGen += hatch.maxEUOutput() / maxEUoutputMin * hatch.Amperes;
            }
        } else {
            maxEUinputMin = 0;
            maxEUinputMax = 0;
            eMaxAmpereFlow = 0;
            maxEUoutputMin = 0;
            maxEUoutputMax = 0;
            eMaxAmpereGen = 0;
        }
    }

    protected void dischargeController_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (ePowerPass && getEUVar() > getMinimumStoredEU()) {
            powerPass(aBaseMetaTileEntity);
        }
    }

    protected final void powerPass(IGregTechTileEntity aBaseMetaTileEntity) {
        long euVar;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : filterValidMTEs(mDynamoHatches)) {
            euVar = tHatch.maxEUOutput() * tHatch.maxAmperesOut();
            if (tHatch.getBaseMetaTileEntity()
                .getStoredEU() <= tHatch.maxEUStore() - euVar
                && aBaseMetaTileEntity
                    .decreaseStoredEnergyUnits(euVar + Math.max(euVar / 24576, tHatch.maxAmperesOut()), false)) {
                tHatch.setEUVar(
                    tHatch.getBaseMetaTileEntity()
                        .getStoredEU() + euVar);
            }
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : filterValidMTEs(eDynamoMulti)) {
            euVar = tHatch.maxEUOutput() * tHatch.maxAmperesOut();
            if (tHatch.getBaseMetaTileEntity()
                .getStoredEU() <= tHatch.maxEUStore() - euVar
                && aBaseMetaTileEntity
                    .decreaseStoredEnergyUnits(euVar + Math.max(euVar / 24576, tHatch.maxAmperesOut()), false)) {
                tHatch.setEUVar(
                    tHatch.getBaseMetaTileEntity()
                        .getStoredEU() + euVar);
            }
        }
    }

    protected final void powerPass_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        long euVar;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : filterValidMTEs(mDynamoHatches)) {
            euVar = tHatch.maxEUOutput();
            if (tHatch.getBaseMetaTileEntity()
                .getStoredEU() <= tHatch.maxEUStore() - euVar
                && aBaseMetaTileEntity.decreaseStoredEnergyUnits(euVar + Math.max(euVar / 24576, 1), false)) {
                tHatch.setEUVar(
                    tHatch.getBaseMetaTileEntity()
                        .getStoredEU() + euVar);
            }
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : filterValidMTEs(eDynamoMulti)) {
            euVar = tHatch.maxEUOutput() * tHatch.Amperes;
            if (tHatch.getBaseMetaTileEntity()
                .getStoredEU() <= tHatch.maxEUStore() - euVar
                && aBaseMetaTileEntity
                    .decreaseStoredEnergyUnits(euVar + Math.max(euVar / 24576, tHatch.Amperes), false)) {
                tHatch.setEUVar(
                    tHatch.getBaseMetaTileEntity()
                        .getStoredEU() + euVar);
            }
        }
    }

    protected void chargeController_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        powerInput();
    }

    protected final void powerInput() {
        long euVar;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            if (getEUVar() > getMinimumStoredEU()) {
                break;
            }
            euVar = Math.min(tHatch.maxEUInput() * tHatch.maxAmperesIn(), tHatch.getEUVar());
            if (tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(euVar, false)) {
                setEUVar(getEUVar() + euVar);
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : filterValidMTEs(eEnergyMulti)) {
            if (getEUVar() > getMinimumStoredEU()) {
                break;
            }
            euVar = Math.min(tHatch.maxEUInput() * tHatch.maxAmperesIn(), tHatch.getEUVar());
            if (tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(euVar, false)) {
                setEUVar(getEUVar() + euVar);
            }
        }
    }

    protected final void powerInput_EM() {
        long euVar;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            if (getEUVar() > getMinimumStoredEU()) {
                break;
            }
            euVar = tHatch.maxEUInput();
            if (tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(euVar, false)) {
                setEUVar(getEUVar() + euVar);
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : filterValidMTEs(eEnergyMulti)) {
            if (getEUVar() > getMinimumStoredEU()) {
                break;
            }
            euVar = tHatch.maxEUInput() * tHatch.Amperes;
            if (tHatch.getBaseMetaTileEntity()
                .decreaseStoredEnergyUnits(euVar, false)) {
                setEUVar(getEUVar() + euVar);
            }
        }
    }

    // endregion

    // region EFFICIENCY AND FIXING LIMITS

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

    // endregion

    // region ENERGY!!!!

    // new method
    public boolean energyFlowOnRunningTick_EM(ItemStack aStack, boolean allowProduction) {
        long euFlow = getPowerFlow() * eAmpereFlow; // quick scope sign
        if (allowProduction && euFlow > 0) {
            addEnergyOutput_EM(getPowerFlow() * (long) mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
        } else if (euFlow < 0) {
            if (POWERLESS_MODE) {
                return true;
            }
            if (!drainEnergyInput_EM(
                getPowerFlow(),
                getPowerFlow() * getMaxEfficiency(aStack) / Math.max(1000L, mEfficiency),
                eAmpereFlow)) {
                criticalStopMachine();
                return false;
            }
        }
        return true;
    }

    public boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        long euFlow = getPowerFlow() * eAmpereFlow; // quick scope sign
        if (allowProduction && euFlow > 0) {
            addEnergyOutput_EM(getPowerFlow() * (long) mEfficiency / getMaxEfficiency(aStack), eAmpereFlow);
        } else if (euFlow < 0) {
            if (POWERLESS_MODE) {
                return true;
            }
            if (!drainEnergyInput(
                getPowerFlow() * getMaxEfficiency(aStack) / Math.max(1000L, mEfficiency),
                eAmpereFlow)) {
                criticalStopMachine();
                return false;
            }
        }
        return true;
    }

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
    public final boolean addEnergyOutput(long eu) {
        return addEnergyOutput_EM(eu, 1);
    }

    public boolean addEnergyOutput_EM(long EU, long Amperes) {
        if (EU < 0) {
            EU = -EU;
        }
        if (Amperes < 0) {
            Amperes = -Amperes;
        }
        long euVar = EU * Amperes;
        long diff;
        for (GT_MetaTileEntity_Hatch_Dynamo tHatch : filterValidMTEs(mDynamoHatches)) {
            if (tHatch.maxEUOutput() < EU) {
                explodeMultiblock();
            }
            diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            if (diff > 0) {
                if (euVar > diff) {
                    tHatch.setEUVar(tHatch.maxEUStore());
                    euVar -= diff;
                } else if (euVar <= diff) {
                    tHatch.setEUVar(
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + euVar);
                    return true;
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_DynamoMulti tHatch : filterValidMTEs(eDynamoMulti)) {
            if (tHatch.maxEUOutput() < EU) {
                explodeMultiblock();
            }
            diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            if (diff > 0) {
                if (euVar > diff) {
                    tHatch.setEUVar(tHatch.maxEUStore());
                    euVar -= diff;
                } else if (euVar <= diff) {
                    tHatch.setEUVar(
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + euVar);
                    return true;
                }
            }
        }
        setEUVar(min(getEUVar() + euVar, maxEUStore()));
        return false;
    }

    @Deprecated
    @Override
    public final boolean drainEnergyInput(long eu) {
        return drainEnergyInput_EM(0, eu, 1);
    }

    public boolean drainEnergyInput_EM(long EUtTierVoltage, long EUtEffective, long Amperes) {
        long EUuse = EUtEffective * Amperes;
        if (EUuse == 0) {
            return true;
        }
        if (maxEUinputMin == 0) {
            return false;
        }
        if (EUuse < 0) {
            EUuse = -EUuse;
        }
        if (EUuse > getEUVar() || // not enough power
            (EUtTierVoltage == 0 ? EUuse > getMaxInputEnergy() : (EUtTierVoltage > maxEUinputMax) || // TIER IS
                                                                                                     // BASED ON
                                                                                                     // BEST HATCH!
                                                                                                     // not total
                                                                                                     // EUtEffective
                                                                                                     // input
                (EUtTierVoltage * Amperes - 1) / maxEUinputMin + 1 > eMaxAmpereFlow)) { // EUuse==0? --> (EUuse
                                                                                        // - 1) / maxEUinputMin
                                                                                        // + 1 = 1! //if
            // not too much A
            if (DEBUG_MODE) {
                TecTech.LOGGER.debug("L1 " + EUuse + ' ' + getEUVar() + ' ' + (EUuse > getEUVar()));
                TecTech.LOGGER.debug("L2 " + EUtEffective + ' ' + maxEUinputMax + ' ' + (EUtEffective > maxEUinputMax));
                TecTech.LOGGER.debug("L3 " + Amperes + ' ' + getMaxInputEnergy());
                TecTech.LOGGER.debug(
                    "L4 " + ((EUuse - 1) / maxEUinputMin + 1)
                        + ' '
                        + eMaxAmpereFlow
                        + ' '
                        + ((EUuse - 1) / maxEUinputMin + 1 > eMaxAmpereFlow));
            }
            return false;
        }
        // sub eu
        setEUVar(getEUVar() - EUuse);
        return true;
    }

    public boolean drainEnergyInput(long EUtEffective, long Amperes) {
        long EUuse = EUtEffective * Amperes;
        if (EUuse == 0) {
            return true;
        }
        if (maxEUinputMin == 0) {
            return false;
        }
        if (EUuse < 0) {
            EUuse = -EUuse;
        }
        // not enough power
        if (EUuse > getEUVar() || EUuse > getMaxInputEnergy()) { // EUuse==0? --> (EUuse - 1) / maxEUinputMin + 1 = 1!
                                                                 // //if not too much
            // A
            return false;
        }
        // sub eu
        setEUVar(getEUVar() - EUuse);
        return true;
    }

    // new method
    public final boolean overclockAndPutValuesIn_EM(long EU, int time) { // TODO revise
        if (EU == 0L) {
            setPowerFlow(0);
            mMaxProgresstime = time;
            return true;
        }
        long tempEUt = Math.max(EU, V[1]);
        long tempTier = maxEUinputMax >> 2;
        while (tempEUt < tempTier) {
            tempEUt <<= 2;
            time >>= 1;
            EU = time == 0 ? EU >> 1 : EU << 2; // U know, if the time is less than 1 tick make the machine use less
                                                // power
        }
        if (EU > Integer.MAX_VALUE || EU < Integer.MIN_VALUE) {
            setPowerFlow(Integer.MAX_VALUE - 1);
            mMaxProgresstime = Integer.MAX_VALUE - 1;
            return false;
        }
        setPowerFlow(EU);
        mMaxProgresstime = time == 0 ? 1 : time;
        return true;
    } // Use in EM check recipe return statement if you want overclocking

    /**
     * Use {@link #getMaxInputVoltage()}
     */
    @Deprecated
    public final long getMaxInputVoltageSum() {
        return getMaxInputVoltage();
    }

    /**
     * Use {@link #getMaxInputEu()}
     */
    @Deprecated
    public final long getMaxInputEnergy() {
        return getMaxInputEu();
    }

    /**
     * Use {@link #getMaxInputEu()}
     */
    @Deprecated
    public final long getMaxInputEnergy_EM() {
        return getMaxInputEu();
    }

    // new Method
    public final int getMaxEnergyInputTier_EM() {
        return getTier(maxEUinputMax);
    }

    // new Method
    public final int getMinEnergyInputTier_EM() {
        return getTier(maxEUinputMin);
    }

    public final long getMaxAmpereFlowAtMinTierOfEnergyHatches() {
        return eAmpereFlow;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch> getExoticAndNormalEnergyHatchList() {
        List<GT_MetaTileEntity_Hatch> list = new ArrayList<>();
        list.addAll(mEnergyHatches);
        list.addAll(eEnergyMulti);
        return list;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch> getExoticEnergyHatches() {
        List<GT_MetaTileEntity_Hatch> list = new ArrayList<>();
        list.addAll(eEnergyMulti);
        return list;
    }

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
            TecTech.proxy.broadcast(
                "Multi Explode BOOM! " + getBaseMetaTileEntity().getXCoord()
                    + ' '
                    + getBaseMetaTileEntity().getYCoord()
                    + ' '
                    + getBaseMetaTileEntity().getZCoord());
            StackTraceElement[] ste = Thread.currentThread()
                .getStackTrace();
            TecTech.proxy.broadcast("Multi Explode BOOM! " + ste[2].toString());
            return;
        }
        extraExplosions_EM();
        GT_Pollution.addPollution(getBaseMetaTileEntity(), 600000);
        mInventory[1] = null;
        @SuppressWarnings("unchecked")
        Iterable<MetaTileEntity> allHatches = Iterables.concat(
            mInputBusses,
            mOutputBusses,
            mInputHatches,
            mOutputHatches,
            mDynamoHatches,
            mMufflerHatches,
            mEnergyHatches,
            mMaintenanceHatches,
            eParamHatches,
            eEnergyMulti,
            eUncertainHatches,
            eDynamoMulti,
            eInputData,
            eOutputData);
        for (MetaTileEntity tTileEntity : allHatches) {
            if (tTileEntity != null && tTileEntity.getBaseMetaTileEntity() != null) {
                tTileEntity.getBaseMetaTileEntity()
                    .doExplosion(V[9]);
            }
        }
        getBaseMetaTileEntity().doExplosion(V[15]);
    }

    @Override
    public void doExplosion(long aExplosionPower) {
        if (!TecTech.configTecTech.BOOM_ENABLE) {
            TecTech.proxy.broadcast(
                "Multi DoExplosion BOOM! " + getBaseMetaTileEntity().getXCoord()
                    + ' '
                    + getBaseMetaTileEntity().getYCoord()
                    + ' '
                    + getBaseMetaTileEntity().getZCoord());
            StackTraceElement[] ste = Thread.currentThread()
                .getStackTrace();
            TecTech.proxy.broadcast("Multi DoExplosion BOOM! " + ste[2].toString());
            return;
        }
        explodeMultiblock();
        super.doExplosion(aExplosionPower);
    } // Redirecting to explodemultiblock
      // endregion

    // region adder methods
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
        if (aMetaTileEntity instanceof IDualInputHatch) {
            return mDualInputHatches.add((IDualInputHatch) aMetaTileEntity);
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
        if (aMetaTileEntity instanceof IDualInputHatch) {
            return mDualInputHatches.add((IDualInputHatch) aMetaTileEntity);
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
        if (aMetaTileEntity instanceof IDualInputHatch) {
            ((IDualInputHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDualInputHatches.add((IDualInputHatch) aMetaTileEntity);
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

    // New Method
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

    // NEW METHOD
    public final boolean addElementalInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }

        return false;
    }

    // NEW METHOD
    public final boolean addElementalOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }

        return false;
    }

    // NEW METHOD
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

    // NEW METHOD
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

    // NEW METHOD
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

    // NEW METHOD
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
    // endregion

    protected static <T extends GT_MetaTileEntity_MultiblockBase_EM> IStructureElement<T> classicHatches(
        int casingIndex, int dot, Block casingBlock, int casingMeta) {
        return GT_HatchElementBuilder.<T>builder()
            .atLeast(
                InputBus,
                InputHatch,
                OutputHatch,
                OutputBus,
                Maintenance,
                Muffler,
                HatchElement.EnergyMulti,
                HatchElement.DynamoMulti,
                HatchElement.InputData,
                HatchElement.OutputData,
                HatchElement.Uncertainty)
            .casingIndex(casingIndex)
            .dot(dot)
            .buildAndChain(casingBlock, casingMeta);
    }

    protected static <T extends GT_MetaTileEntity_MultiblockBase_EM> IStructureElement<T> allHatches(int casingIndex,
        int dot, Block casingBlock, int casingMeta) {
        return GT_HatchElementBuilder.<T>builder()
            .atLeast(
                InputBus,
                InputHatch,
                OutputHatch,
                OutputBus,
                Maintenance,
                Muffler,
                HatchElement.EnergyMulti,
                HatchElement.DynamoMulti,
                HatchElement.InputData,
                HatchElement.OutputData,
                HatchElement.Uncertainty)
            .casingIndex(casingIndex)
            .dot(dot)
            .buildAndChain(casingBlock, casingMeta);
    }

    public enum HatchElement implements IHatchElement<GT_MetaTileEntity_MultiblockBase_EM> {

        Param(GT_MetaTileEntity_MultiblockBase_EM::addParametrizerToMachineList, GT_MetaTileEntity_Hatch_Param.class) {

            @Override
            public long count(GT_MetaTileEntity_MultiblockBase_EM t) {
                return t.eParamHatches.size();
            }
        },
        Uncertainty(GT_MetaTileEntity_MultiblockBase_EM::addUncertainToMachineList,
            GT_MetaTileEntity_Hatch_Uncertainty.class) {

            @Override
            public long count(GT_MetaTileEntity_MultiblockBase_EM t) {
                return t.eUncertainHatches.size();
            }
        },
        EnergyMulti(GT_MetaTileEntity_MultiblockBase_EM::addEnergyInputToMachineList,
            GT_MetaTileEntity_Hatch_EnergyMulti.class) {

            @Override
            public long count(GT_MetaTileEntity_MultiblockBase_EM t) {
                return t.eEnergyMulti.size();
            }
        },
        DynamoMulti(GT_MetaTileEntity_MultiblockBase_EM::addDynamoToMachineList,
            GT_MetaTileEntity_Hatch_DynamoMulti.class) {

            @Override
            public long count(GT_MetaTileEntity_MultiblockBase_EM t) {
                return t.eDynamoMulti.size();
            }
        },
        InputData(GT_MetaTileEntity_MultiblockBase_EM::addDataConnectorToMachineList,
            GT_MetaTileEntity_Hatch_InputData.class) {

            @Override
            public long count(GT_MetaTileEntity_MultiblockBase_EM t) {
                return t.eInputData.size();
            }
        },
        OutputData(GT_MetaTileEntity_MultiblockBase_EM::addDataConnectorToMachineList,
            GT_MetaTileEntity_Hatch_OutputData.class) {

            @Override
            public long count(GT_MetaTileEntity_MultiblockBase_EM t) {
                return t.eOutputData.size();
            }
        },;

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_MultiblockBase_EM> adder;

        @SafeVarargs
        HatchElement(IGT_HatchAdder<GT_MetaTileEntity_MultiblockBase_EM> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_MultiblockBase_EM> adder() {
            return adder;
        }
    }

    /**
     * Check if the computation timeout is still active
     *
     * @return True if the timeout is still active or false if the machine should fail
     */
    protected boolean checkComputationTimeout() {
        if (eComputationTimeout > 0) {
            return --eComputationTimeout > 0;
        }
        return false;
    }

    // region ModularUI

    @Override
    public int getGUIWidth() {
        return 198;
    }

    @Override
    public int getGUIHeight() {
        return 192;
    }

    @Override
    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.bindPlayerInventory(buildContext.getPlayer(), new Pos2d(7, 109), getGUITextureSet().getItemSlot());
    }

    public boolean isPowerPassButtonEnabled() {
        return true;
    }

    public boolean isSafeVoidButtonEnabled() {
        return true;
    }

    public boolean isAllowedToWorkButtonEnabled() {
        return true;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO_DARK)
                .setSize(18, 18)
                .setPos(173, 74));
    }

    private static byte LEDCounter = 0;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (doesBindPlayerInventory()) {
            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                    .setPos(4, 4)
                    .setSize(190, 91));
        } else {
            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.BACKGROUND_SCREEN_BLUE_NO_INVENTORY)
                    .setPos(4, 4)
                    .setSize(190, 171));
        }
        final SlotWidget inventorySlot = new SlotWidget(new BaseSlot(inventoryHandler, 1) {

            @Override
            public int getSlotStackLimit() {
                return getInventoryStackLimit();
            }
        });
        if (doesBindPlayerInventory()) {
            builder
                .widget(
                    inventorySlot.setBackground(getGUITextureSet().getItemSlot(), TecTechUITextures.OVERLAY_SLOT_MESH)
                        .setPos(173, 167))
                .widget(
                    new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_HEAT_SINK_SMALL)
                        .setPos(173, 185)
                        .setSize(18, 6));
        }

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, inventorySlot);
        builder.widget(screenElements.setPos(7, 8));

        Widget powerPassButton = createPowerPassButton();
        builder.widget(powerPassButton)
            .widget(new FakeSyncWidget.BooleanSyncer(() -> ePowerPass, val -> ePowerPass = val))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> ePowerPassCover, val -> ePowerPassCover = val));
        Widget safeVoidButton = createSafeVoidButton();
        builder.widget(safeVoidButton)
            .widget(new FakeSyncWidget.BooleanSyncer(() -> eSafeVoid, val -> eSafeVoid = val));
        Widget powerSwitchButton = createPowerSwitchButton();
        builder.widget(powerSwitchButton)
            .widget(new FakeSyncWidget.BooleanSyncer(() -> getBaseMetaTileEntity().isAllowedToWork(), val -> {
                if (val) getBaseMetaTileEntity().enableWorking();
                else getBaseMetaTileEntity().disableWorking();
            }));

        builder.widget(new DrawableWidget() {

            @Override
            public void draw(float partialTicks) {
                super.draw(partialTicks);
                LEDCounter = (byte) ((1 + LEDCounter) % 6);
            }
        }.setDrawable(TecTechUITextures.PICTURE_PARAMETER_BLANK)
            .setPos(5, doesBindPlayerInventory() ? 96 : 176)
            .setSize(166, 12));
        for (int hatch = 0; hatch < 10; hatch++) {
            for (int param = 0; param < 2; param++) {
                int ledID = hatch + param * 10;
                buildContext
                    .addSyncedWindow(LED_WINDOW_BASE_ID + ledID, (player) -> createLEDConfigurationWindow(ledID));
                addParameterLED(builder, hatch, param, true);
                addParameterLED(builder, hatch, param, false);
            }
        }

        if (doesBindPlayerInventory()) {
            builder.widget(
                new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_UNCERTAINTY_MONITOR_MULTIMACHINE)
                    .setPos(173, 96)
                    .setSize(18, 18));
            for (int i = 0; i < 9; i++) {
                final int index = i;
                builder.widget(new DrawableWidget().setDrawable(() -> {
                    UITexture valid = TecTechUITextures.PICTURE_UNCERTAINTY_VALID[index];
                    UITexture invalid = TecTechUITextures.PICTURE_UNCERTAINTY_INVALID[index];
                    switch (eCertainMode) {
                        case 1: // ooo oxo ooo
                            if (index == 4) return eCertainStatus == 0 ? valid : invalid;
                            break;
                        case 2: // ooo xox ooo
                            if (index == 3) return (eCertainStatus & 1) == 0 ? valid : invalid;
                            if (index == 5) return (eCertainStatus & 2) == 0 ? valid : invalid;
                            break;
                        case 3: // oxo xox oxo
                            if (index == 1) return (eCertainStatus & 1) == 0 ? valid : invalid;
                            if (index == 3) return (eCertainStatus & 2) == 0 ? valid : invalid;
                            if (index == 5) return (eCertainStatus & 4) == 0 ? valid : invalid;
                            if (index == 7) return (eCertainStatus & 8) == 0 ? valid : invalid;
                            break;
                        case 4: // xox ooo xox
                            if (index == 0) return (eCertainStatus & 1) == 0 ? valid : invalid;
                            if (index == 2) return (eCertainStatus & 2) == 0 ? valid : invalid;
                            if (index == 6) return (eCertainStatus & 4) == 0 ? valid : invalid;
                            if (index == 8) return (eCertainStatus & 8) == 0 ? valid : invalid;
                            break;
                        case 5: // xox oxo xox
                            if (index == 0) return (eCertainStatus & 1) == 0 ? valid : invalid;
                            if (index == 2) return (eCertainStatus & 2) == 0 ? valid : invalid;
                            if (index == 4) return (eCertainStatus & 4) == 0 ? valid : invalid;
                            if (index == 6) return (eCertainStatus & 8) == 0 ? valid : invalid;
                            if (index == 8) return (eCertainStatus & 16) == 0 ? valid : invalid;
                            break;
                    }
                    return null;
                })
                    .setPos(174 + (index % 3) * 6, 97 + (index / 3) * 6)
                    .setSize(4, 4));
            }
            builder.widget(new FakeSyncWidget.ByteSyncer(() -> eCertainMode, val -> eCertainMode = val))
                .widget(new FakeSyncWidget.ByteSyncer(() -> eCertainStatus, val -> eCertainStatus = val));
        }
    }

    protected ButtonWidget createPowerPassButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isPowerPassButtonEnabled() || ePowerPassCover) {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                ePowerPass = !ePowerPass;
                if (!isAllowedToWorkButtonEnabled()) { // TRANSFORMER HACK
                    if (ePowerPass) {
                        getBaseMetaTileEntity().enableWorking();
                    } else {
                        getBaseMetaTileEntity().disableWorking();
                    }
                }
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                if (!isPowerPassButtonEnabled() && !ePowerPassCover) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_PASS_DISABLED);
                } else {
                    if (ePowerPass) {
                        ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_PASS_ON);
                    } else {
                        ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_PASS_OFF);
                    }
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, doesBindPlayerInventory() ? 116 : 140)
            .setSize(16, 16);
        if (isPowerPassButtonEnabled()) {
            button.addTooltip("Power Pass")
                .setTooltipShowUpDelay(TOOLTIP_DELAY);
        }
        return (ButtonWidget) button;
    }

    protected ButtonWidget createSafeVoidButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isSafeVoidButtonEnabled()) {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                eSafeVoid = !eSafeVoid;
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                if (!isSafeVoidButtonEnabled()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_DISABLED);
                } else {
                    if (eSafeVoid) {
                        ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_ON);
                    } else {
                        ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_OFF);
                    }
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, doesBindPlayerInventory() ? 132 : 156)
            .setSize(16, 16);
        if (isSafeVoidButtonEnabled()) {
            button.addTooltip("Safe Void")
                .setTooltipShowUpDelay(TOOLTIP_DELAY);
        }
        return (ButtonWidget) button;
    }

    protected ButtonWidget createPowerSwitchButton() {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isAllowedToWorkButtonEnabled()) {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                if (getBaseMetaTileEntity().isAllowedToWork()) {
                    getBaseMetaTileEntity().disableWorking();
                } else {
                    getBaseMetaTileEntity().enableWorking();
                }
            }
        })
            .setPlayClickSound(false)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
                if (!isAllowedToWorkButtonEnabled()) {
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
                } else {
                    if (getBaseMetaTileEntity().isAllowedToWork()) {
                        ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
                    } else {
                        ret.add(TecTechUITextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
                    }
                }
                return ret.toArray(new IDrawable[0]);
            })
            .setPos(174, doesBindPlayerInventory() ? 148 : 172)
            .setSize(16, 16);
        if (isAllowedToWorkButtonEnabled()) {
            button.addTooltip("Power Switch")
                .setTooltipShowUpDelay(TOOLTIP_DELAY);
        }
        return (ButtonWidget) button;
    }

    private ModularWindow createLEDConfigurationWindow(int ledID) {
        return ModularWindow.builder(100, 40)
            .setBackground(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
            .setPos(
                (screenSize, mainWindow) -> new Pos2d(
                    (screenSize.width / 2 - mainWindow.getSize().width / 2) - 110,
                    (screenSize.height / 2 - mainWindow.getSize().height / 2)))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(85, 3))
            .widget(
                new NumericWidget().setGetter(() -> parametrization.iParamsIn[ledID])
                    .setSetter(val -> parametrization.iParamsIn[ledID] = val)
                    .setIntegerOnly(false)
                    .modifyNumberFormat(format -> format.setMaximumFractionDigits(8))
                    .setTextColor(Color.LIGHT_BLUE.normal)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setFocusOnGuiOpen(true)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                    .setPos(5, 20)
                    .setSize(90, 15))
            .widget(
                new TextWidget((ledID % 10) + ":" + (ledID / 10) + ":I").setDefaultColor(Color.WHITE.normal)
                    .setTextAlignment(Alignment.Center)
                    .setPos(5, 5))
            .build();
    }

    private void addParameterLED(ModularWindow.Builder builder, int hatch, int param, boolean input) {
        final int parameterIndex = hatch + param * 10;
        final int posIndex = hatch * 2 + param;
        ButtonWidget ledWidget = new ButtonWidget() {

            @Override
            public void draw(float partialTicks) {
                IDrawable texture = null;
                final LedStatus status = input ? parametrization.eParamsInStatus[parameterIndex]
                    : parametrization.eParamsOutStatus[parameterIndex];
                switch (status) {
                    case STATUS_WTF: {
                        int c = LEDCounter;
                        if (c > 4) {
                            c = TecTech.RANDOM.nextInt(5);
                        }
                        switch (c) {
                            case 0:
                                texture = TecTechUITextures.PICTURE_PARAMETER_BLUE[posIndex];
                                break;
                            case 1:
                                texture = TecTechUITextures.PICTURE_PARAMETER_CYAN[posIndex];
                                break;
                            case 2:
                                texture = TecTechUITextures.PICTURE_PARAMETER_GREEN[posIndex];
                                break;
                            case 3:
                                texture = TecTechUITextures.PICTURE_PARAMETER_ORANGE[posIndex];
                                break;
                            case 4:
                                texture = TecTechUITextures.PICTURE_PARAMETER_RED[posIndex];
                                break;
                        }
                        break;
                    }
                    case STATUS_WRONG: // fallthrough
                        if (LEDCounter < 2) {
                            texture = TecTechUITextures.PICTURE_PARAMETER_BLUE[posIndex];
                            break;
                        } else if (LEDCounter < 4) {
                            texture = TecTechUITextures.PICTURE_PARAMETER_RED[posIndex];
                            break;
                        }
                    case STATUS_OK: // ok
                        texture = TecTechUITextures.PICTURE_PARAMETER_GREEN[posIndex];
                        break;
                    case STATUS_TOO_LOW: // too low blink
                        if (LEDCounter < 3) {
                            texture = TecTechUITextures.PICTURE_PARAMETER_BLUE[posIndex];
                            break;
                        }
                    case STATUS_LOW: // too low
                        texture = TecTechUITextures.PICTURE_PARAMETER_CYAN[posIndex];
                        break;
                    case STATUS_TOO_HIGH: // too high blink
                        if (LEDCounter < 3) {
                            texture = TecTechUITextures.PICTURE_PARAMETER_RED[posIndex];
                            break;
                        }
                    case STATUS_HIGH: // too high
                        texture = TecTechUITextures.PICTURE_PARAMETER_ORANGE[posIndex];
                        break;
                    case STATUS_NEUTRAL:
                        if (LEDCounter < 3) {
                            GL11.glColor4f(.85f, .9f, .95f, .5F);
                        } else {
                            GL11.glColor4f(.8f, .9f, 1f, .5F);
                        }
                        texture = TecTechUITextures.PICTURE_PARAMETER_GRAY;
                        break;
                    case STATUS_UNDEFINED:
                        if (LEDCounter < 3) {
                            GL11.glColor4f(.5f, .1f, .15f, .5F);
                        } else {
                            GL11.glColor4f(0f, .1f, .2f, .5F);
                        }
                        texture = TecTechUITextures.PICTURE_PARAMETER_GRAY;
                        break;
                    case STATUS_UNUSED:
                    default:
                        // no-op
                        break;
                }
                setBackground(texture);
                super.draw(partialTicks);
                GL11.glColor4f(1f, 1f, 1f, 1f);
            }
        }.setOnClick((clickData, widget) -> {
            if (!widget.isClient() && input
                && parametrization.eParamsInStatus[parameterIndex] != LedStatus.STATUS_UNUSED) {
                // We don't use CloseAllButMain here in case MB implementation adds their own window
                for (int i = 0; i < parametrization.eParamsInStatus.length; i++) {
                    if (widget.getContext()
                        .isWindowOpen(LED_WINDOW_BASE_ID + i)) {
                        widget.getContext()
                            .closeWindow(LED_WINDOW_BASE_ID + i);
                    }
                }
                widget.getContext()
                    .openSyncedWindow(LED_WINDOW_BASE_ID + parameterIndex);
            }
        });
        builder.widget(ledWidget.dynamicTooltip(() -> {
            if (input) {
                return getFullLedDescriptionIn(hatch, param);
            } else {
                return getFullLedDescriptionOut(hatch, param);
            }
        })
            .setPos(12 + posIndex * 8, (doesBindPlayerInventory() ? 97 : 177) + (input ? 0 : 1) * 6)
            .setSize(6, 4));
        if (input) {
            builder
                .widget(
                    new FakeSyncWidget.ByteSyncer(
                        () -> parametrization.eParamsInStatus[parameterIndex].getOrdinalByte(),
                        val -> parametrization.eParamsInStatus[parameterIndex] = LedStatus.getStatus(val))
                            .setOnClientUpdate(val -> ledWidget.notifyTooltipChange()))
                .widget(
                    new FakeSyncWidget.DoubleSyncer(
                        () -> parametrization.iParamsIn[parameterIndex],
                        val -> parametrization.iParamsIn[parameterIndex] = val)
                            .setOnClientUpdate(val -> ledWidget.notifyTooltipChange()));
        } else {
            builder
                .widget(
                    new FakeSyncWidget.ByteSyncer(
                        () -> parametrization.eParamsOutStatus[parameterIndex].getOrdinalByte(),
                        val -> parametrization.eParamsOutStatus[parameterIndex] = LedStatus.getStatus(val))
                            .setOnClientUpdate(val -> ledWidget.notifyTooltipChange()))
                .widget(
                    new FakeSyncWidget.DoubleSyncer(
                        () -> parametrization.iParamsOut[parameterIndex],
                        val -> parametrization.iParamsOut[parameterIndex] = val)
                            .setOnClientUpdate(val -> ledWidget.notifyTooltipChange()));
        }
    }

    // endregion
}
