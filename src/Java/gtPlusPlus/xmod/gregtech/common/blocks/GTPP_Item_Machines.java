package gtPlusPlus.xmod.gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_ItsNotMyFaultException;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class GTPP_Item_Machines extends ItemBlock {
	public GTPP_Item_Machines(Block par1) {
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setCreativeTab(GregTech_API.TAB_GREGTECH);
	}

	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean par4) {
		try {
			int e = this.getDamage(aStack) + 30400; //Add Offset
			if (e <= 0 || e >= GregTech_API.METATILEENTITIES.length) {
				return;
			}

			if (GregTech_API.METATILEENTITIES[e] != null) {
				
				IGregTechTileEntity aNBT = GregTech_API.METATILEENTITIES[e].getBaseMetaTileEntity();				

				final long tVoltage = aNBT.getInputVoltage();
				byte tTier = (byte) ((byte) Math.max(1, GT_Utility.getTier(tVoltage)));
				
				/*if (aNBT.getDescription() != null) {
					int tAmount = 0;
					String[] arg7 = aNBT.getDescription();
					int arg8 = arg7.length-1;

					if (arg7 != null && arg7.length > 0) {
						for (String t : arg7) {
							aList.add(t);
						}
						
					}
					else {
						aList.add("ERROR");						
					}
					
					for (int y = 0; y < arg8; y++) {
						String tDescription = arg7[y];
						
						if (tDescription != null) {
							aList.add(tDescription+"|"+arg8);
							continue;
						}
						else {
							continue;
						}
					}
				}*/

				if (aNBT.getEUCapacity() > 0L) {
					
					//Custom handling
					if ((e - 30400) <= 100) {
						

						if ((e - 30400) <= 10) {
							tTier -= 2;
							aList.add(EnumChatFormatting.BOLD+"16"+" Fuse Slots"+EnumChatFormatting.GRAY);
							aList.add("One will blow per each A of eu extra passed into it");
							aList.add("Can receive an additional "+EnumChatFormatting.YELLOW+GT_Values.V[tTier]+EnumChatFormatting.GRAY+" EU/t per Fuse");
							aList.add("This machine can accept upto a single amp of "+GT_Values.VN[Math.min(tTier+2, 12)]);
							aList.add(GT_LanguageManager.addStringLocalization("TileEntity_Breaker_Loss", "Breaker Loss: "+EnumChatFormatting.RED+""+(GT_Values.V[Math.max(tTier-1, 0)]/10)+EnumChatFormatting.GRAY+" EU/t", !GregTech_API.sPostloadFinished) + EnumChatFormatting.GRAY);
						}

						
						aList.add(GT_LanguageManager.addStringLocalization("TileEntity_Special_Power_1", EnumChatFormatting.RED+"Special Power Handling, please read manual", !GregTech_API.sPostloadFinished) + EnumChatFormatting.GRAY);
						//aList.add(GT_LanguageManager.addStringLocalization("TileEntity_BreakerBox_2", EnumChatFormatting.RED+"Special Power Handling, please read manual", !GregTech_API.sPostloadFinished) + EnumChatFormatting.GRAY);
						//aList.add(GT_LanguageManager.addStringLocalization("TileEntity_BreakerBox_3", EnumChatFormatting.RED+"Special Power Handling, please read manual", !GregTech_API.sPostloadFinished) + EnumChatFormatting.GRAY);
					}					
					
					
					if (aNBT.getInputVoltage() > 0L) {
						aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_IN", "Voltage IN: ",
								!GregTech_API.sPostloadFinished) + EnumChatFormatting.GREEN + aNBT.getInputVoltage()
								+ " (" + GT_Values.VN[GT_Utility.getTier(aNBT.getInputVoltage())] + ")"
								+ EnumChatFormatting.GRAY);
					}

					if (aNBT.getOutputVoltage() > 0L) {
						aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_OUT", "Voltage OUT: ",
								!GregTech_API.sPostloadFinished) + EnumChatFormatting.GREEN + aNBT.getOutputVoltage()
								+ " (" + GT_Values.VN[GT_Utility.getTier(aNBT.getOutputVoltage())] + ")"
								+ EnumChatFormatting.GRAY);
					}
					
					aList.add(GT_LanguageManager.addStringLocalization("TileEntity_Lossess_EU", "Transmission Loss: "+EnumChatFormatting.DARK_BLUE+"0", !GregTech_API.sPostloadFinished) + EnumChatFormatting.GRAY);
					

					if (aNBT.getOutputAmperage() > 1L) {
						aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_AMOUNT", "Amperage: ",
								!GregTech_API.sPostloadFinished) + EnumChatFormatting.YELLOW + aNBT.getOutputAmperage()
								+ EnumChatFormatting.GRAY);
					}

					aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_STORE", "Capacity: ",
							!GregTech_API.sPostloadFinished) + EnumChatFormatting.BLUE + aNBT.getEUCapacity()
							+ EnumChatFormatting.GRAY);
				}
			}

			NBTTagCompound arg16 = aStack.getTagCompound();
			if (arg16 != null) {
				if (arg16.getBoolean("mMuffler")) {
					aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_MUFFLER", "has Muffler Upgrade",
							!GregTech_API.sPostloadFinished));
				}

				if (arg16.getBoolean("mSteamConverter")) {
					aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMCONVERTER",
							"has Steam Upgrade", !GregTech_API.sPostloadFinished));
				}

				boolean arg17 = false;
				byte arg18;
				if ((arg18 = arg16.getByte("mSteamTanks")) > 0) {
					aList.add(arg18 + " " + GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMTANKS",
							"Steam Tank Upgrades", !GregTech_API.sPostloadFinished));
				}
			}
			

			aList.add(EnumChatFormatting.UNDERLINE+"Special GT++ Machine");
			
		} catch (Throwable arg15) {
			arg15.printStackTrace(GT_Log.err);
		}

	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
		return false;
	}

	public String getUnlocalizedName(ItemStack aStack) {
		short tDamage = (short) (this.getDamage(aStack) + 30400); //Add Offset;
		return tDamage >= 0 && tDamage < GregTech_API.METATILEENTITIES.length
				? (GregTech_API.METATILEENTITIES[tDamage] != null
						? "gt.blockmachines" + "." + GregTech_API.METATILEENTITIES[tDamage].getMetaName()
						: "")
				: "";
	}

	public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		super.onCreated(aStack, aWorld, aPlayer);
		short tDamage = (short) ((short) this.getDamage(aStack) + 30400); //Add Offset;
		if (tDamage < 0
				|| tDamage >= GregTech_API.METATILEENTITIES.length && GregTech_API.METATILEENTITIES[tDamage] != null) {
			GregTech_API.METATILEENTITIES[tDamage].onCreated(aStack, aWorld, aPlayer);
		}

	}

	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side,
			float hitX, float hitY, float hitZ, int aMeta) {
		short tDamage = (short) ((short) this.getDamage(aStack) + 30400); //Add Offset;
		if (tDamage > 0) {
			if (GregTech_API.METATILEENTITIES[tDamage] == null) {
				return false;
			}

			byte tMetaData = GregTech_API.METATILEENTITIES[tDamage].getTileEntityBaseType();
			if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tMetaData, 3)) {
				return false;
			}

			if (aWorld.getBlock(aX, aY, aZ) != this.field_150939_a) {
				throw new GT_ItsNotMyFaultException(
						"Failed to place Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don\'t report this Bug to me, I don\'t know how to fix it.");
			}

			if (aWorld.getBlockMetadata(aX, aY, aZ) != tMetaData) {
				throw new GT_ItsNotMyFaultException(
						"Failed to set the MetaValue of the Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don\'t report this Bug to me, I don\'t know how to fix it.");
			}

			IGregTechTileEntity tTileEntity = (IGregTechTileEntity) aWorld.getTileEntity(aX, aY, aZ);
			if (tTileEntity != null) {
				tTileEntity.setInitialValuesAsNBT(tTileEntity.isServerSide() ? aStack.getTagCompound() : null, tDamage);
				if (aPlayer != null) {
					tTileEntity.setOwnerName(aPlayer.getDisplayName());
				}

				tTileEntity.getMetaTileEntity().initDefaultModes(aStack.getTagCompound());
			}
		} else if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tDamage, 3)) {
			return false;
		}

		if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a) {
			this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
			this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
		}

		return true;
	}
}