package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class MTEPurificationUnitFlocculation extends MTEPurificationUnitBase<MTEPurificationUnitFlocculation>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "main_survival";

    private static final int STRUCTURE_X_OFFSET = 4;
    private static final int STRUCTURE_Y_OFFSET = 3;
    private static final int STRUCTURE_Z_OFFSET = 0;

    /**
     * Amount of input fluid needed to boost the success chance by another level
     */
    public static final long INPUT_CHEMICAL_PER_LEVEL = 100000;
    /**
     * Amount of waste water produced for each success chance level. This matches the amount of input fluid so it can be
     * perfectly recycled into each other.
     */
    private static final long WASTE_WATER_PER_LEVEL = INPUT_CHEMICAL_PER_LEVEL;
    /**
     * Additive boost to success chance for each level of input fluid supplied
     */
    public static final float SUCCESS_PER_LEVEL = 10.0f;
    /**
     * Amount of ticks between each tick the unit will try to consume input fluid
     */
    private static final int CONSUME_INTERVAL = 1 * SECONDS;

    /**
     * Fluid that needs to be supplied to boost success chance
     */
    private static final Materials INPUT_CHEMICAL = Materials.PolyAluminiumChloride;
    /**
     * Output fluid to be produced as waste. The intended behaviour is that this output fluid can be cycled
     * semi-perfectly into the input fluid.
     */
    private static final Materials OUTPUT_WASTE = Materials.FlocculationWasteLiquid;

    /**
     * Total amount of input fluid consumed during this recipe cycle.
     */
    private long inputFluidConsumed = 0;

    private static final String[][] structure = new String[][]
    // spotless:off
        {
        { "         ", "         ", " BBBBBBB ", " BBB~BBB ", " BBBBBBB " },
        { "         ", "         ", " B     B ", " BWWWWWB ", " BCCCCCB " },
        { "         ", "         ", " B     B ", " GWWWWWG ", " BCAAACB " },
        { "         ", "         ", " B     B ", " GWWWWWG ", " BCAAACB " },
        { "         ", "         ", " B     B ", " GWWWWWG ", " BCAAACB " },
        { "         ", " EE   EE ", " BE   EB ", " BEWWWEB ", " BCCCCCB " },
        { "D       D", "DEE   EED", "DBBBBBBBD", "DBBBBBBBD", "DBBBBBBBD" },
        { "DD     DD", "DD     DD", "DD     DD", "DD     DD", "DD     DD" }
        };
        // spotless:on

    private static final int MAIN_CASING_INDEX = getTextureIndex(GregTechAPI.sBlockCasings9, 6);

    private int casingCount = 0;
    private static final int MIN_CASING = 56;

    private static final IStructureDefinition<MTEPurificationUnitFlocculation> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPurificationUnitFlocculation>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            Arrays.stream(structure)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replaceAll("W", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        // Filter machine casing
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings3, 11))
        .addElement(
            'B',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitFlocculation>buildHatchAdder()
                        .atLeastList(t.getAllowedHatches())
                        .casingIndex(MAIN_CASING_INDEX)
                        .hint(1)
                        .build()),
                // Clean Flocculation Casing
                onElementPass(t -> t.casingCount++, ofBlock(GregTechAPI.sBlockCasings9, 6))))
        // Reinforced Sterile Water Plant Casing
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings9, 5))
        // Sterile Water Plant Casing
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 4))
        .addElement('E', ofFrame(Materials.Adamantium))
        .addElement('W', ofAnyWater(false))
        // Tinted industrial glass
        .addElement('G', ofBlockAnyMeta(GregTechAPI.sBlockTintedGlass))
        .build();

    List<IHatchElement<? super MTEPurificationUnitFlocculation>> getAllowedHatches() {
        return ImmutableList.of(InputBus, InputHatch, OutputBus, OutputHatch);
    }

    public MTEPurificationUnitFlocculation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPurificationUnitFlocculation(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPurificationUnitFlocculation(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX),
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
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(MAIN_CASING_INDEX) };
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
        int built = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            stackSize,
            STRUCTURE_X_OFFSET,
            STRUCTURE_Y_OFFSET,
            STRUCTURE_Z_OFFSET,
            elementBudget,
            env,
            true);
        if (built == -1) {
            GTUtility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done ! Now go place the water yourself !");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEPurificationUnitFlocculation> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // Do not allow rotation when water would flow out
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingCount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;

        // At most three input hatches allowed
        if (mInputHatches.size() > 3) {
            return false;
        }

        // At most three output hatches allowed
        if (mOutputHatches.size() > 3) {
            return false;
        }

        if (casingCount < MIN_CASING) return false;

        return super.checkMachine(aBaseMetaTileEntity, aStack);
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
                "Supply with " + EnumChatFormatting.WHITE
                    + INPUT_CHEMICAL.mLocalizedName
                    + EnumChatFormatting.GRAY
                    + " to operate")
            .addInfo(
                "Outputs " + EnumChatFormatting.WHITE
                    + OUTPUT_WASTE.mLocalizedName
                    + EnumChatFormatting.GRAY
                    + " that can be recycled")
            .addSeparator()
            .addInfo(
                "During operation, will consume ALL " + EnumChatFormatting.WHITE
                    + INPUT_CHEMICAL.mLocalizedName
                    + EnumChatFormatting.GRAY
                    + " in the input hatch")
            .addInfo(
                "At the end of the recipe, for every " + EnumChatFormatting.RED
                    + INPUT_CHEMICAL_PER_LEVEL
                    + "L "
                    + EnumChatFormatting.GRAY
                    + "of "
                    + EnumChatFormatting.WHITE
                    + INPUT_CHEMICAL.mLocalizedName
                    + EnumChatFormatting.GRAY
                    + " consumed")
            .addInfo(
                "gain an additive " + EnumChatFormatting.RED
                    + SUCCESS_PER_LEVEL
                    + "%"
                    + EnumChatFormatting.GRAY
                    + " increase to success. If total fluid supplied is not")
            .addInfo(
                "a multiple of " + EnumChatFormatting.RED
                    + INPUT_CHEMICAL_PER_LEVEL
                    + "L"
                    + EnumChatFormatting.GRAY
                    + ", a penalty to success is applied using the following formula:")
            .addInfo(EnumChatFormatting.GREEN + "Success = Success * 2^(-10 * Overflow ratio)")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Step three in purifying water is to remove microscopic contaminants such as dusts, microplastics and other")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "pollutants using a clarifying agent (In this case, polyaluminium chloride) to cause flocculation - the process")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "of aggregating dispersed suspended particles from a solution into larger clumps for further filtration.")
            .beginStructureBlock(9, 5, 8, false)
            .addController("Front center")
            .addCasingInfoRangeColored(
                "Slick Sterile Flocculation Casing",
                EnumChatFormatting.GRAY,
                MIN_CASING,
                65,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Sterile Water Plant Casing",
                EnumChatFormatting.GRAY,
                30,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Reinforced Sterile Water Plant Casing",
                EnumChatFormatting.GRAY,
                16,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tinted Industrial Glass",
                EnumChatFormatting.GRAY,
                6,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Adamantium Frame Box",
                EnumChatFormatting.GRAY,
                12,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Filter Machine Casing",
                EnumChatFormatting.GRAY,
                9,
                EnumChatFormatting.GOLD,
                false)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "+", 1)
            .addInputHatch(
                EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "-" + EnumChatFormatting.GOLD + "3",
                1)
            .addOutputHatch(
                EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + "-" + EnumChatFormatting.GOLD + "3",
                1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void startCycle(int cycleTime, int progressTime) {
        super.startCycle(cycleTime, progressTime);
        // Reset amount of fluid consumed in this cycle.
        this.inputFluidConsumed = 0;
    }

    @Override
    public void endCycle() {
        super.endCycle();
        // Output waste water proportional to amount of boost levels. We do this even when the recipe fails, so you can
        // always fully recycle.
        // NOTE: If this process ever PRODUCES excess chlorine, there is a recipe bug.
        int levels = calculateBoostLevels();
        long amount = levels * WASTE_WATER_PER_LEVEL;
        this.addFluidOutputs(new FluidStack[] { OUTPUT_WASTE.getFluid(amount) });
        // Make sure to reset consumed fluid (again)
        this.inputFluidConsumed = 0;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);

        // Consume all input fluid periodically, only when running
        if (aTick % CONSUME_INTERVAL == 0 && mMaxProgresstime > 0) {
            // Iterate over all fluids stored
            List<FluidStack> fluids = this.getStoredFluids();
            for (FluidStack fluid : fluids) {
                // If this FluidStack is the input chemical, consume it all
                if (fluid.getFluid()
                    .equals(INPUT_CHEMICAL.mFluid)) {
                    this.inputFluidConsumed += fluid.amount;
                    if (!this.depleteInput(fluid)) {
                        stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
                    }
                }
            }
        }
    }

    private int calculateBoostLevels() {
        return (int) Math.floor((float) this.inputFluidConsumed / (float) INPUT_CHEMICAL_PER_LEVEL);
    }

    @Override
    public float calculateFinalSuccessChance() {
        // Amount of times the required amount of input fluid was fully inserted
        int levels = calculateBoostLevels();
        // Target amount of fluid needed to reach this amount of boost levels
        long targetAmount = levels * INPUT_CHEMICAL_PER_LEVEL;
        // Amount of excess fluid inserted.
        long overflow = inputFluidConsumed - targetAmount;
        // Base boost chance, before applying overflow penalty
        float boost = SUCCESS_PER_LEVEL * levels;
        // If there was any overflow, apply an exponential penalty multiplier based on percentage overflow
        if (overflow > 0) {
            float overflowPct = (float) overflow / INPUT_CHEMICAL_PER_LEVEL;
            float penaltyMultiplier = (float) Math.pow(2.0f, overflowPct * -10.0);
            // First cap to 100%, then apply penalty
            return Math.max(0.0f, Math.min(100.0f, this.currentRecipeChance + boost) * penaltyMultiplier);
        } else {
            return Math.min(100.0f, this.currentRecipeChance + boost);
        }
    }

    @Override
    public int getWaterTier() {
        return 3;
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_ZPM;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationFlocculationRecipes;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> infoData = new ArrayList<>(Arrays.asList(super.getInfoData()));
        infoData.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.purification_unit_flocculation.consumed",
                INPUT_CHEMICAL.mLocalizedName,
                "" + EnumChatFormatting.RED + inputFluidConsumed));
        return infoData.toArray(new String[] {});
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mInputFluidConsumed", inputFluidConsumed);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.inputFluidConsumed = aNBT.getLong("mInputFluidConsumed");
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_COAGULATION_LOOP;
    }
}
