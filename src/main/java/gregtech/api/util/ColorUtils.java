package gregtech.api.util;

import com.gtnewhorizon.gtnhlib.color.ColorResource;

public class ColorUtils {

    private static final ColorResource.Factory color = new ColorResource.Factory("gregtech");

    public static final ColorResource
    // spotless:off
        networkAnalyzerNodeInput       = color.rgb("networkAnalyzerNodeInput",      "0xF18F01"),
        networkAnalyzerNodeOutput      = color.rgb("networkAnalyzerNodeOutput",     "0x66CCFF"),
        networkAnalyzerNodeFork        = color.rgb("networkAnalyzerNodeFork",       "0x00FF00"),

        networkAnalyzerSeverityNone    = color.rgb("networkAnalyzerSeverityNone",    "0x7D7D7D"),
        networkAnalyzerSeveritySuccess = color.rgb("networkAnalyzerSeveritySuccess", "0x00FF00"),
        networkAnalyzerSeverityWarning = color.rgb("networkAnalyzerSeverityWarning", "0xE9C46A"),
        networkAnalyzerSeverityAlert   = color.rgb("networkAnalyzerSeverityAlert",   "0xD62828"),

        networkAnalyzerLabelBackground = color.argb("networkAnalyzerLabelBackground", "0x80000000"),

        progressBarTop                 = color.argb("progressBarTop", "0xFF69BF3D"),
        progressBarBottom              = color.argb("progressBarBottom", "0xFF4B8230"),

        euBarTop                       = color.argb("euBarTop", "0xFFE6C920"),
        euBarBottom                    = color.argb("euBarBottom", "0xFF8A6E00");
    // spotless:on
}
