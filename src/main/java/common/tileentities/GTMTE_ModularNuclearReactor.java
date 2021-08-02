package common.tileentities;

import org.joml.Vector3i;
import org.lwjgl.input.Keyboard;

import client.gui.GUIContainer_ModularNuclearReactor;
import common.Blocks;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GTMTE_ModularNuclearReactor extends GT_MetaTileEntity_MultiBlockBase {
	
	private final Block CASING = GregTech_API.sBlockCasings3;
	private final int CASING_META = 12;
	private final int CASING_TEXTURE_ID = 44;
	
	private final Block CHAMBER_OFF = Blocks.reactorChamberOFF;
	private final Block CHAMBER_ON = Blocks.reactorChamberON;
	private final Block CONTROL_ROD = Blocks.reactorControlRod;
	
	private boolean euMode = true;
	
	public GTMTE_ModularNuclearReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		
	}
	
	public GTMTE_ModularNuclearReactor(String aName) {
		super(aName);
		
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity var1) {
		return new GTMTE_ModularNuclearReactor(super.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Disabled"};
		/*
		final MultiBlockTooltipBuilder b =  new MultiBlockTooltipBuilder();
		b.addInfo("Can be built, BUT DOES NOT WORK")
				.addInfo("Converts fissile material and outputs power or heat")
				.addSeparator()
				.addInfo("EU-MODE:")
				.addInfo("   Directly outputs electricity depending on inserted fuel rods")
				.addSeparator()
				.addInfo("COOLANT-MODE:")
				.addInfo("   Requires coolant to be pumped into the reactor.")
				.addInfo("   Coolant is heated and should be drained and converted to electricity by other means.")
				.addSeparator()
				.addInfo("NOTES:")
				.addInfo("  Does NOT use Industrialcraft 2 reactor components!")
				.addInfo("  Consult controller GUI on how to arrange the outer casings.")
				.addSeparator()
				.beginStructureBlock(7, 6, 7)
				.addController("Front bottom Center")
				.addCasingInfo("Radiation Proof Machine Casing", 100)
				.addOtherStructurePart("Control Rods", "Four pillars, four blocks high each. Diagonal to the inner edges of the shell")
				.addOtherStructurePart("Nuclear Reactor Chamber", "17 of them to fill out the rest of the floor inside the shell")
				.addDynamoHatch("ONLY in EU-mode, at least one")
				.addOtherStructurePart("Input Bus, Output Bus", "Optional but required for automation")
				.addOtherStructurePart("Input Hatch, Output Hatch", "ONLY in Coolant-Mode, at least one each")
				.signAndFinalize("Kekzdealer");
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			return b.getInformation();
		} else {
			return b.getStructureInformation();
		}*/
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return aSide == aFacing
				? new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
						new GT_RenderedTexture(aActive ? 
								Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE 
								: Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)}
				: new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID)};
	}
	
	// TODO: Opening UI crashes server. Controller isn't craftable right now.
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		/*return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(),
				"MultiblockDisplay.png");*/
		return new GUIContainer_ModularNuclearReactor(aBaseMetaTileEntity, aPlayerInventory.player);
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack stack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack stack) {
		return false;
	}
	
	@Override
	public void onPostTick(IGregTechTileEntity thisController, long aTick) {
		super.onPostTick(thisController, aTick);
		
		if(super.getBaseMetaTileEntity().isActive()) {
			// Switch to ON blocks
		} else {
			// Switch to OFF blocks
		}
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity thisController, ItemStack guiSlotItem) {		
		// Figure out the vector for the direction the back face of the controller is facing
		final int dirX = ForgeDirection.getOrientation(thisController.getBackFacing()).offsetX;
		final int dirZ = ForgeDirection.getOrientation(thisController.getBackFacing()).offsetZ;
		int minCasingAmount = 100;
		boolean checklist = true; // if this is still true at the end, machine is good to go :)
		
		// Determine the ground level center of the structure
		final Vector3i center = new Vector3i(
				thisController.getXCoord(),
				thisController.getYCoord(),
				thisController.getZCoord())
				.add(dirX * 3, 0, dirZ * 3);
		// Scan for outer tube
		//	- Scan sides
		for(int x = -3; x <= 3; x++) {
			for(int z = -3; z <= 3; z++) {
				// Only scan the three wide even sides, skip rest
				if((Math.abs(x) <= 1 && Math.abs(z) == 3) || (Math.abs(z) <= 1 && Math.abs(x) == 3)) {
					for(int h = 0; h < 6; h++) {
						final Vector3i pos = new Vector3i(center.x() + x, center.y() + h, center.z() + z);
						if(h == 0 && pos.x() == thisController.getXCoord() && pos.y() == thisController.getYCoord() && pos.z() == thisController.getZCoord()) {
							// Ignore controller
							continue;
						} else if (thisController.getBlock(pos.x(), pos.y(), pos.z()) == CASING
								&& thisController.getMetaID(pos.x(), pos.y(), pos.z()) == CASING_META) {
							minCasingAmount--;
						} else {
							checklist = false;
						}
					}
				}
			}
		}
		// 	- Scan corners of tube
		for(int x = -2; x <= 2; x++) {
			for(int z = -2; z <= 2; z++) {
				// Only scan the four corners, skip rest
				if(Math.abs(x) + Math.abs(z) == 4) {
					for(int h = 0; h < 6; h++) {
						final Vector3i pos = new Vector3i(center.x() + x, center.y() + h, center.z() + z);
						if(h == 0 && pos.x() == thisController.getXCoord() && pos.y() == thisController.getYCoord() && pos.z() == thisController.getZCoord()) {
							// Ignore controller
							continue;
						} else if (thisController.getBlock(pos.x(), pos.y(), pos.z()) == CASING
								&& thisController.getMetaID(pos.x(), pos.y(), pos.z()) == CASING_META) {
							minCasingAmount--;
						} else {
							checklist = false;
						}
					}
				}
			}
		}
		// Scan ground layer
		for(int x = -2; x <= 2; x++) {
			for(int z = -2; z <= 2; z++) {
				if(!(thisController.getBlock(center.x() + x, center.y(), center.z() + z) == CASING 
						&& thisController.getMetaID(center.x() + x, center.y(), center.z() + z) == CASING_META)) {
					checklist = false;
				} else {
					minCasingAmount--;
				}
			}
		}
		// Scan reactor chambers
		for(int x = -2; x <= 2; x++) {
			for(int z = -2; z <= 2; z++) {
				// Skip if diagonal, don't skip center
				if(Math.abs(x) == Math.abs(z) && !(x == 0 && z == 0)) {
					continue;
				}
				if(!(thisController.getBlock(center.x() + x, center.y() + 1, center.z() + z) == CHAMBER_OFF
						|| thisController.getBlock(center.x() + x, center.y() + 1, center.z() + z) == CHAMBER_ON)) {
					checklist = false;
				}
			}
		}
		// Scan control rods
		for(int h = 1; h < 5; h++) {
			for(int x = -1; x <= 1; x++) {
				for(int z = -1; z <= 1; z++) {
					// Only check diagonal
					if(x == 0 || z == 0) {
						continue;
					}
					if(!(thisController.getBlock(center.x() + x, center.y() + h, center.z() + z) == CONTROL_ROD)) {
						checklist = false;
					}
				}
			}			
		}
		
		
		
		
		if(minCasingAmount > 0) {
			checklist = false;
		}
		
		return checklist;
	}

	@Override
	public int getMaxEfficiency(ItemStack stack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack stack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack stack) {
		return false;
	}

}
