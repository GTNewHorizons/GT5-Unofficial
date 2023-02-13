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
    public String getCommandUsage(ICommandSender aSender) {
        return "/" + this.getCommandName() + " <subCommand>. Available subCommands: reset, unlock, lock, list, copy";
    }

    @Override
    public void processCommand(ICommandSender aSender, String[] aArguments) {
        if (aArguments.length < 1) {
            printHelp(aSender);
            return;
        }
        switch (aArguments[0]) {
            case RESET:
                if (!aSender.canCommandSenderUseCommand(4, getCommandName())) {
                    aSender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                processReset(aSender, aArguments.length >= 2 ? aArguments[1] : aSender.getCommandSenderName());
                break;
            case UNLOCK:
                if (!aSender.canCommandSenderUseCommand(4, getCommandName())) {
                    aSender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                if (aArguments.length < 3) {
                    aSender.addChatMessage(
                            new ChatComponentText("Not enough arguments. Needs to mention a project and a location"));
                    return;
                }
                processUnlock(
                        aSender,
                        aArguments[1],
                        aArguments[2],
                        aArguments.length >= 4 ? aArguments[3] : aSender.getCommandSenderName());
                break;
            case LOCK:
                if (!aSender.canCommandSenderUseCommand(4, getCommandName())) {
                    aSender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                if (aArguments.length < 3) {
                    aSender.addChatMessage(
                            new ChatComponentText("Not enough arguments. Needs to mention a project and a location"));
                    return;
                }
                processLock(
                        aSender,
                        aArguments[1],
                        aArguments[2],
                        aArguments.length >= 4 ? aArguments[3] : aSender.getCommandSenderName());
            case LIST:
                if (aArguments.length < 2) {
                    aSender.addChatMessage(
                            new ChatComponentText(
                                    "No Argument for list subCommand. Usage /spm list -all, -available or -unlocked"));
                    return;
                }
                processList(
                        aSender,
                        aArguments[1],
                        aArguments.length >= 3 ? aArguments[2] : aSender.getCommandSenderName());
                break;
            case COPY:
                if (!aSender.canCommandSenderUseCommand(4, getCommandName())) {
                    aSender.addChatMessage(
                            new ChatComponentText("You don't have the permissions to execute this command"));
                    return;
                }
                if (aArguments.length < 3) {
                    aSender.addChatMessage(new ChatComponentText("Not enough arguments. Needs to mention 2 players"));
                    return;
                }
                processCopy(aSender, aArguments[1], aArguments[2]);
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender aSender, String[] aArguments) {
        List<String> tAutoComplete = new ArrayList<>();
        switch (aArguments.length) {
            case 1:
                tAutoComplete.addAll(Arrays.asList(getSubCommands()));
                break;
            case 2:
                if (aArguments[0].equals(LIST)) {
                    tAutoComplete.addAll(Arrays.asList(getListArguments()));
                } else if (aArguments[0].equals(COPY) || aArguments[0].equals(RESET)) {
                    tAutoComplete.addAll(Arrays.asList(getPlayers()));
                } else {
                    tAutoComplete.addAll(Arrays.asList(getProjects()));
                }
                break;
            case 3:
                if (aArguments[1].equals(ALL)) {
                    break;
                } else if (aArguments[0].equals(LIST)) {
                    tAutoComplete.addAll(Arrays.asList(getPlayers()));
                } else {
                    tAutoComplete.addAll(Arrays.asList(getLocations()));
                }
                break;
            case 4:
                tAutoComplete.addAll(Arrays.asList(getPlayers()));
                break;
        }
        return tAutoComplete;
    }

    private String[] getPlayers() {
        return MinecraftServer.getServer().getAllUsernames();
    }

    private String[] getLocations() {
        return SpaceProjectManager.getLocations().stream().map(body -> body.getName()).collect(Collectors.toList())
                .toArray(new String[0]);
    }

    private String[] getProjects() {
        return SpaceProjectManager.getProjects().stream().map(project -> project.getProjectName())
                .collect(Collectors.toList()).toArray(new String[0]);
    }

    private String[] getSubCommands() {
        return new String[] { RESET, COPY, UNLOCK, LOCK, LIST };
    }

    private String[] getListArguments() {
        return new String[] { ALL, AVAILABLE, UNLOCKED };
    }

    private void processReset(ICommandSender aSender, String aPlayerName) {
        SpaceProjectManager.mSpaceTeamProjects.clear();
        SpaceProjectWorldSavedData.INSTANCE.markDirty();
    }

    private void processLock(ICommandSender aSender, String aProjectName, String aLocation, String aPlayerName) {
        UUID tID = SpaceProjectManager.getPlayerUUIDFromName(aPlayerName);
        SpaceProjectManager.addTeamProject(tID, getLocation(aLocation), aProjectName, null);
    }

    private void processUnlock(ICommandSender aSender, String aProjectName, String aLocation, String aPlayerName) {
        UUID tID = SpaceProjectManager.getPlayerUUIDFromName(aPlayerName);
        ISpaceProject tProject = SpaceProjectManager.getProject(aProjectName);
        if (tProject != null) {
            tProject.setProjectStage(tProject.getTotalStages());
            SpaceProjectManager.addTeamProject(tID, getLocation(aLocation), aProjectName, tProject);
        } else {
            aSender.addChatMessage(new ChatComponentText("Incorrect internal project name. Try again"));
        }
    }

    private void processList(ICommandSender aSender, String aArgument, String aPlayerName) {
        UUID tID = SpaceProjectManager.getPlayerUUIDFromName(aPlayerName);
        switch (aArgument) {
            case ALL:
                for (String tProject : SpaceProjectManager.getAllProjects().keySet()) {
                    aSender.addChatMessage(new ChatComponentText(tProject));
                }
                break;
            case AVAILABLE:
                // Needs more thought
                break;
            case UNLOCKED:
                for (ISpaceProject tProject : SpaceProjectManager.getTeamSpaceProjects(tID)) {
                    aSender.addChatMessage(new ChatComponentText(tProject.getProjectName()));
                }
                break;
        }
    }

    private void processCopy(ICommandSender aSender, String aPlayerToCopyFrom, String aPlayerCopyingTo) {
        // This will take a while
    }

    private void printHelp(ICommandSender aSender) {

    }
}
