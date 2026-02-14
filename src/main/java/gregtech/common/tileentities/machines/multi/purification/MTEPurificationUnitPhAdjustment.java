package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GTAuthors.AuthorNotAPenguin;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.SimpleShutDownReason;

public class MTEPurificationUnitPhAdjustment extends MTEPurificationUnitBase<MTEPurificationUnitPhAdjustment>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 7;
    private static final int STRUCTURE_Y_OFFSET = 4;
    private static final int STRUCTURE_Z_OFFSET = 1;

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "E   E     E   E", "EAAAE     EAAAE", "EAGAE     EAHAE", "EAGAE     EAHAE", "EAGAE     EAHAE", "EAAAE     EAAAE" },
        { " AAA       AAA ", "A   A     A   A", "A   A     A   A", "A   A     A   A", "A   ABB~BBA   A", "AAAAA     AAAAA" },
        { " AXA       AYA ", "A   A     A   A", "G   A     A   H", "G   ABBBBBA   H", "G             H", "AAAAABRBRBAAAAA" },
        { " AAA       AAA ", "A   A     A   A", "A   A     A   A", "A   A     A   A", "A   AIIIIIA   A", "AAAAA     AAAAA" },
        { "E   E     E   E", "EAAAE     EAAAE", "EAGAE     EAHAE", "EAGAE     EAHAE", "EAGAE     EAHAE", "EAAAE     EAAAE" } };
    // spotless:on

    private static final int CASING_INDEX_MIDDLE = getTextureIndex(GregTechAPI.sBlockCasings9, 7);
    private static final int CASING_INDEX_TOWER = getTextureIndex(GregTechAPI.sBlockCasings9, 8);

    /**
     * The current pH value of the water inside the multiblock
     */
    private float currentpHValue = 0.0f;

    /**
     * The multiblock will try to consume catalyst every CONSUME_INTERVAL ticks.
     */
    private static final int CONSUME_INTERVAL = 1 * SECONDS;

    /**
     * Maximum deviation the initial pH value can have away from the neutral value.
     */
    private static final float INITIAL_PH_DEVIATION = 2.5f;

    /**
     * pH value of entirely pH neutral water.
     */
    private static final float PH_NEUTRAL_VALUE = 7.0f;

    /**
     * Maximum deviation from the neutral value that is allowed for the recipe to succeed.
     */
    private static final float PH_MAX_DEVIATION = 0.05f;

    /**
     * Change in pH value for each piece of alkaline dust supplied.
     */
    public static final float PH_PER_ALKALINE_DUST = 0.01f;

    /**
     * Change in pH value for every 10L of acid supplied.
     */
    public static final float PH_PER_10_ACID_LITER = -0.01f;

    /**
     * Alkaline catalyst material
     */
    public static final Materials ALKALINE_MATERIAL = Materials.SodiumHydroxide;

    /**
     * Acidic catalyst material
     */
    public static final Materials ACIDIC_MATERIAL = Materials.HydrochloricAcid;

    /**
     * The input hatch for the acidic material
     */
    private MTEHatchInput acidInputHatch;
    /**
     * The input bus for the alkaline material
     */
    private MTEHatchInputBus alkalineInputBus;

    /**
     * List of all placed sensor hatches in the multi, so we can update them with the proper pH value when it changes.
     */
    private final ArrayList<MTEHatchPHSensor> sensorHatches = new ArrayList<>();

    private static final IStructureDefinition<MTEPurificationUnitPhAdjustment> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPurificationUnitPhAdjustment>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Extreme Corrosion Resistant Casing
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings9, 8))
        // Naquadah Reinforced Water Plant Casing
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings9, 7))
        .addElement('E', ofFrame(Materials.NaquadahAlloy))
        // pH Resistant Glass
        .addElement('G', ofBlock(GregTechAPI.sBlockGlass1, 0))
        .addElement('H', ofBlock(GregTechAPI.sBlockGlass1, 0))
        // Regular I/O hatches
        .addElement(
            'I',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitPhAdjustment>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .hint(1)
                        .casingIndex(CASING_INDEX_MIDDLE)
                        .build()),
                // Naquadah Reinforced Water Plant Casing
                ofBlock(GregTechAPI.sBlockCasings9, 7)))
        .addElement(
            'R',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitPhAdjustment>buildHatchAdder()
                        .atLeast(SpecialHatchElement.PhSensor)
                        .hint(2)
                        .casingIndex(CASING_INDEX_MIDDLE)
                        .build()),
                // Naquadah Reinforced Water Plant Casing
                ofBlock(GregTechAPI.sBlockCasings9, 7)))
        // Special I/O hatches
        .addElement(
            'X',
            lazy(
                t -> GTStructureUtility.<MTEPurificationUnitPhAdjustment>buildHatchAdder()
                    .atLeast(InputBus)
                    .hint(3)
                    .adder(MTEPurificationUnitPhAdjustment::addAlkalineBusToMachineList)
                    .cacheHint(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.MBTT.InputBus.WithFormat",
                            ALKALINE_MATERIAL.getLocalizedName()))
                    .casingIndex(CASING_INDEX_TOWER)
                    .allowOnly(ForgeDirection.UP)
                    .build()))
        .addElement(
            'Y',
            lazy(
                t -> GTStructureUtility.<MTEPurificationUnitPhAdjustment>buildHatchAdder()
                    .atLeast(InputHatch)
                    .hint(4)
                    .adder(MTEPurificationUnitPhAdjustment::addAcidHatchToMachineList)
                    .cacheHint(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.MBTT.InputHatch.WithFormat",
                            ACIDIC_MATERIAL.getLocalizedName()))
                    .casingIndex(CASING_INDEX_TOWER)
                    .allowOnly(ForgeDirection.UP)
                    .build()))
        .build();

    private List<IHatchElement<? super MTEPurificationUnitPhAdjustment>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, OutputHatch);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MIDDLE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MIDDLE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MIDDLE) };
    }

    public MTEPurificationUnitPhAdjustment(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPurificationUnitPhAdjustment(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPurificationUnitPhAdjustment(this.mName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
    }

    @Override
    public IStructureDefinition<MTEPurificationUnitPhAdjustment> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationPhAdjustmentRecipes;
    }

    public boolean addAcidHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            acidInputHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addAlkalineBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInputBus) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInputBus) aMetaTileEntity).mRecipeMap = null;
            alkalineInputBus = (MTEHatchInputBus) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchPHSensor) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return this.sensorHatches.add((MTEHatchPHSensor) aMetaTileEntity);
        }
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + formatNumber(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Must be linked to a Purification Plant using a data stick to work")
            .addSeparator()
            .addInfo(
                "Initial pH value every cycle varies from " + EnumChatFormatting.RED
                    + (PH_NEUTRAL_VALUE - INITIAL_PH_DEVIATION)
                    + EnumChatFormatting.GRAY
                    + " - "
                    + EnumChatFormatting.RED
                    + (PH_NEUTRAL_VALUE + INITIAL_PH_DEVIATION)
                    + " pH")
            .addInfo(
                "If the pH value is within " + EnumChatFormatting.RED
                    + PH_MAX_DEVIATION
                    + " pH "
                    + EnumChatFormatting.GRAY
                    + "of 7.0 pH at the end of the cycle, the recipe always succeeds")
            .addInfo("Otherwise, the recipe always fails")
            .addInfo("Use a pH Sensor Hatch to read the current pH value")
            .addInfo("For safety, the machine will shut down if the pH goes below 0 or exceeds 14")
            .addSeparator()
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + CONSUME_INTERVAL
                    + EnumChatFormatting.GRAY
                    + " ticks, consumes ALL "
                    + EnumChatFormatting.WHITE
                    + addFormattedString(ALKALINE_MATERIAL.getLocalizedName())
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.WHITE
                    + addFormattedString(ACIDIC_MATERIAL.getLocalizedName())
                    + EnumChatFormatting.GRAY
                    + " in the special hatches")
            .addInfo(
                EnumChatFormatting.RED + "Raises "
                    + EnumChatFormatting.GRAY
                    + "the pH value by "
                    + EnumChatFormatting.RED
                    + PH_PER_ALKALINE_DUST
                    + " pH "
                    + EnumChatFormatting.GRAY
                    + "per piece of "
                    + EnumChatFormatting.WHITE
                    + ALKALINE_MATERIAL.getDust(1)
                        .getDisplayName())
            .addInfo(
                EnumChatFormatting.RED + "Lowers "
                    + EnumChatFormatting.GRAY
                    + "the pH value by "
                    + EnumChatFormatting.RED
                    + -PH_PER_10_ACID_LITER
                    + " pH "
                    + EnumChatFormatting.GRAY
                    + "per "
                    + EnumChatFormatting.RED
                    + "10L "
                    + EnumChatFormatting.GRAY
                    + "of "
                    + EnumChatFormatting.WHITE
                    + ACIDIC_MATERIAL.getFluid(1L)
                        .getLocalizedName())
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "The fourth step of water purification is to neutralize the solution and bring its pH to exactly 7, rendering")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "the solution inert with no hydrogen ion activity beyond water’s natural amphiproticity. Acids and bases from soils")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "and geology cause natural alkalinity variations in water which can cause corrosive reactions with sensitive")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "materials. This necessitates the use of the corresponding neutralizing agents to pH balance the water.")
            .beginStructureBlock(15, 6, 5, false)
            .addController("Front center")
            .addCasingInfoExactlyColored(
                "Stabilized Naquadah Water Plant Casing",
                EnumChatFormatting.GRAY,
                16,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Chemical Grade Glass",
                EnumChatFormatting.GRAY,
                18,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Naquadah Alloy Frame Box",
                EnumChatFormatting.GRAY,
                48,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Inert Neutralization Water Plant Casing",
                EnumChatFormatting.GRAY,
                67 * 2,
                EnumChatFormatting.GOLD,
                false)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.input_hatch_water"),
                EnumChatFormatting.GOLD + "1+",
                1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.output_hatch"),
                EnumChatFormatting.GOLD + "1",
                1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.ph_sensor_hatch"),
                EnumChatFormatting.GOLD + "2",
                2)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.input_bus_sodium_hydroxide"),
                EnumChatFormatting.GOLD + "1",
                3)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.input_hatch_hydrochloric_acid"),
                EnumChatFormatting.GOLD + "1",
                4)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void startCycle(int cycleTime, int progressTime) {
        super.startCycle(cycleTime, progressTime);
        // Randomize initial pH value
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // Generate random integer in [-RNG_PRECISION, RNG_PRECISION]
        final int RNG_PRECISION = 1000;
        int rng = random.nextInt(-RNG_PRECISION, RNG_PRECISION);
        // Remap to [-1.0, 1.0] and then to [-INITIAL_PH_DEVIATION, INITIAL_PH_DEVIATION]
        float deviation = ((float) rng / RNG_PRECISION) * INITIAL_PH_DEVIATION;
        // Round to 2 digits
        this.currentpHValue = Math.round((PH_NEUTRAL_VALUE + deviation) * 100.0f) / 100.0f;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        // Eat all acid and alkaline material every second
        if (mMaxProgresstime > 0 && aTick % CONSUME_INTERVAL == 0) {
            // Important that we drain backwards, since draining stacks can auto-sort the bus
            long totalAlkalineDrained = 0;
            for (int i = alkalineInputBus.getSizeInventory() - 1; i >= 0; --i) {
                ItemStack stack = alkalineInputBus.getStackInSlot(i);
                // If this ItemStack is the alkaline material, drain it entirely and record the amount drained
                if (stack != null && stack.isItemEqual(ALKALINE_MATERIAL.getDust(1))) {
                    totalAlkalineDrained += stack.stackSize;
                    alkalineInputBus.decrStackSize(i, stack.stackSize);
                }
            }

            // Now do fluid, this is simpler since we only need to bother with one slot
            FluidStack stack = acidInputHatch.getDrainableStack();
            int numMultiples = 0;
            if (stack != null && stack.isFluidEqual(ACIDIC_MATERIAL.getFluid(1))) {
                int acidAvailable = stack.amount;
                // We only care about multiples of 10, but we still drain all.
                numMultiples = Math.floorDiv(acidAvailable, 10);
                acidInputHatch.drain(acidAvailable, true);
            } else {
                // Little easier egg: Fluoroantimonic acid has a pH value of -31, it's an acid so strong it will
                // instantly shatter the glass in the structure.

                Fluid acid = FluidRegistry.getFluid("fluoroantimonic acid");
                if (stack != null && stack.getFluid()
                    .equals(acid)) {
                    // TODO: Actually break the glass and trigger achievement lol
                }

            }

            // Adjust pH with to new value
            this.currentpHValue = this.currentpHValue + totalAlkalineDrained * PH_PER_ALKALINE_DUST
                + numMultiples * PH_PER_10_ACID_LITER;

            // Clamp pH to sensible values
            this.currentpHValue = Math.min(Math.max(this.currentpHValue, 0.0f), 14.0f);

            // Round to 2 decimals
            this.currentpHValue = Math.round(this.currentpHValue * 100.0f) / 100.0f;

            // If pH is 0 or 14, stop the machine
            if (Math.abs(this.currentpHValue) < 0.001 || Math.abs(this.currentpHValue - 14.0f) < 0.001) {
                stopMachine(SimpleShutDownReason.ofNormal("critical_ph_value"));
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        // Update sensor hatch
        for (MTEHatchPHSensor hatch : sensorHatches) {
            hatch.updateRedstoneOutput(this.currentpHValue);
        }
    }

    @Override
    public float calculateFinalSuccessChance() {
        // Success chance is 100% when inside target range, 0% otherwise
        float distance = Math.abs(this.currentpHValue - PH_NEUTRAL_VALUE);
        if (distance <= PH_MAX_DEVIATION) {
            return 100.0f;
        } else {
            return 0.0f;
        }
    }

    @Override
    public int getWaterTier() {
        return 4;
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_ZPM;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        // Do not form without positioned hatches
        if (acidInputHatch == null || alkalineInputBus == null) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> infoData = new ArrayList<>(Arrays.asList(super.getInfoData()));
        infoData.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.purification_unit_ph_adjustment.ph",
                "" + EnumChatFormatting.YELLOW + currentpHValue));
        return infoData.toArray(new String[] {});
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("mCurrentpH", this.currentpHValue);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.currentpHValue = aNBT.getFloat("mCurrentpH");
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_PURIFICATION_PH_LOOP;
    }

    private enum SpecialHatchElement implements IHatchElement<MTEPurificationUnitPhAdjustment> {

        PhSensor(MTEPurificationUnitPhAdjustment::addSensorHatchToMachineList, MTEHatchPHSensor.class) {

            @Override
            public long count(MTEPurificationUnitPhAdjustment gtMetaTileEntityPurificationUnitPhAdjustment) {
                return gtMetaTileEntityPurificationUnitPhAdjustment.sensorHatches.size();
            }

            @Override
            public String getDisplayName() {
                return StatCollector.translateToLocal("GT5U.MBTT.pHSensorHatch");
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEPurificationUnitPhAdjustment> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEPurificationUnitPhAdjustment> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEPurificationUnitPhAdjustment> adder() {
            return adder;
        }
    }
}
