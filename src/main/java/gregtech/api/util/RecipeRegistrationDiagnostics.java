package gregtech.api.util;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.objects.OreDictItemStack;
import gregtech.api.objects.SubstituteFluidStack;

public class RecipeRegistrationDiagnostics {

    private static final ConcurrentLinkedQueue<Report> PENDING_REPORTS = new ConcurrentLinkedQueue<>();
    @Nullable
    private static volatile Runnable pendingReportListener;

    private RecipeRegistrationDiagnostics() {}

    public static void reportAll(boolean failOnErrors) {
        if (PENDING_REPORTS.isEmpty()) return;
        List<IllegalArgumentException> failures = new ArrayList<>();
        int issues = 0;
        Report report;
        while ((report = PENDING_REPORTS.poll()) != null) {
            ReportedRecipe result = report.log(failures.size() + 1);
            if (result == null) continue;
            issues += result.componentCount();
            failures.add(result.failure());
        }
        if (failures.isEmpty()) return;

        RecipeRegistrationException exception = new RecipeRegistrationException(failures.size(), issues);
        failures.forEach(exception::addSuppressed);
        GT_FML_LOGGER.error(exception.getMessage());
        IllegalArgumentException source = new IllegalArgumentException("First invalid recipe registration");
        source.setStackTrace(
            failures.get(0)
                .getStackTrace());
        GT_FML_LOGGER.error("Recipe registration source stack (recipe #1)", source);
        if (failOnErrors) throw exception;
    }

    public static void setPendingReportListener(@Nullable Runnable listener) {
        pendingReportListener = listener;
        if (listener != null && !PENDING_REPORTS.isEmpty()) listener.run();
    }

    public static boolean hasPendingReports() {
        return !PENDING_REPORTS.isEmpty();
    }

    public static void clear() {
        Report report;
        while ((report = PENDING_REPORTS.poll()) != null) report.release();
    }

    public static String describe(Object value) {
        if (value == null) return "null";
        try {
            if (value.getClass()
                .isArray()) {
                StringBuilder text = new StringBuilder("[");
                for (int i = 0, length = Array.getLength(value); i < length; i++) {
                    if (i > 0) text.append(", ");
                    text.append(describe(Array.get(value, i)));
                }
                return text.append(']')
                    .toString();
            }
            if (value instanceof ItemStack stack) {
                Item item = stack.getItem();
                Object name = item == null ? "<null item>" : Item.itemRegistry.getNameForObject(item);
                return name + ":"
                    + stack.getItemDamage()
                    + " x"
                    + stack.stackSize
                    + (stack.hasTagCompound() ? " tag=" + stack.getTagCompound() : "");
            }
            if (value instanceof FluidStack stack) {
                String name = stack.getFluid() == null ? "<null fluid>"
                    : stack.getFluid()
                        .getName();
                return name + " x" + stack.amount + (stack.tag == null ? "" : " tag=" + stack.tag);
            }
            if (value instanceof OreDictItemStack stack) {
                return "ore:" + stack.mOreName + " x" + stack.mAmount;
            }
            if (value instanceof SubstituteFluidStack stack) {
                return "substitutes=" + describe(stack.fluidStacks.toArray());
            }
            return String.valueOf(value);
        } catch (RuntimeException | LinkageError e) {
            return "<unavailable " + value.getClass()
                .getSimpleName()
                + ": "
                + e.getClass()
                    .getSimpleName()
                + ">";
        }
    }

    public record ComponentIssue(String path, String problem, String arguments, List<StackTraceElement> stackTrace) {

        public ComponentIssue {
            stackTrace = List.copyOf(stackTrace);
        }
    }

    private record IssueContext(String problem, String arguments, List<StackTraceElement> stackTrace) {}

    private record ReportedRecipe(IllegalArgumentException failure, int componentCount) {}

    private static List<StackTraceElement> registrationStack(List<StackTraceElement> stackTrace) {
        List<StackTraceElement> result = new ArrayList<>();
        for (StackTraceElement frame : stackTrace) {
            String name = frame.getClassName();
            if (name.equals(GTRecipeBuilder.class.getName())
                || name.equals(RecipeRegistrationDiagnostics.class.getName())
                || name.startsWith(RecipeRegistrationDiagnostics.class.getName() + "$")) continue;
            if (name.startsWith("java.") || name.startsWith("jdk.")
                || name.startsWith("sun.")
                || name.startsWith("cpw.mods.fml.")
                || name.startsWith("com.google.common.eventbus.")
                || name.startsWith("net.minecraft.")) {
                if (!result.isEmpty()) break;
                continue;
            }
            result.add(frame);
        }
        return result;
    }

    public static class Report {

        private Supplier<String> description;
        private List<ComponentIssue> issues = new ArrayList<>();
        private Set<String> targets = new LinkedHashSet<>();
        private boolean queued;

        public Report(Supplier<String> description) {
            this.description = description;
        }

        public synchronized void addIssue(String path, String problem, Object arguments, Supplier<String> description) {
            if (this.description == null) {
                this.description = description;
                issues = new ArrayList<>();
                targets = new LinkedHashSet<>();
            }
            issues
                .add(new ComponentIssue(path, problem, describe(arguments), List.of(new Throwable().getStackTrace())));
            enqueue();
        }

        public synchronized void addTarget(String target, Supplier<String> description) {
            if (this.description == null) return;
            targets.add(target);
            this.description = description;
            if (!issues.isEmpty()) enqueue();
        }

        public synchronized void freezeDescription() {
            if (description == null) return;
            String details = description.get();
            description = () -> details;
        }

        private void enqueue() {
            if (queued) return;
            queued = true;
            PENDING_REPORTS.add(this);
            Runnable listener = pendingReportListener;
            if (listener != null) listener.run();
        }

        private synchronized void release() {
            description = null;
            issues = List.of();
            targets = Set.of();
            queued = false;
        }

        @Nullable
        private synchronized ReportedRecipe log(int index) {
            try {
                if (description == null || issues.isEmpty()) return null;
                String details = "targets=" + (targets.isEmpty() ? "<not submitted>" : targets)
                    + ", "
                    + description.get();
                IllegalArgumentException failure = new IllegalArgumentException(details);
                failure.setStackTrace(
                    issues.get(0)
                        .stackTrace()
                        .toArray(new StackTraceElement[0]));
                GT_FML_LOGGER.error("Recipe component diagnostics #{}: {}", index, details);
                Map<IssueContext, List<String>> grouped = new LinkedHashMap<>();
                for (ComponentIssue issue : issues) {
                    IssueContext context = new IssueContext(issue.problem(), issue.arguments(), issue.stackTrace());
                    grouped.computeIfAbsent(context, key -> new ArrayList<>())
                        .add(issue.path());
                }
                for (Map.Entry<IssueContext, List<String>> entry : grouped.entrySet()) {
                    IssueContext context = entry.getKey();
                    String paths = String.join(", ", entry.getValue());
                    List<StackTraceElement> source = registrationStack(context.stackTrace());
                    GT_FML_LOGGER.error(
                        "  {}: {}; arguments={}; source={}",
                        paths,
                        context.problem(),
                        context.arguments(),
                        source.isEmpty() ? "<unavailable>" : source.get(0));
                    IllegalArgumentException component = new IllegalArgumentException(
                        paths + ": " + context.problem() + "; arguments=" + context.arguments());
                    component.setStackTrace(
                        context.stackTrace()
                            .toArray(new StackTraceElement[0]));
                    failure.addSuppressed(component);
                }
                List<StackTraceElement> registration = registrationStack(
                    issues.get(0)
                        .stackTrace());
                if (registration.size() > 1) {
                    GT_FML_LOGGER.error("  registration chain: {}", registration);
                }
                return new ReportedRecipe(failure, issues.size());
            } finally {
                release();
            }
        }
    }
}
