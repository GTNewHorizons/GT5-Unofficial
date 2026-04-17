package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.furnaceRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static kubatech.loaders.ArcFurnaceLoader.ARC_FURNACE_ELECTRODE;
import static kubatech.tileentity.gregtech.multiblock.MTEIndustrialArcFurnace.ArcFurnaceHatches.ElectrodeDetectorHatch;
import static kubatech.tileentity.gregtech.multiblock.MTEIndustrialArcFurnace.ArcFurnaceHatches.ElectrodeHatch;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import kubatech.api.arcfurnace.ArcFurnaceContext;
import kubatech.api.arcfurnace.ArcFurnaceProcessingEvent;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.loaders.ArcFurnaceElectrode;
import kubatech.tileentity.gregtech.hatch.MTEElectrodeDetectorHatch;
import kubatech.tileentity.gregtech.hatch.MTEElectrodeHatch;

public class MTEIndustrialArcFurnace extends KubaTechGTMultiBlockBase<MTEIndustrialArcFurnace>
    implements ISurvivalConstructable, ArcFurnaceContext {

    private static final int STARTUP_DURATION_TICKS = 20 * 6;
    private static final int SHUTDOWN_DURATION_TICKS = 20 * 6;
    private static final int ORE_MODE_STARTUP_TICKS = 20 * 10;
    private static final int ORE_MODE_IDLE_FINISH_TICKS = 5;
    private static final int ARC_SURGE_DURABILITY_THRESHOLD_PERCENT = 30;
    private static final int ARC_SURGE_CHANCE_PERCENT = 5;
    private static final int ARC_BREAK_DURABILITY_THRESHOLD_PERCENT = 10;
    private static final int ARC_BREAK_CHANCE_PERCENT = 2;
    private static final int BLAST_MODE_POWER_MULTIPLIER = 16;
    private static final double ARC_SURGE_DAMAGE_THRESHOLD = 1d - (ARC_SURGE_DURABILITY_THRESHOLD_PERCENT / 100d);
    private static final double ARC_BREAK_DAMAGE_THRESHOLD = 1d - (ARC_BREAK_DURABILITY_THRESHOLD_PERCENT / 100d);

    public MTEIndustrialArcFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEIndustrialArcFurnace(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialArcFurnace(this.mName);
    }

    private int mCasing = 0;
    private MTEElectrodeHatch electrodeHatch;
    private final List<MTEElectrodeDetectorHatch> electrodeDetectorHatch = new ArrayList<>();

    enum ArcFurnaceMode {

        Normal,
        Blast,
        Ore;

        static final ArcFurnaceMode[] modes = values();

        ArcFurnaceMode next() {
            return modes[(this.ordinal() + 1) % modes.length];
        }
    }

    enum ArcFurnacePhase {

        Standby,
        ArcIgnition,
        Processing,
        ArcShutdown;

        static final ArcFurnacePhase[] values = values();
    }

    private ArcFurnaceMode mode = ArcFurnaceMode.Normal;
    private ArcFurnacePhase phase = ArcFurnacePhase.Standby;
    private ArcFurnaceElectrode electrode = null;
    private int durabilityCostThisRun = 1;
    private NBTTagCompound effectState = new NBTTagCompound();
    private double electrodeDamagePercentage = 0d;
    private GTRecipe fakeRecipeCache = null;

    private int didOres = 0;
    private int didOCs = 0;
    private final Map<Fluid, Long> oreOutputs = new Object2LongOpenHashMap<>();

    private static final int OFFSET_H = 6;
    private static final int OFFSET_V = 7;
    private static final int OFFSET_D = 1;
    private static final byte START_ARC_SOUND_INDEX = 10;
    private static final byte STOP_ARC_SOUND_INDEX = 11;
    private static final byte LOOP_ARC_SOUND_INDEX = 12;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIndustrialArcFurnace> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialArcFurnace>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { // spotless:off
                    {"                 ","                 ","                 ","                 ","     DDD         ","    D   D        ","    D   D        ","    D   D        ","     DDD         ","     D D         ","     D D         ","     D D         ","     D D         ","     D D         ","     D D         ","     D D         ","     D D         ","     DDD         ","                 "},
                    {"                 ","                 ","                 ","                 ","                 ","     IDI         ","     D D         ","     DID         ","      D          ","      D          ","      D          ","      D          ","      D          ","      D          ","      D          ","    A D A        ","    AD DA        ","                 ","     D D         "},
                    {"                 ","                 ","                 ","                 ","                 ","     I I         ","                 ","      I          ","                 ","                 ","                 ","                 ","              E  ","             EDE ","              E  ","    A   A        ","    DD DD        ","    A   A        ","     DHD         "},
                    {"                 ","                 ","                 ","                 ","                 ","     I I         ","                 ","      I          ","                 ","                 ","                 ","      F          ","      FF      E  ","       FFFFFFE E ","              E  ","    A   A        ","    DD DD        ","    A   A        ","     HHH         "},
                    {"                 ","                 ","                 ","      B          ","    BBBBB        ","    BI IB        ","   BB   BB       ","    BBIBB        ","    BBBBB        ","                 ","      F          ","      F          ","             DED ","             E E ","             DED ","    A   A        ","    DD DD        ","    A   A        ","     HHH         "},
                    {"                 ","    EEEEE        ","   EEBBBEE       ","  EBBB BBBE      "," EEB     BEE     "," EBB I I BBE     "," EB       BE     "," EBB  I  BBE     "," EEB     BEE     ","  EBBBBBBBE      ","   EEBFBEE       ","    EEEEE        ","             DED ","             E E ","             DED ","    A   A        ","    DD DD        ","    A   A        ","     HHH         "},
                    {"    D   D        ","   AABABAA       ","  A       A      "," A         A     ","DA         AD    "," B         B     "," C         C     "," B         B     ","DA         AD    "," A         A     ","  A       A      ","   AABCBAA       ","    D   D    DED ","             E E ","             DED ","    A   A        ","    DD DD        ","    A   A        ","     HHH         "},
                    {"    D   D        ","   GAB~BAG       ","  G       G      "," G         G     ","DA         AD    "," B         B     "," C         C     "," B         B     ","DA         AD    "," G         G     ","  G       G      ","   GABCBAG       ","    D   D    DED ","             E E ","             DED ","    A   A        ","    DD DD        ","    A   A        ","     HHH         "},
                    {"    D   D        ","   AABABAA       ","  A       A      "," A         A     ","DA         AD    "," B         B     "," C         C     "," B         B     ","DA         AD    "," A         A     ","  A       A      ","   AABCBAA       ","    D   D    DED ","             E E ","    A   A    DED ","    A   A        ","    DD DD        ","    A   A        ","     DHD         "},
                    {"    D   D        ","    A   A        ","   AAAAAAA       ","  AAAA AAAA      ","DAAAAC CAAAAD    ","  AACC CCAA      ","  CCC   CCC      ","  AACC CCAA      ","DAAAACCCAAAAD    ","  AAAACAAAA      ","   AAACAAA       ","    A   A    BBB ","    D   D   BEEEB","            BE EB","    A   A   BEEEB","    A   A    BBB ","    DD DD        ","    A   A        ","    ADDDA        "},
                    {"    D   D        ","  DDA   ADD      "," D   AAA   D     "," D    A    D     ","DA    A    AD    ","D     A     D    ","D    AAA    D    ","D     A     D    ","DA         AD    "," D         D     "," D         D     ","  DDA   ADD  BBB ","    D   D   B   B","            B   B","    A   A   B   B","    A   A    BBB ","    AD DA        ","    A   A        ","    ADDDA        "}
                } // spotless:on
            ))
        .addElement(
            'A',
            buildHatchAdder(MTEIndustrialArcFurnace.class)
                .atLeast(
                    ElectrodeHatch,
                    ElectrodeDetectorHatch,
                    InputBus,
                    OutputBus,
                    InputHatch,
                    OutputHatch,
                    Maintenance,
                    Energy,
                    ExoticEnergy)
                .casingIndex(Casings.SolidSteelMachineCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(e -> e.mCasing++, Casings.SolidSteelMachineCasing.asElement())))
        .addElement('B', Casings.SteelPipeCasing.asElement())
        .addElement('C', Casings.CupronickelCoilBlock.asElement())
        .addElement('D', ofFrame(Materials.Steel))
        .addElement('E', Casings.BoltedNaquadahCasing.asElement())
        .addElement('F', Casings.InsulatedFluidPipeCasing.asElement())
        .addElement('G', Casings.HeatProofCokeOvenCasing.asElement())
        .addElement('H', Casings.BlastSmelterHeatContainmentCoil.asElement())
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings13, 4))
        .build();

    @Override
    public IStructureDefinition<MTEIndustrialArcFurnace> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_H, OFFSET_V, OFFSET_D);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_H,
            OFFSET_V,
            OFFSET_D,
            elementBudget,
            env,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_H, OFFSET_V, OFFSET_D)) return false;
        if (mCasing < 10) return false;
        if (electrodeHatch == null) return false;
        if (electrode == null && electrodeHatch.getStackInSlot(0) != null) electrodeChanged();
        if (electrodeDetectorHatch != null) {
            if (electrode != null)
                updateDetectorHatches(ARC_FURNACE_ELECTRODE.remainingDurability(electrodeHatch.getStackInSlot(0)));
            else updateDetectorHatches(0);
        }
        return true;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        electrodeHatch = null;
        electrodeDetectorHatch.clear();
    }

    private boolean addElectrodeHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (electrodeHatch != null) return false;
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEElectrodeHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            electrodeHatch = hatch;
            return true;
        }
        return false;
    }

    private boolean addElectrodeDetectorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEElectrodeDetectorHatch hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            electrodeDetectorHatch.add(hatch);
            return true;
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = ArcFurnaceMode.modes[aNBT.getInteger("mode")];
        phase = ArcFurnacePhase.values[aNBT.getInteger("phase")];
        int electrodeOrdinal = aNBT.getInteger("electrode");
        electrode = ArcFurnaceElectrode.getById(electrodeOrdinal);
        electrodeDamagePercentage = aNBT.getDouble("electrodeDamagePercentage");
        durabilityCostThisRun = aNBT.getInteger("durabilityCostThisRun");
        effectState = aNBT.hasKey("effectState") ? aNBT.getCompoundTag("effectState") : new NBTTagCompound();
        didOres = aNBT.getInteger("didOres");
        didOCs = aNBT.getInteger("didOCs");
        oreOutputs.clear();
        int oreOutputsSize = aNBT.getInteger("oreOutputsSize");
        for (int i = 0; i < oreOutputsSize; i++) {
            String fluidName = aNBT.getString("oreOutputFluid" + i);
            long amount = aNBT.getLong("oreOutputAmount" + i);
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            if (fluid != null) {
                oreOutputs.put(fluid, amount);
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mode", mode.ordinal());
        aNBT.setInteger("phase", phase.ordinal());
        aNBT.setInteger("electrode", electrode == null ? -1 : electrode.ordinal());
        aNBT.setDouble("electrodeDamagePercentage", electrodeDamagePercentage);
        aNBT.setInteger("durabilityCostThisRun", durabilityCostThisRun);
        aNBT.setTag("effectState", effectState);
        aNBT.setInteger("didOres", didOres);
        aNBT.setInteger("didOCs", didOCs);
        aNBT.setInteger("oreOutputsSize", oreOutputs.size());
        int i = 0;
        for (Map.Entry<Fluid, Long> entry : oreOutputs.entrySet()) {
            aNBT.setString("oreOutputFluid" + i, FluidRegistry.getFluidName(entry.getKey()));
            aNBT.setLong("oreOutputAmount" + i, entry.getValue());
            i++;
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Arc Furnace, IAF")
            .addInfo("Insert electrode in the electrode hatch")
            .addInfo("Speed, Parallel, OC and power use depend on the electrode")
            .addInfo("Some electrodes have special effects, check their tooltip")
            .addInfo(
                "Below " + EnumChatFormatting.RED
                    + ARC_SURGE_DURABILITY_THRESHOLD_PERCENT
                    + EnumChatFormatting.GRAY
                    + "% durability: "
                    + EnumChatFormatting.RED
                    + ARC_SURGE_CHANCE_PERCENT
                    + EnumChatFormatting.GRAY
                    + "% chance for random arc surge")
            .addInfo(
                "Below " + EnumChatFormatting.RED
                    + ARC_BREAK_DURABILITY_THRESHOLD_PERCENT
                    + EnumChatFormatting.GRAY
                    + "% durability: "
                    + EnumChatFormatting.RED
                    + ARC_BREAK_CHANCE_PERCENT
                    + EnumChatFormatting.GRAY
                    + "% chance to destroy items in the arc")
            .addInfo("Startup: machine ignites the arc before processing")
            .addInfo("Startup power: based on electrode startup surge and parallels")
            .addInfo("Shutdown: machine powers down the arc after work ends")
            .addInfo("-------------------------------Blast mode----------------------------------")
            .addInfo(
                "Processes EBF recipes at " + EnumChatFormatting.RED
                    + BLAST_MODE_POWER_MULTIPLIER
                    + EnumChatFormatting.GRAY
                    + "x power cost")
            .addInfo("--------------------------------Ore Mode----------------------------------")
            .addInfo("Quickly process metallic ores!")
            .addInfo(
                "Startup time: " + EnumChatFormatting.RED
                    + (ORE_MODE_STARTUP_TICKS / 20)
                    + EnumChatFormatting.GRAY
                    + " seconds")
            .addInfo("Consumes all ores and raw ores from input hatches")
            .addInfo("Only furnace-smeltable ores, no blasting")
            .addInfo("If queued ore exceeds capacity, startup ends immediately")
            .addInfo(
                "If no ores enter for " + EnumChatFormatting.RED
                    + ORE_MODE_IDLE_FINISH_TICKS
                    + EnumChatFormatting.GRAY
                    + " ticks, startup ends immediately")
            .addInfo("Outputs molten metals")
            .addInfo("Right-click with Screwdriver to change mode")
            .beginStructureBlock(17, 11, 19, false)
            .addController("Front center")
            .addCasingInfoMin("Solid Steel Machine Casing", 10, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addOtherStructurePart("Electrode Hatch", "Any Casing", 1)
            .addOtherStructurePart("Electrode Sensor Hatch", "Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        final ITexture casingTexture = Casings.SolidSteelMachineCasing.getCasingTexture();
        if (side == facing) {
            if (active) return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexture, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexture };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (getBaseMetaTileEntity().isActive() && phase == ArcFurnacePhase.Processing) {
            GTUtility.sendChatTrans(aPlayer, "kubatech.chat.forbidden_while_running");
            return;
        }
        mode = mode.next();
        GTUtility.sendChatTrans(aPlayer, "kubatech.chat.mode.generic", mode.name());
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_ARC_FURNACE_LOOP;
    }

    @Override
    protected void doActivitySound(SoundResource activitySound) {
        if (phase != ArcFurnacePhase.Processing) return;
        super.doActivitySound(activitySound);
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        switch (aIndex) {
            case START_ARC_SOUND_INDEX -> {
                phase = ArcFurnacePhase.Standby;
                GTUtility.doSoundAtClient(
                    SoundResource.GT_MACHINES_ARC_FURNACE_STARTUP,
                    STARTUP_DURATION_TICKS,
                    1.0F,
                    aX,
                    aY,
                    aZ);
            }
            case STOP_ARC_SOUND_INDEX -> {
                phase = ArcFurnacePhase.Standby;
                GTUtility.doSoundAtClient(
                    SoundResource.GT_MACHINES_ARC_FURNACE_SHUTDOWN,
                    SHUTDOWN_DURATION_TICKS,
                    1.0F,
                    aX,
                    aY,
                    aZ);
            }
            case LOOP_ARC_SOUND_INDEX -> phase = ArcFurnacePhase.Processing;
            default -> super.doSound(aIndex, aX, aY, aZ);
        }
    }

    @Override
    public int getDurabilityConsumptionThisRun() {
        return durabilityCostThisRun;
    }

    @Override
    public void setDurabilityConsumptionThisRun(int durability) {
        durabilityCostThisRun = durability;
    }

    @Override
    public NBTTagCompound getEffectState() {
        return effectState;
    }

    @Override
    public void resetEffectState() {
        effectState = new NBTTagCompound();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return mode == ArcFurnaceMode.Normal ? arcFurnaceRecipes
            : (mode == ArcFurnaceMode.Blast ? blastFurnaceRecipes : furnaceRecipes);
    }

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(arcFurnaceRecipes, blastFurnaceRecipes, furnaceRecipes);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean depleteInputAndUpdate(ItemStack stack) {
        startRecipeProcessing();
        boolean result = depleteInput(stack);
        if (result) {
            updateSlots();
        }
        endRecipeProcessing();
        return result;
    }

    @Override
    public int getRandomNumber(int range) {
        return getBaseMetaTileEntity().getRandomNumber(range);
    }

    private int calculateMaximumParallel(long eut) {
        if (electrode == null) return 0;
        eut = (long) ((double) eut * electrode.amperagePerParallel);
        long volts = getAverageInputVoltage();
        long amps = getMaxInputAmps();
        int paraLimit = electrode.parallelLimit;
        long para = (volts * amps) / eut;
        if (para > paraLimit) para = paraLimit;
        return (int) para;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        if (electrode == null) return;
        logic.setSpeedBonus(1d / electrode.speedModifier);
        logic.setMaxParallel(electrode.parallelLimit);
        logic.setOverclock(electrode.OCSpeedFactor, electrode.OCPowerFactor);
        logic.setEuModifier(electrode.amperagePerParallel);
        logic.setAvailableVoltage(getAverageInputVoltage());
        logic.setAvailableAmperage(getMaxInputAmps());
        logic.setMaxTierSkips(0);
        logic.noRecipeCaching();
        applySpecialEffect(new ArcFurnaceProcessingEvent.EventConfigureProcessing(this, logic));
    }

    private void applySpecialEffect(ArcFurnaceProcessingEvent event) {
        if (electrode != null && electrode.specialEffect != null) {
            electrode.specialEffect.accept(event);
        }
    }

    private void resetElectrodeEffectState() {
        resetEffectState();
        applySpecialEffect(new ArcFurnaceProcessingEvent.EventReset(this));
        didOres = 0;
        didOCs = 0;
        oreOutputs.clear();
    }

    private void startShutDown() {
        phase = ArcFurnacePhase.ArcShutdown;
        resetElectrodeEffectState();
        mMaxProgresstime = SHUTDOWN_DURATION_TICKS;
        lEUt = 0;
        checkRecipeResult = SimpleCheckRecipeResult.ofSuccess("arc_shutdown");
        ArcFurnaceProcessingEvent.EventStartShutdown event = new ArcFurnaceProcessingEvent.EventStartShutdown(
            this,
            mMaxProgresstime);
        applySpecialEffect(event);
        mMaxProgresstime = event.duration;
        sendSound(STOP_ARC_SOUND_INDEX);
    }

    private void updateDetectorHatches(int durability) {
        for (var hatch : electrodeDetectorHatch) {
            hatch.updateRedstoneOutput(durability);
        }
    }

    private void electrodeChanged() {
        if (phase == ArcFurnacePhase.Processing || phase == ArcFurnacePhase.ArcIgnition) {
            stopMachine(SimpleShutDownReason.ofCritical("electrode_changed"));
            phase = ArcFurnacePhase.Standby;
        } else {
            resetElectrodeEffectState();
        }
        ItemStack electrodeStack = electrodeHatch.getStackInSlot(0);
        electrode = ArcFurnaceElectrode.getByStack(electrodeStack);
        if (electrode != null) {
            electrodeDamagePercentage = (double) ARC_FURNACE_ELECTRODE.usedDurability(electrodeStack)
                / (double) electrode.durability;
            updateDetectorHatches(ARC_FURNACE_ELECTRODE.remainingDurability(electrodeStack));
            return;
        }
        updateDetectorHatches(0);
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        /*
         * Ore mode has two sequences:
         * When recipe is found, a simple no input no output recipe is started that almost takes no power and has
         * ORE_MODE_STARTUP_TICKS duration
         * To avoid creating additional variables this phase is represented by didOres not being set to -1
         * In this phase we check every 5 ticks if there are ores to process in input busses,
         * if there is anything to process we consume the ores and increase didOres and didOCs accordingly,
         * if there is nothing to process for 5 ticks or the possible overclocks/parallel is used up we end the phase by
         * forcing mProgresstime to be one tick before mMaxProgresstime
         * which starts the second phase, where the actual processing happens
         * we set all the outputs and calculate additional overclock if it wasn't used up in the first phase and then
         * start processing with the normal logic of the machine
         */
        if (phase == ArcFurnacePhase.Processing && mode == ArcFurnaceMode.Ore
            && mMaxProgresstime > 0
            && didOres != -1
            && (aTick % 5 == 0 || mProgresstime == 0)) {
            int maxOCs = (int) GTUtility.log4(getAverageInputVoltage() / 32);
            int OCsLeft = maxOCs - didOCs;
            if (electrode.OCSpeedFactor == 1) OCsLeft = 0;
            /*
             * We simulate batchmode/underonetick overclock here, instead of decreasing time, we increase the amount of
             * parallels
             * We cannot use the normal Processing logic for this, because it would require us to have correct recipe
             * map
             */
            int maxPara = (int) ((double) calculateMaximumParallel(30) * Math.pow(electrode.OCSpeedFactor, didOCs));
            boolean didSomething = false;
            int paraToDoLeft = maxPara - didOres;
            startRecipeProcessing();
            // processing ores
            for (ItemStack inputItem : getAllStoredInputs()) {
                ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(inputItem, false, null);
                if (smeltedOutput != null) {
                    ItemData data = GTOreDictUnificator.getItemData(inputItem);
                    if (data != null && data.mPrefix != null
                        && data.mPrefix.getMaterialPostfix()
                            .equals(" Ore")) {
                        ItemData outputData = GTOreDictUnificator.getItemData(smeltedOutput);
                        if (outputData != null) {
                            FluidStack output = outputData.mMaterial.mMaterial.getMolten(1);
                            long amount = outputData.mPrefix.getMaterialAmount() / (GTValues.M / 144L)
                                * smeltedOutput.stackSize;
                            if (output != null) {
                                int toDo = Math.min(paraToDoLeft, inputItem.stackSize);
                                amount *= toDo;
                                oreOutputs.merge(output.getFluid(), amount, Long::sum);
                                inputItem.stackSize -= toDo;
                                paraToDoLeft -= toDo;
                                didOres += toDo;
                                didSomething = true;
                                if (paraToDoLeft == 0 && OCsLeft > 0) {
                                    didOCs++;
                                    maxPara = (int) ((double) maxPara * electrode.OCSpeedFactor);
                                    paraToDoLeft = maxPara - didOres;
                                }
                            }
                        }
                    }
                }
            }
            endRecipeProcessing();
            /*
             * Didn't do anything or used up all the parallels and overclocks, we end the startup phase and start
             * processing whatever we got
             * Note: First tick is gauranteed to do anything, because the actual recipe check that starts the startup
             * phase is done only after
             * checking if there is any ore to process
             */
            if (!didSomething || paraToDoLeft <= 0 && OCsLeft <= 0) {
                mProgresstime = mMaxProgresstime - 1;
            }
        }
        if (phase == ArcFurnacePhase.Processing && mode == ArcFurnaceMode.Ore
            && mProgresstime > 0
            && mProgresstime >= mMaxProgresstime - 1
            && !oreOutputs.isEmpty()) {
            mProgresstime = 0;
            mMaxProgresstime = 20 * 1;
            List<FluidStack> outputs = new ArrayList<>(oreOutputs.size());
            for (Map.Entry<Fluid, Long> entry : oreOutputs.entrySet()) {
                long value = entry.getValue();
                while (value > 0) {
                    long amount = Math.min(value, Integer.MAX_VALUE);
                    outputs.add(new FluidStack(entry.getKey(), (int) amount));
                    value -= amount;
                }
            }
            mOutputFluids = outputs.toArray(new FluidStack[0]);
            oreOutputs.clear();
            lEUt = -(30L) * (didOCs == 0 ? didOres
                : (calculateMaximumParallel(30) * (long) Math.pow(electrode.OCPowerFactor, didOCs)));
            didOres = -1;
            // calculate additional overclocks if we didn't use them all in the startup phase
            OverclockCalculator calculator = new OverclockCalculator();
            calculator.setMaxTierSkips(0);
            calculator.setEUt(getAverageInputVoltage());
            calculator.setAmperage(getMaxInputAmps());
            calculator.setRecipeEUt(-lEUt);
            calculator.setDuration(mMaxProgresstime);
            calculator.setDurationDecreasePerOC(electrode.OCSpeedFactor);
            calculator.setEUtIncreasePerOC(electrode.OCPowerFactor);
            calculator.setMaxOverclocks((int) GTUtility.log4(getAverageInputVoltage() / 32) - didOCs);
            calculator.setDurationModifier(1d / electrode.speedModifier);
            calculator.calculate();
            mMaxProgresstime = calculator.getDuration();
            lEUt = -calculator.getConsumption();
        }
        super.runMachine(aBaseMetaTileEntity, aTick);
        if (mMaxProgresstime > 0 && phase == ArcFurnacePhase.Processing && (aTick % 20 == 0 || mProgresstime == 0)) {
            sendSound(LOOP_ARC_SOUND_INDEX);
        }
        if (mMaxProgresstime <= 0 && phase != ArcFurnacePhase.Standby) {
            startShutDown();
        }
        if (electrodeHatch != null && electrodeHatch.hasJustBeenUpdated()) {
            electrodeChanged();
        }
    }

    @Override
    protected void outputAfterRecipe() {
        super.outputAfterRecipe();
        if (phase == ArcFurnacePhase.ArcIgnition) {
            phase = ArcFurnacePhase.Processing;
        } else if (phase == ArcFurnacePhase.ArcShutdown) {
            phase = ArcFurnacePhase.Standby;
        } else if (phase == ArcFurnacePhase.Processing) {
            applySpecialEffect(new ArcFurnaceProcessingEvent.EventRunCompleted(this));
            if (electrode != null) {
                ItemStack electrodeStack = electrodeHatch.getStackInSlot(0);
                if (electrodeStack != null) {
                    boolean shouldRemove = ARC_FURNACE_ELECTRODE.damageElectrode(electrodeStack, durabilityCostThisRun);
                    durabilityCostThisRun = 1;
                    if (shouldRemove) {
                        startShutDown();
                        electrodeHatch.setInventorySlotContents(0, null);
                        electrodeChanged();
                    } else {
                        electrodeHatch.markDirty();
                        electrodeDamagePercentage = (double) ARC_FURNACE_ELECTRODE.usedDurability(electrodeStack)
                            / (double) electrode.durability;
                        updateDetectorHatches(
                            ARC_FURNACE_ELECTRODE.remainingDurability(electrodeHatch.getStackInSlot(0)));
                    }
                }
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        super.stopMachine(reason);
        phase = ArcFurnacePhase.Standby;
        resetElectrodeEffectState();
    }

    @Override
    protected @NotNull CheckRecipeResult postCheckRecipe(@NotNull CheckRecipeResult result,
        @NotNull ProcessingLogic processingLogic) {
        result = super.postCheckRecipe(result, processingLogic);
        if (!result.wasSuccessful() && phase == ArcFurnacePhase.ArcIgnition) {
            phase = ArcFurnacePhase.Standby;
        }
        return result;
    }

    private GTRecipe fakeRecipe() {
        if (fakeRecipeCache == null) {
            fakeRecipeCache = new GTRecipe(false, null, null, null, null, null, null, null, null, null, 1, 1, 0);
            fakeRecipeCache.mCanBeBuffered = false;
        }
        return fakeRecipeCache;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                Stream<GTRecipe> str = super.findRecipeMatches(map).limit(1);
                if (mode == ArcFurnaceMode.Normal) return str;
                GTRecipe found = str.findAny()
                    .orElse(null);
                if (found == null) return Stream.of();
                if (mode == ArcFurnaceMode.Blast) {
                    GTRecipe copy = found.copy()
                        .setEUt((int) Math.min(found.mEUt * BLAST_MODE_POWER_MULTIPLIER, Integer.MAX_VALUE));
                    copy.mCanBeBuffered = false;
                    return Stream.of(copy);
                }
                // ores
                for (ItemStack inputItem : inputItems) {
                    ItemStack smeltedOutput = GTModHandler.getSmeltingOutput(inputItem, false, null);
                    if (smeltedOutput != null) {
                        ItemData data = GTOreDictUnificator.getItemData(inputItem);
                        if (data != null && data.mPrefix != null
                            && data.mPrefix.getMaterialPostfix()
                                .equals(" Ore")) {
                            GTRecipe fake = fakeRecipe();
                            fake.mEUt = 16;
                            fake.mDuration = ORE_MODE_STARTUP_TICKS;
                            didOres = 0;
                            didOCs = 0;
                            oreOutputs.clear();
                            return Stream.of(fake);
                        }
                    }
                }
                return Stream.of();
            }

            @Override
            protected @NotNull CheckRecipeResult applyRecipe(@NotNull GTRecipe recipe, @NotNull ParallelHelper helper,
                @NotNull OverclockCalculator calculator, @NotNull CheckRecipeResult result) {
                CheckRecipeResult ret = super.applyRecipe(recipe, helper, calculator, result);
                if (result.wasSuccessful() && phase == ArcFurnacePhase.Processing) {
                    double batchedRecipes = (double) helper.getCurrentParallel()
                        / (double) calculator.getCurrentParallel();
                    if (batchedRecipes > 1d) {
                        batchedRecipes -= 1;
                        int bint = (int) batchedRecipes;
                        batchedRecipes -= bint;
                        if (batchedRecipes > 0d && getRandomNumber(100) < (batchedRecipes * 100d)) {
                            bint += 1;
                        }
                        durabilityCostThisRun += bint;
                    }
                    if (electrodeDamagePercentage > ARC_BREAK_DAMAGE_THRESHOLD
                        && getRandomNumber(100) < ARC_BREAK_CHANCE_PERCENT) {
                        this.overwriteOutputItems();
                        this.overwriteOutputFluids();
                    }
                }
                ArcFurnaceProcessingEvent.EventPostRecipeCheck afterRecipe = new ArcFurnaceProcessingEvent.EventPostRecipeCheck(
                    MTEIndustrialArcFurnace.this,
                    this,
                    recipe,
                    helper,
                    calculator,
                    ret,
                    phase == ArcFurnacePhase.Processing,
                    this.calculatedEut);
                applySpecialEffect(afterRecipe);
                this.calculatedEut = afterRecipe.eut;
                if (phase == ArcFurnacePhase.ArcIgnition) {
                    this.calculatedEut = (long) (getAverageInputVoltage() * 30d
                        / 32d
                        * this.maxParallel
                        * (electrode.startupSurge + 1d)
                        * electrode.amperagePerParallel);
                    sendSound(START_ARC_SOUND_INDEX);
                    return SimpleCheckRecipeResult.ofSuccess("arc_ignition");
                }
                return ret;
            }

            @Override
            protected @NotNull CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                return super.onRecipeStart(recipe);
            }

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (electrode == null) return SimpleCheckRecipeResult.ofFailure("no_electrode");
                CheckRecipeResult result = super.validateRecipe(recipe);
                if (!result.wasSuccessful()) return result;
                // check if we can even process anything
                if (this.availableVoltage < recipe.mEUt)
                    return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt);
                return result;
            }

            @Override
            protected @NotNull OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                if (phase == ArcFurnacePhase.ArcIgnition || (mode == ArcFurnaceMode.Ore && recipe == fakeRecipe())) {
                    return super.createOverclockCalculator(fakeRecipe()).setNoOverclock(true)
                        .setAmperage(1)
                        .setEUt(this.availableVoltage * this.availableAmperage)
                        .setDurationModifier(1);
                }
                OverclockCalculator calculator = super.createOverclockCalculator(recipe)
                    .setMaxOverclocks((int) GTUtility.log4(this.availableVoltage / Math.max((long) recipe.mEUt, 32)));
                ArcFurnaceProcessingEvent.EventConfigureOverclock event = new ArcFurnaceProcessingEvent.EventConfigureOverclock(
                    MTEIndustrialArcFurnace.this,
                    calculator);
                applySpecialEffect(event);
                return calculator;
            }

            @Override
            protected @NotNull ParallelHelper createParallelHelper(@NotNull GTRecipe recipe) {
                if (phase == ArcFurnacePhase.Standby
                    || (phase == ArcFurnacePhase.Processing && electrodeDamagePercentage > ARC_SURGE_DAMAGE_THRESHOLD
                        && getRandomNumber(100) < ARC_SURGE_CHANCE_PERCENT)) {
                    phase = ArcFurnacePhase.ArcIgnition;
                    this.setBatchSize(1);
                    GTRecipe ignitionRecipe = fakeRecipe();
                    final long use = (long) (getAverageInputVoltage() * 30d
                        / 32d
                        * this.maxParallel
                        * (electrode.startupSurge + 1d));
                    // we set here to generate insufficient power error
                    ignitionRecipe.mEUt = (int) Math.min(use, Integer.MAX_VALUE);
                    ignitionRecipe.mDuration = STARTUP_DURATION_TICKS;
                    ArcFurnaceProcessingEvent.EventStartIgnition event = new ArcFurnaceProcessingEvent.EventStartIgnition(
                        MTEIndustrialArcFurnace.this,
                        ignitionRecipe.mDuration,
                        ignitionRecipe.mEUt);
                    applySpecialEffect(event);
                    ignitionRecipe.mEUt = event.eut;
                    ignitionRecipe.mDuration = event.duration;
                    return super.createParallelHelper(ignitionRecipe).setConsumption(false)
                        .setMaxParallel(1);
                }
                if (phase == ArcFurnacePhase.Processing && mode == ArcFurnaceMode.Ore && recipe == fakeRecipe()) {
                    this.setBatchSize(1);
                    return super.createParallelHelper(recipe).setConsumption(false)
                        .setMaxParallel(1);
                }

                ParallelHelper helper = super.createParallelHelper(recipe);
                applySpecialEffect(
                    new ArcFurnaceProcessingEvent.EventCreateParallelHelper(MTEIndustrialArcFurnace.this, helper));
                return helper;
            }

        };
    }

    enum ArcFurnaceHatches implements IHatchElement<MTEIndustrialArcFurnace> {

        ElectrodeHatch(MTEIndustrialArcFurnace::addElectrodeHatchToMachineList, MTEElectrodeHatch.class) {

            @Override
            public long count(MTEIndustrialArcFurnace t) {
                if (t.electrodeHatch == null) return 0;
                return 1;
            }
        },
        ElectrodeDetectorHatch(MTEIndustrialArcFurnace::addElectrodeDetectorHatchToMachineList,
            MTEElectrodeDetectorHatch.class) {

            @Override
            public long count(MTEIndustrialArcFurnace t) {
                return t.electrodeDetectorHatch.size();
            }
        },;

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEIndustrialArcFurnace> adder;

        @SafeVarargs
        ArcFurnaceHatches(IGTHatchAdder<MTEIndustrialArcFurnace> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTEIndustrialArcFurnace> adder() {
            return adder;
        }
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull String[] getCreditsText() {
        return new String[] {
            translateToLocalFormatted("kubatech.gui.tooltip.contributors.added", GTAuthors.AuthorKuba),
            translateToLocalFormatted("kubatech.gui.tooltip.contributors.design", GTAuthors.AuthorPxx500),
            translateToLocalFormatted(
                "kubatech.gui.tooltip.contributors.structure",
                EnumChatFormatting.LIGHT_PURPLE + "Sol_IX") };
    }

}
