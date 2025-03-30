package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTLanguageManager;

public class BlockCasingsSteam extends BlockCasingsAbstract {

    public BlockCasingsSteam() {
        super(ItemCasings.class, "gt.blockcasingssteam", MaterialCasings.INSTANCE, 16);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Steamgate Ring Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Steamgate Chevron Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Vibration-Safe Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Extractinator Solid Base");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Iron Reinforced Wood");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Bronze Reinforced Wood");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Steel Reinforced Wood");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Breel Pipe Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Stronze-Wrapped Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Solar Boiling Cell");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Hydraulic Assembling Casing");

        ItemList.Steamgate_Ring_Block.set(new ItemStack(this, 1, 0));
        ItemList.Steamgate_Chevron_Block.set(new ItemStack(this, 1, 1));
        ItemList.Vibration_Safe_Casing.set(new ItemStack(this, 1, 2));
        ItemList.Extractinator_Casing.set(new ItemStack(this, 1, 3));
        ItemList.Iron_Wood_Casing.set(new ItemStack(this, 1, 4));
        ItemList.Bronze_Wood_Casing.set(new ItemStack(this, 1, 5));
        ItemList.Steel_Wood_Casing.set(new ItemStack(this, 1, 6));
        ItemList.Breel_Pipe_Casing.set(new ItemStack(this, 1, 7));
        ItemList.Stronze_Casing.set(new ItemStack(this, 1, 8));
        ItemList.Solar_Boiler_Cell.set(new ItemStack(this, 1, 9));
        ItemList.Hydraulic_Assembling_Casing.set(new ItemStack(this, 1, 10));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.STEAMGATE_CASING.getIcon();
            case 1 -> Textures.BlockIcons.STEAMGATE_CHEVRON_BLOCK.getIcon();
            case 2 -> Textures.BlockIcons.EXTRACTINATOR_CASING.getIcon();
            case 3 -> ordinalSide > 1 ? Textures.BlockIcons.EXTRACTINATOR_BASE.getIcon()
                : ordinalSide == 1 ? Textures.BlockIcons.EXTRACTINATOR_BASE_TOP.getIcon()
                    : Textures.BlockIcons.EXTRACTINATOR_BASE_BASE.getIcon();
            case 4 -> Textures.BlockIcons.CASING_WOOD_IRON.getIcon();
            case 5 -> Textures.BlockIcons.CASING_WOOD_BRONZE.getIcon();
            case 6 -> Textures.BlockIcons.CASING_WOOD_STEEL.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_PIPE_CASING_BREEL.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_STRONZE.getIcon();
            case 9 -> ordinalSide == 1 ? Textures.BlockIcons.SOLAR_CELL_TOP.getIcon()
                : Textures.BlockIcons.SOLAR_CELL_SIDE.getIcon();
            case 10 -> Textures.BlockIcons.HYDRAULIC_ASSEMBLING_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
        };
    }
}
