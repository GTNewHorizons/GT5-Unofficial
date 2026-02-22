package gregtech.common.gui.modularui.multiblock.godforge.data;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;

public final class StarColors {

    public interface IStarColor {

        String getTitle();

        EnumChatFormatting getColor();

        int getHexColor();

        float getDefaultValue();

        String getTooltip(float value);
    }

    public enum RGB implements IStarColor {

        RED(EnumChatFormatting.RED, 0xFFFF5555, ForgeOfGodsStarColor.DEFAULT_RED),
        GREEN(EnumChatFormatting.GREEN, 0xFF55FF55, ForgeOfGodsStarColor.DEFAULT_GREEN),
        BLUE(EnumChatFormatting.BLUE, 0xFF5555FF, ForgeOfGodsStarColor.DEFAULT_BLUE);

        private final String title;
        private final EnumChatFormatting color;
        private final int hexColor;
        private final int defaultValue;

        RGB(EnumChatFormatting color, int hexColor, int defaultValue) {
            this.title = "fog.cosmetics.color." + name().toLowerCase();
            this.color = color;
            this.hexColor = hexColor;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getTitle() {
            return color + translateToLocal(title);
        }

        @Override
        public EnumChatFormatting getColor() {
            return color;
        }

        @Override
        public int getHexColor() {
            return hexColor;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String getTooltip(float value) {
            return String.format("%s: %d", getTitle(), (int) value);
        }
    }

    public enum HSV implements IStarColor {

        HUE(EnumChatFormatting.LIGHT_PURPLE, 0xFFFF55FF, ForgeOfGodsStarColor.DEFAULT_HUE),
        SATURATION(EnumChatFormatting.GOLD, 0xFFFFAA00, ForgeOfGodsStarColor.DEFAULT_SATURATION),
        VALUE(EnumChatFormatting.AQUA, 0xFF55FFFF, ForgeOfGodsStarColor.DEFAULT_VALUE);

        private final String title;
        private final EnumChatFormatting color;
        private final int hexColor;
        private final float defaultValue;

        HSV(EnumChatFormatting color, int hexColor, float defaultValue) {
            this.title = "fog.cosmetics.color." + name().toLowerCase();
            this.color = color;
            this.hexColor = hexColor;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getTitle() {
            return color + translateToLocal(title);
        }

        @Override
        public EnumChatFormatting getColor() {
            return color;
        }

        @Override
        public int getHexColor() {
            return hexColor;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String getTooltip(float value) {
            if (this == HUE) {
                return String.format("%s: %.1f", getTitle(), value);
            }
            return String.format("%s: %.3f", getTitle(), value);
        }
    }

    public enum Extra implements IStarColor {

        GAMMA(EnumChatFormatting.GRAY, 0xFFAAAAAA, ForgeOfGodsStarColor.DEFAULT_GAMMA);

        private final String title;
        private final EnumChatFormatting color;
        private final int hexColor;
        private final float defaultValue;

        Extra(EnumChatFormatting color, int hexColor, float defaultValue) {
            this.title = "fog.cosmetics.color." + name().toLowerCase();
            this.color = color;
            this.hexColor = hexColor;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getTitle() {
            return color + translateToLocal(title);
        }

        @Override
        public EnumChatFormatting getColor() {
            return color;
        }

        @Override
        public int getHexColor() {
            return hexColor;
        }

        @Override
        public float getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String getTooltip(float value) {
            return String.format("%s: %.2f", getTitle(), value);
        }
    }
}
