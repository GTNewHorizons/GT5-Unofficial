package gregtech.api.structure;

import java.util.List;

import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureWalker;

import gregtech.api.structure.error.PositionedStructureError;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;

public class StructureChecker<T> implements IStructureWalker<T> {

    T instance;
    final boolean forced;
    final List<StructureError> errors;
    public boolean success = true;

    public StructureChecker(T instance, boolean forced, List<StructureError> errors) {
        this.instance = instance;
        this.forced = forced;
        this.errors = errors;
    }

    @Override
    public boolean visit(IStructureElement<T> element, World world, int x, int y, int z, int a, int b, int c) {
        boolean result = element.check(instance, world, x, y, z);

        if (!result) {
            this.success = false;
            errors.add(new PositionedStructureError(x, y, z));
        }

        return result;
    }

    @Override
    public boolean blockNotLoaded(IStructureElement<T> element, World world, int x, int y, int z, int a, int b, int c) {
        if (forced) {
            return visit(element, world, x, y, z, a, b, c);
        }
        this.success = false;
        errors.add(StructureErrorRegistry.BLOCK_NOT_LOADED);
        return false;
    }
}
