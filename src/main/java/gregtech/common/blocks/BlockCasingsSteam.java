package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTLanguageManager;

public class BlockCasingsSteam extends BlockCasingsAbstract {

    public BlockCasingsSteam() {
        super(ItemCasings12.class, "gt.blockcasingssteam", MaterialCasings.INSTANCE, 16);
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Steamgate Ring Block");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Steamgate Chevron Block");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 1 -> ordinalSide > 1 ? Textures.BlockIcons.STEAMGATE_CHEVRON_BLOCK.getIcon()
                : Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
            default -> Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS.getIcon();
        };
    }
}
