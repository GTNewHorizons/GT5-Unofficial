package gregtech.common.ores;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.util.OptionalBoolean;

public final class OreManager {

    private OreManager() {}

    private static final List<IOreAdapter<?>> ORE_ADAPTERS = ImmutableList
        .of(GTOreAdapter.INSTANCE, GTPPOreAdapter.INSTANCE, BWOreAdapter.INSTANCE, UnificationOreAdapter.INSTANCE);

    public static OptionalBoolean isOre(Block block, int meta) {
        int size = ORE_ADAPTERS.size();

        for (int i = 0; i < size; i++) {
            IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

            try (OreInfo<?> info = oreAdapter.getOreInfo(block, meta)) {
                if (info != null) {
                    return info.isNatural ? OptionalBoolean.TRUE : OptionalBoolean.FALSE;
                }
            }
        }

        return OptionalBoolean.NONE;
    }

    public static OreInfo<IOreMaterial> getOreInfo(IBlockAccess world, int x, int y, int z) {
        return getOreInfo(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static OreInfo<IOreMaterial> getOreInfo(Block block, int meta) {
        int size = ORE_ADAPTERS.size();

        for (int i = 0; i < size; i++) {
            IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

            OreInfo info = oreAdapter.getOreInfo(block, meta);
            if (info != null) {
                info.cachedAdapter = oreAdapter;
                return info;
            }
        }

        return null;
    }

    public static OreInfo<IOreMaterial> getOreInfo(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return null;

        return getOreInfo(itemBlock.field_150939_a, Items.feather.getDamage(stack));
    }

    public static IOreAdapter<?> getAdapter(OreInfo<?> info) {
        if (info.cachedAdapter != null && info.cachedAdapter.supports(info)) return info.cachedAdapter;

        int size = ORE_ADAPTERS.size();

        for (int i = 0; i < size; i++) {
            IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

            if (oreAdapter.supports(info)) return oreAdapter;
        }

        return null;
    }

    public static boolean setOreForWorldGen(World world, int x, int y, int z, IStoneType defaultStone,
        IOreMaterial material, boolean small) {
        if (y < 0 || y >= world.getActualHeight()) return false;

        IStoneType existingStone = StoneType.findStoneType(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));

        if (existingStone != null && !existingStone.canGenerateInWorld(world)) return false;

        if (existingStone == null) {
            if (defaultStone != null) {
                existingStone = defaultStone;
            } else {
                return false;
            }
        }

        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = existingStone;
            info.isSmall = small;
            info.isNatural = true;

            int size = ORE_ADAPTERS.size();

            for (int i = 0; i < size; i++) {
                IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

                ImmutableBlockMeta oreBlock = oreAdapter.getBlock(info);

                if (oreBlock != null) {
                    world.setBlock(x, y, z, oreBlock.getBlock(), oreBlock.getBlockMeta(), 3);

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean setOre(World world, int x, int y, int z, @Nullable IStoneType stoneType,
        IOreMaterial material, boolean small) {
        if (y < 0 || y >= world.getActualHeight()) return false;

        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = stoneType;
            info.isSmall = small;
            info.isNatural = true;

            int size = ORE_ADAPTERS.size();

            for (int i = 0; i < size; i++) {
                IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

                ImmutableBlockMeta oreBlock = oreAdapter.getBlock(info);

                if (oreBlock != null) {
                    world.setBlock(x, y, z, oreBlock.getBlock(), oreBlock.getBlockMeta(), 2);

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean setExistingOreStoneType(World world, int x, int y, int z, IStoneType newStoneType) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        try (OreInfo<?> info = getOreInfo(block, meta)) {
            if (info == null) return false;

            info.stoneType = newStoneType;

            ImmutableBlockMeta oreBlock = getAdapter(info).getBlock(info);

            if (oreBlock == null) return false;

            world.setBlock(x, y, z, oreBlock.getBlock(), oreBlock.getBlockMeta(), 2);

            return true;
        }
    }

    public static List<ItemStack> mineBlock(Random random, World world, int x, int y, int z, boolean silktouch,
        int fortune, boolean simulate, boolean replaceWithCobblestone) {
        Block ore = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        List<ItemStack> oreBlockDrops;
        ImmutableBlockMeta replacement;

        try (OreInfo<?> info = getOreInfo(ore, meta)) {
            if (info != null) {
                oreBlockDrops = info.cachedAdapter.getOreDrops(random, info, silktouch, fortune);

                replacement = info.stoneType.getCobblestone();
            } else {
                if (silktouch && ore.canSilkHarvest(world, null, x, y, z, meta)) {
                    oreBlockDrops = ImmutableList.of(new ItemStack(ore, 1, ore.damageDropped(meta)));
                } else {
                    // Regular ore
                    oreBlockDrops = ImmutableList.copyOf(ore.getDrops(world, x, y, z, meta, fortune));
                }

                replacement = new BlockMeta(Blocks.cobblestone, 0);
            }
        }

        if (!simulate) {
            if (replaceWithCobblestone) {
                world.setBlock(x, y, z, replacement.getBlock(), replacement.getBlockMeta(), 3);
            } else {
                world.setBlockToAir(x, y, z);
            }
        }

        return oreBlockDrops;
    }

    public static IOreMaterial getMaterial(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return null;

        return getMaterial(itemBlock.field_150939_a, Items.feather.getDamage(stack));
    }

    public static IOreMaterial getMaterial(Block block, int meta) {
        try (OreInfo<?> info = getOreInfo(block, meta)) {
            return info == null ? null : info.material;
        }
    }

    public static ItemStack getStack(OreInfo<?> info, int amount) {
        int size = ORE_ADAPTERS.size();

        for (int i = 0; i < size; i++) {
            IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

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

    public static List<ItemStack> getPotentialDrops(OreInfo<?> info) {
        int size = ORE_ADAPTERS.size();

        for (int i = 0; i < size; i++) {
            IOreAdapter<?> oreAdapter = ORE_ADAPTERS.get(i);

            if (oreAdapter.supports(info)) {
                return oreAdapter.getPotentialDrops(info);
            }
        }

        return null;
    }
}
