package gregtech.common.blocks;

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
public class BlockCasings8 extends BlockCasingsAbstract {

    public static boolean mConnectedMachineTextures = true;

    // WATCH OUT FOR TEXTURE ID's
    public BlockCasings8() {
        super(ItemCasings.class, "gt.blockcasings8", MaterialCasings.INSTANCE, 15);
        /*
         * DO NOT USE INDEX 15 ! USED HERE: https://github.com/GTNewHorizons/KubaTech/pull/101 glee8e: above comment
         * means texture ID15 + 48 is used for DEFCCasingBlock meta 0 so it should not be reused here without added
         * special case for this ID in #getTextureIndex()
         */

        register(0, ItemList.Casing_Chemically_Inert);
        register(1, ItemList.Casing_Pipe_Polytetrafluoroethylene);
        register(2, ItemList.Casing_MiningNeutronium);
        register(3, ItemList.Casing_MiningBlackPlutonium);
        register(4, ItemList.Casing_ExtremeEngineIntake);
        register(5, ItemList.Casing_AdvancedRadiationProof);
        register(6, ItemList.Casing_Advanced_Rhodium_Palladium);
        register(7, ItemList.Casing_Advanced_Iridium);
        register(8, ItemList.Casing_Magical);
        register(10, ItemList.RadiantNaquadahAlloyCasing);
        register(11, ItemList.BasicPhotolithographicFrameworkCasing);
        register(12, ItemList.ReinforcedPhotolithographicFrameworkCasing);
        register(13, ItemList.RadiationProofPhotolithographicFrameworkCasing);
        register(14, ItemList.InfinityCooledCasing);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | (aMeta + 48);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_CHEMICALLY_INERT.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_MINING_NEUTRONIUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_MINING_BLACKPLUTONIUM.getIcon();
            // changed color in a terrible way
            case 4 -> Textures.BlockIcons.MACHINE_CASING_EXTREME_ENGINE_INTAKE.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_ADVANCEDRADIATIONPROOF.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_RHODIUM_PALLADIUM.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_IRIDIUM.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_MAGICAL.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_HSSS.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_RADIANT_NAQUADAH_ALLOY.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_1.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_2.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_PCB_TIER_3.getIcon();
            case 14 -> Textures.BlockIcons.INFINITY_COOLED_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}
