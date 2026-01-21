package gtnhlanth.common.hatch;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSink;
import com.gtnewhorizon.gtnhlib.capability.item.ItemSource;
import com.gtnewhorizon.gtnhlib.item.ItemTransfer;
import com.gtnewhorizon.gtnhlib.util.ItemUtil;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEBusInputFocusGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchNbtConsumable;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtnhlanth.common.item.ICanFocus;
import it.unimi.dsi.fastutil.ints.IntIterators;

public class MTEBusInputFocus extends MTEHatchNbtConsumable {

    private static final int INPUT_SLOTS = 64;

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
    public void tryFillUsageSlots() {
        Item focus = null;

        // Try to find the first focus in the usage slots
        for (int i = getFirstUsageSlot(); i < getLastUsageSlot(); i++) {
            ItemStack stack = mInventory[i];

            if (ItemUtil.isStackEmpty(stack)) continue;

            focus = stack.getItem();
            break;
        }

        // No focus in the usage slots, try to find one in the input slots
        if (focus == null) {
            for (int i = getFirstInputSlot(); i < getLastInputSlot(); i++) {
                ItemStack stack = mInventory[i];

                if (ItemUtil.isStackEmpty(stack)) continue;

                focus = stack.getItem();
                break;
            }

            // No focus in the input slots either, no reason to transfer anything so we can bail here
            if (focus == null) {
                return;
            }
        }

        ItemSource source = getItemSource(ForgeDirection.UNKNOWN);
        ItemSink sink = getItemSink(ForgeDirection.UNKNOWN);

        ItemTransfer transfer = new ItemTransfer();

        transfer.source(source);
        transfer.sink(sink);

        transfer.setSourceSlots(IntIterators.unwrap(IntIterators.fromTo(getFirstInputSlot(), getLastInputSlot())));
        transfer.setSinkSlots(IntIterators.unwrap(IntIterators.fromTo(getFirstUsageSlot(), getLastUsageSlot())));

        final Item focus2 = focus;
        transfer.setFilter(stack -> stack.getItem() == focus2);

        transfer.setStacksToTransfer(getLastUsageSlot() - getFirstUsageSlot());

        transfer.transfer();
    }

    @Override
    public boolean isItemValidForInputSlot(ItemStack aStack) {
        // Always allow focuses into the input slots, regardless of what type they are
        return aStack != null && aStack.getItem() instanceof ICanFocus;
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
