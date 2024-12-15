package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GTRenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.config.Configuration;

public class TexturesGtBlock {

    private static final boolean mAnimated = Configuration.visual.enableAnimatedTextures;
    private static final ArrayList<Runnable> mCustomiconMap = new ArrayList<>();

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
            mCustomiconMap.add(this);
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
     * Add Some Custom Textures below. I am not sure whether I need to declare them as such, but better to be
     * safe than sorry. Right?
     */

    // PlaceHolder Texture
    public static final CustomIcon _PlaceHolder = new CustomIcon("TileEntities/_PlaceHolder");
    public static final CustomIcon OVERLAY_ENERGY_OUT_MULTI_BUFFER = new CustomIcon(
        "iconsets/OVERLAY_ENERGY_OUT_MULTI_BUFFER");

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

    // Trinium Alloys
    public static final CustomIcon Casing_Trinium_Titanium = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_TRINIUM_TITANIUM");
    public static final CustomIcon Casing_Trinium_Naquadah_Vent = new CustomIcon(
        "TileEntities/MACHINE_CASING_STABLE_TRINIUM_NAQUADAH_VENT");

    // Material Machine/Firebox Casings
    public static final CustomIcon Casing_Staballoy_Firebox = new CustomIcon(
        "TileEntities/MACHINE_CASING_FIREBOX_STABALLOY");

    // Misc Casings
    public static final CustomIcon Casing_Machine_Redstone_Off = new CustomIcon(
        "TileEntities/cover_redstone_conductor");
    public static final CustomIcon Casing_Machine_Redstone_On = new CustomIcon("TileEntities/cover_redstone_emitter");

    // Redox Cells
    public static final CustomIcon Casing_Redox_1 = new CustomIcon("redox/redox1");
    public static final CustomIcon Casing_Redox_2 = new CustomIcon("redox/redox2");
    public static final CustomIcon Casing_Redox_3 = new CustomIcon("redox/redox3");
    public static final CustomIcon Casing_Redox_4 = new CustomIcon("redox/redox4");
    public static final CustomIcon Casing_Redox_5 = new CustomIcon("redox/redox5");
    public static final CustomIcon Casing_Redox_6 = new CustomIcon("redox/redox6");

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

    // Quantum Force Transformer Casing
    // spotless:off
    public static final CustomIcon Casing_Coil_QFT = new CustomIcon("TileEntities/MACHINE_CASING_QFT_COIL");
    public static final CustomIcon NeutronPulseManipulator = mAnimated ? new CustomIcon(
        "qft/NeutronPulseManipulator") : new CustomIcon("qft/NeutronPulseManipulatorStatic");
    public static final CustomIcon CosmicFabricManipulator = mAnimated ? new CustomIcon(
        "qft/CosmicFabricManipulator") : new CustomIcon("qft/CosmicFabricManipulatorStatic");
    public static final CustomIcon InfinityInfusedManipulator = mAnimated ? new CustomIcon(
        "qft/InfinityInfusedManipulator") : new CustomIcon("qft/InfinityInfusedManipulatorStatic");
    public static final CustomIcon SpaceTimeContinuumRipper = mAnimated ? new CustomIcon(
        "qft/SpaceTimeContinuumRipper") : new CustomIcon("qft/SpaceTimeContinuumRipperStatic");
    public static final CustomIcon Manipulator_Top = new CustomIcon("qft/Manipulator_Top");
    public static final CustomIcon NeutronShieldingCore = mAnimated ? new CustomIcon(
        "qft/NeutronShieldingCore") : new CustomIcon("qft/NeutronShieldingCoreStatic");
    public static final CustomIcon CosmicFabricShieldingCore = mAnimated ? new CustomIcon(
        "qft/CosmicFabricShieldingCore") : new CustomIcon("qft/CosmicFabricShieldingCoreStatic");
    public static final CustomIcon InfinityInfusedShieldingCore = mAnimated ? new CustomIcon(
        "qft/InfinityInfusedShieldingCore") : new CustomIcon("qft/InfinityInfusedShieldingCoreStatic");
    public static final CustomIcon SpaceTimeBendingCore = mAnimated ? new CustomIcon(
        "qft/SpaceTimeBendingCore") : new CustomIcon("qft/SpaceTimeBendingCoreStatic");
    public static final CustomIcon ForceFieldGlass = new CustomIcon("qft/ForceFieldGlass");
    public static final CustomIcon ForceField = new CustomIcon("qft/ForceField");
    public static final CustomIcon Blank = new CustomIcon("qft/blank");
    //spotless:on

    // MACHINE_CASING_FARM_MANAGER_STRUCTURAL
    // Farm Manager Casings
    public static final CustomIcon Casing_Machine_Farm_Manager = new CustomIcon(
        "TileEntities/MACHINE_CASING_FARM_MANAGER_STRUCTURAL");
    // Sterile Casing
    public static final CustomIcon Sterile_Casing = new CustomIcon("TileEntities/sterileCasing");
    // Podzol Top
    public static final CustomIcon Casing_Machine_Podzol = new CustomIcon("TileEntities/dirt_podzol_top");

    // Structural Blocks
    public static final CustomIcon Casing_Machine_Metal_Grate_A = new CustomIcon("chrono/MetalGrate");
    public static final CustomIcon Casing_Machine_Metal_Panel_A = new CustomIcon("chrono/MetalPanel");
    public static final CustomIcon Casing_Machine_Metal_Sheet_A = new CustomIcon("chrono/MetalSheet");
    public static final CustomIcon Casing_Machine_Metal_Sheet_H = new CustomIcon("chrono/MetalSheet8");
    public static final CustomIcon Casing_Machine_Metal_Sheet_I = new CustomIcon("chrono/MetalSheet9");
    public static final CustomIcon Overlay_Machine_Cyber_A = new CustomIcon("chrono/CyberPanel");

    public static final CustomIcon TEXTURE_CASING_AMAZON = new CustomIcon("TileEntities/CASING_AMAZON");
    public static final CustomIcon TEXTURE_CASING_ADVANCED_CRYOGENIC = new CustomIcon(
        "TileEntities/MACHINE_CASING_ADVANCED_CRYOGENIC");
    public static final CustomIcon TEXTURE_CASING_ADVANCED_VOLCNUS = new CustomIcon(
        "TileEntities/MACHINE_CASING_ADVANCED_VOLCANUS");
    public static final CustomIcon TEXTURE_CASING_ROCKETDYNE = new CustomIcon("TileEntities/MACHINE_CASING_ROCKETDYNE");
    public static final CustomIcon TEXTURE_CASING_GRINDING_MILL = new CustomIcon(
        "TileEntities/MACHINE_CASING_GRINDING_FACTORY");
    public static final CustomIcon TEXTURE_CASING_FLOTATION = new CustomIcon("TileEntities/MACHINE_CASING_FLOTATION");

    // Redstone blocks
    public static final CustomIcon Casing_Redstone_Top_Off = new CustomIcon(
        "TileEntities/gt4/redstone/machine_top_redstone_off");
    public static final CustomIcon Casing_Redstone_Top_Main_Off = new CustomIcon(
        "TileEntities/gt4/redstone/machine_top_redstone_main_off");
    public static final CustomIcon Casing_Redstone_Top_On = new CustomIcon(
        "TileEntities/gt4/redstone/machine_top_redstone_on");
    public static final CustomIcon Casing_Redstone_Top_Main_On = new CustomIcon(
        "TileEntities/gt4/redstone/machine_top_redstone_main_on");

    public static final CustomIcon Casing_Redstone_Side_Off = new CustomIcon(
        "TileEntities/gt4/redstone/machine_side_redstone_off");
    public static final CustomIcon Casing_Redstone_Side_Main_Off = new CustomIcon(
        "TileEntities/gt4/redstone/machine_side_redstone_main_off");
    public static final CustomIcon Casing_Redstone_Side_On = new CustomIcon(
        "TileEntities/gt4/redstone/machine_side_redstone_on");
    public static final CustomIcon Casing_Redstone_Side_Main_On = new CustomIcon(
        "TileEntities/gt4/redstone/machine_side_redstone_main_on");

    public static final CustomIcon Casing_Redstone_Bottom_Off = new CustomIcon(
        "TileEntities/gt4/redstone/machine_bottom_redstone_off");
    public static final CustomIcon Casing_Redstone_Bottom_Main_Off = new CustomIcon(
        "TileEntities/gt4/redstone/machine_bottom_redstone_main_off");
    public static final CustomIcon Casing_Redstone_Bottom_On = new CustomIcon(
        "TileEntities/gt4/redstone/machine_bottom_redstone_on");
    public static final CustomIcon Casing_Redstone_Bottom_Main_On = new CustomIcon(
        "TileEntities/gt4/redstone/machine_bottom_redstone_main_on");

    public static final CustomIcon Casing_InventoryManagaer_Red = new CustomIcon("TileEntities/gt4/OVERLAY_RED");
    public static final CustomIcon Casing_InventoryManagaer_Red_Redstone = new CustomIcon(
        "TileEntities/gt4/OVERLAY_RED_REDSTONE");

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

    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III = new CustomIcon("iconsets/MACHINE_CASING_FUSION_4");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_INNER = new CustomIcon(
        "iconsets/MACHINE_CASING_FUSION_COIL_III");
    public static final CustomIcon TEXTURE_CASING_FUSION_CASING_HYPER = new CustomIcon(
        "iconsets/MACHINE_CASING_FUSION_GLASS_HYPER");
    //
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_1 = new CustomIcon("iconsets/FUSIONIII_1");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_2 = new CustomIcon("iconsets/FUSIONIII_2");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_3 = new CustomIcon("iconsets/FUSIONIII_3");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_4 = new CustomIcon("iconsets/FUSIONIII_4");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_5 = new CustomIcon("iconsets/FUSIONIII_5");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_6 = new CustomIcon("iconsets/FUSIONIII_6");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_7 = new CustomIcon("iconsets/FUSIONIII_7");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_8 = new CustomIcon("iconsets/FUSIONIII_8");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_9 = new CustomIcon("iconsets/FUSIONIII_9");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_10 = new CustomIcon("iconsets/FUSIONIII_10");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_11 = new CustomIcon("iconsets/FUSIONIII_11");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_II_12 = new CustomIcon("iconsets/FUSIONIII_12");

    // MK5 Fusion casings
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_1 = new CustomIcon("iconsets/FUSIONIV_1");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_2 = new CustomIcon("iconsets/FUSIONIV_2");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_3 = new CustomIcon("iconsets/FUSIONIV_3");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_4 = new CustomIcon("iconsets/FUSIONIV_4");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_5 = new CustomIcon("iconsets/FUSIONIV_5");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_6 = new CustomIcon("iconsets/FUSIONIV_6");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_7 = new CustomIcon("iconsets/FUSIONIV_7");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_8 = new CustomIcon("iconsets/FUSIONIV_8");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_9 = new CustomIcon("iconsets/FUSIONIV_9");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_10 = new CustomIcon("iconsets/FUSIONIV_10");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_11 = new CustomIcon("iconsets/FUSIONIV_11");
    public static final CustomIcon TEXTURE_CASING_FUSION_COIL_III_12 = new CustomIcon("iconsets/FUSIONIV_12");

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
    public static final CustomIcon oMCDIndustrialWireMillActive = new CustomIcon(
        "iconsets/controllerFaces/industrialWiremillActive");

    public static final CustomIcon oMCDIndustrialSifter = new CustomIcon("iconsets/controllerFaces/industrialSifter");
    public static final CustomIcon oMCDIndustrialSifterActive = new CustomIcon(
        "iconsets/controllerFaces/industrialSifterActive");

    public static final CustomIcon oMCDAlgaePondBase = new CustomIcon("iconsets/controllerFaces/algaePondBase");
    public static final CustomIcon oMCDAlgaePondBaseActive = new CustomIcon(
        "iconsets/controllerFaces/algaePondBaseActive");

    public static final CustomIcon oMCDIndustrialMixer = new CustomIcon("iconsets/controllerFaces/industrialMixer");
    public static final CustomIcon oMCDIndustrialMixerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialMixerActive");

    public static final CustomIcon oMCDIndustrialThermalCentrifuge = new CustomIcon(
        "iconsets/controllerFaces/industrialThermalCentrifuge");
    public static final CustomIcon oMCDIndustrialThermalCentrifugeActive = new CustomIcon(
        "iconsets/controllerFaces/industrialThermalCentrifugeActive");

    public static final CustomIcon oMCDIndustrialExtruder = new CustomIcon(
        "iconsets/controllerFaces/industrialExtruder");
    public static final CustomIcon oMCDIndustrialExtruderActive = new CustomIcon(
        "iconsets/controllerFaces/industrialExtruderActive");

    public static final CustomIcon oMCDIndustrialWashPlant = new CustomIcon(
        "iconsets/controllerFaces/industrialWashPlant");
    public static final CustomIcon oMCDIndustrialWashPlantActive = new CustomIcon(
        "iconsets/controllerFaces/industrialWashPlantActive");

    public static final CustomIcon oMCDAlloyBlastSmelter = new CustomIcon("iconsets/controllerFaces/alloyBlastSmelter");
    public static final CustomIcon oMCDAlloyBlastSmelterActive = new CustomIcon(
        "iconsets/controllerFaces/alloyBlastSmelterActive");

    public static final CustomIcon oMCDIndustrialArcFurnace = new CustomIcon(
        "iconsets/controllerFaces/industrialArcFurnace");
    public static final CustomIcon oMCDIndustrialArcFurnaceActive = new CustomIcon(
        "iconsets/controllerFaces/industrialArcFurnaceActive");

    public static final CustomIcon oMCDIndustrialCuttingMachine = new CustomIcon(
        "iconsets/controllerFaces/industrialCuttingMachine");
    public static final CustomIcon oMCDIndustrialCuttingMachineActive = new CustomIcon(
        "iconsets/controllerFaces/industrialCuttingMachineActive");

    public static final CustomIcon oMCDIndustrialPlatePress = new CustomIcon(
        "iconsets/controllerFaces/industrialPlatePress");
    public static final CustomIcon oMCDIndustrialPlatePressActive = new CustomIcon(
        "iconsets/controllerFaces/industrialPlatePressActive");

    public static final CustomIcon oMCDIndustrialElectrolyzer = new CustomIcon(
        "iconsets/controllerFaces/industrialElectrolyzer");
    public static final CustomIcon oMCDIndustrialElectrolyzerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialElectrolyzerActive");

    public static final CustomIcon oMCDFrothFlotationCell = new CustomIcon(
        "iconsets/controllerFaces/frothFlotationCell");
    public static final CustomIcon oMCDFrothFlotationCellActive = new CustomIcon(
        "iconsets/controllerFaces/frothFlotationCellActive");

    public static final CustomIcon Overlay_Machine_Controller_Advanced = new CustomIcon(
        "iconsets/OVERLAY_FRONT_ADVANCED_MULTIBLOCK_ANIMATED");
    public static final CustomIcon Overlay_Machine_Controller_Advanced_Active = new CustomIcon(
        "iconsets/OVERLAY_FRONT_ADVANCED_MULTIBLOCK_ANIMATED_ACTIVE");

    // oMCA = Overlay_Machine_Controller_Advanced
    public static final CustomIcon oMCACokeOven = new CustomIcon("iconsets/controllerFaces/cokeOven");
    public static final CustomIcon oMCACokeOvenActive = new CustomIcon("iconsets/controllerFaces/cokeOvenActive");

    public static final CustomIcon oMCAChemicalPlant = new CustomIcon("iconsets/controllerFaces/chemicalPlant");
    public static final CustomIcon oMCAChemicalPlantActive = new CustomIcon(
        "iconsets/controllerFaces/chemicalPlantActive");

    public static final CustomIcon oMCAMegaAlloyBlastSmelter = new CustomIcon(
        "iconsets/controllerFaces/megaAlloyBlastSmelter");
    public static final CustomIcon oMCAMegaAlloyBlastSmelterActive = new CustomIcon(
        "iconsets/controllerFaces/megaAlloyBlastSmelterActive");

    public static final CustomIcon oMCATreeFarm = new CustomIcon("iconsets/controllerFaces/treeFarm");
    public static final CustomIcon oMCATreeFarmActive = new CustomIcon("iconsets/controllerFaces/treeFarmActive");

    public static final CustomIcon oMCAIndustrialRockBreaker = new CustomIcon(
        "iconsets/controllerFaces/industrialRockBreaker");
    public static final CustomIcon oMCAIndustrialRockBreakerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialRockBreakerActive");

    public static final CustomIcon oMCAAdvancedHeatExchanger = new CustomIcon(
        "iconsets/controllerFaces/advancedHeatExchanger");
    public static final CustomIcon oMCAAdvancedHeatExchangerActive = new CustomIcon(
        "iconsets/controllerFaces/advancedHeatExchangerActive");

    public static final CustomIcon oMCALargeRocketEngine = new CustomIcon("iconsets/controllerFaces/largeRocketEngine");
    public static final CustomIcon oMCALargeRocketEngineActive = new CustomIcon(
        "iconsets/controllerFaces/largeRocketEngineActive");

    public static final CustomIcon oMCAIndustrialChisel = new CustomIcon("iconsets/controllerFaces/industrialChisel");
    public static final CustomIcon oMCAIndustrialChiselActive = new CustomIcon(
        "iconsets/controllerFaces/industrialChiselActive");

    public static final CustomIcon oMCAIndustrialMolecularTransformer = new CustomIcon(
        "iconsets/controllerFaces/industrialMolecularTransformer");
    public static final CustomIcon oMCAIndustrialMolecularTransformerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialMolecularTransformerActive");

    public static final CustomIcon oMCAElementalDuplicator = new CustomIcon(
        "iconsets/controllerFaces/elementalDuplicator");
    public static final CustomIcon oMCAElementalDuplicatorActive = new CustomIcon(
        "iconsets/controllerFaces/elementalDuplicatorActive");

    public static final CustomIcon oMCAFluidHeater = new CustomIcon("iconsets/controllerFaces/fluidHeater");
    public static final CustomIcon oMCAFluidHeaterActive = new CustomIcon("iconsets/controllerFaces/fluidHeaterActive");

    public static final CustomIcon oMCAAmazonPackager = new CustomIcon("iconsets/controllerFaces/amazonPackager");
    public static final CustomIcon oMCAAmazonPackagerActive = new CustomIcon(
        "iconsets/controllerFaces/amazonPackagerActive");

    public static final CustomIcon oMCAIndustrialDehydrator = new CustomIcon(
        "iconsets/controllerFaces/industrialDehydrator");
    public static final CustomIcon oMCAIndustrialDehydratorActive = new CustomIcon(
        "iconsets/controllerFaces/industrialDehydratorActive");

    public static final CustomIcon oMCAIndustrialForgeHammer = new CustomIcon(
        "iconsets/controllerFaces/industrialForgeHammer");
    public static final CustomIcon oMCAIndustrialForgeHammerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialForgeHammerActive");

    public static final CustomIcon oMCAAdvancedEBF = new CustomIcon("iconsets/controllerFaces/advancedEBF");
    public static final CustomIcon oMCAAdvancedEBFActive = new CustomIcon("iconsets/controllerFaces/advancedEBFActive");

    public static final CustomIcon oMCASpargeTower = new CustomIcon("iconsets/controllerFaces/spargeTower");
    public static final CustomIcon oMCASpargeTowerActive = new CustomIcon("iconsets/controllerFaces/spargeTowerActive");

    public static final CustomIcon oMCAIndustrialVacuumFreezer = new CustomIcon(
        "iconsets/controllerFaces/industrialVacuumFreezer");
    public static final CustomIcon oMCAIndustrialVacuumFreezerActive = new CustomIcon(
        "iconsets/controllerFaces/industrialVacuumFreezerActive");

    public static final CustomIcon oMCAThermalBoiler = new CustomIcon("iconsets/controllerFaces/thermalBoiler");
    public static final CustomIcon oMCAThermalBoilerActive = new CustomIcon(
        "iconsets/controllerFaces/thermalBoilerActive");

    public static final CustomIcon oMCAQFT = new CustomIcon("iconsets/controllerFaces/quantumForceTransformer");
    public static final CustomIcon oMCAQFTActive = new CustomIcon(
        "iconsets/controllerFaces/quantumForceTransformerActive");

    public static final CustomIcon oMCAIndustrialMultiMachine = new CustomIcon(
        "iconsets/controllerFaces/industrialMultiMachine");
    public static final CustomIcon oMCAIndustrialMultiMachineActive = new CustomIcon(
        "iconsets/controllerFaces/industrialMultiMachineActive");

    public static final CustomIcon oMCAAdvancedImplosion = new CustomIcon("iconsets/controllerFaces/advancedImplosion");
    public static final CustomIcon oMCAAdvancedImplosionActive = new CustomIcon(
        "iconsets/controllerFaces/advancedImplosionActive");

    // Crafting Overlays
    public static final CustomIcon Casing_Adv_Workbench_Crafting_Overlay = new CustomIcon(
        "TileEntities/gt4/machine_top_crafting");
    public static final CustomIcon Casing_CropHarvester_Cutter = new CustomIcon("TileEntities/gt4/OVERLAY_CROP");
    public static final CustomIcon Casing_CropHarvester_Boxes = new CustomIcon("TileEntities/gt4/OVERLAY_BOXES");

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
    public static final CustomIcon Overlay_MatterFab_Active = new CustomIcon(
        "TileEntities/adv_machine_matterfab_active");
    public static final CustomIcon Overlay_MatterFab_Animated = new CustomIcon(
        "TileEntities/adv_machine_matterfab_animated");
    public static final CustomIcon Overlay_MatterFab_Active_Animated = new CustomIcon(
        "TileEntities/adv_machine_matterfab_active_animated");
    public static final CustomIcon Overlay_Water = new CustomIcon("TileEntities/adv_machine_water");
    public static final CustomIcon Overlay_UU_Matter = new CustomIcon("TileEntities/adv_machine_uum");

    // GT++ Tiered Hulls
    public static final CustomIcon TEXTURE_CASING_TIERED_ULV = new CustomIcon("iconsets/TieredHulls/CASING_ULV");
    public static final CustomIcon TEXTURE_CASING_TIERED_LV = new CustomIcon("iconsets/TieredHulls/CASING_LV");
    public static final CustomIcon TEXTURE_CASING_TIERED_MV = new CustomIcon("iconsets/TieredHulls/CASING_MV");
    public static final CustomIcon TEXTURE_CASING_TIERED_HV = new CustomIcon("iconsets/TieredHulls/CASING_HV");
    public static final CustomIcon TEXTURE_CASING_TIERED_EV = new CustomIcon("iconsets/TieredHulls/CASING_EV");
    public static final CustomIcon TEXTURE_CASING_TIERED_IV = new CustomIcon("iconsets/TieredHulls/CASING_IV");
    public static final CustomIcon TEXTURE_CASING_TIERED_LuV = new CustomIcon("iconsets/TieredHulls/CASING_LuV");
    public static final CustomIcon TEXTURE_CASING_TIERED_ZPM = new CustomIcon("iconsets/TieredHulls/CASING_ZPM");
    public static final CustomIcon TEXTURE_CASING_TIERED_UV = new CustomIcon("iconsets/TieredHulls/CASING_UV");
    public static final CustomIcon TEXTURE_CASING_TIERED_MAX = new CustomIcon("iconsets/TieredHulls/CASING_MAX");

    // Metroid related
    public static final CustomIcon TEXTURE_METAL_PANEL_A = new CustomIcon("metro/TEXTURE_METAL_PANEL_A");
    public static final CustomIcon TEXTURE_METAL_PANEL_B = new CustomIcon("metro/TEXTURE_METAL_PANEL_B");
    public static final CustomIcon TEXTURE_METAL_PANEL_C = new CustomIcon("metro/TEXTURE_METAL_PANEL_C");
    public static final CustomIcon TEXTURE_METAL_PANEL_D = new CustomIcon("metro/TEXTURE_METAL_PANEL_D");
    public static final CustomIcon TEXTURE_METAL_PANEL_F = new CustomIcon("metro/TEXTURE_METAL_PANEL_F");

    public static final CustomIcon TEXTURE_MAGIC_PANEL_A = new CustomIcon("metro/TEXTURE_MAGIC_A");
    public static final CustomIcon TEXTURE_MAGIC_PANEL_B = new CustomIcon("metro/TEXTURE_MAGIC_B");

    public static final CustomIcon TEXTURE_ORGANIC_PANEL_A_GLOWING = new CustomIcon(
        "metro/TEXTURE_ORGANIC_PANEL_A_GLOWING");

    public static final CustomIcon TEXTURE_STONE_RED_A = new CustomIcon("metro/TEXTURE_STONE_RED_A");
    public static final CustomIcon TEXTURE_STONE_RED_B = new CustomIcon("metro/TEXTURE_STONE_RED_B");

    public static final CustomIcon OVERLAY_SC_TURBINE1 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL1");
    public static final CustomIcon OVERLAY_SC_TURBINE2 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL2");
    public static final CustomIcon OVERLAY_SC_TURBINE3 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL3");
    public static final CustomIcon OVERLAY_SC_TURBINE4 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL4");
    public static final CustomIcon OVERLAY_SC_TURBINE5 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL5");
    public static final CustomIcon OVERLAY_SC_TURBINE6 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL6");
    public static final CustomIcon OVERLAY_SC_TURBINE7 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL7");
    public static final CustomIcon OVERLAY_SC_TURBINE8 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL8");
    public static final CustomIcon OVERLAY_SC_TURBINE9 = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE_IDEL9");

    public static final CustomIcon OVERLAY_SC_TURBINE1_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE1");
    public static final CustomIcon OVERLAY_SC_TURBINE2_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE2");
    public static final CustomIcon OVERLAY_SC_TURBINE3_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE3");
    public static final CustomIcon OVERLAY_SC_TURBINE4_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE4");
    public static final CustomIcon OVERLAY_SC_TURBINE5_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE5");
    public static final CustomIcon OVERLAY_SC_TURBINE6_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE6");
    public static final CustomIcon OVERLAY_SC_TURBINE7_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE7");
    public static final CustomIcon OVERLAY_SC_TURBINE8_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE8");
    public static final CustomIcon OVERLAY_SC_TURBINE9_ACTIVE = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE9");
    public static final CustomIcon TEXTURE_TECH_A = new CustomIcon("metro/TEXTURE_TECH_A");
    public static final CustomIcon TEXTURE_TECH_B = new CustomIcon("metro/TEXTURE_TECH_B");
    public static final CustomIcon TEXTURE_TECH_C = new CustomIcon("metro/TEXTURE_TECH_C");

    public static final CustomIcon TEXTURE_TECH_PANEL_D = new CustomIcon("metro/TEXTURE_TECH_PANEL_D");
    public static final CustomIcon TEXTURE_TECH_PANEL_H = new CustomIcon("metro/TEXTURE_TECH_PANEL_H");

    public static ITexture[] OVERLAYS_ENERGY_OUT_MULTI_BUFFER = new ITexture[] {
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 220, 220, 220, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 220, 220, 220, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 255, 100, 0, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 255, 255, 30, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 128, 128, 128, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
        new GTRenderedTexture(OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }) };

    public static IIconContainer[] CONNECTED_FUSION_HULLS = new IIconContainer[] { TEXTURE_CASING_FUSION_COIL_II_1,
        TEXTURE_CASING_FUSION_COIL_II_2, TEXTURE_CASING_FUSION_COIL_II_3, TEXTURE_CASING_FUSION_COIL_II_4,
        TEXTURE_CASING_FUSION_COIL_II_5, TEXTURE_CASING_FUSION_COIL_II_6, TEXTURE_CASING_FUSION_COIL_II_7,
        TEXTURE_CASING_FUSION_COIL_II_8, TEXTURE_CASING_FUSION_COIL_II_9, TEXTURE_CASING_FUSION_COIL_II_10,
        TEXTURE_CASING_FUSION_COIL_II_11, TEXTURE_CASING_FUSION_COIL_II_12 };

    public static IIconContainer[] CONNECTED_FUSION_HULLS_MK4 = new IIconContainer[] { TEXTURE_CASING_FUSION_COIL_III_1,
        TEXTURE_CASING_FUSION_COIL_III_2, TEXTURE_CASING_FUSION_COIL_III_3, TEXTURE_CASING_FUSION_COIL_III_4,
        TEXTURE_CASING_FUSION_COIL_III_5, TEXTURE_CASING_FUSION_COIL_III_6, TEXTURE_CASING_FUSION_COIL_III_7,
        TEXTURE_CASING_FUSION_COIL_III_8, TEXTURE_CASING_FUSION_COIL_III_9, TEXTURE_CASING_FUSION_COIL_III_10,
        TEXTURE_CASING_FUSION_COIL_III_11, TEXTURE_CASING_FUSION_COIL_III_12 };

    public static IIconContainer[] TIERED_MACHINE_HULLS = new IIconContainer[] { TEXTURE_CASING_TIERED_ULV,
        TEXTURE_CASING_TIERED_LV, TEXTURE_CASING_TIERED_MV, TEXTURE_CASING_TIERED_HV, TEXTURE_CASING_TIERED_EV,
        TEXTURE_CASING_TIERED_IV, TEXTURE_CASING_TIERED_LuV, TEXTURE_CASING_TIERED_ZPM, TEXTURE_CASING_TIERED_UV,
        TEXTURE_CASING_TIERED_MAX };

}
