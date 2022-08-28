package com.github.technus.tectech.thing.metaTileEntity.multi;

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
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static java.lang.Math.*;
import static net.minecraft.util.EnumChatFormatting.*;


public class GT_MetaTileEntity_EM_EyeOfHarmony extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int SpacetimeCompressionFieldMetadata = -1;
    private int TimeAccelerationFieldMetadata = -1;
    private int StabilisationFieldMetadata = -1;

    // Multiblock structure.
    @SuppressWarnings("SpellCheckingInspection")
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
                            {"                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "               C C               ", "               C C               ", "                D                ", "                D                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "      CC                 CC      ", "        DD             DD        ", "      CC                 CC      ", "                                 ", "                                 ", "                                 ", "                D                ", "                D                ", "                D                ", "                D                ", "               C C               ", "               C C               ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 "},
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

    private double max_percentage_chance_gain_from_computation_per_second = 0.3;

    private long ticks_between_hatch_drain = 20;

    private void calculate_hydrogen_helium_input_excess_values(long hydrogen_recipe_requirement, long helium_recipe_requirement) {

        long hydrogen_stored = valid_fluid_map.get(Materials.Hydrogen.getFluid(1L));
        long helium_stored =  valid_fluid_map.get(Materials.Helium.getFluid(1L));

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
                + max_percentage_chance_gain_from_computation_per_second * (1 - exp(-10e-5 * eAvailableData))); // eAvailableData = computation/s maybe?.

        if (chance < 0) {
            return 0;
        }

        if (chance > 1) {
            return 1;
        }

        return chance;
    }


    private double recipeYieldCalculator() {
        double yield = 1
                - hydrogen_overflow_probability_adjustment
                - helium_overflow_probability_adjustment
                - StabilisationFieldMetadata * 0.05;

        if (yield < 0) {
            return 0;
        }

        if (yield > 1) {
            return 1;
        }

        return yield;
    }

    private double recipeProcessTimeCalculator(double recipe_time, long recipe_spacetime_casing_required) {
        long spacetime_casing_difference = (SpacetimeCompressionFieldMetadata - recipe_spacetime_casing_required);
        return recipe_time * pow(2, -TimeAccelerationFieldMetadata) * pow(1.03, spacetime_casing_difference);
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
        return structureCheck_EM("main", 16, 16, 0);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Spacetime Manipulator")
                .addInfo("Creates a pocket of spacetime that is bigger on the inside using")
                .addInfo("transdimensional engineering. Certified Time Lord regulation compliant.")
                .addInfo("This multi outputs too much EU to be handled with conventional means.")
                .addInfo("All EU generated is directly deposited into your wireless EU network.")
                .addSeparator()
                .beginStructureBlock(33, 33, 33, false)
                .toolTipFinisher(CommonValues.TEC_MARK_EM + " & " + AuthorColen.substring(10));
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

    private final Map<FluidStack, Long> valid_fluid_map =  new HashMap<FluidStack, Long>() {{
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
            for (FluidStack valid_fluid : valid_fluid_map.keySet()) {
                if (fluid_in_hatch.isFluidEqual(valid_fluid)) {
                    valid_fluid_map.put(valid_fluid, valid_fluid_map.get(valid_fluid) + (long) fluid_in_hatch.amount);
                    input_hatch.setFillableStack(null);
                }
            }
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aTick % ticks_between_hatch_drain == 0) {
            drainFluidFromHatchesAndStoreInternally();
        }
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(GOLD + "--------------- Control Block Statistics ---------------");
        str.add("Spacetime Compression Field Grade: " + BLUE + SpacetimeCompressionFieldMetadata);
        str.add("Time Dilation Field Grade: " + BLUE + TimeAccelerationFieldMetadata);
        str.add("Stabilisation Field Grade: " + BLUE + TimeAccelerationFieldMetadata);
        str.add(GOLD + "---------------- Internal Fluids Stored ---------------");
        valid_fluid_map.forEach((key, value) -> str.add(BLUE + key.getLocalizedName() + RESET +
                " : " + RED + GT_Utility.formatNumbers(value)));
        str.add(GOLD + "-------------------------------------------------------");
        return str.toArray(new String[0]);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] {"TEST1", "TEST2"};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {

        // Save the quantity of fluid stored inside the controller.
        valid_fluid_map.forEach((key, value) -> aNBT.setLong("stored." + key.getUnlocalizedName(), value));

        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {

        // Load the quantity of fluid stored inside the controller.
        valid_fluid_map.forEach((key, value) -> valid_fluid_map.put(key, aNBT.getLong("stored." + key.getUnlocalizedName())));

        super.loadNBTData(aNBT);
    }
}
