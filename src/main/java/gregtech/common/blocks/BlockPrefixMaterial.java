package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Nullable;

import appeng.api.parts.IFacadeControl;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.common.render.GTRendererBlock;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

public abstract class BlockPrefixMaterial extends BlockStorage implements IBlockWithTextures, IFacadeControl {

    protected final Int2ObjectFunction<IOreMaterial> materials;
    protected final int maxMeta;
    protected final OrePrefixes prefix;

    public BlockPrefixMaterial(String aName, OrePrefixes prefix, Int2ObjectFunction<IOreMaterial> materials,
        int maxMeta) {
        super(ItemStorage.class, aName, Material.iron);
        this.prefix = prefix;
        this.materials = materials;
        this.maxMeta = maxMeta;

        GregTechAPI.sAfterGTLoad.add(() -> {
            WerkstoffLoader.load();

            for (int i = 0; i < maxMeta; i++) {
                IOreMaterial material = materials.get(i);

                if (material == null) continue;
                if (!material.generatesPrefix(prefix)) continue;

                OreDictionary.registerOre(
                    prefix.get(material.getInternalName())
                        .toString(),
                    new ItemStack(this, 1, i));
            }
        });

        GregTechAPI.sAfterGTPostload.add(this::registerRecipes);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public String getLocalizedName(int meta) {
        IOreMaterial material = materials.get(meta);

        if (material == null) material = Materials._NULL;

        return prefix.getLocalizedNameForItem(material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item self, CreativeTabs tab, List<ItemStack> stacks) {
        for (int i = 0; i < maxMeta; i++) {
            IOreMaterial material = materials.get(i);

            if (material == null) continue;
            if (!material.generatesPrefix(prefix)) continue;

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
                .addIcon(material.getTextureSet().mTextures[prefix.getTextureIndex()])
                .setRGBA(material.getRGBA())
                .build();
        } else {
            texture = TextureFactory.builder()
                .addIcon(TextureSet.SET_NONE.mTextures[prefix.getTextureIndex()])
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
        IOreMaterial material = materials.get(aMeta);
        if (material == null) return null;
        return material.getTextureSet().mTextures[prefix.getTextureIndex()].getIcon();
    }

    public abstract void registerRecipes();

    @Override
    public boolean createFacadeForBlock(int meta) {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.onBlockAdded(world, x, y, z);
    }
}
