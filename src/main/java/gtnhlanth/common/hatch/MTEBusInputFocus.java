package gtnhlanth.common.hatch;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEBusInputFocusGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchNbtConsumable;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtnhlanth.common.item.ICanFocus;

public class MTEBusInputFocus extends MTEHatchNbtConsumable {

    private static final int INPUT_SLOTS = 64;

    private Item currentFocus;

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
    public boolean areUsageStacksUnique() {
        return false;
    }

    @Override
    protected void validateUsageSlots() {
        // Reset the current focus each tick
        currentFocus = null;

        // Loop through the inventory to find the first focus in the right group of slots
        for (int i = inputSlotCount; i < totalSlotCount; i++) {
            ItemStack slot = mInventory[i];

            if (slot != null) {
                currentFocus = slot.getItem();
                break;
            }
        }

        super.validateUsageSlots();
    }

    @Override
    public boolean isItemValidForUsageSlot(ItemStack aStack) {
        if (currentFocus != null) {
            return currentFocus == aStack.getItem();
        }

        if (aStack.getItem() instanceof ICanFocus) {
            // Awful hack so that you can't insert different types of foci in the same tick
            // This will be corrected in the next tick if this item is never actually inserted
            currentFocus = aStack.getItem();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBusInputFocusGui(this).build(data, syncManager, uiSettings);
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
