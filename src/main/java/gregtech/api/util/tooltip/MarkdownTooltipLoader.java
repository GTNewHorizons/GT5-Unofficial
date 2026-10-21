package gregtech.api.util.tooltip;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.getFluidUnit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final String COMMENT_START = "; ";
    public static final MarkdownTooltipLoader STANDARD = new MarkdownTooltipLoader();

    private final HashMap<String, MarkdownCommand> commands = new HashMap<>();

    public MarkdownTooltipLoader() {
        addCommand("item", new ItemMarkdownCommand());
        addCommand("fluid", new FluidMarkdownCommand());

        for (EnumChatFormatting format : EnumChatFormatting.values()) {
            addCommand(format.getFriendlyName(), new ColorMarkdownCommand(format));
        }

        // standard coloring for certain concepts
        addCommand("color_tier", new ColorMarkdownCommand(TooltipHelper.TIER_COLOR));
        addCommand("color_name", new ColorMarkdownCommand(TooltipHelper.NAME_COLOR));
        addCommand("color_formula", new ColorMarkdownCommand(TooltipHelper.FORMULA_COLOR));
        addCommand("color_steam", new ColorMarkdownCommand(TooltipHelper.STEAM_COLOR));
        addCommand("color_note", new ColorMarkdownCommand(TooltipHelper.NOTE_COLOR));
        addCommand("color_machine_type", new ColorMarkdownCommand(TooltipHelper.MACHINE_TYPE_COLOR));
        addCommand("color_misc", new ColorMarkdownCommand(TooltipHelper.MISC_COLOR));
        addCommand("color_cold", new ColorMarkdownCommand(TooltipHelper.COLD_COLOR));
        addCommand("color_speed", new ColorMarkdownCommand(TooltipHelper.SPEED_COLOR));
        addCommand("color_range", new ColorMarkdownCommand(TooltipHelper.RANGE_COLOR));
        addCommand("color_amount", new ColorMarkdownCommand(TooltipHelper.AMOUNT_COLOR));
        addCommand("color_hatch", new ColorMarkdownCommand(TooltipHelper.HATCH_COLOR));
        addCommand("color_loss", new ColorMarkdownCommand(TooltipHelper.LOSS_COLOR));
        addCommand("color_parallel", new ColorMarkdownCommand(TooltipHelper.PARALLEL_COLOR));
        addCommand("color_mode", new ColorMarkdownCommand(TooltipHelper.MODE_COLOR));
        addCommand("color_key", new ColorMarkdownCommand(TooltipHelper.KEY_COLOR));
        addCommand("color_efficiency", new ColorMarkdownCommand(TooltipHelper.EFF_COLOR));
        addCommand("color_eu_usage", new ColorMarkdownCommand(TooltipHelper.EU_USAGE_COLOR));
        addCommand("color_heat", new ColorMarkdownCommand(TooltipHelper.HEAT_COLOR));
        addCommand("color_hot", new ColorMarkdownCommand(TooltipHelper.HOT_COLOR));
        addCommand("color_electron_volt", new ColorMarkdownCommand(TooltipHelper.ELECTRON_VOLT_COLOR));
        addCommand("color_deprecated", new ColorMarkdownCommand(TooltipHelper.DEPRECATED_COLOR));
        addCommand("color_danger", new ColorMarkdownCommand(TooltipHelper.DANGER_COLOR));
        addCommand("color_mechanic", new ColorMarkdownCommand(TooltipHelper.MECHANIC_COLOR));
        addCommand("color_time", new ColorMarkdownCommand(TooltipHelper.TIME_COLOR));
        addCommand("color_pollution", new ColorMarkdownCommand(TooltipHelper.POLLUTION_COLOR));
        addCommand("color_amperage", new ColorMarkdownCommand(TooltipHelper.AMP_COLOR));

        addCommand("hr", new HRMarkdownCommand());
        addCommand("var", new VarMarkdownCommand(false));
        addCommand("var-eval", new VarMarkdownCommand(true));
        addCommand("lang", new LangMarkdownCommand(false));
        addCommand("lang-eval", new LangMarkdownCommand(true));
        addCommand("isolate", new IsolationMarkdownCommand());
        addAliases(new VoltageFormatMarkdownCommand(), "tier-color", "TC", "voltage-format", "VF");
        addAliases(new VoltageNameMarkdownCommand(), "tier-name", "TN", "voltage-name", "VN");
        addAliases(
            new FormattedVoltageNameMarkdownCommand(),
            "tier-color-name",
            "TCN",
            "voltage-name-formatted",
            "VNF");
        addAliases(new CommentMarkdownCommand(), "comment", "#", "//");
        addCommand("color-pattern", new ColorPatternMarkdownCommand());
        addAliases(new IfMarkdownCommand(true), "if-true", "if");
        addAliases(new IfMarkdownCommand(false), "if-false", "if-not");
        addAliases(new PluralityMarkdownCommand(true), "singular", "if-1");
        addAliases(new PluralityMarkdownCommand(false), "plural", "if-n-1");

        addAliases(new FluidUnitCommand(), "fluid-unit", "l", "L", "mb", "mB");
        addAliases(new SimpleUnitCommand("EU"), "energy-unit", "eu", "EU");
        addAliases(
            new SimpleUnitCommand("EU/t"),
            "energy-usage-unit",
            "eut",
            "EUt",
            "EUT",
            "eu/t",
            "EU/t",
            "eu/T",
            "EU/T");
        addAliases(new SimpleUnitCommand("GeV"), "giga-electron-volt-unit", "gev", "GeV", "GEV");
        addAliases(new SimpleUnitCommand("MeV"), "mega-electron-volt-unit", "mev", "MeV", "MEV");
        addAliases(new SimpleUnitCommand("KeV"), "kilo-electron-volt-unit", "kev", "KeV", "KEV");
        addAliases(new SimpleUnitCommand("K"), "heat-unit", "K", "k", "Kelvin", "kelvin");

    }

    public void addAliases(MarkdownCommand command, String... aliases) {
        for (String alias : aliases) {
            addCommand(alias, command);
        }
    }

    public void addCommand(String name, MarkdownCommand command) {
        commands.put(name, command);
    }

    /// Loads a markdown document at `assets/[mod id]/lang/[language id]/tooltip/[path].md`. On the client, resource
    /// packs are scanned. On the server, the file is loaded directly from the current jar.
    public List<String> loadStandardPath(ResourceLocation markdown, Map<String, Object> vars) {
        try {
            return loadStandardPath(markdown, getCurrentLanguage(), vars);
        } catch (IOException t1) {
            if (!(t1.getCause() instanceof FileNotFoundException)) {
                GTLoggers.GT_FML_LOGGER.error("Error loading {}", markdown, t1);
            }

            try {
                return loadStandardPath(markdown, "en_US", vars);
            } catch (IOException e) {
                throw new RuntimeException(
                    "Could not load the " + getCurrentLanguage() + " or en_US version of " + markdown,
                    e);
            }
        }
    }

    public List<String> loadStandardPath(ResourceLocation markdown, String language, Map<String, Object> vars)
        throws IOException {
        return loadExactPath(
            new ResourceLocation(
                markdown.getResourceDomain(),
                "lang/" + language + "/tooltip/" + markdown.getResourcePath() + ".md"),
            vars);
    }

    public List<String> loadExactPath(ResourceLocation markdown, Map<String, Object> vars) throws IOException {
        try {
            List<String> lines = loadGeneric(markdown);
            List<String> out = new ArrayList<>();

            for (String line : lines) {
                out.add(GTUtility.processFormatStacks(EnumChatFormatting.GRAY + eval(line, vars)));
            }

            return out;
        } catch (IOException e) {
            throw new IOException("Could not load or decode " + markdown, e);
        } catch (Throwable t) {
            throw new RuntimeException("Could not parse " + markdown, t);
        }
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

                int colon = command.indexOf(':');
                String commandName = colon == -1 ? command : command.substring(0, colon);
                String arg = colon == -1 ? "" : command.substring(colon + 1);

                MarkdownCommand commandHandler = MarkdownTooltipLoader.this.commands.get(commandName.trim());

                if (commandHandler == null) {
                    throw new IllegalStateException("Could not find command: " + commandName.trim());
                }

                commandHandler.process(MarkdownTooltipLoader.this, arg, out, vars);
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

            if (curr == '\\') {
                if (i < chars.length - 1) {
                    visitor.visitChar(chars[i + 1]);
                    i++;
                }
            } else if (curr == '{') {
                int end = i;
                int open = 0;

                while (end < chars.length) {
                    if (chars[end] == '{') {
                        open++;
                    }

                    if (chars[end] == '}') {
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

                    throw new IllegalStateException(
                        "Unclosed open brace in line:\n" + unparsed + "\n" + new String(spaces) + "^");
                }

                String command = unparsed.substring(i, end + 1);

                if (command.length() <= 2) {
                    throw new IllegalStateException(
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
    public interface MarkdownCommand {

        void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput, Map<String, Object> vars);
    }

    /**
     * <p>
     * Appends the localized name of the given item id.
     * </p>
     * <p>
     * Usage Example: {item:gregtech:gt.blockmachines:15757}
     * </p>
     */
    public static class ItemMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] chunks = arg.split(":");

            switch (chunks.length) {
                case 2 -> {
                    Item item = GameRegistry.findItem(chunks[0].trim(), chunks[1].trim());

                    if (item == null) {
                        lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                            .append(EnumChatFormatting.RED)
                            .append("[INVALID ITEM: ")
                            .append(chunks[0])
                            .append(":")
                            .append(chunks[1])
                            .append("]")
                            .append(GTUtility.FORMAT_POP_STACK);
                    } else {
                        lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                            .append(new ItemStack(item, 1).getDisplayName())
                            .append(GTUtility.FORMAT_POP_STACK);
                    }
                }
                case 3 -> {
                    Item item = GameRegistry.findItem(chunks[0].trim(), chunks[1].trim());

                    if (item == null) {
                        lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                            .append(EnumChatFormatting.RED)
                            .append("[INVALID ITEM: ")
                            .append(chunks[0])
                            .append(":")
                            .append(chunks[1])
                            .append("]")
                            .append(GTUtility.FORMAT_POP_STACK);
                        return;
                    }

                    int meta;

                    try {
                        meta = Integer.parseInt(chunks[2].trim());
                    } catch (NumberFormatException e) {
                        lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                            .append(EnumChatFormatting.RED)
                            .append("[INVALID META: ")
                            .append(chunks[2])
                            .append("]")
                            .append(GTUtility.FORMAT_POP_STACK);
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
     * Usage Example: {fluid:temporalfluid}
     * </p>
     */
    public static class FluidMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            Fluid fluid = FluidRegistry.getFluid(arg);

            if (fluid == null) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[INVALID FLUID: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
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
    public static class ColorMarkdownCommand implements MarkdownCommand {

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
    public static class HRMarkdownCommand implements MarkdownCommand {

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
    public static class VarMarkdownCommand implements MarkdownCommand {

        public final boolean eval;

        public VarMarkdownCommand(boolean eval) {
            this.eval = eval;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            Object value = vars.get(arg);

            if (value == null) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[MISSING VAR: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            if (eval) {
                loader.emit(value.toString(), lineOutput, vars);
            } else {
                lineOutput.append(value);
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
    public static class LangMarkdownCommand implements MarkdownCommand {

        public final boolean eval;

        public LangMarkdownCommand(boolean eval) {
            this.eval = eval;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            Localized loc;

            if (arg.contains(":")) {
                List<Object> paramList = new ArrayList<>();
                StringBuilder paramBuffer = new StringBuilder();

                // We use the same parse for eval and no-eval to keep the syntax the same.
                // If the command 'quoting' isn't desirable, the end-user can escape braces to split on colons within
                // command-like structures.
                parseCommands(arg, new CommandVisitor() {

                    @Override
                    public void visitChar(char c) {
                        if (c == ':') {
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

                String key = paramList.remove(0)
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
    public static class IsolationMarkdownCommand implements MarkdownCommand {

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
    public static class VoltageFormatMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            // format should be {command-name:tierVar:text to convert}
            String[] parts = arg.split(":", 2);

            // must have 1 or 2 parts
            if (parts.length != 1 && parts.length != 2) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER FORMAT ERROR NOT ENOUGH ARGS]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // var is required
            if (parts[0].isEmpty()) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER FORMAT ERROR: tier var is missing ]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if the arg is the correct type
            Object unparsed = vars.get(parts[0]);
            if (!(unparsed instanceof Integer tierIndex)) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER FORMAT ERROR MISSING VAR: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if it's a valid value
            if (tierIndex < 0 || tierIndex >= GTValues.TIER_COLORS.length) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER FORMAT ERROR: value is not a tier]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }
            String formatting = GTValues.TIER_COLORS[tierIndex];

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
    public static class VoltageNameMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            // check if the arg is the correct type
            Object unparsed = vars.get(arg);
            if (!(unparsed instanceof Integer tierIndex)) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER NAME ERROR MISSING VAR: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if it's a valid value
            if (tierIndex < 0 || tierIndex >= GTValues.VN.length) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER FORMAT ERROR: value is not a tier]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // else just append the code
            lineOutput.append(GTValues.VN[tierIndex]);
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
    public static class FormattedVoltageNameMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {

            // check if the arg is the correct type
            Object unparsed = vars.get(arg);
            if (!(unparsed instanceof Integer tierIndex)) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER NAME ERROR MISSING VAR: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if it's a valid value
            if (tierIndex < 0 || tierIndex >= GTValues.VN.length) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[TIER FORMAT ERROR: value is not a tier]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // else just append the code
            lineOutput.append(GTUtility.getColoredTierNameForTooltip(tierIndex));
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
    public static class CommentMarkdownCommand implements MarkdownCommand {

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
    public static class ColorPatternMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            // format should be {command-name:tierVar:text to convert}
            String[] parts = arg.split(":", 2);

            // must have 2 parts
            if (parts.length != 2) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[REPEAT COLOR MUST HAVE 2 ARGS]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // ignore if text is empty
            if (parts[1].isEmpty()) return;

            // colors are is required
            if (parts[0].isEmpty()) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[REPEAT COLOR MUST HAVE ARGS]")
                    .append(GTUtility.FORMAT_POP_STACK);
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
    public static class IfMarkdownCommand implements MarkdownCommand {

        final boolean wantsTrue;

        public IfMarkdownCommand(boolean mustBeTrue) {
            this.wantsTrue = mustBeTrue;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] parts = arg.split(":", 2);

            // must have 2 parts
            if (parts.length != 2) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[IF MUST HAVE 2 ARGS]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if the arg is the correct type
            Object unparsed = vars.get(parts[0]);
            if (!(unparsed instanceof Boolean value)) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[IF ERROR MISSING VAR: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if it's a valid value
            if (value == wantsTrue) loader.emit(parts[1], lineOutput, vars);
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
    public static class PluralityMarkdownCommand implements MarkdownCommand {

        final boolean wantsSingular;

        public PluralityMarkdownCommand(boolean wantsSingular) {
            this.wantsSingular = wantsSingular;
        }

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            String[] parts = arg.split(":", 2);

            // must have 2 parts
            if (parts.length != 2) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[PLURALITY MUST HAVE 2 ARGS]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if the arg is the correct type
            Object unparsed = vars.get(parts[0]);
            if (!(unparsed instanceof Integer value)) {
                lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.RED)
                    .append("[PLURALITY ERROR MISSING VAR: ")
                    .append(arg)
                    .append("]")
                    .append(GTUtility.FORMAT_POP_STACK);
                return;
            }

            // check if it's a valid value
            if (wantsSingular ^ (value != 1 && value != -1)) loader.emit(parts[1], lineOutput, vars);
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
    public static class FluidUnitCommand implements MarkdownCommand {

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
    public static class SimpleUnitCommand implements MarkdownCommand {

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
