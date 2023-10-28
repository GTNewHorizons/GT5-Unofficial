package kubatech.loaders.block.defc;

import static kubatech.kubatech.KT;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import kubatech.Tags;
import kubatech.api.enums.ItemList;

public class DEFCCasingBlock extends GT_Block_Casings_Abstract {

    @SideOnly(Side.CLIENT)
    private IIcon[] texture;

    public DEFCCasingBlock() {
        super(DEFCCasingItemBlock.class, "defc.casing", Material.anvil);
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        this.setCreativeTab(KT);

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Naquadah Alloy Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Bloody Ichorium Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Draconium Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Wyvern Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Awakened Draconium Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Chaotic Fusion Casing");

        ItemList.DEFCCasingBase.set(new ItemStack(this, 1, 0));
        ItemList.DEFCCasingT1.set(new ItemStack(this, 1, 1));
        ItemList.DEFCCasingT2.set(new ItemStack(this, 1, 2));
        ItemList.DEFCCasingT3.set(new ItemStack(this, 1, 3));
        ItemList.DEFCCasingT4.set(new ItemStack(this, 1, 4));
        ItemList.DEFCCasingT5.set(new ItemStack(this, 1, 5));

        // Taking one texture slot :P
        Textures.BlockIcons.setCasingTexture((byte) 1, (byte) (15 + 48), TextureFactory.of(this, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return texture[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {
        texture = new IIcon[6];
        for (int i = 0; i < texture.length; i++) {
            texture[i] = aIconRegister.registerIcon(Tags.MODID + ":casing/defc_" + i);
        }
    }
}
