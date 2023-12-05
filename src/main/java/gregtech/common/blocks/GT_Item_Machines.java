package gregtech.common.blocks;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item;
import gregtech.api.util.GT_ItsNotMyFaultException;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalTankBase;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperChest;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_SuperTank;

public class GT_Item_Machines extends ItemBlock implements IFluidContainerItem {

    public GT_Item_Machines(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
    }

    public static IMetaTileEntity getMetaTileEntity(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return null;
        if (!(aStack.getItem() instanceof GT_Item_Machines)) return null;
        if (aStack.getItemDamage() < 0 || aStack.getItemDamage() > GregTech_API.METATILEENTITIES.length) return null;
        return GregTech_API.METATILEENTITIES[aStack.getItemDamage()];
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        try {
            final int tDamage = getDamage(aStack);
            if ((tDamage <= 0) || (tDamage >= GregTech_API.METATILEENTITIES.length)) {
                return;
            }

            if (GregTech_API.METATILEENTITIES[tDamage] != null) {
                final IGregTechTileEntity tTileEntity = GregTech_API.METATILEENTITIES[tDamage].getBaseMetaTileEntity();
                if (!GregTech_API.sPostloadFinished
                    && tTileEntity.getMetaTileEntity() instanceof ISecondaryDescribable) {
                    final String[] tSecondaryDescription = ((ISecondaryDescribable) tTileEntity.getMetaTileEntity())
                        .getSecondaryDescription();
                    addDescription(null, tSecondaryDescription, tDamage, "_Secondary", true);
                }
                {
                    final IMetaTileEntity tMetaTileEntity = tTileEntity.getMetaTileEntity();
                    final String tSuffix = (tMetaTileEntity instanceof ISecondaryDescribable
                        && ((ISecondaryDescribable) tMetaTileEntity).isDisplaySecondaryDescription()) ? "_Secondary"
                            : "";
                    addDescription(
                        aList,
                        tTileEntity.getDescription(),
                        tDamage,
                        tSuffix,
                        !GregTech_API.sPostloadFinished);
                    tMetaTileEntity.addAdditionalTooltipInformation(aStack, aList);
                }
                if (tTileEntity.getEUCapacity() > 0L) {
                    if (tTileEntity.getInputVoltage() > 0L) {
                        final byte inputTier = GT_Utility.getTier(tTileEntity.getInputVoltage());
                        aList.add(
                            GT_LanguageManager.addStringLocalization(
                                "TileEntity_EUp_IN",
                                "Voltage IN: ",
                                !GregTech_API.sPostloadFinished) + EnumChatFormatting.GREEN
                                + GT_Utility.formatNumbers(tTileEntity.getInputVoltage())
                                + " ("
                                + GT_Utility.getColoredTierNameFromTier(inputTier)
                                + EnumChatFormatting.GREEN
                                + ")"
                                + EnumChatFormatting.GRAY);
                    }
                    if (tTileEntity.getOutputVoltage() > 0L) {
                        final byte outputTier = GT_Utility.getTier(tTileEntity.getOutputVoltage());
                        aList.add(
                            GT_LanguageManager.addStringLocalization(
                                "TileEntity_EUp_OUT",
                                "Voltage OUT: ",
                                !GregTech_API.sPostloadFinished) + EnumChatFormatting.GREEN
                                + GT_Utility.formatNumbers(tTileEntity.getOutputVoltage())
                                + " ("
                                + GT_Utility.getColoredTierNameFromTier(outputTier)
                                + EnumChatFormatting.GREEN
                                + ")"
                                + EnumChatFormatting.GRAY);
                    }
                    if (tTileEntity.getOutputAmperage() > 1L) {
                        aList.add(
                            GT_LanguageManager.addStringLocalization(
                                "TileEntity_EUp_AMOUNT",
                                "Amperage: ",
                                !GregTech_API.sPostloadFinished) + EnumChatFormatting.YELLOW
                                + GT_Utility.formatNumbers(tTileEntity.getOutputAmperage())
                                + EnumChatFormatting.GRAY);
                    }
                    aList.add(
                        GT_LanguageManager.addStringLocalization(
                            "TileEntity_EUp_STORE",
                            "Capacity: ",
                            !GregTech_API.sPostloadFinished) + EnumChatFormatting.BLUE
                            + GT_Utility.formatNumbers(tTileEntity.getEUCapacity())
                            + EnumChatFormatting.GRAY
                            + " EU");
                }
            }
            final NBTTagCompound aNBT = aStack.getTagCompound();
            if (aNBT != null) {
                if (aNBT.getBoolean("mMuffler")) {
                    aList.add(
                        GT_LanguageManager.addStringLocalization(
                            "GT_TileEntity_MUFFLER",
                            "has Muffler Upgrade",
                            !GregTech_API.sPostloadFinished));
                }
                if (aNBT.getBoolean("mSteamConverter")) {
                    aList.add(
                        GT_LanguageManager.addStringLocalization(
                            "GT_TileEntity_STEAMCONVERTER",
                            "has Steam Upgrade",
                            !GregTech_API.sPostloadFinished));
                }
                int tAmount = 0;
                if ((tAmount = aNBT.getByte("mSteamTanks")) > 0) {
                    aList.add(
                        tAmount + " "
                            + GT_LanguageManager.addStringLocalization(
                                "GT_TileEntity_STEAMTANKS",
                                "Steam Tank Upgrades",
                                !GregTech_API.sPostloadFinished));
                }

                CoverableTileEntity.addInstalledCoversInformation(aNBT, aList);
                if (aNBT.hasKey("mColor") && aNBT.getByte("mColor") != -1) {
                    aList.add(
                        GT_LanguageManager
                            .addStringLocalization("GT_TileEntity_COLORED", "Colored", !GregTech_API.sPostloadFinished)
                            + " ("
                            + Dyes.get(aNBT.getByte("mColor") - 1).formatting
                            + Dyes.get(aNBT.getByte("mColor") - 1).mName
                            + EnumChatFormatting.GRAY
                            + ")");
                }
            }
        } catch (Throwable e) {
            GT_FML_LOGGER.error("addInformation", e);
        }
    }

    private void addDescription(@Nullable List<String> aList, @Nullable String[] aDescription, int aDamage,
        String aSuffix, boolean aWriteIntoLangFile) {
        if (aDescription == null) return;
        for (int i = 0, tLength = aDescription.length; i < tLength; i++) {
            String tDescLine = aDescription[i];
            if (!GT_Utility.isStringValid(tDescLine)) continue;

            String tKey = String.format("TileEntity_DESCRIPTION_%05d%s_Index_%02d", aDamage, aSuffix, i);
            if (tDescLine.contains("%%%")) {
                final String[] tSplitStrings = tDescLine.split("%%%");
                final StringBuilder tBuffer = new StringBuilder();
                final String[] tRep = new String[tSplitStrings.length / 2];
                for (int j = 0; j < tSplitStrings.length; j++) if (j % 2 == 0) tBuffer.append(tSplitStrings[j]);
                else {
                    tBuffer.append("%s");
                    tRep[j / 2] = tSplitStrings[j];
                }
                final String tTranslated = String.format(
                    GT_LanguageManager.addStringLocalization(tKey, tBuffer.toString(), aWriteIntoLangFile),
                    (Object[]) tRep);
                if (aList != null) aList.add(tTranslated);
            } else {
                String tTranslated = GT_LanguageManager.addStringLocalization(tKey, tDescLine, aWriteIntoLangFile);
                if (aList != null) aList.add(tTranslated.equals("") ? tDescLine : tTranslated);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerDescription(int aDamage) {
        if (aDamage >= GregTech_API.METATILEENTITIES.length) return;
        if (GregTech_API.METATILEENTITIES[aDamage] != null) {
            final IMetaTileEntity tMetaTileEntity = GregTech_API.METATILEENTITIES[aDamage].getBaseMetaTileEntity()
                .getMetaTileEntity();
            if (tMetaTileEntity instanceof ISecondaryDescribable) {
                final String[] tSecondaryDescription = ((ISecondaryDescribable) tMetaTileEntity)
                    .getSecondaryDescription();
                addDescription(null, tSecondaryDescription, aDamage, "_Secondary", true);
            }
            addDescription(null, tMetaTileEntity.getDescription(), aDamage, "", true);
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        final short tDamage = (short) getDamage(aStack);
        if ((tDamage < 0) || (tDamage >= GregTech_API.METATILEENTITIES.length)) {
            return "";
        }
        if (GregTech_API.METATILEENTITIES[tDamage] != null) {
            return getUnlocalizedName() + "." + GregTech_API.METATILEENTITIES[tDamage].getMetaName();
        }
        return "";
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        final short aDamage = (short) getDamage(aStack);
        if (aDamage >= 0 && aDamage < GregTech_API.METATILEENTITIES.length
            && GregTech_API.METATILEENTITIES[aDamage] != null) {
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

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        super.onCreated(aStack, aWorld, aPlayer);
        final short tDamage = (short) getDamage(aStack);
        if ((tDamage < 0) || ((tDamage >= GregTech_API.METATILEENTITIES.length)
            && (GregTech_API.METATILEENTITIES[tDamage] != null))) {
            GregTech_API.METATILEENTITIES[tDamage].onCreated(aStack, aWorld, aPlayer);
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int ordinalSide, float hitX, float hitY, float hitZ, int aMeta) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        final short tDamage = (short) getDamage(aStack);
        if (tDamage > 0) {
            if (GregTech_API.METATILEENTITIES[tDamage] == null) {
                return false;
            }
            final int tMetaData = GregTech_API.METATILEENTITIES[tDamage].getTileEntityBaseType();
            if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tMetaData, 3)) {
                return false;
            }
            if (aWorld.getBlock(aX, aY, aZ) != this.field_150939_a) {
                throw new GT_ItsNotMyFaultException(
                    "Failed to place Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don't report this Bug to me, I don't know how to fix it.");
            }
            if (aWorld.getBlockMetadata(aX, aY, aZ) != tMetaData) {
                throw new GT_ItsNotMyFaultException(
                    "Failed to set the MetaValue of the Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don't report this Bug to me, I don't know how to fix it.");
            }
            final IGregTechTileEntity tTileEntity = (IGregTechTileEntity) aWorld.getTileEntity(aX, aY, aZ);
            if (tTileEntity != null) {
                tTileEntity.setInitialValuesAsNBT(tTileEntity.isServerSide() ? aStack.getTagCompound() : null, tDamage);
                if (aPlayer != null) {
                    tTileEntity.setOwnerName(aPlayer.getDisplayName());
                    tTileEntity.setOwnerUuid(aPlayer.getUniqueID());
                }
                tTileEntity.getMetaTileEntity()
                    .initDefaultModes(aStack.getTagCompound());
                final ForgeDirection oppositeSide = side.getOpposite();
                if (tTileEntity.getMetaTileEntity() instanceof IConnectable connectable) {
                    // If we're connectable, try connecting to whatever we're up against
                    connectable.connect(oppositeSide);
                } else if (aPlayer != null && aPlayer.isSneaking()) {
                    // If we're being placed against something that is connectable, try telling it to connect to us
                    final IGregTechTileEntity aTileEntity = tTileEntity.getIGregTechTileEntityAtSide(oppositeSide);
                    if (aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof IConnectable connectable) {
                        connectable.connect(side);
                    }
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

    @Override
    public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {
        super.onUpdate(aStack, aWorld, aPlayer, aTimer, aIsInHand);
        final short tDamage = (short) getDamage(aStack);
        final EntityLivingBase tPlayer = (EntityPlayer) aPlayer;
        if (GregTech_API.METATILEENTITIES[tDamage] instanceof GT_MetaTileEntity_SuperChest
            || GregTech_API.METATILEENTITIES[tDamage] instanceof GT_MetaTileEntity_SuperTank) {
            final NBTTagCompound tNBT = aStack.stackTagCompound;
            if (tNBT == null) return;
            if (tNBT.hasNoTags()) {
                aStack.setTagCompound(null);
                return;
            }
            if ((tNBT.hasKey("mItemCount") && tNBT.getInteger("mItemCount") > 0) || (tNBT.hasKey("mFluid")
                && FluidStack.loadFluidStackFromNBT(tNBT.getCompoundTag("mFluid")).amount > 64000)) {
                final FluidStack tFluid = FluidStack.loadFluidStackFromNBT(tNBT.getCompoundTag("mFluid"));
                int tEffectDuration = 1200;
                if (tFluid != null) {
                    final double tFluidAmount = tFluid.amount;
                    final double tMiddlePoint = 4096000;
                    final double tSmoothingCoefficient = 2000000;
                    final int tMaxDuration = 12000;
                    final double tmp = (tFluidAmount - tMiddlePoint) / tSmoothingCoefficient;
                    tEffectDuration = (int) (Math.exp(tmp) / (Math.exp(tmp) + Math.exp(-tmp)) * tMaxDuration);
                }
                tPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, tEffectDuration, 1));
                tPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, tEffectDuration, 1));
                tPlayer.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, tEffectDuration, 1));
                tPlayer.addPotionEffect(new PotionEffect(Potion.weakness.id, tEffectDuration, 1));
            }
        }
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        if (container != null) {
            final NBTTagCompound tNBT = container.stackTagCompound;
            if (tNBT != null && tNBT.hasKey("mFluid", 10)) {
                return FluidStack.loadFluidStackFromNBT(tNBT.getCompoundTag("mFluid"));
            }
        }
        return null;
    }

    @Override
    public int getCapacity(ItemStack container) {
        if (container != null) {
            final int tDamage = container.getItemDamage();
            final IMetaTileEntity tMetaTile = GregTech_API.METATILEENTITIES[tDamage];
            if (tMetaTile != null) return tMetaTile.getCapacity();
        }
        return 0;
    }

    @Nullable
    private Fluid getLockedFluid(@Nonnull ItemStack container) {
        final NBTTagCompound tag = container.stackTagCompound;
        if (tag == null) return null;
        String lockedName = tag.getString("lockedFluidName");
        if (GT_Utility.isStringInvalid(lockedName)) return null;
        return FluidRegistry.getFluid(lockedName);
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (container != null && resource != null) {
            final int tDamage = container.getItemDamage();
            final IMetaTileEntity tMetaTile = GregTech_API.METATILEENTITIES[tDamage];
            if (!(tMetaTile instanceof GT_MetaTileEntity_DigitalTankBase)) {
                return 0;
            }
            if (container.stackTagCompound == null) container.stackTagCompound = new NBTTagCompound();
            final FluidStack tStoredFluid = getFluid(container);
            final int tCapacity = getCapacity(container);
            if (tCapacity <= 0) return 0;
            final Fluid lockedFluid = getLockedFluid(container);
            if (lockedFluid != null && resource.getFluid() != lockedFluid) {
                return 0;
            }
            if (tStoredFluid != null && tStoredFluid.isFluidEqual(resource)) {
                final int tAmount = Math.min(tCapacity - tStoredFluid.amount, resource.amount);
                if (doFill) {
                    final FluidStack tNewFluid = new FluidStack(tStoredFluid, tAmount + tStoredFluid.amount);
                    container.stackTagCompound.setTag("mFluid", tNewFluid.writeToNBT(new NBTTagCompound()));
                }
                return tAmount;
            }
            if (tStoredFluid == null) {
                final int tAmount = Math.min(tCapacity, resource.amount);
                if (doFill) {
                    final FluidStack tNewFluid = new FluidStack(resource, tAmount);
                    container.stackTagCompound.setTag("mFluid", tNewFluid.writeToNBT(new NBTTagCompound()));
                }
                return tAmount;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (container != null && container.hasTagCompound()) {
            final int tDamage = container.getItemDamage();
            final IMetaTileEntity tMetaTile = GregTech_API.METATILEENTITIES[tDamage];
            if (!(tMetaTile instanceof GT_MetaTileEntity_DigitalTankBase)) {
                return null;
            }
            final FluidStack tStoredFluid = getFluid(container);
            if (tStoredFluid != null) {
                final int tAmount = Math.min(maxDrain, tStoredFluid.amount);
                final FluidStack tNewFluid = new FluidStack(tStoredFluid, tStoredFluid.amount - tAmount);
                final FluidStack tOutputFluid = new FluidStack(tStoredFluid, tAmount);
                if (doDrain) {
                    if (tNewFluid.amount <= 0) {
                        container.stackTagCompound.removeTag("mFluid");
                        if (container.stackTagCompound.hasNoTags()) container.setTagCompound(null);
                    } else {
                        container.stackTagCompound.setTag("mFluid", tNewFluid.writeToNBT(new NBTTagCompound()));
                    }
                }
                return tOutputFluid;
            }
        }
        return null;
    }
}
