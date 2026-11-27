package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTRecipeConstants.LFTR_OUTPUT_POWER;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.structure.error.TranslatableText;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.pollution.Pollution;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.util.math.MathUtils;

@IMetaTileEntity.SkipGenerateDescription
public class MTENuclearReactor extends MTEExtendedPowerMultiBlockBase<MTENuclearReactor>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int remainingFuel = 0;

    private int casingAmount;
    // spotless:off
    private static final String[][] structure = {
        { "CCCCCCC", "COOOOOC", "COXXXOC", "COXXXOC", "COXXXOC", "COOOOOC", "CCCCCCC" },
        { "GGGGGGG", "G-----G", "G-----G", "G-----G", "G-----G", "G-----G", "GGGGGGG" },
        { "GGGGGGG", "G-----G", "G-----G", "G-----G", "G-----G", "G-----G", "GGGGGGG" },
        { "CCC~CCC", "COOOOOC", "COOOOOC", "COOOOOC", "COOOOOC", "COOOOOC", "CCCCCCC" }, };
    // spotless:on
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTENuclearReactor> STRUCTURE_DEFINITION = null;

    private static final int U233_CHANCE_TICKS = 300;
    private static final int WARMUP_SECONDS = 250;

    public MTENuclearReactor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTENuclearReactor(final String aName) {
        super(aName);
    }

    @Override
    public long maxEUStore() {
        return (640000000L * (Math.min(16, this.mEnergyHatches.size()))) / 16L;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.liquidFluorineThoriumReactorRecipes;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.molten_salt_reactor"))
            .addMarkdown(
                new ResourceLocation("gregtech", "nuclear-reactor"),
                ImmutableMap.<String, Object>builder()
                    .put("warmup", formatNumber(WARMUP_SECONDS))
                    .put("tier1", GTUtility.getColoredTierNameFromTier((byte) VoltageIndex.EV))
                    .put("tier2", GTUtility.getColoredTierNameFromTier((byte) VoltageIndex.IV))
                    .put("tier3", GTUtility.getColoredTierNameFromTier((byte) VoltageIndex.LuV))
                    .build())
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 4, 7, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("27-86", Casings.HastelloyNReactorCasing.getLocalizedName(), false)
            .addCasing("26", Casings.ReactorShieldCasing.getLocalizedName(), false)
            .addDynamoHatch("4", StatCollector.translateToLocalFormatted("gt.mbtt.structure.any_edge_casing_tier", "EV-LuV"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_edge_casing"), 1)
            .addMufflerHatch("4", StatCollector.translateToLocalFormatted("gt.mbtt.structure.any_top_3x3_center_casing_tier", "IV+"), 2)
            .addInputHatch("1+", StatCollector.translateToLocalFormatted("gt.mbtt.structure.any_edge_casing_tier", "IV+"), 1)
            .addOutputHatch("4+", StatCollector.translateToLocalFormatted("gt.mbtt.structure.any_edge_casing_tier", "IV+"), 1)
            .addAir(StatCollector.translateToLocal("gt.mbtt.structure.interior"))
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DynamoLimit"))
            .addStructureFooter(StatCollector.translateToLocal("gt.mbtt.structure.me_output_hatch_replaces_four"))
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR,
            Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR,
            Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR_ACTIVE,
            Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR_ACTIVE);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.HastelloyNReactorCasing.getCasingTexture();
    }

    @Override
    public IStructureDefinition<MTENuclearReactor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTENuclearReactor>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTENuclearReactor.class).atLeast(Maintenance, InputHatch, OutputHatch, Dynamo)
                            .casingIndex(Casings.HastelloyNReactorCasing.textureId)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.casingAmount, Casings.HastelloyNReactorCasing.asElement())))
                .addElement(
                    'X',
                    buildHatchAdder(MTENuclearReactor.class).atLeast(Muffler)
                        .casingIndex(Casings.HastelloyNReactorCasing.textureId)
                        .hint(2)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.HastelloyNReactorCasing.asElement())))
                .addElement('O', Casings.HastelloyNReactorCasing.asElement())
                .addElement('G', Casings.ReactorShieldCasing.asElement())
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0, errors)) return;
        checkCasingMin(errors, casingAmount, 27);
        boolean hasNoMEOutputHatch = GTUtility.getMTEsOfType(mOutputHatches, MTEHatchOutputME.class)
            .isEmpty();
        checkHatchExact(errors, Dynamo, 4);
        checkHasMaintenanceHatch(errors);
        checkHatchExact(errors, Muffler, 4);
        checkHasInputHatch(errors);

        if (mOutputHatches.size() < 4 && hasNoMEOutputHatch) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, OutputHatch, mOutputHatches.size(), 4));
        }

        for (MTEHatchMuffler hatch : mMufflerHatches) {
            if (hatch.getTierForStructure() < 5) {
                errors.add(
                    StructureErrors.of(
                        "GT5U.gui.text.structure_error.hatch_tier_too_low",
                        TranslatableText.hatchName(Muffler),
                        TranslatableText.literal(VN[5])));
                break;
            }
        }
        for (MTEHatchDynamo hatch : mDynamoHatches) {
            if (hatch.getTierForStructure() < 4) {
                errors.add(
                    StructureErrors.of(
                        "GT5U.gui.text.structure_error.hatch_tier_too_low",
                        TranslatableText.hatchName(Dynamo),
                        TranslatableText.literal(VN[4])));
                break;
            }
            if (hatch.getTierForStructure() > 6) {
                errors.add(
                    StructureErrors.of(
                        "GT5U.gui.text.structure_error.hatch_tier_too_high",
                        TranslatableText.hatchName(Dynamo),
                        TranslatableText.literal(VN[6])));
                break;
            }
        }
        for (MTEHatchInput hatch : mInputHatches) {
            if (hatch.getTierForStructure() < 5) {
                errors.add(
                    StructureErrors.of(
                        "GT5U.gui.text.structure_error.hatch_tier_too_low",
                        TranslatableText.hatchName(InputHatch),
                        TranslatableText.literal(VN[5])));
                break;
            }
        }
        for (MTEHatchOutput hatch : mOutputHatches) {
            if (hatch.getTierForStructure() < 5) {
                errors.add(
                    StructureErrors.of(
                        "GT5U.gui.text.structure_error.hatch_tier_too_low",
                        TranslatableText.hatchName(OutputHatch),
                        TranslatableText.literal(VN[5])));
                break;
            }
        }
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 4000;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTENuclearReactor(this.mName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return OverclockCalculator
                    .ofNoOverclock(recipe.getMetadataOrDefault(LFTR_OUTPUT_POWER, 0) * 4L, recipe.mDuration);
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (!result.wasSuccessful()) {
                    resetMultiProcessing();
                }
                return result;
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                remainingFuel = 0;
                int li2bef4 = 0;
                FluidStack fuelFluid = null;
                for (FluidStack fluidInput : recipe.mFluidInputs) {
                    if (!fluidInput.getFluid()
                        .equals(MaterialsNuclides.Li2BeF4.getFluid())) {
                        fuelFluid = fluidInput;
                        break;
                    }
                }
                if (fuelFluid != null) {
                    for (FluidStack fluidStack : inputFluids) {
                        if (fluidStack.isFluidEqual(fuelFluid)) {
                            remainingFuel += fluidStack.amount;
                        } else if (fluidStack.getFluid()
                            .equals(MaterialsNuclides.Li2BeF4.getFluid())) {
                                li2bef4 += fluidStack.amount;
                            }
                    }
                }
                if (remainingFuel < 100) {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
                if (li2bef4 < 200) {
                    return SimpleCheckRecipeResult.ofFailure("no_li2bef4");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        };
    }

    protected void resetMultiProcessing() {
        this.mEfficiency = 0;
        stopMachine(ShutDownReasonRegistry.NONE);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        // Warm up for 4~ minutes
        if (mEfficiency < this.getMaxEfficiency(null)) {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = 2;
            this.lEUt = 0;
            return SimpleCheckRecipeResult.ofSuccess("warm_up");
        }
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) {
            // We produce EU, so we negate the value, if negative
            if (lEUt < 0) {
                lEUt = -lEUt;
            }
        }
        return result;
    }

    @Override
    public void explodeMultiblock() {
        GTLog.writeExplosionLog(this, "MultiBlockExplosion");

        Pollution.addPollution(getBaseMetaTileEntity(), GTMod.proxy.mPollutionOnExplosion);
        mInventory[1] = null;
        // noinspection unchecked
        Iterable<MetaTileEntity> allHatches = Iterables
            .concat(mInputHatches, mOutputHatches, mDynamoHatches, mMufflerHatches, mMaintenanceHatches);
        for (MetaTileEntity hatch : allHatches) {
            if (hatch != null && hatch.getBaseMetaTileEntity() != null) {
                hatch.getBaseMetaTileEntity()
                    .doExplosion(MathUtils.randLong(Integer.MAX_VALUE, 8589934588L));
            }
        }
        getBaseMetaTileEntity().doExplosion(MathUtils.randLong(Integer.MAX_VALUE, 8589934588L));
    }

    @Override
    public boolean needsClientTick() {
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // See if we're warmed up.
        if (this.mEfficiency == this.getMaxEfficiency(null)) {
            // Try output some Uranium-233
            if (MathUtils.randInt(1, U233_CHANCE_TICKS) == 1) {
                this.addOutputPartial(
                    MaterialsElements.getInstance().URANIUM233.getFluidStack(MathUtils.randInt(1, 10)));
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mFuelRemaining", this.remainingFuel);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.remainingFuel = aNBT.getInteger("mFuelRemaining");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
