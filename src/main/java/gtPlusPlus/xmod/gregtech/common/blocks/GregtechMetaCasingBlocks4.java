package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocks4 extends GregtechMetaCasingBlocksAbstract {

    public GregtechMetaCasingBlocks4() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.4", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            if (i == 0 || i == 1
                || i == 2
                || i == 4
                || i == 5
                || i == 6
                || i == 7
                || i == 8
                || i == 12
                || i == 13
                || i == 14
                || i == 15) {
                continue;
            }
            TAE.registerTexture(3, i, TextureFactory.of(this, i));
        }

        GregtechItemList.Casing_Industrial_Arc_Furnace.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Coil_QuantumForceTransformer.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Vacuum_Furnace.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_RocketEngine.set(new ItemStack(this, 1, 11));
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return getStaticIcon((byte) ordinalSide, aMeta);
    }

    public static IIcon getStaticIcon(final byte aSide, final int aMeta) {
        // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 3 -> Textures.BlockIcons.TEMPERED_ARC_FURNACE_CASING.getIcon();
                case 4 -> TexturesGtBlock.Casing_Coil_QFT.getIcon();
                case 9 -> TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
                case 10 -> aSide < 2 ? Textures.BlockIcons.VACUUM_CASING_TOP.getIcon() : Textures.BlockIcons.VACUUM_CASING_SIDE.getIcon();
                case 11 -> TexturesGtBlock.TEXTURE_CASING_ROCKETDYNE.getIcon();
                default -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
            };
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 3));
        aList.add(new ItemStack(aItem, 1, 4));

        aList.add(new ItemStack(aItem, 1, 10));
        aList.add(new ItemStack(aItem, 1, 11));
    }
}
