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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

@SuppressWarnings("SpellCheckingInspection")
public class GT_MetaTileEntity_EM_EyeOfHarmony extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable, IGlobalWirelessEnergy {
    //region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    private int SpacetimeCompressionFieldMetadata = -1;
    private int TimeAccelerationFieldMetadata = -1;
    private int StabilisationFieldMetadata = -1;

    private String user_uuid = "";
    private String user_name = "";
    private long eu_output = 0;

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

    private static final double max_percentage_chance_gain_from_computation_per_second = 0.3;

    private static final long ticks_between_hatch_drain = 20;

    List<Pair<ItemStack, Long>> output_items;

    private void calculateHydrogenHeliumInputExcessValues(long hydrogen_recipe_requirement, long helium_recipe_requirement) {

        long hydrogen_stored = valid_fluid_map.get(Materials.Hydrogen.getGas(1L));
        long helium_stored =  valid_fluid_map.get(Materials.Helium.getGas(1L));

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
                + max_percentage_chance_gain_from_computation_per_second * (1 - exp(-10e-5 * eAvailableData))); // eAvailableData = computation/s (kinda).

        return clamp_probability(chance);
    }

    // Restrict number between 0 and 1. MathHelper.clamp was giving NoClassDefFoundError, so I wrote this.
    private double clamp_probability(double number) {
        if (number > (double) 1) { return 1; }
        return Math.max(number, 0);
    }

    private double recipeYieldCalculator() {
        double yield = 1
                - hydrogen_overflow_probability_adjustment
                - helium_overflow_probability_adjustment
                - StabilisationFieldMetadata * 0.05;

        // Restrict value between 0 and 1 given it is a probability.
        return clamp_probability(yield);
    }

    private long recipeProcessTimeCalculator(long recipe_time, long recipe_spacetime_casing_required) {

        double double_recipe_time = (double) recipe_time;

        long spacetime_casing_difference = (recipe_spacetime_casing_required - SpacetimeCompressionFieldMetadata);
        return (long) (double_recipe_time * pow(2, -TimeAccelerationFieldMetadata) * pow(1.03, spacetime_casing_difference));
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
                .toolTipFinisher(CommonValues.TEC_MARK_EM + " & " + AuthorColen.substring(8));
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
    public boolean checkRecipe_EM(ItemStack aStack) {


        long hydrogen_stored = valid_fluid_map.get(Materials.Hydrogen.getGas(1));
        long helium_stored = valid_fluid_map.get(Materials.Helium.getGas(1));

        EyeOfHarmonyRecipe recipe;

        recipe = EyeOfHarmonyRecipeStorage.recipe_0;
        if ((hydrogen_stored >= recipe.getHydrogenRequirement()) & (helium_stored >= recipe.getHeliumRequirement())) {
            return processRecipe(recipe);
        }

        return false;
    }


    public boolean processRecipe(EyeOfHarmonyRecipe recipeObject) {

        mMaxProgresstime = (int) max(1, recipeProcessTimeCalculator(recipeObject.getRecipeTime(), recipeObject.getSpacetimeCasingTierRequired()));

        calculateHydrogenHeliumInputExcessValues(recipeObject.getHydrogenRequirement(), recipeObject.getHeliumRequirement());
        // DEBUG ! DELETE THESE TWO LINES:
        hydrogen_overflow_probability_adjustment = 0;
        helium_overflow_probability_adjustment = 0;

        success_chance = recipeChanceCalculator(recipeObject.getBaseRecipeSuccessChance());

        // Remove EU from the users network.
        if (!addEUToGlobalEnergyMap(user_uuid, -recipeObject.getEUStartCost())) {
            return false;
        }

        // Determine EU recipe output.
        eu_output = recipeObject.getEUOutput();

        // Reduce internal storage by hydrogen and helium quantity required for recipe.
        valid_fluid_map.put(Materials.Hydrogen.getGas(1), 0L);
        valid_fluid_map.put(Materials.Helium.getGas(1), 0L);

        double yield = recipeYieldCalculator();

        List<Pair<ItemStack, Long>> tmp_items_output = recipeObject.getOutputItems();
        FluidStack[] tmp_fluids_output = recipeObject.getOutputFluids().clone();

        // Iterate over item output list and apply yield values.
        for (int i = 0; i < tmp_items_output.size(); i++) {
            Pair<ItemStack, Long> tmp_0 = tmp_items_output.get(i);
            Pair<ItemStack, Long> tmp_1 = Pair.of(tmp_0.getLeft(), (long) (tmp_0.getRight() * yield));
            tmp_items_output.set(i, tmp_1);
        }

        // Iterate over fluid output list and apply yield values.
        for (FluidStack fluidStack : tmp_fluids_output) {
            fluidStack.amount *= yield;
        }

        output_items = tmp_items_output;
        mOutputFluids = tmp_fluids_output;
        updateSlots();

        return true;
    }

    private double success_chance;

    private void outputFailedChance() {

    }

    public void outputAfterRecipe_EM() {

        if (success_chance > random()) {
            outputFailedChance();
            return;
        }

        addEUToGlobalEnergyMap(user_uuid, eu_output);
        eu_output = 0;

        int index = 0;
        for (Pair<ItemStack, Long> item : output_items) {
            // If you can output the item, increment the input hatch index.
            try {
                if (outputItemToQuantumChest(mOutputBusses.get(index), item.getLeft(), item.getRight())) {
                    index++;
                }
            } catch(Exception ignored) { break; }
        }

        super.outputAfterRecipe_EM();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aTick == 1) {
            user_uuid = String.valueOf(getBaseMetaTileEntity().getOwnerUuid());
            user_name = getBaseMetaTileEntity().getOwnerName();

            strongCheckOrAddUser(user_uuid, user_name);
        }

        if (aTick % ticks_between_hatch_drain == 0) {
            drainFluidFromHatchesAndStoreInternally();
        }
    }

    private void addItemsToQuantumChest(IGregTechTileEntity quantum_chest, long amount) {
        long stored_quantity = quantum_chest.getProgress();
        int max_inventory_size = quantum_chest.getMaxItemCount();

        if ((stored_quantity + amount) > max_inventory_size) {
            quantum_chest.setItemCount(max_inventory_size);
        } else {
            quantum_chest.setItemCount((int) (amount + stored_quantity));
        }
    }

    private int[] coordinate_calculator(GT_MetaTileEntity_Hatch output_bus) {

        IGregTechTileEntity tile_entity = output_bus.getBaseMetaTileEntity();

        int x = tile_entity.getXCoord();
        int y = tile_entity.getYCoord();
        int z = tile_entity.getZCoord();

        // Get direction that the output bus is currently facing.
        switch (tile_entity.getFrontFacing()) {
            case 0:
                y -= 1;
            case 1:
                y += 1;
            case 2:
                z -= 1;
            case 3:
                z += 1;
            case 4:
                x -= 1;
            case 5:
                x += 1;
        }

        int[] coordinates = new int[3];

        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;

        return coordinates;
    }

    private boolean outputItemToQuantumChest(GT_MetaTileEntity_Hatch_OutputBus output_bus, ItemStack item, long amount) {

        int[] coords = coordinate_calculator(output_bus);
        int x = coords[0];
        int y = coords[1];
        int z = coords[2];

        try {
            // Get block in front of output bus.
            IGregTechTileEntity quantum_chest = (IGregTechTileEntity) getBaseMetaTileEntity().getTileEntity(x, y, z);

            // Slot 0 = Top input slot of quantum chest.
            // Slot 1 = Bottom output slot of quantum chest.
            // Slot 2 = Stored item slot.
            ItemStack quantum_chest_stored_item = quantum_chest.getStackInSlot(2);

            // Adjust sizes so no extra items from quantum chest nonsense.
            amount = amount - 64;
            item.stackSize = 64;

            // Check if chest contains item already. If not, add it.
            if (quantum_chest_stored_item == null) {
                quantum_chest.setInventorySlotContents(0, item);
                addItemsToQuantumChest(quantum_chest, amount);
                return true;
            } else if (quantum_chest_stored_item.isItemEqual(item)) {
                addItemsToQuantumChest(quantum_chest, amount);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add(GOLD + "---------------- Control Block Statistics ----------------");
        str.add("Spacetime Compression Field Grade: " + BLUE + SpacetimeCompressionFieldMetadata);
        str.add("Time Dilation Field Grade: " + BLUE + TimeAccelerationFieldMetadata);
        str.add("Stabilisation Field Grade: " + BLUE + StabilisationFieldMetadata);
        str.add(GOLD + "----------------- Internal Fluids Stored ----------------");
        valid_fluid_map.forEach((key, value) -> str.add(BLUE + key.getLocalizedName() + RESET +
                " : " + RED + GT_Utility.formatNumbers(value)));
        str.add(GOLD + "-----------------------------------------------------");
        return str.toArray(new String[0]);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[] {"?? No idea what this does."};
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
