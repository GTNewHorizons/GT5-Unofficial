package gregtech.common.blocks;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import appeng.api.parts.IFacadeControl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.render.GTRendererBlock;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

public class BlockSheetMetal extends BlockStorage implements IBlockWithTextures, IFacadeControl {

    final Int2ObjectFunction<Material> materials;
    private final int maxMeta;

    public BlockSheetMetal(String aName, Int2ObjectFunction<Material> materials, int maxMeta) {
        super(ItemStorage.class, aName, net.minecraft.block.material.Material.iron);
        this.materials = materials;
        this.maxMeta = maxMeta;

        GregTechAPI.sAfterGTLoad.add(() -> {

            for (int i = 0; i < maxMeta; i++) {
                Material material = materials.get(i);

                if (material == null) continue;
                if (!MU.generatesPrefix(material, OrePrefixes.sheetmetal)) continue;

                OreDictionary.registerOre(
                    OrePrefixes.sheetmetal.oreDictName(MaterialUtils.internalName(material))
                        .toString(),
                    new ItemStack(this, 1, i));
            }
        });

        GregTechAPI.sAfterGTPostload.add(this::registerRecipes);
    }

    @Override
    public String getLocalizedName(int meta) {
        Material material = materials.get(meta);

        if (material == null) material = Materials2Materials.NULL;

        return OrePrefixes.sheetmetal.getLocalizedNameForItem(MaterialUtils.internalName(material));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item self, CreativeTabs tab, List<ItemStack> stacks) {
        for (int i = 0; i < maxMeta; i++) {
            Material material = materials.get(i);

            if (material == null) continue;
            if (!MU.generatesPrefix(material, OrePrefixes.sheetmetal)) continue;

            stacks.add(new ItemStack(self, 1, i));
        }
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.RENDER_ID;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Can render in both opaque (pass 0) and alpha-blended (pass 1) rendering passes.
     */
    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    private final Int2ObjectLinkedOpenHashMap<ITexture[][]> textureCache = new Int2ObjectLinkedOpenHashMap<>();

    @Override
    public synchronized @Nullable ITexture[][] getTextures(int meta) {
        ITexture[][] cached = textureCache.getAndMoveToFirst(meta);

        if (cached != null) return cached;

        Material material = materials.get(meta);

        ITexture texture;

        if (material != null) {
            texture = TextureFactory.builder()
                .addIcon(MaterialUtils.iconSet(material).mTextures[OrePrefixes.sheetmetal.getTextureIndex()])
                .setRGBA(MaterialUtils.rgba(material))
                .build();
        } else {
            texture = TextureFactory.builder()
                .addIcon(TextureSet.SET_NONE.mTextures[OrePrefixes.sheetmetal.getTextureIndex()])
                .build();
        }

        cached = new ITexture[][] { { texture }, { texture }, { texture }, { texture }, { texture }, { texture }, };

        textureCache.putAndMoveToFirst(meta, cached);

        while (textureCache.size() > 512) textureCache.removeLast();

        return cached;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        Material material = materials.get(aMeta);
        if (material == null) return null;
        return MaterialUtils.iconSet(material).mTextures[OrePrefixes.sheetmetal.getTextureIndex()].getIcon();
    }

    public void registerRecipes() {
        for (int i = 0; i < maxMeta; i++) {
            Material material = materials.get(i);

            if (material == null) continue;
            if (!MU.generatesPrefix(material, OrePrefixes.sheetmetal)) continue;
            if (MaterialUtils.hasSubTag(material, SubTag.NO_RECIPES)) continue;

            GTValues.RA.stdBuilder()
                .itemInputs(MU.partOf(material, OrePrefixes.plate, 2), GTUtility.getIntegratedCircuit(11))
                .itemOutputs(MU.partOf(material, OrePrefixes.sheetmetal, 1))
                .eut(TierEU.RECIPE_LV)
                .duration(10)
                .addTo(RecipeMaps.benderRecipes);
        }
    }

    @Override
    public boolean createFacadeForBlock(int meta) {
        return false;
    }
}
