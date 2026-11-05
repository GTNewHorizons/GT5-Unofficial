package gregtech.api.recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import com.google.common.collect.ImmutableSet;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Immutable conjunctive requirements shared by recipes, their copies and saved active processes
 * Saved IDs with missing definitions are discarded when loading an active process
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RecipeEnvironmentRequirements {

    public static final RecipeEnvironmentRequirements EMPTY = new RecipeEnvironmentRequirements(ImmutableSet.of());

    private final Set<String> ids;

    private RecipeEnvironmentRequirements(Collection<String> ids) {
        this.ids = ImmutableSet.copyOf(ids);
    }

    public static RecipeEnvironmentRequirements of(RecipeEnvironment... environments) {
        return EMPTY.with(environments);
    }

    public RecipeEnvironmentRequirements with(RecipeEnvironment... environments) {
        Objects.requireNonNull(environments, "environments");
        Set<String> combined = new LinkedHashSet<>(ids);
        for (RecipeEnvironment environment : environments) {
            Objects.requireNonNull(environment, "environment");
            if (RecipeEnvironment.get(environment.getId()) != environment) {
                throw new IllegalArgumentException("Unregistered recipe environment: " + environment.getId());
            }
            combined.add(environment.getId());
        }
        return combined.size() == ids.size() ? this : new RecipeEnvironmentRequirements(combined);
    }

    public RecipeEnvironmentRequirements without(RecipeEnvironment environment) {
        return without(new RecipeEnvironment[] { environment });
    }

    public RecipeEnvironmentRequirements without(RecipeEnvironment... environments) {
        Objects.requireNonNull(environments, "environments");
        Set<String> remaining = new LinkedHashSet<>(ids);
        for (RecipeEnvironment environment : environments) {
            remaining.remove(
                Objects.requireNonNull(environment, "environment")
                    .getId());
        }
        if (remaining.size() == ids.size()) return this;
        return remaining.isEmpty() ? EMPTY : new RecipeEnvironmentRequirements(remaining);
    }

    public RecipeEnvironmentRequirements and(RecipeEnvironmentRequirements other) {
        Objects.requireNonNull(other, "other");
        if (other.isEmpty()) return this;
        Set<String> combined = new LinkedHashSet<>(ids);
        combined.addAll(other.ids);
        return combined.size() == ids.size() ? this : new RecipeEnvironmentRequirements(combined);
    }

    public boolean contains(RecipeEnvironment environment) {
        return ids.contains(
            Objects.requireNonNull(environment, "environment")
                .getId());
    }

    public boolean isEmpty() {
        return ids.isEmpty();
    }

    public Set<String> getIds() {
        return ids;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
            || (other instanceof RecipeEnvironmentRequirements requirements && ids.equals(requirements.ids));
    }

    @Override
    public int hashCode() {
        return ids.hashCode();
    }

    public boolean matches(MetaTileEntity machine, boolean whileRunning) {
        for (String id : ids) {
            RecipeEnvironment environment = RecipeEnvironment.get(id);
            if (environment == null) return false;
            if ((!whileRunning || environment.requiresContinuousValidation()) && !environment.isSatisfied(machine)) {
                return false;
            }
        }
        return true;
    }

    public List<RecipeEnvironment> getUnmetEnvironments(MetaTileEntity machine, boolean whileRunning) {
        List<RecipeEnvironment> unmet = new ArrayList<>();
        for (String id : ids) {
            RecipeEnvironment environment = RecipeEnvironment.get(id);
            if (environment != null && (!whileRunning || environment.requiresContinuousValidation())
                && !environment.isSatisfied(machine)) {
                unmet.add(environment);
            }
        }
        return unmet;
    }

    public boolean requiresValidationWhileRunning() {
        for (String id : ids) {
            RecipeEnvironment environment = RecipeEnvironment.get(id);
            if (environment != null && environment.requiresContinuousValidation()) return true;
        }
        return false;
    }

    public NBTTagList writeToNBT() {
        NBTTagList result = new NBTTagList();
        for (String id : ids) {
            result.appendTag(new NBTTagString(id));
        }
        return result;
    }

    /**
     * Loads active requirements after addon environments have been registered
     */
    public static RecipeEnvironmentRequirements readFromNBT(NBTTagList tags) {
        if (tags.tagCount() == 0) return EMPTY;
        Set<String> ids = new LinkedHashSet<>();
        for (int i = 0; i < tags.tagCount(); i++) {
            String id = tags.getStringTagAt(i);
            if (RecipeEnvironment.get(id) != null) ids.add(id);
        }
        return ids.isEmpty() ? EMPTY : new RecipeEnvironmentRequirements(ids);
    }
}
