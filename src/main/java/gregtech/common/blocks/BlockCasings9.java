package gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.translatedText;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings9 extends BlockCasingsAbstract {

    public BlockCasings9() {
        super(ItemCasings.class, "gt.blockcasings9", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Pipe_Polybenzimidazole, "PBI Pipe Casing");
        register(1, ItemList.Casing_Vent_T2, "Advanced Filter Casing", translatedText("gt.casings.advanced-filter"));
        register(2, ItemList.WoodenCasing, "Primitive Wooden Casing");
        register(3, ItemList.BlockIndustrialStrengthConcrete, "Superplasticizer-Treated High Strength Concrete");
        register(4, ItemList.BlockIndustrialWaterPlantCasing, "Sterile Water Plant Casing");
        register(5, ItemList.BlockSterileWaterPlantCasing, "Reinforced Sterile Water Plant Casing");
        register(6, ItemList.BlockFlocculationCasing, "Slick Sterile Flocculation Casing");
        register(7, ItemList.BlockNaquadahReinforcedWaterPlantCasing, "Stabilized Naquadah Water Plant Casing");
        register(8, ItemList.BlockExtremeCorrosionResistantCasing, "Inert Neutralization Water Plant Casing");
        register(9, ItemList.BlockHighPressureResistantCasing, "Reactive Gas Containment Casing");
        register(10, ItemList.BlockOzoneCasing, "Inert Filtration Casing");
        register(11, ItemList.BlockPlasmaHeatingCasing, "Heat-Resistant Trinium Plated Casing");
        register(12, ItemList.BlockNaquadriaReinforcedWaterPlantCasing, "Naquadria-Reinforced Water Plant Casing");
        register(13, ItemList.BlockUltraVioletLaserEmitter, "High Energy Ultraviolet Emitter Casing");
        // placeholder name
        register(14, ItemList.BlockQuarkPipe, "Particle Beam Guidance Pipe Casing");
        register(15, ItemList.BlockQuarkReleaseChamber, "Femtometer-Calibrated Particle Beam Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 16);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_PIPE_POLYBENZIMIDAZOLE.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_VENT_T2.getIcon();
            case 2 -> ordinalSide >= 2 ? Textures.BlockIcons.TEXTURE_METAL_PANEL_E.getIcon()
                : Textures.BlockIcons.TEXTURE_METAL_PANEL_E_A.getIcon();
            case 3 -> Textures.BlockIcons.INDUSTRIAL_STRENGTH_CONCRETE.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_INDUSTRIAL_WATER_PLANT.getIcon();
            case 5 -> Textures.BlockIcons.WATER_PLANT_CONCRETE_CASING.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_FLOCCULATION.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_NAQUADAH_REINFORCED_WATER_PLANT.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_CORROSION_RESISTANT.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_HIGH_PRESSURE_RESISTANT.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_OZONE.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_PLASMA_HEATER.getIcon();
            case 12 -> Textures.BlockIcons.NAQUADRIA_REINFORCED_WATER_PLANT_CASING.getIcon();
            case 13 -> Textures.BlockIcons.UV_BACKLIGHT_STERILIZER_CASING.getIcon();
            case 14 -> Textures.BlockIcons.BLOCK_QUARK_PIPE.getIcon();
            case 15 -> Textures.BlockIcons.BLOCK_QUARK_RELEASE_CHAMBER.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
