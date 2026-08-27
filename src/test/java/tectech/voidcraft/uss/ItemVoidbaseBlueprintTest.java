package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Bare-JVM tests for the Voidbase blueprint item: the payload round-trip (item NBT -> blueprint -> item NBT)
 * and the empty-blueprint detection.
 */
public class ItemVoidbaseBlueprintTest {

    private static VoidcraftBlueprint testBase() {
        byte[] grid = { (byte) VoidcraftComponent.CONTROLLER.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), (byte) VoidcraftComponent.FRAME.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), };
        byte[] covers = new byte[4 * 6];
        covers[1 * 6 + 0] = (byte) VoidcraftCoverComponent.SOLAR_PANEL.toGridValue();
        return VoidcraftBlueprint.ofBase(1, 1, 4, grid, null, covers);
    }

    @Test
    public void testBlueprintPayloadRoundTrip() {
        VoidcraftBlueprint original = testBase();
        ItemStack stack = ItemVoidbaseBlueprint.fromBlueprint(original, "Test Station", "uuid-1", 111L, null);
        assertEquals(1, stack.stackSize);
        assertNotNull(stack.getTagCompound());

        VoidcraftBlueprint back = ItemVoidbaseBlueprint.getBlueprint(stack);
        assertNotNull(back, "the blueprint reads back from the item NBT");
        assertEquals(original, back, "the full grid round-trips");
        assertEquals(2000L, back.computeStats().energyGen, "the derived stats round-trip with it (one solar panel)");

        NBTTagCompound nbt = stack.getTagCompound();
        assertEquals("uuid-1", nbt.getString(VoidcraftNbt.TAG_UUID));
        assertEquals("Test Station", nbt.getString(VoidcraftNbt.TAG_NAME));
        assertFalse(nbt.hasKey(VoidcraftNbt.TAG_PROGRAM), "no program tag when the controller had none");
    }

    @Test
    public void testEmptyBlueprintDetection() {
        ItemStack empty = new ItemStack(ItemVoidbaseBlueprint.INSTANCE, 1);
        assertTrue(ItemVoidbaseBlueprint.isEmptyBlueprint(empty), "a bare item is an empty blueprint");
        assertNull(ItemVoidbaseBlueprint.getBlueprint(empty));
        assertTrue(ItemVoidbaseBlueprint.isEmptyBlueprint(null));

        ItemStack full = ItemVoidbaseBlueprint.fromBlueprint(testBase(), "S", "u", 1L, null);
        assertFalse(ItemVoidbaseBlueprint.isEmptyBlueprint(full), "a digitized blueprint is not empty");
    }

    @Test
    public void testWrongItemGivesNull() {
        // A VOIDCRAFT (ship) item is not a base blueprint
        net.minecraft.nbt.NBTTagList program = null;
        tectech.voidcraft.ship.VoidcraftBlueprint ship = tectech.voidcraft.ship.VoidcraftBlueprint.of(
            1,
            1,
            4,
            new byte[] { (byte) VoidcraftComponent.CONTROLLER.toGridValue(),
                (byte) VoidcraftComponent.FRAME.toGridValue(), (byte) VoidcraftComponent.FRAME.toGridValue(),
                (byte) VoidcraftComponent.FRAME.toGridValue() });
        ItemStack shipStack = tectech.voidcraft.item.ItemVoidcraft.fromBlueprint(ship, "S", "u", 1L, program);
        assertNull(ItemVoidbaseBlueprint.getBlueprint(shipStack), "getBlueprint only reads base blueprint items");
    }
}
