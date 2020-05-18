package common.blocks;

import common.itemBlocks.IB_SpaceElevator;
import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class Block_SpaceElevator extends BaseGTUpdateableBlock {

    private static final Block_SpaceElevator INSTANCE = new Block_SpaceElevator();

    private IIcon baseTop;
    private IIcon baseSide;
    private IIcon coilHolder;

    private Block_SpaceElevator() {
        super(Material.iron);
    }

    public static Block registerBlock() {
        final String blockName = "kekztech_spaceelevator_block";
        INSTANCE.setBlockName(blockName);
        INSTANCE.setCreativeTab(CreativeTabs.tabMisc);
        INSTANCE.setHardness(7.0f);
        INSTANCE.setResistance(10.0f);
        GameRegistry.registerBlock(INSTANCE, IB_SpaceElevator.class, blockName);

        return INSTANCE;
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        baseTop = ir.registerIcon("kekztech:SpaceElevatorBase_top");
        baseSide = ir.registerIcon("kekztech:SpaceElevatorBase_side");
        coilHolder = ir.registerIcon("kekztech:CoilHolder");
    }

    @Override
    @SuppressWarnings({"unchecked" })
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if(meta == 0) {
            return (side < 2) ? baseTop : baseSide;
        } else {
            return coilHolder;
        }
    }
}
