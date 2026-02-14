package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings4 extends BlockCasingsAbstract {

    public BlockCasings4() {
        super(ItemCasings.class, "gt.blockcasings4", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_RobustTungstenSteel, "Robust Tungstensteel Machine Casing");
        register(1, ItemList.Casing_CleanStainlessSteel, "Clean Stainless Steel Machine Casing");
        register(2, ItemList.Casing_StableTitanium, "Stable Titanium Machine Casing");
        register(3, ItemList.Casing_Firebox_Titanium, "Titanium Firebox Casing");
        register(6, ItemList.Casing_Fusion, "Fusion Machine Casing");
        register(7, ItemList.Casing_Fusion_Coil, "Fusion Coil Block");
        register(8, ItemList.Casing_Fusion2, "Fusion Machine Casing MK II");
        register(9, ItemList.Casing_Turbine, "Turbine Casing");
        register(10, ItemList.Casing_Turbine1, "Stainless Steel Turbine Casing");
        register(11, ItemList.Casing_Turbine2, "Titanium Turbine Casing");
        register(12, ItemList.Casing_Turbine3, "Tungstensteel Turbine Casing");
        register(13, ItemList.Casing_EngineIntake, "Engine Intake Casing");
        register(14, ItemList.Casing_MiningOsmiridium, "Mining Osmiridium Casing");
        register(15, ItemList.Casing_Firebricks, "Firebricks");
    }

    @Override
    public String getHarvestTool(int meta) {
        if (meta == 15) return HarvestTool.PickaxeLevel2.getHarvestTool();
        return super.getHarvestTool(meta);
    }

    @Override
    public int getHarvestLevel(int meta) {
        if (meta == 15) return HarvestTool.PickaxeLevel2.getHarvestLevel();
        return super.getHarvestLevel(meta);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta + 48;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 3 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_TITANIUM.getIcon()
                : Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 4 ->
                // Do not overwrite!
                Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW.getIcon();
            case 5 ->
                // Do not overwrite!
                Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_FUSION.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_FUSION_COIL.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_FUSION_2.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STEEL.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STAINLESSSTEEL.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TITANIUM.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TUNGSTENSTEEL.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_ENGINE_INTAKE.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_MINING_OSMIRIDIUM.getIcon();
            case 15 -> Textures.BlockIcons.MACHINE_CASING_DENSEBRICKS.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }
}
