package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LNE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.recipe.BartWorksRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTELargeNeutralizationEngineGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.IDualInputHatch;

public class MTELargeNeutralizationEngine extends MTEEnhancedMultiBlockBase<MTELargeNeutralizationEngine>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private int structureTier;
    private int mCasing;

    private static final int HORIZONTAL_OFF_SET = 5;
    private static final int VERTICAL_OFF_SET = 1;
    private static final int DEPTH_OFF_SET = 0;

    // LNE information
    private int fuelValue = 0;
    private int fuelConsumption = 0;
    private float boosterEUBoost = 1.0F;
    private int boosterBoostTicks = 0;
    public int toxicResidue = 0;
    public int residueDecay;
    public int residueIncrease;
    private float robotArmDecayBoost;
    private int robotArmTier;
    private int robotArmAmount;
    public int residueCapacity;

    // random number generation
    private int randomFactor;
    private int randomGoal;
    private boolean isApproachingFromHigher;

    private int maxFluidUse = 200;

    private final static ItemStack FRANCIUM_HYDROXIDE_DUST = Materials.FranciumHydroxide.getDust(1);
    private final static ItemStack CAESIUM_HYDROXIDE_DUST = Materials.CaesiumHydroxide.getDust(1);
    private final static ItemStack POTASSIUM_HYDROXIDE_DUST = Materials.PotassiumHydroxide.getDust(1);
    private final static ItemStack SODIUM_HYDROXIDE_DUST = Materials.SodiumHydroxide.getDust(1);

    private static final String SEPARATOR = EnumChatFormatting.GRAY + " : ";

    private final ArrayList<MTEToxicResidueSensor> sensorHatches = new ArrayList<>();

    public MTELargeNeutralizationEngine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeNeutralizationEngine(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeNeutralizationEngine(this.mName);
    }

    @Nullable
    private static Integer getStructureCasingTier(Block b, int m) {
        if (b != getBlock()) return null;
        if (m <= 4 || m >= 8) return null;
        return m - 4;
    }

    public void updateResidueCapacity() {
        this.residueCapacity = switch (structureTier) {
            case 1 -> 375000;
            case 2 -> 1000000;
            case 3 -> 2500000;
            default -> -1;
        };
    }

    public float getResidueUsedPercentage() {
        if (residueCapacity == 0) return 0;
        return Math.round(10 * (float) toxicResidue / residueCapacity * 100F) / 10F;
    }

    private int getBaseResidueDecay() {
        return switch (structureTier) {
            case 1 -> 200;
            case 2 -> 400;
            case 3 -> 700;
            default -> -1;
        };
    }

    private static int normalizeIntoRange(int random) {
        return Math.max(Math.min(random, 1500), 500);
    }

    private int getDistanceFromGoal() {
        return ((randomFactor - randomGoal) * (isApproachingFromHigher ? 1 : -1));
    }

    /*
     * This algorithm basically just picks a random number (randomGoal)
     * for the randomFactor to "walk" to (it will walk randomly but tend to the randomGoal)
     * Once it reaches the randomGoal it will find a new value to walk towards
     */
    private void randomWalk() {
        if (getDistanceFromGoal() <= 0) {
            do {
                randomGoal = getBaseMetaTileEntity().getRandomNumber(1001) + 500;
            } while (Math.abs(randomFactor - randomGoal) <= 50);
            isApproachingFromHigher = randomFactor > randomGoal;
        }
        int randomChange = getBaseMetaTileEntity().getRandomNumber(23) - 7;
        randomFactor = normalizeIntoRange(randomFactor + (isApproachingFromHigher ? -1 : 1) * randomChange);
    }

    private float getRandomIncreaseMultiplier() {
        float randomIncreaseMultiplier = randomFactor / 1000F;
        randomWalk();
        return randomIncreaseMultiplier;
    }

    private static float getRobotArmDecayBoost(int tier) {
        if (tier <= 5) return (float) Math.pow(1.2F, tier);
        return (float) Math.pow(1.4F, tier);
    }

    private float getResidueScaleDecayBoost() {
        return (float) Math.pow(toxicResidue, 0.08F);
    }

    private float getMachineActiveDecayBoost() {
        return this.isAllowedToWork() ? 1F : 0.1F;
    }

    private int getResidueDecay() {
        return Math.max(
            0,
            (int) (getBaseResidueDecay() * this.robotArmDecayBoost
                * getResidueScaleDecayBoost()
                * getMachineActiveDecayBoost()));
    }

    private float getResidueRate() {
        return (float) (Math.pow(fuelValue, 0.8F) * 0.05F);
    }

    private int getResidueIncrease() {
        return (int) (getResidueRate() * fuelConsumption * getRandomIncreaseMultiplier());
    }

    public int getNetResidue() {
        return residueIncrease - residueDecay;
    }

    private static Block getBlock() {
        return GregTechAPI.sBlockCasings12;
    }

    private int getRobotArmTier() {
        ArrayList<ItemStack> storedInputs = getStoredInputs();
        for (int i = ItemList.ROBOT_ARMS.length - 1; i >= 0; i--) {
            int currentStackSize = 0;
            for (ItemStack storedInput : storedInputs) {
                if (GTUtility.areStacksEqual(storedInput, ItemList.ROBOT_ARMS[i].get(1L))) {
                    currentStackSize = Math.max(currentStackSize, storedInput.stackSize);
                }
            }
            if (currentStackSize > 0) {
                robotArmAmount = currentStackSize;
                return i;
            }
        }
        robotArmAmount = 0;
        return -1;
    }

    private static IStructureDefinition<MTELargeNeutralizationEngine> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<MTELargeNeutralizationEngine> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTELargeNeutralizationEngine>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    transpose(
                        new String[][] { { "           ", "   PPPPP   ", "           " },
                            { "   CC~CC   ", "  P-----P  ", "   CCCCC   " },
                            { "  CCFFFCC  ", " P--CCC--P ", "  CCFFFCC  " },
                            { " CCF   FCC ", "P--C   C--P", " CCF   FCC " },
                            { " CF     FC ", "P-CF   FC-P", " CF     FC " },
                            { " CF     FC ", "P-CF   FC-P", " CF     FC " },
                            { " F F   F F ", " CFCF FCFC ", " F F   F F " } }))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTELargeNeutralizationEngine.class)
                            .atLeast(
                                Dynamo.or(ExoticDynamo),
                                Maintenance,
                                InputBus,
                                InputHatch,
                                SpecialHatchElement.ToxicResidueSensor)
                            .casingIndex(getCasingTextureId())
                            .allowOnly(ForgeDirection.NORTH)
                            .hint(1)
                            .build(),
                        onElementPass(
                            m -> m.mCasing++,
                            ofBlocksTiered(
                                MTELargeNeutralizationEngine::getStructureCasingTier,
                                ImmutableList.of(
                                    Pair.of(getBlock(), Casings.StrengthenedInanimateCasing.getBlockMeta()),
                                    Pair.of(getBlock(), Casings.PreciseStationaryCasing.getBlockMeta()),
                                    Pair.of(getBlock(), Casings.UltimateStaticCasing.getBlockMeta())),
                                -1,
                                (m, t) -> m.structureTier = t,
                                m -> m.structureTier))))
                .addElement('F', ofFrame(Materials.Polytetrafluoroethylene))
                .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private static String getAlkaliTextFormatted(String baseName, int efficiency, int frequency) {
        return EnumChatFormatting.AQUA + baseName
            + SEPARATOR
            + EnumChatFormatting.LIGHT_PURPLE
            + efficiency
            + "%"
            + SEPARATOR
            + EnumChatFormatting.WHITE
            + frequency
            + EnumChatFormatting.GRAY
            + "/minute";
    }

    private static String getTierInfoTextFormatted(int tier, String casingName, int baseDecay, int capacity) {
        return EnumChatFormatting.WHITE + "T"
            + tier
            + SEPARATOR
            + EnumChatFormatting.WHITE
            + casingName
            + SEPARATOR
            + EnumChatFormatting.BLUE
            + baseDecay
            + SEPARATOR
            + EnumChatFormatting.DARK_AQUA
            + formatNumber(capacity);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Acid Generator, LNE")
            .addInfo("(Dis)solves all your problems!")
            .addSeparator()
            .addInfo(
                "Can " + EnumChatFormatting.WHITE
                    + "use "
                    + EnumChatFormatting.GRAY
                    + "a "
                    + EnumChatFormatting.AQUA
                    + "base "
                    + EnumChatFormatting.GRAY
                    + "to boost "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "efficiency "
                    + EnumChatFormatting.GRAY
                    + "(consumes one by one):")
            .addInfo(getAlkaliTextFormatted("Sodium Hydroxide", 150, 60))
            .addInfo(getAlkaliTextFormatted("Potassium Hydroxide", 190, 24))
            .addInfo(getAlkaliTextFormatted("Caesium Hydroxide", 250, 6))
            .addInfo(getAlkaliTextFormatted("Francium Hydroxide", 500, 5))
            .addSeparator()
            .addInfo(
                "Produces " + EnumChatFormatting.RED
                    + "Toxic Residue "
                    + EnumChatFormatting.GRAY
                    + "from burning acids")
            .addInfo(
                "If the " + EnumChatFormatting.RED
                    + "Toxic Residue "
                    + EnumChatFormatting.GRAY
                    + "exceeds the "
                    + EnumChatFormatting.DARK_AQUA
                    + "Capacity"
                    + EnumChatFormatting.GRAY
                    + ", the multiblock will "
                    + EnumChatFormatting.DARK_RED
                    + "EXPLODE!")
            .addInfo(
                "Every " + EnumChatFormatting.WHITE
                    + "tick"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.RED
                    + "Toxic Residue "
                    + EnumChatFormatting.GRAY
                    + "will "
                    + EnumChatFormatting.WHITE
                    + "increase "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.GOLD
                    + "Residue Rate"
                    + EnumChatFormatting.GRAY
                    + "*"
                    + EnumChatFormatting.YELLOW
                    + "Fuel Consumption(L/t)"
                    + EnumChatFormatting.GRAY
                    + "*rand("
                    + EnumChatFormatting.WHITE
                    + "0.5"
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.WHITE
                    + "1.5"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo(
                EnumChatFormatting.GOLD + "Residue Rate "
                    + EnumChatFormatting.GRAY
                    + "is calculated as "
                    + EnumChatFormatting.WHITE
                    + "0.05"
                    + EnumChatFormatting.GRAY
                    + "*("
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Fuel Value (EU/L)"
                    + EnumChatFormatting.GRAY
                    + ")^"
                    + EnumChatFormatting.WHITE
                    + "0.8")
            .addInfo(
                "Every " + EnumChatFormatting.WHITE
                    + "tick"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.RED
                    + "Toxic Residue "
                    + EnumChatFormatting.GRAY
                    + "will "
                    + EnumChatFormatting.WHITE
                    + "decrease "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.BLUE
                    + "Base Decay"
                    + EnumChatFormatting.GRAY
                    + "*"
                    + EnumChatFormatting.YELLOW
                    + "Decay Boost"
                    + EnumChatFormatting.GRAY
                    + "*("
                    + EnumChatFormatting.RED
                    + "Toxic Residue"
                    + EnumChatFormatting.GRAY
                    + "^"
                    + EnumChatFormatting.WHITE
                    + "0.08"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo(
                "Residue will decay " + EnumChatFormatting.WHITE
                    + "10 "
                    + EnumChatFormatting.GRAY
                    + "times slower when multiblock is disabled")
            .addSeparator()
            .addInfo(
                "Insert " + EnumChatFormatting.LIGHT_PURPLE
                    + "Robot Arms "
                    + EnumChatFormatting.GRAY
                    + "to increase "
                    + EnumChatFormatting.YELLOW
                    + "Decay Boost"
                    + EnumChatFormatting.GRAY
                    + ":")
            .addInfo(
                EnumChatFormatting.YELLOW + "Decay Boost "
                    + EnumChatFormatting.GRAY
                    + "is calculated as "
                    + EnumChatFormatting.WHITE
                    + "1.2"
                    + EnumChatFormatting.GRAY
                    + "^"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Robot Arm Tier "
                    + EnumChatFormatting.GRAY
                    + "if "
                    + EnumChatFormatting.GREEN
                    + "IV"
                    + EnumChatFormatting.GRAY
                    + " or below, "
                    + EnumChatFormatting.WHITE
                    + "1.4"
                    + EnumChatFormatting.GRAY
                    + "^"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Robot Arm Tier "
                    + EnumChatFormatting.GRAY
                    + "if "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "LuV"
                    + EnumChatFormatting.GRAY
                    + " or above")
            .addInfo(
                "Insert " + EnumChatFormatting.LIGHT_PURPLE
                    + "multiple "
                    + EnumChatFormatting.GRAY
                    + "("
                    + EnumChatFormatting.WHITE
                    + "16"
                    + EnumChatFormatting.GRAY
                    + " max) robot arms to multiply "
                    + EnumChatFormatting.YELLOW
                    + "Decay Boost "
                    + EnumChatFormatting.GRAY
                    + "by sqrt("
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Robot Arm Amount"
                    + EnumChatFormatting.GRAY
                    + ")")
            .addInfo(
                "Every " + EnumChatFormatting.WHITE
                    + "minute"
                    + EnumChatFormatting.GRAY
                    + ", 1/("
                    + EnumChatFormatting.WHITE
                    + "45"
                    + EnumChatFormatting.GRAY
                    + "*(1+"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Robot Arm Tier"
                    + EnumChatFormatting.GRAY
                    + "))chance for "
                    + EnumChatFormatting.WHITE
                    + "all "
                    + EnumChatFormatting.GRAY
                    + "used robot arms to "
                    + EnumChatFormatting.RED
                    + "void")
            .addSeparator()
            .addInfo(
                "Structure has " + EnumChatFormatting.WHITE
                    + "3 Tiers"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.WHITE
                    + "Tier "
                    + EnumChatFormatting.GRAY
                    + "determines "
                    + EnumChatFormatting.BLUE
                    + "Base Decay "
                    + EnumChatFormatting.GRAY
                    + "and "
                    + EnumChatFormatting.DARK_AQUA
                    + "Capacity"
                    + EnumChatFormatting.GRAY
                    + ":")
            .addInfo(getTierInfoTextFormatted(1, "Strengthened Inanimate Machine Casing", 200, 375000))
            .addInfo(getTierInfoTextFormatted(2, "Precise Stationary Machine Casing", 400, 1000000))
            .addInfo(getTierInfoTextFormatted(3, "Ultimate Static Machine Casing", 700, 2500000))
            .beginStructureBlock(11, 7, 3, true)
            .addController("Top center")
            .addCasingInfoRange("Tiered Casings", 30, 46, false)
            .addCasingInfoExactly("Polytetrafluoroethylene Frame Box", 34, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 15, false)
            .addInputBus("Any Tiered Casing", 1)
            .addInputHatch("Any Tiered Casing", 1)
            .addMaintenanceHatch("Any Tiered Casing", 1)
            .addDynamoHatch("Any Tiered Casing", 1)
            .addOtherStructurePart("Toxic Residue Sensor Hatch", "Any Tiered Casing")
            .addTecTechHatchInfo()
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        structureTier = -1;
        sensorHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        if (mCasing < 30 || structureTier < 1) return false;
        if (mMaintenanceHatches.size() != 1) return false;
        if (getBaseMetaTileEntity() == null) return false;
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        updateHatchTexture();
        updateResidueCapacity();
        return true;
    }

    public void updateHatchTexture() {
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mInputBusses) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mEnergyHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mInputHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatch h : mExoticDynamoHatches) h.updateTexture(getCasingTextureId());
        for (MTEHatchDynamo h : mDynamoHatches) h.updateTexture(getCasingTextureId());
        for (MTEToxicResidueSensor h : sensorHatches) h.updateTexture(getCasingTextureId());
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LNE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LNE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LNE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LNE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    private ITexture getCasingTexture() {
        return getCasingTextureForId(getCasingTextureId());
    }

    private int getCasingTextureId() {
        return switch (structureTier) {
            case 2 -> Casings.PreciseStationaryCasing.getTextureId();
            case 3 -> Casings.UltimateStaticCasing.getTextureId();
            default -> Casings.StrengthenedInanimateCasing.getTextureId();
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return BartWorksRecipeMaps.acidGenFuels;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        structureTier = aValue;
    }

    @Override
    public byte getUpdateData() {
        return (byte) structureTier;
    }

    private void useBooster() {
        if (depleteInput(FRANCIUM_HYDROXIDE_DUST)) {
            this.boosterEUBoost = 5F;
            this.boosterBoostTicks = 240;
        } else if (depleteInput(CAESIUM_HYDROXIDE_DUST)) {
            this.boosterEUBoost = 2.5F;
            this.boosterBoostTicks = 200;
        } else if (depleteInput(POTASSIUM_HYDROXIDE_DUST)) {
            this.boosterEUBoost = 1.9F;
            this.boosterBoostTicks = 50;
        } else if (depleteInput(SODIUM_HYDROXIDE_DUST)) {
            this.boosterEUBoost = 1.5F;
            this.boosterBoostTicks = 20;
        } else {
            this.boosterEUBoost = 1.0F;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("structureTier", (byte) structureTier);
        aNBT.setInteger("fuelValue", fuelValue);
        aNBT.setInteger("fuelConsumption", fuelConsumption);
        aNBT.setFloat("boosterEUBoost", boosterEUBoost);
        aNBT.setInteger("boosterBoostTicks", boosterBoostTicks);
        aNBT.setInteger("toxicResidue", toxicResidue);
        aNBT.setInteger("residueIncrease", residueIncrease);
        aNBT.setInteger("residueDecay", residueDecay);
        aNBT.setFloat("robotArmDecayBoost", robotArmDecayBoost);
        aNBT.setInteger("maxFluidUse", maxFluidUse);
        aNBT.setInteger("randomFactor", randomFactor);
        aNBT.setInteger("nextRandomGoal", randomGoal);
        aNBT.setBoolean("isApproachingFromHigher", isApproachingFromHigher);
        aNBT.setInteger("robotArmTier", robotArmTier);
        aNBT.setInteger("robotArmAmount", robotArmAmount);
        aNBT.setInteger("residueCapacity", residueCapacity);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        structureTier = aNBT.getByte("structureTier");
        fuelValue = aNBT.getInteger("fuelValue");
        fuelConsumption = aNBT.getInteger("fuelConsumption");
        boosterEUBoost = aNBT.getFloat("boosterEUBoost");
        boosterBoostTicks = aNBT.getInteger("boosterBoostTicks");
        toxicResidue = aNBT.getInteger("toxicResidue");
        residueIncrease = aNBT.getInteger("residueIncrease");
        residueDecay = aNBT.getInteger("residueDecay");
        robotArmDecayBoost = aNBT.getFloat("robotArmDecayBoost");
        maxFluidUse = aNBT.getInteger("maxFluidUse");
        randomFactor = aNBT.getInteger("randomFactor");
        randomFactor = normalizeIntoRange(randomFactor);
        randomGoal = aNBT.getInteger("nextRandomGoal");
        randomGoal = normalizeIntoRange(randomGoal);
        isApproachingFromHigher = aNBT.getBoolean("isApproachingFromHigher");
        robotArmTier = aNBT.getInteger("robotArmTier");
        robotArmAmount = aNBT.getInteger("robotArmAmount");
        residueCapacity = aNBT.getInteger("residueCapacity");
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET,
            elementBudget,
            env,
            false,
            true);
    }

    public long getMaximumEUOutput() {
        long aTotal = 0;
        for (MTEHatchDynamo aDynamo : validMTEList(mDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            aTotal += aDynamo.maxAmperesOut() * aVoltage;
        }
        for (MTEHatch aDynamo : validMTEList(mExoticDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            aTotal += aDynamo.maxAmperesOut() * aVoltage;
        }
        return aTotal;
    }

    private int getFuelEUOutput(int fluidAmount) {
        return this.fuelValue * fluidAmount;
    }

    private int getEUOutput(int fluidAmount) {
        return Math
            .min((int) (getFuelEUOutput(fluidAmount) * this.boosterEUBoost), GTUtility.safeInt(getMaximumEUOutput()));
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> storedFluids = this.getStoredFluids();
        if (storedFluids.isEmpty()) {
            this.shutDown();
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;
        }
        for (FluidStack storedFluid : storedFluids) {
            GTRecipe recipe = getRecipeMap().getBackend()
                .findFuel(storedFluid);
            if (recipe == null) continue;
            this.fuelValue = recipe.mSpecialValue;
            FluidStack storedFluidConsume = storedFluid.copy();
            storedFluidConsume.amount = Math.min(storedFluidConsume.amount, maxFluidUse);
            this.fuelConsumption = storedFluidConsume.amount;
            if (!depleteInput(storedFluidConsume)) return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            if (boosterBoostTicks > 0) {
                boosterBoostTicks--;
            } else {
                useBooster();
            }
            this.mEUt = getEUOutput(fuelConsumption);
            this.mEfficiencyIncrease = 50;
            this.mProgresstime = 1;
            this.mMaxProgresstime = 1;
            residueIncrease = getResidueIncrease(); // Residue decrease is in onPostTick so decay can still happen when
                                                    // multi is off
            this.toxicResidue += residueIncrease;
            return CheckRecipeResultRegistry.GENERATING;
        }
        this.shutDown();
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        this.residueIncrease = 0;
        super.onPostTick(aBaseMetaTileEntity, aTick);
        for (MTEToxicResidueSensor toxicResidueSensorHatch : sensorHatches) { // done in onPostTick so it can update
                                                                              // even when multi is off
            toxicResidueSensorHatch.updateRedstoneOutput(toxicResidue, residueCapacity);
        }
        robotArmTier = getRobotArmTier();
        if (robotArmTier != -1) {
            int amount = Math.min(robotArmAmount, 16);
            this.robotArmDecayBoost = (float) (getRobotArmDecayBoost(robotArmTier) * Math.sqrt(amount));
            if (getBaseMetaTileEntity().getWorld()
                .getTotalWorldTime() % MINUTES == 0) {
                int random = getBaseMetaTileEntity().getRandomNumber(45 * (1 + robotArmTier));
                ItemStack robotArmItemStack = ItemList.ROBOT_ARMS[robotArmTier].get(amount);
                if (random == 0) depleteInput(robotArmItemStack);
            }
        } else {
            this.robotArmDecayBoost = 1;
        }
        residueDecay = getResidueDecay();
        this.toxicResidue = Math.max(0, this.toxicResidue - residueDecay);
        if (this.toxicResidue > Math.max(99999, this.residueCapacity)) {// don't explode on construction lmao
            explodeMultiblock();
        }
    }

    private void shutDown() {
        this.mEUt = 0;
        this.mEfficiency = 0;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        for (MTEHatch tHatch : validMTEList(mExoticDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.infodata.large_neutralization_engine")
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + formatNumber(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            getIdealStatus() == getRepairStatus()
                ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                    + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                    + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.engine.output") + ": "
                + EnumChatFormatting.RED
                + formatNumber(((long) mEUt * mEfficiency / 10000))
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.engine.consumption") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelConsumption)
                + EnumChatFormatting.RESET
                + " L/t",
            StatCollector.translateToLocal("GT5U.engine.value") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(fuelValue)
                + EnumChatFormatting.RESET
                + " EU/L",
            StatCollector.translateToLocal("GT5U.engine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + (mEfficiency / 100F)
                + EnumChatFormatting.YELLOW
                + " %",
            StatCollector.translateToLocal("GT5U.multiblock.recipesDone") + ": "
                + EnumChatFormatting.GREEN
                + formatNumber(recipesDone)
                + EnumChatFormatting.RESET };
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTELargeNeutralizationEngineGui(this);
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    public int getMaxFluidUse() {
        return maxFluidUse;
    }

    public void setMaxFluidUse(int maxFluidUse) {
        this.maxFluidUse = maxFluidUse;
    }

    private boolean addSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEToxicResidueSensor sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            return this.sensorHatches.add(sensor);
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<MTELargeNeutralizationEngine> {

        ToxicResidueSensor(MTELargeNeutralizationEngine::addSensorHatchToMachineList, MTEToxicResidueSensor.class) {

            @Override
            public long count(MTELargeNeutralizationEngine gtMetaTileEntityLargeNeutralizationEngine) {
                return gtMetaTileEntityLargeNeutralizationEngine.sensorHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTELargeNeutralizationEngine> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTELargeNeutralizationEngine> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTELargeNeutralizationEngine> adder() {
            return adder;
        }
    }
}
