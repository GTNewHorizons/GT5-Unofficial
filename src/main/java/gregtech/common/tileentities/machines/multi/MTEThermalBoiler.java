package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEThermalBoiler extends MTEExtendedPowerMultiBlockBase<MTEThermalBoiler>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 1;

    private int casingAmountThermalContainment;
    private int casingAmountThermalProcessing;
    private int casingAmountRobust;
    private static IStructureDefinition<MTEThermalBoiler> STRUCTURE_DEFINITION = null;

    private static final int lavaFilterResilience = 30; // Damage lava filter with 1/n probability every operation.
    private int dryHeatCounter = 0; // Counts up to dryHeatMaximum to check for explosion conditions.
    private static final int dryHeatMaximum = 10; // 10 consecutive operations without water = BOOM

    protected final ArrayList<MTEHatchInput> mHotLiquidInputHatches = new ArrayList<>();
    protected final ArrayList<MTEHatchOutput> mHotLiquidOutputHatches = new ArrayList<>();
    protected final ArrayList<MTEHatchInput> mCoolantInputHatches = new ArrayList<>();
    protected final ArrayList<MTEHatchOutput> mCoolantOutputHatches = new ArrayList<>();

    public MTEThermalBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEThermalBoiler(String aName) {
        super(aName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEThermalBoiler(this.mName);
    }

    private boolean addCoolantHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;

        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            mCoolantInputHatches.add(tHatch);
            mInputHatches.add(tHatch);
            return true;
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            mCoolantOutputHatches.add(tHatch);
            mOutputHatches.add(tHatch);
            return true;
        }
        return false;
    }

    private boolean addHotLiquidHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;

        if (aMetaTileEntity instanceof MTEHatchInput tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            tHatch.mRecipeMap = getRecipeMap();
            mInputHatches.add(tHatch);
            mHotLiquidInputHatches.add(tHatch);
            return true;
        }
        if (aMetaTileEntity instanceof MTEHatchOutput tHatch) {
            tHatch.updateTexture(aBaseCasingIndex);
            mOutputHatches.add(tHatch);
            mHotLiquidOutputHatches.add(tHatch);
            return true;
        }
        return false;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (aStack != null && aStack.getItem() == ItemList.Component_LavaFilter.getItem()) ? 1 : 0;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.thermalBoilerRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            // Only test against the first fluid input in the recipe.
            // We still want to run if we lack water (and subsequently explode).
            @NotNull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                if (lastRecipe != null && depleteInput(lastRecipe.mFluidInputs[0], true)) {
                    return Stream.of(lastRecipe);
                }
                if (map == null) {
                    return Stream.empty();
                }
                return map.getAllRecipes()
                    .stream()
                    .filter(recipe -> depleteInput(recipe.mFluidInputs[0], true));
            }

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                GTRecipe adjustedRecipe = recipe.copy();

                // Hack the recipe logic to not consume water, so that we can explode.
                for (FluidStack inputFluid : adjustedRecipe.mFluidInputs) {
                    if (inputFluid != null && (inputFluid.getFluid() == FluidRegistry.WATER
                        || inputFluid.getFluid() == GTModHandler.getDistilledWater(1)
                            .getFluid())) {
                        inputFluid.amount = 0;
                    }
                }

                // If we don't have a lava filter, remove non-obsidian outputs
                // so that output space for them is not required if void protection is on.
                if (!findLavaFilter()) {
                    for (ItemStack outputItem : adjustedRecipe.mOutputs) {
                        if (outputItem != null && outputItem.getItem() != Item.getItemFromBlock(Blocks.obsidian)) {
                            outputItem.stackSize = 0;
                        }
                    }
                }
                return super.createParallelHelper(adjustedRecipe);
            }
        };
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (!hasValidHotInput()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int hotSpace = mHotLiquidOutputHatches.stream()
            .mapToInt(h -> h.getCapacity() - h.getFluidAmount())
            .sum();
        if (hotSpace <= 0) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        int coldSpace = mCoolantOutputHatches.stream()
            .mapToInt(h -> h.getCapacity() - h.getFluidAmount())
            .sum();
        if (coldSpace <= 0) {
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        }

        int efficiency = mEfficiency;
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) {
            mEfficiency = efficiency;
            mEfficiencyIncrease = mMaxProgresstime * getEfficiencyIncrease();

            if (mOutputFluids != null) {
                for (FluidStack outputFluid : mOutputFluids) {
                    if (outputFluid != null && (outputFluid.getFluid() == Materials.Steam.getGas(1)
                        .getFluid() || outputFluid.getFluid()
                            == GTModHandler.getSuperHeatedSteam(1)
                                .getFluid())) {

                        if (outputFluid.getFluid() == Materials.Steam.getGas(1)
                            .getFluid()) {
                            lEUt = outputFluid.amount / mMaxProgresstime / 2;
                        } else {
                            lEUt = outputFluid.amount / mMaxProgresstime;
                        }

                        if (mEfficiency < getMaxEfficiency(null)) {
                            outputFluid.amount = Math
                                .max(1, (outputFluid.amount * mEfficiency) / getMaxEfficiency(null));
                        }

                        if (!useWater(outputFluid.amount)) {
                            outputFluid.amount = 0;
                            lEUt = 0;
                        }
                    }
                }
            }

            if (mOutputItems != null && mOutputItems.length > 0) {
                if (!damageLavaFilter()) {
                    for (ItemStack outputItem : mOutputItems) {
                        if (outputItem != null && outputItem.getItem() != Item.getItemFromBlock(Blocks.obsidian)) {
                            outputItem.stackSize = 0;
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean findLavaFilter() {
        if (getControllerSlot() == null) {
            for (var bus : mInputBusses) {
                for (ItemStack stack : bus.mInventory) {
                    if (stack != null && stack.getItem() == ItemList.Component_LavaFilter.getItem()) {
                        if (this.mInventory[1] == null) {
                            this.mInventory[1] = stack != null ? stack.copy() : null;
                            this.depleteInput(stack);
                            this.updateSlots();
                        }
                        return true;
                    }
                }
            }
            return false;
        } else {
            return getControllerSlot().getItem() == ItemList.Component_LavaFilter.getItem();
        }
    }

    private boolean damageLavaFilter() {
        if (!findLavaFilter()) return false;
        if (getBaseMetaTileEntity().getRandomNumber(lavaFilterResilience) > 0) return true;

        ItemStack filter = getControllerSlot();
        if (filter.attemptDamageItem(1, getBaseMetaTileEntity().getWorld().rand)) {
            mInventory[1] = null;
        }
        return true;
    }

    private boolean useWater(int steamAmount) {
        // Round up to not dupe decimal amounts of water.
        int waterAmount = Math.floorDiv(steamAmount + GTValues.STEAM_PER_WATER - 1, GTValues.STEAM_PER_WATER);
        if (depleteInput(Materials.Water.getFluid(waterAmount))
            || depleteInput(GTModHandler.getDistilledWater(waterAmount))) {
            dryHeatCounter = 0;
            return true;
        } else {
            // Add some leniency with explosions.
            if (dryHeatCounter < dryHeatMaximum) {
                ++dryHeatCounter;
            } else {
                GTLog.writeExplosionLog(this, "was too hot and had no more Water!");
                explodeMultiblock(); // Generate crater
            }
            return false;
        }
    }

    public int getEfficiencyIncrease() {
        return 12;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiThermalBoiler;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Boiler")
            .addInfo("Converts Water & Heat into Steam")
            .addInfo("Filters raw materials from lava")
            .addInfo("Requires a Lava Filter in the controller slot for some byproducts")
            .addInfo("Explodes if water is not supplied")
            .addInfo("Consult user manual for more information")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front center")
            .addCasingInfoMin("Thermal Containment Casing", 20, false)
            .addCasingInfoMin("Thermal Processing Casing", 10, false)
            .addCasingInfoMin("Robust Tungstensteel Casing", 10, false)
            .addInputHatch("Hot liquid/Steam I/O – Any Thermal Processing Casing", 2)
            .addOutputHatch("Hot liquid/Steam I/O – Any Thermal Processing Casing", 2)
            .addInputHatch("Coolant I/O – Any Robust Tungstensteel Casing", 3)
            .addOutputHatch("Coolant I/O – Any Robust Tungstensteel Casing", 3)
            .addInputBus("Any Thermal Processing or Robust Tungstensteel Casing", 2, 3)
            .addOutputBus("Any Thermal Processing or Robust Tungstensteel Casing", 2, 3)
            .addMaintenanceHatch("Any Thermal Containment Casing", 1)
            .addMufflerHatch("Any Thermal Containment Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "ArsinXArscosX")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Casings.ThermalContainmentCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAThermalBoilerActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAThermalBoilerActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.ThermalContainmentCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAThermalBoiler)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAThermalBoilerGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.ThermalContainmentCasing.getCasingTexture() };
    }

    @Override
    public IStructureDefinition<MTEThermalBoiler> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEThermalBoiler>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "     ", "D   E", "C   B", "D   E", "AA AA" },
                        { "D A E", "DFFFE", "DF~FE", "DFFFE", "DAAAE" },
                        { "CCFBB", "DFFFE", "DF FE", "DFFFE", "CCFBB" },
                        { "D A E", "DFFFE", "DFFFE", "DFFFE", "DAAAE" },
                        { "     ", "D   E", "C   B", "D   E", "AA AA" } })
                .addElement('A', ofFrame(MaterialsAlloy.MARAGING350))
                .addElement('B', Casings.BronzePipeCasing.asElement())
                .addElement('C', Casings.TungstensteelPipeCasing.asElement())
                .addElement(
                    'D',
                    buildHatchAdder(MTEThermalBoiler.class)
                        .atLeast(
                            InputHatch.withAdder(MTEThermalBoiler::addCoolantHatchToMachineList)
                                .withCount(t -> t.mCoolantInputHatches.size()),
                            OutputHatch.withAdder(MTEThermalBoiler::addCoolantHatchToMachineList)
                                .withCount(t -> t.mCoolantOutputHatches.size()),
                            InputBus.withCount(t -> t.mInputBusses.size()),
                            OutputBus.withCount(t -> t.mOutputBusses.size()))
                        .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                        .hint(3)
                        .buildAndChain(
                            onElementPass(
                                x -> ++x.casingAmountRobust,
                                Casings.RobustTungstenSteelMachineCasing.asElement())))
                .addElement(
                    'E',
                    buildHatchAdder(MTEThermalBoiler.class)
                        .atLeast(
                            InputHatch.withAdder(MTEThermalBoiler::addHotLiquidHatchToMachineList)
                                .withCount(t -> t.mHotLiquidInputHatches.size()),
                            OutputHatch.withAdder(MTEThermalBoiler::addHotLiquidHatchToMachineList)
                                .withCount(t -> t.mHotLiquidOutputHatches.size()),
                            InputBus.withCount(t -> t.mInputBusses.size()),
                            OutputBus.withCount(t -> t.mOutputBusses.size()))
                        .casingIndex(Casings.ThermalProcessingCasing.textureId)
                        .hint(2)
                        .buildAndChain(
                            onElementPass(
                                x -> ++x.casingAmountThermalProcessing,
                                Casings.ThermalProcessingCasing.asElement())))
                .addElement(
                    'F',
                    buildHatchAdder(MTEThermalBoiler.class).atLeast(Maintenance, Muffler)
                        .casingIndex(Casings.ThermalContainmentCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(
                                x -> ++x.casingAmountThermalContainment,
                                Casings.ThermalContainmentCasing.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluidsForColor(Optional<Byte> color) {
        ArrayList<FluidStack> rList = new ArrayList<>();

        for (MTEHatchInput hatch : mHotLiquidInputHatches) {
            FluidStack fluid = hatch.getFillableStack();
            if (fluid != null) {
                rList.add(fluid);
            }
        }

        for (MTEHatchInput hatch : mCoolantInputHatches) {
            FluidStack fluid = hatch.getFillableStack();
            if (fluid != null) {
                rList.add(fluid);
            }
        }

        return rList;
    }

    @Override
    public boolean addOutput(FluidStack fluid) {
        if (fluid == null) return false;

        ArrayList<MTEHatchOutput> targets = isHotOutput(fluid) ? mHotLiquidOutputHatches : mCoolantOutputHatches;

        return dumpToHatches(targets, fluid);
    }

    private boolean dumpToHatches(ArrayList<MTEHatchOutput> hatches, FluidStack fluid) {
        int remaining = fluid.amount;
        for (MTEHatchOutput hatch : hatches) {
            if (remaining <= 0) break;
            FluidStack attempt = fluid.copy();
            attempt.amount = remaining;
            int accepted = hatch.fill(attempt, true);
            remaining -= accepted;
        }
        fluid.amount = remaining;
        return remaining <= 0;
    }

    private boolean isHotInput(FluidStack f) {
        Fluid fluid = f.getFluid();

        return fluid == Materials.Lava.getFluid(1)
            .getFluid() || fluid
                == GTModHandler.getPahoehoeLava(1)
                    .getFluid()
            || fluid == GTModHandler.getHotCoolant(1)
                .getFluid()
            || fluid == MaterialMisc.SOLAR_SALT_HOT.getFluidStack(1)
                .getFluid();
    }

    private boolean isHotOutput(FluidStack f) {
        return f.getFluid() == Materials.Steam.getGas(1)
            .getFluid() || f.getFluid()
                == GTModHandler.getSuperHeatedSteam(1)
                    .getFluid();
    }

    private boolean hasValidHotInput() {
        for (MTEHatchInput hatch : mHotLiquidInputHatches) {
            FluidStack f = hatch.getFillableStack();
            if (f != null && isHotInput(f)) return true;
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmountThermalContainment = 0;
        casingAmountThermalProcessing = 0;
        casingAmountRobust = 0;

        mHotLiquidInputHatches.clear();
        mHotLiquidOutputHatches.clear();
        mCoolantInputHatches.clear();
        mCoolantOutputHatches.clear();

        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmountRobust >= 10
            && casingAmountThermalProcessing >= 10
            && casingAmountThermalContainment >= 20
            && checkHatch();
    }

    public boolean checkHatch() {
        return mMufflerHatches.size() == 1 && mInputHatches.size() >= 1 && mOutputHatches.size() >= 1;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
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
