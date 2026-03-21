package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
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

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEPurificationUnitUVTreatment extends MTEPurificationUnitBase<MTEPurificationUnitUVTreatment>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX_MAIN = getTextureIndex(GregTechAPI.sBlockCasings9, 12);

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int STRUCTURE_X_OFFSET = 6;
    private static final int STRUCTURE_Y_OFFSET = 8;
    private static final int STRUCTURE_Z_OFFSET = 0;

    private MTEHatchLensHousing lensInputBus;
    private MTEHatchLensIndicator lensIndicator;

    private UVTreatmentLensCycle lensCycle = null;

    /**
     * Bonus chance to success for each lens swap
     */
    public static final float SUCCESS_PER_LENS = 10.0f;

    /**
     * Maximum amount of ticks between two lens swaps
     */
    public static final int MAX_TIME_BETWEEN_SWAPS = MTEPurificationPlant.CYCLE_TIME_TICKS / 8;
    /**
     * Minimum amount of time between two lens swaps
     */
    public static final int MIN_TIME_BETWEEN_SWAPS = MAX_TIME_BETWEEN_SWAPS / 4;

    public static final ArrayList<ItemStack> LENS_ITEMS = new ArrayList<>();

    private int numSwapsPerformed = 0;
    private int timeUntilNextSwap = 0;

    private boolean removedTooEarly = false;

    private static final String[][] structure = new String[][] {
        // spotless:off
        { "             ", "     DDD     ", "             ", "             ", "             ", "             ", "             ", "     DDD     ", "     H~H     " },
        { "     AAA     ", "   DDAAADD   ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "   DDBBBDD   ", "   AAAAAAA   " },
        { "   AAAAAAA   ", " DDAACCCAADD ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", " DDBB   BBDD ", " AAAAAAAAAAA " },
        { " AAAAAAAAAAA ", "DAACCCCCCCAAD", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", "DBB       BBD", "HAAAAAAAAAAAH" },
        { " AAAAALAAAAA ", "DACCCCCCCCCAD", " B         B ", " B         B ", " B         B ", " B         B ", " B         B ", "DB         BD", "HAAAAAAAAAAAH" },
        { " AAAAAAAAAAA ", "DAACCCCCCCAAD", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", " BB       BB ", "DBB       BBD", "HAAAAAAAAAAAH" },
        { "   AAAAAAA   ", " DDAACCCAADD ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", "   BB   BB   ", " DDBB   BBDD ", " AAAAAAAAAAA " },
        { "     AIA     ", "   DDAAADD   ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "     BBB     ", "   DDBBBDD   ", "   AAAAAAA   " },
        { "             ", "     DDD     ", "             ", "             ", "             ", "             ", "             ", "     DDD     ", "     HHH     " } };
        // spotless:on

    private static final IStructureDefinition<MTEPurificationUnitUVTreatment> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPurificationUnitUVTreatment>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        // Naquadria-Reinforced Water Plant Casing
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings9, 12))
        // Neutronium-Coated UV-Resistant Glass
        .addElement('B', ofBlock(GregTechAPI.sBlockGlass1, 1))
        // UV Backlight sterilizer casing
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings9, 13))
        .addElement('D', ofFrame(Materials.StellarAlloy))
        // Lens housing bus
        .addElement(
            'L',
            lazy(
                t -> GTStructureUtility.<MTEPurificationUnitUVTreatment>buildHatchAdder()
                    .atLeast(SpecialHatchElement.LensHousing)
                    .hint(2)
                    .casingIndex(CASING_INDEX_MAIN)
                    .build()))
        // Lens indicator hatch
        .addElement(
            'I',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitUVTreatment>buildHatchAdder()
                        .atLeast(SpecialHatchElement.LensIndicator)
                        .hint(3)
                        .casingIndex(CASING_INDEX_MAIN)
                        .build()),
                ofBlock(GregTechAPI.sBlockCasings9, 12)))
        // Input or output hatch
        .addElement(
            'H',
            ofChain(
                lazy(
                    t -> GTStructureUtility.<MTEPurificationUnitUVTreatment>buildHatchAdder()
                        .atLeastList(Arrays.asList(InputHatch, OutputHatch))
                        .hint(1)
                        .casingIndex(CASING_INDEX_MAIN)
                        .build()),
                // Naquadria-reinforced Water Plant Casing
                ofBlock(GregTechAPI.sBlockCasings9, 12)))
        .build();

    public MTEPurificationUnitUVTreatment(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPurificationUnitUVTreatment(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPurificationUnitUVTreatment(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN),
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
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN) };
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
    public IStructureDefinition<MTEPurificationUnitUVTreatment> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Purification Unit");
        tt.addInfo(
            EnumChatFormatting.AQUA + ""
                + EnumChatFormatting.BOLD
                + "Water Tier: "
                + EnumChatFormatting.WHITE
                + formatNumber(getWaterTier())
                + EnumChatFormatting.RESET)
            .addInfo("Must be linked to a Purification Plant using a data stick to work")
            .addSeparator()
            .addInfo("During operation, swap the lens in the " + EnumChatFormatting.WHITE + "Lens Housing")
            .addInfo(
                "The multiblock will output a signal through the " + EnumChatFormatting.WHITE + "Lens Indicator Hatch")
            .addInfo("when the current lens must be swapped")
            .addInfo(
                "Lens swaps will be requested in random intervals of " + EnumChatFormatting.RED
                    + (MIN_TIME_BETWEEN_SWAPS / SECONDS)
                    + " to "
                    + (MAX_TIME_BETWEEN_SWAPS / SECONDS)
                    + "s")
            .addSeparator()
            .addInfo(
                "Success chance is boosted by " + EnumChatFormatting.RED
                    + SUCCESS_PER_LENS
                    + "% "
                    + EnumChatFormatting.GRAY
                    + "for each successful swap performed.")
            .addInfo("Removing a lens too early will fail the recipe")
            .addInfo("Find the order of lenses in the recipe in NEI,")
            .addInfo("or use a portable scanner to view the currently requested lens")
            .addInfo("The recipe always starts at the Orundum Lens")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "The sixth step of water purification involves identifying any remaining negatively charged ions within")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "the water which may cause electrical faults in future wafer manufacturing. Bombarding the water with varying")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "wavelengths of photon beams will impart energy into outer-shell electrons, causing them to detach from the")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "atoms themselves and pass through the walls of the tank, ensuring the water is perfectly electrically polar.")
            .beginStructureBlock(13, 9, 9, true)
            .addController("Front center")
            .addCasingInfoRangeColored(
                "Naquadria-Reinforced Water Plant Casing",
                EnumChatFormatting.GRAY,
                147,
                155,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Electron-Permeable Neutronium Coated Glass",
                EnumChatFormatting.GRAY,
                144,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "High Energy Ultraviolet Emitter Casing",
                EnumChatFormatting.GRAY,
                29,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Stellar Alloy Frame Box",
                EnumChatFormatting.GRAY,
                56,
                EnumChatFormatting.GOLD,
                false)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.input_hatch_output_hatch"),
                EnumChatFormatting.GOLD + "1+",
                1)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.lens_housing"),
                EnumChatFormatting.GOLD + "1",
                2)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GT5U.tooltip.structure.lens_indicator"),
                EnumChatFormatting.GOLD + "1",
                3)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.purificationUVTreatmentRecipes;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) {
            this.lensCycle = new UVTreatmentLensCycle(LENS_ITEMS);
        }
        return result;
    }

    private int generateNextSwapTime() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(MIN_TIME_BETWEEN_SWAPS, MAX_TIME_BETWEEN_SWAPS);
    }

    @Override
    public void startCycle(int cycleTime, int progressTime) {
        super.startCycle(cycleTime, progressTime);
        // Reset internal state
        this.timeUntilNextSwap = 0;
        this.numSwapsPerformed = 0;
        this.lensCycle.reset();
        this.removedTooEarly = false;
    }

    private ItemStack getCurrentlyInsertedLens() {
        return this.lensInputBus.getStackInSlot(0);
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);

        // Do no processing if no recipe is running
        if (mMaxProgresstime <= 0) return;

        // This can happen because the lens cycle isn't saved to NBT correctly yet, FIXME
        if (this.lensCycle == null) {
            // FIXME: Properly save current recipe in NBT instead of exiting early
            return;
        }

        ItemStack currentLens = getCurrentlyInsertedLens();

        // If we are currently counting down to a next swap, do so
        if (timeUntilNextSwap > 0) {
            timeUntilNextSwap -= 1;
            // Set the indicator to not output a signal for now
            if (lensIndicator != null) lensIndicator.updateRedstoneOutput(false);

            // If we are counting down to the next swap, and there is no correct lens in the bus, we removed a lens
            // too early
            if (currentLens == null || !currentLens.isItemEqual(lensCycle.current())) {
                removedTooEarly = true;
            }

            // If the time until the next swap became zero, move on to the next requested lens
            if (timeUntilNextSwap == 0) {
                boolean advanced = lensCycle.advance();
                if (!advanced) {
                    // cycle didn't advance, we arrived at the end. This mainly means we want to stop the cycle
                    // The easiest way to do this is by setting the time until next swap larger than the recipe time
                    timeUntilNextSwap = mMaxProgresstime + 1;
                }
            }
        }

        // Time until next swap is zero, this means we are waiting for the user to output a lens.
        else if (timeUntilNextSwap == 0) {
            // Set the indicator to output a signal
            if (lensIndicator != null) lensIndicator.updateRedstoneOutput(true);

            // If we now have a matching lens, we can accept it and move on to the next swap
            if (currentLens != null && currentLens.isItemEqual(lensCycle.current())) {
                numSwapsPerformed += 1;
                timeUntilNextSwap = generateNextSwapTime();
            }
        }
    }

    @Override
    public int getWaterTier() {
        return 6;
    }

    @Override
    public long getBasePowerUsage() {
        return TierEU.RECIPE_UV;
    }

    @Override
    public float calculateFinalSuccessChance() {
        if (removedTooEarly) return 0.0f;
        return numSwapsPerformed * SUCCESS_PER_LENS + currentRecipeChance;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> infoData = new ArrayList<>(Arrays.asList(super.getInfoData()));
        if (this.lensCycle != null) {
            infoData.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.purification_unit_uv_treatment.lens_swaps",
                    "" + EnumChatFormatting.YELLOW + numSwapsPerformed));
            infoData.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.purification_unit_uv_treatment.lens_requested",
                    EnumChatFormatting.GREEN + lensCycle.current()
                        .getDisplayName()));
            if (removedTooEarly) {
                infoData.add(
                    StatCollector.translateToLocal("GT5U.infodata.purification_unit_uv_treatment.removed_too_early"));
            }
        }
        return infoData.toArray(new String[] {});
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("numSwapsPerformed", numSwapsPerformed);
        aNBT.setInteger("timeUntilNextSwap", timeUntilNextSwap);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        numSwapsPerformed = aNBT.getInteger("numSwapsPerformed");
        timeUntilNextSwap = aNBT.getInteger("timeUntilNextSwap");
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET)) return false;
        // Do not form without lens bus
        if (lensInputBus == null) return false;
        return super.checkMachine(aBaseMetaTileEntity, aStack);
    }

    public boolean addLensHousingToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchLensHousing) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            this.lensInputBus = (MTEHatchLensHousing) aMetaTileEntity;
            return true;
        }
        return false;
    }

    public boolean addLensIndicatorToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchLensIndicator) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            this.lensIndicator = (MTEHatchLensIndicator) aMetaTileEntity;
            lensIndicator.updateRedstoneOutput(false);
            return true;
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<MTEPurificationUnitUVTreatment> {

        LensHousing(MTEPurificationUnitUVTreatment::addLensHousingToMachineList, MTEHatchLensHousing.class) {

            @Override
            public long count(MTEPurificationUnitUVTreatment gtMetaTileEntityPurificationUnitUVTreatment) {
                if (gtMetaTileEntityPurificationUnitUVTreatment.lensInputBus == null) return 0;
                else return 1;
            }

            @Override
            public String getDisplayName() {
                return StatCollector.translateToLocal("GT5U.MBTT.LensHousing");
            }
        },

        LensIndicator(MTEPurificationUnitUVTreatment::addLensIndicatorToMachineList, MTEHatchLensIndicator.class) {

            @Override
            public long count(MTEPurificationUnitUVTreatment gtMetaTileEntityPurificationUnitUVTreatment) {
                if (gtMetaTileEntityPurificationUnitUVTreatment.lensIndicator == null) return 0;
                else return 1;
            }

            @Override
            public String getDisplayName() {
                return StatCollector.translateToLocal("GT5U.MBTT.LensIndicator");
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEPurificationUnitUVTreatment> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEPurificationUnitUVTreatment> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEPurificationUnitUVTreatment> adder() {
            return adder;
        }
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
    }
}
