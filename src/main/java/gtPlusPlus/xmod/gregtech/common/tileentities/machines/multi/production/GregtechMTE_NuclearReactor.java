package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMTE_NuclearReactor extends GregtechMeta_MultiBlockBase<GregtechMTE_NuclearReactor>
    implements ISurvivalConstructable {

    protected int mFuelRemaining = 0;

    private int mCasing;
    private static IStructureDefinition<GregtechMTE_NuclearReactor> STRUCTURE_DEFINITION = null;

    public GregtechMTE_NuclearReactor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_NuclearReactor(final String aName) {
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
        return GTPPRecipeMaps.liquidFluorineThoriumReactorRecipes;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Liquid Fluoride Thorium Reactor.")
            .addInfo("Produces energy and new elements from Radioactive Beta Decay!")
            .addInfo("Input LFTB and a molten salt as fuel, and match the 4 Buffered Dynamo Hatches:")
            .addInfo("LFTR Fuel 1 (4 EV Hatches), LFTR Fuel 2 (4 IV Hatches), LFTR Fuel 3 (4 LuV Hatches)")
            .addInfo("If using better hatches for a worse fuel, only 1 hatch will output EU")
            .addInfo("Outputs U233 every 10 seconds, on average, while the reactor is running")
            .addInfo("Check NEI to see the other 3 outputs - they differ between fuels")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(7, 4, 7, true)
            .addController("Bottom Center")
            .addCasingInfoMin("Hastelloy-N Reactor Casing", 27, false)
            .addCasingInfoMin("Zeron-100 Reactor Shielding", 26, false)
            .addInputHatch("Top or bottom layer edges", 1)
            .addOutputHatch("Top or bottom layer edges", 1)
            .addDynamoHatch("Top or bottom layer edges", 1)
            .addMaintenanceHatch("Top or bottom layer edges", 1)
            .addMufflerHatch("Top 3x3", 2)
            .addStructureInfo("All dynamos must be between EV and LuV tier.")
            .addStructureInfo("All other hatches must be IV+ tier.")
            .addStructureInfo("4x Output Hatches or 1x Output Hatch (ME), 1+ Input Hatches, 4x Dynamo Hatches")
            .addStructureInfo("2x Maintenance Hatches, 4x Mufflers")
            .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public String[] getExtraInfoData() {
        final String tRunning = (this.mMaxProgresstime > 0 ? "Reactor running" : "Reactor stopped");
        final String tMaintainance = (this.getIdealStatus() == this.getRepairStatus() ? "No Maintainance issues"
            : "Needs Maintainance");

        return new String[] { "Liquid Fluoride Thorium Reactor", tRunning, tMaintainance,
            "Current Output: " + this.lEUt + " EU/t", "Fuel Remaining: " + this.mFuelRemaining + " Litres",
            "Current Efficiency: " + (this.mEfficiency / 5) + "%", "Current Efficiency (Raw): " + (this.mEfficiency),
            "It requires you to have 100% Efficiency." };
    }

    @Override
    public boolean allowCoverOnSide(final ForgeDirection side, final GT_ItemStack aStack) {
        return side != this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        boolean aWarmedUp = this.mEfficiency == this.getMaxEfficiency(null);
        if (!aBaseMetaTileEntity.isActive() || !aWarmedUp) {
            if (side == facing) {
                if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE)
                        .extFacing()
                        .build() };
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)
                        .extFacing()
                        .build() };
            }
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)) };
        } else if (aBaseMetaTileEntity.isActive() && aWarmedUp) {
            if (side == facing) {
                if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE)
                        .extFacing()
                        .build() };
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)),
                    TextureFactory.builder()
                        .addIcon(Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)
                        .extFacing()
                        .build() };
            }
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)) };
    }

    public final boolean addNuclearReactorEdgeList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo dynamo
                && dynamo.getTierForStructure() >= 4
                && dynamo.getTierForStructure() <= 6) {
                    return addToMachineList(aTileEntity, aBaseCasingIndex);
                } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input hatch
                    && hatch.getTierForStructure() >= 5) {
                        return addToMachineList(aTileEntity, aBaseCasingIndex);
                    } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output hatch
                        && hatch.getTierForStructure() >= 5) {
                            return addToMachineList(aTileEntity, aBaseCasingIndex);
                        }
        }
        return false;
    }

    public final boolean addNuclearReactorTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler hatch && hatch.getTierForStructure() >= 5) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public IStructureDefinition<GregtechMTE_NuclearReactor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_NuclearReactor>builder()
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
                        buildHatchAdder(GregtechMTE_NuclearReactor.class).atLeast(Maintenance)
                            .casingIndex(TAE.GTPP_INDEX(12))
                            .dot(1)
                            .build(),
                        buildHatchAdder(GregtechMTE_NuclearReactor.class).atLeast(InputHatch, OutputHatch)
                            .adder(GregtechMTE_NuclearReactor::addNuclearReactorEdgeList)
                            .hatchItemFilterAnd(t -> filterByMTETier(5, Integer.MAX_VALUE))
                            .casingIndex(TAE.GTPP_INDEX(12))
                            .dot(1)
                            .build(),
                        buildHatchAdder(GregtechMTE_NuclearReactor.class).atLeast(Dynamo)
                            .adder(GregtechMTE_NuclearReactor::addNuclearReactorEdgeList)
                            .hatchItemFilterAnd(t -> filterByMTETier(4, 6))
                            .casingIndex(TAE.GTPP_INDEX(12))
                            .dot(1)
                            .build(),
                        onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 12))))
                .addElement(
                    'X',
                    buildHatchAdder(GregtechMTE_NuclearReactor.class).atLeast(Muffler)
                        .adder(GregtechMTE_NuclearReactor::addNuclearReactorTopList)
                        .hatchItemFilterAnd(t -> filterByMTETier(5, Integer.MAX_VALUE))
                        .casingIndex(TAE.GTPP_INDEX(12))
                        .dot(1)
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
        return survivialBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        if (checkPiece(mName, 3, 3, 0) && mCasing >= 27) {
            if ((mOutputHatches.size() >= 3 || canDumpFluidToME()) && mInputHatches.size() >= 1
                && mDynamoHatches.size() == 4
                && mMufflerHatches.size() == 4
                && mMaintenanceHatches.size() == 2) {
                this.turnCasingActive(false);
                return true;
            }
        }
        return false;
    }

    // Alk's Life Lessons from Greg.
    /*
     * [23:41:15] <GregoriusTechneticies> xdir and zdir are x2 and not x3 [23:41:26] <GregoriusTechneticies> thats you
     * issue [23:44:33] <Alkalus> mmm? [23:44:49] <Alkalus> Should they be x3? [23:44:50] <GregoriusTechneticies> you
     * just do a x2, what is for a 5x5 multiblock [23:45:01] <GregoriusTechneticies> x3 is for a 7x7 one [23:45:06]
     * <Alkalus> I have no idea what that value does, tbh.. [23:45:15] <GregoriusTechneticies> its the offset [23:45:23]
     * <Alkalus> Debugging checkMachine has been a pain and I usually trash designs that don't work straight up..
     * [23:45:28] <GregoriusTechneticies> it determines the horizontal middle of the multiblock [23:45:47]
     * <GregoriusTechneticies> which is in your case THREE blocks away from the controller [23:45:51] <Alkalus> Ahh
     * [23:45:57] <GregoriusTechneticies> and not 2 [23:46:06] <Alkalus> Noted, thanks :D
     */

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(final ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_NuclearReactor(this.mName);
    }

    public boolean turnCasingActive(final boolean status) {
        // TODO
        if (this.mDynamoHatches != null) {
            for (final GT_MetaTileEntity_Hatch_Dynamo hatch : this.mDynamoHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mMufflerHatches != null) {
            for (final GT_MetaTileEntity_Hatch_Muffler hatch : this.mMufflerHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mOutputHatches != null) {
            for (final GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mInputHatches != null) {
            for (final GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
                hatch.updateTexture(status ? TAE.GTPP_INDEX(13) : TAE.GTPP_INDEX(12));
            }
        }
        if (this.mMaintenanceHatches != null) {
            for (final GT_MetaTileEntity_Hatch_Maintenance hatch : this.mMaintenanceHatches) {
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
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe.mSpecialValue * 4L, recipe.mDuration);
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
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                mFuelRemaining = 0;
                int li2bef4 = 0;
                FluidStack aFuelFluid = null;
                for (FluidStack aFluidInput : recipe.mFluidInputs) {
                    if (!aFluidInput.getFluid()
                        .equals(NUCLIDE.Li2BeF4.getFluid())) {
                        aFuelFluid = aFluidInput;
                        break;
                    }
                }
                if (aFuelFluid != null) {
                    for (FluidStack fluidStack : getStoredFluids()) {
                        if (fluidStack.isFluidEqual(aFuelFluid)) {
                            mFuelRemaining += fluidStack.amount;
                        } else if (fluidStack.getFluid()
                            .equals(NUCLIDE.Li2BeF4.getFluid())) {
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
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public void explodeMultiblock() {
        this.mInventory[1] = null;
        long explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
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
                if (this.mEfficiency == this.getMaxEfficiency(null)) {
                    this.turnCasingActive(true);
                } else {
                    this.turnCasingActive(false);
                }
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
                this.addOutput(ELEMENT.getInstance().URANIUM233.getFluidStack(MathUtils.randInt(1, 10)));
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
