package gregtech.common.misc.spaceprojects.commands;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.getLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.SpaceProjectWorldSavedData;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

/**
 * @author BlueWeabo
 */
public class SPM_Command extends CommandBase {

    private static final String RESET = "reset";
    private static final String UNLOCK = "unlock";
    private static final String LOCK = "lock";
    private static final String LIST = "list";
    private static final String ALL = "-all";
    private static final String AVAILABLE = "-available";
    private static final String UNLOCKED = "-unlocked";
    private static final String COPY = "copy";

    @Override
    public String getCommandName() {
        return "spm";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName() + " <subCommand>. Available subCommands: reset, unlock, lock, list, copy";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {
        if (arguments.length < 1) {
            printHelp(sender);
            return;
        }
        switch (arguments[0]) {
            case RESET:
                if (!sender.canCommandSenderUseCommand(4, getCommandName())) {
                    sender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                processReset(sender, arguments.length >= 2 ? arguments[1] : sender.getCommandSenderName());
                break;
            case UNLOCK:
                if (!sender.canCommandSenderUseCommand(4, getCommandName())) {
                    sender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                if (arguments.length < 3) {
                    sender.addChatMessage(
                            new ChatComponentText("Not enough arguments. Needs to mention a project and a location"));
                    return;
                }
                processUnlock(
                        sender,
                        arguments[1],
                        arguments[2],
                        arguments.length >= 4 ? arguments[3] : sender.getCommandSenderName());
                break;
            case LOCK:
                if (!sender.canCommandSenderUseCommand(4, getCommandName())) {
                    sender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                if (arguments.length < 3) {
                    sender.addChatMessage(
                            new ChatComponentText("Not enough arguments. Needs to mention a project and a location"));
                    return;
                }
                processLock(
                        sender,
                        arguments[1],
                        arguments[2],
                        arguments.length >= 4 ? arguments[3] : sender.getCommandSenderName());
            case LIST:
                if (arguments.length < 2) {
                    sender.addChatMessage(
                            new ChatComponentText(
                                    "No Argument for list subCommand. Usage /spm list -all, -available or -unlocked"));
                    return;
                }
                processList(sender, arguments[1], arguments.length >= 3 ? arguments[2] : sender.getCommandSenderName());
                break;
            case COPY:
                if (!sender.canCommandSenderUseCommand(4, getCommandName())) {
                    sender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                if (arguments.length < 3) {
                    sender.addChatMessage(new ChatComponentText("Not enough arguments. Needs to mention 2 players"));
                    return;
                }
                processCopy(sender, arguments[1], arguments[2]);
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] arguments) {
        List<String> autoComplete = new ArrayList<>();
        String filter = arguments.length == 0 ? "" : arguments[0].trim();
        switch (arguments.length) {
            case 1 -> autoComplete.addAll(Arrays.asList(getSubCommands()));
            case 2 -> {
                filter = arguments.length == 1 ? "" : arguments[1].trim();
                if (arguments[0].equals(LIST)) {
                    autoComplete.addAll(Arrays.asList(getListArguments()));
                } else if (arguments[0].equals(COPY) || arguments[0].equals(RESET)) {
                    autoComplete.addAll(Arrays.asList(getPlayers()));
                } else {
                    autoComplete.addAll(Arrays.asList(getProjects()));
                }
            }
            case 3 -> {
                filter = arguments.length == 2 ? "" : arguments[2].trim();
                if (arguments[1].equals(ALL)) {} else if (arguments[0].equals(LIST)) {
                    autoComplete.addAll(Arrays.asList(getPlayers()));
                } else {
                    autoComplete.addAll(Arrays.asList(getLocations()));
                }
            }
            case 4 -> {
                filter = arguments.length == 3 ? "" : arguments[3].trim();
                autoComplete.addAll(Arrays.asList(getPlayers()));
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

    private String[] getLocations() {
        return SpaceProjectManager.getLocationNames()
                                  .toArray(new String[0]);
    }

    private String[] getProjects() {
        return SpaceProjectManager.getProjectsMap()
                                  .keySet()
                                  .toArray(new String[0]);
    }

    private String[] getSubCommands() {
        return new String[] { RESET, COPY, UNLOCK, LOCK, LIST };
    }

    private String[] getListArguments() {
        return new String[] { ALL, AVAILABLE, UNLOCKED };
    }

    private void processReset(ICommandSender sender, String playerName) {
        SpaceProjectManager.spaceTeamProjects.clear();
        SpaceProjectWorldSavedData.INSTANCE.markDirty();
        sender.addChatMessage(new ChatComponentText("Cleared away map"));
    }

    private void processLock(ICommandSender sender, String projectName, String location, String playerName) {
        UUID tID = SpaceProjectManager.getPlayerUUIDFromName(playerName);
        SpaceProjectManager.addTeamProject(tID, getLocation(location), projectName, null);
        sender.addChatMessage(new ChatComponentText("Project locked"));
    }

    private void processUnlock(ICommandSender sender, String projectName, String location, String playerName) {
        UUID tID = SpaceProjectManager.getPlayerUUIDFromName(playerName);
        ISpaceProject tProject = SpaceProjectManager.getTeamProjectOrCopy(tID, projectName, getLocation(location));
        if (tProject != null) {
            tProject.setProjectCurrentStage(tProject.getTotalStages());
            SpaceProjectManager.addTeamProject(tID, getLocation(location), projectName, tProject);
            sender.addChatMessage(new ChatComponentText("Project unlocked"));
        } else {
            sender.addChatMessage(new ChatComponentText("Incorrect internal project name. Try again"));
        }
    }

    private void processList(ICommandSender sender, String argument, String playerName) {
        UUID tID = SpaceProjectManager.getPlayerUUIDFromName(playerName);
        switch (argument) {
            case ALL -> {
                for (String project : SpaceProjectManager.getProjectsMap()
                                                         .keySet()) {
                    sender.addChatMessage(new ChatComponentText(project));
                }
            }
            case AVAILABLE -> {
                for (ISpaceProject project : SpaceProjectManager.getAllProjects()) {
                    if (project.meetsRequirements(tID, false)) {
                        sender.addChatMessage(new ChatComponentText(project.getProjectName()));
                    }
                }
            }
            case UNLOCKED -> {
                for (ISpaceProject project : SpaceProjectManager.getTeamSpaceProjects(tID)) {
                    sender.addChatMessage(new ChatComponentText(project.getProjectName()));
                }
            }
        }
    }

    private void processCopy(ICommandSender sender, String playerToCopyFrom, String playerCopyingTo) {
        // This will take a while
    }

    private void printHelp(ICommandSender sender) {

    }
}
