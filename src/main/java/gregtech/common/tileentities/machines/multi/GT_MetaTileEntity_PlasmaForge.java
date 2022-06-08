package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;


import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;



public class GT_MetaTileEntity_PlasmaForge extends GT_MetaTileEntity_AbstractMultiFurnace<GT_MetaTileEntity_PlasmaForge> implements IConstructable {
    private int mHeatingCapacity = 0;

    protected static final int NULL_CASING = 12;
    protected static final int USEFUL_CASING = 13;
    protected static final int DIM_CASING = 14;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_PlasmaForge> STRUCTURE_DEFINITION = StructureDefinition.<GT_MetaTileEntity_PlasmaForge>builder()
            .addShape(STRUCTURE_PIECE_MAIN, new String[][] {
                    {"                                 ", "         N   N     N   N         ", "         N   N     N   N         ", "         N   N     N   N         ", "                                 ", "                                 ", "                                 ", "         N   N     N   N         ", "         N   N     N   N         ", " NNN   NNN   N     N   NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN "},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CCC   CCC   N     N   CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN    N N    NbbbN NbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "      NNNbbbbbNNsNNbbbbbNNN      ", "    ss   bCCCb     bCCCb   ss    ", "   s     N   N     N   N     s   ", "   s                         s   ", "  N      N   N     N   N      N  ", "  N      bCCCb     bCCCb      N  ", "  N     sbbbbbNNsNNbbbbbs     N  ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ", "         bCCCb     bCCCb         ", "  s      NCCCN     NCCCN      s  ", "  s      NCCCN     NCCCN      s  ", "         NCCCN     NCCCN         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN    NbN    NbbbNNNbbbN",},
                    {"                                 ", "         N   N     N   N         ", "   s     N   N     N   N     s   ", "  s      NCCCN     NCCCN      s  ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "         N   N     N   N         ", " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN     NbN     NNN   NNN ",},
                    {"                                 ", "                                 ", "   s                         s   ", "  s      NCCCN     NCCCN      s  ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "                                 ", "   N   N                 N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N                 N   N   ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "   N   N                 N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N       NbN       N   N   ",},
                    {"                                 ", "         N   N     N   N         ", "  N      N   N     N   N      N  ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "         N   N     N   N         ", " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN     NbN     NNN   NNN ",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ", "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN    NbN    NbbbNNNbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  s     sbbbbbNNsNNbbbbbs     s  ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN",},
                    {" NNN   NNN   N     N   NNN   NNN ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NNNN   NNNCCCb     bCCCNNN   NNNN", " CCC   CCC   N     N   CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN    NbN    NbbbN NbbbN",},
                    {"                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ", " CCC   CCC   N     N   CCC   CCC ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN     NbN     NNN   NNN ",},
                    {"                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N      NbN      N     N  ",},
                    {"                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N      NbN      N     N  ",},
                    {" NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ", "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N     NsNsN     N     N  ",},
                    {"                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N    NbbbbbN    N     N  ",},
                    {"                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                N                ", " NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ",},
                    {"                                 ", "                                 ", "  s     s               s     s  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  s     s               s     s  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                ~                ", "               NNN               ", "  NbbbbbNbbbbNbbbbbNbbbbNbbbbbN  ",},
                    {"                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                N                ", " NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ",},
                    {"                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N               N     N  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N    NbbbbbN    N     N  ",},
                    {" NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ", "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N     NsNsN     N     N  ",},
                    {"                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N      NbN      N     N  ",},
                    {"                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  N     N      NbN      N     N  ",},
                    {"                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ", " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ", " CCC   CCC   N     N   CCC   CCC ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN     NbN     NNN   NNN ",},
                    {" NNN   NNN   N     N   NNN   NNN ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ", "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NNNN   NNNCCCb     bCCCNNN   NNNN", " CCC   CCC   N     N   CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN    NbN    NbbbN NbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  s     sbbbbbNNsNNbbbbbs     s  ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ", "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN    NbN    NbbbNNNbbbN",},
                    {"                                 ", "         N   N     N   N         ", "  N      N   N     N   N      N  ", "         NCCCN     NCCCN         ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "         N   N     N   N         ", " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN     NbN     NNN   NNN ",},
                    {"                                 ", "                                 ", "   s                         s   ", "  s      NCCCN     NCCCN      s  ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "                                 ", "   N   N                 N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N                 N   N   ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "   N   N                 N   N   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   N   N       NbN       N   N   ",},
                    {"                                 ", "         N   N     N   N         ", "   s     N   N     N   N     s   ", "  s      NCCCN     NCCCN      s  ", "                                 ", "                                 ", "                                 ", "         NCCCN     NCCCN         ", "         N   N     N   N         ", " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " NNN   NNN     NbN     NNN   NNN ",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ", "         bCCCb     bCCCb         ", "  s      NCCCN     NCCCN      s  ", "  s      NCCCN     NCCCN      s  ", "         NCCCN     NCCCN         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "NbbbNNNbbbN    NbN    NbbbNNNbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "      NNNbbbbbNNsNNbbbbbNNN      ", "    ss   bCCCb     bCCCb   ss    ", "   s     N   N     N   N     s   ", "   s                         s   ", "  N      N   N     N   N      N  ", "  N      bCCCb     bCCCb      N  ", "  N     sbbbbbNNsNNbbbbbs     N  ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "  s     s               s     s  ", " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN",},
                    {"         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         N   N     N   N         ", "                                 ", "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CCC   CCC   N     N   CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ", "  N     N               N     N  ", "                                 ", "  N     N               N     N  ", "  N     N               N     N  ", "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "NbbbN NbbbN    N N    NbbbN NbbbN",},
                    {"                                 ", "         N   N     N   N         ", "         N   N     N   N         ", "         N   N     N   N         ", "                                 ", "                                 ", "                                 ", "         N   N     N   N         ", "         N   N     N   N         ", " NNN   NNN   N     N   NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ", "                                 ", "                                 ", "                                 ", " NNN   NNN             NNN   NNN ",}
           })
            .addElement('C', ofCoil(GT_MetaTileEntity_PlasmaForge::setCoilLevel, GT_MetaTileEntity_PlasmaForge::getCoilLevel))
            .addElement('b', ofHatchAdderOptional(GT_MetaTileEntity_PlasmaForge::addBottomHatch, USEFUL_CASING, 3, GregTech_API.sBlockCasings1, USEFUL_CASING))
            .addElement('N', ofBlock(GregTech_API.sBlockCasings1, NULL_CASING))
            .addElement('s', ofBlock(GregTech_API.sBlockCasings1, DIM_CASING))
            .build();

    public GT_MetaTileEntity_PlasmaForge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PlasmaForge(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PlasmaForge(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Blast Furnace")
                .addInfo("Controller block for the Dimensionally Transcendent Plasma Forge")
                .addInfo("Author: Colen")
                .addSeparator()
                .beginStructureBlock(33, 24, 33, false)
                .addStructureInfo("Structure is too complex! See schematic for details.")
                .addStructureInfo("2112 Heating coils required.")
                .addStructureInfo("120 Dimensional bridge blocks required.")
                .addStructureInfo("1270 Dimensional injection casing required.")
                .addStructureInfo("2121 Dimensionally transcendent casing required.")
                .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                        casingTexturePages[0][DIM_CASING],
                        TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE).extFacing().build(),
                        TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW).extFacing().glow().build()};
            return new ITexture[]{
                    casingTexturePages[0][DIM_CASING],
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE).extFacing().build(),
                    TextureFactory.builder().addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW).extFacing().glow().build()};
        }
        return new ITexture[]{casingTexturePages[0][DIM_CASING]};
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "PlasmaForge.png");
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack){
        return 0;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sPlasmaForgeRecipes;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PlasmaForge> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return processRecipe(getCompactedInputs(), getCompactedFluids());
    }

    protected boolean processRecipe(ItemStack[] tItems, FluidStack[] tFluids) {

        // Get information about multi configuration.
        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        // Look up recipe.
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sPlasmaForgeRecipes.findRecipe(
                getBaseMetaTileEntity(),
                false,
                V[tTier],
                tFluids,
                tItems
        );

        // Sanity checks.
        if (tRecipe == null)
            return false;

        if (!tRecipe.isRecipeInputEqual(true, tFluids, tItems)) {
            return false;
        }

        // Vital recipe info.
        mEUt = -tRecipe.mEUt;
        mMaxProgresstime = tRecipe.mDuration;

        // Outputs.
        mOutputItems = tRecipe.mOutputs.clone();
        mOutputFluids = tRecipe.mFluidOutputs.clone();
        updateSlots();

        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mHeatingCapacity = 0;

        setCoilLevel(HeatingCoilLevel.None);

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 16, 21, 16))
            return false;

        if (getCoilLevel() == HeatingCoilLevel.None)
            return false;

        if (mMaintenanceHatches.size() != 1)
            return false;

        this.mHeatingCapacity = (int) getCoilLevel().getHeat() + 100 * (GT_Utility.getTier(getMaxInputVoltage()) - 2);
        return true;
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null)
            return false;
        FluidStack tLiquid = aLiquid.copy();

        return dumpFluid(this.mOutputHatches, tLiquid, true) ||
            dumpFluid(this.mOutputHatches, tLiquid, false);
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (!isValidMetaTileEntity(tHatch))
                continue;
            storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
        }

        return new String[]{
            "------------ Critical Information ------------",
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " +
                    EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mProgresstime) + EnumChatFormatting.RESET + "t / " +
                    EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(mMaxProgresstime) + EnumChatFormatting.RESET + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " +
                    EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                    EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(maxEnergy) + EnumChatFormatting.RESET + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " +
                    EnumChatFormatting.RED + GT_Utility.formatNumbers(-mEUt) + EnumChatFormatting.RESET + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " +
                    EnumChatFormatting.YELLOW + GT_Utility.formatNumbers(getMaxInputVoltage()) + EnumChatFormatting.RESET + " EU/t(*2A) " +
                    StatCollector.translateToLocal("GT5U.machines.tier") + ": " +
                    EnumChatFormatting.YELLOW + VN[GT_Utility.getTier(getMaxInputVoltage())] + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.EBF.heat") + ": " +
                    EnumChatFormatting.GREEN + GT_Utility.formatNumbers(mHeatingCapacity) + EnumChatFormatting.RESET + " K",
            "----------------------------------------------"

        };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 16,21,16);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }
}
