package detrav.items.behaviours;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import com.sinthoras.visualprospecting.VisualProspecting_API;

import bartworks.system.material.Werkstoff;
import cpw.mods.fml.common.registry.LanguageRegistry;
import detrav.DetravScannerMod;
import detrav.items.DetravMetaGeneratedTool01;
import detrav.utils.BartWorksHelper;
import detrav.utils.GTppHelper;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.Pollution;
import gregtech.common.UndergroundOil;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;
import gregtech.common.items.behaviors.BehaviourNone;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolProspector extends BehaviourNone {

    static final int[] DISTANCEINTS = new int[] { 0, 4, 25, 64 };
    int distTextIndex;

    HashMap<String, Integer> ores;
    int badluck;

    protected final int mCosts;

    static final String CHAT_MSG_SEPARATOR = "--------------------";

    public BehaviourDetravToolProspector(int aCosts) {
        mCosts = aCosts;
    }

    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int aSide, float hitX, float hitY, float hitZ) {

        SplittableRandom aRandom = new SplittableRandom();
        int chance = ((1 + aStack.getItemDamage()) * 8) > 100 ? 100 : (1 + aStack.getItemDamage()) * 8;

        if (aWorld.isRemote) return false;

        if (aWorld.getBlock(aX, aY, aZ) == Blocks.bedrock) {
            if (!aWorld.isRemote && aRandom.nextInt(100) < chance) {
                FluidStack fStack = UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1);
                addChatMassageByValue(aPlayer, fStack.amount / 2, "a Fluid");// fStack.getLocalizedName());
                /*
                 * boolean fluid = GT_UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1)!=null
                 * &&GT_UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1).getFluid()!=null; if
                 * (fluid) aPlayer.addChatMessage(new
                 * ChatComponentText(EnumChatFormatting.GREEN+"You found some liquid.")); else
                 * aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"You found no liquid."));
                 */
                if (!aPlayer.capabilities.isCreativeMode)
                    ((DetravMetaGeneratedTool01) aItem).doDamage(aStack, this.mCosts);

                if (Mods.VisualProspecting.isModLoaded()) {
                    VisualProspecting_API.LogicalServer.sendProspectionResultsToClient(
                        (EntityPlayerMP) aPlayer,
                        new ArrayList<>(),
                        VisualProspecting_API.LogicalServer
                            .prospectUndergroundFluidsWithingRadius(aWorld, (int) aPlayer.posX, (int) aPlayer.posZ, 0));
                }
            }
            return true;
        }
        if (aWorld.getBlock(aX, aY, aZ)
            .getMaterial() == Material.rock
            || aWorld.getBlock(aX, aY, aZ)
                .getMaterial() == Material.ground
            || aWorld.getBlock(aX, aY, aZ) == GregTechAPI.sBlockOres1) {
            if (!aWorld.isRemote) {
                prospectChunks((DetravMetaGeneratedTool01) aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aRandom, chance);
            }
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

        int range = ((DetravMetaGeneratedTool01) aItem).getHarvestLevel(aStack, "") / 2 + (aStack.getItemDamage() / 4);
        if ((range % 2) == 0) {
            range += 1; // kinda not needed here, divide takes it out, but we put it back in with the range+1 in the
                        // loop
        }
        range = range / 2; // Convert range from diameter to radius

        aPlayer.addChatMessage(
            new ChatComponentText(
                EnumChatFormatting.GOLD + GTLanguageManager.sEnglishFile
                    .get("LanguageFile", "gt.scanner.prospecting", "Prospecting at ")
                    .getString() + EnumChatFormatting.BLUE + "(" + bX + ", " + bZ + ")"));
        for (int x = -(range); x < (range + 1); ++x) {
            aX = bX + (x * 16);
            for (int z = -(range); z < (range + 1); ++z) {

                aZ = bZ + (z * 16);
                int dist = x * x + z * z;

                for (distTextIndex = 0; distTextIndex < DISTANCEINTS.length; distTextIndex++) {
                    if (dist <= DISTANCEINTS[distTextIndex]) {
                        break;
                    }
                }
                if (DetravScannerMod.DEBUG_ENABLED) aPlayer.addChatMessage(
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
                processOreProspecting(
                    (DetravMetaGeneratedTool01) aItem,
                    aStack,
                    aPlayer,
                    aWorld.getChunkFromBlockCoords(aX, aZ),
                    aWorld.getTileEntity(aX, aY, aZ),
                    GTOreDictUnificator.getAssociation(
                        new ItemStack(aWorld.getBlock(aX, aY, aZ), 1, aWorld.getBlockMetadata(aX, aY, aZ))),
                    aRandom,
                    chance);
            }
        }

        // List to hold unsorted scanner messages
        List<ChatComponentText> oreMessages = new ArrayList<ChatComponentText>();

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

        List<ChatComponentText> oreMessagesSorted = new ArrayList<ChatComponentText>();
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

        if (Mods.VisualProspecting.isModLoaded()) {
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
            aWorld.getChunkFromBlockCoords(aX, aZ),
            aWorld.getTileEntity(aX, aY, aZ),
            GTOreDictUnificator
                .getAssociation(new ItemStack(aWorld.getBlock(aX, aY, aZ), 1, aWorld.getBlockMetadata(aX, aY, aZ))),
            new SplittableRandom(),
            1000);

        for (String key : ores.keySet()) {
            int value = ores.get(key);
            addChatMassageByValue(aPlayer, value, key);
        }

        if (Mods.VisualProspecting.isModLoaded()) {
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
        Chunk aChunk, TileEntity aTileEntity, ItemData tAssotiation, SplittableRandom aRandom, int chance)// TileEntity
                                                                                                          // aTileEntity)
    {
        if (aTileEntity != null) {
            if (aTileEntity instanceof TileEntityOres) {
                TileEntityOres gt_entity = (TileEntityOres) aTileEntity;
                short meta = gt_entity.getMetaData();
                String format = LanguageRegistry.instance()
                    .getStringLocalization("gt.blockores." + meta + ".name");
                String name = Materials.getLocalizedNameForItem(format, meta % 1000);
                addOreToHashMap(name, aPlayer);
                if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);
                return;
            }
        } else if (tAssotiation != null) {
            try {
                String name = tAssotiation.toString();
                addChatMassageByValue(aPlayer, -1, name);
                if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);
                return;
            } catch (Exception e) {
                addChatMassageByValue(aPlayer, -1, "ERROR, lol ^_^");
            }
        } else if (aRandom.nextInt(100) < chance) {
            final int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack)
                .intValue();
            final String small_ore_keyword = StatCollector.translateToLocal("detrav.scanner.small_ore.keyword");
            for (int x = 0; x < 16; x++) for (int z = 0; z < 16; z++) {
                int ySize = aChunk.getHeightValue(x, z);
                for (int y = 1; y < ySize; y++) {

                    Block tBlock = aChunk.getBlock(x, y, z);
                    short tMetaID = (short) aChunk.getBlockMetadata(x, y, z);
                    if (tBlock instanceof BlockOresAbstract) {
                        TileEntity tTileEntity = aChunk.getTileEntityUnsafe(x, y, z);
                        if ((tTileEntity instanceof TileEntityOres) && ((TileEntityOres) tTileEntity).mNatural) {
                            tMetaID = (short) ((TileEntityOres) tTileEntity).getMetaData();
                            try {
                                String format = LanguageRegistry.instance()
                                    .getStringLocalization(tBlock.getUnlocalizedName() + "." + tMetaID + ".name");
                                String name = Materials.getLocalizedNameForItem(format, tMetaID % 1000);
                                if (data != 1 && name.startsWith(small_ore_keyword)) continue;
                                addOreToHashMap(name, aPlayer);
                            } catch (Exception e) {
                                String name = tBlock.getUnlocalizedName() + ".";
                                if (data != 1 && name.contains(".small.")) continue;
                                addOreToHashMap(name, aPlayer);
                            }
                        }
                    } else if (GTppHelper.isGTppBlock(tBlock)) {
                        String name = GTppHelper.getGTppVeinName(tBlock);
                        if (!name.isEmpty()) addOreToHashMap(name, aPlayer);
                    } else if (BartWorksHelper.isOre(tBlock)) {
                        if (data != 1 && BartWorksHelper.isSmallOre(tBlock)) continue;
                        final Werkstoff werkstoff = Werkstoff.werkstoffHashMap.getOrDefault(
                            (short) ((BartWorksHelper.getMetaFromBlock(aChunk, x, y, z, tBlock)) * -1),
                            null);
                        String type = BartWorksHelper.isSmallOre(tBlock) ? "oreSmall" : "ore";
                        String translated = GTLanguageManager.getTranslation("bw.blocktype." + type);
                        addOreToHashMap(translated.replace("%material", werkstoff.getLocalizedName()), aPlayer);
                    } else if (data == 1) {
                        tAssotiation = GTOreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                        if ((tAssotiation != null) && (tAssotiation.mPrefix.toString()
                            .startsWith("ore"))) {
                            try {
                                try {
                                    tMetaID = (short) tAssotiation.mMaterial.mMaterial.mMetaItemSubID;
                                    String format = LanguageRegistry.instance()
                                        .getStringLocalization("gt.blockores." + tMetaID + ".name");
                                    String name = Materials.getLocalizedNameForItem(format, tMetaID % 1000);
                                    addOreToHashMap(name, aPlayer);
                                } catch (Exception e1) {
                                    String name = tAssotiation.toString();
                                    addOreToHashMap(name, aPlayer);
                                }
                            } catch (Exception ignored) {}
                        }
                    }

                }
            }

            if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts);

            return;
        } else {
            if (DetravScannerMod.DEBUG_ENABLED)
                aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + " Failed on this chunk"));
            badluck++;
            if (!aPlayer.capabilities.isCreativeMode) aItem.doDamage(aStack, this.mCosts / 4);
        }
        // addChatMassageByValue(aPlayer,0,null);
    }

    void addOreToHashMap(String orename, EntityPlayer aPlayer) {
        String oreDistance = orename + StatCollector.translateToLocal("detrav.scanner.distance.texts." + distTextIndex); // orename
                                                                                                                         // +
                                                                                                                         // the
                                                                                                                         // textual
                                                                                                                         // distance
                                                                                                                         // of
                                                                                                                         // the
                                                                                                                         // ore
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

    public static int getPolution(World aWorld, int aX, int aZ) {
        return Pollution.getPollution(aWorld.getChunkFromBlockCoords(aX, aZ));
    }
}
