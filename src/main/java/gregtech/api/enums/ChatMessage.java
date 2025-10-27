package gregtech.api.enums;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import gregtech.GTMod;
import gregtech.api.net.GTPacketChat;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Localized;

/**
 * Each entry in this enum represents a chat message that can be sent to the client arbitrarily. These are essentially
 * fancy localization keys, except they can have custom logic on top of localization. Most are chat messages, but that
 * isn't a hard requirement. Exact use and arguments are defined per-entry. These can be chained recursively via
 * {@link Localized}, which can accept other Localized instances as format arguments (they are localized when the
 * Localized instance is converted to a string, not at construction).
 *
 * @see GTPacketChat
 * @see Localized
 * @see GTUtility#processFormatStacks(String)
 * @see gregtech.api.util.GTTextBuilder
 */
public enum ChatMessage {

    PowerfailsCleared(localized("GT5U.gui.chat.powerfail.cleared")),
    PowerfailsClearedDim(localized("GT5U.gui.chat.powerfail.cleared-dim")),
    PowerfailsListNone(localized("GT5U.gui.chat.powerfail.list.none")),
    PowerfailsListHeader(localized("GT5U.gui.chat.powerfail.list.uncleared-header")),
    PowerfailsListEntry(localized("GT5U.gui.chat.powerfail.list.entry")),
    PowerfailsListTruncated(localized("GT5U.gui.chat.powerfail.list.truncated")),
    PowerfailGreeting(plural("GT5U.gui.chat.powerfail.login", "GT5U.gui.chat.powerfail.login.plural", 0)),
    PowerfailCommandHint(localized("GT5U.gui.chat.powerfail.hint")),
    PowerfailRenderShown(localized("GT5U.gui.chat.powerfail.shown")),
    PowerfailRenderHidden(localized("GT5U.gui.chat.powerfail.hidden")),

    PowerfailWaypoint(plural("GT5U.gui.chat.powerfail.waypoint", "GT5U.gui.chat.powerfail.waypoint.plural", 1)),
    PowerfailDescription(plural("GT5U.gui.chat.powerfail", "GT5U.gui.chat.powerfail.plural", 3)),

    Dimension(args -> GTUtility.getDimensionName((Integer) args[0])),

    ;

    interface Localizer {

        String localize(Object[] args);
    }

    private final Localizer localizer;

    ChatMessage(Localizer localizer) {
        this.localizer = localizer;
    }

    public String localize(Object... args) {
        // Style push and pops, see GTUtility.processFormatStacks
        return "§s" + localizer.localize(args) + "§t";
    }

    public void send(EntityPlayer player, Object... args) {
        if (GTUtility.isServer()) {
            GTValues.NW.sendToPlayer(new GTPacketChat(this, args), (EntityPlayerMP) player);
        } else {
            GTUtility.sendChatToPlayer(player, localize(args));
        }
    }

    private static Localizer localized(String key) {
        return args -> GTUtility.translate(key, Localized.processArgs(args));
    }

    private static Localizer plural(String singular, String plural, int pluralIndex) {
        return args -> {
            int value;

            if (args[pluralIndex] instanceof Integer i) {
                value = i;
            } else {
                StringBuilder sb = new StringBuilder();

                char[] charArray = args[pluralIndex].toString()
                    .toCharArray();

                for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
                    char c = charArray[i];

                    if (c == '§') {
                        i++;
                        continue;
                    }

                    if (c >= '0' && c <= '9') {
                        sb.append(c);
                    }
                }

                try {
                    value = Integer.parseInt(sb.toString());
                } catch (NumberFormatException e) {
                    GTMod.GT_FML_LOGGER.warn(
                        "Could not parse ChatMessage value to check if the index is plural. Lang Key (Singular)={}, Index={}, Value={}",
                        singular,
                        pluralIndex,
                        args[pluralIndex],
                        new Exception());
                    value = 2;
                }
            }

            String key = value == 1 ? singular : plural;

            return GTUtility.translate(key, Localized.processArgs(args));
        };
    }
}
