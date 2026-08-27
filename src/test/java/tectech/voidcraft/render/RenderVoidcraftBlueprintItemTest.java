package tectech.voidcraft.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Pins the item renderer's claiming and helper decision tables (the parts that are pure logic) and the model
 * fit scale. The actual GL draw is client-only and is verified by playtest.
 */
class RenderVoidcraftBlueprintItemTest {

    private static ItemStack stackWithGrid(Item item) {
        ItemStack stack = new ItemStack(item, 1);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger(VoidcraftNbt.TAG_GRID, 3);
        stack.setTagCompound(nbt);
        return stack;
    }

    private static ItemStack stackWithoutGrid(Item item) {
        ItemStack stack = new ItemStack(item, 1);
        stack.setTagCompound(new NBTTagCompound());
        return stack;
    }

    @Test
    void fitScaleFillsTheItemBox() {
        assertEquals(0.085, RenderVoidcraftBlueprintItem.fitScale(10), 1e-9);
        assertEquals(0.85 / 15.0, RenderVoidcraftBlueprintItem.fitScale(15), 1e-15);
        assertEquals(0.85, RenderVoidcraftBlueprintItem.fitScale(1), 1e-9);
        assertEquals(0.0, RenderVoidcraftBlueprintItem.fitScale(0), 1e-9);
        assertEquals(0.0, RenderVoidcraftBlueprintItem.fitScale(-3), 1e-9);
    }

    @Test
    void hasBlueprintRequiresTheGridTag() {
        assertFalse(RenderVoidcraftBlueprintItem.hasBlueprint(null));
        assertFalse(RenderVoidcraftBlueprintItem.hasBlueprint(new ItemStack(ItemVoidcraft.INSTANCE, 1)));
        assertFalse(RenderVoidcraftBlueprintItem.hasBlueprint(stackWithoutGrid(ItemVoidcraft.INSTANCE)));
        assertTrue(RenderVoidcraftBlueprintItem.hasBlueprint(stackWithGrid(ItemVoidcraft.INSTANCE)));
        assertTrue(RenderVoidcraftBlueprintItem.hasBlueprint(stackWithGrid(ItemVoidbaseBlueprint.INSTANCE)));
    }

    @Test
    void handleRenderTypeClaimsOnlyTheRenderTypesWithPayload() {
        RenderVoidcraftBlueprintItem ship = new RenderVoidcraftBlueprintItem(false);
        ItemStack withPayload = stackWithGrid(ItemVoidcraft.INSTANCE);
        ItemStack noPayload = stackWithoutGrid(ItemVoidcraft.INSTANCE);

        for (ItemRenderType type : new ItemRenderType[] { ItemRenderType.INVENTORY, ItemRenderType.EQUIPPED,
            ItemRenderType.EQUIPPED_FIRST_PERSON, ItemRenderType.ENTITY }) {
            assertTrue(ship.handleRenderType(withPayload, type), type + " should claim a stack with a payload");
            assertFalse(
                ship.handleRenderType(noPayload, type),
                type + " must fall back to the vanilla icon without a payload");
        }
        assertFalse(ship.handleRenderType(withPayload, ItemRenderType.FIRST_PERSON_MAP));
    }

    @Test
    void shouldUseRenderHelperSelectsThe3DTransforms() {
        RenderVoidcraftBlueprintItem ship = new RenderVoidcraftBlueprintItem(false);
        ItemStack stack = stackWithGrid(ItemVoidcraft.INSTANCE);

        assertTrue(ship.shouldUseRenderHelper(ItemRenderType.INVENTORY, stack, ItemRendererHelper.INVENTORY_BLOCK));
        for (ItemRendererHelper helper : new ItemRendererHelper[] { ItemRendererHelper.ENTITY_ROTATION,
            ItemRendererHelper.ENTITY_BOBBING, ItemRendererHelper.EQUIPPED_BLOCK, ItemRendererHelper.BLOCK_3D }) {
            assertFalse(ship.shouldUseRenderHelper(ItemRenderType.INVENTORY, stack, helper));
        }

        for (ItemRenderType type : new ItemRenderType[] { ItemRenderType.EQUIPPED,
            ItemRenderType.EQUIPPED_FIRST_PERSON }) {
            assertTrue(ship.shouldUseRenderHelper(type, stack, ItemRendererHelper.EQUIPPED_BLOCK));
            for (ItemRendererHelper helper : new ItemRendererHelper[] { ItemRendererHelper.ENTITY_ROTATION,
                ItemRendererHelper.ENTITY_BOBBING, ItemRendererHelper.INVENTORY_BLOCK, ItemRendererHelper.BLOCK_3D }) {
                assertFalse(ship.shouldUseRenderHelper(type, stack, helper));
            }
        }

        assertTrue(ship.shouldUseRenderHelper(ItemRenderType.ENTITY, stack, ItemRendererHelper.BLOCK_3D));
        assertTrue(ship.shouldUseRenderHelper(ItemRenderType.ENTITY, stack, ItemRendererHelper.ENTITY_BOBBING));
        assertFalse(ship.shouldUseRenderHelper(ItemRenderType.ENTITY, stack, ItemRendererHelper.ENTITY_ROTATION));
        assertFalse(ship.shouldUseRenderHelper(ItemRenderType.ENTITY, stack, ItemRendererHelper.EQUIPPED_BLOCK));

        for (ItemRendererHelper helper : ItemRendererHelper.values()) {
            assertFalse(ship.shouldUseRenderHelper(ItemRenderType.FIRST_PERSON_MAP, stack, helper));
        }
    }
}
