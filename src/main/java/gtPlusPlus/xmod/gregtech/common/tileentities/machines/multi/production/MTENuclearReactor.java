package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.structure.error.TranslatableText;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputME;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class MTENuclearReactor extends GTPPMultiBlockBase<MTENuclearReactor> implements ISurvivalConstructable {

    protected int mFuelRemaining = 0;

    private int mCasing;
    private static IStructureDefinition<MTENuclearReactor> STRUCTURE_DEFINITION = null;

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
    public String getMachineType() {
        return "Reactor";
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
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Liquid Fluoride Thorium Reactor")
            .addInfo("Produces energy and new elements from Radioactive Beta Decay!")
            .addInfo("Input LFTB and a molten salt as fuel, and match the four 4A dynamo hatches:")
            .addInfo("LFTR Fuel 1 (4 EV Hatches), LFTR Fuel 2 (4 IV Hatches), LFTR Fuel 3 (4 LuV Hatches)")
            .addInfo("If using better hatches for a worse fuel, only 1 hatch outputs EU")
            .addInfo("Outputs U-233 every 10 seconds, on average, while the reactor is running")
            .addInfo("Check NEI to see the other 3 outputs - they differ between fuels")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 4, 7, true)
            .addController("Front bottom center")
            .addCasing("27-86", "Hastelloy-N Reactor Casing", false)
            .addCasing("26", "Reactor Shield Casing", false)
            .addDynamoHatch("4", "Any edge casing (EV-LuV)", 1)
            .addMaintenanceHatch("1", "Any edge casing", 1)
            .addMufflerHatch("4", "Any top 3x3 center casing (IV+)", 2)
            .addInputHatch("1+", "Any edge casing (IV+)", 1)
            .addOutputHatch("4+", "Any edge casing (IV+)", 1)
            .addAir("Interior of the structure")
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DynamoLimit"))
            .addStructureFooter("One ME output hatch can replace the four regular output hatches")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        boolean aWarmedUp = this.mEfficiency == this.getMaxEfficiency(null);
        if (!aBaseMetaTileEntity.isActive() || !aWarmedUp) {
            if (side == facing) {
                if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR_ACTIVE)
                        .extFacing()
                        .build() };
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR)
                        .extFacing()
                        .build() };
            }
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)) };
        } else if (aBaseMetaTileEntity.isActive() && aWarmedUp) {
            if (side == facing) {
                if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR_ACTIVE)
                        .extFacing()
                        .build() };
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_NUCLEAR_REACTOR)
                        .extFacing()
                        .build() };
            }
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)) };
    }

    public final boolean addNuclearReactorEdgeList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchMaintenance || aMetaTileEntity instanceof MTEHatchDynamo
            || aMetaTileEntity instanceof MTEHatchInput
            || aMetaTileEntity instanceof MTEHatchOutput) {
            return addToMachineList(aTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public final boolean addNuclearReactorTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchMuffler) {
            return addToMachineList(aTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public IStructureDefinition<MTENuclearReactor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTENuclearReactor>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "CCCCCCC", "COOOOOC", "COXXXOC", "COXXXOC", "COXXXOC", "COOOOOC", "CCCCCCC" },
                            { "GGGGGGG", "G-----G", "G-----G", "G-----G", "G-----G", "G-----G", "GGGGGGG" },
                            { "GGGGGGG", "G-----G", "G-----G", "G-----G", "G-----G", "G-----G", "GGGGGGG" },
                            { "CCC~CCC", "COOOOOC", "COOOOOC", "COOOOOC", "COOOOOC", "COOOOOC", "CCCCCCC" }, }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTENuclearReactor.class).atLeast(Maintenance)
                            .casingIndex(TAE.GTPP_INDEX(12))
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTENuclearReactor.class).atLeast(InputHatch, OutputHatch)
                            .adder(MTENuclearReactor::addNuclearReactorEdgeList)
                            .casingIndex(TAE.GTPP_INDEX(12))
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTENuclearReactor.class).atLeast(Dynamo)
                            .adder(MTENuclearReactor::addNuclearReactorEdgeList)
                            .casingIndex(TAE.GTPP_INDEX(12))
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 12))))
                .addElement(
                    'X',
                    buildHatchAdder(MTENuclearReactor.class).atLeast(Muffler)
                        .adder(MTENuclearReactor::addNuclearReactorTopList)
                        .casingIndex(TAE.GTPP_INDEX(12))
                        .hint(2)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 12))))
                .addElement('O', ofBlock(ModBlocks.blockCasingsMisc, 12))
                .addElement('G', ofBlock(ModBlocks.blockCasingsMisc, 13))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mCasing = 0;
        if (!checkPiece(mName, 3, 3, 0, errors)) return;
        checkCasingMin(errors, mCasing, 27);
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
        if (errors.isEmpty()) {
            this.turnCasingActive(false);
        }
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 4000;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTENuclearReactor(this.mName);
    }

    public boolean turnCasingActive(final boolean status) {
        // TODO
        if (this.mDynamoHatches != null) {
            for (final MTEHatchDynamo hatch : this.mDynamoHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mMufflerHatches != null) {
            for (final MTEHatchMuffler hatch : this.mMufflerHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mOutputHatches != null) {
            for (final MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mInputHatches != null) {
            for (final MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mMaintenanceHatches != null) {
            for (final MTEHatchMaintenance hatch : this.mMaintenanceHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        return true;
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
                mFuelRemaining = 0;
                int li2bef4 = 0;
                FluidStack aFuelFluid = null;
                for (FluidStack aFluidInput : recipe.mFluidInputs) {
                    if (!aFluidInput.getFluid()
                        .equals(MaterialsNuclides.Li2BeF4.getFluid())) {
                        aFuelFluid = aFluidInput;
                        break;
                    }
                }
                if (aFuelFluid != null) {
                    for (FluidStack fluidStack : inputFluids) {
                        if (fluidStack.isFluidEqual(aFuelFluid)) {
                            mFuelRemaining += fluidStack.amount;
                        } else if (fluidStack.getFluid()
                            .equals(MaterialsNuclides.Li2BeF4.getFluid())) {
                                li2bef4 += fluidStack.amount;
                            }
                    }
                }
                if (mFuelRemaining < 100) {
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
        this.mLastRecipe = null;
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
        this.mInventory[1] = null;
        long explodevalue;
        for (final MetaTileEntity tTileEntity : this.mInputBusses) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mOutputBusses) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mInputHatches) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mOutputHatches) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mDynamoHatches) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mMufflerHatches) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mEnergyHatches) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        for (final MetaTileEntity tTileEntity : this.mMaintenanceHatches) {
            explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(explodevalue);
        }
        explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
        this.getBaseMetaTileEntity()
            .doExplosion(explodevalue);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.getWorld().isRemote) {
            if (aBaseMetaTileEntity.isActive()) {
                // Set casings active if we're warmed up.
                this.turnCasingActive(this.mEfficiency == this.getMaxEfficiency(null));
            } else {
                this.turnCasingActive(false);
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // See if we're warmed up.
        if (this.mEfficiency == this.getMaxEfficiency(null)) {
            // Try output some Uranium-233
            if (MathUtils.randInt(1, 300) == 1) {
                this.addOutputPartial(
                    MaterialsElements.getInstance().URANIUM233.getFluidStack(MathUtils.randInt(1, 10)));
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mFuelRemaining", this.mFuelRemaining);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mFuelRemaining = aNBT.getInteger("mFuelRemaining");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
