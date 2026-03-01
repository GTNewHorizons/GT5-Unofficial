package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.loader.Loaders.supercriticalFluidTurbineCasing;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.metadata.CentrifugeRecipeKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.gui.modularui.multiblock.MTESpinmatronGui;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;

public class MTESpinmatron extends MTEExtendedPowerMultiBlockBase<MTESpinmatron> implements ISurvivalConstructable {

    public boolean tier2Fluid = false;
    public double mode = 1.0; // i think it has to be a double cuz slider. 0 = speed, 1 = normal, 2 = heavy
    public int RP = 0;
    public float speed = 3F;
    public float euMultiplier = 1;
    private final int horizontalOffset = 8; // base offset for tier 1
    private final int verticalOffset = 8; // base offset for tier 2
    private final int depthOffset = 2;
    private int amountToDrain = 1; // drain amount.
    private int tier;
    private int lastCheckedTierIndex = 0;
    private List<StructureData> tierCheckOrderList = Arrays.asList(StructureData.values());
    public final LimitingItemStackHandler turbineHolder = new LimitingItemStackHandler(8, 1);
    private static final String STRUCTURE_TIER_1 = "t1";
    private static final String STRUCTURE_TIER_2 = "t2";
    private static final String STRUCTURE_TIER_3 = "t3";
    private static final String STRUCTURE_TIER_4 = "t4";
    private static final IIconContainer TEXTURE_CONTROLLER = new Textures.BlockIcons.CustomIcon("iconsets/TFFT");
    private static final IIconContainer TEXTURE_CONTROLLER_ACTIVE = new Textures.BlockIcons.CustomIcon(
        "iconsets/TFFT_ACTIVE");
    private static final IIconContainer TEXTURE_CONTROLLER_ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
        "iconsets/TFFT_ACTIVE_GLOW");
    public ArrayList<MTEHatchTurbine> turbineRotorHatchList = new ArrayList<>();
    private static final String anyCasing = GTUtility.nestParams(
        "GT5U.MBTT.HatchInfo",
        ItemList.Spinmatron_Casing.get(1)
            .getDisplayName());

    private boolean staticAnimations = false;
    // spotless:off

    private static final IStructureDefinition<MTESpinmatron> STRUCTURE_DEFINITION = StructureDefinition.<MTESpinmatron>builder()
        .addShape(
            STRUCTURE_TIER_1,
            transpose(
                new String[][] {
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   aaa   A   ", "  A   aaaaa   A  ", " GGGGaaEEEaaGGGG ", " GGGGaaEEEaaGGGG ", " GGGGaaEEEaaGGGG ", "  A   aaaaa   A  ", "   A   aaa   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  b  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AA~AA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  b  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  b  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   aaa   A   ", "  A   aaaaa   A  ", " GGGGaaEEEaaGGGG ", " GGGGaaEEEaaGGGG ", " GGGGaaEEEaaGGGG ", "  A   aaaaa   A  ", "   A   aaa   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " } }))
        .addShape(
            STRUCTURE_TIER_2,
            transpose(
                new String[][] {
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   ccc   A   ", "  A   ccccc   A  ", " GGGGccEEEccGGGG ", " GGGGccEEEccGGGG ", " GGGGccEEEccGGGG ", "  A   ccccc   A  ", "   A   ccc   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  d  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AA~AA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  d  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  d  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   ccc   A   ", "  A   ccccc   A  ", " GGGGccEEEccGGGG ", " GGGGccEEEccGGGG ", " GGGGccEEEccGGGG ", "  A   ccccc   A  ", "   A   ccc   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " } }))
        .addShape(
            STRUCTURE_TIER_3,
            transpose(
                new String[][] {

                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   eee   A   ", "  A   eeeee   A  ", " GGGGeeEEEeeGGGG ", " GGGGeeEEEeeGGGG ", " GGGGeeEEEeeGGGG ", "  A   eeeee   A  ", "   A   eee   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  f  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AA~AA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  f  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  f  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   eee   A   ", "  A   eeeee   A  ", " GGGGeeEEEeeGGGG ", " GGGGeeEEEeeGGGG ", " GGGGeeEEEeeGGGG ", "  A   eeeee   A  ", "   A   eee   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " } }))
        .addShape(
            STRUCTURE_TIER_4,
            transpose(
                new String[][] {
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   ggg   A   ", "  A   ggggg   A  ", " GGGGggEEEggGGGG ", " GGGGggEEEggGGGG ", " GGGGggEEEggGGGG ", "  A   ggggg   A  ", "   A   ggg   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  h  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AA~AA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      ADDDA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  D  C     C  D  ", "  D  C  h  C  D  ", "  D  C     C  D  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      ADDDA      ", "                 ", "                 " },
                    { "                 ", "                 ", "      AAAAA      ", "    BA     AB    ", "   B         B   ", "   A   CCC   A   ", "  A   C   C   A  ", "  A  C     C  A  ", "  A  C  h  C  A  ", "  A  C     C  A  ", "  A   C   C   A  ", "   A   CCC   A   ", "   B         B   ", "    BA     AB    ", "      AAAAA      ", "                 ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A   ggg   A   ", "  A   ggggg   A  ", " GGGGggEEEggGGGG ", " GGGGggEEEggGGGG ", " GGGGggEEEggGGGG ", "  A   ggggg   A  ", "   A   ggg   A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "                 ", "       GHG       ", "      AG GA      ", "    BA G G AB    ", "   B   GGG   B   ", "   A    F    A   ", "  A    BFB    A  ", " GGGG BEFEB GGGG ", " H  GFFFEFFFG  H ", " GGGG BEFEB GGGG ", "  A    BFB    A  ", "   A    F    A   ", "   B   GGG   B   ", "    BA G G AB    ", "      AG GA      ", "       GHG       ", "                 " },
                    { "                 ", "       GGG       ", "      AGGGA      ", "    BA GGG AB    ", "   B   GGG   B   ", "   A    B    A   ", "  A    BEB    A  ", " GGGG BEEEB GGGG ", " GGGGBEEEEEBGGGG ", " GGGG BEEEB GGGG ", "  A    BEB    A  ", "   A    B    A   ", "   B   GGG   B   ", "    BA GGG AB    ", "      AGGGA      ", "       GGG       ", "                 " },
                    { "      AAAAA      ", "    AAAAAAAAA    ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAA", " AAAAAAAAAAAAAAA ", " AAAAAAAAAAAAAAA ", "  AAAAAAAAAAAAA  ", "  AAAAAAAAAAAAA  ", "    AAAAAAAAA    ", "      AAAAA      " } }))
        //spotless:on
        .addElement(
            'A',
            buildHatchAdder(MTESpinmatron.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, ExoticEnergy)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(9))
                .hint(1)
                .buildAndChain(onElementPass(MTESpinmatron::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 9))))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings9, 0)) // PBI Pipe Casing
        .addElement('C', ofBlock(GregTechAPI.sBlockGlass1, 6)) // Central Grate Casing
        .addElement('D', chainAllGlasses())
        .addElement('E', Casings.IsaMillGearboxCasing.asElement()) // Isamill central casing
        .addElement('F', Casings.TurbineShaft.asElement()) // Turbine Central Casing
        .addElement('G', ofBlock(supercriticalFluidTurbineCasing, 0)) // Turbine External Casing
        .addElement('H', CentrifugeHatchElement.ROTOR_ASSEMBLY.newAny(1538, 2)) // turbine hatches
        .addElement('a', ofBlock(GregTechAPI.sBlockMetal4, 13)) // t1 block, Naq Alloy
        .addElement(
            'b',
            lazy(
                t -> ofBlock(
                    Block.getBlockFromItem(
                        MaterialsAlloy.PIKYONIUM.getFrameBox(1)
                            .getItem()),
                    0))) // t1 frame, Pikyonium
        .addElement(
            'c',
            lazy(
                t -> Mods.Avaritia.isModLoaded() ? ofBlock(LudicrousBlocks.resource_block, 0)
                    : ofBlock(GregTechAPI.sBlockMetal5, 2))) // t2 block, Cosmic Neutronium. fallback included for dev
        .addElement('d', ofFrame(Materials.Neutronium)) // t2 frame, Neutronium
        .addElement(
            'e',
            lazy(
                t -> Mods.Avaritia.isModLoaded() ? ofBlock(LudicrousBlocks.resource_block, 1)
                    : ofBlock(GregTechAPI.sBlockMetal5, 3))) // t3 block, Infinity. fallback included for dev
        .addElement('f', ofFrame(Materials.Infinity)) // t3 frame, Infinity
        .addElement('g', ofBlock(GregTechAPI.sBlockMetal9, 6)) // t4 block, WDM.
        .addElement('h', lazy(t -> ofFrame(Materials.SpaceTime))) // t4 frame
        .build();

    public MTESpinmatron(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESpinmatron(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTESpinmatron> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void onBlockDestroyed() {

        final IGregTechTileEntity meta = getBaseMetaTileEntity();
        World w = getBaseMetaTileEntity().getWorld();
        final int aX = meta.getXCoord(), aY = meta.getYCoord(), aZ = meta.getZCoord();
        for (int i = 0; i < turbineHolder.getSlots(); i++) {
            if (turbineHolder.getStackInSlot(i) != null) {
                ItemStack currentItem = turbineHolder.extractItem(i, 1, false);
                EntityItem entityItem = new EntityItem(w, aX, aY, aZ, currentItem);
                w.spawnEntityInWorld(entityItem);

            }
        }
        this.setTurbineInactive();
        this.turbineRotorHatchList.clear();
        super.onBlockDestroyed();
    }

    public boolean addTurbineHatch(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchTurbine turbine) {
            turbine.updateTexture(aBaseCasingIndex);
            turbineRotorHatchList.add(turbine);
            return true;
        }
        return false;
    }

    private void rotateTurbines() {
        for (int i = 0; i < turbineRotorHatchList.size(); i++) {
            if (turbineRotorHatchList.get(i) == null) continue;
            MTEHatchTurbine turbine = turbineRotorHatchList.get(i);
            ForgeDirection direction = this.getDirection();
            IGregTechTileEntity te = turbine.getBaseMetaTileEntity();
            // 0, 1 = front top, front bottom
            // 2, 4 = left top, left bottom
            // 3, 5 = right top, right bottom
            // 6, 7 = back top, back bottom (all in theory)
            switch (i) {
                case 0, 1 -> {
                    te.setFrontFacing(direction);
                }
                case 2, 4 -> {
                    te.setFrontFacing(direction.getRotation(ForgeDirection.EAST));
                }
                case 3, 5 -> {
                    te.setFrontFacing(direction.getRotation(ForgeDirection.WEST));
                }
                case 6, 7 -> {
                    te.setFrontFacing(direction.getOpposite());
                }
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tier = aNBT.getInteger("multiTier");
        mode = aNBT.getDouble("multiMode");
        RP = aNBT.getInteger("RP");
        staticAnimations = aNBT.getBoolean("turbineAnimationsStatic");
        tier2Fluid = aNBT.getBoolean("tier2FluidOn");
        lastCheckedTierIndex = aNBT.getInteger("lastCheckedTierIndex");
        if (turbineHolder != null) {
            turbineHolder.deserializeNBT(aNBT.getCompoundTag("inventory"));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("multiTier", tier);
        aNBT.setDouble("multiMode", mode);
        aNBT.setBoolean("tier2FluidOn", tier2Fluid);
        aNBT.setInteger("RP", RP);
        aNBT.setBoolean("turbineAnimationsStatic", staticAnimations);
        aNBT.setInteger("lastCheckedTierIndex", lastCheckedTierIndex);
        if (turbineHolder != null) {
            aNBT.setTag("inventory", turbineHolder.serializeNBT());
        }

    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESpinmatron(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 9)),
                    TextureFactory.builder()
                        .addIcon(TEXTURE_CONTROLLER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(TEXTURE_CONTROLLER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 9)),
                    TextureFactory.builder()
                        .addIcon(TEXTURE_CONTROLLER)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 9)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("gt.recipe.centrifuge")
            .addInfo("gt.spinmatron.tips.1")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo("gt.spinmatron.tips.2")
            .addDynamicParallelInfo(4, TooltipTier.TURBINE)
            .addInfo("gt.spinmatron.tips.3")
            .addStaticSpeedInfo(3f)
            .addStaticEuEffInfo(0.7f)
            .addInfo("gt.spinmatron.tips.4")
            .beginStructureBlock(17, 17, 17, false)
            .addController("front_center")
            .addCasingInfoExactly("GT5U.MBTT.AnyGlass", 81, true)
            .addCasingInfoMin(
                ItemList.Spinmatron_Casing.get(1)
                    .getDisplayName(),
                550)
            .addCasingInfoExactly(
                ItemList.Spinmatron_Chamber_Grate.get(1)
                    .getDisplayName(),
                144)
            .addCasingInfoExactly("gt.spinmatron.info.frame", 9, true)
            .addCasingInfoExactly("gt.spinmatron.info.rotor", 56, true)
            .addCasingInfoExactly(
                GregtechItemList.Casing_IsaMill_Gearbox.get(1)
                    .getDisplayName(),
                54)
            .addCasingInfoExactly(
                ItemList.Casing_Pipe_Polybenzimidazole.get(1)
                    .getDisplayName(),
                160)
            .addCasingInfoExactly(
                GregtechItemList.Casing_Turbine_Shaft.get(1)
                    .getDisplayName(),
                24)
            .addCasingInfoExactly("gt.blockmachines.hatch.turbine.name", 8)
            .addCasingInfoExactly(
                GregtechItemList.Casing_Turbine_SC.get(1)
                    .getDisplayName(),
                264)
            .addInputBus(anyCasing, 1)
            .addOutputBus(anyCasing, 1)
            .addInputHatch(anyCasing, 1)
            .addOutputHatch(anyCasing, 1)
            .addEnergyHatch(anyCasing, 1)
            .addMaintenanceHatch(anyCasing, 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();

        return tt;
    }

    @Override
    public void construct(ItemStack holoStack, boolean hintsOnly) {
        if (holoStack.stackSize == 1) {
            buildPiece(STRUCTURE_TIER_1, holoStack, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        }
        if (holoStack.stackSize == 2) {
            buildPiece(STRUCTURE_TIER_2, holoStack, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        }

        if (holoStack.stackSize == 3) {
            buildPiece(STRUCTURE_TIER_3, holoStack, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        }

        if (holoStack.stackSize >= 4) {
            buildPiece(STRUCTURE_TIER_4, holoStack, hintsOnly, horizontalOffset, verticalOffset, depthOffset);
        }

    }

    @Override
    public int survivalConstruct(ItemStack holoStack, int elementBudget, ISurvivalBuildEnvironment env) {
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);

        if (mMachine) return -1;
        if (holoStack.stackSize == 1) {
            return survivalBuildPiece(
                STRUCTURE_TIER_1,
                holoStack,
                horizontalOffset,
                verticalOffset,
                depthOffset,
                realBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize == 2) {
            return survivalBuildPiece(
                STRUCTURE_TIER_2,
                holoStack,
                horizontalOffset,
                verticalOffset,
                depthOffset,
                realBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize == 3) {
            return survivalBuildPiece(
                STRUCTURE_TIER_3,
                holoStack,
                horizontalOffset,
                verticalOffset,
                depthOffset,
                realBudget,
                env,
                false,
                true);
        }
        if (holoStack.stackSize >= 4) {
            return survivalBuildPiece(
                STRUCTURE_TIER_4,
                holoStack,
                horizontalOffset,
                verticalOffset,
                depthOffset,
                realBudget,
                env,
                false,
                true);
        }
        return 0;
    }

    private int casingAmount;

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        tier = 0;
        if (lastCheckedTierIndex != 0) Collections.swap(tierCheckOrderList, 0, lastCheckedTierIndex);
        for (int i = 0; i < tierCheckOrderList.size(); i++) {
            StructureData piece = tierCheckOrderList.get(i);
            resetParameters();
            if (checkPiece(piece.structurePiece, horizontalOffset, verticalOffset, depthOffset)) {
                tier = piece.machineTier;
                lastCheckedTierIndex = i;
                rotateTurbines();
                return casingAmount >= 550;
            }
        }

        return false;
    }

    private void resetParameters() {
        clearHatches();
        casingAmount = 0;
        turbineRotorHatchList.clear();
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setMaxParallel(getTrueParallel());
        logic.setUnlimitedTierSkips();
        if (mExoticEnergyHatches.isEmpty() && !debugEnergyPresent) {
            logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
            logic.setAvailableAmperage(1L);
        } else super.setProcessingLogicPower(logic);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                amountToDrain = GTUtility.getTier(recipe.mEUt) * 10;
                euMultiplier = 1;
                if (!checkFluid(5 * amountToDrain)) return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");
                if (mode == 0.0 && GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt) < 3)
                    return CheckRecipeResultRegistry.NO_RECIPE;
                if (mode == 2.0) {
                    if (!tier2Fluid) return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");
                    euMultiplier = 16;
                }

                if (recipe.getMetadataOrDefault(CentrifugeRecipeKey.INSTANCE, Boolean.FALSE) && mode != 2.0)
                    return CheckRecipeResultRegistry.NO_RECIPE;

                getSpeed();
                setSpeedBonus(1F / speed);
                setEuModifier(0.7 * euMultiplier);
                return super.validateRecipe(recipe);
            }

            @Override
            protected @NotNull CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                setTurbineActive();
                return super.onRecipeStart(recipe);
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) { // implements Hatch+1 OC
                return super.createOverclockCalculator(recipe).setMaxOverclocks(
                    (GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt)) + 1);
            }
        };
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        setTurbineInactive();
        super.stopMachine(reason);
    }

    public boolean isTurbine(ItemStack aStack) { // thank you airfilter!
        if (aStack == null) return false;
        if (!(aStack.getItem() instanceof MetaGeneratedTool01 tool)) return false;
        if (aStack.getItemDamage() < 170 || aStack.getItemDamage() > 179) return false;

        IToolStats stats = tool.getToolStats(aStack);
        if (stats == null || stats.getSpeedMultiplier() <= 0) return false;

        Materials material = MetaGeneratedTool.getPrimaryMaterial(aStack);
        return material != null && material.mToolSpeed > 0;
    }

    private int getSumRotorLevels() {
        int sumRotorLevels = 0;

        for (int i = 0; i < tier * 2; i++) {
            if (turbineHolder.getStackInSlot(i) != null) { // operate under the assumption the tool in the slot IS a
                // rotor.
                ItemStack currentItem = turbineHolder.getStackInSlot(i);
                IToolStats toolStats = ((MetaGeneratedTool) currentItem.getItem()).getToolStats(currentItem);
                int harvestLevel = ((MetaGeneratedTool) currentItem.getItem()).getHarvestLevel(currentItem, "test");

                if (toolStats instanceof ToolTurbineHuge) {
                    sumRotorLevels += harvestLevel;
                    continue;
                }
                if (toolStats instanceof ToolTurbineLarge) {
                    sumRotorLevels += (int) (0.75F * harvestLevel);
                    continue;
                }
                if (toolStats instanceof ToolTurbineNormal) {
                    sumRotorLevels += (int) (0.5F * harvestLevel);
                    continue;
                }
                if (toolStats instanceof ToolTurbineSmall) {
                    sumRotorLevels += (int) (0.25F * harvestLevel);
                }

            }
        }

        return sumRotorLevels;
    }

    private boolean checkFluid(int amount) {
        // checks for fluid in hatch, does not drain it.
        final FluidStack tFluid = tier2Fluid ? Materials.BiocatalyzedPropulsionFluid.getFluid(amount)
            : new FluidStack(GTPPFluids.Kerosene, amount);

        return this.depleteInput(tFluid, true);
    }

    public void setTurbineActive() {
        if (staticAnimations) return;

        for (MTEHatchTurbine h : validMTEList(this.turbineRotorHatchList)) {
            h.setActive(true);
            h.onTextureUpdate();
        }
    }

    public void setTurbineInactive() {
        for (MTEHatchTurbine h : validMTEList(this.turbineRotorHatchList)) {
            h.setActive(false);
            h.onTextureUpdate();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 100 == 0) {
            if (!getBaseMetaTileEntity().isActive() && !this.turbineRotorHatchList.isEmpty()) {
                setTurbineInactive();
            }
        }
    }

    @Override
    public int getMaxParallelRecipes() {

        getRP(); // updates RP
        int parallels = RP;
        if (tier2Fluid) {
            parallels = (int) Math.floor(parallels * 1.25);
        }
        if (mode == 2.0) {
            parallels /= 32;
        }
        return parallels > 0 ? parallels : 1; // if its 1, something messed up lol, just a failsafe in case i mess up
    }

    private int ticker = 1; // just increments and drains (amountToDrain) of the given

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }
        // might need a cleanup here
        if (ticker % 21 == 0) {

            FluidStack tFluid = tier2Fluid ? Materials.BiocatalyzedPropulsionFluid.getFluid(amountToDrain)
                : new FluidStack(GTPPFluids.Kerosene, amountToDrain); // gets fluid to drain
            for (MTEHatchInput mInputHatch : mInputHatches) {
                if (drain(mInputHatch, tFluid, true)) {
                    ticker = 1;
                    return true;
                }
            }
            stopMachine(ShutDownReasonRegistry.outOfFluid(tFluid));
            ticker = 1;
            return false;
        }
        ticker++;
        return true;
    }

    @Override
    protected @NotNull MTESpinmatronGui getGui() {
        return new MTESpinmatronGui(this);
    }

    public int getRP() {
        RP = 4 * getSumRotorLevels();
        return RP;
    }

    public float getSpeed() {
        speed = 3F;
        if (mode == 0.0) {
            speed = 4.0F;
        }
        return speed;
    }

    public String getSpeedStr() {
        return (getSpeed()) * 100 + "%";
    }

    public String modeToString() {
        if (mode == 0.0) {
            return "Light";
        }
        if (mode == 1.0) {
            return "Standard";
        }
        if (mode == 2.0) {
            return "Heavy";
        }
        return "Unset";
    }

    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        staticAnimations = !staticAnimations;
        GTUtility
            .sendChatToPlayer(aPlayer, "Using " + (staticAnimations ? "Static" : "Animated") + " Turbine Texture.");
        for (MTEHatchTurbine h : validMTEList(this.turbineRotorHatchList)) {
            h.mUsingAnimation = staticAnimations;
        }
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_SPINMATRON;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.centrifugeNonCellRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
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
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    private enum CentrifugeHatchElement implements IHatchElement<MTESpinmatron> {

        ROTOR_ASSEMBLY(MTESpinmatron::addTurbineHatch, MTEHatchTurbine.class) {

            @Override
            public long count(MTESpinmatron mteSpinmatron) {
                return mteSpinmatron.turbineRotorHatchList.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTESpinmatron> adder;

        @SafeVarargs
        CentrifugeHatchElement(IGTHatchAdder<MTESpinmatron> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTESpinmatron> adder() {
            return adder;
        }
    }
}

// struct for packaging data for structure piece so i don't have to do String manipulation
enum StructureData {

    tier1(1, "t1"),
    tier2(2, "t2"),
    tier3(3, "t3"),
    tier4(4, "t4");

    public final int machineTier;
    public final String structurePiece;

    StructureData(int machineTier, String structurePiece) {
        this.machineTier = machineTier;
        this.structurePiece = structurePiece;
    }

}
