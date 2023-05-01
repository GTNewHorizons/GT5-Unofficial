package common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import common.itemBlocks.IB_TFFTStorageField;
import common.tileentities.GTMTE_TFFT;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import kekztech.KekzCore;

public class Block_TFFTStorageField extends BaseGTUpdateableBlock {

    private static final Block_TFFTStorageField INSTANCE = new Block_TFFTStorageField();
    private static final int SUB_BLOCK_COUNT = GTMTE_TFFT.Field.VALUES.length + 1;
    private static final IIcon[] textures = new IIcon[SUB_BLOCK_COUNT];

    public enum TFFTCasingIcon implements IIconContainer {

        INSTANCE;

        @Override
        public IIcon getIcon() {
            return textures[0];
        }

        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    // I guess glodblock won't mind
    static {
        GT_Utility.addTexturePage((byte) 12);
        Textures.BlockIcons.setCasingTexture(
                (byte) 12,
                (byte) 127,
                TextureFactory.of(Block_TFFTStorageField.TFFTCasingIcon.INSTANCE));
    }

    private Block_TFFTStorageField() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_tfftstoragefield_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(5.0f);
        INSTANCE.setResistance(6.0f);
        GameRegistry.registerBlock(INSTANCE, IB_TFFTStorageField.class, blockName);

        return INSTANCE;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        textures[0] = ir.registerIcon(KekzCore.MODID + ":" + "TFFTCasing");
        for (int i = 1; i < SUB_BLOCK_COUNT; i++) {
            textures[i] = ir.registerIcon(KekzCore.MODID + ":" + "TFFTStorageFieldBlock" + i);
        }
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < SUB_BLOCK_COUNT; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return textures[meta];
    }
}
