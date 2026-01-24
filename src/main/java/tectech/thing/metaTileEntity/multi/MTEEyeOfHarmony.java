package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.api.util.ParallelHelper.calculateIntegralChancedOutputMultiplier;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static java.lang.Math.exp;
import static kekztech.util.Util.toStandardForm;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.BLUE;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.STRIKETHROUGH;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import gregtech.common.tileentities.machines.MTEHatchOutputME;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtneioreplugin.plugin.block.BlockDimensionDisplay;
import gtneioreplugin.plugin.block.ModBlocks;
import tectech.TecTech;
import tectech.recipe.EyeOfHarmonyRecipe;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.util.CommonValues;
import tectech.util.FluidStackLong;
import tectech.util.ItemStackLong;

@SuppressWarnings("SpellCheckingInspection")
public class MTEEyeOfHarmony extends TTMultiblockBase implements IConstructable, ISurvivalConstructable {

    public static final boolean EOH_DEBUG_MODE = false;
    private static final long MOLTEN_SPACETIME_PER_FAILURE_TIER = 14_400L;
    private static final double SPACETIME_FAILURE_BASE = 2;

    // Region variables.
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int spacetimeCompressionFieldMetadata = -1;
    private int timeAccelerationFieldMetadata = -1;
    private int stabilisationFieldMetadata = -1;

    private static final double SPACETIME_CASING_DIFFERENCE_DISCOUNT_PERCENTAGE = 0.03;
    private static final double TIME_ACCEL_DECREASE_CHANCE_PER_TIER = 0.0925;
    // % Increase in recipe chance and % decrease in yield per tier.
    private static final double STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER = 0.05;
    private static final double PARALLEL_FOR_FIRST_ASTRAL_ARRAY = 8;
    private static final double CONSTANT_FOR_LOG = 1.7;
    private static final double LOG_CONSTANT = Math.log(CONSTANT_FOR_LOG);
    private static final double PARALLEL_MULTIPLIER_CONSTANT = 1.63;
    private static final double POWER_DIVISION_CONSTANT = 20.7;
    private static final double POWER_INCREASE_CONSTANT = 2.3;
    private static final int TOTAL_CASING_TIERS_WITH_POWER_PENALTY = 8;
    private static final long PRECISION_MULTIPLIER = 1_000_000;
    // Exact value to get 2^21 parallels.
    private static final long ASTRAL_ARRAY_LIMIT = 8637;

    private UUID userUUID;
    private BigInteger outputEU_BigInt = BigInteger.ZERO;
    private long startEU = 0;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5); // 200 blocks max per
                                                                                                  // placement.
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 16, 16, 0, realBudget, source, actor, false, true);
    }

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    // Multiblock structure.
    private static final IStructureDefinition<MTEEyeOfHarmony> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEEyeOfHarmony>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "            CCCCCCCCC            ",
                        "               C C               ", "            CCCCCCCCC            ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "              DDDDD              ",
                        "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ",
                        "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ",
                        "             DDCDCDD             ", "              DDDDD              ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "                D                ", "                D                ",
                        "             DDDDDDD             ", "            DD     DD            ",
                        "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ",
                        "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ",
                        "            D  EEE  D            ", "            DD     DD            ",
                        "             DDDDDDD             ", "                D                ",
                        "                D                ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "                D                ", "                D                ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "      CC                 CC      ",
                        "        DD             DD        ", "      CC                 CC      ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                D                ",
                        "                D                ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "              CCCCC              ", "                D                ",
                        "                A                ", "                A                ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "      C                   C      ", "     CC                   CC     ",
                        "      CDAA             AADC      ", "     CC                   CC     ",
                        "      C                   C      ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                A                ",
                        "                A                ", "                D                ",
                        "              CCCCC              ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "                D                ", "             SEEAEES             ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "       S                 S       ",
                        "       E                 E       ", "    CC E                 E CC    ",
                        "      DA                 AD      ", "    CC E                 E CC    ",
                        "       E                 E       ", "       S                 S       ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "             SEEAEES             ",
                        "                D                ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "              CCCCC              ", "                D                ",
                        "                A                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "    C                       C    ", "   CC                       CC   ",
                        "    CDA                   ADC    ", "   CC                       CC   ",
                        "    C                       C    ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                A                ", "                D                ",
                        "              CCCCC              ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "                D                ", "             SEEAEES             ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "     S                     S     ",
                        "     E                     E     ", "  CC E                     E CC  ",
                        "    DA                     AD    ", "  CC E                     E CC  ",
                        "     E                     E     ", "     S                     S     ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "             SEEAEES             ",
                        "                D                ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "               C C               ", "                D                ",
                        "                A                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "  C                           C  ",
                        "   DA                       AD   ", "  C                           C  ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                A                ", "                D                ",
                        "               C C               ", "                                 ",
                        "                                 " },
                    { "                                 ", "               C C               ",
                        "               C C               ", "                D                ",
                        "                A                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", " CC                           CC ",
                        "   DA                       AD   ", " CC                           CC ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                A                ", "                D                ",
                        "               C C               ", "               C C               ",
                        "                                 " },
                    { "                                 ", "               C C               ",
                        "                D                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", " C                             C ",
                        "  D                           D  ", " C                             C ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                D                ", "               C C               ",
                        "                                 " },
                    { "                                 ", "               C C               ",
                        "                D                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", " C                             C ",
                        "  D                           D  ", " C                             C ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                D                ", "               C C               ",
                        "                                 " },
                    { "             CCCCCCC             ", "               C C               ",
                        "             DDDDDDD             ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "  D                           D  ",
                        "  D                           D  ", "CCD                           DCC",
                        "  D                           D  ", "CCD                           DCC",
                        "  D                           D  ", "  D                           D  ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "             DDDDDDD             ", "               C C               ",
                        "               C C               " },
                    { "            CCHHHHHCC            ", "              DDDDD              ",
                        "            DD     DD            ", "                                 ",
                        "                                 ", "       S                 S       ",
                        "                                 ", "     S                     S     ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "  D                           D  ", "  D                           D  ",
                        " D                             D ", "CD                             DC",
                        " D                             D ", "CD                             DC",
                        " D                             D ", "  D                           D  ",
                        "  D                           D  ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "     S                     S     ",
                        "                                 ", "       S                 S       ",
                        "                                 ", "                                 ",
                        "            DD     DD            ", "              DDDDD              ",
                        "               C C               " },
                    { "            CHHHHHHHC            ", "             DDCDCDD             ",
                        "            D  EEE  D            ", "                                 ",
                        "      C                   C      ", "       E                 E       ",
                        "    C                       C    ", "     E                     E     ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "  D                           D  ", " D                             D ",
                        " D                             D ", "CCE                           ECC",
                        " DE                           ED ", "CCE                           ECC",
                        " D                             D ", " D                             D ",
                        "  D                           D  ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "     E                     E     ",
                        "    C                       C    ", "       E                 E       ",
                        "      C                   C      ", "                                 ",
                        "            D  EEE  D            ", "             DDCDCDD             ",
                        "               C C               " },
                    { "            CHHCCCHHC            ", "         CCCCDCCDCCDCCCC         ",
                        "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ",
                        "     CC                   CC     ", "    CC E                 E CC    ",
                        "   CC                       CC   ", "  CC E                     E CC  ",
                        "  C                           C  ", " CC                           CC ",
                        " C                             C ", " C                             C ",
                        "CCD                           DCC", "CD                             DC",
                        "CCE                           ECC", "CCA                           ACC",
                        "CDA                           ADC", "CCA                           ACC",
                        "CCE                           ECC", "CD                             DC",
                        "CCD                           DCC", " C                             C ",
                        " C                             C ", " CC                           CC ",
                        "  C                           C  ", "  CC E                     E CC  ",
                        "   CC                       CC   ", "    CC E                 E CC    ",
                        "     CC                   CC     ", "      CC                 CC      ",
                        "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ",
                        "            CCCCCCCCC            " },
                    { "            CHHC~CHHC            ", "             DDDDDDD             ",
                        "          DDD EAAAE DDD          ", "        DD             DD        ",
                        "      CDAA             AADC      ", "      DA                 AD      ",
                        "    CDA                   ADC    ", "    DA                     AD    ",
                        "   DA                       AD   ", "   DA                       AD   ",
                        "  D                           D  ", "  D                           D  ",
                        "  D                           D  ", " D                             D ",
                        " DE                           ED ", "CDA                           ADC",
                        " DA                           AD ", "CDA                           ADC",
                        " DE                           ED ", " D                             D ",
                        "  D                           D  ", "  D                           D  ",
                        "  D                           D  ", "   DA                       AD   ",
                        "   DA                       AD   ", "    DA                     AD    ",
                        "    CDA                   ADC    ", "      DA                 AD      ",
                        "      CDAA             AADC      ", "        DD             DD        ",
                        "          DDD EAAAE DDD          ", "             DDDDDDD             ",
                        "               C C               " },
                    { "            CHHCCCHHC            ", "         CCCCDCCDCCDCCCC         ",
                        "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ",
                        "     CC                   CC     ", "    CC E                 E CC    ",
                        "   CC                       CC   ", "  CC E                     E CC  ",
                        "  C                           C  ", " CC                           CC ",
                        " C                             C ", " C                             C ",
                        "CCD                           DCC", "CD                             DC",
                        "CCE                           ECC", "CCA                           ACC",
                        "CDA                           ADC", "CCA                           ACC",
                        "CCE                           ECC", "CD                             DC",
                        "CCD                           DCC", " C                             C ",
                        " C                             C ", " CC                           CC ",
                        "  C                           C  ", "  CC E                     E CC  ",
                        "   CC                       CC   ", "    CC E                 E CC    ",
                        "     CC                   CC     ", "      CC                 CC      ",
                        "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ",
                        "            CCCCCCCCC            " },
                    { "            CHHHHHHHC            ", "             DDCDCDD             ",
                        "            D  EEE  D            ", "                                 ",
                        "      C                   C      ", "       E                 E       ",
                        "    C                       C    ", "     E                     E     ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "  D                           D  ", " D                             D ",
                        " D                             D ", "CCE                           ECC",
                        " DE                           ED ", "CCE                           ECC",
                        " D                             D ", " D                             D ",
                        "  D                           D  ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "     E                     E     ",
                        "    C                       C    ", "       E                 E       ",
                        "      C                   C      ", "                                 ",
                        "            D  EEE  D            ", "             DDCDCDD             ",
                        "               C C               " },
                    { "            CCHHHHHCC            ", "              DDDDD              ",
                        "            DD     DD            ", "                                 ",
                        "                                 ", "       S                 S       ",
                        "                                 ", "     S                     S     ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "  D                           D  ", "  D                           D  ",
                        " D                             D ", "CD                             DC",
                        " D                             D ", "CD                             DC",
                        " D                             D ", "  D                           D  ",
                        "  D                           D  ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "     S                     S     ",
                        "                                 ", "       S                 S       ",
                        "                                 ", "                                 ",
                        "            DD     DD            ", "              DDDDD              ",
                        "               C C               " },
                    { "             CCCCCCC             ", "               C C               ",
                        "             DDDDDDD             ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "  D                           D  ",
                        "  D                           D  ", "CCD                           DCC",
                        "  D                           D  ", "CCD                           DCC",
                        "  D                           D  ", "  D                           D  ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "             DDDDDDD             ", "               C C               ",
                        "               C C               " },
                    { "                                 ", "               C C               ",
                        "                D                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", " C                             C ",
                        "  D                           D  ", " C                             C ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                D                ", "               C C               ",
                        "                                 " },
                    { "                                 ", "               C C               ",
                        "                D                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", " C                             C ",
                        "  D                           D  ", " C                             C ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                D                ", "               C C               ",
                        "                                 " },
                    { "                                 ", "               C C               ",
                        "               C C               ", "                D                ",
                        "                A                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", " CC                           CC ",
                        "   DA                       AD   ", " CC                           CC ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                A                ", "                D                ",
                        "               C C               ", "               C C               ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "               C C               ", "                D                ",
                        "                A                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "  C                           C  ",
                        "   DA                       AD   ", "  C                           C  ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                A                ", "                D                ",
                        "               C C               ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "                D                ", "             SEEAEES             ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "     S                     S     ",
                        "     E                     E     ", "  CC E                     E CC  ",
                        "    DA                     AD    ", "  CC E                     E CC  ",
                        "     E                     E     ", "     S                     S     ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "             SEEAEES             ",
                        "                D                ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "              CCCCC              ", "                D                ",
                        "                A                ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "    C                       C    ", "   CC                       CC   ",
                        "    CDA                   ADC    ", "   CC                       CC   ",
                        "    C                       C    ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                A                ", "                D                ",
                        "              CCCCC              ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "                D                ", "             SEEAEES             ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "       S                 S       ",
                        "       E                 E       ", "    CC E                 E CC    ",
                        "      DA                 AD      ", "    CC E                 E CC    ",
                        "       E                 E       ", "       S                 S       ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "             SEEAEES             ",
                        "                D                ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "              CCCCC              ", "                D                ",
                        "                A                ", "                A                ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "      C                   C      ", "     CC                   CC     ",
                        "      CDAA             AADC      ", "     CC                   CC     ",
                        "      C                   C      ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                A                ",
                        "                A                ", "                D                ",
                        "              CCCCC              ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "                D                ", "                D                ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "      CC                 CC      ",
                        "        DD             DD        ", "      CC                 CC      ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                D                ",
                        "                D                ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "                D                ", "                D                ",
                        "             DDDDDDD             ", "            DD     DD            ",
                        "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ",
                        "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ",
                        "            D  EEE  D            ", "            DD     DD            ",
                        "             DDDDDDD             ", "                D                ",
                        "                D                ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "              DDDDD              ",
                        "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ",
                        "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ",
                        "             DDCDCDD             ", "              DDDDD              ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "               C C               ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " },
                    { "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "            CCCCCCCCC            ",
                        "               C C               ", "            CCCCCCCCC            ",
                        "               C C               ", "               C C               ",
                        "               C C               ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 ", "                                 ",
                        "                                 " } }))
        .addElement(
            'A',
            GTStructureChannels.EOH_COMPRESSION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.SpacetimeCompressionFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 0),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 1),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 3),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 4),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 5),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 6),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 7),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.spacetimeCompressionFieldMetadata = meta,
                    t -> t.spacetimeCompressionFieldMetadata)))
        .addElement(
            'S',
            GTStructureChannels.EOH_STABILISATION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.StabilisationFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 0),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 1),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 2),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 3),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 4),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 5),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 6),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 7),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.stabilisationFieldMetadata = meta,
                    t -> t.stabilisationFieldMetadata)))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11))
        .addElement('D', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10))
        .addElement(
            'H',
            buildHatchAdder(MTEEyeOfHarmony.class).atLeast(InputHatch, OutputHatch, InputBus, OutputBus)
                .casingIndex(BlockGTCasingsTT.texturePage << 7)
                .hint(1)
                .buildAndChain(TTCasingsContainer.sBlockCasingsBA0, 12))
        .addElement(
            'E',
            GTStructureChannels.EOH_DILATION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.TimeAccelerationFieldGenerator ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 0),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 1),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 2),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 3),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 4),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 5),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 6),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 7),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)),
                    -1,
                    (t, meta) -> t.timeAccelerationFieldMetadata = meta,
                    t -> t.timeAccelerationFieldMetadata)))
        .build();

    private double hydrogenOverflowProbabilityAdjustment;
    private double heliumOverflowProbabilityAdjustment;
    private double stellarPlasmaOverflowProbabilityAdjustment;
    private static final long TICKS_BETWEEN_HATCH_DRAIN = EOH_DEBUG_MODE ? 10 : 20;

    private List<ItemStackLong> outputItems = new ArrayList<>();
    private List<FluidStackLong> outputFluids = new ArrayList<>();

    private void calculateInputFluidExcessValues(final long hydrogenRecipeRequirement,
        final long heliumRecipeRequirement) {

        double hydrogenStored = getHydrogenStored();
        double heliumStored = getHeliumStored();
        double stellarPlasmaStored = getStellarPlasmaStored();

        double hydrogenExcessPercentage = hydrogenStored / hydrogenRecipeRequirement - 1;
        double heliumExcessPercentage = heliumStored / heliumRecipeRequirement - 1;
        double stellarPlasmaExcessPercentage = stellarPlasmaStored
            / (heliumRecipeRequirement * (12.4 / 1_000_000f) * parallelAmount) - 1;

        hydrogenOverflowProbabilityAdjustment = 1 - exp(-GTUtility.powInt(30 * hydrogenExcessPercentage, 2));
        heliumOverflowProbabilityAdjustment = 1 - exp(-GTUtility.powInt(30 * heliumExcessPercentage, 2));
        stellarPlasmaOverflowProbabilityAdjustment = 1 - exp(-GTUtility.powInt(30 * stellarPlasmaExcessPercentage, 2));
    }

    private double recipeChanceCalculator() {
        double chance = currentRecipe.getBaseRecipeSuccessChance()
            - timeAccelerationFieldMetadata * TIME_ACCEL_DECREASE_CHANCE_PER_TIER
            + stabilisationFieldMetadata * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;

        if (parallelAmount > 1) {
            chance -= stellarPlasmaOverflowProbabilityAdjustment;
        } else {
            if (chance == previousRecipeChance && pityChance >= 1) {
                chance = 1;
            }
            chance -= (hydrogenOverflowProbabilityAdjustment + heliumOverflowProbabilityAdjustment);
        }

        return MathHelper.clamp_double(chance, 0.0, 1.0);
    }

    private double recipeYieldCalculator() {
        double yield = 1.0 - stabilisationFieldMetadata * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;

        if (parallelAmount > 1) {
            yield -= stellarPlasmaOverflowProbabilityAdjustment;
        } else {
            yield -= (hydrogenOverflowProbabilityAdjustment + heliumOverflowProbabilityAdjustment);
        }
        return MathHelper.clamp_double(yield, 0.0, 1.0);
    }

    private int recipeProcessTimeCalculator(final long recipeTime, final long recipeSpacetimeCasingRequired) {

        // Tier 1 recipe.
        // Tier 2 spacetime blocks.
        // = 3% discount.

        // Tier 1 recipe.
        // Tier 3 spacetime blocks.
        // = 3%*3% = 5.91% discount.

        final long spacetimeCasingDifference = (recipeSpacetimeCasingRequired - spacetimeCompressionFieldMetadata);
        final double recipeTimeDiscounted = recipeTime * GTUtility.powInt(2.0, -timeAccelerationFieldMetadata)
            * GTUtility.powInt(1 - SPACETIME_CASING_DIFFERENCE_DISCOUNT_PERCENTAGE, -spacetimeCasingDifference)
            * Math.min(1, GTUtility.powInt(2, -currentCircuitMultiplier));
        return (int) Math.max(recipeTimeDiscounted, 1.0);
    }

    @Override
    public IStructureDefinition<MTEEyeOfHarmony> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public MTEEyeOfHarmony(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEyeOfHarmony(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEyeOfHarmony(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {

        spacetimeCompressionFieldMetadata = -1;
        timeAccelerationFieldMetadata = -1;
        stabilisationFieldMetadata = -1;

        // Check structure of multi.
        if (!structureCheck_EM(STRUCTURE_PIECE_MAIN, 16, 16, 0)) {
            return false;
        }

        // Make sure there are no Crafting Input Buffers/Buses/Slaves.
        if (!mDualInputHatches.isEmpty()) {
            return false;
        }

        // Check if there is 1 output bus, and it is a ME output bus.
        {
            if (mOutputBusses.size() != 1) {
                return false;
            }

            if (!(mOutputBusses.get(0) instanceof MTEHatchOutputBusME)) {
                return false;
            }
        }

        // Check if there is 1 output hatch, and they are ME output hatches.
        {
            if (mOutputHatches.size() != 1) {
                return false;
            }

            if (!(mOutputHatches.get(0) instanceof MTEHatchOutputME)) {
                return false;
            }
        }

        // Check there is 1 input bus, and it is not a stocking input bus.
        {
            if (mInputBusses.size() != 1) {
                return false;
            }

            if (mInputBusses.get(0) instanceof MTEHatchInputBusME) {
                return false;
            }
        }

        // Make sure there are no energy hatches.
        {
            if (!mEnergyHatches.isEmpty()) {
                return false;
            }

            if (!mExoticEnergyHatches.isEmpty()) {
                return false;
            }
        }

        // Make sure there are 2 input hatches.
        return mInputHatches.size() == 2;
    }

    private boolean animationsEnabled = true;

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        animationsEnabled = !animationsEnabled;
        aPlayer.addChatMessage(
            new ChatComponentText("Animations are now " + (animationsEnabled ? "enabled" : "disabled") + "."));
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (astralArrayAmount != 0) {
            if (recipeRunning) {
                GTUtility.sendChatTrans(aPlayer, "eoh.rightclick.wirecutter.1");
            } else {
                long originalAmount = astralArrayAmount;
                while (astralArrayAmount >= 64) {
                    if (aPlayer.inventory.getFirstEmptyStack() != -1) {
                        aPlayer.inventory.addItemStackToInventory(CustomItemList.astralArrayFabricator.get(64));
                        astralArrayAmount -= 64;
                    } else {
                        break;
                    }
                }
                if (aPlayer.inventory.getFirstEmptyStack() != -1) {
                    aPlayer.inventory
                        .addItemStackToInventory(CustomItemList.astralArrayFabricator.get(astralArrayAmount));
                    astralArrayAmount = 0;
                }
                if (originalAmount - astralArrayAmount > 0) {
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        StatCollector.translateToLocalFormatted(
                            "eoh.rightclick.wirecutter.2",
                            GTUtility.formatNumbers(originalAmount - astralArrayAmount)));
                }
            }
        }
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (getControllerSlot() == null) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (GTUtility.getBlockFromStack(heldItem) instanceof BlockDimensionDisplay) {
                mInventory[getControllerSlotIndex()] = heldItem.copy();
                mInventory[getControllerSlotIndex()].stackSize = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Spacetime Manipulator, EOH")
            .addInfo("Creates a pocket of spacetime that is bigger on the inside using transdimensional")
            .addInfo("engineering. Certified Time Lord regulation compliant. This multi uses too much EU")
            .addInfo("to be handled with conventional means. All EU requirements are handled directly by")
            .addInfo("your wireless EU network")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo("This multiblock will constantly consume hydrogen and helium when it is not running a")
            .addInfo("recipe once per second. It will store this internally, you can see the totals by")
            .addInfo("using a scanner. This multi also has three tiered blocks with " + RED + 9 + GRAY + " tiers")
            .addInfo("each. They are as follows and have the associated effects on the multi:")
            .addInfo(BLUE + "Spacetime Compression Field Generator:")
            .addInfo("- The tier of this block determines what recipes can be run. If the multiblocks")
            .addInfo("  spacetime compression field block exceeds the requirements of the recipe it")
            .addInfo(
                "  will decrease the processing time by " + RED
                    + formatNumbers(SPACETIME_CASING_DIFFERENCE_DISCOUNT_PERCENTAGE * 100)
                    + "%"
                    + GRAY
                    + " per tier over the requirement (multiplicative)")
            .addInfo(BLUE + "Time Dilation Field Generator:")
            .addInfo(
                "- Decreases the time required for a recipe by " + RED
                    + "50%"
                    + GRAY
                    + " per tier of block (multiplicative)")
            .addInfo(
                "  Decreases the probability of a recipe succeeding by " + RED
                    + formatNumbers(TIME_ACCEL_DECREASE_CHANCE_PER_TIER * 100)
                    + "%"
                    + GRAY
                    + " per tier (additive)")
            .addInfo(BLUE + "Stabilisation Field Generator:")
            .addInfo(
                "- Increases the probability of a recipe succeeding by " + RED
                    + formatNumbers(STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * 100)
                    + "%"
                    + GRAY
                    + " per tier (additive)")
            .addInfo(
                "  Decreases the yield of a recipe by " + RED
                    + formatNumbers(STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * 100)
                    + "%"
                    + GRAY
                    + " per tier (additive). ")
            .addInfo("  > Low tier stabilisation field generators have a power output penalty")
            .addInfo(
                "     The power output penalty for using Crude Stabilisation Field Generators is " + RED
                    + formatNumbers(
                        STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * TOTAL_CASING_TIERS_WITH_POWER_PENALTY
                            * 100)
                    + "%")
            .addInfo(
                "     This penalty decreases by " + RED
                    + formatNumbers(STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * 100)
                    + "%"
                    + GRAY
                    + " per tier (additive)")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo("Going over a recipe requirement on hydrogen or helium has a penalty on yield and recipe chance")
            .addInfo("All stored hydrogen and helium is consumed during a craft. The associated formulas are:")
            .addInfo(GREEN + "Overflow ratio = (Stored fluid / Recipe requirement) - 1")
            .addInfo(GREEN + "Adjustment value = 1 - exp(-(30 * Overflow ratio)^2)")
            .addInfo("The Adjustment value is then subtracted from the total yield and recipe chance")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo("It should be noted that base recipe chance is determined per recipe and yield always starts")
            .addInfo("at 1 and subtracts depending on penalties. All fluid/item outputs are multiplied by the")
            .addInfo("yield. Failure fluid is exempt")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo("This multiblock can only output to ME output buses/hatches")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo("This multiblock can be overclocked by placing a programmed circuit into the input bus")
            .addInfo("E.g. A circuit of 2 will provide 2 OCs, 16x EU input and 0.25x the time. EU output is unaffected")
            .addInfo("All outputs are equal. All item and fluid output chances & amounts per recipe are unaffected")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo(
                "If a recipe fails the EOH will output " + GREEN
                    + "Success chance * "
                    + formatNumbers(MOLTEN_SPACETIME_PER_FAILURE_TIER)
                    + " * ("
                    + SPACETIME_FAILURE_BASE
                    + ")^(Recipe tier)"
                    + GRAY
                    + "L of molten")
            .addInfo(
                Materials.SpaceTime.getLocalizedNameForItem("%material")
                    + " instead of fluid/item outputs and output as much EU as a successful recipe")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo(
                "This multiblock can perform parallel processing by placing Astral Array Fabricators into the input bus")
            .addInfo(
                "They are stored internally and can be retrieved via right-clicking the controller with a wire cutter")
            .addInfo(
                "The maximum amount of stored Astral Arrays is " + formatNumbers(ASTRAL_ARRAY_LIMIT)
                    + ". Parallel amount is calculated via these formulas:")
            .addInfo(
                GREEN + "Parallel exponent = floor(log("
                    + formatNumbers(PARALLEL_FOR_FIRST_ASTRAL_ARRAY)
                    + " * Astral Array amount) / log("
                    + formatNumbers(CONSTANT_FOR_LOG)
                    + "))")
            .addInfo(GREEN + "Parallel = 2^(Parallel exponent)")
            .addInfo("If the EOH is running parallel recipes, the power calculation changes")
            .addInfo("The power needed for parallel processing is calculated as follows:")
            .addInfo(
                GREEN + "total EU = ((EU output - EU input * "
                    + formatNumbers(PARALLEL_MULTIPLIER_CONSTANT)
                    + ") / "
                    + formatNumbers(POWER_DIVISION_CONSTANT)
                    + ") * "
                    + formatNumbers(POWER_INCREASE_CONSTANT)
                    + "^(Parallel exponent)")
            .addInfo(
                "Furthermore, if parallel recipes are run, the recipes consume "
                    + Materials.RawStarMatter.getLocalizedNameForItem("%material"))
            .addInfo("instead of helium and hydrogen. Overflow penalties still apply")
            .addInfo(
                "The required amount of fluid to start a recipe is " + GREEN + "12.4 / 10^6 * Helium amount * Parallel")
            .addInfo("The success or failure of each parallel is determined independently")
            .addSeparator(EnumChatFormatting.GOLD, 87)
            .addInfo("Animations can be disabled by using a screwdriver on the multiblock")
            .addInfo("Planet block can be inserted directly by right-clicking the controller with planet block")
            .beginStructureBlock(33, 33, 33, false)
            .addStructureInfo(
                EnumChatFormatting.GOLD + "896" + EnumChatFormatting.GRAY + " Reinforced Spatial Structure Casing.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "534" + EnumChatFormatting.GRAY + " Reinforced Temporal Structure Casing.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "31"
                    + EnumChatFormatting.GRAY
                    + " Infinite SpaceTime Energy Boundary Casing.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "168" + EnumChatFormatting.GRAY + " Time Dilation Field Generator.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "48" + EnumChatFormatting.GRAY + " Stabilisation Field Generator.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "138" + EnumChatFormatting.GRAY + " Spacetime Compression Field Generator.")
            .addStructureInfoSeparator()
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 2 + EnumChatFormatting.GRAY + " input hatches.")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " ME output hatch.")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " input bus.")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " ME output bus.")
            .addSubChannelUsage(GTStructureChannels.EOH_STABILISATION)
            .addSubChannelUsage(GTStructureChannels.EOH_DILATION)
            .addSubChannelUsage(GTStructureChannels.EOH_COMPRESSION)
            .toolTipFinisher(EnumChatFormatting.GOLD, 87, GTValues.AuthorColen);
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_BHG_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][12],
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][12] };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 16, 16, 0, stackSize, hintsOnly);
    }

    private final Map<Fluid, Long> validFluidMap = new HashMap<>() {

        private static final long serialVersionUID = -8452610443191188130L;

        {
            put(Materials.Hydrogen.mGas, 0L);
            put(Materials.Helium.mGas, 0L);
            put(Materials.RawStarMatter.mFluid, 0L);
        }
    };

    private void drainFluidFromHatchesAndStoreInternally() {
        List<FluidStack> fluidStacks = getStoredFluids();
        for (FluidStack fluidStack : fluidStacks) {
            if (validFluidMap.containsKey(fluidStack.getFluid())) {
                validFluidMap.merge(fluidStack.getFluid(), (long) fluidStack.amount, Long::sum);
                fluidStack.amount = 0;
            }
        }
        updateSlots();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return TecTechRecipeMaps.eyeOfHarmonyRecipes;
    }

    private EyeOfHarmonyRecipe currentRecipe;

    // Counter for lag prevention.
    private long lagPreventer = 0;

    // Check for recipe every recipeCheckInterval ticks.
    private static final long RECIPE_CHECK_INTERVAL = 3 * 20;
    private long currentCircuitMultiplier = 0;
    private long astralArrayAmount = 0;
    private long parallelAmount = 1;
    private long successfulParallelAmount = 0;
    private double yield = 0;
    private BigInteger usedEU = BigInteger.ZERO;
    private FluidStackLong stellarPlasma;
    private FluidStackLong starMatter;

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        ItemStack controllerStack = getControllerSlot();
        if (controllerStack == null) {
            return SimpleCheckRecipeResult.ofFailure("no_planet_block");
        }

        lagPreventer++;
        if (lagPreventer < RECIPE_CHECK_INTERVAL) {
            lagPreventer = 0;
            // No item in multi gui slot.

            currentRecipe = TecTech.eyeOfHarmonyRecipeStorage.recipeLookUp(controllerStack);
            if (currentRecipe == null) {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
            CheckRecipeResult result = processRecipe(currentRecipe);
            if (!result.wasSuccessful()) currentRecipe = null;
            return result;
        }
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    private long getHydrogenStored() {
        return validFluidMap.get(Materials.Hydrogen.mGas);
    }

    private long getHeliumStored() {
        return validFluidMap.get(Materials.Helium.mGas);
    }

    private long getStellarPlasmaStored() {
        return validFluidMap.get(Materials.RawStarMatter.mFluid);
    }

    public CheckRecipeResult processRecipe(EyeOfHarmonyRecipe recipeObject) {

        // Get circuit damage, clamp it and then use it later for overclocking.
        currentCircuitMultiplier = 0;
        for (ItemStack itemStack : mInputBusses.get(0)
            .getRealInventory()) {
            if (GTUtility.isAnyIntegratedCircuit(itemStack)) {
                currentCircuitMultiplier = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 24);
                break;
            }
        }

        for (ItemStack itemStack : mInputBusses.get(0)
            .getRealInventory()) {
            if (astralArrayAmount >= ASTRAL_ARRAY_LIMIT) break;
            if (itemStack != null && itemStack.isItemEqual(CustomItemList.astralArrayFabricator.get(1))) {
                long insertAmount = Math.min(itemStack.stackSize, ASTRAL_ARRAY_LIMIT - astralArrayAmount);
                astralArrayAmount += insertAmount;
                itemStack.stackSize -= insertAmount;
            }
        }

        long parallelExponent = 1;

        if (astralArrayAmount != 0) {
            parallelExponent = (long) Math.floor(
                Math.log(PARALLEL_FOR_FIRST_ASTRAL_ARRAY * Math.min(astralArrayAmount, ASTRAL_ARRAY_LIMIT))
                    / LOG_CONSTANT);
            parallelAmount = (long) GTUtility.powInt(2, parallelExponent);
        } else {
            parallelAmount = 1;
        }

        // Debug mode, overwrites the required fluids to initiate the recipe to 100L of each.
        if (parallelAmount > 1) {
            if ((EOH_DEBUG_MODE && getStellarPlasmaStored() < 100) || (!EOH_DEBUG_MODE && getStellarPlasmaStored()
                < currentRecipe.getHeliumRequirement() * (12.4 / 1_000_000f) * parallelAmount)) {
                return SimpleCheckRecipeResult.ofFailure("no_stellar_plasma");
            }
        }

        if (parallelAmount == 1) {
            if ((EOH_DEBUG_MODE && getHydrogenStored() < 100)
                || (!EOH_DEBUG_MODE && getHydrogenStored() < currentRecipe.getHydrogenRequirement())) {
                return SimpleCheckRecipeResult.ofFailure("no_hydrogen");
            }

            if ((EOH_DEBUG_MODE && getHeliumStored() < 100)
                || (!EOH_DEBUG_MODE && getHeliumStored() < currentRecipe.getHeliumRequirement())) {
                return SimpleCheckRecipeResult.ofFailure("no_helium");
            }
        }

        if (spacetimeCompressionFieldMetadata == -1) {
            return CheckRecipeResultRegistry
                .insufficientMachineTier((int) recipeObject.getSpacetimeCasingTierRequired());
        }

        // Check tier of spacetime compression blocks is high enough.
        if (spacetimeCompressionFieldMetadata < recipeObject.getSpacetimeCasingTierRequired()) {
            return CheckRecipeResultRegistry
                .insufficientMachineTier((int) recipeObject.getSpacetimeCasingTierRequired());
        }

        // Calculate multipliers used in power calculations
        double powerMultiplier = Math.max(1, GTUtility.powInt(POWER_INCREASE_CONSTANT, parallelExponent));

        // Determine EU recipe input
        startEU = recipeObject.getEUStartCost();

        // Calculate normal EU values
        double outputEUPenalty = (TOTAL_CASING_TIERS_WITH_POWER_PENALTY - stabilisationFieldMetadata)
            * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;
        outputEU_BigInt = BigInteger.valueOf((long) (recipeObject.getEUOutput() * (1 - outputEUPenalty)));
        usedEU = BigInteger.valueOf(-startEU)
            .multiply(BigInteger.valueOf((long) GTUtility.powInt(4, currentCircuitMultiplier)));

        // Calculate parallel EU values
        if (parallelAmount > 1) {
            outputEU_BigInt = outputEU_BigInt
                .multiply(BigInteger.valueOf((long) (powerMultiplier * PRECISION_MULTIPLIER)))
                .divide(BigInteger.valueOf((long) (PRECISION_MULTIPLIER * POWER_DIVISION_CONSTANT)));

            usedEU = usedEU
                .multiply(
                    BigInteger.valueOf((long) (powerMultiplier * PARALLEL_MULTIPLIER_CONSTANT * PRECISION_MULTIPLIER)))
                .divide(BigInteger.valueOf((long) (PRECISION_MULTIPLIER * POWER_DIVISION_CONSTANT)));
        }

        // Remove EU from the users network.
        if (!addEUToGlobalEnergyMap(userUUID, usedEU)) {
            return CheckRecipeResultRegistry.insufficientStartupPower(usedEU.abs());
        }

        mMaxProgresstime = recipeProcessTimeCalculator(
            recipeObject.getRecipeTimeInTicks(),
            recipeObject.getSpacetimeCasingTierRequired());

        calculateInputFluidExcessValues(recipeObject.getHydrogenRequirement(), recipeObject.getHeliumRequirement());

        if (EOH_DEBUG_MODE) {
            hydrogenOverflowProbabilityAdjustment = 0;
            heliumOverflowProbabilityAdjustment = 0;
            stellarPlasmaOverflowProbabilityAdjustment = 0;
        }

        // If pityChance needs to be reset, it will be set to Double.MIN_Value at the end of the recipe.
        if (pityChance == Double.MIN_VALUE) {
            pityChance = currentRecipe.getBaseRecipeSuccessChance()
                - timeAccelerationFieldMetadata * TIME_ACCEL_DECREASE_CHANCE_PER_TIER
                + stabilisationFieldMetadata * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;
        }

        previousRecipeChance = successChance;
        successChance = recipeChanceCalculator();
        currentRecipeRocketTier = currentRecipe.getRocketTier();

        // Reduce internal storage by input fluid quantity required for recipe.
        if (parallelAmount > 1) {
            validFluidMap.put(Materials.RawStarMatter.mFluid, 0L);
        } else {
            validFluidMap.put(Materials.Hydrogen.mGas, 0L);
            validFluidMap.put(Materials.Helium.mGas, 0L);
        }

        yield = recipeYieldCalculator();
        if (EOH_DEBUG_MODE) {
            successChance = 1; // Debug recipes, sets them to 100% output chance.
        }

        // Return copies of the output objects.
        outputFluids = recipeObject.getOutputFluids();
        outputItems = recipeObject.getOutputItems();

        // Star matter is always the last element in the array.
        starMatter = new FluidStackLong(outputFluids.get(outputFluids.size() - 1));

        // And stellar plasma is the second last.
        stellarPlasma = new FluidStackLong(outputFluids.get(outputFluids.size() - 2));

        successfulParallelAmount = calculateIntegralChancedOutputMultiplier(
            (int) (10000 * successChance),
            (int) parallelAmount);
        // Iterate over item output list and apply yield & successful parallel values.
        for (ItemStackLong itemStackLong : outputItems) {
            itemStackLong.stackSize *= yield * successfulParallelAmount;
        }

        // Iterate over fluid output list and apply yield & successful parallel values.
        for (FluidStackLong fluidStackLong : outputFluids) {
            fluidStackLong.amount *= yield * successfulParallelAmount;
        }

        updateSlots();

        if (animationsEnabled) {
            createRenderBlock(currentRecipe);
        }

        recipeRunning = true;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void createRenderBlock(final EyeOfHarmonyRecipe currentRecipe) {

        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock(
                (int) (x + xOffset),
                (int) (y + yOffset),
                (int) (z + zOffset),
                TTCasingsContainer.eyeOfHarmonyRenderBlock);
        TileEntityEyeOfHarmony rendererTileEntity = (TileEntityEyeOfHarmony) this.getBaseMetaTileEntity()
            .getWorld()
            .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));

        rendererTileEntity.setTier(currentRecipe.getRocketTier());

        int recipeSpacetimeTier = (int) currentRecipe.getSpacetimeCasingTierRequired();

        // Star is a larger size depending on the spacetime tier of the recipe.
        rendererTileEntity.setSize((1 + recipeSpacetimeTier));

        // Star rotates faster the higher tier time dilation you use in the multi.
        // Lower value = faster rotation speed.
        rendererTileEntity.setRotationSpeed((1 + timeAccelerationFieldMetadata) / 2.0f);
    }

    private double successChance;
    private double pityChance;
    private double previousRecipeChance;
    private long currentRecipeRocketTier;

    private void outputFailedChance() {
        long failedParallelAmount = parallelAmount - successfulParallelAmount;
        if (failedParallelAmount > 0) {
            // 2^Tier spacetime released upon recipe failure.
            outputFluidToAENetwork(
                Materials.SpaceTime.getMolten(1),
                (long) ((successChance * MOLTEN_SPACETIME_PER_FAILURE_TIER
                    * GTUtility.powInt(SPACETIME_FAILURE_BASE, currentRecipeRocketTier + 1)) * failedParallelAmount));
            if (parallelAmount == 1) {
                // Add chance to pity if previous recipe is equal to current one, else reset pity
                if (previousRecipeChance == successChance) {
                    pityChance += (1 - successChance) * successChance;
                } else {
                    pityChance = successChance;
                }
            }
        } else if (parallelAmount == 1) {
            // Recipe succeeded, reset pity
            // Set to Double.MIN_VALUE here and actually reset this when the recipe starts.
            pityChance = Double.MIN_VALUE;
        }
        super.outputAfterRecipe_EM();
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        destroyRenderBlock();
        recipeRunning = false;
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        destroyRenderBlock();
    }

    private void destroyRenderBlock() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        this.getBaseMetaTileEntity()
            .getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
    }

    public void outputAfterRecipe_EM() {
        recipeRunning = false;
        eRequiredData = 0L;

        destroyRenderBlock();

        // Output EU
        addEUToGlobalEnergyMap(userUUID, outputEU_BigInt);

        startEU = 0;
        outputEU_BigInt = BigInteger.ZERO;

        outputFailedChance();

        if (successfulParallelAmount > 0) {
            for (ItemStackLong itemStack : outputItems) {
                outputItemToAENetwork(itemStack.itemStack, itemStack.stackSize);
            }

            for (FluidStackLong fluidStack : outputFluids) {
                outputFluidToAENetwork(fluidStack.fluidStack, fluidStack.amount);
            }
        }

        // Clear the array list for new recipes.
        outputItems = new ArrayList<>();
        outputFluids = new ArrayList<>();

        // Do other stuff from TT superclasses. E.g. outputting fluids.
        super.outputAfterRecipe_EM();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aTick == 1) {
            userUUID = getBaseMetaTileEntity().getOwnerUuid();
            strongCheckOrAddUser(userUUID);
        }

        if (!recipeRunning && mMachine) {
            if ((aTick % TICKS_BETWEEN_HATCH_DRAIN) == 0) {
                drainFluidFromHatchesAndStoreInternally();
            }
        }
    }

    private boolean recipeRunning = false;

    private void outputItemToAENetwork(ItemStack item, long amount) {
        if (item == null || amount <= 0) return;

        while (amount >= Integer.MAX_VALUE) {
            ItemStack tmpItem = item.copy();
            tmpItem.stackSize = Integer.MAX_VALUE;
            mOutputBusses.get(0)
                .storePartial(tmpItem);
            amount -= Integer.MAX_VALUE;
        }
        ItemStack tmpItem = item.copy();
        tmpItem.stackSize = (int) amount;
        mOutputBusses.get(0)
            .storePartial(tmpItem);
    }

    private void outputFluidToAENetwork(FluidStack fluid, long amount) {
        if (fluid == null || amount <= 0) return;

        while (amount >= Integer.MAX_VALUE) {
            FluidStack tmpFluid = fluid.copy();
            tmpFluid.amount = Integer.MAX_VALUE;
            ((MTEHatchOutputME) mOutputHatches.get(0)).tryFillAE(tmpFluid);
            amount -= Integer.MAX_VALUE;
        }
        FluidStack tmpFluid = fluid.copy();
        tmpFluid.amount = (int) amount;
        ((MTEHatchOutputME) mOutputHatches.get(0)).tryFillAE(tmpFluid);
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            GOLD.toString() + STRIKETHROUGH
                + "-------------"
                + RESET
                + GOLD
                + " "
                + StatCollector.translateToLocal("tt.infodata.eoh.control_block_statistics")
                + " "
                + STRIKETHROUGH
                + "-------------");
        if (spacetimeCompressionFieldMetadata < 0) {
            info.add(StatCollector.translateToLocal("tt.infodata.eoh.spacetime_compression.grade.none"));
        } else {
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.spacetime_compression.grade",
                    CommonValues.getLocalizedEohTierFancyNames(spacetimeCompressionFieldMetadata) + RESET,
                    "" + YELLOW + (spacetimeCompressionFieldMetadata + 1) + RESET));
        }
        if (timeAccelerationFieldMetadata < 0) {
            info.add(StatCollector.translateToLocal("tt.infodata.eoh.time_dilation.grade.none"));
        } else {
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.time_dilation.grade",
                    CommonValues.getLocalizedEohTierFancyNames(timeAccelerationFieldMetadata) + RESET,
                    "" + YELLOW + (timeAccelerationFieldMetadata + 1) + RESET));
        }
        if (stabilisationFieldMetadata < 0) {
            info.add(StatCollector.translateToLocal("tt.infodata.eoh.stabilisation.grade.none"));
        } else {
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.stabilisation.grade",
                    CommonValues.getLocalizedEohTierFancyNames(stabilisationFieldMetadata) + RESET,
                    "" + YELLOW + (stabilisationFieldMetadata + 1) + RESET));
        }
        info.add(
            GOLD.toString() + STRIKETHROUGH
                + "-----------------"
                + RESET
                + GOLD
                + " "
                + StatCollector.translateToLocal("tt.infodata.eoh.internal_storage")
                + " "
                + STRIKETHROUGH
                + "----------------");
        validFluidMap.forEach(
            (key, value) -> info.add(BLUE + key.getLocalizedName() + RESET + " : " + RED + formatNumbers(value)));
        info.add(
            BLUE + StatCollector.translateToLocal(
                "tt.infodata.eoh.astral_array_fabricators") + RESET + " : " + RED + formatNumbers(astralArrayAmount));
        if (recipeRunning) {
            info.add(
                GOLD.toString() + STRIKETHROUGH
                    + "-----------------"
                    + RESET
                    + GOLD
                    + " "
                    + StatCollector.translateToLocal("tt.infodata.eoh.other_stats")
                    + " "
                    + STRIKETHROUGH
                    + "-----------------");
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.success_chance",
                    RED + formatNumbers(100 * successChance) + RESET + "%"));
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.recipe_yield",
                    RED + formatNumbers(100 * yield) + RESET + "%"));
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.effective_astral_array_fabricators",
                    RED + formatNumbers(Math.min(astralArrayAmount, ASTRAL_ARRAY_LIMIT))));
            info.add(
                StatCollector
                    .translateToLocalFormatted("tt.infodata.eoh.total_parallel", RED + formatNumbers(parallelAmount)));
            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.eu_output",
                    RED + toStandardForm(outputEU_BigInt) + RESET));
            info.add(
                StatCollector
                    .translateToLocalFormatted("tt.infodata.eoh.eu_input", RED + toStandardForm(usedEU.abs()) + RESET));
            int currentMaxProgresstime = Math.max(maxProgresstime(), 1);
            if (starMatter != null && starMatter.fluidStack != null) {
                FluidStackLong starMatterOutput = new FluidStackLong(
                    starMatter.fluidStack,
                    (long) (starMatter.amount * yield * successChance * parallelAmount));
                info.add(
                    StatCollector.translateToLocalFormatted(
                        "tt.infodata.eoh.avg_output",
                        starMatterOutput.fluidStack.getLocalizedName(),
                        RED + formatNumbers(starMatterOutput.amount) + RESET,
                        YELLOW + formatNumbers(starMatterOutput.amount * 20.0 / currentMaxProgresstime) + RESET));

                FluidStackLong stellarPlasmaOutput = new FluidStackLong(
                    Materials.RawStarMatter.getFluid(0),
                    (long) (stellarPlasma.amount * yield * successChance * parallelAmount));
                info.add(
                    StatCollector.translateToLocalFormatted(
                        "tt.infodata.eoh.avg_output",
                        stellarPlasmaOutput.fluidStack.getLocalizedName(),
                        RED + formatNumbers(stellarPlasmaOutput.amount) + RESET,
                        YELLOW + formatNumbers(stellarPlasmaOutput.amount * 20.0 / currentMaxProgresstime) + RESET));
            }
            BigInteger euPerTick = (outputEU_BigInt.subtract(usedEU.abs()))
                .divide(BigInteger.valueOf(currentMaxProgresstime));

            info.add(
                StatCollector.translateToLocalFormatted(
                    "tt.infodata.eoh.estimated_eu",
                    RED + toStandardForm(euPerTick) + RESET));
        }
        info.add(GOLD.toString() + STRIKETHROUGH + "-----------------------------------------------------");
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] { "Eye of Harmony multiblock" };
    }

    // NBT save/load strings.
    private static final String EYE_OF_HARMONY = "eyeOfHarmonyOutput";
    private static final String NUMBER_OF_ITEMS_NBT_TAG = EYE_OF_HARMONY + "numberOfItems";
    private static final String NUMBER_OF_FLUIDS_NBT_TAG = EYE_OF_HARMONY + "numberOfFluids";
    private static final String ITEM_OUTPUT_NBT_TAG = EYE_OF_HARMONY + "itemOutput";
    private static final String FLUID_OUTPUT_NBT_TAG = EYE_OF_HARMONY + "fluidOutput";
    private static final String RECIPE_RUNNING_NBT_TAG = EYE_OF_HARMONY + "recipeRunning";
    private static final String CURRENT_RECIPE_STAR_MATTER_TAG = EYE_OF_HARMONY + "recipeStarMatter";
    private static final String CURRENT_RECIPE_STELLAR_PLASMA_TAG = EYE_OF_HARMONY + "recipeStellarPlasma";
    private static final String CURRENT_RECIPE_FIXED_OUTPUTS_TAG = EYE_OF_HARMONY + "recipeFixedOutputs";
    private static final String RECIPE_SUCCESS_CHANCE_NBT_TAG = EYE_OF_HARMONY + "recipeSuccessChance";
    private static final String ROCKET_TIER_NBT_TAG = EYE_OF_HARMONY + "rocketTier";
    private static final String CURRENT_CIRCUIT_MULTIPLIER_TAG = EYE_OF_HARMONY + "currentCircuitMultiplier";
    private static final String ANIMATIONS_ENABLED = EYE_OF_HARMONY + "animationsEnabled";
    private static final String CALCULATED_EU_OUTPUT_NBT_TAG = EYE_OF_HARMONY + "outputEU_BigInt";
    private static final String PARALLEL_AMOUNT_NBT_TAG = EYE_OF_HARMONY + "parallelAmount";
    private static final String YIELD_NBT_TAG = EYE_OF_HARMONY + "yield";
    private static final String SUCCESSFUL_PARALLEL_AMOUNT_NBT_TAG = EYE_OF_HARMONY + "successfulParallelAmount";
    private static final String ASTRAL_ARRAY_AMOUNT_NBT_TAG = EYE_OF_HARMONY + "astralArrayAmount";
    private static final String CALCULATED_EU_INPUT_NBT_TAG = EYE_OF_HARMONY + "usedEU";
    private static final String EXTRA_PITY_CHANCE_BOOST_NBT_TAG = EYE_OF_HARMONY + "pityChance";
    private static final String PREVIOUS_RECIPE_CHANCE_NBT_TAG = EYE_OF_HARMONY + "previousChance";

    // Sub tags, less specific names required.
    private static final String STACK_SIZE = "stackSize";
    private static final String ITEM_STACK_NBT_TAG = "itemStack";
    private static final String FLUID_AMOUNT = "fluidAmount";
    private static final String FLUID_STACK_NBT_TAG = "fluidStack";

    // Tags for pre-setting
    public static final String PLANET_BLOCK = "planetBlock";

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        super.initDefaultModes(aNBT);
        if (aNBT != null && aNBT.hasKey(PLANET_BLOCK) && getControllerSlot() == null) {
            mInventory[getControllerSlotIndex()] = new ItemStack(ModBlocks.getBlock(aNBT.getString(PLANET_BLOCK)));
            aNBT.removeTag(PLANET_BLOCK);
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey(PLANET_BLOCK)) {
                tooltip.add(
                    1,
                    GTLanguageManager.addStringLocalization("EOH_Controller_PlanetBlock", "Current Planet Block: ")
                        + AQUA
                        + new ItemStack(ModBlocks.getBlock(nbt.getString(PLANET_BLOCK))).getDisplayName());
            }
            if (nbt.getLong(ASTRAL_ARRAY_AMOUNT_NBT_TAG) > 0) {
                tooltip.add(
                    1,
                    GTLanguageManager
                        .addStringLocalization("EOH_Controller_AstralArrayAmount", "Stored Astral Arrays: ") + AQUA
                        + formatNumbers(nbt.getLong(ASTRAL_ARRAY_AMOUNT_NBT_TAG)));
            }
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound NBT) {
        if (astralArrayAmount > 0) NBT.setLong(ASTRAL_ARRAY_AMOUNT_NBT_TAG, astralArrayAmount);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        // Save the quantity of fluid stored inside the controller.
        validFluidMap.forEach((key, value) -> aNBT.setLong("stored." + key.getUnlocalizedName(), value));

        aNBT.setBoolean(RECIPE_RUNNING_NBT_TAG, recipeRunning);
        aNBT.setDouble(RECIPE_SUCCESS_CHANCE_NBT_TAG, successChance);
        aNBT.setLong(ROCKET_TIER_NBT_TAG, currentRecipeRocketTier);
        aNBT.setLong(CURRENT_CIRCUIT_MULTIPLIER_TAG, currentCircuitMultiplier);
        aNBT.setBoolean(ANIMATIONS_ENABLED, animationsEnabled);
        aNBT.setLong(PARALLEL_AMOUNT_NBT_TAG, parallelAmount);
        aNBT.setLong(SUCCESSFUL_PARALLEL_AMOUNT_NBT_TAG, successfulParallelAmount);
        aNBT.setDouble(YIELD_NBT_TAG, yield);
        aNBT.setLong(ASTRAL_ARRAY_AMOUNT_NBT_TAG, astralArrayAmount);
        aNBT.setByteArray(CALCULATED_EU_OUTPUT_NBT_TAG, outputEU_BigInt.toByteArray());
        aNBT.setByteArray(CALCULATED_EU_INPUT_NBT_TAG, usedEU.toByteArray());
        aNBT.setDouble(EXTRA_PITY_CHANCE_BOOST_NBT_TAG, pityChance);
        aNBT.setDouble(PREVIOUS_RECIPE_CHANCE_NBT_TAG, previousRecipeChance);

        // Store damage values/stack sizes of GT items being outputted.
        NBTTagCompound itemStackListNBTTag = new NBTTagCompound();
        itemStackListNBTTag.setLong(NUMBER_OF_ITEMS_NBT_TAG, outputItems.size());

        int index = 0;
        for (ItemStackLong itemStackLong : outputItems) {
            // Save stack size to NBT.
            itemStackListNBTTag.setLong(index + STACK_SIZE, itemStackLong.stackSize);

            // Save ItemStack to NBT.
            aNBT.setTag(index + ITEM_STACK_NBT_TAG, itemStackLong.itemStack.writeToNBT(new NBTTagCompound()));

            index++;
        }

        aNBT.setTag(ITEM_OUTPUT_NBT_TAG, itemStackListNBTTag);

        // Store damage values/stack sizes of GT fluids being outputted.
        NBTTagCompound fluidStackListNBTTag = new NBTTagCompound();
        fluidStackListNBTTag.setLong(NUMBER_OF_FLUIDS_NBT_TAG, outputFluids.size());

        int indexFluids = 0;
        for (FluidStackLong fluidStackLong : outputFluids) {
            // Save fluid amount to NBT.
            fluidStackListNBTTag.setLong(indexFluids + FLUID_AMOUNT, fluidStackLong.amount);

            // Save FluidStack to NBT.
            aNBT.setTag(indexFluids + FLUID_STACK_NBT_TAG, fluidStackLong.fluidStack.writeToNBT(new NBTTagCompound()));

            indexFluids++;
        }

        aNBT.setTag(FLUID_OUTPUT_NBT_TAG, fluidStackListNBTTag);

        if (starMatter != null && starMatter.fluidStack != null) {

            NBTTagCompound fixedRecipeOutputs = new NBTTagCompound();

            fixedRecipeOutputs.setLong(0 + FLUID_AMOUNT, starMatter.amount);
            aNBT.setTag(CURRENT_RECIPE_STAR_MATTER_TAG, starMatter.fluidStack.writeToNBT(new NBTTagCompound()));

            fixedRecipeOutputs.setLong(1 + FLUID_AMOUNT, stellarPlasma.amount);
            aNBT.setTag(CURRENT_RECIPE_STELLAR_PLASMA_TAG, stellarPlasma.fluidStack.writeToNBT(new NBTTagCompound()));

            aNBT.setTag(CURRENT_RECIPE_FIXED_OUTPUTS_TAG, fixedRecipeOutputs);
        }

        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {

        // Load the quantity of fluid stored inside the controller.
        validFluidMap
            .forEach((key, value) -> validFluidMap.put(key, aNBT.getLong("stored." + key.getUnlocalizedName())));

        // Load other stuff from NBT.
        recipeRunning = aNBT.getBoolean(RECIPE_RUNNING_NBT_TAG);
        successChance = aNBT.getDouble(RECIPE_SUCCESS_CHANCE_NBT_TAG);
        currentRecipeRocketTier = aNBT.getLong(ROCKET_TIER_NBT_TAG);
        currentCircuitMultiplier = aNBT.getLong(CURRENT_CIRCUIT_MULTIPLIER_TAG);
        if (aNBT.hasKey(ANIMATIONS_ENABLED)) animationsEnabled = aNBT.getBoolean(ANIMATIONS_ENABLED);
        parallelAmount = aNBT.getLong(PARALLEL_AMOUNT_NBT_TAG);
        yield = aNBT.getDouble(YIELD_NBT_TAG);
        successfulParallelAmount = aNBT.getLong(SUCCESSFUL_PARALLEL_AMOUNT_NBT_TAG);
        astralArrayAmount = aNBT.getLong(ASTRAL_ARRAY_AMOUNT_NBT_TAG);
        if (aNBT.hasKey(CALCULATED_EU_OUTPUT_NBT_TAG))
            outputEU_BigInt = new BigInteger(aNBT.getByteArray(CALCULATED_EU_OUTPUT_NBT_TAG));
        if (aNBT.hasKey(CALCULATED_EU_INPUT_NBT_TAG))
            usedEU = new BigInteger(aNBT.getByteArray(CALCULATED_EU_INPUT_NBT_TAG));
        pityChance = aNBT.getDouble(EXTRA_PITY_CHANCE_BOOST_NBT_TAG);
        previousRecipeChance = aNBT.getDouble(PREVIOUS_RECIPE_CHANCE_NBT_TAG);

        // Load damage values/stack sizes of GT items being outputted and convert back to items.
        NBTTagCompound tempItemTag = aNBT.getCompoundTag(ITEM_OUTPUT_NBT_TAG);

        // Iterate over all stored items.
        for (int index = 0; index < tempItemTag.getInteger(NUMBER_OF_ITEMS_NBT_TAG); index++) {

            // Load stack size from NBT.
            long stackSize = tempItemTag.getLong(index + STACK_SIZE);

            // Load ItemStack from NBT.
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(index + ITEM_STACK_NBT_TAG));

            outputItems.add(new ItemStackLong(itemStack, stackSize));
        }

        // Load damage values/fluid amounts of GT fluids being outputted and convert back to fluids.
        NBTTagCompound tempFluidTag = aNBT.getCompoundTag(FLUID_OUTPUT_NBT_TAG);

        // Iterate over all stored fluids.
        for (int indexFluids = 0; indexFluids < tempFluidTag.getInteger(NUMBER_OF_FLUIDS_NBT_TAG); indexFluids++) {

            // Load fluid amount from NBT.
            long fluidAmount = tempFluidTag.getLong(indexFluids + FLUID_AMOUNT);

            // Load FluidStack from NBT.
            FluidStack fluidStack = FluidStack
                .loadFluidStackFromNBT(aNBT.getCompoundTag(indexFluids + FLUID_STACK_NBT_TAG));

            outputFluids.add(new FluidStackLong(fluidStack, fluidAmount));
        }

        tempFluidTag = aNBT.getCompoundTag(CURRENT_RECIPE_FIXED_OUTPUTS_TAG);
        starMatter = new FluidStackLong(
            FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag(CURRENT_RECIPE_STAR_MATTER_TAG)),
            tempFluidTag.getLong(0 + FLUID_AMOUNT));
        stellarPlasma = new FluidStackLong(
            FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag(CURRENT_RECIPE_STELLAR_PLASMA_TAG)),
            tempFluidTag.getLong(1 + FLUID_AMOUNT));

        super.loadNBTData(aNBT);
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_EYE_OF_HARMONY_LOOP;
    }
}
