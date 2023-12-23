package gregtech.loaders.materialprocessing;

import static gregtech.api.enums.Mods.Metallurgy;
import static gregtech.api.enums.Mods.RotaryCraft;
import static gregtech.api.enums.Mods.UndergroundBiomes;

import gregtech.api.enums.Materials;

public class ProcessingModSupport implements gregtech.api.interfaces.IMaterialHandler {

    public static boolean aEnableThaumcraftMats = true;

    public ProcessingModSupport() {
        Materials.add(this);
    }

    @Override
    public void onMaterialsInit() {
        // Disable Materials if Parent Mod is not loaded
        if (!Metallurgy.isModLoaded()) {
            Materials.Angmallen.mHasParentMod = false;
            Materials.Atlarus.mHasParentMod = false;
            Materials.Carmot.mHasParentMod = false;
            Materials.Celenegil.mHasParentMod = false;
            Materials.Eximite.mHasParentMod = false;
            Materials.Haderoth.mHasParentMod = false;
            Materials.Hepatizon.mHasParentMod = false;
            Materials.Ignatius.mHasParentMod = false;
            Materials.Infuscolium.mHasParentMod = false;
            Materials.Inolashite.mHasParentMod = false;
            Materials.Kalendrite.mHasParentMod = false;
            Materials.Lemurite.mHasParentMod = false;
            Materials.Meutoite.mHasParentMod = false;
            Materials.Oureclase.mHasParentMod = false;
            Materials.Prometheum.mHasParentMod = false;
            Materials.Sanguinite.mHasParentMod = false;
        }

        if (!UndergroundBiomes.isModLoaded()) {
            Materials.Blueschist.mHasParentMod = false;
            Materials.Chert.mHasParentMod = false;
            Materials.Dacite.mHasParentMod = false;
            Materials.Eclogite.mHasParentMod = false;
            Materials.Gabbro.mHasParentMod = false;
            Materials.Gneiss.mHasParentMod = false;
            Materials.Greenschist.mHasParentMod = false;
            Materials.Greywacke.mHasParentMod = false;
            Materials.Komatiite.mHasParentMod = false;
            Materials.Rhyolite.mHasParentMod = false;
        }
        if (!RotaryCraft.isModLoaded()) {
            Materials.HSLA.mHasParentMod = false;
        }

    }
}
