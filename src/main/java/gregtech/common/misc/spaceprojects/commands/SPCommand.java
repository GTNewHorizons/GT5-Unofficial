package gregtech.common.misc.spaceprojects.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.commands.GTBaseCommand;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

/**
 * @author BlueWeabo
 */
public class SPCommand extends GTBaseCommand {

    private static final Set<Pair<EntityPlayerMP, EntityPlayerMP>> invite = Collections
        .newSetFromMap(new WeakHashMap<>());
    private static final Set<EntityPlayerMP> confirm = Collections.newSetFromMap(new WeakHashMap<>());

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
            sendHelpMessage(sender);
            return;
        }
        switch (arguments[0]) {
            case INVITE -> {
                if (arguments.length < 2) {
                    return;
                }
                processInvite(sender, arguments[1]);
            }
            case ACCEPT -> {
                if (arguments.length < 2) {
                    return;
                }
                processAccept(sender, arguments[1]);
            }
            case LEAVE -> processLeave(sender);
            case CONFIRM -> processConfirm(sender);
        }
    }

    private void processInvite(ICommandSender sender, String playerInvited) {
        EntityPlayerMP teamLeader = getCommandSenderAsPlayer(sender);
        EntityPlayerMP teamMember = getPlayer(sender, playerInvited);
        invite.add(Pair.of(teamMember, teamLeader));
        sendChatToPlayer(
            teamMember,
            new ChatComponentTranslation(
                "GT5U.chat.space_project.invite",
                EnumChatFormatting.GOLD + teamLeader.getCommandSenderName() + EnumChatFormatting.RESET,
                EnumChatFormatting.GOLD + "/sp accept "
                    + teamLeader.getCommandSenderName()
                    + EnumChatFormatting.RESET));
    }

    private void processAccept(ICommandSender sender, String playerInviter) {
        EntityPlayerMP teamMember = getCommandSenderAsPlayer(sender);
        EntityPlayerMP teamLeader = getPlayer(sender, playerInviter);
        if (invite.contains(Pair.of(teamMember, teamLeader))) {
            SpaceProjectManager.putInTeam(teamMember.getUniqueID(), teamLeader.getUniqueID());
            sendChatToPlayer(
                teamLeader,
                new ChatComponentTranslation(
                    "GT5U.chat.space_project.invite_accepted",
                    EnumChatFormatting.GOLD + teamMember.getCommandSenderName() + EnumChatFormatting.RESET));
            invite.remove(Pair.of(teamMember, teamLeader));
        }
    }

    private void processLeave(ICommandSender sender) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        sendChatToPlayer(
            player,
            new ChatComponentTranslation(
                "GT5U.chat.space_project.leave_confirm",
                EnumChatFormatting.GOLD + "/sp confirm" + EnumChatFormatting.RESET));
        confirm.add(player);
    }

    private void processConfirm(ICommandSender sender) {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (confirm.contains(player)) {
            SpaceProjectManager.putInTeam(player.getUniqueID(), player.getUniqueID());
            sendChatToPlayer(player, new ChatComponentTranslation("GT5U.chat.space_project.left"));
            confirm.remove(player);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] arguments) {
        List<String> autoComplete = new ArrayList<>();
        String filter = arguments.length == 0 ? "" : arguments[0].trim();
        switch (arguments.length) {
            case 1 -> autoComplete.addAll(Arrays.asList(getSubCommands()));
            case 2 -> {
                filter = arguments[1].trim();
                if (arguments[0].equals(INVITE)) {
                    autoComplete.addAll(Arrays.asList(getPlayers()));
                    break;
                }

                if (arguments[0].equals(CONFIRM)) {
                    Optional<Pair<EntityPlayerMP, EntityPlayerMP>> pairOpt = invite.stream()
                        .filter(
                            (e) -> e.getKey()
                                .getUniqueID() == getCommandSenderAsPlayer(sender).getUniqueID())
                        .findFirst();
                    if (pairOpt.isPresent()) {
                        autoComplete.add(
                            SpaceProjectManager.getPlayerNameFromUUID(
                                pairOpt.get()
                                    .getRight()
                                    .getUniqueID()));
                    }
                }
            }
        }
        String finalFilter = filter;
        return autoComplete.stream()
            .filter(s -> finalFilter.isEmpty() || s.startsWith(finalFilter))
            .collect(Collectors.toList());
    }

    private String[] getPlayers() {
        return MinecraftServer.getServer()
            .getAllUsernames();
    }

    private String[] getSubCommands() {
        return new String[] { INVITE, ACCEPT, LEAVE, CONFIRM };
    }

}
