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

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.general.ItemLavaFilter;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT4Entity_ThermalBoiler extends GregtechMeta_MultiBlockBase<GT4Entity_ThermalBoiler>
        implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GT4Entity_ThermalBoiler> STRUCTURE_DEFINITION = null;
    private int mSuperEfficencyIncrease = 0;

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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public String getMachineType() {
        return "Boiler";
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        // log("Trying to damage component.");
        return (aStack != null && aStack.getItem() == mLavaFilter) ? 1 : 0;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.thermalBoilerRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    private static Item mLavaFilter;
    private static Fluid mLava = null;
    private static Fluid mPahoehoe = null;
    private static Fluid mSolarSaltHot = null;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        ItemStack controllerStack = getControllerSlot();
        this.mSuperEfficencyIncrease = 0;

        if (mLavaFilter == null) {
            mLavaFilter = ItemList.Component_LavaFilter.getItem();
        }
        if (mLava == null) {
            mLava = FluidRegistry.LAVA;
        }
        if (mPahoehoe == null) {
            mPahoehoe = FluidUtils.getPahoehoeLava(1).getFluid();
        }
        if (mSolarSaltHot == null) {
            mSolarSaltHot = MISC_MATERIALS.SOLAR_SALT_HOT.getFluid();
        }

        // Try reload new Lava Filter
        if (controllerStack == null) {
            ItemStack uStack = this.findItemInInventory(mLavaFilter);
            if (uStack != null) {
                this.setGUIItemStack(uStack);
                controllerStack = this.getControllerSlot();
            }
        }

        for (GT_Recipe tRecipe : GTPPRecipeMaps.thermalBoilerRecipes.getAllRecipes()) {
            FluidStack tFluid = tRecipe.mFluidInputs[0];
            if (tFluid != null) {

                if (tFluid.getFluid() == mLava || tFluid.getFluid() == mPahoehoe) {
                    if (depleteInput(tFluid)) {
                        this.mMaxProgresstime = Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2));
                        this.lEUt = getEUt();
                        this.mEfficiencyIncrease = (this.mMaxProgresstime * getEfficiencyIncrease());

                        int loot_MAXCHANCE = 100000;
                        if (controllerStack != null && controllerStack.getItem() == mLavaFilter) {
                            if ((tRecipe.getOutput(0) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(0))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(0)) };
                            }
                            if ((tRecipe.getOutput(1) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(1))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(1)) };
                            }
                            if ((tRecipe.getOutput(2) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(2))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(2)) };
                            }
                            if ((tRecipe.getOutput(3) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(3))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(3)) };
                            }
                            if ((tRecipe.getOutput(4) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(4))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(4)) };
                            }
                            if ((tRecipe.getOutput(5) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(5))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(5)) };
                            }
                        }
                        final FluidStack[] mFluidOutputs = tRecipe.mFluidOutputs;
                        this.mOutputFluids = new FluidStack[mFluidOutputs.length];
                        for (int i = 0, mFluidOutputsLength = mFluidOutputs.length; i < mFluidOutputsLength; i++) {
                            this.mOutputFluids[i] = mFluidOutputs[i].copy();
                        }
                        // Give Obsidian without Lava Filter
                        if (tFluid.getFluid() == mLava) {
                            if ((tRecipe.getOutput(6) != null)
                                    && (getBaseMetaTileEntity().getRandomNumber(loot_MAXCHANCE)
                                            < tRecipe.getOutputChance(6))) {
                                this.mOutputItems = new ItemStack[] { GT_Utility.copy(tRecipe.getOutput(6)) };
                            }
                        }
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                } else if (tFluid.getFluid() == mSolarSaltHot) {
                    if (depleteInput(tFluid)) {
                        this.mMaxProgresstime = tRecipe.mDuration;
                        this.lEUt = 0;
                        this.mEfficiency = 10000;
                        for (FluidStack aOutput : tRecipe.mFluidOutputs) {
                            this.addOutput(FluidUtils.getFluidStack(aOutput, aOutput.amount));
                        }
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
        }
        this.mMaxProgresstime = 0;
        this.lEUt = 0;
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt > 0) {
            if (this.mSuperEfficencyIncrease > 0) {
                this.mEfficiency = Math.min(10000, this.mEfficiency + this.mSuperEfficencyIncrease);
            }
            int tGeneratedEU = (int) (this.lEUt * 2L * this.mEfficiency / 10000L);
            if (tGeneratedEU > 0) {
                long amount = (tGeneratedEU + 160) / 160;
                if (depleteInput(Materials.Water.getFluid(amount))
                        || depleteInput(GT_ModHandler.getDistilledWater(amount))) {
                    addOutput(GT_ModHandler.getSteam(tGeneratedEU));
                } else {
                    explodeMultiblock();
                }
            }
            return true;
        }
        return true;
    }

    public int getEUt() {
        return 400;
    }

    public int getEfficiencyIncrease() {
        return 12;
    }

    int runtimeBoost(int mTime) {
        return mTime * 150 / 100;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
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
                .addInfo("Converts Water & Heat into Steam").addInfo("Explodes if water is not supplied")
                .addInfo("Consult user manual for more information").addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator().beginStructureBlock(3, 3, 3, true).addController("Front Center")
                .addCasingInfoMin("Thermal Containment Casings", 10, false).addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
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

    public void damageFilter() {
        ItemStack filter = this.mInventory[1];
        if (filter != null) {
            if (filter.getItem() instanceof ItemLavaFilter) {

                long currentUse = ItemLavaFilter.getFilterDamage(filter);

                // Remove broken Filter
                if (currentUse >= 100 - 1) {
                    this.mInventory[1] = null;
                } else {
                    // Do Damage
                    ItemLavaFilter.setFilterDamage(filter, currentUse + 1);
                }
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // Reload Lava Filter
            if (this.getControllerSlot() == null) {
                if (this.mInputBusses.size() > 0) {
                    for (GT_MetaTileEntity_Hatch_InputBus aBus : this.mInputBusses) {
                        for (ItemStack aStack : aBus.mInventory) {
                            if (aStack != null && aStack.getItem() instanceof ItemLavaFilter) {
                                this.setGUIItemStack(aStack);
                            }
                        }
                    }
                }
            }

            if (this.lEUt > 0) {
                if (aTick % 600L == 0L) {
                    damageFilter();
                }
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }
}
