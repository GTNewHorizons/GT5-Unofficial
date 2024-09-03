package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER_GLOW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTESuperBuffer extends MTEChestBuffer {

    public MTESuperBuffer(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            257,
            new String[] { "Buffers up to 256 Item Stacks", "Use Screwdriver to regulate output stack size",
                getTickRateDesc(aTier) });
    }

    public MTESuperBuffer(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTESuperBuffer(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperBuffer(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_SUPERBUFFER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_SUPERBUFFER_GLOW)
                .glow()
                .build());
    }

    @Override
    protected void fillStacksIntoFirstSlots() {
        // no order, this is super buffer
        HashMap<GTUtility.ItemId, Integer> slots = new HashMap<>(mInventory.length);
        HashMap<GTUtility.ItemId, ItemStack> stacks = new HashMap<>(mInventory.length);
        List<Integer> validSlots = new ArrayList<>(mInventory.length);
        // List<String> order = new ArrayList<>(mInventory.length);
        for (int i = 0; i < mInventory.length - 1; i++) {
            if (!isValidSlot(i)) continue;
            validSlots.add(i);
            ItemStack s = mInventory[i];
            if (s == null) continue;
            GTUtility.ItemId sID = GTUtility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if (!stacks.containsKey(sID)) stacks.put(sID, s);
            // order.add(sID);
            mInventory[i] = null;
        }
        int i = 0;
        for (Map.Entry<GTUtility.ItemId, Integer> entry : slots.entrySet()) {
            do {
                int slot = validSlots.get(i);
                mInventory[slot] = stacks.get(entry.getKey())
                    .copy();
                int toSet = Math.min(entry.getValue(), mInventory[slot].getMaxStackSize());
                mInventory[slot].stackSize = toSet;
                entry.setValue(entry.getValue() - toSet);
                i++;
            } while (entry.getValue() > 0);
        }
    }

    @Override
    protected void addMainUI(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SUPER_BUFFER)
                .setPos(61, 4)
                .setSize(54, 54));
    }
}
