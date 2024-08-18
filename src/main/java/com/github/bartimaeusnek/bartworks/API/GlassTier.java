package com.github.bartimaeusnek.bartworks.API;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;

import java.util.HashMap;
import java.util.Objects;

import net.minecraft.block.Block;

import org.jetbrains.annotations.NotNull;

public class GlassTier {

    private static final HashMap<BlockMetaPair, Integer> glasses = new HashMap<>();

    /**
     * @param modname              The modid owning the block
     * @param unlocalisedBlockName The name of the block itself
     * @param meta                 The meta of the block
     * @param tier                 the glasses Tier = Voltage tier (MIN 3)
     */
    public static void addCustomGlass(String modname, String unlocalisedBlockName, int meta, int tier) {
        Block block = findBlock(modname, unlocalisedBlockName);
        if (block != null) {
            addCustomGlass(block, meta, tier);
        } else {
            new IllegalArgumentException(
                "Block: " + unlocalisedBlockName
                    + " of the Mod: "
                    + modname
                    + " was NOT found when attempting to register a glass!").printStackTrace();
        }
    }

    public static void addCustomGlass(@NotNull Block block, int meta, int tier) {
        GlassTier.glasses.put(new BlockMetaPair(block, (byte) meta), tier);
    }

    public static HashMap<BlockMetaPair, Integer> getGlassMap() {
        return glasses;
    }

    public static int getGlassTier(Block block, int meta) {
        return glasses.getOrDefault(new BlockMetaPair(block, (byte) meta), 0);
    }

    public static class BlockMetaPair {

        private final Block block;
        private final int meta;

        public BlockMetaPair(Block block, int aByte) {
            this.block = block;
            this.meta = aByte;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            BlockMetaPair that = (BlockMetaPair) o;
            return Objects.equals(this.getBlock(), that.getBlock()) && Objects.equals(this.getMeta(), that.getMeta());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getBlock(), this.getMeta());
        }

        public Block getBlock() {
            return this.block;
        }

        public int getMeta() {
            return this.meta;
        }
    }
}
