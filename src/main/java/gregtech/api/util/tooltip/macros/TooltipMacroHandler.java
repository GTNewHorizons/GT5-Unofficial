package gregtech.api.util.tooltip.macros;

import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Class to handle text transformation from {@link TooltipMacroProcessor} instances.
 * <br />
 * The instance can be shared across different builders for different purposes,
 * but modification of processors should be done in a synchronized way, or by
 * providing a concurrent {@link Map} implementation, like {@link java.util.concurrent.ConcurrentHashMap}.
 *
 * @author <a href="https://github.com/RainVaporeon">Rain</a>
 */
public class TooltipMacroHandler {

    // processor map
    private final Map<String, TooltipMacroProcessor> processors;

    // post-macro invocation string
    private String postfix;

    /**
     * Creates a new {@link TooltipMacroHandler} instance, backed by the
     * default {@link HashMap} instance.
     *
     * @apiNote If an instance with a custom Map implementation is desired,
     * use {@link TooltipMacroHandler#TooltipMacroHandler(Supplier)}
     */
    public TooltipMacroHandler() {
        this.processors = new HashMap<>();
    }

    /**
     * Creates a new {@link TooltipMacroHandler} instance, backed by the supplier's
     * provided Map implementation
     * @param supplier the supplier that provides the {@link Map} implementation
     *                 for the underlying Map this handler uses.
     */
    public TooltipMacroHandler(Supplier<Map<String, TooltipMacroProcessor>> supplier) {
        this.processors = supplier.get();
    }

    /**
     * Registers this macro processor for the instance
     * @param processor the macro processor
     * @apiNote macros with duplicate names will be replaced
     */
    public void addProcessor(TooltipMacroProcessor processor) {
        this.processors.put(
            processor.getName(),
            processor
        );
    }

    /**
     * Registers multiple macro processors for the builder
     * @param macros the macro processors
     * @apiNote this method is a convenience method, identical to: <pre>
     *     for (TooltipMacroProcessor mp : macros) this.addProcessor(mp);
     * </pre>
     */
    public void addMacros(TooltipMacroProcessor... macros) {
        for (TooltipMacroProcessor mp : macros) this.addProcessor(mp);
    }

    /**
     * Registers multiple macro processors for the builder
     * @param macros the macro processor collection
     * @apiNote this method is a convenience method, identical to: <pre>
     *     for (TooltipMacroProcessor mp : macros) this.addProcessor(mp);
     * </pre>
     */
    public void addMacros(Iterable<TooltipMacroProcessor> macros) {
        for (TooltipMacroProcessor mp : macros) this.addProcessor(mp);
    }

    /**
     * Sets the content to append after each macro invocation
     * @param postfix the content, or {@code null} to disable this behavior.
     * @apiNote This method is deliberately designed to accept a String
     * instance rather than {@link EnumChatFormatting} to account for
     * the possibility of needing to chain multiple formatting styles
     * (for example, gray + italic)
     */
    public void setPostfix(Object postfix) {
        this.postfix = (postfix == null) ? null : String.valueOf(postfix);
    }

    /**
     * Gets the string appended after each macro invocation
     * @return the string, or {@code null} if none set.
     */
    public String postfix() {
        return postfix;
    }

    /**
     * Clears all stored macro processors.
     *
     * @apiNote One should consider creating a new instance
     * rather than clearing and reusing the same instance elsewhere.
     */
    public void clear() {
        this.processors.clear();
    }

    /**
     * Processes the provided text, translating '&' color/format codes (e.g. &a, &l, &r)
     * into Minecraft's '§' formatting codes, and resolving macros of the form
     * {macro_name:param} using registered {@link TooltipMacroProcessor}s.
     * <p>
     * Use '&&' to escape and insert a literal '&' character.
     *
     * @param text The line to be formatted
     * @return The formatted line
     * @apiNote Macros called without a parameter is treated as an invocation with an empty string.
     */
    public String processString(String text) {
        StringBuilder result = new StringBuilder();
        int len = text.length();

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);

            if (c == '&' && i + 1 < len) {
                char next = text.charAt(i + 1);

                if (next == '&') {
                    result.append('&');
                    i++;
                    continue;
                }

                // mc color codes
                if ("0123456789abcdefklmnor".indexOf(Character.toLowerCase(next)) >= 0) {
                    // mc color symbol
                    result.append('\u00A7').append(next);
                    i++;
                    continue;
                }

                result.append(c);
                continue;
            }

            if (c == '{') {
                int close = text.indexOf('}', i + 1);
                if (close > i) {
                    String macroContent = text.substring(i + 1, close);
                    int colonIdx = macroContent.indexOf(':');

                    String name = colonIdx >= 0 ? macroContent.substring(0, colonIdx) : macroContent;
                    String param = colonIdx >= 0 ? macroContent.substring(colonIdx + 1) : "";

                    TooltipMacroProcessor processor = processors.get(name);

                    if (processor != null) {
                        result.append(processor.process(param));

                        if (this.postfix != null) {
                            result.append(this.postfix);
                        }

                        i = close;
                        continue;
                    }
                }
                result.append(c);
                continue;
            }

            result.append(c);
        }

        return result.toString();
    }
}
