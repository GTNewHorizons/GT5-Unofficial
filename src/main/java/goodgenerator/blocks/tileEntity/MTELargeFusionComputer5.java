package goodgenerator.blocks.tileEntity;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.blocks.tileEntity.base.MTELargeFusionComputerPP;
import goodgenerator.loader.Loaders;
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
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

@IMetaTileEntity.SkipGenerateDescription
public class MTELargeFusionComputer5 extends MTELargeFusionComputerPP {

    public MTELargeFusionComputer5(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public MTELargeFusionComputer5(String name) {
        super(name);
    }

    @Override
    public int tier() {
        return 10;
    }

    @Override
    public long capableStartupCanonical() {
        return 20_480_000_000L;
    }

    @Override
    public Block getCasingBlock() {
        return ModBlocks.blockCasings6Misc;
    }

    @Override
    public int getCasingMeta() {
        return 0;
    }

    @Override
    public Block getCoilBlock() {
        return Loaders.compactFusionCoil;
    }

    @Override
    public int getCoilMeta() {
        return 4;
    }

    @Override
    public Block getGlassBlock() {
        return ItemRegistry.bw_realglas;
    }

    @Override
    public int getGlassMeta() {
        return 7;
    }

    @Override
    public Block getGlassBlock2() {
        return ItemRegistry.bw_realglas2;
    }

    @Override
    public int getGlassMeta2() {
        return 2;
    }

    @Override
    public Materials getFrameBox() {
        return Materials.Infinity;
    }

    @Override
    public ITexture getTextureOverlay() {
        if (this.getBaseMetaTileEntity()
            .isActive())
            return TextureFactory.of(
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Casing_Machine_Screen_Rainbow)
                    .extFacing()
                    .build());
        else return TextureFactory.of(
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Casing_Machine_Screen_1)
                .extFacing()
                .build());
    }

    @Override
    public int extraPara(long startEnergy) {
        if (startEnergy < 160000000L) return 5;
        if (startEnergy < 320000000L) return 4;
        if (startEnergy < 640000000L) return 3;
        if (startEnergy < 5120000000L) return 2;
        return 1;
    }

    @Override
    public boolean turnCasingActive(boolean status) {
        if (this.mEnergyHatches != null) {
            for (MTEHatchEnergy hatch : this.mEnergyHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(3, 6) : 53);
            }
        }
        if (this.eEnergyMulti != null) {
            for (MTEHatchEnergyMulti hatch : this.eEnergyMulti) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(3, 6) : 53);
            }
        }
        if (this.mOutputHatches != null) {
            for (MTEHatchOutput hatch : this.mOutputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(3, 6) : 53);
            }
        }
        if (this.mInputHatches != null) {
            for (MTEHatchInput hatch : this.mInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(3, 6) : 53);
            }
        }
        if (this.mDualInputHatches != null) {
            for (IDualInputHatch hatch : this.mDualInputHatches) {
                hatch.updateTexture(status ? TAE.getIndexFromPage(3, 6) : 53);
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
            .addIcon(TexturesGtBlock.TEXTURE_CASING_FUSION_OVERLAY)
            .extFacing()
            .build() };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeFusionComputer5(mName);
    }
}
