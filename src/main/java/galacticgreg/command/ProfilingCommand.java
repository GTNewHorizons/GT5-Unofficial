package galacticgreg.command;

import net.minecraft.command.ICommandSender;

import galacticgreg.GalacticGreg;
import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.registry.GalacticGregRegistry;
import gregtech.commands.GTBaseCommand;

/**
 * Ingame command to get the average oregen time(s) for the active dimensions Doesn't need to be changed when adding new
 * planets/mods
 */
public class ProfilingCommand extends GTBaseCommand {

    @Override
    public String getCommandName() {
        return "ggregprofiler";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sendChatToPlayer(sender, "Average OreGen times:");
        for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
            for (ModDimensionDef mdd : mc.getDimensionList()) {
                long tTime = GalacticGreg.Profiler.GetAverageTime(mdd);
                String tInfo;
                if (tTime == -1) tInfo = "N/A";
                else tInfo = String.format("%d ms", tTime);
                sendChatToPlayer(
                    sender,
                    String.format("%s (%s): %s", mdd.getDimIdentifier(), mdd.getDimensionName(), tInfo));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return isOpedPlayer(sender);
    }
}
