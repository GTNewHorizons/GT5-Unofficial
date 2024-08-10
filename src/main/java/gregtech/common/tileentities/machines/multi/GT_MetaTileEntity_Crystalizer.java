package gregtech.common.tileentities.machines.multi;

import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.*;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
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
import gtPlusPlus.core.material.ALLOY;

/**
 * 晶胞发生器
 * 类型：高压釜
 * 阶段: LuV
 * 使用力场科技从溶液中析出结晶！
 * 由Twist Spece Technology出口而来
 * 比单方块快150%！
 * 初始提供16并行，无法超频。
 * 每提升一级力场方块，获得额外的16并行，力场方块由外围和中央两部分构成，并行由外围等级控制，中央方块等级至少需要高出外围一级.
 * 机器所属区块污染小于50万时，额外获得250%加速
 * 机器所属区块污染大于150万时，配方成功率将下降为90%，此后每提升10万污染，下降1%，最多下降40%，每提升50w污染，加速会下降20%，最多下降120%。
 * 每提升10w污染，机器能量消耗提高5%，没有上限. 将机器置入超净间可以免疫污染,超净间的洁净度必须锁定在100%水平才能免疫污染影响。
 * 机器位于低重力环境下时，有25%的概率获得额外一份产物并再次加速50%
 * 机器不产生任何污染
 * 外围机械方块升级到遏制场机械方块时，解锁激光仓+有损超频, 此时为256并行, 支持MAX+超频.
 *
 * @author koiNoCirculation
 */
public class GT_MetaTileEntity_Crystalizer extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_Crystalizer> implements ISurvivalConstructable {

    // Structure:

    // Blocks:
    // A -> ofBlock...(tile.blockLapis, 0, ...);
    // B -> ofBlock...(tile.cloth, 5, ...);
    // C -> ofBlock...(tile.cloth, 6, ...);
    // D -> ofBlock...(tile.cloth, 10, ...);
    // E -> ofBlock...(tile.glass, 0, ...);
    // F -> ofBlock...(tile.sponge, 0, ...);
    // G -> ofBlock...(tile.stone, 0, ...);

    // Tiles:

    // Special Tiles:

    // Offsets:
    // -1 11 -1

    // Normal Scan:
    private static boolean initialized;
    private int fleldGeneratorTier = -1;

    private int fleldGeneratorTier2 = -1;

    private byte glassTier = -1;

    private double baseSpeedModifier = 4.0f;

    private double pollutionSpeedInitialNerf = 2.5f;

    private double baseEUtModifier = 0.7f;

    static final int MARGIN = 1500000;

    static final int POLLUTION_INITIAL_NERF = 500000;

    static final double POLLUTION_PER_SPEED_DROP = 500000;

    static final double POLLUTION_PER_POWER_INCREASE = 100000;

    // HV 16 EV 32 IV 48 LuV 64 ZPM 72 UV 96 UHV 256

    static final int PARALLELISM_INCR = 16;

    private static List<Pair<Block, Integer>> containment_field_blocks;

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
        "   G~G   ",
        "   GGG   "
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
        "   GGG   ",
        "   GGG   "
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
        "    F    ",
        "  GGGGG  "
    },{
        "         ",
        "         ",
        "    E    ",
        "   EEE   ",
        "   EEE   ",
        "   EEE   ",
        "    E    ",
        "    F    ",
        "    F    ",
        "    F    ",
        "         ",
        " GGGGGGG "
    },{
        "         ",
        "   EEE   ",
        "  EE EE  ",
        "  E  CE  ",
        "  E C E  ",
        "  EC  E  ",
        "  EE EE  ",
        "   EEE   ",
        "   CCC   ",
        "         ",
        "         ",
        "GGGGGGGGG"
    },{
        "   EEE   ",
        "  E C E  ",
        "  EC CE  ",
        " EC    E ",
        " E     E ",
        " E    CE ",
        "  EC CE  ",
        "  E   E  ",
        "  CEEEC  ",
        "    B    ",
        "   DDD   ",
        "GGGGGGGGG"
    },{
        "   EEE   ",
        "  ECCCE  ",
        " E     E ",
        " E     E ",
        " EC A CE ",
        " E     E ",
        " E     E ",
        " FE C EF ",
        " FCEEECF ",
        " F BBB F ",
        "F  DDD  F",
        "GGGGGGGGG"
    },{
        "   EEE   ",
        "  E C E  ",
        "  EC CE  ",
        " E    CE ",
        " E     E ",
        " EC    E ",
        "  EC CE  ",
        "  E   E  ",
        "  CEEEC  ",
        "    B    ",
        "   DDD   ",
        "GGGGGGGGG"
    },{
        "         ",
        "   EEE   ",
        "  EE EE  ",
        "  EC  E  ",
        "  E C E  ",
        "  E  CE  ",
        "  EE EE  ",
        "   EEE   ",
        "   CCC   ",
        "         ",
        "         ",
        "GGGGGGGGG"
    },{
        "         ",
        "         ",
        "    E    ",
        "   EEE   ",
        "   EEE   ",
        "   EEE   ",
        "    E    ",
        "    F    ",
        "    F    ",
        "    F    ",
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
        "    F    ",
        "  GGGGG  "
    }};
    // spotless:on
    static IStructureDefinition<GT_MetaTileEntity_Crystalizer> DEF = null;

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
        GT_ModHandler.addCraftingRecipe(
            ItemList.Machine_Large_Crystalizer.get(1),
            new Object[] { "XVX", "GOG", "XGX", 'X',
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.LuV), 1), 'O',
                ItemList.Machine_EV_Autoclave.get(1), 'V', ItemList.Field_Generator_EV.get(4), 'G',
                new ItemStack(ItemBlock.getItemFromBlock(Blocks.glass), 1) });

        RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ContainmentFieldHV.get(1),
                ItemList.Casing_StableTitanium.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Aluminium, 8),
                ItemList.Field_Generator_EV.get(4),
                GT_Utility.getIntegratedCircuit(24))
            .fluidInputs(Materials.SolderingAlloy.getMolten(864))
            .itemOutputs(ItemList.Casing_ContainmentFieldEV.get(1))
            .duration(400)
            .eut(TierEU.EV)
            .addTo(RecipeMaps.assemblerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ContainmentFieldEV.get(1),
                ItemList.Casing_RobustTungstenSteel.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 16),
                ItemList.Field_Generator_IV.get(4),
                GT_Utility.getIntegratedCircuit(24))
            .fluidInputs(new FluidStack(ALLOY.NITINOL_60.getFluid(), 1296))
            .itemOutputs(ItemList.Casing_ContainmentFieldIV.get(1))
            .duration(800)
            .eut(TierEU.IV)
            .addTo(RecipeMaps.assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Casing_ContainmentField.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Casing_ContainmentField.get(1),
                ItemList.Casing_Fusion.get(1),
                ItemList.Field_Generator_ZPM.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64))
            .itemOutputs(ItemList.Casing_ContainmentFieldZPM.get(1))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(2304),
                new FluidStack(ALLOY.HELICOPTER.getFluid(), 2304),
                new FluidStack(ALLOY.PIKYONIUM.getFluid(), 1296))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Casing_ContainmentFieldZPM.get(1))
            .metadata(RESEARCH_TIME, 4 * HOURS)
            .itemInputs(
                ItemList.Casing_ContainmentFieldZPM.get(1),
                ItemList.Casing_Fusion2.get(1),
                ItemList.Field_Generator_UV.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 64))
            .itemOutputs(ItemList.Casing_ContainmentFieldUV.get(1))
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(4608),
                new FluidStack(ALLOY.HELICOPTER.getFluid(), 4608),
                new FluidStack(ALLOY.PIKYONIUM.getFluid(), 2304))
            .duration(120 * SECONDS)
            .eut((int) TierEU.RECIPE_UV)
            .addTo(AssemblyLine);
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
        return RecipeMaps.autoclaveRecipes;
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
                'G', // basement, tungsten steel
                GT_HatchElementBuilder.<GT_MetaTileEntity_Crystalizer>builder()
                    .atLeast(
                        GT_HatchElement.Maintenance,
                        GT_HatchElement.InputBus,
                        GT_HatchElement.OutputBus,
                        GT_HatchElement.InputHatch,
                        GT_HatchElement.OutputHatch,
                        GT_HatchElement.Energy.or(GT_HatchElement.ExoticEnergy))
                    .dot(1)
                    .casingIndex(((GT_Block_Casings_Abstract) GregTech_API.sBlockCasings4).getTextureIndex(0))
                    .buildAndChain(GregTech_API.sBlockCasings4, 0))
            .addElement(
                'F',
                StructureUtility.ofBlock(
                    Block.getBlockFromItem(
                        ALLOY.ARCANITE.getFrameBox(1)
                            .getItem()),
                    0)) // framebox
            .addElement( // glass
                'E',
                StructureUtility.withChannel(
                    "glass",
                    BorosilicateGlass.ofBoroGlass(
                        (byte) -1,
                        GT_MetaTileEntity_Crystalizer::setGlassTier,
                        GT_MetaTileEntity_Crystalizer::getGlassTier)))
            .addElement('B', StructureUtility.ofBlock(Block.getBlockFromItem(ItemList.Casing_Vent.getItem()), 11))
            .addElement('D', StructureUtility.ofBlock(Block.getBlockFromItem(ItemList.Casing_AcidHazard.getItem()), 6))
            .addElement(
                'C',
                StructureUtility.withChannel(
                    "field_surround",
                    StructureUtility.ofBlocksTiered(
                        GT_MetaTileEntity_Crystalizer::getFieldGeneratorTier,
                        containment_field_blocks,
                        -1,
                        (te, tier) -> { te.fleldGeneratorTier = tier; },
                        (te) -> te.fleldGeneratorTier)))
            .addElement(
                'A',
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
                "Creating crystals with the power of force field. It's 500% faster than single block autoclave and only cost 70% of energy.")
            .addInfo("Initially this machine can process 64 items at a time, but it cannot over clock.")
            .addInfo(
                "Upgrading Field Containment casing can obtain extra processing power. Machine can process 64 more items on every upgrade.")
            .addInfo(
                "Tectech containment field generator casing can unlock overclocking. And ultimate containment field generator casing can give 256 more parallelisms.")
            .addInfo(
                "Though this machine can provide cleanroom environment, do not place this machine in heavy pollution.")
            .addInfo(
                "When pollution is greater than 500000, there's 10% of chance voiding output. And it will increase 1% on every 100000 pollution.")
            .addInfo(
                "Recipe success rate can decrease to 60% due to pollution. And processing speed will drop to 200% faster. The energy cost will increase by 50%")
            .addInfo(
                "Putting it into cleanroom can protect it from pollution, the cleanness must be 100% to make it happen.")
            .addInfo("When placed in low-gravity environment, there's 25% of chance getting extra output.")
            .addInfo(
                "Support GT Energy hatches, and when tectech containment field generator is applied, it can support laser hatches.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(9, 10, 33, false)
            .addStructureInfo("This structure is too complex! See schematic for details.")
            .addOtherStructurePart("Borosilicate Glass", "Can be UV tier or higher")
            .addInputBus("Start of conveyor belt", 1)
            .addOutputBus("End of conveyor belt", 2)
            .addEnergyHatch("Second-top layer", 3)
            .addMaintenanceHatch("Around the controller", 4)
            .addInputHatch("Bottom left and right corners", 5)
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
        if (mExoticEnergyHatches.size() > 0 && fleldGeneratorTier < 7) {
            return false;
        }
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

    class CrystalizerLogic extends ProcessingLogic {

        @NotNull
        @Override
        protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
            int pollution = GT_Pollution.getPollution(
                getBaseMetaTileEntity().getWorld(),
                getBaseMetaTileEntity().getXCoord() >> 4,
                getBaseMetaTileEntity().getZCoord() >> 4);
            double speedModifier = 1 + baseSpeedModifier;
            double euModifier = baseEUtModifier;
            if (pollution > POLLUTION_INITIAL_NERF && pollution <= MARGIN) {
                speedModifier -= pollutionSpeedInitialNerf;
            } else if (pollution > MARGIN) {
                speedModifier -= Math.min(
                    speedModifier - pollutionSpeedInitialNerf
                        - 0.2 * Math.floor((pollution - MARGIN) / POLLUTION_PER_SPEED_DROP),
                    0.2);
                euModifier += 0.05 * Math.floor((pollution - MARGIN) / POLLUTION_PER_POWER_INCREASE);
            }
            GT_OverclockCalculator overclockCalculator = super.createOverclockCalculator(recipe);
            return overclockCalculator
                .limitOverclockCount(fleldGeneratorTier < 7 ? fleldGeneratorTier / 2 : Integer.MAX_VALUE)
                .setSpeedBoost((float) (1.0 / (speedModifier)))
                .setRecipeEUt((long) (recipe.mEUt * euModifier));
        }

        @NotNull
        @Override
        protected GT_ParallelHelper createParallelHelper(@NotNull GT_Recipe recipe) {
            int pollution = GT_Pollution.getPollution(
                getBaseMetaTileEntity().getWorld(),
                getBaseMetaTileEntity().getXCoord() >> 4,
                getBaseMetaTileEntity().getZCoord() >> 4);
            // float eutModifier = (float) (baseEUtModifier
            // + (pollution >= MARGIN ? 0.1 + 0.01 * Math.floor((pollution - MARGIN) / 100000.0) : 0));
            return new GT_ParallelHelper().setRecipe(recipe)
                .setItemInputs(inputItems)
                .setFluidInputs(inputFluids)
                .setAvailableEUt(availableVoltage * availableAmperage)
                .setMachine(machine, protectItems, protectFluids)
                .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
                .setMaxParallel(PARALLELISM_INCR * fleldGeneratorTier)
                // .setEUtModifier(eutModifier)
                // .setChanceMultiplier(1.0 - voidChance)
                .enableBatchMode(batchSize)
                .setConsumption(true)
                .setOutputCalculation(true);
        }

        @NotNull
        @Override
        protected CheckRecipeResult applyRecipe(@NotNull GT_Recipe recipe, @NotNull GT_ParallelHelper helper,
            @NotNull GT_OverclockCalculator calculator, @NotNull CheckRecipeResult result) {
            CheckRecipeResult r = super.applyRecipe(recipe, helper, calculator, result);
            int pollution = GT_Pollution.getPollution(
                getBaseMetaTileEntity().getWorld(),
                getBaseMetaTileEntity().getXCoord() >> 4,
                getBaseMetaTileEntity().getZCoord() >> 4);
            double voidChance = pollution >= MARGIN
                ? Math.min(0.4, 0.1 + 0.01 * Math.floor((pollution - MARGIN) / 100000.0))
                : 0;
            if (getBaseMetaTileEntity().getWorld().rand.nextDouble() < voidChance) {
                outputFluids = null;
                outputItems = null;
            }
            return r;
        }
    }
}
