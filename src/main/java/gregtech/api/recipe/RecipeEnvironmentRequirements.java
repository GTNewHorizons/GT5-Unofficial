package gregtech.api.recipe;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import com.google.common.collect.ImmutableSet;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Immutable conjunctive requirements shared by recipes, their copies and saved active processes
 * Saved IDs remain present when their defining addon is missing, so unknown conditions fail validation
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
        Set<String> combined = new LinkedHashSet<>(ids);
        for (RecipeEnvironment environment : environments) {
            if (RecipeEnvironment.get(environment.getId()) != environment) {
                throw new IllegalArgumentException("Unregistered recipe environment: " + environment.getId());
            }
            combined.add(environment.getId());
        }
        return combined.size() == ids.size() ? this : new RecipeEnvironmentRequirements(combined);
    }

    public RecipeEnvironmentRequirements without(RecipeEnvironment environment) {
        if (!contains(environment)) return this;
        Set<String> remaining = new LinkedHashSet<>(ids);
        remaining.remove(environment.getId());
        return remaining.isEmpty() ? EMPTY : new RecipeEnvironmentRequirements(remaining);
    }

    public RecipeEnvironmentRequirements and(RecipeEnvironmentRequirements other) {
        if (other.isEmpty()) return this;
        Set<String> combined = new LinkedHashSet<>(ids);
        combined.addAll(other.ids);
        return combined.size() == ids.size() ? this : new RecipeEnvironmentRequirements(combined);
    }

    public boolean contains(RecipeEnvironment environment) {
        return ids.contains(environment.getId());
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

    public boolean hasContinuousRequirements() {
        for (String id : ids) {
            RecipeEnvironment environment = RecipeEnvironment.get(id);
            if (environment == null || environment.requiresContinuousValidation()) return true;
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

    public static RecipeEnvironmentRequirements readFromNBT(NBTTagList tags) {
        if (tags.tagCount() == 0) return EMPTY;
        Set<String> ids = new LinkedHashSet<>();
        for (int i = 0; i < tags.tagCount(); i++) {
            ids.add(tags.getStringTagAt(i));
        }
        return new RecipeEnvironmentRequirements(ids);
    }
}
