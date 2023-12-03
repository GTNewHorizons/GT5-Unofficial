package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.TileEntitySolarHeater;

public class GregtechMetaTileEntity_SolarTower extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_SolarTower>
        implements ISurvivalConstructable {

    // 862
    private static final int mCasingTextureID = TAE.getIndexFromPage(3, 9);
    private int mHeatLevel = 0;
    private int mCasing1;
    private int mCasing2;
    private int mCasing3;
    private int mCasing4;

    public ArrayList<TileEntitySolarHeater> mSolarHeaters = new ArrayList<>();

    public GregtechMetaTileEntity_SolarTower(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_SolarTower(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_SolarTower(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Solar Tower";
    }

    @Override
    protected final GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Contributing Green Energy towards the future")
                .addInfo("Surround with rings of Solar Reflectors")
                .addInfo("The Reflectors increase the internal heat value of the Tower (see below for formula)")
                .addInfo("Each Reflector ring increases tier, the first ring is required for the Tower to work")
                .addInfo("Input: " + MISC_MATERIALS.SOLAR_SALT_COLD.getLocalizedName())
                .addInfo("Output: " + MISC_MATERIALS.SOLAR_SALT_HOT.getLocalizedName())
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
                .addInfo("1 ring = 36, 2 rings = 88, 3 rings = 156, 4 rings = 240, 5 rings = 340").addSeparator()
                .beginVariableStructureBlock(15, 31, 28, 28, 15, 31, false).addController("Top Middle")
                .addCasingInfoMin("Structural Solar Casing", 229, false)
                .addCasingInfoMin("Thermally Insulated Casing", 60, false)
                .addCasingInfoMin("Salt Containment Casing", 66, false)
                .addCasingInfoMin("Thermal Containment Casing", 60, false).addInputHatch("Any 2 dot hint(min 1)", 2)
                .addOutputHatch("Any 2 dot hint(min 1)", 2).addMaintenanceHatch("Any 2 dot hint", 2)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    private static final String STRUCTURE_PIECE_BASE = "base";
    private static final String STRUCTURE_PIECE_TOWER = "tower";
    private static final String STRUCTURE_PIECE_TOP = "top";

    private static final String[] STRUCTURE_PIECE_SOLAR_HEATER_RING = { "ring1", "ring2", "ring3", "ring4", "ring5" };
    private static final String SOLAR_HEATER_RING_1 = STRUCTURE_PIECE_SOLAR_HEATER_RING[0];
    private static final String SOLAR_HEATER_RING_2 = STRUCTURE_PIECE_SOLAR_HEATER_RING[1];
    private static final String SOLAR_HEATER_RING_3 = STRUCTURE_PIECE_SOLAR_HEATER_RING[2];
    private static final String SOLAR_HEATER_RING_4 = STRUCTURE_PIECE_SOLAR_HEATER_RING[3];
    private static final String SOLAR_HEATER_RING_5 = STRUCTURE_PIECE_SOLAR_HEATER_RING[4];

    private static final ClassValue<IStructureDefinition<GregtechMetaTileEntity_SolarTower>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<GregtechMetaTileEntity_SolarTower> computeValue(Class<?> type) {
            return StructureDefinition.<GregtechMetaTileEntity_SolarTower>builder()

                    // s = salt
                    // c = thermal containment
                    // i = thermal insulated
                    // t = solar structural
                    // h = hatch
                    // g = solar heater

                    .addShape(
                            STRUCTURE_PIECE_TOP,
                            (new String[][] { { "     ", "     ", "  ~  ", "     ", "     " },
                                    { "     ", "  s  ", " sss ", "  s  ", "     " },
                                    { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
                                    { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
                                    { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
                                    { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " },
                                    { "  c  ", " ccc ", "ccscc", " ccc ", "  c  " }, }))
                    .addShape(
                            STRUCTURE_PIECE_TOWER,
                            (new String[][] { { " i ", "isi", " i " }, { " i ", "isi", " i " }, { " i ", "isi", " i " },
                                    { " i ", "isi", " i " }, { " i ", "isi", " i " }, { " i ", "isi", " i " },
                                    { " i ", "isi", " i " }, { " i ", "isi", " i " }, { " i ", "isi", " i " },
                                    { " i ", "isi", " i " }, { " i ", "isi", " i " }, { " i ", "isi", " i " },
                                    { " i ", "isi", " i " }, { " i ", "isi", " i " }, { " i ", "isi", " i " }, }))
                    .addShape(
                            STRUCTURE_PIECE_BASE,
                            (new String[][] {
                                    { "           ", "           ", "     t     ", "    ttt    ", "   ttstt   ",
                                            "  ttssstt  ", "   ttstt   ", "    ttt    ", "     t     ", "           ",
                                            "           " },
                                    { "           ", "           ", "     t     ", "    ttt    ", "   tssst   ",
                                            "  ttssstt  ", "   tssst   ", "    ttt    ", "     t     ", "           ",
                                            "           " },
                                    { "           ", "     t     ", "    ttt    ", "   ttttt   ", "  ttssstt  ",
                                            " tttsssttt ", "  ttssstt  ", "   ttttt   ", "    ttt    ", "     t     ",
                                            "           " },
                                    { "           ", "     t     ", "    ttt    ", "   ttttt   ", "  ttssstt  ",
                                            " tttsssttt ", "  ttssstt  ", "   ttttt   ", "    ttt    ", "     t     ",
                                            "           " },
                                    { "    hhh    ", "   ttttt   ", "  ttttttt  ", " ttttttttt ", "htttsssttth",
                                            "htttsssttth", "htttsssttth", " ttttttttt ", "  ttttttt  ", "   ttttt   ",
                                            "    hhh    " },
                                    { "    hhh    ", "   ttttt   ", "  ttttttt  ", " ttttttttt ", "httttttttth",
                                            "httttttttth", "httttttttth", " ttttttttt ", "  ttttttt  ", "   ttttt   ",
                                            "    hhh    " }, }))
                    .addShape(
                            SOLAR_HEATER_RING_1,
                            (new String[][] { { "     ggggg     ", "    g     g    ", "   g       g   ",
                                    "  g         g  ", " g           g ", "g             g", "g             g",
                                    "g             g", "g             g", "g             g", " g           g ",
                                    "  g         g  ", "   g       g   ", "    g     g    ", "     ggggg     ", } }))
                    .addShape(
                            SOLAR_HEATER_RING_2,
                            (new String[][] { { "     ggggggggg     ", "    g         g    ", "   g           g   ",
                                    "  g             g  ", " g               g ", "g                 g",
                                    "g                 g", "g                 g", "g                 g",
                                    "g                 g", "g                 g", "g                 g",
                                    "g                 g", "g                 g", " g               g ",
                                    "  g             g  ", "   g           g   ", "    g         g    ",
                                    "     ggggggggg     ", } }))
                    .addShape(
                            SOLAR_HEATER_RING_3,
                            (new String[][] { { "     ggggggggggggg     ", "    g             g    ",
                                    "   g               g   ", "  g                 g  ", " g                   g ",
                                    "g                     g", "g                     g", "g                     g",
                                    "g                     g", "g                     g", "g                     g",
                                    "g                     g", "g                     g", "g                     g",
                                    "g                     g", "g                     g", "g                     g",
                                    "g                     g", " g                   g ", "  g                 g  ",
                                    "   g               g   ", "    g             g    ",
                                    "     ggggggggggggg     ", } }))
                    .addShape(
                            SOLAR_HEATER_RING_4,
                            (new String[][] { { "     ggggggggggggggggg     ", "    g                 g    ",
                                    "   g                   g   ", "  g                     g  ",
                                    " g                       g ", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    "g                         g", "g                         g",
                                    " g                       g ", "  g                     g  ",
                                    "   g                   g   ", "    g                 g    ",
                                    "     ggggggggggggggggg     ", } }))
                    .addShape(
                            SOLAR_HEATER_RING_5,
                            (new String[][] { { "     ggggggggggggggggggggg     ", "    g                     g    ",
                                    "   g                       g   ", "  g                         g  ",
                                    " g                           g ", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    "g                             g", "g                             g",
                                    " g                           g ", "  g                         g  ",
                                    "   g                       g   ", "    g                     g    ",
                                    "     ggggggggggggggggggggg     ", } }))
                    .addElement(
                            'g',
                            lazy(
                                    t -> buildHatchAdder(GregtechMetaTileEntity_SolarTower.class)
                                            .hatchClass(TileEntitySolarHeater.class)
                                            .adder(GregtechMetaTileEntity_SolarTower::addSolarHeater)
                                            // Use a positive casing index to make adder builder happy
                                            .casingIndex(1).dot(1).continueIfSuccess().build()))
                    .addElement(
                            't',
                            lazy(t -> onElementPass(x -> ++x.mCasing1, ofBlock(t.getCasingBlock(), t.getCasingMeta()))))
                    .addElement(
                            'i',
                            lazy(
                                    t -> onElementPass(
                                            x -> ++x.mCasing2,
                                            ofBlock(t.getCasingBlock(), t.getCasingMeta2()))))
                    .addElement(
                            's',
                            lazy(
                                    t -> onElementPass(
                                            x -> ++x.mCasing3,
                                            ofBlock(t.getCasingBlock(), t.getCasingMeta3()))))
                    .addElement(
                            'c',
                            lazy(
                                    t -> onElementPass(
                                            x -> ++x.mCasing4,
                                            ofBlock(t.getCasingBlock2(), t.getCasingMeta4()))))
                    .addElement(
                            'h',
                            lazy(
                                    t -> buildHatchAdder(GregtechMetaTileEntity_SolarTower.class)
                                            .atLeast(InputHatch, OutputHatch, Maintenance)
                                            .casingIndex(t.getCasingTextureIndex()).dot(2).buildAndChain(
                                                    onElementPass(
                                                            x -> ++x.mCasing1,
                                                            ofBlock(t.getCasingBlock(), t.getCasingMeta())))))
                    .build();
        }
    };

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        resetSolarHeaters();
        this.mMaintenanceHatches.clear();
        this.mInputHatches.clear();
        this.mOutputHatches.clear();
        mCasing1 = 0;
        mCasing2 = 0;
        mCasing3 = 0;
        mCasing4 = 0;

        boolean aStructureTop = checkPiece(STRUCTURE_PIECE_TOP, 2, 2, 0);
        log("Top Check: " + aStructureTop);
        boolean aStructureTower = checkPiece(STRUCTURE_PIECE_TOWER, 1, 1, -7);
        log("Tower Check: " + aStructureTower);
        boolean aStructureBase = checkPiece(STRUCTURE_PIECE_BASE, 5, 5, -22);
        log("Base Check: " + aStructureBase);
        boolean aCasingCount1 = mCasing1 >= 229;
        boolean aCasingCount2 = mCasing2 == 60;
        boolean aCasingCount3 = mCasing3 == 66;
        boolean aCasingCount4 = mCasing4 == 60;
        boolean aAllStructure = aStructureTop && aStructureTower && aStructureBase;
        boolean aAllCasings = aCasingCount1 && aCasingCount2 && aCasingCount3 && aCasingCount4;
        if (!aAllCasings || !aAllStructure
                || mMaintenanceHatches.size() != 1
                || mInputHatches.size() < 1
                || mOutputHatches.size() < 1) {
            log(
                    "Bad Hatches - Solar Heaters: " + mSolarHeaters.size()
                            + ", Maint: "
                            + mMaintenanceHatches.size()
                            + ", Input Hatches: "
                            + mInputHatches.size()
                            + ", Output Hatches: "
                            + mOutputHatches.size()
                            + ", Top: "
                            + aStructureTop
                            + ", Tower: "
                            + aStructureTower
                            + ", Base: "
                            + aStructureBase
                            + ", Casing Count: "
                            + aCasingCount1
                            + " | Found: "
                            + mCasing1
                            + ", Casing Count: "
                            + aCasingCount2
                            + " | Found: "
                            + mCasing2
                            + ", Casing Count: "
                            + aCasingCount3
                            + " | Found: "
                            + mCasing3
                            + ", Casing Count: "
                            + aCasingCount4
                            + " | Found: "
                            + mCasing4);
            return false;
        }
        log(
                "Built " + this.getLocalName()
                        + " with "
                        + mCasing1
                        + " Structural Solar casings, "
                        + mCasing2
                        + " Thermally Insulated casings, "
                        + mCasing3
                        + " Salt Containment casings, "
                        + mCasing4
                        + " Thermal Containment casings.");
        return aAllCasings && aAllStructure;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        // Tower
        buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, 2, 2, 0);
        buildPiece(STRUCTURE_PIECE_TOWER, stackSize, hintsOnly, 1, 1, -7);
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 5, 5, -22);

        // Solar Heaters
        if (stackSize.stackSize >= 1) {
            buildPiece(SOLAR_HEATER_RING_1, stackSize, hintsOnly, 7, 7, -27);
            if (stackSize.stackSize >= 2) {
                buildPiece(SOLAR_HEATER_RING_2, stackSize, hintsOnly, 9, 9, -27);
                if (stackSize.stackSize >= 3) {
                    buildPiece(SOLAR_HEATER_RING_3, stackSize, hintsOnly, 11, 11, -27);
                    if (stackSize.stackSize >= 4) {
                        buildPiece(SOLAR_HEATER_RING_4, stackSize, hintsOnly, 13, 13, -27);
                        if (stackSize.stackSize >= 5) {
                            buildPiece(SOLAR_HEATER_RING_5, stackSize, hintsOnly, 15, 15, -27);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 2);
        // Tower
        built = survivialBuildPiece(STRUCTURE_PIECE_TOP, stackSize, 2, 2, 0, realBudget, env, false, true);
        if (built >= 0) return built;
        built = survivialBuildPiece(STRUCTURE_PIECE_TOWER, stackSize, 1, 1, -7, realBudget, env, false, true);
        if (built >= 0) return built;
        built = survivialBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 5, 5, -22, realBudget, env, false, true);
        if (built >= 0) return built;

        // Solar Heaters
        if (stackSize.stackSize < 1) return -1;
        built = survivialBuildPiece(SOLAR_HEATER_RING_1, stackSize, 7, 7, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 2) return -1;
        built = survivialBuildPiece(SOLAR_HEATER_RING_2, stackSize, 9, 9, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 3) return -1;
        built = survivialBuildPiece(SOLAR_HEATER_RING_3, stackSize, 11, 11, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 4) return -1;
        built = survivialBuildPiece(SOLAR_HEATER_RING_4, stackSize, 13, 13, -27, realBudget, env, false, true);
        if (built >= 0) return built;
        if (stackSize.stackSize < 5) return -1;
        return survivialBuildPiece(SOLAR_HEATER_RING_5, stackSize, 15, 15, -27, realBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SolarTower> getStructureDefinition() {
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
                    TextureFactory.builder().addIcon(TexturesGtBlock.Overlay_Machine_Controller_Default_Active)
                            .extFacing().build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)), TextureFactory
                    .builder().addIcon(TexturesGtBlock.Overlay_Machine_Controller_Default).extFacing().build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)) };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return GTPPRecipeMaps.solarTowerRecipes;
    }

    private int getHeaterTier() {
        int aSolarHeaterCounter = this.mSolarHeaters.size();
        if (aSolarHeaterCounter > 0) {
            if (aSolarHeaterCounter == 36) {
                return 1;
            } else if (aSolarHeaterCounter == 88) {
                return 2;
            } else if (aSolarHeaterCounter == 156) {
                return 4;
            } else if (aSolarHeaterCounter == 240) {
                return 8;
            } else if (aSolarHeaterCounter == 340) {
                return 16;
            }
        }
        return 0;
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

    public boolean getConnectedSolarReflectors() {

        resetSolarHeaters();
        int aRing = 1;

        if (this.mSolarHeaters.size() < 36) {
            // 15x15
            boolean aRing1 = checkPiece(SOLAR_HEATER_RING_1, 7, 7, -27);
            if (aRing1) {
                // log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
            }
        }
        if (this.mSolarHeaters.size() < 88) {
            // 17x17
            boolean aRing2 = checkPiece(SOLAR_HEATER_RING_2, 9, 9, -27);
            if (aRing2) {
                // log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
            }
        }
        if (this.mSolarHeaters.size() < 156) {
            // 19x19
            boolean aRing3 = checkPiece(SOLAR_HEATER_RING_3, 11, 11, -27);
            if (aRing3) {
                // log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
            }
        }
        if (this.mSolarHeaters.size() < 240) {
            // 21x21
            boolean aRing4 = checkPiece(SOLAR_HEATER_RING_4, 13, 13, -27);
            if (aRing4) {
                // log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
            }
        }
        if (this.mSolarHeaters.size() < 340) {
            // 23x23
            boolean aRing5 = checkPiece(SOLAR_HEATER_RING_5, 15, 15, -27);
            if (aRing5) {
                // log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
            }
        }
        return mSolarHeaters.size() > 0;
    }

    private boolean addSolarHeater(IGregTechTileEntity aTileEntity, int a) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof TileEntitySolarHeater mTile) {
                if (!mTile.hasSolarTower() && mTile.canSeeSky()) {
                    // Logger.INFO("Found Solar Reflector, Injecting Data.");
                    mTile.setSolarTower(this);
                    return this.mSolarHeaters.add(mTile);
                }
            }
        }
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d == ForgeDirection.UP;
    }

    private Fluid mColdSalt = null;
    private Fluid mHotSalt = null;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        this.mEfficiencyIncrease = 100;
        this.mMaxProgresstime = 200;

        if (this.mSolarHeaters.isEmpty() || this.mSolarHeaters.size() < 340
                || this.getTotalRuntimeInTicks() % 200 == 0) {
            getConnectedSolarReflectors();
        }

        int aTier = getHeaterTier();
        int aHeaters = getHeaterCountForTier(aTier);

        // Original formula was (-Math.pow(this.mHeatLevel - 50000, 0.8) + 7000) / 7000
        // However, negative numbers to the power of a non-integer result in NaN, by default
        // Max efficiency is 1, at mHeatLevel = 50000, and it lowers at the same rate if going above or below this heat
        // Min efficiency is 0.179, at mHeatLevel = 0 or 100000
        double aEfficiency = (-Math.pow(Math.abs(this.mHeatLevel - 50000), 0.8) + 7000) / 7000;

        World w = this.getBaseMetaTileEntity().getWorld();

        // Manage Heat every 10s
        // Add Heat First, if sources available and it's daytime, heat gain is halved if raining
        if (w != null) {
            if (aHeaters > 0 && w.isDaytime()) {
                if (w.isRaining() && this.getBaseMetaTileEntity().getBiome().rainfall > 0.0F) {
                    this.mHeatLevel += GT_Utility.safeInt((long) ((aHeaters / 2) * aEfficiency * (10 + aTier)));
                } else {
                    this.mHeatLevel += GT_Utility.safeInt((long) (aHeaters * aEfficiency * (10 + aTier)));
                }
            }

            // Remove Heat, based on time of day
            if (mHeatLevel > 0) {
                if (mHeatLevel > 100000) {
                    this.mHeatLevel = 100000;
                } else {
                    this.mHeatLevel -= 10;
                }
            }
        }

        if (this.mEfficiency == this.getMaxEfficiency(null) && this.mHeatLevel >= 30000) {
            if (mColdSalt == null) {
                mColdSalt = MISC_MATERIALS.SOLAR_SALT_COLD.getFluid();
            }
            if (mHotSalt == null) {
                mHotSalt = MISC_MATERIALS.SOLAR_SALT_HOT.getFluid();
            }
            ArrayList<FluidStack> aFluids = this.getStoredFluids();
            for (FluidStack aFluid : aFluids) {
                if (aFluid.getFluid().equals(mColdSalt)) {
                    int aFluidAmount = Math.min(aFluid.amount, this.mHeatLevel);

                    this.mHeatLevel -= aFluidAmount;
                    this.depleteInput(FluidUtils.getFluidStack(mColdSalt, aFluidAmount));
                    this.addOutput(FluidUtils.getFluidStack(mHotSalt, aFluidAmount));
                    this.mHeatLevel = Math.max(this.mHeatLevel, 0);

                    break;
                }
            }
        }

        return CheckRecipeResultRegistry.GENERATING;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(final ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockSpecialMultiCasings;
    }

    public Block getCasingBlock2() {
        return ModBlocks.blockCasings2Misc;
    }

    public byte getCasingMeta() {
        return 6;
    }

    public byte getCasingMeta2() {
        return 8;
    }

    public byte getCasingMeta3() {
        return 7;
    }

    public byte getCasingMeta4() {
        return 11;
    }

    public byte getCasingTextureIndex() {
        return (byte) mCasingTextureID;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {}

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mHeatLevel", mHeatLevel);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mHeatLevel = aNBT.getInteger("mHeatLevel");
    }

    @Override
    public void onRemoval() {
        resetSolarHeaters();
        super.onRemoval();
    }

    private void resetSolarHeaters() {
        for (TileEntitySolarHeater aTile : this.mSolarHeaters) {
            aTile.clearSolarTower();
        }
        this.mSolarHeaters.clear();
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { "Internal Heat Level: " + this.mHeatLevel,
                "Connected Solar Reflectors: " + this.mSolarHeaters.size() };
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }
}
