package goodgenerator.blocks.tileEntity;

import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION2;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION2_GLOW;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;

import goodgenerator.blocks.tileEntity.base.LargeFusionComputer;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.power.FusionPower;

public class LargeFusionComputer2 extends LargeFusionComputer {

    private static final ITexture textureOverlay = TextureFactory.of(
            TextureFactory.builder().addIcon(OVERLAY_FUSION2).extFacing().build(),
            TextureFactory.builder().addIcon(OVERLAY_FUSION2_GLOW).extFacing().glow().build());

    public LargeFusionComputer2(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
        power = new FusionPower((byte) 7, 320_000_000);
    }

    public LargeFusionComputer2(String name) {
        super(name);
        power = new FusionPower((byte) 7, 320_000_000);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Reactor").addInfo("Millions of nuclear.")
                .addInfo("Controller block for the Compact Fusion Reactor MK-II.")
                .addInfo("524,288EU/t and 10M EU capacity per Energy Hatch")
                .addInfo("If the recipe has a startup cost greater than the")
                .addInfo("number of energy hatches * cap, you can't do it")
                .addInfo("Make sure the whole structure is built in the 3x3")
                .addInfo("chunk area of the ring center (not controller).")
                .addInfo("Startup < 160,000,000 EU: 128x Parallel").addInfo("Startup < 320,000,000 EU: 64x Parallel")
                .addInfo(
                        "Support" + EnumChatFormatting.BLUE
                                + " Tec"
                                + EnumChatFormatting.DARK_BLUE
                                + "Tech"
                                + EnumChatFormatting.GRAY
                                + " Energy/Laser Hatches!")
                .addInfo("The structure is too complex!").addInfo(BLUE_PRINT_INFO).addSeparator()
                .addCasingInfo("Fusion Machine Casing", 1664).addCasingInfo("Compact Fusion Coil", 560)
                .addCasingInfo("Duranium Frame Box", 128)
                .addCasingInfo("Iridium Reinforced Borosilicate Glass Block", 63)
                .addEnergyHatch("1-32, Hint block with dot 2", 2).addInputHatch("1-16, Hint block with dot 1", 1)
                .addOutputHatch("1-16, Hint block with dot 1", 1)
                .addStructureInfo(
                        "ALL Hatches must be " + GT_Utility.getColoredTierNameFromTier((byte) hatchTier())
                                + EnumChatFormatting.GRAY
                                + " or better")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public long maxEUStore() {
        return 320006000L * (Math.min(32, this.mEnergyHatches.size() + this.eEnergyMulti.size())) / 32L;
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 6;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 1;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 4;
    }

    @Override
    public int hatchTier() {
        return 7;
    }

    @Override
    public Materials getFrameBox() {
        return Materials.Duranium;
    }

    @Override
    public int getMaxPara() {
        return 64;
    }

    @Override
    public int extraPara(int startEnergy) {
        return startEnergy < 160000000 ? 2 : 1;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public int tierOverclock() {
        return 2;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("LargeFusion2.hint", 9);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeFusionComputer2(mName);
    }
}
