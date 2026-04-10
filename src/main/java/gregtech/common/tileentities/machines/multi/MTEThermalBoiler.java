package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
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
        // super.checkProcessing() instantly sets efficiency to maximum, override this.
        int efficiency = mEfficiency;
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) {
            mEfficiency = efficiency;
            mEfficiencyIncrease = mMaxProgresstime * getEfficiencyIncrease();

            // Adjust steam output based on efficiency.
            if (mOutputFluids != null) {
                for (FluidStack outputFluid : mOutputFluids) {
                    if (outputFluid != null && (outputFluid.getFluid() == Materials.Steam.getGas(1)
                        .getFluid() || outputFluid.getFluid()
                            == GTModHandler.getSuperHeatedSteam(1)
                                .getFluid())) {

                        // Purely for display reasons, we don't actually make any EU.
                        if (outputFluid.getFluid() == Materials.Steam.getGas(1)
                            .getFluid()) {
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
                        } ;
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
            .addCasingInfoMin("Thermal Containment Casings", 10, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
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
                    buildHatchAdder(MTEThermalBoiler.class).atLeast(InputHatch, OutputHatch)
                        .casingIndex(Casings.RobustTungstenSteelMachineCasing.textureId)
                        .hint(3)
                        .buildAndChain(
                            onElementPass(
                                x -> ++x.casingAmountRobust,
                                Casings.RobustTungstenSteelMachineCasing.asElement())))
                .addElement(
                    'E',
                    buildHatchAdder(MTEThermalBoiler.class).atLeast(InputHatch, OutputHatch)
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
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmountRobust >= 5
            && casingAmountThermalProcessing >= 5
            && casingAmountThermalContainment >= 5
            && checkHatch();
    }

    public boolean checkHatch() {
        return mMufflerHatches.size() == 1 && mOutputHatches.size() >= 1 && mInputHatches.size() >= 1;
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
