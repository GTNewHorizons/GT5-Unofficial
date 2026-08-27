package tectech.voidcraft.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;

import tectech.voidcraft.uss.USSBlueprintProgram;

/**
 * {@link VoidcraftProgramSource} for a digitized blueprint item (ship / base) in the player inventory: the
 * program is loaded from the item NBT at open; every accepted edit is applied on the server and written back
 * to the item in its inventory slot (the slot stack is replaced and the updated slot pushed to the client).
 */
public class VoidcraftProgramItemSource implements VoidcraftProgramSource {

    private final PlayerInventoryGuiData data;
    private final Item item;
    private final USSBlueprintProgram store;

    /**
     * @param data the GUI data (carries the player and the slot index of the blueprint item)
     * @param item the blueprint item instance (ship / base) — used to verify the slot still holds it
     */
    public VoidcraftProgramItemSource(PlayerInventoryGuiData data, Item item) {
        this.data = data;
        this.item = item;
        this.store = new USSBlueprintProgram(stackNbt(data.getUsedItemStack()));
    }

    private static NBTTagCompound stackNbt(ItemStack stack) {
        return stack == null ? null : stack.getTagCompound();
    }

    @Override
    public List<String> getProgramRows() {
        return store.getProgramRows();
    }

    @Override
    public String getNote() {
        return store.getNote();
    }

    @Override
    public void applyAction(String actionJson) {
        if (data.isClient()) {
            return; // server authoritative — actions only ever run here
        }
        if (store.applyAction(actionJson).ok) {
            writeBack();
        }
    }

    /**
     * Write the edited program back into the blueprint item in its inventory slot. Skipped when the item is no
     * longer in the slot (the GUI was left open while the item was dropped / moved).
     */
    private void writeBack() {
        ItemStack cur = data.getUsedItemStack();
        if (cur == null || cur.getItem() != item) {
            return;
        }
        NBTTagCompound nbt = cur.getTagCompound() == null ? new NBTTagCompound()
            : (NBTTagCompound) cur.getTagCompound()
                .copy();
        USSBlueprintProgram.writeProgram(nbt, store.getProgram());
        ItemStack out = new ItemStack(cur.getItem(), 1);
        out.setTagCompound(nbt);
        data.setUsedItemStack(out);
        EntityPlayer player = data.getPlayer();
        if (player != null && player.inventoryContainer != null) {
            player.inventoryContainer.detectAndSendChanges();
        }
    }
}
