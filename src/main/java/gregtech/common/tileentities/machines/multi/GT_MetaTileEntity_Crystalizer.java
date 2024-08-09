package gregtech.common.tileentities.machines.multi;

import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.*;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.github.technus.tectech.thing.CustomItemList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gtPlusPlus.core.material.ALLOY;

/**
 * 晶胞发生器
 * 类型：高压釜
 * 阶段: LuV
 * 使用力场科技从溶液中析出结晶！
 * 由Twist Spece Technology出口而来
 * 比单方块快300%！
 * 初始提供64并行，无法超频。
 * 每提升一级力场方块，获得额外的64并行
 * UHV力场方块解锁有损超频，此时不再额外获得并行
 * 该机器提供有限的超净间和微重力环境
 * 机器所属区块污染大于150万时，配方成功率将下降为90%，此后每提升10万污染，下降1%，最多下降40%，每提升10w污染，加速会下降20%，最多下降300%。
 * 将机器置入超净间可以免疫污染并获得额外的100%加速，超净间的洁净度必须锁定在100%水平才能免疫污染影响。
 * 机器位于低重力环境下时，有25%的概率获得额外一份产物并再次加速50%
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

    private int fleldGeneratorTier;

    private byte glassTier = -1;

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

    public static int getFleldGeneratorTier(GT_MetaTileEntity_Crystalizer te) {
        return te.fleldGeneratorTier;
    }

    public static void setFleldGeneratorTier(GT_MetaTileEntity_Crystalizer te, byte tier) {
        te.glassTier = tier;
    }

    public static byte getGlassTier(GT_MetaTileEntity_Crystalizer te) {
        return te.glassTier;
    }

    public static void setGlassTier(GT_MetaTileEntity_Crystalizer te, byte tier) {
        te.glassTier = tier;
    }

    public static IStructureDefinition<GT_MetaTileEntity_Crystalizer> initializeDefinition() {
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
                        GT_HatchElement.OutputHatch)
                    .adder(GT_MetaTileEntity_Crystalizer::addToMachineList)
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
            .addElement('C', StructureUtility.ofBlock(ItemList.Casing_ContainmentField.getBlock(), 8))
            .addElement('A', StructureUtility.ofBlock(CustomItemList.eM_Containment_Field.getBlock(), 6))
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
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece("main", 4, 10, 0);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

        };
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
}
