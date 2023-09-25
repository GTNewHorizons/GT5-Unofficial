package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.STEAM_PER_WATER;
import static gregtech.api.enums.ItemList.Circuit_Integrated;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.formatNumbers;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GT_Mod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.LargeBoilerFuelFakeRecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public abstract class GT_MetaTileEntity_LargeBoiler
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_LargeBoiler> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<GT_MetaTileEntity_LargeBoiler>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<GT_MetaTileEntity_LargeBoiler> computeValue(Class<?> type) {
            return StructureDefinition.<GT_MetaTileEntity_LargeBoiler>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "ccc", "ccc", "ccc" }, { "ccc", "cPc", "ccc" }, { "ccc", "cPc", "ccc" },
                            { "ccc", "cPc", "ccc" }, { "f~f", "fff", "fff" }, }))
                .addElement('P', lazy(t -> ofBlock(t.getPipeBlock(), t.getPipeMeta())))
                .addElement(
                    'c',
                    lazy(
                        t -> buildHatchAdder(GT_MetaTileEntity_LargeBoiler.class).atLeast(OutputHatch)
                            .casingIndex(t.getCasingTextureIndex())
                            .dot(2)
                            .buildAndChain(
                                onElementPass(
                                    GT_MetaTileEntity_LargeBoiler::onCasingAdded,
                                    ofBlock(t.getCasingBlock(), t.getCasingMeta())))))
                .addElement(
                    'f',
                    lazy(
                        t -> buildHatchAdder(GT_MetaTileEntity_LargeBoiler.class)
                            .atLeast(Maintenance, InputHatch, InputBus, Muffler)
                            .casingIndex(t.getFireboxTextureIndex())
                            .dot(1)
                            .buildAndChain(
                                onElementPass(
                                    GT_MetaTileEntity_LargeBoiler::onFireboxAdded,
                                    ofBlock(t.getFireboxBlock(), t.getFireboxMeta())))))
                .build();
        }
    };
    private boolean firstRun = true;
    private int mSuperEfficencyIncrease = 0;
    private int integratedCircuitConfig = 0; // Steam output is reduced by 1000L per config
    private int excessWater = 0; // Eliminate rounding errors for water
    private int excessFuel = 0; // Eliminate rounding errors for fuels that burn half items
    private int excessProjectedEU = 0; // Eliminate rounding errors from throttling the boiler
    private int mCasingAmount;
    private int mFireboxAmount;
    protected int pollutionPerSecond = 1; // placeholder for the child classes

    public GT_MetaTileEntity_LargeBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_LargeBoiler(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();

        tt.addMachineType("Boiler")
            .addInfo("Controller block for the Large " + getCasingMaterial() + " Boiler");
        // Tooltip differs between the boilers that output Superheated Steam (Titanium and Tungstensteel) and the ones
        // that do not (Bronze and Steel)
        if (isSuperheated()) {
            tt.addInfo(
                "Produces " + formatNumbers((getEUt() * 40) * ((runtimeBoost(20) / (20f)) / superToNormalSteam))
                    + "L of Superheated Steam with 1 Coal at "
                    + formatNumbers((getEUt() * 40L) / superToNormalSteam)
                    + "L/s") // ?
                .addInfo("A programmed circuit in the main block throttles the boiler (-1000L/s per config)")
                .addInfo("Only some solid fuels are allowed (check the NEI Large Boiler tab for details)")
                .addInfo("If there are any disallowed fuels in the input bus, the boiler won't run!");
        } else {
            tt.addInfo(
                "Produces " + formatNumbers((getEUt() * 40) * (runtimeBoost(20) / 20f))
                    + "L of Steam with 1 Coal at "
                    + formatNumbers(getEUt() * 40L)
                    + "L/s") // ?
                .addInfo("A programmed circuit in the main block throttles the boiler (-1000L/s per config)")
                .addInfo("Solid Fuels with a burn value that is too high or too low will not work");
        }
        tt.addInfo(
            String.format(
                "Diesel fuels have 1/4 efficiency - Takes %s seconds to heat up",
                formatNumbers(500.0 / getEfficiencyIncrease()))) // ? check semifluid again
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 5, 3, false)
            .addController("Front bottom")
            .addCasingInfoRange(getCasingMaterial() + " " + getCasingBlockType() + " Casing", 24, 31, false) // ?
            .addOtherStructurePart(getCasingMaterial() + " Fire Boxes", "Bottom layer, 3 minimum")
            .addOtherStructurePart(getCasingMaterial() + " Pipe Casing Blocks", "Inner 3 blocks")
            .addMaintenanceHatch("Any firebox", 1)
            .addMufflerHatch("Any firebox", 1)
            .addInputBus("Solid fuel, Any firebox", 1)
            .addInputHatch("Liquid fuel, Any firebox", 1)
            .addStructureInfo("You can use either, or both")
            .addInputHatch("Water, Any firebox", 1)
            .addOutputHatch("Steam, any casing", 2)
            .toolTipFinisher("Gregtech");

        return tt;
    }

    public abstract String getCasingMaterial();

    public abstract Block getCasingBlock();

    public abstract String getCasingBlockType();

    public abstract byte getCasingMeta();

    public abstract byte getCasingTextureIndex();

    public abstract Block getPipeBlock();

    public abstract byte getPipeMeta();

    public abstract Block getFireboxBlock();

    public abstract byte getFireboxMeta();

    public abstract byte getFireboxTextureIndex();

    public abstract int getEUt();

    public abstract int getEfficiencyIncrease();

    public int getIntegratedCircuitConfig() {
        return integratedCircuitConfig;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        // allows for 0 pollution if circuit throttle is too high
        return Math.max(
            0,
            (int) (pollutionPerSecond
                * (1 - GT_Mod.gregtechproxy.mPollutionReleasedByThrottle * getIntegratedCircuitConfig())));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { BlockIcons.getCasingTextureForId(getCasingTextureIndex()), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_BOILER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_BOILER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    boolean isFuelValid() {
        if (!isSuperheated()) return true;
        for (ItemStack input : getStoredInputs()) {
            if (!LargeBoilerFuelFakeRecipeMap.isAllowedSolidFuel(input)
                && !Circuit_Integrated.isStackEqual(input, true, true)) {
                // if any item is not in ALLOWED_SOLID_FUELS, operation cannot be allowed because it might still be
                // consumed
                this.mMaxProgresstime = 0;
                this.mEUt = 0;
                return false;
            }
        }
        return true;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (!isFuelValid()) return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        // Do we have an integrated circuit with a valid configuration?
        if (Circuit_Integrated.isStackEqual(mInventory[1], true, true)) {
            int circuit_config = mInventory[1].getItemDamage();
            if (circuit_config >= 1 && circuit_config <= 25) {
                // If so, overwrite the current config
                this.integratedCircuitConfig = circuit_config;
            }
        } else {
            // If not, set the config to zero
            this.integratedCircuitConfig = 0;
        }

        this.mSuperEfficencyIncrease = 0;
        if (!isSuperheated()) {
            for (GT_Recipe tRecipe : RecipeMap.sDieselFuels.mRecipeList) {
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
                if (tFluid != null && tRecipe.mSpecialValue > 1) {
                    tFluid.amount = 1000;
                    if (depleteInput(tFluid)) {
                        this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(tRecipe.mSpecialValue / 2));
                        this.mEUt = adjustEUtForConfig(getEUt());
                        this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease() * 4;
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
            for (GT_Recipe tRecipe : RecipeMap.sDenseLiquidFuels.mRecipeList) {
                FluidStack tFluid = GT_Utility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
                if (tFluid != null) {
                    tFluid.amount = 1000;
                    if (depleteInput(tFluid)) {
                        this.mMaxProgresstime = adjustBurnTimeForConfig(
                            Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2)));
                        this.mEUt = adjustEUtForConfig(getEUt());
                        this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
        }

        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (!tInputList.isEmpty()) {
            if (isSuperheated()) {
                for (ItemStack tInput : tInputList) {
                    if (tInput != GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Lava, 1)) {
                        if (GT_Utility.getFluidForFilledItem(tInput, true) == null
                            && (this.mMaxProgresstime = GT_ModHandler.getFuelValue(tInput) / 80) > 0) {
                            this.excessFuel += GT_ModHandler.getFuelValue(tInput) % 80;
                            this.mMaxProgresstime += this.excessFuel / 80;
                            this.excessFuel %= 80;
                            this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(this.mMaxProgresstime));
                            this.mEUt = adjustEUtForConfig(getEUt());
                            this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                            this.mOutputItems = new ItemStack[] { GT_Utility.getContainerItem(tInput, true) };
                            tInput.stackSize -= 1;
                            updateSlots();
                            if (this.mEfficiencyIncrease > 5000) {
                                this.mEfficiencyIncrease = 0;
                                this.mSuperEfficencyIncrease = 20;
                            }
                            return CheckRecipeResultRegistry.SUCCESSFUL;
                        }
                    }
                }
            } else {
                for (ItemStack tInput : tInputList) {
                    if (tInput != GT_OreDictUnificator.get(OrePrefixes.bucket, Materials.Lava, 1)) {
                        // Solid fuels with burn values below getEUt are ignored (mostly items like sticks), and also
                        // those with very high fuel values that would cause an overflow error.
                        if (GT_Utility.getFluidForFilledItem(tInput, true) == null
                            && (this.mMaxProgresstime = GT_ModHandler.getFuelValue(tInput) / 80) > 0
                            && (GT_ModHandler.getFuelValue(tInput) * 2 / this.getEUt()) > 1
                            && GT_ModHandler.getFuelValue(tInput) < 100000000) {
                            this.excessFuel += GT_ModHandler.getFuelValue(tInput) % 80;
                            this.mMaxProgresstime += this.excessFuel / 80;
                            this.excessFuel %= 80;
                            this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(this.mMaxProgresstime));
                            this.mEUt = adjustEUtForConfig(getEUt());
                            this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                            this.mOutputItems = new ItemStack[] { GT_Utility.getContainerItem(tInput, true) };
                            tInput.stackSize -= 1;
                            updateSlots();
                            if (this.mEfficiencyIncrease > 5000) {
                                this.mEfficiencyIncrease = 0;
                                this.mSuperEfficencyIncrease = 20;
                            }
                            return CheckRecipeResultRegistry.SUCCESSFUL;
                        }
                    }
                }
            }
        }
        this.mMaxProgresstime = 0;
        this.mEUt = 0;
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    abstract int runtimeBoost(int mTime);

    abstract boolean isSuperheated();

    private final int superToNormalSteam = 3;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0) {
            if (this.mSuperEfficencyIncrease > 0) mEfficiency = Math.max(
                0,
                Math.min(
                    mEfficiency + mSuperEfficencyIncrease,
                    getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
            int tGeneratedEU = (int) (this.mEUt * 2L * this.mEfficiency / 10000L);
            if (tGeneratedEU > 0) {
                long amount = (tGeneratedEU + STEAM_PER_WATER) / STEAM_PER_WATER;
                excessWater += amount * STEAM_PER_WATER - tGeneratedEU;
                amount -= excessWater / STEAM_PER_WATER;
                excessWater %= STEAM_PER_WATER;
                if (isSuperheated()) {
                    // Consumes only one third of the water if producing Superheated Steam, to maintain water in the
                    // chain.
                    if (depleteInput(Materials.Water.getFluid(amount / superToNormalSteam))
                        || depleteInput(GT_ModHandler.getDistilledWater(amount / superToNormalSteam))) {
                        // Outputs Superheated Steam instead of Steam, at one third of the amount (equivalent in power
                        // output to the normal Steam amount).
                        addOutput(
                            FluidRegistry.getFluidStack("ic2superheatedsteam", tGeneratedEU / superToNormalSteam));
                    } else {
                        GT_Log.exp.println("Boiler " + this.mName + " had no Water!");
                        explodeMultiblock();
                    }
                } else {
                    if (depleteInput(Materials.Water.getFluid(amount))
                        || depleteInput(GT_ModHandler.getDistilledWater(amount))) {
                        addOutput(GT_ModHandler.getSteam(tGeneratedEU));
                    } else {
                        GT_Log.exp.println("Boiler " + this.mName + " had no Water!");
                        explodeMultiblock();
                    }
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("excessFuel", excessFuel);
        aNBT.setInteger("excessWater", excessWater);
        aNBT.setInteger("excessProjectedEU", excessProjectedEU);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        excessFuel = aNBT.getInteger("excessFuel");
        excessWater = aNBT.getInteger("excessWater");
        excessProjectedEU = aNBT.getInteger("excessProjectedEU");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime > 0 && firstRun) {
            firstRun = false;
            GT_Mod.achievements.issueAchievement(
                aBaseMetaTileEntity.getWorld()
                    .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                "extremepressure");
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_LargeBoiler> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private void onFireboxAdded() {
        mFireboxAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mFireboxAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0) && mCasingAmount >= 24
            && mFireboxAmount >= 3
            && mMaintenanceHatches.size() == 1
            && !mMufflerHatches.isEmpty();
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    private int adjustEUtForConfig(int rawEUt) {
        int adjustedSteamOutput = rawEUt - (isSuperheated() ? 75 : 25) * integratedCircuitConfig;
        return Math.max(adjustedSteamOutput, 25);
    }

    private int adjustBurnTimeForConfig(int rawBurnTime) {
        if (mEfficiency < getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)) {
            return rawBurnTime;
        }
        int adjustedEUt = Math.max(25, getEUt() - (isSuperheated() ? 75 : 25) * integratedCircuitConfig);
        int adjustedBurnTime = rawBurnTime * getEUt() / adjustedEUt;
        this.excessProjectedEU += getEUt() * rawBurnTime - adjustedEUt * adjustedBurnTime;
        adjustedBurnTime += this.excessProjectedEU / adjustedEUt;
        this.excessProjectedEU %= adjustedEUt;
        return adjustedBurnTime;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }
}
