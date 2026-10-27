package gregtech.common.tileentities.machines.multi.purification;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.ItemList.Hatch_LensHousing;
import static gregtech.api.enums.ItemList.Hatch_LensIndicator;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.items.GGMaterial;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;

@IMetaTileEntity.SkipGenerateDescription
public class MTEPurificationUnitUVTreatment extends MTEPurificationUnitBase<MTEPurificationUnitUVTreatment>
    implements ISurvivalConstructable, ICasingTextureProvider {

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
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_MAIN);
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
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.purification_unit_uv_treatment"))
            .addMarkdown(
                new ResourceLocation("gregtech", "purification-unit-uv-treatment"),
                ImmutableMap.<String, Object>builder()
                    .put("tier", getWaterTier())
                    .put("housing", StatCollector.translateToLocal("GT5U.tooltip.structure.lens_housing"))
                    .put("indicator", StatCollector.translateToLocal("GT5U.tooltip.structure.lens_indicator"))
                    .put("min_swap", formatNumber((float) MIN_TIME_BETWEEN_SWAPS / SECONDS))
                    .put("max_swap", formatNumber((float) MAX_TIME_BETWEEN_SWAPS / SECONDS))
                    .put("success", formatNumber(SUCCESS_PER_LENS))
                    .put(
                        "first_lens",
                        GGMaterial.orundum.get(OrePrefixes.lens, 1)
                            .getDisplayName())
                    .build())
            .beginStructureBlock(13, 9, 9, true)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("144-153", Casings.NaquadriaReinforcedWaterPlantCasing.getLocalizedName(), false)
            .addCasing("144", Casings.ElectronPermeableNeutroniumCoatedGlass.getLocalizedName(), false)
            .addCasing("56", OrePrefixes.frameGt.getLocalizedNameForItem(Materials.StellarAlloy), false)
            .addCasing("29", Casings.HighEnergyUltravioletEmitterCasing.getLocalizedName(), false)
            .addMiscHatch(
                "1",
                StatCollector.translateToLocal("GT5U.tooltip.structure.lens_housing"),
                StatCollector.translateToLocal("gt.mbtt.structure.top_center_casing"),
                2)
            .addMiscHatch(
                "1",
                StatCollector.translateToLocal("GT5U.tooltip.structure.lens_indicator"),
                StatCollector.translateToLocal("gt.mbtt.structure.top_back_casing"),
                3)
            .addInputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_side_casing"), 1)
            .addOutputHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_bottom_side_casing"), 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DataStick.Waterline"))
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
                IGregTechDeviceInformation.encode(
                    "GT5U.infodata.purification_unit_uv_treatment.lens_swaps",
                    "" + EnumChatFormatting.YELLOW + numSwapsPerformed));
            infoData.add(
                IGregTechDeviceInformation.encode(
                    "GT5U.infodata.purification_unit_uv_treatment.lens_requested",
                    EnumChatFormatting.GREEN + lensCycle.current()
                        .getDisplayName()));
            if (removedTooEarly) {
                infoData.add("GT5U.infodata.purification_unit_uv_treatment.removed_too_early");
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

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, STRUCTURE_X_OFFSET, STRUCTURE_Y_OFFSET, STRUCTURE_Z_OFFSET, errors))
            return;
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
        // Do not form without lens bus
        if (lensInputBus == null) {
            errors.add(StructureErrors.missingHatch(Hatch_LensHousing.get(1)));
        }
        if (lensIndicator == null) {
            errors.add(StructureErrors.missingHatch(Hatch_LensIndicator.get(1)));
        }
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

        LensHousing("GT5U.MBTT.LensHousing", MTEPurificationUnitUVTreatment::addLensHousingToMachineList,
            MTEHatchLensHousing.class) {

            @Override
            public long count(MTEPurificationUnitUVTreatment gtMetaTileEntityPurificationUnitUVTreatment) {
                if (gtMetaTileEntityPurificationUnitUVTreatment.lensInputBus == null) return 0;
                else return 1;
            }
        },

        LensIndicator("GT5U.MBTT.LensIndicator", MTEPurificationUnitUVTreatment::addLensIndicatorToMachineList,
            MTEHatchLensIndicator.class) {

            @Override
            public long count(MTEPurificationUnitUVTreatment gtMetaTileEntityPurificationUnitUVTreatment) {
                if (gtMetaTileEntityPurificationUnitUVTreatment.lensIndicator == null) return 0;
                else return 1;
            }
        };

        private final String name;
        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEPurificationUnitUVTreatment> adder;

        @SafeVarargs
        SpecialHatchElement(String name, IGTHatchAdder<MTEPurificationUnitUVTreatment> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.name = name;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTEPurificationUnitUVTreatment> adder() {
            return adder;
        }

        @Override
        public String getDisplayName() {
            return StatCollector.translateToLocal(name);
        }

        @Override
        public String getDescriptionLangKey() {
            return name;
        }
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
    }
}
