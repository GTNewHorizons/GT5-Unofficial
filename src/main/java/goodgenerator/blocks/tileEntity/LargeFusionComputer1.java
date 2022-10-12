package goodgenerator.blocks.tileEntity;

import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;

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
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class LargeFusionComputer1 extends LargeFusionComputer {

    private static final ITexture textureOverlay = TextureFactory.of(
            TextureFactory.builder().addIcon(OVERLAY_FUSION1).extFacing().build(),
            TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build());

    public LargeFusionComputer1(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public LargeFusionComputer1(String name) {
        super(name);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fusion Reactor")
                .addInfo("Millions of nuclear.")
                .addInfo("Controller block for the Compact Fusion Reactor MK-I Prototype.")
                .addInfo("131,072EU/t and 5M EU capacity per Energy Hatch")
                .addInfo("If the recipe has a startup cost greater than the")
                .addInfo("number of energy hatches * cap, you can't do it")
                .addInfo("Make sure the whole structure is built in the 3x3")
                .addInfo("chuck area of the ring center (not controller).")
                .addInfo("It can run 64x recipes at most.")
                .addInfo("Support" + EnumChatFormatting.BLUE + " Tec" + EnumChatFormatting.DARK_BLUE + "Tech"
                        + EnumChatFormatting.GRAY + " Energy/Laser Hatches!")
                .addInfo("The structure is too complex!")
                .addInfo(BLUE_PRINT_INFO)
                .addSeparator()
                .addCasingInfo("LuV Machine Casing", 1664)
                .addCasingInfo("Ameliorated Superconduct Coil", 560)
                .addCasingInfo("Naquadah Alloy Frame Boxes", 128)
                .addCasingInfo("Rhodium-Plated Palladium Reinforced Borosilicate Glass Block", 63)
                .addEnergyHatch("1-32, Hint block with dot 3", 3)
                .addInputHatch("2-16, Hint block with dot 1", 1)
                .addOutputHatch("1-16, Hint block with dot 2", 2)
                .addStructureInfo("ALL Hatches must be LuV or better")
                .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    public long maxEUStore() {
        return 160008000L * (Math.min(32, this.mEnergyHatches.size() + this.eEnergyMulti.size())) / 32L;
    }

    @Override
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings1;
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
        return 0;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 3;
    }

    @Override
    public int hatchTier() {
        return 6;
    }

    @Override
    public Materials getFrameBox() {
        return Materials.NaquadahAlloy;
    }

    @Override
    public int getMaxPara() {
        return 64;
    }

    @Override
    public int extraPara(int startEnergy) {
        return 1;
    }

    @Override
    public ITexture getTextureOverlay() {
        return textureOverlay;
    }

    @Override
    public int tierOverclock() {
        return 1;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("LargeFusion1.hint", 9);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeFusionComputer1(mName);
    }
}
