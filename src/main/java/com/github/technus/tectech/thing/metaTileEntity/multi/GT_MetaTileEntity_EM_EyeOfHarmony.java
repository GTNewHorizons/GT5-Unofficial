package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsBA0;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static gregtech.api.util.GT_Utility.formatNumbers;
import static java.lang.Math.*;
import static net.minecraft.util.EnumChatFormatting.*;

import appeng.util.ReadableNumberConverter;
import com.github.technus.tectech.recipe.EyeOfHarmonyRecipe;
import com.github.technus.tectech.recipe.EyeOfHarmonyRecipeStorage;
import com.github.technus.tectech.thing.casing.TT_Block_SpacetimeCompressionFieldGenerators;
import com.github.technus.tectech.thing.casing.TT_Block_StabilisationFieldGenerators;
import com.github.technus.tectech.thing.casing.TT_Block_TimeAccelerationFieldGenerators;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.ItemStackLong;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;
import java.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import org.spongepowered.libraries.com.google.common.math.LongMath;

@SuppressWarnings("SpellCheckingInspection")
public class GT_MetaTileEntity_EM_EyeOfHarmony extends GT_MetaTileEntity_MultiblockBase_EM
        implements IConstructable, IGlobalWirelessEnergy {
    // Region variables.
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private static EyeOfHarmonyRecipeStorage recipes;

    private int spacetimeCompressionFieldMetadata = -1;
    private int timeAccelerationFieldMetadata = -1;
    private int stabilisationFieldMetadata = -1;

    private static final double spacetimeCasingDifferenceDiscountPercentage = 0.03;

    private String userUUID = "";
    private String userName = "";
    private long euOutput = 0;

    private final Stack<Long> computationStack = new Stack<>();

    // Multiblock structure.
    private static final IStructureDefinition<GT_MetaTileEntity_EM_EyeOfHarmony> STRUCTURE_DEFINITION =
            IStructureDefinition.<GT_MetaTileEntity_EM_EyeOfHarmony>builder()
                    .addShape("main", transpose(new String[][] {
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "            CCCCCCCCC            ",
                            "               C C               ",
                            "            CCCCCCCCC            ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "              DDDDD              ",
                            "             DDCDCDD             ",
                            "         CCCCDCCDCCDCCCC         ",
                            "             DDDDDDD             ",
                            "         CCCCDCCDCCDCCCC         ",
                            "             DDCDCDD             ",
                            "              DDDDD              ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "                D                ",
                            "             DDDDDDD             ",
                            "            DD     DD            ",
                            "            D  EEE  D            ",
                            "       CCC  D EAAAE D  CCC       ",
                            "          DDD EAAAE DDD          ",
                            "       CCC  D EAAAE D  CCC       ",
                            "            D  EEE  D            ",
                            "            DD     DD            ",
                            "             DDDDDDD             ",
                            "                D                ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "                D                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "      CC                 CC      ",
                            "        DD             DD        ",
                            "      CC                 CC      ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                D                ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "              CCCCC              ",
                            "                D                ",
                            "                A                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "      C                   C      ",
                            "     CC                   CC     ",
                            "      CDAA             AADC      ",
                            "     CC                   CC     ",
                            "      C                   C      ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                A                ",
                            "                D                ",
                            "              CCCCC              ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "             SEEAEES             ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "       S                 S       ",
                            "       E                 E       ",
                            "    CC E                 E CC    ",
                            "      DA                 AD      ",
                            "    CC E                 E CC    ",
                            "       E                 E       ",
                            "       S                 S       ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "             SEEAEES             ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "              CCCCC              ",
                            "                D                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "    C                       C    ",
                            "   CC                       CC   ",
                            "    CDA                   ADC    ",
                            "   CC                       CC   ",
                            "    C                       C    ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                D                ",
                            "              CCCCC              ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "             SEEAEES             ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "     S                     S     ",
                            "     E                     E     ",
                            "  CC E                     E CC  ",
                            "    DA                     AD    ",
                            "  CC E                     E CC  ",
                            "     E                     E     ",
                            "     S                     S     ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "             SEEAEES             ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "                D                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  C                           C  ",
                            "   DA                       AD   ",
                            "  C                           C  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                D                ",
                            "               C C               ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            " CC                           CC ",
                            "   DA                       AD   ",
                            " CC                           CC ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "               C C               ",
                            "                D                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            " C                             C ",
                            "  D                           D  ",
                            " C                             C ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                D                ",
                            "               C C               ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "               C C               ",
                            "                D                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            " C                             C ",
                            "  D                           D  ",
                            " C                             C ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                D                ",
                            "               C C               ",
                            "                                 "
                        },
                        {
                            "             CCCCCCC             ",
                            "               C C               ",
                            "             DDDDDDD             ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "CCD                           DCC",
                            "  D                           D  ",
                            "CCD                           DCC",
                            "  D                           D  ",
                            "  D                           D  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "             DDDDDDD             ",
                            "               C C               ",
                            "               C C               "
                        },
                        {
                            "            CCHHHHHCC            ",
                            "              DDDDD              ",
                            "            DD     DD            ",
                            "                                 ",
                            "                                 ",
                            "       S                 S       ",
                            "                                 ",
                            "     S                     S     ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  D                           D  ",
                            "  D                           D  ",
                            " D                             D ",
                            "CD                             DC",
                            " D                             D ",
                            "CD                             DC",
                            " D                             D ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "     S                     S     ",
                            "                                 ",
                            "       S                 S       ",
                            "                                 ",
                            "                                 ",
                            "            DD     DD            ",
                            "              DDDDD              ",
                            "               C C               "
                        },
                        {
                            "            CHHHHHHHC            ",
                            "             DDCDCDD             ",
                            "            D  EEE  D            ",
                            "                                 ",
                            "      C                   C      ",
                            "       E                 E       ",
                            "    C                       C    ",
                            "     E                     E     ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  D                           D  ",
                            " D                             D ",
                            " D                             D ",
                            "CCE                           ECC",
                            " DE                           ED ",
                            "CCE                           ECC",
                            " D                             D ",
                            " D                             D ",
                            "  D                           D  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "     E                     E     ",
                            "    C                       C    ",
                            "       E                 E       ",
                            "      C                   C      ",
                            "                                 ",
                            "            D  EEE  D            ",
                            "             DDCDCDD             ",
                            "               C C               "
                        },
                        {
                            "            CHHCCCHHC            ",
                            "         CCCCDCCDCCDCCCC         ",
                            "       CCC  D EAAAE D  CCC       ",
                            "      CC                 CC      ",
                            "     CC                   CC     ",
                            "    CC E                 E CC    ",
                            "   CC                       CC   ",
                            "  CC E                     E CC  ",
                            "  C                           C  ",
                            " CC                           CC ",
                            " C                             C ",
                            " C                             C ",
                            "CCD                           DCC",
                            "CD                             DC",
                            "CCE                           ECC",
                            "CCA                           ACC",
                            "CDA                           ADC",
                            "CCA                           ACC",
                            "CCE                           ECC",
                            "CD                             DC",
                            "CCD                           DCC",
                            " C                             C ",
                            " C                             C ",
                            " CC                           CC ",
                            "  C                           C  ",
                            "  CC E                     E CC  ",
                            "   CC                       CC   ",
                            "    CC E                 E CC    ",
                            "     CC                   CC     ",
                            "      CC                 CC      ",
                            "       CCC  D EAAAE D  CCC       ",
                            "         CCCCDCCDCCDCCCC         ",
                            "            CCCCCCCCC            "
                        },
                        {
                            "            CHHC~CHHC            ",
                            "             DDDDDDD             ",
                            "          DDD EAAAE DDD          ",
                            "        DD             DD        ",
                            "      CDAA             AADC      ",
                            "      DA                 AD      ",
                            "    CDA                   ADC    ",
                            "    DA                     AD    ",
                            "   DA                       AD   ",
                            "   DA                       AD   ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "  D                           D  ",
                            " D                             D ",
                            " DE                           ED ",
                            "CDA                           ADC",
                            " DA                           AD ",
                            "CDA                           ADC",
                            " DE                           ED ",
                            " D                             D ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "   DA                       AD   ",
                            "   DA                       AD   ",
                            "    DA                     AD    ",
                            "    CDA                   ADC    ",
                            "      DA                 AD      ",
                            "      CDAA             AADC      ",
                            "        DD             DD        ",
                            "          DDD EAAAE DDD          ",
                            "             DDDDDDD             ",
                            "               C C               "
                        },
                        {
                            "            CHHCCCHHC            ",
                            "         CCCCDCCDCCDCCCC         ",
                            "       CCC  D EAAAE D  CCC       ",
                            "      CC                 CC      ",
                            "     CC                   CC     ",
                            "    CC E                 E CC    ",
                            "   CC                       CC   ",
                            "  CC E                     E CC  ",
                            "  C                           C  ",
                            " CC                           CC ",
                            " C                             C ",
                            " C                             C ",
                            "CCD                           DCC",
                            "CD                             DC",
                            "CCE                           ECC",
                            "CCA                           ACC",
                            "CDA                           ADC",
                            "CCA                           ACC",
                            "CCE                           ECC",
                            "CD                             DC",
                            "CCD                           DCC",
                            " C                             C ",
                            " C                             C ",
                            " CC                           CC ",
                            "  C                           C  ",
                            "  CC E                     E CC  ",
                            "   CC                       CC   ",
                            "    CC E                 E CC    ",
                            "     CC                   CC     ",
                            "      CC                 CC      ",
                            "       CCC  D EAAAE D  CCC       ",
                            "         CCCCDCCDCCDCCCC         ",
                            "            CCCCCCCCC            "
                        },
                        {
                            "            CHHHHHHHC            ",
                            "             DDCDCDD             ",
                            "            D  EEE  D            ",
                            "                                 ",
                            "      C                   C      ",
                            "       E                 E       ",
                            "    C                       C    ",
                            "     E                     E     ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  D                           D  ",
                            " D                             D ",
                            " D                             D ",
                            "CCE                           ECC",
                            " DE                           ED ",
                            "CCE                           ECC",
                            " D                             D ",
                            " D                             D ",
                            "  D                           D  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "     E                     E     ",
                            "    C                       C    ",
                            "       E                 E       ",
                            "      C                   C      ",
                            "                                 ",
                            "            D  EEE  D            ",
                            "             DDCDCDD             ",
                            "               C C               "
                        },
                        {
                            "            CCHHHHHCC            ",
                            "              DDDDD              ",
                            "            DD     DD            ",
                            "                                 ",
                            "                                 ",
                            "       S                 S       ",
                            "                                 ",
                            "     S                     S     ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  D                           D  ",
                            "  D                           D  ",
                            " D                             D ",
                            "CD                             DC",
                            " D                             D ",
                            "CD                             DC",
                            " D                             D ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "     S                     S     ",
                            "                                 ",
                            "       S                 S       ",
                            "                                 ",
                            "                                 ",
                            "            DD     DD            ",
                            "              DDDDD              ",
                            "               C C               "
                        },
                        {
                            "             CCCCCCC             ",
                            "               C C               ",
                            "             DDDDDDD             ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  D                           D  ",
                            "  D                           D  ",
                            "CCD                           DCC",
                            "  D                           D  ",
                            "CCD                           DCC",
                            "  D                           D  ",
                            "  D                           D  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "             DDDDDDD             ",
                            "               C C               ",
                            "               C C               "
                        },
                        {
                            "                                 ",
                            "               C C               ",
                            "                D                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            " C                             C ",
                            "  D                           D  ",
                            " C                             C ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                D                ",
                            "               C C               ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "               C C               ",
                            "                D                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            " C                             C ",
                            "  D                           D  ",
                            " C                             C ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                D                ",
                            "               C C               ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            " CC                           CC ",
                            "   DA                       AD   ",
                            " CC                           CC ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "                D                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "  C                           C  ",
                            "   DA                       AD   ",
                            "  C                           C  ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                D                ",
                            "               C C               ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "             SEEAEES             ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "     S                     S     ",
                            "     E                     E     ",
                            "  CC E                     E CC  ",
                            "    DA                     AD    ",
                            "  CC E                     E CC  ",
                            "     E                     E     ",
                            "     S                     S     ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "             SEEAEES             ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "              CCCCC              ",
                            "                D                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "    C                       C    ",
                            "   CC                       CC   ",
                            "    CDA                   ADC    ",
                            "   CC                       CC   ",
                            "    C                       C    ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                D                ",
                            "              CCCCC              ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "             SEEAEES             ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "       S                 S       ",
                            "       E                 E       ",
                            "    CC E                 E CC    ",
                            "      DA                 AD      ",
                            "    CC E                 E CC    ",
                            "       E                 E       ",
                            "       S                 S       ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "             SEEAEES             ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "              CCCCC              ",
                            "                D                ",
                            "                A                ",
                            "                A                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "      C                   C      ",
                            "     CC                   CC     ",
                            "      CDAA             AADC      ",
                            "     CC                   CC     ",
                            "      C                   C      ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                A                ",
                            "                A                ",
                            "                D                ",
                            "              CCCCC              ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "                D                ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "      CC                 CC      ",
                            "        DD             DD        ",
                            "      CC                 CC      ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                D                ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                D                ",
                            "                D                ",
                            "             DDDDDDD             ",
                            "            DD     DD            ",
                            "            D  EEE  D            ",
                            "       CCC  D EAAAE D  CCC       ",
                            "          DDD EAAAE DDD          ",
                            "       CCC  D EAAAE D  CCC       ",
                            "            D  EEE  D            ",
                            "            DD     DD            ",
                            "             DDDDDDD             ",
                            "                D                ",
                            "                D                ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "              DDDDD              ",
                            "             DDCDCDD             ",
                            "         CCCCDCCDCCDCCCC         ",
                            "             DDDDDDD             ",
                            "         CCCCDCCDCCDCCCC         ",
                            "             DDCDCDD             ",
                            "              DDDDD              ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        },
                        {
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "            CCCCCCCCC            ",
                            "               C C               ",
                            "            CCCCCCCCC            ",
                            "               C C               ",
                            "               C C               ",
                            "               C C               ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 ",
                            "                                 "
                        }
                    }))
                    .addElement(
                            'A',
                            ofBlocksTiered(
                                    (block, meta) -> block
                                                    == TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators
                                            ? meta
                                            : -1,
                                    ImmutableList.of(
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    0),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    1),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    2),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    3),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    4),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    5),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    6),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    7),
                                            Pair.of(
                                                    TT_Block_SpacetimeCompressionFieldGenerators
                                                            .SpacetimeCompressionFieldGenerators,
                                                    8)),
                                    -1,
                                    (t, meta) -> t.spacetimeCompressionFieldMetadata = meta,
                                    t -> t.spacetimeCompressionFieldMetadata))
                    .addElement(
                            'S',
                            ofBlocksTiered(
                                    (block, meta) ->
                                            block == TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators
                                                    ? meta
                                                    : -1,
                                    ImmutableList.of(
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    0),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    1),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    2),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    3),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    4),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    5),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    6),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    7),
                                            Pair.of(
                                                    TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators,
                                                    8)),
                                    -1,
                                    (t, meta) -> t.stabilisationFieldMetadata = meta,
                                    t -> t.stabilisationFieldMetadata))
                    .addElement('C', ofBlock(sBlockCasingsBA0, 11))
                    .addElement('D', ofBlock(sBlockCasingsBA0, 10))
                    .addElement(
                            'E',
                            ofBlocksTiered(
                                    (block, meta) -> block
                                                    == TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator
                                            ? meta
                                            : -1,
                                    ImmutableList.of(
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    0),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    1),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    2),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    3),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    4),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    5),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    6),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    7),
                                            Pair.of(
                                                    TT_Block_TimeAccelerationFieldGenerators
                                                            .TimeAccelerationFieldGenerator,
                                                    8)),
                                    -1,
                                    (t, meta) -> t.timeAccelerationFieldMetadata = meta,
                                    t -> t.timeAccelerationFieldMetadata))
                    .addElement(
                            'H',
                            ofHatchAdderOptional(
                                    GT_MetaTileEntity_EM_EyeOfHarmony::addClassicToMachineList,
                                    textureOffset,
                                    1,
                                    sBlockCasingsBA0,
                                    12))
                    .build();

    private double hydrogenOverflowProbabilityAdjustment;
    private double heliumOverflowProbabilityAdjustment;

    // Maximum additional chance of recipe success that can be obtained from adding computation.
    private static final double maxPercentageChanceGainFromComputationPerSecond = 0.3;

    // todo: make higher on final release.
    private static final long ticksBetweenHatchDrain = 20;

    private List<ItemStackLong> outputItems = new ArrayList<>();

    private void calculateHydrogenHeliumInputExcessValues(
            long hydrogen_recipe_requirement, long helium_recipe_requirement) {

        long hydrogen_stored = validFluidMap.get(Materials.Hydrogen.getGas(1L));
        long helium_stored = validFluidMap.get(Materials.Helium.getGas(1L));

        double hydrogen_excess_percentage = abs(1 - hydrogen_stored / hydrogen_recipe_requirement);
        double helium_excess_percentage = abs(1 - helium_stored / helium_recipe_requirement);

        hydrogenOverflowProbabilityAdjustment = 1 - exp(-pow(30 * hydrogen_excess_percentage, 2));
        heliumOverflowProbabilityAdjustment = 1 - exp(-pow(30 * helium_excess_percentage, 2));
    }

    private double recipeChanceCalculator() {
        double chance = (currentRecipe.getBaseRecipeSuccessChance()
                - timeAccelerationFieldMetadata * 0.1
                + stabilisationFieldMetadata * 0.05
                - hydrogenOverflowProbabilityAdjustment
                - heliumOverflowProbabilityAdjustment
                + maxPercentageChanceGainFromComputationPerSecond * (1 - exp(-10e-5 * getComputation())));

        return clamp(chance, 0.0, 1.0);
    }

    public static double clamp(double amount, double min, double max) {
        return Math.max(min, Math.min(amount, max));
    }

    private double recipeYieldCalculator() {
        double yield = 1.0
                - hydrogenOverflowProbabilityAdjustment
                - heliumOverflowProbabilityAdjustment
                - stabilisationFieldMetadata * 0.05;

        return clamp(yield, 0.0, 1.0);
    }

    private int recipeProcessTimeCalculator(long recipeTime, long recipeSpacetimeCasingRequired) {

        // Tier 1 recipe.
        // Tier 2 spacetime blocks.
        // = 3% discount.

        // Tier 1 recipe.
        // Tier 3 spacetime blocks.
        // = 3%*3% = 5.91% discount.

        long spacetimeCasingDifference = (recipeSpacetimeCasingRequired - spacetimeCompressionFieldMetadata);
        double recipeTimeDiscounted = recipeTime
                * pow(2.0, -timeAccelerationFieldMetadata)
                * pow(1 - spacetimeCasingDifferenceDiscountPercentage, spacetimeCasingDifference);
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
        if (!structureCheck_EM("main", 16, 16, 0)) {
            return false;
        }

        // Check if there is 1+ output bus, and they are ME output busses.
        {
            if (mOutputBusses.size() == 0) {
                return false;
            }

            for (GT_MetaTileEntity_Hatch_OutputBus hatch : mOutputBusses) {
                if (!(hatch instanceof GT_MetaTileEntity_Hatch_OutputBus_ME)) {
                    return false;
                }
            }
        }

        // Check if there is 1+ output hatch, and they are ME output hatches.
        {
            if (mOutputHatches.size() == 0) {
                return false;
            }

            for (GT_MetaTileEntity_Hatch_Output hatch : mOutputHatches) {
                if (!(hatch instanceof GT_MetaTileEntity_Hatch_Output_ME)) {
                    return false;
                }
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

        // Make sure there is 2 input hatches.
        if (mInputHatches.size() != 2) {
            return false;
        }

        // 1 Maintenance hatch, as usual.
        return (mMaintenanceHatches.size() == 1);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Spacetime Manipulator")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Creates a pocket of spacetime that is bigger on the inside using transdimensional")
                .addInfo("engineering. Certified Time Lord regulation compliant. This multi uses too much EU")
                .addInfo("to be handled with conventional means. All EU requirements are handled directly by your")
                .addInfo("your wireless EU network.")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("This multiblock will constantly consume hydrogen and helium when it is not running a")
                .addInfo("recipe as fast as it can. It will store this internally, you can see the totals by")
                .addInfo("using a scanner. This multi also has three tiered blocks with " + RED + 9 + GRAY + " tiers")
                .addInfo("each. They are as follows and have the associated effects on the multi.")
                .addInfo(BLUE + "Spacetime Compression Field Generator:")
                .addInfo("- The tier of this block determines what recipes can be run. If the multiblocks")
                .addInfo("  spacetime compression field block exceeds the requirements of the recipe it")
                .addInfo("  will decrease the processing time by " + RED + "3%" + GRAY
                        + " per tier over the requirement. This")
                .addInfo("  is multiplicative.")
                .addInfo(BLUE + "Time Dilation Field Generator:")
                .addInfo("- Decreases the time required by a recipe by a factor of " + RED + "2" + GRAY
                        + " per tier of block.")
                .addInfo("  Decreases the probability of a recipe succeeding by " + RED + "10%" + GRAY
                        + " per tier (additive)")
                .addInfo(BLUE + "Stabilisation Field Generator:")
                .addInfo("- Increases the probability of a recipe succeeding by " + RED + "5%" + GRAY
                        + " per tier (additive).")
                .addInfo("  Decreases the yield of a recipe by " + RED + "5%" + GRAY + " per tier (additive). ")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Computation/s provided to the multiblock can increase the chance by up to " + RED
                        + formatNumbers(maxPercentageChanceGainFromComputationPerSecond * 100) + GRAY
                        + "%.")
                .addInfo("The associated formula is " + GREEN
                        + "additional_chance = 0.3 * exp(10^(-5) * computation_per_second)" + GRAY + ".")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Going over a recipe requirement on hydrogen or helium has a penalty on yield and recipe")
                .addInfo(
                        "chance. All stored hydrogen and helium is consumed during a craft. The associated formulas are:")
                .addInfo(GREEN + "percentage_overflow = abs(1 - fluid_stored/recipe_requirement)")
                .addInfo(GREEN + "adjustment_value = 1 - exp(-(30 * percentage_overflow)^2)")
                .addInfo("The value of adjustment_value is then subtracted from the total yield and recipe chance.")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("It should be noted that base recipe chance is determined per recipe and yield always")
                .addInfo("starts at 1 and subtracts depending on penalities. All fluid/item outputs are multiplied")
                .addInfo("by the yield calculated.")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("This multiblock can only output to ME output busses/hatches. If no space in the network")
                .addInfo(
                        "is avaliable the items/fluids will be " + UNDERLINE + DARK_RED + "voided" + RESET + GRAY + ".")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Recipes that fail will return a random amount of the fluid back from the recipe and some")
                .addInfo("exotic material that rejects conventional physics.")
                .addSeparator()
                .addStructureInfo("Eye of Harmony structure is too complex! See schematic for details.")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "896" + EnumChatFormatting.GRAY + " Ultimate Molecular Casing.")
                .addStructureInfo(EnumChatFormatting.GOLD + "534" + EnumChatFormatting.GRAY
                        + " Ultimate Advanced Molecular Casing.")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "680" + EnumChatFormatting.GRAY + " Time Dilation Field Generator.")
                .addStructureInfo(
                        EnumChatFormatting.GOLD + "48" + EnumChatFormatting.GRAY + " Stabilisation Field Generator.")
                .addStructureInfo(EnumChatFormatting.GOLD + "138" + EnumChatFormatting.GRAY
                        + " Spacetime Compression Field Generator.")
                .addStructureInfo("--------------------------------------------")
                .addStructureInfo(
                        "Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " maintenance hatch.")
                .addStructureInfo(
                        "Requires " + EnumChatFormatting.GOLD + 2 + EnumChatFormatting.GRAY + " input hatches.")
                .addStructureInfo(
                        "Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + "+ ME output hatch.")
                .addStructureInfo(
                        "Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " input busses.")
                .addStructureInfo(
                        "Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " ME output bus.")
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
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[] {
                Textures.BlockIcons.casingTexturePages[texturePage][12],
                new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)
            };
        }
        return new ITexture[] {Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

        structureBuild_EM("main", 16, 16, 0, stackSize, hintsOnly);
    }

    private final Map<FluidStack, Long> validFluidMap = new HashMap<FluidStack, Long>() {
        {
            put(Materials.Hydrogen.getGas(1), 0L);
            put(Materials.Helium.getGas(1), 0L);
        }
    };

    private void drainFluidFromHatchesAndStoreInternally() {
        for (GT_MetaTileEntity_Hatch_Input inputHatch : mInputHatches) {
            FluidStack fluidInHatch = inputHatch.getFluid();

            if (fluidInHatch == null) {
                continue;
            }

            // Iterate over valid fluids and store them in a hashmap.
            for (FluidStack validFluid : validFluidMap.keySet()) {
                if (fluidInHatch.isFluidEqual(validFluid)) {
                    validFluidMap.put(validFluid, validFluidMap.get(validFluid) + (long) fluidInHatch.amount);
                    inputHatch.setFillableStack(null);
                }
            }
        }
    }

    private EyeOfHarmonyRecipe currentRecipe;

    private long lagPreventer = 0;
    private final long recipeCheckInterval = 3 * 20;

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        if (aStack == null) {
            return false;
        }

        lagPreventer++;
        if (lagPreventer < recipeCheckInterval) {
            lagPreventer = 0;
            // No item in multi gui slot.

            currentRecipe = recipes.recipeLookUp(aStack);
            if (processRecipe(currentRecipe)) {
                return true;
            }

            currentRecipe = null;
        }
        return false;
    }

    private long getHydrogenStored() {
        return validFluidMap.get(Materials.Hydrogen.getGas(1));
    }

    private long getHeliumStored() {
        return validFluidMap.get(Materials.Helium.getGas(1));
    }

    public boolean processRecipe(EyeOfHarmonyRecipe recipeObject) {

        //        if ((getHydrogenStored() < currentRecipe.getHydrogenRequirement())
        //                || (getHeliumStored() < currentRecipe.getHeliumRequirement())) {
        //            return false;
        //        }

        // todo: DEBUG, DELETE THIS:
        if ((getHydrogenStored() < 100) || (getHeliumStored() < 100)) {
            return false;
        }

        // Check tier of spacetime compression blocks is high enough.
        if (spacetimeCompressionFieldMetadata < recipeObject.getSpacetimeCasingTierRequired()) {
            return false;
        }

        // Remove EU from the users network.
        if (!addEUToGlobalEnergyMap(userUUID, -recipeObject.getEUStartCost())) {
            return false;
        }

        mMaxProgresstime = recipeProcessTimeCalculator(
                recipeObject.getRecipeTimeInTicks(), recipeObject.getSpacetimeCasingTierRequired());

        calculateHydrogenHeliumInputExcessValues(
                recipeObject.getHydrogenRequirement(), recipeObject.getHeliumRequirement());

        // todo: DEBUG ! DELETE THESE TWO LINES:
        hydrogenOverflowProbabilityAdjustment = 0;
        heliumOverflowProbabilityAdjustment = 0;

        successChance = recipeChanceCalculator();

        // Determine EU recipe output.
        euOutput = recipeObject.getEUOutput();

        // Set expected recipe computation.
        eRequiredData = getComputation();

        // Reduce internal storage by hydrogen and helium quantity required for recipe.
        validFluidMap.put(Materials.Hydrogen.getGas(1), 0L);
        validFluidMap.put(Materials.Helium.getGas(1), 0L);

        double yield = recipeYieldCalculator();
        successChance = 1; // todo debug, remove.

        // Return copies of the output objects.
        mOutputFluids = recipeObject.getOutputFluids();
        outputItems = recipeObject.getOutputItems();

        if (yield != 1.0) {
            // Iterate over item output list and apply yield values.
            for (ItemStackLong itemStackLong : outputItems) {
                itemStackLong.stackSize *= yield;
            }

            // Iterate over fluid output list and apply yield values.
            for (FluidStack fluidStack : mOutputFluids) {
                fluidStack.amount *= yield;
            }
        }

        updateSlots();

        recipeRunning = true;
        return true;
    }

    private double successChance;

    private void outputFailedChance() {
        // todo Replace with proper fluid once added to GT.
        int exoticMaterialOutputAmount =
                (int) ((successChance) * 1440 * (getHydrogenStored() + getHeliumStored()) / 1_000_000_000.0);
        mOutputFluids = new FluidStack[] {Materials.SpaceTime.getFluid(exoticMaterialOutputAmount)};
        super.outputAfterRecipe_EM();
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        recipeRunning = false;
    }

    public void outputAfterRecipe_EM() {
        recipeRunning = false;
        eRequiredData = 0L;

        if (successChance < random()) {
            outputFailedChance();
            outputItems = new ArrayList<>();
            return;
        }

        addEUToGlobalEnergyMap(userUUID, euOutput);
        euOutput = 0;

        for (ItemStackLong itemStack : outputItems) {
            outputItemToAENetwork(itemStack.itemStack, itemStack.stackSize);
        }

        // Clear the array list for new recipes.
        outputItems = new ArrayList<>();

        // Do other stuff from TT superclasses. E.g. outputting fluids.
        super.outputAfterRecipe_EM();
    }

    private void pushComputation() {
        if (computationStack.size() == computationTickCacheSize) {
            computationStack.remove(0);
        }
        computationStack.push(eAvailableData);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aTick == 1) {
            userUUID = String.valueOf(getBaseMetaTileEntity().getOwnerUuid());
            userName = getBaseMetaTileEntity().getOwnerName();
            strongCheckOrAddUser(userUUID, userName);

            // If no multi exists this will set the recipe storage.
            // This must be done after game load otherwise it fails.
            if (recipes == null) {
                recipes = new EyeOfHarmonyRecipeStorage();
            }
        }

        // Add computation to stack. Prevents small interruptions causing issues.
        pushComputation();

        if (!recipeRunning) {
            if ((aTick % ticksBetweenHatchDrain) == 0) {
                drainFluidFromHatchesAndStoreInternally();
            }
        }
    }

    private boolean recipeRunning = false;
    private static final int computationTickCacheSize = 5;

    private long getComputation() {
        return Collections.max(computationStack);
    }

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

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(GOLD + "---------------- Control Block Statistics ----------------");
        str.add("Spacetime Compression Field Grade: " + BLUE + spacetimeCompressionFieldMetadata);
        str.add("Time Dilation Field Grade: " + BLUE + timeAccelerationFieldMetadata);
        str.add("Stabilisation Field Grade: " + BLUE + stabilisationFieldMetadata);
        str.add(GOLD + "----------------- Internal Fluids Stored ----------------");
        validFluidMap.forEach(
                (key, value) -> str.add(BLUE + key.getLocalizedName() + RESET + " : " + RED + formatNumbers(value)));
        if (recipeRunning) {
            str.add(GOLD + "---------------------- Other Stats ---------------");
            str.add("Recipe Success Chance: " + RED + formatNumbers(100 * successChance) + RESET + "%");
            str.add("Recipe Yield: " + RED + formatNumbers(100 * successChance) + RESET + "%");
            str.add("EU Output: " + RED + formatNumbers(euOutput) + RESET + " EU");
            if (mOutputFluids.length > 0) {
                // Star matter is always the last element in the array.
                str.add("Estimated Star Matter Output: " + RED
                        + formatNumbers(mOutputFluids[mOutputFluids.length - 1].amount) + RESET + " L");
            }
            long euPerTick = euOutput / maxProgresstime();
            if (euPerTick < LongMath.pow(10, 12)) {
                str.add("Estimated EU/t: " + RED + formatNumbers(euOutput / maxProgresstime()) + RESET + " EU/t");
            } else {
                str.add("Estimated EU/t: " + RED
                        + ReadableNumberConverter.INSTANCE.toWideReadableForm(euOutput / maxProgresstime()) + RESET
                        + " EU/t");
            }
            str.add(GOLD + "-----------------------------------------------------");
        }
        return str.toArray(new String[0]);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] {"Eye of Harmony multiblock"};
    }

    // NBT save/load strings.
    private static final String eyeOfHarmony = "eyeOfHarmonyOutput";
    private static final String numberOfItemsNBTTag = eyeOfHarmony + "numberOfItems";
    private static final String itemOutputNBTTag = eyeOfHarmony + "itemOutput";
    private static final String recipeRunningNBTTag = eyeOfHarmony + "recipeRunning";
    private static final String recipeEUOutputNBTTag = eyeOfHarmony + "euOutput";
    private static final String recipeSuccessChanceNBTTag = eyeOfHarmony + "recipeSuccessChance";

    // Sub tags, less specific names required.
    private static final String stackSizeNBTTag = "stackSize";
    private static final String itemStackNBTTag = "itemStack";

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        // Save the quantity of fluid stored inside the controller.
        validFluidMap.forEach((key, value) -> aNBT.setLong("stored." + key.getUnlocalizedName(), value));

        aNBT.setBoolean(recipeRunningNBTTag, recipeRunning);
        aNBT.setLong(recipeEUOutputNBTTag, euOutput);
        aNBT.setDouble(recipeSuccessChanceNBTTag, successChance);

        // Store damage values/stack sizes of GT items being outputted.
        NBTTagCompound itemStackListNBTTag = new NBTTagCompound();
        itemStackListNBTTag.setLong(numberOfItemsNBTTag, outputItems.size());

        int index = 0;
        for (ItemStackLong itemStackLong : outputItems) {
            // Save stack size to NBT.
            itemStackListNBTTag.setLong(index + stackSizeNBTTag, itemStackLong.stackSize);

            // Save ItemStack to NBT.
            aNBT.setTag(index + itemStackNBTTag, itemStackLong.itemStack.writeToNBT(new NBTTagCompound()));

            index++;
        }

        aNBT.setTag(itemOutputNBTTag, itemStackListNBTTag);

        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {

        // Load the quantity of fluid stored inside the controller.
        validFluidMap.forEach(
                (key, value) -> validFluidMap.put(key, aNBT.getLong("stored." + key.getUnlocalizedName())));

        // Load other stuff from NBT.
        recipeRunning = aNBT.getBoolean(recipeRunningNBTTag);
        euOutput = aNBT.getLong(recipeEUOutputNBTTag);
        successChance = aNBT.getDouble(recipeSuccessChanceNBTTag);

        // Load damage values/stack sizes of GT items being outputted and convert back to items.
        NBTTagCompound tempItemTag = aNBT.getCompoundTag(itemOutputNBTTag);

        // Iterate over all stored items.
        for (int index = 0; index < tempItemTag.getInteger(numberOfItemsNBTTag); index++) {

            // Load stack size from NBT.
            long stackSize = tempItemTag.getLong(index + stackSizeNBTTag);

            // Load ItemStack from NBT.
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag(index + itemStackNBTTag));

            outputItems.add(new ItemStackLong(itemStack, stackSize));
        }

        super.loadNBTData(aNBT);
    }

    @SideOnly(Side.CLIENT)
    private void renderQuad(
            double x, double y, double z, int side, double minU, double maxU, double minV, double maxV) {
        // spotless:off
        Tessellator tes = Tessellator.instance;
        tes.addVertexWithUV(x + 3 - 0.5, y    , z + 7, maxU, maxV);
        tes.addVertexWithUV(x + 3 - 0.5, y + 4, z + 7, maxU, minV);
        tes.addVertexWithUV(x - 3 + 0.5, y + 4, z + 7, minU, minV);
        tes.addVertexWithUV(x - 3 + 0.5, y    , z + 7, minU, maxV);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderInWorld(IBlockAccess aWorld, int x, int y, int z, Block block, RenderBlocks renderer) {
        Tessellator tes = Tessellator.instance;
//        IIcon texture = Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        IIcon texture = Blocks.stained_hardened_clay.getIcon(0, 4);
        float size = 2.0f;
        //if (getBaseMetaTileEntity().isActive()) {
        if (true) {
            double minU = texture.getMinU();
            double maxU = texture.getMaxU();
            double minV = texture.getMinV();
            double maxV = texture.getMaxV();
            double xOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetX;
            double zOffset = 16 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            tes.setColorOpaque_F(1f, 1f, 1f);
            //   5---6
            //  /|  /|    |  /
            // 2-4-1 7    y z
            // |/  |/     |/
            // 3---0      0---x---

            //Add the rendering calls here (Can and should use helper functions that do the vertex calls)

            double X[] = {x + xOffset - 0.5 - size, x + xOffset - 0.5 - size, x + xOffset + 0.5 + size, x + xOffset + 0.5 + size,
                         x + xOffset + 0.5 + size, x + xOffset + 0.5 + size, x + xOffset - 0.5 - size, x + xOffset - 0.5 - size};
            double Y[] = {y + 0.5 + size, y - 0.5 - size, y - 0.5 - size, y + 0.5 + size,
                         y + 0.5 + size, y - 0.5 - size, y - 0.5 - size, y + 0.5 + size};
            double Z[] = {z + zOffset + 0.5 + size, z + zOffset + 0.5 + size, z + zOffset + 0.5 + size, z + zOffset + 0.5 + size,
                         z + zOffset - 0.5 - size, z + zOffset - 0.5 - size, z + zOffset - 0.5 - size, z + zOffset - 0.5 - size};
//            int index = 0;
//            for (double[] X : arrayTest) {
//                tes.addVertexWithUV(X[0], Y[0], Z[0], maxU, maxV);
//                tes.addVertexWithUV(X[1], Y[1], Z[1], maxU, minV);
//                tes.addVertexWithUV(X[2], Y[2], Z[2], minU, minV);
//                tes.addVertexWithUV(X[3], Y[3], Z[3], minU, maxV);
//            }

            double[][] testArray = new double[][] {new double[]{-1.39,-1.88,1.06},new double[]{-1.54,-1.86,0.87},new double[]{-1.42,-1.98,0.81},new double[]{-1.27,-2.0,0.98},new double[]{-1.27,1.9,1.17},new double[]{-1.37,1.75,1.3},new double[]{-1.17,1.76,1.46},new double[]{-1.06,1.91,1.34},new double[]{1.55,-1.71,-1.12},new double[]{1.61,-1.57,-1.23},new double[]{1.77,-1.53,-1.06},new double[]{1.71,-1.67,-0.94},new double[]{-1.55,1.95,0.63},new double[]{-1.71,1.87,0.43},new double[]{-1.79,1.74,0.6},new double[]{-1.64,1.81,0.79},new double[]{-1.38,-1.3,1.73},new double[]{-1.59,-1.32,1.52},new double[]{-1.47,-1.54,1.43},new double[]{-1.27,-1.53,1.63},new double[]{-1.94,-1.41,-0.91},new double[]{-1.81,-1.59,-0.89},new double[]{-1.91,-1.58,-0.67},new double[]{-2.04,-1.4,-0.68},new double[]{1.33,0.72,-2.07},new double[]{1.07,0.68,-2.23},new double[]{1.0,0.96,-2.16},new double[]{1.25,1.01,-2.0},new double[]{-2.34,0.73,-0.76},new double[]{-2.43,0.66,-0.53},new double[]{-2.37,0.87,-0.49},new double[]{-2.28,0.95,-0.7},new double[]{-2.38,-0.68,-0.68},new double[]{-2.29,-0.95,-0.69},new double[]{-2.34,-0.95,-0.46},new double[]{-2.43,-0.7,-0.44},new double[]{0.26,0.35,2.53},new double[]{0.21,0.57,2.5},new double[]{0.06,0.5,2.52},new double[]{0.1,0.29,2.55},new double[]{-1.77,1.53,1.06},new double[]{-1.91,1.48,0.89},new double[]{-1.94,1.36,1.0},new double[]{-1.81,1.4,1.17},new double[]{0.34,-0.73,-2.44},new double[]{0.37,-0.93,-2.37},new double[]{0.19,-0.9,-2.4},new double[]{0.15,-0.71,-2.46},new double[]{-1.62,-0.48,-1.93},new double[]{-1.38,-0.42,-2.12},new double[]{-1.3,-0.71,-2.1},new double[]{-1.53,-0.77,-1.91},new double[]{-1.75,1.48,-1.16},new double[]{-1.68,1.62,-1.07},new double[]{-1.53,1.65,-1.24},new double[]{-1.58,1.52,-1.33},new double[]{2.2,1.32,0.08},new double[]{2.22,1.29,-0.08},new double[]{2.11,1.46,-0.06},new double[]{2.08,1.49,0.1},new double[]{-0.35,0.12,-2.54},new double[]{-0.39,0.36,-2.51},new double[]{-0.22,0.37,-2.53},new double[]{-0.18,0.15,-2.56},new double[]{0.3,0.12,2.55},new double[]{0.26,0.35,2.53},new double[]{0.1,0.29,2.55},new double[]{0.14,0.07,2.56},new double[]{2.17,1.04,-0.89},new double[]{2.06,1.28,-0.84},new double[]{2.16,1.24,-0.63},new double[]{2.26,1.01,-0.67},new double[]{1.48,-1.21,1.71},new double[]{1.28,-1.22,1.86},new double[]{1.26,-1.4,1.75},new double[]{1.45,-1.39,1.6},new double[]{0.02,-1.56,-2.04},new double[]{-0.13,-1.51,-2.07},new double[]{0.0,-1.42,-2.14},new double[]{0.15,-1.46,-2.1},new double[]{-1.45,-0.1,2.12},new double[]{-1.19,-0.07,2.28},new double[]{-1.22,0.23,2.25},new double[]{-1.47,0.2,2.09},new double[]{0.36,2.52,-0.3},new double[]{0.57,2.5,-0.1},new double[]{0.73,2.44,-0.3},new double[]{0.53,2.46,-0.5},new double[]{1.6,-1.15,-1.64},new double[]{1.78,-1.12,-1.46},new double[]{1.72,-1.29,-1.4},new double[]{1.55,-1.32,-1.56},new double[]{2.53,-0.21,-0.36},new double[]{2.55,-0.18,-0.19},new double[]{2.53,-0.39,-0.2},new double[]{2.5,-0.43,-0.37},new double[]{0.9,1.12,2.13},new double[]{0.81,1.32,2.05},new double[]{0.62,1.23,2.17},new double[]{0.69,1.03,2.25},new double[]{2.25,0.33,1.19},new double[]{2.37,0.37,0.92},new double[]{2.3,0.66,0.94},new double[]{2.18,0.63,1.2},new double[]{-1.8,-1.67,0.74},new double[]{-1.91,-1.62,0.55},new double[]{-1.78,-1.77,0.51},new double[]{-1.67,-1.82,0.69},new double[]{0.17,1.22,-2.25},new double[]{-0.01,1.14,-2.3},new double[]{-0.05,1.32,-2.2},new double[]{0.12,1.41,-2.15},new double[]{0.4,2.54,0.12},new double[]{0.6,2.48,0.31},new double[]{0.77,2.45,0.09},new double[]{0.57,2.5,-0.1},new double[]{1.65,-0.95,-1.72},new double[]{1.84,-0.92,-1.53},new double[]{1.78,-1.12,-1.46},new double[]{1.6,-1.15,-1.64},new double[]{1.06,2.08,1.07},new double[]{1.16,1.98,1.16},new double[]{1.29,1.99,0.98},new double[]{1.2,2.1,0.88},new double[]{-0.02,0.18,-2.56},new double[]{-0.18,0.15,-2.56},new double[]{-0.22,0.37,-2.53},new double[]{-0.06,0.42,-2.53},new double[]{2.09,-0.86,1.22},new double[]{2.03,-1.08,1.13},new double[]{2.17,-1.02,0.92},new double[]{2.23,-0.8,0.99},new double[]{-0.04,-2.37,-0.99},new double[]{-0.25,-2.29,-1.13},new double[]{-0.06,-2.2,-1.32},new double[]{0.15,-2.27,-1.19},new double[]{1.62,1.1,1.66},new double[]{1.5,1.33,1.6},new double[]{1.31,1.28,1.81},new double[]{1.42,1.04,1.87},new double[]{0.8,1.72,1.73},new double[]{0.72,1.84,1.64},new double[]{0.56,1.76,1.78},new double[]{0.64,1.64,1.87},new double[]{-0.62,-1.23,-2.17},new double[]{-0.44,-1.14,-2.26},new double[]{-0.38,-1.32,-2.17},new double[]{-0.54,-1.41,-2.08},new double[]{0.3,0.12,2.55},new double[]{0.26,0.35,2.53},new double[]{0.1,0.29,2.55},new double[]{0.14,0.07,2.56},new double[]{-0.63,1.88,1.63},new double[]{-0.76,1.74,1.73},new double[]{-0.56,1.71,1.84},new double[]{-0.43,1.84,1.74},new double[]{-0.62,2.35,0.84},new double[]{-0.79,2.21,1.04},new double[]{-0.56,2.2,1.21},new double[]{-0.38,2.32,1.03},new double[]{-0.37,1.66,1.92},new double[]{-0.49,1.55,1.99},new double[]{-0.31,1.51,2.05},new double[]{-0.19,1.61,1.99},new double[]{-0.94,-2.3,-0.64},new double[]{-1.09,-2.29,-0.43},new double[]{-1.21,-2.19,-0.57},new double[]{-1.08,-2.2,-0.77},new double[]{-0.34,0.73,2.44},new double[]{-0.15,0.71,2.46},new double[]{-0.19,0.9,2.39},new double[]{-0.37,0.93,2.36},new double[]{1.51,-0.99,1.82},new double[]{1.29,-1.01,1.97},new double[]{1.28,-1.22,1.86},new double[]{1.48,-1.21,1.71},new double[]{2.17,1.19,0.69},new double[]{2.24,1.18,0.46},new double[]{2.11,1.38,0.47},new double[]{2.04,1.4,0.68},new double[]{0.17,0.22,-2.55},new double[]{-0.02,0.18,-2.56},new double[]{-0.06,0.42,-2.53},new double[]{0.13,0.47,-2.52},new double[]{-1.29,2.21,-0.2},new double[]{-1.45,2.09,-0.38},new double[]{-1.6,2.0,-0.16},new double[]{-1.46,2.12,0.02},new double[]{-1.68,-1.59,-1.11},new double[]{-1.55,-1.75,-1.07},new double[]{-1.67,-1.75,-0.86},new double[]{-1.81,-1.6,-0.89},new double[]{-1.69,-0.17,-1.92},new double[]{-1.73,0.14,-1.89},new double[]{-1.5,0.19,-2.08},new double[]{-1.45,-0.12,-2.11},new double[]{-2.41,-0.5,0.72},new double[]{-2.47,-0.5,0.51},new double[]{-2.41,-0.74,0.49},new double[]{-2.35,-0.76,0.7},new double[]{1.05,-0.54,2.28},new double[]{0.83,-0.56,2.36},new double[]{0.85,-0.8,2.28},new double[]{1.07,-0.79,2.19},new double[]{-0.38,2.32,1.03},new double[]{-0.56,2.19,1.21},new double[]{-0.33,2.15,1.36},new double[]{-0.15,2.27,1.19},new double[]{-0.32,-1.47,-2.08},new double[]{-0.17,-1.37,-2.16},new double[]{-0.13,-1.51,-2.07},new double[]{-0.26,-1.6,-1.99},new double[]{-0.17,-1.22,2.25},new double[]{-0.12,-1.41,2.14},new double[]{0.05,-1.32,2.2},new double[]{0.01,-1.14,2.3},new double[]{1.17,-1.34,-1.85},new double[]{1.14,-1.48,-1.76},new double[]{0.96,-1.47,-1.87},new double[]{0.97,-1.33,-1.96},new double[]{0.22,-1.08,-2.32},new double[]{0.25,-1.23,-2.24},new double[]{0.09,-1.2,-2.27},new double[]{0.06,-1.04,-2.35},new double[]{0.43,-2.45,-0.63},new double[]{0.19,-2.43,-0.82},new double[]{0.38,-2.32,-1.03},new double[]{0.62,-2.34,-0.84},new double[]{2.39,-0.48,0.81},new double[]{2.34,-0.73,0.75},new double[]{2.42,-0.66,0.53},new double[]{2.47,-0.41,0.57},new double[]{-1.23,0.76,2.12},new double[]{-0.99,0.76,2.24},new double[]{-0.99,0.98,2.15},new double[]{-1.22,0.98,2.04},new double[]{-1.84,1.78,-0.13},new double[]{-1.93,1.67,-0.29},new double[]{-2.03,1.58,-0.1},new double[]{-1.94,1.68,0.06},new double[]{1.54,1.57,1.33},new double[]{1.68,1.59,1.11},new double[]{1.55,1.75,1.07},new double[]{1.41,1.73,1.27},new double[]{-1.25,0.23,-2.23},new double[]{-1.28,0.52,-2.16},new double[]{-1.05,0.54,-2.28},new double[]{-1.02,0.27,-2.34},new double[]{1.55,-1.32,-1.56},new double[]{1.5,-1.46,-1.48},new double[]{1.32,-1.48,-1.63},new double[]{1.36,-1.34,-1.71},new double[]{0.5,-2.3,1.04},new double[]{0.68,-2.18,1.17},new double[]{0.49,-2.15,1.33},new double[]{0.31,-2.25,1.2},new double[]{-1.45,-0.12,-2.12},new double[]{-1.21,-0.06,-2.27},new double[]{-1.15,-0.35,-2.27},new double[]{-1.39,-0.42,-2.12},new double[]{-0.78,1.32,2.06},new double[]{-0.59,1.29,2.14},new double[]{-0.6,1.43,2.05},new double[]{-0.78,1.46,1.97},new double[]{-0.11,2.09,1.49},new double[]{-0.28,1.97,1.63},new double[]{-0.08,1.9,1.72},new double[]{0.09,2.01,1.59},new double[]{-1.97,0.39,-1.6},new double[]{-2.15,0.33,-1.36},new double[]{-2.13,0.61,-1.29},new double[]{-1.95,0.67,-1.53},new double[]{2.32,-0.57,-0.95},new double[]{2.41,-0.52,-0.74},new double[]{2.35,-0.74,-0.73},new double[]{2.26,-0.79,-0.92},new double[]{-0.98,-0.89,-2.2},new double[]{-0.77,-0.81,-2.31},new double[]{-0.69,-1.03,-2.24},new double[]{-0.9,-1.12,-2.13},new double[]{0.67,0.02,-2.48},new double[]{0.71,-0.26,-2.46},new double[]{0.48,-0.27,-2.51},new double[]{0.44,-0.01,-2.53},new double[]{-0.62,-0.3,2.47},new double[]{-0.57,-0.57,2.44},new double[]{-0.34,-0.52,2.49},new double[]{-0.39,-0.26,2.52},new double[]{0.3,-1.94,1.66},new double[]{0.45,-1.84,1.73},new double[]{0.3,-1.79,1.81},new double[]{0.15,-1.88,1.74},new double[]{-1.27,-1.53,1.63},new double[]{-1.15,-1.72,1.53},new double[]{-0.94,-1.68,1.7},new double[]{-1.05,-1.49,1.81},new double[]{0.52,-1.18,2.22},new double[]{0.36,-1.16,2.27},new double[]{0.38,-1.32,2.17},new double[]{0.54,-1.34,2.12},new double[]{0.73,1.63,-1.85},new double[]{0.52,1.56,-1.97},new double[]{0.45,1.72,-1.85},new double[]{0.64,1.79,-1.73},new double[]{0.39,0.26,-2.53},new double[]{0.44,-0.01,-2.53},new double[]{0.22,-0.03,-2.56},new double[]{0.17,0.22,-2.55},new double[]{0.44,0.41,2.5},new double[]{0.38,0.64,2.46},new double[]{0.21,0.57,2.5},new double[]{0.26,0.35,2.53},new double[]{1.37,1.53,1.54},new double[]{1.25,1.69,1.47},new double[]{1.08,1.64,1.65},new double[]{1.19,1.47,1.73},new double[]{-0.9,-2.05,-1.26},new double[]{-1.05,-2.08,-1.07},new double[]{-1.16,-1.97,-1.16},new double[]{-1.02,-1.94,-1.33},new double[]{-0.39,-0.26,2.52},new double[]{-0.17,-0.22,2.55},new double[]{-0.22,0.03,2.56},new double[]{-0.43,0.01,2.53},new double[]{-2.5,0.43,0.37},new double[]{-2.46,0.63,0.37},new double[]{-2.49,0.58,0.21},new double[]{-2.53,0.39,0.2},new double[]{-0.27,1.54,-2.04},new double[]{-0.4,1.45,-2.08},new double[]{-0.42,1.58,-1.98},new double[]{-0.29,1.66,-1.93},new double[]{-2.46,0.72,-0.11},new double[]{-2.48,0.65,0.06},new double[]{-2.43,0.83,0.07},new double[]{-2.4,0.91,-0.09},new double[]{-0.91,-2.4,0.11},new double[]{-1.03,-2.33,0.3},new double[]{-1.19,-2.28,0.13},new double[]{-1.07,-2.33,-0.06},new double[]{0.08,1.57,-2.03},new double[]{-0.09,1.48,-2.09},new double[]{-0.13,1.62,-1.99},new double[]{0.03,1.71,-1.92},new double[]{2.41,0.08,0.89},new double[]{2.41,-0.21,0.85},new double[]{2.49,-0.15,0.61},new double[]{2.48,0.12,0.64},new double[]{-2.02,1.57,0.24},new double[]{-2.1,1.48,0.08},new double[]{-2.15,1.38,0.24},new double[]{-2.07,1.46,0.4},new double[]{1.22,-0.23,-2.25},new double[]{1.23,-0.5,-2.2},new double[]{0.98,-0.51,-2.32},new double[]{0.96,-0.24,-2.37},new double[]{0.02,-1.56,-2.04},new double[]{-0.13,-1.51,-2.08},new double[]{0.0,-1.42,-2.14},new double[]{0.15,-1.46,-2.11},new double[]{0.9,-2.39,-0.21},new double[]{1.08,-2.29,-0.43},new double[]{1.28,-2.21,-0.22},new double[]{1.11,-2.31,0.0},new double[]{-0.62,-0.3,2.47},new double[]{-0.57,-0.57,2.44},new double[]{-0.34,-0.52,2.49},new double[]{-0.39,-0.26,2.52},new double[]{-1.37,1.34,1.72},new double[]{-1.17,1.34,1.85},new double[]{-1.14,1.48,1.76},new double[]{-1.33,1.48,1.63},new double[]{-0.52,1.18,-2.22},new double[]{-0.54,1.34,-2.12},new double[]{-0.38,1.32,-2.17},new double[]{-0.36,1.16,-2.27},new double[]{0.39,0.26,-2.52},new double[]{0.17,0.22,-2.55},new double[]{0.13,0.47,-2.52},new double[]{0.34,0.52,-2.49},new double[]{0.75,1.97,-1.47},new double[]{0.56,1.92,-1.61},new double[]{0.49,2.03,-1.5},new double[]{0.66,2.07,-1.37},new double[]{2.5,-0.43,-0.37},new double[]{2.53,-0.39,-0.2},new double[]{2.49,-0.58,-0.21},new double[]{2.46,-0.63,-0.37},new double[]{-0.03,-1.71,1.92},new double[]{0.01,-1.82,1.81},new double[]{0.16,-1.74,1.88},new double[]{0.13,-1.62,1.98},new double[]{-1.21,-0.97,-2.05},new double[]{-0.99,-0.89,-2.2},new double[]{-0.9,-1.12,-2.13},new double[]{-1.1,-1.2,-1.98},new double[]{-1.5,-1.33,-1.6},new double[]{-1.31,-1.28,-1.81},new double[]{-1.19,-1.48,-1.73},new double[]{-1.37,-1.53,-1.54},new double[]{-2.02,1.37,-0.78},new double[]{-2.13,1.31,-0.59},new double[]{-2.05,1.45,-0.54},new double[]{-1.94,1.51,-0.72},new double[]{1.72,1.85,0.45},new double[]{1.8,1.82,0.28},new double[]{1.67,1.93,0.28},new double[]{1.6,1.96,0.45},new double[]{1.65,0.45,-1.92},new double[]{1.58,0.76,-1.88},new double[]{1.8,0.78,-1.65},new double[]{1.88,0.47,-1.69},new double[]{-0.12,-1.66,-1.96},new double[]{-0.26,-1.6,-1.99},new double[]{-0.13,-1.51,-2.08},new double[]{0.02,-1.56,-2.04},new double[]{-1.6,1.15,1.64},new double[]{-1.4,1.17,1.8},new double[]{-1.36,1.34,1.71},new double[]{-1.55,1.32,1.56},new double[]{-0.26,-1.93,-1.67},new double[]{-0.42,-1.85,-1.73},new double[]{-0.27,-1.76,-1.85},new double[]{-0.1,-1.83,-1.79},new double[]{-2.29,-0.03,-1.16},new double[]{-2.41,-0.08,-0.89},new double[]{-2.41,0.21,-0.85},new double[]{-2.3,0.27,-1.11},new double[]{0.12,1.66,1.95},new double[]{-0.02,1.56,2.04},new double[]{0.13,1.51,2.07},new double[]{0.26,1.6,1.99},new double[]{-1.47,1.86,0.99},new double[]{-1.64,1.81,0.79},new double[]{-1.71,1.67,0.94},new double[]{-1.55,1.71,1.12},new double[]{2.47,0.5,-0.51},new double[]{2.5,0.49,-0.32},new double[]{2.53,0.25,-0.34},new double[]{2.5,0.24,-0.53},new double[]{2.01,-1.21,-1.04},new double[]{2.12,-1.15,-0.86},new double[]{2.05,-1.3,-0.83},new double[]{1.94,-1.35,-1.0},new double[]{1.05,1.49,-1.81},new double[]{0.82,1.44,-1.96},new double[]{0.73,1.63,-1.85},new double[]{0.94,1.68,-1.7},new double[]{-2.45,-0.23,0.74},new double[]{-2.46,0.03,0.75},new double[]{-2.51,0.01,0.54},new double[]{-2.5,-0.24,0.53},new double[]{-1.65,0.95,1.72},new double[]{-1.6,1.15,1.64},new double[]{-1.78,1.12,1.46},new double[]{-1.84,0.92,1.53},new double[]{-0.97,-0.01,-2.38},new double[]{-1.02,0.27,-2.34},new double[]{-0.79,0.31,-2.42},new double[]{-0.74,0.04,-2.46},new double[]{-0.73,-1.63,1.84},new double[]{-0.64,-1.79,1.73},new double[]{-0.45,-1.72,1.85},new double[]{-0.52,-1.56,1.97},new double[]{1.42,1.04,1.87},new double[]{1.31,1.28,1.81},new double[]{1.1,1.2,1.98},new double[]{1.21,0.97,2.05},new double[]{-0.73,-1.63,1.84},new double[]{-0.64,-1.79,1.73},new double[]{-0.45,-1.72,1.85},new double[]{-0.52,-1.56,1.97},new double[]{-1.31,-1.28,-1.81},new double[]{-1.1,-1.2,-1.98},new double[]{-1.0,-1.41,-1.9},new double[]{-1.19,-1.48,-1.73},new double[]{2.4,-0.52,-0.74},new double[]{2.46,-0.47,-0.54},new double[]{2.41,-0.68,-0.54},new double[]{2.35,-0.73,-0.72},new double[]{-1.92,0.43,1.65},new double[]{-1.89,0.69,1.59},new double[]{-2.06,0.66,1.38},new double[]{-2.1,0.4,1.42},new double[]{-0.09,-2.01,-1.59},new double[]{-0.26,-1.93,-1.67},new double[]{-0.1,-1.83,-1.79},new double[]{0.08,-1.9,-1.72},new double[]{0.32,2.45,-0.69},new double[]{0.53,2.46,-0.5},new double[]{0.68,2.38,-0.67},new double[]{0.48,2.37,-0.86},new double[]{-1.97,0.39,-1.6},new double[]{-1.95,0.67,-1.53},new double[]{-1.75,0.71,-1.74},new double[]{-1.75,0.44,-1.82},new double[]{0.88,-1.21,2.08},new double[]{0.69,-1.2,2.16},new double[]{0.7,-1.37,2.06},new double[]{0.88,-1.39,1.97},new double[]{0.08,-2.47,0.69},new double[]{0.3,-2.39,0.87},new double[]{0.11,-2.34,1.05},new double[]{-0.1,-2.41,0.88},new double[]{0.97,-1.33,-1.96},new double[]{0.96,-1.47,-1.87},new double[]{0.78,-1.46,-1.97},new double[]{0.78,-1.32,-2.06},new double[]{0.27,-1.54,2.04},new double[]{0.29,-1.66,1.94},new double[]{0.42,-1.58,1.98},new double[]{0.4,-1.46,2.08},new double[]{0.35,-0.12,2.54},new double[]{0.18,-0.15,2.56},new double[]{0.22,-0.37,2.53},new double[]{0.39,-0.36,2.51},new double[]{-1.79,1.74,0.6},new double[]{-1.92,1.66,0.41},new double[]{-1.98,1.53,0.57},new double[]{-1.86,1.6,0.75},new double[]{-0.77,0.97,2.25},new double[]{-0.56,0.95,2.32},new double[]{-0.58,1.13,2.23},new double[]{-0.78,1.16,2.16},new double[]{-1.27,1.9,1.17},new double[]{-1.37,1.75,1.3},new double[]{-1.17,1.76,1.46},new double[]{-1.06,1.91,1.34},new double[]{-0.01,-2.5,-0.59},new double[]{-0.21,-2.53,-0.36},new double[]{-0.42,-2.47,-0.54},new double[]{-0.23,-2.44,-0.77},new double[]{0.3,-0.51,-2.5},new double[]{0.34,-0.73,-2.44},new double[]{0.15,-0.71,-2.46},new double[]{0.11,-0.51,-2.51},new double[]{-0.02,2.56,0.16},new double[]{0.21,2.53,0.36},new double[]{0.4,2.53,0.12},new double[]{0.18,2.56,-0.08},new double[]{-1.36,2.01,0.83},new double[]{-1.55,1.95,0.63},new double[]{-1.64,1.81,0.79},new double[]{-1.47,1.86,0.99},new double[]{-0.08,-1.57,2.03},new double[]{-0.03,-1.71,1.92},new double[]{0.13,-1.62,1.98},new double[]{0.09,-1.48,2.09},new double[]{2.15,-0.33,1.36},new double[]{2.13,-0.61,1.3},new double[]{2.28,-0.55,1.05},new double[]{2.3,-0.27,1.11},new double[]{-1.75,0.72,-1.74},new double[]{-1.72,0.96,-1.64},new double[]{-1.51,1.0,-1.82},new double[]{-1.53,0.75,-1.92},new double[]{1.0,0.96,-2.16},new double[]{0.74,0.9,-2.28},new double[]{0.67,1.15,-2.19},new double[]{0.91,1.22,-2.07},new double[]{2.53,0.25,-0.34},new double[]{2.55,0.26,-0.17},new double[]{2.56,0.03,-0.18},new double[]{2.55,0.02,-0.35},new double[]{0.3,-1.94,1.66},new double[]{0.46,-1.84,1.73},new double[]{0.3,-1.79,1.81},new double[]{0.15,-1.88,1.74},new double[]{1.92,-1.66,-0.41},new double[]{1.98,-1.53,-0.57},new double[]{2.07,-1.46,-0.4},new double[]{2.02,-1.57,-0.24},new double[]{-0.64,-1.64,-1.87},new double[]{-0.48,-1.56,-1.99},new double[]{-0.41,-1.68,-1.9},new double[]{-0.56,-1.77,-1.78},new double[]{2.46,-0.03,-0.75},new double[]{2.51,-0.01,-0.54},new double[]{2.5,-0.25,-0.54},new double[]{2.44,-0.28,-0.75},new double[]{-0.54,0.08,-2.51},new double[]{-0.35,0.12,-2.54},new double[]{-0.3,-0.12,-2.55},new double[]{-0.49,-0.17,-2.51},new double[]{-1.71,0.46,1.86},new double[]{-1.69,0.73,1.79},new double[]{-1.89,0.69,1.59},new double[]{-1.92,0.43,1.65},new double[]{0.52,-1.18,2.22},new double[]{0.36,-1.16,2.27},new double[]{0.38,-1.32,2.17},new double[]{0.54,-1.34,2.12},new double[]{-1.78,-1.32,1.29},new double[]{-1.93,-1.31,1.07},new double[]{-1.81,-1.52,1.0},new double[]{-1.65,-1.54,1.22},new double[]{1.2,-1.17,-1.94},new double[]{1.17,-1.34,-1.85},new double[]{0.97,-1.33,-1.96},new double[]{0.98,-1.17,-2.06},new double[]{-0.98,1.92,-1.39},new double[]{-0.8,1.91,-1.52},new double[]{-0.93,1.79,-1.59},new double[]{-1.1,1.8,-1.46},new double[]{-1.8,-0.78,1.65},new double[]{-1.99,-0.79,1.41},new double[]{-1.89,-1.07,1.36},new double[]{-1.71,-1.06,1.59},new double[]{-1.65,0.95,1.72},new double[]{-1.6,1.15,1.64},new double[]{-1.78,1.12,1.46},new double[]{-1.84,0.92,1.53},new double[]{1.65,-0.96,-1.72},new double[]{1.84,-0.93,-1.53},new double[]{1.79,-1.12,-1.47},new double[]{1.6,-1.15,-1.64},new double[]{-1.37,-1.53,-1.54},new double[]{-1.19,-1.48,-1.73},new double[]{-1.08,-1.64,-1.66},new double[]{-1.25,-1.7,-1.47},new double[]{0.27,-1.54,2.04},new double[]{0.29,-1.66,1.93},new double[]{0.42,-1.58,1.98},new double[]{0.4,-1.45,2.08},new double[]{-0.61,-2.28,1.0},new double[]{-0.42,-2.25,1.16},new double[]{-0.55,-2.16,1.28},new double[]{-0.73,-2.19,1.12},new double[]{-1.77,1.53,1.06},new double[]{-1.9,1.48,0.88},new double[]{-1.94,1.35,1.0},new double[]{-1.81,1.4,1.17},new double[]{1.71,-1.67,-0.94},new double[]{1.77,-1.53,-1.06},new double[]{1.91,-1.48,-0.89},new double[]{1.86,-1.61,-0.75},new double[]{-0.07,-1.13,-2.3},new double[]{0.06,-1.04,-2.35},new double[]{0.09,-1.2,-2.27},new double[]{-0.03,-1.28,-2.22},new double[]{-0.63,-0.48,-2.44},new double[]{-0.43,-0.41,-2.5},new double[]{-0.38,-0.64,-2.46},new double[]{-0.57,-0.72,-2.4},new double[]{2.04,1.45,-0.59},new double[]{1.91,1.62,-0.55},new double[]{2.0,1.57,-0.38},new double[]{2.12,1.4,-0.41},new double[]{0.94,1.68,-1.7},new double[]{0.73,1.63,-1.84},new double[]{0.64,1.79,-1.73},new double[]{0.84,1.84,-1.58},new double[]{-2.43,-0.7,-0.44},new double[]{-2.34,-0.95,-0.46},new double[]{-2.38,-0.94,-0.24},new double[]{-2.46,-0.7,-0.23},new double[]{-0.57,-2.5,0.1},new double[]{-0.73,-2.44,0.3},new double[]{-0.91,-2.4,0.11},new double[]{-0.76,-2.45,-0.09},new double[]{0.44,2.36,0.93},new double[]{0.62,2.26,1.06},new double[]{0.79,2.29,0.85},new double[]{0.62,2.39,0.71},new double[]{0.68,-2.18,1.17},new double[]{0.84,-2.05,1.29},new double[]{0.65,-2.03,1.43},new double[]{0.49,-2.14,1.32},new double[]{-0.94,-2.3,-0.64},new double[]{-1.09,-2.29,-0.43},new double[]{-1.21,-2.19,-0.57},new double[]{-1.08,-2.2,-0.77},new double[]{-1.19,-2.09,-0.88},new double[]{-1.32,-2.09,-0.69},new double[]{-1.41,-1.99,-0.8},new double[]{-1.29,-1.99,-0.98},new double[]{2.15,0.79,-1.17},new double[]{2.05,1.06,-1.12},new double[]{2.17,1.04,-0.89},new double[]{2.26,0.78,-0.93},new double[]{1.21,0.06,2.27},new double[]{0.97,0.01,2.38},new double[]{1.02,-0.27,2.34},new double[]{1.26,-0.23,2.23},new double[]{0.49,-1.0,2.31},new double[]{0.33,-0.98,2.35},new double[]{0.36,-1.16,2.27},new double[]{0.52,-1.18,2.22},new double[]{-0.11,0.51,2.51},new double[]{0.06,0.5,2.52},new double[]{0.02,0.69,2.47},new double[]{-0.15,0.71,2.46},new double[]{2.07,-1.46,-0.4},new double[]{2.11,-1.35,-0.54},new double[]{2.19,-1.28,-0.38},new double[]{2.15,-1.38,-0.24},new double[]{-1.58,1.96,-0.54},new double[]{-1.68,1.82,-0.68},new double[]{-1.82,1.75,-0.48},new double[]{-1.72,1.88,-0.33},new double[]{-0.37,1.66,1.92},new double[]{-0.49,1.55,1.99},new double[]{-0.31,1.51,2.05},new double[]{-0.19,1.61,1.99},new double[]{0.37,-1.67,-1.92},new double[]{0.19,-1.62,-1.99},new double[]{0.32,-1.51,-2.06},new double[]{0.49,-1.55,-1.99},new double[]{-0.63,-0.48,-2.44},new double[]{-0.43,-0.41,-2.5},new double[]{-0.38,-0.64,-2.46},new double[]{-0.57,-0.72,-2.4},new double[]{1.27,1.53,-1.63},new double[]{1.15,1.71,-1.53},new double[]{1.35,1.73,-1.34},new double[]{1.47,1.54,-1.43},new double[]{0.58,1.93,1.59},new double[]{0.42,1.85,1.73},new double[]{0.56,1.76,1.78},new double[]{0.72,1.84,1.64},new double[]{1.44,-1.6,-1.4},new double[]{1.5,-1.46,-1.48},new double[]{1.66,-1.44,-1.33},new double[]{1.61,-1.57,-1.23},new double[]{-0.85,2.33,0.64},new double[]{-1.08,2.29,0.43},new double[]{-1.23,2.16,0.64},new double[]{-1.02,2.2,0.84},new double[]{-0.33,2.15,1.36},new double[]{-0.49,2.02,1.51},new double[]{-0.28,1.97,1.63},new double[]{-0.11,2.09,1.49},new double[]{-1.72,0.18,1.9},new double[]{-1.71,0.47,1.86},new double[]{-1.92,0.43,1.65},new double[]{-1.93,0.15,1.69},new double[]{2.17,1.19,0.69},new double[]{2.24,1.18,0.46},new double[]{2.11,1.38,0.47},new double[]{2.04,1.4,0.68},new double[]{1.8,1.82,0.28},new double[]{1.85,1.78,0.12},new double[]{1.74,1.89,0.13},new double[]{1.67,1.93,0.28},new double[]{1.08,-2.29,-0.43},new double[]{1.23,-2.16,-0.64},new double[]{1.43,-2.09,-0.44},new double[]{1.28,-2.21,-0.22},new double[]{-1.23,2.05,-0.94},new double[]{-1.04,2.06,-1.13},new double[]{-1.17,1.93,-1.23},new double[]{-1.35,1.91,-1.06},new double[]{0.7,-2.32,0.85},new double[]{0.88,-2.2,1.0},new double[]{0.68,-2.18,1.17},new double[]{0.5,-2.3,1.04},new double[]{2.43,-0.83,-0.07},new double[]{2.37,-0.99,-0.09},new double[]{2.39,-0.92,-0.23},new double[]{2.45,-0.76,-0.22},new double[]{1.64,1.87,0.64},new double[]{1.72,1.85,0.45},new double[]{1.6,1.96,0.45},new double[]{1.51,1.98,0.62},new double[]{2.0,1.57,-0.38},new double[]{1.87,1.72,-0.34},new double[]{1.95,1.66,-0.19},new double[]{2.06,1.52,-0.21},new double[]{-0.97,-0.01,-2.38},new double[]{-1.02,0.27,-2.34},new double[]{-0.79,0.31,-2.42},new double[]{-0.74,0.04,-2.46},new double[]{-2.27,-0.78,0.93},new double[]{-2.35,-0.77,0.7},new double[]{-2.26,-1.01,0.67},new double[]{-2.17,-1.04,0.89},new double[]{1.4,0.42,-2.11},new double[]{1.45,0.1,-2.12},new double[]{1.19,0.07,-2.27},new double[]{1.14,0.38,-2.27},new double[]{1.8,0.78,-1.65},new double[]{1.71,1.07,-1.6},new double[]{1.9,1.07,-1.36},new double[]{1.99,0.79,-1.41},new double[]{0.05,-2.55,0.29},new double[]{-0.14,-2.52,0.5},new double[]{-0.36,-2.53,0.3},new double[]{-0.18,-2.56,0.08},new double[]{1.0,0.96,-2.16},new double[]{0.75,0.9,-2.29},new double[]{0.67,1.15,-2.2},new double[]{0.91,1.22,-2.07},new double[]{-0.79,0.31,-2.43},new double[]{-0.83,0.56,-2.37},new double[]{-0.62,0.58,-2.43},new double[]{-0.58,0.33,-2.48},new double[]{-0.5,-0.84,2.37},new double[]{-0.44,-1.08,2.28},new double[]{-0.23,-1.01,2.35},new double[]{-0.28,-0.77,2.43},new double[]{0.62,-2.34,-0.84},new double[]{0.38,-2.32,-1.03},new double[]{0.56,-2.19,-1.21},new double[]{0.79,-2.21,-1.04},new double[]{-2.19,1.28,0.38},new double[]{-2.25,1.21,0.23},new double[]{-2.28,1.12,0.37},new double[]{-2.22,1.18,0.51},new double[]{-1.71,0.47,1.86},new double[]{-1.48,0.49,2.04},new double[]{-1.46,0.75,1.97},new double[]{-1.69,0.73,1.8},new double[]{-1.36,1.74,1.3},new double[]{-1.55,1.71,1.12},new double[]{-1.61,1.57,1.23},new double[]{-1.44,1.6,1.4},new double[]{-1.71,0.46,1.86},new double[]{-1.48,0.49,2.04},new double[]{-1.46,0.75,1.97},new double[]{-1.69,0.73,1.79},new double[]{-0.47,2.52,0.18},new double[]{-0.23,2.53,0.39},new double[]{-0.02,2.56,0.16},new double[]{-0.26,2.55,-0.05},new double[]{-1.65,-0.45,1.91},new double[]{-1.58,-0.76,1.88},new double[]{-1.33,-0.72,2.07},new double[]{-1.4,-0.42,2.11},new double[]{1.25,1.01,-2.01},new double[]{1.0,0.96,-2.16},new double[]{0.91,1.22,-2.07},new double[]{1.15,1.27,-1.92},new double[]{2.4,-0.91,0.09},new double[]{2.33,-1.07,0.07},new double[]{2.37,-0.99,-0.09},new double[]{2.43,-0.83,-0.07},new double[]{1.21,0.06,2.26},new double[]{0.97,0.01,2.38},new double[]{1.02,-0.27,2.34},new double[]{1.25,-0.23,2.23},new double[]{0.41,-1.26,-2.2},new double[]{0.43,-1.4,-2.11},new double[]{0.27,-1.37,-2.16},new double[]{0.25,-1.23,-2.24},new double[]{-1.27,1.9,1.17},new double[]{-1.36,1.74,1.3},new double[]{-1.17,1.76,1.46},new double[]{-1.06,1.91,1.34},new double[]{-2.32,0.57,0.94},new double[]{-2.26,0.79,0.92},new double[]{-2.35,0.73,0.72},new double[]{-2.4,0.52,0.74},new double[]{-0.69,-1.03,-2.25},new double[]{-0.5,-0.95,-2.34},new double[]{-0.44,-1.14,-2.26},new double[]{-0.62,-1.23,-2.17},new double[]{-1.58,1.95,-0.54},new double[]{-1.68,1.82,-0.68},new double[]{-1.81,1.75,-0.48},new double[]{-1.72,1.87,-0.33},new double[]{1.08,-2.29,-0.43},new double[]{1.23,-2.16,-0.64},new double[]{1.43,-2.09,-0.44},new double[]{1.28,-2.21,-0.22},new double[]{-1.2,1.17,1.94},new double[]{-0.98,1.17,2.06},new double[]{-0.97,1.33,1.96},new double[]{-1.17,1.34,1.85},new double[]{-1.07,0.79,-2.19},new double[]{-1.08,1.02,-2.09},new double[]{-0.87,1.02,-2.19},new double[]{-0.85,0.8,-2.28},new double[]{-0.05,1.32,-2.2},new double[]{-0.21,1.24,-2.24},new double[]{-0.24,1.4,-2.14},new double[]{-0.09,1.48,-2.09},new double[]{-0.44,0.01,2.53},new double[]{-0.22,0.03,2.56},new double[]{-0.26,0.28,2.54},new double[]{-0.48,0.27,2.51},new double[]{-0.91,-2.4,0.11},new double[]{-1.03,-2.33,0.3},new double[]{-1.19,-2.28,0.13},new double[]{-1.07,-2.33,-0.06},new double[]{-0.01,-2.5,-0.59},new double[]{-0.21,-2.54,-0.36},new double[]{-0.42,-2.48,-0.54},new double[]{-0.23,-2.44,-0.77},new double[]{-1.25,-1.69,-1.47},new double[]{-1.08,-1.64,-1.65},new double[]{-0.97,-1.78,-1.57},new double[]{-1.13,-1.83,-1.4},new double[]{1.72,-1.29,-1.4},new double[]{1.88,-1.26,-1.22},new double[]{1.81,-1.4,-1.17},new double[]{1.66,-1.44,-1.33},new double[]{-1.92,0.43,1.65},new double[]{-1.89,0.7,1.6},new double[]{-2.06,0.66,1.38},new double[]{-2.1,0.4,1.42},new double[]{-0.62,2.34,0.84},new double[]{-0.79,2.21,1.04},new double[]{-0.56,2.19,1.21},new double[]{-0.38,2.32,1.03},new double[]{1.28,2.2,-0.3},new double[]{1.4,2.14,-0.14},new double[]{1.48,2.08,-0.29},new double[]{1.36,2.13,-0.45},new double[]{1.41,-2.01,0.74},new double[]{1.52,-1.87,0.87},new double[]{1.35,-1.91,1.06},new double[]{1.23,-2.05,0.94},new double[]{-1.75,-1.85,0.31},new double[]{-1.83,-1.79,0.17},new double[]{-1.71,-1.9,0.15},new double[]{-1.63,-1.96,0.28},new double[]{-1.45,2.09,-0.38},new double[]{-1.58,1.96,-0.54},new double[]{-1.72,1.88,-0.33},new double[]{-1.6,2.0,-0.16},new double[]{0.49,-1.55,-1.99},new double[]{0.31,-1.51,-2.05},new double[]{0.43,-1.4,-2.11},new double[]{0.6,-1.43,-2.04},new double[]{-2.25,0.36,1.19},new double[]{-2.2,0.61,1.16},new double[]{-2.32,0.57,0.94},new double[]{-2.36,0.32,0.96},new double[]{0.78,2.4,0.49},new double[]{0.94,2.3,0.64},new double[]{1.09,2.29,0.43},new double[]{0.94,2.38,0.27},new double[]{0.07,-0.28,-2.55},new double[]{0.11,-0.51,-2.52},new double[]{-0.06,-0.5,-2.52},new double[]{-0.1,-0.29,-2.55},new double[]{2.25,-1.21,-0.23},new double[]{2.28,-1.12,-0.37},new double[]{2.33,-1.06,-0.23},new double[]{2.3,-1.14,-0.1},new double[]{-1.41,-1.88,-1.02},new double[]{-1.29,-1.99,-0.98},new double[]{-1.41,-1.99,-0.8},new double[]{-1.53,-1.88,-0.83},new double[]{-0.75,1.77,-1.7},new double[]{-0.59,1.74,-1.79},new double[]{-0.71,1.64,-1.84},new double[]{-0.87,1.66,-1.75},new double[]{0.43,2.02,1.52},new double[]{0.26,1.93,1.67},new double[]{0.42,1.85,1.73},new double[]{0.58,1.93,1.59},new double[]{0.91,2.4,-0.11},new double[]{1.07,2.33,0.06},new double[]{1.18,2.27,-0.13},new double[]{1.03,2.33,-0.3},new double[]{-2.28,0.55,-1.05},new double[]{-2.39,0.48,-0.81},new double[]{-2.34,0.73,-0.75},new double[]{-2.23,0.8,-0.99},new double[]{0.76,-0.76,-2.33},new double[]{0.77,-0.97,-2.25},new double[]{0.56,-0.95,-2.32},new double[]{0.54,-0.75,-2.4},new double[]{-2.03,-0.59,-1.46},new double[]{-1.92,-0.88,-1.45},new double[]{-2.08,-0.92,-1.19},new double[]{-2.18,-0.63,-1.2},new double[]{0.31,1.49,-2.07},new double[]{0.12,1.41,-2.15},new double[]{0.08,1.57,-2.03},new double[]{0.26,1.65,-1.95},new double[]{-0.58,0.33,-2.48},new double[]{-0.62,0.58,-2.43},new double[]{-0.43,0.58,-2.46},new double[]{-0.39,0.36,-2.51},new double[]{0.31,-2.09,1.45},new double[]{0.47,-1.99,1.55},new double[]{0.3,-1.94,1.66},new double[]{0.14,-2.03,1.56},new double[]{0.76,-1.74,-1.73},new double[]{0.56,-1.71,-1.83},new double[]{0.67,-1.58,-1.91},new double[]{0.87,-1.6,-1.81},new double[]{-2.4,0.52,0.74},new double[]{-2.35,0.73,0.72},new double[]{-2.41,0.68,0.54},new double[]{-2.46,0.47,0.54},new double[]{1.2,2.1,0.88},new double[]{1.29,1.99,0.98},new double[]{1.41,1.99,0.8},new double[]{1.32,2.09,0.69},new double[]{0.27,2.33,-1.03},new double[]{0.42,2.25,-1.16},new double[]{0.23,2.19,-1.31},new double[]{0.07,2.27,-1.19},new double[]{-0.46,1.84,-1.73},new double[]{-0.3,1.79,-1.81},new double[]{-0.44,1.7,-1.87},new double[]{-0.59,1.74,-1.8},new double[]{-0.63,-0.48,-2.44},new double[]{-0.44,-0.41,-2.5},new double[]{-0.38,-0.64,-2.46},new double[]{-0.57,-0.72,-2.4},new double[]{-0.91,-2.05,-1.26},new double[]{-1.06,-2.08,-1.07},new double[]{-1.16,-1.98,-1.16},new double[]{-1.02,-1.94,-1.33},new double[]{-1.65,-0.45,1.91},new double[]{-1.58,-0.76,1.88},new double[]{-1.33,-0.72,2.07},new double[]{-1.4,-0.42,2.11},new double[]{-2.45,-0.41,-0.66},new double[]{-2.49,-0.43,-0.43},new double[]{-2.53,-0.17,-0.4},new double[]{-2.48,-0.12,-0.64},new double[]{-1.41,-1.88,-1.02},new double[]{-1.29,-1.99,-0.98},new double[]{-1.41,-1.99,-0.8},new double[]{-1.53,-1.88,-0.83},new double[]{2.08,-1.04,-1.08},new double[]{2.2,-0.98,-0.89},new double[]{2.12,-1.15,-0.86},new double[]{2.01,-1.21,-1.04},new double[]{2.53,0.25,-0.34},new double[]{2.55,0.26,-0.17},new double[]{2.56,0.03,-0.18},new double[]{2.54,0.02,-0.35},new double[]{-0.08,-0.71,2.47},new double[]{-0.03,-0.93,2.39},new double[]{0.14,-0.86,2.41},new double[]{0.1,-0.65,2.48},new double[]{0.23,-2.53,-0.39},new double[]{0.02,-2.56,-0.16},new double[]{-0.21,-2.53,-0.36},new double[]{-0.01,-2.5,-0.59},new double[]{2.16,-1.39,0.07},new double[]{2.21,-1.3,-0.09},new double[]{2.26,-1.22,0.05},new double[]{2.2,-1.3,0.21},new double[]{-2.57,0.0,0.02},new double[]{-2.56,-0.03,0.18},new double[]{-2.56,0.18,0.19},new double[]{-2.56,0.23,0.03},new double[]{0.99,-0.98,-2.15},new double[]{0.98,-1.17,-2.06},new double[]{0.78,-1.16,-2.16},new double[]{0.77,-0.97,-2.25},new double[]{2.17,-1.02,0.92},new double[]{2.1,-1.21,0.85},new double[]{2.21,-1.14,0.65},new double[]{2.28,-0.95,0.7},new double[]{0.32,2.45,-0.69},new double[]{0.53,2.46,-0.5},new double[]{0.68,2.38,-0.67},new double[]{0.48,2.37,-0.86},new double[]{0.4,2.53,0.12},new double[]{0.6,2.48,0.31},new double[]{0.76,2.45,0.09},new double[]{0.57,2.5,-0.1},new double[]{2.45,0.23,-0.74},new double[]{2.5,0.24,-0.53},new double[]{2.51,-0.01,-0.54},new double[]{2.46,-0.03,-0.74},new double[]{-0.39,0.36,-2.51},new double[]{-0.43,0.58,-2.46},new double[]{-0.26,0.59,-2.49},new double[]{-0.22,0.37,-2.53},new double[]{2.55,0.2,0.19},new double[]{2.56,-0.05,0.17},new double[]{2.57,-0.0,-0.02},new double[]{2.56,0.23,0.0},new double[]{-2.34,0.98,0.37},new double[]{-2.28,1.12,0.37},new double[]{-2.33,1.06,0.23},new double[]{-2.39,0.92,0.23},new double[]{2.0,-1.56,0.42},new double[]{2.09,-1.47,0.24},new double[]{2.14,-1.38,0.37},new double[]{2.05,-1.45,0.55},new double[]{-0.1,0.65,-2.48},new double[]{-0.26,0.59,-2.48},new double[]{-0.3,0.79,-2.42},new double[]{-0.14,0.86,-2.41},new double[]{-0.67,2.44,0.42},new double[]{-0.85,2.33,0.64},new double[]{-0.62,2.34,0.84},new double[]{-0.43,2.45,0.63},new double[]{1.95,-0.67,1.53},new double[]{1.75,-0.72,1.74},new double[]{1.72,-0.96,1.64},new double[]{1.92,-0.92,1.44},new double[]{-0.44,-0.41,-2.5},new double[]{-0.26,-0.35,-2.53},new double[]{-0.21,-0.57,-2.5},new double[]{-0.38,-0.64,-2.46},new double[]{2.53,0.47,0.02},new double[]{2.52,0.48,-0.15},new double[]{2.47,0.7,-0.14},new double[]{2.47,0.7,0.04},new double[]{2.22,-1.23,0.41},new double[]{2.14,-1.38,0.37},new double[]{2.21,-1.3,0.21},new double[]{2.28,-1.15,0.23},new double[]{2.28,0.95,0.69},new double[]{2.34,0.95,0.46},new double[]{2.23,1.18,0.46},new double[]{2.17,1.19,0.69},new double[]{0.78,-1.32,-2.06},new double[]{0.78,-1.46,-1.97},new double[]{0.6,-1.43,-2.05},new double[]{0.59,-1.29,-2.14},new double[]{2.46,-0.72,0.11},new double[]{2.4,-0.91,0.09},new double[]{2.43,-0.83,-0.07},new double[]{2.49,-0.65,-0.06},new double[]{0.47,-2.52,-0.18},new double[]{0.7,-2.47,0.03},new double[]{0.49,-2.5,0.26},new double[]{0.26,-2.55,0.05},new double[]{2.25,0.33,1.18},new double[]{2.37,0.37,0.92},new double[]{2.3,0.66,0.93},new double[]{2.18,0.63,1.2},new double[]{-2.09,0.86,-1.22},new double[]{-2.23,0.8,-0.99},new double[]{-2.17,1.02,-0.92},new double[]{-2.03,1.08,-1.13},new double[]{-2.36,0.99,-0.26},new double[]{-2.4,0.91,-0.09},new double[]{-2.33,1.07,-0.07},new double[]{-2.28,1.15,-0.23},new double[]{-0.23,2.53,0.39},new double[]{-0.43,2.45,0.63},new double[]{-0.19,2.43,0.82},new double[]{0.01,2.5,0.59},new double[]{1.72,-1.29,-1.4},new double[]{1.88,-1.26,-1.22},new double[]{1.81,-1.4,-1.17},new double[]{1.66,-1.44,-1.33},new double[]{2.35,-0.98,-0.37},new double[]{2.39,-0.92,-0.23},new double[]{2.33,-1.06,-0.23},new double[]{2.28,-1.12,-0.37},new double[]{2.17,1.19,0.69},new double[]{2.23,1.18,0.46},new double[]{2.11,1.38,0.46},new double[]{2.04,1.4,0.68},new double[]{0.72,1.49,1.96},new double[]{0.64,1.64,1.87},new double[]{0.47,1.56,1.98},new double[]{0.54,1.41,2.08},new double[]{-1.95,1.09,1.28},new double[]{-1.88,1.26,1.22},new double[]{-2.01,1.21,1.04},new double[]{-2.08,1.04,1.09},new double[]{0.26,1.93,1.67},new double[]{0.1,1.83,1.79},new double[]{0.27,1.76,1.85},new double[]{0.42,1.85,1.73},new double[]{-0.98,1.92,-1.39},new double[]{-0.8,1.91,-1.52},new double[]{-0.93,1.79,-1.59},new double[]{-1.1,1.8,-1.46},new double[]{-0.22,-1.22,-2.25},new double[]{-0.07,-1.13,-2.3},new double[]{-0.03,-1.28,-2.22},new double[]{-0.17,-1.37,-2.16},new double[]{0.64,1.79,-1.73},new double[]{0.45,1.72,-1.85},new double[]{0.38,1.86,-1.73},new double[]{0.56,1.92,-1.61},new double[]{-1.62,-1.1,-1.66},new double[]{-1.42,-1.04,-1.87},new double[]{-1.31,-1.27,-1.81},new double[]{-1.5,-1.33,-1.6},new double[]{-0.24,1.4,-2.14},new double[]{-0.38,1.32,-2.17},new double[]{-0.4,1.46,-2.08},new double[]{-0.27,1.54,-2.04},new double[]{-0.49,1.55,1.99},new double[]{-0.6,1.43,2.05},new double[]{-0.43,1.4,2.11},new double[]{-0.32,1.51,2.06},new double[]{-0.67,1.01,-2.26},new double[]{-0.69,1.2,-2.16},new double[]{-0.52,1.18,-2.22},new double[]{-0.49,1.0,-2.31},new double[]{1.91,0.23,1.7},new double[]{2.1,0.28,1.45},new double[]{2.03,0.59,1.46},new double[]{1.84,0.54,1.71},new double[]{1.65,0.45,-1.91},new double[]{1.7,0.13,-1.92},new double[]{1.45,0.1,-2.12},new double[]{1.4,0.42,-2.11},new double[]{-1.33,-2.16,-0.37},new double[]{-1.43,-2.12,-0.19},new double[]{-1.52,-2.04,-0.32},new double[]{-1.43,-2.07,-0.5},new double[]{-2.55,-0.02,0.35},new double[]{-2.53,0.21,0.36},new double[]{-2.56,0.18,0.19},new double[]{-2.56,-0.03,0.18},new double[]{1.48,-0.49,-2.04},new double[]{1.46,-0.75,-1.97},new double[]{1.23,-0.76,-2.12},new double[]{1.23,-0.51,-2.2},new double[]{-1.71,1.67,0.94},new double[]{-1.86,1.61,0.75},new double[]{-1.91,1.48,0.89},new double[]{-1.77,1.53,1.06},new double[]{1.81,1.6,0.89},new double[]{1.91,1.58,0.67},new double[]{1.77,1.74,0.65},new double[]{1.67,1.75,0.86},new double[]{-0.88,-0.34,2.39},new double[]{-0.62,-0.3,2.47},new double[]{-0.67,-0.02,2.48},new double[]{-0.92,-0.04,2.4},new double[]{-1.49,-1.05,1.81},new double[]{-1.71,-1.07,1.6},new double[]{-1.59,-1.32,1.52},new double[]{-1.38,-1.3,1.73},new double[]{-1.23,0.51,2.2},new double[]{-0.98,0.52,2.32},new double[]{-0.99,0.76,2.24},new double[]{-1.23,0.76,2.12},new double[]{-2.46,0.47,0.54},new double[]{-2.41,0.68,0.54},new double[]{-2.46,0.63,0.37},new double[]{-2.5,0.43,0.37},new double[]{-0.52,1.18,-2.22},new double[]{-0.54,1.34,-2.12},new double[]{-0.38,1.32,-2.17},new double[]{-0.36,1.16,-2.27},new double[]{-0.97,-0.01,-2.38},new double[]{-0.75,0.04,-2.46},new double[]{-0.69,-0.22,-2.46},new double[]{-0.91,-0.29,-2.38},new double[]{1.05,-1.55,1.76},new double[]{0.88,-1.53,1.86},new double[]{0.87,-1.66,1.76},new double[]{1.04,-1.67,1.65},new double[]{-2.41,0.52,0.74},new double[]{-2.35,0.74,0.73},new double[]{-2.42,0.68,0.54},new double[]{-2.47,0.47,0.54},new double[]{1.7,0.13,-1.92},new double[]{1.92,0.16,-1.7},new double[]{1.93,-0.15,-1.68},new double[]{1.71,-0.18,-1.9},new double[]{-0.58,0.33,-2.48},new double[]{-0.62,0.58,-2.43},new double[]{-0.43,0.58,-2.46},new double[]{-0.39,0.36,-2.51},new double[]{-2.1,1.48,0.08},new double[]{-2.16,1.39,-0.07},new double[]{-2.21,1.3,0.09},new double[]{-2.15,1.38,0.24},new double[]{0.31,0.12,2.55},new double[]{0.26,0.35,2.53},new double[]{0.1,0.29,2.55},new double[]{0.14,0.07,2.56},new double[]{-2.26,0.79,0.92},new double[]{-2.2,0.98,0.89},new double[]{-2.29,0.93,0.71},new double[]{-2.35,0.73,0.72},new double[]{1.84,-1.78,0.13},new double[]{1.94,-1.68,-0.06},new double[]{2.03,-1.58,0.1},new double[]{1.93,-1.67,0.29},new double[]{0.27,1.76,1.85},new double[]{0.12,1.66,1.95},new double[]{0.26,1.6,1.99},new double[]{0.41,1.68,1.89},new double[]{-0.37,0.93,2.37},new double[]{-0.19,0.9,2.4},new double[]{-0.22,1.08,2.32},new double[]{-0.39,1.11,2.28},new double[]{1.62,1.1,1.66},new double[]{1.81,1.15,1.42},new double[]{1.67,1.37,1.38},new double[]{1.5,1.33,1.6},new double[]{0.31,-1.51,-2.05},new double[]{0.15,-1.46,-2.1},new double[]{0.27,-1.37,-2.16},new double[]{0.43,-1.4,-2.11},new double[]{2.5,0.43,0.43},new double[]{2.53,0.17,0.41},new double[]{2.55,0.2,0.19},new double[]{2.52,0.45,0.21},new double[]{1.81,-1.33,1.25},new double[]{1.64,-1.36,1.43},new double[]{1.58,-1.52,1.33},new double[]{1.75,-1.48,1.16},new double[]{0.71,-2.42,0.46},new double[]{0.91,-2.31,0.64},new double[]{0.7,-2.32,0.85},new double[]{0.5,-2.43,0.68},new double[]{-0.22,-1.22,-2.25},new double[]{-0.07,-1.13,-2.3},new double[]{-0.03,-1.28,-2.22},new double[]{-0.17,-1.38,-2.16},new double[]{-1.66,-1.9,0.47},new double[]{-1.75,-1.85,0.31},new double[]{-1.63,-1.96,0.28},new double[]{-1.54,-2.01,0.43},new double[]{-0.35,0.12,-2.54},new double[]{-0.18,0.15,-2.56},new double[]{-0.14,-0.07,-2.56},new double[]{-0.3,-0.12,-2.55},new double[]{1.58,0.76,-1.88},new double[]{1.33,0.72,-2.07},new double[]{1.25,1.01,-2.01},new double[]{1.49,1.05,-1.81},new double[]{2.51,-0.01,-0.54},new double[]{2.55,0.02,-0.35},new double[]{2.53,-0.21,-0.36},new double[]{2.5,-0.25,-0.54},new double[]{-0.3,2.39,-0.87},new double[]{-0.11,2.34,-1.05},new double[]{-0.3,2.25,-1.2},new double[]{-0.5,2.29,-1.03},new double[]{-1.22,0.98,2.04},new double[]{-0.99,0.98,2.16},new double[]{-0.99,1.17,2.06},new double[]{-1.2,1.18,1.95},new double[]{-2.36,0.32,0.96},new double[]{-2.32,0.57,0.94},new double[]{-2.4,0.52,0.74},new double[]{-2.44,0.28,0.74},new double[]{0.26,1.65,-1.95},new double[]{0.08,1.57,-2.03},new double[]{0.03,1.71,-1.92},new double[]{0.2,1.78,-1.83},new double[]{-0.19,2.43,0.82},new double[]{-0.38,2.32,1.03},new double[]{-0.15,2.27,1.19},new double[]{0.04,2.37,0.99},new double[]{1.36,-1.34,-1.71},new double[]{1.32,-1.48,-1.63},new double[]{1.14,-1.48,-1.76},new double[]{1.17,-1.34,-1.85},new double[]{1.07,-1.4,1.87},new double[]{0.88,-1.39,1.97},new double[]{0.88,-1.53,1.86},new double[]{1.05,-1.55,1.76},new double[]{-1.23,0.51,2.2},new double[]{-0.98,0.52,2.32},new double[]{-0.99,0.76,2.24},new double[]{-1.23,0.76,2.12},new double[]{-0.35,0.12,-2.54},new double[]{-0.39,0.36,-2.51},new double[]{-0.22,0.37,-2.53},new double[]{-0.18,0.15,-2.56},new double[]{2.13,-1.31,0.59},new double[]{2.05,-1.45,0.54},new double[]{2.13,-1.37,0.37},new double[]{2.21,-1.23,0.41},new double[]{-1.07,-0.63,-2.25},new double[]{-0.84,-0.56,-2.36},new double[]{-0.77,-0.81,-2.31},new double[]{-0.98,-0.89,-2.2},new double[]{1.61,-1.74,0.98},new double[]{1.68,-1.62,1.07},new double[]{1.53,-1.65,1.24},new double[]{1.45,-1.78,1.16},new double[]{1.45,-1.78,1.16},new double[]{1.53,-1.65,1.24},new double[]{1.37,-1.67,1.39},new double[]{1.28,-1.79,1.32},new double[]{1.27,1.53,-1.63},new double[]{1.05,1.49,-1.81},new double[]{0.94,1.68,-1.7},new double[]{1.15,1.72,-1.53},new double[]{-0.44,0.01,2.53},new double[]{-0.22,0.03,2.56},new double[]{-0.26,0.28,2.54},new double[]{-0.48,0.27,2.51},new double[]{1.15,1.27,-1.91},new double[]{0.91,1.22,-2.07},new double[]{0.82,1.44,-1.96},new double[]{1.05,1.49,-1.81},new double[]{0.77,2.15,1.17},new double[]{0.6,2.1,1.35},new double[]{0.75,2.0,1.43},new double[]{0.91,2.05,1.26},new double[]{1.47,1.54,-1.43},new double[]{1.35,1.73,-1.34},new double[]{1.52,1.72,-1.14},new double[]{1.65,1.54,-1.22},new double[]{0.23,-2.53,-0.39},new double[]{-0.01,-2.5,-0.59},new double[]{0.19,-2.43,-0.82},new double[]{0.43,-2.45,-0.63},new double[]{2.49,0.43,0.43},new double[]{2.53,0.17,0.4},new double[]{2.55,0.2,0.19},new double[]{2.52,0.45,0.21},new double[]{-1.72,0.96,-1.64},new double[]{-1.68,1.18,-1.54},new double[]{-1.48,1.21,-1.71},new double[]{-1.51,0.99,-1.82},new double[]{-0.35,0.12,-2.54},new double[]{-0.18,0.15,-2.56},new double[]{-0.14,-0.07,-2.56},new double[]{-0.3,-0.12,-2.55},new double[]{-1.81,1.33,-1.25},new double[]{-1.97,1.28,-1.05},new double[]{-1.89,1.44,-0.97},new double[]{-1.75,1.48,-1.16},new double[]{1.81,-1.75,0.48},new double[]{1.93,-1.67,0.29},new double[]{2.0,-1.55,0.42},new double[]{1.89,-1.63,0.61},new double[]{0.79,-0.31,2.42},new double[]{0.58,-0.33,2.48},new double[]{0.62,-0.58,2.42},new double[]{0.83,-0.56,2.36},new double[]{2.49,-0.15,0.61},new double[]{2.47,-0.42,0.57},new double[]{2.52,-0.35,0.35},new double[]{2.54,-0.1,0.38},new double[]{-1.81,1.75,-0.48},new double[]{-1.89,1.63,-0.61},new double[]{-2.0,1.55,-0.42},new double[]{-1.93,1.67,-0.29},new double[]{0.69,1.03,2.24},new double[]{0.62,1.23,2.16},new double[]{0.44,1.14,2.26},new double[]{0.5,0.94,2.33},new double[]{-2.0,1.55,-0.42},new double[]{-2.05,1.45,-0.54},new double[]{-2.13,1.37,-0.37},new double[]{-2.09,1.47,-0.24},new double[]{-1.69,0.73,1.79},new double[]{-1.65,0.95,1.72},new double[]{-1.84,0.92,1.53},new double[]{-1.89,0.69,1.59},new double[]{-1.4,-2.14,0.14},new double[]{-1.48,-2.08,0.29},new double[]{-1.58,-2.02,0.14},new double[]{-1.51,-2.08,-0.02},new double[]{-0.08,2.47,-0.69},new double[]{0.1,2.41,-0.88},new double[]{-0.11,2.34,-1.05},new double[]{-0.3,2.39,-0.87},new double[]{-0.48,0.27,2.51},new double[]{-0.26,0.28,2.54},new double[]{-0.3,0.51,2.5},new double[]{-0.51,0.52,2.46},new double[]{1.87,-1.14,1.34},new double[]{1.68,-1.18,1.54},new double[]{1.64,-1.36,1.43},new double[]{1.81,-1.33,1.25},new double[]{2.08,-1.04,-1.08},new double[]{2.2,-0.98,-0.89},new double[]{2.12,-1.15,-0.86},new double[]{2.01,-1.21,-1.04},new double[]{1.62,0.48,1.93},new double[]{1.53,0.77,1.91},new double[]{1.3,0.71,2.1},new double[]{1.38,0.42,2.12},new double[]{1.21,2.25,0.23},new double[]{1.33,2.17,0.37},new double[]{1.43,2.13,0.19},new double[]{1.32,2.21,0.04},new double[]{2.45,0.41,0.66},new double[]{2.48,0.12,0.64},new double[]{2.53,0.17,0.4},new double[]{2.49,0.43,0.43},new double[]{0.71,-2.05,-1.37},new double[]{0.49,-2.02,-1.51},new double[]{0.63,-1.88,-1.63},new double[]{0.85,-1.91,-1.5},new double[]{-1.35,-1.73,1.34},new double[]{-1.52,-1.72,1.14},new double[]{-1.39,-1.88,1.06},new double[]{-1.22,-1.88,1.24},new double[]{0.54,-0.08,2.51},new double[]{0.35,-0.12,2.54},new double[]{0.39,-0.36,2.51},new double[]{0.58,-0.33,2.48},new double[]{-0.35,0.12,-2.54},new double[]{-0.18,0.15,-2.56},new double[]{-0.14,-0.07,-2.56},new double[]{-0.3,-0.12,-2.55},new double[]{-0.01,-1.97,1.65},new double[]{0.15,-1.88,1.74},new double[]{0.01,-1.82,1.81},new double[]{-0.15,-1.9,1.72},new double[]{-1.91,-0.23,-1.7},new double[]{-1.95,0.08,-1.67},new double[]{-1.74,0.14,-1.89},new double[]{-1.69,-0.17,-1.93},new double[]{-1.23,2.16,0.64},new double[]{-1.43,2.09,0.44},new double[]{-1.55,1.95,0.63},new double[]{-1.36,2.01,0.83},new double[]{1.91,0.23,1.7},new double[]{1.84,0.54,1.71},new double[]{1.62,0.48,1.93},new double[]{1.69,0.17,1.93},new double[]{0.75,2.0,1.43},new double[]{0.58,1.93,1.59},new double[]{0.72,1.84,1.65},new double[]{0.87,1.9,1.5},new double[]{1.28,-1.22,1.86},new double[]{1.08,-1.22,1.99},new double[]{1.07,-1.4,1.87},new double[]{1.26,-1.4,1.75},new double[]{-0.39,1.11,2.28},new double[]{-0.22,1.08,2.32},new double[]{-0.24,1.23,2.24},new double[]{-0.41,1.26,2.2},new double[]{-1.67,-1.37,-1.38},new double[]{-1.54,-1.57,-1.33},new double[]{-1.68,-1.59,-1.11},new double[]{-1.82,-1.4,-1.15},new double[]{2.54,-0.1,0.38},new double[]{2.52,-0.35,0.35},new double[]{2.55,-0.29,0.15},new double[]{2.56,-0.05,0.17},new double[]{0.48,2.37,-0.86},new double[]{0.68,2.38,-0.67},new double[]{0.8,2.29,-0.83},new double[]{0.61,2.28,-1.0},new double[]{-1.11,2.27,-0.42},new double[]{-1.27,2.15,-0.6},new double[]{-1.45,2.08,-0.38},new double[]{-1.29,2.21,-0.2},new double[]{1.25,1.01,-2.0},new double[]{1.0,0.96,-2.16},new double[]{0.91,1.22,-2.07},new double[]{1.15,1.27,-1.91},new double[]{-0.22,-1.22,-2.25},new double[]{-0.07,-1.13,-2.3},new double[]{-0.03,-1.28,-2.22},new double[]{-0.17,-1.37,-2.16},new double[]{0.61,2.28,-1.0},new double[]{0.73,2.19,-1.12},new double[]{0.55,2.16,-1.28},new double[]{0.42,2.25,-1.16},new double[]{1.52,-1.88,0.87},new double[]{1.68,-1.82,0.68},new double[]{1.76,-1.69,0.8},new double[]{1.61,-1.74,0.98},new double[]{0.62,1.23,2.17},new double[]{0.54,1.41,2.08},new double[]{0.38,1.32,2.17},new double[]{0.44,1.14,2.26},new double[]{-1.6,1.15,1.64},new double[]{-1.4,1.17,1.8},new double[]{-1.37,1.34,1.72},new double[]{-1.55,1.32,1.56},new double[]{2.56,0.23,0.0},new double[]{2.57,-0.0,-0.02},new double[]{2.56,0.03,-0.18},new double[]{2.55,0.26,-0.17},new double[]{-0.83,0.56,-2.36},new double[]{-0.85,0.8,-2.28},new double[]{-0.65,0.8,-2.35},new double[]{-0.62,0.58,-2.42},new double[]{-1.82,1.75,-0.48},new double[]{-1.89,1.63,-0.61},new double[]{-2.0,1.56,-0.42},new double[]{-1.93,1.67,-0.29},new double[]{0.17,0.22,-2.55},new double[]{0.22,-0.03,-2.56},new double[]{0.03,-0.05,-2.57},new double[]{-0.02,0.18,-2.56},new double[]{-1.55,1.32,1.56},new double[]{-1.36,1.34,1.71},new double[]{-1.32,1.48,1.63},new double[]{-1.5,1.46,1.48},new double[]{2.53,-0.21,-0.36},new double[]{2.56,-0.18,-0.19},new double[]{2.53,-0.39,-0.2},new double[]{2.51,-0.43,-0.37},new double[]{-0.69,1.2,-2.16},new double[]{-0.7,1.37,-2.06},new double[]{-0.54,1.34,-2.12},new double[]{-0.52,1.18,-2.22},new double[]{-2.29,-0.03,-1.15},new double[]{-2.41,-0.08,-0.89},new double[]{-2.41,0.21,-0.85},new double[]{-2.3,0.27,-1.11},new double[]{-1.75,1.48,-1.16},new double[]{-1.89,1.44,-0.97},new double[]{-1.82,1.57,-0.9},new double[]{-1.68,1.62,-1.07},new double[]{2.5,0.49,-0.32},new double[]{2.52,0.48,-0.15},new double[]{2.55,0.26,-0.17},new double[]{2.53,0.25,-0.34},new double[]{-2.11,-1.38,-0.47},new double[]{-1.99,-1.56,-0.46},new double[]{-2.04,-1.53,-0.27},new double[]{-2.17,-1.36,-0.26},new double[]{2.38,0.68,0.68},new double[]{2.43,0.7,0.44},new double[]{2.34,0.95,0.46},new double[]{2.29,0.95,0.69},new double[]{-0.39,-0.26,2.52},new double[]{-0.17,-0.22,2.55},new double[]{-0.22,0.03,2.56},new double[]{-0.43,0.01,2.53},new double[]{-0.23,1.78,1.83},new double[]{-0.37,1.66,1.92},new double[]{-0.19,1.61,1.99},new double[]{-0.05,1.72,1.9},new double[]{1.45,0.12,2.11},new double[]{1.38,0.42,2.12},new double[]{1.15,0.35,2.27},new double[]{1.21,0.06,2.26},new double[]{0.54,-0.08,2.51},new double[]{0.35,-0.12,2.54},new double[]{0.39,-0.36,2.51},new double[]{0.58,-0.33,2.48},new double[]{-1.1,1.8,-1.46},new double[]{-0.93,1.79,-1.59},new double[]{-1.04,1.67,-1.65},new double[]{-1.2,1.68,-1.53},new double[]{1.75,-0.44,1.82},new double[]{1.52,-0.48,2.01},new double[]{1.52,-0.75,1.92},new double[]{1.75,-0.71,1.74},new double[]{-1.84,1.78,0.24},new double[]{-1.94,1.68,0.06},new double[]{-2.02,1.57,0.24},new double[]{-1.92,1.66,0.42},new double[]{-1.51,1.0,-1.82},new double[]{-1.49,1.21,-1.71},new double[]{-1.28,1.22,-1.86},new double[]{-1.29,1.01,-1.97},new double[]{1.59,-2.0,-0.23},new double[]{1.71,-1.87,-0.43},new double[]{1.84,-1.78,-0.24},new double[]{1.73,-1.9,-0.04},new double[]{0.07,2.27,-1.19},new double[]{0.23,2.19,-1.32},new double[]{0.04,2.12,-1.45},new double[]{-0.12,2.19,-1.33},new double[]{1.11,-2.28,0.42},new double[]{1.27,-2.15,0.6},new double[]{1.08,-2.19,0.8},new double[]{0.91,-2.31,0.64},new double[]{1.21,0.06,2.26},new double[]{0.97,0.01,2.38},new double[]{1.02,-0.27,2.34},new double[]{1.25,-0.23,2.23},new double[]{-2.26,0.79,0.92},new double[]{-2.2,0.98,0.89},new double[]{-2.29,0.93,0.71},new double[]{-2.35,0.73,0.72},new double[]{-0.7,2.32,-0.85},new double[]{-0.5,2.29,-1.03},new double[]{-0.68,2.18,-1.17},new double[]{-0.88,2.19,-1.0},new double[]{2.21,-0.61,-1.16},new double[]{2.32,-0.57,-0.95},new double[]{2.26,-0.79,-0.92},new double[]{2.15,-0.84,-1.13},new double[]{-1.97,0.39,-1.61},new double[]{-1.95,0.67,-1.53},new double[]{-1.75,0.72,-1.74},new double[]{-1.75,0.44,-1.82},new double[]{-0.47,2.52,0.18},new double[]{-0.7,2.47,-0.03},new double[]{-0.9,2.39,0.21},new double[]{-0.67,2.44,0.42},new double[]{1.04,1.87,-1.42},new double[]{0.84,1.84,-1.58},new double[]{0.75,1.97,-1.47},new double[]{0.93,2.0,-1.32},new double[]{-1.82,1.75,-0.48},new double[]{-1.89,1.63,-0.61},new double[]{-2.0,1.56,-0.42},new double[]{-1.93,1.67,-0.29},new double[]{2.26,1.01,-0.67},new double[]{2.16,1.24,-0.63},new double[]{2.23,1.2,-0.44},new double[]{2.33,0.98,-0.47},new double[]{0.9,1.58,1.82},new double[]{0.8,1.72,1.73},new double[]{0.64,1.64,1.87},new double[]{0.72,1.5,1.96},new double[]{0.17,0.22,-2.55},new double[]{0.22,-0.03,-2.56},new double[]{0.03,-0.05,-2.57},new double[]{-0.02,0.18,-2.56},new double[]{-0.13,-0.47,2.52},new double[]{-0.08,-0.71,2.47},new double[]{0.1,-0.65,2.48},new double[]{0.06,-0.42,2.53},new double[]{-1.92,0.92,-1.44},new double[]{-1.87,1.14,-1.34},new double[]{-1.68,1.18,-1.54},new double[]{-1.72,0.96,-1.64},new double[]{-0.3,0.51,2.5},new double[]{-0.11,0.51,2.51},new double[]{-0.15,0.71,2.46},new double[]{-0.34,0.73,2.44},new double[]{-1.5,-1.33,-1.6},new double[]{-1.31,-1.27,-1.81},new double[]{-1.19,-1.47,-1.73},new double[]{-1.37,-1.53,-1.54},new double[]{0.08,-1.9,-1.72},new double[]{-0.1,-1.83,-1.8},new double[]{0.05,-1.73,-1.9},new double[]{0.23,-1.79,-1.83},new double[]{2.1,0.28,1.45},new double[]{2.25,0.33,1.18},new double[]{2.18,0.63,1.2},new double[]{2.03,0.59,1.46},new double[]{-0.23,-2.44,-0.77},new double[]{-0.44,-2.36,-0.93},new double[]{-0.25,-2.29,-1.13},new double[]{-0.04,-2.37,-0.99},new double[]{-1.84,-0.54,-1.71},new double[]{-1.62,-0.48,-1.93},new double[]{-1.53,-0.78,-1.91},new double[]{-1.74,-0.84,-1.7},new double[]{-1.58,-2.02,0.14},new double[]{-1.63,-1.96,0.28},new double[]{-1.71,-1.9,0.15},new double[]{-1.66,-1.95,0.0},new double[]{0.71,-1.51,1.95},new double[]{0.55,-1.49,2.02},new double[]{0.56,-1.61,1.92},new double[]{0.71,-1.64,1.85},new double[]{0.98,2.29,-0.65},new double[]{1.14,2.25,-0.47},new double[]{1.23,2.17,-0.62},new double[]{1.08,2.2,-0.79},new double[]{0.98,2.29,-0.65},new double[]{1.14,2.25,-0.47},new double[]{1.23,2.17,-0.62},new double[]{1.08,2.2,-0.79},new double[]{-0.64,-1.79,1.73},new double[]{-0.56,-1.92,1.61},new double[]{-0.38,-1.86,1.73},new double[]{-0.45,-1.72,1.85},new double[]{-0.44,-1.08,2.29},new double[]{-0.38,-1.3,2.18},new double[]{-0.17,-1.22,2.25},new double[]{-0.23,-1.01,2.35},new double[]{-1.6,1.15,1.64},new double[]{-1.55,1.32,1.56},new double[]{-1.72,1.29,1.4},new double[]{-1.78,1.12,1.46},new double[]{-1.4,-0.42,2.11},new double[]{-1.14,-0.38,2.27},new double[]{-1.19,-0.07,2.28},new double[]{-1.45,-0.1,2.12},new double[]{2.26,-0.79,-0.92},new double[]{2.35,-0.73,-0.72},new double[]{2.29,-0.93,-0.71},new double[]{2.2,-0.98,-0.89},new double[]{0.1,-0.65,2.48},new double[]{0.14,-0.86,2.42},new double[]{0.3,-0.79,2.43},new double[]{0.26,-0.59,2.49},new double[]{-2.5,-0.24,0.53},new double[]{-2.51,0.01,0.54},new double[]{-2.55,-0.02,0.35},new double[]{-2.53,-0.25,0.34},new double[]{0.03,1.71,-1.92},new double[]{-0.13,1.62,-1.98},new double[]{-0.16,1.74,-1.88},new double[]{-0.01,1.82,-1.81},new double[]{-1.96,-1.17,-1.18},new double[]{-1.82,-1.4,-1.15},new double[]{-1.94,-1.41,-0.91},new double[]{-2.08,-1.19,-0.93},new double[]{0.18,-1.06,2.33},new double[]{0.21,-1.24,2.24},new double[]{0.36,-1.16,2.27},new double[]{0.33,-0.98,2.35},new double[]{1.5,1.33,1.6},new double[]{1.37,1.53,1.54},new double[]{1.19,1.47,1.73},new double[]{1.31,1.27,1.81},new double[]{-0.9,-1.12,-2.13},new double[]{-0.69,-1.03,-2.25},new double[]{-0.62,-1.23,-2.17},new double[]{-0.81,-1.32,-2.05},new double[]{-0.78,-2.4,-0.49},new double[]{-0.94,-2.38,-0.27},new double[]{-1.09,-2.29,-0.43},new double[]{-0.94,-2.3,-0.64},new double[]{-0.11,2.34,-1.05},new double[]{0.07,2.27,-1.19},new double[]{-0.12,2.19,-1.33},new double[]{-0.3,2.25,-1.2},new double[]{-0.9,2.39,0.21},new double[]{-1.11,2.31,-0.0},new double[]{-1.28,2.21,0.22},new double[]{-1.08,2.29,0.43},new double[]{2.15,0.79,-1.17},new double[]{2.05,1.06,-1.12},new double[]{2.17,1.04,-0.89},new double[]{2.26,0.78,-0.93},new double[]{0.67,-2.44,-0.42},new double[]{0.43,-2.45,-0.63},new double[]{0.62,-2.35,-0.84},new double[]{0.86,-2.34,-0.64},new double[]{-0.69,-1.03,-2.25},new double[]{-0.5,-0.95,-2.34},new double[]{-0.44,-1.14,-2.26},new double[]{-0.62,-1.23,-2.17},new double[]{1.98,1.56,0.46},new double[]{2.04,1.53,0.27},new double[]{1.92,1.68,0.28},new double[]{1.85,1.72,0.46},new double[]{1.45,0.1,-2.12},new double[]{1.47,-0.2,-2.09},new double[]{1.22,-0.23,-2.25},new double[]{1.19,0.07,-2.28},new double[]{1.27,-1.9,-1.17},new double[]{1.06,-1.91,-1.34},new double[]{1.17,-1.76,-1.46},new double[]{1.36,-1.74,-1.3},new double[]{-1.33,-2.16,-0.37},new double[]{-1.43,-2.12,-0.19},new double[]{-1.52,-2.04,-0.32},new double[]{-1.43,-2.07,-0.5},new double[]{0.8,-1.91,1.52},new double[]{0.93,-1.79,1.59},new double[]{0.75,-1.77,1.7},new double[]{0.62,-1.88,1.64},new double[]{-1.06,1.91,1.34},new double[]{-1.17,1.76,1.46},new double[]{-0.97,1.76,1.61},new double[]{-0.85,1.91,1.5},new double[]{2.34,-0.73,0.76},new double[]{2.28,-0.95,0.7},new double[]{2.37,-0.87,0.49},new double[]{2.43,-0.66,0.53},new double[]{-0.76,1.77,-1.7},new double[]{-0.59,1.74,-1.8},new double[]{-0.71,1.64,-1.85},new double[]{-0.87,1.66,-1.76},new double[]{1.27,1.53,-1.63},new double[]{1.15,1.72,-1.53},new double[]{1.35,1.73,-1.34},new double[]{1.47,1.54,-1.43},new double[]{2.2,0.94,0.94},new double[]{2.29,0.95,0.69},new double[]{2.17,1.19,0.69},new double[]{2.08,1.19,0.93},new double[]{0.18,2.56,-0.08},new double[]{0.4,2.54,0.12},new double[]{0.57,2.5,-0.1},new double[]{0.36,2.53,-0.3},new double[]{2.05,1.06,-1.12},new double[]{1.93,1.31,-1.07},new double[]{2.06,1.28,-0.84},new double[]{2.17,1.04,-0.89},new double[]{-0.28,-0.77,2.43},new double[]{-0.23,-1.01,2.35},new double[]{-0.03,-0.93,2.39},new double[]{-0.08,-0.71,2.47},new double[]{-0.3,-0.12,-2.55},new double[]{-0.14,-0.07,-2.56},new double[]{-0.1,-0.29,-2.55},new double[]{-0.26,-0.35,-2.53},new double[]{-1.68,-1.82,0.69},new double[]{-1.78,-1.78,0.51},new double[]{-1.66,-1.9,0.47},new double[]{-1.55,-1.95,0.64},new double[]{1.68,1.59,1.11},new double[]{1.81,1.6,0.89},new double[]{1.67,1.75,0.86},new double[]{1.55,1.75,1.07},new double[]{-1.95,0.08,-1.66},new double[]{-2.14,0.03,-1.42},new double[]{-2.15,0.33,-1.36},new double[]{-1.97,0.39,-1.6},new double[]{0.58,-0.33,2.48},new double[]{0.39,-0.36,2.51},new double[]{0.43,-0.58,2.46},new double[]{0.62,-0.58,2.43},new double[]{2.3,-0.27,1.11},new double[]{2.28,-0.55,1.05},new double[]{2.39,-0.48,0.81},new double[]{2.41,-0.21,0.85},new double[]{-2.46,0.63,0.37},new double[]{-2.41,0.81,0.37},new double[]{-2.45,0.76,0.22},new double[]{-2.49,0.58,0.21},new double[]{1.38,1.3,-1.73},new double[]{1.15,1.27,-1.92},new double[]{1.05,1.49,-1.81},new double[]{1.27,1.53,-1.63},new double[]{-2.41,0.68,0.54},new double[]{-2.35,0.87,0.53},new double[]{-2.41,0.81,0.37},new double[]{-2.46,0.63,0.37},new double[]{-1.43,2.09,0.44},new double[]{-1.59,2.0,0.23},new double[]{-1.7,1.87,0.43},new double[]{-1.55,1.95,0.63},new double[]{-0.51,0.52,2.46},new double[]{-0.3,0.51,2.5},new double[]{-0.34,0.73,2.44},new double[]{-0.54,0.75,2.4},new double[]{-2.06,-0.49,1.44},new double[]{-2.21,-0.5,1.2},new double[]{-2.15,-0.79,1.17},new double[]{-1.99,-0.79,1.41},new double[]{-2.34,0.73,-0.75},new double[]{-2.42,0.66,-0.53},new double[]{-2.36,0.87,-0.49},new double[]{-2.28,0.95,-0.7},new double[]{2.51,-0.51,0.13},new double[]{2.46,-0.72,0.11},new double[]{2.48,-0.65,-0.06},new double[]{2.53,-0.45,-0.05},new double[]{-0.41,1.26,2.2},new double[]{-0.25,1.23,2.24},new double[]{-0.27,1.37,2.16},new double[]{-0.43,1.4,2.11},new double[]{1.52,2.04,0.32},new double[]{1.6,1.96,0.44},new double[]{1.67,1.93,0.28},new double[]{1.6,2.0,0.15},new double[]{1.04,1.87,-1.42},new double[]{0.84,1.84,-1.58},new double[]{0.75,1.97,-1.47},new double[]{0.93,2.0,-1.32},new double[]{-0.59,-1.37,2.09},new double[]{-0.52,-1.56,1.97},new double[]{-0.31,-1.49,2.07},new double[]{-0.38,-1.3,2.18},new double[]{-0.68,-2.38,0.67},new double[]{-0.8,-2.3,0.83},new double[]{-0.98,-2.29,0.65},new double[]{-0.86,-2.37,0.49},new double[]{0.51,-0.52,-2.46},new double[]{0.54,-0.75,-2.4},new double[]{0.34,-0.73,-2.44},new double[]{0.3,-0.51,-2.5},new double[]{0.62,2.28,-1.0},new double[]{0.73,2.19,-1.12},new double[]{0.55,2.16,-1.28},new double[]{0.42,2.25,-1.17},new double[]{0.3,-1.94,1.66},new double[]{0.46,-1.84,1.73},new double[]{0.3,-1.79,1.81},new double[]{0.15,-1.88,1.74},new double[]{0.23,-2.53,-0.39},new double[]{-0.01,-2.5,-0.59},new double[]{0.19,-2.43,-0.82},new double[]{0.43,-2.45,-0.63},new double[]{-2.44,0.28,0.74},new double[]{-2.4,0.52,0.74},new double[]{-2.46,0.47,0.54},new double[]{-2.5,0.25,0.54},new double[]{-1.76,1.69,-0.8},new double[]{-1.82,1.57,-0.9},new double[]{-1.94,1.52,-0.72},new double[]{-1.89,1.63,-0.61},new double[]{2.39,-0.48,0.81},new double[]{2.34,-0.73,0.75},new double[]{2.42,-0.66,0.53},new double[]{2.47,-0.41,0.57},new double[]{0.62,0.3,-2.47},new double[]{0.39,0.26,-2.53},new double[]{0.34,0.52,-2.49},new double[]{0.57,0.58,-2.44},new double[]{-0.55,-2.16,1.28},new double[]{-0.37,-2.11,1.42},new double[]{-0.49,-2.02,1.5},new double[]{-0.66,-2.07,1.37},new double[]{0.49,0.17,2.52},new double[]{0.44,0.41,2.5},new double[]{0.26,0.35,2.53},new double[]{0.31,0.12,2.55},new double[]{1.71,-0.46,-1.86},new double[]{1.92,-0.43,-1.65},new double[]{1.89,-0.69,-1.59},new double[]{1.69,-0.73,-1.79},new double[]{-1.58,-0.76,1.88},new double[]{-1.49,-1.05,1.81},new double[]{-1.25,-1.01,2.01},new double[]{-1.33,-0.72,2.07},new double[]{-0.12,-0.96,-2.38},new double[]{0.02,-0.88,-2.41},new double[]{0.06,-1.04,-2.35},new double[]{-0.07,-1.13,-2.3},new double[]{0.48,-0.27,-2.51},new double[]{0.51,-0.52,-2.46},new double[]{0.3,-0.51,-2.5},new double[]{0.26,-0.28,-2.54},new double[]{-2.14,0.03,-1.42},new double[]{-2.29,-0.03,-1.15},new double[]{-2.3,0.27,-1.11},new double[]{-2.15,0.33,-1.36},new double[]{2.56,-0.05,0.17},new double[]{2.55,-0.29,0.15},new double[]{2.56,-0.23,-0.03},new double[]{2.57,-0.0,-0.02},new double[]{1.15,1.71,-1.53},new double[]{1.04,1.87,-1.42},new double[]{1.22,1.88,-1.24},new double[]{1.35,1.73,-1.34},new double[]{0.17,0.22,-2.55},new double[]{0.22,-0.03,-2.56},new double[]{0.03,-0.05,-2.57},new double[]{-0.02,0.18,-2.56},new double[]{2.2,0.94,0.94},new double[]{2.29,0.95,0.69},new double[]{2.17,1.19,0.69},new double[]{2.08,1.19,0.93},new double[]{0.57,0.72,2.4},new double[]{0.5,0.95,2.34},new double[]{0.33,0.86,2.4},new double[]{0.38,0.64,2.46},new double[]{-1.19,-0.07,2.27},new double[]{-0.92,-0.04,2.39},new double[]{-0.96,0.24,2.37},new double[]{-1.22,0.23,2.25},new double[]{-1.07,-0.68,2.23},new double[]{-1.0,-0.96,2.16},new double[]{-0.74,-0.9,2.28},new double[]{-0.81,-0.63,2.35},new double[]{-0.62,-2.28,1.0},new double[]{-0.73,-2.19,1.12},new double[]{-0.91,-2.2,0.96},new double[]{-0.8,-2.3,0.83},new double[]{1.0,1.41,1.9},new double[]{0.9,1.58,1.82},new double[]{0.72,1.5,1.96},new double[]{0.81,1.32,2.05},new double[]{-2.3,0.27,-1.11},new double[]{-2.41,0.21,-0.85},new double[]{-2.39,0.48,-0.81},new double[]{-2.28,0.55,-1.05},new double[]{0.18,2.56,-0.08},new double[]{0.4,2.54,0.12},new double[]{0.57,2.5,-0.1},new double[]{0.36,2.53,-0.3},new double[]{-0.5,2.43,-0.68},new double[]{-0.3,2.4,-0.87},new double[]{-0.5,2.3,-1.04},new double[]{-0.7,2.32,-0.85},new double[]{-0.71,1.51,-1.95},new double[]{-0.71,1.64,-1.85},new double[]{-0.56,1.61,-1.92},new double[]{-0.55,1.49,-2.02},new double[]{-0.72,-1.49,-1.96},new double[]{-0.54,-1.41,-2.08},new double[]{-0.47,-1.56,-1.98},new double[]{-0.64,-1.64,-1.87},new double[]{-2.22,-0.5,1.2},new double[]{-2.26,-0.2,1.21},new double[]{-2.37,-0.22,0.97},new double[]{-2.33,-0.5,0.95},new double[]{1.06,-1.61,-1.69},new double[]{0.87,-1.6,-1.81},new double[]{0.96,-1.47,-1.87},new double[]{1.14,-1.48,-1.76},new double[]{1.52,-0.48,2.01},new double[]{1.28,-0.52,2.16},new double[]{1.3,-0.78,2.07},new double[]{1.52,-0.75,1.92},new double[]{1.55,-1.32,-1.56},new double[]{1.5,-1.46,-1.48},new double[]{1.32,-1.48,-1.63},new double[]{1.36,-1.34,-1.71},new double[]{-1.35,1.91,-1.06},new double[]{-1.17,1.93,-1.23},new double[]{-1.28,1.79,-1.32},new double[]{-1.45,1.78,-1.16},new double[]{1.45,-2.08,0.38},new double[]{1.6,-2.0,0.16},new double[]{1.72,-1.87,0.33},new double[]{1.58,-1.95,0.54},new double[]{-2.03,-0.59,-1.46},new double[]{-1.93,-0.88,-1.45},new double[]{-2.08,-0.92,-1.2},new double[]{-2.18,-0.63,-1.2},new double[]{-2.03,-0.59,-1.46},new double[]{-1.92,-0.88,-1.45},new double[]{-2.08,-0.92,-1.19},new double[]{-2.18,-0.63,-1.2},new double[]{0.79,-2.21,-1.04},new double[]{0.56,-2.2,-1.21},new double[]{0.71,-2.05,-1.37},new double[]{0.94,-2.07,-1.2},new double[]{-1.92,-0.16,1.7},new double[]{-1.93,0.15,1.68},new double[]{-2.12,0.12,1.45},new double[]{-2.1,-0.18,1.46},new double[]{-1.37,-1.53,-1.54},new double[]{-1.19,-1.47,-1.73},new double[]{-1.08,-1.64,-1.65},new double[]{-1.25,-1.69,-1.47},new double[]{1.91,0.23,1.7},new double[]{1.69,0.17,1.92},new double[]{1.73,-0.14,1.89},new double[]{1.95,-0.08,1.66},new double[]{2.37,0.95,-0.28},new double[]{2.28,1.16,-0.26},new double[]{2.31,1.11,-0.1},new double[]{2.4,0.91,-0.12},new double[]{-0.63,1.88,1.63},new double[]{-0.76,1.74,1.73},new double[]{-0.56,1.71,1.83},new double[]{-0.43,1.84,1.74},new double[]{-0.62,-0.3,2.47},new double[]{-0.39,-0.26,2.52},new double[]{-0.43,0.01,2.53},new double[]{-0.67,-0.02,2.48},new double[]{-2.01,0.89,1.33},new double[]{-1.95,1.09,1.28},new double[]{-2.08,1.04,1.09},new double[]{-2.15,0.84,1.13},new double[]{-1.22,0.23,2.25},new double[]{-0.96,0.24,2.37},new double[]{-0.98,0.52,2.32},new double[]{-1.23,0.51,2.2},new double[]{1.89,-0.7,-1.6},new double[]{2.06,-0.66,-1.38},new double[]{2.01,-0.89,-1.33},new double[]{1.84,-0.93,-1.53},new double[]{-1.5,-1.33,-1.6},new double[]{-1.31,-1.27,-1.81},new double[]{-1.19,-1.47,-1.73},new double[]{-1.37,-1.53,-1.54},new double[]{-1.28,1.8,-1.32},new double[]{-1.1,1.8,-1.46},new double[]{-1.2,1.68,-1.53},new double[]{-1.37,1.67,-1.39},new double[]{-2.56,0.05,-0.17},new double[]{-2.57,0.0,0.02},new double[]{-2.56,0.23,0.03},new double[]{-2.55,0.29,-0.15},new double[]{0.73,2.19,-1.12},new double[]{0.91,2.2,-0.96},new double[]{1.0,2.11,-1.07},new double[]{0.84,2.1,-1.22},new double[]{-2.41,0.52,0.74},new double[]{-2.35,0.74,0.73},new double[]{-2.42,0.68,0.54},new double[]{-2.47,0.47,0.54},new double[]{2.38,0.68,0.68},new double[]{2.43,0.7,0.44},new double[]{2.34,0.95,0.46},new double[]{2.29,0.95,0.69},new double[]{1.4,0.42,-2.11},new double[]{1.14,0.38,-2.27},new double[]{1.07,0.68,-2.23},new double[]{1.33,0.72,-2.07},new double[]{-0.58,-1.93,-1.59},new double[]{-0.72,-1.84,-1.64},new double[]{-0.56,-1.76,-1.78},new double[]{-0.42,-1.85,-1.73},new double[]{2.35,0.77,-0.7},new double[]{2.26,1.01,-0.67},new double[]{2.33,0.98,-0.47},new double[]{2.41,0.75,-0.49},new double[]{0.97,-1.33,-1.97},new double[]{0.96,-1.47,-1.87},new double[]{0.78,-1.46,-1.97},new double[]{0.78,-1.32,-2.06},new double[]{1.72,-1.29,-1.4},new double[]{1.88,-1.25,-1.22},new double[]{1.81,-1.4,-1.17},new double[]{1.66,-1.44,-1.33},new double[]{1.59,-2.0,-0.23},new double[]{1.7,-1.87,-0.43},new double[]{1.84,-1.78,-0.24},new double[]{1.73,-1.89,-0.04},new double[]{0.9,1.58,1.82},new double[]{0.8,1.72,1.73},new double[]{0.64,1.64,1.87},new double[]{0.72,1.5,1.96},new double[]{-2.52,-0.45,-0.21},new double[]{-2.46,-0.7,-0.23},new double[]{-2.47,-0.7,-0.04},new double[]{-2.52,-0.47,-0.02},new double[]{0.65,-2.03,1.43},new double[]{0.8,-1.91,1.52},new double[]{0.62,-1.88,1.64},new double[]{0.47,-1.99,1.56},new double[]{-0.7,2.47,-0.03},new double[]{-0.49,2.51,-0.26},new double[]{-0.71,2.42,-0.46},new double[]{-0.92,2.39,-0.23},new double[]{0.73,2.44,-0.3},new double[]{0.91,2.4,-0.11},new double[]{1.03,2.33,-0.3},new double[]{0.86,2.37,-0.49},new double[]{0.36,2.52,-0.3},new double[]{0.57,2.5,-0.1},new double[]{0.73,2.44,-0.3},new double[]{0.53,2.46,-0.5},new double[]{-0.26,-1.93,-1.67},new double[]{-0.42,-1.85,-1.73},new double[]{-0.27,-1.76,-1.85},new double[]{-0.1,-1.83,-1.79},new double[]{1.45,0.12,2.12},new double[]{1.39,0.42,2.12},new double[]{1.15,0.35,2.27},new double[]{1.21,0.06,2.27},new double[]{-0.12,-0.96,-2.38},new double[]{0.02,-0.88,-2.41},new double[]{0.06,-1.04,-2.35},new double[]{-0.07,-1.13,-2.3},new double[]{-2.11,-1.4,0.41},new double[]{-2.17,-1.34,0.24},new double[]{-2.06,-1.51,0.21},new double[]{-2.0,-1.57,0.38},new double[]{0.67,-2.44,-0.42},new double[]{0.85,-2.33,-0.64},new double[]{1.08,-2.29,-0.43},new double[]{0.9,-2.39,-0.21},new double[]{-1.29,1.01,-1.97},new double[]{-1.28,1.22,-1.86},new double[]{-1.08,1.22,-1.99},new double[]{-1.08,1.02,-2.1},new double[]{0.38,1.32,2.17},new double[]{0.32,1.47,2.08},new double[]{0.17,1.37,2.16},new double[]{0.22,1.22,2.25},new double[]{-1.84,0.93,1.53},new double[]{-1.79,1.12,1.47},new double[]{-1.95,1.09,1.28},new double[]{-2.01,0.89,1.33},new double[]{-1.07,1.4,-1.87},new double[]{-1.05,1.55,-1.76},new double[]{-0.88,1.53,-1.86},new double[]{-0.88,1.39,-1.98},new double[]{-0.26,2.55,-0.05},new double[]{-0.05,2.55,-0.29},new double[]{-0.28,2.5,-0.49},new double[]{-0.49,2.5,-0.26},new double[]{0.85,-1.91,-1.5},new double[]{0.63,-1.88,-1.63},new double[]{0.76,-1.74,-1.73},new double[]{0.97,-1.76,-1.61},new double[]{-1.72,0.96,-1.64},new double[]{-1.68,1.18,-1.54},new double[]{-1.48,1.21,-1.71},new double[]{-1.51,0.99,-1.82},new double[]{-0.79,-2.29,-0.85},new double[]{-0.94,-2.3,-0.64},new double[]{-1.08,-2.2,-0.77},new double[]{-0.93,-2.19,-0.97},new double[]{-2.02,1.38,-0.78},new double[]{-2.13,1.31,-0.59},new double[]{-2.05,1.45,-0.55},new double[]{-1.94,1.52,-0.72},new double[]{-1.43,-2.07,-0.5},new double[]{-1.52,-2.04,-0.32},new double[]{-1.6,-1.96,-0.45},new double[]{-1.51,-1.98,-0.62},new double[]{0.13,0.47,-2.52},new double[]{-0.06,0.42,-2.53},new double[]{-0.1,0.65,-2.48},new double[]{0.08,0.71,-2.47},new double[]{0.49,-1.0,2.31},new double[]{0.33,-0.98,2.35},new double[]{0.36,-1.16,2.27},new double[]{0.52,-1.18,2.22},new double[]{1.15,1.71,-1.53},new double[]{1.04,1.87,-1.42},new double[]{1.22,1.88,-1.24},new double[]{1.35,1.73,-1.34},new double[]{-2.03,1.58,-0.1},new double[]{-2.09,1.47,-0.24},new double[]{-2.16,1.39,-0.07},new double[]{-2.1,1.48,0.08},new double[]{0.65,-0.8,2.35},new double[]{0.46,-0.8,2.4},new double[]{0.49,-1.0,2.31},new double[]{0.67,-1.01,2.26},new double[]{-2.37,-0.22,0.97},new double[]{-2.38,0.06,0.97},new double[]{-2.46,0.03,0.74},new double[]{-2.45,-0.23,0.74},new double[]{1.81,1.6,0.89},new double[]{1.91,1.58,0.67},new double[]{1.77,1.74,0.65},new double[]{1.67,1.75,0.86},new double[]{-1.93,-0.88,-1.45},new double[]{-1.81,-1.15,-1.42},new double[]{-1.96,-1.17,-1.18},new double[]{-2.08,-0.92,-1.2},new double[]{0.92,-2.39,0.23},new double[]{1.11,-2.28,0.42},new double[]{0.91,-2.31,0.64},new double[]{0.71,-2.42,0.46},new double[]{-0.26,-1.93,-1.67},new double[]{-0.42,-1.85,-1.73},new double[]{-0.27,-1.76,-1.85},new double[]{-0.1,-1.83,-1.8},new double[]{1.52,-0.75,1.92},new double[]{1.3,-0.78,2.07},new double[]{1.29,-1.01,1.97},new double[]{1.51,-0.99,1.82},new double[]{-0.21,-0.57,-2.49},new double[]{-0.06,-0.5,-2.52},new double[]{-0.02,-0.69,-2.47},new double[]{-0.16,-0.77,-2.44},new double[]{-2.57,0.0,0.02},new double[]{-2.56,-0.03,0.18},new double[]{-2.56,0.18,0.19},new double[]{-2.56,0.23,0.03},new double[]{-1.52,0.48,-2.01},new double[]{-1.53,0.75,-1.92},new double[]{-1.3,0.78,-2.08},new double[]{-1.28,0.52,-2.16},new double[]{-1.71,1.67,0.94},new double[]{-1.86,1.6,0.75},new double[]{-1.9,1.48,0.88},new double[]{-1.77,1.53,1.06},new double[]{1.22,-0.98,-2.04},new double[]{1.2,-1.18,-1.95},new double[]{0.99,-1.17,-2.06},new double[]{0.99,-0.98,-2.16},new double[]{0.23,1.01,-2.35},new double[]{0.03,0.93,-2.39},new double[]{-0.01,1.14,-2.3},new double[]{0.17,1.22,-2.25},new double[]{1.37,-1.75,-1.3},new double[]{1.17,-1.76,-1.46},new double[]{1.26,-1.62,-1.55},new double[]{1.44,-1.6,-1.4},new double[]{1.38,1.3,-1.73},new double[]{1.15,1.27,-1.92},new double[]{1.05,1.49,-1.81},new double[]{1.27,1.53,-1.63},new double[]{-2.51,0.01,0.54},new double[]{-2.5,0.25,0.54},new double[]{-2.53,0.21,0.36},new double[]{-2.54,-0.02,0.35},new double[]{-0.39,1.11,2.28},new double[]{-0.22,1.08,2.32},new double[]{-0.25,1.23,2.24},new double[]{-0.41,1.26,2.2},new double[]{-0.24,1.23,2.24},new double[]{-0.09,1.19,2.27},new double[]{-0.12,1.33,2.19},new double[]{-0.27,1.37,2.16},new double[]{0.63,-1.88,-1.63},new double[]{0.43,-1.84,-1.74},new double[]{0.56,-1.71,-1.83},new double[]{0.76,-1.74,-1.73},new double[]{0.19,-0.9,-2.4},new double[]{0.22,-1.08,-2.32},new double[]{0.06,-1.04,-2.35},new double[]{0.02,-0.88,-2.41},new double[]{-2.47,-0.5,0.51},new double[]{-2.5,-0.24,0.53},new double[]{-2.53,-0.25,0.34},new double[]{-2.5,-0.49,0.32},new double[]{1.7,0.13,-1.92},new double[]{1.71,-0.18,-1.9},new double[]{1.47,-0.2,-2.09},new double[]{1.45,0.1,-2.12},new double[]{-0.14,2.03,-1.56},new double[]{0.01,1.97,-1.65},new double[]{-0.15,1.88,-1.74},new double[]{-0.3,1.94,-1.66},new double[]{1.14,2.25,-0.47},new double[]{1.28,2.21,-0.3},new double[]{1.36,2.13,-0.45},new double[]{1.23,2.17,-0.62},new double[]{-0.43,-2.02,-1.52},new double[]{-0.58,-1.93,-1.59},new double[]{-0.42,-1.85,-1.73},new double[]{-0.26,-1.93,-1.67},new double[]{-1.73,1.89,0.04},new double[]{-1.84,1.78,-0.13},new double[]{-1.94,1.68,0.06},new double[]{-1.84,1.78,0.24},new double[]{0.44,1.08,-2.28},new double[]{0.23,1.01,-2.35},new double[]{0.17,1.22,-2.25},new double[]{0.38,1.3,-2.18},new double[]{0.12,1.41,-2.15},new double[]{-0.05,1.32,-2.2},new double[]{-0.09,1.48,-2.09},new double[]{0.08,1.57,-2.03},new double[]{0.85,-0.8,2.28},new double[]{0.65,-0.8,2.35},new double[]{0.67,-1.01,2.26},new double[]{0.87,-1.02,2.19},new double[]{0.67,0.02,-2.48},new double[]{0.71,-0.26,-2.45},new double[]{0.48,-0.27,-2.51},new double[]{0.43,-0.01,-2.53},new double[]{-0.78,-2.29,-0.85},new double[]{-0.94,-2.3,-0.64},new double[]{-1.08,-2.2,-0.77},new double[]{-0.93,-2.19,-0.97}};
            GL11.glColor4f(0.5F, 1.0F, 0.5F, 1.0F);

            for(int i = 0; i < testArray.length; i += 4) {
                tes.addVertexWithUV(x + testArray[i + 0][0], y + testArray[i + 0][1], z + testArray[i + 0][2], maxU, maxV);
                tes.addVertexWithUV(x + testArray[i + 1][0], y + testArray[i + 1][1], z + testArray[i + 1][2], maxU, minV);
                tes.addVertexWithUV(x + testArray[i + 2][0], y + testArray[i + 2][1], z + testArray[i + 2][2], minU, minV);
                tes.addVertexWithUV(x + testArray[i + 3][0], y + testArray[i + 3][1], z + testArray[i + 3][2], minU, maxV);
            }
//            tes.addVertexWithUV(X[0], Y[0], Z[0], maxU, maxV);
//            tes.addVertexWithUV(X[1], Y[1], Z[1], maxU, minV);
//            tes.addVertexWithUV(X[2], Y[2], Z[2], minU, minV);
//            tes.addVertexWithUV(X[3], Y[3], Z[3], minU, maxV);
            


//            tes.addVertexWithUV(X[7], Y[7], Z[7], maxU, maxV);
//            tes.addVertexWithUV(X[6], Y[6], Z[6], maxU, minV);
//            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, minV);
//            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, maxV);
//
//            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, maxV);
//            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, minV);
//            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, minV);
//            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, maxV);
//
//            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, maxV);
//            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, minV);
//            tes.addVertexWithUV(X[5], Y[5], Z[5], minU, minV);
//            tes.addVertexWithUV(X[4], Y[4], Z[4], minU, maxV);
//
//            tes.addVertexWithUV(X[1], Y[1], Z[1], maxU, maxV);
//            tes.addVertexWithUV(X[6], Y[6], Z[6], maxU, minV);
//            tes.addVertexWithUV(X[5], Y[5], Z[5], minU, minV);
//            tes.addVertexWithUV(X[2], Y[2], Z[2], minU, maxV);
//
//            tes.addVertexWithUV(X[7], Y[7], Z[7], maxU, maxV);
//            tes.addVertexWithUV(X[0], Y[0], Z[0], maxU, minV);
//            tes.addVertexWithUV(X[3], Y[3], Z[3], minU, minV);
//            tes.addVertexWithUV(X[4], Y[4], Z[4], minU, maxV);

            // ----------------------------------------------
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glRotated(Math.random(),Math.random(),Math.random(),Math.random());
            GL11.glPopMatrix();
        }
        return false;
        //spotless:on
    }
}
