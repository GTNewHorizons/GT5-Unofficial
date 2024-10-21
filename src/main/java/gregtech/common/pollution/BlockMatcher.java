package gregtech.common.pollution;

import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.event.world.WorldEvent;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import gregtech.GTMod;

// Shamelessly Taken from BetterFoliage by octarine-noise
public class BlockMatcher {

    public Map<Class<?>, ColorOverrideType> whiteList = Maps.newHashMap();
    public Set<Class<?>> blackList = Sets.newHashSet();
    public Map<Integer, ColorOverrideType> blockIDs = Maps.newHashMap();

    public ColorOverrideType matchesID(int blockId) {
        return blockIDs.get(blockId);
    }

    public ColorOverrideType matchesID(Block block) {
        return blockIDs.get(Block.blockRegistry.getIDForObject(block));
    }

    public void updateClassList(String[] cfg) {
        whiteList.clear();
        blackList.clear();
        for (String line : cfg) {
            GTMod.GT_FML_LOGGER.info("Checking for block:" + line);
            String[] lines = line.split(":");
            ColorOverrideType type = null;
            if (lines.length > 1) {
                try {
                    type = ColorOverrideType.fromName(lines[1].trim());
                } catch (NumberFormatException e) {
                    GTMod.GT_FML_LOGGER.error(String.format("Invalid type [%s]", line));
                    continue;
                }
            }

            if (lines[0].startsWith("-")) {
                try {
                    blackList.add(Class.forName(lines[0].substring(1)));
                    GTMod.GT_FML_LOGGER.info("\t added blacklist:" + lines[0].substring(1));
                } catch (ClassNotFoundException ignored) {}
            } else {
                if (type == null) {
                    GTMod.GT_FML_LOGGER.error(String.format("Invalid type [%s]", line));
                    continue;
                }

                try {
                    whiteList.put(Class.forName(lines[0]), type);
                    GTMod.GT_FML_LOGGER.info("\t added whitelist:" + lines[0]);
                } catch (ClassNotFoundException ignored) {}
            }
        }
        // updateBlockIDs();
    }

    private void updateBlockIDs() {
        blockIDs.clear();
        FMLControlledNamespacedRegistry<Block> blockRegistry = GameData.getBlockRegistry();
        for (Block block : blockRegistry.typeSafeIterable()) {
            ColorOverrideType t = matchesClass(block);
            if (t != null) blockIDs.put(Block.blockRegistry.getIDForObject(block), t);
        }
    }

    private ColorOverrideType matchesClass(Block block) {
        for (Class<?> clazz : blackList) if (clazz.isAssignableFrom(block.getClass())) return null;
        for (Class<?> clazz : whiteList.keySet())
            if (clazz.isAssignableFrom(block.getClass())) return whiteList.get(clazz);
        return null;
    }

    /**
     * Caches block IDs on world load for fast lookup
     *
     * @param event
     */
    @SubscribeEvent
    public void handleWorldLoad(WorldEvent.Load event) {
        if (event.world instanceof WorldClient) {
            updateBlockIDs();
        }
    }
}
