package detrav.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.chunk.Chunk;

import gregtech.commands.GTBaseCommand;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

/**
 * Created by wital_000 on 17.03.2016.
 */
public class DetravScannerCommand extends GTBaseCommand {

    public DetravScannerCommand() {
        super("dscan");
    }

    @Override
    public String getCommandName() {
        return "detravscanner";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String name = null;

        ArrayList<String> strs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            strs.add(args[i]);
            if (!args[i].startsWith("\"")) {
                continue;
            }

            for (i++; i < args.length; i++) {
                String temp = strs.get(strs.size() - 1);
                temp = temp + " " + args[i];
                temp = temp.replace("\"", "");
                strs.set(strs.size() - 1, temp);
                if (args[i].endsWith("\"")) break;
            }
        }
        args = new String[strs.size()];
        args = strs.toArray(args);

        switch (args.length) {
            case 0:
                break;
            case 1:
                if (args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender);
                    return;
                }
                name = args[0];
                break;
            default:
                sendHelpMessage(sender);
                return;
        }
        ChunkCoordinates c = sender.getPlayerCoordinates();
        if (name != null) name = name.toLowerCase();
        process(sender, (int) Math.floor(c.posX / 16.0), (int) Math.floor(c.posZ / 16.0), name);
    }

    private void process(ICommandSender sender, int aX, int aZ, String fName) {
        Chunk c = sender.getEntityWorld()
            .getChunkFromChunkCoords(aX, aZ);

        if (c == null) {
            sender.addChatMessage(new ChatComponentText("ERROR"));
            return;
        }

        HashMap<String, Integer> ores = new HashMap<>();

        BiFunction<Integer, Integer, Integer> sum = Integer::sum;

        for (int x = 0; x < 16; x++) for (int z = 0; z < 16; z++) {
            int ySize = c.getHeightValue(x, z);
            for (int y = 1; y < ySize; y++) {
                Block b = c.getBlock(x, y, z);
                int meta = c.getBlockMetadata(x, y, z);

                try (OreInfo<?> info = OreManager.getOreInfo(b, meta)) {
                    if (info == null || info.isSmall) continue;

                    String matName = info.material.getLocalizedName();

                    if (fName == null || matName.toLowerCase()
                        .contains(fName)) {
                        ores.merge(matName, 1, sum);
                    }
                }
            }
        }

        sender.addChatMessage(new ChatComponentText("*** Detrav Scanner Begin"));
        for (String key : ores.keySet()) {
            sender.addChatMessage(new ChatComponentText(String.format("%s : %d", key, ores.get(key))));
        }
        sender.addChatMessage(new ChatComponentText("*** Detrav Scanner End"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length != 1) return null;
        if ("help".startsWith(args[0].toLowerCase())) {
            List<String> result = new ArrayList<>();
            result.add("help");
            sendHelpMessage(sender);
            return result;
        }
        return null;
    }
}
