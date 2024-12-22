package gregtech.common.ores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IMaterial;
import gregtech.api.interfaces.IStoneType;
import it.unimi.dsi.fastutil.Pair;

public class OreManager {

    private OreManager() {}

    public static final ImmutableList<IOreAdapter<?>> ORE_ADAPTERS = ImmutableList
        .of(GTOreAdapter.INSTANCE, GTPPOreAdapter.INSTANCE, BWOreAdapter.INSTANCE);

    public static IStoneType getStoneType(Block block, int meta) {
        IStoneType stoneType = StoneType.findStoneType(block, meta);

        if (stoneType != null) return stoneType;

        return null;
    }

    public static boolean isOre(Block block, int meta) {
        for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
            try (OreInfo<?> info = oreAdapter.getOreInfo(block, meta)) {
                if (info != null && info.isNatural) return true;
            }
        }

        return false;
    }

    public static Pair<IOreAdapter<?>, OreInfo<?>> getOreInfo(Block block, int meta) {
        for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
            OreInfo<?> info = oreAdapter.getOreInfo(block, meta);
            if (info != null) return Pair.of(oreAdapter, info);
        }

        return null;
    }

    public static Pair<IOreAdapter<?>, OreInfo<?>> getOreInfo(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return null;

        return getOreInfo(itemBlock.field_150939_a, Items.feather.getDamage(stack));
    }

    public static IOreAdapter<?> getAdapter(OreInfo<?> info) {
        for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
            if (oreAdapter.supports(info)) return oreAdapter;
        }

        return null;
    }

    public static boolean setOreForWorldGen(World world, int x, int y, int z, IStoneType defaultStone,
        IMaterial material, boolean small) {
        if (y < 0 || y >= world.getActualHeight()) return false;

        IStoneType existingStone = getStoneType(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));

        if (existingStone != null && !existingStone.canGenerateInWorld(world)) return false;

        if (existingStone == null) {
            if (defaultStone != null) {
                existingStone = defaultStone;
            } else {
                return false;
            }
        }

        try (OreInfo<IMaterial> info = OreInfo.getNewInfo();) {
            info.material = material;
            info.stoneType = existingStone;
            info.isSmall = small;
            info.isNatural = true;

            for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
                var block = oreAdapter.getBlock(info);

                if (block != null) {
                    world.setBlock(x, y, z, block.left(), block.rightInt(), 3);

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean setOre(World world, int x, int y, int z, @Nullable IStoneType stoneType, IMaterial material,
        boolean small) {
        if (y < 0 || y >= world.getActualHeight()) return false;

        try (OreInfo<IMaterial> info = OreInfo.getNewInfo();) {
            info.material = material;
            info.stoneType = stoneType;
            info.isSmall = small;
            info.isNatural = true;

            for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
                var block = oreAdapter.getBlock(info);

                if (block != null) {
                    world.setBlock(x, y, z, block.left(), block.rightInt(), 2);

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean setExistingOreStoneType(World world, int x, int y, int z, IStoneType newStoneType) {
        var p = getOreInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));

        if (p == null) return false;

        IOreAdapter<?> oreAdapter = p.left();
        OreInfo<?> info = p.right();

        info.stoneType = newStoneType;

        var block = oreAdapter.getBlock(info);

        if (block == null) return false;

        world.setBlock(x, y, z, block.left(), block.rightInt(), 2);

        return true;
    }

    public static List<ItemStack> mineBlock(World world, int x, int y, int z, boolean silktouch, int fortune, boolean simulate,
        boolean replaceWithCobblestone) {
        Block ore = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        var p = getOreInfo(ore, meta);

        List<ItemStack> oreBlockDrops;
        Block replacement;
        int replacementMeta;

        if (p != null) {
            try (OreInfo<?> info = p.right()) {
                oreBlockDrops = p.left()
                    .getOreDrops(info, silktouch, fortune);

                var cobble = info.stoneType.getCobblestone();

                replacement = cobble.left();
                replacementMeta = cobble.rightInt();
            }
        } else {
            if (silktouch && ore.canSilkHarvest(world, null, x, y, z, meta)) {
                oreBlockDrops = new ArrayList<>(Arrays.asList(new ItemStack(ore, 1, meta)));
            } else {
                // Regular ore
                oreBlockDrops = ore.getDrops(world, x, y, z, meta, fortune);
            }

            replacement = Blocks.cobblestone;
            replacementMeta = 0;
        }

        if (!simulate) {
            if (replaceWithCobblestone) {
                world.setBlock(x, y, z, replacement, replacementMeta, 3);
            } else {
                world.setBlockToAir(x, y, z);
            }
        }

        return oreBlockDrops;
    }

    public static IMaterial getMaterial(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return null;

        return getMaterial(itemBlock.field_150939_a, Items.feather.getDamage(stack));
    }

    public static IMaterial getMaterial(Block block, int meta) {
        var p = getOreInfo(block, meta);

        return p == null ? null : p.right().material;
    }

    public static ItemStack getStack(OreInfo<?> info, int amount) {
        for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
            ItemStack stack = oreAdapter.getStack(info, amount);

            if (stack != null) return stack;
        }

        return null;
    }

    public static String getLocalizedName(OreInfo<?> info) {
        ItemStack stack = getStack(info, 1);

        if (stack == null) return "<illegal ore>";

        return stack.getDisplayName();
    }

    public static List<ItemStack> getDrops(Block block, int meta, boolean silktouch, int fortune) {
        for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
            try (OreInfo<?> info = oreAdapter.getOreInfo(block, meta)) {
                if (info != null) {
                    return oreAdapter.getOreDrops(info, silktouch, fortune);
                }
            }
        }

        return null;
    }

    public static List<ItemStack> getPotentialDrops(OreInfo<?> info) {
        for (IOreAdapter<?> oreAdapter : ORE_ADAPTERS) {
            if (oreAdapter.supports(info)) {
                return oreAdapter.getPotentialDrops(info);
            }
        }

        return null;
    }
}
