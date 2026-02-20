package kubatech.loaders.block.defc;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.translatedText;
import static kubatech.kubatech.KT;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.ItemCasings;
import kubatech.Tags;
import kubatech.api.enums.ItemList;

public class BlockDEFCCasing extends BlockCasingsAbstract {

    @SideOnly(Side.CLIENT)
    private IIcon[] texture;

    public BlockDEFCCasing() {
        super(ItemCasings.class, "defc.casing", Material.anvil);
        this.setHardness(15.0F);
        this.setResistance(30.0F);
        this.setCreativeTab(KT);

        register(7, ItemList.DEFCCasingBase);
        register(8, ItemList.DEFCCasingT1, translatedText("defc.casing.tip", 1));
        register(9, ItemList.DEFCCasingT2, translatedText("defc.casing.tip", 2));
        register(10, ItemList.DEFCCasingT3, translatedText("defc.casing.tip", 3));
        register(11, ItemList.DEFCCasingT4, translatedText("defc.casing.tip", 4));
        register(12, ItemList.DEFCCasingT5, translatedText("defc.casing.tip", 5));

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
