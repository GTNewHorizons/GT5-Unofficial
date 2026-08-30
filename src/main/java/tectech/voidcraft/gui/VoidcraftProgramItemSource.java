package tectech.voidcraft.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;

import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftStats;
import tectech.voidcraft.uss.USSBlueprintProgram;
import tectech.voidcraft.uss.USSCapabilities;
import tectech.voidcraft.uss.USSCommand;

/**
 * {@link VoidcraftProgramSource} for a digitized blueprint item (ship / base) in the player inventory: the
 * program is loaded from the item NBT at open; every accepted edit is applied on the server and written back
 * to the item in its inventory slot (the slot stack is replaced and the updated slot pushed to the client).
 *
 * <p>
 * The capability set is DERIVED from the item itself (the capability system): a digitized SHIP item carries its
 * exact denormalized stats (speed / mining / scan / siphon / construction / logistics power), so the editor
 * offers exactly the commands that ship can run; a base item offers everything a voidcraft runs EXCEPT MOVE —
 * MINE / SCAN / SIPHON / CONSTRUCT / SEND / TAKE (its stats) + REPAIR (a repair bay in the blueprint) — and
 * never the MOVE capability (a base cannot move).
 */
public class VoidcraftProgramItemSource implements VoidcraftProgramSource {

    private final PlayerInventoryGuiData data;
    private final Item item;
    private final USSBlueprintProgram store;
    private final USSCapabilities caps;
    private final long miningPower;
    private final long scanPower;
    private final long siphonPower;
    private final long constructionPower;
    private final long logisticsPower;
    private final int repairBays;
    private final double speed;

    /**
     * @param data the GUI data (carries the player and the slot index of the blueprint item)
     * @param item the blueprint item instance (ship / base) — used to verify the slot still holds it
     */
    public VoidcraftProgramItemSource(PlayerInventoryGuiData data, Item item) {
        this.data = data;
        this.item = item;
        ItemStack stack = data.getUsedItemStack();
        NBTTagCompound nbt = stack == null ? null : stack.getTagCompound();
        this.caps = deriveCaps(item, stack);
        this.store = new USSBlueprintProgram(nbt, caps);
        // The stat lines (tooltips) are derived from the same source as the caps — read once here (the item's
        // stats never change while the GUI is open).
        if (item instanceof ItemVoidcraft) {
            this.speed = VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_SPEED);
            this.miningPower = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MINING);
            this.scanPower = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_SCAN);
            this.siphonPower = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_STARLIFTER);
            this.constructionPower = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_CONSTRUCTION);
            this.logisticsPower = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_LOGISTICS);
            this.repairBays = 0;
        } else {
            this.speed = 0.0; // a base cannot move
            long mining = 0L;
            long scan = 0L;
            long siphon = 0L;
            long construction = 0L;
            long logistics = 0L;
            int bays = 0;
            VoidcraftBlueprint blueprint = stack == null ? null : ItemVoidbaseBlueprint.getBlueprint(stack);
            if (blueprint != null) {
                VoidcraftStats stats = blueprint.computeStats();
                mining = stats.miningPower;
                scan = stats.scanPower;
                siphon = stats.starlifterPower;
                construction = stats.constructionPower;
                logistics = stats.logisticsPower;
                bays = blueprint.countCover(VoidcraftCoverComponent.REPAIR_BAY);
            }
            this.miningPower = mining;
            this.scanPower = scan;
            this.siphonPower = siphon;
            this.constructionPower = construction;
            this.logisticsPower = logistics;
            this.repairBays = bays;
        }
    }

    /** The capability set of the blueprint item (ship stats / base stats minus MOVE). */
    private static USSCapabilities deriveCaps(Item item, ItemStack stack) {
        NBTTagCompound nbt = stack == null ? null : stack.getTagCompound();
        int bits = 0;
        if (item instanceof ItemVoidcraft) {
            if (VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_SPEED) > 0.0) {
                bits |= USSCapabilities.MOVE;
            }
            if (VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MINING) > 0L) {
                bits |= USSCapabilities.MINE;
            }
            if (VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_SCAN) > 0L) {
                bits |= USSCapabilities.SCAN;
            }
            if (VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_STARLIFTER) > 0L) {
                bits |= USSCapabilities.SIPHON;
            }
            if (VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_CONSTRUCTION) > 0L) {
                bits |= USSCapabilities.CONSTRUCT;
            }
            if (VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_LOGISTICS) > 0L) {
                bits |= USSCapabilities.LOGISTICS;
            }
            // a ship never repairs (a REPAIR on a ship SKIPs)
        } else if (item instanceof ItemVoidbaseBlueprint && stack != null) {
            VoidcraftBlueprint blueprint = ItemVoidbaseBlueprint.getBlueprint(stack);
            if (blueprint != null) {
                VoidcraftStats stats = blueprint.computeStats();
                if (stats.miningPower > 0L) {
                    bits |= USSCapabilities.MINE;
                }
                if (stats.scanPower > 0L) {
                    bits |= USSCapabilities.SCAN;
                }
                if (stats.starlifterPower > 0L) {
                    bits |= USSCapabilities.SIPHON;
                }
                if (stats.constructionPower > 0L) {
                    bits |= USSCapabilities.CONSTRUCT;
                }
                if (stats.logisticsPower > 0L) {
                    bits |= USSCapabilities.LOGISTICS;
                }
                if (blueprint.countCover(VoidcraftCoverComponent.REPAIR_BAY) > 0) {
                    bits |= USSCapabilities.REPAIR;
                }
            }
        }
        return USSCapabilities.of(bits);
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

    @Override
    public USSCapabilities getCommandCaps() {
        return caps;
    }

    @Override
    public String getCommandStatLine(int commandId) {
        switch (commandId) {
            case USSCommand.MOVE:
                return speed > 0.0 ? "Speed: " + formatSpeed(speed) : "";
            case USSCommand.MINE:
                return miningPower > 0L ? "Mining power: " + miningPower : "";
            case USSCommand.SCAN:
                return scanPower > 0L ? "Scan power: " + scanPower : "";
            case USSCommand.SIPHON:
                return siphonPower > 0L ? "Siphon power: " + siphonPower : "";
            case USSCommand.CONSTRUCT:
                return constructionPower > 0L ? "Construction power: " + constructionPower : "";
            case USSCommand.REPAIR:
                return repairBays > 0 ? "Repair bays: " + repairBays : "";
            case USSCommand.SEND:
            case USSCommand.TAKE:
                return logisticsPower > 0L ? "Logistics power: " + logisticsPower : "";
            default:
                return "";
        }
    }

    private static String formatSpeed(double speed) {
        if (speed == Math.floor(speed)) {
            return String.valueOf((long) speed);
        }
        return String.format("%.1f", speed);
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
