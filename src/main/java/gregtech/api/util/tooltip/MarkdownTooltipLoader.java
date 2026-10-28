package gregtech.api.util.tooltip;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.getFluidUnit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTLoggers;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Localized;
import gregtech.api.util.StringUtils;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/// A loader that reads a pseudo-markdown file from a mod's jar or resource pack and converts it to text that can be
/// rendered normally. While the format of the file is similar to markdown, many of its features have been removed or
/// completely changed. The goal is similar, but the syntax is very different. Primarily, formatting is done with
/// 'commands' instead of punctuation.
/// A command is denoted by an open brace, followed by some text, followed by a close brace. Some commands accept an
/// argument, which must be inserted after a colon: `{command_name:command_argument}`. Commands run an arbitrary java
/// lambda, so they can perform almost any action, but custom commands should follow the below standard unless they have
/// a good reason not to.
/// 1. Commands can be nested, but most commands will not evaluate nested commands by default. These commands have a
/// variant with an `-eval` suffix.
/// 2. Command arguments should be colon-separated or comma separated
/// 3. Commands should clean up after themselves, unless explicitly disabled by the user. As an example, when given an
/// argument, the MC color/format commands use [GTUtility#FORMAT_PUSH_STACK] to prevent format clobbering.
public class MarkdownTooltipLoader {

    public static final String COMMENT_START = "//";
    public static final char ESCAPE_CHAR = '\\';
    public static final char ARG_SPLITTER = ':';
    public static final String ARG_SPLITTER_PATTERN = ":";
    public static final char COMMAND_START = '{';
    public static final char COMMAND_END = '}';
    public static final String LINE_PROCESSOR_sTART = "#";

    /** Makes all line gray by default */
    public static final MarkdownTooltipLoader STANDARD = new MarkdownTooltipLoader("tooltip") {

        @Override
        public String formatLine(String parsedLine) {
            return EnumChatFormatting.GRAY + parsedLine;
        }
    };

    /** For multi-line text, applies no default formatting to parsed lines. */
    public static final MarkdownTooltipLoader TEXT = new MarkdownTooltipLoader("text");

    private final Object2ObjectOpenHashMap<String, MarkdownLineProcessor> lineProcessor = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<String, IMarkdownCommand> commands = new Object2ObjectOpenHashMap<>();

    private static final Object2ObjectOpenHashMap<String, MarkdownLineProcessor> GLOBAL_LINE_PROCESSORS = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String, IMarkdownCommand> GLOBAL_COMMANDS = new Object2ObjectOpenHashMap<>();

    public static class MarkdownTooltipParsingException extends IllegalStateException {

        public final String[] userLines;

        public static MarkdownTooltipParsingException lineProcessorStandard(String processorName, String message) {
            return new MarkdownTooltipParsingException(
                new String[] { "Line Processor Exception # " + processorName + " # " + message },
                new String[] { "Line Processor Exception", processorName + " # " + message });
        }

        public MarkdownTooltipParsingException(String... lines) {
            super(String.join("\n", lines));
            this.userLines = lines;
        }

        public MarkdownTooltipParsingException(String[] logLines, String[] userLines) {
            super(String.join("\n", logLines));
            this.userLines = userLines;
        }
    }

    public static void addGlobalCommandWithAliases(IMarkdownCommand command, String... aliases) {
        for (String alias : aliases) {
            addGlobalCommand(alias, command);
        }
    }

    public static void addGlobalCommand(String name, IMarkdownCommand command) {
        GLOBAL_COMMANDS.put(name, command);
    }

    public static void addGlobalLineProcessorWithAliases(MarkdownLineProcessor command, String... aliases) {
        for (String alias : aliases) {
            addGlobalLineProcessor(alias, command);
        }
    }

    public static void addGlobalLineProcessor(String name, MarkdownLineProcessor command) {
        GLOBAL_LINE_PROCESSORS.put(LINE_PROCESSOR_sTART + name, command);
    }

    static {
        // global line processors
        addGlobalLineProcessorWithAliases(($1, $2, $3, $4, $5) -> {}, "comment", "");
        addGlobalLineProcessorWithAliases(new IncludeMarkdownLineProcessor(), "include", "import");
        addGlobalLineProcessorWithAliases(new IfMarkdownLineProcessor(true), "if-true", "ift", "if");
        addGlobalLineProcessorWithAliases(new IfMarkdownLineProcessor(false), "if-false", "iif", "if-not", "ifn");
        addGlobalLineProcessorWithAliases(
            new IfDefMarkdownLineProcessor(true),
            "if-exists",
            "ife",
            "if-defined",
            "if-def",
            "ifdef");
        addGlobalLineProcessorWithAliases(
            new IfDefMarkdownLineProcessor(false),
            "if-not-exists",
            "ifne",
            "if-not-defined",
            "if-ndef",
            "ifndef");

        // coloring commands
        for (EnumChatFormatting format : EnumChatFormatting.values()) {
            addGlobalCommand(format.getFriendlyName(), new ColorMarkdownCommand(format));
        }
        addGlobalCommand("color-pattern", new ColorPatternMarkdownCommand());
        addGlobalCommandWithAliases(new VoltageFormatMarkdownCommand(), "tier-color", "TC", "voltage-format", "VF");

        // evaluating vars
        addGlobalCommand("item", new ItemMarkdownCommand());
        addGlobalCommand("fluid", new FluidMarkdownCommand());
        addGlobalCommand("hr", new HRMarkdownCommand());
        addGlobalCommand("var", new VarMarkdownCommand(false));
        addGlobalCommand("var-eval", new VarMarkdownCommand(true));
        addGlobalCommand("lang", new LangMarkdownCommand(false));
        addGlobalCommand("lang-eval", new LangMarkdownCommand(true));
        addGlobalCommand("isolate", new IsolationMarkdownCommand());
        addGlobalCommandWithAliases(new VoltageNameMarkdownCommand(), "tier-name", "TN", "voltage-name", "VN");
        addGlobalCommandWithAliases(
            new FormattedVoltageNameMarkdownCommand(),
            "tier-color-name",
            "TCN",
            "voltage-name-formatted",
            "VNF");
        addGlobalCommandWithAliases(new CommentMarkdownCommand(), "comment", "#", "//");
        addGlobalCommandWithAliases(new IfMarkdownCommand(true), "if-true", "if");
        addGlobalCommandWithAliases(new IfMarkdownCommand(false), "if-false", "if-not");
        addGlobalCommandWithAliases(
            new IfDefMarkdownCommand(true),
            "if-exists",
            "ife",
            "if-defined",
            "if-def",
            "ifdef");
        addGlobalCommandWithAliases(
            new IfDefMarkdownCommand(false),
            "if-not-exists",
            "ifne",
            "if-not-defined",
            "if-ndef",
            "ifndef");
        addGlobalCommandWithAliases(new PluralityMarkdownCommand(true), "singular", "if-1");
        addGlobalCommandWithAliases(new PluralityMarkdownCommand(false), "plural", "if-n-1");

        // unit standardization commands
        addGlobalCommandWithAliases(new FluidUnitCommand(), "fluid-unit", "l", "L", "mb", "mB");
        addGlobalCommandWithAliases(new SimpleUnitCommand("EU"), "energy-unit", "eu", "EU");
        addGlobalCommandWithAliases(
            new SimpleUnitCommand("EU/t"),
            "energy-usage-unit",
            "eut",
            "EUt",
            "EUT",
            "eu/t",
            "EU/t",
            "eu/T",
            "EU/T");
        addGlobalCommandWithAliases(new SimpleUnitCommand("TeV"), "tera-electron-volt-unit", "tev", "TeV", "TEV");
        addGlobalCommandWithAliases(new SimpleUnitCommand("GeV"), "giga-electron-volt-unit", "gev", "GeV", "GEV");
        addGlobalCommandWithAliases(new SimpleUnitCommand("MeV"), "mega-electron-volt-unit", "mev", "MeV", "MEV");
        addGlobalCommandWithAliases(new SimpleUnitCommand("KeV"), "kilo-electron-volt-unit", "kev", "KeV", "KEV");
        // No "ev"/"EV" aliases: EV is also a voltage tier name.
        addGlobalCommandWithAliases(new SimpleUnitCommand("eV"), "electron-volt-unit", "eV");
        addGlobalCommandWithAliases(new SimpleUnitCommand("K"), "heat-unit", "K", "k", "Kelvin", "kelvin");

        // standard coloring for certain concepts
        STANDARD.addCommand("color_tier", new ColorMarkdownCommand(TooltipHelper.TIER_COLOR));
        STANDARD.addCommand("color_name", new ColorMarkdownCommand(TooltipHelper.NAME_COLOR));
        STANDARD.addCommand("color_formula", new ColorMarkdownCommand(TooltipHelper.FORMULA_COLOR));
        STANDARD.addCommand("color_steam", new ColorMarkdownCommand(TooltipHelper.STEAM_COLOR));
        STANDARD.addCommand("color_note", new ColorMarkdownCommand(TooltipHelper.NOTE_COLOR));
        STANDARD.addCommand("color_machine_type", new ColorMarkdownCommand(TooltipHelper.MACHINE_TYPE_COLOR));
        STANDARD.addCommand("color_misc", new ColorMarkdownCommand(TooltipHelper.MISC_COLOR));
        STANDARD.addCommand("color_cold", new ColorMarkdownCommand(TooltipHelper.COLD_COLOR));
        STANDARD.addCommand("color_speed", new ColorMarkdownCommand(TooltipHelper.SPEED_COLOR));
        STANDARD.addCommand("color_range", new ColorMarkdownCommand(TooltipHelper.RANGE_COLOR));
        STANDARD.addCommand("color_amount", new ColorMarkdownCommand(TooltipHelper.AMOUNT_COLOR));
        STANDARD.addCommand("color_hatch", new ColorMarkdownCommand(TooltipHelper.HATCH_COLOR));
        STANDARD.addCommand("color_loss", new ColorMarkdownCommand(TooltipHelper.LOSS_COLOR));
        STANDARD.addCommand("color_parallel", new ColorMarkdownCommand(TooltipHelper.PARALLEL_COLOR));
        STANDARD.addCommand("color_mode", new ColorMarkdownCommand(TooltipHelper.MODE_COLOR));
        STANDARD.addCommand("color_key", new ColorMarkdownCommand(TooltipHelper.KEY_COLOR));
        STANDARD.addCommand("color_efficiency", new ColorMarkdownCommand(TooltipHelper.EFF_COLOR));
        STANDARD.addCommand("color_eu_usage", new ColorMarkdownCommand(TooltipHelper.EU_USAGE_COLOR));
        STANDARD.addCommand("color_heat", new ColorMarkdownCommand(TooltipHelper.HEAT_COLOR));
        STANDARD.addCommand("color_hot", new ColorMarkdownCommand(TooltipHelper.HOT_COLOR));
        STANDARD.addCommand("color_electron_volt", new ColorMarkdownCommand(TooltipHelper.ELECTRON_VOLT_COLOR));
        STANDARD.addCommand("color_deprecated", new ColorMarkdownCommand(TooltipHelper.DEPRECATED_COLOR));
        STANDARD.addCommand("color_danger", new ColorMarkdownCommand(TooltipHelper.DANGER_COLOR));
        STANDARD.addCommand("color_mechanic", new ColorMarkdownCommand(TooltipHelper.MECHANIC_COLOR));
        STANDARD.addCommand("color_time", new ColorMarkdownCommand(TooltipHelper.TIME_COLOR));
        STANDARD.addCommand("color_pollution", new ColorMarkdownCommand(TooltipHelper.POLLUTION_COLOR));
        STANDARD.addCommand("color_amperage", new ColorMarkdownCommand(TooltipHelper.AMP_COLOR));
        STANDARD.addCommand("color_voltage", new ColorMarkdownCommand(TooltipHelper.EU_VOLT_COLOR));
        STANDARD.addCommand("color_eu_capacity", new ColorMarkdownCommand(TooltipHelper.EU_CAPACITY_COLOR));
    }

    private final String folder;

    public MarkdownTooltipLoader(String folder) {
        this.folder = "/" + folder + "/";
    }

    public void addCommandWithAliases(IMarkdownCommand command, String... aliases) {
        for (String alias : aliases) {
            addCommand(alias, command);
        }
    }

    public void addCommand(String name, IMarkdownCommand command) {
        commands.put(name, command);
    }

    public void addLineProcessorWithAliases(MarkdownLineProcessor processor, String... aliases) {
        for (String alias : aliases) {
            addLineProcessor(alias, processor);
        }
    }

    public void addLineProcessor(String name, MarkdownLineProcessor processor) {
        lineProcessor.put(name, processor);
    }

    private @Nullable MarkdownTooltipLoader.MarkdownLineProcessor getLineProcessor(String name) {
        MarkdownLineProcessor ret = GLOBAL_LINE_PROCESSORS.getOrDefault(name, null);
        return ret == null ? lineProcessor.getOrDefault(name, null) : ret;
    }

    private @Nullable IMarkdownCommand getCommand(String name) {
        IMarkdownCommand ret = GLOBAL_COMMANDS.getOrDefault(name, null);
        return ret == null ? commands.getOrDefault(name, null) : ret;
    }

    /// Loads a Markdown document at `assets/[mod id]/lang/[language id]/tooltip/[path].md`. On the client, resource
    /// packs are scanned. On the server, the file is loaded directly from the current jar.
    public List<String> loadStandardPath(ResourceLocation markdown, Map<String, Object> vars) {
        return this.loadStandardPath(markdown, vars, null);
    }

    public List<String> loadStandardPath(ResourceLocation markdown, Map<String, Object> vars,
        @Nullable Stack<ResourceLocation> includeHistory) {
        try {
            return loadStandardPath(markdown, getCurrentLanguage(), vars, includeHistory);
        } catch (IOException t1) {
            if (!(t1.getCause() instanceof FileNotFoundException)) {
                GTLoggers.GT_FML_LOGGER.error("Error loading {}", markdown, t1);
            }

            try {
                return loadStandardPath(markdown, "en_US", vars, includeHistory);
            } catch (IOException e) {
                throw new RuntimeException(
                    "Could not load the " + getCurrentLanguage() + " or en_US version of " + markdown,
                    e);
            }
        }
    }

    public List<String> loadStandardPath(ResourceLocation markdown, String language, Map<String, Object> vars)
        throws IOException {
        return loadStandardPath(markdown, language, vars, null);
    }

    public List<String> loadStandardPath(ResourceLocation markdown, String language, Map<String, Object> vars,
        @Nullable Stack<ResourceLocation> includeHistory) throws IOException {
        return loadExactPath(
            new ResourceLocation(
                markdown.getResourceDomain(),
                "lang/" + language + folder + markdown.getResourcePath() + ".md"),
            vars,
            includeHistory);
    }

    public List<String> loadExactPath(ResourceLocation markdown, Map<String, Object> vars) throws IOException {
        return loadExactPath(markdown, vars, null);
    }

    public List<String> loadExactPath(ResourceLocation markdown, Map<String, Object> vars,
        @Nullable Stack<ResourceLocation> includeHistory) throws IOException {

        // prevent infinite loops
        if (includeHistory == null) {
            includeHistory = new Stack<>();
        } else {
            for (ResourceLocation rs : includeHistory) {
                if (rs.equals(markdown)) {
                    String error = GTUtility.wrapFormatting(EnumChatFormatting.RED, "[INCLUDE LOOP: " + markdown + "]");
                    return Collections.singletonList(GTUtility.processFormatStacks(formatLine(error)));
                }
            }
        }
        includeHistory.push(markdown);

        try {
            List<String> lines = loadGeneric(markdown);
            List<String> out = new ArrayList<>();
            evaluateLines(out, lines, vars, includeHistory);
            out.replaceAll(parsedLine -> GTUtility.processFormatStacks(formatLine(parsedLine)));
            return out;
        } catch (IOException e) {
            throw new IOException("Could not load or decode " + markdown + ": " + e.getMessage(), e);
        } catch (Throwable t) {
            throw new RuntimeException("Could not parse " + markdown + ": " + t.getMessage(), t);
        } finally {
            includeHistory.pop();
        }
    }

    public void evaluateLines(List<String> out, List<String> lines, Map<String, Object> vars,
        Stack<ResourceLocation> includeHistory) {
        for (String line : lines) {
            // ignore comments
            if (line.startsWith(COMMENT_START)) continue;
            try {
                if (line.startsWith(LINE_PROCESSOR_sTART)) {
                    String[] parts = line.split(ARG_SPLITTER_PATTERN, 2);
                    if (parts.length < 2) {
                        throw MarkdownTooltipParsingException
                            .lineProcessorStandard(parts.length >= 1 ? parts[0].trim() : "", "NOT ENOUGH ARGS");
                    }
                    MarkdownLineProcessor processor = getLineProcessor(parts[0]);
                    if (processor == null) {
                        throw MarkdownTooltipParsingException
                            .lineProcessorStandard("UNKNOWN LINE PROCESSOR", parts[0].trim());
                    }
                    processor.processLine(this, out, parts[1], vars, includeHistory);
                    continue;
                }
                out.add(eval(line, vars));
            } catch (MarkdownTooltipParsingException ise) {
                for (String errLine : ise.userLines) {
                    out.add(GTUtility.wrapFormatting(EnumChatFormatting.RED, errLine));
                }
                // spams a lot, but good for debugging
                // GTLoggers.GT_FML_LOGGER.error("Error parsing markdown line {}", ise.getMessage(), ise);
            }
        }
    }

    public String formatLine(String parsedLine) {
        return parsedLine;
    }

    public String eval(String unparsed, Map<String, Object> vars) {
        StringBuilder sb = new StringBuilder(unparsed.length());

        emit(unparsed, sb, vars);

        return sb.toString();
    }

    public void emit(String unparsed, StringBuilder out, Map<String, Object> vars) {
        parseCommands(unparsed, new CommandVisitor() {

            @Override
            public void visitChar(char c) {
                out.append(c);
            }

            @Override
            public void visitCommand(String command) {
                command = command.substring(1, command.length() - 1);

                int colon = command.indexOf(ARG_SPLITTER);
                String commandName = colon == -1 ? command : command.substring(0, colon);
                String arg = colon == -1 ? "" : command.substring(colon + 1);

                IMarkdownCommand commandHandler = MarkdownTooltipLoader.this.getCommand(commandName.trim());
                if (commandHandler == null) {
                    appendCommandError(out, "UNKNOWN COMMAND", commandName.trim());
                } else {
                    commandHandler.process(MarkdownTooltipLoader.this, arg, out, vars);
                }
            }
        });
    }

    public interface CommandVisitor {

        void visitChar(char c);

        void visitCommand(String command);
    }

    public static void parseCommands(String unparsed, CommandVisitor visitor) {
        final char[] chars = unparsed.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

            if (curr == ESCAPE_CHAR) {
                if (i < chars.length - 1) {
                    visitor.visitChar(chars[i + 1]);
                    i++;
                }
            } else if (curr == COMMAND_START) {
                int end = i;
                int open = 0;

                while (end < chars.length) {
                    if (chars[end] == COMMAND_START) {
                        open++;
                    }

                    if (chars[end] == COMMAND_END) {
                        open--;
                    }

                    if (open <= 0) {
                        break;
                    }

                    end++;
                }

                if (open > 0) {
                    char[] spaces = new char[i];
                    Arrays.fill(spaces, ' ');

                    throw new MarkdownTooltipParsingException(
                        new String[] { "Unclosed open brace in line:", unparsed, new String(spaces) + "^" },
                        new String[] { "Unclosed open brace in line:", unparsed, unparsed.substring(0, i) + "^" });
                }

                String command = unparsed.substring(i, end + 1);

                if (command.length() <= 2) {
                    throw new MarkdownTooltipParsingException(
                        "Invalid command: '" + command
                            + "': expected open + close brace that contains the command name and (optional) arguments");
                }

                visitor.visitCommand(command);

                i = end;
            } else {
                visitor.visitChar(chars[i]);
            }
        }
    }

    public static String getCurrentLanguage() {
        if (GTUtility.isClient()) {
            return Minecraft.getMinecraft()
                .getLanguageManager()
                .getCurrentLanguage()
                .getLanguageCode();
        } else {
            return "en_US";
        }
    }

    public static List<String> loadGeneric(ResourceLocation loc) throws IOException {
        // Returns server only for dedicated servers
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            return loadResource(loc);
        } else {
            return loadFile(loc);
        }
    }

    @SideOnly(Side.CLIENT)
    public static List<String> loadResource(ResourceLocation loc) throws IOException {
        IResourceManager manager = Minecraft.getMinecraft()
            .getResourceManager();

        IResource resource = manager.getResource(loc);

        return IOUtils.readLines(resource.getInputStream());
    }

    public static List<String> loadFile(ResourceLocation loc) throws IOException {
        String path = String.format("assets/%s/%s", loc.getResourceDomain(), loc.getResourcePath());

        try (InputStream is = MarkdownTooltipLoader.class.getClassLoader()
            .getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Could not find jar asset " + path);
            }

            return IOUtils.readLines(is);
        }
    }

    @FunctionalInterface
    public interface MarkdownLineProcessor {

        void processLine(MarkdownTooltipLoader loader, List<String> out, String line, Map<String, Object> vars,
            Stack<ResourceLocation> includeHistory);
    }

    /**
     * <p>
     * Reads the content of another markdown file and appends it.
     * </p>
     * <p>
     * Usage Example: #include:modid:template
     * </p>
     */
    public static class IncludeMarkdownLineProcessor implements MarkdownLineProcessor {

        @Override
        public void processLine(MarkdownTooltipLoader loader, List<String> lineOutput, String line,
            Map<String, Object> vars, Stack<ResourceLocation> includeHistory) {
            ResourceLocation loc = new ResourceLocation(line.trim());
            try {
                lineOutput.addAll(loader.loadStandardPath(loc, vars, includeHistory));
            } catch (RuntimeException re) {
                if (re.getCause() instanceof IOException ioe && ioe.getCause() instanceof FileNotFoundException) {
                    throw MarkdownTooltipParsingException.lineProcessorStandard("INCLUDE", "FILE NOT FOUND: " + loc);
                }
                throw re;
            }
        }
    }

    /**
     * <p>
     * Only renders the line if the given variable is true (or false)
     * </p>
     * <p>
     * Usage Example: <br>
     * - <code>#if:boolVar:This line only appears if boolVar is true</code><br>
     * - <code>#if-not:boolVar:This line only appears if boolVar is false</code>
     * </p>
     */
    public static class IfMarkdownLineProcessor implements MarkdownLineProcessor {

        private final boolean wantsTrue;

        public IfMarkdownLineProcessor(boolean wantsTrue) {
            this.wantsTrue = wantsTrue;
        }

        @Override
        public void processLine(MarkdownTooltipLoader loader, List<String> out, String line, Map<String, Object> vars,
            Stack<ResourceLocation> includeHistory) {
            String[] parts = line.split(ARG_SPLITTER_PATTERN, 2);

            // must have 2 parts
            if (parts.length != 2) {
                throw MarkdownTooltipParsingException.lineProcessorStandard("IF", "MUST HAVE 2 ARGS");
            }

            // check if the arg is the correct type
            if (!(vars.get(parts[0]) instanceof Boolean value)) {
                throw MarkdownTooltipParsingException.lineProcessorStandard("IF", "UNKNOWN VAR" + parts[0]);
            }

            // check if it's a valid value
            if (value == wantsTrue)
                loader.evaluateLines(out, Collections.singletonList(parts[1]), vars, includeHistory);
        }
    }

    /**
     * <p>
     * Only renders the line if the given variable is defined (or doesn't)
     * </p>
     * <p>
     * Usage Example:<br>
     * - <code>#ifdef:var:This line only appears if var has been defined</code></br>
     * - <code>#ifndef:var:This line only appears if var has not been defined</code>
     * </p>
     */
    public static class IfDefMarkdownLineProcessor implements MarkdownLineProcessor {

        private final boolean wantsExists;

        public IfDefMarkdownLineProcessor(boolean wantsExists) {
            this.wantsExists = wantsExists;
        }

        @Override
        public void processLine(MarkdownTooltipLoader loader, List<String> out, String line, Map<String, Object> vars,
            Stack<ResourceLocation> includeHistory) {
            String[] parts = line.split(ARG_SPLITTER_PATTERN, 2);

            // must have 2 parts
            if (parts.length != 2) {
                throw MarkdownTooltipParsingException.lineProcessorStandard("IF-EXIST", "MUST HAVE 2 ARGS");
            }

            // check if the arg is the correct type
            if (vars.containsKey(parts[0]) == wantsExists) {
                loader.evaluateLines(out, Collections.singletonList(parts[1]), vars, includeHistory);
            }

        }
    }

    @FunctionalInterface
    public interface IMarkdownCommand {

        void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput, Map<String, Object> vars);

    }

    public static abstract class MarkdownCommand implements IMarkdownCommand {

        private final String name;

        public MarkdownCommand(String name) {
            this.name = name;
        }

        protected void appendArgCountError(StringBuilder lineOutput, int needs, int has) {
            appendError(lineOutput, String.format("NEEDS %d ARGS HAS %d", needs, has));
        }

        protected void appendUnknownVarError(StringBuilder lineOutput, String varName) {
            appendError(lineOutput, "UNKNOWN VAR: " + varName);
        }

        protected void appendNotAVoltageIndexError(StringBuilder lineOutput, String argName, int voltageIndex) {
            String formatted = String.format("THE VALUE OF %s (%d) IS NOT A VOLTAGE TIER", argName, voltageIndex);
            appendError(lineOutput, formatted);
        }

        protected void appendError(StringBuilder lineOutput, String message) {
            appendCommandError(lineOutput, name, message);
        }
    }

    public static void appendCommandError(StringBuilder lineOutput, String commandName, String message) {
        lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
            .append(EnumChatFormatting.RED)
            .append("[")
            .append(commandName)
            .append(" # ")
            .append(message)
            .append("]")
            .append(GTUtility.FORMAT_POP_STACK);
    }

    /**
     * <p>
     * Appends the localized name of the given item id.
     * </p>
     * <p>
     * Usage Example: <code>{item:gregtech:gt.blockmachines:15757}</code>
     * </p>
     */
    public static class ItemMarkdownCommand extends MarkdownCommand {

        protected ItemMarkdownCommand() {
            super("ITEM");
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] chunks = arg.split(ARG_SPLITTER_PATTERN);

            switch (chunks.length) {
                case 2 -> {
                    Item item = GameRegistry.findItem(chunks[0].trim(), chunks[1].trim());

                    if (item == null) {
                        appendError(lineOutput, "INVALID ITEM: " + chunks[0] + ":" + chunks[1]);
                    } else {
                        lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                            .append(new ItemStack(item, 1).getDisplayName())
                            .append(GTUtility.FORMAT_POP_STACK);
                    }
                }
                case 3 -> {
                    Item item = GameRegistry.findItem(chunks[0].trim(), chunks[1].trim());

                    if (item == null) {
                        appendError(lineOutput, "INVALID ITEM: " + chunks[0] + ":" + chunks[1]);
                        return;
                    }

                    int meta;

                    try {
                        meta = Integer.parseInt(chunks[2].trim());
                    } catch (NumberFormatException e) {
                        appendError(lineOutput, "INVALID META: " + chunks[2]);
                        return;
                    }

                    lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                        .append(new ItemStack(item, 1, meta).getDisplayName())
                        .append(GTUtility.FORMAT_POP_STACK);
                }
            }
        }
    }

    /**
     * <p>
     * Appends the name of the given fluid.
     * </p>
     * <p>
     * Usage Example: <code>{fluid:temporalfluid}</code>
     * </p>
     */
    public static class FluidMarkdownCommand extends MarkdownCommand {

        protected FluidMarkdownCommand() {
            super("FLUID");
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            Fluid fluid = FluidRegistry.getFluid(arg);

            if (fluid == null) {
                appendError(lineOutput, "INVALID FLUID: " + arg);
            } else {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(new FluidStack(fluid, 1).getLocalizedName())
                    .append(GTUtility.FORMAT_POP_STACK);
            }
        }
    }

    /**
     * <p>
     * Highlights a segment of text with a given color or appends the color code if no content is provided.
     * </p>
     * <p>
     * Usage Example:<br>
     * - <code>{red:this text is red} this text is the default color</code><br>
     * - <code>{red:this text is red} this text is the default color</code>
     * </p>
     */
    public static class ColorMarkdownCommand implements IMarkdownCommand {

        public final EnumChatFormatting code;

        public ColorMarkdownCommand(EnumChatFormatting code) {
            this.code = code;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            if (arg.isEmpty()) {
                lineOutput.append(code);
            } else {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(code);
                loader.emit(arg, lineOutput, vars);
                lineOutput.append(GTUtility.FORMAT_POP_STACK);
            }
        }
    }

    /**
     * <p>
     * Appends a horizontal rule with the given length or 41 if none is provided.
     * </p>
     * <p>
     * Usage Example:<br>
     * - <code>{hr}</code><br>
     * - <code>{hr:30}</code>
     * </p>
     */
    public static class HRMarkdownCommand implements IMarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            int length = 41;

            if (!arg.isEmpty()) {
                length = Integer.parseInt(arg);
            }

            switch (GTMod.proxy.getSeparatorStyle()) {
                case Empty -> lineOutput.append(" ");
                case Dashes -> lineOutput.append(StringUtils.getRepetitionOf('-', length));
                case ContinuousLine -> lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.STRIKETHROUGH)
                    .append(StringUtils.getRepetitionOf('-', length))
                    .append(GTUtility.FORMAT_POP_STACK);
            }
        }
    }

    /**
     * <p>
     * Parses a given variable and optionally parses it's contents for more markdown commands.
     * </p>
     * <p>
     * Usage Example:<br>
     * - <code>{var:varName}</code><br>
     * - <code>{var-eval:varName}</code>
     * </p>
     */
    public static class VarMarkdownCommand extends MarkdownCommand {

        public final boolean eval;

        public VarMarkdownCommand(boolean eval) {
            super("VAR");
            this.eval = eval;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            Object value = vars.get(arg);

            if (value == null) {
                appendUnknownVarError(lineOutput, arg);
                return;
            }

            if (eval) {
                loader.emit(value.toString(), lineOutput, vars);
            } else {
                if (value instanceof Number v) {
                    lineOutput.append(formatNumber(v));
                } else {
                    lineOutput.append(value);
                }
            }
        }
    }

    /**
     * <p>
     * Appends the localised text associated with a given lang key and optionally processes the args and result for more
     * markdown commands.
     * </p>
     * <p>
     * Usage Example:<br>
     * - <code>{lang:key}</code><br>
     * - <code>{lang:key:arg1:arg2:...}</code><br>
     * - <code>{lang-eval:GT5U.MBTT.Speed.Base:{var:speed}}</code>
     * </p>
     */
    public static class LangMarkdownCommand implements IMarkdownCommand {

        public final boolean eval;

        public LangMarkdownCommand(boolean eval) {
            this.eval = eval;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            Localized loc;

            if (arg.contains(ARG_SPLITTER_PATTERN)) {
                List<Object> paramList = new ArrayList<>();
                StringBuilder paramBuffer = new StringBuilder();

                // We use the same parse for eval and no-eval to keep the syntax the same.
                // If the command 'quoting' isn't desirable, the end-user can escape braces to split on colons within
                // command-like structures.
                parseCommands(arg, new CommandVisitor() {

                    @Override
                    public void visitChar(char c) {
                        if (c == ARG_SPLITTER) {
                            paramList.add(paramBuffer.toString());
                            paramBuffer.setLength(0);
                        } else {
                            paramBuffer.append(c);
                        }
                    }

                    @Override
                    public void visitCommand(String command) {
                        if (eval) {
                            paramBuffer.append(loader.eval(command, vars));
                        } else {
                            paramBuffer.append(command);
                        }
                    }
                });

                if (!paramBuffer.isEmpty()) {
                    paramList.add(paramBuffer.toString());
                }

                String key = paramList.removeFirst()
                    .toString();

                loc = new Localized(key, paramList.toArray(new Object[0]));
            } else {
                loc = new Localized(arg);
            }

            if (eval) {
                loader.emit(loc.toString(), lineOutput, vars);
            } else {
                lineOutput.append(loc);
            }
        }
    }

    /**
     * <p>
     * Isolates any formatting changes to the contents of the tag.
     * </p>
     * <p>
     * Example Usage:
     * <code>normal {yellow:yellow {italic}yellow italic {isolate:yellow italic {green}green, {italic}green italic, {blue}blue} yellow italic} normal</code>>
     * </p>
     * <p>
     * NOTE: Color codes disables previous style codes, this is a feature of MC not a bug with the code.
     * </p>
     */
    public static class IsolationMarkdownCommand implements IMarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            if (arg.isEmpty()) return;
            lineOutput.append(GTUtility.FORMAT_PUSH_STACK);
            loader.emit(arg, lineOutput, vars);
            lineOutput.append(GTUtility.FORMAT_POP_STACK);
        }
    }

    /**
     * <p>
     * Appends the formatting code(s) for a given voltage tier.
     * </p>
     * <p>
     * Usage Examples:<br>
     * - <code>{TC:tierVar:text to format}</code><br>
     * - <code>{VF:tierVar:vf}</code>
     * </p>
     */
    public static class VoltageFormatMarkdownCommand extends MarkdownCommand {

        public VoltageFormatMarkdownCommand() {
            super("TIER-COLOR");
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            // format should be {command-name:tierVar:text to convert}
            String[] parts = arg.split(ARG_SPLITTER_PATTERN, 2);

            // must have 1 or 2 parts
            if (parts.length != 1 && parts.length != 2) {
                appendError(lineOutput, "NEEDS 1 OR 2 ARGS HAS " + parts.length);
                return;
            }

            // check if the arg is the correct type
            if (!(vars.get(parts[0]) instanceof Number num)) {
                appendUnknownVarError(lineOutput, arg);
                return;
            }
            int voltageIndex = num.intValue();

            // check if it's a valid value
            if (voltageIndex < 0 || voltageIndex >= GTValues.TIER_COLORS.length) {
                appendNotAVoltageIndexError(lineOutput, arg, voltageIndex);
                return;
            }
            String formatting = GTValues.TIER_COLORS[voltageIndex];

            // if there is a 2nd part, isolate the formatting to that part only
            if (parts.length == 2) {
                if (parts[1].isEmpty()) return;
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(formatting);
                loader.emit(parts[1], lineOutput, vars);
                lineOutput.append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // else just append the code
            lineOutput.append(formatting);
        }
    }

    /**
     * <p>
     * Appends the voltage name for the given voltage tier.
     * </p>
     * <p>
     * Usage Example: <code>{VN:tierVar}</code>
     * </p>
     */
    public static class VoltageNameMarkdownCommand extends MarkdownCommand {

        public VoltageNameMarkdownCommand() {
            super("TIER-NAME");
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            // check if the arg is the correct type
            if (!(vars.get(arg) instanceof Number num)) {
                appendUnknownVarError(lineOutput, arg);
                return;
            }
            int voltageIndex = num.intValue();

            // check if it's a valid value
            if (voltageIndex < 0 || voltageIndex >= GTValues.VN.length) {
                appendNotAVoltageIndexError(lineOutput, arg, voltageIndex);
                return;
            }

            // else just append the code
            lineOutput.append(GTValues.VN[voltageIndex]);
        }
    }

    /**
     * <p>
     * Appends the formatted voltage name for the given voltage tier.
     * </p>
     * <p>
     * Usage Example: <code>{VNF:tierVar}</code>
     * </p>
     */
    public static class FormattedVoltageNameMarkdownCommand extends MarkdownCommand {

        public FormattedVoltageNameMarkdownCommand() {
            super("TIER-NAME-COLORED");
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            // check if the arg is the correct type
            if (!(vars.get(arg) instanceof Number num)) {
                appendUnknownVarError(lineOutput, arg);
                return;
            }
            int voltageIndex = num.intValue();

            // check if it's a valid value
            if (voltageIndex < 0 || voltageIndex >= GTValues.VN.length) {
                appendNotAVoltageIndexError(lineOutput, arg, voltageIndex);
                return;
            }

            // else just append the code
            lineOutput.append(GTUtility.wrapFormatting(GTValues.TIER_COLORS[voltageIndex], GTValues.VN[voltageIndex]));
        }
    }

    /**
     * <p>
     * Ignores the contents of the command.
     * </p>
     * <p>
     * Example usage: <code>{#:this won't be shown to the player}</code>
     * </p>
     */
    public static class CommentMarkdownCommand implements IMarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {}
    }

    /**
     * <p>
     * Repeats a color sequence over the given text, cannot contain evaluated text that contains color codes.
     * </p>
     * <p>
     * Usage Examples:<br>
     * - <code>{color-pattern:green,dark_green:RADIATION}</code><br>
     * - <code>{color-pattern:dark_red,red,gold,yellow,green,aqua,light_purple,dark_purple:rainbow text is easy}</code>
     * </p>
     */
    public static class ColorPatternMarkdownCommand extends MarkdownCommand {

        public ColorPatternMarkdownCommand() {
            super("COLOR-PATTERN");
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            // format should be {command-name:tierVar:text to convert}
            String[] parts = arg.split(ARG_SPLITTER_PATTERN, 2);

            // must have 2 parts
            if (parts.length != 2) {
                appendArgCountError(lineOutput, 2, parts.length);
                return;
            }

            // ignore if text is empty
            if (parts[1].isEmpty()) return;

            // colors are is required
            if (parts[0].isEmpty()) {
                appendError(lineOutput, "PATTERN IS EMPTY");
                return;
            }

            // translate the codes
            StringBuilder sb = new StringBuilder();
            String[] unparsed = parts[0].split(",");
            List<IntObjectPair<String>> parsed = new ArrayList<>(unparsed.length);
            for (String s : unparsed) {
                String[] repetition = s.split("\\*", 2);
                int count;
                String params;
                if (repetition.length == 2) {
                    count = Integer.parseInt(repetition[0]);
                    params = repetition[1];
                } else {
                    count = 1;
                    params = repetition[0];
                }

                for (String format : params.split("\\|")) {
                    sb.append(
                        EnumChatFormatting.getValueByName(format)
                            .toString());
                }
                parsed.add(IntObjectPair.of(count, sb.toString()));
                sb.delete(0, sb.length());
            }

            // evaluate the contents
            loader.emit(parts[1], sb, vars);
            char[] arr = sb.toString()
                .toCharArray();
            // render out the patterned text
            IntObjectPair<String> active = parsed.getFirst();
            int remaining = active.keyInt();
            int pos = 0;
            for (int i = 0; i < arr.length; i++) {
                // skip and include passed tags
                if (arr[i] == '§') {
                    lineOutput.append(arr[i++])
                        .append(arr[i]);
                    continue;
                }
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(active.value())
                    .append(arr[i])
                    .append(GTUtility.FORMAT_POP_STACK);
                if (--remaining <= 0) {
                    active = parsed.get(++pos % parsed.size());
                    remaining = active.keyInt();
                }
            }
        }
    }

    /**
     * <p>
     * Only appends the given text if the variable parameter has the right boolean value.
     * </p>
     * <p>
     * Usage Examples:<br>
     * - <code>{if-true:boolVar:Shown if boolVar is true}</code><br>
     * - <code>{if-false:boolVar:Shown if boolVar is false}</code>
     * </p>
     */
    public static class IfMarkdownCommand extends MarkdownCommand {

        final boolean wantsTrue;

        public IfMarkdownCommand(boolean mustBeTrue) {
            super(mustBeTrue ? "IF-TRUE" : "IF-FALSE");
            this.wantsTrue = mustBeTrue;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] parts = arg.split(ARG_SPLITTER_PATTERN, 2);

            // must have 2 parts
            if (parts.length != 2) {
                appendArgCountError(lineOutput, 2, parts.length);
                return;
            }

            // check if the arg is the correct type
            if (!(vars.get(parts[0]) instanceof Boolean value)) {
                appendUnknownVarError(lineOutput, arg);
                return;
            }

            // check if it's a valid value
            if (value == wantsTrue) loader.emit(parts[1], lineOutput, vars);
        }
    }

    /**
     * <p>
     * Only appends the given text if a given variable exists.
     * </p>
     * <p>
     * Usage Examples:<br>
     * - <code>{if-def:minGlasstier:Needs {TC:minGlassTier:{var:minGlassTier} Glass}}</code><br>
     * </p>
     */
    public static class IfDefMarkdownCommand extends MarkdownCommand {

        final boolean wantsExist;

        public IfDefMarkdownCommand(boolean wantsExist) {
            super(wantsExist ? "IFDEF" : "IF-NOT-DEF");
            this.wantsExist = wantsExist;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] parts = arg.split(ARG_SPLITTER_PATTERN, 2);

            // must have 2 parts
            if (parts.length != 2) {
                appendArgCountError(lineOutput, 2, parts.length);
                return;
            }

            // check if the arg is the correct type
            if (vars.containsKey(parts[0]) == wantsExist) {
                loader.emit(parts[1], lineOutput, vars);
            }
        }
    }

    /**
     * <p>
     * Allows the insertion of plurality suffixes depending on a numeric variable.
     * </p>
     * <p>
     * Usage Examples:<br>
     * - <code>There {plural:count:are}{singular:count:is} {var:count} bird{plural:count:s}</code><br>
     * </p>
     */
    public static class PluralityMarkdownCommand extends MarkdownCommand {

        final boolean wantsSingular;

        public PluralityMarkdownCommand(boolean wantsSingular) {
            super(wantsSingular ? "SINGULAR" : "PLURAL");
            this.wantsSingular = wantsSingular;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] parts = arg.split(ARG_SPLITTER_PATTERN, 2);

            // must have 2 parts
            if (parts.length != 2) {
                appendArgCountError(lineOutput, 2, parts.length);
                return;
            }

            // check if the arg is the correct type
            Object unparsed = vars.get(parts[0]);
            boolean result;
            switch (unparsed) {
                case String v -> result = wantsSingular ^ (!v.equals("1") && !v.equals("-1"));
                case Number v -> result = wantsSingular ^ (v.longValue() != 1L && v.longValue() != -1L);
                default -> {
                    appendUnknownVarError(lineOutput, arg);
                    return;
                }
            }

            // check if it's a valid value
            if (result) loader.emit(parts[1], lineOutput, vars);
        }
    }

    /**
     * <p>
     * Appends the user's preferred fluid unit.
     * </p>
     * <p>
     * Usage Example: <code>{var:consumed} {L}/s</code> -> <code>### mB/s</code>
     * </p>
     */
    public static class FluidUnitCommand implements IMarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            lineOutput.append(getFluidUnit());
        }
    }

    /**
     * <p>
     * Enforces formatting or naming conventions for a static unit.
     * </p>
     * <p>
     * This helps writers write what ever format they like and all the values get rendered the same to the player so
     * regardless of writer preference, the outcome is the same to the player.
     * </p>
     * <p>
     * Usage Example: <code>{var:consumed} {EUt}</code> -> <code>### EU/t</code>
     * </p>
     */
    public static class SimpleUnitCommand implements IMarkdownCommand {

        final String unit;

        public SimpleUnitCommand(String unit) {
            this.unit = unit;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            lineOutput.append(unit);
        }
    }

}
