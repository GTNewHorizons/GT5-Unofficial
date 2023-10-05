package gregtech.api.ModernMaterials.Blocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseTileEntity;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.FrameBoxNewDumb;
import gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb.NewDumb;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxSimpleBlockRenderer;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.PartsClasses.IAssociatedMaterials;
import gregtech.api.ModernMaterials.PartsClasses.IGetItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public enum BlocksEnum implements IGetItem, IAssociatedMaterials {

    // Define new blocks here.
    FrameBox(
        "% LARP Box",
        FrameBoxNewDumb.class,
        FrameBoxTileEntity.class,
        new FrameBoxSimpleBlockRenderer()
    );

    public String getLocalisedName(final ModernMaterial material) {

        if (material == null) return "Error: NULL";

        return unlocalizedName.replace("%", material.getMaterialName());
    }

    public Class<? extends BaseTileEntity> getTileEntityClass() {
        return tileClass;
    }

    public Class<? extends NewDumb> getBlockClass() {
        return blockClass;
    }

    final private Class<? extends NewDumb> blockClass;
    final private Class<? extends BaseTileEntity> tileClass;
    final private ISimpleBlockRenderingHandler simpleBlockRenderingHandler;
    final private String unlocalizedName;
    private Item item;
    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();

    BlocksEnum(@NotNull final String unlocalizedName, @NotNull final Class<? extends NewDumb> blockClass, @NotNull final Class<? extends BaseTileEntity> tileClass, @NotNull ISimpleBlockRenderingHandler simpleBlockRenderingHandler) {
        this.unlocalizedName = unlocalizedName;

        this.blockClass = blockClass;
        this.tileClass = tileClass;
        this.simpleBlockRenderingHandler = simpleBlockRenderingHandler;
    }

    @Override
    public void setAssociatedItem(final Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public HashSet<ModernMaterial> getAssociatedMaterials() {
        return associatedMaterials;
    }

    @Override
    public void addAssociatedMaterial(final ModernMaterial modernMaterial) {
        associatedMaterials.add(modernMaterial);
    }

    public IItemRenderer getItemRenderer(final int materialID) {
        return itemRendererHashMap.get(materialID);
    }

    public TileEntitySpecialRenderer getBlockRenderer(final int materialID) {
        return tileEntitySpecialRendererHashMap.get(materialID);
    }

    public void setBlockRenderer(int materialID, final TileEntitySpecialRenderer tileEntitySpecialRenderer) {
        tileEntitySpecialRendererHashMap.put(materialID, tileEntitySpecialRenderer);
    }

    public int getRenderId() {
        return simpleBlockRenderingHandler.getRenderId();
    }

    public void setItemRenderer(int materialID, IItemRenderer itemRenderer) {
        itemRendererHashMap.put(materialID, itemRenderer);
    }

    // Material ID -> renderers.
    private final HashMap<Integer, IItemRenderer> itemRendererHashMap = new HashMap<>();
    private final HashMap<Integer, TileEntitySpecialRenderer> tileEntitySpecialRendererHashMap = new HashMap<>();

}
