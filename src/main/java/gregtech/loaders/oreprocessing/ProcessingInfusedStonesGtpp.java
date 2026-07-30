package gregtech.loaders.oreprocessing;

import java.util.Set;

import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.OrePrefixes;

/// Replays the missed dust-prefix oreDict notification for `InfusedAir`/`InfusedFire`/`InfusedEarth`/
/// `InfusedWater`, restoring the [ProcessingDust] recipes (implosion compressor, autoclave, magic fuels) that
/// depend on it.
///
/// These four are real legacy-named materials (carrying a `GTMaterialProperties#OLD_SUB_ID` of 540-543), so
/// MaterialLib registers their canonical dust item into Forge's `OreDictionary` during its own early
/// resolution, before `GTProxy#catchUpPreExistingOreDictEntries` replays existing entries -- but that replay
/// itself runs from GT's preInit before `LoaderOreProcessing` constructs [ProcessingDust] and adds it as a
/// `dust`-prefix listener, so
/// the replay finds no listener to notify. Nothing re-registers the same (name, stack) pair afterward for
/// `GTOreDictUnificator` to fire a second event from, so this replays the notification directly.
public final class ProcessingInfusedStonesGtpp {

    private ProcessingInfusedStonesGtpp() {}

    private static final Set<Material> MATERIALS = Set.of(
        Materials.InfusedAir,
        Materials.InfusedFire,
        Materials.InfusedEarth,
        Materials.InfusedWater);

    public static void run() {
        if (ProcessingDust.INSTANCE == null) return;
        for (Material material : MATERIALS) {
            ItemStack dust = MaterialLibAPI.getStack(material, Shapes.dust, 1);
            ProcessingDust.INSTANCE
                .registerOre(OrePrefixes.dust, material, OrePrefixes.dust.oreDictName(material), "gregtech", dust);
        }
    }
}
