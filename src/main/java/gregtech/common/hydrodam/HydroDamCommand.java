package gregtech.common.hydrodam;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet3D;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.commands.GTBaseCommand;
import it.unimi.dsi.fastutil.longs.LongIterator;

/// Debug command for creating, inspecting and changing dam water volumes without pumps or turbines.
public class HydroDamCommand extends GTBaseCommand {

    private static final String[] SUBCOMMANDS = { "create", "add", "remove", "set", "info", "at", "list", "delete",
        "why" };

    public HydroDamCommand() {
        super("hydrodam");
    }

    @Override
    public String getCommandName() {
        return "hydrodam";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /hydrodam <subcommand>. Valid subcommands are: " + String.join(", ", SUBCOMMANDS) + ".";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    protected List<IChatComponent> getHelpMessages() {
        // spotless:off
        List<IChatComponent> messages = new ArrayList<>();
        messages.add(new ChatComponentText(getCommandUsage(null)));
        messages.add(new ChatComponentText(" create <x> <y> <z> [litres] - Creates a volume at a position"));
        messages.add(new ChatComponentText(" add <id> <litres> - Adds water to a volume"));
        messages.add(new ChatComponentText(" remove <id> <litres> - Removes water from a volume"));
        messages.add(new ChatComponentText(" set <id> <litres> - Sets the water in a volume"));
        messages.add(new ChatComponentText(" info <id> - Prints a volume's details"));
        messages.add(new ChatComponentText(" at <x> <y> <z> - Prints the details of the volume at a position"));
        messages.add(new ChatComponentText(" list - Lists the volumes in this dimension"));
        messages.add(new ChatComponentText(" delete <id> - Deletes a volume and its water blocks"));
        messages.add(new ChatComponentText(" why <id> - Explains what stops a volume from growing another layer"));
        messages.add(new ChatComponentText("Coordinates accept ~ for relative positions."));
        return messages;
        // spotless:on
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, SUBCOMMANDS);

        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sendHelpMessage(sender);
            return;
        }

        DamVolumes volumes = DamVolumes.get(sender.getEntityWorld());

        switch (args[0]) {
            case "create" -> create(sender, volumes, args);
            case "add" -> {
                requireArgs(args, 3);
                WaterVolume volume = parseVolume(sender, volumes, args[1]);
                double stored = volumes.addLitres(volume, parseDoubleWithMin(sender, args[2], 0));
                sendChatToPlayer(sender, String.format("Stored %.0f L in volume %d.", stored, volume.getId()));
                sendMergeNotice(sender, volumes, volume);
            }
            case "remove" -> {
                requireArgs(args, 3);
                WaterVolume volume = parseVolume(sender, volumes, args[1]);
                double removed = volumes.removeLitres(volume, parseDoubleWithMin(sender, args[2], 0));
                sendChatToPlayer(sender, String.format("Removed %.0f L from volume %d.", removed, volume.getId()));
            }
            case "set" -> set(sender, volumes, args);
            case "info" -> {
                requireArgs(args, 2);
                sendInfo(sender, volumes, parseVolume(sender, volumes, args[1]));
            }
            case "at" -> {
                requireArgs(args, 4);
                ChunkCoordinates pos = parsePosition(sender, args, 1);
                WaterVolume volume = volumes.getVolumeAt(pos.posX, pos.posY, pos.posZ);

                if (volume == null) {
                    sendChatToPlayer(sender, "There is no volume there.");
                } else {
                    sendInfo(sender, volumes, volume);
                }
            }
            case "list" -> list(sender, volumes);
            case "delete" -> {
                requireArgs(args, 2);
                WaterVolume volume = parseVolume(sender, volumes, args[1]);
                volumes.deleteVolume(volume);
                sendChatToPlayer(sender, "Deleted volume " + volume.getId() + ".");
            }
            case "why" -> why(sender, volumes, args);
            default -> sendHelpMessage(sender);
        }
    }

    private void create(ICommandSender sender, DamVolumes volumes, String[] args) {
        requireArgs(args, 4);

        ChunkCoordinates pos = parsePosition(sender, args, 1);
        double litres = args.length > 4 ? parseDoubleWithMin(sender, args[4], 0) : WaterVolume.LITRES_PER_CELL;

        if (litres <= 0) throw new CommandException("A new volume must hold some water.");

        WaterVolume volume = volumes.createVolume(pos.posX, pos.posY, pos.posZ, litres);

        if (volume == null) {
            throw new CommandException(
                "Could not create a volume there: the block can't hold water, or no ids are free.");
        }

        sendChatToPlayer(sender, "Created volume " + volume.getId() + ".");
        sendInfo(sender, volumes, volume);
    }

    private void set(ICommandSender sender, DamVolumes volumes, String[] args) {
        requireArgs(args, 3);

        WaterVolume volume = parseVolume(sender, volumes, args[1]);
        double target = parseDoubleWithMin(sender, args[2], 0);
        double litres = volume.getLitres();

        if (target > litres) {
            volumes.addLitres(volume, target - litres);
        } else {
            volumes.removeLitres(volume, litres - target);
        }

        WaterVolume current = volumes.follow(volume);

        if (current == null) {
            sendChatToPlayer(sender, "Volume " + volume.getId() + " is empty and was deleted.");
        } else {
            sendMergeNotice(sender, volumes, volume);
            sendInfo(sender, volumes, current);
        }
    }

    private static void sendMergeNotice(ICommandSender sender, DamVolumes volumes, WaterVolume volume) {
        WaterVolume current = volumes.follow(volume);

        if (current != null && current != volume) {
            sendChatToPlayer(sender, "Volume " + volume.getId() + " was merged into volume " + current.getId() + ".");
        }
    }

    /// Runs the fill for the volume's next layer without placing anything, and reports what would stop it.
    private void why(ICommandSender sender, DamVolumes volumes, String[] args) {
        requireArgs(args, 2);

        WaterVolume volume = parseVolume(sender, volumes, args[1]);
        World world = sender.getEntityWorld();
        int nextLayer = volume.getTopY() + 1;

        sendChatToPlayer(sender, String.format("Volume %d, growing into layer y=%d:", volume.getId(), nextLayer));

        if (volume.getTopLayerFill() < 1) {
            sendChatToPlayer(
                sender,
                String.format(
                    " The top layer is only %.1f%% full, so the volume isn't trying to grow yet.",
                    volume.getTopLayerFill() * 100));
        }

        long retry = volumes.getTicksUntilExpansionRetry(volume);

        if (retry > 0) {
            sendChatToPlayer(sender, String.format(" The last attempt failed; it tries again in %d ticks.", retry));
        }

        if (nextLayer >= world.getHeight()) {
            sendChatToPlayer(sender, " The top layer is at the build limit.");
            return;
        }

        VolumeFill fill = VolumeFill.growth(world, volume);

        if (!fill.escapes.isEmpty()) {
            sendEscapes(sender, fill.escapes);
            return;
        }

        if (fill.isEmpty()) {
            sendChatToPlayer(sender, " There's no open space directly above the top layer.");
        } else {
            sendChatToPlayer(
                sender,
                String.format(
                    " Nothing blocks it: it would add %d cells (%d in the new layer, %d in pockets below it).",
                    fill.surface.size() + fill.pockets.size(),
                    fill.surface.size(),
                    fill.pockets.size()));
        }
    }

    private static void sendEscapes(ICommandSender sender, List<VolumeFill.Escape> escapes) {
        final int shown = 10;

        sendChatToPlayer(sender, String.format(" Blocked: water escapes in %d place(s).", escapes.size()));

        for (VolumeFill.Escape escape : escapes.subList(0, Math.min(shown, escapes.size()))) {
            sendChatToPlayer(
                sender,
                String.format(
                    "  %s: %s, spreading from %s",
                    formatCell(escape.cell),
                    describe(escape.reason),
                    formatCell(escape.seed)));
        }

        if (escapes.size() > shown) {
            sendChatToPlayer(sender, String.format("  ...and %d more.", escapes.size() - shown));
        }
    }

    private static String describe(VolumeFill.EscapeReason reason) {
        return switch (reason) {
            case TOO_FAR -> "open space more than " + VolumeFill.MAX_SPREAD + " blocks sideways";
            case TOO_DEEP -> "a drop of more than " + VolumeFill.MAX_DEPTH + " blocks";
            case UNLOADED -> "an unloaded chunk";
        };
    }

    private static String formatCell(long cell) {
        return String.format(
            "(%d, %d, %d)",
            CoordinatePacker.unpackX(cell),
            CoordinatePacker.unpackY(cell),
            CoordinatePacker.unpackZ(cell));
    }

    private void list(ICommandSender sender, DamVolumes volumes) {
        if (volumes.getVolumes()
            .isEmpty()) {
            sendChatToPlayer(sender, "There are no volumes in this dimension.");
            return;
        }

        for (WaterVolume volume : volumes.getVolumes()) {
            sendChatToPlayer(
                sender,
                String.format(
                    "#%d: %.0f L, level %.3f, %d cells",
                    volume.getId(),
                    volume.getLitres(),
                    volume.getLevel(),
                    volume.getCellCount()));
        }
    }

    private void sendInfo(ICommandSender sender, DamVolumes volumes, WaterVolume volume) {
        if (volumes.getVolume(volume.getId()) == null) {
            sendChatToPlayer(sender, "Volume " + volume.getId() + " no longer exists.");
            return;
        }

        sendChatToPlayer(sender, "Volume " + volume.getId() + ":");
        sendChatToPlayer(sender, String.format(" Litres: %.1f / %.0f", volume.getLitres(), volume.getCapacity()));
        sendChatToPlayer(
            sender,
            String.format(" Level: %.4f (top layer %.1f%% full)", volume.getLevel(), volume.getTopLayerFill() * 100));
        sendChatToPlayer(
            sender,
            String.format(
                " Cells: %d in %d layers (y %d to %d)",
                volume.getCellCount(),
                volume.getLayerCount(),
                volume.getBottomY(),
                volume.getTopY()));
        sendChatToPlayer(sender, String.format(" Top layer area: %d", volume.getLayerArea(volume.getTopY())));
        sendLeaks(sender, volume);
    }

    private void sendLeaks(ICommandSender sender, WaterVolume volume) {
        HashSet3D leaks = volume.getLeaks();

        sendChatToPlayer(sender, String.format(" Leaks: %d", leaks.size()));

        LongIterator iter = leaks.iterator();

        while (iter.hasNext()) {
            long leak = iter.nextLong();

            int y = CoordinatePacker.unpackY(leak);

            sendChatToPlayer(
                sender,
                String.format(
                    "  (%d, %d, %d) %s",
                    CoordinatePacker.unpackX(leak),
                    y,
                    CoordinatePacker.unpackZ(leak),
                    volume.isLeakActive(y) ? "draining" : "dry"));
        }
    }

    @NotNull
    private static WaterVolume parseVolume(ICommandSender sender, DamVolumes volumes, String arg) {
        int id = parseIntBounded(sender, arg, 1, DamWaterMeta.MAX_ID);
        WaterVolume volume = volumes.getVolume(id);

        if (volume == null) throw new CommandException("There is no volume with id " + id + ".");

        return volume;
    }

    @NotNull
    private static ChunkCoordinates parsePosition(ICommandSender sender, String[] args, int start) {
        ChunkCoordinates origin = sender.getPlayerCoordinates();

        int x = (int) Math.floor(func_110666_a(sender, origin.posX + 0.5, args[start]));
        int y = (int) Math.floor(func_110666_a(sender, origin.posY, args[start + 1]));
        int z = (int) Math.floor(func_110666_a(sender, origin.posZ + 0.5, args[start + 2]));

        return new ChunkCoordinates(x, y, z);
    }

    private static void requireArgs(String[] args, int count) {
        if (args.length < count) {
            throw new WrongUsageException("Not enough arguments for '" + args[0] + "'. See /hydrodam for usage.");
        }
    }
}
