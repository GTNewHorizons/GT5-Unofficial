package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbTileEntity;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import gregtech.api.ModernMaterials.Blocks.FrameBox.TESR.UniversiumFrameItemRenderer;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.PartsClasses.IAssociatedMaterials;
import gregtech.api.ModernMaterials.PartsClasses.IGetItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public enum BlocksEnum implements IGetItem, IAssociatedMaterials {

    // Define new blocks here.
    FrameBox(
        "% LARP Box",
        FrameBoxBlock.class,
        FrameBoxTileEntity.class
    );

    public String getLocalisedName(final ModernMaterial material) {
        return unlocalizedName.replace("%", material.getMaterialName());
    }

    public Class<? extends DumbTileEntity> getTileEntityClass() {
        return tileClass;
    }

    public Class<? extends DumbBlock> getBlockClass() {
        return blockClass;
    }

    final private Class<? extends DumbBlock> blockClass;
    final private Class<? extends DumbTileEntity> tileClass;

    final private String unlocalizedName;
    private Item item;
    private final ArrayList<ModernMaterial> associatedMaterials = new ArrayList<>();

    BlocksEnum(@NotNull final String unlocalizedName, @NotNull final Class<? extends DumbBlock> blockClass, @NotNull final Class<? extends DumbTileEntity> tileClass) {
        this.unlocalizedName = unlocalizedName;

        this.blockClass = blockClass;
        this.tileClass = tileClass;
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
    public ArrayList<ModernMaterial> getAssociatedMaterials() {
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

    public void setItemRenderer(int materialID, IItemRenderer itemRenderer) {
        itemRendererHashMap.put(materialID, itemRenderer);
    }

    // Material ID -> renderers.
    private final HashMap<Integer, IItemRenderer> itemRendererHashMap = new HashMap<>();
    private final HashMap<Integer, TileEntitySpecialRenderer> tileEntitySpecialRendererHashMap = new HashMap<>();

}
