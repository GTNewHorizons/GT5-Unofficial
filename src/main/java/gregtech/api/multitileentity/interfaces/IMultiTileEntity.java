package gregtech.api.multitileentity.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.multitileentity.MultiTileEntityBlockInternal;
import gregtech.api.multitileentity.MultiTileEntityItemInternal;
import gregtech.api.multitileentity.MultiTileEntityRegistry;

/*
 * Heavily inspired by GT6
 */
public interface IMultiTileEntity {

    int getMetaId();

    int getRegistryId();


}
