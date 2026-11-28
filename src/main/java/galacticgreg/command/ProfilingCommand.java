package galacticgreg.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

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
        sendChatToPlayer(sender, new ChatComponentTranslation("GT5U.chat.oregen.profiling"));
        for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
            for (ModDimensionDef mdd : mc.getDimensionList()) {
                long tTime = GalacticGreg.Profiler.GetAverageTime(mdd);
                ChatComponentTranslation time = tTime == -1
                    ? new ChatComponentTranslation("GT5U.gui.text.drone_no_data")
                    : new ChatComponentTranslation("GT5U.chat.oregen.profiling.time", tTime);
                sendChatToPlayer(
                    sender,
                    new ChatComponentTranslation(
                        "GT5U.chat.oregen.profiling.dimension",
                        mdd.getDimIdentifier(),
                        mdd.getDimensionName(),
                        time));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return isOpedPlayer(sender);
    }
}
