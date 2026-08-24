package tectech.voidcraft.uss;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.item.Item;

/**
 * Test helper for bare-JVM unit tests (no game, no mod loader).
 *
 * <p>
 * In a bare JVM, {@link Item#itemRegistry} is the FML-controlled registry, whose registration path requires a
 * {@code LaunchClassLoader} and throws — so vanilla {@code Items.*} fields are all {@code null}, and
 * {@link net.minecraft.item.ItemStack} NBT round-trips ({@code Item.getIdFromItem} /
 * {@code Item.getItemById}) resolve nothing.
 *
 * <p>
 * This registers a test {@link Item} by writing directly into the registry's internal identity map (bypassing the
 * FML guard on the registration methods). After that, item ↔ id resolution — and therefore
 * {@code ItemStack.writeToNBT}/{@code loadItemStackFromNBT} — works for the registered items.
 *
 * <p>
 * <strong>Id range:</strong> {@code ItemStack} NBT stores the id as a <em>signed short</em>, so ids must stay in
 * 1..32767 (use ids in the 30000s for these tests; a 92001 truncates to 26465 and the round-trip breaks).
 */
final class BareJvmItemRegistry {

    private BareJvmItemRegistry() {}

    /**
     * Make {@link Item} id resolution work for {@code item} at {@code id} in this bare JVM.
     *
     * @param item the item to register (a fresh {@code new Item()})
     * @param id   stable id in 1..32767 (NBT short)
     * @return the same item
     */
    static Item register(Item item, int id) {
        Object registry = Item.itemRegistry;
        for (Class<?> c = registry.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField("underlyingIntegerMap");
                f.setAccessible(true);
                Object idMap = f.get(registry);
                Method add = findAdd(idMap);
                add.invoke(idMap, item, id);
                return item;
            } catch (NoSuchFieldException e) {
                // field is declared on another class in the hierarchy — keep walking up
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Could not register bare-JVM test item at id " + id, e);
            }
        }
        throw new IllegalStateException("underlyingIntegerMap not found on " + registry.getClass());
    }

    private static Method findAdd(Object idMap) throws NoSuchMethodException {
        // MCP name in a fully mapped environment, SRG name in this fork's partial mapping.
        for (String name : new String[] { "add", "func_148746_a" }) {
            try {
                return idMap.getClass()
                    .getMethod(name, Object.class, int.class);
            } catch (NoSuchMethodException ignored) {
                // try the next name
            }
        }
        throw new NoSuchMethodException(
            "add(Object, int) on " + idMap.getClass()
                .getName());
    }
}
