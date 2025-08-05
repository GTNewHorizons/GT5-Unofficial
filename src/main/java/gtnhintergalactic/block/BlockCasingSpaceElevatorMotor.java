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
import gtnhintergalactic.item.ItemCasingSpaceElevatorMotor;

/**
 * Motors used to construct the Space Elevator
 *
 * @author minecraft7771
 */
public class BlockCasingSpaceElevatorMotor extends BlockCasingsAbstract {

    /** Number of motors that exist */
    private static final int NUMBER_OF_MOTORS = 5;
    /** Icon for the top and bottom of a motor */
    private static IIcon IconSEMotorTop;
    /** Icons for the side of the different motors */
    private static final IIcon[] IconSEMotorSide = new IIcon[NUMBER_OF_MOTORS];

    /**
     * Create new Space Elevator motors
     */
    public BlockCasingSpaceElevatorMotor() {
        super(ItemCasingSpaceElevatorMotor.class, "gt.blockcasingsSEMotor", MaterialCasings.INSTANCE);

        for (byte b = 0; b < NUMBER_OF_MOTORS; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[32][b + 16] = TextureFactory.of(this, b);
        }

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Space Elevator Motor MK-I");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Space Elevator Motor MK-II");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Space Elevator Motor MK-III");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Space Elevator Motor MK-IV");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Space Elevator Motor MK-V");

        ItemList.SpaceElevatorMotorT1.set(new ItemStack(this, 1, 0));
        ItemList.SpaceElevatorMotorT2.set(new ItemStack(this, 1, 1));
        ItemList.SpaceElevatorMotorT3.set(new ItemStack(this, 1, 2));
        ItemList.SpaceElevatorMotorT4.set(new ItemStack(this, 1, 3));
        ItemList.SpaceElevatorMotorT5.set(new ItemStack(this, 1, 4));
    }

    /**
     * Register the block icons
     *
     * @param iconRegister Register to which the icons will be added
     */
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        IconSEMotorTop = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/Motor");
        IconSEMotorSide[0] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT1_Side");
        IconSEMotorSide[1] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT2_Side");
        IconSEMotorSide[2] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT3_Side");
        IconSEMotorSide[3] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT4_Side");
        IconSEMotorSide[4] = iconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT5_Side");
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
        if (side == 0 || side == 1) {
            return IconSEMotorTop;
        } else {
            if (meta < NUMBER_OF_MOTORS) {
                return IconSEMotorSide[meta];
            }
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
        int tMeta = world.getBlockMetadata(x, y, z);
        return getIcon(side, tMeta);
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
        for (int i = 0; i < NUMBER_OF_MOTORS; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
