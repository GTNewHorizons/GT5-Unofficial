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
public class BlockCasings6 extends BlockCasingsAbstract {

    public BlockCasings6() {
        super(ItemCasings.class, "gt.blockcasings6", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Tank_0, "Hermetic Casing");
        register(1, ItemList.Casing_Tank_1, "Hermetic Casing I");
        register(2, ItemList.Casing_Tank_2, "Hermetic Casing II");
        register(3, ItemList.Casing_Tank_3, "Hermetic Casing III");
        register(4, ItemList.Casing_Tank_4, "Hermetic Casing IV");
        register(5, ItemList.Casing_Tank_5, "Hermetic Casing V");
        register(6, ItemList.Casing_Tank_6, "Hermetic Casing VI");
        register(7, ItemList.Casing_Tank_7, "Hermetic Casing VII");
        register(8, ItemList.Casing_Tank_8, "Hermetic Casing VIII");
        register(9, ItemList.Casing_Tank_9, "Hermetic Casing IX");
        register(10, ItemList.Casing_Tank_10, "Hermetic Casing X");
        register(11, ItemList.Casing_Tank_11, "Hermetic Casing XI");
        register(12, ItemList.Casing_Tank_12, "Hermetic Casing XII");
        register(13, ItemList.Casing_Tank_13, "Hermetic Casing XIII");
        register(14, ItemList.Casing_Tank_14, "Hermetic Casing XIV");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (8 << 7) | (aMeta + 112);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if (ordinalSide == 0) {
            return Textures.BlockIcons.MACHINECASINGS_BOTTOM[aMeta].getIcon();
        }
        if (ordinalSide == 1) {
            return Textures.BlockIcons.MACHINECASINGS_TOP[aMeta].getIcon();
        }
        return switch (aMeta) {
            case 1 -> Textures.BlockIcons.MACHINE_CASING_TANK_1.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_TANK_2.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_TANK_3.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_TANK_4.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_TANK_5.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_TANK_6.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_TANK_7.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_TANK_8.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TANK_9.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_TANK_10.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_TANK_11.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_TANK_12.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_TANK_13.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_TANK_14.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_TANK_0.getIcon();
        };
    }
}
