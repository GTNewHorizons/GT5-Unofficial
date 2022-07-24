package gregtech.common.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gtnewhorizon.structurelib.StructureLib;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IGlobalWirelessEnergy;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.common.GT_Pollution;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public final class GT_Command extends CommandBase implements IGlobalWirelessEnergy {

    @Override
    public String getCommandName() {
        return "gt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: gt <subcommand>. Valid subcommands are: toggle, chunks, pollution.";
    }

    private void printHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("Usage: gt <toggle|chunks|pollution|global_energy_add|global_energy_set|global_energy_join>"));
        sender.addChatMessage(new ChatComponentText("\"toggle D1\" - toggles general.Debug (D1)"));
        sender.addChatMessage(new ChatComponentText("\"toggle D2\" - toggles general.Debug2 (D2)"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugCleanroom\" - toggles cleanroom debug log"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugDriller\" - toggles oil drill debug log"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugBlockPump\" - Possible issues with pumps"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugBlockMiner\" - Possible issues with miners"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugEntityCramming\" - How long it takes and how many entities it finds"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugWorldGen\" - toggles generic worldgen debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugOrevein\" - toggles worldgen ore vein debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugSmallOres\" - toggles worldgen small vein debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugStones\" - toggles worldgen stones debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugChunkloaders\" - toggles chunkloaders debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugMulti\" - toggles structurelib debug"));
        sender.addChatMessage(new ChatComponentText("\"chunks\" - print a list of the force loaded chunks"));
        sender.addChatMessage(new ChatComponentText(
                "\"pollution <amount>\" - adds the <amount> of the pollution to the current chunk, " +
                        "\n if <amount> isnt specified, will add" + GT_Mod.gregtechproxy.mPollutionSmogLimit + "gibbl."
        ));
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] ss) {
        List<String> l = new ArrayList<>();
        String test = ss.length == 0 ? "" : ss[0].trim();
        if (ss.length == 0 || ss.length == 1 && (test.isEmpty() || Stream.of("toggle", "chunks", "pollution").anyMatch(s -> s.startsWith(test)))) {
            Stream.of("toggle", "chunks", "pollution", "gt")
                    .filter(s -> test.isEmpty() || s.startsWith(test))
                    .forEach(l::add);
        } else if (test.equals("toggle")) {
            String test1 = ss[1].trim();
            Stream.of("D1", "D2", "debugCleanroom", "debugDriller", "debugBlockPump", "debugBlockMiner", "debugWorldGen", "debugEntityCramming",
                    "debugOrevein", "debugSmallOres", "debugStones", "debugChunkloaders", "debugMulti", "debugWorldData")
                    .filter(s -> test1.isEmpty() || s.startsWith(test1))
                    .forEach(l::add);
        }
        return l;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {
        if (strings.length < 1) {
            printHelp(sender);
            return;
        }

        String username;
        String uuid;

        switch (strings[0]) {
            case "toggle":
                if (strings.length < 2) {
                    printHelp(sender);
                    return;
                }
                if ("debugMulti".equals(strings[1])) {
                    StructureLib.DEBUG_MODE = !StructureLib.DEBUG_MODE;
                    sender.addChatMessage(new ChatComponentText(strings[1] + " = " + (StructureLib.DEBUG_MODE ? "true" : "false")));
                    return;
                }
                try {
                    Field field = GT_Values.class.getDeclaredField(strings[1]);
                    if (field.getType() != boolean.class) {
                        sender.addChatMessage(new ChatComponentText("Wrong variable: " + strings[1]));
                        return;
                    }
                    boolean b = !field.getBoolean(null);
                    field.setBoolean(null, b);
                    sender.addChatMessage(new ChatComponentText(strings[1] + " = " + (b ? "true" : "false")));
                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText("No such variable: " + strings[0]));
                }
                break;
            case "chunks":
                GT_ChunkManager.printTickets();
                sender.addChatMessage(new ChatComponentText("Forced chunks logged to GregTech.log"));
                break;
            case "pollution":
                ChunkCoordinates coordinates = sender.getPlayerCoordinates();
                int amount = (strings.length < 2) ? GT_Mod.gregtechproxy.mPollutionSmogLimit : Integer.parseInt(strings[1]);
                GT_Pollution.addPollution(sender
                        .getEntityWorld()
                        .getChunkFromBlockCoords(
                            coordinates.posX,
                            coordinates.posZ
                        ),
                    amount
                );
                break;
            case "global_energy_add":

                username = strings[1];
                uuid = IGlobalWirelessEnergy.super.GetUUIDFromUsername(username);

                String EU_String = strings[2];

                // Usage is /gt global_energy_add username EU

                if (uuid.equals("")) {
                    sender.addChatMessage(new ChatComponentText("User " + username + " has no global energy network."));
                    break;
                }

                if (addEUToGlobalEnergyMap(uuid, new BigInteger(EU_String)))
                    sender.addChatMessage(new ChatComponentText("Successfully added " + EU_String + "EU to the global energy network of " + username + "."));
                else
                    sender.addChatMessage(new ChatComponentText("Failed to add " + EU_String + "EU to the global energy map of " + username +
                        ". Insufficient energy in network. " + username + "currently has " + IGlobalWirelessEnergy.super.GetUserEU(uuid).toString() + " in their network."));

                break;
            case "global_energy_set":

                // Usage is /gt global_energy_set username EU

                username = strings[1];
                uuid = IGlobalWirelessEnergy.super.GetUUIDFromUsername(username);

                String EU_String_0 = strings[2];

                IGlobalWirelessEnergy.super.SetUserEU(uuid, new BigInteger(EU_String_0));
                break;

            case "global_energy_join":

                // Usage is /gt global_energy_join username_of_you username_to_join

                String username_0 = strings[1];
                String username_1 = strings[2];

                String uuid_0 = IGlobalWirelessEnergy.super.GetUUIDFromUsername(username_0);
                String uuid_1 = IGlobalWirelessEnergy.super.GetUUIDFromUsername(username_1);

                if (uuid_0.equals("")) {
                    sender.addChatMessage(new ChatComponentText("User " + username_0 + " has no global energy network."));
                    break;
                }

                if (uuid_0.equals("")) {
                    sender.addChatMessage(new ChatComponentText("User " + username_1 + " has no global energy network."));
                    break;
                }

                IGlobalWirelessEnergy.super.JoinUserNetwork(uuid_0, uuid_1);

                sender.addChatMessage(new ChatComponentText("Success! " + username_0 + " has joined " + username_1 + "."));
                sender.addChatMessage(new ChatComponentText("To undo this simply join your own network again with /gt global_energy_join " + username_0 + " " + username_0 + "."));

                break;
            default:
                printHelp(sender);
        }
    }
}
