package gtnhlanth.common.hatch;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchNbtConsumable;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtnhlanth.common.item.ICanFocus;

public class MTEBusInputFocus extends MTEHatchNbtConsumable {

    private static final int INPUT_SLOTS = 16;

    public MTEBusInputFocus(int id, String name, String nameRegional) {
        super(id, name, nameRegional, 0, INPUT_SLOTS, "Input Bus for Foci", true);
    }

    public MTEBusInputFocus(String name, String[] descriptionArray, ITexture[][][] textures) {
        super(name, 0, INPUT_SLOTS, descriptionArray, true, textures);
    }

    @Override
    public int getInputSlotCount() {
        return INPUT_SLOTS;
    }

    @Override
    public ArrayList<ItemStack> getItemsValidForUsageSlots() {
        return new ArrayList<>();
    }

    @Override
    public boolean isItemValidForUsageSlot(ItemStack aStack) {

        if (this.getContentUsageSlots()
            .size() < 16) {
            if (this.getContentUsageSlots()
                .isEmpty()) return aStack.getItem() instanceof ICanFocus;
            return aStack.getItem() == this.getContentUsageSlots()
                .get(0)
                .getItem();
        } else {
            return false;
        }

    }

    @Override
    public String getNameGUI() {
        return "Focus Input Bus";
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TexturesGtBlock.Overlay_Bus_Catalyst) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(TexturesGtBlock.Overlay_Bus_Catalyst) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBusInputFocus(mName, mDescriptionArray, mTextures);
    }

}
