package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIsaMill;

public class MTEHatchMillingBalls extends MTEHatchNbtConsumable {

    public MTEHatchMillingBalls(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 6, 4, "Dedicated Milling Ball Storage", false);
    }

    public MTEHatchMillingBalls(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 6, 4, aDescription, false, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TexturesGtBlock.Overlay_Bus_Milling_Balls) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TexturesGtBlock.Overlay_Bus_Milling_Balls) };
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, GTPPCore.GT_Tooltip.get());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchMillingBalls(mName, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isItemValidForInputSlot(ItemStack aStack) {
        return MTEIsaMill.isMillingBall(aStack);
    }

    @Override
    public int getInputSlotCount() {
        return 4;
    }
}
