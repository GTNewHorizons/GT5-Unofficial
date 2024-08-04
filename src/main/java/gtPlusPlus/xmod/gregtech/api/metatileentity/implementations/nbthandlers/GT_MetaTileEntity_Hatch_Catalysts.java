package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_Hatch_Catalysts extends GT_MetaTileEntity_Hatch_NbtConsumable {

    public GT_MetaTileEntity_Hatch_Catalysts(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0, 16, "Dedicated Catalyst Storage", false);
    }

    public GT_MetaTileEntity_Hatch_Catalysts(String aName, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, 0, 16, aDescription, false, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Bus_Catalyst) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GT_RenderedTexture(TexturesGtBlock.Overlay_Bus_Catalyst) };
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, CORE.GT_Tooltip.get());
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Catalysts(mName, mDescriptionArray, mTextures);
    }

    @Override
    public String getNameGUI() {
        return "Catalyst Container";
    }

    @Override
    public AutoMap<ItemStack> getItemsValidForUsageSlots() {
        return new AutoMap<>();
    }

    @Override
    public boolean isItemValidForUsageSlot(ItemStack aStack) {
        return ItemUtils.isCatalyst(aStack);
    }

    @Override
    public int getInputSlotCount() {
        return 16;
    }
}
