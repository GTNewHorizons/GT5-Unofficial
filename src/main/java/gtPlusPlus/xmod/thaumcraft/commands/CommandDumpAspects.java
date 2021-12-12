package gtPlusPlus.xmod.thaumcraft.commands;

import static gtPlusPlus.core.util.minecraft.PlayerUtils.messagePlayer;

import java.util.ArrayList;
import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.commands.CommandUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.thaumcraft.objects.ThreadAspectScanner;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandDumpAspects implements ICommand {
	private final List<String> aliases;
	public static long mLastScanTime = System.currentTimeMillis();

	public CommandDumpAspects() {
		this.aliases = new ArrayList<>();
		this.aliases.add("DA");
		this.aliases.add("da");
		this.aliases.add("dumpaspects");
		this.aliases.add("dumptc");
		Logger.INFO("Registered Aspect Dump Command.");
	}

	@Override
	public int compareTo(final Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "DumpAspects";
	}

	@Override
	public String getCommandUsage(final ICommandSender var1) {
		return "/DumpAspects";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(final ICommandSender S, final String[] argString) {
		final EntityPlayer P = CommandUtils.getPlayer(S);
		final long T = System.currentTimeMillis();
		final long D = T - mLastScanTime;
		final int Z = (int) (D / 1000);
		if (Z >= 30) {
			// Lets process this in the Background on a new Thread.
			Thread t = createNewThread();
			messagePlayer(P, "Beginning to dump information about all items/blocks & their aspects to file.");
			messagePlayer(P, "Please do not close your game during this process, you will be notified upon completion.");
			t.start();
		} else {
			messagePlayer(P, "Your last run of DA was less than 30 seconds ago, please wait " + (30 - Z)
					+ " seconds before trying again.");
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(final ICommandSender var1) {
		final EntityPlayer P = CommandUtils.getPlayer(var1);
		if (P == null || !PlayerUtils.isPlayerOP(P)) {
			return false;
		}
		return true;
	}

	@Override
	public List<?> addTabCompletionOptions(final ICommandSender var1, final String[] var2) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(final String[] var1, final int var2) {
		return false;
	}

	private static Thread createNewThread() {
		return new ThreadAspectScanner();
	}

}