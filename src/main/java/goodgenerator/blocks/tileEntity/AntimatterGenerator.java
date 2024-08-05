package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.filterByMTETier;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.util.GT_HatchElementBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntimatterGenerator extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
    implements IConstructable, ISurvivalConstructable {

    public static final String MAIN_NAME = "antimatterGenerator";
    protected IStructureDefinition<AntimatterGenerator> multiDefinition = null;
    protected long trueOutput = 0;
    protected int trueEff = 0;
    protected int times = 1;

    final static String[][] structure = new String[][]{{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 F                 ",
        "                 F                 ",
        "                 F                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                FDF                ",
        "                FDF                ",
        "                FDF                ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                EDE                ",
        "            EEEE D EEEE            ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "            EEEE D EEEE            ",
        "                EDE                ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "          EE     D     EE          ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "          EE     D     EE          ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "              EEEDEEE              ",
        "          EEEE   D   EEEE          ",
        "         E               E         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "       F                   F       ",
        "       F                   F       ",
        "       F                   F       ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         E               E         ",
        "          EEEE   D   EEEE          ",
        "              EEEDEEE              ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "       D EEE     D     EEE D       ",
        "       DE                 ED       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       DE                 ED       ",
        "       D EEE     D     EEE D       ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "        D EEEEE  D  EEEEE D        ",
        "        EE               EE        ",
        "       ED                 DE       ",
        "                                   ",
        "                                   ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "     BB                     BB     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     BB                     BB     ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "                                   ",
        "                                   ",
        "       ED                 DE       ",
        "        EE               EE        ",
        "        D EEEEE  D  EEEEE D        ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "         D   EEEEDEEEE   D         ",
        "         EEEE    D    EEEE         ",
        "       EED               DEE       ",
        "      E                     E      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      E                     E      ",
        "       EED               DEE       ",
        "         EEEE    D    EEEE         ",
        "         D   EEEEDEEEE   D         ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "          DEEEEEEDEEEEEED          ",
        "        EEE      D      EEE        ",
        "      EE  D             D  EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    B                         B    ",
        "          D             D          ",
        "          D             D          ",
        "               EEEEE               ",
        "                                   ",
        "               EEEEE               ",
        "          D             D          ",
        "          D             D          ",
        "    B                         B    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE  D             D  EE      ",
        "        EEE      D      EEE        ",
        "          DEEEEEEDEEEEEED          ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "           D     D     D           ",
        "          EEEEEEEDEEEEEEE          ",
        "        EE D     D     D EE        ",
        "      EE                   EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "           D           D           ",
        "            BEEAAAAAEEBD           ",
        "               CCCCC               ",
        "           DBEEAAAAAEEBD           ",
        "           D           D           ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE                   EE      ",
        "        EE D     D     D EE        ",
        "          EEEEEEEDEEEEEEE          ",
        "           D     D     D           ",
        "                                   "
    },{
        "                 D                 ",
        "            D  EEDEE  D            ",
        "          EEEEE  D  EEEEE          ",
        "       EEE  D         D  EEE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           BEAAEEEEEAAEB           ",
        "             CCEECEECC             ",
        "           BEAAEEEEEAAEB           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EEE  D         D  EEE       ",
        "          EEEEE  D  EEEEE          ",
        "            D  EEDEE  D            ",
        "                 D                 "
    },{
        "                 D                 ",
        "             DEEEDEEED             ",
        "         EEEEE   D   EEEEE         ",
        "       EE    D       D    EE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           EAEE     EEAE           ",
        "            CEE  C  EEC            ",
        "           EAEE     EEAE           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EE    D       D    EE       ",
        "         EEEEE   D   EEEEE         ",
        "             DEEEDEEED             ",
        "                 D                 "
    },{
        "              D  D  D              ",
        "             EEEEDEEEE             ",
        "         EEEE D  D  D EEEE         ",
        "      EEE                 EEE      ",
        "     E                       E     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "           EAE       EAE           ",
        "            CE   C   EC            ",
        "           EAE       EAE           ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     E                       E     ",
        "      EEE                 EEE      ",
        "         EEEE D  D  D EEEE         ",
        "             EEEEDEEEE             ",
        "              D  D  D              "
    },{
        "               DDDDD               ",
        "            EEEEEDEEEEE            ",
        "        EEEE   DDDDD   EEEE        ",
        "      EE       D   D       EE      ",
        "     E         D   D         E     ",
        "   EE                         EE   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "          EAE         EAE          ",
        "           CE    C    EC           ",
        "          EAE         EAE          ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   EE                         EE   ",
        "     E         D   D         E     ",
        "      EE       D   D       EE      ",
        "        EEEE   DDDDD   EEEE        ",
        "            EEEEEDEEEEE            ",
        "               DDDDD               "
    },{
        "               D   D               ",
        "            EEEEEEEEEEE            ",
        "        EEEE   DEEED   EEEE        ",
        "      EE        EEE        EE      ",
        "    EE          EEE          EE    ",
        "   E            DED            E   ",
        "                DED                ",
        "                DED                ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        " F        EAE    E    EAE        F ",
        " F         CE   ECE   EC         F ",
        " F        EAE    E    EAE        F ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "                DED                ",
        "                DED                ",
        "   E            DED            E   ",
        "    EE          EEE          EE    ",
        "      EE        E~E        EE      ",
        "        EEEE   DEEED   EEEE        ",
        "            EEEEEEEEEEE            ",
        "               D   D               "
    },{
        "            DDDD   DDDD            ",
        "        DDDDDDDDEGEDDDDDDDD        ",
        "      DDDDDDDDDDECEDDDDDDDDDD      ",
        "   DDDDDDDDD    ECE    DDDDDDDDD   ",
        "  DDDDDD        ECE        DDDDDD  ",
        "  DDDD          ECE          DDDD  ",
        " DDD            ECE            DD  ",
        " D              ECE             DD ",
        " D              ACA              D ",
        " D              ACA              D ",
        " D              ACA              D ",
        " DB              C              BD ",
        " D               C               D ",
        " D               C               D ",
        "FD        EAE   ECE   EAE        DF",
        "FD         CCCCCCCCCCCCC         DF",
        "FD        EAE   ECE   EAE        DF",
        " D               C               D ",
        " D               C               D ",
        " DB              C              BD ",
        " D              ACA              D ",
        " D              ACA              D ",
        " D              ACA              D ",
        " DD             ECE             DD ",
        "  DD            ECE            DD  ",
        "  DDDD          ECE          DDDD  ",
        "  DDDDDD        ECE        DDDDDD  ",
        "   DDDDDDDDD    ECE    DDDDDDDDD   ",
        "      DDDDDDDDDDECEDDDDDDDDDD      ",
        "        DDDDDDDDEGEDDDDDDDD        ",
        "            DDDD   DDDD            "
    },{
        "               D   D               ",
        "            EEEEEEEEEEE            ",
        "        EEEE   DEEED   EEEE        ",
        "      EE        EEE        EE      ",
        "    EE          EEE          EE    ",
        "   E            DED            E   ",
        "                DED                ",
        "                DED                ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        " F        EAE    E    EAE        F ",
        " F         CE   ECE   EC         F ",
        " F        EAE    E    EAE        F ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                 A                 ",
        "                 A                 ",
        "                 A                 ",
        "                DED                ",
        "                DED                ",
        "   E            DED            E   ",
        "    EE          EEE          EE    ",
        "      EE        EEE        EE      ",
        "        EEEE   DEEED   EEEE        ",
        "            EEEEEEEEEEE            ",
        "               D   D               "
    },{
        "               DDDDD               ",
        "            EEEEEDEEEEE            ",
        "        EEEE   DDDDD   EEEE        ",
        "      EE       D   D       EE      ",
        "     E         D   D         E     ",
        "   EE                         EE   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "          EAE         EAE          ",
        "           CE    C    EC           ",
        "          EAE         EAE          ",
        "                                   ",
        "                                   ",
        "  B                             B  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   EE                         EE   ",
        "     E         D   D         E     ",
        "      EE       D   D       EE      ",
        "        EEEE   DDDDD   EEEE        ",
        "            EEEEEDEEEEE            ",
        "               DDDDD               "
    },{
        "              D  D  D              ",
        "             EEEEDEEEE             ",
        "         EEEE D  D  D EEEE         ",
        "      EEE                 EEE      ",
        "     E                       E     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "           EAE       EAE           ",
        "            CE   C   EC            ",
        "           EAE       EAE           ",
        "                                   ",
        "                                   ",
        "  BB                           BB  ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     E                       E     ",
        "      EEE                 EEE      ",
        "         EEEE D  D  D EEEE         ",
        "             EEEEDEEEE             ",
        "              D  D  D              "
    },{
        "                 D                 ",
        "             DEEEDEEED             ",
        "         EEEEE   D   EEEEE         ",
        "       EE    D       D    EE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           EAEE     EEAE           ",
        "            CEE  C  EEC            ",
        "           EAEE     EEAE           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EE    D       D    EE       ",
        "         EEEEE   D   EEEEE         ",
        "             DEEEDEEED             ",
        "                 D                 "
    },{
        "                 D                 ",
        "            D  EEDEE  D            ",
        "          EEEEE  D  EEEEE          ",
        "       EEE  D         D  EEE       ",
        "     EE                     EE     ",
        "    E                         E    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "           BEAAEEEEEAAEB           ",
        "             CCEECEECC             ",
        "           BEAAEEEEEAAEB           ",
        "                                   ",
        "                                   ",
        "   B                           B   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    E                         E    ",
        "     EE                     EE     ",
        "       EEE  D         D  EEE       ",
        "          EEEEE  D  EEEEE          ",
        "            D  EEDEE  D            ",
        "                 D                 "
    },{
        "                                   ",
        "           D     D     D           ",
        "          EEEEEEEDEEEEEEE          ",
        "        EE D     D     D EE        ",
        "      EE                   EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "           D           D           ",
        "            BEEAAAAAEEBD           ",
        "               CCCCC               ",
        "           DBEEAAAAAEEBD           ",
        "           D           D           ",
        "                                   ",
        "   BB                         BB   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE                   EE      ",
        "        EE D     D     D EE        ",
        "          EEEEEEEDEEEEEEE          ",
        "           D     D     D           ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "          DEEEEEEDEEEEEED          ",
        "        EEE      D      EEE        ",
        "      EE  D             D  EE      ",
        "     E                       E     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "    B                         B    ",
        "          D             D          ",
        "          D             D          ",
        "               EEEEE               ",
        "                                   ",
        "               EEEEE               ",
        "          D             D          ",
        "          D             D          ",
        "    B                         B    ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     E                       E     ",
        "      EE  D             D  EE      ",
        "        EEE      D      EEE        ",
        "          DEEEEEEDEEEEEED          ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "         D   EEEEDEEEE   D         ",
        "         EEEE    D    EEEE         ",
        "       EED               DEE       ",
        "      E                     E      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         D               D         ",
        "    BB   D               D   BB    ",
        "         D               D         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      E                     E      ",
        "       EED               DEE       ",
        "         EEEE    D    EEEE         ",
        "         D   EEEEDEEEE   D         ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "        D EEEEE  D  EEEEE D        ",
        "        EE               EE        ",
        "       ED                 DE       ",
        "                                   ",
        "                                   ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "     BB                     BB     ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "     BB                     BB     ",
        "        D                 D        ",
        "        D                 D        ",
        "        D                 D        ",
        "                                   ",
        "                                   ",
        "       ED                 DE       ",
        "        EE               EE        ",
        "        D EEEEE  D  EEEEE D        ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "       D EEE     D     EEE D       ",
        "       DE                 ED       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "      FD                   DF      ",
        "       D                   D       ",
        "       D                   D       ",
        "      BD                   DB      ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       D                   D       ",
        "       DE                 ED       ",
        "       D EEE     D     EEE D       ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                 D                 ",
        "              EEEDEEE              ",
        "          EEEE   D   EEEE          ",
        "         E               E         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "       F                   F       ",
        "       F                   F       ",
        "       F                   F       ",
        "                                   ",
        "                                   ",
        "      BBB                 BBB      ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         E               E         ",
        "          EEEE   D   EEEE          ",
        "              EEEDEEE              ",
        "                 D                 ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "            EEEEEDEEEEE            ",
        "          EE     D     EE          ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "        BB               BB        ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "          EE     D     EE          ",
        "            EEEEEDEEEEE            ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                EDE                ",
        "            EEEE D EEEE            ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "         BBB           BBB         ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "            EEEE D EEEE            ",
        "                EDE                ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "           BBBB     BBBB           ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "               EEDEE               ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "              BBBBBBB              ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                FDF                ",
        "                FDF                ",
        "                FDF                ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                 D                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    },{
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                 F                 ",
        "                 F                 ",
        "                 F                 ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   ",
        "                                   "
    }};

    private static final IStructureDefinition<AntimatterGenerator> STRUCTURE_DEFINITION = IStructureDefinition
        .<AntimatterGenerator>builder()
                .addShape(
                    MAIN_NAME, transpose(structure))
                .addElement('A', lazy(x -> Glasses.chainAllGlasses()))
                .addElement('B', lazy(x -> ofFrame(Materials.Iron)))
                .addElement('C', lazy(x -> ofBlock(x.getCoilBlock(), x.getCoilMeta())))
                .addElement('D', lazy(x -> ofBlock(x.getCasingBlock(1), x.getCasingMeta())))
                .addElement('E', lazy(x -> ofBlock(x.getCasingBlock(2), x.getCasingMeta())))
                //idk how you want to handle these adders
                .addElement('F', lazy(x -> GT_HatchElementBuilder.<AntimatterGenerator>builder()
                    .anyOf(GT_HatchElement.ExoticEnergy)
                    .adder(AntimatterGenerator::addLaserSource)
                    .casingIndex(x.textureIndex())
                    .hatchItemFilterAnd(x2 -> filterByMTETier(x2.hatchTier(), Integer.MAX_VALUE))
                    .dot(4)
                    .build()))
                .addElement('G', lazy(x -> buildHatchAdder(AntimatterGenerator.class)
                    .atLeast(GT_HatchElement.InputHatch)
                    .casingIndex(0)
                    .dot(1)
                    .buildAndChain(x.getCasingBlock(1), x.getCasingMeta())))
                .build();

    private boolean addLaserSource(IGregTechTileEntity aBaseMetaTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = aBaseMetaTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoTunnel tHatch) {
            if (tHatch.getTierForStructure() < hatchTier()) return false;
            tHatch.updateTexture(aBaseCasingIndex);
            return mExoticEnergyHatches.add(tHatch);
        }
        return false;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("AntimatterGenerator.hint", 8);
    }

    public AntimatterGenerator(String name) {
        super(name);
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public AntimatterGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {

        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {

        super.saveNBTData(aNBT);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = "Probably makes: " + EnumChatFormatting.RED
            + GT_Utility.formatNumbers(Math.abs(this.trueOutput))
            + EnumChatFormatting.RESET
            + " EU/t";
        info[6] = "Problems: " + EnumChatFormatting.RED
            + (this.getIdealStatus() - this.getRepairStatus())
            + EnumChatFormatting.RESET
            + " Efficiency: "
            + EnumChatFormatting.YELLOW
            + trueEff
            + EnumChatFormatting.RESET
            + " %";
        return info;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            if (mMaxProgresstime != 0 && mProgresstime % 20 == 0) {
                startRecipeProcessing();
                List<FluidStack> inputFluids = getStoredFluids();
                long containedAntimatter = 0;
                FluidStack catalystFluid = null;
                int i;

                for (i = 0; i < inputFluids.size(); i++) {
                    FluidStack inputFluid = inputFluids.get(i);
                    if (inputFluid.isFluidEqual(MaterialsUEVplus.Antimatter.getFluid(1)))
                        containedAntimatter += inputFluid.amount;
                    else catalystFluid = inputFluid;
                    inputFluid.amount = 0;
                }

                //If i > 1, we iterated 3 times and have too many fluids.
                if (i == 1 && containedAntimatter > 0 && catalystFluid != null) {
                    createEU(containedAntimatter, catalystFluid);
                }

                return super.onRunningTick(aStack);
            }
        }
        return true;
    }

    private static final Map<Fluid, Float> catalysts;

    static {
        catalysts = new HashMap<>();
        catalysts.put(Materials.Copper.mFluid, 1F);
        catalysts.put(Materials.SuperconductorUIVBase.mFluid, 1.02F);
        catalysts.put(Materials.SuperconductorUMVBase.mFluid, 1.03F);
        catalysts.put(MaterialsUEVplus.BlackDwarfMatter.mFluid, 1.04F);
    }

    //(Antimatter^(EXP) * 1e12 )/(Math.min((Antimatter/Matter),(Matter/Antimatter)))
    public void createEU(long antimatter, FluidStack catalyst) {
        Float modifier = catalysts.get(catalyst.getFluid());
        long catalystCount = catalyst.amount;
        long generatedEU = 0;

        if (modifier != null) {
            generatedEU = (long) ((Math.pow(antimatter, modifier) * 1e12) / (Math.min((antimatter / catalystCount), (catalystCount / antimatter))));
        }

        long amps = maxAmperesOut();
        addEnergyOutput_EM(generatedEU / amps, amps);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!structureCheck_EM(mName, 17, 15, 3) && mMaintenanceHatches.size() == 1
            && mDynamoHatches.size() + eDynamoMulti.size() == 60) return false;
        return !(mInputHatches.size() == 2);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AntimatterGenerator(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Antimatter Generator")
            .addInfo("Controller block for the Shielded Lagrangian Annihilation Matrix")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(35, 31, 35, false)
            .addController("Front bottom")
            .addInfo("No crashy please :3")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44),
                new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44),
                new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 17, 15, 3, elementBudget, env, false, true);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 17, 15, 3, itemStack, hintsOnly);
    }

    public Block getCoilBlock() {
        return Loaders.protomatterActivationCoil;
    }

    public int getCoilMeta() {
        return 0;
    }

    public Block getCasingBlock(int type) {
        switch(type) {
            case 1:
                return Loaders.antimatterContainmentCasing;
            case 2:
                return ItemList.Casing_AdvancedRadiationProof.getBlock();
            default:
                return Loaders.antimatterContainmentCasing;
        }
    }

    public int getCasingMeta() {
        return 0;
    }

    public int textureIndex() {
        return 53;
    }

    public int hatchTier() {
        return 6;
    }


}
