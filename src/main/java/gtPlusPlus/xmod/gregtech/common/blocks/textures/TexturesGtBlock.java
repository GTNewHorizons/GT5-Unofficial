package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.config.Configuration;

public class TexturesGtBlock {

    private static final boolean mAnimated = Configuration.visual.enableAnimatedTextures;

    /*
     * Handles Custom Textures.
     */

    public static class CustomIcon implements IIconContainer, Runnable {

        protected IIcon mIcon;
        protected String mIconName;
        protected String mModID;

        public CustomIcon(final String aIconName) {
            this(GTPlusPlus.ID, aIconName);
        }

        public CustomIcon(final String aModID, final String aIconName) {
            this.mIconName = aIconName;
            this.mModID = aModID;
            Logger.WARNING("Constructing a Custom Texture. " + this.mIconName);
            GregTechAPI.sGTBlockIconload.add(this);
        }

        @Override
        public IIcon getIcon() {
            return this.mIcon;
        }

        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public void run() {
            this.mIcon = GregTechAPI.sBlockIcons.registerIcon(this.mModID + ":" + this.mIconName);
            Logger.WARNING(
                "FIND ME _ Processing texture: " + this.getTextureFile()
                    .getResourcePath());
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    /*
     * Add Some Custom Textures below. I am not sure whether I need to declare them as such, but better to be safe than
     * sorry. Right?
     */

    // Machine Casings
    // Simple
    public static final CustomIcon Casing_Machine_Simple_Top = new CustomIcon("TileEntities/machine_top");
    public static final CustomIcon Casing_Machine_Simple_Bottom = new CustomIcon("TileEntities/machine_bottom");
    // Advanced and Ultra
    public static final CustomIcon Casing_Machine_Advanced = new CustomIcon("TileEntities/high_adv_machine");
    public static final CustomIcon Casing_Machine_Ultra = new CustomIcon("TileEntities/adv_machine_lesu");
    // Dimensional - Non Overlay
    public static final CustomIcon Casing_Machine_Dimensional = new CustomIcon("TileEntities/adv_machine_dimensional");

    // Material Casings
    public static final CustomIcon Casing_Material_Tantalloy61 = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_TANTALLOY61");
    public static final CustomIcon Casing_Material_MaragingSteel = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_MARAGINGSTEEL");
    public static final CustomIcon Casing_Material_Stellite = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_STELLITE");
    public static final CustomIcon Casing_Material_Talonite = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_TALONITE");
    public static final CustomIcon Turbine_SC_Material_Casing = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE");
    public static final CustomIcon Casing_Material_Tumbaga = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_TUMBAGA");
    public static final CustomIcon Casing_Material_Zeron100 = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_ZERON100");
    public static final CustomIcon Casing_Material_Potin = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_POTIN");

    public static final CustomIcon Casing_Material_Grisium = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_GRISIUM");
    public static final CustomIcon Casing_Material_RedSteel = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_RED_STEEL");
    public static final CustomIcon Casing_Material_ZirconiumCarbide = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_ZIRCONIUM_CARBIDE");
    public static final CustomIcon Casing_Material_HastelloyX = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_HASTELLOY_X");
    public static final CustomIcon Casing_Material_HastelloyN = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_HASTELLOY_N");
    public static final CustomIcon Casing_Material_Fluid_IncoloyDS = new CustomIcon(
        "TileEntities/MACHINE_CASING_FLUID_INCOLOY_DS");
    public static final CustomIcon Casing_Material_Laurenium = new CustomIcon("TileEntities/MACHINE_CASING_LAURENIUM");
    public static final CustomIcon Casing_Material_Botmium = new CustomIcon("TileEntities/MACHINE_CASING_BOTMIUM");

    // Trinium Alloy
    public static final CustomIcon Casing_Trinium_Naquadah_Vent = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_TRINIUM_NAQUADAH_VENT");

    // Material Machine/Firebox Casings
    public static final CustomIcon Casing_Staballoy_Firebox = new CustomIcon(
        "TileEntities/MACHINE_CASING_FIREBOX_STABALLOY");

    // Misc Casings
    public static final CustomIcon Casing_Machine_Redstone_Off = new CustomIcon(
        "TileEntities/cover_redstone_conductor");
    public static final CustomIcon Casing_Machine_Redstone_On = new CustomIcon("TileEntities/cover_redstone_emitter");

    // Special Block 2
    public static final CustomIcon Casing_Resonance_1 = new CustomIcon("special/block_1");
    public static final CustomIcon Casing_Resonance_2 = new CustomIcon("special/block_2");
    public static final CustomIcon Casing_Resonance_3 = new CustomIcon("special/block_3");
    public static final CustomIcon Casing_Resonance_4 = new CustomIcon("special/block_4");
    public static final CustomIcon Casing_Modulator_1 = new CustomIcon("special/block_5");
    public static final CustomIcon Casing_Modulator_2 = new CustomIcon("special/block_6");
    public static final CustomIcon Casing_Modulator_3 = new CustomIcon("special/block_7");
    public static final CustomIcon Casing_Modulator_4 = new CustomIcon("special/block_8");

    // Centrifuge Casing
    public static final CustomIcon Casing_Material_Centrifuge = new CustomIcon(
        "TileEntities/MACHINE_CASING_CENTRIFUGE");

    // Sterile Casing
    public static final CustomIcon Sterile_Casing = new CustomIcon("TileEntities/sterileCasing");

    public static final CustomIcon TEXTURE_CASING_AMAZON = new CustomIcon("TileEntities/CASING_AMAZON");
    public static final CustomIcon TEXTURE_CASING_ADVANCED_CRYOGENIC = new CustomIcon(
        "TileEntities/MACHINE_CASING_ADVANCED_CRYOGENIC");
    public static final CustomIcon TEXTURE_CASING_ADVANCED_VOLCNUS = new CustomIcon(
        "TileEntities/MACHINE_CASING_ADVANCED_VOLCANUS");
    public static final CustomIcon TEXTURE_CASING_ROCKETDYNE = new CustomIcon("TileEntities/MACHINE_CASING_ROCKETDYNE");
    public static final CustomIcon TEXTURE_CASING_GRINDING_MILL = new CustomIcon(
        "TileEntities/MACHINE_CASING_GRINDING_FACTORY");
    public static final CustomIcon TEXTURE_CASING_FLOTATION = new CustomIcon("TileEntities/MACHINE_CASING_FLOTATION");

    // Custom Pipes
    public static final CustomIcon TEXTURE_PIPE_GRINDING_MILL = new CustomIcon("TileEntities/MACHINE_CASING_PIPE_T1");
    public static final CustomIcon TEXTURE_PIPE_GENERIC = new CustomIcon("iconsets/MACHINE_CASING_PIPE_GENERIC");

    // Custom Gearboxes
    public static final CustomIcon TEXTURE_GEARBOX_GRINDING_MILL = new CustomIcon(
        "TileEntities/MACHINE_CASING_GEARBOX_T1");
    public static final CustomIcon TEXTURE_GEARBOX_GENERIC = new CustomIcon("iconsets/MACHINE_CASING_GEARBOX_GENERIC");

    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II = new CustomIcon("iconsets/MACHINE_CASING_FUSION_3");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_INNER = new CustomIcon(
        "iconsets/MACHINE_CASING_FUSION_COIL_II");
    public static final CustomIcon TEXTURE_CASING_FUSION_CASING_ULTRA = new CustomIcon(
        "iconsets/MACHINE_CASING_FUSION_GLASS_ULTRA");

    public static final CustomIcon TEXTURE_CASING_FUSION_4 = new CustomIcon("iconsets/MACHINE_CASING_FUSION_4");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_4 = new CustomIcon(
        "iconsets/MACHINE_CASING_FUSION_COIL_III");
    public static final CustomIcon TEXTURE_CASING_FUSION_OVERLAY = new CustomIcon(
        "iconsets/MACHINE_CASING_FUSION_GLASS_HYPER");

    // Overlays
    // Fan Textures
    public static final CustomIcon Overlay_Machine_Vent = new CustomIcon("TileEntities/machine_top_vent_rotating");
    public static final CustomIcon Overlay_Machine_Vent_Fast = new CustomIcon(
        "TileEntities/machine_top_vent_rotating_fast");
    // Diesel Engines
    public static final CustomIcon Overlay_Machine_Diesel_Vertical = new CustomIcon(
        "TileEntities/machine_top_dieselmotor");
    public static final CustomIcon Overlay_Machine_Diesel_Horizontal = new CustomIcon(
        "TileEntities/machine_top_dieselmotor2");
    public static final CustomIcon Overlay_Machine_Diesel_Vertical_Active = new CustomIcon(
        "TileEntities/machine_top_dieselmotor_active");
    public static final CustomIcon Overlay_Machine_Diesel_Horizontal_Active = new CustomIcon(
        "TileEntities/machine_top_dieselmotor2_active");
    // Computer Screens
    public static final CustomIcon Casing_Machine_Screen_1 = new CustomIcon("TileEntities/adv_machine_screen_random1");
    public static final CustomIcon Casing_Machine_Screen_2 = new CustomIcon("TileEntities/adv_machine_screen_random2");
    public static final CustomIcon Casing_Machine_Screen_3 = new CustomIcon("TileEntities/adv_machine_screen_random3");
    public static final CustomIcon Casing_Machine_Screen_Inactive = new CustomIcon(
        "TileEntities/adv_machine_screen_inactive");

    public static final CustomIcon Casing_Machine_Screen_Rainbow = new CustomIcon("TileEntities/overlay_rainbowscreen");
    public static final CustomIcon Casing_Machine_Screen_Frequency = new CustomIcon(
        "TileEntities/adv_machine_screen_frequency");
    public static final CustomIcon Overlay_Machine_Screen_Logo = new CustomIcon("TileEntities/adv_machine_screen_logo");

    // Machine Controller Overlays

    // oMCD = Overlay_Machine_Controller_Default
    public static final CustomIcon oMCDSolarTower = new CustomIcon("iconsets/controllerFaces/solarTower");
    public static final CustomIcon oMCDSolarTowerActive = new CustomIcon("iconsets/controllerFaces/solarTowerActive");

    public static final CustomIcon oMCDIndustrialWireMill = new CustomIcon(
        "iconsets/controllerFaces/industrialWiremill");
    public static final CustomIcon oMCDIndustrialWireMillGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialWiremillGlow");
    public static final CustomIcon oMCDIndustrialWireMillActive = new CustomIcon(
        "iconsets/controllerFaces/industrialWiremillActive");
    public static final CustomIcon oMCDIndustrialWireMillActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialWiremillActiveGlow");

    public static final CustomIcon oMCDIndustrialSifter = new CustomIcon("iconsets/controllerFaces/industrialSifter");
    public static final CustomIcon oMCDIndustrialSifterGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialSifterGlow");
    public static final CustomIcon oMCDIndustrialSifterActive = new CustomIcon(
        "iconsets/controllerFaces/industrialSifterActive");
    public static final CustomIcon oMCDIndustrialSifterActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialSifterActiveGlow");

    public static final CustomIcon oMCDAlgaePondBase = new CustomIcon("iconsets/controllerFaces/algaePondBase");
    public static final CustomIcon oMCDAlgaePondBaseGlow = new CustomIcon("iconsets/controllerFaces/algaePondBaseGlow");
    public static final CustomIcon oMCDAlgaePondBaseActive = new CustomIcon(
        "iconsets/controllerFaces/algaePondBaseActive");
    public static final CustomIcon oMCDAlgaePondBaseActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/algaePondBaseActiveGlow");

    public static final CustomIcon oMCDIndustrialMixer = new CustomIcon("iconsets/controllerFaces/industrialMixer");
    public static final CustomIcon oMCDIndustrialMixerGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialMixerGlow");
    public static final CustomIcon oMCDIndustrialMixerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialMixerActive");
    public static final CustomIcon oMCDIndustrialMixerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialMixerActiveGlow");

    public static final CustomIcon oMCDIndustrialThermalCentrifuge = new CustomIcon(
        "iconsets/controllerFaces/industrialThermalCentrifuge");
    public static final CustomIcon oMCDIndustrialThermalCentrifugeGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialThermalCentrifugeGlow");
    public static final CustomIcon oMCDIndustrialThermalCentrifugeActive = new CustomIcon(
        "iconsets/controllerFaces/industrialThermalCentrifugeActive");
    public static final CustomIcon oMCDIndustrialThermalCentrifugeActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialThermalCentrifugeActiveGlow");

    public static final CustomIcon oMCDIndustrialExtruder = new CustomIcon(
        "iconsets/controllerFaces/industrialExtruder");
    public static final CustomIcon oMCDIndustrialExtruderGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialExtruderGlow");
    public static final CustomIcon oMCDIndustrialExtruderActive = new CustomIcon(
        "iconsets/controllerFaces/industrialExtruderActive");
    public static final CustomIcon oMCDIndustrialExtruderActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialExtruderActiveGlow");

    public static final CustomIcon oMCDIndustrialWashPlant = new CustomIcon(
        "iconsets/controllerFaces/industrialWashPlant");
    public static final CustomIcon oMCDIndustrialWashPlantGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialWashPlantGlow");
    public static final CustomIcon oMCDIndustrialWashPlantActive = new CustomIcon(
        "iconsets/controllerFaces/industrialWashPlantActive");
    public static final CustomIcon oMCDIndustrialWashPlantActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialWashPlantActiveGlow");

    public static final CustomIcon oMCDAlloyBlastSmelter = new CustomIcon("iconsets/controllerFaces/alloyBlastSmelter");
    public static final CustomIcon oMCDAlloyBlastSmelterGlow = new CustomIcon(
        "iconsets/controllerFaces/alloyBlastSmelterGlow");
    public static final CustomIcon oMCDAlloyBlastSmelterActive = new CustomIcon(
        "iconsets/controllerFaces/alloyBlastSmelterActive");
    public static final CustomIcon oMCDAlloyBlastSmelterActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/alloyBlastSmelterActiveGlow");

    public static final CustomIcon oMCDIndustrialArcFurnace = new CustomIcon(
        "iconsets/controllerFaces/industrialArcFurnace");
    public static final CustomIcon oMCDIndustrialArcFurnaceGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialArcFurnaceGlow");
    public static final CustomIcon oMCDIndustrialArcFurnaceActive = new CustomIcon(
        "iconsets/controllerFaces/industrialArcFurnaceActive");
    public static final CustomIcon oMCDIndustrialArcFurnaceActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialArcFurnaceActiveGlow");

    public static final CustomIcon oMCDIndustrialCuttingMachine = new CustomIcon(
        "iconsets/controllerFaces/industrialCuttingMachine");
    public static final CustomIcon oMCDIndustrialCuttingMachineGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialCuttingMachineGlow");
    public static final CustomIcon oMCDIndustrialCuttingMachineActive = new CustomIcon(
        "iconsets/controllerFaces/industrialCuttingMachineActive");
    public static final CustomIcon oMCDIndustrialCuttingMachineActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialCuttingMachineActiveGlow");

    public static final CustomIcon oMCDIndustrialPlatePress = new CustomIcon(
        "iconsets/controllerFaces/industrialPlatePress");
    public static final CustomIcon oMCDIndustrialPlatePressGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialPlatePressGlow");
    public static final CustomIcon oMCDIndustrialPlatePressActive = new CustomIcon(
        "iconsets/controllerFaces/industrialPlatePressActive");
    public static final CustomIcon oMCDIndustrialPlatePressActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialPlatePressActiveGlow");

    public static final CustomIcon oMCDIndustrialElectrolyzer = new CustomIcon(
        "iconsets/controllerFaces/industrialElectrolyzer");
    public static final CustomIcon oMCDIndustrialElectrolyzerGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialElectrolyzerGlow");
    public static final CustomIcon oMCDIndustrialElectrolyzerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialElectrolyzerActive");
    public static final CustomIcon oMCDIndustrialElectrolyzerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialElectrolyzerActiveGlow");

    public static final CustomIcon oMCDFrothFlotationCell = new CustomIcon(
        "iconsets/controllerFaces/frothFlotationCell");
    public static final CustomIcon oMCDFrothFlotationCellGlow = new CustomIcon(
        "iconsets/controllerFaces/frothFlotationCellGlow");
    public static final CustomIcon oMCDFrothFlotationCellActive = new CustomIcon(
        "iconsets/controllerFaces/frothFlotationCellActive");
    public static final CustomIcon oMCDFrothFlotationCellActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/frothFlotationCellActiveGlow");

    public static final CustomIcon Overlay_Machine_Controller_Advanced = new CustomIcon(
        "iconsets/OVERLAY_FRONT_ADVANCED_MULTIBLOCK_ANIMATED");
    public static final CustomIcon Overlay_Machine_Controller_Advanced_Active = new CustomIcon(
        "iconsets/OVERLAY_FRONT_ADVANCED_MULTIBLOCK_ANIMATED_ACTIVE");

    // oMCA = Overlay_Machine_Controller_Advanced
    public static final CustomIcon oMCACokeOven = new CustomIcon("iconsets/controllerFaces/cokeOven");
    public static final CustomIcon oMCACokeOvenGlow = new CustomIcon("iconsets/controllerFaces/cokeOvenGlow");
    public static final CustomIcon oMCACokeOvenActive = new CustomIcon("iconsets/controllerFaces/cokeOvenActive");
    public static final CustomIcon oMCACokeOvenActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/cokeOvenActiveGlow");

    public static final CustomIcon oMCAChemicalPlant = new CustomIcon("iconsets/controllerFaces/chemicalPlant");
    public static final CustomIcon oMCAChemicalPlantGlow = new CustomIcon("iconsets/controllerFaces/chemicalPlantGlow");
    public static final CustomIcon oMCAChemicalPlantActive = new CustomIcon(
        "iconsets/controllerFaces/chemicalPlantActive");
    public static final CustomIcon oMCAChemicalPlantActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/chemicalPlantActiveGlow");

    public static final CustomIcon oMCAMegaAlloyBlastSmelter = new CustomIcon(
        "iconsets/controllerFaces/megaAlloyBlastSmelter");
    public static final CustomIcon oMCAMegaAlloyBlastSmelterGlow = new CustomIcon(
        "iconsets/controllerFaces/megaAlloyBlastSmelterGlow");
    public static final CustomIcon oMCAMegaAlloyBlastSmelterActive = new CustomIcon(
        "iconsets/controllerFaces/megaAlloyBlastSmelterActive");
    public static final CustomIcon oMCAMegaAlloyBlastSmelterActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/megaAlloyBlastSmelterActiveGlow");

    public static final CustomIcon oMCATreeFarm = new CustomIcon("iconsets/controllerFaces/treeFarm");
    public static final CustomIcon oMCATreeFarmActive = new CustomIcon("iconsets/controllerFaces/treeFarmActive");

    public static final CustomIcon oMCAIndustrialRockBreaker = new CustomIcon(
        "iconsets/controllerFaces/industrialRockBreaker");
    public static final CustomIcon oMCAIndustrialRockBreakerGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialRockBreakerGlow");
    public static final CustomIcon oMCAIndustrialRockBreakerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialRockBreakerActive");
    public static final CustomIcon oMCAIndustrialRockBreakerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialRockBreakerActiveGlow");

    public static final CustomIcon oMCAAdvancedHeatExchanger = new CustomIcon(
        "iconsets/controllerFaces/advancedHeatExchanger");
    public static final CustomIcon oMCAAdvancedHeatExchangerGlow = new CustomIcon(
        "iconsets/controllerFaces/advancedHeatExchangerGlow");
    public static final CustomIcon oMCAAdvancedHeatExchangerActive = new CustomIcon(
        "iconsets/controllerFaces/advancedHeatExchangerActive");
    public static final CustomIcon oMCAAdvancedHeatExchangerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/advancedHeatExchangerActiveGlow");

    public static final CustomIcon oMCALargeRocketEngine = new CustomIcon("iconsets/controllerFaces/largeRocketEngine");
    public static final CustomIcon oMCALargeRocketEngineGlow = new CustomIcon(
        "iconsets/controllerFaces/largeRocketEngineGlow");
    public static final CustomIcon oMCALargeRocketEngineActive = new CustomIcon(
        "iconsets/controllerFaces/largeRocketEngineActive");
    public static final CustomIcon oMCALargeRocketEngineActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/largeRocketEngineActiveGlow");

    public static final CustomIcon oMCAIndustrialChisel = new CustomIcon("iconsets/controllerFaces/industrialChisel");
    public static final CustomIcon oMCAIndustrialChiselGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialChiselGlow");
    public static final CustomIcon oMCAIndustrialChiselActive = new CustomIcon(
        "iconsets/controllerFaces/industrialChiselActive");
    public static final CustomIcon oMCAIndustrialChiselActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialChiselActiveGlow");

    public static final CustomIcon oMCAIndustrialMolecularTransformer = new CustomIcon(
        "iconsets/controllerFaces/industrialMolecularTransformer");
    public static final CustomIcon oMCAIndustrialMolecularTransformerGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialMolecularTransformerGlow");
    public static final CustomIcon oMCAIndustrialMolecularTransformerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialMolecularTransformerActive");
    public static final CustomIcon oMCAIndustrialMolecularTransformerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialMolecularTransformerActiveGlow");

    public static final CustomIcon oMCAElementalDuplicator = new CustomIcon(
        "iconsets/controllerFaces/elementalDuplicator");
    public static final CustomIcon oMCAElementalDuplicatorGlow = new CustomIcon(
        "iconsets/controllerFaces/elementalDuplicatorGlow");
    public static final CustomIcon oMCAElementalDuplicatorActive = new CustomIcon(
        "iconsets/controllerFaces/elementalDuplicatorActive");
    public static final CustomIcon oMCAElementalDuplicatorActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/elementalDuplicatorActiveGlow");

    public static final CustomIcon oMCAFluidHeater = new CustomIcon("iconsets/controllerFaces/fluidHeater");
    public static final CustomIcon oMCAFluidHeaterGlow = new CustomIcon("iconsets/controllerFaces/fluidHeaterGlow");
    public static final CustomIcon oMCAFluidHeaterActive = new CustomIcon("iconsets/controllerFaces/fluidHeaterActive");
    public static final CustomIcon oMCAFluidHeaterActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/fluidHeaterActiveGlow");

    public static final CustomIcon oMCAAmazonPackager = new CustomIcon("iconsets/controllerFaces/amazonPackager");
    public static final CustomIcon oMCAAmazonPackagerGlow = new CustomIcon(
        "iconsets/controllerFaces/amazonPackagerGlow");
    public static final CustomIcon oMCAAmazonPackagerActive = new CustomIcon(
        "iconsets/controllerFaces/amazonPackagerActive");
    public static final CustomIcon oMCAAmazonPackagerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/amazonPackagerActiveGlow");

    public static final CustomIcon oMCAIndustrialDehydrator = new CustomIcon(
        "iconsets/controllerFaces/industrialDehydrator");
    public static final CustomIcon oMCAIndustrialDehydratorGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialDehydratorGlow");
    public static final CustomIcon oMCAIndustrialDehydratorActive = new CustomIcon(
        "iconsets/controllerFaces/industrialDehydratorActive");
    public static final CustomIcon oMCAIndustrialDehydratorActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialDehydratorActiveGlow");

    public static final CustomIcon oMCAIndustrialForgeHammer = new CustomIcon(
        "iconsets/controllerFaces/industrialForgeHammer");
    public static final CustomIcon oMCAIndustrialForgeHammerGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialForgeHammerGlow");
    public static final CustomIcon oMCAIndustrialForgeHammerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialForgeHammerActive");
    public static final CustomIcon oMCAIndustrialForgeHammerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialForgeHammerActiveGlow");

    public static final CustomIcon oMCAAdvancedEBF = new CustomIcon("iconsets/controllerFaces/advancedEBF");
    public static final CustomIcon oMCAAdvancedEBFGlow = new CustomIcon("iconsets/controllerFaces/advancedEBFGlow");
    public static final CustomIcon oMCAAdvancedEBFActive = new CustomIcon("iconsets/controllerFaces/advancedEBFActive");
    public static final CustomIcon oMCAAdvancedEBFActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/advancedEBFActiveGlow");

    public static final CustomIcon oMCASpargeTower = new CustomIcon("iconsets/controllerFaces/spargeTower");
    public static final CustomIcon oMCASpargeTowerGlow = new CustomIcon("iconsets/controllerFaces/spargeTowerGlow");
    public static final CustomIcon oMCASpargeTowerActive = new CustomIcon("iconsets/controllerFaces/spargeTowerActive");
    public static final CustomIcon oMCASpargeTowerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/spargeTowerActiveGlow");

    public static final CustomIcon oMCAIndustrialVacuumFreezer = new CustomIcon(
        "iconsets/controllerFaces/industrialVacuumFreezer");
    public static final CustomIcon oMCAIndustrialVacuumFreezerGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialVacuumFreezerGlow");
    public static final CustomIcon oMCAIndustrialVacuumFreezerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialVacuumFreezerActive");
    public static final CustomIcon oMCAIndustrialVacuumFreezerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/industrialVacuumFreezerActiveGlow");

    public static final CustomIcon oMCAThermalBoiler = new CustomIcon("iconsets/controllerFaces/thermalBoiler");
    public static final CustomIcon oMCAThermalBoilerGlow = new CustomIcon("iconsets/controllerFaces/thermalBoilerGlow");
    public static final CustomIcon oMCAThermalBoilerActive = new CustomIcon(
        "iconsets/controllerFaces/thermalBoilerActive");
    public static final CustomIcon oMCAThermalBoilerActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/thermalBoilerActiveGlow");

    public static final CustomIcon oMCAQFT = new CustomIcon("iconsets/controllerFaces/quantumForceTransformer");
    public static final CustomIcon oMCAQFTGlow = new CustomIcon("iconsets/controllerFaces/quantumForceTransformerGlow");
    public static final CustomIcon oMCAQFTActive = new CustomIcon(
        "iconsets/controllerFaces/quantumForceTransformerActive");
    public static final CustomIcon oMCAQFTActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/quantumForceTransformerActiveGlow");

    public static final CustomIcon oMCAAdvancedImplosion = new CustomIcon("iconsets/controllerFaces/advancedImplosion");
    public static final CustomIcon oMCAAdvancedImplosionGlow = new CustomIcon(
        "iconsets/controllerFaces/advancedImplosionGlow");
    public static final CustomIcon oMCAAdvancedImplosionActive = new CustomIcon(
        "iconsets/controllerFaces/advancedImplosionActive");
    public static final CustomIcon oMCAAdvancedImplosionActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/advancedImplosionActiveGlow");

    public static final CustomIcon oMCALargeFluidExtractor = new CustomIcon(
        "iconsets/controllerFaces/largeFluidExtractor");
    public static final CustomIcon oMCALargeFluidExtractorGlow = new CustomIcon(
        "iconsets/controllerFaces/largeFluidExtractorGlow");
    public static final CustomIcon oMCALargeFluidExtractorActive = new CustomIcon(
        "iconsets/controllerFaces/largeFluidExtractorActive");
    public static final CustomIcon oMCALargeFluidExtractorActiveGlow = new CustomIcon(
        "iconsets/controllerFaces/largeFluidExtractorActiveGlow");

    // Crafting Overlays
    public static final CustomIcon Casing_Adv_Workbench_Crafting_Overlay = new CustomIcon(
        "TileEntities/gt4/machine_top_crafting");

    // Covers
    public static final CustomIcon Overlay_Overflow_Valve = new CustomIcon("iconsets/OVERLAY_OVERFLOW_VALVE");

    // Hatch Overlays
    // Charger Texture
    public static final CustomIcon Overlay_Hatch_Charger = new CustomIcon("TileEntities/cover_charger");
    // Discharger Texture
    public static final CustomIcon Overlay_Hatch_Discharger = new CustomIcon("TileEntities/cover_discharge");
    // Advanced Muffler
    public static final CustomIcon Overlay_Hatch_Muffler_Adv = new CustomIcon("iconsets/OVERLAY_MUFFLER_ADV");
    // Milling Ball Bus
    public static final CustomIcon Overlay_Bus_Milling_Balls = new CustomIcon("iconsets/OVERLAY_MILLING_BALL_BUS");
    // Catalyst Bus
    public static final CustomIcon Overlay_Bus_Catalyst = new CustomIcon("iconsets/OVERLAY_CATALYSTS");
    // Data Orb Hatch
    public static final CustomIcon Overlay_Hatch_Data_Orb = new CustomIcon("iconsets/OVERLAY_DATA_ORB");

    // Dimensional
    public static final CustomIcon Overlay_Machine_Dimensional_Orange = new CustomIcon(
        "TileEntities/adv_machine_dimensional_cover_orange");

    // Icons
    public static final CustomIcon Overlay_MatterFab = new CustomIcon("TileEntities/adv_machine_matterfab");
    public static final CustomIcon Overlay_MatterFab_Glow = new CustomIcon("TileEntities/adv_machine_matterfab_glow");
    public static final CustomIcon Overlay_MatterFab_Active = new CustomIcon(
        "TileEntities/adv_machine_matterfab_active");
    public static final CustomIcon Overlay_MatterFab_Active_Glow = new CustomIcon(
        "TileEntities/adv_machine_matterfab_active_glow");

    public static final CustomIcon Overlay_MatterFab_Animated = new CustomIcon(
        "TileEntities/adv_machine_matterfab_animated");
    public static final CustomIcon Overlay_MatterFab_Animated_Glow = new CustomIcon(
        "TileEntities/adv_machine_matterfab_animated_glow");
    public static final CustomIcon Overlay_MatterFab_Active_Animated = new CustomIcon(
        "TileEntities/adv_machine_matterfab_active_animated");
    public static final CustomIcon Overlay_MatterFab_Active_Animated_Glow = new CustomIcon(
        "TileEntities/adv_machine_matterfab_active_animated_glow");

    public static final CustomIcon Overlay_Water = new CustomIcon("TileEntities/adv_machine_water");
    public static final CustomIcon Overlay_UU_Matter = new CustomIcon("TileEntities/adv_machine_uum");

}
