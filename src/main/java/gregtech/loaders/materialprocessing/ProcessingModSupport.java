package gregtech.loaders.materialprocessing;

import static gregtech.api.enums.Mods.RotaryCraft;

import gregtech.api.enums.Materials;

public class ProcessingModSupport implements gregtech.api.interfaces.IMaterialHandler {

    public static boolean aEnableThaumcraftMats = true;

    public ProcessingModSupport() {
        Materials.add(this);
    }

    @Override
    public void onMaterialsInit() {
        // Disable Materials if Parent Mod is not loaded
        if (!RotaryCraft.isModLoaded()) {
            Materials.HSLA.mHasParentMod = false;
        }

    }
}
