package gregtech.loaders.oreprocessing;

import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;

/// Replays the missed dust-prefix oreDict notification for `InfusedAir`/`InfusedFire`/`InfusedEarth`/
/// `InfusedWater`, restoring the [ProcessingDust] recipes (implosion compressor, autoclave, magic fuels) that
/// depend on it.
///
/// These four are real legacy-named materials, so MaterialLib registers their canonical dust into Forge's
/// `OreDictionary` before `LoaderOreProcessing` has constructed [ProcessingDust] as a `dust`-prefix listener.
/// `GTProxy#catchUpPreExistingOreDictEntries` has already replayed existing entries by then, and nothing
/// re-registers the same (name, stack) pair afterward, so this replays the notification directly.
public final class ProcessingInfusedStonesGtpp {

    private ProcessingInfusedStonesGtpp() {}

    private static final Set<Material> MATERIALS = Set
        .of(Materials.InfusedAir, Materials.InfusedFire, Materials.InfusedEarth, Materials.InfusedWater);

    public static void run() {
        if (ProcessingDust.INSTANCE == null) return;
        for (Material material : MATERIALS) {
            ItemStack dust = MaterialLibAPI.getStack(material, Shapes.dust, 1);
            ProcessingDust.INSTANCE
                .registerOre(OrePrefixes.dust, material, OrePrefixes.dust.oreDictName(material), "gregtech", dust);
        }
    }
}
