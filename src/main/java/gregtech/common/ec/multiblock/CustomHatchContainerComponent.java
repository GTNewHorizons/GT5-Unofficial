package gregtech.common.ec.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.multiblock.CustomHatchKey;
import gregtech.api.ec.component.multiblock.ICustomHatchContainerComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;

public final class CustomHatchContainerComponent extends BaseHatchContainerComponent
    implements ICustomHatchContainerComponent {

    private static final class CustomHatchEntry<T extends MTEHatch> {

        final CustomHatchKey<T> key;
        final List<T> hatches = new ArrayList<>();

        private CustomHatchEntry(CustomHatchKey<T> key) {
            this.key = key;
        }
    }

    private final CustomHatchEntry<?>[] entries;

    public CustomHatchContainerComponent(@NotNull Collection<? extends CustomHatchKey<?>> enabled) {
        entries = new CustomHatchEntry[enabled.size()];
        int i = 0;
        for (CustomHatchKey<?> key : enabled) {
            entries[i] = new CustomHatchEntry<>(key);
            i++;
        }
    }

    private <T extends MTEHatch> @Nullable List<T> find(@NotNull CustomHatchKey<T> key) {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].key == key) return (List<T>) entries[i].hatches;
        }
        return null;
    }

    @Override
    public <T extends MTEHatch> boolean supports(@NotNull CustomHatchKey<T> key) {
        return find(key) != null;
    }

    @Override
    public <T extends MTEHatch> boolean tryAttach(@NotNull CustomHatchKey<T> key, @NotNull IMetaTileEntity metaTile,
        int baseCasingIndex) {
        var hatches = find(key);
        if (hatches == null) return false;
        var matched = key.tryCast(metaTile);
        if (matched == null) return false;
        if (!hatches.contains(matched)) {
            onHatchAttached(matched, baseCasingIndex);
            hatches.add(matched);
            key.onAttach(getMachine(), matched);
        }
        return true;
    }

    @Override
    public @NotNull <T extends MTEHatch> List<T> getHatches(@NotNull CustomHatchKey<T> key) {
        var hatches = find(key);
        return hatches == null ? Collections.emptyList() : hatches;
    }

    @Override
    public void detachAllHatches() {
        for (var entry : entries) {
            detachEntryHatches(entry);
        }
    }

    private <T extends MTEHatch> void detachEntryHatches(@NotNull CustomHatchEntry<T> entry) {
        var hatches = entry.hatches;
        if (hatches.isEmpty()) return;
        clearCraftingIcons(hatches);
        var multiblock = getMachine();
        for (var hatch : hatches) {
            entry.key.onDetach(multiblock, hatch);
        }
        hatches.clear();
    }

    @Override
    public void explodeAllHatches(long power) {
        for (var entry : entries) {
            if (!entry.key.explodeOnMultiblockExplosion) continue;
            for (var hatch : validMTEList(entry.hatches)) {
                hatch.getBaseMetaTileEntity()
                    .doExplosion(power);
            }
        }
    }
}
