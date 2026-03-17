package gregtech.api.util.tooltip;

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
import gregtech.GTMod;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;

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

    public static final MarkdownTooltipLoader STANDARD = new MarkdownTooltipLoader();

    private final HashMap<String, MarkdownCommand> commands = new HashMap<>();

    public MarkdownTooltipLoader() {
        addCommand("item", new ItemMarkdownCommand());
        addCommand("fluid", new FluidMarkdownCommand());

        for (EnumChatFormatting format : EnumChatFormatting.values()) {
            addCommand(format.getFriendlyName(), new ColorMarkdownCommand(format));
        }

        addCommand("hr", new HRMarkdownCommand());
        addCommand("var", new VarMarkdownCommand(false));
        addCommand("var-eval", new VarMarkdownCommand(true));
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
                GTMod.GT_FML_LOGGER.error("Error loading {}", markdown, t1);
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
        final char[] chars = unparsed.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char curr = chars[i];

            if (curr == '\\') {
                if (i < chars.length - 1) {
                    out.append(chars[i + 1]);
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

                command = command.substring(1, command.length() - 1);

                int colon = command.indexOf(':');
                String commandName = colon == -1 ? command : command.substring(0, colon);
                String arg = colon == -1 ? "" : command.substring(colon + 1);

                MarkdownCommand commandHandler = this.commands.get(commandName.trim());

                if (commandHandler == null) {
                    throw new IllegalStateException("Could not find command: " + commandName.trim());
                }

                commandHandler.process(this, arg, out, vars);

                i = end;
            } else {
                out.append(chars[i]);
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

    public interface MarkdownCommand {

        void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput, Map<String, Object> vars);
    }

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

    public static class HRMarkdownCommand implements MarkdownCommand {

        @Override
        public void process(MarkdownTooltipLoader loader, String arg, StringBuilder lineOutput,
            Map<String, Object> vars) {
            int length = 41;

            if (!arg.isEmpty()) {
                length = Integer.parseInt(arg);
            }

            switch (GTMod.proxy.separatorStyle) {
                case 0 -> lineOutput.append(" ");
                case 1 -> lineOutput.append(StringUtils.getRepetitionOf('-', length));
                default -> lineOutput.append(GTUtility.FORMAT_PUSH_STACK)
                    .append(EnumChatFormatting.STRIKETHROUGH)
                    .append(StringUtils.getRepetitionOf('-', length))
                    .append(GTUtility.FORMAT_POP_STACK);
            }
        }
    }

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
}
