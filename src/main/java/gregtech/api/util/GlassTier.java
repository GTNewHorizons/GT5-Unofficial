package gregtech.api.util;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.Mods.BloodArsenal;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTechAPI;
import net.minecraft.block.Block;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.VoltageIndex;
import tectech.thing.block.BlockQuantumGlass;

public class GlassTier {

    private static final Comparator<Pair<Integer, Integer>> tierComparator = Comparator
        .comparing(Pair<Integer, Integer>::getLeft)
        .thenComparing(Pair::getRight);
    private static final Map<Pair<Integer, Integer>, Pair<Block, Integer>> tierToGlass = new TreeMap<>(tierComparator);
    private static final HashMap<Pair<Block, Integer>, Integer> glassToTier = new HashMap<>();
    // For default tier ordering, so the primary (borosilicate) glasses come before the variants
    private static final int minTier = VoltageIndex.HV;
    private static final int maxTier = VoltageIndex.UMV;
    private static final List<Pair<Block, Integer>> mainGlass = new ArrayList<>(
        Collections.nCopies(maxTier + 1 - minTier, null));
    private static final List<Pair<Block, Integer>> glassList = new ArrayList<>();

    /**
     * @param modname              The modid owning the block
     * @param unlocalisedBlockName The name of the block itself
     * @param meta                 The meta of the block
     * @param tier                 the glasses Tier = Voltage tier (MIN 3)
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
        GlassTier.glassToTier.put(Pair.of(block, meta), tier);
        GlassTier.tierToGlass.put(Pair.of(tier, subtier), Pair.of(block, meta));
        if (subtier == 0) {
            mainGlass.set(tier - minTier, Pair.of(block, meta));
        }
    }

    public static HashMap<Pair<Block, Integer>, Integer> getGlassMap() {
        return glassToTier;
    }

    public static int getGlassTier(Block block, int meta) {
        return glassToTier.getOrDefault(Pair.of(block, meta), 0);
    }

    public static List<Pair<Block, Integer>> getGlassList() {
        if (glassList.isEmpty()) {
            glassList.addAll(mainGlass);
            for (Map.Entry<Pair<Integer, Integer>, Pair<Block, Integer>> entry : tierToGlass.entrySet()) {
                if (entry.getKey()
                    .getRight() == 0) continue;
                glassList.add(entry.getValue());
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
            // Stained boro glass
            for (int i = 0; i < 6; ++i) {
                addCustomGlass(ItemRegistry.bw_realglas, i + 6, 3, i + 1);
            }
            if (EnderIO.isModLoaded()) {
                GlassTier.addCustomGlass(EnderIO.ID, "blockFusedQuartz", 0, 3, 7);
            }
            if (Thaumcraft.isModLoaded()) {
                // Warded glass
                addCustomGlass(Thaumcraft.ID, "blockCosmeticOpaque", 2, 3, 8);
            }

            // --- EV ---
            addCustomGlass(ItemRegistry.bw_realglas, 1, 4, 0);
            for (int i = 0; i < 4; i++) {
                addCustomGlass(GregTechAPI.sBlockTintedGlass, i, 4, i + 1);
            }
            addCustomGlass(GregTechAPI.sBlockGlass1, 0, 4, 1);
            addCustomGlass(IndustrialCraft2.ID, "blockAlloyGlass", 0, 4, 5);
            if (BloodArsenal.isModLoaded()) {
                addCustomGlass(BloodArsenal.ID, "blood_stained_glass", 0, 4, 6);
            }
            if (Botania.isModLoaded()) {
                addCustomGlass(Botania.ID, "manaGlass", 0, 4, 7);
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
            addCustomGlass(GregTechAPI.sBlockGlass1, 3, 10, 1);

            // --- UIV ---
            addCustomGlass(ItemRegistry.bw_realglas, 15, 11, 0);
            addCustomGlass(GregTechAPI.sBlockGlass1, 4, 11, 1);

            // --- UMV ---
            addCustomGlass(ItemRegistry.bw_realglas2, 0, 12, 0);
        }

        private static void registerGlassOreDicts() {

            // Register glass ore dict entries.
            for (Map.Entry<Pair<Block, Integer>, Integer> pair : getGlassMap()
                .entrySet()) {
                String oreName = "blockGlass" + VN[pair.getValue()];
                ItemStack itemStack = new ItemStack(
                    pair.getKey()
                        .getLeft(),
                    1,
                    pair.getKey()
                        .getRight());
                OreDictionary.registerOre(oreName, itemStack);
            }
        }
    }
}
