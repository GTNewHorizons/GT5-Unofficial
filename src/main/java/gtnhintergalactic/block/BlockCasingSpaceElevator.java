package gtnhintergalactic.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.MaterialCasings;
import gtnhintergalactic.GTNHIntergalactic;
import gtnhintergalactic.item.ItemCasingSpaceElevator;

/**
 * Casings used to construct the Space Elevator
 *
 * @author minecraft7771
 */
public class BlockCasingSpaceElevator extends BlockCasingsAbstract {

    /** Number of casings that exist */
    private static final int NUMBER_OF_CASINGS = 3;
    /** Icon for the base casing */
    private static IIcon IconSECasing0;
    /** Icons for the support structure */
    private static final IIcon[] IconSECasing1 = new IIcon[2];
    /** Icons for the internal structure */
    private static final IIcon[] IconSECasing2 = new IIcon[2];

    /**
     * Create new Space Elevator casings
     */
    public BlockCasingSpaceElevator() {
        super(ItemCasingSpaceElevator.class, "gt.blockcasingsSE", MaterialCasings.INSTANCE);

        for (byte b = 0; b < NUMBER_OF_CASINGS; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[32][b] = TextureFactory.of(this, b);
        }

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Space Elevator Base Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Space Elevator Support Structure");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Space Elevator Internal Structure");

        ItemList.SpaceElevatorBaseCasing.set(new ItemStack(this, 1, 0));
        ItemList.SpaceElevatorSupportStructure.set(new ItemStack(this, 1, 1));
        ItemList.SpaceElevatorInternalStructure.set(new ItemStack(this, 1, 2));
    }

    /**
     * Register the block icons
     *
     * @param iconRegister Register to which the icons will be added
     */
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        IconSECasing0 = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/BaseCasing");
        IconSECasing1[0] = iconRegister
            .registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/SupportStructure");
        IconSECasing1[1] = iconRegister
            .registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/SupportStructure_Side");
        IconSECasing2[0] = iconRegister
            .registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/InternalStructure");
        IconSECasing2[1] = iconRegister
            .registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/InternalStructure_Side");
    }

    /**
     * Get the icon for this block
     *
     * @param side Side for which the icon should be gotten
     * @param meta Meta of the block
     * @return Icon of the block
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        switch (meta) {
            case 0:
                return IconSECasing0;
            case 1:
                switch (side) {
                    case 0:
                    case 1:
                        return IconSECasing1[0];
                    default:
                        return IconSECasing1[1];
                }
            case 2:
                switch (side) {
                    case 0:
                    case 1:
                        return IconSECasing2[0];
                    default:
                        return IconSECasing2[1];
                }
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    /**
     * Get the icon for this block
     *
     * @param world World in which the block exists
     * @param x     X coordinate of the block
     * @param y     Y coordinate of the block
     * @param z     Z coordinate of the block
     * @param side  Side for which the icon should be gotten
     * @return Icon of the block
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        return getIcon(side, meta);
    }

    /**
     * Get all sub blocks of this block
     *
     * @param item Item that represents this block
     * @param tabs Create tab of the game
     * @param list List to which the sub blocks will be added
     */
    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List list) {
        for (int i = 0; i < NUMBER_OF_CASINGS; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
