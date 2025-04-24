package gregtech.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings11 extends BlockCasingsAbstract {

    public BlockCasings11() {
        super(ItemCasings.class, "gt.blockcasings11", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_Item_Pipe_Tin, "Tin Item Pipe Casing");
        register(1, ItemList.Casing_Item_Pipe_Brass, "Brass Item Pipe Casing");
        register(2, ItemList.Casing_Item_Pipe_Electrum, "Electrum Item Pipe Casing");
        register(3, ItemList.Casing_Item_Pipe_Platinum, "Platinum Item Pipe Casing");
        register(4, ItemList.Casing_Item_Pipe_Osmium, "Osmium Item Pipe Casing");
        register(5, ItemList.Casing_Item_Pipe_Quantium, "Quantium Item Pipe Casing");
        register(6, ItemList.Casing_Item_Pipe_Fluxed_Electrum, "Fluxed Electrum Item Pipe Casing");
        register(7, ItemList.Casing_Item_Pipe_Black_Plutonium, "Black Plutonium Item Pipe Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 64);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
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

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);

        tooltip.add(
            StatCollector
                .translateToLocalFormatted("GT5U.tooltip.channelvalue", stack.getItemDamage() + 1, "item_pipe"));
    }
}
