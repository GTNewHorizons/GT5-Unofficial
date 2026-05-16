package gregtech.common.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Mods.GregTech;
import static ic2.core.util.LiquidUtil.drainContainerStack;
import static ic2.core.util.LiquidUtil.fillContainerStack;
import static ic2.core.util.LiquidUtil.placeFluid;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.items.GTGenericItem;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.item.VolumetricFlaskGui;
import gregtech.crossmod.backhand.Backhand;
import gtPlusPlus.xmod.gregtech.common.helpers.VolumetricFlaskHelper;
import ic2.core.util.LiquidUtil;

public class ItemVolumetricFlask extends GTGenericItem
    implements IFluidContainerItem, IGuiHolder<PlayerInventoryGuiData> {

    private final int maxCapacity;
    private final String unlocalFlaskName;

    @SideOnly(Side.CLIENT)
    public IIcon iconWindow;

    public ItemVolumetricFlask(String unlocalized, String english, int maxCapacity) {
        super(unlocalized, english, null);
        this.maxCapacity = maxCapacity;
        unlocalFlaskName = unlocalized;
        setMaxStackSize(64);
        setNoRepair();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && VolumetricFlaskHelper.isFlaskEmpty(stack)
            && getMovingObjectPositionFromPlayer(world, player, true) == null) {
            if (stack == Backhand.getOffhandItem(player)) {
                GuiFactories.playerInventory()
                    .openFromPlayerInventory(player, Backhand.getOffhandSlot(player));
            } else {
                GuiFactories.playerInventory()
                    .openFromMainHand(player);
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int ordinalSide,
        float xOffset, float yOffset, float zOffset) {
        if (player instanceof FakePlayer) {
            return false;
        }
        if (world.isRemote) return false;
        if (interactWithTank(stack, player, world, x, y, z, ordinalSide)) {
            return true;
        }
        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, true);
        if (mop == null) {
            return false;
        }
        if (mop.typeOfHit == MovingObjectType.BLOCK) {
            x = mop.blockX;
            y = mop.blockY;
            z = mop.blockZ;
            if (!world.canMineBlock(player, x, y, z) || !player.canPlayerEdit(x, y, z, mop.sideHit, stack)) {
                return false;
            }
            if (collectFluidBlock(stack, player, world, x, y, z)) {
                return true;
            }
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[mop.sideHit];
            FluidStack fluidStack = drainContainerStack(stack, player, 1000, true);
            if (placeFluid(fluidStack, world, x, y, z)
                || (player.canPlayerEdit(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, mop.sideHit, stack)
                    && placeFluid(fluidStack, world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))) {
                if (!player.capabilities.isCreativeMode) drainContainerStack(stack, player, 1000, false);
                return true;
            }
        }
        return false;
    }

    public int getFreeSpace(ItemStack stack) {
        int capacity = getCapacity(stack);
        if (capacity > 0) {
            FluidStack fluidStack = getFluid(stack);
            return fluidStack == null ? capacity : capacity - fluidStack.amount;
        }
        return 0;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return VolumetricFlaskHelper.getFlaskCapacity(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aIconRegister) {
        super.registerIcons(aIconRegister);
        iconWindow = aIconRegister.registerIcon(GregTech.getResourcePath("gt." + unlocalFlaskName + ".window"));
    }

    public void setCapacity(ItemStack stack, int capacity) {
        VolumetricFlaskHelper.setFlaskCapacity(stack, capacity);
    }

    @Override
    public FluidStack getFluid(ItemStack stack) {
        return VolumetricFlaskHelper.getFlaskFluid(stack);
    }

    public void setFluid(ItemStack stack, FluidStack fluidStack) {
        VolumetricFlaskHelper.setFluid(stack, fluidStack);
    }

    @Override
    public int fill(ItemStack stack, FluidStack resource, boolean doFill) {
        if (stack.stackSize != 1) return 0;
        if ((resource == null) || (resource.amount <= 0)) {
            return 0;
        }
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack == null) {
            fluidStack = new FluidStack(resource, 0);
        } else if (!fluidStack.isFluidEqual(resource)) {
            return 0;
        }
        int amount = Math.min(getCapacity(stack) - fluidStack.amount, resource.amount);
        if ((doFill) && (amount > 0)) {
            fluidStack.amount += amount;
            setFluid(stack, fluidStack);
        }
        return amount;
    }

    @Override
    public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain) {
        if (stack.stackSize != 1) return null;
        FluidStack fluidStack = getFluid(stack);
        if (fluidStack == null) return null;
        maxDrain = Math.min(fluidStack.amount, maxDrain);
        if (doDrain) {
            fluidStack.amount -= maxDrain;
            setFluid(stack, fluidStack);
        }
        return new FluidStack(fluidStack, maxDrain);
    }

    @Override
    protected void addAdditionalToolTips(List<String> info, ItemStack stack, EntityPlayer aPlayer) {
        FluidStack fs = getFluid(stack);
        if (fs != null) {
            info.add(String.format("< %s, %s mB >", GTUtility.getFluidName(fs, true), formatNumber(fs.amount)));
        } else {
            info.add(
                String.format(
                    "< %s, %s mB >",
                    StatCollector.translateToLocal("GT5U.tooltip.volumetric_flask.empty"),
                    formatNumber(getCapacity(stack))));
        }
        info.add(StatCollector.translateToLocal("GT5U.tooltip.volumetric_flask.set_volume"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> itemList) {
        itemList.add(new ItemStack(this));
        for (Fluid fluid : FluidRegistry.getRegisteredFluids()
            .values()) {
            if (fluid != null) {
                ItemStack stack = new ItemStack(this);
                setCapacity(stack, getMaxCapacity());
                fill(stack, new FluidStack(fluid, Integer.MAX_VALUE), true);
                itemList.add(stack);
            }
        }
    }

    private boolean interactWithTank(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide) {
        if (world.isRemote) {
            return false;
        }
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof IFluidHandler handler)) {
            return false;
        }
        ForgeDirection dir = ForgeDirection.getOrientation(ordinalSide);
        FluidStack fs = this.getFluid(stack);
        int capacity = getCapacity(stack);
        if (fs != null && (!player.isSneaking() || fs.amount >= capacity)) {
            int amount = handler.fill(dir, fs, false);
            if (amount > 0) {
                fs = LiquidUtil.drainContainerStack(stack, player, amount, false);
                if (fs != null && fs.amount > 0) {
                    handler.fill(dir, fs, true);
                }
            }
        } else {
            int amount = fs == null ? capacity : capacity - fs.amount;
            FluidStack input = handler.drain(dir, amount, false);
            if (input != null && input.amount > 0) {
                amount = LiquidUtil.fillContainerStack(stack, player, input, false);
                if (amount > 0) {
                    handler.drain(dir, amount, true);
                }
            }
        }
        return true;
    }

    private boolean collectFluidBlock(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof IFluidBlock liquid) {
            if (liquid.canDrain(world, x, y, z)) {
                FluidStack fluid = liquid.drain(world, x, y, z, false);
                int amount = fillContainerStack(stack, player, fluid, true);
                if (amount == fluid.amount) {
                    fillContainerStack(stack, player, fluid, false);
                    liquid.drain(world, x, y, z, true);
                    return true;
                }
            }
        } else if (world.getBlockMetadata(x, y, z) == 0) {
            FluidStack fluid = null;
            if (block != Blocks.water && block != Blocks.flowing_water) {
                if (block == Blocks.lava || block == Blocks.flowing_lava) {
                    fluid = Materials.Lava.getFluid(1_000);
                }
            } else {
                fluid = Materials.Water.getFluid(1_000);
            }

            if (fluid != null) {
                int amount = fillContainerStack(stack, player, fluid, true);
                if (amount == fluid.amount) {
                    fillContainerStack(stack, player, fluid, false);
                    world.setBlockToAir(x, y, z);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new VolumetricFlaskGui(data).build();
    }

    @Override
    public ModularScreen createScreen(PlayerInventoryGuiData data, ModularPanel mainPanel) {
        return new ModularScreen(GregTech.ID, mainPanel);
    }
}
