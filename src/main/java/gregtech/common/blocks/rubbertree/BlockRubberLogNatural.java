package gregtech.common.blocks.rubbertree;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;

public class BlockRubberLogNatural extends Block {

    public static final String NAME = "gt.block_rubber_log_natural";
    public static final int META_EMPTY = 0;

    // TODO Remove in next major version after GT rubber tree is implemented
    public static final int META_POSTEA_TRANSFORM = 1;

    private static final int ELECTRIC_TREETAP_EU_PER_USE = 50;

    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;

    @SideOnly(Side.CLIENT)
    private IIcon topIcon;

    @SideOnly(Side.CLIENT)
    private IIcon resinSideIcon;

    public BlockRubberLogNatural() {
        super(Material.wood);

        setBlockName(NAME);
        setCreativeTab(null);
        setStepSound(soundTypeWood);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("axe", 0);

        GameRegistry.registerBlock(this, ItemBlockRubberLogNatural.class, NAME);
    }

    public static boolean hasResin(int meta) {
        return meta >= 2 && meta <= 5;
    }

    public static boolean isResinSide(int meta, int side) {
        return hasResin(meta) && meta == side;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(@NotNull IIconRegister iconRegister) {
        this.sideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_side");
        this.topIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_top");
        this.resinSideIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_log_resin_side");
        this.blockIcon = this.sideIcon;
    }

    @Override
    public String getUnlocalizedName() {
        return NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return this.topIcon;
        }

        return isResinSide(meta, side) ? this.resinSideIcon : this.sideIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(@NotNull IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(side, world.getBlockMetadata(x, y, z));
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubBlocks(Item item, net.minecraft.creativetab.CreativeTabs tab, List list) {
        // world gen only
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }

    @Override
    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public Item getItemDropped(int meta, java.util.Random random, int fortune) {
        return Item.getItemFromBlock(GregTechAPI.sBlockRubberLog);
    }

    @Override
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0);
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(GregTechAPI.sBlockRubberLog);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>(1);
        drops.add(new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0));
        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return new ItemStack(GregTechAPI.sBlockRubberLog, 1, 0);
    }

    @Override
    public void updateTick(@NotNull World world, int x, int y, int z, java.util.Random random) {
        if (world.isRemote) {
            return;
        }

        int meta = world.getBlockMetadata(x, y, z);

        // TODO Remove in next major version after GT rubber tree is implemented
        // Special case: old IC2 log migrated by Postea
        if (meta == META_POSTEA_TRANSFORM) {
            world.setBlockMetadataWithNotify(x, y, z, META_EMPTY, 2);
            RubberTreeResinLogic.scheduleTreeRefill(world, x, y, z, random);
            return;
        }

        RubberTreeResinLogic.tryRefillTree(world, x, y, z, random);
    }

    private static @Nullable Item getIC2ToolItem(String ic2Name) {
        ItemStack stack = GTModHandler.getIC2Item(ic2Name, 1L);
        return stack == null ? null : stack.getItem();
    }

    private static boolean isRegularTreetap(ItemStack stack) {
        if (stack == null) return false;
        Item treetap = getIC2ToolItem("treetap");
        return treetap != null && stack.getItem() == treetap;
    }

    private static boolean isElectricTreetap(ItemStack stack) {
        if (stack == null) return false;
        Item electricTreetap = getIC2ToolItem("electricTreetap");
        return electricTreetap != null && stack.getItem() == electricTreetap;
    }

    private static boolean canUseToolForResinHarvest(ItemStack stack, EntityPlayer player) {
        if (stack == null || player == null) {
            return false;
        }

        if (player.capabilities.isCreativeMode) {
            return isRegularTreetap(stack) || isElectricTreetap(stack);
        }

        if (isElectricTreetap(stack)) {
            return GTModHandler.canUseElectricItem(stack, ELECTRIC_TREETAP_EU_PER_USE);
        }

        return isRegularTreetap(stack);
    }

    private static boolean consumeToolForResinHarvest(ItemStack stack, EntityPlayer player) {
        if (stack == null || player == null) {
            return false;
        }

        if (player.capabilities.isCreativeMode) {
            return true;
        }

        if (isElectricTreetap(stack)) {
            return GTModHandler.useElectricItem(stack, ELECTRIC_TREETAP_EU_PER_USE, player);
        }

        if (isRegularTreetap(stack)) {
            stack.damageItem(1, player);

            if (stack.stackSize <= 0) {
                player.destroyCurrentEquippedItem();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, int x, int y, int z, @NotNull EntityPlayer player, int side,
        float hitX, float hitY, float hitZ) {

        int meta = world.getBlockMetadata(x, y, z);
        boolean canHarvestFromThisFace = isResinSide(meta, side);
        ItemStack heldStack = player.getCurrentEquippedItem();
        boolean usableTool = canUseToolForResinHarvest(heldStack, player);

        if (world.isRemote) {
            return canHarvestFromThisFace && usableTool;
        }

        if (!canHarvestFromThisFace || !usableTool) {
            return false;
        }

        if (!consumeToolForResinHarvest(heldStack, player)) {
            return false;
        }

        player.inventory.markDirty();
        player.inventoryContainer.detectAndSendChanges();

        world.setBlockMetadataWithNotify(x, y, z, META_EMPTY, 3);
        RubberTreeResinLogic.scheduleTreeRefill(world, x, y, z, world.rand);

        spawnResinDrop(world, x, y, z, side, ItemList.Sticky_Resin.get(getResinAmount(world)));

        return true;
    }

    private void spawnResinDrop(World world, int x, int y, int z, int side, ItemStack stack) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        double spawnX = x + 0.5D + dir.offsetX * 0.70D;
        double spawnY = y + 0.55D;
        double spawnZ = z + 0.5D + dir.offsetZ * 0.70D;

        EntityItem entityItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
        entityItem.motionX = dir.offsetX * 0.08D;
        entityItem.motionY = 0.02D + world.rand.nextDouble() * 0.03D;
        entityItem.motionZ = dir.offsetZ * 0.08D;
        entityItem.delayBeforeCanPickup = 5;

        world.spawnEntityInWorld(entityItem);

        RubberTreeEffects.playResinHarvestSound(world, x, y, z);
    }

    private int getResinAmount(@NotNull World world) {
        int resinAmount = 1;
        if (world.rand.nextFloat() < 0.35F) resinAmount++;
        if (world.rand.nextFloat() < 0.20F) resinAmount++;
        if (world.rand.nextFloat() < 0.10F) resinAmount++;

        return resinAmount;
    }
}
