package pers.gwyog.gtneioreplugin.plugin.item;

import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.LOG;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.plugin.block.BlockDimensionDisplay;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.plugin.renderer.ItemDimensionDisplayRenderer;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;

public class ItemDimensionDisplay extends ItemBlock {

    public ItemDimensionDisplay(Block block) {
        super(block);
        setCreativeTab(GTNEIOrePlugin.creativeTab);

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT) {
            MinecraftForgeClient.registerItemRenderer(this, new ItemDimensionDisplayRenderer());
        }
    }

    public static ItemStack getItem(String dimension) {
        Block block = ModBlocks.getBlock(dimension);
        if (block != null) {
            return new ItemStack(block);
        }
        if (dimension != null) {
            LOG.warn("Unknown dimension queried for ItemDimensionDisplay: " + dimension);
        }
        return null;
    }

    public static String getDimension(ItemStack stack) {
        if (stack.getItem() instanceof ItemDimensionDisplay) {
            return ((BlockDimensionDisplay) Block.getBlockFromItem(stack.getItem())).getDimension();
        }
        return null;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dimension = getDimension(stack);
        if (dimension != null) {
            return DimensionHelper.convertCondensedStringToToolTip(dimension)
                .get(0);
        }
        return super.getItemStackDisplayName(stack);
    }
}
