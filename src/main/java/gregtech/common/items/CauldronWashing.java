package gregtech.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.ProcessingSubstitutes;

/// Washes items in a water cauldron, either dropped in or used on it. Impure and purified dusts become clean dust,
/// or their material's processing substitute (see [ProcessingSubstitutes#washing]), crushed ore becomes purified
/// crushed ore, and wheat dust becomes
/// dough. Each wash lowers the water by one level.
public final class CauldronWashing {

    private CauldronWashing() {}

    /// Attaches washing to the dropped-item and item-use callbacks of the washable item shapes.
    public static void register() {
        attach(Shapes.dustImpure, OrePrefixes.dustImpure);
        attach(Shapes.dustPure, OrePrefixes.dustPure);
        attach(Shapes.crushed, OrePrefixes.crushed);
        attach(Shapes.dust, OrePrefixes.dust);
    }

    private static void attach(Shape shape, OrePrefixes prefix) {
        MaterialLibAPI.editShape("gregtech", shape.getName())
            .onEntityItemUpdate((material, entity) -> washDropped(prefix, material, entity))
            .onItemUse(
                (material, stack, player, world, x, y, z, side, hitX, hitY,
                    hitZ) -> washHeld(prefix, material, stack, player, world, x, y, z));
    }

    /// Returns the washed form of `stackSize` items of `prefix` and `material`, or null when they do not wash.
    public static ItemStack result(OrePrefixes prefix, Material material, int stackSize) {
        if (material == null || material == Materials.Empty || material == Materials.NULL) return null;
        return switch (prefix.getName()) {
            case "dustImpure", "dustPure" -> ProcessingSubstitutes.washing(material, stackSize);
            case "crushed" -> MaterialLibAPI.findStack(material, Shapes.crushedPurified, stackSize);
            case "dust" -> material == Materials.Wheat ? ItemList.Food_Dough.get(stackSize) : null;
            default -> null;
        };
    }

    /// Washes a dropped item that sits in a cauldron holding water. Returns whether it washed, always false on the
    /// client.
    public static boolean washDropped(OrePrefixes prefix, Material material, EntityItem entity) {
        World world = entity.worldObj;
        if (world.isRemote || !washes(prefix, material)) return false;
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY);
        int z = MathHelper.floor_double(entity.posZ);
        if (!holdsWater(world, x, y, z)) return false;
        ItemStack washed = result(prefix, material, entity.getEntityItem().stackSize);
        if (washed == null) return false;
        entity.setEntityItemStack(washed);
        entity.delayBeforeCanPickup = 0;
        entity.motionX = 0;
        entity.motionY = 0;
        entity.motionZ = 0;
        entity.posX = x + 0.5;
        entity.posY = y + 1.5;
        entity.posZ = z + 0.5;
        lowerWater(world, x, y, z);
        return true;
    }

    /// Washes the item `player` holds when used on a cauldron holding water. Returns whether it washed.
    public static boolean washHeld(OrePrefixes prefix, Material material, ItemStack stack, EntityPlayer player,
        World world, int x, int y, int z) {
        if (!washes(prefix, material) || !holdsWater(world, x, y, z)) return false;
        ItemStack washed = result(prefix, material, stack.stackSize);
        if (washed == null) return false;
        lowerWater(world, x, y, z);
        player.inventory.setInventorySlotContents(player.inventory.currentItem, washed);
        return true;
    }

    private static boolean washes(OrePrefixes prefix, Material material) {
        return prefix != null && (prefix != OrePrefixes.dust || material == Materials.Wheat);
    }

    private static boolean holdsWater(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == Blocks.cauldron && world.getBlockMetadata(x, y, z) > 0;
    }

    private static void lowerWater(World world, int x, int y, int z) {
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, 3);
    }
}
