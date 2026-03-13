package gregtech.api.util;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Mods.BloodArsenal;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

import bartworks.common.loaders.ItemRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.VoltageIndex;
import gregtech.common.misc.GTStructureChannels;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;

public class GlassTier {

    private static final Comparator<Pair<Integer, Integer>> tierComparator = Comparator
        .comparing(Pair<Integer, Integer>::getLeft)
        .thenComparing(Pair::getRight);
    private static final Map<Pair<Integer, Integer>, Pair<Block, Integer>> tierToGlass = new TreeMap<>(tierComparator);
    private static final HashMap<Pair<Block, Integer>, Pair<Integer, Integer>> glassToTierAndIndex = new HashMap<>();
    // For default tier ordering, so the primary (borosilicate) glasses come before the variants
    private static final int minTier = VoltageIndex.HV;
    private static final int maxTier = VoltageIndex.UMV;
    private static final Pair<Integer, Integer> defaultGlassTier = Pair.of(null, 0);
    private static final List<Pair<Block, Integer>> mainGlass = new ArrayList<>(
        Collections.nCopies(maxTier + 1 - minTier, null));
    private static final List<Pair<Block, Integer>> glassList = new ArrayList<>();

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
        GlassTier.glassToTierAndIndex.put(Pair.of(block, meta), Pair.of(tier, -1));
        GlassTier.tierToGlass.put(Pair.of(tier, subtier), Pair.of(block, meta));
        if (subtier == 0) {
            mainGlass.set(tier - minTier, Pair.of(block, meta));
        }
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
    public static int getGlassBlockTier(Block block, int meta) {
        Pair<Integer, Integer> p = glassToTierAndIndex.get(Pair.of(block, meta));
        if (p == null || p.getLeft() == null) {
            return 0; // prevents a -1
        }
        return p.getLeft();
    }

    public static int getGlassChannelValue(Block block, int meta) {
        return glassToTierAndIndex.getOrDefault(Pair.of(block, meta), defaultGlassTier)
            .getRight();
    }

    public static List<Pair<Block, Integer>> getGlassList() {
        if (glassList.isEmpty()) {
            int ctr = 1; // For channel index, starts at 1
            for (Pair<Block, Integer> glass : mainGlass) {
                glassList.add(glass);
                glassToTierAndIndex.put(glass, Pair.of(getGlassBlockTier(glass.getLeft(), glass.getRight()), ctr));
                GTStructureChannels.BOROGLASS
                    .registerAsIndicator(new ItemStack(glass.getLeft(), 1, glass.getRight()), ctr);
                ctr++;
            }
            for (Map.Entry<Pair<Integer, Integer>, Pair<Block, Integer>> entry : tierToGlass.entrySet()) {
                if (entry.getKey()
                    .getRight() == 0) continue;
                Pair<Block, Integer> glass = entry.getValue();
                glassList.add(glass);
                glassToTierAndIndex.put(
                    glass,
                    Pair.of(
                        entry.getKey()
                            .getLeft(),
                        ctr));
                GTStructureChannels.BOROGLASS
                    .registerAsIndicator(new ItemStack(glass.getLeft(), 1, glass.getRight()), ctr);
                ctr++;
            }
            glassList.add(mainGlass.get(mainGlass.size() - 1));
        }
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
            addCustomGlass(IndustrialCraft2.ID, "blockAlloyGlass", 0, 4, 6);
            if (BloodArsenal.isModLoaded()) {
                addCustomGlass(BloodArsenal.ID, "blood_stained_glass", 0, 4, 7);
            }
            if (Botania.isModLoaded()) {
                addCustomGlass(Botania.ID, "manaGlass", 0, 4, 8);
            }

            // --- IV ---
            addCustomGlass(ItemRegistry.bw_realglas, 2, 5, 0);
            // Thorium-Yttrium
            addCustomGlass(ItemRegistry.bw_realglas, 12, 5, 1);
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
            addCustomGlass(ItemRegistry.bw_realglas, 13, 9, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 1, 9, 1);
            addCustomGlass(GregTechAPI.sBlockGlass1, 2, 9, 2);

            // --- UEV ---
            addCustomGlass(ItemRegistry.bw_realglas, 14, 10, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 7, 10, 1);
            addCustomGlass(GregTechAPI.sBlockGlass1, 3, 10, 2);

            // --- UIV ---
            addCustomGlass(ItemRegistry.bw_realglas, 15, 11, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 4, 11, 1);

            // --- UMV ---
            addCustomGlass(ItemRegistry.bw_realglas2, 0, 12, 0);
            addCustomGlass(BlockGodforgeGlass.INSTANCE, 0, 12, 1);
            addCustomGlass(Loaders.antimatterContainmentCasing, 0, 12, 2);
            addCustomGlass(GregTechAPI.sBlockGlass1, 5, 12, 3);
        }

        private static void registerGlassOreDicts() {

            // Register glass ore dict entries.
            for (Map.Entry<Pair<Block, Integer>, Pair<Integer, Integer>> entry : glassToTierAndIndex.entrySet()) {
                String oreName = "blockGlass" + VN[entry.getValue()
                    .getLeft()];
                ItemStack itemStack = new ItemStack(
                    entry.getKey()
                        .getLeft(),
                    1,
                    entry.getKey()
                        .getRight());
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
