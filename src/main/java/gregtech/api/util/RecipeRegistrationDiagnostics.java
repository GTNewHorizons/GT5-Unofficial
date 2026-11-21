package gregtech.api.util;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
            IllegalArgumentException failure = report.log(failures.size() + 1);
            if (failure == null) continue;
            issues += failure.getSuppressed().length;
            failures.add(failure);
        }
        if (failures.isEmpty()) return;

        RecipeRegistrationException exception = new RecipeRegistrationException(failures.size(), issues);
        failures.forEach(exception::addSuppressed);
        GT_FML_LOGGER.error(exception.getMessage());
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
        private synchronized IllegalArgumentException log(int index) {
            try {
                if (description == null || issues.isEmpty()) return null;
                String details = "targets=" + (targets.isEmpty() ? "<not submitted>" : targets)
                    + ", "
                    + description.get();
                IllegalArgumentException failure = new IllegalArgumentException(details);
                GT_FML_LOGGER.error("Recipe component diagnostics #{}: {}", index, details);
                for (ComponentIssue issue : issues) {
                    GT_FML_LOGGER.error("  {}: {}; arguments={}", issue.path(), issue.problem(), issue.arguments());
                    IllegalArgumentException component = new IllegalArgumentException(
                        issue.path() + ": " + issue.problem() + "; arguments=" + issue.arguments());
                    component.setStackTrace(
                        issue.stackTrace()
                            .toArray(new StackTraceElement[0]));
                    failure.addSuppressed(component);
                    for (StackTraceElement frame : issue.stackTrace()) {
                        if (frame.getClassName()
                            .equals(Report.class.getName())) continue;
                        GT_FML_LOGGER.error("    at {}", frame);
                    }
                }
                return failure;
            } finally {
                release();
            }
        }
    }
}
