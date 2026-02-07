package gregtech.common.blocks;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class BlockStonesAbstract extends GTGenericBlock implements IOreRecipeRegistrator {

    public BlockStonesAbstract(Class<? extends ItemBlock> aItemClass, String aName) {
        super(aItemClass, aName, Material.rock);
        OrePrefixes.crafting.add(this);
        setStepSound(soundTypeStone);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
        setHardness(4.5f);

        this.registerSmeltingRecipes();
        this.registerAssemblerRecipes();
        this.registerCraftingRecipes();
        this.registerForgeHammerRecipes();
    }

    private void registerSmeltingRecipes() {
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 0), new ItemStack(this, 1, 7));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 1), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 2), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 3), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 4), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 5), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 6), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 7), new ItemStack(this, 1, 0));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 8), new ItemStack(this, 1, 15));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 9), new ItemStack(this, 1, 8));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 10), new ItemStack(this, 1, 8));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 11), new ItemStack(this, 1, 8));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 12), new ItemStack(this, 1, 8));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 13), new ItemStack(this, 1, 8));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 14), new ItemStack(this, 1, 8));
        GTModHandler.addSmeltingRecipe(new ItemStack(this, 1, 15), new ItemStack(this, 1, 8));

    }

    private void registerAssemblerRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this, 1, 0))
            .circuit(4)
            .itemOutputs(new ItemStack(this, 1, 3))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this, 1, 8))
            .circuit(4)
            .itemOutputs(new ItemStack(this, 1, 11))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(assemblerRecipes);
    }

    private void registerCraftingRecipes() {
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 1, 6),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "f", "X", 'X', new ItemStack(this, 1, 7) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 1, 14),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "f", "X", 'X', new ItemStack(this, 1, 15) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 1, 4),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "h", "X", 'X', new ItemStack(this, 1, 3) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 1, 12),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "h", "X", 'X', new ItemStack(this, 1, 11) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 1, 1),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "h", "X", 'X', new ItemStack(this, 1, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 1, 9),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "h", "X", 'X', new ItemStack(this, 1, 8) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 4, 3),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XX", "XX", 'X', new ItemStack(this, 4, 0) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 4, 11),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XX", "XX", 'X', new ItemStack(this, 4, 8) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 4, 3),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XX", "XX", 'X', new ItemStack(this, 4, 7) });
        GTModHandler.addCraftingRecipe(
            new ItemStack(this, 4, 11),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "XX", "XX", 'X', new ItemStack(this, 4, 15) });
    }

    private void registerForgeHammerRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this, 1, 3))
            .itemOutputs(new ItemStack(this, 1, 4))
            .duration(16 * TICKS)
            .eut(10)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this, 1, 11))
            .itemOutputs(new ItemStack(this, 1, 12))
            .duration(16 * TICKS)
            .eut(10)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this, 1, 0))
            .itemOutputs(new ItemStack(this, 1, 1))
            .duration(16 * TICKS)
            .eut(10)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this, 1, 8))
            .itemOutputs(new ItemStack(this, 1, 9))
            .duration(16 * TICKS)
            .eut(10)
            .addTo(hammerRecipes);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.equals(OreDictNames.craftingLensWhite.toString())) {

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(this, 1, 7), GTUtility.copyAmount(0, aStack))
                .itemOutputs(new ItemStack(this, 1, 6))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(laserEngraverRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(this, 1, 15), GTUtility.copyAmount(0, aStack))
                .itemOutputs(new ItemStack(this, 1, 14))
                .duration(2 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(laserEngraverRecipes);

        }
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 1;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return this.blockHardness = Blocks.stone.getBlockHardness(aWorld, aX, aY, aZ) * 3.0F;
    }

    @Override
    public String getUnlocalizedName() {
        return this.mUnlocalizedName;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.mUnlocalizedName + ".name");
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return true;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return gregtech.api.enums.Textures.BlockIcons.GRANITES[aMeta].getIcon();
        }
        return null;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) % 8 < 3;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata % 8 == 0 ? metadata + 1 : metadata;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        for (int i = 0; i < 16; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
