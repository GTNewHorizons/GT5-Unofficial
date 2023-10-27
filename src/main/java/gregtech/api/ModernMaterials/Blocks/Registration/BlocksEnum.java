package gregtech.api.ModernMaterials.Blocks.Registration;

import java.util.HashMap;
import java.util.HashSet;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import gregtech.api.ModernMaterials.Blocks.EarthOreNormal.EarthOreNormalBaseMaterialBlock;
import gregtech.api.ModernMaterials.Blocks.EarthOreNormal.NormalOreSimpleBlockRenderer;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxBaseMaterialBlock;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialTileEntity;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxSimpleBlockRenderer;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.PartsClasses.IEnumPart;

/**
 * Enum representation of block types with their associated details and materials.
 */
public enum BlocksEnum implements IEnumPart {

    //  Define new blocks here.
    FrameBox("% LARP Box", FrameBoxBaseMaterialBlock.class, new FrameBoxSimpleBlockRenderer()),
    EarthOreNormal("% Ore", EarthOreNormalBaseMaterialBlock.class, new NormalOreSimpleBlockRenderer(Blocks.stone, 0)),
    MoonOreNormal("% Ore", EarthOreNormalBaseMaterialBlock.class, new NormalOreSimpleBlockRenderer(Blocks.end_stone, 0)),
    MarsOreNormal("% Ore", EarthOreNormalBaseMaterialBlock.class, new NormalOreSimpleBlockRenderer(Blocks.netherrack, 0));


    private final String unlocalizedName;
    private final Class<? extends BaseMaterialBlock> blockClass;
    private final ISimpleBlockRenderingHandler simpleBlockRenderingHandler;

    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();
    private final HashSet<ModernMaterial> specialBlockRenderAssociatedMaterials = new HashSet<>();
    private HashSet<ModernMaterial> simpleBlockRenderAssociatedMaterials;

    private final HashMap<Integer, IItemRenderer> itemRendererHashMap = new HashMap<>();
    private final HashMap<Integer, TileEntitySpecialRenderer> tileEntitySpecialRendererHashMap = new HashMap<>();

    private Item item;

    /**
     * Constructs an instance of the enum with the given parameters.
     *
     * @param unlocalizedName             The unlocalized name for the block.
     * @param blockClass                  The class representing the block.
     * @param simpleBlockRenderingHandler The rendering handler for the block.
     */
    BlocksEnum(@NotNull final String unlocalizedName, @NotNull final Class<? extends BaseMaterialBlock> blockClass,
        @NotNull ISimpleBlockRenderingHandler simpleBlockRenderingHandler) {
        this.unlocalizedName = unlocalizedName;
        this.blockClass = blockClass;
        this.simpleBlockRenderingHandler = simpleBlockRenderingHandler;
    }

    /**
     * Returns the localized name of the block for a given material.
     *
     * @param material The material for which to get the localized name.
     * @return The localized name.
     */
    public String getLocalisedName(final ModernMaterial material) {
        return material == null ? "Error: NULL" : unlocalizedName.replace("%", material.getMaterialName());
    }

    public Class<? extends BaseMaterialBlock> getBlockClass() {
        return blockClass;
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

    // TODO: Collections.unmodifiableSet wrappers?
    public HashSet<ModernMaterial> getSpecialBlockRenderAssociatedMaterials() {
        return specialBlockRenderAssociatedMaterials;
    }

    public HashSet<ModernMaterial> getSimpleBlockRenderAssociatedMaterials() {
        if (simpleBlockRenderAssociatedMaterials == null) {
            simpleBlockRenderAssociatedMaterials = new HashSet<>(associatedMaterials);
            simpleBlockRenderAssociatedMaterials.removeAll(specialBlockRenderAssociatedMaterials);
        }
        return simpleBlockRenderAssociatedMaterials;
    }

    @Override
    public void addAssociatedMaterial(final ModernMaterial modernMaterial) {
        associatedMaterials.add(modernMaterial);
    }

    public void addSimpleBlockRenderAssociatedMaterial(final ModernMaterial modernMaterial) {
        simpleBlockRenderAssociatedMaterials.add(modernMaterial);
    }

    public void addSpecialBlockRenderAssociatedMaterial(final ModernMaterial modernMaterial) {
        specialBlockRenderAssociatedMaterials.add(modernMaterial);
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

    public Class<? extends TileEntity> getTileEntityClass() {
        return BaseMaterialTileEntity.class;
    }

    public TileEntitySpecialRenderer getSpecialRenderer(int id) {
        return tileEntitySpecialRendererHashMap.get(id);
    }
}
