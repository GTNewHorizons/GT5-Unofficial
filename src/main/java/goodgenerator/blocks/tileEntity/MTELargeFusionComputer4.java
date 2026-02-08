package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputerPP;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTELargeFusionComputer4 extends MTELargeFusionComputerPP {

    public MTELargeFusionComputer4(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELargeFusionComputer4(String name) {
        super(name);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fusion Reactor")
            .addInfo("Power Overwhelming!!!")
            .addInfo(
                EnumChatFormatting.AQUA + formatNumber(getSingleHatchPower())
                    + EnumChatFormatting.GRAY
                    + " EU/t and "
                    + EnumChatFormatting.AQUA
                    + formatNumber(capableStartupCanonical() / 32 / M)
                    + "M"
                    + EnumChatFormatting.GRAY
                    + " EU capacity per Energy Hatch")
            .addInfo("If the recipe has a startup cost greater than the")
            .addInfo("number of energy hatches * cap, you can't do it")
            .addInfo(
                "If the recipe requires a voltage tier over " + GTUtility.getColoredTierNameFromTier((byte) tier())
                    + EnumChatFormatting.GRAY
                    + " , you can't do it either")
            .addInfo("Performs 4/4 overclock")
            .addInfo(createParallelText())
            .addTecTechHatchInfo()
            .addCasingInfoMin("Fusion Machine Casing MK III", 1664, false)
            .addCasingInfoMin("Compact Fusion Coil MK-II Prototype", 560, false)
            .addCasingInfoMin("Infinity Catalyst Frame Box", 128, false)
            .addCasingInfoMin("Neutronium Reinforced Borosilicate Glass Block", 63, false)
            .addEnergyHatch("1-32, Hint Block Number 2", 2)
            .addInputHatch("1-16, Hint Block Number 1", 1)
            .addOutputHatch("1-16, Hint Block Number 1", 1)
            .addStructureInfo("Supports Crafting Input Buffer")
            .addStructureInfo(
                "Energy Hatches must be " + GTUtility.getColoredTierNameFromTier((byte) energyHatchTier())
                    + EnumChatFormatting.GRAY
                    + " or better")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public int tier() {
        return 9;
    }

    @Override
    public long capableStartupCanonical() {
        return 5_120_000_000L;
    }

    @Override
    public Block getCasingBlock() {
        return ModBlocks.blockCasings3Misc;
    }

    @Override
    public int getCasingMeta() {
        return 12;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 3;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 13;
    }

    @Override
    public int energyHatchTier() {
        return 9;
    }

    @Override
    public Materials getFrameBox() {
        return Materials.InfinityCatalyst;
    }

    @Override
    public ITexture getTextureOverlay() {
        if (this.mMaxProgresstime > 0) return TextureFactory.of(
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Casing_Machine_Screen_3)
                .extFacing()
                .build());
        else return TextureFactory.of(
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Casing_Machine_Screen_1)
                .extFacing()
                .build());
    }

    @Override
    public int getMaxPara() {
        return 64;
    }

    @Override
    public int extraPara(long startEnergy) {
        if (startEnergy < 160000000L) return 4;
        if (startEnergy < 320000000L) return 3;
        if (startEnergy < 640000000L) return 2;
        return 1;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return DescTextLocalization.addText("LargeFusion4.hint", 9);
    }

    @Override
    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.eEnergyMulti != null) {
            for (MTEHatchEnergyMulti hatch : this.eEnergyMulti) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mOutputHatches != null) {
            for (MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        if (this.mDualInputHatches != null) {
            for (IDualInputHatch hatch : this.mDualInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(2, 14) : 53);
            }
        }
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) return new ITexture[] { TextureFactory.builder()
            .addIcon(MACHINE_CASING_FUSION_GLASS)
            .extFacing()
            .build(), getTextureOverlay() };
        if (!aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(52) };
        return new ITexture[] { TextureFactory.builder()
            .addIcon(TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA)
            .extFacing()
            .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFusionComputer4(mName);
    }
}
