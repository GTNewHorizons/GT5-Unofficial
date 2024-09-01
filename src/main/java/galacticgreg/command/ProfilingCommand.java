package galacticgreg.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import galacticgreg.GalacticGreg;
import galacticgreg.api.ModContainer;
import galacticgreg.api.ModDimensionDef;
import galacticgreg.registry.GalacticGregRegistry;

/**
 * Ingame command to get the average oregen time(s) for the active dimensions Doesn't need to be changed when adding new
 * planets/mods
 *
 */
public class ProfilingCommand implements ICommand {

    private List<String> aliases;

    public ProfilingCommand() {
        this.aliases = new ArrayList<>();
        this.aliases.add("ggregprofiler");
    }

    @Override
    public String getCommandName() {
        return "ggregprofiler";
    }

    @Override
    public String getCommandUsage(ICommandSender pCommandSender) {
        return "ggregprofiler";
    }

    @Override
    public List<String> getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender pCommandSender, String[] pArgs) {
        pCommandSender.addChatMessage(new ChatComponentText("Average OreGen times:"));

        for (ModContainer mc : GalacticGregRegistry.getModContainers()) {
            String tModName = mc.getModName();
            for (ModDimensionDef mdd : mc.getDimensionList()) {
                long tTime = GalacticGreg.Profiler.GetAverageTime(mdd);
                String tInfo;
                if (tTime == -1) tInfo = "N/A";
                else tInfo = String.format("%d ms", tTime);
                pCommandSender.addChatMessage(
                    new ChatComponentText(
                        String.format("%s (%s): %s", mdd.getDimIdentifier(), mdd.getDimensionName(), tInfo)));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender pCommandSender) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER
            && !FMLCommonHandler.instance()
                .getMinecraftServerInstance()
                .isDedicatedServer())
            return true;

        if (pCommandSender instanceof EntityPlayerMP) {
            EntityPlayerMP tEP = (EntityPlayerMP) pCommandSender;
            return MinecraftServer.getServer()
                .getConfigurationManager()
                .func_152596_g(tEP.getGameProfile());
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }
}
