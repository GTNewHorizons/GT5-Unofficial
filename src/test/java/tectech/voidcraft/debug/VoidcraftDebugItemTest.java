package tectech.voidcraft.debug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tectech.voidcraft.item.ItemVoidcraftDebugDysonSwarm;

/**
 * Unit tests for the debug item → effect registry and the Dyson Swarm injector's amount rule (bare-JVM).
 */
public class VoidcraftDebugItemTest {

    @BeforeEach
    public void setUp() {
        VoidcraftDebugEffectRegistry.resetForTests();
    }

    @AfterEach
    public void tearDown() {
        VoidcraftDebugEffectRegistry.resetForTests();
    }

    @Test
    public void testInjectAmountIsTenPercentOfCapacity() {
        // The debug injector adds 10% of the star's satellite capacity per click — the amount rule.
        assertEquals(0L, ItemVoidcraftDebugDysonSwarm.injectAmountFor(0L), "no capacity → nothing");
        assertEquals(0L, ItemVoidcraftDebugDysonSwarm.injectAmountFor(-5L), "negative capacity → nothing");
        assertEquals(5L, ItemVoidcraftDebugDysonSwarm.injectAmountFor(50L), "smallest white dwarf: 10% of 50");
        assertEquals(30L, ItemVoidcraftDebugDysonSwarm.injectAmountFor(300L), "main star: 10% of 300");
        assertEquals(80L, ItemVoidcraftDebugDysonSwarm.injectAmountFor(800L), "800-capacity star: 10% of 800");
        assertEquals(1L, ItemVoidcraftDebugDysonSwarm.injectAmountFor(4L), "round(0.4) = 0 → at least 1");
    }

    @Test
    public void testRegistryRoutesTheHeldItemToItsEffect() {
        // Null / unregistered stacks have no effect.
        assertNull(VoidcraftDebugEffectRegistry.effectFor(null));
        Item itemA = new Item();
        Item itemB = new Item();
        assertNull(VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemA)), "unregistered → null");

        VoidcraftDebugEffectRegistry.Effect effectA = (machine, player) -> {};
        VoidcraftDebugEffectRegistry.Effect effectB = (machine, player) -> {};
        VoidcraftDebugEffectRegistry.register(itemA, effectA);
        VoidcraftDebugEffectRegistry.register(itemB, effectB);

        assertSame(effectA, VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemA)));
        assertSame(effectB, VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemB)));
        assertSame(
            effectA,
            VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemA, 1, 7)),
            "damage values are irrelevant — routing is by item identity");
        assertSame(effectB, VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemB, 1, 3)));

        // Re-registering the same item replaces its effect.
        VoidcraftDebugEffectRegistry.Effect effectA2 = (machine, player) -> {};
        VoidcraftDebugEffectRegistry.register(itemA, effectA2);
        assertSame(effectA2, VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemA)));
        assertSame(effectB, VoidcraftDebugEffectRegistry.effectFor(new ItemStack(itemB)), "other items untouched");
    }

    @Test
    public void testResetClearsEverything() {
        Item item = new Item();
        VoidcraftDebugEffectRegistry.Effect effect = (machine, player) -> {};
        VoidcraftDebugEffectRegistry.register(item, effect);
        assertSame(effect, VoidcraftDebugEffectRegistry.effectFor(new ItemStack(item)));
        VoidcraftDebugEffectRegistry.resetForTests();
        assertNull(VoidcraftDebugEffectRegistry.effectFor(new ItemStack(item)), "reset → the item routes nowhere");
    }
}
