package gregtech.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IItemContainer;

public class BlockFenceMetal extends BlockFence {

    public BlockFenceMetal() {
        this(ItemFence.class, "gt.blockfencemetal", Material.iron);

        register(0, ItemList.FenceIron);
    }

    protected BlockFenceMetal(Class<? extends ItemBlock> aItemClass, String aName, Material aMaterial) {
        super(aName, aMaterial);
        GregTechAPI.registerMachineBlock(this, -1);
        GameRegistry.registerBlock(this, aItemClass, getUnlocalizedName());
        setHardness(3.0F);
        setResistance(10.0F);
        setStepSound(soundTypeMetal);
        setBlockName(aName);
        this.isBlockContainer = false;
        this.useNeighborBrightness = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.BLOCK_IRON_FENCE.getIcon();
            default -> Textures.BlockIcons.BLOCK_IRON_FENCE.getIcon();
        };
    }

    protected final void register(int meta, @Nullable IItemContainer container) {
        ItemStack stack = new ItemStack(this, 1, meta);

        if (container != null) {
            container.set(stack.copy());
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z) {}

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {

    }

}
