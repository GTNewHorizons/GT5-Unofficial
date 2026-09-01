package tectech.voidcraft.uss;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import tectech.voidcraft.item.ItemVoidcraftCovers;
import tectech.voidcraft.item.ItemVoidcraftInfraComponent;
import tectech.voidcraft.item.ItemVoidcraftSatellite;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftCoverRegistry;

/**
 * The item-identity boundary of the ship's cargo: maps an input-bus {@link ItemStack} to the CARGO ITEM KEY the
 * ship's hold stores it under, and back (key → stack at the delivery boundary).
 *
 * <p>
 * The hold itself stays bare JVM (key → count); this class is where the key meets Forge items. Key scheme (one
 * namespace, no validity checks on the caller side):
 *
 * <ul>
 * <li>GT material items (dust, ingot, gem, ore, ...) → the material's name (the mining cargo's convention)</li>
 * <li>Voidcraft hull parts → the blueprint parts-list keys ({@code block.<component>} / {@code cover.<cover>})</li>
 * <li>Infrastructure payloads (Power Satellite, the builder components, the UMV / UXV Field Generators) → their
 * dedicated keys ({@link USSInfra} / {@link USSConstants})</li>
 * <li>anything else → the item's unlocalized name + {@code :} + meta</li>
 * </ul>
 *
 * <p>
 * Game-side only (item registry lookups) — the gateway's launch dump, the delivery boundary
 * ({@link USSShipCargo#cargoFromHold}) and the constructor's parts resolution all go through
 * {@link #keyOf(ItemStack)} / {@link #stackOf(String, long)}.
 */
public final class USSItemCargo {

    private USSItemCargo() {
        throw new AssertionError("Static helpers");
    }

    /**
     * The cargo item key of an input stack (null for an invalid stack — the gateway simply leaves such a slot
     * untouched).
     *
     * @param stack the stack on the gateway's input side
     * @return the key the hold stores the stack under (never null for a valid stack)
     */
    public static String keyOf(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) {
            return null;
        }
        if (stack.getItem() == ItemVoidcraftSatellite.INSTANCE) {
            return USSInfra.KEY_POWER_SATELLITE;
        }
        int infraType = ItemVoidcraftInfraComponent.typeOf(stack);
        if (infraType != -1) {
            String key = USSInfra.componentKey(infraType);
            if (key != null) {
                return key;
            }
        }
        if (GTUtility.areStacksEqual(ItemList.Field_Generator_UMV.get(1L), stack)) {
            return USSConstants.FIELD_GENERATOR_UMV;
        }
        if (GTUtility.areStacksEqual(ItemList.Field_Generator_UXV.get(1L), stack)) {
            return USSConstants.FIELD_GENERATOR_UXV;
        }
        for (VoidcraftComponent component : VoidcraftComponent.values()) {
            ItemStack blockStack = VoidcraftLoader.blockItem(component);
            if (blockStack != null && GTUtility.areStacksEqual(blockStack, stack)) {
                return "block." + component.name();
            }
        }
        VoidcraftCoverComponent cover = VoidcraftCoverRegistry.byStack(stack);
        if (cover != null) {
            return "cover." + cover.name();
        }
        ItemData data = GTOreDictUnificator.getAssociation(stack);
        if (data != null && data.mMaterial != null && data.mMaterial.mMaterial != null) {
            return data.mMaterial.mMaterial.getName();
        }
        return stack.getItem()
            .getUnlocalizedNameInefficiently(stack) + ":"
            + stack.getItemDamage();
    }

    /**
     * The stack a hold item key resolves to (the delivery boundary): infrastructure payloads, the UMV / UXV Field
     * Generators, the Voidcraft hull parts ({@code block.<component>} / {@code cover.<cover>}), a GT material's
     * unified dust for a material-name key, and the registry item for a generic {@code name:meta} key.
     *
     * @param key    the hold's item key (null / empty → null)
     * @param amount the amount (≤ 0 → null)
     * @return the resolved stack, or null when the key does not resolve (the entry is dropped, not corrupted)
     */
    @Nullable
    public static ItemStack stackOf(String key, long amount) {
        if (key == null || key.isEmpty() || amount <= 0L) {
            return null;
        }
        int size = (int) Math.min(Integer.MAX_VALUE, amount);
        try {
            if (USSInfra.KEY_POWER_SATELLITE.equals(key)) {
                return ItemVoidcraftSatellite.stack(size);
            }
            if (USSInfra.KEY_INJECTOR_COMPONENT.equals(key)) {
                return sized(ItemVoidcraftInfraComponent.stack(USSInfraBuild.INJECTOR), size);
            }
            if (USSInfra.KEY_STABILIZER_COMPONENT.equals(key)) {
                return sized(ItemVoidcraftInfraComponent.stack(USSInfraBuild.STABILIZER), size);
            }
            if (USSInfra.KEY_LENS_COMPONENT.equals(key)) {
                return sized(ItemVoidcraftInfraComponent.stack(USSInfraBuild.LENS), size);
            }
            if (USSConstants.FIELD_GENERATOR_UMV.equals(key)) {
                return ItemList.Field_Generator_UMV.get(amount);
            }
            if (USSConstants.FIELD_GENERATOR_UXV.equals(key)) {
                return ItemList.Field_Generator_UXV.get(amount);
            }
            if (key.startsWith("block.")) {
                ItemStack block = VoidcraftLoader.blockItem(VoidcraftComponent.valueOf(key.substring(6)));
                return block == null ? null : sized(block, size);
            }
            if (key.startsWith("cover.")) {
                return new ItemStack(
                    ItemVoidcraftCovers.INSTANCE,
                    size,
                    VoidcraftCoverComponent.valueOf(key.substring(6))
                        .getId());
            }
            int sep = key.lastIndexOf(':');
            if (sep > 0 && sep < key.length() - 1) {
                int meta;
                try {
                    meta = Integer.parseInt(key.substring(sep + 1));
                } catch (NumberFormatException e) {
                    return null;
                }
                Item item = (Item) Item.itemRegistry.getObject(key.substring(0, sep));
                return item == null ? null : new ItemStack(item, size, meta);
            }
            Materials material = Materials.get(key);
            return material == null || material == Materials._NULL ? null : material.getDust(size);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * One abstract cargo entry for a hold item key (the hold → delivery-boundary conversion, the same entry
     * format the mining cargo writes: {@code {id, Damage, amount}} + the material name for material-name keys —
     * {@link USSShipCargo#fillHold} resolves the key back from it).
     *
     * @param key    the hold's item key
     * @param amount the amount on board
     * @return the entry, or null when the key does not resolve (dropped, not corrupted)
     */
    @Nullable
    public static NBTTagCompound abstractEntry(String key, long amount) {
        if (key == null || key.isEmpty() || amount <= 0L) {
            return null;
        }
        ItemStack one = stackOf(key, 1L);
        if (one == null) {
            return null;
        }
        NBTTagCompound entry = new NBTTagCompound();
        entry.setShort(USSShipCargo.ENTRY_ID, (short) Item.getIdFromItem(one.getItem()));
        entry.setShort(USSShipCargo.ENTRY_DAMAGE, (short) one.getItemDamage());
        entry.setInteger(USSShipCargo.ENTRY_AMOUNT, (int) Math.min(Integer.MAX_VALUE, amount));
        Materials material = Materials.get(key);
        if (material != null && material != Materials._NULL) {
            entry.setString(USSShipCargo.ITEM_ENTRY_MATERIAL, material.getName());
        }
        return entry;
    }

    private static ItemStack sized(ItemStack one, int size) {
        ItemStack out = one.copy();
        out.stackSize = size;
        return out;
    }
}
