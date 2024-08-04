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

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Naquadah Alloy Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Bloody Ichorium Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Draconium Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Wyvern Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Awakened Draconium Fusion Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Chaotic Fusion Casing");

        ItemList.DEFCCasingBase.set(new ItemStack(this, 1, 7));
        ItemList.DEFCCasingT1.set(new ItemStack(this, 1, 8));
        ItemList.DEFCCasingT2.set(new ItemStack(this, 1, 9));
        ItemList.DEFCCasingT3.set(new ItemStack(this, 1, 10));
        ItemList.DEFCCasingT4.set(new ItemStack(this, 1, 11));
        ItemList.DEFCCasingT5.set(new ItemStack(this, 1, 12));

        // Taking one texture slot :P
        Textures.BlockIcons.setCasingTexture((byte) 1, (byte) (15 + 48), TextureFactory.of(this, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 7 || meta > 12) return texture[0];
        return texture[meta - 7];
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
