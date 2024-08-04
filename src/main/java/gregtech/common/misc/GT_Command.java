package gregtech.common.misc;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.setUserEU;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.structurelib.StructureLib;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.api.util.GT_MusicSystem;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public final class GT_Command extends CommandBase {

    @Override
    public String getCommandName() {
        return "gt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: gt <subcommand>. Valid subcommands are: toggle, chunks, pollution, global_energy_add, global_energy_set, global_energy_join, dump_music_durations.";
    }

    private void printHelp(ICommandSender sender) {
        sender.addChatMessage(
            new ChatComponentText(
                "Usage: gt <toggle|chunks|pollution|global_energy_add|global_energy_set|global_energy_join|dump_music_durations>"));
        sender.addChatMessage(new ChatComponentText("\"toggle D1\" - toggles general.Debug (D1)"));
        sender.addChatMessage(new ChatComponentText("\"toggle D2\" - toggles general.Debug2 (D2)"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugCleanroom\" - toggles cleanroom debug log"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugDriller\" - toggles oil drill debug log"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugBlockPump\" - Possible issues with pumps"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugBlockMiner\" - Possible issues with miners"));
        sender.addChatMessage(
            new ChatComponentText("\"toggle debugEntityCramming\" - How long it takes and how many entities it finds"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugWorldGen\" - toggles generic worldgen debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugOrevein\" - toggles worldgen ore vein debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugSmallOres\" - toggles worldgen small vein debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugStones\" - toggles worldgen stones debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugChunkloaders\" - toggles chunkloaders debug"));
        sender.addChatMessage(new ChatComponentText("\"toggle debugMulti\" - toggles structurelib debug"));
        sender.addChatMessage(new ChatComponentText("\"chunks\" - print a list of the force loaded chunks"));
        sender.addChatMessage(
            new ChatComponentText(
                "\"pollution <amount>\" - adds the <amount> of the pollution to the current chunk, "
                    + "\n if <amount> isnt specified, will add"
                    + GT_Mod.gregtechproxy.mPollutionSmogLimit
                    + "gibbl."));
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + " --- Global wireless EU controls ---"));
        sender.addChatMessage(new ChatComponentText("Allows you to set the amount of EU in a users wireless network."));
        sender.addChatMessage(
            new ChatComponentText(
                "Usage:" + EnumChatFormatting.RED
                    + " global_energy_set "
                    + EnumChatFormatting.BLUE
                    + "[Name] "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "[EU]"));
        sender.addChatMessage(
            new ChatComponentText("Allows you to add EU to a users wireless network. Also accepts negative numbers."));
        sender.addChatMessage(
            new ChatComponentText(
                "Usage:" + EnumChatFormatting.RED
                    + " global_energy_add "
                    + EnumChatFormatting.BLUE
                    + "[Name] "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "[EU]"));
        sender.addChatMessage(
            new ChatComponentText(
                "Allows you to join two users together into one network. Can be undone by writing the users name twice."));
        sender.addChatMessage(
            new ChatComponentText(
                "Usage:" + EnumChatFormatting.RED
                    + " global_energy_join "
                    + EnumChatFormatting.BLUE
                    + "[User joining] [User to join]"));
        sender.addChatMessage(new ChatComponentText("Shows the amount of EU in a users energy network."));
        sender.addChatMessage(
            new ChatComponentText(
                "Usage:" + EnumChatFormatting.RED + " global_energy_display " + EnumChatFormatting.BLUE + "[Name]"));
        sender.addChatMessage(
            new ChatComponentText(
                "\"dump_music_durations\" - dumps soundmeta/durations.json for all registered records in the game to the log. Client-only"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] ss) {
        List<String> l = new ArrayList<>();
        String test = ss.length == 0 ? "" : ss[0].trim();
        if (ss.length == 0 || ss.length == 1 && (test.isEmpty() || Stream
            .of(
                "toggle",
                "chunks",
                "pollution",
                "global_energy_add",
                "global_energy_set",
                "global_energy_join",
                "global_energy_display",
                "dump_music_durations")
            .anyMatch(s -> s.startsWith(test)))) {
            Stream
                .of(
                    "toggle",
                    "chunks",
                    "pollution",
                    "global_energy_add",
                    "global_energy_set",
                    "global_energy_join",
                    "global_energy_display",
                    "dump_music_durations")
                .filter(s -> test.isEmpty() || s.startsWith(test))
                .forEach(l::add);
        } else if (test.equals("toggle")) {
            String test1 = ss[1].trim();
            Stream
                .of(
                    "D1",
                    "D2",
                    "debugCleanroom",
                    "debugDriller",
                    "debugBlockPump",
                    "debugBlockMiner",
                    "debugWorldGen",
                    "debugEntityCramming",
                    "debugOrevein",
                    "debugSmallOres",
                    "debugStones",
                    "debugChunkloaders",
                    "debugMulti",
                    "debugWorldData")
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
        switch (strings[0]) {
            case "toggle" -> {
                if (strings.length < 2) {
                    printHelp(sender);
                    return;
                }
                if ("debugMulti".equals(strings[1])) {
                    StructureLib.DEBUG_MODE = !StructureLib.DEBUG_MODE;
                    sender.addChatMessage(
                        new ChatComponentText(strings[1] + " = " + (StructureLib.DEBUG_MODE ? "true" : "false")));
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
            }
            case "chunks" -> {
                GT_ChunkManager.printTickets();
                sender.addChatMessage(new ChatComponentText("Forced chunks logged to GregTech.log"));
            }
            case "pollution" -> {
                ChunkCoordinates coordinates = sender.getPlayerCoordinates();
                int amount = (strings.length < 2) ? GT_Mod.gregtechproxy.mPollutionSmogLimit
                    : Integer.parseInt(strings[1]);
                GT_Pollution.addPollution(
                    sender.getEntityWorld()
                        .getChunkFromBlockCoords(coordinates.posX, coordinates.posZ),
                    amount);
            }
            case "global_energy_add" -> {
                String username = strings[1];
                String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
                UUID uuid = SpaceProjectManager.getPlayerUUIDFromName(username);

                String EU_String = strings[2];

                // Usage is /gt global_energy_add username EU

                String EU_string_formatted = EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(new BigInteger(EU_String))
                    + EnumChatFormatting.RESET;

                if (addEUToGlobalEnergyMap(uuid, new BigInteger(EU_String))) sender.addChatMessage(
                    new ChatComponentText(
                        "Successfully added " + EU_string_formatted
                            + "EU to the global energy network of "
                            + formatted_username
                            + "."));
                else sender.addChatMessage(
                    new ChatComponentText(
                        "Failed to add " + EU_string_formatted
                            + "EU to the global energy map of "
                            + formatted_username
                            + ". Insufficient energy in network. "));

                sender.addChatMessage(
                    new ChatComponentText(
                        formatted_username + " currently has "
                            + EnumChatFormatting.RED
                            + GT_Utility.formatNumbers(new BigInteger(getUserEU(uuid).toString()))
                            + EnumChatFormatting.RESET
                            + "EU in their network."));

            }
            case "global_energy_set" -> {

                // Usage is /gt global_energy_set username EU

                String username = strings[1];
                String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
                UUID uuid = SpaceProjectManager.getPlayerUUIDFromName(username);

                String EU_String_0 = strings[2];

                if ((new BigInteger(EU_String_0).compareTo(BigInteger.ZERO)) < 0) {
                    sender.addChatMessage(
                        new ChatComponentText("Cannot set a users energy network to a negative value."));
                    break;
                }

                setUserEU(uuid, new BigInteger(EU_String_0));

                sender.addChatMessage(
                    new ChatComponentText(
                        "Successfully set " + formatted_username
                            + "'s global energy network to "
                            + EnumChatFormatting.RED
                            + GT_Utility.formatNumbers(new BigInteger(EU_String_0))
                            + EnumChatFormatting.RESET
                            + "EU."));

            }
            case "global_energy_join" -> {

                // Usage is /gt global_energy_join username_of_you username_to_join

                String usernameSubject = strings[1];
                String usernameTeam = strings[2];

                String formattedUsernameSubject = EnumChatFormatting.BLUE + usernameSubject + EnumChatFormatting.RESET;
                String formattedUsernameTeam = EnumChatFormatting.BLUE + usernameTeam + EnumChatFormatting.RESET;

                UUID uuidSubject = SpaceProjectManager.getPlayerUUIDFromName(usernameSubject);
                UUID uuidTeam = SpaceProjectManager.getLeader(SpaceProjectManager.getPlayerUUIDFromName(usernameTeam));

                if (uuidSubject.equals(uuidTeam)) {
                    // leave team
                    SpaceProjectManager.putInTeam(uuidSubject, uuidSubject);
                    sender.addChatMessage(
                        new ChatComponentText(
                            "User " + formattedUsernameSubject + " has rejoined their own global energy network."));
                    break;
                }

                // join other's team

                if (uuidSubject.equals(uuidTeam)) {
                    sender.addChatMessage(new ChatComponentText("They are already in the same network!"));
                    break;
                }

                SpaceProjectManager.putInTeam(uuidSubject, uuidTeam);

                sender.addChatMessage(
                    new ChatComponentText(
                        "Success! " + formattedUsernameSubject + " has joined " + formattedUsernameTeam + "."));
                sender.addChatMessage(
                    new ChatComponentText(
                        "To undo this simply join your own network again with /gt global_energy_join "
                            + formattedUsernameSubject
                            + " "
                            + formattedUsernameSubject
                            + "."));

            }
            case "global_energy_display" -> {

                // Usage is /gt global_energy_display username.

                String username = strings[1];
                String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
                UUID userUUID = SpaceProjectManager.getPlayerUUIDFromName(username);

                if (!SpaceProjectManager.isInTeam(userUUID)) {
                    sender.addChatMessage(
                        new ChatComponentText("User " + formatted_username + " has no global energy network."));
                    break;
                }
                UUID teamUUID = SpaceProjectManager.getLeader(userUUID);

                sender.addChatMessage(
                    new ChatComponentText(
                        "User " + formatted_username
                            + " has "
                            + EnumChatFormatting.RED
                            + GT_Utility.formatNumbers(getUserEU(userUUID))
                            + EnumChatFormatting.RESET
                            + "EU in their network."));
                if (!userUUID.equals(teamUUID)) sender.addChatMessage(
                    new ChatComponentText(
                        "User " + formatted_username
                            + " is currently in network of "
                            + EnumChatFormatting.BLUE
                            + SpaceProjectManager.getPlayerNameFromUUID(teamUUID)
                            + EnumChatFormatting.RESET
                            + "."));

            }
            case "dump_music_durations" -> {
                if (!FMLLaunchHandler.side()
                    .isClient()) {
                    sender
                        .addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "This command is client-only."));
                }
                GT_MusicSystem.ClientSystem.dumpAllRecordDurations();
            }
            default -> {
                sender
                    .addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Invalid command/syntax detected."));
                printHelp(sender);
            }
        }
    }
}
