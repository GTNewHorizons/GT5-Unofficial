package tectech.thing.casing;

import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.WHITE;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GTCopiedBlockTexture;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.MaterialCasings;
import tectech.thing.CustomItemList;
import tectech.util.CommonValues;

@SuppressWarnings("SpellCheckingInspection")
public class SpacetimeCompressionFieldCasing extends BlockCasingsAbstract {

    private static IIcon textureTier0;
    private static IIcon textureTier1;
    private static IIcon textureTier2;
    private static IIcon textureTier3;
    private static IIcon textureTier4;
    private static IIcon textureTier5;
    private static IIcon textureTier6;
    private static IIcon textureTier7;
    private static IIcon textureTier8;
    private static final int MAX_BLOCK_TIER = 9;

    private static final byte START_INDEX = 16;

    public SpacetimeCompressionFieldCasing() {
        super(ItemCasingsSpacetime.class, "gt.spacetime_compression_field_generator", MaterialCasings.INSTANCE);
        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[7][b + START_INDEX] = new GTCopiedBlockTexture(this, 6, b);
        }

        for (int i = 0; i < MAX_BLOCK_TIER; i++) {
            GTLanguageManager.addStringLocalization(
                getUnlocalizedName() + "." + i + ".name",
                WHITE + CommonValues.EOH_TIER_FANCY_NAMES[i] + RESET + " Spacetime Compression Field Generator");
        }

        CustomItemList.SpacetimeCompressionFieldGeneratorTier0.set(new ItemStack(this, 1, 0));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier1.set(new ItemStack(this, 1, 1));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier2.set(new ItemStack(this, 1, 2));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier3.set(new ItemStack(this, 1, 3));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier4.set(new ItemStack(this, 1, 4));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier5.set(new ItemStack(this, 1, 5));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier6.set(new ItemStack(this, 1, 6));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier7.set(new ItemStack(this, 1, 7));
        CustomItemList.SpacetimeCompressionFieldGeneratorTier8.set(new ItemStack(this, 1, 8));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        textureTier0 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_0");
        textureTier1 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_1");
        textureTier2 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_2");
        textureTier3 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_3");
        textureTier4 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_4");
        textureTier5 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_5");
        textureTier6 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_6");
        textureTier7 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_7");
        textureTier8 = aIconRegister.registerIcon("gregtech:iconsets/EM_DIM_8");
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return textureTier0;
            case 1:
                return textureTier1;
            case 2:
                return textureTier2;
            case 3:
                return textureTier3;
            case 4:
                return textureTier4;
            case 5:
                return textureTier5;
            case 6:
                return textureTier6;
            case 7:
                return textureTier7;
            case 8:
                return textureTier8;
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int ordinalSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(ordinalSide, tMeta);
    }

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List<ItemStack> aList) {
        for (int i = 0; i < MAX_BLOCK_TIER; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
