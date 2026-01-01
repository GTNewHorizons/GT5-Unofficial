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
    EMPTY {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return indent.isEmpty() ? " " : indent + " ";
        }
    },
    DASHED {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return indent + color + StringUtils.getRepetitionOf('-', length);
        }
    },
    CONTINUOUS {

        @Override
        public String generate(EnumChatFormatting color, int length, String indent) {
            return indent + color + EnumChatFormatting.STRIKETHROUGH + StringUtils.getRepetitionOf('-', length);
        }
    };

    public abstract String generate(EnumChatFormatting color, int length, String indent);

    public static SeparatorStyle forSeparator(int style) {
        return switch (style) {
            case 0 -> EMPTY;
            case 1 -> DASHED;
            default -> CONTINUOUS;
        };
    }

    public static SeparatorStyle forFinisher(int style) {
        return switch (style) {
            case 0 -> NONE;
            case 1 -> EMPTY;
            case 2 -> DASHED;
            default -> CONTINUOUS;
        };
    }
}
