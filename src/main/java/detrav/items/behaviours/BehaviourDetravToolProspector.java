package detrav.items.behaviours;

import static gregtech.api.enums.Mods.VisualProspecting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTLanguageManager;
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

    static final int[] DISTANCEINTS = new int[] { 0, 4, 25, 64 };
    int distTextIndex;

    HashMap<String, Integer> ores;
    int badluck;

    protected final int mCosts;

    static final String CHAT_MSG_SEPARATOR = EnumChatFormatting.STRIKETHROUGH + "--------------------";

    public BehaviourDetravToolProspector(int aCosts) {
        mCosts = aCosts;
    }

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
                addChatMassageByValue(aPlayer, fStack.amount / 2, "a Fluid");

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
        ores = new HashMap<>();

        int range = aItem.getHarvestLevel(aStack, "") / 2 + (aStack.getItemDamage() / 4);
        if (range % 2 == 0) {
            range += 1; // kinda not needed here, divide takes it out, but we put it back in with the range+1 in the
                        // loop
        }
        range = range / 2; // Convert range from diameter to radius

        aPlayer.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + GTLanguageManager.sEnglishFile
                    .get("LanguageFile", "gt.scanner.prospecting", "Prospecting at ")
                    .getString() + EnumChatFormatting.BLUE + "(" + bX + ", " + bZ + ")"));
        for (int x = -range; x < range + 1; ++x) {
            aX = bX + x * 16;
            for (int z = -range; z < range + 1; ++z) {

                aZ = bZ + z * 16;
                int dist = x * x + z * z;

                for (distTextIndex = 0; distTextIndex < DISTANCEINTS.length; distTextIndex++) {
                    if (dist <= DISTANCEINTS[distTextIndex]) {
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

        // List to hold unsorted scanner messages
        List<ChatComponentText> oreMessages = new ArrayList<>();

        for (String key : ores.keySet()) {
            int value = ores.get(key);
            appendChatMessageByValue(oreMessages, aPlayer, value, key);
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

        if (badluck == 0) {
            oreMessages.add(
                new ChatComponentText(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal("detrav.scanner.success")));
        } else {
            oreMessages.add(
                new ChatComponentText(
                    EnumChatFormatting.WHITE + StatCollector.translateToLocal("detrav.scanner.fail")
                        .replace("%badluck", Integer.toString(badluck))));
        }

        // Print the sorted messages
        for (ChatComponentText msg : oreMessagesSorted) {
            aPlayer.addChatMessage(msg);
        }

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
        ores = new HashMap<>();
        aPlayer.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + StatCollector.translateToLocal(
                    "detrav.scanner.prospecting") + EnumChatFormatting.BLUE + "(" + aX + ", " + aZ + ")"));
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

        for (String key : ores.keySet()) {
            int value = ores.get(key);
            addChatMassageByValue(aPlayer, value, key);
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
            addOreToHashMap(blockStack.getDisplayName(), aPlayer);
            if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);
            return;
        }

        ItemData itemData = GTOreDictUnificator.getAssociation(blockStack);

        if (itemData != null) {
            try {
                String name = itemData.toString();
                addChatMassageByValue(aPlayer, -1, name);
                if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);
            } catch (Exception e) {
                addChatMassageByValue(aPlayer, -1, "ERROR, lol ^_^");
            }

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
                                addOreToHashMap(oreStack.getDisplayName(), aPlayer);
                                continue;
                            }
                        }

                        if (data == DetravMetaGeneratedTool01.MODE_ALL_ORES) {
                            ItemStack oreStack = new ItemStack(tBlock, 1, tMetaID);

                            itemData = GTOreDictUnificator.getAssociation(oreStack);
                            if (itemData != null && itemData.mPrefix.toString()
                                .startsWith("ore")) {
                                addOreToHashMap(oreStack.getDisplayName(), aPlayer);
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

    void addOreToHashMap(String orename, EntityPlayer aPlayer) {
        // orename + the textual distance of the ore
        String oreDistance = orename + StatCollector.translateToLocal("detrav.scanner.distance.texts." + distTextIndex);
        if (!ores.containsKey(oreDistance)) {
            if (DetravScannerMod.DEBUG_ENABLED) aPlayer
                .addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + " Adding to oremap " + oreDistance));
            ores.put(oreDistance, 1);
        } else {
            int val = ores.get(oreDistance);
            ores.put(oreDistance, val + 1);
        }
    }

    void addChatMassageByValue(EntityPlayer aPlayer, int value, String name) {
        if (value < 0) {
            aPlayer.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.6") + name));
        } else if (value < 1) {
            aPlayer
                .addChatMessage(new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.0")));
        } else if (value < 10) aPlayer.addChatMessage(
            new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.1")));
        else if (value < 30) aPlayer.addChatMessage(
            new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.2")));
        else if (value < 60) aPlayer.addChatMessage(
            new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.3")));
        else if (value < 100) aPlayer.addChatMessage(
            new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.4")));
        else aPlayer.addChatMessage(
            new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.5")));
    }

    // Same as addChatMassageByValue but appends to a list of chat messages and spelled correctly
    void appendChatMessageByValue(List<ChatComponentText> chatMessageList, EntityPlayer aPlayer, int value,
        String name) {
        if (value < 0) {
            chatMessageList
                .add(new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.6") + name));
        } else if (value < 1) {
            chatMessageList.add(new ChatComponentText(StatCollector.translateToLocal("detrav.scanner.found.texts.0")));
        } else if (value < 10) chatMessageList
            .add(new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.1")));
        else if (value < 30) chatMessageList
            .add(new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.2")));
        else if (value < 60) chatMessageList
            .add(new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.3")));
        else if (value < 100) chatMessageList
            .add(new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.4")));
        else chatMessageList
            .add(new ChatComponentText(name + StatCollector.translateToLocal("detrav.scanner.found.texts.5")));
    }

    public static int getPollution(World aWorld, int aX, int aZ) {
        return Pollution.getPollution(aWorld.getChunkFromBlockCoords(aX, aZ));
    }
}
