package gtPlusPlus.core.block.base;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.itemblock.ItemBlockGtBlock;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BlockBaseModular extends BasicBlock {

    protected Material blockMaterial;

    protected int blockColour;
    public BlockTypes thisBlock;
    protected String thisBlockMaterial;
    protected String thisBlockMaterialTranslatedName;
    protected final String thisBlockType;

    private static final HashMap<String, Block> sBlockCache = new HashMap<>();

    public static Block getMaterialBlock(Material aMaterial, BlockTypes aType) {
        return sBlockCache.get(aMaterial.getUnlocalizedName() + "." + aType.name());
    }

    public BlockBaseModular(final Material material, final BlockTypes blockType) {
        this(material, blockType, material.getRgbAsHex());
    }

    public BlockBaseModular(final Material material, final BlockTypes blockType, final int colour) {
        this(
                material.getUnlocalizedName(),
                material.getLocalizedName(),
                net.minecraft.block.material.Material.iron,
                blockType,
                colour,
                Math.min(Math.max(material.vTier, 1), 6));
        blockMaterial = material;
        registerComponent();
        sBlockCache.put(material.getUnlocalizedName() + "." + blockType.name(), this);
        thisBlockMaterialTranslatedName = material.getTranslatedName();
        GT_LanguageManager.addStringLocalization("gtplusplus." + getUnlocalizedName() + ".name", getProperName());
    }

    protected BlockBaseModular(final String unlocalizedName, final String blockMaterialString,
            final net.minecraft.block.material.Material vanillaMaterial, final BlockTypes blockType, final int colour,
            final int miningLevel) {
        super(blockType, unlocalizedName, vanillaMaterial, miningLevel);
        this.setHarvestLevel(blockType.getHarvestTool(), miningLevel);
        this.setBlockTextureName(GTPlusPlus.ID + ":" + blockType.getTexture());
        this.blockColour = colour;
        this.thisBlock = blockType;
        this.thisBlockMaterial = blockMaterialString;
        this.thisBlockType = blockType.name().toUpperCase();
        this.setBlockName(this.getUnlocalizedProperName());
        int fx = getBlockTypeMeta();
        GameRegistry.registerBlock(
                this,
                ItemBlockGtBlock.class,
                Utils.sanitizeString(blockType.getTexture() + unlocalizedName));
        if (fx == 0) {
            GT_OreDictUnificator
                    .registerOre("block" + unifyMaterialName(thisBlockMaterial), ItemUtils.getSimpleStack(this));
        } else if (fx == 1) {
            GT_OreDictUnificator
                    .registerOre("frameGt" + unifyMaterialName(thisBlockMaterial), ItemUtils.getSimpleStack(this));
        } else if (fx == 2) {
            GT_OreDictUnificator
                    .registerOre("frameGt" + unifyMaterialName(thisBlockMaterial), ItemUtils.getSimpleStack(this));
        }
    }

    public static String unifyMaterialName(String rawMaterName) {
        return rawMaterName.replace(" ", "").replace("-", "").replace("_", "");
    }

    public void registerComponent() {
        Logger.MATERIALS("Attempting to register " + this.getUnlocalizedName() + ".");
        if (this.blockMaterial == null) {
            Logger.MATERIALS("Tried to register " + this.getUnlocalizedName() + " but the material was null.");
            return;
        }
        String aName = blockMaterial.getUnlocalizedName();
        // Register Component
        Map<String, ItemStack> aMap = Material.mComponentMap.get(aName);
        if (aMap == null) {
            aMap = new HashMap<>();
        }
        int fx = getBlockTypeMeta();
        String aKey = (fx == 0 ? OrePrefixes.block.name()
                : (fx == 1 ? OrePrefixes.frameGt.name() : OrePrefixes.ore.name()));
        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, ItemUtils.getSimpleStack(this));
            Logger.MATERIALS("Registering a material component. Item: [" + aName + "] Map: [" + aKey + "]");
            Material.mComponentMap.put(aName, aMap);
        } else {
            // Bad
            Logger.MATERIALS("Tried to double register a material component.");
        }
    }

    public int getBlockTypeMeta() {
        if (this.thisBlockType.equals(BlockTypes.STANDARD.name().toUpperCase())) {
            return 0;
        } else if (this.thisBlockType.equals(BlockTypes.FRAME.name().toUpperCase())) {
            return 1;
        } else if (this.thisBlockType.equals(BlockTypes.ORE.name().toUpperCase())) {
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
        String tempIngot = null;
        if (this.thisBlock == BlockTypes.STANDARD) {
            tempIngot = "Block of %material";
        } else if (this.thisBlock == BlockTypes.FRAME) {
            tempIngot = "%material Frame Box";
        } else if (this.thisBlock == BlockTypes.ORE) {
            tempIngot = "%material Ore [Old]";
        }
        return tempIngot;
    }

    public String getUnlocalizedProperName() {
        return getProperName().replace("%s", "%temp").replace("%material", this.thisBlockMaterial)
                .replace("%temp", "%s");
    }

    @Override
    public String getLocalizedName() {
        return GT_LanguageManager.getTranslation("gtplusplus." + getUnlocalizedName() + ".name").replace("%s", "%temp")
                .replace("%material", this.thisBlockMaterialTranslatedName).replace("%temp", "%s");
    }

    @Override
    public String getUnlocalizedName() {
        return "block." + blockMaterial.getUnlocalizedName() + "." + this.thisBlock.name().toLowerCase();
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
        if (!CORE.ConfigSwitches.useGregtechTextures || this.blockMaterial == null
                || this.thisBlock == BlockTypes.ORE) {
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

        if (this.blockColour == 0) {
            return MathUtils.generateSingularRandomHexValue();
        }

        return this.blockColour;
    }

    @Override
    public int getRenderColor(final int aMeta) {
        if (this.blockColour == 0) {
            return MathUtils.generateSingularRandomHexValue();
        }

        return this.blockColour;
    }

    @Override
    public int getBlockColor() {
        if (this.blockColour == 0) {
            return MathUtils.generateSingularRandomHexValue();
        }

        return this.blockColour;
    }
}
