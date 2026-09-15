package gtPlusPlus.xmod.thaumcraft.commands;

import net.minecraft.command.ICommandSender;

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
            sendChatToPlayer(sender, "Beginning to dump information about all items/blocks & their aspects to file.");
            sendChatToPlayer(
                sender,
                "Please do not close your game during this process, you will be notified upon completion.");
            t.start();
        } else {
            sendChatToPlayer(
                sender,
                "Your last run of DA was less than 30 seconds ago, please wait " + (30 - Z)
                    + " seconds before trying again.");
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
