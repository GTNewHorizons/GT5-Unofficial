// copied from gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine
// The origin one in gt made the abstract method private so i can't imp it.
package goodgenerator.blocks.tileEntity.base;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.util.GTStructureUtility.*;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElementCheckOnly;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.common.items.MetaGeneratedTool01;

public abstract class MTELargeTurbineBase extends MTEEnhancedMultiBlockBase<MTELargeTurbineBase>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTELargeTurbineBase>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<MTELargeTurbineBase>>() {

        @Override
        protected IStructureDefinition<MTELargeTurbineBase> computeValue(Class<?> type) {
            return StructureDefinition.<MTELargeTurbineBase>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "     ", "xxxxx", "xxxxx", "xxxxx", "xxxxx", },
                            { " --- ", "xcccx", "xchcx", "xchcx", "xcccx", },
                            { " --- ", "xc~cx", "xh-hx", "xh-hx", "xcdcx", },
                            { " --- ", "xcccx", "xchcx", "xchcx", "xcccx", },
                            { "     ", "xxxxx", "xxxxx", "xxxxx", "xxxxx", }, }))
                .addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
                .addElement('d', lazy(t -> Dynamo.newAny(t.getCasingTextureIndex(), 1)))
                .addElement(
                    'h',
                    lazy(
                        t -> buildHatchAdder(MTELargeTurbineBase.class)
                            .atLeast(Maintenance, InputHatch, OutputHatch, OutputBus, InputBus, Muffler)
                            .casingIndex(t.getCasingTextureIndex())
                            .dot(2)
                            .buildAndChain(t.getCasingBlock(), t.getCasingMeta())))
                .addElement('x', (IStructureElementCheckOnly<MTELargeTurbineBase>) (aContext, aWorld, aX, aY, aZ) -> {
                    TileEntity tTile = aWorld.getTileEntity(aX, aY, aZ);
                    return !(tTile instanceof IGregTechTileEntity)
                        || !(((IGregTechTileEntity) tTile).getMetaTileEntity() instanceof MTELargeTurbineBase);
                })
                .build();
        }
    };

    protected int baseEff = 0;
    protected int optFlow = 0;
    protected double realOptFlow = 0;
    protected int storedFluid = 0;
    protected int counter = 0;
    protected boolean looseFit = false;
    protected long maxPower = 0;

    public MTELargeTurbineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeTurbineBase(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    @Override
    public IStructureDefinition<MTELargeTurbineBase> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        maxPower = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 1) && mMaintenanceHatches.size() == 1
            && mMufflerHatches.isEmpty() == (getPollutionPerTick(null) == 0)) {
            maxPower = getMaximumOutput();
            return true;
        }
        return false;
    }

    public abstract Block getCasingBlock();

    public abstract int getCasingMeta();

    public abstract int getCasingTextureIndex();

    @Override
    public boolean addToMachineList(IGregTechTileEntity tTileEntity, int aBaseCasingIndex) {
        return addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())
            || addInputToMachineList(tTileEntity, getCasingTextureIndex())
            || addOutputToMachineList(tTileEntity, getCasingTextureIndex())
            || addMufflerToMachineList(tTileEntity, getCasingTextureIndex());
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
    public @NotNull CheckRecipeResult checkProcessing() {
        ItemStack controllerSlot = getControllerSlot();
        if ((counter & 7) == 0 && (controllerSlot == null || !(controllerSlot.getItem() instanceof MetaGeneratedTool)
            || controllerSlot.getItemDamage() < 170
            || controllerSlot.getItemDamage() > 179)) {
            stopMachine();
            return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
        }
        ArrayList<FluidStack> tFluids = getStoredFluids();
        if (tFluids.size() > 0) {
            if (baseEff == 0 || optFlow == 0
                || counter >= 512
                || this.getBaseMetaTileEntity()
                    .hasWorkJustBeenEnabled()
                || this.getBaseMetaTileEntity()
                    .hasInventoryBeenModified()) {
                counter = 0;
                baseEff = GTUtility.safeInt(
                    (long) ((5F + ((MetaGeneratedTool) controllerSlot.getItem()).getToolCombatDamage(controllerSlot))
                        * 1000F));
                optFlow = GTUtility.safeInt(
                    (long) Math.max(
                        Float.MIN_NORMAL,
                        ((MetaGeneratedTool) controllerSlot.getItem()).getToolStats(controllerSlot)
                            .getSpeedMultiplier() * MetaGeneratedTool.getPrimaryMaterial(controllerSlot).mToolSpeed
                            * 50));
                if (optFlow <= 0 || baseEff <= 0) {
                    stopMachine(); // in case the turbine got removed
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
            } else {
                counter++;
            }
        }

        int newPower = fluidIntoPower(tFluids, optFlow, baseEff); // How much the turbine should be producing with this
                                                                  // flow
        int difference = newPower - this.mEUt; // difference between current output and new output

        // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in
        // power level (per tick)
        // This is how much the turbine can actually change during this tick
        int maxChangeAllowed = Math.max(10, GTUtility.safeInt((long) Math.abs(difference) / 100));

        if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
            int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
            this.mEUt += change; // Apply the change
        } else this.mEUt = newPower;

        if (this.mEUt <= 0) {
            // stopMachine();
            this.mEUt = 0;
            this.mEfficiency = 0;
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        } else {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = 10;
            // Overvoltage is handled inside the MultiBlockBase when pushing out to dynamos. no need to do it here.
            return CheckRecipeResultRegistry.GENERATING;
        }
    }

    public abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff);

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) {
            return 0;
        }
        if (aStack.getItem() instanceof MetaGeneratedTool01) {
            return 10000;
        }
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    public long getMaximumOutput() {
        long aTotal = 0;
        for (MTEHatchDynamo aDynamo : filterValidMTEs(mDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            aTotal = aDynamo.maxAmperesOut() * aVoltage;
            break;
        }
        return aTotal;
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (MTEHatchMuffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        String tRunning = mMaxProgresstime > 0
            ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.running.true")
                + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.running.false")
                + EnumChatFormatting.RESET;
        String tMaintainance = getIdealStatus() == getRepairStatus()
            ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                + EnumChatFormatting.RESET;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof MetaGeneratedTool01) {
            tDura = GTUtility.safeInt(
                (long) (100.0f / MetaGeneratedTool.getToolMaxDamage(mInventory[1])
                    * (MetaGeneratedTool.getToolDamage(mInventory[1])) + 1));
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : filterValidMTEs(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        String[] ret = new String[] {
            // 8 Lines available for information panels
            tRunning + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(mEUt)
                + EnumChatFormatting.RESET
                + " EU/t", /* 1 */
            tMaintainance, /* 2 */
            StatCollector.translateToLocal("GT5U.turbine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.RESET
                + "%", /* 2 */
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + /* 3 */ EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(GTUtility.safeInt((long) realOptFlow))
                + EnumChatFormatting.RESET
                + " L/t"
                + /* 4 */ EnumChatFormatting.YELLOW
                + " ("
                + (looseFit ? StatCollector.translateToLocal("GT5U.turbine.loose")
                    : StatCollector.translateToLocal("GT5U.turbine.tight"))
                + ")", /* 5 */
            StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                + EnumChatFormatting.GOLD
                + GTUtility.formatNumbers(storedFluid)
                + EnumChatFormatting.RESET
                + "L", /* 6 */
            StatCollector.translateToLocal(
                "GT5U.turbine.dmg") + ": " + EnumChatFormatting.RED + tDura + EnumChatFormatting.RESET + "%", /* 7 */
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %" /* 8 */
        };
        if (!this.getClass()
            .getName()
            .contains("Steam"))
            ret[4] = StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.safeInt((long) realOptFlow)
                + EnumChatFormatting.RESET
                + " L/t";
        return ret;
    }

    public boolean hasTurbine() {
        return this.getMaxEfficiency(mInventory[1]) > 0;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 1, elementBudget, env, false, true);
    }
}
