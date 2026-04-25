package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings11 extends BlockCasingsAbstract {

    public BlockCasings11() {
        super(ItemCasings.class, "gt.blockcasings11", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Item_Pipe_Tin);
        register(1, ItemList.Casing_Item_Pipe_Brass);
        register(2, ItemList.Casing_Item_Pipe_Electrum);
        register(3, ItemList.Casing_Item_Pipe_Platinum);
        register(4, ItemList.Casing_Item_Pipe_Osmium);
        register(5, ItemList.Casing_Item_Pipe_Quantium);
        register(6, ItemList.Casing_Item_Pipe_Fluxed_Electrum);
        register(7, ItemList.Casing_Item_Pipe_Black_Plutonium);

        for (int i = 0; i < 8; i++) {
            GTStructureChannels.ITEM_PIPE_CASING.registerAsIndicator(new ItemStack(this, 1, i), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 64);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return switch (meta) {
            case 1 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_BRASS.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_ELECTRUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_PLATINUM.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_OSMIUM.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_QUANTIUM.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_FLUXED_ELECTRUM.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_BLACK_PLUTONIUM.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ITEM_PIPE_TIN.getIcon();
        };
    }
}
