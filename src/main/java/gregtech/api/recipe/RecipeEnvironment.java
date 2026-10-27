package gregtech.api.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * A registered recipe condition evaluated against the machine's current surroundings
 * Register addon conditions during startup, before recipes and saved machines use their IDs
 * ProcessingLogic checks admission and records the accepted requirements before processing starts
 * Standard basic and multiblock lifecycles, including native fusion and TecTech controllers, validate
 * continuous requirements every 100 ticks and before completion
 * Custom recipe consumers must check admission before consuming inputs, validate active requirements before progress
 * or output, and clear them on completion or stop
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class RecipeEnvironment {

    // Addons register during startup
    // Recipe execution and saved machines share these IDs for the game session
    private static final Map<String, RecipeEnvironment> REGISTRY = new HashMap<>();

    public static final RecipeEnvironment CLEANROOM = register(
        new RecipeEnvironment("gregtech:cleanroom", "GT5U.nei.needs_cleanroom") {

            @Override
            public boolean isSatisfied(MetaTileEntity machine) {
                // Existing multiblock processing is exempt from cleanroom requirements
                if (!(machine instanceof MTEBasicMachine)) return true;
                ICleanroomReceiver receiver = machine.getCapability(ICleanroomReceiver.class, ForgeDirection.UNKNOWN);
                ICleanroom cleanroom = receiver == null ? null : receiver.getCleanroom();
                return cleanroom != null && cleanroom.isValidCleanroom() && cleanroom.getCleanness() != 0;
            }

            @Override
            public boolean requiresContinuousValidation() {
                return false;
            }
        });

    public static final RecipeEnvironment LOW_GRAVITY = register(
        new RecipeEnvironment("gregtech:low_gravity", "GT5U.nei.needs_low_gravity") {

            @Override
            public boolean isSatisfied(MetaTileEntity machine) {
                if (!(machine instanceof MTEBasicMachine) || !GTMod.proxy.mLowGravProcessing) return true;
                return MTEBasicMachine.isValidForLowGravity(
                    machine.getBaseMetaTileEntity()
                        .getWorld().provider.dimensionId);
            }

            @Override
            public boolean requiresContinuousValidation() {
                return false;
            }
        });

    private final String id;
    private final String translationKey;

    protected RecipeEnvironment(String id, String translationKey) {
        this.id = new ResourceLocation(Objects.requireNonNull(id)).toString();
        this.translationKey = Objects.requireNonNull(translationKey);
    }

    /**
     * Registers one shared condition instance
     * Duplicate IDs are rejected
     */
    public static <T extends RecipeEnvironment> T register(T environment) {
        Objects.requireNonNull(environment);
        if (REGISTRY.putIfAbsent(environment.getId(), environment) != null) {
            throw new IllegalArgumentException("Duplicate recipe environment: " + environment.getId());
        }
        return environment;
    }

    @Nullable
    public static RecipeEnvironment get(String id) {
        return REGISTRY.get(id);
    }

    public final String getId() {
        return id;
    }

    public String getDescription() {
        return StatCollector.translateToLocal(translationKey);
    }

    /**
     * Reads current conditions without consuming resources or modifying the machine
     */
    public abstract boolean isSatisfied(MetaTileEntity machine);

    /**
     * Whether losing this condition during processing follows the machine's native power failure path
     * Fixed conditions and admission-only requirements return false
     * Continuous requirements are checked every 100 ticks and before completion
     * Providers can notify machines through {@link MetaTileEntity#onRecipeEnvironmentChanged()} for an earlier check
     */
    public boolean requiresContinuousValidation() {
        return true;
    }
}
