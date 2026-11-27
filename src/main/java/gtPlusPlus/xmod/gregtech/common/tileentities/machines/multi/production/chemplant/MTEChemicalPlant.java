package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainFluidPipeCasings;
import static gregtech.api.util.GTStructureUtility.filterByMTETier;
import static gregtech.api.util.GTStructureUtility.ofCoil;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchCatalysts;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@IMetaTileEntity.SkipGenerateDescription
public class MTEChemicalPlant extends MTEExtendedPowerMultiBlockBase<MTEChemicalPlant>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final int PARALLELS_PER_PIPE_TIER = 2;
    private static final double CATALYST_SAVE_PER_PIPE_TIER = 0.15D;
    private static final float SPEED_BONUS_PER_COIL_TIER = 0.5f;
    private static final int UNBREAKABLE_COIL_TIER = 10;
    private static final int UNBREAKABLE_PIPE_TIER = 7;
    private static final int MAX_CATALYST_DURABILITY = 50;

    private int solidCasingTier = 0;
    private int machineCasingTier = 0;
    private int pipeCasingTier = 0;
    private int coilTier = 0;
    private HeatingCoilLevel checkCoil;
    private final int[] checkCasing = new int[8];
    private int checkMachine;
    private int checkPipe;
    private int maxTierOfHatch;
    private int casingAmount;
    // spotless:off
    private static final String[][] structure = {
        { "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "CCCCCCC" },
        { "C     C", " MMMMM ", " MHHHM ", " MHHHM ", " MHHHM ", " MMMMM ", "C     C" },
        { "C     C", "       ", "  PPP  ", "  PPP  ", "  PPP  ", "       ", "C     C" },
        { "C     C", "       ", "  HHH  ", "  HHH  ", "  HHH  ", "       ", "C     C" },
        { "C     C", "       ", "  PPP  ", "  PPP  ", "  PPP  ", "       ", "C     C" },
        { "C     C", " MMMMM ", " MHHHM ", " MHHHM ", " MHHHM ", " MMMMM ", "C     C" },
        { "CCC~CCC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CMMMMMC", "CCCCCCC" }, };
    // spotless:on
    private static IStructureDefinition<MTEChemicalPlant> STRUCTURE_DEFINITION = null;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public static final Set<GTUtility.ItemId> CHEMPLANT_CATALYSTS = new HashSet<>();

    private final ArrayList<MTEHatchCatalysts> catalystBuses = new ArrayList<>();

    private static final HashMap<Integer, Triple<Block, Integer, Integer>> tieredBlockRegistry = new HashMap<>();

    public MTEChemicalPlant(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEChemicalPlant(final String aName) {
        super(aName);
    }

    @Nullable
    private static Integer getMachineCasingTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings1) return null;
        if (metaID < 0 || metaID > 9) return null;
        return metaID;
    }

    public static void registerMachineCasingForTier(int tier, Block block, int meta, int casingTextureID) {
        Triple<Block, Integer, Integer> aCasingData = Triple.of(block, meta, casingTextureID);
        if (tieredBlockRegistry.containsKey(tier)) {
            throw new IllegalStateException(
                "Tried to register a Machine casing for tier " + tier
                    + " to the Chemical Plant, however this tier already contains one.");
        }
        tieredBlockRegistry.put(tier, aCasingData);
        GTStructureChannels.METAL_MACHINE_CASING.registerAsIndicator(new ItemStack(block, 1, meta), tier + 1);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEChemicalPlant(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.chemical_plant"))
            .addMarkdown(
                new ResourceLocation("gregtech", "chemical-plant"),
                ImmutableMap.<String, Object>builder()
                    .put("parallels", PARALLELS_PER_PIPE_TIER)
                    .put("speed", Math.round(SPEED_BONUS_PER_COIL_TIER * 100))
                    .put("catalyst_save", Math.round(CATALYST_SAVE_PER_PIPE_TIER * 100))
                    .put("max_casing", GTValues.TIER_COLORS[9] + GTValues.VN[9])
                    .build())
            .beginStructureBlock(7, 7, 7, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_bottom_center"))
            .addCasing("70-91", StatCollector.translateToLocal("GT5U.MBTT.Tiers.MetalMachineCasing"), true)
            .addCasing("57", StatCollector.translateToLocal("GT5U.MBTT.Tiers.MachineCasing"), true)
            .addCasing("27", StatCollector.translateToLocal("GT5U.MBTT.Tiers.Coil"), true)
            .addCasing("18", StatCollector.translateToLocal("GT5U.MBTT.Tiers.FluidPipe"), true)
            .addStructureHint("item.GTPP.catalyst_housing.name", 1)
            .addMiscHatch("0-1", StatCollector.translateToLocal("item.GTPP.catalyst_housing.name"), StatCollector.translateToLocal("gt.mbtt.structure.any_metal_machine_casing"), 1)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_metal_machine_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_metal_machine_casing"), 1)
            .addInputAny("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_metal_machine_casing"), 1)
            .addOutputAny("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_metal_machine_casing"), 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.METAL_MACHINE_CASING)
            .addSubChannel(GTStructureChannels.TIER_MACHINE_CASING)
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .addSubChannel(GTStructureChannels.PIPE_CASING)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    public void setMachineMeta(int meta) {
        checkMachine = meta;
    }

    public int getMachineMeta() {
        return checkMachine;
    }

    public void setPipeMeta(int meta) {
        checkPipe = meta;
    }

    public int getPipeMeta() {
        return checkPipe;
    }

    public void setCoilMeta(HeatingCoilLevel meta) {
        checkCoil = meta;
    }

    public HeatingCoilLevel getCoilMeta() {
        return checkCoil;
    }

    @Override
    public IStructureDefinition<MTEChemicalPlant> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            IStructureElement<MTEChemicalPlant> allCasingsElement = GTStructureChannels.METAL_MACHINE_CASING.use(
                ofChain(
                    IntStream.range(0, 8)
                        .mapToObj(MTEChemicalPlant::ofSolidCasing)
                        .collect(Collectors.toList())));
            STRUCTURE_DEFINITION = StructureDefinition.<MTEChemicalPlant>builder()
                .addShape(STRUCTURE_PIECE_MAIN, transpose(structure))
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEChemicalPlant.class).atLeast(Maintenance)
                            .casingIndex(getCasingTextureID())
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEChemicalPlant.class)
                            .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy)
                            .adder(MTEChemicalPlant::addChemicalPlantList)
                            .hatchItemFilterAnd(
                                (t, s) -> filterByMTETier(
                                    Integer.MIN_VALUE,
                                    s.stackSize >= 10 ? Integer.MAX_VALUE : s.stackSize))
                            .casingIndex(getCasingTextureID())
                            .hint(1)
                            .build(),
                        buildHatchAdder(MTEChemicalPlant.class).atLeast(CatalystHatchElement.CATALYST_BUS)
                            .shouldReject(t -> !t.catalystBuses.isEmpty())
                            .casingIndex(getCasingTextureID())
                            .hint(1)
                            .build(),
                        allCasingsElement))
                .addElement(
                    'M',
                    GTStructureChannels.TIER_MACHINE_CASING.use(
                        ofBlocksTiered(
                            MTEChemicalPlant::getMachineCasingTierFromMeta,
                            ImmutableList.of(
                                Pair.of(GregTechAPI.sBlockCasings1, 0),
                                Pair.of(GregTechAPI.sBlockCasings1, 1),
                                Pair.of(GregTechAPI.sBlockCasings1, 2),
                                Pair.of(GregTechAPI.sBlockCasings1, 3),
                                Pair.of(GregTechAPI.sBlockCasings1, 4),
                                Pair.of(GregTechAPI.sBlockCasings1, 5),
                                Pair.of(GregTechAPI.sBlockCasings1, 6),
                                Pair.of(GregTechAPI.sBlockCasings1, 7),
                                Pair.of(GregTechAPI.sBlockCasings1, 8),
                                Pair.of(GregTechAPI.sBlockCasings1, 9)),
                            -1,
                            MTEChemicalPlant::setMachineMeta,
                            MTEChemicalPlant::getMachineMeta)))
                .addElement(
                    'H',
                    GTStructureChannels.HEATING_COIL
                        .use(activeCoils(ofCoil(MTEChemicalPlant::setCoilMeta, MTEChemicalPlant::getCoilMeta))))
                .addElement(
                    'P',
                    chainFluidPipeCasings(-1, MTEChemicalPlant::setPipeMeta, MTEChemicalPlant::getPipeMeta))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private static IStructureElement<MTEChemicalPlant> ofSolidCasing(int aIndex) {
        return new IStructureElement<MTEChemicalPlant>() {

            @Override
            public boolean check(MTEChemicalPlant t, World world, int x, int y, int z) {
                if (check(aIndex, world, x, y, z)) {
                    t.checkCasing[aIndex]++;
                    t.casingAmount++;
                    return true;
                } else return false;
            }

            @Override
            public boolean couldBeValid(MTEChemicalPlant mteChemicalPlant, World world, int x, int y, int z,
                ItemStack trigger) {
                return check(aIndex, world, x, y, z);
            }

            private boolean check(int index, World world, int x, int y, int z) {
                Block block = world.getBlock(x, y, z);
                int meta = world.getBlockMetadata(x, y, z);
                Block target = tieredBlockRegistry.get(index)
                    .getLeft();
                int targetMeta = tieredBlockRegistry.get(index)
                    .getMiddle();
                return target.equals(block) && meta == targetMeta;
            }

            int getIndex(int size) {
                if (size > 8) size = 8;
                return size - 1;
            }

            @Override
            public boolean spawnHint(MTEChemicalPlant t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(
                    world,
                    x,
                    y,
                    z,
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle());
                return true;
            }

            @Override
            public boolean placeBlock(MTEChemicalPlant t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(
                    x,
                    y,
                    z,
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle(),
                    3);
            }

            @Nullable
            @Override
            public BlocksToPlace getBlocksToPlace(MTEChemicalPlant t, World world, int x, int y,
                int z, ItemStack trigger, AutoPlaceEnvironment env) {
                return BlocksToPlace.create(
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle());
            }

            @Override
            public PlaceResult survivalPlaceBlock(MTEChemicalPlant t, World world, int x, int y, int z,
                ItemStack trigger, AutoPlaceEnvironment env) {
                if (check(getIndex(trigger.stackSize), world, x, y, z)) return PlaceResult.SKIP;
                return StructureUtility.survivalPlaceBlock(
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getLeft(),
                    tieredBlockRegistry.get(getIndex(trigger.stackSize))
                        .getMiddle(),
                    world,
                    x,
                    y,
                    z,
                    env.getSource(),
                    env.getActor(),
                    env.getChatter());
            }
        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 6, 0, elementBudget, env, false, true);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        clearCraftingIcons(catalystBuses);
        catalystBuses.clear();
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        for (int i = 0; i < 8; i++) {
            checkCasing[i] = 0;
        }
        checkPipe = -1;
        checkMachine = -1;
        solidCasingTier = 0;
        machineCasingTier = 0;
        pipeCasingTier = 0;
        coilTier = 0;
        maxTierOfHatch = 0;
        catalystBuses.clear();
        setCoilMeta(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 0, errors)) return;
        for (int i = 0; i < 8; i++) {
            if (checkCasing[i] == casingAmount) {
                solidCasingTier = i;
            } else if (checkCasing[i] > 0) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.chemplant_casing_problem"));
                return;
            }
        }
        checkCasingMin(errors, casingAmount, 70);
        machineCasingTier = checkMachine;
        pipeCasingTier = checkPipe;
        coilTier = checkCoil.getTier();
        if (machineCasingTier < 9 && machineCasingTier < maxTierOfHatch) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.chemplant_hatch_problem"));
        }
        if (catalystBuses.size() > 1) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.chemplant_too_many_catalyst_hatch"));
        }
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        updateHatchTexture();
    }

    public void updateHatchTexture() {
        for (MTEHatch h : catalystBuses) h.updateTexture(getCasingTextureID());
        for (IDualInputHatch h : mDualInputHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mInputBusses) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mMaintenanceHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mEnergyHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mOutputBusses) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mInputHatches) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mOutputHatches) h.updateTexture(getCasingTextureID());
    }

    public final boolean addChemicalPlantList(IGregTechTileEntity igte, int baseCasingIndex) {
        if (igte == null) return false;
        IMetaTileEntity imte = igte.getMetaTileEntity();
        if (imte instanceof MTEHatchMaintenance) {
            return addToMachineList(igte, baseCasingIndex);
        }
        switch (imte) {
            case MTEHatchInputBus bus -> maxTierOfHatch = Math.max(maxTierOfHatch, bus.mTier);
            case MTEHatchEnergy hatch -> maxTierOfHatch = Math.max(maxTierOfHatch, hatch.mTier);
            case MTEHatchOutputBus bus -> maxTierOfHatch = Math.max(maxTierOfHatch, bus.mTier);
            case MTEHatchInput hatch -> maxTierOfHatch = Math.max(maxTierOfHatch, hatch.mTier);
            case MTEHatchOutput hatch -> maxTierOfHatch = Math.max(maxTierOfHatch, hatch.mTier);
            case null, default -> {
                return false;
            }
        }
        return addToMachineList(igte, baseCasingIndex);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_CHEMICAL;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            facing,
            active,
            TexturesGtBlock.oMCAChemicalPlant,
            TexturesGtBlock.oMCAChemicalPlantGlow,
            TexturesGtBlock.oMCAChemicalPlantActive,
            TexturesGtBlock.oMCAChemicalPlantActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        Triple<Block, Integer, Integer> casing = tieredBlockRegistry.get(solidCasingTier);
        if (casing == null) return null;
        return Textures.BlockIcons.getCasingTextureForId(casing.getRight());
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.chemicalPlantRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        return PARALLELS_PER_PIPE_TIER * pipeCasingTier;
    }

    private int getCasingTextureID() {
        return tieredBlockRegistry.getOrDefault(solidCasingTier, tieredBlockRegistry.get(0))
            .getRight();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSolidCasingTier", this.solidCasingTier);
        aNBT.setInteger("mMachineCasingTier", this.machineCasingTier);
        aNBT.setInteger("mPipeCasingTier", this.pipeCasingTier);
        aNBT.setInteger("mCoilTier", this.coilTier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        solidCasingTier = aNBT.getInteger("mSolidCasingTier");
        machineCasingTier = aNBT.getInteger("mMachineCasingTier");
        pipeCasingTier = aNBT.getInteger("mPipeCasingTier");
        coilTier = aNBT.getInteger("mCoilTier");
    }

    public int getMaxCatalystDurability() {
        return MAX_CATALYST_DURABILITY;
    }

    @Override
    public byte getUpdateData() {
        return (byte) solidCasingTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA && (aValue & 0x80) == 0) {
            // received an update data from above method
            // if no &0x80 clause it might catch the noop texture page event
            solidCasingTier = aValue;
        }
    }

    /**
     * @return if the catalyst item is fully destroyed as a result of the damage applied.
     */
    private boolean damageCatalyst(@Nonnull ItemStack stack, int minParallel) {
        // Awakened Draconium Coils with Neutronium Pipe Casings (or above) no longer consume catalysts.
        if (!isCatalystDamageable()) return false;
        for (int i = 0; i < minParallel; i++) {
            if (MathUtils.randFloat(0, 10000000) / 10000000f
                < (1.2f - (CATALYST_SAVE_PER_PIPE_TIER * this.pipeCasingTier))) {
                int damage = getDamage(stack) + 1;
                if (damage >= getMaxCatalystDurability()) {
                    addOutputPartial(GregtechItemList.EmptyCatalystCarrier.get(1));
                    stack.stackSize -= 1;
                    return stack.stackSize == 0;
                } else {
                    setDamage(stack, damage);
                }
            }
        }
        return false;
    }

    private boolean isCatalystDamageable() {
        return this.coilTier < UNBREAKABLE_COIL_TIER || this.pipeCasingTier < UNBREAKABLE_PIPE_TIER;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            ItemStack catalyst;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > solidCasingTier) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue + 1);
                }
                // checks if it has a catalyst
                ItemStack catalystInRecipe = null;
                for (ItemStack item : recipe.mInputs) {
                    if (MTEChemicalPlant.isCatalyst(item)) {
                        catalystInRecipe = item;
                        break;
                    }
                }

                if (catalystInRecipe != null) {
                    catalyst = findCatalyst(getCatalystInputs().toArray(new ItemStack[0]), catalystInRecipe);
                    if (catalyst == null) {
                        return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                    }
                } else {
                    // remove reference to the catalyst if it is invalid, or if the damage destroys it
                    catalyst = null;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            public ItemStack[] prepareCatalyst(ItemStack[] inputItems) {
                ArrayList<ItemStack> inputItemsList = new ArrayList<>(Arrays.asList(inputItems));
                inputItemsList.addAll(getCatalystInputs());
                inputItems = inputItemsList.toArray(new ItemStack[0]);
                return inputItems;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                if (!GTUtility.isStackValid(catalyst) || damageCatalyst(catalyst, getCurrentParallels())) {
                    // remove reference to the catalyst if it is invalid, or if the damage destroys it
                    catalyst = null;
                }
                return super.onRecipeStart(recipe);
            }

        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        super.setupProcessingLogic(logic);
        // Same speed bonus as pyro oven
        logic.setSpeedBonus(2.0D / (1.0D + (double) this.coilTier));
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        for (MTEHatchCatalysts h : catalystBuses) {
            h.tryFillUsageSlots();
            h.updateSlots();
        }
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    private enum CatalystHatchElement implements IHatchElement<MTEChemicalPlant> {

        CATALYST_BUS("gt.blockmachines.hatch.catalysts.name");

        private final String name;

        CatalystHatchElement(String name) {
            this.name = name;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchCatalysts.class);
        }

        @Override
        public IGTHatchAdder<? super MTEChemicalPlant> adder() {
            return (self, igte, texture) -> {
                if (igte.getMetaTileEntity() instanceof MTEHatchCatalysts imte) {
                    imte.updateTexture(texture);
                    imte.updateCraftingIcon(self.getMachineCraftingIcon(), self.getBaseMetaTileEntity());
                    self.addIfSmartInput(imte);
                    self.catalystBuses.add(imte);
                    return true;
                }
                return false;
            };
        }

        @Override
        public long count(MTEChemicalPlant mte) {
            return mte.catalystBuses.size();
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

    private ItemStack findCatalyst(ItemStack[] itemInputs, ItemStack catalyst) {
        if (itemInputs != null) {
            for (ItemStack item : itemInputs) {
                if (GTUtility.areStacksEqual(item, catalyst, true)) {
                    return item;
                }
            }
        }
        return null;
    }

    private int getDamage(@Nonnull ItemStack stack) {
        return ItemGenericChemBase.getCatalystDamage(stack);
    }

    private void setDamage(@Nonnull ItemStack stack, int amount) {
        ItemGenericChemBase.setCatalystDamage(stack, amount);
    }

    public ArrayList<ItemStack> getCatalystInputs() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (MTEHatchCatalysts hatch : validMTEList(catalystBuses)) {
            ArrayList<ItemStack> aHatchContent = hatch.getContentUsageSlots();
            if (!aHatchContent.isEmpty()) {
                items.addAll(aHatchContent);
            }
        }
        return items;
    }

    public static void registerChemplantCatalyst(ItemStack stack) {
        if (stack == null) return;
        CHEMPLANT_CATALYSTS.add(GTUtility.ItemId.createWithoutNBT(stack));
    }

    public static boolean isCatalyst(ItemStack stack) {
        return stack != null && CHEMPLANT_CATALYSTS.contains(GTUtility.ItemId.createWithoutNBT(stack));
    }

    static {
        registerChemplantCatalyst(GregtechItemList.BlueMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.BrownMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.OrangeMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PurpleMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PinkMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.FormaldehydeCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.SolidAcidCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.InfiniteMutationCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.GreenMetalCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PlatinumGroupCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.PlasticPolymerCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RubberPolymerCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.AdhesionPromoterCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.TitaTungstenIndiumCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RadioactivityCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RareEarthGroupCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.SimpleNaquadahCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.HellishForceCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.CrystalColorizationCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.AdvancedNaquadahCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.RawIntelligenceCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.UltimatePlasticCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.BiologicalIntelligenceCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.TemporalHarmonyCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(1));
        registerChemplantCatalyst(GregtechItemList.ChlorinationCatalyst.get(1));
    }
}
