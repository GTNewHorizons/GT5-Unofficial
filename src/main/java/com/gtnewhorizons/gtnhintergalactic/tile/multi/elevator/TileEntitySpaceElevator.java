package com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.EnumChatFormatting.*;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_infuser;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.gtnhintergalactic.Tags;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.client.lore.LoreHolder;
import com.gtnewhorizons.gtnhintergalactic.config.Config;
import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;
import com.gtnewhorizons.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.GT_MetaTileEntity_EnhancedMultiBlockBase_EM;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleBase;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import galaxyspace.core.register.GSBlocks;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_Utility;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

/**
 * Space Elevator multiblock used to start space projects for end game crafts
 *
 * @author minecraft7771
 */
public class TileEntitySpaceElevator extends GT_MetaTileEntity_EnhancedMultiBlockBase_EM
        implements ISurvivalConstructable {
    // region Structure and textures variables

    /** List of project modules in this elevator */
    public ArrayList<TileEntityModuleBase> mProjectModuleHatches = new ArrayList<>();
    /** TE of the cable */
    protected TileEntitySpaceElevatorCable elevatorCable;

    /** Motor tier of the Space Elevator */
    protected int motorTier = 0;

    /** Flag if the chunks of the machine are loaded by it */
    private boolean isLoadedChunk;

    /** Flag if the extension for more modules is enabled */
    private boolean isExtensionEnabled = false;

    /** Interval in which the modules will be supplied with power in ticks */
    private static final int MODULE_CHARGE_INTERVAL = 20;
    /** Multiplier for the internal EU buffer */
    private static final int INTERNAL_BUFFER_MULTIPLIER = 8;

    /** Index of the Space Elevator base casing */
    public static final int CASING_INDEX_BASE = 32 * 128;

    /** Name of the main structure piece */
    private static final String STRUCTURE_PIECE_MAIN = "main";

    /** Name of the extended structure piece */
    private static final String STRUCTURE_PIECE_EXTENDED = "extended";

    /** Window ID of the contributors child window */
    private static final int CONTRIBUTORS_WINDOW_ID = 10;

    /** Vertical offset of the main structure piece */
    private static final int STRUCTURE_PIECE_MAIN_VERT_OFFSET = 39;
    /** Horizontal offset of the main structure piece */
    private static final int STRUCTURE_PIECE_MAIN_HOR_OFFSET = 17;
    /** Depth offset of the main structure piece */
    private static final int STRUCTURE_PIECE_MAIN_DEPTH_OFFSET = 14;

    /** Vertical offset of the extended structure piece */
    private static final int STRUCTURE_PIECE_EXTENDED_VERT_OFFSET = 1;
    /** Horizontal offset of the extended structure piece */
    private static final int STRUCTURE_PIECE_EXTENDED_HOR_OFFSET = 23;
    /** Depth offset of the extended structure piece */
    private static final int STRUCTURE_PIECE_EXTENDED_DEPTH_OFFSET = 20;

    /** Lore tooltip of the machine. Randomly picked from a selection */
    @LoreHolder("gt.blockmachines.multimachine.ig.elevator.lore")
    private static String loreTooltip;

    // spotless:off
    /** Structure definition of this machine */
    private static final IStructureDefinition<TileEntitySpaceElevator> STRUCTURE_DEFINITION =
        StructureDefinition.<TileEntitySpaceElevator>builder()
            .addShape(STRUCTURE_PIECE_MAIN, new String[][] {
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "               FF FF               ", "               AAAAA               " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "               D   D               ", "            FFFFF FFFFF            ", "            AAAAAAAAAAA            " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "            DDDDE EDDDD            ", "          FFFFFFF FFFFFFF          ", "          AAAAAAAAAAAAAAA          " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "          DD   DE ED   DD          ", "         FFFFFFD   DFFFFFF         ", "        AAAAAAAAAAAAAAAAAAA        " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               D   D               ", "         FFF           FFF         ", "       AAAAAAAAAAAAAAAAAAAAA       " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "      AAAAAAAAAAAAAAAAAAAAAAA      " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "                                   ", "     AAAAAAAAAAAAAAAAAAAAAAAAA     " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "              HDE EDH              ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "    AAAAAAAAAAAAAAAAAAAAAAAAAAA    " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "            HH DE ED HH            ", "                                   ", "                                   ", "                                   ", "                                   ", "               X X X               ", "               X X X               ", "               X X X               ", "               X X X               ", "   AAAAAAAAAAAAX X XAAAAAAAAAAAA   " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "         E               E         ", "         EHH           HHE         ", "         E               E         ", "         E               E         ", "         E               E         ", "         E               E         ", "         E     X X X     E         ", "         E     I I I     E         ", "         E     X X X     E         ", "   FF    E     X X X     E    FF   ", "   AAAAAAAAAAAAX X XAAAAAAAAAAAA   " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "          E             E          ", "          E             E          ", "          E             E          ", "          E             E          ", "          E             E          ", "         HE             EH         ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "   D                           D   ", "  FFF          F F F          FFF  ", "  AAAAAAAAAAAAA     AAAAAAAAAAAAA  " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "           E           E           ", "           E           E           ", "           E           E           ", "           E           E           ", "           E           E           ", "                                   ", "                                   ", "         H               H         ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "   D                           D   ", "  FFF          F F F          FFF  ", "  AAAAAAAAAAAAA     AAAAAAAAAAAAA  " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "             HH     HH             ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "        H                 H        ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "  D                             D  ", " FFF          FFFFFFF          FFF ", " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                FFF                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "            HE       EH            ", "             E       E             ", "             E       E             ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "        H                 H        ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "  D                             D  ", " FFF         FFFFFFFFF         FFF ", " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " },
                {"                FFF                ", "                 E                 ", "                 E                 ", "                 E                 ", "                 E                 ", "                 E                 ", "               F   F               ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "                                   ", "                                   ", "                FFF                ", "                                   ", "                                   ", "            H         H            ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                FFF                ", "                                   ", "                                   ", "                                   ", "                                   ", "       H                   H       ", "                                   ", "                                   ", "                                   ", "                                   ", "                XXX                ", "                X~X                ", "  D             XXX             D  ", " FFF        FFFFFFFFFFF        FFF ", " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " },
                {"               F D F               ", "                 D                 ", "                 D                 ", "                 D                 ", "                 D                 ", "                 D                 ", "              F  D  F              ", "                 D                 ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "           DD    D    DD           ", "           D     D     D           ", "           D     D     D           ", "           D   F D F   D           ", "           D     C     D           ", "           D     C     D           ", "           D     C     D           ", "           D     C     D           ", "          DD     C     DD          ", "          D      C      D          ", "          D      C      D          ", "          D      C      D          ", "         DD      C      DD         ", "         D     FDCDF     D         ", "         D      DCD      D         ", "        DD      DCD      DD        ", "        D       DCD       D        ", "        D       DCD       D        ", "       DD      DDCDD      DD       ", "       D       D C D       D       ", "       D       D C D       D       ", "      DD       D C D       DD      ", "      D        D C D        D      ", "     DD XX     XDCDX     XX DD     ", "    DD  XI     XDCDX     IX  DD    ", " DDDD   XX     XDCDX     XX   DDDD ", "FFFD    XXFFFFFDDDDDFFFFFXX    DFFF", "AAAAAAAAXX  AAAXXXXXAAA  XXAAAAAAAA" },
                {"              F     F              ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "             F       F             ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "           EE         EE           ", "           EE         EE           ", "           E           E           ", "           E           E           ", "           E  F     F  E           ", "           E           E           ", "           E           E           ", "           E           E           ", "          EE           EE          ", "          EE           EE          ", "          E             E          ", "          E             E          ", "         EE             EE         ", "         EE             EE         ", "         E    FD   DF    E         ", "        EE     D   D     EE        ", "        EE     D   D     EE        ", "        E      D   D      E        ", "       EE      D   D      EE       ", "       EE      D   D      EE       ", "       E                   E       ", "      EE                   EE      ", "      EE                   EE      ", "     EE                     EE     ", "    EEE       XD   DX       EEE    ", "   EEE        XD   DX        EEE   ", "  EE          XD   DX          EE  ", "FFF         FFFDDDDDFFF         FFF", "AAAAAAAA    AAAXXXXXAAA    AAAAAAAA" },
                {"              FD   DF              ", "              ED   DE              ", "              ED   DE              ", "              ED   DE              ", "              ED   DE              ", "              ED   DE              ", "             F D   D F             ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D   D               ", "               D B D               ", "              FD - DF              ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "              FC - CF              ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "               C - C               ", "        XX    XC - CX    XX        ", "        XI    XC - CX    IX        ", "        XX    XC - CX    XX        ", "        XXFFFFFDDDDDFFFFFXX        ", "AAAAAAAAXX  AAAXXXXXAAA  XXAAAAAAAA" },
                {"              F     F              ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "             F       F             ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "           EE         EE           ", "           EE         EE           ", "           E           E           ", "           E           E           ", "           E  F     F  E           ", "           E           E           ", "           E           E           ", "           E           E           ", "          EE           EE          ", "          EE           EE          ", "          E             E          ", "          E             E          ", "         EE             EE         ", "         EE             EE         ", "         E    FD   DF    E         ", "        EE     D   D     EE        ", "        EE     D   D     EE        ", "        E      D   D      E        ", "       EE      D   D      EE       ", "       EE      D   D      EE       ", "       E                   E       ", "      EE                   EE      ", "      EE                   EE      ", "     EE                     EE     ", "    EEE       XD   DX       EEE    ", "   EEE        XD   DX        EEE   ", "  EE          XD   DX          EE  ", "FFF         FFFDDDDDFFF         FFF", "AAAAAAAA    AAAXXXXXAAA    AAAAAAAA" },
                {"               F D F               ", "                 D                 ", "                 D                 ", "                 D                 ", "                 D                 ", "                 D                 ", "              F  D  F              ", "                 D                 ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "            D    D    D            ", "           DD    D    DD           ", "           D     D     D           ", "           D     D     D           ", "           D   F D F   D           ", "           D     C     D           ", "           D     C     D           ", "           D     C     D           ", "           D     C     D           ", "          DD     C     DD          ", "          D      C      D          ", "          D      C      D          ", "          D      C      D          ", "         DD      C      DD         ", "         D     FDCDF     D         ", "         D      DCD      D         ", "        DD      DCD      DD        ", "        D       DCD       D        ", "        D       DCD       D        ", "       DD      DDCDD      DD       ", "       D       D C D       D       ", "       D       D C D       D       ", "      DD       D C D       DD      ", "      D        D C D        D      ", "     DD XX     XDCDX     XX DD     ", "    DD  XI     XDCDX     IX  DD    ", " DDDD   XX     XDCDX     XX   DDDD ", "FFFD    XXFFFFFDDDDDFFFFFXX    DFFF", "AAAAAAAAXX  AAAXXXXXAAA  XXAAAAAAAA" },
                {"                FFF                ", "                 E                 ", "                 E                 ", "                 E                 ", "                 E                 ", "                 E                 ", "               F   F               ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "              E     E              ", "                                   ", "                                   ", "                FFF                ", "                                   ", "                                   ", "            H         H            ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                FFF                ", "                                   ", "                                   ", "                                   ", "                                   ", "       H                   H       ", "                                   ", "                                   ", "                                   ", "                                   ", "                XXX                ", "                XXX                ", "  D             XXX             D  ", " FFF        FFFFFFFFFFF        FFF ", " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                FFF                ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "             E       E             ", "            HE       EH            ", "             E       E             ", "             E       E             ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "        H                 H        ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "  D                             D  ", " FFF         FFFFFFFFF         FFF ", " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "             HH     HH             ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "            E         E            ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "        H                 H        ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "  D                             D  ", " FFF          FFFFFFF          FFF ", " AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "           E           E           ", "           E           E           ", "           E           E           ", "           E           E           ", "           E           E           ", "                                   ", "                                   ", "         H               H         ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "   D                           D   ", "  FFF          F F F          FFF  ", "  AAAAAAAAAAAAA     AAAAAAAAAAAAA  " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "          E             E          ", "          E             E          ", "          E             E          ", "          E             E          ", "          E             E          ", "         HE             EH         ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "   D                           D   ", "  FFF          F F F          FFF  ", "  AAAAAAAAAAAAA     AAAAAAAAAAAAA  " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "         E               E         ", "         EHH           HHE         ", "         E               E         ", "         E               E         ", "         E               E         ", "         E               E         ", "         E     X X X     E         ", "         E     I I I     E         ", "         E     X X X     E         ", "   FF    E     X X X     E    FF   ", "   AAAAAAAAAAAAX X XAAAAAAAAAAAA   " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "            HH DE ED HH            ", "                                   ", "                                   ", "                                   ", "                                   ", "               X X X               ", "               X X X               ", "               X X X               ", "               X X X               ", "   AAAAAAAAAAAAX X XAAAAAAAAAAAA   " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "              HDE EDH              ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "    AAAAAAAAAAAAAAAAAAAAAAAAAAA    " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "                                   ", "     AAAAAAAAAAAAAAAAAAAAAAAAA     " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               DE ED               ", "                                   ", "                                   ", "      AAAAAAAAAAAAAAAAAAAAAAA      " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "               DE ED               ", "               D   D               ", "         FFF           FFF         ", "       AAAAAAAAAAAAAAAAAAAAA       " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                E E                ", "          DD   DE ED   DD          ", "         FFFFFFD   DFFFFFF         ", "        AAAAAAAAAAAAAAAAAAA        " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "            DDDDE EDDDD            ", "          FFFFFFF FFFFFFF          ", "          AAAAAAAAAAAAAAA          " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "               D   D               ", "            FFFFF FFFFF            ", "            AAAAAAAAAAA            " },
                {"                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "                                   ", "               FF FF               ", "               AAAAA               " }
            })
            .addShape(STRUCTURE_PIECE_EXTENDED, new String[][]{
                {"                                               ", "                                               ", "                                               ", "                    FFFFFFF                    ", "                    AAAAAAA                    "},
                {"                     X X X                     ", "                     X X X                     ", "                     X X X                     ", "                   FFX X XFF                   ", "                 AAAAX X XAAAA                 "},
                {"                     X X X                     ", "                     I I I                     ", "                     X X X                     ", "                   FFX X XFF                   ", "              AAAAAAAX X XAAAAAAA              "},
                {"                                               ", "                                               ", "                                               ", "                   FFF F FFF                   ", "            AAAAAAAAAA   AAAAAAAAAA            "},
                {"                                               ", "                                               ", "                                               ", "                  FFFF F FFFF                  ", "          AAAA   AAAAA   AAAAA   AAAA          "},
                {"                                               ", "                                               ", "                                               ", "                  FFFF F FFFF                  ", "        AAAA     AAAAA   AAAAA     AAAA        "},
                {"                                               ", "                                               ", "                                               ", "                  FFFFFFFFFFF                  ", "       AAA      AAAAAAAAAAAAAAA      AAA       "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "      AAA       AA           AA       AAA      "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "     AAA                               AAA     "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "     AA                                 AA     "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "    AA                                   AA    "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "    AA                                   AA    "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "   AA                                     AA   "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "   AA                                     AA   "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "  AA                                       AA  "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "  AA                                       AA  "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "  AA  AA                               AA  AA  "},
                {"                                               ", "                                               ", "                                               ", "                                               ", " AAAAAAA                               AAAAAAA "},
                {"                                               ", "                                               ", "                                               ", "    FFF                                 FFF    ", " AAAAAA                                 AAAAAA "},
                {"                                               ", "                                               ", "                                               ", " FFFFFF                                 FFFFFF ", " AAAAAA                                 AAAAAA "},
                {"                                               ", "                                               ", "                                               ", "FFFFFFF                                 FFFFFFF", "AAAAAAA                                 AAAAAAA"},
                {" XX                                         XX ", " XI                                         IX ", " XX                                         XX ", "FXXFFFF                                 FFFFXXF", "AXXAAAA                                 AAAAXXA"},
                {"                                               ", "                                               ", "                                               ", "F     F                                 F     F", "A     A                                 A     A"},
                {" XX                                         XX ", " XI                                         IX ", " XX                                         XX ", "FXXFFFF                                 FFFFXXF", "AXX   A                                 A   XXA"},
                {"                                               ", "                                               ", "                                               ", "F     F                                 F     F", "A     A                                 A     A"},
                {" XX                                         XX ", " XI                                         IX ", " XX                                         XX ", "FXXFFFF                                 FFFFXXF", "AXXAAAA                                 AAAAXXA"},
                {"                                               ", "                                               ", "                                               ", "FFFFFFF                                 FFFFFFF", "AAAAAAA                                 AAAAAAA"},
                {"                                               ", "                                               ", "                                               ", " FFFFFF                                 FFFFFF ", " AAAAAA                                 AAAAAA "},
                {"                                               ", "                                               ", "                                               ", "    FFF                                 FFF    ", " AAAAAA                                 AAAAAA "},
                {"                                               ", "                                               ", "                                               ", "                                               ", " AAAAAAA                               AAAAAAA "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "  AA  AA                               AA  AA  "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "  AA                                       AA  "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "  AA                                       AA  "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "   AA                                     AA   "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "   AA                                     AA   "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "    AA                                   AA    "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "    AA                                   AA    "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "     AA                                 AA     "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "     AAA                               AAA     "},
                {"                                               ", "                                               ", "                                               ", "                                               ", "      AAA       AA           AA       AAA      "},
                {"                                               ", "                                               ", "                                               ", "                  FFFFFFFFFFF                  ", "       AAA      AAAAAAAAAAAAAAA      AAA       "},
                {"                                               ", "                                               ", "                                               ", "                  FFFF F FFFF                  ", "        AAAA     AAAAA   AAAAA     AAAA        "},
                {"                                               ", "                                               ", "                                               ", "                  FFFF F FFFF                  ", "          AAAA   AAAAA   AAAAA   AAAA          "},
                {"                                               ", "                                               ", "                                               ", "                   FFF F FFF                   ", "            AAAAAAAAAA   AAAAAAAAAA            "},
                {"                     X X X                     ", "                     I I I                     ", "                     X X X                     ", "                   FFX X XFF                   ", "              AAAAAAAX X XAAAAAAA              "},
                {"                     X X X                     ", "                     X X X                     ", "                     X X X                     ", "                   FFX X XFF                   ", "                 AAAAX X XAAAA                 "},
                {"                                               ", "                                               ", "                                               ", "                    FFFFFFF                    ", "                    AAAAAAA                    "}
            })
            .addElement(
                'E', StructureUtility.ofBlock(IGBlocks.SpaceElevatorCasing, 1)) // Support Structure
            .addElement('B', ElevatorUtil.ofBlockAdder(TileEntitySpaceElevator::addCable, IGBlocks.SpaceElevatorCable, 0))
            .addElement(
                'X',
                classicHatches(CASING_INDEX_BASE, 1, IGBlocks.SpaceElevatorCasing, 0))
            .addElement('H', GT_StructureUtility.ofFrame(Materials.Neutronium)) // Neutronium frame boxes
            .addElement('F', StructureUtility.ofBlock(IGBlocks.SpaceElevatorCasing, 2)) // Internal Structure
            .addElement(
                'C',
                StructureUtility.ofBlocksTiered(
                    ElevatorUtil.motorTierConverter(),
                    ElevatorUtil.getMotorTiers(),
                    0,
                    TileEntitySpaceElevator::setMotorTier,
                    TileEntitySpaceElevator::getMotorTier)) // Motors
            .addElement('A', StructureUtility.ofBlock(GSBlocks.DysonSwarmBlocks, 9)) // Concrete
            .addElement('D', StructureUtility.ofBlock(IGBlocks.SpaceElevatorCasing, 0)) // Base Casing
            .addElement(
                'I',
                GT_HatchElementBuilder.<TileEntitySpaceElevator>builder()
                    .atLeast(ElevatorUtil.ProjectModuleElement.ProjectModule)
                    .casingIndex(CASING_INDEX_BASE)
                    .dot(2)
                    .buildAndChain(IGBlocks.SpaceElevatorCasing, 0)) // Base Casing or project module
            .build();
    // spotless:on

    // endregion

    /**
     * Create a new Space Elevator
     *
     * @param aID           ID of the controller
     * @param aName         Name of the controller
     * @param aNameRegional Localized name of the controller
     */
    public TileEntitySpaceElevator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        useLongPower = true;
    }

    /**
     * Create a new Space Elevator
     *
     * @param aName Name of the controller
     */
    protected TileEntitySpaceElevator(String aName) {
        super(aName);
        useLongPower = true;
    }

    /**
     * Get a new meta tile entity of this controller
     *
     * @param aTileEntity this
     * @return New meta tile entity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TileEntitySpaceElevator(this.mName);
    }

    /**
     * Load the controller data from NBT
     *
     * @param aNBT NBT data containing information
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        motorTier = aNBT.getInteger("motorTier");
        isExtensionEnabled = aNBT.getBoolean("isExtensionEnabled");
        super.loadNBTData(aNBT);
    }

    /**
     * Save the controller data to NBT
     *
     * @param aNBT NBT data to which will be saved
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("motorTier", motorTier);
        aNBT.setBoolean("isExtensionEnabled", isExtensionEnabled);
        super.saveNBTData(aNBT);
    }

    /**
     * Get the size of the internal energy buffer
     *
     * @return Size of the internal energy buffer
     */
    @Override
    public long maxEUStore() {
        return INTERNAL_BUFFER_MULTIPLIER * super.maxEUStore();
    }

    /**
     * Get the number of installed project modules
     *
     * @return Number of installed project modules
     */
    public int getNumberOfModules() {
        return mProjectModuleHatches != null ? mProjectModuleHatches.size() : 0;
    }

    /**
     * Get the chunk X coordinate in which the controller resides
     *
     * @return Chunk X coordinate
     */
    public int getChunkX() {
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    /**
     * Get the chunk Z coordinate in which the controller resides
     *
     * @return Chunk Z coordinate
     */
    public int getChunkZ() {
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }

    // region Structure

    /**
     * Get the structure of the Space Elevator
     *
     * @return Structure definition
     */
    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    /**
     * Get possible alignments of this controller
     *
     * @return List of alignments that are possible or denied
     */
    @Override
    public IAlignmentLimits getAlignmentLimits() {
        // The elevator should only be buildable upright
        return IAlignmentLimits.Builder.allowAll().deny(ForgeDirection.DOWN).deny(ForgeDirection.UP)
                .deny(Rotation.UPSIDE_DOWN).deny(Rotation.CLOCKWISE).deny(Rotation.COUNTER_CLOCKWISE).build();
    }

    /**
     * Construct the structure of Space Elevator
     *
     * @param stackSize Hologram projector item stack
     * @param hintsOnly Should only hints be displayed?
     */
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(
                STRUCTURE_PIECE_MAIN,
                STRUCTURE_PIECE_MAIN_HOR_OFFSET,
                STRUCTURE_PIECE_MAIN_VERT_OFFSET,
                STRUCTURE_PIECE_MAIN_DEPTH_OFFSET,
                stackSize,
                hintsOnly);
        if (isExtensionEnabled) {
            structureBuild_EM(
                    STRUCTURE_PIECE_EXTENDED,
                    STRUCTURE_PIECE_EXTENDED_HOR_OFFSET,
                    STRUCTURE_PIECE_EXTENDED_VERT_OFFSET,
                    STRUCTURE_PIECE_EXTENDED_DEPTH_OFFSET,
                    stackSize,
                    hintsOnly);
        }
    }

    /**
     * Construct the structure of the Space Elevator in survival
     *
     * @param stackSize     Hologram projector item stack
     * @param elementBudget Max at once placeable blocks
     * @param source        Source of the building material
     * @param actor         Player that is constructing
     * @return Number of placed blocks
     */
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        } else {
            int consumedBudget = survivialBuildPiece(
                    STRUCTURE_PIECE_MAIN,
                    stackSize,
                    STRUCTURE_PIECE_MAIN_HOR_OFFSET,
                    STRUCTURE_PIECE_MAIN_VERT_OFFSET,
                    STRUCTURE_PIECE_MAIN_DEPTH_OFFSET,
                    elementBudget,
                    source,
                    actor,
                    false,
                    true);
            if (isExtensionEnabled) {
                consumedBudget += survivialBuildPiece(
                        STRUCTURE_PIECE_EXTENDED,
                        stackSize,
                        STRUCTURE_PIECE_EXTENDED_HOR_OFFSET,
                        STRUCTURE_PIECE_EXTENDED_VERT_OFFSET,
                        STRUCTURE_PIECE_EXTENDED_DEPTH_OFFSET,
                        elementBudget,
                        source,
                        actor,
                        false,
                        true);
            }
            return consumedBudget;
        }
    }

    /**
     * Check if the structure of this machine is valid
     *
     * @param aBaseMetaTileEntity This
     * @param aStack              Item stack present in the controller GUI
     * @return True if valid, else false
     */
    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        boolean isMachineValid = true;
        mProjectModuleHatches.clear();
        elevatorCable = null;
        motorTier = 0;
        // Check structure
        if (!structureCheck_EM(
                STRUCTURE_PIECE_MAIN,
                STRUCTURE_PIECE_MAIN_HOR_OFFSET,
                STRUCTURE_PIECE_MAIN_VERT_OFFSET,
                STRUCTURE_PIECE_MAIN_DEPTH_OFFSET)) {
            if (elevatorCable != null) {
                elevatorCable.setShouldRender(false);
            }
            return false;
        }
        if (motorTier > 2 && isExtensionEnabled) {
            if (!structureCheck_EM(
                    STRUCTURE_PIECE_EXTENDED,
                    STRUCTURE_PIECE_EXTENDED_HOR_OFFSET,
                    STRUCTURE_PIECE_EXTENDED_VERT_OFFSET,
                    STRUCTURE_PIECE_EXTENDED_DEPTH_OFFSET)) {
                if (elevatorCable != null) {
                    elevatorCable.setShouldRender(false);
                }
                return false;
            }
        }
        // Check if the allowed module amount is exceeded. Motor tier 5 unlocks all module slots
        isMachineValid = ElevatorUtil.getModuleSlotsUnlocked(motorTier) >= mProjectModuleHatches.size();
        // Fix maintenance issues
        fixAllIssues();
        if (elevatorCable != null) {
            elevatorCable.setShouldRender(isMachineValid);
            return isMachineValid;
        }
        return isMachineValid;
    }

    /**
     * Add a project module to the module list
     *
     * @param aTileEntity      Project module
     * @param aBaseCasingIndex Index of the casing texture it should take
     * @return True if input entity is a valid module and could be added, else false
     */
    public boolean addProjectModuleToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof TileEntityModuleBase) {
            return mProjectModuleHatches.add((TileEntityModuleBase) aMetaTileEntity);
        }
        return false;
    }

    /**
     * Add the cable block the the elevator
     *
     * @param block            Cable block
     * @param aBaseCasingIndex Index of the casing texture it should take
     * @param world            World that the block is in
     * @param x                X coordinate of the block
     * @param y                Y coordinate of the block
     * @param z                Z coordinate of the block
     * @return True if block is valid, else false
     */
    public boolean addCable(Block block, int aBaseCasingIndex, World world, int x, int y, int z) {
        // Check if the cable block is valid and can see the sky
        if (block != IGBlocks.SpaceElevatorCable || world == null) {
            return false;
        }
        if (!world.canBlockSeeTheSky(x, y + 1, z)) {
            return false;
        }

        TileEntity te = world.getTileEntity(x, y, z);

        if (te instanceof TileEntitySpaceElevatorCable) {
            elevatorCable = (TileEntitySpaceElevatorCable) te;
            return true;
        }

        return false;
    }

    /**
     * Set the motor tier of the elevator
     *
     * @param tier Tier to be set
     */
    public void setMotorTier(int tier) {
        motorTier = tier;
    }

    /**
     * Get the current motor tier of the elevator
     *
     * @return Motor tier of the elevator
     */
    public int getMotorTier() {
        return motorTier;
    }

    /**
     * Callback that will be invoked when the controller is removed
     */
    @Override
    public void onRemoval() {
        if (elevatorCable != null) {
            elevatorCable.setShouldRender(false);
        }
        if (mProjectModuleHatches != null && mProjectModuleHatches.size() > 0) {
            for (TileEntityModuleBase projectModule : mProjectModuleHatches) {
                projectModule.disconnect();
            }
        }
        super.onRemoval();
    }

    /**
     * Callback that will be invoked on post tick
     *
     * @param aBaseMetaTileEntity This
     * @param aTick               Tick
     */
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick == 1) {
                SpaceProjectManager.checkOrCreateTeam(aBaseMetaTileEntity.getOwnerUuid());
            }
            if (!aBaseMetaTileEntity.isAllowedToWork()) {
                // if machine has stopped, stop chunkloading
                GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
                isLoadedChunk = false;
            } else if (!isLoadedChunk) {
                // load a 3x3 area when machine is running
                GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
                int offX = aBaseMetaTileEntity.getFrontFacing().offsetX;
                int offZ = aBaseMetaTileEntity.getFrontFacing().offsetZ;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        GT_ChunkManager.requestChunkLoad(
                                (TileEntity) aBaseMetaTileEntity,
                                new ChunkCoordIntPair(getChunkX() + offX + i, getChunkZ() + offZ + j));
                    }
                }
                this.isLoadedChunk = true;
            } else {
                if (elevatorCable != null && Config.isCableRenderingEnabled
                        && elevatorCable.getAnimation() == TileEntitySpaceElevatorCable.ClimberAnimation.NO_ANIMATION
                        && aTick % 2000 == 0) {
                    elevatorCable.startAnimation(TileEntitySpaceElevatorCable.ClimberAnimation.DELIVER_ANIMATION);
                }
            }

            // Charge project modules
            if (getBaseMetaTileEntity().isAllowedToWork()) {
                if (aTick % MODULE_CHARGE_INTERVAL == 0) {
                    if (mProjectModuleHatches.size() > 0) {
                        long tEnergy = getEUVar() / mProjectModuleHatches.size() * MODULE_CHARGE_INTERVAL;
                        for (TileEntityModuleBase projectModule : mProjectModuleHatches) {
                            if (projectModule.getNeededMotorTier() <= motorTier) {
                                long tAvailableEnergy = getEUVar();
                                if (tAvailableEnergy > 0) {
                                    setEUVar(
                                            Math.max(
                                                    0,
                                                    tAvailableEnergy - projectModule
                                                            .increaseStoredEU(Math.min(tEnergy, tAvailableEnergy))));
                                }
                            }
                        }
                    }
                }
            } else {
                if (mProjectModuleHatches.size() > 0) {
                    for (TileEntityModuleBase projectModule : mProjectModuleHatches) {
                        projectModule.disconnect();
                    }
                }
            }
            if (mEfficiency < 0) mEfficiency = 0;
            fixAllIssues();
        }
    }

    /**
     * Get the sound for when the machine is active
     *
     * @return Location of the sound
     */
    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return GT_MetaTileEntity_EM_infuser.activitySound;
    }

    /**
     * Fix all maintenance issues of this controller
     */
    protected void fixAllIssues() {
        mWrench = true;
        mScrewdriver = true;
        mSoftHammer = true;
        mHardHammer = true;
        mSolderingTool = true;
        mCrowbar = true;
    }

    // endregion

    // region Recipe

    /**
     * Keep the machine going if enabled, it wont execute any recipes
     *
     * @param aStack Item stack in the controller GUI (none)
     * @return True if enabled, else false
     */
    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        if (getBaseMetaTileEntity().isAllowedToWork()) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
            return true;
        }

        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return false;
    }

    // endregion

    // region Client

    /**
     * Create the tooltip of this controller
     *
     * @return Tooltip builder
     */
    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.name"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc0"))
                .addInfo(ITALIC + loreTooltip)
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc2"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc3"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc4"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc5"))
                .addInfo(GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.desc6"))
                .addInfo(GCCoreUtil.translate("ig.elevator.structure.TooComplex"))
                .addInfo(buildAddedBy(LIGHT_PURPLE + "minecraft7771"))
                .addInfo(GCCoreUtil.translate("ig.structure.moreContributors")).addSeparator()
                .beginStructureBlock(35, 43, 35, false)
                .addOtherStructurePart(
                        GCCoreUtil.translate("ig.elevator.structure.ProjectModule"),
                        GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith2Dot"),
                        2)
                .addCasingInfoExactly(GCCoreUtil.translate("tile.DysonSwarmFloor.name"), 800, false)
                .addCasingInfoRange(GCCoreUtil.translate("gt.blockcasings.ig.0.name"), 593, 785, false)
                .addCasingInfoExactly(GCCoreUtil.translate("gt.blockcasings.ig.1.name"), 620, false)
                .addCasingInfoExactly(GCCoreUtil.translate("gt.blockcasings.ig.2.name"), 360, false)
                .addCasingInfoExactly(GCCoreUtil.translate("gt.blockcasings.ig.cable.name"), 1, false)
                .addCasingInfoExactly(GCCoreUtil.translate("ig.elevator.structure.FrameNeutronium"), 56, false)
                .addCasingInfoExactly(GCCoreUtil.translate("ig.elevator.structure.Motor"), 88, true)
                .addEnergyHatch(GCCoreUtil.translate("ig.elevator.structure.AnyBaseCasingWith1Dot"), 1)
                .toolTipFinisher(DARK_PURPLE + Tags.MODNAME);
        return tt;
    }

    /**
     * Get the texture of this controller
     *
     * @param aBaseMetaTileEntity This
     * @param side                Side for which the texture will be gotten
     * @param facing              Facing side of the controller
     * @param colorIndex          Color index
     * @param aActive             Flag if the controller is active
     * @param aRedstone           Flag if Redstone is present
     * @return Texture array of this controller
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex,
            boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_BASE),
                    new TT_RenderedExtendedFacingTexture(
                            aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                                    : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_BASE) };
    }
    // endregion

    // region Misc

    /**
     * Get the information data of this controller
     *
     * @return String, that contains the provided information data
     */
    @Override
    public String[] getInfoData() {
        return new String[] { LIGHT_PURPLE + "Operational Data:" + RESET,
                "Maintenance Status: " + (getRepairStatus() == getIdealStatus() ? GREEN + "Working perfectly" + RESET
                        : RED + "Has problems" + RESET),
                "---------------------------------------------" };
    }

    /**
     * @return Whether to bind the inventory in the GUI or not
     */
    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    /**
     * Add the logo to this GUI
     *
     * @param builder Used GUI builder
     */
    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
                new DrawableWidget().setDrawable(IG_UITextures.PICTURE_ELEVATOR_LOGO_DARK).setSize(18, 18)
                        .setPos(150, 154));
    }

    /**
     * Draw the GUI texts
     *
     * @param screenElements Elements that contain the text
     * @param inventorySlot  Controller slot
     */
    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.setSynced(false).setSpace(0).setPos(10, 7);

        screenElements
                .widget(
                        new TextWidget(GT_Utility.trans("138", "Incomplete Structure."))
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> !mMachine))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val));

        screenElements.widget(
                new TextWidget(StatCollector.translateToLocal("gt.blockmachines.multimachine.ig.elevator.gui.ready"))
                        .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine));

        screenElements.widget(
                TextWidget
                        .dynamicString(
                                () -> StatCollector.translateToLocal(
                                        "gt.blockmachines.multimachine.ig.elevator.gui.numOfModules") + ": "
                                        + getNumberOfModules())
                        .setDefaultColor(COLOR_TEXT_WHITE.get())
                        .setEnabled(widget -> getBaseMetaTileEntity().getErrorDisplayID() == 0));
    }

    /**
     * Add UI widgets to this GUI
     *
     * @param builder      Used UI builder
     * @param buildContext Build context
     */
    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);

        // Extension button
        builder.widget(
                new CycleButtonWidget().setToggle(() -> isExtensionEnabled, val -> isExtensionEnabled = val)
                        .setPlayClickSound(true)
                        .setVariableBackgroundGetter(
                                (state) -> new UITexture[] {
                                        state > 0 ? IG_UITextures.OVERLAY_BUTTON_SPACE_ELEVATOR_EXTENSION_ENABLED
                                                : IG_UITextures.OVERLAY_BUTTON_SPACE_ELEVATOR_EXTENSION_DISABLED })
                        .setPos(115, 155).setSize(16, 16)
                        .addTooltip(StatCollector.translateToLocal("ig.button.extension"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY));

        // Teleportation button
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.getContext().isClient()) {
                if (getBaseMetaTileEntity().isAllowedToWork() && motorTier > 0) {
                    EntityPlayer player = widget.getContext().getPlayer();
                    if (player instanceof EntityPlayerMP) {
                        EntityPlayerMP playerBase = (EntityPlayerMP) player;
                        final GCPlayerStats stats = GCPlayerStats.get(playerBase);
                        stats.coordsTeleportedFromX = playerBase.posX;
                        stats.coordsTeleportedFromZ = playerBase.posZ;
                        try {
                            WorldUtil.toCelestialSelection(
                                    playerBase,
                                    stats,
                                    ElevatorUtil.getPlanetaryTravelTier(motorTier),
                                    GuiCelestialSelection.MapMode.TELEPORTATION);
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).setPlayClickSound(false).setBackground(() -> {
            List<UITexture> ret = new ArrayList<>();
            ret.add(TecTechUITextures.BUTTON_STANDARD_16x16);
            ret.add(IG_UITextures.OVERLAY_BUTTON_PLANET_TELEPORT);
            return ret.toArray(new IDrawable[0]);
        }).setPos(174, doesBindPlayerInventory() ? 132 : 156).setSize(16, 16)
                .addTooltip(GCCoreUtil.translate("ig.button.travel")).setTooltipShowUpDelay(TOOLTIP_DELAY));

        // Open contributor window button
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.getContext().isClient()) {
                widget.getContext().openSyncedWindow(CONTRIBUTORS_WINDOW_ID);
            }
        }).addTooltip(StatCollector.translateToLocal("ig.structure.contributors"))
                .setBackground(ModularUITextures.ICON_INFO).setPos(133, 155).setSize(16, 16));

        // Contributor window
        buildContext.addSyncedWindow(CONTRIBUTORS_WINDOW_ID, (player) -> {
            DynamicPositionedColumn texts = new DynamicPositionedColumn();
            texts.setSynced(false).setSpace(2).setPos(10, 7);

            texts.widget(
                    new TextWidget(StatCollector.translateToLocal("ig.structure.contributors"))
                            .setDefaultColor(Color.PURPLE.normal));
            texts.widget(
                    new TextWidget(StatCollector.translateToLocal("ig.structure.programming"))
                            .setDefaultColor(Color.PINK.normal));
            texts.widget(new TextWidget("minecraft7771").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(new TextWidget("BlueWeabo").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(
                    new TextWidget(StatCollector.translateToLocal("ig.structure.design"))
                            .setDefaultColor(Color.PINK.normal));
            texts.widget(new TextWidget("Sampsa").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(new TextWidget("Jimbno").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(new TextWidget("Adam").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(new TextWidget("Baunti").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(
                    new TextWidget(StatCollector.translateToLocal("ig.structure.specialThanks"))
                            .setDefaultColor(Color.PINK.normal));
            texts.widget(new TextWidget("glowredman").setDefaultColor(Color.GRAY.bright(1)));
            texts.widget(new TextWidget("miozune").setDefaultColor(Color.GRAY.bright(1)));

            return ModularWindow.builder(120, 130).setBackground(TecTechUITextures.BACKGROUND_SCREEN_BLUE)
                    .widget(ButtonWidget.closeWindowButton(true).setPos(100, 7)).widget(texts).build();
        });
    }

    /**
     * @return Safe void button of this GUI
     */
    @Override
    protected ButtonWidget createSafeVoidButton() {
        return null;
    }

    /**
     * Will this machine explode in rain?
     *
     * @return False
     */
    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    // endregion
}
