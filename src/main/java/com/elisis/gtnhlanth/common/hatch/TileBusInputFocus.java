package com.elisis.gtnhlanth.common.hatch;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.elisis.gtnhlanth.common.item.ICanFocus;
import com.elisis.gtnhlanth.util.Util;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_NbtConsumable;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class TileBusInputFocus extends GT_MetaTileEntity_Hatch_NbtConsumable {

    private static final int INPUT_SLOTS = 4;

    public TileBusInputFocus(int id, String name, String nameRegional) {
        super(id, name, nameRegional, 0, INPUT_SLOTS, "Input Bus for Foci", false);
    }

    public TileBusInputFocus(String name, String[] descriptionArray, ITexture[][][] textures) {
        super(name, 0, INPUT_SLOTS, descriptionArray, false, textures);
    }

    @Override
    public int getInputSlotCount() {
        return INPUT_SLOTS;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public AutoMap<ItemStack> getItemsValidForUsageSlots() {
        return new AutoMap<>();
    }

    @Override
    public boolean isItemValidForUsageSlot(ItemStack aStack) {

        if (this.getContentUsageSlots()
            .size() == 0) {
            return aStack.getItem() instanceof ICanFocus;
        } else {
            return false;
        }

    }

    @Override
    public String getNameGUI() {
        return "Focus Input Bus";
    }

    public void depleteFocusDurability(int damage) {

        ItemStack stack = this.getContentUsageSlots()
            .toArray()[0];

        Util.depleteDurabilityOfStack(stack, damage);

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
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TileBusInputFocus(mName, mDescriptionArray, mTextures);
    }

}
