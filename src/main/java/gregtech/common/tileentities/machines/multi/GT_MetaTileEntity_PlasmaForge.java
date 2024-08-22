package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_RAINBOWSCREEN_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.thing.gui.TecTechUITextures;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.SoundResource;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_PlasmaForge extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PlasmaForge> implements ISurvivalConstructable {

    // 3600 seconds in an hour, 8 hours, 20 ticks in a second.
    private static final double max_efficiency_time_in_ticks = 3600d * 8d * 20d;
    // Multiplier for the efficiency decay rate
    private static final double efficiency_decay_rate = 100;
    private static final double maximum_discount = 0.5d;
    private static final int CONVERGENCE_BITMAP = 0b1;
    private static final int DISCOUNT_BITMAP = 0b10;

    // Valid fuels which the discount will get applied to.
    private static final FluidStack[] valid_fuels = { MaterialsUEVplus.ExcitedDTCC.getFluid(1L),
        MaterialsUEVplus.ExcitedDTPC.getFluid(1L), MaterialsUEVplus.ExcitedDTRC.getFluid(1L),
        MaterialsUEVplus.ExcitedDTEC.getFluid(1L), MaterialsUEVplus.ExcitedDTSC.getFluid(1L) };

    private static final HashMap<Fluid, Pair<Long, Float>> FUEL_ENERGY_VALUES = new HashMap<>() {

        {
            put(
                MaterialsUEVplus.ExcitedDTCC.getFluid(1L)
                    .getFluid(),
                Pair.of(14_514_983L, 1 / 8f));
            put(
                MaterialsUEVplus.ExcitedDTPC.getFluid(1L)
                    .getFluid(),
                Pair.of(66_768_460L, 1 / 4f));
            put(
                MaterialsUEVplus.ExcitedDTRC.getFluid(1L)
                    .getFluid(),
                Pair.of(269_326_451L, 1 / 2f));
            put(
                MaterialsUEVplus.ExcitedDTEC.getFluid(1L)
                    .getFluid(),
                Pair.of(1_073_007_393L, 1f));
            put(
                MaterialsUEVplus.ExcitedDTSC.getFluid(1L)
                    .getFluid(),
                Pair.of(4_276_767_521L, 2f));
        }
    };

    private static final int min_input_hatch = 0;
    private static final int max_input_hatch = 7;
    private static final int min_output_hatch = 0;
    private static final int max_output_hatch = 2;
    private static final int min_input_bus = 0;
    private static final int max_input_bus = 6;
    private static final int min_output_bus = 0;
    private static final int max_output_bus = 1;

    // Current discount rate. 1 = 0%, 0 = 100%.
    private double discount = 1;
    private int mHeatingCapacity = 0;
    private long running_time = 0;
    private boolean convergence = false;
    private HeatingCoilLevel mCoilLevel;
    private GT_OverclockCalculator overclockCalculator;

    @SuppressWarnings("SpellCheckingInspection")
    private static final String[][] structure_string = new String[][] { { "                                 ",
        "         N   N     N   N         ", "         N   N     N   N         ", "         N   N     N   N         ",
        "                                 ", "                                 ", "                                 ",
        "         N   N     N   N         ", "         N   N     N   N         ", " NNN   NNN   N     N   NNN   NNN ",
        "                                 ", "                                 ", "                                 ",
        " NNN   NNN             NNN   NNN ", "                                 ", "                                 ",
        "                                 ", "                                 ", "                                 ",
        " NNN   NNN             NNN   NNN ", "                                 ", "                                 ",
        "                                 ", " NNN   NNN             NNN   NNN " },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    N N    NbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "      NNNbbbbbNNsNNbbbbbNNN      ",
            "    ss   bCCCb     bCCCb   ss    ", "   s     N   N     N   N     s   ",
            "   s                         s   ", "  N      N   N     N   N      N  ",
            "  N      bCCCb     bCCCb      N  ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ",
            "         bCCCb     bCCCb         ", "  s      NCCCN     NCCCN      s  ",
            "  s      NCCCN     NCCCN      s  ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "                                 ", "         N   N     N   N         ", "   s     N   N     N   N     s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "                                 ", "                                 ", "   s                         s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N                 N   N   ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N       NbN       N   N   ", },
        { "                                 ", "         N   N     N   N         ", "  N      N   N     N   N      N  ",
            "         NCCCN     NCCCN         ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ",
            "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ",
            "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "  s     sbbbbbNNsNNbbbbbs     s  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { " NNN   NNN   N     N   NNN   NNN ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NNNN   NNNCCCb     bCCCNNN   NNNN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    NbN    NbbbN NbbbN", },
        { "                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCC   CCC   N     N   CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N     NsNsN     N     N  ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N    NbbbbbN    N     N  ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                N                ",
            " NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ", },
        { "                                 ", "                                 ", "  s     s               s     s  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  s     s               s     s  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                ~                ", "               NNN               ",
            "  NbbbbbNbbbbNbbbbbNbbbbNbbbbbN  ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                N                ",
            " NsNNNNNsNNNNsbbbbbsNNNNsNNNNNsN ", },
        { "                                 ", "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "  N     N               N     N  ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N    NbbbbbN    N     N  ", },
        { " NNN   NNN             NNN   NNN ", "NbbbN NbbbN           NbbbN NbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbNNNbbbN           NbbbNNNbbbN", " NNN   NNN             NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN             NNN   NNN ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "NbbbN NbbbN           NbbbN NbbbN",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N     NsNsN     N     N  ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "  N     N      NbN      N     N  ", },
        { "                                 ", " CCC   CCC   N     N   CCC   CCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " CCCCCCCCC   N     N   CCCCCCCCC ", " CbC   CbC   N     N   CbC   CbC ",
            " CCC   CCC   N     N   CCC   CCC ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { " NNN   NNN   N     N   NNN   NNN ", "NbbbN NbbNCCCb     bCCCNbbN NbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " NNN   NNN   N     N   NNN   NNN ",
            "   N   N                 N   N   ", " NNN   NNN   N     N   NNN   NNN ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", "NbbbN NbbNCCCb     bCCCNbbN NbbbN",
            "NNNN   NNNCCCb     bCCCNNN   NNNN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    NbN    NbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "  s     sbbbbbNNsNNbbbbbs     s  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "  N      bCCCb     bCCCb      N  ",
            "         bCCCb     bCCCb         ", "         NCCCN     NCCCN         ",
            "         NCCCN     NCCCN         ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "                                 ", "         N   N     N   N         ", "  N      N   N     N   N      N  ",
            "         NCCCN     NCCCN         ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "                                 ", "                                 ", "   s                         s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N                 N   N   ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            "   N   N                 N   N   ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            "   N   N       NbN       N   N   ", },
        { "                                 ", "         N   N     N   N         ", "   s     N   N     N   N     s   ",
            "  s      NCCCN     NCCCN      s  ", "                                 ",
            "                                 ", "                                 ",
            "         NCCCN     NCCCN         ", "         N   N     N   N         ",
            " NNN   NN    N     N    NN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "   C   C                 C   C   ",
            "   C   C                 C   C   ", "   C   C                 C   C   ",
            " NNN   NNN     NbN     NNN   NNN ", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ",
            "         bCCCb     bCCCb         ", "  s      NCCCN     NCCCN      s  ",
            "  s      NCCCN     NCCCN      s  ", "         NCCCN     NCCCN         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbNNNbbNCCCb     bCCCNbbNNNbbbN", " CCCCCCCCC   N     N   CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN           NbbbNNNbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbNNNbbbN           NbbbNNNbbbN", " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ",
            "NbbbNNNbbbN    NbN    NbbbNNNbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "      NNNbbbbbNNsNNbbbbbNNN      ",
            "    ss   bCCCb     bCCCb   ss    ", "   s     N   N     N   N     s   ",
            "   s                         s   ", "  N      N   N     N   N      N  ",
            "  N      bCCCb     bCCCb      N  ", "  N     sbbbbbNNsNNbbbbbs     N  ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CbC   CbC   N     N   CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbN           NbbbN NbbbN", " NNN   NNN             NNN   NNN ",
            " NNN   NNN             NNN   NNN ", "  s     s               s     s  ",
            " NNN   NNN             NNN   NNN ", " NNN   NNN             NNN   NNN ",
            "NbbbN NbbbN           NbbbN NbbbN", " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ",
            "NbbbN NbbbNNNNNsNsNNNNNbbbN NbbbN", },
        { "         N   N     N   N         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ", "         N   N     N   N         ",
            "                                 ", "         N   N     N   N         ",
            "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ",
            "NbbbN NbbNCCCb     bCCCNbbN NbbbN", " CCC   CCC   N     N   CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN           NbbbN NbbbN", "  N     N               N     N  ",
            "  N     N               N     N  ", "                                 ",
            "  N     N               N     N  ", "  N     N               N     N  ",
            "NbbbN NbbbN           NbbbN NbbbN", " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ",
            "NbbbN NbbbN    N N    NbbbN NbbbN", },
        { "                                 ", "         N   N     N   N         ", "         N   N     N   N         ",
            "         N   N     N   N         ", "                                 ",
            "                                 ", "                                 ",
            "         N   N     N   N         ", "         N   N     N   N         ",
            " NNN   NNN   N     N   NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", "                                 ",
            "                                 ", "                                 ",
            " NNN   NNN             NNN   NNN ", } };

    protected static final int DIM_TRANS_CASING = 12;
    protected static final int DIM_INJECTION_CASING = 13;
    protected static final int DIM_BRIDGE_CASING = 14;

    private boolean isMultiChunkloaded = true;

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_PlasmaForge> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PlasmaForge>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure_string)
        .addElement(
            'C',
            ofCoil(GT_MetaTileEntity_PlasmaForge::setCoilLevel, GT_MetaTileEntity_PlasmaForge::getCoilLevel))
        .addElement(
            'b',
            buildHatchAdder(GT_MetaTileEntity_PlasmaForge.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy, ExoticEnergy, Maintenance)
                .casingIndex(DIM_INJECTION_CASING)
                .dot(3)
                .buildAndChain(GregTech_API.sBlockCasings1, DIM_INJECTION_CASING))
        .addElement('N', ofBlock(GregTech_API.sBlockCasings1, DIM_TRANS_CASING))
        .addElement('s', ofBlock(GregTech_API.sBlockCasings1, DIM_BRIDGE_CASING))
        .build();

    public GT_MetaTileEntity_PlasmaForge(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PlasmaForge(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PlasmaForge(mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Plasma Forge, DTPF")
            .addInfo("Transcending Dimensional Boundaries.")
            .addInfo(
                "Takes " + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(max_efficiency_time_in_ticks / (3600 * 20))
                    + EnumChatFormatting.GRAY
                    + " hours of continuous run time to fully breach dimensional")
            .addInfo(
                "boundaries and achieve maximum efficiency, reducing fuel consumption by up to "
                    + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(100 * maximum_discount)
                    + "%"
                    + EnumChatFormatting.GRAY
                    + ".")
            .addInfo("Multidimensional spaces can be perfectly aligned and synchronized in this state, ")
            .addInfo(
                "allowing " + EnumChatFormatting.GOLD
                    + "Dimensional Convergence "
                    + EnumChatFormatting.GRAY
                    + "to occur. To reach the required stability threshold,")
            .addInfo(
                "a " + EnumChatFormatting.AQUA
                    + "Transdimensional Alignment Matrix "
                    + EnumChatFormatting.GRAY
                    + "must be placed in the controller.")
            .addInfo(
                "When " + EnumChatFormatting.GOLD
                    + "Convergence "
                    + EnumChatFormatting.GRAY
                    + "is active, it allows the forge to perform "
                    + EnumChatFormatting.RED
                    + "Perfect Overclocks"
                    + EnumChatFormatting.GRAY
                    + ",")
            .addInfo("but the extra power cost is instead added in form of increased catalyst amounts.")
            .addInfo(
                "When no recipe is running, fuel discount decays x" + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(efficiency_decay_rate)
                    + EnumChatFormatting.GRAY
                    + " as fast as it builds up.")
            .addInfo(AuthorColen)
            .addSeparator()
            .beginStructureBlock(33, 24, 33, false)
            .addStructureInfo("DTPF Structure is too complex! See schematic for details.")
            .addStructureInfo(EnumChatFormatting.GOLD + "2,112" + EnumChatFormatting.GRAY + " Heating coils required.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "120" + EnumChatFormatting.GRAY + " Dimensional bridge blocks required.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "1,270"
                    + EnumChatFormatting.GRAY
                    + " Dimensional injection casings required.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "2,121"
                    + EnumChatFormatting.GRAY
                    + " Dimensionally transcendent casings required.")
            .addStructureInfo("--------------------------------------------")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " energy hatches or "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " TT energy hatch.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + min_input_hatch
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + max_input_hatch
                    + EnumChatFormatting.GRAY
                    + " input hatches.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + min_output_hatch
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + max_output_hatch
                    + EnumChatFormatting.GRAY
                    + " output hatches.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + min_input_bus
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + max_input_bus
                    + EnumChatFormatting.GRAY
                    + " input busses.")
            .addStructureInfo(
                "Requires " + EnumChatFormatting.GOLD
                    + min_output_bus
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + max_input_bus
                    + EnumChatFormatting.GRAY
                    + " output busses.")
            .addStructureInfo("--------------------------------------------")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        IIconContainer glow = OVERLAY_FUSION1_GLOW;
        if (convergence && discount == maximum_discount) {
            glow = OVERLAY_RAINBOWSCREEN_GLOW;
        }
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][DIM_BRIDGE_CASING], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_ON)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(glow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][DIM_BRIDGE_CASING], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }
        return new ITexture[] { casingTexturePages[0][DIM_BRIDGE_CASING] };
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
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
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.plasmaForgeRecipes;
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
    @NotNull
    public CheckRecipeResult checkProcessing() {
        CheckRecipeResult recipe_process = super.checkProcessing();
        if (recipe_process.wasSuccessful()) {
            running_time = running_time + mMaxProgresstime;
        }
        return recipe_process;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                overclockCalculator = super.createOverclockCalculator(recipe).setRecipeHeat(recipe.mSpecialValue)
                    .setMachineHeat(mHeatingCapacity);
                if (discount == maximum_discount && convergence) {
                    overclockCalculator = overclockCalculator.enablePerfectOC();
                }
                return overclockCalculator;
            }

            @NotNull
            @Override
            protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
                return super.createParallelHelper(recipeAfterAdjustments(recipe));
            }

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                return recipe.mSpecialValue <= mHeatingCapacity ? CheckRecipeResultRegistry.SUCCESSFUL
                    : CheckRecipeResultRegistry.insufficientHeat(recipe.mSpecialValue);
            }
        };
    }

    @Nonnull
    protected GT_Recipe recipeAfterAdjustments(@Nonnull GT_Recipe recipe) {
        GT_Recipe tRecipe = recipe.copy();
        boolean adjusted = false;
        outside: for (int i = 0; i < recipe.mFluidInputs.length; i++) {
            for (FluidStack fuel : valid_fuels) {
                if (tRecipe.mFluidInputs[i].isFluidEqual(fuel)) {
                    recalculateDiscount();
                    if (discount == maximum_discount && convergence
                        && overclockCalculator != null
                        && overclockCalculator.getCalculationStatus()) {
                        calculateCatalystIncrease(tRecipe, i, false);
                        getBaseMetaTileEntity()
                            .sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
                    }
                    tRecipe.mFluidInputs[i].amount = (int) Math.round(tRecipe.mFluidInputs[i].amount * discount);
                    adjusted = true;
                    break outside;
                }
            }
        }
        // Convergence adjusts the recipe even if it has no catalyst input
        if (!adjusted && discount == maximum_discount
            && convergence
            && overclockCalculator != null
            && overclockCalculator.getCalculationStatus()) {
            recalculateDiscount();
            calculateCatalystIncrease(tRecipe, 0, true);
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        }
        return tRecipe;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        // Reset heating capacity.
        mHeatingCapacity = 0;

        // Get heating capacity from coils in structure.
        setCoilLevel(HeatingCoilLevel.None);

        // Check the main structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 16, 21, 16)) return false;

        if (getCoilLevel() == HeatingCoilLevel.None) return false;

        // Item input bus check.
        if (mInputBusses.size() > max_input_bus) return false;

        // Item output bus check.
        if (mOutputBusses.size() > max_output_bus) return false;

        // Fluid input hatch check.
        if (mInputHatches.size() > max_input_hatch) return false;

        // Fluid output hatch check.
        if (mOutputHatches.size() > max_output_hatch) return false;

        // If there is more than 1 TT energy hatch, the structure check will fail.
        // If there is a TT hatch and a normal hatch, the structure check will fail.
        if (mExoticEnergyHatches.size() > 0) {
            if (mEnergyHatches.size() > 0) return false;
            if (mExoticEnergyHatches.size() > 1) return false;
        }

        // If there is 0 or more than 2 energy hatches structure check will fail.
        if (mEnergyHatches.size() > 0) {
            if (mEnergyHatches.size() > 2) return false;

            // Check will also fail if energy hatches are not of the same tier.
            byte tier_of_hatch = mEnergyHatches.get(0).mTier;
            for (GT_MetaTileEntity_Hatch_Energy energyHatch : mEnergyHatches) {
                if (energyHatch.mTier != tier_of_hatch) {
                    return false;
                }
            }
        }

        // If there are no energy hatches or TT energy hatches, structure will fail to form.
        if ((mEnergyHatches.size() == 0) && (mExoticEnergyHatches.size() == 0)) return false;

        // Maintenance hatch not required but left for compatibility.
        // Don't allow more than 1, no free casing spam!
        if (mMaintenanceHatches.size() > 1) return false;

        // Heat capacity of coils used on multi. No free heat from extra EU!
        mHeatingCapacity = (int) getCoilLevel().getHeat();

        // All structure checks passed, return true.
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mExoticEnergyHatches.clear();
    }

    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack tLiquid = aLiquid.copy();

        return dumpFluid(mOutputHatches, tLiquid, true) || dumpFluid(mOutputHatches, tLiquid, false);
    }

    @Override
    public String[] getInfoData() {

        long storedEnergy = 0;
        long maxEnergy = 0;

        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mExoticEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        long voltage = getAverageInputVoltage();
        long amps = getMaxInputAmps();

        // Calculate discount to make sure it is shown properly even when machine is off but decaying
        recalculateDiscount();

        return new String[] { "------------ Critical Information ------------",
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(voltage)
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + EnumChatFormatting.YELLOW
                + amps
                + EnumChatFormatting.RESET
                + "A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(voltage)]
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.EBF.heat") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mHeatingCapacity)
                + EnumChatFormatting.RESET
                + " K",
            "Ticks run: " + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(running_time)
                + EnumChatFormatting.RESET
                + ", Fuel Discount: "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(100 * (1 - discount))
                + EnumChatFormatting.RESET
                + "%",
            "Convergence: " + (convergence ? EnumChatFormatting.GREEN + "Active" : EnumChatFormatting.RED + "Inactive"),
            "-----------------------------------------" };
    }

    private void recalculateDiscount() {
        double time_percentage = running_time / max_efficiency_time_in_ticks;
        time_percentage = Math.min(time_percentage, 1.0d);
        // Multiplied by 0.5 because that is the maximum achievable discount
        discount = 1 - time_percentage * 0.5;
        discount = Math.max(maximum_discount, discount);
    }

    private int catalystTypeForRecipesWithoutCatalyst = 1;

    private void calculateCatalystIncrease(GT_Recipe recipe, int index, boolean withoutCatalyst) {
        long machineConsumption = overclockCalculator.getConsumption();
        int numberOfOverclocks = (int) Math.ceil(calculateTier(machineConsumption) - GT_Utility.getTier(recipe.mEUt));
        double recipeDuration = recipe.mDuration / Math.pow(4, numberOfOverclocks);
        // Power difference between regular and perfect OCs for this recipe duration
        long extraPowerNeeded = (long) ((Math.pow(2, numberOfOverclocks) - 1) * machineConsumption * recipeDuration);
        int inputFluids = recipe.mFluidInputs.length;
        int outputFluids = recipe.mFluidOutputs.length;
        int extraCatalystNeeded;
        Fluid validFuel;
        if (!withoutCatalyst) {
            validFuel = recipe.mFluidInputs[index].getFluid();
            extraCatalystNeeded = (int) (extraPowerNeeded / FUEL_ENERGY_VALUES.get(validFuel)
                .getLeft());
            recipe.mFluidInputs[index].amount += extraCatalystNeeded;
            // Increase present catalyst and residue by calculated amount
            for (int j = 0; j < outputFluids; j++) {
                if (recipe.mFluidOutputs[j]
                    .isFluidEqual(MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1))) {
                    recipe.mFluidOutputs[j].amount += (int) (extraCatalystNeeded * FUEL_ENERGY_VALUES.get(validFuel)
                        .getRight());
                }
            }
        } else {
            // Add chosen catalyst as recipe input
            validFuel = valid_fuels[catalystTypeForRecipesWithoutCatalyst - 1].getFluid();
            extraCatalystNeeded = (int) (extraPowerNeeded / FUEL_ENERGY_VALUES.get(validFuel)
                .getLeft());
            FluidStack[] newInputFluids = new FluidStack[inputFluids + 1];
            for (int i = 0; i < inputFluids; i++) {
                newInputFluids[i] = recipe.mFluidInputs[i].copy();
            }
            newInputFluids[inputFluids] = new FluidStack(validFuel, extraCatalystNeeded / 2);
            recipe.mFluidInputs = newInputFluids;
            // Add residue as recipe output
            FluidStack[] newOutputFluids = new FluidStack[outputFluids + 1];
            for (int i = 0; i < outputFluids; i++) {
                newOutputFluids[i] = recipe.mFluidOutputs[i].copy();
            }
            newOutputFluids[outputFluids] = new FluidStack(
                MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(1),
                (int) (extraCatalystNeeded * FUEL_ENERGY_VALUES.get(validFuel)
                    .getRight()));
            recipe.mFluidOutputs = newOutputFluids;
        }
    }

    private static final double LN2 = Math.log(2);

    private double calculateTier(long voltage) {
        return (1 + Math.max(0, (Math.log(voltage) / LN2) - 5) / 2);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // If machine has stopped, stop chunkloading.
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);
            isMultiChunkloaded = false;
        } else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !isMultiChunkloaded) {
            // Load a 3x3 area centered on controller when machine is running.
            GT_ChunkManager.releaseTicket((TileEntity) aBaseMetaTileEntity);

            int ControllerXCoordinate = ((TileEntity) aBaseMetaTileEntity).xCoord;
            int ControllerZCoordinate = ((TileEntity) aBaseMetaTileEntity).zCoord;

            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate + 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate, ControllerZCoordinate - 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate + 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate + 16, ControllerZCoordinate - 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate + 16));
            GT_ChunkManager.requestChunkLoad(
                (TileEntity) aBaseMetaTileEntity,
                new ChunkCoordIntPair(ControllerXCoordinate - 16, ControllerZCoordinate - 16));

            isMultiChunkloaded = true;
        }

        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            if (mMaxProgresstime == 0) {
                running_time = Math.max(0, running_time - (long) efficiency_decay_rate);
            }
            if (aTick % 100 == 0) {
                ItemStack controllerStack = this.getControllerSlot();
                if (convergence && (controllerStack == null
                    || !controllerStack.isItemEqual(ItemList.Transdimensional_Alignment_Matrix.get(1)))) {
                    convergence = false;
                    getBaseMetaTileEntity()
                        .sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
                }
            }
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 16, 21, 16);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 16, 21, 16, realBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected ResourceLocation getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_PLASMAFORGE_LOOP.resourceLocation;
    }

    @Override
    public byte getUpdateData() {
        byte data = 0;
        if (discount == maximum_discount) {
            data += DISCOUNT_BITMAP;
        }
        if (convergence) {
            data += CONVERGENCE_BITMAP;
        }
        return data;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA) {
            convergence = (aValue & CONVERGENCE_BITMAP) == CONVERGENCE_BITMAP;
            if ((aValue & DISCOUNT_BITMAP) == DISCOUNT_BITMAP) {
                discount = maximum_discount;
            }
        }
    }

    private static final int CATALYST_WINDOW_ID = 10;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(CATALYST_WINDOW_ID, this::createCatalystWindow);
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            ItemStack controllerStack = this.getControllerSlot();
            if (clickData.mouseButton == 0) {
                if (controllerStack != null
                    && controllerStack.isItemEqual(ItemList.Transdimensional_Alignment_Matrix.get(1))) {
                    convergence = !convergence;
                }
            } else if (clickData.mouseButton == 1 && !widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(CATALYST_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (convergence) {
                    ret.add(GT_UITextures.BUTTON_STANDARD_PRESSED);
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_ON);
                } else {
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(translateToLocal("GT5U.DTPF.convergencebutton"))
            .addTooltip(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.convergencebuttontooltip.0"))
            .addTooltip(EnumChatFormatting.GRAY + translateToLocal("GT5U.DTPF.convergencebuttontooltip.1"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 129)
            .setSize(16, 16)
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(() -> convergence, (val) -> convergence = val), builder));
        super.addUIWidgets(builder, buildContext);
    }

    protected ModularWindow createCatalystWindow(final EntityPlayer player) {
        final int WIDTH = 58;
        final int HEIGHT = 52;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.BottomRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)
                        .subtract(0, 10)));
        builder.widget(
            TextWidget.localised("GT5U.DTPF.catalysttier")
                .setPos(3, 4)
                .setSize(50, 20))
            .widget(
                new NumericWidget().setSetter(val -> catalystTypeForRecipesWithoutCatalyst = (int) val)
                    .setGetter(() -> catalystTypeForRecipesWithoutCatalyst)
                    .setBounds(1, 5)
                    .setDefaultValue(1)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(50, 18)
                    .setPos(4, 25)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                    .addTooltip(translateToLocal("GT5U.DTPF.catalystinfotooltip"))
                    .attachSyncer(
                        new FakeSyncWidget.IntegerSyncer(
                            () -> catalystTypeForRecipesWithoutCatalyst,
                            (val) -> catalystTypeForRecipesWithoutCatalyst = val),
                        builder));
        return builder.build();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("eRunningTime", running_time);
        aNBT.setDouble("eLongDiscountValue", discount);
        aNBT.setInteger("catalystType", catalystTypeForRecipesWithoutCatalyst);
        aNBT.setBoolean("convergence", convergence);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        running_time = aNBT.getLong("eRunningTime");
        discount = aNBT.getDouble("eLongDiscountValue");
        catalystTypeForRecipesWithoutCatalyst = aNBT.getInteger("catalystType");
        convergence = aNBT.getBoolean("convergence");
        super.loadNBTData(aNBT);
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
