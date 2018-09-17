package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.debugCleanroom;

public class GT_MetaTileEntity_Cleanroom extends GT_MetaTileEntity_MultiBlockBase {

    public GT_MetaTileEntity_Cleanroom(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Cleanroom(String aName) {
        super(aName);
    }

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Cleanroom(mName);
	}

	@Override
    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Cleanroom",
                "Min(WxHxD): 3x4x3 (Hollow), Max(WxHxD): 15x15x15 (Hollow)",
                "Controller (Top center)",
                "Top besides contoller and edges: Filter Casings",
                "1 Reinforced Door (keep closed for 100% efficency)",
				"1x LV or 1x MV Energy Hatch, 1x Maintainance Hatch",
				"Up to 20 Machine Hull Item & Energy transfer through walls",
				"Remaining Blocks: Plascrete, 20 min",
				GT_Values.cleanroomGlass+"% of the Plascrete can be Reinforced Glass (min 20 Plascrete still apply)",
				"Consumes 40 EU/t when first turned on and 4 EU/t once at 100% efficiency",
				"An energy hatch accepts up to 2A, so you can use 2A LV or 1A MV",
				"2 LV batteries + 1 LV generator or 1 MV generator",
				"Make sure your Energy Hatch matches!"};
    }

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		mEfficiencyIncrease = 100;
		mMaxProgresstime = 100;
		mEUt = -4;
		return true;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int x = 1;
		int z = 1;
		int y = 1;
		int mDoorCount = 0;
		int mHullCount = 0;
		int mPlascreteCount = 0;
		int mGlassCount = 0;
		boolean doorState = false;
		this.mUpdate = 100;
		
		if (debugCleanroom) {
			GT_Log.out.println(
							"Cleanroom: Checking machine"
			);
		}
		for (int i = 1; i < 8; i++) {
			Block tBlock = aBaseMetaTileEntity.getBlockOffset(i, 0, 0);
			int tMeta = aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0);
			if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
				if (tBlock == GregTech_API.sBlockReinforced || tMeta == 2) {
					x = i;
					z = i;
					break;
				} else {
					if (debugCleanroom) {
						GT_Log.out.println(
							"Cleanroom: Unable to detect room edge?"
						);
					}
					return false;
				}
			}
		}
		for (int i = -1; i > -16; i--) {
			Block tBlock = aBaseMetaTileEntity.getBlockOffset(x, i, z);
			int tMeta = aBaseMetaTileEntity.getMetaIDOffset(x, i, z);
			if (tBlock != GregTech_API.sBlockReinforced || tMeta != 2) {
				y = i + 1;
				break;
			}
		}
		if (y > -2) {
			if (debugCleanroom) {
				GT_Log.out.println(
					"Cleanroom: Room not tall enough?"
				);
			}
			return false;
		}
		for (int dX = -x; dX <= x; dX++) {
			for (int dZ = -z; dZ <= z; dZ++) {
				for (int dY = 0; dY >= y; dY--) {
					if (dX == -x || dX == x || dY == -y || dY == y || dZ == -z || dZ == z) {
						Block tBlock = aBaseMetaTileEntity.getBlockOffset(dX, dY, dZ);
						int tMeta = aBaseMetaTileEntity.getMetaIDOffset(dX, dY, dZ);
						if (y == 0) {
							if (dX == -x || dX == x || dZ == -z || dZ == z) {
								if (tBlock != GregTech_API.sBlockReinforced || tMeta != 2) {
									if (debugCleanroom) {
										GT_Log.out.println(
											"Cleanroom: Non reinforced block on top edge? tMeta != 2"
										);
									}
									return false;
								}
							} else if (dX == 0 && dZ == 0) {
							} else {
								if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
									if (debugCleanroom) {
										GT_Log.out.println(
											"Cleanroom: Non reinforced block on top face interior? tMeta != 11"
										);
									}
									return false;
								}
							}
						} else if (tBlock == GregTech_API.sBlockReinforced && tMeta == 2) {
							mPlascreteCount++;
						} else if (tBlock != null && tBlock.getUnlocalizedName().equals("blockAlloyGlass")){
							++mGlassCount;
						} else {
							IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dX, dY, dZ);
							if ((!this.addMaintenanceToMachineList(tTileEntity, 82)) && (!this.addEnergyInputToMachineList(tTileEntity, 82))) {
								if (tBlock instanceof ic2.core.block.BlockIC2Door) {
									if ((tMeta & 8) == 0) {
										doorState = (Math.abs(dX) > Math.abs(dZ) == ((tMeta & 1) != 0)) != ((tMeta & 4) != 0);
									}
									mDoorCount++;
								} else {
									if (tTileEntity == null) {
											if (debugCleanroom) {
												GT_Log.out.println(
													"Cleanroom: Missing block? Not a tTileEntity"
												);
											}
											return false;
									}
									IMetaTileEntity aMetaTileEntity = tTileEntity.getMetaTileEntity();
									if (aMetaTileEntity == null) {
										if (debugCleanroom) {
											GT_Log.out.println(
												"Cleanroom: Missing block? Not a aMetaTileEntity"
											);
										}
										return false;
									}
									if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicHull) {
										mHullCount++;
									} else {
										if (debugCleanroom) {
											GT_Log.out.println(
												"Cleanroom: Incorrect block?"
											);
										}
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		if (this.mMaintenanceHatches.size() != 1 || this.mEnergyHatches.size() != 1 || mDoorCount != 2 || mHullCount > 20) {
			return false;
		}
		for (int dX = -x + 1; dX <= x - 1; dX++) {
			for (int dZ = -z + 1; dZ <= z - 1; dZ++) {
				for (int dY = -1; dY >= y + 1; dY--) {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dX, dY, dZ);
					if (tTileEntity != null) {
						IMetaTileEntity aMetaTileEntity = tTileEntity.getMetaTileEntity();
						if (aMetaTileEntity != null && aMetaTileEntity instanceof GT_MetaTileEntity_BasicMachine_GT_Recipe) {
							if (debugCleanroom) {
								GT_Log.out.println(
									"Cleanroom: Machine detected, adding pointer back to cleanroom"
								);
							}
							((GT_MetaTileEntity_BasicMachine_GT_Recipe) aMetaTileEntity).mCleanroom = this;
						}
					}
				}
			}
		}

        if (doorState) {
			this.mEfficiency = Math.max(0, this.mEfficiency - 200);
        }
        for(byte i = 0 ; i<6 ; i++){
        	byte t = (byte) Math.max(1, (byte)(15/(10000f / this.mEfficiency)));
        aBaseMetaTileEntity.setInternalOutputRedstoneSignal(i, t);
        }

        float ratio = (((float)mPlascreteCount)/100f)* GT_Values.cleanroomGlass;

        return mPlascreteCount>=20 && mGlassCount < (int) Math.floor(ratio);
    }
    
    @Override
    public boolean allowGeneralRedstoneOutput(){
    	return true;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == 0 || aSide == 1) {
            return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.BLOCK_PLASCRETE),
                    new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE : Textures.BlockIcons.OVERLAY_TOP_CLEANROOM)};

        }
        return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.BLOCK_PLASCRETE)};
    }

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MultiblockDisplay.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}
}
