package gregtech.common.ores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.libraries.com.google.common.collect.ImmutableList;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import bartworks.system.material.BWMetaGeneratedOres;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IStoneType;
import gregtech.common.GTMockWorld;
import gregtech.common.blocks.GTBlockOre;
import gtPlusPlus.core.block.base.BlockBaseOre;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class UnificationOreAdapter implements IOreAdapter<Materials> {

    private static final ImmutableList<OrePrefixes> ORE_ORE_PREFIXES = Arrays.stream(OrePrefixes.VALUES)
        .filter(
            p -> p.getName()
                .startsWith("ore"))
        .filter(p -> p != OrePrefixes.oreNether && p != OrePrefixes.oreEnd)
        .collect(ImmutableList.toImmutableList());

    public static final UnificationOreAdapter INSTANCE = new UnificationOreAdapter();

    private static final Table<OrePrefixes, Materials, ImmutableBlockMeta> BLOCK_TABLE = HashBasedTable.create();

    private static final Object2ObjectOpenHashMap<ImmutableBlockMeta, Pair<OrePrefixes, Materials>> MAT_BLOCK_TABLE = new Object2ObjectOpenHashMap<>();

    private static boolean initialized = false;

    public static void load() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    private UnificationOreAdapter() {

    }

    public static class EventHandler {

        @SubscribeEvent
        public void onOreRegistered(OreRegisterEvent event) {
            UnificationOreAdapter.onOreRegistered(event);
        }
    }

    public static void onOreRegistered(OreRegisterEvent event) {
        if (!initialized) {
            initialized = true;
            init();
        }

        if (!(event.Ore.getItem() instanceof ItemBlock itemBlock)) return;

        Block block = itemBlock.field_150939_a;

        if (block instanceof GTBlockOre) return;
        if (block instanceof BWMetaGeneratedOres) return;
        if (block instanceof BlockBaseOre) return;

        ImmutableBlockMeta bm = new BlockMeta(block, itemBlock.getMetadata(event.Ore.getItemDamage()));

        for (OrePrefixes ore : ORE_ORE_PREFIXES) {
            if (!event.Name.startsWith(ore.name())) continue;

            String matName = event.Name.substring(
                ore.name()
                    .length());

            Materials mat = Materials.get(matName);

            if (mat == Materials._NULL) continue;

            BLOCK_TABLE.put(ore, mat, bm);
            MAT_BLOCK_TABLE.put(bm, Pair.of(ore, mat));
        }
    }

    private static void init() {
        for (String name : OreDictionary.getOreNames()) {
            for (OrePrefixes prefix : ORE_ORE_PREFIXES) {
                if (!name.startsWith(prefix.name())) continue;

                String matName = name.substring(
                    prefix.name()
                        .length());

                Materials mat = Materials.get(matName);

                if (mat == Materials._NULL) continue;

                for (ItemStack ore : OreDictionary.getOres(name)) {
                    if (!(ore.getItem() instanceof ItemBlock itemBlock)) return;

                    Block block = itemBlock.field_150939_a;

                    if (block instanceof GTBlockOre) return;
                    if (block instanceof BWMetaGeneratedOres) return;
                    if (block instanceof BlockBaseOre) return;

                    ImmutableBlockMeta bm = new BlockMeta(block, itemBlock.getMetadata(ore.getItemDamage()));

                    BLOCK_TABLE.put(prefix, mat, bm);
                    MAT_BLOCK_TABLE.put(bm, Pair.of(prefix, mat));
                }
            }
        }
    }

    private final BlockMeta pooled = new BlockMeta(Blocks.air, 0);

    @Override
    public boolean supports(Block block, int meta) {
        pooled.setBlock(block);
        pooled.setBlockMeta(meta);

        return MAT_BLOCK_TABLE.containsKey(pooled);
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        IStoneType stoneType = info.stoneType == null ? StoneType.Stone : info.stoneType;

        // noinspection SuspiciousMethodCalls
        return BLOCK_TABLE.contains(stoneType.getPrefix(), info.material);
    }

    @Override
    public OreInfo<Materials> getOreInfo(Block block, int meta) {
        pooled.setBlock(block);
        pooled.setBlockMeta(meta);

        Pair<OrePrefixes, Materials> pair = MAT_BLOCK_TABLE.get(pooled);

        if (pair == null) return null;

        OreInfo<Materials> info = OreInfo.getNewInfo();

        info.stoneType = StoneType.findStoneTypeByPrefix(pair.left());
        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        info.material = pair.right();
        info.isNatural = true;

        info.isSmall = pair.left() == OrePrefixes.oreSmall || pair.left() == OrePrefixes.orePoor;

        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        IStoneType stoneType = info.stoneType == null ? StoneType.Stone : info.stoneType;

        // noinspection SuspiciousMethodCalls
        return BLOCK_TABLE.get(stoneType.getPrefix(), info.material);
    }

    private final GTMockWorld mockWorld = new GTMockWorld();

    @Override
    public @NotNull ArrayList<ItemStack> getOreDrops(Random rng, OreInfo<?> info, boolean silktouch, int fortune) {
        ImmutableBlockMeta bm = getBlock(info);

        if (bm == null) return new ArrayList<>();

        try {
            mockWorld.clear();
            mockWorld.setBlock(0, 0, 0, bm.getBlock(), bm.getBlockMeta(), 0);

            return bm.getBlock()
                .getDrops(mockWorld, 0, 0, 0, bm.getBlockMeta(), fortune);
        } catch (Throwable t) {
            GTMod.GT_FML_LOGGER.error("Could not get drops for ore block: {}", bm, t);

            return new ArrayList<>();
        }
    }

    @Override
    public List<ItemStack> getPotentialDrops(OreInfo<?> info) {
        return Collections.emptyList();
    }
}
