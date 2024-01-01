package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT4Entity_ThermalBoiler extends GregtechMeta_MultiBlockBase<GT4Entity_ThermalBoiler>
        implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GT4Entity_ThermalBoiler> STRUCTURE_DEFINITION = null;

    private static final int lavaFilterResilience = 30; // Damage lava filter with 1/n probability every operation.
    private int dryHeatCounter = 0; // Counts up to dryHeatMaximum to check for explosion conditions.
    private static final int dryHeatMaximum = 10; // 10 consecutive operations without water = BOOM

    private static final Item itemLavaFilter = ItemList.Component_LavaFilter.getItem();
    private static final Item itemObsidian = Item.getItemFromBlock(Blocks.obsidian);
    private static final Fluid fluidWater = FluidRegistry.WATER;
    private static final Fluid fluidDistilledWater = FluidUtils.getDistilledWater(1).getFluid();
    private static final Fluid fluidSteam = FluidUtils.getSteam(1).getFluid();
    private static final Fluid fluidSHSteam = FluidUtils.getSuperHeatedSteam(1).getFluid();

    public GT4Entity_ThermalBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT4Entity_ThermalBoiler(String mName) {
        super(mName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT4Entity_ThermalBoiler(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Boiler";
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return (aStack != null && aStack.getItem() == itemLavaFilter) ? 1 : 0;
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            // Only test against the first fluid input in the recipe.
            // We still want to run if we lack water (and subsequently explode).
            @NotNull
            @Override
            protected Stream<GT_Recipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                if (lastRecipe != null && depleteInput(lastRecipe.mFluidInputs[0], true)) {
                    return Stream.of(lastRecipe);
                }
                if (map == null) {
                    return Stream.empty();
                }
                return map.getAllRecipes().stream().filter(recipe -> depleteInput(recipe.mFluidInputs[0], true));
            }

            @NotNull
            @Override
            protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
                GT_Recipe adjustedRecipe = recipe.copy();

                // Hack the recipe logic to not consume water, so that we can explode.
                for (FluidStack inputFluid : adjustedRecipe.mFluidInputs) {
                    if (inputFluid != null
                            && (inputFluid.getFluid() == fluidWater || inputFluid.getFluid() == fluidDistilledWater)) {
                        inputFluid.amount = 0;
                    }
                }

                // If we don't have a lava filter, remove non-obsidian outputs
                // so that output space for them is not required if void protection is on.
                if (!findLavaFilter()) {
                    for (ItemStack outputItem : adjustedRecipe.mOutputs) {
                        if (outputItem != null && outputItem.getItem() != itemObsidian) {
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
        // super.checkProcessing() instantly sets efficiency to maximum, override this.
        int efficiency = mEfficiency;
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) {
            mEfficiency = efficiency;
            mEfficiencyIncrease = mMaxProgresstime * getEfficiencyIncrease();

            // Adjust steam output based on efficiency.
            if (mOutputFluids != null) {
                for (FluidStack outputFluid : mOutputFluids) {
                    if (outputFluid != null
                            && (outputFluid.getFluid() == fluidSteam || outputFluid.getFluid() == fluidSHSteam)) {

                        // Purely for display reasons, we don't actually make any EU.
                        if (outputFluid.getFluid() == fluidSteam) {
                            lEUt = outputFluid.amount / mMaxProgresstime / 2;
                        } else {
                            lEUt = outputFluid.amount / mMaxProgresstime;
                        }

                        // Adjust steam output based on efficiency.
                        // TODO: This is not reflected in the GUI while the player has it open??
                        if (mEfficiency < getMaxEfficiency(null)) {
                            outputFluid.amount = Math
                                    .max(1, (outputFluid.amount * mEfficiency) / getMaxEfficiency(null));
                        }

                        // Consume water to run recipe.
                        if (!useWater(outputFluid.amount)) {
                            outputFluid.amount = 0;
                            lEUt = 0;
                        }
                    }
                }
            }

            // Remove non-obsidian outputs if we can't damage lava filter.
            if (mOutputItems != null && mOutputItems.length > 0) {
                if (!damageLavaFilter()) {
                    for (ItemStack outputItem : mOutputItems) {
                        if (outputItem != null && outputItem.getItem() != itemObsidian) {
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
                    if (stack != null && stack.getItem() == itemLavaFilter) {
                        setGUIItemStack(stack);
                        return true;
                    }
                }
            }
            return false;
        } else {
            return getControllerSlot().getItem() == itemLavaFilter;
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
        int waterAmount = Math.floorDiv(steamAmount + GT_Values.STEAM_PER_WATER - 1, GT_Values.STEAM_PER_WATER);
        if (depleteInput(FluidUtils.getWater(waterAmount)) || depleteInput(FluidUtils.getDistilledWater(waterAmount))) {
            dryHeatCounter = 0;
            return true;
        } else {
            // Add some leniency with explosions.
            if (dryHeatCounter < dryHeatMaximum) {
                ++dryHeatCounter;
            } else {
                GT_Log.exp.println(this.mName + " was too hot and had no more Water!");
                explodeMultiblock(); // Generate crater
            }
            return false;
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    public int getEfficiencyIncrease() {
        return 12;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiThermalBoiler;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Thermal Boiler Controller")
                .addInfo("Converts Water & Heat into Steam").addInfo("Filters raw materials from lava")
                .addInfo("Explodes if water is not supplied").addInfo("Consult user manual for more information")
                .addPollutionAmount(getPollutionPerSecond(null)).beginStructureBlock(3, 3, 3, true)
                .addController("Front Center").addCasingInfoMin("Thermal Containment Casings", 10, false)
                .addInputBus("Any Casing", 1).addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1)
                .addOutputHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(1);
    }

    @Override
    public IStructureDefinition<GT4Entity_ThermalBoiler> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GT4Entity_ThermalBoiler>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GT4Entity_ThermalBoiler.class)
                                    .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.getIndexFromPage(0, 1)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 11))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
    }
}
