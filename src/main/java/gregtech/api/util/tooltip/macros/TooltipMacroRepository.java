package gregtech.api.util.tooltip.macros;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.util.EnumChatFormatting;

import afishstash.IconRepository;
import gregtech.api.util.CustomGlyphs;

/**
 * Repository holding frequently-used macros for {@link gregtech.api.util.MultiblockTooltipBuilder}.
 * <br />
 * This utility class is meant to hold commonly used methods and helper
 * methods to generate commonly used patterns rather than
 * be a comprehensive collection of all use-cases.
 *
 * @see TooltipMacroProcessor
 * @see gregtech.api.util.MultiblockTooltipBuilder#addMacro(TooltipMacroProcessor)
 * @author <a href="https://github.com/RainVaporeon">Rain</a>
 */
public final class TooltipMacroRepository {

    private TooltipMacroRepository() {}

    /**
     * A macro that returns the input text itself.
     */
    @Transform(contract = "%s -> %s")
    public static final TooltipMacroProcessor LITERAL = TooltipMacroProcessor.of("literal", Function.identity());

    /**
     * A macro that formats "Heat"
     */
    @Transform(contract = "%s -> §c\uE100%sK")
    public static final TooltipMacroProcessor HEAT = TooltipMacroProcessor
        .of("heat", s -> EnumChatFormatting.RED + CustomGlyphs.HEAT + s + "K");

    /**
     * A macro that formats "Parallel", supports singular and plural terms
     */
    @Transform(contract = "%s -> §6\uE101%s Parallel", supportPlural = true)
    public static final TooltipMacroProcessor PARALLEL = TooltipMacroProcessor.of(
        "parallel",
        s -> EnumChatFormatting.GOLD + CustomGlyphs.PARALLEL + s + " Parallel" + ("1".equals(s) ? "" : "s"));

    /**
     * A macro that formats "Pollution"
     */
    @Transform(contract = "%s -> §5\uE103§r%s Pollution/s")
    public static final TooltipMacroProcessor POLLUTION = TooltipMacroProcessor.of(
        "pollution",
        s -> EnumChatFormatting.DARK_PURPLE + CustomGlyphs.POISON + EnumChatFormatting.RESET + s + " Pollution/s");

    /**
     * A macro that formats fluid input rate
     *
     * @apiNote This does not format input <b>material</b>
     */
    @Transform(contract = "%s -> §9\uE104%s")
    public static final TooltipMacroProcessor FLUID = TooltipMacroProcessor
        .of("fluid", s -> EnumChatFormatting.BLUE + CustomGlyphs.POTION + s);

    /**
     * A macro that returns the speed text
     *
     * @apiNote This processor does not take an argument.
     */
    @Transform(contract = "_ -> §b\uE105Speed")
    public static final TooltipMacroProcessor SPEED = TooltipMacroProcessor
        .of("speed", _ -> EnumChatFormatting.AQUA + CustomGlyphs.SPEED + "Speed");

    /**
     * A macro that formats special attributes
     */
    @Transform(contract = "%s -> §d%s")
    public static final TooltipMacroProcessor SPECIAL = TooltipMacroProcessor
        .of("special", s -> EnumChatFormatting.LIGHT_PURPLE + s);

    /**
     * A macro that formats timespan.
     */
    @Transform(contract = "%s -> §e\uE108%s")
    public static final TooltipMacroProcessor INTERVAL = TooltipMacroProcessor
        .of("interval", s -> EnumChatFormatting.YELLOW + CustomGlyphs.TIME + s);

    /**
     * A macro that formats positive attributes.
     */
    @Transform(contract = "%s -> §a%s")
    public static final TooltipMacroProcessor POSITIVE = TooltipMacroProcessor
        .of("positive", s -> EnumChatFormatting.GREEN + s);

    /**
     * A macro that formats negative attributes.
     */
    @Transform(contract = "%s -> §c%s")
    public static final TooltipMacroProcessor NEGATIVE = TooltipMacroProcessor
        .of("negative", s -> EnumChatFormatting.RED + s);

    private static final EnumChatFormatting[] RAINBOW_SEQ = { EnumChatFormatting.RED, EnumChatFormatting.GOLD,
        EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.BLUE,
        EnumChatFormatting.DARK_PURPLE };
    /**
     * A fun macro that formats the text into rainbow colors
     */
    @Transform(contract = "%s -> LSD")
    public static final TooltipMacroProcessor RAINBOW = TooltipMacroProcessor.of("rainbow", s -> {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(RAINBOW_SEQ[i % RAINBOW_SEQ.length])
                .append(s.charAt(i));
        }
        return sb.toString();
    });

    /**
     * Macro that gets the icon by key
     */
    public static final TooltipMacroProcessor ICON = TooltipMacroProcessor.of("icon", IconRepository::getComponentKey);

    /**
     * A collection of color macros for readability purposes only.
     *
     * @see EnumChatFormatting
     */
    @Transform(contract = "%s, color -> color%s")
    public static final Set<TooltipMacroProcessor> COLORS = Arrays.stream(EnumChatFormatting.values())
        .map(
            e -> TooltipMacroProcessor.of(
                e.name()
                    .toLowerCase(Locale.ROOT),
                str -> e + str))
        .collect(Collectors.toSet());

    /**
     * A collection of modifier-related macros.
     *
     * @see TooltipMacroRepository#POSITIVE
     * @see TooltipMacroRepository#NEGATIVE
     */
    public static final Set<TooltipMacroProcessor> MODIFIER = Set.of(POSITIVE, NEGATIVE);

    /**
     * A collection of attribute-related macros.
     *
     * @see TooltipMacroRepository#HEAT
     * @see TooltipMacroRepository#PARALLEL
     * @see TooltipMacroRepository#POLLUTION
     * @see TooltipMacroRepository#INTERVAL
     */
    public static final Set<TooltipMacroProcessor> ATTRIBUTES = Set.of(HEAT, PARALLEL, POLLUTION, INTERVAL);

    /**
     * Universal set of all the macro processors on this repository
     */
    public static final Set<TooltipMacroProcessor> ALL;

    static {
        // initializer: singleton instances
        Set<TooltipMacroProcessor> singletonInstances = Set.of(LITERAL, FLUID, SPEED, SPECIAL, RAINBOW, ICON);
        // initializer: collections
        List<Set<TooltipMacroProcessor>> collectionInstances = List.of(COLORS, MODIFIER, ATTRIBUTES);

        Set<TooltipMacroProcessor> universal = new HashSet<>(singletonInstances);
        collectionInstances.forEach(universal::addAll);

        ALL = universal;
    }

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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            MacroStub macroStub = (MacroStub) o;
            return Objects.equals(name, macroStub.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }

    /**
     * Helper method to create a {@link TooltipMacroProcessor} instance
     *
     * @param name the name of the processor
     * @param op   the operation
     * @return the built MacroProcessor instance
     */
    static TooltipMacroProcessor makeUnary(String name, Function<String, String> op) {
        return new MacroStub(name, op);
    }

    /**
     * Annotation providing a general overview on how a text may
     * be transformed.
     * <br />
     * Value contract loosely follows the format:
     * {@code <param> -> <transformation>},
     * <br />
     * The field {@code param} can be any value, though conventionally uses {@code %s}
     * as the input string.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @Target({ ElementType.METHOD, ElementType.FIELD })
    private @interface Transform {

        /**
         * The transformation contract.
         *
         * @return the output format
         */
        String contract();

        /**
         * Whether this transformation supports plural
         *
         * @return {@code true} if it supports it
         * @apiNote it does not transform the input text, only indicates
         *          that the output text may vary if the input is "1"; for example:
         *          for {@link TooltipMacroRepository#PARALLEL}, if the input is
         *          exactly "1", the output would end with "Parallel", otherwise
         *          it ends with "Parallels".
         */
        boolean supportPlural() default false;
    }
}
