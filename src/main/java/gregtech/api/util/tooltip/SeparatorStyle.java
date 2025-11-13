package gregtech.api.util.tooltip;

import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.StringUtils;

public enum SeparatorStyle {

    NONE {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return "";
        }
    },
    BLANK {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return indent.isEmpty() ? " " : indent + " ";
        }
    },
    PLAIN {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return indent + color + StringUtils.getRepetitionOf('-', length);
        }
    },
    STRIKETHROUGH {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return indent + color + EnumChatFormatting.STRIKETHROUGH + StringUtils.getRepetitionOf('-', length);
        }
    };

    public abstract String generate(EnumChatFormatting color, int length, String indent);

    public static SeparatorStyle forSeparator(int style) {
        return switch (style) {
            case 0 -> BLANK;
            case 1 -> PLAIN;
            default -> STRIKETHROUGH;
        };
    }

    public static SeparatorStyle forFinisher(int style) {
        return switch (style) {
            case 0 -> NONE;
            case 1 -> BLANK;
            case 2 -> PLAIN;
            default -> STRIKETHROUGH;
        };
    }
}
