package goodgenerator.blocks.tileEntity;

import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION3_GLOW;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputer;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTELargeFusionComputer3 extends MTELargeFusionComputer {

    private static final ITexture textureOverlay = TextureFactory.of(
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION3)
            .extFacing()
            .build(),
        TextureFactory.builder()
            .addIcon(OVERLAY_FUSION3_GLOW)
            .extFacing()
            .glow()
            .build());

    public MTELargeFusionComputer3(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELargeFusionComputer3(String name) {
        super(name);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fusion Reactor")
            .addInfo("Millions of nuclear.")
            .addInfo("Controller block for the Compact Fusion Reactor MK-III.")
            .addInfo(
                EnumChatFormatting.AQUA + GTUtility.formatNumbers(getSingleHatchPower())
                    + EnumChatFormatting.GRAY
                    + " EU/t and "
                    + EnumChatFormatting.AQUA
                    + GTUtility.formatNumbers(capableStartupCanonical() / 32 / M)
                    + "M"
                    + EnumChatFormatting.GRAY
                    + " EU capacity per Energy Hatch")
            .addInfo("If the recipe has a startup cost greater than the")
            .addInfo("number of energy hatches * cap, you can't do it")
            .addInfo(
                "If the recipe requires a voltage tier over " + GTUtility.getColoredTierNameFromTier((byte) tier())
                    + EnumChatFormatting.GRAY
                    + " , you can't do it either")
            .addInfo("Make sure the whole structure is built in the 3x3")
            .addInfo("chunk area of the ring center (not controller).")
            .addInfo("Startup < 160,000,000 EU: 192x Parallel")
            .addInfo("Startup < 320,000,000 EU: 128x Parallel")
            .addInfo("Startup >= 320,000,000 EU: 64x Parallel")
            .addInfo(
                "Support" + EnumChatFormatting.BLUE
                    + " Tec"
                    + EnumChatFormatting.DARK_BLUE
                    + "Tech"
                    + EnumChatFormatting.GRAY
                    + " Energy/Laser Hatches!")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .addCasingInfo("Fusion Machine Casing MK II", 1664)
            .addCasingInfo("Advanced Compact Fusion Coil", 560)
            .addCasingInfo("Neutronium Frame Box", 128)
            .addCasingInfo("Osmium Reinforced Borosilicate Glass Block", 63)
            .addEnergyHatch("1-32, Hint block with dot 2", 2)
            .addInputHatch("1-16, Hint block with dot 1", 1)
            .addOutputHatch("1-16, Hint block with dot 1", 1)
            .addStructureInfo("Supports Crafting Input Buffer")
            .addStructureInfo(
                "ALL Hatches must be " + GTUtility.getColoredTierNameFromTier((byte) hatchTier())
                    + EnumChatFormatting.GRAY
                    + " or better")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public int tier() {
        return 8;
    }

    @Override
    public long capableStartupCanonical() {
        return 640_000_000;
    }

    @Override
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public int getCasingMeta() {
        return 8;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 2;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 5;
    }

    @Override
    public int hatchTier() {
        return 8;
    }

    @Override
    public Materials getFrameBox() {
        return Materials.Neutronium;
    }

    @Override
    public int getMaxPara() {
        return 64;
    }

    @Override
    public int extraPara(int startEnergy) {
        return (startEnergy < 160000000 ? 3 : (startEnergy < 320000000 ? 2 : 1));
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("LargeFusion3.hint", 9);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFusionComputer3(mName);
    }
}
