package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;

public class MTEUniversalChemicalFuelEngine extends MTETooltipMultiBlockBaseEM
    implements IConstructable, ISurvivalConstructable {

    protected final double DIESEL_EFFICIENCY_COEFFICIENT = 0.04D;
    protected final double GAS_EFFICIENCY_COEFFICIENT = 0.04D;
    protected final double ROCKET_EFFICIENCY_COEFFICIENT = 0.005D;
    protected final double EFFICIENCY_CEILING = 1.5D;
    protected final int HEATING_TIMER = TickTime.SECOND * 10;

    private long tEff;
    private int heatingTicks;
    private boolean isStoppingSafe;

    private IStructureDefinition<MTEUniversalChemicalFuelEngine> multiDefinition = null;

    public MTEUniversalChemicalFuelEngine(String name) {
        super(name);
        super.useLongPower = true;
    }

    public MTEUniversalChemicalFuelEngine(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        super.useLongPower = true;
    }

    public final boolean addMaintenance(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchMaintenance) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMaintenanceHatches.add((MTEHatchMaintenance) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addMuffler(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchMuffler) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMufflerHatches.add((MTEHatchMuffler) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addInputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchInput) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mInputHatches.add((MTEHatchInput) aMetaTileEntity);
            }
        }
        return false;
    }

    public final boolean addDynamoHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchDynamo) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mDynamoHatches.add((MTEHatchDynamo) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof MTEHatchDynamoMulti) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.eDynamoMulti.add((MTEHatchDynamoMulti) aMetaTileEntity);
            }
        }
        return false;
    }

    @Override
    public IStructureDefinition<MTEUniversalChemicalFuelEngine> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTEUniversalChemicalFuelEngine>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "TTTTT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTMMT", "TTTTT" },
                            { "TTTTT", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "SPCCI-", "TTTTT" },
                            { "TT~TT", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "SPGGI-", "TTETT" },
                            { "TTWTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT", "TTTTT" } }))
                .addElement('T', ofBlock(GregTechAPI.sBlockCasings4, 2))
                .addElement('W', gregtech.api.enums.HatchElement.Maintenance.newAny(50, 1))
                .addElement('M', gregtech.api.enums.HatchElement.Muffler.newAny(50, 2))
                .addElement('S', gregtech.api.enums.HatchElement.InputHatch.newAny(50, 3))
                .addElement('E', gregtech.api.enums.HatchElement.Dynamo.newAny(50, 4))
                .addElement('P', ofBlock(GregTechAPI.sBlockCasings2, 14))
                .addElement('C', ofBlock(Loaders.titaniumPlatedCylinder, 0))
                .addElement('G', ofBlock(GregTechAPI.sBlockCasings2, 4))
                .addElement('I', ofBlock(GregTechAPI.sBlockCasings4, 13))
                .build();
        }
        return multiDefinition;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 2, 2, 0);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(mName, 2, 2, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("UniversalChemicalFuelEngine.hint", 11);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return (int) Math.sqrt(this.getPowerFlow()) / 20;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Engine")
            .addInfo("Controller block for the Chemical Engine")
            .addInfo("BURNING BURNING BURNING")
            .addInfo("Use combustible liquid to generate power.")
            .addInfo("You need to supply Combustion Promoter to keep it running.")
            .addInfo("It will consume all the fuel and promoter in the hatch every second.")
            .addInfo("If the Dynamo Hatch's buffer fills up, the machine will stop.")
            .addInfo("When turned on, there's 10-second period where the machine will not stop.")
            .addInfo("Even if it doesn't stop, all the fuel in the hatch will be consumed.")
            .addInfo("The efficiency is determined by the proportion of Combustion Promoter to fuel.")
            .addInfo("The proportion is bigger, and the efficiency will be higher.")
            .addInfo("Start machine with power button to force structure check.")
            .addInfo("It creates sqrt(Current Output Power) pollution every second")
            .addInfo(
                "If you forget to supply Combustion Promoter, this engine will swallow all the fuel "
                    + EnumChatFormatting.YELLOW
                    + "without outputting energy"
                    + EnumChatFormatting.GRAY
                    + ".")
            .addInfo("The efficiency is up to 150%.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(5, 4, 9, false)
            .addMaintenanceHatch("Hint block with dot 1")
            .addMufflerHatch("Hint block with dot 2 (fill all slots with mufflers)")
            .addInputHatch("Hint block with dot 3 (fill all slots with input hatches)")
            .addDynamoHatch("Hint block with dot 4")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {

        ArrayList<FluidStack> tFluids = getStoredFluids();

        long PromoterAmount = findLiquidAmount(getPromoter(), tFluids);

        CheckRecipeResult result;

        result = processFuel(tFluids, RecipeMaps.dieselFuels, PromoterAmount, DIESEL_EFFICIENCY_COEFFICIENT, 1);
        if (result.wasSuccessful()) return result;

        result = processFuel(tFluids, RecipeMaps.gasTurbineFuels, PromoterAmount, GAS_EFFICIENCY_COEFFICIENT, 1);
        if (result.wasSuccessful()) return result;

        result = processFuel(tFluids, GTPPRecipeMaps.rocketFuels, PromoterAmount, ROCKET_EFFICIENCY_COEFFICIENT, 3);
        if (result.wasSuccessful()) return result;

        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    protected CheckRecipeResult processFuel(ArrayList<FluidStack> tFluids, RecipeMap<FuelBackend> recipeMap,
        long PromoterAmount, double efficiencyCoefficient, double FuelsValueBonus) {
        for (GTRecipe recipe : recipeMap.getAllRecipes()) {
            FluidStack tFuel = findFuel(recipe);
            if (tFuel == null) continue;
            long FuelAmount = findLiquidAmount(tFuel, tFluids);
            if (FuelAmount == 0) continue;
            calculateEfficiency(FuelAmount, PromoterAmount, efficiencyCoefficient);
            consumeAllLiquid(tFuel, tFluids);
            consumeAllLiquid(getPromoter(), tFluids);
            this.setPowerFlow((long) (FuelAmount * recipe.mSpecialValue * FuelsValueBonus / 20.0D));
            this.mMaxProgresstime = 20;
            this.updateSlots();
            return CheckRecipeResultRegistry.GENERATING;
        }
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public void stopMachine() {
        // Reset the counter for heating, so that it works again when the machine restarts
        heatingTicks = 0;
        super.stopMachine();
    }

    @Override
    public boolean onRunningTick(ItemStack stack) {
        super.onRunningTick(stack);
        // Counts ticks up to the defined timer (200 ticks, 10 seconds)
        // The multiblock will not stop due to excess energy during this time
        // Machine used to explode in the past, this timer was first made to prevent that
        if (heatingTicks < HEATING_TIMER) {
            heatingTicks++;
            isStoppingSafe = true;
        } else if (isStoppingSafe) isStoppingSafe = false;

        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            addAutoEnergy();
        }
        return true;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = "Probably makes: " + EnumChatFormatting.RED
            + GTUtility.formatNumbers(this.getPowerFlow())
            + EnumChatFormatting.RESET
            + " EU/t";
        info[6] = "Problems: " + EnumChatFormatting.RED
            + GTUtility.formatNumbers(this.getIdealStatus() - this.getRepairStatus())
            + EnumChatFormatting.RESET
            + " Efficiency: "
            + EnumChatFormatting.YELLOW
            + GTUtility.formatNumbers(tEff / 100D)
            + EnumChatFormatting.RESET
            + " %";
        return info;
    }

    void addAutoEnergy() {
        long exEU = this.getPowerFlow() * tEff / 10000;
        if (!mDynamoHatches.isEmpty()) {
            MTEHatchDynamo tHatch = mDynamoHatches.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= exEU) {
                tHatch.setEUVar(
                    Math.min(
                        tHatch.maxEUStore(),
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + exEU));
            } else if (!isStoppingSafe) {
                stopMachine();
            }
        }
        if (!eDynamoMulti.isEmpty()) {
            MTEHatchDynamoMulti tHatch = eDynamoMulti.get(0);
            if (tHatch.maxEUOutput() * tHatch.maxAmperesOut() >= exEU) {
                tHatch.setEUVar(
                    Math.min(
                        tHatch.maxEUStore(),
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + exEU));
            } else if (!isStoppingSafe) {
                stopMachine();
            }
        }
    }

    public FluidStack getPromoter() {
        return FluidRegistry.getFluidStack("combustionpromotor", 1);
    }

    public FluidStack findFuel(GTRecipe aFuel) {
        if (aFuel.mInputs != null && aFuel.mInputs.length > 0)
            return GTUtility.getFluidForFilledItem(aFuel.mInputs[0], true);
        else return aFuel.mFluidInputs[0];
    }

    public void calculateEfficiency(long aFuel, long aPromoter, double coefficient) {
        if (aPromoter == 0) {
            this.tEff = 0;
            return;
        }
        this.tEff = (int) (Math.exp(-coefficient * (double) aFuel / (double) aPromoter) * EFFICIENCY_CEILING * 10000);
    }

    public long findLiquidAmount(FluidStack liquid, List<FluidStack> input) {
        long cnt = 0;
        for (FluidStack fluid : input) {
            if (fluid.isFluidEqual(liquid)) {
                cnt += fluid.amount;
            }
        }
        if (cnt < 0) cnt = 0;
        return cnt;
    }

    public void consumeAllLiquid(FluidStack liquid, List<FluidStack> input) {
        for (FluidStack fluid : input) {
            if (fluid.isFluidEqual(liquid)) fluid.amount = 0;
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][50],
                TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE), TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][50], TextureFactory.of(OVERLAY_FRONT_DIESEL_ENGINE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DIESEL_ENGINE_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][50] };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEUniversalChemicalFuelEngine(this.mName);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 2, 2, 0, elementBudget, env, false, true);
    }
}
