package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorNotAPenguin;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.api.util.shutdown.SimpleShutDownReason;

public class GT_MetaTileEntity_PurificationUnitPhAdjustment
    extends GT_MetaTileEntity_PurificationUnitBase<GT_MetaTileEntity_PurificationUnitPhAdjustment>
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

    private static final int CASING_INDEX_MIDDLE = getTextureIndex(GregTech_API.sBlockCasings9, 7);
    private static final int CASING_INDEX_TOWER = getTextureIndex(GregTech_API.sBlockCasings9, 8);

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
    private GT_MetaTileEntity_Hatch_Input acidInputHatch;
    /**
     * The input bus for the alkaline material
     */
    private GT_MetaTileEntity_Hatch_InputBus alkalineInputBus;

    /**
     * List of all placed sensor hatches in the multi, so we can update them with the proper pH value when it changes.
     */
    private final ArrayList<GT_MetaTileEntity_pHSensor> sensorHatches = new ArrayList<>();

    private static final IStructureDefinition<GT_MetaTileEntity_PurificationUnitPhAdjustment> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PurificationUnitPhAdjustment>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Extreme Corrosion Resistant Casing
        .addElement('A', ofBlock(GregTech_API.sBlockCasings9, 8))
        // Naquadah Reinforced Water Plant Casing
        .addElement('B', ofBlock(GregTech_API.sBlockCasings9, 7))
        .addElement('E', ofFrame(Materials.NaquadahAlloy))
        // pH Resistant Glass
        .addElement('G', ofBlock(GregTech_API.sBlockGlass1, 0))
        .addElement('H', ofBlock(GregTech_API.sBlockGlass1, 0))
        // Regular I/O hatches
        .addElement(
            'I',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .dot(1)
                        .casingIndex(CASING_INDEX_MIDDLE)
                        .build()),
                // Naquadah Reinforced Water Plant Casing
                ofBlock(GregTech_API.sBlockCasings9, 7)))
        .addElement(
            'R',
            ofChain(
                lazy(
                    t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                        .atLeast(SpecialHatchElement.PhSensor)
                        .dot(2)
                        .cacheHint(() -> "pH Sensor Hatch")
                        .casingIndex(CASING_INDEX_MIDDLE)
                        .build()),
                // Naquadah Reinforced Water Plant Casing
                ofBlock(GregTech_API.sBlockCasings9, 7)))
        // Special I/O hatches
        .addElement(
            'X',
            lazy(
                t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                    .atLeast(InputBus)
                    .dot(3)
                    .adder(GT_MetaTileEntity_PurificationUnitPhAdjustment::addAlkalineBusToMachineList)
                    .cacheHint(() -> "Input Bus (" + ALKALINE_MATERIAL.mLocalizedName + ")")
                    .casingIndex(CASING_INDEX_TOWER)
                    .allowOnly(ForgeDirection.UP)
                    .build()))
        .addElement(
            'Y',
            lazy(
                t -> GT_StructureUtility.<GT_MetaTileEntity_PurificationUnitPhAdjustment>buildHatchAdder()
                    .atLeast(InputHatch)
                    .dot(4)
                    .adder(GT_MetaTileEntity_PurificationUnitPhAdjustment::addAcidHatchToMachineList)
                    .cacheHint(() -> "Input Hatch (" + ACIDIC_MATERIAL.mLocalizedName + ")")
                    .casingIndex(CASING_INDEX_TOWER)
                    .allowOnly(ForgeDirection.UP)
                    .build()))
        .build();

    private List<IHatchElement<? super GT_MetaTileEntity_PurificationUnitPhAdjustment>> getAllowedHatches() {
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

    public GT_MetaTileEntity_PurificationUnitPhAdjustment(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PurificationUnitPhAdjustment(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PurificationUnitPhAdjustment(this.mName);
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
        return survivialBuildPiece(
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
    public IStructureDefinition<GT_MetaTileEntity_PurificationUnitPhAdjustment> getStructureDefinition() {
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;
            acidInputHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addAlkalineBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = null;
            alkalineInputBus = (GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_pHSensor) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return this.sensorHatches.add((GT_MetaTileEntity_pHSensor) aMetaTileEntity);
        }
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Purification Unit")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.BOLD
                    + "Water Tier: "
                    + EnumChatFormatting.WHITE
                    + GT_Utility.formatNumbers(getWaterTier())
                    + EnumChatFormatting.RESET)
            .addInfo("Controller block for the pH Neutralization Purification Unit.")
            .addInfo("Must be linked to a Purification Plant using a data stick to work.")
            .addSeparator()
            .addInfo(
                "Initial pH value every cycle varies from " + EnumChatFormatting.RED
                    + (PH_NEUTRAL_VALUE - INITIAL_PH_DEVIATION)
                    + EnumChatFormatting.GRAY
                    + " - "
                    + EnumChatFormatting.RED
                    + (PH_NEUTRAL_VALUE + INITIAL_PH_DEVIATION)
                    + " pH"
                    + EnumChatFormatting.GRAY
                    + ".")
            .addInfo(
                "If the pH value is within " + EnumChatFormatting.RED
                    + PH_MAX_DEVIATION
                    + " pH "
                    + EnumChatFormatting.GRAY
                    + "of 7.0 pH at the end of the cycle, the recipe always succeeds.")
            .addInfo("Otherwise, the recipe always fails.")
            .addInfo("Use a pH Sensor Hatch to read the current pH value.")
            .addInfo("For safety, the machine will shut down if the pH goes below 0 or exceeds 14.")
            .addSeparator()
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + CONSUME_INTERVAL
                    + EnumChatFormatting.GRAY
                    + " ticks, consumes ALL "
                    + EnumChatFormatting.WHITE
                    + ALKALINE_MATERIAL.mLocalizedName
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.WHITE
                    + ACIDIC_MATERIAL.mLocalizedName
                    + EnumChatFormatting.GRAY
                    + " in the special hatches.")
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
                        .getDisplayName()
                    + EnumChatFormatting.GRAY
                    + ".")
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
                        .getLocalizedName()
                    + EnumChatFormatting.GRAY
                    + ".")
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
            .addInfo(AuthorNotAPenguin)
            .beginStructureBlock(7, 4, 7, false)
            .addCasingInfoExactlyColored(
                "Stabilized Naquadah Water Plant Casing",
                EnumChatFormatting.GRAY,
                15,
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
            .addController("Front center")
            .addOtherStructurePart("Input Hatch (Water)", EnumChatFormatting.GOLD + "1+", 1)
            .addOtherStructurePart("Output Hatch", EnumChatFormatting.GOLD + "1", 1)
            .addOtherStructurePart("pH Sensor Hatch", EnumChatFormatting.GOLD + "2", 2)
            .addOtherStructurePart("Input Bus (Sodium Hydroxide)", EnumChatFormatting.GOLD + "1", 3)
            .addOtherStructurePart("Input Hatch (Hydrochloric Acid)", EnumChatFormatting.GOLD + "1", 4)
            .addStructureInfo("Use the StructureLib Hologram Projector to build the structure.")
            .toolTipFinisher("GregTech");
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
                if (GoodGenerator.isModLoaded()) {
                    Fluid acid = FluidRegistry.getFluid("fluoroantimonic acid");
                    if (stack != null && stack.getFluid()
                        .equals(acid)) {
                        // TODO: Actually break the glass and trigger achievement lol
                    }
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
        for (GT_MetaTileEntity_pHSensor hatch : sensorHatches) {
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
        infoData.add("Current pH Value: " + EnumChatFormatting.YELLOW + currentpHValue + " pH");
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
    protected ResourceLocation getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_PURIFICATION_PH_LOOP.resourceLocation;
    }

    private enum SpecialHatchElement implements IHatchElement<GT_MetaTileEntity_PurificationUnitPhAdjustment> {

        PhSensor(GT_MetaTileEntity_PurificationUnitPhAdjustment::addSensorHatchToMachineList,
            GT_MetaTileEntity_pHSensor.class) {

            @Override
            public long count(
                GT_MetaTileEntity_PurificationUnitPhAdjustment gtMetaTileEntityPurificationUnitPhAdjustment) {
                return gtMetaTileEntityPurificationUnitPhAdjustment.sensorHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_PurificationUnitPhAdjustment> adder;

        @SafeVarargs
        SpecialHatchElement(IGT_HatchAdder<GT_MetaTileEntity_PurificationUnitPhAdjustment> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_PurificationUnitPhAdjustment> adder() {
            return adder;
        }
    }
}
