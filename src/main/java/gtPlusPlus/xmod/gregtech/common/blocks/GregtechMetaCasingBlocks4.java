package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocks4 extends GregtechMetaCasingBlocksAbstract {

    public GregtechMetaCasingBlocks4() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.4", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            if (i == 2 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 12 || i == 13 || i == 14 || i == 15) {
                continue;
            }
            TAE.registerTexture(3, i, TextureFactory.of(this, i));
        }
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Tempered Arc Furnace Casing");
        GTLanguageManager
            .addStringLocalization(this.getUnlocalizedName() + ".4.name", "Quantum Force Transformer Coil Casings");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Vacuum Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Turbodyne Casing");

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
            switch (aMeta) {
                case 0 -> {
                    return TexturesGtBlock.Casing_Trinium_Titanium.getIcon();
                }
                case 1 -> {
                    return TexturesGtBlock.TEXTURE_TECH_C.getIcon();
                }
                case 2 -> {
                    return TexturesGtBlock.TEXTURE_ORGANIC_PANEL_A_GLOWING.getIcon();
                }
                case 3 -> {
                    return TexturesGtBlock.TEXTURE_METAL_PANEL_A.getIcon();
                }
                case 4 -> {
                    return TexturesGtBlock.Casing_Coil_QFT.getIcon();
                }
                case 5, 6, 7, 8, 12, 13, 14, 15 -> {
                    return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
                }
                case 10 -> {
                    if (aSide < 2) {
                        return TexturesGtBlock.TEXTURE_STONE_RED_B.getIcon();
                    } else {
                        return TexturesGtBlock.TEXTURE_STONE_RED_A.getIcon();
                    }
                }
                case 11 -> {
                    return TexturesGtBlock.TEXTURE_CASING_ROCKETDYNE.getIcon();
                }
                default -> {
                    return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
                }
            }
        }
        return TexturesGtBlock._PlaceHolder.getIcon();
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
        aList.add(new ItemStack(aItem, 1, 1));
        aList.add(new ItemStack(aItem, 1, 2));
        aList.add(new ItemStack(aItem, 1, 3));
        aList.add(new ItemStack(aItem, 1, 4));

        aList.add(new ItemStack(aItem, 1, 10));
        aList.add(new ItemStack(aItem, 1, 11));
    }
}
