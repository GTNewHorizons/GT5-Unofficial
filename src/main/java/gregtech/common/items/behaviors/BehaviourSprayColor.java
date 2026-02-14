package gregtech.common.items.behaviors;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.ColoredBlockContainer;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Other;

public class BehaviourSprayColor extends BehaviourNone {

    private final ItemStack mEmpty;
    private final ItemStack mUsed;
    private final ItemStack mFull;
    private final long mUses;
    private final byte mColor;

    public BehaviourSprayColor(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses, int aColor) {
        this.mEmpty = aEmpty;
        this.mUsed = aUsed;
        this.mFull = aFull;
        this.mUses = aUses;
        this.mColor = ((byte) aColor);
    }

    public BehaviourSprayColor(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses) {
        this.mEmpty = aEmpty;
        this.mUsed = aUsed;
        this.mFull = aFull;
        this.mUses = aUses;
        this.mColor = 0;
    }

    @Override
    // Included for Ring of Loki support.
    public boolean onItemUse(final MetaBaseItem aItem, final ItemStack aStack, final EntityPlayer aPlayer,
        final World aWorld, final int aX, final int aY, final int aZ, final int ordinalSide, final float hitX,
        final float hitY, final float hitZ) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);

        if (ColoredBlockContainer.getInstance(aPlayer, aX, aY, aZ, side)
            .isValid()) {
            return onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, side, hitX, hitY, hitZ);
        }

        return false;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if ((aWorld.isRemote) || (aStack.stackSize != 1)) {
            return false;
        }
        boolean rOutput = false;
        if (!aPlayer.canPlayerEdit(aX, aY, aZ, side.ordinal(), aStack)) {
            return false;
        }
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        long tUses = getUses(aStack, tNBT);

        int painted = 0;
        int maxPainted = Other.sprayCanChainRange;
        ForgeDirection lookSide;
        Vec3 look = aPlayer.getLookVec();
        double absX = Math.abs(look.xCoord);
        double absY = Math.abs(look.yCoord);
        double absZ = Math.abs(look.zCoord);
        if (absX > absY && absX > absZ) {
            lookSide = look.xCoord > 0 ? ForgeDirection.EAST : ForgeDirection.WEST;
        } else if (absY > absX && absY > absZ) {
            lookSide = look.yCoord > 0 ? ForgeDirection.UP : ForgeDirection.DOWN;
        } else {
            lookSide = look.zCoord > 0 ? ForgeDirection.SOUTH : ForgeDirection.NORTH;
        }
        Block initialBlock = aWorld.getBlock(aX, aY, aZ);
        int initialBlockMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        TileEntity initialTE = aWorld.getTileEntity(aX, aY, aZ);
        while ((GTUtility.areStacksEqual(aStack, this.mUsed, true)) && (colorize(aWorld, aX, aY, aZ, side, aPlayer))) {
            GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_SPRAY_CAN, 1.0F, 1.0F, aX, aY, aZ);
            if (!aPlayer.capabilities.isCreativeMode) {
                tUses -= 1L;
            }
            rOutput = true;
            painted++;
            if (painted >= maxPainted && maxPainted != -1) break;
            if (!aPlayer.isSneaking() || tUses <= 0) break;
            switch (lookSide) {
                case UP -> aY += 1;
                case DOWN -> aY -= 1;
                case NORTH -> aZ -= 1;
                case SOUTH -> aZ += 1;
                case WEST -> aX -= 1;
                case EAST -> aX += 1;
                default -> throw new IllegalArgumentException("Unexpected value: " + lookSide);
            }

            if (aWorld.getBlock(aX, aY, aZ) != initialBlock) break;
            if (aWorld.getBlockMetadata(aX, aY, aZ) != initialBlockMeta) break;

            /*
             * Check if the initial block had a TE and if the next one does, check if it's the same kind. else one does
             * and the other doesn't, thus stop checking.
             */
            TileEntity targetTE = aWorld.getTileEntity(aX, aY, aZ);
            if (initialTE == null ^ targetTE == null) break;
            if (initialTE != null && targetTE != null) {
                if (!initialTE.getClass()
                    .isInstance(targetTE)) break;

                if (initialTE instanceof IGregTechTileEntity currentGTTile
                    && targetTE instanceof IGregTechTileEntity targetGTTile) {
                    if (currentGTTile.getMetaTileID() != targetGTTile.getMetaTileID()) break;
                }
            }
        }
        setRemainingUses(aStack, tNBT, tUses);
        return rOutput;
    }

    @Override
    public boolean onBlockPlacedWhileWieldingOffhanded(final BlockSnapshot blockSnapshot, final ItemStack itemStack, final EntityPlayer player) {
        if (itemStack.getItem() instanceof final MetaBaseItem item) {
            this.onItemUseFirst(item, itemStack, player, player.worldObj, blockSnapshot.x, blockSnapshot.y, blockSnapshot.z, ForgeDirection.SOUTH, 0, 0, 0);
        }

        return false;
    }

    @Override
    public boolean shouldInterruptBlockActivation(final EntityPlayer player, final TileEntity tileEntity,
        final ForgeDirection side) {
        return ColoredBlockContainer.getInstance(player, tileEntity, side)
            .isValid();
    }

    protected long getUses(ItemStack aStack, NBTTagCompound tNBT) {
        long tUses = tNBT.getLong("GT.RemainingPaint");
        if (GTUtility.areStacksEqual(aStack, this.mFull, true)) {
            aStack.func_150996_a(this.mUsed.getItem());
            Items.feather.setDamage(aStack, Items.feather.getDamage(this.mUsed));
            tUses = this.mUses;
        }
        return tUses;
    }

    protected void setRemainingUses(ItemStack aStack, NBTTagCompound tNBT, long tUses) {
        tNBT.removeTag("GT.RemainingPaint");
        if (tUses > 0L) {
            tNBT.setLong("GT.RemainingPaint", tUses);
        }
        if (tNBT.hasNoTags()) {
            aStack.setTagCompound(null);
        } else {
            aStack.setTagCompound(tNBT);
        }
        if (tUses <= 0L) {
            if (this.mEmpty == null) {
                aStack.stackSize -= 1;
            } else {
                aStack.func_150996_a(this.mEmpty.getItem());
                Items.feather.setDamage(aStack, Items.feather.getDamage(this.mEmpty));
            }
        }
    }

    protected boolean colorize(World aWorld, int aX, int aY, int aZ, ForgeDirection side, EntityPlayer player) {
        return ColoredBlockContainer.getInstance(player, aX, aY, aZ, side)
            .setColor(getColor());
    }

    protected byte getColor() {
        return this.mColor;
    }

    protected Supplier<String> getTooltip() {
        return () -> StatCollector.translateToLocalFormatted(
            "gt.behaviour.paintspray.tooltip",
            Dyes.get(this.mColor)
                .getLocalizedDyeName());
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        if (getTooltip() != null) aList.add(StatCollector.translateToLocal(getTooltip().get()));
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.chain"));
        aList.add(
            StatCollector.translateToLocalFormatted("gt.behaviour.paintspray.chain_amount", Other.sprayCanChainRange));
        NBTTagCompound tNBT = aStack.getTagCompound();
        long tRemainingPaint = tNBT == null ? this.mUses
            : GTUtility.areStacksEqual(aStack, this.mFull, true) ? this.mUses : tNBT.getLong("GT.RemainingPaint");
        aList.add(StatCollector.translateToLocalFormatted("gt.behaviour.paintspray.uses", tRemainingPaint));
        aList.add(StatCollector.translateToLocal("gt.behaviour.unstackable"));
        return aList;
    }
}
