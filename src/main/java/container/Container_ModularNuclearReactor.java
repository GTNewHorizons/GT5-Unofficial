package container;

import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import reactor.ButtonSlot;
import tileentities.GTMTE_ModularNuclearReactor;

public class Container_ModularNuclearReactor extends GT_Container_MultiMachine {

	private final GTMTE_ModularNuclearReactor REACTOR_TILE;
	private final IInventory PLAYER_INVENTORY;
	
	private int nextSlotID = 0;
	private final Slot[] REACTOR_SLOTS = new Slot[54];
	private final Slot SLOT_CONFIGURATION;
	private final Slot BUTTON_EU_MODE;
	private final Slot BUTTON_FLUID_MODE;
	private final Slot BUTTON_CONDITION;
	private final Slot BUTTON_CONFIGURE;
	private final Slot BUTTON_RESET;

	
	public Container_ModularNuclearReactor(InventoryPlayer inventoryPlayer, IGregTechTileEntity reactorTile) {
		
		super(inventoryPlayer, reactorTile);
		
		this.REACTOR_TILE = (GTMTE_ModularNuclearReactor) reactorTile;
		this.PLAYER_INVENTORY = inventoryPlayer;
		
		// Add the reactor chamber
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 6; y++){
				REACTOR_SLOTS[nextSlotID] = super.addSlotToContainer(new Slot(PLAYER_INVENTORY, getNextSlotID(), (16 + 67 * x), (16 + 67 * y)));
			}
		}
		// Add the configuration slot
		SLOT_CONFIGURATION = super.addSlotToContainer(new Slot(PLAYER_INVENTORY, getNextSlotID(), 0, 0));
		
		// Add buttons (they're also slots)
		BUTTON_EU_MODE = super.addSlotToContainer(new ButtonSlot(PLAYER_INVENTORY, getNextSlotID(), 0, 0));	
		BUTTON_FLUID_MODE = super.addSlotToContainer(new ButtonSlot(PLAYER_INVENTORY, getNextSlotID(), 0, 0));
		BUTTON_CONDITION = super.addSlotToContainer(new ButtonSlot(PLAYER_INVENTORY, getNextSlotID(), 0, 0));
		BUTTON_CONFIGURE = super.addSlotToContainer(new ButtonSlot(PLAYER_INVENTORY, getNextSlotID(), 0, 0));
		BUTTON_RESET = super.addSlotToContainer(new ButtonSlot(PLAYER_INVENTORY, getNextSlotID(), 0, 0));
		
	}
	
	private int getNextSlotID() {
		nextSlotID++;
		return nextSlotID - 1;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {		
		return true;
	}
	
	 @Override
	    public void detectAndSendChanges() {
	        super.detectAndSendChanges();
	        if (REACTOR_TILE.getBaseMetaTileEntity().isServerSide() == false) {
	        	return;
	        }
	 }

}
