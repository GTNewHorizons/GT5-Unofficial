package gtPlusPlus.core.block.base;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtBlock;
import gtPlusPlus.core.material.Material;

public class BlockBaseModular extends BasicBlock {

    protected Material blockMaterial;

    protected int blockColour;
    public BlockTypes thisBlock;
    protected String thisBlockMaterial;
    protected final String thisBlockType;

    private static final HashMap<String, Block> sBlockCache = new HashMap<>();

    public static Block getMaterialBlock(Material aMaterial, BlockTypes aType) {
        return sBlockCache.get(aMaterial.getUnlocalizedName() + "." + aType.name());
    }

    public BlockBaseModular(final Material material, final BlockTypes blockType) {
        this(material, blockType, material.getTextureSet().is_custom ? 0xFFFFFF : material.getRgbAsHex());
    }

    public BlockBaseModular(final Material material, final BlockTypes blockType, final int colour) {
        this(
            material.getUnlocalizedName(),
            material.getDefaultLocalName(),
            net.minecraft.block.material.Material.iron,
            blockType,
            colour,
            Math.min(Math.max(material.vTier, 1), 6));
        blockMaterial = material;
        registerComponent();
        sBlockCache.put(material.getUnlocalizedName() + "." + blockType.name(), this);
    }

    protected BlockBaseModular(final String unlocalizedName, final String blockMaterialString,
        final net.minecraft.block.material.Material vanillaMaterial, final BlockTypes blockType, final int colour,
        final int miningLevel) {
        super(blockType, unlocalizedName, vanillaMaterial, miningLevel);
        this.setHarvestLevel(blockType.getHarvestTool(), miningLevel);
        this.setBlockTextureName(GTPlusPlus.ID + ":" + blockType.getTexture());
        this.blockColour = colour == 0 ? Dyes._NULL.toInt() : colour;
        this.thisBlock = blockType;
        this.thisBlockMaterial = blockMaterialString;
        this.thisBlockType = blockType.name()
            .toUpperCase();
        this.setBlockName(this.getUnlocalizedProperName());
        int fx = getBlockTypeMeta();
        GameRegistry.registerBlock(
            this,
            ItemBlockGtBlock.class,
            StringUtils.sanitizeString(blockType.getTexture() + unlocalizedName));
        if (fx == 0) {
            GTOreDictUnificator.registerOre("block" + unifyMaterialName(thisBlockMaterial), new ItemStack(this));
        } else if (fx == 1) {
            GTOreDictUnificator.registerOre("frameGt" + unifyMaterialName(thisBlockMaterial), new ItemStack(this));
        } else if (fx == 2) {
            GTOreDictUnificator.registerOre("frameGt" + unifyMaterialName(thisBlockMaterial), new ItemStack(this));
        }
    }

    public static String unifyMaterialName(String rawMaterName) {
        return rawMaterName.replace(" ", "")
            .replace("-", "")
            .replace("_", "");
    }

    public void registerComponent() {
        Logger.MATERIALS("Attempting to register " + this.getUnlocalizedName() + ".");

        if (this.blockMaterial == null) {
            Logger.MATERIALS("Tried to register " + this.getUnlocalizedName() + " but the material was null.");
            return;
        }

        final String name = blockMaterial.getUnlocalizedName();

        // Register Component
        final Map<String, ItemStack> map = Material.mComponentMap.computeIfAbsent(name, x -> new HashMap<>());

        final String key = getKey(getBlockTypeMeta());

        if (map.containsKey(key)) {
            Logger.MATERIALS("Tried to double register a material component.");
            return;
        }

        Logger.MATERIALS("Registering a material component. Item: [" + name + "] Map: [" + key + "]");
        map.put(key, new ItemStack(this));
    }

    private static @NotNull String getKey(int fx) {
        if (fx == 0) return OrePrefixes.block.getName();
        if (fx == 1) return OrePrefixes.frameGt.getName();
        return OrePrefixes.ore.getName();
    }

    public int getBlockTypeMeta() {
        if (this.thisBlockType.equals(
            BlockTypes.STANDARD.name()
                .toUpperCase())) {
            return 0;
        } else if (this.thisBlockType.equals(
            BlockTypes.FRAME.name()
                .toUpperCase())) {
                    return 1;
                } else
            if (this.thisBlockType.equals(
                BlockTypes.ORE.name()
                    .toUpperCase())) {
                        return 2;
                    }
        return 0;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        if (this.thisBlock == BlockTypes.FRAME) {
            return 1;
        }
        return 0;
    }

    public String getProperName() {
        switch (this.thisBlock) {
            case STANDARD -> {
                return "Block of %s";
            }
            case FRAME -> {
                return "%s Frame Box";
            }
            case ORE -> {
                return "%s Ore [Old]";
            }
        }
        return null;
    }

    public String getUnlocalizedProperName() {
        return String.format(getProperName(), this.thisBlockMaterial);
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocalFormatted(
            OrePrefixes.getOreprefixKey(getProperName(), "%s"),
            this.blockMaterial.getLocalizedName());
    }

    @Override
    public String getUnlocalizedName() {
        return "block." + blockMaterial.getUnlocalizedName()
            + "."
            + this.thisBlock.name()
                .toLowerCase();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public Material getMaterialEx() {
        return this.blockMaterial;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister iIcon) {
        if (this.blockMaterial == null || this.thisBlock == BlockTypes.ORE) {
            this.blockIcon = iIcon.registerIcon(GTPlusPlus.ID + ":" + this.thisBlock.getTexture());
        }
        String metType = "9j4852jyo3rjmh3owlhw9oe";
        if (this.blockMaterial != null) {
            TextureSet u = this.blockMaterial.getTextureSet();
            if (u != null) {
                metType = u.mSetName;
            }
        }
        metType = (metType.equals("9j4852jyo3rjmh3owlhw9oe") ? "METALLIC" : metType);
        int tier = blockMaterial != null ? this.blockMaterial.vTier : 0;
        String aType = (this.thisBlock == BlockTypes.FRAME) ? "frameGt" : (tier <= 4 ? "block1" : "block5");
        this.blockIcon = iIcon.registerIcon(GregTech.ID + ":" + "materialicons/" + metType + "/" + aType);
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
