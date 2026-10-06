package gttests.ores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.postea.utility.ChunkFixerUtility;

import gregtech.api.enums.Mods;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.common.ores.GTOreAdapter;
import gttests.PosteaTestSupport;

/// Saved legacy `bw.blockores*` ores, placed or as items, load as the MaterialLib ore of the same material, stone
/// and size. Skips in a world's first session; see [PosteaTestSupport].
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class LegacyBartworksOreMigrationTests {

    private record Legacy(String registryName, StoneType stone, boolean small) {}

    private static final Legacy[] LEGACY_BLOCKS = { new Legacy("bw.blockores.01", StoneType.Stone, false),
        new Legacy("bw.blockores.natural.01", StoneType.Stone, false),
        new Legacy("bw.blockores.02", StoneType.Stone, true),
        new Legacy("bw.blockores.natural.02", StoneType.Stone, true),
        new Legacy("bw.blockores.03", StoneType.Moon, false),
        new Legacy("bw.blockores.natural.03", StoneType.Moon, false),
        new Legacy("bw.blockores.04", StoneType.Moon, true),
        new Legacy("bw.blockores.natural.04", StoneType.Moon, true) };

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void placedLegacyOresLoadAsMaterialLibOres(GameTestHelper helper) {
        PosteaTestSupport.assumeBlockTransformers(helper);
        World world = helper.getWorld();
        int legacyId = LegacyWerkstoffIndex.idOf(Materials.Bismutite);
        helper.assertTrue(legacyId >= 0, "Bismutite has no legacy werkstoff id");

        Chunk chunk = new Chunk(world, 1000, 1000);
        ExtendedBlockStorage section = new ExtendedBlockStorage(0, !world.provider.hasNoSky);
        chunk.getBlockStorageArray()[0] = section;
        for (int i = 0; i < LEGACY_BLOCKS.length; i++) {
            section.func_150818_a(i, 1, 0, legacyBlock(helper, LEGACY_BLOCKS[i].registryName()));
            section.setExtBlockMetadata(i, 1, 0, legacyId);
        }

        NBTTagList tileEntities = new NBTTagList();
        tileEntities.appendTag(legacyTileEntity("bw.blockoresTE", 0, legacyId));
        tileEntities.appendTag(legacyTileEntity("bw.blockoresSmallTE", 1, legacyId));
        section.func_150818_a(0, 2, 0, Blocks.stone);
        section.func_150818_a(1, 2, 0, Blocks.stone);
        NBTTagCompound level = new NBTTagCompound();
        level.setTag("TileEntities", tileEntities);

        ChunkFixerUtility.onChunkRead(chunk, world, level);

        List<String> mismatches = new ArrayList<>();
        for (int i = 0; i < LEGACY_BLOCKS.length; i++) {
            Legacy legacy = LEGACY_BLOCKS[i];
            check(mismatches, legacy.registryName(), section, i, 1, legacy.stone(), legacy.small());
        }
        check(mismatches, "bw.blockoresTE", section, 0, 2, StoneType.Stone, false);
        check(mismatches, "bw.blockoresSmallTE", section, 1, 2, StoneType.Stone, true);
        helper.assertTrue(mismatches.isEmpty(), "Placed legacy ores did not migrate: " + mismatches);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void savedLegacyOreItemsLoadAsMaterialLibOres(GameTestHelper helper) {
        PosteaTestSupport.assumeItemTransformers(helper);
        int legacyId = LegacyWerkstoffIndex.idOf(Materials.Bismutite);
        List<String> mismatches = new ArrayList<>();
        for (Legacy legacy : LEGACY_BLOCKS) {
            ItemStack saved = new ItemStack(legacyBlock(helper, legacy.registryName()), 3, legacyId);
            ItemStack loaded = ItemStack.loadItemStackFromNBT(saved.writeToNBT(new NBTTagCompound()));
            ItemStack expected = GTOreAdapter.INSTANCE
                .getVariantStack(Materials.Bismutite, legacy.stone(), legacy.small());
            if (expected == null || loaded == null
                || loaded.getItem() != expected.getItem()
                || loaded.getItemDamage() != expected.getItemDamage()
                || loaded.stackSize != 3) {
                mismatches.add(legacy.registryName() + " -> " + describe(loaded));
            }
        }
        helper.assertTrue(mismatches.isEmpty(), "Saved legacy ore items did not migrate: " + mismatches);
        helper.succeed();
    }

    private static Block legacyBlock(GameTestHelper helper, String registryName) {
        Block block = (Block) Block.blockRegistry.getObject("bartworks:" + registryName);
        if (block == null || block == Blocks.air)
            helper.fail("Legacy block bartworks:" + registryName + " is not registered");
        return block;
    }

    private static NBTTagCompound legacyTileEntity(String id, int x, int legacyId) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", id);
        tag.setInteger("x", 1000 * 16 + x);
        tag.setInteger("y", 2);
        tag.setInteger("z", 1000 * 16);
        tag.setInteger("m", legacyId);
        return tag;
    }

    private static void check(List<String> mismatches, String legacy, ExtendedBlockStorage section, int x, int y,
        StoneType stone, boolean small) {
        ItemStack expected = GTOreAdapter.INSTANCE.getVariantStack(Materials.Bismutite, stone, small);
        Block block = section.getBlockByExtId(x, y, 0);
        int meta = section.getExtBlockMetadata(x, y, 0);
        if (expected == null || block != Block.getBlockFromItem(expected.getItem())
            || meta != expected.getItemDamage()) {
            mismatches.add(legacy + " -> " + Block.blockRegistry.getNameForObject(block) + "@" + meta);
        }
    }

    private static String describe(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "null";
        return Item.itemRegistry.getNameForObject(stack.getItem()) + "@" + stack.getItemDamage();
    }
}
