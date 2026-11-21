package gregtech.common;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.RecipeRegistrationDiagnostics;
import gregtech.api.util.RecipeRegistrationException;

public class RecipeRegistrationErrorHandler {

    private static final RecipeRegistrationErrorHandler INSTANCE = new RecipeRegistrationErrorHandler();

    private boolean active;
    private boolean listening;

    public static void start() {
        synchronized (INSTANCE) {
            INSTANCE.active = true;
        }
        RecipeRegistrationDiagnostics.setPendingReportListener(INSTANCE::subscribe);
    }

    public static void stop() {
        RecipeRegistrationDiagnostics.setPendingReportListener(null);
        synchronized (INSTANCE) {
            INSTANCE.active = false;
            INSTANCE.unsubscribe();
        }
        RecipeRegistrationDiagnostics.clear();
    }

    private synchronized void subscribe() {
        if (!active || listening) return;
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        listening = true;
    }

    private synchronized void unsubscribe() {
        if (!listening) return;
        FMLCommonHandler.instance()
            .bus()
            .unregister(this);
        listening = false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        try {
            reportErrors();
        } finally {
            unsubscribe();
            if (RecipeRegistrationDiagnostics.hasPendingReports()) subscribe();
        }
    }

    public static void reportErrors() {
        try {
            RecipeRegistrationDiagnostics.reportAll(GTRecipeBuilder.PANIC_MODE_NULL);
        } catch (RecipeRegistrationException exception) {
            CrashReport report = CrashReport
                .makeCrashReport(exception, "Invalid recipe components during registration");
            String details = report.getCompleteReport();
            GT_FML_LOGGER.fatal(details);
            Path output = Loader.instance()
                .getConfigDir()
                .toPath()
                .getParent()
                .resolve("crash-reports")
                .resolve("crash-recipe-registration-" + System.currentTimeMillis() + ".txt");
            try {
                Files.createDirectories(output.getParent());
                Files.writeString(output, details, StandardCharsets.UTF_8);
                GT_FML_LOGGER.fatal("Recipe registration crash report saved to {}", output.toAbsolutePath());
            } catch (IOException | RuntimeException writeFailure) {
                GT_FML_LOGGER.error("Could not save the recipe registration crash report", writeFailure);
            }
            FMLCommonHandler.instance()
                .exitJava(1, false);
            throw new ReportedException(report);
        }
    }
}
