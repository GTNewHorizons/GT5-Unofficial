package detrav.items.behaviours;

import static gregtech.api.enums.Mods.VisualProspecting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.chat.customcomponents.ChatComponentItemName;
import com.sinthoras.visualprospecting.VisualProspecting_API;

import detrav.DetravScannerMod;
import detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.UndergroundOil;
import gregtech.common.items.behaviors.BehaviourNone;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gregtech.common.pollution.Pollution;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolProspector extends BehaviourNone {

    static final int[] DISTANCE_INTS = new int[] { 0, 4, 25, 64 };
    int distTextIndex;

    // Found ores by item id and meta, one map per distance index
    List<Map<String, ItemStack>> ores;
    int badluck;

    protected final int mCosts;

    static final String CHAT_MSG_SEPARATOR = EnumChatFormatting.STRIKETHROUGH + "--------------------";

    public BehaviourDetravToolProspector(int aCosts) {
        mCosts = aCosts;
    }

    @Override
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int aSide, float hitX, float hitY, float hitZ) {

        SplittableRandom aRandom = new SplittableRandom();
        int chance = Math.min(((1 + aStack.getItemDamage()) * 8), 100);

        if (aWorld.isRemote) return false;

        Block block = aWorld.getBlock(aX, aY, aZ);
        int meta = aWorld.getBlockMetadata(aX, aY, aZ);

        if (block == Blocks.bedrock) {
            if (!aWorld.isRemote && aRandom.nextInt(100) < chance) {
                FluidStack fStack = UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1);
                addChatMassageByValue(
                    aPlayer,
                    fStack.amount / 2,
                    new ChatComponentTranslation("detrav.scanner.found.fluid"));

                if (!aPlayer.capabilities.isCreativeMode)
                    ((DetravMetaGeneratedTool01) aItem).doDamage(aStack, this.mCosts);

                if (VisualProspecting.isModLoaded()) {
                    VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                        (EntityPlayerMP) aPlayer,
                        new ArrayList<>(),
                        VisualProspecting_API.LogicalServer
                            .prospectUndergroundFluidsWithingRadius(aWorld, (int) aPlayer.posX, (int) aPlayer.posZ, 0));
                }
            }
            return true;
        }

        if (block.getMaterial() == Material.rock || block.getMaterial() == Material.ground
            || GTUtility.isOre(block, meta)) {
            prospectChunks(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aRandom, chance);

            return true;
        }

        return false;
    }

    protected void prospectChunks(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, SplittableRandom aRandom, int chance) {
        int bX = aX;
        int bZ = aZ;

        badluck = 0;
        ores = newOreMaps();

        int range = aItem.getHarvestLevel(aStack, "") / 2 + (aStack.getItemDamage() / 4);
        if (range % 2 == 0) {
            range += 1; // kinda not needed here, divide takes it out, but we put it back in with the range+1 in the
                        // loop
        }
        range = range / 2; // Convert range from diameter to radius

        aPlayer.addChatMessage(prospectingMessage(bX, bZ));
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
                    aPlayer.addChatMessage(
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

                processOreProspecting(
                    (DetravMetaGeneratedTool01) aItem,
                    aStack,
                    aPlayer,
                    aWorld,
                    aX,
                    aY,
                    aZ,
                    aRandom,
                    chance);
            }
        }

        // Print ores from far away to next to you, separated by -----
        aPlayer.addChatMessage(new ChatComponentText(CHAT_MSG_SEPARATOR));
        for (int dist = ores.size() - 1; dist >= 0; dist--) {
            Map<String, ItemStack> group = ores.get(dist);
            if (group.isEmpty()) continue;
            sendOres(aPlayer, group, dist);
            aPlayer.addChatMessage(new ChatComponentText(CHAT_MSG_SEPARATOR));
        }

        IChatComponent result = badluck == 0 ? new ChatComponentTranslation("detrav.scanner.success")
            : new ChatComponentTranslation("detrav.scanner.fail.chunks", badluck);
        result.getChatStyle()
            .setColor(EnumChatFormatting.WHITE);
        aPlayer.addChatMessage(result);

        if (VisualProspecting.isModLoaded()) {
            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                (EntityPlayerMP) aPlayer,
                VisualProspecting_API.LogicalServer.prospectOreVeinsWithinRadius(
                    aWorld.provider.dimensionId,
                    (int) aPlayer.posX,
                    (int) aPlayer.posZ,
                    range * 16),
                new ArrayList<>());
        }
    }

    // Used by Electric scanner when scanning the chunk whacked by the scanner. 100% chance find rate
    protected void prospectSingleChunk(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ) {
        ores = newOreMaps();
        aPlayer.addChatMessage(prospectingMessage(aX, aZ));
        processOreProspecting(
            (DetravMetaGeneratedTool01) aItem,
            aStack,
            aPlayer,
            aWorld,
            aX,
            aY,
            aZ,
            new SplittableRandom(),
            1000);

        for (int dist = 0; dist < ores.size(); dist++) {
            sendOres(aPlayer, ores.get(dist), dist);
        }

        if (VisualProspecting.isModLoaded()) {
            VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                (EntityPlayerMP) aPlayer,
                VisualProspecting_API.LogicalServer.prospectOreVeinsWithinRadius(
                    aWorld.provider.dimensionId,
                    (int) aPlayer.posX,
                    (int) aPlayer.posZ,
                    0),
                new ArrayList<>());
        }
    }

    protected void processOreProspecting(DetravMetaGeneratedTool01 aItem, ItemStack aStack, EntityPlayer aPlayer,
        World world, int x, int y, int z, SplittableRandom aRandom, int chance) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);

        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        ItemStack blockStack = new ItemStack(block, 1, meta);

        if (GTUtility.isOre(block, meta)) {
            addOreToHashMap(blockStack, aPlayer);
            if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);
            return;
        }

        ItemData itemData = GTOreDictUnificator.getAssociation(blockStack);

        if (itemData != null) {
            addChatMassageByValue(aPlayer, -1, new ChatComponentItemName(blockStack));
            if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);

            return;
        }

        if (aRandom.nextInt(100) < chance) {
            final int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack)
                .intValue();

            for (int cx = 0; cx < 16; cx++) {
                for (int cz = 0; cz < 16; cz++) {
                    int ySize = chunk.getHeightValue(cx, cz);
                    for (int cy = 1; cy < ySize; cy++) {

                        Block tBlock = chunk.getBlock(cx, cy, cz);
                        short tMetaID = (short) chunk.getBlockMetadata(cx, cy, cz);

                        try (OreInfo<?> info = OreManager.getOreInfo(tBlock, tMetaID)) {
                            if (info != null) {
                                if (!info.isNatural) continue;
                                if (data != DetravMetaGeneratedTool01.MODE_ALL_ORES && info.isSmall) continue;

                                ItemStack oreStack = new ItemStack(tBlock, 1, tMetaID);
                                addOreToHashMap(oreStack, aPlayer);
                                continue;
                            }
                        }

                        if (data == DetravMetaGeneratedTool01.MODE_ALL_ORES) {
                            ItemStack oreStack = new ItemStack(tBlock, 1, tMetaID);

                            itemData = GTOreDictUnificator.getAssociation(oreStack);
                            if (itemData != null && itemData.mPrefix.toString()
                                .startsWith("ore")) {
                                addOreToHashMap(oreStack, aPlayer);
                            }
                        }
                    }
                }
            }

            if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);

            return;
        }

        if (DetravScannerMod.DEBUG_ENABLED)
            aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + " Failed on this chunk"));
        badluck++;
        if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts / 4);
    }

    void addOreToHashMap(ItemStack ore, EntityPlayer aPlayer) {
        Map<String, ItemStack> group = ores.get(distTextIndex);
        String key = Item.getIdFromItem(ore.getItem()) + ":" + ore.getItemDamage();
        if (DetravScannerMod.DEBUG_ENABLED && !group.containsKey(key))
            aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + " Adding to oremap " + key));
        // stackSize holds the number of ore blocks found
        group.computeIfAbsent(key, k -> {
            ItemStack copy = ore.copy();
            copy.stackSize = 0;
            return copy;
        }).stackSize++;
    }

    /** Sends one chat line per ore; the client localizes the ore name. */
    static void sendOres(EntityPlayer aPlayer, Map<String, ItemStack> group, int dist) {
        for (ItemStack ore : group.values()) {
            aPlayer.addChatMessage(foundMessage(ore.stackSize, new ChatComponentItemName(ore), dist));
        }
    }

    void addChatMassageByValue(EntityPlayer aPlayer, int value, IChatComponent name) {
        aPlayer.addChatMessage(foundMessage(value, name, distTextIndex));
    }

    /** Builds the chat line for a find: the name, its distance and the sample size for the given count. */
    static IChatComponent foundMessage(int value, IChatComponent name, int dist) {
        if (value < 0) return new ChatComponentTranslation("detrav.scanner.found.texts.6", name);
        if (value < 1) return new ChatComponentTranslation("detrav.scanner.found.texts.0");
        int size = value < 10 ? 1 : value < 30 ? 2 : value < 60 ? 3 : value < 100 ? 4 : 5;
        return name.appendSibling(new ChatComponentTranslation("detrav.scanner.distance.texts." + dist))
            .appendSibling(new ChatComponentTranslation("detrav.scanner.found.texts." + size));
    }

    /** Builds the gold "Prospecting at (x, z)" chat line. */
    static IChatComponent prospectingMessage(int x, int z) {
        IChatComponent message = new ChatComponentTranslation("detrav.scanner.prospecting");
        message.getChatStyle()
            .setColor(EnumChatFormatting.GOLD);
        return message.appendSibling(
            new ChatComponentText(" (" + x + ", " + z + ")")
                .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE)));
    }

    /** Returns one empty ore map per distance group, from next to you to far away. */
    static List<Map<String, ItemStack>> newOreMaps() {
        List<Map<String, ItemStack>> maps = new ArrayList<>();
        for (int i = 0; i <= DISTANCE_INTS.length; i++) maps.add(new HashMap<>());
        return maps;
    }

    public static int getPollution(World aWorld, int aX, int aZ) {
        return Pollution.getPollution(aWorld.getChunkFromBlockCoords(aX, aZ));
    }
}
