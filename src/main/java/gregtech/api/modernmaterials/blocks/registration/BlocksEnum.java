package gregtech.api.modernmaterials.blocks.registration;

import java.util.HashMap;
import java.util.HashSet;

import gregtech.api.GregTech_API;
import gregtech.api.modernmaterials.blocks.blocktypes.blockof.BlockOfBaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.blocktypes.blockof.BlockOfSimpleBlockRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.ore.smallore.SmallOreBlock;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.FrameBoxBaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.blocktypes.framebox.FrameBoxSimpleBlockRenderer;
import gregtech.api.modernmaterials.blocks.blocktypes.ore.normalore.NormalOreBlock;
import gregtech.api.modernmaterials.blocks.blocktypes.ore.OreBlockRenderer;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialTileEntity;
import gregtech.api.modernmaterials.items.partclasses.IEnumPart;
import gregtech.api.modernmaterials.ModernMaterial;

/**
 * Enum representation of block types with their associated details and materials.
 */
public enum BlocksEnum implements IEnumPart {


    // Define new blocks here.
    FrameBox("% Frame Box", FrameBoxBaseMaterialBlock.class, new FrameBoxSimpleBlockRenderer()),
    SolidBlock("Block of %", BlockOfBaseMaterialBlock.class, new BlockOfSimpleBlockRenderer()),


    EarthNormalOre("% Ore", NormalOreBlock.class, new OreBlockRenderer(Blocks.stone, 0)),
    NetherNormalOre("% Nether Ore", NormalOreBlock.class, new OreBlockRenderer(Blocks.netherrack, 0)),
    EndNormalOre("% End Ore", NormalOreBlock.class, new OreBlockRenderer(Blocks.end_stone, 0)),
    BlackGraniteNormalOre("% Black Granite Ore", NormalOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockGranites, 0)),
    RedGraniteNormalOre("% Red Granite Ore", NormalOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockGranites, 8)),
    MarbleNormalOre("% Marble Ore", NormalOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockStones, 0)),
    BasaltNormalOre("% Basalt Ore", NormalOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockStones, 8)),
    MoonNormalOre("% Moon Ore", NormalOreBlock.class, new OreBlockRenderer("GalacticraftCore:tile.moonBlock", 4)), // Repeat for other planets, proof of concept.


    EarthSmallOre("Small % Ore", SmallOreBlock.class, new OreBlockRenderer(Blocks.stone, 0)),
    NetherSmallOre("Small % Nether Ore", SmallOreBlock.class, new OreBlockRenderer(Blocks.netherrack, 0)),
    EndSmallOre("Small % End Ore", SmallOreBlock.class, new OreBlockRenderer(Blocks.end_stone, 0)),
    BlackGraniteSmallOre("Small % Black Granite Ore", SmallOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockGranites, 0)),
    RedGraniteSmallOre("Small % Red Granite Ore", SmallOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockGranites, 8)),
    MarbleSmallOre("Small % Marble Ore", SmallOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockStones, 0)),
    BasaltSmallOre("Small % Basalt Ore", SmallOreBlock.class, new OreBlockRenderer(GregTech_API.sBlockStones, 8)),
    MoonSmallOre("Small % Moon Ore", SmallOreBlock.class, new OreBlockRenderer("GalacticraftCore:tile.moonBlock", 4)); // Repeat for other planets, proof of concept.



    private final String unlocalizedName;
    private final Class<? extends BaseMaterialBlock> blockClass;
    private final ISimpleBlockRenderingHandler simpleBlockRenderingHandler;

    private final HashSet<ModernMaterial> associatedMaterials = new HashSet<>();
    private final HashSet<ModernMaterial> specialBlockRenderAssociatedMaterials = new HashSet<>();

    private HashSet<ModernMaterial> simpleBlockRenderAssociatedMaterials;

    private final HashMap<Integer, IItemRenderer> itemRendererHashMap = new HashMap<>();
    private final HashMap<Integer, TileEntitySpecialRenderer> tileEntitySpecialRendererHashMap = new HashMap<>();

    private final HashMap<ModernMaterial, Item> materialIDToItem = new HashMap<>();
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
        return unlocalizedName.replace("%", material.getMaterialName());
    }

    @Override
    public @NotNull ItemStack getPart(@NotNull ModernMaterial material, int stackSize) {
        return new ItemStack(getItem(material), stackSize, material.getMaterialID());
    }


    public Class<? extends BaseMaterialBlock> getBlockClass() {
        return blockClass;
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

    public void setItem(@NotNull ModernMaterial material, @NotNull Item item) {
        Item val = materialIDToItem.putIfAbsent(material, item);
        if (val != null) throw new RuntimeException("Material " + material + " already has an item set for " + this);
    }

    public Item getItem(@NotNull ModernMaterial material) {
        return materialIDToItem.getOrDefault(material, null);
    }

    @Override
    public void addAssociatedMaterial(final ModernMaterial modernMaterial) {
        associatedMaterials.add(modernMaterial);
    }

    public void addSpecialBlockRenderAssociatedMaterial(final ModernMaterial modernMaterial) {
        specialBlockRenderAssociatedMaterials.add(modernMaterial);
    }

    public IItemRenderer getItemRenderer(final int materialID) {
        return itemRendererHashMap.get(materialID);
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
