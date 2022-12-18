package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.recipe.EyeOfHarmonyRecipe;
import com.github.technus.tectech.recipe.EyeOfHarmonyRecipeStorage;
import com.github.technus.tectech.thing.casing.TT_Block_SpacetimeCompressionFieldGenerators;
import com.github.technus.tectech.thing.casing.TT_Block_StabilisationFieldGenerators;
import com.github.technus.tectech.thing.casing.TT_Block_TimeAccelerationFieldGenerators;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
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
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Output_ME;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static java.lang.Math.*;
import static net.minecraft.util.EnumChatFormatting.*;
import static pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper.dimToOreWrapper;

@SuppressWarnings("SpellCheckingInspection")
public class GT_MetaTileEntity_EM_EyeOfHarmony extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable, IGlobalWirelessEnergy {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private static EyeOfHarmonyRecipeStorage recipes;

    private int SpacetimeCompressionFieldMetadata = -1;
    private int TimeAccelerationFieldMetadata = -1;
    private int StabilisationFieldMetadata = -1;

    private String user_uuid = "";
    private String user_name = "";
    private long euOutput = 0;

    private final long[] computationStack = new long[computationTickCacheSize];

    // Multiblock structure.
    private static final IStructureDefinition<GT_MetaTileEntity_EM_EyeOfHarmony> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_EyeOfHarmony>builder()
            .addShape("main", transpose(new String[][]{
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "               C C               ", "            CCCCCCCCC            ", "               C C               ", "            CCCCCCCCC            ", "               C C               ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "               C C               ", "               C C               ", "              DDDDD              ", "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ", "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ", "             DDCDCDD             ", "              DDDDD              ", "               C C               ", "               C C               ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "               C C               ", "                D                ", "                D                ", "             DDDDDDD             ", "            DD     DD            ", "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ", "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ", "            D  EEE  D            ", "            DD     DD            ", "             DDDDDDD             ", "                D                ", "                D                ", "               C C               ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "      CC                 CC      ", "        DD             DD        ", "      CC                 CC      ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                D                ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "              CCCCC              ", "                D                ", "                A                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "      C                   C      ", "     CC                   CC     ", "      CDAA             AADC      ", "     CC                   CC     ", "      C                   C      ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                A                ", "                D                ", "              CCCCC              ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "             SEEAEES             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "       S                 S       ", "       E                 E       ", "    CC E                 E CC    ", "      DA                 AD      ", "    CC E                 E CC    ", "       E                 E       ", "       S                 S       ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "             SEEAEES             ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "               C C               ", "              CCCCC              ", "                D                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "    C                       C    ", "   CC                       CC   ", "    CDA                   ADC    ", "   CC                       CC   ", "    C                       C    ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                D                ", "              CCCCC              ", "               C C               ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "             SEEAEES             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "     S                     S     ", "     E                     E     ", "  CC E                     E CC  ", "    DA                     AD    ", "  CC E                     E CC  ", "     E                     E     ", "     S                     S     ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "             SEEAEES             ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "               C C               ", "                D                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  C                           C  ", "   DA                       AD   ", "  C                           C  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                D                ", "               C C               ", "                                 ", "                                 "},
                            {"                                 ", "               C C               ", "               C C               ", "                D                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CC                           CC ", "   DA                       AD   ", " CC                           CC ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                D                ", "               C C               ", "               C C               ", "                                 "},
                            {"                                 ", "               C C               ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " C                             C ", "  D                           D  ", " C                             C ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                D                ", "               C C               ", "                                 "},
                            {"                                 ", "               C C               ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " C                             C ", "  D                           D  ", " C                             C ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                D                ", "               C C               ", "                                 "},
                            {"             CCCCCCC             ", "               C C               ", "             DDDDDDD             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  D                           D  ", "  D                           D  ", "CCD                           DCC", "  D                           D  ", "CCD                           DCC", "  D                           D  ", "  D                           D  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "             DDDDDDD             ", "               C C               ", "               C C               "},
                            {"            CCHHHHHCC            ", "              DDDDD              ", "            DD     DD            ", "                                 ", "                                 ", "       S                 S       ", "                                 ", "     S                     S     ", "                                 ", "                                 ", "                                 ", "                                 ", "  D                           D  ", "  D                           D  ", " D                             D ", "CD                             DC", " D                             D ", "CD                             DC", " D                             D ", "  D                           D  ", "  D                           D  ", "                                 ", "                                 ", "                                 ", "                                 ", "     S                     S     ", "                                 ", "       S                 S       ", "                                 ", "                                 ", "            DD     DD            ", "              DDDDD              ", "               C C               "},
                            {"            CHHHHHHHC            ", "             DDCDCDD             ", "            D  EEE  D            ", "                                 ", "      C                   C      ", "       E                 E       ", "    C                       C    ", "     E                     E     ", "                                 ", "                                 ", "                                 ", "                                 ", "  D                           D  ", " D                             D ", " D                             D ", "CCE                           ECC", " DE                           ED ", "CCE                           ECC", " D                             D ", " D                             D ", "  D                           D  ", "                                 ", "                                 ", "                                 ", "                                 ", "     E                     E     ", "    C                       C    ", "       E                 E       ", "      C                   C      ", "                                 ", "            D  EEE  D            ", "             DDCDCDD             ", "               C C               "},
                            {"            CHHFFFHHC            ", "         CCCCDCCDCCDCCCC         ", "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ", "     CC                   CC     ", "    CC E                 E CC    ", "   CC                       CC   ", "  CC E                     E CC  ", "  C                           C  ", " CC                           CC ", " C                             C ", " C                             C ", "CCD                           DCC", "CD                             DC", "CCE                           ECC", "CCA                           ACC", "CDA                           ADC", "CCA                           ACC", "CCE                           ECC", "CD                             DC", "CCD                           DCC", " C                             C ", " C                             C ", " CC                           CC ", "  C                           C  ", "  CC E                     E CC  ", "   CC                       CC   ", "    CC E                 E CC    ", "     CC                   CC     ", "      CC                 CC      ", "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ", "            CCCCCCCCC            "},
                            {"            CHHF~FHHC            ", "             DDDDDDD             ", "          DDD EAAAE DDD          ", "        DD             DD        ", "      CDAA             AADC      ", "      DA                 AD      ", "    CDA                   ADC    ", "    DA                     AD    ", "   DA                       AD   ", "   DA                       AD   ", "  D                           D  ", "  D                           D  ", "  D                           D  ", " D                             D ", " DE                           ED ", "CDA                           ADC", " DA                           AD ", "CDA                           ADC", " DE                           ED ", " D                             D ", "  D                           D  ", "  D                           D  ", "  D                           D  ", "   DA                       AD   ", "   DA                       AD   ", "    DA                     AD    ", "    CDA                   ADC    ", "      DA                 AD      ", "      CDAA             AADC      ", "        DD             DD        ", "          DDD EAAAE DDD          ", "             DDDDDDD             ", "               C C               "},
                            {"            CHHFFFHHC            ", "         CCCCDCCDCCDCCCC         ", "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ", "     CC                   CC     ", "    CC E                 E CC    ", "   CC                       CC   ", "  CC E                     E CC  ", "  C                           C  ", " CC                           CC ", " C                             C ", " C                             C ", "CCD                           DCC", "CD                             DC", "CCE                           ECC", "CCA                           ACC", "CDA                           ADC", "CCA                           ACC", "CCE                           ECC", "CD                             DC", "CCD                           DCC", " C                             C ", " C                             C ", " CC                           CC ", "  C                           C  ", "  CC E                     E CC  ", "   CC                       CC   ", "    CC E                 E CC    ", "     CC                   CC     ", "      CC                 CC      ", "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ", "            CCCCCCCCC            "},
                            {"            CHHHHHHHC            ", "             DDCDCDD             ", "            D  EEE  D            ", "                                 ", "      C                   C      ", "       E                 E       ", "    C                       C    ", "     E                     E     ", "                                 ", "                                 ", "                                 ", "                                 ", "  D                           D  ", " D                             D ", " D                             D ", "CCE                           ECC", " DE                           ED ", "CCE                           ECC", " D                             D ", " D                             D ", "  D                           D  ", "                                 ", "                                 ", "                                 ", "                                 ", "     E                     E     ", "    C                       C    ", "       E                 E       ", "      C                   C      ", "                                 ", "            D  EEE  D            ", "             DDCDCDD             ", "               C C               "},
                            {"            CCHHHHHCC            ", "              DDDDD              ", "            DD     DD            ", "                                 ", "                                 ", "       S                 S       ", "                                 ", "     S                     S     ", "                                 ", "                                 ", "                                 ", "                                 ", "  D                           D  ", "  D                           D  ", " D                             D ", "CD                             DC", " D                             D ", "CD                             DC", " D                             D ", "  D                           D  ", "  D                           D  ", "                                 ", "                                 ", "                                 ", "                                 ", "     S                     S     ", "                                 ", "       S                 S       ", "                                 ", "                                 ", "            DD     DD            ", "              DDDDD              ", "               C C               "},
                            {"             CCCCCCC             ", "               C C               ", "             DDDDDDD             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  D                           D  ", "  D                           D  ", "CCD                           DCC", "  D                           D  ", "CCD                           DCC", "  D                           D  ", "  D                           D  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "             DDDDDDD             ", "               C C               ", "               C C               "},
                            {"                                 ", "               C C               ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " C                             C ", "  D                           D  ", " C                             C ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                D                ", "               C C               ", "                                 "},
                            {"                                 ", "               C C               ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " C                             C ", "  D                           D  ", " C                             C ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                D                ", "               C C               ", "                                 "},
                            {"                                 ", "               C C               ", "               C C               ", "                D                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CC                           CC ", "   DA                       AD   ", " CC                           CC ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                D                ", "               C C               ", "               C C               ", "                                 "},
                            {"                                 ", "                                 ", "               C C               ", "                D                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  C                           C  ", "   DA                       AD   ", "  C                           C  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                D                ", "               C C               ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "             SEEAEES             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "     S                     S     ", "     E                     E     ", "  CC E                     E CC  ", "    DA                     AD    ", "  CC E                     E CC  ", "     E                     E     ", "     S                     S     ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "             SEEAEES             ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "               C C               ", "              CCCCC              ", "                D                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "    C                       C    ", "   CC                       CC   ", "    CDA                   ADC    ", "   CC                       CC   ", "    C                       C    ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                D                ", "              CCCCC              ", "               C C               ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "             SEEAEES             ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "       S                 S       ", "       E                 E       ", "    CC E                 E CC    ", "      DA                 AD      ", "    CC E                 E CC    ", "       E                 E       ", "       S                 S       ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "             SEEAEES             ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "              CCCCC              ", "                D                ", "                A                ", "                A                ", "                                 ", "                                 ", "                                 ", "                                 ", "      C                   C      ", "     CC                   CC     ", "      CDAA             AADC      ", "     CC                   CC     ", "      C                   C      ", "                                 ", "                                 ", "                                 ", "                                 ", "                A                ", "                A                ", "                D                ", "              CCCCC              ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "      CC                 CC      ", "        DD             DD        ", "      CC                 CC      ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                D                ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "               C C               ", "                D                ", "                D                ", "             DDDDDDD             ", "            DD     DD            ", "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ", "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ", "            D  EEE  D            ", "            DD     DD            ", "             DDDDDDD             ", "                D                ", "                D                ", "               C C               ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "               C C               ", "               C C               ", "              DDDDD              ", "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ", "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ", "             DDCDCDD             ", "              DDDDD              ", "               C C               ", "               C C               ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "               C C               ", "            CCCCCCCCC            ", "               C C               ", "            CCCCCCCCC            ", "               C C               ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "}
                    }))
            .addElement('A', ofBlocksTiered(
                    (block, meta) -> block == TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators ? meta : -1,
                    ImmutableList.of(
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 0),
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 1),
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 2),
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 3),
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 4),
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 5),
                            Pair.of(TT_Block_SpacetimeCompressionFieldGenerators.SpacetimeCompressionFieldGenerators, 6)
                    ),
                    -1,
                    (t, meta) -> t.SpacetimeCompressionFieldMetadata = meta,
                    t -> t.SpacetimeCompressionFieldMetadata
            ))
            .addElement('S', ofBlocksTiered(
                    (block, meta) -> block == TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators ? meta : -1,
                    ImmutableList.of(
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 0),
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 1),
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 2),
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 3),
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 4),
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 5),
                            Pair.of(TT_Block_StabilisationFieldGenerators.StabilisationFieldGenerators, 6)
                    ),
                    -1,
                    (t, meta) -> t.StabilisationFieldMetadata = meta,
                    t -> t.StabilisationFieldMetadata
            ))
            .addElement('B', ofBlock(sBlockCasingsTT, 11))
            .addElement('C', ofBlock(sBlockCasingsTT, 12))
            .addElement('D', ofBlock(sBlockCasingsTT, 13))
            .addElement('E', ofBlocksTiered(
                    (block, meta) -> block == TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator ? meta : -1,
                    ImmutableList.of(
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 0),
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 1),
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 2),
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 3),
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 4),
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 5),
                            Pair.of(TT_Block_TimeAccelerationFieldGenerators.TimeAccelerationFieldGenerator, 6)
                    ),
                    -1,
                    (t, meta) -> t.TimeAccelerationFieldMetadata = meta,
                    t -> t.TimeAccelerationFieldMetadata
            ))
            .addElement('H', ofHatchAdderOptional(GT_MetaTileEntity_EM_EyeOfHarmony::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .addElement('F', ofHatchAdderOptional(GT_MetaTileEntity_EM_EyeOfHarmony::addElementalToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .build();


    private double hydrogen_overflow_probability_adjustment;
    private double helium_overflow_probability_adjustment;

    // Maximum additional chance of recipe success that can be obtained from adding computation.
    private static final double max_percentage_chance_gain_from_computation_per_second = 0.3;

    private static final long ticks_between_hatch_drain = 20;

    List<Pair<ItemStack, Long>> output_items;

    private void calculateHydrogenHeliumInputExcessValues(long hydrogen_recipe_requirement, long helium_recipe_requirement) {

        long hydrogen_stored = validFluidMap.get(Materials.Hydrogen.getGas(1L));
        long helium_stored =  validFluidMap.get(Materials.Helium.getGas(1L));

        double hydrogen_excess_percentage = abs(1 - hydrogen_stored / hydrogen_recipe_requirement);
        double helium_excess_percentage = abs(1 - helium_stored / helium_recipe_requirement);

        hydrogen_overflow_probability_adjustment = 1 - exp(- pow(30 * hydrogen_excess_percentage, 2));
        helium_overflow_probability_adjustment = 1 - exp(- pow(30 * helium_excess_percentage, 2));
    }

    private double recipeChanceCalculator(double base_recipe_chance) {
        double chance = (base_recipe_chance
                - TimeAccelerationFieldMetadata * 0.1
                + StabilisationFieldMetadata * 0.05
                - hydrogen_overflow_probability_adjustment
                - helium_overflow_probability_adjustment
                + max_percentage_chance_gain_from_computation_per_second * (1 - exp(-10e-5 * getComputation())));

        return clamp(chance, 0.0, 1.0);
    }

    public static double clamp(double amount, double min, double max) {
        return Math.max(min, Math.min(amount, max));
    }

    private double recipeYieldCalculator() {
        double yield = 1
                - hydrogen_overflow_probability_adjustment
                - helium_overflow_probability_adjustment
                - StabilisationFieldMetadata * 0.05;

        return clamp(yield, 0.0, 1.0);
    }

    private long recipeProcessTimeCalculator(long recipe_time, long recipe_spacetime_casing_required) {

        double double_recipe_time = (double) recipe_time;

        long spacetime_casing_difference = (recipe_spacetime_casing_required - SpacetimeCompressionFieldMetadata);
        return (long) (double_recipe_time * pow(2, -TimeAccelerationFieldMetadata) * pow(0.97, spacetime_casing_difference));
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

        // Make sure there is 1 input bus.
        if (mInputBusses.size() != 1) {
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
                .addInfo("Creates a pocket of spacetime that is bigger on the inside using")
                .addInfo("transdimensional engineering. Certified Time Lord regulation compliant.")
                .addInfo("This multi outputs too much EU to be handled with conventional means.")
                .addInfo("All EU requirements are handled directly by your wireless EU network.")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("This multiblock will constantly consume hydrogen and helium when it is")
                .addInfo("not running. It will store this internally, you can see the totals by")
                .addInfo("using a scanner. This multi also has three tiered blocks with " + RED + "7" + GRAY + " tiers")
                .addInfo("each. They are as follows and have the associated effects on the multi.")
                .addInfo(BLUE + "Spacetime Compression Field Generator:")
                .addInfo("- The tier of this block determines what recipes can be run. If the multiblocks")
                .addInfo("  spacetime compression field block exceeds the requirements of the recipe it")
                .addInfo("  will decrease the processing time by " + RED + "3%" + GRAY + " per tier over the requirement. This")
                .addInfo("  is multiplicative.")
                .addInfo(BLUE + "Time Dilation Field Generator:")
                .addInfo("- Decreases the time required by a recipe by a factor of " + RED + "2" + GRAY + " per tier of block.")
                .addInfo("  Decreases the probability of a recipe succeeding by " + RED + "10%" + GRAY + " per tier (additive)")
                .addInfo(BLUE + "Stabilisation Field Generator:")
                .addInfo("- Increases the probability of a recipe succeeding by " + RED + "5%" + GRAY + " per tier (additive).")
                .addInfo("  Decreases the yield of a recipe by " + RED + "5%" + GRAY + " per tier (additive). ")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Computation/s provided to the multiblock can increase the chance by up to " + RED + GT_Utility.formatNumbers(max_percentage_chance_gain_from_computation_per_second * 100) + GRAY + "%.")
                .addInfo("The associated formula is " + GREEN + "additional_chance = 0.3 * exp(10^(-5) * computation_per_second)" + GRAY + ".")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Going over a recipe requirement on hydrogen or helium has a penalty on yield and recipe")
                .addInfo("chance. All stored hydrogen and helium is consumed during a craft. The associated formulas are:")
                .addInfo(GREEN + "percentage_overflow = abs(1 - fluid_stored/recipe_requirement)")
                .addInfo(GREEN + "adjustment_value = 1 - exp(-(30 * percentage_overflow)^2)")
                .addInfo("The value of adjustment_value is then subtracted from the total yield and recipe chance.")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("It should be noted that base recipe chance is determined per recipe and yield always")
                .addInfo("starts at 1 and subtracts depending on penalities. All fluid/item outputs are multiplied")
                .addInfo("by the yield calculated.")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("This multiblock can only output to ME output busses/hatches. If no space in the network")
                .addInfo("is avaliable the items/fluids will be " + UNDERLINE + DARK_RED + "voided" + RESET + GRAY + ".")
                .addInfo(GOLD + "--------------------------------------------------------------------------------")
                .addInfo("Recipes that fail will return a random amount of the fluid back from the recipe and some")
                .addInfo("exotic material that rejects conventional physics.")
                .addSeparator()
                .addStructureInfo("Eye of Harmony structure is too complex! See schematic for details.")
                .addStructureInfo(EnumChatFormatting.GOLD + "888" + EnumChatFormatting.GRAY + " Ultimate Molecular Casing.")
                .addStructureInfo(EnumChatFormatting.GOLD + "534" + EnumChatFormatting.GRAY + " Ultimate Advanced Molecular Casing.")
                .addStructureInfo(EnumChatFormatting.GOLD + "680" + EnumChatFormatting.GRAY + " Time Dilation Field Generator.")
                .addStructureInfo(EnumChatFormatting.GOLD + "48" + EnumChatFormatting.GRAY + " Stabilisation Field Generator.")
                .addStructureInfo(EnumChatFormatting.GOLD + "138" + EnumChatFormatting.GRAY + " Spacetime Compression Field Generator.")
                .addStructureInfo("--------------------------------------------")
                .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " maintenance hatch.")
                .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 2 + EnumChatFormatting.GRAY + " input hatches.")
                .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + "+ ME output hatch.")
                .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " input busses.")
                .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " ME output bus.")
                .addStructureInfo("--------------------------------------------")
                .beginStructureBlock(33, 33, 33, false)
                .toolTipFinisher( AuthorColen.substring(8) + EnumChatFormatting.GRAY + "&" + CommonValues.TEC_MARK_EM);
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        SpacetimeCompressionFieldMetadata = -1;
        TimeAccelerationFieldMetadata = -1;
        StabilisationFieldMetadata = -1;
        structureBuild_EM("main", 16, 16, 0, stackSize, hintsOnly);
    }

    private final Map<FluidStack, Long> validFluidMap =  new HashMap<FluidStack, Long>() {{
        put(Materials.Hydrogen.getGas(1), 0L);
        put(Materials.Helium.getGas(1), 0L);
    }};

    private void drainFluidFromHatchesAndStoreInternally() {
        for (GT_MetaTileEntity_Hatch_Input input_hatch : mInputHatches) {
            FluidStack fluid_in_hatch = input_hatch.getFluid();

            if (fluid_in_hatch == null) {
                continue;
            }

            // Iterate over valid fluids and store them in a hashmap.
            for (FluidStack valid_fluid : validFluidMap.keySet()) {
                if (fluid_in_hatch.isFluidEqual(valid_fluid)) {
                    validFluidMap.put(valid_fluid, validFluidMap.get(valid_fluid) + (long) fluid_in_hatch.amount);
                    input_hatch.setFillableStack(null);
                }
            }
        }
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {

        long hydrogen_stored = validFluidMap.get(Materials.Hydrogen.getGas(1));
        long helium_stored = validFluidMap.get(Materials.Helium.getGas(1));

        if ((hydrogen_stored >= recipes.overworld.getHydrogenRequirement()) & (helium_stored >= recipes.overworld.getHeliumRequirement())) {
            return processRecipe(recipes.overworld);
        }

        return false;
    }

    public boolean processRecipe(EyeOfHarmonyRecipe recipeObject) {

        // Check tier of spacetime compression blocks is high enough.
        if (SpacetimeCompressionFieldMetadata <= recipeObject.getSpacetimeCasingTierRequired()) {
            return false;
        }

        // Remove EU from the users network.
        if (!addEUToGlobalEnergyMap(user_uuid, -recipeObject.getEUStartCost())) {
            return false;
        }

        mMaxProgresstime = (int) Math.max(1, recipeProcessTimeCalculator(recipeObject.getRecipeTime(), recipeObject.getSpacetimeCasingTierRequired()));

        calculateHydrogenHeliumInputExcessValues(recipeObject.getHydrogenRequirement(), recipeObject.getHeliumRequirement());
        // DEBUG ! DELETE THESE TWO LINES:
        hydrogen_overflow_probability_adjustment = 0;
        helium_overflow_probability_adjustment = 0;

        success_chance = recipeChanceCalculator(recipeObject.getBaseRecipeSuccessChance());

        // Determine EU recipe output.
        euOutput = recipeObject.getEUOutput();

        // Set expected recipe computation.
        eRequiredData = getComputation();

        // Reduce internal storage by hydrogen and helium quantity required for recipe.
        validFluidMap.put(Materials.Hydrogen.getGas(1), 0L);
        validFluidMap.put(Materials.Helium.getGas(1), 0L);

        double yield = recipeYieldCalculator();

        List<Pair<ItemStack, Long>> tmp_items_output = new ArrayList<>();
        FluidStack[] tmp_fluids_output = recipeObject.getOutputFluids().clone();

        // Iterate over item output list and apply yield values.
        for (Pair<ItemStack, Long> pair : recipeObject.getOutputItems()) {
            tmp_items_output.add(Pair.of(pair.getLeft(), (long) (pair.getRight() * yield)));
        }

        // Iterate over fluid output list and apply yield values.
        for (FluidStack fluidStack : tmp_fluids_output) {
            fluidStack.amount *= yield;
        }

        output_items = tmp_items_output;
        mOutputFluids = tmp_fluids_output;
        updateSlots();

        recipeRunning = true;
        return true;
    }

    private double success_chance;

    private void outputFailedChance() {}

    @Override
    public void stopMachine() {
        super.stopMachine();
        recipeRunning = false;
    }

    public void outputAfterRecipe_EM() {
        recipeRunning = false;
        eRequiredData = 0L;

        if (success_chance > random()) {
            outputFailedChance();
            return;
        }

        addEUToGlobalEnergyMap(user_uuid, euOutput);
        euOutput = 0;

        for (Pair<ItemStack, Long> itemPair : output_items) {
            outputItemToAENetwork(itemPair.getLeft(), itemPair.getRight());
        }

        super.outputAfterRecipe_EM();
    }

    private void pushComputation() {
        // Add computation to finite "stack".
        for (int i = computationTickCacheSize - 1 ; i > 2; i--) {
            computationStack[i-1] = computationStack[i];
        }
        computationStack[0] = eAvailableData;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aTick == 1) {
            user_uuid = String.valueOf(getBaseMetaTileEntity().getOwnerUuid());
            user_name = getBaseMetaTileEntity().getOwnerName();
            strongCheckOrAddUser(user_uuid, user_name);

            // Move into tick == 1 after debug.
            if (recipes == null) {
                recipes = new EyeOfHarmonyRecipeStorage();
            }
        }

        // Add computation to stack. Prevents small interruptions causing issues.
        pushComputation();

        if (!recipeRunning) {
            if ((aTick % ticks_between_hatch_drain) == 0) {
                drainFluidFromHatchesAndStoreInternally();
            }
        }
    }

    private boolean recipeRunning = false;
    private final static int computationTickCacheSize = 5;

    private long getComputation() {
        return max(computationStack);
    }

    private long max(long[] array) {
        long max = array[0];

        for (long i : array) {
            if (i > max) { max = i; }
        }

        return max;
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
        str.add("Spacetime Compression Field Grade: " + BLUE + SpacetimeCompressionFieldMetadata);
        str.add("Time Dilation Field Grade: " + BLUE + TimeAccelerationFieldMetadata);
        str.add("Stabilisation Field Grade: " + BLUE + StabilisationFieldMetadata);
        str.add(GOLD + "----------------- Internal Fluids Stored ----------------");
        validFluidMap.forEach((key, value) -> str.add(BLUE + key.getLocalizedName() + RESET +
                " : " + RED + GT_Utility.formatNumbers(value)));
        str.add(GOLD + "-----------------------------------------------------");
        return str.toArray(new String[0]);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] {"Eye of Harmony multiblock"};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        // Save the quantity of fluid stored inside the controller.
        validFluidMap.forEach((key, value) -> aNBT.setLong("stored." + key.getUnlocalizedName(), value));

        aNBT.setBoolean("eye_of_harmony_recipeRunning", recipeRunning);

        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {

        // Load the quantity of fluid stored inside the controller.
        validFluidMap.forEach((key, value) -> validFluidMap.put(key, aNBT.getLong("stored." + key.getUnlocalizedName())));
        recipeRunning = aNBT.getBoolean("eye_of_harmony_recipeRunning");

        super.loadNBTData(aNBT);
    }
}
