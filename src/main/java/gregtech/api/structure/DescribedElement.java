package gregtech.api.structure;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;

/**
 * Wraps an {@link IStructureElement} with a human-readable description of what was expected at this position. Used by
 * {@link StructureChecker} to produce more informative error messages.
 * <p>
 * When {@link #check} fails, the description is stored in a static field so that {@link StructureChecker} can retrieve
 * it even when this element is inside wrapper elements (e.g. {@code lazy()}). This is safe because structure checking
 * runs on the server thread only.
 */
public class DescribedElement<T> implements IStructureElement<T> {

    static String lastFailedDescription;

    private final IStructureElement<T> delegate;
    private final Supplier<String> descriptionSupplier;
    private String description;

    public DescribedElement(IStructureElement<T> delegate, Supplier<String> descriptionSupplier) {
        this.delegate = delegate;
        this.descriptionSupplier = descriptionSupplier;
    }

    public String getDescription() {
        if (description == null) {
            description = descriptionSupplier.get();
        }
        return description;
    }

    @Override
    public boolean check(T t, World world, int x, int y, int z) {
        boolean result = delegate.check(t, world, x, y, z);
        if (!result) {
            lastFailedDescription = getDescription();
        }
        return result;
    }

    @Override
    public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
        return delegate.spawnHint(t, world, x, y, z, trigger);
    }

    @Override
    public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
        return delegate.placeBlock(t, world, x, y, z, trigger);
    }

    @Override
    public boolean couldBeValid(T t, World world, int x, int y, int z, ItemStack trigger) {
        return delegate.couldBeValid(t, world, x, y, z, trigger);
    }

    @Override
    public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
        AutoPlaceEnvironment env) {
        return delegate.getBlocksToPlace(t, world, x, y, z, trigger, env);
    }

    @Deprecated
    @Override
    public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger, IItemSource s,
        EntityPlayerMP actor, Consumer<IChatComponent> chatter) {
        return delegate.survivalPlaceBlock(t, world, x, y, z, trigger, s, actor, chatter);
    }

    @Override
    public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
        AutoPlaceEnvironment env) {
        return delegate.survivalPlaceBlock(t, world, x, y, z, trigger, env);
    }

    @Override
    public IStructureElementNoPlacement<T> noPlacement() {
        return delegate.noPlacement();
    }

    @Override
    public int getStepA() {
        return delegate.getStepA();
    }

    @Override
    public int getStepB() {
        return delegate.getStepB();
    }

    @Override
    public int getStepC() {
        return delegate.getStepC();
    }

    @Override
    public boolean resetA() {
        return delegate.resetA();
    }

    @Override
    public boolean resetB() {
        return delegate.resetB();
    }

    @Override
    public boolean resetC() {
        return delegate.resetC();
    }

    @Override
    public boolean isNavigating() {
        return delegate.isNavigating();
    }
}
