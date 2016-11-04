package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.UUID;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.gui.GT_Slot_Holo;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.core.util.player.PlayerCache;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GregtechMetaSafeBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CONTAINER_SafeBlock extends GT_ContainerMetaTile_Machine {
	// public String UUID =
	// ((BaseMetaTileEntity)mTileEntity).getMetaTileEntity().getBaseMetaTileEntity().getOwnerName();
	public UUID		ownerUUID	= ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).ownerUUID;

	public String	tempPlayer	= PlayerCache.lookupPlayerByUUID(this.ownerUUID);
	public boolean	blockStatus	= ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bUnbreakable;
	public CONTAINER_SafeBlock(final InventoryPlayer aInventoryPlayer, final IGregTechTileEntity aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
	}

	@Override
	public void addSlots(final InventoryPlayer aInventoryPlayer) {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlotToContainer(new Slot(this.mTileEntity, x + y * 9, 8 + x * 18, 5 + y * 18));
			}
		}
		this.addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 8, 63, false, true, 1));
		this.addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 26, 63, false, true, 1));
		this.addSlotToContainer(new GT_Slot_Holo(this.mTileEntity, 27, 44, 63, false, true, 1));
	}

	@Override
	public int getShiftClickSlotCount() {
		return 27;
	}

	@Override
	public int getSlotCount() {
		return 27;
	}

	@Override
	public ItemStack slotClick(final int aSlotIndex, final int aMouseclick, final int aShifthold,
			final EntityPlayer aPlayer) {
		final int runs = 0;
		if (aSlotIndex < 27) {
			return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
		}
		final Slot tSlot = (Slot) this.inventorySlots.get(aSlotIndex);
		if (tSlot != null) {
			if (this.mTileEntity.getMetaTileEntity() == null) {
				return null;
			}
			if (aSlotIndex == 27) {

				/*
				 * ((GregtechMetaSafeBlock)
				 * this.mTileEntity.getMetaTileEntity()).bOutput =
				 * (!((GregtechMetaSafeBlock)
				 * this.mTileEntity.getMetaTileEntity()).bOutput); if
				 * (((GregtechMetaSafeBlock)
				 * this.mTileEntity.getMetaTileEntity()).bOutput) { if (aPlayer
				 * != null && aPlayer instanceof EntityPlayerMP &&
				 * (((GregtechMetaSafeBlock)this.mTileEntity.getMetaTileEntity()
				 * ).bOutput != false)) {
				 * 
				 * Utils.LOG_INFO(String.valueOf(Sys.is64Bit()));
				 * Utils.messagePlayer(aPlayer, "Salmon"); }
				 * 
				 * GT_Utility.sendChatToPlayer(aPlayer,
				 * "Emit Energy to Outputside"); } else {
				 * GT_Utility.sendChatToPlayer(aPlayer, "Don't emit Energy"); }
				 * return null;
				 */

			}
			if (aSlotIndex == 28) {
			}
			/*
			 * ((GregtechMetaSafeBlock)
			 * this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull =
			 * (!((GregtechMetaSafeBlock)
			 * this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull); if
			 * (((GregtechMetaSafeBlock)
			 * this.mTileEntity.getMetaTileEntity()).bRedstoneIfFull) {
			 * GT_Utility.sendChatToPlayer(aPlayer,
			 * "Emit Redstone if no Slot is free"); } else {
			 * GT_Utility.sendChatToPlayer(aPlayer, "Don't emit Redstone"); }
			 * return null; }
			 */
			if (aSlotIndex == 29) /*
									 * { if (((GregtechMetaSafeBlock)
									 * this.mTileEntity.getMetaTileEntity()).
									 * bUnbreakable) { if
									 * (((GregtechMetaSafeBlock)
									 * this.mTileEntity.getMetaTileEntity()).
									 * bUnbreakable) {
									 * makeIndestructible(aPlayer); } else {
									 * 
									 * } } else { makeIndestructible(aPlayer); }
									 * return null; }
									 */

			{
				((GregtechMetaSafeBlock) this.mTileEntity
						.getMetaTileEntity()).bUnbreakable = !((GregtechMetaSafeBlock) this.mTileEntity
								.getMetaTileEntity()).bUnbreakable;
				this.blockStatus = ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).bUnbreakable;
				this.ownerUUID = ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity()).ownerUUID;
				// Utils.messagePlayer(aPlayer, "Is the safe locked?
				// "+String.valueOf(((GregtechMetaSafeBlock)
				// this.mTileEntity.getMetaTileEntity()).bUnbreakable).toUpperCase());
				/*
				 * if (aPlayer != null && aPlayer instanceof EntityPlayerMP &&
				 * (((GregtechMetaSafeBlock)this.mTileEntity.getMetaTileEntity()
				 * ).bUnbreakable != false)) { UnbreakableBlockManager Xasda =
				 * new UnbreakableBlockManager();
				 * Xasda.setmTileEntity((BaseMetaTileEntity) mTileEntity,
				 * aPlayer); } else { UnbreakableBlockManager Xasda = new
				 * UnbreakableBlockManager();
				 * Xasda.setmTileEntity((BaseMetaTileEntity) mTileEntity,
				 * aPlayer); }
				 */
				return null;
			}

		}
		return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
	}

}
