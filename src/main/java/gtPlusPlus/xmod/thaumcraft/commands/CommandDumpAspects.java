package gtPlusPlus.xmod.thaumcraft.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

import gregtech.commands.GTBaseCommand;
import gtPlusPlus.xmod.thaumcraft.objects.ThreadAspectScanner;

public class CommandDumpAspects extends GTBaseCommand {

    public static long mLastScanTime = System.currentTimeMillis();

    public CommandDumpAspects() {
        super("da", "dumpaspects", "dumptc");
    }

    @Override
    public String getCommandName() {
        return "dumpAspects";
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        final long T = System.currentTimeMillis();
        final long D = T - mLastScanTime;
        final int Z = (int) (D / 1000);
        if (Z >= 30) {
            // Lets process this in the Background on a new Thread.
            Thread t = createNewThread();
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.aspects.dump_start"));
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.aspects.dump_wait"));
            t.start();
        } else {
            sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.aspects.dump_cooldown", 30 - Z));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return isOpedPlayer(sender);
    }

    private static Thread createNewThread() {
        return new ThreadAspectScanner();
    }
}
