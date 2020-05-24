package gregtech.common.misc;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.objects.GT_ChunkManager;
import gregtech.common.GT_Pollution;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class GT_Command extends CommandBase {

    @Override
    public String getCommandName() {
        return "gt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: gt <subcommand>. Valid subcommands are: toggle, chunks, pollution.";
    }
    private void printHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("Usage: gt <toggle|chunks|pollution>"));
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
            Stream.of("toggle", "chunks", "pollution")
                    .filter(s -> test.isEmpty() || s.startsWith(test))
                    .forEach(l::add);
        } else if (test.equals("toggle")) {
            String test1 = ss[1].trim();
            Stream.of("D1", "D2", "debugCleanroom", "debugDriller", "debugBlockPump", "debugBlockMiner", "debugWorldGen", "debugEntityCramming",
                                       "debugOrevein", "debugSmallOres", "debugStones", "debugChunkloaders")
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
            case "toggle":
                if (strings.length < 2) {
                    printHelp(sender);
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
            case "pollution": {
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
            }
            default:
                printHelp(sender);
        }
    }
}
