package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings3 extends BlockCasingsAbstract {

    public BlockCasings3() {
        super(ItemCasings.class, "gt.blockcasings3", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Stripes_A, "Yellow Stripes Block");
        register(1, ItemList.Casing_Stripes_B, "Yellow Stripes Block");
        register(2, ItemList.Casing_RadioactiveHazard, "Radioactive Hazard Sign Block");
        register(3, ItemList.Casing_BioHazard, "Bio Hazard Sign Block");
        register(4, ItemList.Casing_ExplosionHazard, "Explosion Hazard Sign Block");
        register(5, ItemList.Casing_FireHazard, "Fire Hazard Sign Block");
        register(6, ItemList.Casing_AcidHazard, "Acid Hazard Sign Block");
        register(7, ItemList.Casing_MagicHazard, "Magic Hazard Sign Block");
        register(8, ItemList.Casing_FrostHazard, "Frost Hazard Sign Block");
        register(9, ItemList.Casing_NoiseHazard, "Noise Hazard Sign Block");
        register(10, ItemList.Casing_Grate, "Grate Machine Casing");
        register(11, ItemList.Casing_Vent, "Filter Machine Casing");
        register(12, ItemList.Casing_RadiationProof, "Radiation Proof Machine Casing");
        register(13, ItemList.Casing_Firebox_Bronze, "Bronze Firebox Casing");
        register(14, ItemList.Casing_Firebox_Steel, "Steel Firebox Casing");
        register(15, ItemList.Casing_Firebox_TungstenSteel, "Tungstensteel Firebox Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta + 32;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_STRIPES_A.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_STRIPES_B.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_BIOHAZARD.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_EXPLOSIONHAZARD.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_FIREHAZARD.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_MAGICHAZARD.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_FROSTHAZARD.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_NOISEHAZARD.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_GRATE.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_VENT.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
            case 13 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_BRONZE.getIcon()
                : Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
            case 14 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_STEEL.getIcon()
                : Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
            case 15 -> ordinalSide > 1 ? Textures.BlockIcons.MACHINE_CASING_FIREBOX_TUNGSTENSTEEL.getIcon()
                : Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }
}
