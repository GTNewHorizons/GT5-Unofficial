package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Minecraft;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;

public class TexturesGtBlock {

    private static boolean mAnimated = CORE.ConfigSwitches.enableAnimatedTextures;

    private static AutoMap<Runnable> mCustomiconMap = new AutoMap<>();

    static {}

    // public static ITexture[] CASING_BLOCKS_GTPP = new ITexture[256];

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
            mCustomiconMap.put(this);
            Logger.WARNING("Constructing a Custom Texture. " + this.mIconName);
            GregTech_API.sGTBlockIconload.add(this);
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
            this.mIcon = GregTech_API.sBlockIcons.registerIcon(this.mModID + ":" + this.mIconName);
            Logger.WARNING("FIND ME _ Processing texture: " + this.getTextureFile().getResourcePath());
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    public static class VanillaIcon implements IIconContainer, Runnable {

        protected IIcon mIcon;
        protected String mIconName;

        public VanillaIcon(final String aIconName) {
            this.mIconName = aIconName;
            mCustomiconMap.put(this);
            Logger.WARNING("Constructing a Custom Texture. " + this.mIconName);
            GregTech_API.sGTBlockIconload.add(this);
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
            this.mIcon = GregTech_API.sBlockIcons.registerIcon(Minecraft.ID + ":" + this.mIconName);
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    public static GT_RenderedTexture getTextureFromIcon(CustomIcon aIcon, short[] aRGB) {
        return new GT_RenderedTexture(aIcon, aRGB);
    }

    /*
     * Add Some Custom Textures below. I am not sure whether or not I need to declare them as such, but better to be
     * safe than sorry. Right?
     */

    // Vanilla Textures
    public static final VanillaIcon VanillaIcon_OakPlanks = new VanillaIcon("planks_oak");

    // PlaceHolder Texture
    private static final CustomIcon Internal_PlaceHolder = new CustomIcon("TileEntities/_PlaceHolder");
    public static final CustomIcon _PlaceHolder = Internal_PlaceHolder;

    public static final CustomIcon OVERLAY_ENERGY_OUT_MULTI_BUFFER = new CustomIcon(
            "iconsets/OVERLAY_ENERGY_OUT_MULTI_BUFFER");

    // Controllers
    private static final CustomIcon Internal_Casing_Fusion_Simple_Front = new CustomIcon(
            "TileEntities/MACHINE_CASING_FUSION_FRONT");
    public static final CustomIcon Casing_Fusion_Simple_Front = Internal_Casing_Fusion_Simple_Front;
    private static final CustomIcon Internal_Casing_Fusion_Simple_Front_Active = new CustomIcon(
            "TileEntities/MACHINE_CASING_FUSION_FRONT_ACTIVE");
    public static final CustomIcon Casing_Fusion_Simple_Front_Active = Internal_Casing_Fusion_Simple_Front_Active;

    // Machine Casings
    // Simple
    private static final CustomIcon Internal_Casing_Machine_Simple_Top = new CustomIcon("TileEntities/machine_top");
    public static final CustomIcon Casing_Machine_Simple_Top = Internal_Casing_Machine_Simple_Top;
    private static final CustomIcon Internal_Casing_Machine_Simple_Bottom = new CustomIcon(
            "TileEntities/machine_bottom");
    public static final CustomIcon Casing_Machine_Simple_Bottom = Internal_Casing_Machine_Simple_Bottom;
    // Advanced and Ultra
    private static final CustomIcon Internal_Casing_Machine_Advanced = new CustomIcon("TileEntities/high_adv_machine");
    public static final CustomIcon Casing_Machine_Advanced = Internal_Casing_Machine_Advanced;
    private static final CustomIcon Internal_Casing_Machine_Ultra = new CustomIcon("TileEntities/adv_machine_lesu");
    public static final CustomIcon Casing_Machine_Ultra = Internal_Casing_Machine_Ultra;
    // Dimensional - Non Overlay
    private static final CustomIcon Internal_Casing_Machine_Dimensional = new CustomIcon(
            "TileEntities/adv_machine_dimensional");
    public static final CustomIcon Casing_Machine_Dimensional = Internal_Casing_Machine_Dimensional;
    private static final CustomIcon Internal_Casing_Machine_Dimensional_Adv = new CustomIcon(
            "TileEntities/high_adv_machine_dimensional");
    public static final CustomIcon Casing_Machine_Dimensional_Adv = Internal_Casing_Machine_Dimensional_Adv;

    // Material Casings
    private static final CustomIcon Internal_Casing_Tantalloy61 = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TANTALLOY61");
    public static final CustomIcon Casing_Material_Tantalloy61 = Internal_Casing_Tantalloy61;
    private static final CustomIcon Internal_Casing_MaragingSteel = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_MARAGINGSTEEL");
    public static final CustomIcon Casing_Material_MaragingSteel = Internal_Casing_MaragingSteel;
    private static final CustomIcon Internal_Casing_Stellite = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_STELLITE");
    public static final CustomIcon Casing_Material_Stellite = Internal_Casing_Stellite;
    private static final CustomIcon Internal_Casing_Talonite = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TALONITE");
    public static final CustomIcon Casing_Material_Talonite = Internal_Casing_Talonite;
    private static final CustomIcon Internal_Turbine_SC_Casing = new TexturesGtBlock.CustomIcon("iconsets/SC_TURBINE");
    public static final CustomIcon Turbine_SC_Material_Casing = Internal_Turbine_SC_Casing;
    private static final CustomIcon Internal_Casing_Tumbaga = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TUMBAGA");
    public static final CustomIcon Casing_Material_Tumbaga = Internal_Casing_Tumbaga;
    private static final CustomIcon Internal_Casing_Zeron100 = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_ZERON100");
    public static final CustomIcon Casing_Material_Zeron100 = Internal_Casing_Zeron100;
    private static final CustomIcon Internal_Casing_Potin = new CustomIcon("TileEntities/MACHINE_CASING_STABLE_POTIN");
    public static final CustomIcon Casing_Material_Potin = Internal_Casing_Potin;

    private static final CustomIcon Internal_Casing_Grisium = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_GRISIUM");
    public static final CustomIcon Casing_Material_Grisium = Internal_Casing_Grisium;
    private static final CustomIcon Internal_Casing_RedSteel = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_RED_STEEL");
    public static final CustomIcon Casing_Material_RedSteel = Internal_Casing_RedSteel;
    private static final CustomIcon Internal_Casing_Incoloy020 = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_INCOLOY_020");
    public static final CustomIcon Casing_Material_Incoloy020 = Internal_Casing_Incoloy020;
    private static final CustomIcon Internal_Casing_IncoloyDS = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_INCOLOY_DS");
    public static final CustomIcon Casing_Material_IncoloyDS = Internal_Casing_IncoloyDS;
    private static final CustomIcon Internal_Casing_IncoloyMA956 = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_INCOLOY_MA956");
    public static final CustomIcon Casing_Material_IncoloyMA956 = Internal_Casing_IncoloyMA956;
    private static final CustomIcon Internal_Casing_ZirconiumCarbide = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_ZIRCONIUM_CARBIDE");
    public static final CustomIcon Casing_Material_ZirconiumCarbide = Internal_Casing_ZirconiumCarbide;

    private static final CustomIcon Internal_Casing_HastelloyX = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_HASTELLOY_X");
    public static final CustomIcon Casing_Material_HastelloyX = Internal_Casing_HastelloyX;
    private static final CustomIcon Internal_Casing_HastelloyN = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_HASTELLOY_N");
    public static final CustomIcon Casing_Material_HastelloyN = Internal_Casing_HastelloyN;
    private static final CustomIcon Internal_Casing_Fluid_IncoloyDS = new CustomIcon(
            "TileEntities/MACHINE_CASING_FLUID_INCOLOY_DS");
    public static final CustomIcon Casing_Material_Fluid_IncoloyDS = Internal_Casing_Fluid_IncoloyDS;

    private static final CustomIcon Internal_Casing_Laurenium = new CustomIcon("TileEntities/MACHINE_CASING_LAURENIUM");
    public static final CustomIcon Casing_Material_Laurenium = Internal_Casing_Laurenium;

    // Trinium Alloys
    public static final CustomIcon Casing_Trinium_Titanium = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TRINIUM_TITANIUM");
    public static final CustomIcon Casing_Trinium_Naquadah = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TRINIUM_NAQUADAH");
    public static final CustomIcon Casing_Trinium_Naquadah_Vent = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TRINIUM_NAQUADAH_VENT");
    public static final CustomIcon Casing_Trinium_Naquadah_Carbon = new CustomIcon(
            "TileEntities/MACHINE_CASING_STABLE_TRINIUM_NAQUADAH_CARBON");

    // Material Machine/Firebox Casings
    private static final CustomIcon Internal_Casing_Staballoy_Firebox = new CustomIcon(
            "TileEntities/MACHINE_CASING_FIREBOX_STABALLOY");
    public static final CustomIcon Casing_Staballoy_Firebox = Internal_Casing_Staballoy_Firebox;

    // Misc Casings
    private static final CustomIcon Internal_Casing_Machine_Redstone_Off = new CustomIcon(
            "TileEntities/cover_redstone_conductor");
    public static final CustomIcon Casing_Machine_Redstone_Off = Internal_Casing_Machine_Redstone_Off;
    private static final CustomIcon Internal_Casing_Machine_Redstone_On = new CustomIcon(
            "TileEntities/cover_redstone_emitter");
    public static final CustomIcon Casing_Machine_Redstone_On = Internal_Casing_Machine_Redstone_On;

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
    private static final CustomIcon Internal_Casing_Centrifuge = new CustomIcon(
            "TileEntities/MACHINE_CASING_CENTRIFUGE");
    public static final CustomIcon Casing_Material_Centrifuge = Internal_Casing_Centrifuge;

    // Quantum Force Transformer Casing
    // spotless:off
    private static final CustomIcon Internal_Casing_QFT = mAnimated ? new CustomIcon("TileEntities/MACHINE_CASING_QFT_COIL") : new CustomIcon("TileEntites/MACHINE_CASING_QFT_COIL");
    public static final CustomIcon Casing_Coil_QFT = Internal_Casing_QFT;
    public static final CustomIcon NeutronPulseManipulator = mAnimated ? new CustomIcon("NeutronPulseManipulator") : new CustomIcon("NeutronPulseManipulatorStatic");
    public static final CustomIcon CosmicFabricManipulator = mAnimated ? new CustomIcon("CosmicFabricManipulator") : new CustomIcon("CosmicFabricManipulatorStatic");
    public static final CustomIcon InfinityInfusedManipulator = mAnimated ? new CustomIcon("InfinityInfusedManipulator") : new CustomIcon("InfinityInfusedManipulatorStatic");
    public static final CustomIcon SpaceTimeContinuumRipper = mAnimated ? new CustomIcon("SpaceTimeContinuumRipper") : new CustomIcon("SpaceTimeContinuumRipperStatic");
    public static final CustomIcon Manipulator_Top = new CustomIcon("Manipulator_Top");
    public static final CustomIcon NeutronShieldingCore = mAnimated ? new CustomIcon("NeutronShieldingCore") : new CustomIcon("NeutronShieldingCoreStatic");
    public static final CustomIcon CosmicFabricShieldingCore = mAnimated ? new CustomIcon("CosmicFabricShieldingCore") : new CustomIcon("CosmicFabricShieldingCoreStatic");
    public static final CustomIcon InfinityInfusedShieldingCore = mAnimated ? new CustomIcon("InfinityInfusedShieldingCore") : new CustomIcon("InfinityInfusedShieldingCoreStatic");
    public static final CustomIcon SpaceTimeBendingCore = mAnimated ? new CustomIcon("SpaceTimeBendingCore") : new CustomIcon("SpaceTimeBendingCoreStatic");
    public static final CustomIcon ForceFieldGlass = new CustomIcon("ForceFieldGlass");
    public static final CustomIcon ForceField = new CustomIcon("rendering/ForceField");
    public static final CustomIcon Blank = new CustomIcon("Blank");
    //spotless:on

    // MACHINE_CASING_FARM_MANAGER_STRUCTURAL
    // Farm Manager Casings
    private static final CustomIcon Internal_Casing_Machine_Farm_Manager = new CustomIcon(
            "TileEntities/MACHINE_CASING_FARM_MANAGER_STRUCTURAL");
    public static final CustomIcon Casing_Machine_Farm_Manager = Internal_Casing_Machine_Farm_Manager;
    // Acacia_Log
    private static final CustomIcon Internal_Casing_Machine_Acacia_Log = new CustomIcon("TileEntities/log_acacia_top");
    public static final CustomIcon Casing_Machine_Acacia_Log = Internal_Casing_Machine_Acacia_Log;
    // Podzol Top
    private static final CustomIcon Internal_Casing_Machine_Podzol = new CustomIcon("TileEntities/dirt_podzol_top");
    public static final CustomIcon Casing_Machine_Podzol = Internal_Casing_Machine_Podzol;

    // Structural Blocks
    private static final CustomIcon Internal_Casing_Machine_Metal_Grate_A = new CustomIcon("chrono/MetalGrate");
    public static final CustomIcon Casing_Machine_Metal_Grate_A = Internal_Casing_Machine_Metal_Grate_A;
    private static final CustomIcon Internal_Casing_Machine_Metal_Grate_A_Solid = new CustomIcon(
            "chrono/MetalGrateA_Solid");
    public static final CustomIcon Casing_Machine_Metal_Grate_A_Solid = Internal_Casing_Machine_Metal_Grate_A_Solid;
    private static final CustomIcon Internal_Casing_Machine_Metal_Grate_B = new CustomIcon("chrono/MetalGrate2");
    public static final CustomIcon Casing_Machine_Metal_Grate_B = Internal_Casing_Machine_Metal_Grate_B;
    private static final CustomIcon Internal_Casing_Machine_Metal_Panel_A = new CustomIcon("chrono/MetalPanel");
    public static final CustomIcon Casing_Machine_Metal_Panel_A = Internal_Casing_Machine_Metal_Panel_A;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_A = new CustomIcon("chrono/MetalSheet");
    public static final CustomIcon Casing_Machine_Metal_Sheet_A = Internal_Casing_Machine_Metal_Sheet_A;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_B = new CustomIcon("chrono/MetalSheet2");
    public static final CustomIcon Casing_Machine_Metal_Sheet_B = Internal_Casing_Machine_Metal_Sheet_B;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_C = new CustomIcon("chrono/MetalSheet3");
    public static final CustomIcon Casing_Machine_Metal_Sheet_C = Internal_Casing_Machine_Metal_Sheet_C;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_D = new CustomIcon("chrono/MetalSheet4");
    public static final CustomIcon Casing_Machine_Metal_Sheet_D = Internal_Casing_Machine_Metal_Sheet_D;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_E = new CustomIcon("chrono/MetalSheet5");
    public static final CustomIcon Casing_Machine_Metal_Sheet_E = Internal_Casing_Machine_Metal_Sheet_E;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_F = new CustomIcon("chrono/MetalSheet6");
    public static final CustomIcon Casing_Machine_Metal_Sheet_F = Internal_Casing_Machine_Metal_Sheet_F;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_G = new CustomIcon("chrono/MetalSheet7");
    public static final CustomIcon Casing_Machine_Metal_Sheet_G = Internal_Casing_Machine_Metal_Sheet_G;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_H = new CustomIcon("chrono/MetalSheet8");
    public static final CustomIcon Casing_Machine_Metal_Sheet_H = Internal_Casing_Machine_Metal_Sheet_H;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_I = new CustomIcon("chrono/MetalSheet9");
    public static final CustomIcon Casing_Machine_Metal_Sheet_I = Internal_Casing_Machine_Metal_Sheet_I;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_J = new CustomIcon("chrono/MetalSheet10");
    public static final CustomIcon Casing_Machine_Metal_Sheet_J = Internal_Casing_Machine_Metal_Sheet_J;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_K = new CustomIcon("chrono/MetalSheet11");
    public static final CustomIcon Casing_Machine_Metal_Sheet_K = Internal_Casing_Machine_Metal_Sheet_K;
    private static final CustomIcon Internal_Casing_Machine_Metal_Sheet_L = new CustomIcon("chrono/MetalSheet12");
    public static final CustomIcon Casing_Machine_Metal_Sheet_L = Internal_Casing_Machine_Metal_Sheet_L;
    private static final CustomIcon Internal_Overlay_Machine_Cyber_A = new CustomIcon("chrono/CyberPanel");
    public static final CustomIcon Overlay_Machine_Cyber_A = Internal_Overlay_Machine_Cyber_A;
    private static final CustomIcon Internal_Overlay_Machine_Cyber_B = new CustomIcon("chrono/CyberPanel2");
    public static final CustomIcon Overlay_Machine_Cyber_B = Internal_Overlay_Machine_Cyber_B;

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
    private static final CustomIcon Internal_Overlay_Machine_Vent = new CustomIcon(
            "TileEntities/machine_top_vent_rotating");
    public static final CustomIcon Overlay_Machine_Vent = Internal_Overlay_Machine_Vent;
    private static final CustomIcon Internal_Overlay_Machine_Vent_Fast = new CustomIcon(
            "TileEntities/machine_top_vent_rotating_fast");
    public static final CustomIcon Overlay_Machine_Vent_Fast = Internal_Overlay_Machine_Vent_Fast;
    private static final CustomIcon Internal_Overlay_Machine_Vent_Adv = new CustomIcon(
            "TileEntities/adv_machine_vent_rotating");
    public static final CustomIcon Overlay_Machine_Vent_Adv = Internal_Overlay_Machine_Vent_Adv;
    private static final CustomIcon Internal_Overlay_Machine_Turbine_Active = new CustomIcon(
            "TileEntities/STEAM_TURBINE_SIDE_ACTIVE");
    public static final CustomIcon Overlay_Machine_Turbine_Active = Internal_Overlay_Machine_Turbine_Active;
    // Grate Texture
    public static final CustomIcon OVERLAY_GRATE_A = new CustomIcon("metro/OVERLAY_GRATE_A");
    // Speaker Texture
    private static final CustomIcon Internal_Overlay_Machine_Sound = new CustomIcon("TileEntities/audio_out");
    public static final CustomIcon Overlay_Machine_Sound = Internal_Overlay_Machine_Sound;
    private static final CustomIcon Internal_Overlay_Machine_Sound_Active = new CustomIcon(
            "TileEntities/audio_out_active");
    public static final CustomIcon Overlay_Machine_Sound_Active = Internal_Overlay_Machine_Sound_Active;
    // Diesel Engines
    private static final CustomIcon Internal_Overlay_Machine_Diesel_Vertical = new CustomIcon(
            "TileEntities/machine_top_dieselmotor");
    public static final CustomIcon Overlay_Machine_Diesel_Vertical = Internal_Overlay_Machine_Diesel_Vertical;
    private static final CustomIcon Internal_Overlay_Machine_Diesel_Horizontal = new CustomIcon(
            "TileEntities/machine_top_dieselmotor2");
    public static final CustomIcon Overlay_Machine_Diesel_Horizontal = Internal_Overlay_Machine_Diesel_Horizontal;
    private static final CustomIcon Internal_Overlay_Machine_Diesel_Vertical_Active = new CustomIcon(
            "TileEntities/machine_top_dieselmotor_active");
    public static final CustomIcon Overlay_Machine_Diesel_Vertical_Active = Internal_Overlay_Machine_Diesel_Vertical_Active;
    private static final CustomIcon Internal_Overlay_Machine_Diesel_Horizontal_Active = new CustomIcon(
            "TileEntities/machine_top_dieselmotor2_active");
    public static final CustomIcon Overlay_Machine_Diesel_Horizontal_Active = Internal_Overlay_Machine_Diesel_Horizontal_Active;
    // Computer Screens
    private static final CustomIcon Internal_Casing_Machine_Screen_1 = new CustomIcon(
            "TileEntities/adv_machine_screen_random1");
    public static final CustomIcon Casing_Machine_Screen_1 = Internal_Casing_Machine_Screen_1;
    private static final CustomIcon Internal_Casing_Machine_Screen_2 = new CustomIcon(
            "TileEntities/adv_machine_screen_random2");
    public static final CustomIcon Casing_Machine_Screen_2 = Internal_Casing_Machine_Screen_2;
    private static final CustomIcon Internal_Casing_Machine_Screen_3 = new CustomIcon(
            "TileEntities/adv_machine_screen_random3");
    public static final CustomIcon Casing_Machine_Screen_3 = Internal_Casing_Machine_Screen_3;

    private static final CustomIcon Internal_Casing_Machine_Screen_Rainbow = new CustomIcon(
            "TileEntities/overlay_rainbowscreen");
    public static final CustomIcon Casing_Machine_Screen_Rainbow = Internal_Casing_Machine_Screen_Rainbow;
    private static final CustomIcon Internal_Casing_Machine_Screen_Frequency = new CustomIcon(
            "TileEntities/adv_machine_screen_frequency");
    public static final CustomIcon Casing_Machine_Screen_Frequency = Internal_Casing_Machine_Screen_Frequency;
    private static final CustomIcon Internal_Overlay_Machine_Screen_Logo = new CustomIcon(
            "TileEntities/adv_machine_screen_logo");
    public static final CustomIcon Overlay_Machine_Screen_Logo = Internal_Overlay_Machine_Screen_Logo;
    private static final CustomIcon Internal_Overlay_Machine_Cyber_Interface = new CustomIcon("chrono/Overlay_Cyber");
    public static final CustomIcon Overlay_Machine_Cyber_Interface = Internal_Overlay_Machine_Cyber_Interface;

    // Machine Controller Overlays
    private static final CustomIcon Internal_Overlay_Machine_Controller_Default = new CustomIcon(
            "iconsets/OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ANIMATED");
    public static final CustomIcon Overlay_Machine_Controller_Default = Internal_Overlay_Machine_Controller_Default;
    private static final CustomIcon Internal_Overlay_Machine_Controller_Default_Active = new CustomIcon(
            "iconsets/OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ANIMATED_ACTIVE");
    public static final CustomIcon Overlay_Machine_Controller_Default_Active = Internal_Overlay_Machine_Controller_Default_Active;

    private static final CustomIcon Internal_Overlay_Machine_Controller_Advanced = new CustomIcon(
            "iconsets/OVERLAY_FRONT_ADVANCED_MULTIBLOCK_ANIMATED");
    public static final CustomIcon Overlay_Machine_Controller_Advanced = Internal_Overlay_Machine_Controller_Advanced;
    private static final CustomIcon Internal_Overlay_Machine_Controller_Advanced_Active = new CustomIcon(
            "iconsets/OVERLAY_FRONT_ADVANCED_MULTIBLOCK_ANIMATED_ACTIVE");
    public static final CustomIcon Overlay_Machine_Controller_Advanced_Active = Internal_Overlay_Machine_Controller_Advanced_Active;

    // Crafting Overlays
    public static final CustomIcon Overlay_Crafting_Bronze = new CustomIcon("TileEntities/bronze_top_crafting");
    public static final CustomIcon Overlay_Crafting_Steel = new CustomIcon("TileEntities/cover_crafting");

    public static final CustomIcon Casing_Workbench_Crafting_Overlay = new CustomIcon(
            "TileEntities/gt4/bronze_top_crafting");
    public static final CustomIcon Casing_Workbench_Top = new CustomIcon("TileEntities/gt4/bronze_top");
    public static final CustomIcon Casing_Workbench_Side = new CustomIcon("TileEntities/gt4/bronze_side");
    public static final CustomIcon Casing_Workbench_Bottom = new CustomIcon("TileEntities/gt4/bronze_bottom");
    public static final CustomIcon Casing_Adv_Workbench_Crafting_Overlay = new CustomIcon(
            "TileEntities/gt4/machine_top_crafting");
    public static final CustomIcon Casing_Adv_Workbench_Top = new CustomIcon("TileEntities/gt4/machine_top");
    public static final CustomIcon Casing_Adv_Workbench_Side = new CustomIcon("TileEntities/gt4/machine_side");
    public static final CustomIcon Casing_Adv_Workbench_Bottom = new CustomIcon("TileEntities/gt4/machine_bottom");

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

    public static final CustomIcon Casing_Electric_Auto_Workbench_Side = new CustomIcon(
            "TileEntities/gt4/OVERLAY_SIDE_CABINET");

    public static final CustomIcon Casing_CropHarvester_Cutter = new CustomIcon("TileEntities/gt4/OVERLAY_CROP");
    public static final CustomIcon Casing_CropHarvester_Boxes = new CustomIcon("TileEntities/gt4/OVERLAY_BOXES");

    public static final CustomIcon Casing_InventoryManagaer_Cyan = new CustomIcon("TileEntities/gt4/OVERLAY_CYAN");
    public static final CustomIcon Casing_InventoryManagaer_Green = new CustomIcon("TileEntities/gt4/OVERLAY_GREEN");
    public static final CustomIcon Casing_InventoryManagaer_Purple = new CustomIcon("TileEntities/gt4/OVERLAY_PURPLE");
    public static final CustomIcon Casing_InventoryManagaer_Red = new CustomIcon("TileEntities/gt4/OVERLAY_RED");
    public static final CustomIcon Casing_InventoryManagaer_Yellow = new CustomIcon("TileEntities/gt4/OVERLAY_YELLOW");
    public static final CustomIcon Casing_InventoryManagaer_Blue = new CustomIcon("TileEntities/gt4/OVERLAY_BLUE");

    public static final CustomIcon Casing_InventoryManagaer_Cyan_Redstone = new CustomIcon(
            "TileEntities/gt4/OVERLAY_CYAN_REDSTONE");
    public static final CustomIcon Casing_InventoryManagaer_Green_Redstone = new CustomIcon(
            "TileEntities/gt4/OVERLAY_GREEN_REDSTONE");
    public static final CustomIcon Casing_InventoryManagaer_Purple_Redstone = new CustomIcon(
            "TileEntities/gt4/OVERLAY_PURPLE_REDSTONE");
    public static final CustomIcon Casing_InventoryManagaer_Red_Redstone = new CustomIcon(
            "TileEntities/gt4/OVERLAY_RED_REDSTONE");
    public static final CustomIcon Casing_InventoryManagaer_Yellow_Redstone = new CustomIcon(
            "TileEntities/gt4/OVERLAY_YELLOW_REDSTONE");
    public static final CustomIcon Casing_InventoryManagaer_Blue_Redstone = new CustomIcon(
            "TileEntities/gt4/OVERLAY_BLUE_REDSTONE");

    // Covers
    private static final CustomIcon Internal_Overlay_Overflow_Valve = new CustomIcon("iconsets/OVERLAY_OVERFLOW_VALVE");
    public static final CustomIcon Overlay_Overflow_Valve = Internal_Overlay_Overflow_Valve;

    // Hatch Overlays
    // Charger Texture
    private static final CustomIcon Internal_Overlay_Hatch_Charger = new CustomIcon("TileEntities/cover_charger");
    public static final CustomIcon Overlay_Hatch_Charger = Internal_Overlay_Hatch_Charger;
    // Discharger Texture
    private static final CustomIcon Internal_Overlay_Hatch_Discharger = new CustomIcon("TileEntities/cover_discharge");
    public static final CustomIcon Overlay_Hatch_Discharger = Internal_Overlay_Hatch_Discharger;
    // Advanced Muffler
    private static final CustomIcon Internal_Overlay_Hatch_Muffler_Adv = new CustomIcon("iconsets/OVERLAY_MUFFLER_ADV");
    public static final CustomIcon Overlay_Hatch_Muffler_Adv = Internal_Overlay_Hatch_Muffler_Adv;
    // Control Core Bus
    private static final CustomIcon Internal_Overlay_Hatch_Control_Core = new CustomIcon(
            "iconsets/OVERLAY_CONTROL_CORE_BUS");
    public static final CustomIcon Overlay_Hatch_Control_Core = Internal_Overlay_Hatch_Control_Core;
    // Milling Ball Bus
    private static final CustomIcon Internal_Overlay_Bus_Milling_Balls = new CustomIcon(
            "iconsets/OVERLAY_MILLING_BALL_BUS");
    public static final CustomIcon Overlay_Bus_Milling_Balls = Internal_Overlay_Bus_Milling_Balls;
    // Catalyst Bus
    private static final CustomIcon Internal_Overlay_Bus_Catalyst = new CustomIcon("iconsets/OVERLAY_CATALYSTS");
    public static final CustomIcon Overlay_Bus_Catalyst = Internal_Overlay_Bus_Catalyst;

    public static final CustomIcon Overlay_Cobble = new CustomIcon("iconsets/OVERLAY_COBBLE");
    public static final CustomIcon Overlay_Connector = new CustomIcon("iconsets/OVERLAY_CONNECTOR");
    public static final CustomIcon Overlay_Fire = new CustomIcon("iconsets/OVERLAY_FIRE");
    public static final CustomIcon Overlay_Grinder = new CustomIcon("iconsets/OVERLAY_GRINDER");
    public static final CustomIcon Overlay_GT_Logo = new CustomIcon("iconsets/OVERLAY_GT");
    public static final CustomIcon Overlay_Ice = new CustomIcon("iconsets/OVERLAY_ICE");
    public static final CustomIcon Overlay_Implosion = new CustomIcon("iconsets/OVERLAY_IMPLOSION");
    // Lapo Orb Hatch
    public static final CustomIcon Overlay_Hatch_Lapo_Orb = new CustomIcon("iconsets/OVERLAY_LAPO");
    // Data Orb Hatch
    public static final CustomIcon Overlay_Hatch_Data_Orb = new CustomIcon("iconsets/OVERLAY_DATA_ORB");

    // RTG Hatch
    private static final CustomIcon Internal_Overlay_Hatch_RTG_Off = new CustomIcon("iconsets/OVERLAY_ENERGY_RTG_OFF");
    public static final CustomIcon Overlay_Hatch_RTG_Off = Internal_Overlay_Hatch_RTG_Off;
    private static final CustomIcon Internal_Overlay_Hatch_RTG_On = new CustomIcon("iconsets/OVERLAY_ENERGY_RTG_ON");
    public static final CustomIcon Overlay_Hatch_RTG_On = Internal_Overlay_Hatch_RTG_On;

    // Dimensional
    private static final CustomIcon Internal_Overlay_Machine_Dimensional_Blue = new CustomIcon(
            "TileEntities/adv_machine_dimensional_cover_blue");
    public static final CustomIcon Overlay_Machine_Dimensional_Blue = Internal_Overlay_Machine_Dimensional_Blue;
    private static final CustomIcon Internal_Overlay_Machine_Dimensional_Orange = new CustomIcon(
            "TileEntities/adv_machine_dimensional_cover_orange");
    public static final CustomIcon Overlay_Machine_Dimensional_Orange = Internal_Overlay_Machine_Dimensional_Orange;
    // Icons
    private static final CustomIcon Internal_Overlay_MatterFab = new CustomIcon("TileEntities/adv_machine_matterfab");
    public static final CustomIcon Overlay_MatterFab = Internal_Overlay_MatterFab;
    private static final CustomIcon Internal_Overlay_MatterFab_Active = new CustomIcon(
            "TileEntities/adv_machine_matterfab_active");
    public static final CustomIcon Overlay_MatterFab_Active = Internal_Overlay_MatterFab_Active;

    private static final CustomIcon Internal_Overlay_MatterFab_Animated = new CustomIcon(
            "TileEntities/adv_machine_matterfab_animated");
    public static final CustomIcon Overlay_MatterFab_Animated = Internal_Overlay_MatterFab_Animated;
    private static final CustomIcon Internal_Overlay_MatterFab_Active_Animated = new CustomIcon(
            "TileEntities/adv_machine_matterfab_active_animated");
    public static final CustomIcon Overlay_MatterFab_Active_Animated = Internal_Overlay_MatterFab_Active_Animated;

    private static final CustomIcon Internal_Overlay_Oil = new CustomIcon("TileEntities/adv_machine_oil");
    public static final CustomIcon Overlay_Oil = Internal_Overlay_Oil;
    private static final CustomIcon Internal_Overlay_Water = new CustomIcon("TileEntities/adv_machine_water");
    public static final CustomIcon Overlay_Water = Internal_Overlay_Water;
    private static final CustomIcon Internal_Overlay_UU_Matter = new CustomIcon("TileEntities/adv_machine_uum");
    public static final CustomIcon Overlay_UU_Matter = Internal_Overlay_UU_Matter;

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
    public static final CustomIcon TEXTURE_METAL_PANEL_E = new CustomIcon("metro/TEXTURE_METAL_PANEL_E");
    public static final CustomIcon TEXTURE_METAL_PANEL_F = new CustomIcon("metro/TEXTURE_METAL_PANEL_F");
    public static final CustomIcon TEXTURE_METAL_PANEL_G = new CustomIcon("metro/TEXTURE_METAL_PANEL_G");
    public static final CustomIcon TEXTURE_METAL_PANEL_H = new CustomIcon("metro/TEXTURE_METAL_PANEL_H");
    public static final CustomIcon TEXTURE_METAL_PANEL_I = new CustomIcon("metro/TEXTURE_METAL_PANEL_I");

    public static final CustomIcon TEXTURE_MAGIC_PANEL_A = new CustomIcon("metro/TEXTURE_MAGIC_A");
    public static final CustomIcon TEXTURE_MAGIC_PANEL_B = new CustomIcon("metro/TEXTURE_MAGIC_B");

    public static final CustomIcon TEXTURE_ORGANIC_PANEL_A = new CustomIcon("metro/TEXTURE_ORGANIC_PANEL_A");
    public static final CustomIcon TEXTURE_ORGANIC_PANEL_A_GLOWING = new CustomIcon(
            "metro/TEXTURE_ORGANIC_PANEL_A_GLOWING");

    public static final CustomIcon TEXTURE_STONE_BIRD_A = new CustomIcon("metro/TEXTURE_STONE_BIRD_A");
    public static final CustomIcon TEXTURE_STONE_BIRD_A_LEFT = new CustomIcon("metro/TEXTURE_STONE_BIRD_A_LEFT");
    public static final CustomIcon TEXTURE_STONE_BIRD_A_RIGHT = new CustomIcon("metro/TEXTURE_STONE_BIRD_A_RIGHT");
    public static final CustomIcon TEXTURE_STONE_RED_A = new CustomIcon("metro/TEXTURE_STONE_RED_A");
    public static final CustomIcon TEXTURE_STONE_RED_B = new CustomIcon("metro/TEXTURE_STONE_RED_B");
    public static final CustomIcon TEXTURE_STONE_BLUE_A = new CustomIcon("metro/TEXTURE_STONE_BLUE_A");
    public static final CustomIcon TEXTURE_STONE_GREEN_A = new CustomIcon("metro/TEXTURE_STONE_GREEN_A");
    public static final CustomIcon TEXTURE_STONE_TABLET_A = new CustomIcon("metro/TEXTURE_STONE_TABLET_A");
    public static final CustomIcon TEXTURE_STONE_TABLET_B = new CustomIcon("metro/TEXTURE_STONE_TABLET_B");

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

    public static final CustomIcon TEXTURE_TECH_PANEL_A = new CustomIcon("metro/TEXTURE_TECH_PANEL_A");
    public static final CustomIcon TEXTURE_TECH_PANEL_B = new CustomIcon("metro/TEXTURE_TECH_PANEL_B");
    public static final CustomIcon TEXTURE_TECH_PANEL_C = new CustomIcon("metro/TEXTURE_TECH_PANEL_C");
    public static final CustomIcon TEXTURE_TECH_PANEL_D = new CustomIcon("metro/TEXTURE_TECH_PANEL_D");
    public static final CustomIcon TEXTURE_TECH_PANEL_E = new CustomIcon("metro/TEXTURE_TECH_PANEL_E");
    public static final CustomIcon TEXTURE_TECH_PANEL_F = new CustomIcon("metro/TEXTURE_TECH_PANEL_F");
    public static final CustomIcon TEXTURE_TECH_PANEL_G = new CustomIcon("metro/TEXTURE_TECH_PANEL_G");
    public static final CustomIcon TEXTURE_TECH_PANEL_H = new CustomIcon("metro/TEXTURE_TECH_PANEL_H");
    public static final CustomIcon TEXTURE_TECH_PANEL_I = new CustomIcon("metro/TEXTURE_TECH_PANEL_I");

    public static final CustomIcon TEXTURE_TECH_PANEL_RADIOACTIVE = new CustomIcon(
            "TileEntities/DecayablesChest_bottom");
    public static final CustomIcon TEXTURE_TECH_PANEL_RADIOACTIVE_ALT = new CustomIcon(
            "TileEntities/DecayablesChest_top");

    public static ITexture[] OVERLAYS_ENERGY_OUT_MULTI_BUFFER = new ITexture[] {
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 220, 220, 220, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 220, 220, 220, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 255, 100, 0, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 255, 255, 30, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 128, 128, 128, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
            new GT_RenderedTexture((IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER, new short[] { 240, 240, 245, 0 }),
            new GT_RenderedTexture(
                    (IIconContainer) OVERLAY_ENERGY_OUT_MULTI_BUFFER,
                    new short[] { 240, 240, 245, 0 }) };

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
