package gtPlusPlus.core.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.MiningUtils;

public class CommandMath implements ICommand {

    private final List<String> aliases;

    protected String fullEntityName;
    protected Entity conjuredEntity;

    public CommandMath() {
        this.aliases = new ArrayList<>();
    }

    @Override
    public int compareTo(final Object o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "alkalus";
    }

    @Override
    public String getCommandUsage(final ICommandSender var1) {
        return "/alkalus [Dev Command]";
    }

    @Override
    public List<String> getCommandAliases() {
        return this.aliases;
    }

    @Override
    public void processCommand(final ICommandSender S, final String[] argString) {
        Logger.INFO("Debug Command");
        final World W = S.getEntityWorld();
        final EntityPlayer P = CommandUtils.getPlayer(S);
        if (P.getDisplayName().equalsIgnoreCase("draknyte1")) {
            Logger.INFO("[Bedrock Miner] OreType Scan");
            MiningUtils.iterateAllOreTypes();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender var1) {
        final EntityPlayer P = CommandUtils.getPlayer(var1);
        if (P == null) {
            return false;
        }
        if (P.getDisplayName().toLowerCase().equals("draknyte1")
                || P.getCommandSenderName().toLowerCase().equals("draknyte1")
                || CORE.DEVENV) {
            return true;
        }
        return false;
    }

    @Override
    public List<?> addTabCompletionOptions(final ICommandSender var1, final String[] var2) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(final String[] var1, final int var2) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean playerUsesCommand(final World W, final EntityPlayer P, final int cost) {

        return true;
    }
}
