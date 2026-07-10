package gregtech.api.util.tooltip;

import gregtech.api.util.CustomGlyphs;
import net.minecraft.util.EnumChatFormatting;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.Function;

/**
 * Repository holding frequently-used macros for {@link gregtech.api.util.MultiblockTooltipBuilder}.
 * <br />
 * This utility class is meant to hold commonly used methods and helper
 * methods to generate commonly used patterns rather than
 * be a comprehensive collection of all use-cases.
 * @see TooltipMacroProcessor
 * @see gregtech.api.util.MultiblockTooltipBuilder#addMacro(TooltipMacroProcessor)
 */
public final class TooltipMacroRepository {

    private TooltipMacroRepository() {}

    /**
     * A macro that returns the input text itself.
     */
    @Transform(contract = "%s -> %s")
    public static TooltipMacroProcessor LITERAL = TooltipMacroProcessor.of(
        "literal", Function.identity()
    );

    /**
     * A macro that formats "Heat"
     */
    @Transform(contract = "%s -> §c\uE100%sK")
    public static TooltipMacroProcessor HEAT = TooltipMacroProcessor.of(
        "heat", s -> EnumChatFormatting.RED + CustomGlyphs.HEAT + s + "K"
    );

    /**
     * A macro that formats "Parallel", supports singular and plural terms
     */
    @Transform(contract = "%s -> §6\uE101%s Parallel", supportPlural = true)
    public static TooltipMacroProcessor PARALLEL = TooltipMacroProcessor.of(
        "parallel", s -> EnumChatFormatting.GOLD + CustomGlyphs.PARALLEL + s + " Parallel" + ("1".equals(s) ? "" : "s")
    );

    /**
     * A macro that formats "Pollution"
     */
    @Transform(contract = "%s -> §5\uE103§r%s Pollution/s")
    public static TooltipMacroProcessor POLLUTION = TooltipMacroProcessor.of(
        "pollution", s -> EnumChatFormatting.DARK_PURPLE + CustomGlyphs.POISON
        + EnumChatFormatting.RESET + s + " Pollution/s"
    );

    /**
     * A macro that formats fluid input rate
     * @apiNote This does not format input <b>material</b>
     */
    @Transform(contract = "%s -> §9\uE104%s")
    public static TooltipMacroProcessor FLUID = TooltipMacroProcessor.of(
        "fluid", s -> EnumChatFormatting.BLUE + CustomGlyphs.POTION + s
    );

    /**
     * A macro that returns the speed text
     * @apiNote This processor does not take an argument.
     */
    @Transform(contract = "_ -> §b\uE105Speed")
    public static TooltipMacroProcessor SPEED = TooltipMacroProcessor.of(
        "speed", _ -> EnumChatFormatting.AQUA + CustomGlyphs.SPEED + "Speed"
    );

    /**
     * A macro that formats special attributes
     */
    @Transform(contract = "%s -> §d%s")
    public static TooltipMacroProcessor SPECIAL = TooltipMacroProcessor.of(
        "special", s -> EnumChatFormatting.LIGHT_PURPLE + s
    );

    /**
     * A macro that formats timespan.
     */
    @Transform(contract = "%s -> §e\uE108%s")
    public static TooltipMacroProcessor INTERVAL = TooltipMacroProcessor.of(
        "interval", s -> EnumChatFormatting.YELLOW + CustomGlyphs.TIME + s
    );

    /**
     * A macro that formats positive attributes.
     */
    @Transform(contract = "%s -> §a%s")
    public static TooltipMacroProcessor POSITIVE = TooltipMacroProcessor.of(
        "positive", s -> EnumChatFormatting.GREEN + s
    );

    /**
     * A macro that formats negative attributes.
     */
    @Transform(contract = "%s -> §c%s")
    public static TooltipMacroProcessor NEGATIVE = TooltipMacroProcessor.of(
        "negative", s -> EnumChatFormatting.RED + s
    );

    /**
     * A collection of modifier-related macros.
     *
     * @see TooltipMacroRepository#POSITIVE
     * @see TooltipMacroRepository#NEGATIVE
     */
    public static final List<TooltipMacroProcessor> MODIFIER = List.of(
        POSITIVE, NEGATIVE
    );

    /**
     * A collection of attribute-related macros.
     *
     * @see TooltipMacroRepository#HEAT
     * @see TooltipMacroRepository#PARALLEL
     * @see TooltipMacroRepository#POLLUTION
     * @see TooltipMacroRepository#INTERVAL
     */
    public static final List<TooltipMacroProcessor> ATTRIBUTES = List.of(
        HEAT, PARALLEL, POLLUTION, INTERVAL
    );

    /**
     * Universal set of all the macro processors on this repository
     */
    public static final List<TooltipMacroProcessor> ALL = List.of(
        LITERAL, HEAT, PARALLEL, POLLUTION, FLUID, SPEED, SPECIAL, INTERVAL, POSITIVE, NEGATIVE
    );

    // Internal class - convenient type for a TooltipMacroProcessor instance
    private static class MacroStub implements TooltipMacroProcessor {
        private final String name;
        private final Function<String, String> op;

        public MacroStub(String name, Function<String, String> op) {
            this.name = name;
            this.op = op;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String process(String param) {
            return op.apply(param);
        }
    }

    /**
     * Helper method to create a {@link TooltipMacroProcessor} instance
     * @param name the name of the processor
     * @param op the operation
     * @return the built MacroProcessor instance
     */
    static TooltipMacroProcessor makeUnary(String name, Function<String, String> op) {
        return new MacroStub(name, op);
    }


    /**
     * Annotation providing a general overview on how a text may
     * be transformed.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.METHOD, ElementType.FIELD})
    private @interface Transform {
        /**
         * The transformation contract.
         * @return the output format
         */
        String contract();

        /**
         * Whether this transformation supports plural
         * @return {@code true} if it supports it
         * @apiNote it does not transform the input text, only indicates
         * that the output text may vary if the input is "1"; for example:
         * for {@link TooltipMacroRepository#PARALLEL}, if the input is
         * exactly "1", the output would end with "Parallel", otherwise
         * it ends with "Parallels".
         */
        boolean supportPlural() default false;
    }
}
