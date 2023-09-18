package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbTileEntity;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBlock;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.PartsClasses.IAssociatedMaterials;
import gregtech.api.ModernMaterials.PartsClasses.IGetItem;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public enum BlocksEnum implements IGetItem, IAssociatedMaterials {

    // Define new blocks here.
    FrameBox("% LARP Box", FrameBoxBlock.class, FrameBoxTileEntity.class);

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

    BlocksEnum(@NotNull final String unlocalizedName, @NotNull Class<? extends DumbBlock> blockClass, Class<? extends DumbTileEntity> tileClass) {
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

}
