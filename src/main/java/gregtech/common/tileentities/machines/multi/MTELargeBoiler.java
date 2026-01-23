package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.STEAM_PER_WATER;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.ItemList.Circuit_Integrated;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_BOILER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.LargeBoilerFuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public abstract class MTELargeBoiler extends MTEEnhancedMultiBlockBase<MTELargeBoiler>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTELargeBoiler>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTELargeBoiler> computeValue(Class<?> type) {
            return StructureDefinition.<MTELargeBoiler>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "ccc", "ccc", "ccc" }, { "ccc", "cPc", "ccc" }, { "ccc", "cPc", "ccc" },
                            { "ccc", "cPc", "ccc" }, { "f~f", "fff", "fff" }, }))
                .addElement('P', lazy(t -> ofBlock(t.getPipeBlock(), t.getPipeMeta())))
                .addElement(
                    'c',
                    lazy(
                        t -> buildHatchAdder(MTELargeBoiler.class).atLeast(OutputHatch)
                            .casingIndex(t.getCasingTextureIndex())
                            .hint(2)
                            .buildAndChain(
                                onElementPass(
                                    MTELargeBoiler::onCasingAdded,
                                    ofBlock(t.getCasingBlock(), t.getCasingMeta())))))
                .addElement(
                    'f',
                    lazy(
                        t -> buildHatchAdder(MTELargeBoiler.class).atLeast(Maintenance, InputHatch, InputBus, Muffler)
                            .casingIndex(t.getFireboxTextureIndex())
                            .hint(1)
                            .buildAndChain(
                                onElementPass(
                                    MTELargeBoiler::onFireboxAdded,
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

    public MTELargeBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeBoiler(String aName) {
        super(aName);
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        tt.addMachineType("Boiler");
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
            .toolTipFinisher();

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
            (int) (pollutionPerSecond * (1 - GTMod.proxy.mPollutionReleasedByThrottle * getIntegratedCircuitConfig())));
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

    boolean isFuelValid() {
        if (!isSuperheated()) return true;
        for (ItemStack input : getStoredInputs()) {
            if (!LargeBoilerFuelBackend.isAllowedSolidFuel(input)
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
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return RecipeMaps.largeBoilerFakeFuels;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
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
            for (GTRecipe tRecipe : RecipeMaps.dieselFuels.getAllRecipes()) {
                FluidStack tFluid = GTUtility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
                if (tFluid != null && tRecipe.mSpecialValue > 1) {
                    tFluid.amount = 1000;
                    if (depleteInput(tFluid)) {
                        this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(tRecipe.mSpecialValue / 2));
                        this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease() * 4;
                        this.mEUt = adjustEUtForConfig(getEUt());
                        if (this.mEfficiencyIncrease > 5000) {
                            this.mEfficiencyIncrease = 0;
                            this.mSuperEfficencyIncrease = 20;
                        }
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
            for (GTRecipe tRecipe : RecipeMaps.denseLiquidFuels.getAllRecipes()) {
                FluidStack tFluid = GTUtility.getFluidForFilledItem(tRecipe.getRepresentativeInput(0), true);
                if (tFluid != null) {
                    tFluid.amount = 1000;
                    if (depleteInput(tFluid)) {
                        this.mMaxProgresstime = adjustBurnTimeForConfig(
                            Math.max(1, runtimeBoost(tRecipe.mSpecialValue * 2)));
                        this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                        this.mEUt = adjustEUtForConfig(getEUt());
                        if (this.mEfficiencyIncrease > 5000) {
                            this.mEfficiencyIncrease = 0;
                            this.mSuperEfficencyIncrease = 20;
                        }
                        return CheckRecipeResultRegistry.SUCCESSFUL;
                    }
                }
            }
        }

        ArrayList<ItemStack> tInputList = getStoredInputs();
        if (!tInputList.isEmpty()) {
            if (isSuperheated()) {
                for (ItemStack tInput : tInputList) {
                    if (tInput != GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Lava, 1)) {
                        if (GTUtility.getFluidForFilledItem(tInput, true) == null
                            && (this.mMaxProgresstime = GTModHandler.getFuelValue(tInput) / 80) > 0) {
                            this.excessFuel += GTModHandler.getFuelValue(tInput) % 80;
                            this.mMaxProgresstime += this.excessFuel / 80;
                            this.excessFuel %= 80;
                            this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                            this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(this.mMaxProgresstime));
                            this.mEUt = adjustEUtForConfig(getEUt());
                            this.mOutputItems = new ItemStack[] { GTUtility.getContainerItem(tInput, true) };
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
                    if (tInput != GTOreDictUnificator.get(OrePrefixes.bucket, Materials.Lava, 1)) {
                        // Solid fuels with burn values below getEUt are ignored (mostly items like sticks), and also
                        // those with very high fuel values that would cause an overflow error.
                        if (GTUtility.getFluidForFilledItem(tInput, true) == null
                            && (this.mMaxProgresstime = GTModHandler.getFuelValue(tInput) / 80) > 0
                            && (GTModHandler.getFuelValue(tInput) * 2L / this.getEUt()) > 1) {
                            this.excessFuel += GTModHandler.getFuelValue(tInput) % 80;
                            this.mMaxProgresstime += this.excessFuel / 80;
                            this.excessFuel %= 80;
                            this.mEfficiencyIncrease = this.mMaxProgresstime * getEfficiencyIncrease();
                            int burnTime = (int) (this.mMaxProgresstime * getLongBurntimeRatio(tInput));
                            this.mMaxProgresstime = adjustBurnTimeForConfig(runtimeBoost(burnTime));
                            this.mEUt = adjustEUtForConfig(getEUt());
                            this.mOutputItems = new ItemStack[] { GTUtility.getContainerItem(tInput, true) };
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

    private double getLongBurntimeRatio(ItemStack tInput) {
        double logScale = Math.log((float) GTModHandler.getFuelValue(tInput) / 1600) / Math.log(9);
        return 1 + logScale * 0.025;
    }

    abstract int runtimeBoost(int mTime);

    abstract boolean isSuperheated();

    private final int superToNormalSteam = 3;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.mEUt > 0) {
            int maxEff = getCorrectedMaxEfficiency(mInventory[1]);
            if (this.mSuperEfficencyIncrease > 0 && mEfficiency < maxEff) {
                mEfficiency = Math.max(0, Math.min(mEfficiency + mSuperEfficencyIncrease, maxEff));
            }
            int tGeneratedEU = (int) (this.mEUt * 2L * this.mEfficiency / 10000L);
            if (tGeneratedEU > 0) {
                long amount = (tGeneratedEU + STEAM_PER_WATER) / STEAM_PER_WATER;
                excessWater += amount * STEAM_PER_WATER - tGeneratedEU;
                amount -= excessWater / STEAM_PER_WATER;
                excessWater %= STEAM_PER_WATER;
                startRecipeProcessing();
                if (isSuperheated()) {
                    // Consumes only one third of the water if producing Superheated Steam, to maintain water in the
                    // chain.
                    if (depleteInput(Materials.Water.getFluid(amount / superToNormalSteam))
                        || depleteInput(GTModHandler.getDistilledWater(amount / superToNormalSteam))) {
                        // Outputs Superheated Steam instead of Steam, at one third of the amount (equivalent in power
                        // output to the normal Steam amount).
                        addOutput(
                            FluidRegistry.getFluidStack("ic2superheatedsteam", tGeneratedEU / superToNormalSteam));
                    } else {
                        GTLog.exp.println("Boiler " + this.mName + " had no Water!");
                        explodeMultiblock();
                    }
                } else {
                    if (depleteInput(Materials.Water.getFluid(amount))
                        || depleteInput(GTModHandler.getDistilledWater(amount))) {
                        addOutput(Materials.Steam.getGas(tGeneratedEU));
                    } else {
                        GTLog.exp.println("Boiler " + this.mName + " had no Water!");
                        explodeMultiblock();
                    }
                }
                endRecipeProcessing();
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
            GTMod.achievements.issueAchievement(
                aBaseMetaTileEntity.getWorld()
                    .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                "extremepressure");
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IStructureDefinition<MTELargeBoiler> getStructureDefinition() {
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

    private int adjustEUtForConfig(int rawEUt) {
        int adjustedSteamOutput = rawEUt - (isSuperheated() ? 75 : 25) * integratedCircuitConfig;
        return Math.max(adjustedSteamOutput, 25);
    }

    private int getCorrectedMaxEfficiency(ItemStack itemStack) {
        return getMaxEfficiency(itemStack) - ((getIdealStatus() - getRepairStatus()) * 1000);
    }

    private int adjustBurnTimeForConfig(int rawBurnTime) {
        // Checks if the fuel is eligible for a super efficiency increase and if so, we want to immediately apply the
        // adjustment!
        // We also want to check that the fuel
        if (mEfficiencyIncrease <= 5000 && mEfficiency < getCorrectedMaxEfficiency(mInventory[1])) {
            return rawBurnTime;
        }
        int adjustedEUt = Math.max(25, getEUt() - (isSuperheated() ? 75 : 25) * integratedCircuitConfig);
        int adjustedBurnTime = (int) (rawBurnTime * (long) getEUt() / adjustedEUt);
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
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_BOILER;
    }

}
