package gregtech.common.misc.spaceprojects.commands;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.util.GT_Utility;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

/**
 * @author BlueWeabo
 */
public class SP_Command extends CommandBase {

    private static Set<Pair<EntityPlayerMP, EntityPlayerMP>> invite = Collections.newSetFromMap(new WeakHashMap<>());
    private static Set<EntityPlayerMP> confirm = Collections.newSetFromMap(new WeakHashMap<>());

    private static final String INVITE = "invite";
    private static final String ACCEPT = "accept";
    private static final String LEAVE = "leave";
    private static final String CONFIRM = "confirm";

    @Override
    public String getCommandName() {
        return "sp";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + "<subCommand> [PlayerName]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {
        if (arguments.length < 1) {
            return;
        }
        switch (arguments[0]) {
            case INVITE:
                if (arguments.length < 2) {
                    return;
                }
                processInvite(sender, arguments[1]);
                break;
            case ACCEPT:
                if (arguments.length < 2) {
                    return;
                }
                processAccept(sender, arguments[1]);
                break;
            case LEAVE:
                processLeave(sender);
                break;
            case CONFIRM:
                processConfirm(sender);
                break;
        }
    }

    private void processInvite(ICommandSender sender, String playerInvited) {
        EntityPlayerMP teamLeader = getCommandSenderAsPlayer(sender);
        EntityPlayerMP teamMember = getPlayer(sender, playerInvited);
        invite.add(Pair.of(teamMember, teamLeader));
        String message = EnumChatFormatting.GOLD + teamLeader.getCommandSenderName()
                + EnumChatFormatting.RESET
                + " has sent you an invite to join their team. Accept it with"
                + EnumChatFormatting.GOLD
                + " /sp accept "
                + teamLeader.getCommandSenderName();
        GT_Utility.sendChatToPlayer(teamMember, message);
    }

    private void processAccept(ICommandSender sender, String playerInviter) {
        EntityPlayerMP teamMember = getCommandSenderAsPlayer(sender);
        EntityPlayerMP teamLeader = getPlayer(sender, playerInviter);
        if (invite.contains(Pair.of(teamMember, teamLeader))) {
            String message = EnumChatFormatting.GOLD + teamMember.getCommandSenderName()
                    + EnumChatFormatting.RESET
                    + " has accepted the invite.";
            SpaceProjectManager.putInTeam(teamMember.getUniqueID(), teamLeader.getUniqueID());
            GT_Utility.sendChatToPlayer(teamLeader, message);
            invite.remove(Pair.of(teamMember, teamLeader));
        }
    }

    private void processLeave(ICommandSender sender) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        String message = "Are you sure you want to leave the team. You will lose all progress. Use "
                + EnumChatFormatting.GOLD
                + "/sp confirm"
                + EnumChatFormatting.RESET
                + " to confirm this. This does nothing if you are the team leader.";
        GT_Utility.sendChatToPlayer(player, message);
        confirm.add(player);
    }

    private void processConfirm(ICommandSender sender) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (confirm.contains(player)) {
            String message = "Successfully left the team.";
            SpaceProjectManager.putInTeam(player.getUniqueID(), player.getUniqueID());
            GT_Utility.sendChatToPlayer(player, message);
            confirm.remove(player);
        }
    }

}
