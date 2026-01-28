package gregtech.common.misc;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.setUserEU;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.gtnewhorizon.structurelib.StructureLib;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.util.FakeCleanroom;
import gregtech.api.util.GTMusicSystem;
import gregtech.commands.GTBaseCommand;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.pollution.Pollution;

public final class GTMiscCommand extends GTBaseCommand {

    private static final List<String> GLOBAL_ENERGY_COMMANDS = Arrays
        .asList("global_energy_set", "global_energy_add", "global_energy_join", "global_energy_display");

    public GTMiscCommand() {
        super("gt");
    }

    @Override
    public String getCommandName() {
        return "gregtech";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: gt <chunks|cleanroom_bypass|dump_music_durations|global_energy_add|global_energy_display|global_energy_join|global_energy_set|pollution|toggle>";
    }

    // spotless:off
    @Override
    protected List<IChatComponent> getHelpMessages() {
        final List<IChatComponent> list = new ArrayList<>();
        list.add(new ChatComponentText("Usage: gt <chunks|cleanroom_bypass|dump_music_durations|global_energy_add|global_energy_display|global_energy_join|global_energy_set|pollution|toggle>"));
        list.add(new ChatComponentText("\"chunks\" - print a list of the force loaded chunks"));
        list.add(new ChatComponentText("\"cleanroom_bypass <on|off|status> <all|cleanroom|lowgrav>\" - Bypass cleanroom and/or low-grav requirements"));
        list.add(new ChatComponentText("\"dump_music_durations\" - dumps soundmeta/durations.json for all registered records in the game to the log. Client-only"));
        list.add(new ChatComponentText("\"pollution <amount>\" - adds the <amount> of the pollution to the current chunk, \n if <amount> isn't specified, will add" + GTMod.proxy.mPollutionSmogLimit + "gibbl."));
        list.add(new ChatComponentText("\"toggle D1\" - toggles general.Debug (D1)"));
        list.add(new ChatComponentText("\"toggle D2\" - toggles general.Debug2 (D2)"));
        list.add(new ChatComponentText("\"toggle debugChunkloaders\" - toggles chunkloaders debug"));
        list.add(new ChatComponentText("\"toggle debugCleanroom\" - toggles cleanroom debug log"));
        list.add(new ChatComponentText("\"toggle debugDriller\" - toggles oil drill debug log"));
        list.add(new ChatComponentText("\"toggle debugBlockMiner\" - Possible issues with miners"));
        list.add(new ChatComponentText("\"toggle debugBlockPump\" - Possible issues with pumps"));
        list.add(new ChatComponentText("\"toggle debugEntityCramming\" - How long it takes and how many entities it finds"));
        list.add(new ChatComponentText("\"toggle debugMulti\" - toggles structurelib debug"));
        list.add(new ChatComponentText("\"toggle debugOrevein\" - toggles worldgen ore vein debug"));
        list.add(new ChatComponentText("\"toggle debugSmallOres\" - toggles worldgen small vein debug"));
        list.add(new ChatComponentText("\"toggle debugStones\" - toggles worldgen stones debug"));
        list.add(new ChatComponentText("\"toggle debugWorldGen\" - toggles generic worldgen debug"));
        list.add(new ChatComponentText(EnumChatFormatting.GOLD + " --- Global wireless EU controls ---"));
        list.add(new ChatComponentText("Allows you to set the amount of EU in a users wireless network."));
        list.add(new ChatComponentText("Usage:" + EnumChatFormatting.RED + " global_energy_set " + EnumChatFormatting.BLUE + "[Name] " + EnumChatFormatting.LIGHT_PURPLE + "[EU]"));
        list.add(new ChatComponentText("Allows you to add EU to a users wireless network. Also accepts negative numbers."));
        list.add(new ChatComponentText("Usage:" + EnumChatFormatting.RED + " global_energy_add " + EnumChatFormatting.BLUE + "[Name] " + EnumChatFormatting.LIGHT_PURPLE + "[EU]"));
        list.add(new ChatComponentText("Allows you to join two users together into one network. Can be undone by writing the users name twice."));
        list.add(new ChatComponentText("Usage:" + EnumChatFormatting.RED + " global_energy_join " + EnumChatFormatting.BLUE + "[User joining] [User to join]"));
        list.add(new ChatComponentText("Shows the amount of EU in a users energy network."));
        list.add(new ChatComponentText("Usage:" + EnumChatFormatting.RED + " global_energy_display " + EnumChatFormatting.BLUE + "[Name]"));
        return list;
    }
    // spotless:on

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(
                args,
                "chunks",
                "cleanroom_bypass",
                "dump_music_durations",
                "global_energy_add",
                "global_energy_display",
                "global_energy_join",
                "global_energy_set",
                "pollution",
                "toggle");
        } else if (args.length == 2) {
            if (args[0].equals("toggle")) {
                return getListOfStringsMatchingLastWord(
                    args,
                    "D1",
                    "D2",
                    "debugBlockMiner",
                    "debugBlockPump",
                    "debugChunkloaders",
                    "debugCleanroom",
                    "debugDriller",
                    "debugEntityCramming",
                    "debugMulti",
                    "debugOrevein",
                    "debugSmallOres",
                    "debugStones",
                    "debugWorldData",
                    "debugWorldGen");
            }
            if (args[0].equals("cleanroom_bypass")) {
                return getListOfStringsMatchingLastWord(args, "on", "off", "status");
            }
            if (GLOBAL_ENERGY_COMMANDS.contains(args[0])) {
                // 1st username of wireless network commands
                return getListOfStringsMatchingLastWord(args, getAllUsernames());
            }
        } else if (args.length == 3) {
            if (args[0].equals("global_energy_join")) {
                // 2nd username of join command
                return getListOfStringsMatchingLastWord(args, getAllUsernames());
            }
            if (args[0].equals("cleanroom_bypass")) {
                return getListOfStringsMatchingLastWord(args, "all", "cleanroom", "lowgrav");
            }
        }

        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1 && GLOBAL_ENERGY_COMMANDS.contains(args[0])
            || index == 2 && args[0].equals("global_energy_join");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            this.sendHelpMessage(sender);
            return;
        }
        switch (args[0]) {
            case "chunks" -> this.processChunksCommand(sender);
            case "cleanroom_bypass" -> this.processCleanroomBypassCommand(sender, args);
            case "dump_music_durations" -> this.processDumpMusicDurationsCommand(sender);
            case "pollution" -> this.processPollutionCommand(sender, args);
            case "global_energy_add" -> this.processGlobalEnergyAddCommand(sender, args);
            case "global_energy_display" -> this.processGlobalEnergyDisplayCommand(sender, args);
            case "global_energy_join" -> this.processGlobalEnergyJoinCommand(sender, args);
            case "global_energy_set" -> this.processGlobalEnergySetCommand(sender, args);
            case "toggle" -> this.processToggleCommand(sender, args);
            default -> {
                sendChatToPlayer(sender, EnumChatFormatting.RED + "Invalid command/syntax detected.");
                sendHelpMessage(sender);
            }
        }
    }

    private void processToggleCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sendHelpMessage(sender);
            return;
        }
        if ("debugMulti".equals(args[1])) {
            StructureLib.DEBUG_MODE = !StructureLib.DEBUG_MODE;
            sendChatToPlayer(sender, args[1] + " = " + StructureLib.DEBUG_MODE);
            return;
        }
        try {
            Field field = GTValues.class.getDeclaredField(args[1]);
            if (field.getType() != boolean.class) {
                sendChatToPlayer(sender, "Wrong variable: " + args[1]);
                return;
            }
            boolean b = !field.getBoolean(null);
            field.setBoolean(null, b);
            sendChatToPlayer(sender, args[1] + " = " + b);
        } catch (Exception e) {
            sendChatToPlayer(sender, "No such variable: " + args[0]);
        }
    }

    private void processCleanroomBypassCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sendChatToPlayer(
                sender,
                EnumChatFormatting.RED + "Usage: gt cleanroom_bypass <on|off|status> <all|cleanroom|lowgrav>");
            return;
        }
        String act = args[1].toLowerCase();
        String spec = (args.length >= 3 ? args[2] : "all").toLowerCase();
        switch (act) {
            case "status":
                break;

            case "on":
                switch (spec) {
                    case "all" -> {
                        FakeCleanroom.CLEANROOM_BYPASS = true;
                        FakeCleanroom.LOWGRAV_BYPASS = true;
                    }
                    case "cleanroom" -> FakeCleanroom.CLEANROOM_BYPASS = true;
                    case "lowgrav" -> FakeCleanroom.LOWGRAV_BYPASS = true;
                }
                break;

            case "off":
                switch (spec) {
                    case "all" -> {
                        FakeCleanroom.CLEANROOM_BYPASS = false;
                        FakeCleanroom.LOWGRAV_BYPASS = false;
                    }
                    case "cleanroom" -> FakeCleanroom.CLEANROOM_BYPASS = false;
                    case "lowgrav" -> FakeCleanroom.LOWGRAV_BYPASS = false;
                }
                break;
        }
        sendChatToPlayer(
            sender,
            EnumChatFormatting.YELLOW + "Cleanroom Bypass: "
                + (FakeCleanroom.CLEANROOM_BYPASS ? EnumChatFormatting.GREEN + "Enabled"
                    : EnumChatFormatting.RED + "Disabled")
                + EnumChatFormatting.YELLOW
                + " & LowGrav Bypass: "
                + (FakeCleanroom.LOWGRAV_BYPASS ? EnumChatFormatting.GREEN + "Enabled"
                    : EnumChatFormatting.RED + "Disabled"));
    }

    private void processChunksCommand(ICommandSender sender) {
        GTChunkManager.printTickets();
        sendChatToPlayer(sender, "Forced chunks logged to GregTech.log");
    }

    private void processPollutionCommand(ICommandSender sender, String[] args) {
        ChunkCoordinates coordinates = sender.getPlayerCoordinates();
        int amount = (args.length < 2) ? GTMod.proxy.mPollutionSmogLimit : parseInt(sender, args[1]);
        Pollution.addPollution(
            sender.getEntityWorld()
                .getChunkFromBlockCoords(coordinates.posX, coordinates.posZ),
            amount);
    }

    private static BigInteger parseEU(String eu) {
        if (eu.contains("e")) {
            String[] halves = eu.split("e");

            double scalar = Double.parseDouble(halves[0]);
            int exponent = Integer.parseInt(halves[1]);

            return BigDecimal.valueOf(scalar)
                .multiply(
                    BigDecimal.valueOf(10)
                        .pow(exponent))
                .toBigInteger();
        } else {
            return new BigInteger(eu);
        }
    }

    private void processGlobalEnergyAddCommand(ICommandSender sender, String[] args) {
        String username = args[1];
        String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
        UUID uuid = SpaceProjectManager.getPlayerUUIDFromName(username);

        BigInteger eu = parseEU(args[2]);

        // Usage is /gt global_energy_add username EU

        String EU_string_formatted = EnumChatFormatting.RED + GTUtility.formatNumbers(eu) + EnumChatFormatting.RESET;

        if (addEUToGlobalEnergyMap(uuid, eu)) {
            sendChatToPlayer(
                sender,
                "Successfully added " + EU_string_formatted
                    + "EU to the global energy network of "
                    + formatted_username
                    + ".");
        } else {
            sendChatToPlayer(
                sender,
                "Failed to add " + EU_string_formatted
                    + "EU to the global energy map of "
                    + formatted_username
                    + ". Insufficient energy in network.");
        }

        sendChatToPlayer(
            sender,
            formatted_username + " currently has "
                + EnumChatFormatting.RED
                + formatNumber(new BigInteger(getUserEU(uuid).toString()))
                + EnumChatFormatting.RESET
                + "EU in their network.");
    }

    private void processGlobalEnergySetCommand(ICommandSender sender, String[] args) {
        // Usage is /gt global_energy_set username EU

        String username = args[1];
        String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
        UUID uuid = SpaceProjectManager.getPlayerUUIDFromName(username);

        BigInteger eu = parseEU(args[2]);

        if ((eu.compareTo(BigInteger.ZERO)) < 0) {
            sendChatToPlayer(sender, "Cannot set a users energy network to a negative value.");
            return;
        }

        setUserEU(uuid, eu);

        sendChatToPlayer(
            sender,
            "Successfully set " + formatted_username
                + "'s global energy network to "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(eu)
                + EnumChatFormatting.RESET
                + "EU.");
    }

    private void processGlobalEnergyJoinCommand(ICommandSender sender, String[] args) {
        // Usage is /gt global_energy_join username_of_you username_to_join

        String usernameSubject = args[1];
        String usernameTeam = args[2];

        String formattedUsernameSubject = EnumChatFormatting.BLUE + usernameSubject + EnumChatFormatting.RESET;
        String formattedUsernameTeam = EnumChatFormatting.BLUE + usernameTeam + EnumChatFormatting.RESET;

        UUID uuidSubject = SpaceProjectManager.getPlayerUUIDFromName(usernameSubject);
        UUID uuidTeam = SpaceProjectManager.getPlayerUUIDFromName(usernameTeam);

        if (uuidSubject.equals(uuidTeam)) {
            // leave team
            SpaceProjectManager.putInTeam(uuidSubject, uuidSubject);
            sendChatToPlayer(
                sender,
                "User " + formattedUsernameSubject + " has rejoined their own global energy network.");
            return;
        }

        // join other's team

        if (SpaceProjectManager.getLeader(uuidSubject)
            .equals(SpaceProjectManager.getLeader(uuidTeam))) {
            sendChatToPlayer(sender, "They are already in the same network!");
            return;
        }

        SpaceProjectManager.putInTeam(uuidSubject, uuidTeam);

        sendChatToPlayer(sender, "Success! " + formattedUsernameSubject + " has joined " + formattedUsernameTeam + ".");
        sendChatToPlayer(
            sender,
            "To undo this simply join your own network again with /gt global_energy_join " + formattedUsernameSubject
                + " "
                + formattedUsernameSubject
                + ".");
    }

    private void processGlobalEnergyDisplayCommand(ICommandSender sender, String[] args) {
        // Usage is /gt global_energy_display username.

        String username = args[1];
        String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
        UUID userUUID = SpaceProjectManager.getPlayerUUIDFromName(username);

        if (!SpaceProjectManager.isInTeam(userUUID)) {
            sendChatToPlayer(sender, "User " + formatted_username + " has no global energy network.");
            return;
        }
        UUID teamUUID = SpaceProjectManager.getLeader(userUUID);

        sendChatToPlayer(
            sender,
            "User " + formatted_username
                + " has "
                + EnumChatFormatting.RED
                + formatNumber(getUserEU(userUUID))
                + EnumChatFormatting.RESET
                + "EU in their network.");
        if (!userUUID.equals(teamUUID)) sendChatToPlayer(
            sender,
            "User " + formatted_username
                + " is currently in network of "
                + EnumChatFormatting.BLUE
                + SpaceProjectManager.getPlayerNameFromUUID(teamUUID)
                + EnumChatFormatting.RESET
                + ".");
    }

    private void processDumpMusicDurationsCommand(ICommandSender sender) {
        if (!FMLLaunchHandler.side()
            .isClient()) {
            sendChatToPlayer(sender, EnumChatFormatting.RED + "This command is client-only.");
        }
        GTMusicSystem.ClientSystem.dumpAllRecordDurations();
    }
}
