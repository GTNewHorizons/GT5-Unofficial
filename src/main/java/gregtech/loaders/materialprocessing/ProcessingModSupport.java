package gregtech.loaders.materialprocessing;

import static gregtech.api.enums.ModIDs.*;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class ProcessingModSupport implements gregtech.api.interfaces.IMaterialHandler {

    public static boolean aTGregSupport = TinkersGregworks.isModLoaded() || NewHorizonsCoreMod.isModLoaded();
    public static boolean aEnableUBCMats = UndergroundBiomes.isModLoaded() || aTGregSupport;
    public static boolean aEnableThaumcraftMats = Thaumcraft.isModLoaded() || aTGregSupport;
    public static boolean aEnableRotaryCraftMats = RotaryCraft.isModLoaded() || aTGregSupport;
    public static boolean aEnableThermalFoundationMats = ThermalFondation.isModLoaded() || aTGregSupport;
    public static boolean aEnableEnderIOMats = EnderIO.isModLoaded() || aTGregSupport;
    public static boolean aEnableRailcraftMats = Railcraft.isModLoaded() || aTGregSupport;
    public static boolean aEnableGCMarsMats = GalacticraftMars.isModLoaded()|| aTGregSupport;
    public static boolean aEnableTwilightMats = TwilightForest.isModLoaded() || aTGregSupport;
    public static boolean aEnableMetallurgyMats = Metallurgy.isModLoaded() || aTGregSupport;
    public static boolean aEnableProjectRedMats = ProjectRedCore.isModLoaded() || aTGregSupport;

    public ProcessingModSupport() {
        Materials.add(this);
    }

    @Override
    public void onMaterialsInit() {
        // Disable Materials if Parent Mod is not loaded
        if (!aTGregSupport) {
            Materials.Dysprosium.mHasParentMod = false;
            Materials.Erbium.mHasParentMod = false;
            Materials.Gadolinium.mHasParentMod = false;
            Materials.Holmium.mHasParentMod = false;
            Materials.Indium.mHasParentMod = false;
            Materials.Lanthanum.mHasParentMod = false;
            Materials.Praseodymium.mHasParentMod = false;
            Materials.Promethium.mHasParentMod = false;
            Materials.Rubidium.mHasParentMod = false;
            Materials.Samarium.mHasParentMod = false;
            Materials.SamariumMagnetic.mHasParentMod = false;
            Materials.Scandium.mHasParentMod = false;
            Materials.Strontium.mHasParentMod = false;
            Materials.Tellurium.mHasParentMod = false;
            Materials.Terbium.mHasParentMod = false;
            Materials.Thulium.mHasParentMod = false;
            Materials.Ytterbium.mHasParentMod = false;
            Materials.HeeEndium.mHasParentMod = false;
            Materials.DarkIron.mHasParentMod = false;
            Materials.ElectrumFlux.mHasParentMod = false;
            Materials.Force.mHasParentMod = false;
            // Materials.Nikolite.mHasParentMod = false;
            Materials.Sunnarium.mHasParentMod = false;
            Materials.BlueAlloy.mHasParentMod = false;
            Materials.Vinteum.mHasParentMod = false;
            Materials.ChromiumDioxide.mHasParentMod = false;
        }
        if (!aEnableMetallurgyMats) {
            Materials.Angmallen.mHasParentMod = false;
            Materials.Atlarus.mHasParentMod = false;
            Materials.Carmot.mHasParentMod = false;
            Materials.Celenegil.mHasParentMod = false;
            Materials.Ceruclase.mHasParentMod = false;
            Materials.Eximite.mHasParentMod = false;
            Materials.Haderoth.mHasParentMod = false;
            Materials.Hepatizon.mHasParentMod = false;
            Materials.Ignatius.mHasParentMod = false;
            Materials.Infuscolium.mHasParentMod = false;
            Materials.Inolashite.mHasParentMod = false;
            Materials.Kalendrite.mHasParentMod = false;
            Materials.Lemurite.mHasParentMod = false;
            Materials.Meutoite.mHasParentMod = false;
            Materials.Orichalcum.mHasParentMod = false;
            Materials.Oureclase.mHasParentMod = false;
            Materials.Prometheum.mHasParentMod = false;
            Materials.Rubracium.mHasParentMod = false;
            Materials.Sanguinite.mHasParentMod = false;
            Materials.Tartarite.mHasParentMod = false;
            Materials.Vulcanite.mHasParentMod = false;
            Materials.Vyroxeres.mHasParentMod = false;
            Materials.DeepIron.mHasParentMod = false;
            Materials.ShadowIron.mHasParentMod = false;
            Materials.ShadowSteel.mHasParentMod = false;
            Materials.AstralSilver.mHasParentMod = false;
            Materials.Trinium.mHasParentMod = false;
        }
        if (!aEnableThaumcraftMats) {
            Materials.Amber.mHasParentMod = false;
            Materials.Thaumium.mHasParentMod = false;
            Materials.InfusedGold.mHasParentMod = false;
            Materials.InfusedAir.mHasParentMod = false;
            Materials.InfusedFire.mHasParentMod = false;
            Materials.InfusedEarth.mHasParentMod = false;
            Materials.InfusedWater.mHasParentMod = false;
            Materials.InfusedEntropy.mHasParentMod = false;
            Materials.InfusedOrder.mHasParentMod = false;
            Materials.InfusedVis.mHasParentMod = false;
            Materials.InfusedDull.mHasParentMod = false;
        }
        if (!aEnableUBCMats) {
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
        if (!aEnableTwilightMats) {
            Materials.FierySteel.mHasParentMod = false;
            Materials.LiveRoot.mHasParentMod = false;
            Materials.IronWood.mHasParentMod = false;
            Materials.Steeleaf.mHasParentMod = false;
            Materials.Knightmetal.mHasParentMod = false;
        }
        if (!aEnableGCMarsMats) {
            Materials.Desh.mHasParentMod = false;
            Materials.MeteoricIron.mHasParentMod = false;
            Materials.MeteoricSteel.mHasParentMod = false;
        }
        if (!aEnableThermalFoundationMats) {
            Materials.Blizz.mHasParentMod = false;
            Materials.Enderium.mHasParentMod = false;
        }
        if (!aEnableRotaryCraftMats) {
            Materials.HSLA.mHasParentMod = false;
        }
        if (!aEnableEnderIOMats) {
            Materials.DarkSteel.mHasParentMod = false;
        }
        if (!aEnableRailcraftMats) {
            Materials.Firestone.mHasParentMod = false;
        }
        if (!aEnableProjectRedMats) {
            Materials.Electrotine.mHasParentMod = false;
        }

        // Enable Materials if correct mod is Loaded
        Materials.ChromiumDioxide.mHasParentMod = Computronics.isModLoaded();
    }

    @Override
    public void onComponentInit() {
        if (Computronics.isModLoaded()) {
            OrePrefixes.ring.enableComponent(Materials.RedAlloy);
            OrePrefixes.ring.enableComponent(Materials.NiobiumTitanium);
            OrePrefixes.foil.enableComponent(Materials.StainlessSteel);
            OrePrefixes.foil.enableComponent(Materials.ChromiumDioxide);
            OrePrefixes.foil.enableComponent(Materials.Iron);
            OrePrefixes.plate.enableComponent(Materials.ChromiumDioxide);
            OrePrefixes.screw.enableComponent(Materials.Copper);
        }
    }

    @Override
    public void onComponentIteration(Materials aMaterial) {
        // NOP
    }
}
