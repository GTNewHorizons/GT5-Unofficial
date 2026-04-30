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
public class BlockCasings14 extends BlockCasingsAbstract {

    public BlockCasings14() {
        super(ItemCasings.class, "gt.blockcasings14", MaterialCasings.INSTANCE, 16);

        register(4, ItemList.FridgeCasing);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 4 -> {
                if (ordinalSide == 0) yield Textures.BlockIcons.MACHINE_CASING_FRIDGE_BOTTOM.getIcon();
                if (ordinalSide == 1) yield Textures.BlockIcons.MACHINE_CASING_FRIDGE_TOP.getIcon();
                yield Textures.BlockIcons.MACHINE_CASING_FRIDGE_SIDE.getIcon();
            }
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 112);
    }
}
