package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Nullable;

import appeng.api.parts.IFacadeControl;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.render.GTRendererBlock;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

public class BlockSheetMetal extends BlockStorage implements IBlockWithTextures, IFacadeControl {

    final Int2ObjectFunction<IOreMaterial> materials;
    private final int maxMeta;

    public BlockSheetMetal(String aName, Int2ObjectFunction<IOreMaterial> materials, int maxMeta) {
        super(ItemStorage.class, aName, Material.iron);
        this.materials = materials;
        this.maxMeta = maxMeta;

        GregTechAPI.sAfterGTLoad.add(() -> {
            WerkstoffLoader.load();

            for (int i = 0; i < maxMeta; i++) {
                IOreMaterial material = materials.get(i);

                if (material == null) continue;
                if (!material.generatesPrefix(OrePrefixes.sheetmetal)) continue;

                OreDictionary.registerOre(
                    OrePrefixes.sheetmetal.get(material.getInternalName())
                        .toString(),
                    new ItemStack(this, 1, i));
            }
        });

        GregTechAPI.sAfterGTPostload.add(this::registerRecipes);
    }

    @Override
    public String getLocalizedName(int meta) {
        IOreMaterial material = materials.get(meta);

        if (material == null) material = Materials._NULL;

        Materials gt = material.getGTMaterial();

        if (gt != null) {
            return OrePrefixes.sheetmetal.getLocalizedNameForItem(gt);
        }

        return OrePrefixes.block.getDefaultLocalNameForItem(Materials._NULL);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item self, CreativeTabs tab, List<ItemStack> stacks) {
        for (int i = 0; i < maxMeta; i++) {
            IOreMaterial material = materials.get(i);

            if (material == null) continue;
            if (!material.generatesPrefix(OrePrefixes.sheetmetal)) continue;

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

        IOreMaterial material = materials.get(meta);

        ITexture texture;

        if (material != null) {
            texture = TextureFactory.builder()
                .addIcon(material.getTextureSet().mTextures[OrePrefixes.sheetmetal.getTextureIndex()])
                .setRGBA(material.getRGBA())
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

    public void registerRecipes() {
        for (int i = 0; i < maxMeta; i++) {
            IOreMaterial material = materials.get(i);

            if (material == null) continue;
            if (!material.generatesPrefix(OrePrefixes.sheetmetal)) continue;
            if (material.contains(SubTag.NO_RECIPES)) continue;

            GTValues.RA.stdBuilder()
                .itemInputs(material.getPart(OrePrefixes.plate, 2), GTUtility.getIntegratedCircuit(11))
                .itemOutputs(material.getPart(OrePrefixes.sheetmetal, 1))
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
