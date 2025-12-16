package gtnhlanth.common.hatch;

import java.util.ArrayList;

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

    private static final int INPUT_SLOTS = 256;

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
        ArrayList<ItemStack> usageSlots = this.getContentUsageSlots();

        if (usageSlots.isEmpty()) {
            return aStack.getItem() instanceof ICanFocus;
        }

        return aStack.getItem() == usageSlots.get(0)
            .getItem();
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBusInputFocusGui(this).build(data, syncManager, uiSettings);
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
