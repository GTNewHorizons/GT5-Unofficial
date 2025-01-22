package gregtech.common.misc.teams.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class TeamCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "gtteam";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        String filter = args.length == 0 ? "" : args[0].trim();
        switch (args.length) {
            case 1 -> completions.addAll(Arrays.asList(Subcommands.getSubcommands()));
            case 2 -> {
                filter = args[1].trim();
                if (args[0].equals(
                    Subcommands.INVITE.name()
                        .toLowerCase())) {

                }
            }
        }

        return super.addTabCompletionOptions(sender, args);
    }

    enum Subcommands {

        CREATE,
        RENAME,

        JOIN,
        INVITE,
        LEAVE,

        ACCEPT,
        DENY,

        PROMOTE,
        DEMOTE,

        INFO,

        SETDATA;

        private static final Subcommands[] VALUES = values();

        public static String[] getSubcommands() {
            return Arrays.stream(VALUES)
                .map(Enum::name)
                .map(String::toLowerCase)
                .toArray(String[]::new);
        }
    }
}
