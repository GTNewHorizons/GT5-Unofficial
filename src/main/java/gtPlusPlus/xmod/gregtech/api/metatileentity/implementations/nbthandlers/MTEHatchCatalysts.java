package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;

public class MTEHatchCatalysts extends MTEHatchNbtConsumable {

    public MTEHatchCatalysts(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0, 16, "Dedicated Catalyst Storage", false);
    }

    public MTEHatchCatalysts(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 0, 16, aDescription, false, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_Bus_Catalyst)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, GTPPCore.GT_Tooltip.get());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCatalysts(mName, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isItemValidForInputSlot(ItemStack aStack) {
        return MTEChemicalPlant.isCatalyst(aStack);
    }

    @Override
    public int getInputSlotCount() {
        return 16;
    }
}
