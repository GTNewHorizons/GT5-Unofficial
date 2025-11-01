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
import static net.minecraft.util.EnumChatFormatting.BOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
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
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
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
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.gui.modularui.multiblock.MTEChamberCentrifugeGui;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;

public class MTEChamberCentrifuge extends MTEExtendedPowerMultiBlockBase<MTEChamberCentrifuge>
    implements ISurvivalConstructable {

    public boolean tier2Fluid = false;
    public double mode = 1.0; // i think it has to be a double cuz slider. 0 = speed, 1 = normal, 2 = heavy
    public int RP = 0;
    public float speed = 3F;
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
    private static final String STRUCTURE_MAIN = "main";
    private static final IIconContainer TEXTURE_CONTROLLER = new Textures.BlockIcons.CustomIcon("iconsets/TFFT");
    private static final IIconContainer TEXTURE_CONTROLLER_ACTIVE = new Textures.BlockIcons.CustomIcon(
        "iconsets/TFFT_ACTIVE");
    private static final IIconContainer TEXTURE_CONTROLLER_ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon(
        "iconsets/TFFT_ACTIVE_GLOW");
    public ArrayList<MTEHatchTurbine> mTurbineRotorHatches = new ArrayList<>();

    private boolean mStaticAnimations = false;
    // spotless:off

    private static final IStructureDefinition<MTEChamberCentrifuge> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEChamberCentrifuge>builder()
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
            buildHatchAdder(MTEChamberCentrifuge.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy, ExoticEnergy)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(9))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEChamberCentrifuge::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings12, 9))))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings9, 0)) // PBI Pipe Casing
        .addElement('C', ofBlock(GregTechAPI.sBlockGlass1, 6)) // Central Grate Casing
        .addElement('D', chainAllGlasses())
        .addElement('E', Casings.IsamillGearBoxCasing.asElement()) // Isamill central casing
        .addElement('F', Casings.TurbineShaft.asElement()) // Turbine Central Casing
        .addElement('G', ofBlock(supercriticalFluidTurbineCasing, 0)) // Turbine External Casing
        .addElement(
            'H',
            buildHatchAdder(MTEChamberCentrifuge.class).adder(MTEChamberCentrifuge::addTurbineHatch)
                .hatchClass(MTEHatchTurbine.class)
                .casingIndex(1538)
                .dot(2)
                .build()) // Turbine Holder Hatches
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

    public MTEChamberCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEChamberCentrifuge(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEChamberCentrifuge> getStructureDefinition() {
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
        this.mTurbineRotorHatches.clear();
        super.onBlockDestroyed();
    }

    public boolean addTurbineHatch(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchTurbine) {
            mTurbineRotorHatches.add((MTEHatchTurbine) aMetaTileEntity);

            return true;
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        tier = aNBT.getInteger("multiTier");
        mode = aNBT.getDouble("multiMode");
        RP = aNBT.getInteger("RP");
        mStaticAnimations = aNBT.getBoolean("turbineAnimationsStatic");
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
        aNBT.setBoolean("turbineAnimationsStatic", mStaticAnimations);
        aNBT.setInteger("lastCheckedTierIndex", lastCheckedTierIndex);
        if (turbineHolder != null) {
            aNBT.setTag("inventory", turbineHolder.serializeNBT());
        }

    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEChamberCentrifuge(this.mName);
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
        tt.addMachineType("Centrifuge")
            .addInfo(
                "3 Modes: " + EnumChatFormatting.LIGHT_PURPLE
                    + "Light"
                    + EnumChatFormatting.GRAY
                    + " | "
                    + EnumChatFormatting.GOLD
                    + "Standard"
                    + EnumChatFormatting.GRAY
                    + " | "
                    + EnumChatFormatting.GREEN
                    + "Heavy")

            .addInfo("Overclocks limited to " + EnumChatFormatting.WHITE + "Hatch Tier + 1")
            .addTecTechHatchInfo()
            .addSeparator()
            .addInfo(
                "Gains " + EnumChatFormatting.WHITE
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " Turbine Slots per Structure Tier")
            .addDynamicParallelInfo(4, TooltipTier.TURBINE)
            .addInfo("Non-Huge Turbines have reduced effectiveness...")
            .addStaticSpeedInfo(3f)
            .addStaticEuEffInfo(0.7f)
            .addInfo(
                "Requires Recipe Tier * " + EnumChatFormatting.BLUE
                    + "10L/s"
                    + EnumChatFormatting.GRAY
                    + " of "
                    + EnumChatFormatting.DARK_PURPLE
                    + "Kerosene"
                    + EnumChatFormatting.GRAY
                    + " to operate by default")
            .addInfo(
                "Supply " + EnumChatFormatting.DARK_PURPLE
                    + "Biocatalyzed Propulsion Fluid"
                    + EnumChatFormatting.GRAY
                    + " instead for a "
                    + EnumChatFormatting.WHITE
                    + "1.25x "
                    + EnumChatFormatting.GRAY
                    + "Parallel multiplier")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + "Light Mode"
                    + EnumChatFormatting.GRAY
                    + ": +"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "100%"
                    + EnumChatFormatting.GRAY
                    + " Speed Bonus, "
                    + "Maximum Recipe Tier is "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Voltage Tier - 3")
            .addInfo(EnumChatFormatting.GOLD + "Standard Mode" + EnumChatFormatting.GRAY + ": No Changes")
            .addInfo(
                EnumChatFormatting.GREEN + "Heavy Mode"
                    + EnumChatFormatting.GRAY
                    + ": Divides Parallels by "
                    + EnumChatFormatting.GREEN
                    + "32"
                    + EnumChatFormatting.GRAY
                    + ", Requires T3 Structure and "
                    + EnumChatFormatting.DARK_PURPLE
                    + "Biocatalyzed Propulsion Fluid")
            .addInfo(
                "Some recipes " + EnumChatFormatting.RED + BOLD + "require" + EnumChatFormatting.GREEN + " Heavy Mode")

            .addSeparator()
            .addInfo(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.DARK_RED + "Maahes guides the way...")
            .beginStructureBlock(17, 17, 17, false)
            .addController("Front Center")
            .addCasingInfoExactly("Any Tiered Glass", 81, true)
            .addCasingInfoMin("Vibration-Safe Casing", 550, false)
            .addCasingInfoExactly("Chamber Grate", 144, false)
            .addCasingInfoExactly("Central Frame Blocks", 9, true)
            .addCasingInfoExactly("Central Rotor Blocks", 56, true)
            .addCasingInfoExactly("IsaMill Gearbox Casing", 54, false)
            .addCasingInfoExactly("PBI Pipe Casing", 160, false)
            .addCasingInfoExactly("Turbine Shaft", 24, false)
            .addCasingInfoExactly("Rotor Assembly", 8, false)
            .addCasingInfoExactly("SC Turbine Casing", 264, false)
            .addInputBus("Any Vibration-Safe Casing", 1)
            .addOutputBus("Any Vibration-Safe Casing", 1)
            .addInputHatch("Any Vibration-Safe Casing", 1)
            .addOutputHatch("Any Vibration-Safe Casing", 1)
            .addEnergyHatch("Any Vibration-Safe Casing", 1)
            .addMaintenanceHatch("Any Vibration-Safe Casing", 1)
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
                return casingAmount >= 550;
            }
        }

        return false;
    }

    private void resetParameters() {
        clearHatches();
        casingAmount = 0;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setMaxParallel(getTrueParallel());
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
                if (!checkFluid(7 * amountToDrain)) return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");
                if (mode == 0.0 && GTUtility.getTier(getAverageInputVoltage()) - GTUtility.getTier(recipe.mEUt) < 3)
                    return CheckRecipeResultRegistry.NO_RECIPE;
                if (mode == 2.0 && !tier2Fluid) return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");

                if (recipe.getMetadataOrDefault(CentrifugeRecipeKey.INSTANCE, Boolean.FALSE) && mode != 2.0)
                    return CheckRecipeResultRegistry.NO_RECIPE;

                getSpeed();
                setSpeedBonus(1F / speed);
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
        }.setEuModifier(0.7F);
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

    private boolean checkFluid(int amount) // checks if 5 seconds worth of fluid is found in ANY of the machines input
                                           // hatches
    {
        // checks for fluid in hatch, does not drain it.
        FluidStack tFluid = tier2Fluid ? Materials.BiocatalyzedPropulsionFluid.getFluid(amount)
            : new FluidStack(GTPPFluids.Kerosene, amount);
        for (MTEHatchInput mInputHatch : mInputHatches) {
            if (drain(mInputHatch, tFluid, false)) {
                return true;
            }
        }
        return false; // fluid was not found.
    }

    public void setTurbineActive() {
        if (mStaticAnimations) return;

        for (MTEHatchTurbine h : validMTEList(this.mTurbineRotorHatches)) {
            h.setActive(true);
            h.onTextureUpdate();
        }
    }

    public void setTurbineInactive() {
        for (MTEHatchTurbine h : validMTEList(this.mTurbineRotorHatches)) {
            h.setActive(false);
            h.onTextureUpdate();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 100 == 0) {
            if (!getBaseMetaTileEntity().isActive() && !this.mTurbineRotorHatches.isEmpty()) {
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
    protected @NotNull MTEChamberCentrifugeGui getGui() {
        return new MTEChamberCentrifugeGui(this);
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
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
        mStaticAnimations = !mStaticAnimations;
        GTUtility
            .sendChatToPlayer(aPlayer, "Using " + (mStaticAnimations ? "Static" : "Animated") + " Turbine Texture.");
        for (MTEHatchTurbine h : validMTEList(this.mTurbineRotorHatches)) {
            h.mUsingAnimation = mStaticAnimations;
        }
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
