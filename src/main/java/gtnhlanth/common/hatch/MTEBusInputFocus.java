package gtnhlanth.common.hatch;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchNbtConsumable;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtnhlanth.common.item.ICanFocus;
import gtnhlanth.util.Util;

public class MTEBusInputFocus extends MTEHatchNbtConsumable {

    private static final int INPUT_SLOTS = 4;

    public MTEBusInputFocus(int id, String name, String nameRegional) {
        super(id, name, nameRegional, 0, INPUT_SLOTS, "Input Bus for Foci", false);
    }

    public MTEBusInputFocus(String name, String[] descriptionArray, ITexture[][][] textures) {
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
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(TexturesGtBlock.Overlay_Bus_Catalyst) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, new GTRenderedTexture(TexturesGtBlock.Overlay_Bus_Catalyst) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBusInputFocus(mName, mDescriptionArray, mTextures);
    }

}
