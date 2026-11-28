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
import java.util.Locale;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.gtnewhorizon.structurelib.StructureLib;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.objects.GTChunkManager;
import gregtech.api.util.FakeCleanroom;
import gregtech.api.util.GTMusicSystem;
import gregtech.commands.GTBaseCommand;
import gregtech.common.GTWorldgenerator;
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
        return Mods.ModIDs.GREG_TECH;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "gt <chunks|cleanroom_bypass|dump_music_durations|global_energy_add|global_energy_display|global_energy_join|global_energy_set|oregenpattern|pollution|toggle>";
    }

    // spotless:off
    @Override
    protected List<IChatComponent> getHelpMessages() {
        final List<IChatComponent> list = new ArrayList<>();
        list.add(new ChatComponentTranslation("commands.generic.usage", getCommandUsage(null)));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.chunks", "chunks"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.cleanroom_bypass", "cleanroom_bypass <on|off|status> <all|cleanroom|lowgrav>"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.dump_music_durations", "dump_music_durations"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.oregenpattern", "oregenpattern [set <pattern> [confirm]]"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.pollution", "pollution <amount>", GTMod.proxy.mPollutionSmogLimit));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug", "toggle D1"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug2", "toggle D2"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_chunkloaders", "toggle debugChunkloaders"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_cleanroom", "toggle debugCleanroom"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_driller", "toggle debugDriller"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_block_miner", "toggle debugBlockMiner"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_block_pump", "toggle debugBlockPump"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_entity_cramming", "toggle debugEntityCramming"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_multi", "toggle debugMulti"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_orevein", "toggle debugOrevein"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_small_ores", "toggle debugSmallOres"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_stones", "toggle debugStones"));
        list.add(new ChatComponentTranslation("GT5U.chat.command.gt.help.debug_worldgen", "toggle debugWorldGen"));
        list.add(new ChatComponentTranslation("GT5U.chat.wireless.help.title").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
        list.add(new ChatComponentTranslation("GT5U.chat.wireless.help.set"));
        list.add(new ChatComponentTranslation("commands.generic.usage", EnumChatFormatting.RED + " global_energy_set " + EnumChatFormatting.BLUE + "[Name] " + EnumChatFormatting.LIGHT_PURPLE + "[EU]"));
        list.add(new ChatComponentTranslation("GT5U.chat.wireless.help.add"));
        list.add(new ChatComponentTranslation("commands.generic.usage", EnumChatFormatting.RED + " global_energy_add " + EnumChatFormatting.BLUE + "[Name] " + EnumChatFormatting.LIGHT_PURPLE + "[EU]"));
        list.add(new ChatComponentTranslation("GT5U.chat.wireless.help.join"));
        list.add(new ChatComponentTranslation("commands.generic.usage", EnumChatFormatting.RED + " global_energy_join " + EnumChatFormatting.BLUE + "[User joining] [User to join]"));
        list.add(new ChatComponentTranslation("GT5U.chat.wireless.help.display"));
        list.add(new ChatComponentTranslation("commands.generic.usage", EnumChatFormatting.RED + " global_energy_display " + EnumChatFormatting.BLUE + "[Name]"));
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
                "oregenpattern",
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
            if (args[0].equals("oregenpattern")) {
                return getListOfStringsMatchingLastWord(args, "set");
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
            if (args[0].equals("oregenpattern") && args[1].equals("set")) {
                return getListOfStringsMatchingLastWord(args, patternNames());
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
            case "oregenpattern" -> this.processOregenPatternCommand(sender, args);
            case "pollution" -> this.processPollutionCommand(sender, args);
            case "global_energy_add" -> this.processGlobalEnergyAddCommand(sender, args);
            case "global_energy_display" -> this.processGlobalEnergyDisplayCommand(sender, args);
            case "global_energy_join" -> this.processGlobalEnergyJoinCommand(sender, args);
            case "global_energy_set" -> this.processGlobalEnergySetCommand(sender, args);
            case "toggle" -> this.processToggleCommand(sender, args);
            default -> {
                sendChatToPlayer(
                    sender,
                    new ChatComponentTranslation("commands.generic.syntax")
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
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
                sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.command.wrong_variable", args[1]));
                return;
            }
            boolean b = !field.getBoolean(null);
            field.setBoolean(null, b);
            sendChatToPlayer(sender, args[1] + " = " + b);
        } catch (Exception e) {
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.command.unknown_variable", args[0]));
        }
    }

    private void processCleanroomBypassCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation(
                    "commands.generic.usage",
                    "gt cleanroom_bypass <on|off|status> <all|cleanroom|lowgrav>")
                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
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
            new ChatComponentTranslation(
                "GT5U.chat.cleanroom.bypass_status",
                bypassStatus(FakeCleanroom.CLEANROOM_BYPASS),
                bypassStatus(FakeCleanroom.LOWGRAV_BYPASS))
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
    }

    private static IChatComponent bypassStatus(boolean enabled) {
        return new ChatComponentTranslation(
            enabled ? "GT5U.gui.button.feature_enabled" : "GT5U.gui.button.feature_disabled")
                .setChatStyle(new ChatStyle().setColor(enabled ? EnumChatFormatting.GREEN : EnumChatFormatting.RED));
    }

    private void processChunksCommand(ICommandSender sender) {
        GTChunkManager.printTickets();
        sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.command.chunks_logged"));
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

        String EU_string_formatted = EnumChatFormatting.RED + formatNumber(eu) + EnumChatFormatting.RESET;

        if (addEUToGlobalEnergyMap(uuid, eu)) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.wireless.added", EU_string_formatted, formatted_username));
        } else {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.wireless.add_failed", EU_string_formatted, formatted_username));
        }

        sendChatToPlayer(
            sender,
            new ChatComponentTranslation(
                "GT5U.chat.wireless.balance",
                formatted_username,
                EnumChatFormatting.RED + formatNumber(new BigInteger(getUserEU(uuid).toString()))
                    + EnumChatFormatting.RESET));
    }

    private void processGlobalEnergySetCommand(ICommandSender sender, String[] args) {
        // Usage is /gt global_energy_set username EU

        String username = args[1];
        String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
        UUID uuid = SpaceProjectManager.getPlayerUUIDFromName(username);

        BigInteger eu = parseEU(args[2]);

        if ((eu.compareTo(BigInteger.ZERO)) < 0) {
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.wireless.negative_balance"));
            return;
        }

        setUserEU(uuid, eu);

        sendChatToPlayer(
            sender,
            new ChatComponentTranslation(
                "GT5U.chat.wireless.balance_set",
                formatted_username,
                EnumChatFormatting.RED + formatNumber(eu) + EnumChatFormatting.RESET));
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
                new ChatComponentTranslation("GT5U.chat.wireless.rejoined", formattedUsernameSubject));
            return;
        }

        // join other's team

        if (SpaceProjectManager.getLeader(uuidSubject)
            .equals(SpaceProjectManager.getLeader(uuidTeam))) {
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.wireless.same_network"));
            return;
        }

        SpaceProjectManager.putInTeam(uuidSubject, uuidTeam);

        sendChatToPlayer(
            sender,
            new ChatComponentTranslation("GT5U.chat.wireless.joined", formattedUsernameSubject, formattedUsernameTeam));
        sendChatToPlayer(
            sender,
            new ChatComponentTranslation(
                "GT5U.chat.wireless.join_undo",
                "/gt global_energy_join " + formattedUsernameSubject + " " + formattedUsernameSubject));
    }

    private void processGlobalEnergyDisplayCommand(ICommandSender sender, String[] args) {
        // Usage is /gt global_energy_display username.

        String username = args[1];
        String formatted_username = EnumChatFormatting.BLUE + username + EnumChatFormatting.RESET;
        UUID userUUID = SpaceProjectManager.getPlayerUUIDFromName(username);

        if (!SpaceProjectManager.isInTeam(userUUID)) {
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.wireless.no_network", formatted_username));
            return;
        }
        UUID teamUUID = SpaceProjectManager.getLeader(userUUID);

        sendChatToPlayer(
            sender,
            new ChatComponentTranslation(
                "GT5U.chat.wireless.balance",
                formatted_username,
                EnumChatFormatting.RED + formatNumber(getUserEU(userUUID)) + EnumChatFormatting.RESET));
        if (!userUUID.equals(teamUUID)) sendChatToPlayer(
            sender,
            new ChatComponentTranslation(
                "GT5U.chat.wireless.member",
                formatted_username,
                EnumChatFormatting.BLUE + SpaceProjectManager.getPlayerNameFromUUID(teamUUID)
                    + EnumChatFormatting.RESET));
    }

    private static String[] patternNames() {
        return Arrays.stream(GTWorldgenerator.OregenPattern.values())
            .map(Enum::name)
            .toArray(String[]::new);
    }

    private void processOregenPatternCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation(
                    "GT5U.chat.oregen.pattern.status",
                    EnumChatFormatting.WHITE + GTWorldgenerator.getOregenPattern()
                        .name() + EnumChatFormatting.YELLOW,
                    patternSource()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
            // An operator who just set this does not need to be told it looks wrong
            if (GTWorldgenerator.getOregenPattern() == GTWorldgenerator.OregenPattern.AXISSYMMETRICAL
                && GTWorldgenerator.getOregenPatternSource() != GTWorldgenerator.PatternSource.COMMAND) {
                // spotless:off
                sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.date_hint", "EQUAL_SPACING").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.date_hint_effect").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                // spotless:on
            }
            return;
        }

        if (!"set".equals(args[1])) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("commands.generic.usage", "gt oregenpattern [set <pattern> [confirm]]")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        // gt already requires permission level 4, this makes the gate explicit on the destructive branch
        if (sender instanceof EntityPlayerMP && !isOpedPlayer(sender)) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.oregen.pattern.no_permission")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        if (args.length < 3) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("commands.generic.usage", "gt oregenpattern set <pattern> [confirm]")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.oregen.pattern.valid", String.join(", ", patternNames()))
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        final GTWorldgenerator.OregenPattern newPattern;
        try {
            newPattern = GTWorldgenerator.OregenPattern.valueOf(args[2].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.oregen.pattern.unknown", args[2])
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        final World overworld = DimensionManager.getWorld(0);
        if (overworld == null) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.oregen.pattern.overworld_unloaded")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        final GTWorldgenerator.OregenPattern oldPattern = GTWorldgenerator.getOregenPattern();

        if (args.length < 4 || !"confirm".equals(args[3])) {
            // spotless:off
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.warning").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.current", EnumChatFormatting.WHITE + oldPattern.name() + EnumChatFormatting.YELLOW, patternSource()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.new", EnumChatFormatting.WHITE + newPattern.toString()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.existing").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.prospecting").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.only_if_wrong").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.pattern.confirm", "/gt oregenpattern set " + newPattern + " confirm").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
            // spotless:on
            return;
        }

        if (!GTWorldgenerator.OregenPatternSavedData.overridePattern(overworld, newPattern)) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.oregen.pattern.newer_format")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        sendChatToPlayer(
            sender,
            new ChatComponentTranslation(
                "GT5U.chat.oregen.pattern.changed",
                EnumChatFormatting.WHITE + oldPattern.name() + EnumChatFormatting.GREEN,
                EnumChatFormatting.WHITE + newPattern.name() + EnumChatFormatting.GREEN)
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
    }

    private static IChatComponent patternSource() {
        return new ChatComponentTranslation(
            "GT5U.chat.oregen.pattern.source." + GTWorldgenerator.getOregenPatternSource()
                .name()
                .toLowerCase(Locale.ROOT));
    }

    private void processDumpMusicDurationsCommand(ICommandSender sender) {
        if (!FMLLaunchHandler.side()
            .isClient()) {
            sendChatToPlayer(
                sender,
                new ChatComponentTranslation("GT5U.chat.command.client_only")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        }
        GTMusicSystem.ClientSystem.dumpAllRecordDurations();
    }
}
