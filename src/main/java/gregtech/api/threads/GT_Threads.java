package gregtech.api.threads;

import gregtech.GT_Mod;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class GT_Threads {

    private final Map<Class<? extends Runnable>, ThreadFactory> CLASS_THREAD_FACTORY_MAP = new HashMap<>();

    static {
        CLASS_THREAD_FACTORY_MAP.put(GT_Runnable_RecipeAsyncHandler.class, r -> {
            Thread thread = new Thread(r);
            thread.setName("GT_RecipeLookup");
            return thread;
        });

        CLASS_THREAD_FACTORY_MAP.put(GT_Runnable_MachineBlockUpdate.class, r -> {
            Thread thread = new Thread(r);
            thread.setName("GT_MachineBlockUpdate");
            return thread;
        });
    }

    @Getter
    private final Map<Class<? extends Runnable>, ExecutorService> EXECUTOR_SERVICE_MAP = new HashMap<>();

    @Getter
    private final ExecutorService OREDICT_EVENT_REGISTRATOR_POOL = Executors.newWorkStealingPool();

    public void stopOREDICT_EVENT_REGISTRATOR_POOL() {
        shutdownService(GT_Runnable_OredictEventRegistrator.class, OREDICT_EVENT_REGISTRATOR_POOL);
    }

    public void initExecutorServices() {
        CLASS_THREAD_FACTORY_MAP.forEach((aClass, threadFactory) ->
                EXECUTOR_SERVICE_MAP.put(
                        aClass,
                        Executors.newFixedThreadPool(
                                (Runtime.getRuntime().availableProcessors() * 2 / 3),
                                threadFactory
                        )
                )
        );
    }

    public void shutdownExecutorServices() {
        EXECUTOR_SERVICE_MAP.forEach(GT_Threads::shutdownService);
    }

    private void shutdownService(Class<? extends Runnable> aCLASS, ExecutorService EXECUTOR_SERVICE) {
        try {
            GT_Mod.GT_FML_LOGGER.info("Shutting down " + aCLASS.getSimpleName() + " executor service");
            EXECUTOR_SERVICE.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                EXECUTOR_SERVICE.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
                    GT_Mod.GT_FML_LOGGER.error("Well this didn't terminated well...");
                }
            }
        } catch (InterruptedException ie) {
            GT_Mod.GT_FML_LOGGER.error("Well this interruption got interrupted...", ie);
            // (Re-)Cancel if current thread also interrupted
            EXECUTOR_SERVICE.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            GT_Mod.GT_FML_LOGGER.error("Well this didn't terminated well...", e);
            // (Re-)Cancel in case
            EXECUTOR_SERVICE.shutdownNow();
        } finally {
            GT_Mod.GT_FML_LOGGER.info("Leaving...");
        }
    }
}