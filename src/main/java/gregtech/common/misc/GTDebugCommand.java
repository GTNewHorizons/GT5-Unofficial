package gregtech.common.misc;

import static com.gtnewhorizon.gtnhlib.util.CommandUtils.literal;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.brigadier.BrigadierApi;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

/**
 * {@code /gtdebug} - developer/debug subcommands, gated behind op level 4 (or singleplayer cheats).
 */
public final class GTDebugCommand {

    private GTDebugCommand() {}

    // spotless:off
    public static void register() {
        BrigadierApi.getCommandDispatcher().register(literal("gtdebug")
                .requires(src -> src.canCommandSenderUseCommand(4, "gtdebug"))
                .executes(ctx -> tell(ctx, EnumChatFormatting.YELLOW, "commands.generic.usage", "/gtdebug <ore-showroom>"))

                .then(literal("ore-showroom")
                        .executes(ctx -> oreShowroom(ctx, false))
                        .then(literal("confirm").executes(ctx -> oreShowroom(ctx, true))))
        );
    }
    // spotless:on

    private static int oreShowroom(CommandContext<ICommandSender> ctx, boolean confirm) {
        if (ctx.getSource() instanceof EntityPlayerMP player) {
            OreShowroom.process(player, confirm);
        } else {
            tell(ctx, EnumChatFormatting.RED, "GT5U.chat.ore_showroom.player_only");
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int tell(CommandContext<ICommandSender> ctx, EnumChatFormatting color, String key, Object... args) {
        ctx.getSource()
            .addChatMessage(new ChatComponentTranslation(key, args).setChatStyle(new ChatStyle().setColor(color)));
        return Command.SINGLE_SUCCESS;
    }
}
