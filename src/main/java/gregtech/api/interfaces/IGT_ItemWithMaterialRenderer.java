package gregtech.api.interfaces;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.render.items.GT_GeneratedMaterial_Renderer;

public interface IGT_ItemWithMaterialRenderer {

    /**
     * @return If allow using {@link gregtech.common.render.items.GT_MetaGenerated_Item_Renderer} to render item
     */
    boolean shouldUseCustomRenderer(int aMetaData);

    /**
     * @return Custom renderer of the Material with offset < 32000
     */
    GT_GeneratedMaterial_Renderer getMaterialRenderer(int aMetaData);

    /**
     * If this returns false, renderer falls back to {@link gregtech.common.render.items.GT_GeneratedItem_Renderer}
     */
    boolean allowMaterialRenderer(int aMetaData);

    /**
     * @return Icon the Material is going to be rendered with
     */
    IIcon getIcon(int aMetaData, int pass);

    /**
     * @return Icon of the Overlay (or null if there is no Icon)
     */
    IIcon getOverlayIcon(int aMetaData, int pass);

    /**
     * @return Color Modulation the Material is going to be rendered with.
     */
    short[] getRGBa(ItemStack aStack);

    @SideOnly(Side.CLIENT)
    default int getSpriteNumber() {
        if (this instanceof Item) {
            return ((Item) this).getSpriteNumber();
        } else {
            throw new RuntimeException(String.format("Class %s does not extend Item!", getClass()));
        }
    }

    @SideOnly(Side.CLIENT)
    default boolean requiresMultipleRenderPasses() {
        if (this instanceof Item) {
            return ((Item) this).requiresMultipleRenderPasses();
        } else {
            throw new RuntimeException(String.format("Class %s does not extend Item!", getClass()));
        }
    }

    default int getRenderPasses(int metadata) {
        if (this instanceof Item) {
            return ((Item) this).getRenderPasses(metadata);
        } else {
            throw new RuntimeException(String.format("Class %s does not extend Item!", getClass()));
        }
    }
}
