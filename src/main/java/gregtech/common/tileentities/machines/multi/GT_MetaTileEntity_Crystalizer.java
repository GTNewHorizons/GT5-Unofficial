package gregtech.common.tileentities.machines.multi;

import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Textures.BlockIcons.CRYSTALIZER_FRONT_OVERLAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.CRYSTALIZER_FRONT_OVERLAY_INACTIVE;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.*;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.casing.GT_Block_CasingsTT;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsLapotronLine;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.blocks.GT_Block_Casings10;
import gregtech.common.blocks.GT_Block_Casings2;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.nei.RecipeDisplayInfo;
import gtPlusPlus.core.material.ALLOY;

public class GT_MetaTileEntity_Crystalizer
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_Crystalizer>
    implements ISurvivalConstructable, ICleanroomReceiver {

    static class CrystalizerFrontend extends RecipeMapFrontend {

        @Override
        public void drawDescription(RecipeDisplayInfo recipeInfo) {
            drawEnergyInfo(recipeInfo);
            drawDurationInfo(recipeInfo);
            drawSpecialInfo(recipeInfo);
            drawMetadataInfo(recipeInfo);
        }

        public CrystalizerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
            super(uiPropertiesBuilder, neiPropertiesBuilder);
        }
    }

    static class RecipeMapBackendCrystalizer extends RecipeMapBackend {

        public RecipeMapBackendCrystalizer(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
            super(propertiesBuilder);
        }

        @Override
        public void reInit() {

        }
    }

    public static final RecipeMap<RecipeMapBackendCrystalizer> CRYSTALIZER_RECIPES = RecipeMapBuilder
        .of("gt.recipe.crystalizertst", RecipeMapBackendCrystalizer::new)
        .minInputs(1, 1)
        .maxIO(6, 1, 3, 0)
        .amperage(1)
        .frontend(CrystalizerFrontend::new)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiSpecialInfoFormatter(recipeInfo -> {
            String[] lvs = { "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV" };
            int minForceLevel = -recipeInfo.recipe.mSpecialValue / 1000;
            boolean lowg = recipeInfo.recipe.mSpecialValue % 1000 == -101;
            ArrayList<String> strings = new ArrayList<>();
            strings.add(String.format("Requires %s force containment casing", lvs[minForceLevel - 1]));
            if (lowg) {
                strings.add("Requires low gravity");
            }
            strings.add("Costs far more time under certainty mode.");
            return strings;
        })
        .build();

    private static boolean initialized;
    private int fleldGeneratorTier = -1;

    private int fleldGeneratorTier2 = -1;

    private byte glassTier = -1;

    private double baseSpeedModifier = 1.0f;
    private double baseEUtModifier = 1.0f;
    private static List<Pair<Block, Integer>> containment_field_blocks;

    private int activeTicks;

    private int maxActiveTicks;

    private ICleanroom cleanroom;

    boolean enableChance = false;

    // spotless:off
    static final String[][] STRUCTURE = new String[][]{{
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "   H~H   ",
        "   HHH   "
    },{
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "   HHH   ",
        "   HHH   "
    },{
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "    B    ",
        "  GGGGG  "
    },{
        "         ",
        "         ",
        "    A    ",
        "   AAA   ",
        "   AAA   ",
        "   AAA   ",
        "    A    ",
        "    B    ",
        "    B    ",
        "    B    ",
        "         ",
        " GGGGGGG "
    },{
        "         ",
        "   AAA   ",
        "  AACAA  ",
        "  A C A  ",
        "  A C A  ",
        "  A C A  ",
        "  AACAA  ",
        "   AAA   ",
        "   CCC   ",
        "         ",
        "         ",
        "GGGGGGGGG"
    },{
        "   AAA   ",
        "  ACCCA  ",
        "  A   A  ",
        " A     A ",
        " A     A ",
        " A     A ",
        "  A   A  ",
        "  ACCCA  ",
        "  CAAAC  ",
        "   FFF   ",
        "   EEE   ",
        "GGGGGGGGG"
    },{
        "   AAA   ",
        "  ACCCA  ",
        " AC   CA ",
        " AC   CA ",
        " AC D CA ",
        " AC   CA ",
        " AC   CA ",
        " BACCCAB ",
        " BCAAACB ",
        " B FFF B ",
        "B  EEE  B",
        "GGGGGGGGG"
    },{
        "   AAA   ",
        "  ACCCA  ",
        "  A   A  ",
        " A     A ",
        " A     A ",
        " A     A ",
        "  A   A  ",
        "  ACCCA  ",
        "  CAAAC  ",
        "   FFF   ",
        "   EEE   ",
        "GGGGGGGGG"
    },{
        "         ",
        "   AAA   ",
        "  AACAA  ",
        "  A C A  ",
        "  A C A  ",
        "  A C A  ",
        "  AACAA  ",
        "   AAA   ",
        "   CCC   ",
        "         ",
        "         ",
        "GGGGGGGGG"
    },{
        "         ",
        "         ",
        "    A    ",
        "   AAA   ",
        "   AAA   ",
        "   AAA   ",
        "    A    ",
        "    B    ",
        "    B    ",
        "    B    ",
        "         ",
        " GGGGGGG "
    },{
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "    B    ",
        "  GGGGG  "
    }};
    // spotless:on
    static IStructureDefinition<GT_MetaTileEntity_Crystalizer> DEF = null;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (enableChance) {
            GT_Utility.sendChatToPlayer(aPlayer, "Enabled certain mode, output chance is 100% but slower.");
        } else {
            GT_Utility.sendChatToPlayer(aPlayer, "Enabled chance mode, keep machine running and get bonus.");
        }
        enableChance = !enableChance;
    }

    public static void initializeRecipes() {
        if (initialized) return;
        // HV Shaped Craft
        // X O X
        // O V O
        // X O X
        // X: HV Field Generator, O: stainless steel plate, V: Stainless steel framebax

        // EV assembling
        // 1X HV Field Generator Block, 1X titanium frame box, 4X Titanium plate, 4X EV field Generator, 16xEV wire
        // 864mb soldering
        // alloy, 1920EU/t 1A 20s

        // IV assembling
        // 1X EV Field Generator Block, 1X tungstensteel frame box, 4X tungsten steel plate, 4X IV field Generator, 16x
        // IV superconductor wire
        // 2304mb soldering alloy, 1296mb Ni2Ti3 7680EU/t 1A 40s

        // ZPM Assembly Line
        // 1X Containment Field Generator, 1X Fusion Casing MK2, 4X ZPM Field Generator, 8X ZPM circuit board, 64X ZPM
        // superconductor, 2304mb Helicopter, 1296mb pikynium 64B, 2304mb soldering alloy 131072EU/t 1A 60s

        // UV assembly Line
        // 1X ZPM Field Generator Block, 1X Fusion Casing MK3, 4X UV Field Generator, 8X UV circuit board, 64X UV
        // superconductor, 4608mb Helicopter, 2304mb pikynium 64B, 4608mb soldering alloy 524288EU/t 1A 120s

        // Controller Shaped Craft
        // X V X
        // G O G
        // X G X
        // O: EV autoclave, X: IV circuit board, V: EV field generator, G: Glass
        // this.addRecipe(new ItemStack(Blocks.jukebox, 1), new Object[] {"###", "#X#", "###", '#', Blocks.planks, 'X',
        // Items.diamond});
        GT_ModHandler.addCraftingRecipe(
            ItemList.Casing_ContainmentFieldHV.get(1),
            new Object[] { "XOX", "OVO", "XOX", 'X', ItemList.Field_Generator_HV.get(1), 'O',
                Materials.StainlessSteel.getPlates(1), 'V',
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1) });

        RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_EV_Autoclave.get(1))
            .metadata(RESEARCH_TIME, 12 * HOURS)
            .itemInputs(
                ItemList.Casing_LuV.get(1),
                ItemList.Machine_EV_Autoclave.get(2),
                ItemList.Machine_IV_Autoclave.get(2),
                GT_Utility.copyAmount(2, LanthItemList.DISSOLUTION_TANK),
                ItemList.Casing_ContainmentFieldIV.get(1),
                ItemList.Casing_ContainmentFieldIV.get(1),
                ItemList.Casing_ContainmentFieldIV.get(1),
                ItemList.Casing_ContainmentFieldIV.get(1),
                new ItemStack(ItemBlock.getItemFromBlock(Blocks.glass), 32),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Europium, 32),
                ItemList.Electric_Pump_LuV.get(16),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 32))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(9216),
                ALLOY.HELICOPTER.getFluidStack(9216),
                ALLOY.ZERON_100.getFluidStack(9216))
            .itemOutputs(ItemList.Machine_Large_Crystalizer.get(1))
            .eut(TierEU.ZPM)
            .duration(600)
            .addTo(AssemblyLine);

        // EV
        RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ContainmentFieldHV.get(1),
                ItemList.Casing_StableTitanium.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                ItemList.Field_Generator_EV.get(6),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                GT_Utility.getIntegratedCircuit(24))
            .fluidInputs(Materials.SolderingAlloy.getMolten(864))
            .itemOutputs(ItemList.Casing_ContainmentFieldEV.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.EV)
            .addTo(RecipeMaps.assemblerRecipes);

        // IV
        RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ContainmentFieldEV.get(1),
                ItemList.Casing_RobustTungstenSteel.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 16),
                ItemList.Field_Generator_IV.get(6),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8 },
                GT_Utility.getIntegratedCircuit(24))
            .fluidInputs(new FluidStack(ALLOY.NITINOL_60.getFluid(), 1296))
            .itemOutputs(ItemList.Casing_ContainmentFieldIV.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.IV)
            .addTo(RecipeMaps.assemblerRecipes);

        // LuV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Casing_ContainmentFieldIV.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Casing_Advanced_Rhodium_Palladium.get(1),
                ItemList.Casing_ContainmentFieldIV.get(1),
                ItemList.Field_Generator_LuV.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 64))
            .itemOutputs(ItemList.Casing_ContainmentField.get(1))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(2304),
                new FluidStack(ALLOY.HELICOPTER.getFluid(), 2304),
                new FluidStack(ALLOY.ZERON_100.getFluid(), 1296))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // ZPM
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Casing_ContainmentField.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Casing_ContainmentField.get(1),
                ItemList.Casing_Fusion.get(1),
                ItemList.Field_Generator_ZPM.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64))
            .itemOutputs(ItemList.Casing_ContainmentFieldZPM.get(1))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(2304),
                new FluidStack(ALLOY.HELICOPTER.getFluid(), 2304),
                new FluidStack(ALLOY.PIKYONIUM.getFluid(), 1296))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // UV
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Casing_ContainmentFieldZPM.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Casing_ContainmentFieldZPM.get(1),
                ItemList.Casing_Fusion2.get(1),
                ItemList.Field_Generator_UV.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 64))
            .itemOutputs(ItemList.Casing_ContainmentFieldUV.get(1))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(4608),
                new FluidStack(ALLOY.HELICOPTER.getFluid(), 4608),
                new FluidStack(ALLOY.PIKYONIUM.getFluid(), 2304))
            .duration(30 * SECONDS)
            .eut((int) TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        // UEV+
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_Ultimate_Containment_Field.get(1),
            16777216,
            2048,
            (int) TierEU.UEV,
            4,
            new ItemStack[] { CustomItemList.eM_Containment_Field.get(4), ItemList.Field_Generator_UEV.get(64),
                ItemList.Field_Generator_UEV.get(16), ItemList.Field_Generator_UIV.get(16), ItemList.Tesseract.get(32),
                ItemList.EnergisedTesseract.get(32),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 32),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 32),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 16) },
            new FluidStack[] { Materials.Infinity.getMolten(4608),
                new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 36864),
                new FluidStack(FluidRegistry.getFluid("molten.mutatedlivingsolder"), 36864), },
            CustomItemList.eM_Ultimate_Containment_Field.get(1),
            30 * SECONDS,
            (int) TierEU.UIV);

        // Lap-Naquadah dust
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Naquadah.getDust(2),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 7, 0))
            .itemOutputs(MaterialsLapotronLine.LapotronNaquadahMixture.getDust(9))
            .duration(SECONDS)
            .eut(TierEU.LuV)
            .addTo(RecipeMaps.mixerRecipes);
        // Lap-Naquadria dust
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Naquadria.getDust(1),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 2, 0))
            .itemOutputs(MaterialsLapotronLine.LapotronNaquadriaMixture.getDust(3))
            .duration(SECONDS)
            .eut(TierEU.ZPM)
            .addTo(RecipeMaps.mixerRecipes);
        // Lap-Am dust
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Americium.getDust(2),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 7, 0))
            .itemOutputs(MaterialsLapotronLine.LapotronAmerciumMixture.getDust(9))
            .duration(5 * SECONDS)
            .eut(TierEU.UHV)
            .addTo(RecipeMaps.mixerRecipes);
        // Lap-Inf dust
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Infinity.getDust(5),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 4, 0),
                Materials.EnrichedHolmium.getDust(5))
            .itemOutputs(MaterialsLapotronLine.LapotronInfinityMixture.getDust(14))
            .duration(5 * SECONDS)
            .eut(TierEU.UEV)
            .addTo(RecipeMaps.mixerRecipes);
        // Perfect recipe 1
        ItemStack lap = ItemList.IC2_LapotronCrystal.get(16);
        lap.setItemDamage(26);
        RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LapotronDust", 1024, 0), lap)
            .fluidInputs(Materials.VibrantAlloy.getMolten(64000))
            .itemOutputs(ItemList.PerfectLapotronCrystal.get(1))
            .outputChances(1140)
            .eut(TierEU.HV)
            .requiresLowGravity()
            .duration(86400 * SECONDS)
            .noOptimize()
            .build()
            .map(r -> {
                r.mSpecialValue -= 1001;
                r.mInputs[0].stackSize = 1024;
                CRYSTALIZER_RECIPES.addRecipe(r);
                return r;
            });

        // Perfect recipe 2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                MaterialsLapotronLine.LapotronInfinityMixture.getDust(256),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.UEV), 4),
                ItemList.LapotronShard.get(1))
            .fluidInputs(
                Materials.Grade8PurifiedWater.getFluid(16000),
                Materials.VibrantAlloy.getMolten(4608),
                ALLOY.QUANTUM.getFluidStack(1440))
            .itemOutputs(ItemList.PerfectLapotronCrystal.get(4))
            .outputChances(5140)
            .eut(TierEU.UV * 2)
            .duration(30 * SECONDS)
            .noOptimize()
            .build()
            .map(r -> {
                r.mSpecialValue -= 5000;
                r.mInputs[0].stackSize = 256;
                CRYSTALIZER_RECIPES.addRecipe(r);
                return r;
            });

        // Perfect Ruby
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Ruby.getDust(128),
                Materials.Redstone.getDust(64),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.LuV), 4))
            .fluidInputs(
                Materials.Grade3PurifiedWater.getFluid(16000),
                ALLOY.ARCANITE.getFluidStack(720),
                Materials.EnergeticAlloy.getMolten(9216))
            .itemOutputs(ItemList.PerfectRuby.get(1))
            .eut(TierEU.LuV)
            .duration(45 * SECONDS)
            .noOptimize()
            .build()
            .map(r -> {
                r.mSpecialValue -= 3000;
                r.mInputs[0].stackSize = 128;
                CRYSTALIZER_RECIPES.addRecipe(r);
                return r;
            });

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.LapotronShard.get(1),
                MaterialsLapotronLine.LapotronNaquadahMixture.getDust(288),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.LuV), 8))
            .fluidInputs(
                Materials.VibrantAlloy.getMolten(2304),
                Materials.Grade5PurifiedWater.getFluid(16000),
                ALLOY.ARCANITE.getFluidStack(576))
            .itemOutputs(ItemList.CrudeLapotronCrystal.get(2))
            .outputChances(5140)
            .eut(TierEU.EV * 2)
            .duration(60 * SECONDS)
            .noOptimize()
            .build()
            .map(r -> {
                r.mSpecialValue -= 2000;
                r.mInputs[1].stackSize = 288;
                CRYSTALIZER_RECIPES.addRecipe(r);
                return r;
            });

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.LapotronShard.get(1),
                MaterialsLapotronLine.LapotronNaquadriaMixture.getDust(288),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.ZPM), 8))
            .fluidInputs(
                Materials.VibrantAlloy.getMolten(2304),
                Materials.Grade6PurifiedWater.getFluid(16000),
                ALLOY.HELICOPTER.getFluidStack(288))
            .itemOutputs(ItemList.StableLapotronCrystal.get(2))
            .outputChances(5140)
            .eut(14314)
            .duration(60 * SECONDS)
            .noOptimize()
            .build()
            .map(r -> {
                r.mSpecialValue -= 3000;
                r.mInputs[1].stackSize = 288;
                CRYSTALIZER_RECIPES.addRecipe(r);
                return r;
            });

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.LapotronShard.get(1),
                MaterialsLapotronLine.LapotronAmerciumMixture.getDust(352),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.UHV), 4))
            .fluidInputs(
                Materials.VibrantAlloy.getMolten(4608),
                Materials.Grade7PurifiedWater.getFluid(16000),
                ALLOY.OCTIRON.getFluidStack(1440))
            .itemOutputs(ItemList.GoodLapotronCrystal.get(3))
            .outputChances(5140)
            .eut(TierEU.ZPM * 2)
            .duration(30 * SECONDS)
            .noOptimize()
            .build()
            .map(r -> {
                r.mSpecialValue -= 4000;
                r.mInputs[1].stackSize = 352;
                CRYSTALIZER_RECIPES.addRecipe(r);
                return r;
            });
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.CrudeLapotronCrystal.get(1))
            .itemOutputs(ItemList.LapotronShard.get(8))
            .eut(TierEU.LuV)
            .duration(SECONDS * 5)
            .addTo(RecipeMaps.hammerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.StableLapotronCrystal.get(1))
            .itemOutputs(ItemList.LapotronShard.get(16))
            .eut(TierEU.LuV)
            .duration(SECONDS * 5)
            .addTo(RecipeMaps.hammerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.GoodLapotronCrystal.get(1))
            .itemOutputs(ItemList.LapotronShard.get(32))
            .eut(TierEU.LuV)
            .duration(SECONDS * 5)
            .addTo(RecipeMaps.hammerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.PerfectLapotronCrystal.get(1))
            .itemOutputs(ItemList.LapotronShard.get(64))
            .eut(TierEU.LuV)
            .duration(SECONDS * 5)
            .addTo(RecipeMaps.hammerRecipes);

        initialized = true;
    }

    public static int getFieldGeneratorTier(Block block, int meta) {
        if (block instanceof GT_Block_Casings10) {
            return switch (meta) {
                case 3 -> 1;
                case 4 -> 2;
                case 5 -> 3;
                case 6 -> 5;
                case 7 -> 6;
                default -> -1;
            };
        } else if (block instanceof GT_Block_Casings2) {
            if (meta == 8) {
                return 4;
            } else {
                return -1;
            }
        } else if (block instanceof GT_Block_CasingsTT) {
            if (meta == 6) return 7;
            else if (meta == 14) return 8;
            else return -1;
        } else {
            return -1;
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return CRYSTALIZER_RECIPES;
    }

    public static byte getGlassTier(GT_MetaTileEntity_Crystalizer te) {
        return te.glassTier;
    }

    public static void setGlassTier(GT_MetaTileEntity_Crystalizer te, byte tier) {
        te.glassTier = tier;
    }

    public static IStructureDefinition<GT_MetaTileEntity_Crystalizer> initializeDefinition() {
        containment_field_blocks = Arrays.asList(
            Pair.of(ItemList.Casing_ContainmentFieldHV.getBlock(), 3),
            Pair.of(ItemList.Casing_ContainmentFieldEV.getBlock(), 4),
            Pair.of(ItemList.Casing_ContainmentFieldIV.getBlock(), 5),
            Pair.of(ItemList.Casing_ContainmentField.getBlock(), 8),
            Pair.of(ItemList.Casing_ContainmentFieldZPM.getBlock(), 6),
            Pair.of(ItemList.Casing_ContainmentFieldUV.getBlock(), 7),
            Pair.of(CustomItemList.eM_Containment_Field.getBlock(), 6),
            Pair.of(CustomItemList.eM_Ultimate_Containment_Field.getBlock(), 14));
        return StructureDefinition.<GT_MetaTileEntity_Crystalizer>builder()
            .addShape("main", STRUCTURE)
            .addElement(
                'H', // basement, tungsten steel
                GT_HatchElementBuilder.<GT_MetaTileEntity_Crystalizer>builder()
                    .atLeast(
                        GT_HatchElement.Maintenance,
                        GT_HatchElement.InputBus,
                        GT_HatchElement.OutputBus,
                        GT_HatchElement.InputHatch,
                        GT_HatchElement.OutputHatch,
                        GT_HatchElement.Energy)
                    .dot(1)
                    .casingIndex(((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings4).getTextureIndex(0))
                    .buildAndChain(GregTech_API.sBlockCasings4, 0))
            .addElement('G', StructureUtility.ofBlock(GregTech_API.sBlockCasings4, 0))
            .addElement(
                'B',
                StructureUtility.ofBlock(
                    Block.getBlockFromItem(
                        ALLOY.HELICOPTER.getFrameBox(1)
                            .getItem()),
                    0)) // framebox
            .addElement( // glass
                'A',
                StructureUtility.withChannel(
                    "glass",
                    BorosilicateGlass.ofBoroGlass(
                        (byte) -1,
                        GT_MetaTileEntity_Crystalizer::setGlassTier,
                        GT_MetaTileEntity_Crystalizer::getGlassTier)))
            .addElement('F', StructureUtility.ofBlock(Block.getBlockFromItem(ItemList.Casing_Vent.getItem()), 11))
            .addElement('E', StructureUtility.ofBlock(Block.getBlockFromItem(ItemList.Casing_AcidHazard.getItem()), 6))
            .addElement(
                'C',
                StructureUtility.withChannel(
                    "field_surround",
                    StructureUtility.ofBlocksTiered(
                        GT_MetaTileEntity_Crystalizer::getFieldGeneratorTier,
                        containment_field_blocks,
                        -1,
                        (te, tier) -> {
                            te.fleldGeneratorTier = tier;
                            te.maxActiveTicks = (7200 + (7 - tier) * 3600) * 20;
                        },
                        (te) -> te.fleldGeneratorTier)))
            .addElement(
                'D',
                StructureUtility.withChannel(
                    "field_center",
                    StructureUtility.ofBlocksTiered(
                        GT_MetaTileEntity_Crystalizer::getFieldGeneratorTier,
                        containment_field_blocks,
                        -1,
                        (te, tier) -> { te.fleldGeneratorTier2 = tier; },
                        (te) -> te.fleldGeneratorTier2)))
            .build();
    }

    public GT_MetaTileEntity_Crystalizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Crystalizer(String aName) {
        super(aName);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece("main", stackSize, 4, 10, 0, elementBudget, env, true);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 4, 10, 0);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_Crystalizer> getStructureDefinition() {
        if (DEF == null) DEF = initializeDefinition();
        return DEF;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Crystalizer")
            .addInfo("Controller block for Large Crystalizer. Brought by Twist Space Technology.")
            .addInfo(
                "Creating crystals with the power of force field, which can produce lapotron and energy chips with far lower cost.")
            .addInfo(
                "It can process 16 items at a time, upgrading Field Containment casing can reduce power cost and recipe time.")
            .addInfo(
                "The Field Containment block has 2 types, center and surrounding. The tier of center block must be higher than surrounding block.")
            .addInfo(
                "The machine is sensitive to pollution. When the pollution is greater than 500000, it will refuse to work.")
            .addInfo(
                "Putting it into clean-room can protect it from heavy pollution if you can't find a clean place for this machine.")
            .addInfo(
                "All recipes are based on chances, putting the machine on space station can obtain 10% more chance of success.")
            .addInfo(
                "The efficiency of the machine grows as it runs. It will take at most 9 hours to get full efficiency.")
            .addInfo("With full efficiency, extra 30% of output is produced and the chance of recipes become 100%.")
            .addInfo("When using containment casing of higher tier, it will cost less time to become 100% efficient.")
            .addInfo("Right click controller with a screwdriver to enable certainty mode.")
            .addInfo("Can process recipes without chance with lower speed. Upgrading field casing can reduce penalty.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .addStructureInfo("This structure is too complex! See schematic for details.")
            .toolTipFinisher(
                EnumChatFormatting.AQUA + "koiNoCirculation"
                    + EnumChatFormatting.GRAY
                    + " via "
                    + EnumChatFormatting.GREEN
                    + "GregTech");

        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Crystalizer(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)),
                TextureFactory.builder()
                    .addIcon(active ? CRYSTALIZER_FRONT_OVERLAY_ACTIVE : CRYSTALIZER_FRONT_OVERLAY_INACTIVE)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)) };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        boolean checkPiece = checkPiece("main", 4, 10, 0);
        return checkPiece && fleldGeneratorTier2 > fleldGeneratorTier;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new CrystalizerLogic();
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
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
    public void setCleanroom(ICleanroom cleanroom) {
        this.cleanroom = cleanroom;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isActive()) {
            activeTicks = Math.min(activeTicks + 1, maxActiveTicks);
        } else {
            activeTicks = Math.max(activeTicks - maxActiveTicks / 6000, 0);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("activeTicks", activeTicks);
        aNBT.setInteger("maxActiveTicks", maxActiveTicks);
        aNBT.setBoolean("enableChance", enableChance);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        activeTicks = aNBT.getInteger("activeTicks");
        maxActiveTicks = aNBT.getInteger("maxActiveTicks");
        enableChance = aNBT.getBoolean("enableChance");
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : getExoticAndNormalEnergyHatchList()) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        long voltage = getAverageInputVoltage();
        long amps = getMaxInputAmps();

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(voltage)
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + amps
                + " A)"
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(voltage)]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 */ StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %",
            "Field Generator Tier:" + EnumChatFormatting.GREEN + fleldGeneratorTier + EnumChatFormatting.RESET,
            String.format("warmup: %3f%%", activeTicks * 100 / (float) maxActiveTicks) };
    }

    class CrystalizerLogic extends ProcessingLogic {

        @NotNull
        @Override
        protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
            int pollution = GT_Pollution.getPollution(
                getBaseMetaTileEntity().getWorld(),
                getBaseMetaTileEntity().getXCoord() >> 4,
                getBaseMetaTileEntity().getZCoord() >> 4);
            if (pollution > 500000 && (cleanroom == null || cleanroom.getCleanness() < 10000)) {
                return SimpleCheckRecipeResult.ofFailure("too_much_pollution");
            }
            if (recipe.mSpecialValue % 1000 == -101 && !GT_MetaTileEntity_BasicMachine
                .isValidForLowGravity(recipe, getBaseMetaTileEntity().getWorld().provider.dimensionId)) {
                return SimpleCheckRecipeResult.ofFailure("high_gravity");
            }
            if (recipe.mSpecialValue / 1000 > fleldGeneratorTier) {
                return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue / 1000);
            }
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }

        @NotNull
        @Override
        protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
            double speedModifier = baseSpeedModifier;
            int minForceLevel = -recipe.mSpecialValue / 1000;
            if (!enableChance) {
                speedModifier = (speedModifier * 10000.0 / recipe.getOutputChance(0))
                    * (2 - 0.2 * (fleldGeneratorTier - minForceLevel));
            } else {
                speedModifier = 1 - 0.05 * (fleldGeneratorTier - minForceLevel);
            }
            double euModifier = baseEUtModifier - 0.05 * fleldGeneratorTier;
            GT_OverclockCalculator overclockCalculator = super.createOverclockCalculator(recipe);
            return overclockCalculator.setSpeedBoost((float) (speedModifier))
                .setRecipeEUt((long) (recipe.mEUt * euModifier));
        }

        @NotNull
        @Override
        protected GT_ParallelHelper createParallelHelper(@NotNull GT_Recipe recipe) {
            return new GT_ParallelHelper() {

                @Override
                protected void calculateItemOutputs(ItemStack[] truncatedItemOutputs) {
                    if (recipe.mSpecialValue % 100 == -1) {
                        // disable bonus on initial perfect lapotron recipe
                        super.calculateItemOutputs(truncatedItemOutputs);
                        return;
                    }
                    int chanceLimit = 10500 + fleldGeneratorTier * 500;
                    int extraChance = GT_MetaTileEntity_BasicMachine
                        .isValidForLowGravity(recipe, getBaseMetaTileEntity().getWorld().provider.dimensionId) ? 1000
                            : 0;
                    if (truncatedItemOutputs.length == 0) return;
                    ArrayList<ItemStack> itemOutputsList = new ArrayList<>();
                    for (int i = 0; i < truncatedItemOutputs.length; i++) {
                        if (recipe.getOutput(i) == null) continue;
                        ItemStack origin = recipe.getOutput(i)
                            .copy();
                        final long itemStackSize = origin.stackSize;
                        double originChance = recipe.getOutputChance(i) + extraChance;
                        double chance;
                        if (enableChance) {
                            chance = Math.min(
                                chanceLimit,
                                originChance + (chanceLimit - originChance) * (activeTicks / (double) maxActiveTicks));
                        } else {
                            chance = 10000;
                        }
                        double chancedOutputMultiplier = calculateChancedOutputMultiplier(
                            (int) (Math.min(chance, 10000) * chanceMultiplier),
                            currentParallel);
                        long items = (long) Math.ceil(itemStackSize * chancedOutputMultiplier);
                        addItemsLong(itemOutputsList, origin, items);
                        if (chance > 10000) {
                            // extra output
                            chancedOutputMultiplier = calculateChancedOutputMultiplier(
                                (int) ((chance - 10000) * chanceMultiplier),
                                currentParallel);
                            items = (long) Math.ceil(itemStackSize * chancedOutputMultiplier);
                            addItemsLong(itemOutputsList, origin, items);
                        }
                    }
                    itemOutputs = itemOutputsList.toArray(new ItemStack[0]);
                }
            }.setRecipe(recipe)
                .setItemInputs(inputItems)
                .setFluidInputs(inputFluids)
                .setAvailableEUt(availableVoltage * availableAmperage)
                .setMachine(machine, protectItems, protectFluids)
                .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
                .setMaxParallel(16)
                .enableBatchMode(batchSize)
                .setConsumption(true)
                .setOutputCalculation(true);
        }
    }
}
