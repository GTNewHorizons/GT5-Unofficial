package gregtech.api.util;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Mods.BloodArsenal;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;

import bartworks.common.loaders.ItemRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.VoltageIndex;
import gregtech.common.misc.GTStructureChannels;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;

public class GlassTier {

    private static final Comparator<Pair<Integer, Integer>> tierComparator = Comparator
        .comparing(Pair<Integer, Integer>::getLeft)
        .thenComparing(Pair::getRight);
    // For default tier ordering, so the primary (borosilicate) glasses come before the variants
    private static final int minTier = VoltageIndex.HV;
    private static final int maxTier = VoltageIndex.UXV;
    private static final Map<Pair<Integer, Integer>, BlockMeta> tierAndSubtierToGlass = new TreeMap<>(tierComparator);
    private static final Int2ObjectOpenHashMap<LinkedList<BlockMeta>> tierToGlasses = new Int2ObjectOpenHashMap<>();
    private static final HashMap<BlockMeta, Pair<Integer, Integer>> glassToTierAndIndex = new HashMap<>();
    private static final Pair<Integer, Integer> defaultGlassTier = Pair.of(null, 0);
    private static final List<BlockMeta> mainGlass = new ArrayList<>(Collections.nCopies(maxTier + 1 - minTier, null));
    private static final List<Pair<Block, Integer>> glassList = new ArrayList<>();
    private static BlockMeta[][] glassesByChannel = new BlockMeta[getNumGlassTiers()][0];
    private static boolean glassesComputed = false;

    /**
     * Register a glass as a tiered glass.
     *
     * @param modname              The modid owning the block
     * @param unlocalisedBlockName The name of the block itself
     * @param meta                 The meta of the block
     * @param tier                 The glasses Tier = Voltage tier (MIN 3)
     * @param subtier              Where the glass falls within a tier (must be unique, increasing 0,1,2...)
     */
    public static void addCustomGlass(String modname, String unlocalisedBlockName, int meta, int tier, int subtier) {
        Block block = findBlock(modname, unlocalisedBlockName);
        if (block != null) {
            addCustomGlass(block, meta, tier, subtier);
        } else {
            new IllegalArgumentException(
                "Block: " + unlocalisedBlockName
                    + " of the Mod: "
                    + modname
                    + " was NOT found when attempting to register a glass!").printStackTrace();
        }
    }

    public static void addCustomGlass(@NotNull Block block, int meta, int tier, int subtier) {
        Objects.requireNonNull(block, "Glass block cannot be null");
        glassesComputed = false;
        GlassTier.glassToTierAndIndex.put(new BlockMeta(block, meta), Pair.of(tier, -1));
        GlassTier.tierAndSubtierToGlass.put(Pair.of(tier, subtier), new BlockMeta(block, meta));
        LinkedList<BlockMeta> tierList = tierToGlasses.computeIfAbsent(tier, x -> new LinkedList<>());
        BlockMeta blockPair = new BlockMeta(block, meta);
        if (subtier == 0) {
            mainGlass.set(tier - minTier, new BlockMeta(block, meta));
            tierList.addFirst(blockPair);
        } else {
            tierList.addLast(blockPair);
        }
    }

    /**
     * Used for determining maximum tier of a multiblock, extra glass type should not be considered tiered.
     */
    public static int getMaxTierIndex() {
        return maxTier - minTier + 1;
    }

    /**
     * Gets the tier of the glass represented by the block:meta passed. If passed non-glass or glass without a tier,
     * returns null.
     *
     * @param block
     * @param meta
     * @return Integer glass tier or null
     */
    @Nullable
    public static Integer getGlassBlockTier(Block block, int meta) {
        return glassToTierAndIndex.getOrDefault(new BlockMeta(block, meta), defaultGlassTier)
            .getLeft();
    }

    public static int getGlassChannelValue(Block block, int meta) {
        return glassToTierAndIndex.getOrDefault(new BlockMeta(block, meta), defaultGlassTier)
            .getRight();
    }

    public static int getNumGlassTiers() {
        return maxTier - minTier + 1;
    }

    private static void computeGlassLists() {
        if (glassesComputed) return;

        // replace with a 2d array to speedup lookups
        final List<BlockMeta[]> newGlassesByChannel = new ArrayList<BlockMeta[]>();
        for (int i = minTier; i <= maxTier; i++) {
            LinkedList<BlockMeta> list = tierToGlasses.getOrDefault(i, null);
            if (list == null) {
                throw new IllegalStateException("No glasses registered for tier " + VN[i]);
            } else {
                newGlassesByChannel.add(list.toArray(new BlockMeta[0]));
            }
        }

        int ctr = 1 + getNumGlassTiers(); // For channel index, starts at numGlassTier + 1
        int tierCounter = 1;
        for (BlockMeta glass : mainGlass) {
            glassList.add(Pair.of(glass.getBlock(), glass.getBlockMeta()));
            glassToTierAndIndex.put(glass, Pair.of(getGlassBlockTier(glass.getBlock(), glass.getBlockMeta()), ctr));
            newGlassesByChannel.add(new BlockMeta[] { glass });
            GTStructureChannels.BOROGLASS
                .registerAsIndicator(new ItemStack(glass.getBlock(), 1, glass.getBlockMeta()), tierCounter++);
            GTStructureChannels.BOROGLASS
                .registerAsIndicator(new ItemStack(glass.getBlock(), 1, glass.getBlockMeta()), ctr);
            ctr++;
        }
        for (Map.Entry<Pair<Integer, Integer>, BlockMeta> entry : tierAndSubtierToGlass.entrySet()) {
            if (entry.getKey()
                .getRight() == 0) continue;
            BlockMeta glass = entry.getValue();
            glassList.add(Pair.of(glass.getBlock(), glass.getBlockMeta()));
            glassToTierAndIndex.put(
                glass,
                Pair.of(
                    entry.getKey()
                        .getLeft(),
                    ctr));
            newGlassesByChannel.add(new BlockMeta[] { glass });
            GTStructureChannels.BOROGLASS.registerAsIndicator(
                new ItemStack(glass.getBlock(), 1, glass.getBlockMeta()),
                entry.getKey()
                    .getLeft() - minTier
                    + 1);
            GTStructureChannels.BOROGLASS
                .registerAsIndicator(new ItemStack(glass.getBlock(), 1, glass.getBlockMeta()), ctr);

            ctr++;
        }

        glassesByChannel = newGlassesByChannel.toArray(new BlockMeta[0][]);
        glassesComputed = true;
    }

    public static BlockMeta[][] getGlassesByChannel() {
        computeGlassLists();
        return glassesByChannel;
    }

    public static List<Pair<Block, Integer>> getGlassList() {
        computeGlassLists();
        return glassList;
    }

    // Register all your glasses here.
    public static class RegisterGlassTiers {

        public static void run() {
            registerGlassAsTiered();
            registerGlassOreDicts();
        }

        private static void registerGlassAsTiered() {

            // --- HV ---
            addCustomGlass(ItemRegistry.bw_realglas, 0, 3, 0);
            addCustomGlass(Blocks.glass, 0, 3, 1);
            if (EnderIO.isModLoaded()) {
                for (int i = 0; i < 6; i++) {
                    addCustomGlass(EnderIO.ID, "blockFusedQuartz", i, 3, 2 + i);
                }
            }
            if (Thaumcraft.isModLoaded()) {
                // Warded glass
                addCustomGlass(Thaumcraft.ID, "blockCosmeticOpaque", 2, 3, 8);
            }

            // --- EV ---
            addCustomGlass(ItemRegistry.bw_realglas, 1, 4, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 0, 4, 1);
            for (int i = 0; i < 4; i++) {
                addCustomGlass(GregTechAPI.sBlockTintedGlass, i, 4, i + 2);
            }
            addCustomGlass(GregTechAPI.sBlockGlass1, 10, 4, 6);
            if (BloodArsenal.isModLoaded()) {
                addCustomGlass(BloodArsenal.ID, "blood_stained_glass", 0, 4, 7);
            }
            if (Botania.isModLoaded()) {
                addCustomGlass(Botania.ID, "manaGlass", 0, 4, 8);
            }

            // --- IV ---
            addCustomGlass(ItemRegistry.bw_realglas, 2, 5, 0);
            // Thorium-Yttrium
            addCustomGlass(ItemRegistry.bw_realglas2, 0, 5, 1);
            if (Botania.isModLoaded()) {
                addCustomGlass(Botania.ID, "elfGlass", 0, 5, 2);
                addCustomGlass(Botania.ID, "bifrostPerm", 0, 5, 3);
            }

            // --- LuV ---
            addCustomGlass(ItemRegistry.bw_realglas, 3, 6, 0);

            // --- ZPM ---
            addCustomGlass(ItemRegistry.bw_realglas, 4, 7, 0);

            // --- UV ---
            addCustomGlass(ItemRegistry.bw_realglas, 5, 8, 0);
            addCustomGlass(BlockQuantumGlass.INSTANCE, 0, 8, 1);

            // --- UHV ---
            addCustomGlass(ItemRegistry.bw_realglas, 6, 9, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 1, 9, 1);
            addCustomGlass(GregTechAPI.sBlockGlass1, 2, 9, 2);
            addCustomGlass(GregTechAPI.sBlockGlass1, 7, 9, 3);
            addCustomGlass(GregTechAPI.sBlockGlass1, 8, 9, 4);

            // --- UEV ---
            addCustomGlass(ItemRegistry.bw_realglas, 7, 10, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 3, 10, 1);

            // --- UIV ---
            addCustomGlass(ItemRegistry.bw_realglas, 8, 11, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 4, 11, 1);
            addCustomGlass(Loaders.antimatterContainmentCasing, 0, 11, 2);

            // --- UMV ---
            addCustomGlass(ItemRegistry.bw_realglas, 9, 12, 0);
            addCustomGlass(BlockGodforgeGlass.INSTANCE, 0, 12, 1);
            addCustomGlass(GregTechAPI.sBlockGlass1, 9, 12, 2);
            addCustomGlass(GregTechAPI.sBlockGlass1, 5, 12, 3);

            // --- UXV ---
            addCustomGlass(ItemRegistry.bw_realglas, 10, 13, 0);
        }

        private static void registerGlassOreDicts() {

            // Register glass ore dict entries.
            for (Map.Entry<BlockMeta, Pair<Integer, Integer>> entry : glassToTierAndIndex.entrySet()) {
                String oreName = "blockGlass" + VN[entry.getValue()
                    .getLeft()];
                ItemStack itemStack = new ItemStack(
                    entry.getKey()
                        .getBlock(),
                    1,
                    entry.getKey()
                        .getBlockMeta());
                OreDictionary.registerOre(oreName, itemStack);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class GlassTooltipHandler {

        @SideOnly(Side.CLIENT)
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void getTooltip(ItemTooltipEvent event) {

            if (event == null || event.itemStack == null || event.itemStack.getItem() == null) return;

            final Block block = Block.getBlockFromItem(event.itemStack.getItem());
            final int meta = event.itemStack.getItemDamage();

            Integer tier = getGlassBlockTier(block, meta);
            if (tier == null) return;

            event.toolTip.add(
                StatCollector.translateToLocal("tooltip.glass_tier.0.name") + " "
                    + getColoredTierNameFromTier(tier.byteValue()));
        }
    }
}
