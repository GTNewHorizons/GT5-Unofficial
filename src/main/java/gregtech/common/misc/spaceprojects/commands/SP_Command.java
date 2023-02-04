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

public class SP_Command extends CommandBase {

    private static Set<Pair<EntityPlayerMP, EntityPlayerMP>> mInvite = Collections
            .newSetFromMap(new WeakHashMap<Pair<EntityPlayerMP, EntityPlayerMP>, Boolean>());
    private static Set<EntityPlayerMP> mConfirm = Collections.newSetFromMap(new WeakHashMap<EntityPlayerMP, Boolean>());

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
    public String getCommandUsage(ICommandSender aSender) {
        return "/" + getCommandName() + "<subCommand> [PlayerName]";
    }

    @Override
    public void processCommand(ICommandSender aSender, String[] aArguments) {
        if (aArguments.length < 1) {
            return;
        }
        switch (aArguments[0]) {
            case INVITE:
                if (aArguments.length < 2) {
                    return;
                }
                processInvite(aSender, aArguments[1]);
                break;
            case ACCEPT:
                if (aArguments.length < 2) {
                    return;
                }
                processAccept(aSender, aArguments[1]);
                break;
            case LEAVE:
                processLeave(aSender);
                break;
            case CONFIRM:
                processConfirm(aSender);
                break;
        }
    }

    private void processInvite(ICommandSender aSender, String aPlayerInvited) {
        EntityPlayerMP tTeamLeader = getCommandSenderAsPlayer(aSender);
        EntityPlayerMP tTeamMember = getPlayer(aSender, aPlayerInvited);
        mInvite.add(Pair.of(tTeamMember, tTeamLeader));
        String tMessage = EnumChatFormatting.GOLD + tTeamLeader.getCommandSenderName()
                + EnumChatFormatting.RESET
                + " has sent you an invite to join their team. Accept it with"
                + EnumChatFormatting.GOLD
                + "/sp accept "
                + tTeamLeader.getCommandSenderName();
        GT_Utility.sendChatToPlayer(tTeamMember, tMessage);
    }

    private void processAccept(ICommandSender aSender, String aPLayerInviter) {
        EntityPlayerMP tTeamMember = getCommandSenderAsPlayer(aSender);
        EntityPlayerMP tTeamLeader = getPlayer(aSender, aPLayerInviter);
        if (mInvite.contains(Pair.of(tTeamMember, tTeamLeader))) {
            String tMessage = EnumChatFormatting.GOLD + tTeamMember.getCommandSenderName()
                    + EnumChatFormatting.RESET
                    + " has accepted the invite.";
            SpaceProjectManager.putInTeam(tTeamMember.getUniqueID(), tTeamLeader.getUniqueID());
            GT_Utility.sendChatToPlayer(tTeamLeader, tMessage);
            mInvite.remove(Pair.of(tTeamMember, tTeamLeader));
        }
    }

    private void processLeave(ICommandSender aSender) {
        EntityPlayerMP tPlayer = getCommandSenderAsPlayer(aSender);
        String tMessage = "Are you sure you want to leave the team. You will lose all progress. Use /sp confirm to confirm this. This does nothing if you are the team leader.";
        GT_Utility.sendChatToPlayer(tPlayer, tMessage);
        mConfirm.add(tPlayer);
    }

    private void processConfirm(ICommandSender aSender) {
        EntityPlayerMP tPlayer = getCommandSenderAsPlayer(aSender);
        if (mConfirm.contains(tPlayer)) {
            String tMessage = "Successfully left the team.";
            SpaceProjectManager.putInTeam(tPlayer.getUniqueID(), tPlayer.getUniqueID());
            GT_Utility.sendChatToPlayer(tPlayer, tMessage);
            mConfirm.remove(tPlayer);
        }
    }

}
