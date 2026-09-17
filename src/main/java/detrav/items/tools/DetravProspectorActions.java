package detrav.items.tools;

import static gregtech.api.enums.Mods.VisualProspecting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import com.sinthoras.visualprospecting.VisualProspecting_API;

import detrav.DetravScannerMod;
import detrav.enums.DetravScannerMode;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.UndergroundOil;
import gregtech.common.items.tools.ToolItemBase;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gregtech.common.pollution.Pollution;

/**
 * The prospecting half of the hand Prospector's Scanner, moved out of {@code BehaviourDetravToolProspector}.
 * <p/>
 * One of these is built per use rather than once per tool type: the old behaviour object was shared by every player
 * holding a scanner and kept the running scan's findings in its own fields, so two players scanning at the same time
 * could read each other's results.
 */
public class DetravProspectorActions {

    static final int[] DISTANCE_INTS = new int[] { 0, 4, 25, 64 };

    static final String CHAT_MSG_SEPARATOR = EnumChatFormatting.STRIKETHROUGH + "--------------------";

    /** Durability cost of scanning one chunk, in the unit where 100 is one durability point. */
    protected final int costs;

    /**
     * The metadata this tier used to occupy on {@code detrav.metatool.01}. The scan range and the per-chunk success
     * chance were both computed from it, and metadata is the crafting material now, so the tier carries the old value
     * along instead and the numbers come out the same.
     */
    protected final int legacyMeta;

    private int distTextIndex;
    private Map<String, Integer> ores;
    private int badluck;

    public DetravProspectorActions(int costs, int legacyMeta) {
        this.costs = costs;
        this.legacyMeta = legacyMeta;
    }

    public boolean onItemUse(ToolItemBase item, ItemStack stack, EntityPlayer player, World world, int x, int y,
        int z) {
        SplittableRandom random = new SplittableRandom();
        int chance = Math.min(((1 + legacyMeta) * 8), 100);

        if (world.isRemote) return false;

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (block == Blocks.bedrock) {
            if (random.nextInt(100) < chance) {
                FluidStack fluid = UndergroundOil.undergroundOil(world.getChunkFromBlockCoords(x, z), -1);
                addChatMessageByValue(player, fluid.amount / 2, "a Fluid");

                if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs);

                if (VisualProspecting.isModLoaded()) {
                    VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                        (EntityPlayerMP) player,
                        new ArrayList<>(),
                        VisualProspecting_API.LogicalServer
                            .prospectUndergroundFluidsWithingRadius(world, (int) player.posX, (int) player.posZ, 0));
                }
            }
            return true;
        }

        if (block.getMaterial() == Material.rock || block.getMaterial() == Material.ground
            || GTUtility.isOre(block, meta)) {
            prospectChunks(item, stack, player, world, x, y, z, random, chance);

            return true;
        }

        return false;
    }

    protected void prospectChunks(ToolItemBase item, ItemStack stack, EntityPlayer player, World world, int aX, int aY,
        int aZ, SplittableRandom random, int chance) {
        int bX = aX;
        int bZ = aZ;

        badluck = 0;
        ores = new HashMap<>();

        int range = item.getHarvestLevel(stack, "") / 2 + (legacyMeta / 4);
        if (range % 2 == 0) {
            range += 1; // kinda not needed here, divide takes it out, but we put it back in with the range+1 in the
                        // loop
        }
        range = range / 2; // Convert range from diameter to radius

        player.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + GTLanguageManager.sEnglishFile
                    .get("LanguageFile", "gt.scanner.prospecting", "Prospecting at ")
                    .getString() + EnumChatFormatting.BLUE + "(" + bX + ", " + bZ + ")"));
        for (int x = -range; x < range + 1; ++x) {
            aX = bX + x * 16;
            for (int z = -range; z < range + 1; ++z) {

                aZ = bZ + z * 16;
                int dist = x * x + z * z;

                for (distTextIndex = 0; distTextIndex < DISTANCE_INTS.length; distTextIndex++) {
                    if (dist <= DISTANCE_INTS[distTextIndex]) {
                        break;
                    }
                }

                if (DetravScannerMod.DEBUG_ENABLED) {
                    player.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.YELLOW + "Chunk at "
                                + aX
                                + "|"
                                + aZ
                                + " to "
                                + (aX + 16)
                                + "|"
                                + (aZ + 16)
                                + StatCollector.translateToLocal("detrav.scanner.distance.texts." + distTextIndex)));
                }

                processOreProspecting(item, stack, player, world, aX, aY, aZ, random, chance);
            }
        }

        // List to hold unsorted scanner messages
        List<ChatComponentText> oreMessages = new ArrayList<>();

        for (String key : ores.keySet()) {
            int value = ores.get(key);
            appendChatMessageByValue(oreMessages, value, key);
        }

        // Define sort order by distance
        List<String> sortOrder = Arrays.asList(
            StatCollector.translateToLocal("detrav.scanner.distance.texts.4"),
            StatCollector.translateToLocal("detrav.scanner.distance.texts.3"),
            StatCollector.translateToLocal("detrav.scanner.distance.texts.2"),
            StatCollector.translateToLocal("detrav.scanner.distance.texts.1"),
            StatCollector.translateToLocal("detrav.scanner.distance.texts.0"));

        List<ChatComponentText> oreMessagesSorted = new ArrayList<>();
        oreMessagesSorted.add(new ChatComponentText(CHAT_MSG_SEPARATOR));

        // Sort ore messages by distance, separated by -----
        for (String oreFrequency : sortOrder) {
            for (ChatComponentText msg : oreMessages) {
                if (msg.getChatComponentText_TextValue()
                    .contains(oreFrequency)) {
                    oreMessagesSorted.add(msg);
                }
            }

            // Only append ----- separator if text has been added
            if (!oreMessagesSorted.get(oreMessagesSorted.size() - 1)
                .getChatComponentText_TextValue()
                .contains(CHAT_MSG_SEPARATOR)) {
                oreMessagesSorted.add(new ChatComponentText(CHAT_MSG_SEPARATOR));
            }
        }

        // Appended to the sorted list, which is the one that gets printed. The old behaviour put this on the
        // unsorted list after it had been read, so the player was never told how the scan went.
        if (badluck == 0) {
            oreMessagesSorted.add(
                new ChatComponentText(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal("detrav.scanner.success")));
        } else {
            oreMessagesSorted.add(
                new ChatComponentText(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal("detrav.scanner.fail")
                        .replace("%badluck", Integer.toString(badluck))));
        }

        // Print the sorted messages
        for (ChatComponentText msg : oreMessagesSorted) {
            player.addChatMessage(msg);
        }

        if (VisualProspecting.isModLoaded()) {
            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                (EntityPlayerMP) player,
                VisualProspecting_API.LogicalServer.prospectOreVeinsWithinRadius(
                    world.provider.dimensionId,
                    (int) player.posX,
                    (int) player.posZ,
                    range * 16),
                new ArrayList<>());
        }
    }

    // Used by the electric scanner when scanning the chunk whacked by the scanner. 100% chance find rate
    protected void prospectSingleChunk(ToolItemBase item, ItemStack stack, EntityPlayer player, World world, int x,
        int y, int z) {
        ores = new HashMap<>();
        player.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + StatCollector.translateToLocal(
                    "detrav.scanner.prospecting") + EnumChatFormatting.BLUE + "(" + x + ", " + z + ")"));
        processOreProspecting(item, stack, player, world, x, y, z, new SplittableRandom(), 1000);

        for (String key : ores.keySet()) {
            int value = ores.get(key);
            addChatMessageByValue(player, value, key);
        }

        if (VisualProspecting.isModLoaded()) {
            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                (EntityPlayerMP) player,
                VisualProspecting_API.LogicalServer
                    .prospectOreVeinsWithinRadius(world.provider.dimensionId, (int) player.posX, (int) player.posZ, 0),
                new ArrayList<>());
        }
    }

    protected void processOreProspecting(ToolItemBase item, ItemStack stack, EntityPlayer player, World world, int x,
        int y, int z, SplittableRandom random, int chance) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        ItemStack blockStack = new ItemStack(block, 1, meta);

        if (GTUtility.isOre(block, meta)) {
            addOreToHashMap(blockStack.getDisplayName(), player);
            if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs);
            return;
        }

        ItemData itemData = GTOreDictUnificator.getAssociation(blockStack);

        if (itemData != null) {
            try {
                String name = itemData.toString();
                addChatMessageByValue(player, -1, name);
                if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs);
            } catch (Exception e) {
                addChatMessageByValue(player, -1, "ERROR, lol ^_^");
            }

            return;
        }

        if (random.nextInt(100) < chance) {
            final int data = item.getMode(stack);

            for (int cx = 0; cx < 16; cx++) {
                for (int cz = 0; cz < 16; cz++) {
                    int ySize = chunk.getHeightValue(cx, cz);
                    for (int cy = 1; cy < ySize; cy++) {

                        Block tBlock = chunk.getBlock(cx, cy, cz);
                        short tMetaID = (short) chunk.getBlockMetadata(cx, cy, cz);

                        try (OreInfo<?> info = OreManager.getOreInfo(tBlock, tMetaID)) {
                            if (info != null) {
                                if (!info.isNatural) continue;
                                if (data != DetravScannerMode.ALL_ORES && info.isSmall) continue;

                                ItemStack oreStack = new ItemStack(tBlock, 1, tMetaID);
                                addOreToHashMap(oreStack.getDisplayName(), player);
                                continue;
                            }
                        }

                        if (data == DetravScannerMode.ALL_ORES) {
                            ItemStack oreStack = new ItemStack(tBlock, 1, tMetaID);

                            itemData = GTOreDictUnificator.getAssociation(oreStack);
                            if (itemData != null && itemData.mPrefix.toString()
                                .startsWith("ore")) {
                                addOreToHashMap(oreStack.getDisplayName(), player);
                            }
                        }
                    }
                }
            }

            if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs);

            return;
        }

        if (DetravScannerMod.DEBUG_ENABLED)
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + " Failed on this chunk"));
        badluck++;
        if (!player.capabilities.isCreativeMode) item.doDamage(stack, this.costs / 4);
    }

    void addOreToHashMap(String orename, EntityPlayer player) {
        // orename + the textual distance of the ore
        String oreDistance = orename + StatCollector.translateToLocal("detrav.scanner.distance.texts." + distTextIndex);
        if (!ores.containsKey(oreDistance)) {
            if (DetravScannerMod.DEBUG_ENABLED) player
                .addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + " Adding to oremap " + oreDistance));
            ores.put(oreDistance, 1);
        } else {
            int val = ores.get(oreDistance);
            ores.put(oreDistance, val + 1);
        }
    }

    void addChatMessageByValue(EntityPlayer player, int value, String name) {
        player.addChatMessage(describe(value, name));
    }

    // Same as addChatMessageByValue, but appends to a list of chat messages so they can be sorted before printing
    void appendChatMessageByValue(List<ChatComponentText> chatMessageList, int value, String name) {
        chatMessageList.add(describe(value, name));
    }

    private static ChatComponentText describe(int value, String name) {
        if (value < 0) {
            return new ChatComponentText(StatCollector.translateToLocalFormatted("detrav.scanner.found.texts.6", name));
        }
        if (value < 1) return new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.0"));
        if (value < 10)
            return new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.1"));
        if (value < 30)
            return new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.2"));
        if (value < 60)
            return new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.3"));
        if (value < 100)
            return new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.4"));
        return new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.5"));
    }

    public static int getPollution(World world, int x, int z) {
        return Pollution.getPollution(world.getChunkFromBlockCoords(x, z));
    }
}
