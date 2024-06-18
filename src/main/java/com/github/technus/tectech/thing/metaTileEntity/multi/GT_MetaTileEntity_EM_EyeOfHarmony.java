package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.TecTech.eyeOfHarmonyRecipeStorage;
import static com.github.technus.tectech.thing.CustomItemList.astralArrayFabricator;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.eyeOfHarmonyRenderBlock;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.github.technus.tectech.util.CommonValues.EOH_TIER_FANCY_NAMES;
import static com.github.technus.tectech.util.TT_Utility.toStandardForm;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.util.GT_ParallelHelper.calculateChancedOutputMultiplier;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static java.lang.Math.exp;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static net.minecraft.util.EnumChatFormatting.BLUE;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.recipe.EyeOfHarmonyRecipe;
import com.github.technus.tectech.recipe.TecTechRecipeMaps;
import com.github.technus.tectech.thing.block.TileEyeOfHarmony;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.FluidStackLong;
import com.github.technus.tectech.util.ItemStackLong;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;

@SuppressWarnings("SpellCheckingInspection")
public class GT_MetaTileEntity_EM_EyeOfHarmony extends GT_MetaTileEntity_MultiblockBase_EM
    implements IConstructable, IGlobalWirelessEnergy, ISurvivalConstructable {

    public static final boolean EOH_DEBUG_MODE = false;
    private static final long MOLTEN_SPACETIME_PER_FAILURE_TIER = 14_400L;
    private static final double SPACETIME_FAILURE_BASE = 2;
    private static final String TOOLTIP_BAR = GOLD
        + "---------------------------------------------------------------------------------------";

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
    private static final double LOG_CONSTANT = Math.log(1.7);
    private static final double PARALLEL_MULTIPLIER_CONSTANT = 1.63;
    private static final long POWER_DIVISION_CONSTANT = 9;
    private static final int TOTAL_CASING_TIERS_WITH_POWER_PENALTY = 8;
    private static final long PRECISION_MULTIPLIER = 1_000_000;

    private String userUUID = "";
    private BigInteger outputEU_BigInt = BigInteger.ZERO;
    private long startEU = 0;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5); // 200 blocks max per
                                                                                                  // placement.
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 16, 16, 0, realBudget, source, actor, false, true);
    }

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    // Multiblock structure.
    private static final IStructureDefinition<GT_MetaTileEntity_EM_EyeOfHarmony> STRUCTURE_DEFINITION = IStructureDefinition
        .<GT_MetaTileEntity_EM_EyeOfHarmony>builder()
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
            withChannel(
                "spacetime compression",
                ofBlocksTiered(
                    (block, meta) -> block == TT_Container_Casings.SpacetimeCompressionFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 0),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 1),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 2),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 3),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 4),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 5),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 6),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 7),
                        Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.spacetimeCompressionFieldMetadata = meta,
                    t -> t.spacetimeCompressionFieldMetadata)))
        .addElement(
            'S',
            withChannel(
                "stabilisation",
                ofBlocksTiered(
                    (block, meta) -> block == TT_Container_Casings.StabilisationFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 0),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 1),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 2),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 3),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 4),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 5),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 6),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 7),
                        Pair.of(TT_Container_Casings.StabilisationFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.stabilisationFieldMetadata = meta,
                    t -> t.stabilisationFieldMetadata)))
        .addElement('C', ofBlock(sBlockCasingsBA0, 11))
        .addElement('D', ofBlock(sBlockCasingsBA0, 10))
        .addElement(
            'H',
            buildHatchAdder(GT_MetaTileEntity_EM_EyeOfHarmony.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus)
                .casingIndex(texturePage << 7)
                .dot(1)
                .buildAndChain(sBlockCasingsBA0, 12))
        .addElement(
            'E',
            withChannel(
                "time dilation",
                ofBlocksTiered(
                    (block, meta) -> block == TT_Container_Casings.TimeAccelerationFieldGenerator ? meta : null,
                    ImmutableList.of(
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 0),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 1),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 2),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 3),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 4),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 5),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 6),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 7),
                        Pair.of(TT_Container_Casings.TimeAccelerationFieldGenerator, 8)),
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

        hydrogenOverflowProbabilityAdjustment = 1 - exp(-pow(30 * hydrogenExcessPercentage, 2));
        heliumOverflowProbabilityAdjustment = 1 - exp(-pow(30 * heliumExcessPercentage, 2));
        stellarPlasmaOverflowProbabilityAdjustment = 1 - exp(-pow(30 * stellarPlasmaExcessPercentage, 2));
    }

    private double recipeChanceCalculator() {
        double chance = currentRecipe.getBaseRecipeSuccessChance()
            - timeAccelerationFieldMetadata * TIME_ACCEL_DECREASE_CHANCE_PER_TIER
            + stabilisationFieldMetadata * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;

        if (parallelAmount > 1) {
            chance -= stellarPlasmaOverflowProbabilityAdjustment;
        } else {
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
        final double recipeTimeDiscounted = recipeTime * pow(2.0, -timeAccelerationFieldMetadata)
            * pow(1 - SPACETIME_CASING_DIFFERENCE_DISCOUNT_PERCENTAGE, -spacetimeCasingDifference)
            / max(1, pow(2, currentCircuitMultiplier));
        return (int) Math.max(recipeTimeDiscounted, 1.0);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_EyeOfHarmony> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public GT_MetaTileEntity_EM_EyeOfHarmony(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_EyeOfHarmony(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_EyeOfHarmony(mName);
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

            if (!(mOutputBusses.get(0) instanceof GT_MetaTileEntity_Hatch_OutputBus_ME)) {
                return false;
            }
        }

        // Check if there is 1 output hatch, and they are ME output hatches.
        {
            if (mOutputHatches.size() != 1) {
                return false;
            }

            if (!(mOutputHatches.get(0) instanceof GT_MetaTileEntity_Hatch_Output_ME)) {
                return false;
            }
        }

        // Check there is 1 input bus, and it is not a stocking input bus.
        {
            if (mInputBusses.size() != 1) {
                return false;
            }

            if (mInputBusses.get(0) instanceof GT_MetaTileEntity_Hatch_InputBus_ME) {
                return false;
            }
        }

        // Make sure there are no energy hatches.
        {
            if (mEnergyHatches.size() > 0) {
                return false;
            }

            if (mExoticEnergyHatches.size() > 0) {
                return false;
            }
        }

        // Make sure there are 2 input hatches.
        if (mInputHatches.size() != 2) {
            return false;
        }

        return true;
    }

    private boolean animationsEnabled = true;

    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        animationsEnabled = !animationsEnabled;
        aPlayer.addChatMessage(
            new ChatComponentText("Animations are now " + (animationsEnabled ? "enabled" : "disabled") + "."));
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Spacetime Manipulator, EOH")
            .addInfo(TOOLTIP_BAR)
            .addInfo("Creates a pocket of spacetime that is bigger on the inside using transdimensional")
            .addInfo("engineering. Certified Time Lord regulation compliant. This multi uses too much EU")
            .addInfo("to be handled with conventional means. All EU requirements are handled directly by")
            .addInfo("your wireless EU network.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("This multiblock will constantly consume hydrogen and helium when it is not running a")
            .addInfo("recipe once per second. It will store this internally, you can see the totals by")
            .addInfo("using a scanner. This multi also has three tiered blocks with " + RED + 9 + GRAY + " tiers")
            .addInfo("each. They are as follows and have the associated effects on the multi.")
            .addInfo(BLUE + "Spacetime Compression Field Generator:")
            .addInfo("- The tier of this block determines what recipes can be run. If the multiblocks")
            .addInfo("  spacetime compression field block exceeds the requirements of the recipe it")
            .addInfo(
                "  will decrease the processing time by " + RED
                    + formatNumbers(SPACETIME_CASING_DIFFERENCE_DISCOUNT_PERCENTAGE * 100)
                    + "%"
                    + GRAY
                    + " per tier over the requirement (multiplicative).")
            .addInfo(BLUE + "Time Dilation Field Generator:")
            .addInfo(
                "- Decreases the time required for a recipe by " + RED
                    + "50%"
                    + GRAY
                    + " per tier of block (multiplicative).")
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
                    + " per tier (additive).")
            .addInfo(
                "  Decreases the yield of a recipe by " + RED
                    + formatNumbers(STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * 100)
                    + "%"
                    + GRAY
                    + " per tier (additive). ")
            .addInfo("  > Low tier stabilisation field generators have a power output penalty.")
            .addInfo(
                "     The power output penalty for using Crude Stabilisation Field Generators is " + RED
                    + formatNumbers(
                        STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * TOTAL_CASING_TIERS_WITH_POWER_PENALTY
                            * 100)
                    + "%"
                    + GRAY
                    + ".")
            .addInfo(
                "     This penalty decreases by " + RED
                    + formatNumbers(STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER * 100)
                    + "%"
                    + GRAY
                    + " per tier (additive).")
            .addInfo(TOOLTIP_BAR)
            .addInfo("Going over a recipe requirement on hydrogen or helium has a penalty on yield and recipe chance.")
            .addInfo("All stored hydrogen and helium is consumed during a craft. The associated formulas are:")
            .addInfo(GREEN + "Overflow ratio = (Stored fluid / Recipe requirement) - 1")
            .addInfo(GREEN + "Adjustment value = 1 - exp(-(30 * Overflow ratio)^2)")
            .addInfo("The Adjustment value is then subtracted from the total yield and recipe chance.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("It should be noted that base recipe chance is determined per recipe and yield always starts")
            .addInfo("at 1 and subtracts depending on penalties. All fluid/item outputs are multiplied by the")
            .addInfo("yield. Failure fluid is exempt.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("This multiblock can only output to ME output buses/hatches.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("This multiblock can be overclocked by placing a programmed circuit into the input bus.")
            .addInfo(
                "E.g. A circuit of 2 will provide 2 OCs, 16x EU input and 0.25x the time. EU output is unaffected.")
            .addInfo("All outputs are equal. All item and fluid output chances & amounts per recipe are unaffected.")
            .addInfo(TOOLTIP_BAR)
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
                MaterialsUEVplus.SpaceTime.getLocalizedNameForItem("%material")
                    + " instead of fluid/item outputs and output as much EU as a successful recipe.")
            .addInfo(TOOLTIP_BAR)
            .addInfo(
                "This multiblock can perform parallel processing by placing Astral Array Fabricators into the input bus.")
            .addInfo("The amount of parallel is calculated via these formulas:")
            .addInfo(GREEN + "Parallel exponent = floor(log(8 * Astral Array amount) / log(1.7))")
            .addInfo(GREEN + "Parallel = 2^(Parallel exponent)")
            .addInfo("If the EOH is running parallel recipes, the power calculation changes.")
            .addInfo("The power needed for parallel processing is calculated as follows:")
            .addInfo(GREEN + "total EU = ((EU output - EU input * 1.63) / 9) * 2.3^(Parallel exponent - 1)")
            .addInfo(
                "Furthermore, if parallel recipes are run, the recipes consume "
                    + MaterialsUEVplus.RawStarMatter.getLocalizedNameForItem("%material"))
            .addInfo("instead of helium and hydrogen. Overflow penalties still apply.")
            .addInfo(
                "The required amount of fluid to start a recipe is " + GREEN
                    + "12.4 / 10^6 * Helium amount * Parallel"
                    + GRAY
                    + ".")
            .addInfo("The success or failure of each parallel is determined independently.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("Animations can be disabled by using a screwdriver on the multiblock.")
            .addSeparator()
            .addStructureInfo("Eye of Harmony structure is too complex! See schematic for details.")
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
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 2 + EnumChatFormatting.GRAY + " input hatches.")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " ME output hatch.")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " input bus.")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " ME output bus.")
            .addStructureInfo("--------------------------------------------")
            .beginStructureBlock(33, 33, 33, false)
            .toolTipFinisher(AuthorColen.substring(8) + EnumChatFormatting.GRAY + "&" + CommonValues.TEC_MARK_EM);
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
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12],
                new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][12] };
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
            put(MaterialsUEVplus.RawStarMatter.mFluid, 0L);
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

            currentRecipe = eyeOfHarmonyRecipeStorage.recipeLookUp(controllerStack);
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
        return validFluidMap.get(MaterialsUEVplus.RawStarMatter.mFluid);
    }

    public CheckRecipeResult processRecipe(EyeOfHarmonyRecipe recipeObject) {

        // Get circuit damage, clamp it and then use it later for overclocking.
        currentCircuitMultiplier = 0;
        if (!mInputBusses.isEmpty()) {
            for (ItemStack itemStack : mInputBusses.get(0)
                .getRealInventory()) {
                if (GT_Utility.isAnyIntegratedCircuit(itemStack)) {
                    currentCircuitMultiplier = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 24);
                    break;
                }
            }
        }

        astralArrayAmount = 0;

        for (ItemStack itemStack : mInputBusses.get(0)
            .getRealInventory()) {
            if (itemStack != null && itemStack.isItemEqual(astralArrayFabricator.get(1))) {
                astralArrayAmount += itemStack.stackSize;
            }
        }

        long parallelExponent = 1;

        if (astralArrayAmount != 0) {
            parallelExponent = (long) Math.floor(Math.log(8 * astralArrayAmount) / LOG_CONSTANT);
            parallelAmount = (long) pow(2, parallelExponent);
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
                || (!EOH_DEBUG_MODE && getHydrogenStored() < currentRecipe.getHydrogenRequirement() * parallelAmount)) {
                return SimpleCheckRecipeResult.ofFailure("no_hydrogen");
            }

            if ((EOH_DEBUG_MODE && getHeliumStored() < 100)
                || (!EOH_DEBUG_MODE && getHeliumStored() < currentRecipe.getHeliumRequirement() * parallelAmount)) {
                return SimpleCheckRecipeResult.ofFailure("no_helium");
            }
        }

        if (spacetimeCompressionFieldMetadata == -1) {
            return CheckRecipeResultRegistry
                .insufficientMachineTier((int) recipeObject.getSpacetimeCasingTierRequired());
        }

        // Check tier of spacetime compression blocks is high enough.
        if ((spacetimeCompressionFieldMetadata + 1) < recipeObject.getSpacetimeCasingTierRequired()) {
            return CheckRecipeResultRegistry
                .insufficientMachineTier((int) recipeObject.getSpacetimeCasingTierRequired());
        }

        // Calculate multipliers used in power calculations
        double powerMultiplier = Math.max(1, Math.pow(2.3, (parallelExponent - 1)));

        // Determine EU recipe input
        startEU = recipeObject.getEUStartCost();

        // Calculate normal EU values
        double outputEUPenalty = (TOTAL_CASING_TIERS_WITH_POWER_PENALTY - stabilisationFieldMetadata)
            * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;
        outputEU_BigInt = BigInteger.valueOf((long) (recipeObject.getEUOutput() * (1 - outputEUPenalty)));
        usedEU = BigInteger.valueOf(-startEU)
            .multiply(BigInteger.valueOf((long) Math.pow(4, currentCircuitMultiplier)));

        // Calculate parallel EU values
        if (parallelAmount > 1) {
            outputEU_BigInt = outputEU_BigInt
                .multiply(BigInteger.valueOf((long) (powerMultiplier * PRECISION_MULTIPLIER)))
                .divide(BigInteger.valueOf(PRECISION_MULTIPLIER * POWER_DIVISION_CONSTANT));

            usedEU = usedEU
                .multiply(
                    BigInteger.valueOf((long) (powerMultiplier * PARALLEL_MULTIPLIER_CONSTANT * PRECISION_MULTIPLIER)))
                .divide(BigInteger.valueOf(PRECISION_MULTIPLIER * POWER_DIVISION_CONSTANT));
        }

        // Remove EU from the users network.
        if (!addEUToGlobalEnergyMap(userUUID, usedEU)) {
            return SimpleCheckRecipeResult.ofFailure("insufficient_power_no_val");
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

        successChance = recipeChanceCalculator();
        currentRecipeRocketTier = currentRecipe.getRocketTier();

        // Reduce internal storage by input fluid quantity required for recipe.
        if (parallelAmount > 1) {
            validFluidMap.put(MaterialsUEVplus.RawStarMatter.mFluid, 0L);
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

        successfulParallelAmount = (long) calculateChancedOutputMultiplier(
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
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), eyeOfHarmonyRenderBlock);
        TileEyeOfHarmony rendererTileEntity = (TileEyeOfHarmony) this.getBaseMetaTileEntity()
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
    private long currentRecipeRocketTier;

    private void outputFailedChance() {
        long failedParallelAmount = parallelAmount - successfulParallelAmount;
        if (failedParallelAmount > 0) {
            // 2^Tier spacetime released upon recipe failure.
            outputFluidToAENetwork(
                MaterialsUEVplus.SpaceTime.getMolten(1),
                (long) ((successChance * MOLTEN_SPACETIME_PER_FAILURE_TIER
                    * pow(SPACETIME_FAILURE_BASE, currentRecipeRocketTier + 1)) * failedParallelAmount));
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
            userUUID = String.valueOf(getBaseMetaTileEntity().getOwnerUuid());
            String userName = getBaseMetaTileEntity().getOwnerName();
            strongCheckOrAddUser(userUUID, userName);
        }

        if (!recipeRunning && mMachine) {
            if ((aTick % TICKS_BETWEEN_HATCH_DRAIN) == 0) {
                drainFluidFromHatchesAndStoreInternally();
            }
        }
    }

    private boolean recipeRunning = false;

    // Will void if AE network is full.
    private void outputItemToAENetwork(ItemStack item, long amount) {

        if ((item == null) || (amount <= 0)) {
            return;
        }

        if (amount < Integer.MAX_VALUE) {
            ItemStack tmpItem = item.copy();
            tmpItem.stackSize = (int) amount;
            ((GT_MetaTileEntity_Hatch_OutputBus_ME) mOutputBusses.get(0)).store(tmpItem);
        } else {
            // For item stacks > Int max.
            while (amount >= Integer.MAX_VALUE) {
                ItemStack tmpItem = item.copy();
                tmpItem.stackSize = Integer.MAX_VALUE;
                ((GT_MetaTileEntity_Hatch_OutputBus_ME) mOutputBusses.get(0)).store(tmpItem);
                amount -= Integer.MAX_VALUE;
            }
            ItemStack tmpItem = item.copy();
            tmpItem.stackSize = (int) amount;
            ((GT_MetaTileEntity_Hatch_OutputBus_ME) mOutputBusses.get(0)).store(tmpItem);
        }
    }

    private void outputFluidToAENetwork(FluidStack fluid, long amount) {

        if ((fluid == null) || (amount <= 0)) {
            return;
        }

        if (amount < Integer.MAX_VALUE) {
            FluidStack tmpFluid = fluid.copy();
            tmpFluid.amount = (int) amount;
            ((GT_MetaTileEntity_Hatch_Output_ME) mOutputHatches.get(0)).tryFillAE(tmpFluid);
        } else {
            // For fluidStacks > Int max.
            while (amount >= Integer.MAX_VALUE) {
                FluidStack tmpFluid = fluid.copy();
                tmpFluid.amount = Integer.MAX_VALUE;
                ((GT_MetaTileEntity_Hatch_Output_ME) mOutputHatches.get(0)).tryFillAE(tmpFluid);
                amount -= Integer.MAX_VALUE;
            }
            FluidStack tmpFluid = fluid.copy();
            tmpFluid.amount = (int) amount;
            ((GT_MetaTileEntity_Hatch_Output_ME) mOutputHatches.get(0)).tryFillAE(tmpFluid);
        }
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(GOLD + "---------------- Control Block Statistics ----------------");
        if (spacetimeCompressionFieldMetadata < 0) {
            str.add("Spacetime Compression Field Grade: None");
        } else {
            str.add(
                "Spacetime Compression Field Grade: " + EOH_TIER_FANCY_NAMES[spacetimeCompressionFieldMetadata]
                    + RESET
                    + " ("
                    + YELLOW
                    + spacetimeCompressionFieldMetadata
                    + RESET
                    + ")");
        }
        if (timeAccelerationFieldMetadata < 0) {
            str.add("Time Dilation Field Grade: None");
        } else {
            str.add(
                "Time Dilation Field Grade: " + EOH_TIER_FANCY_NAMES[timeAccelerationFieldMetadata]
                    + RESET
                    + " ("
                    + YELLOW
                    + timeAccelerationFieldMetadata
                    + RESET
                    + ")");
        }
        if (stabilisationFieldMetadata < 0) {
            str.add("Stabilisation Field Grade: None");
        } else {
            str.add(
                "Stabilisation Field Grade: " + EOH_TIER_FANCY_NAMES[stabilisationFieldMetadata]
                    + RESET
                    + " ("
                    + YELLOW
                    + stabilisationFieldMetadata
                    + RESET
                    + ")");
        }
        str.add(GOLD + "----------------- Internal Fluids Stored ----------------");
        validFluidMap.forEach(
            (key, value) -> str.add(BLUE + key.getLocalizedName() + RESET + " : " + RED + formatNumbers(value)));
        if (recipeRunning) {
            str.add(GOLD + "---------------------- Other Stats ---------------");
            str.add("Recipe Success Chance: " + RED + formatNumbers(100 * successChance) + RESET + "%");
            str.add("Recipe Yield: " + RED + formatNumbers(100 * yield) + RESET + "%");
            str.add("Astral Array Fabricators detected: " + RED + formatNumbers(astralArrayAmount));
            str.add("Total Parallel: " + RED + formatNumbers(parallelAmount));
            str.add("EU Output: " + RED + toStandardForm(outputEU_BigInt) + RESET + " EU");
            str.add("EU Input:  " + RED + toStandardForm(usedEU.abs()) + RESET + " EU");
            int currentMaxProgresstime = Math.max(maxProgresstime(), 1);
            if (starMatter != null && starMatter.fluidStack != null) {
                FluidStackLong starMatterOutput = new FluidStackLong(
                    starMatter.fluidStack,
                    (long) (starMatter.amount * yield * successChance * parallelAmount));
                str.add(
                    "Average " + starMatterOutput.fluidStack.getLocalizedName()
                        + " Output: "
                        + RED
                        + formatNumbers(starMatterOutput.amount)
                        + RESET
                        + " L, "
                        + YELLOW
                        + formatNumbers(starMatterOutput.amount * 20.0 / currentMaxProgresstime)
                        + RESET
                        + " L/s");

                FluidStackLong stellarPlasmaOutput = new FluidStackLong(
                    MaterialsUEVplus.RawStarMatter.getFluid(0),
                    (long) (stellarPlasma.amount * yield * successChance * parallelAmount));
                str.add(
                    "Average " + stellarPlasmaOutput.fluidStack.getLocalizedName()
                        + " Output: "
                        + RED
                        + formatNumbers(stellarPlasmaOutput.amount)
                        + RESET
                        + " L, "
                        + YELLOW
                        + formatNumbers(stellarPlasmaOutput.amount * 20.0 / currentMaxProgresstime)
                        + RESET
                        + " L/s");
            }
            BigInteger euPerTick = (outputEU_BigInt.subtract(usedEU.abs()))
                .divide(BigInteger.valueOf(currentMaxProgresstime));

            str.add("Estimated EU/t: " + RED + toStandardForm(euPerTick) + RESET + " EU/t");
        }
        str.add(GOLD + "-----------------------------------------------------");
        return str.toArray(new String[0]);
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

    // Sub tags, less specific names required.
    private static final String STACK_SIZE = "stackSize";
    private static final String ITEM_STACK_NBT_TAG = "itemStack";
    private static final String FLUID_AMOUNT = "fluidAmount";
    private static final String FLUID_STACK_NBT_TAG = "fluidStack";

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
        animationsEnabled = aNBT.getBoolean(ANIMATIONS_ENABLED);
        parallelAmount = aNBT.getLong(PARALLEL_AMOUNT_NBT_TAG);
        yield = aNBT.getDouble(YIELD_NBT_TAG);
        successfulParallelAmount = aNBT.getLong(SUCCESSFUL_PARALLEL_AMOUNT_NBT_TAG);
        astralArrayAmount = aNBT.getLong(ASTRAL_ARRAY_AMOUNT_NBT_TAG);
        outputEU_BigInt = new BigInteger(aNBT.getByteArray(CALCULATED_EU_OUTPUT_NBT_TAG));
        usedEU = new BigInteger(aNBT.getByteArray(CALCULATED_EU_INPUT_NBT_TAG));

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
}
