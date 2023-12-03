package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Dynamo;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.TTDynamo;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMetaTileEntity_LargeSemifluidGenerator extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_LargeSemifluidGenerator> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_LargeSemifluidGenerator> STRUCTURE_DEFINITION = null;

    protected int fuelConsumption = 0;
    protected int fuelValue = 0;
    protected int fuelRemaining = 0;
    protected boolean boostEu = false;

    public GregtechMetaTileEntity_LargeSemifluidGenerator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_LargeSemifluidGenerator(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Large Semifluid Generator")
                .addInfo("Engine Intake Casings must not be obstructed in front (only air blocks)")
                .addInfo("Supply Semifluid Fuels and 2000L of Lubricant per hour to run.")
                .addInfo("Supply 80L of Oxygen per second to boost output (optional).")
                .addInfo("Default: Produces 2048EU/t at 100% efficiency")
                .addInfo("Boosted: Produces 6144EU/t at 150% efficiency")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 4, false)
                .addController("Front Center").addCasingInfoMin("Stable Titanium Machine Casing", 16, false)
                .addCasingInfoMin("Steel Gear Box Machine Casing", 2, false)
                .addCasingInfoMin("Engine Intake Machine Casing", 8, false).addInputHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1).addDynamoHatch("Back Center", 2)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE;
    }

    @Override
    protected int getCasingTextureId() {
        return 50;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> tFluids = getStoredFluids();

        // Check for lubricant and oxygen first, so we can compute costs ahead of time.
        // This will allow us to check costs without needing to actually try to deplete fluids
        // (wasting earlier fluids in the check if later fluids turn out to be insufficient).
        FluidStack lubricant = Materials.Lubricant.getFluid(0L);
        FluidStack oxygen = Materials.Oxygen.getGas(0L);
        for (FluidStack hatchFluid : tFluids) {
            if (hatchFluid.isFluidEqual(lubricant)) {
                lubricant.amount = Math.max(lubricant.amount, hatchFluid.amount);
            } else if (hatchFluid.isFluidEqual(oxygen)) {
                oxygen.amount = Math.max(oxygen.amount, hatchFluid.amount);
            }
        }
        boostEu = oxygen.amount >= 4L;
        long lubricantCost = boostEu ? 2L : 1L;
        if (lubricant.amount < lubricantCost) {
            return SimpleCheckRecipeResult.ofFailure("no_lubricant");
        }

        for (FluidStack hatchFluid : tFluids) { // Loops through hatches
            GT_Recipe aFuel = GTPPRecipeMaps.semiFluidFuels.getBackend().findFuel(hatchFluid);
            if (aFuel == null) {
                // Not a valid semi-fluid fuel.
                continue;
            }

            int newEUt = boostEu ? 4096 : 2048;
            fuelConsumption = newEUt / aFuel.mSpecialValue; // Calc fuel consumption
            FluidStack tLiquid = new FluidStack(hatchFluid.getFluid(), fuelConsumption);
            if (depleteInput(tLiquid)) { // Deplete that amount
                // We checked beforehand, so both of these depletions should succeed.
                // But check the return values anyway just to be safe.
                if (boostEu) {
                    if (!depleteInput(Materials.Oxygen.getGas(4L))) {
                        return SimpleCheckRecipeResult.ofFailure("no_oxygen");
                    }
                }
                // Deplete Lubricant. 2000L should = 1 hour of runtime (if baseEU = 2048)
                if (mRuntime % 72 == 0 || mRuntime == 0) {
                    if (!depleteInput(Materials.Lubricant.getFluid(lubricantCost))) {
                        return SimpleCheckRecipeResult.ofFailure("no_lubricant");
                    }
                }

                fuelValue = aFuel.mSpecialValue;
                fuelRemaining = hatchFluid.amount; // Record available fuel
                this.lEUt = mEfficiency < 2000 ? 0 : newEUt; // Output 0 if startup is less than 20%
                this.mProgresstime = 1;
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = 15;
                return CheckRecipeResultRegistry.GENERATING;
            }
        }

        this.lEUt = 0;
        this.mEfficiency = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_LargeSemifluidGenerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_LargeSemifluidGenerator>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "III", "CCC", "CCC", "CCC" }, { "I~I", "CGC", "CGC", "CMC" },
                                            { "III", "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_LargeSemifluidGenerator.class)
                                    .atLeast(Muffler, InputHatch, Maintenance).casingIndex(getCasingTextureIndex())
                                    .dot(1).buildAndChain(
                                            onElementPass(
                                                    x -> ++x.mCasing,
                                                    ofBlock(getCasingBlock(), getCasingMeta()))))
                    .addElement('G', ofBlock(getGearboxBlock(), getGearboxMeta()))
                    .addElement('I', ofBlock(getIntakeBlock(), getIntakeMeta()))
                    .addElement('M', Dynamo.or(TTDynamo).newAny(getCasingTextureIndex(), 2)).build();
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
        mDynamoHatches.clear();
        return checkPiece(mName, 1, 1, 0) && mCasing >= 16 && checkHatch();
    }

    public final boolean addLargeSemifluidGeneratorList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    public final boolean addLargeSemifluidGeneratorBackList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo || this.isThisHatchMultiDynamo(aTileEntity)) {
                return addToMachineList(aTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    public byte getCasingMeta() {
        return 2;
    }

    public Block getIntakeBlock() {
        return GregTech_API.sBlockCasings4;
    }

    public byte getIntakeMeta() {
        return 13;
    }

    public Block getGearboxBlock() {
        return GregTech_API.sBlockCasings2;
    }

    public byte getGearboxMeta() {
        return 3;
    }

    public byte getCasingTextureIndex() {
        return 50;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_LargeSemifluidGenerator(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return boostEu ? 15000 : 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiLargeSemiFluidGenerator;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { "Large Semifluid Generator", "Current Output: " + lEUt * mEfficiency / 10000 + " EU/t",
                "Fuel Consumption: " + fuelConsumption + "L/t", "Fuel Value: " + fuelValue + " EU/L",
                "Fuel Remaining: " + fuelRemaining + " Litres", "Current Efficiency: " + (mEfficiency / 100) + "%",
                getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMachineType() {
        return "Semifluid Generator";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

}
