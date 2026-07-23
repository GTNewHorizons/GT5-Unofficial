package gregtech.api.util.tooltip.macros;

import java.util.function.Function;

/**
 * Interface for processing macros of the form {@code {macro_name:param}}.
 * Implementations are registered via {@link gregtech.api.util.MultiblockTooltipBuilder#addMacro(TooltipMacroProcessor)}
 * and
 * are looked up by name when a matching macro is encountered in a formatted string.
 *
 * @see TooltipMacroRepository Repository of commonly used macros
 * @author <a href="https://github.com/RainVaporeon">Rain</a>
 */
public interface TooltipMacroProcessor {

    /**
     * @return The name used to identify this macro, i.e. the "macro_name" in {macro_name:...}
     */
    String getName();

    /**
     * Processes the macro's parameters and returns the string that should
     * replace the macro reference in the output.
     *
     * @param param The parameter passed to the macro. Empty string if none were given.
     * @return The resulting string to insert in place of the macro.
     */
    String process(String param);

    /**
     * Utility method to create a TooltipMacroProcessor
     *
     * @param name        the macro name
     * @param transformer the string transformer
     * @return the built instance
     */
    public static TooltipMacroProcessor of(String name, Function<String, String> transformer) {
        return TooltipMacroRepository.makeUnary(name, transformer);
    }
}
