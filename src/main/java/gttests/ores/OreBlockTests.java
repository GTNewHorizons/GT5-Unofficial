package gttests.ores;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.mojang.authlib.GameProfile;
import com.ruling_0.materiallib.api.Material;

import gregtech.GTMod;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneCategory;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy.OreDropSystem;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gregtech.common.ores.SmallOreDrops;

/// The ore blocks [GTOreAdapter] serves for Iron, Bismutite and Cerite: which stones carry them, their harvest
/// levels, and their drops.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class OreBlockTests {

    private static final TestPos ORE = at(1, 1, 1);
    private static final int SAMPLES = 32;

    private record Ore(Material material, StoneType stone, boolean small, int harvestLevel) {}

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void oresCarryTheirDeclaredHarvestLevel(GameTestHelper helper) {
        Ore[] ores = { new Ore(Materials.Iron, StoneType.Stone, false, 2),
            new Ore(Materials.Iron, StoneType.Stone, true, 1), new Ore(Materials.Bismutite, StoneType.Moon, false, 3),
            new Ore(Materials.Bismutite, StoneType.Moon, true, 2),
            new Ore(Materials.Cerite, StoneType.Stone, false, 1) };
        List<String> mismatches = new ArrayList<>();
        for (Ore ore : ores) {
            ItemStack stack = place(helper, ore.material(), ore.stone(), ore.small());
            Block block = Block.getBlockFromItem(stack.getItem());
            int level = block.getHarvestLevel(stack.getItemDamage());
            String tool = block.getHarvestTool(stack.getItemDamage());
            if (level != ore.harvestLevel() || !"pickaxe".equals(tool)) {
                mismatches.add(describe(ore) + " -> " + tool + " " + level + ", expected " + ore.harvestLevel());
            }
        }
        helper.assertTrue(mismatches.isEmpty(), "Ore harvest levels differ: " + mismatches);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void oresDropOneRawOreWithoutFortune(GameTestHelper helper) {
        assumeFortuneItemDrops(helper);
        EntityPlayer miner = miner(helper, new ItemStack(Items.diamond_pickaxe));
        List<String> mismatches = new ArrayList<>();
        for (Material material : new Material[] { Materials.Iron, Materials.Bismutite, Materials.Cerite }) {
            StoneType stone = material == Materials.Bismutite ? StoneType.Moon : StoneType.Stone;
            place(helper, material, stone, false);
            List<ItemStack> drops = harvest(helper, miner);
            ItemStack rawOre = MaterialParts.stack(OrePrefixes.rawOre, material, 1);
            if (rawOre == null || drops.size() != 1
                || !GTUtility.areStacksEqual(drops.get(0), rawOre)
                || drops.get(0).stackSize != 1) {
                mismatches.add(material.getName() + " on " + stone + " -> " + drops);
            }
        }
        helper.assertTrue(mismatches.isEmpty(), "Ores did not drop one raw ore: " + mismatches);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void fortuneRaisesAPlayersRawOreYield(GameTestHelper helper) {
        assumeFortuneItemDrops(helper);
        EntityPlayer miner = miner(helper, enchanted(Enchantment.fortune, 3));
        int total = rawOreYield(helper, miner, 1, 4);
        helper.assertTrue(total > SAMPLES, "Fortune III never raised the raw iron yield over " + SAMPLES + " ores");
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void fortuneFromAFakePlayerLeavesTheYieldUnchanged(GameTestHelper helper) {
        assumeFortuneItemDrops(helper);
        EntityPlayer fakePlayer = helper.spawnFakePlayer("OreBreaker");
        hold(fakePlayer, enchanted(Enchantment.fortune, 3));
        rawOreYield(helper, fakePlayer, 1, 1);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void silkTouchDropsTheOreBlock(GameTestHelper helper) {
        EntityPlayer miner = miner(helper, enchanted(Enchantment.silkTouch, 1));
        ItemStack ore = place(helper, Materials.Bismutite, StoneType.Moon, false);
        List<ItemStack> drops = harvest(helper, miner);
        helper.assertTrue(
            drops.size() == 1 && GTUtility.areStacksEqual(drops.get(0), ore) && drops.get(0).stackSize == 1,
            "Silk touch on Bismutite Moon ore dropped " + drops);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void smallOresDropMaterialPartsAndStoneDust(GameTestHelper helper) {
        EntityPlayer miner = miner(helper, new ItemStack(Items.diamond_pickaxe));
        List<String> mismatches = new ArrayList<>();
        for (Material material : new Material[] { Materials.Iron, Materials.Bismutite }) {
            StoneType stone = material == Materials.Bismutite ? StoneType.Moon : StoneType.Stone;
            for (int i = 0; i < SAMPLES; i++) {
                place(helper, material, stone, true);
                List<ItemStack> drops = harvest(helper, miner);
                if (countParts(drops, material, stone) < MaterialUtils.oreMultiplier(material)) {
                    mismatches.add("small " + material.getName() + " on " + stone + " -> " + drops);
                    break;
                }
            }
        }
        helper.assertTrue(mismatches.isEmpty(), "Small ores dropped something else: " + mismatches);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void silkTouchOnASmallOreKeepsItsFullYield(GameTestHelper helper) {
        EntityPlayer miner = miner(helper, enchanted(Enchantment.silkTouch, 1));
        int multiplier = MaterialUtils.oreMultiplier(Materials.Redstone);
        helper.assertTrue(multiplier > 1, "Redstone no longer has an ore multiplier");
        place(helper, Materials.Redstone, StoneType.Stone, true);
        List<ItemStack> drops = harvest(helper, miner);
        int parts = countParts(drops, Materials.Redstone, StoneType.Stone);
        helper.assertTrue(
            parts >= multiplier,
            "Silk touch on small Redstone ore dropped " + parts + " parts, expected " + multiplier + ": " + drops);
        helper.succeed();
    }

    @GameTest(timeoutTicks = 20, batch = "gt5.ores")
    public static void formerForeignOresExistOnEveryStoneUnderTheirOreName(GameTestHelper helper) {
        helper.assertNotNull(
            GTOreAdapter.INSTANCE.getVariantStack(Materials.Iron, StoneType.Mars, false),
            "Iron has no Mars ore");
        List<String> missing = new ArrayList<>();
        for (StoneType stone : StoneType.VALUES) {
            if (stone.getCategory() != StoneCategory.Stone) continue;
            if (GTOreAdapter.INSTANCE.getVariantStack(Materials.Iron, stone, false) == null) continue;
            for (Material material : new Material[] { Materials.Bismutite, Materials.Cerite }) {
                ItemStack stack = GTOreAdapter.INSTANCE.getVariantStack(material, stone, false);
                String oreName = stone.getPrefix()
                    .oreDictName(material);
                if (stack == null) {
                    missing.add(material.getName() + " on " + stone);
                } else if (!hasOreName(stack, oreName)) {
                    missing.add(oreName);
                }
            }
        }
        helper.assertTrue(missing.isEmpty(), "Ores missing where Iron ore exists: " + missing);
        helper.succeed();
    }

    private static void assumeFortuneItemDrops(GameTestHelper helper) {
        helper.assumeTrue(
            GTMod.proxy.oreDropSystem == OreDropSystem.FortuneItem,
            "Ore drop system is " + GTMod.proxy.oreDropSystem);
    }

    /// Harvests Iron ore on Stone [#SAMPLES] times and returns the raw iron dropped, failing on any harvest that
    /// drops something else or a count outside `[min, max]`.
    private static int rawOreYield(GameTestHelper helper, EntityPlayer player, int min, int max) {
        ItemStack rawIron = MaterialParts.stack(OrePrefixes.rawOre, Materials.Iron, 1);
        int total = 0;
        for (int i = 0; i < SAMPLES; i++) {
            place(helper, Materials.Iron, StoneType.Stone, false);
            List<ItemStack> drops = harvest(helper, player);
            int count = 0;
            for (ItemStack drop : drops) {
                helper.assertTrue(GTUtility.areStacksEqual(drop, rawIron), "Iron ore dropped " + drops);
                count += drop.stackSize;
            }
            helper.assertTrue(
                count >= min && count <= max,
                "Iron ore dropped " + count + " raw iron, expected " + min + ".." + max);
            total += count;
        }
        return total;
    }

    /// The number of small-ore parts of `material` among `drops`, or `-1` when a drop is neither such a part nor
    /// the dust of `stone`.
    private static int countParts(List<ItemStack> drops, Material material, StoneType stone) {
        List<ItemStack> parts = SmallOreDrops.getDropList(material);
        int count = 0;
        for (ItemStack drop : drops) {
            if (parts.stream()
                .anyMatch(part -> GTUtility.areStacksEqual(part, drop))) {
                count += drop.stackSize;
            } else if (!GTUtility.areStacksEqual(drop, stone.getDust(true, 1))
                && !GTUtility.areStacksEqual(drop, stone.getDust(false, 1))) {
                    return -1;
                }
        }
        return count;
    }

    /// Places the ore [GTOreAdapter] serves for the triple at [#ORE], failing unless it decodes back to it.
    private static ItemStack place(GameTestHelper helper, Material material, StoneType stone, boolean small) {
        ItemStack stack = GTOreAdapter.INSTANCE.getVariantStack(material, stone, small);
        String name = (small ? "small " : "") + material.getName() + " ore on " + stone;
        helper.assertNotNull(stack, "No " + name);
        helper.setBlock(ORE, Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
        TestPos pos = helper.absolute(ORE);
        try (OreInfo info = OreManager.getOreInfo(helper.getWorld(), pos.x(), pos.y(), pos.z())) {
            helper.assertTrue(
                info != null && info.material == material && info.stoneType == stone && info.isSmall == small,
                "Placed " + name + " does not decode back to it");
        }
        return stack;
    }

    /// Breaks the block at [#ORE] the way a player's survival break does and returns what it drops.
    private static List<ItemStack> harvest(GameTestHelper helper, EntityPlayer player) {
        World world = helper.getWorld();
        TestPos pos = helper.absolute(ORE);
        Block block = world.getBlock(pos.x(), pos.y(), pos.z());
        int meta = world.getBlockMetadata(pos.x(), pos.y(), pos.z());
        world.setBlockToAir(pos.x(), pos.y(), pos.z());
        block.harvestBlock(world, player, pos.x(), pos.y(), pos.z(), meta);
        List<ItemStack> drops = new ArrayList<>();
        for (EntityItem item : helper.getEntities(EntityItem.class, ORE)) {
            drops.add(item.getEntityItem());
            item.setDead();
        }
        return drops;
    }

    private static boolean hasOreName(ItemStack stack, String oreName) {
        for (int id : OreDictionary.getOreIDs(stack)) {
            if (OreDictionary.getOreName(id)
                .equals(oreName)) return true;
        }
        return false;
    }

    private static ItemStack enchanted(Enchantment enchantment, int level) {
        ItemStack pickaxe = new ItemStack(Items.diamond_pickaxe);
        pickaxe.addEnchantment(enchantment, level);
        return pickaxe;
    }

    private static EntityPlayer miner(GameTestHelper helper, ItemStack tool) {
        EntityPlayer miner = new Miner(helper.getWorld());
        hold(miner, tool);
        return miner;
    }

    private static void hold(EntityPlayer player, ItemStack tool) {
        player.inventory.currentItem = 0;
        player.inventory.setInventorySlotContents(0, tool);
    }

    private static String describe(Ore ore) {
        return (ore.small() ? "small " : "") + ore.material()
            .getName() + " on " + ore.stone();
    }

    /// A player that GregTech treats as real ([GTUtility#isRealPlayer]), unlike a Forge fake player.
    private static final class Miner extends EntityPlayer {

        Miner(World world) {
            super(world, new GameProfile(UUID.nameUUIDFromBytes("OreMiner".getBytes()), "OreMiner"));
        }

        @Override
        public void addChatMessage(IChatComponent message) {}

        @Override
        public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
            return false;
        }

        @Override
        public ChunkCoordinates getPlayerCoordinates() {
            return new ChunkCoordinates((int) posX, (int) posY, (int) posZ);
        }
    }
}
