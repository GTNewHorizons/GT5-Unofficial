package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.MTESolarHeater;

public class MTESolarTower extends MTEExtendedPowerMultiBlockBase<MTESolarTower>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_TOP = "top";
    private static final String STRUCTURE_PIECE_TOWER = "tower";
    private static final String STRUCTURE_PIECE_BASE = "base";
    private static final String STRUCTURE_PIECE_HEATER_RING_1 = "ring1";
    private static final String STRUCTURE_PIECE_HEATER_RING_2 = "ring2";
    private static final String STRUCTURE_PIECE_HEATER_RING_3 = "ring3";
    private static final String STRUCTURE_PIECE_HEATER_RING_4 = "ring4";
    private static final String STRUCTURE_PIECE_HEATER_RING_5 = "ring5";

    // spotless:off
    private static final String[][] STRUCTURE_STRING_TOP = {
        { "     ", "     ", "  ~  ", "     ", "     " },
        { "     ", "  s  ", " sss ", "  s  ", "     " },
        { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
        { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
        { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
        { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
        { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " }, };
    private static final String[][] STRUCTURE_STRING_TOWER = {
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " },
        { " i ", "isi", " i " }, };
    private static final String[][] STRUCTURE_STRING_BASE = {
        { "           ", "           ", "     t     ", "    ttt    ", "   ttstt   ", "  ttssstt  ", "   ttstt   ", "    ttt    ", "     t     ", "           ", "           " },
        { "           ", "           ", "     t     ", "    ttt    ", "   tssst   ", "  ttssstt  ", "   tssst   ", "    ttt    ", "     t     ", "           ", "           " },
        { "           ", "     t     ", "    ttt    ", "   ttttt   ", "  ttssstt  ", " tttsssttt ", "  ttssstt  ", "   ttttt   ", "    ttt    ", "     t     ", "           " },
        { "           ", "     t     ", "    ttt    ", "   ttttt   ", "  ttssstt  ", " tttsssttt ", "  ttssstt  ", "   ttttt   ", "    ttt    ", "     t     ", "           " },
        { "    hhh    ", "   ttttt   ", "  ttttttt  ", " ttttttttt ", "htttsssttth", "htttsssttth", "htttsssttth", " ttttttttt ", "  ttttttt  ", "   ttttt   ", "    hhh    " },
        { "    hhh    ", "   ttttt   ", "  ttttttt  ", " ttttttttt ", "httttttttth", "httttttttth", "httttttttth", " ttttttttt ", "  ttttttt  ", "   ttttt   ", "    hhh    " }, };
    private static final String[][] STRUCTURE_STRING_HEATER_RING_1 = {
        {   "     ggggg     ",
            "    g     g    ",
            "   g       g   ",
            "  g         g  ",
            " g           g ",
            "g             g",
            "g             g",
            "g             g",
            "g             g",
            "g             g",
            " g           g ",
            "  g         g  ",
            "   g       g   ",
            "    g     g    ",
            "     ggggg     ", }};
    private static final String[][] STRUCTURE_STRING_HEATER_RING_2 = {
        {   "     ggggggggg     ",
            "    g         g    ",
            "   g           g   ",
            "  g             g  ",
            " g               g ",
            "g                 g",
            "g                 g",
            "g                 g",
            "g                 g",
            "g                 g",
            "g                 g",
            "g                 g",
            "g                 g",
            "g                 g",
            " g               g ",
            "  g             g  ",
            "   g           g   ",
            "    g         g    ",
            "     ggggggggg     ", }};
    private static final String[][] STRUCTURE_STRING_HEATER_RING_3 = {
        {   "     ggggggggggggg     ",
            "    g             g    ",
            "   g               g   ",
            "  g                 g  ",
            " g                   g ",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            "g                     g",
            " g                   g ",
            "  g                 g  ",
            "   g               g   ",
            "    g             g    ",
            "     ggggggggggggg     ", }};
    private static final String[][] STRUCTURE_STRING_HEATER_RING_4 = {
        {   "     ggggggggggggggggg     ",
            "    g                 g    ",
            "   g                   g   ",
            "  g                     g  ",
            " g                       g ",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            "g                         g",
            " g                       g ",
            "  g                     g  ",
            "   g                   g   ",
            "    g                 g    ",
            "     ggggggggggggggggg     ", }};
    private static final String[][] STRUCTURE_STRING_HEATER_RING_5 = {
        {   "     ggggggggggggggggggggg     ",
            "    g                     g    ",
            "   g                       g   ",
            "  g                         g  ",
            " g                           g ",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            "g                             g",
            " g                           g ",
            "  g                         g  ",
            "   g                       g   ",
            "    g                     g    ",
            "     ggggggggggggggggggggg     ", }};
    // spotless:on

    private int heatLevel = 0;
    private int casingAmount;
    private Fluid mColdSalt = null;
    private Fluid mHotSalt = null;

    public ArrayList<MTESolarHeater> solarHeaters = new ArrayList<>();

    public MTESolarTower(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESolarTower(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTESolarTower(this.mName);
    }

    @Override
    protected final MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Solar Tower")
            .addInfo("Contributing Green Energy towards the future")
            .addInfo("Surround with rings of Solar Reflectors")
            .addInfo("The Reflectors increase the internal heat value of the Tower (see below for formula)")
            .addInfo("Each Reflector ring increases tier, the first ring is required for the Tower to work")
            .addInfo("Input: " + MaterialMisc.SOLAR_SALT_COLD.getDefaultLocalName())
            .addInfo("Output: " + MaterialMisc.SOLAR_SALT_HOT.getDefaultLocalName())
            .addInfo("Every cycle (10 seconds), heat increases and all the Cold Solar Salt is heated")
            .addInfo("Converting Cold to Hot Solar Salt reduces heat, equal to the amount converted")
            .addInfo("This conversion only happens if heat >= 30000 and controller efficiency = 100%")
            .addInfo("If there's more Cold Salt than heat, all the heat is used up and returns to 0")
            .addInfo("The heat increase is most efficient at exactly half of maximum heat")
            .addInfo("Minimum efficiency at 0 or 100000 heat, maximum efficiency at 50000")
            .addInfo("Heat Efficiency formula: ( 7000 - [|currentHeat - 50000| ^ 0.8]) / 7000")
            .addInfo("Heat gain per cycle: numberHeaters * heatEfficiency * (10 + bonus)")
            .addInfo("Bonus: 1 ring  = +1, 2 rings = +2, 3 rings = +4, 4 rings = +8, 5 rings = +16")
            .addInfo("Total number of reflectors based on how many rings are built:")
            .addInfo("1 ring = 36, 2 rings = 88, 3 rings = 156, 4 rings = 240, 5 rings = 340")
            .beginVariableStructureBlock(15, 31, 28, 28, 15, 31, false)
            .addController("Top center, 28th layer")
            .addCasing("36/88/156/240/340", "Solar Reflector", false)
            .addCasing("229-250", "Structural Solar Casing", false)
            .addCasing("66", "Salt Containment Casing", false)
            .addCasing("60", "Thermal Containment Casing", false)
            .addCasing("60", "Thermally Insulated Casing", false)
            .addMaintenanceHatch("1", "Any bottom side casing", 2)
            .addInputHatch("1+", "Any bottom side casing", 2)
            .addOutputHatch("1+", "Any bottom side casing", 2)
            .addStructureInfo("")
            .addMasterChannel(StatCollector.translateToLocal("channels.gregtech.master.rings"))
            .toolTipFinisher();
        return tt;
    }

    private static final ClassValue<IStructureDefinition<MTESolarTower>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTESolarTower> computeValue(Class<?> type) {
            return StructureDefinition.<MTESolarTower>builder()

                // s = salt
                // c = thermal containment
                // i = thermal insulated
                // t = solar structural
                // h = hatch
                // g = solar heater

                .addShape(STRUCTURE_PIECE_TOP, STRUCTURE_STRING_TOP)
                .addShape(STRUCTURE_PIECE_TOWER, STRUCTURE_STRING_TOWER)
                .addShape(STRUCTURE_PIECE_BASE, STRUCTURE_STRING_BASE)
                .addShape(STRUCTURE_PIECE_HEATER_RING_1, STRUCTURE_STRING_HEATER_RING_1)
                .addShape(STRUCTURE_PIECE_HEATER_RING_2, STRUCTURE_STRING_HEATER_RING_2)
                .addShape(STRUCTURE_PIECE_HEATER_RING_3, STRUCTURE_STRING_HEATER_RING_3)
                .addShape(STRUCTURE_PIECE_HEATER_RING_4, STRUCTURE_STRING_HEATER_RING_4)
                .addShape(STRUCTURE_PIECE_HEATER_RING_5, STRUCTURE_STRING_HEATER_RING_5)
                .addElement('g', lazy(t -> {
                    IStructureElement<MTESolarTower> delegate = buildHatchAdder(MTESolarTower.class)
                        .hatchId(MetaTileEntityIDs.Solar_Tower_Reflector.ID)
                        .adder(MTESolarTower::addSolarHeater)
                        // Use a positive casing index to make adder builder happy
                        .casingIndex(1)
                        .hint(1)
                        .continueIfSuccess()
                        .build();
                    return new IStructureElement<MTESolarTower>() {

                        @Override
                        public boolean check(MTESolarTower t, World world, int x, int y, int z) {
                            return delegate.check(t, world, x, y, z);
                        }

                        @Override
                        public boolean spawnHint(MTESolarTower t, World world, int x, int y, int z, ItemStack trigger) {
                            return delegate.spawnHint(t, world, x, y, z, trigger);
                        }

                        @Override
                        public boolean placeBlock(MTESolarTower t, World world, int x, int y, int z,
                            ItemStack trigger) {
                            ItemStack stack = GregtechItemList.Solar_Tower_Reflector.get(1);
                            if (stack == null) return false;
                            if (!(stack.getItem() instanceof ItemBlock itemBlock)) return false;
                            boolean success = itemBlock.placeBlockAt(stack, null, world, x, y, z, 0, 0, 0, 0, 0);
                            if (!success) return false;
                            if (world.getTileEntity(x, y, z) instanceof ITurnable turnable) {
                                IGregTechTileEntity base = t.getBaseMetaTileEntity();
                                int dx = x - base.getXCoord();
                                int dz = z - base.getZCoord();
                                ForgeDirection facing;
                                if (Math.abs(dx) > Math.abs(dz)) {
                                    facing = dx > 0 ? ForgeDirection.EAST : ForgeDirection.WEST;
                                } else if (Math.abs(dz) > Math.abs(dx)) {
                                    facing = dz > 0 ? ForgeDirection.SOUTH : ForgeDirection.NORTH;
                                } else {
                                    // Corner: pick horizontal based on dx
                                    facing = dx > 0 ? ForgeDirection.EAST : ForgeDirection.WEST;
                                }
                                turnable.setFrontFacing(facing);
                            }
                            return true;
                        }

                        @Override
                        public PlaceResult survivalPlaceBlock(MTESolarTower t, World world, int x, int y, int z,
                            ItemStack trigger, AutoPlaceEnvironment env) {
                            return delegate.survivalPlaceBlock(t, world, x, y, z, trigger, env);
                        }
                    };
                }))
                // casingAmount is shared with the element `h`, this counting can be removed as well
                // but I would need to count how many `t` occurs in the structure.
                .addElement(
                    't',
                    lazy(t -> onElementPass(x -> ++x.casingAmount, Casings.StructuralSolarCasing.asElement())))
                // Elements that don't have a hatch adder must be casing, no need to count the casing.
                .addElement('i', lazy(t -> Casings.ThermallyInsulatedCasing.asElement()))
                .addElement('s', lazy(t -> Casings.SaltContainmentCasing.asElement()))
                .addElement('c', lazy(t -> Casings.ThermalContainmentCasing.asElement()))
                .addElement(
                    'h',
                    lazy(
                        t -> buildHatchAdder(MTESolarTower.class).atLeast(InputHatch, OutputHatch, Maintenance)
                            .casingIndex(Casings.StructuralSolarCasing.textureId)
                            .hint(2)
                            .buildAndChain(
                                onElementPass(x -> ++x.casingAmount, Casings.StructuralSolarCasing.asElement()))))
                .build();
        }
    };

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        resetSolarHeaters();
        this.mMaintenanceHatches.clear();
        this.mInputHatches.clear();
        this.mOutputHatches.clear();
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_TOP, 2, 2, 0, errors)) return;
        if (!checkPiece(STRUCTURE_PIECE_TOWER, 1, 1, -7, errors)) return;
        if (!checkPiece(STRUCTURE_PIECE_BASE, 5, 5, -22, errors)) return;
        checkCasingMin(errors, casingAmount, 229);
        checkOneMaintenanceHatch(errors);
        checkHasInputHatch(errors);
        checkHasOutputHatch(errors);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        // Tower
        buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, 2, 2, 0);
        buildPiece(STRUCTURE_PIECE_TOWER, stackSize, hintsOnly, 1, 1, -7);
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 5, 5, -22);

        // Solar Heaters
        if (stackSize.stackSize >= 1) {
            buildPiece(STRUCTURE_PIECE_HEATER_RING_1, stackSize, hintsOnly, 7, 7, -27);
        }
        if (stackSize.stackSize >= 2) {
            buildPiece(STRUCTURE_PIECE_HEATER_RING_2, stackSize, hintsOnly, 9, 9, -27);
        }
        if (stackSize.stackSize >= 3) {
            buildPiece(STRUCTURE_PIECE_HEATER_RING_3, stackSize, hintsOnly, 11, 11, -27);
        }
        if (stackSize.stackSize >= 4) {
            buildPiece(STRUCTURE_PIECE_HEATER_RING_4, stackSize, hintsOnly, 13, 13, -27);
        }
        if (stackSize.stackSize >= 5) {
            buildPiece(STRUCTURE_PIECE_HEATER_RING_5, stackSize, hintsOnly, 15, 15, -27);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 2);
        // Tower
        built = survivalBuildPiece(STRUCTURE_PIECE_TOP, stackSize, 2, 2, 0, realBudget, env, false, true);
        if (built >= 0) return built;
        built = survivalBuildPiece(STRUCTURE_PIECE_TOWER, stackSize, 1, 1, -7, realBudget, env, false, true);
        if (built >= 0) return built;
        built = survivalBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 5, 5, -22, realBudget, env, false, true);
        if (built >= 0) return built;

        // Solar Heaters
        if (stackSize.stackSize < 1) return -1;
        built = survivalBuildPiece(STRUCTURE_PIECE_HEATER_RING_1, stackSize, 7, 7, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 2) return -1;
        built = survivalBuildPiece(STRUCTURE_PIECE_HEATER_RING_2, stackSize, 9, 9, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 3) return -1;
        built = survivalBuildPiece(STRUCTURE_PIECE_HEATER_RING_3, stackSize, 11, 11, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 4) return -1;
        built = survivalBuildPiece(STRUCTURE_PIECE_HEATER_RING_4, stackSize, 13, 13, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 5) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_HEATER_RING_5, stackSize, 15, 15, -27, realBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<MTESolarTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == ForgeDirection.DOWN || side == ForgeDirection.UP) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDSolarTowerActive)
                    .extFacing()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDSolarTower)
                    .extFacing()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)) };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return RecipeMaps.solarTowerRecipes;
    }

    private int getHeaterTier() {
        return switch (this.solarHeaters.size()) {
            case 36 -> 1;
            case 88 -> 2;
            case 156 -> 4;
            case 240 -> 8;
            case 340 -> 16;
            default -> 0;
        };
    }

    private int getHeaterCountForTier(int aTier) {
        return switch (aTier) {
            case 1 -> 36;
            case 2 -> 88;
            case 4 -> 156;
            case 8 -> 240;
            case 16 -> 340;
            default -> 0;
        };
    }

    public void connectSolarReflectors() {
        resetSolarHeaters();
        List<StructureError> ignored = new ArrayList<>();

        if (this.solarHeaters.size() < 36) {
            // 15x15
            checkPiece(STRUCTURE_PIECE_HEATER_RING_1, 7, 7, -27, ignored);
        }
        if (this.solarHeaters.size() < 88) {
            // 17x17
            checkPiece(STRUCTURE_PIECE_HEATER_RING_2, 9, 9, -27, ignored);
        }
        if (this.solarHeaters.size() < 156) {
            // 19x19
            checkPiece(STRUCTURE_PIECE_HEATER_RING_3, 11, 11, -27, ignored);
        }
        if (this.solarHeaters.size() < 240) {
            // 21x21
            checkPiece(STRUCTURE_PIECE_HEATER_RING_4, 13, 13, -27, ignored);
        }
        if (this.solarHeaters.size() < 340) {
            // 23x23
            checkPiece(STRUCTURE_PIECE_HEATER_RING_5, 15, 15, -27, ignored);
        }
    }

    private boolean addSolarHeater(IGregTechTileEntity aTileEntity, int a) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTESolarHeater mTile) {
                if (!mTile.hasSolarTower() && mTile.canSeeSky()) {
                    mTile.setSolarTower(this);
                    return this.solarHeaters.add(mTile);
                }
            }
        }
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d == ForgeDirection.UP;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        this.mEfficiencyIncrease = 100;
        this.mMaxProgresstime = 200;

        if (this.solarHeaters.isEmpty() || this.solarHeaters.size() < 340 || this.getTotalRuntimeInTicks() % 200 == 0) {
            connectSolarReflectors();
        }

        int aTier = getHeaterTier();
        int aHeaters = getHeaterCountForTier(aTier);

        // Original formula was (-Math.pow(this.heatLevel - 50000, 0.8) + 7000) / 7000
        // However, negative numbers to the power of a non-integer result in NaN, by default
        // Max efficiency is 1, at heatLevel = 50000, and it lowers at the same rate if going above or below this heat
        // Min efficiency is 0.179, at heatLevel = 0 or 100000
        double aEfficiency = (-Math.pow(Math.abs(this.heatLevel - 50000), 0.8) + 7000) / 7000;

        World w = this.getBaseMetaTileEntity()
            .getWorld();

        // Manage Heat every 10s
        // Add Heat First, if sources available and it's daytime, heat gain is halved if raining
        if (w != null) {
            if (aHeaters > 0 && w.isDaytime()) {
                if (w.isRaining() && this.getBaseMetaTileEntity()
                    .getBiome().rainfall > 0.0F) {
                    this.heatLevel += GTUtility.safeInt((long) ((aHeaters / 2) * aEfficiency * (10 + aTier)));
                } else {
                    this.heatLevel += GTUtility.safeInt((long) (aHeaters * aEfficiency * (10 + aTier)));
                }
            }

            // Remove Heat, based on time of day
            if (heatLevel > 0) {
                if (heatLevel > 100000) {
                    this.heatLevel = 100000;
                } else {
                    this.heatLevel -= 10;
                }
            }
        }

        if (this.mEfficiency == this.getMaxEfficiency(null) && this.heatLevel >= 30000) {
            if (mColdSalt == null) {
                mColdSalt = MaterialMisc.SOLAR_SALT_COLD.getFluid();
            }
            if (mHotSalt == null) {
                mHotSalt = MaterialMisc.SOLAR_SALT_HOT.getFluid();
            }
            ArrayList<FluidStack> aFluids = this.getStoredFluids();
            for (FluidStack aFluid : aFluids) {
                if (aFluid.getFluid()
                    .equals(mColdSalt)) {
                    int aFluidAmount = Math.min(aFluid.amount, this.heatLevel);

                    this.heatLevel -= aFluidAmount;
                    this.depleteInput(new FluidStack(mColdSalt, aFluidAmount));
                    this.addOutputPartial(new FluidStack(mHotSalt, aFluidAmount));
                    this.heatLevel = Math.max(this.heatLevel, 0);

                    break;
                }
            }
        }

        return CheckRecipeResultRegistry.GENERATING;
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.StructuralSolarCasing.getCasingTexture();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("heatLevel", heatLevel);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heatLevel = aNBT.getInteger("heatLevel");
    }

    @Override
    public void onRemoval() {
        resetSolarHeaters();
        super.onRemoval();
    }

    private void resetSolarHeaters() {
        for (MTESolarHeater aTile : this.solarHeaters) {
            aTile.clearSolarTower();
        }
        this.solarHeaters.clear();
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}
