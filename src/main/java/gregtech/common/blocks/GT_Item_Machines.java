package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;
import gregtech.api.util.GT_ItsNotMyFaultException;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class GT_Item_Machines
        extends ItemBlock {
    public GT_Item_Machines(Block par1) {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
    }

    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean par4) {
        try {
            int tDamage = getDamage(aStack);
            if ((tDamage <= 0) || (tDamage >= GregTech_API.METATILEENTITIES.length)) {
                return;
            }

            if (GregTech_API.METATILEENTITIES[tDamage] != null) {
                IGregTechTileEntity tTileEntity = GregTech_API.METATILEENTITIES[tDamage].getBaseMetaTileEntity();
                if (tTileEntity.getDescription() != null) {
                    int i = 0;
                    for (String tDescription : tTileEntity.getDescription()) {
                        if (GT_Utility.isStringValid(tDescription)) {
                        	if(tDescription.contains("%%%")){
                        		String[] tString = tDescription.split("%%%");
                        		if(tString.length>=2){
                                                StringBuffer tBuffer = new StringBuffer();
                        			Object tRep[] = new String[tString.length / 2];
                        			for (int j = 0; j < tString.length; j++)
                        				if (j % 2 == 0) tBuffer.append(tString[j]);
                        				else {tBuffer.append(" %s"); tRep[j / 2] = tString[j];}
                                                aList.add(String.format(GT_LanguageManager.addStringLocalization("TileEntity_DESCRIPTION_" + tDamage + "_Index_" + i++, tBuffer.toString(), !GregTech_API.sPostloadFinished), tRep));
                        		}
                        	}else{String tTranslated = GT_LanguageManager.addStringLocalization("TileEntity_DESCRIPTION_" + tDamage + "_Index_" + i++, tDescription, !GregTech_API.sPostloadFinished );
                            aList.add(tTranslated.equals("") ? tDescription : tTranslated);}
                        }else i++;
                    }
                }
                if (tTileEntity.getEUCapacity() > 0L) {
                    if (tTileEntity.getInputVoltage() > 0L) {
                        aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_IN", "Voltage IN: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.GREEN + tTileEntity.getInputVoltage() + " (" + GT_Values.VN[GT_Utility.getTier(tTileEntity.getInputVoltage())] + ")" + EnumChatFormatting.GRAY);
                    }
                    if (tTileEntity.getOutputVoltage() > 0L) {
                        aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_OUT", "Voltage OUT: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.GREEN + tTileEntity.getOutputVoltage() + " (" + GT_Values.VN[GT_Utility.getTier(tTileEntity.getOutputVoltage())] + ")" + EnumChatFormatting.GRAY);
                    }
                    if (tTileEntity.getOutputAmperage() > 1L) {
                        aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_AMOUNT", "Amperage: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.YELLOW + tTileEntity.getOutputAmperage() + EnumChatFormatting.GRAY);
                    }
                    aList.add(GT_LanguageManager.addStringLocalization("TileEntity_EUp_STORE", "Capacity: ", !GregTech_API.sPostloadFinished ) + EnumChatFormatting.BLUE + tTileEntity.getEUCapacity() + EnumChatFormatting.GRAY);
                }
            }
            NBTTagCompound aNBT = aStack.getTagCompound();
            if (aNBT != null) {
                if (aNBT.getBoolean("mMuffler")) {
                    aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_MUFFLER", "has Muffler Upgrade", !GregTech_API.sPostloadFinished ));
                }
                if (aNBT.getBoolean("mSteamConverter")) {
                    aList.add(GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMCONVERTER", "has Steam Upgrade", !GregTech_API.sPostloadFinished ));
                }
                int tAmount = 0;
                if ((tAmount = aNBT.getByte("mSteamTanks")) > 0) {
                    aList.add(tAmount + " " + GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMTANKS", "Steam Tank Upgrades", !GregTech_API.sPostloadFinished ));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public String getUnlocalizedName(ItemStack aStack) {
        short tDamage = (short) getDamage(aStack);
        if ((tDamage < 0) || (tDamage >= GregTech_API.METATILEENTITIES.length)) {
            return "";
        }
        if (GregTech_API.METATILEENTITIES[tDamage] != null) {
            return getUnlocalizedName() + "." + GregTech_API.METATILEENTITIES[tDamage].getMetaName();
        }
        return "";
    }

    public String getItemStackDisplayName(ItemStack aStack) {
    	String aName = super.getItemStackDisplayName(aStack);
    	short aDamage = (short) getDamage(aStack);
    	if (aDamage >= 0 && aDamage < GregTech_API.METATILEENTITIES.length && GregTech_API.METATILEENTITIES[aDamage] != null) {
            Materials aMaterial = null;
            if (GregTech_API.METATILEENTITIES[aDamage] instanceof GT_MetaPipeEntity_Item) {
            	aMaterial = ((GT_MetaPipeEntity_Item) GregTech_API.METATILEENTITIES[aDamage]).mMaterial;
            } else if (GregTech_API.METATILEENTITIES[aDamage] instanceof GT_MetaPipeEntity_Fluid) {
            	aMaterial = ((GT_MetaPipeEntity_Fluid) GregTech_API.METATILEENTITIES[aDamage]).mMaterial;
            } else if (GregTech_API.METATILEENTITIES[aDamage] instanceof GT_MetaPipeEntity_Cable) {
            	aMaterial = ((GT_MetaPipeEntity_Cable) GregTech_API.METATILEENTITIES[aDamage]).mMaterial;
            } else if (GregTech_API.METATILEENTITIES[aDamage] instanceof GT_MetaPipeEntity_Frame) {
            	aMaterial = ((GT_MetaPipeEntity_Frame) GregTech_API.METATILEENTITIES[aDamage]).mMaterial;
            }
            if (aMaterial != null) {
            	aName = aMaterial.getLocalizedNameForItem(aName);
            }
        }
    	return aName;
    }

    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        super.onCreated(aStack, aWorld, aPlayer);
        short tDamage = (short) getDamage(aStack);
        if ((tDamage < 0) || ((tDamage >= GregTech_API.METATILEENTITIES.length) && (GregTech_API.METATILEENTITIES[tDamage] != null))) {
            GregTech_API.METATILEENTITIES[tDamage].onCreated(aStack, aWorld, aPlayer);
        }
    }

    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta) {
        short tDamage = (short) getDamage(aStack);
        if (tDamage > 0) {
            if (GregTech_API.METATILEENTITIES[tDamage] == null) {
                return false;
            }
            int tMetaData = GregTech_API.METATILEENTITIES[tDamage].getTileEntityBaseType();
            if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tMetaData, 3)) {
                return false;
            }
            if (aWorld.getBlock(aX, aY, aZ) != this.field_150939_a) {
                throw new GT_ItsNotMyFaultException("Failed to place Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don't report this Bug to me, I don't know how to fix it.");
            }
            if (aWorld.getBlockMetadata(aX, aY, aZ) != tMetaData) {
                throw new GT_ItsNotMyFaultException("Failed to set the MetaValue of the Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don't report this Bug to me, I don't know how to fix it.");
            }
            IGregTechTileEntity tTileEntity = (IGregTechTileEntity) aWorld.getTileEntity(aX, aY, aZ);
            if (tTileEntity != null) {
                tTileEntity.setInitialValuesAsNBT(tTileEntity.isServerSide() ? aStack.getTagCompound() : null, tDamage);
                if (aPlayer != null) {
                    tTileEntity.setOwnerName(aPlayer.getDisplayName());
                }
                tTileEntity.getMetaTileEntity().initDefaultModes(aStack.getTagCompound());
                if (tTileEntity.getMetaTileEntity() instanceof IConnectable) {
                	((IConnectable) tTileEntity.getMetaTileEntity()).connect(GT_Utility.getOppositeSide(side));
                }
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
