package gtPlusPlus.core.block.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtBlock;
import gtPlusPlus.core.material.Material;

public class BlockBaseModular extends BasicBlock {

    protected Material material;

    protected int blockColour;
    public BlockTypes blockType;
    protected String materialName;

    @SideOnly(Side.CLIENT)
    private IIcon blockIcon;

    private static final HashMap<String, Block> BLOCK_CACHE = new HashMap<>();

    public static Block getMaterialBlock(Material aMaterial, BlockTypes aType) {
        return BLOCK_CACHE.get(aMaterial.getUnlocalizedName() + "." + aType.name());
    }

    private static int getMaterialColour(Material material) {
        return material.getTextureSet().is_custom ? 0xFFFFFF : material.getRgbAsHex();
    }

    public BlockBaseModular(final Material material, final BlockTypes blockType) {
        this(material, blockType, getMaterialColour(material));
    }

    public BlockBaseModular(final Material material, final BlockTypes blockType, final int colour) {
        this(
            material.getUnlocalizedName(),
            material.getDefaultLocalName(),
            net.minecraft.block.material.Material.iron,
            blockType,
            colour,
            Math.min(Math.max(material.vTier, 1), 6));
        this.material = material;
        registerComponent();
        BLOCK_CACHE.put(material.getUnlocalizedName() + "." + blockType.name(), this);
    }

    protected BlockBaseModular(final String unlocalizedName, final String blockMaterialString,
        final net.minecraft.block.material.Material vanillaMaterial, final BlockTypes blockType, final int colour,
        final int miningLevel) {
        super(blockType, unlocalizedName, vanillaMaterial, miningLevel);
        this.setHarvestLevel(blockType.getHarvestTool(), miningLevel);
        this.setBlockTextureName(GTPlusPlus.ID + ":" + blockType.getTexture());
        this.blockColour = colour == 0 ? Dyes._NULL.toInt() : colour;
        this.blockType = blockType;
        this.materialName = blockMaterialString;
        this.setBlockName(String.format(this.blockType.getProperName(), this.materialName));
        GameRegistry.registerBlock(
            this,
            ItemBlockGtBlock.class,
            StringUtils.sanitizeString(blockType.getTexture() + unlocalizedName));
        switch (this.blockType) {
            case STANDARD -> GTOreDictUnificator
                .registerOre("block" + unifyMaterialName(materialName), new ItemStack(this));
            case FRAME, ORE -> GTOreDictUnificator
                .registerOre("frameGt" + unifyMaterialName(materialName), new ItemStack(this));
        }
        if (blockType == BlockTypes.FRAME) GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (blockType == BlockTypes.FRAME) GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (blockType == BlockTypes.FRAME) GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.onBlockAdded(world, x, y, z);
    }

    public static String unifyMaterialName(String rawMaterName) {
        return rawMaterName.replace(" ", "")
            .replace("-", "")
            .replace("_", "");
    }

    public void registerComponent() {

        if (this.material == null) {
            return;
        }

        final String name = material.getUnlocalizedName();

        // Register Component
        final Map<String, ItemStack> map = Material.mComponentMap.computeIfAbsent(name, x -> new HashMap<>());

        final String key = getKey(this.blockType);

        if (map.containsKey(key)) {
            return;
        }

        map.put(key, new ItemStack(this));
    }

    private static @NotNull String getKey(BlockTypes blockType) {
        switch (blockType) {
            case STANDARD -> {
                return OrePrefixes.block.getName();
            }
            case FRAME -> {
                return OrePrefixes.frameGt.getName();
            }
            case ORE -> {
                return OrePrefixes.ore.getName();
            }
        }
        return OrePrefixes.ore.getName();
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        if (this.blockType == BlockTypes.FRAME) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getLocalizedName() {
        return OrePrefixes.getLocalizedNameForItem(blockType.getProperName(), "%s", material);
    }

    @Override
    public String getUnlocalizedName() {
        return "block." + material.getUnlocalizedName()
            + "."
            + this.blockType.name()
                .toLowerCase();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public Material getMaterialEx() {
        return this.material;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iIcon) {
        if (this.material == null || this.blockType == BlockTypes.ORE) {
            this.blockIcon = iIcon.registerIcon(GTPlusPlus.ID + ":" + this.blockType.getTexture());
            return;
        }

        String metType = this.material.getTextureSet() != null ? this.material.getTextureSet().mSetName : "METALLIC";
        int tier = this.material.vTier;
        String aType = (this.blockType == BlockTypes.FRAME) ? "frameGt" : (tier <= 4 ? "block1" : "block5");

        this.blockIcon = Textures.BlockIcons.textureSetWithRegister(metType, "/" + aType, iIcon)
            .getIcon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return blockIcon;
    }

    @Override
    public int colorMultiplier(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4) {
        return this.blockColour;
    }

    @Override
    public int getRenderColor(final int aMeta) {
        return this.blockColour;
    }

    @Override
    public int getBlockColor() {
        return this.blockColour;
    }
}
