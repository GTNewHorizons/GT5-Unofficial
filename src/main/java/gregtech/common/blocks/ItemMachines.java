package gregtech.common.blocks;

import static gregtech.GTMod.GT_FML_LOGGER;
import static gregtech.api.util.GTUtility.translate;
import static gregtech.api.util.tooltip.TooltipMarkupProcessor.INDENT_MARK;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.util.ArrayList;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.CoverableTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
import gregtech.api.util.GTItsNotMyFaultException;
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipMarkupProcessor;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import gregtech.common.tileentities.storage.MTESuperChest;
import gregtech.common.tileentities.storage.MTESuperTank;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GTPPMTEFluidPipe;

public class ItemMachines extends ItemBlock implements IFluidContainerItem {

    public ItemMachines(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
    }

    public static IMetaTileEntity getMetaTileEntity(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return null;
        if (!(aStack.getItem() instanceof ItemMachines)) return null;
        if (aStack.getItemDamage() < 0 || aStack.getItemDamage() > GregTechAPI.METATILEENTITIES.length) return null;
        return GregTechAPI.METATILEENTITIES[aStack.getItemDamage()];
    }

    private String resolveJsonLocalization(String json) {
        try {
            JsonObject obj = new JsonParser().parse(json)
                .getAsJsonObject();

            String key = obj.get("k")
                .getAsString();
            JsonArray paramsArray = obj.getAsJsonArray("p");

            boolean hasIndent = key.startsWith(INDENT_MARK);
            String cleanKey = removeStart(key, INDENT_MARK);

            List<Object> resolvedParams = new ArrayList<>();
            for (JsonElement elem : paramsArray) {
                if (elem.isJsonNull()) {
                    resolvedParams.add("");
                } else {
                    String paramStr = elem.getAsString();
                    if (paramStr.startsWith("{\"k\":")) {
                        resolvedParams.add(resolveJsonLocalization(paramStr));
                    } else {
                        String translated = translateToLocal(paramStr);
                        resolvedParams.add(translated.equals(paramStr) ? paramStr : translated);
                    }
                }
            }

            String result = translate(cleanKey, resolvedParams.toArray());
            if (result.isEmpty()) return key;
            return hasIndent ? INDENT_MARK + result : result;
        } catch (Exception e) {
            GT_FML_LOGGER.error("Failed to parse localization JSON: " + json, e);
            return json;
        }
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        try {
            final int tDamage = getDamage(aStack);
            if ((tDamage <= 0) || (tDamage >= GregTechAPI.METATILEENTITIES.length)) {
                return;
            }

            if (GregTechAPI.METATILEENTITIES[tDamage] != null) {
                final IGregTechTileEntity tTileEntity = GregTechAPI.METATILEENTITIES[tDamage].getBaseMetaTileEntity();
                final IMetaTileEntity tMetaTileEntity = tTileEntity.getMetaTileEntity();
                {
                    addDescription(aList, tMetaTileEntity.getDescription());
                    tMetaTileEntity.addAdditionalTooltipInformation(aStack, aList);
                }
                if (tTileEntity.getEUCapacity() > 0L) {
                    if (tTileEntity.getInputVoltage() > 0L) {
                        aList.add(
                            translateToLocal("gt.tileentity.eup_in") + " "
                                + TooltipHelper.voltageText(tTileEntity.getInputVoltage()));
                    }
                    if (tTileEntity.getOutputVoltage() > 0L) {
                        aList.add(
                            translateToLocal("gt.tileentity.eup_out") + " "
                                + TooltipHelper.voltageText(tTileEntity.getOutputVoltage()));
                    }
                    if (tTileEntity.getOutputAmperage() > 1L) {
                        aList.add(
                            translateToLocal("gt.tileentity.eup_amount") + " "
                                + TooltipHelper.ampText(tTileEntity.getOutputAmperage()));
                    }
                }
            }
            final NBTTagCompound aNBT = aStack.getTagCompound();
            if (aNBT != null) {
                if (aNBT.getBoolean("mMuffler")) {
                    aList.add(translateToLocal("gt.tileentity.has_muffler"));
                }
                if (aNBT.getBoolean("mSteamConverter")) {
                    aList.add(translateToLocal("gt.tileentity.has_steam_upgrade"));
                }
                int tAmount;
                if ((tAmount = aNBT.getByte("mSteamTanks")) > 0) {
                    aList.add(translateToLocalFormatted("gt.tileentity.steamtanks", tAmount));
                }

                CoverableTileEntity.addInstalledCoversInformation(aNBT, aList);
                if (aNBT.hasKey("mColor") && aNBT.getByte("mColor") != -1) {
                    aList.add(
                        translateToLocalFormatted(
                            "gt.tileentity.colored",
                            Dyes.get(aNBT.getByte("mColor") - 1).formatting,
                            Dyes.get(aNBT.getByte("mColor") - 1).mName));
                }
            }
        } catch (Exception e) {
            aList.add("§cAn exception was thrown while getting this item's info.§r");
            aList.add(e.getLocalizedMessage());
            GT_FML_LOGGER.error("addInformation", e);
        }
    }

    private void addDescription(@Nullable List<String> aList, @Nullable String[] aDescription) {
        if (aDescription == null) return;
        for (String tDescLine : aDescription) {
            if (!GTUtility.isStringValid(tDescLine)) continue;

            String translated;
            if (tDescLine.startsWith("{\"k\":")) {
                translated = resolveJsonLocalization(tDescLine);
            } else {
                translated = translate(removeStart(tDescLine, INDENT_MARK));
            }

            final String formatted = TooltipMarkupProcessor.formatTranslatedLine(tDescLine, translated);
            if (aList != null) aList.add(formatted.isEmpty() ? tDescLine : formatted);
        }
        TooltipMarkupProcessor.processTooltips(aList);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        final short tDamage = (short) getDamage(aStack);
        if ((tDamage < 0) || (tDamage >= GregTechAPI.METATILEENTITIES.length)) {
            return "";
        }
        if (GregTechAPI.METATILEENTITIES[tDamage] != null) {
            return getUnlocalizedName() + "." + GregTechAPI.METATILEENTITIES[tDamage].getMetaName();
        }
        return "";
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        final short aDamage = (short) getDamage(aStack);
        if (aDamage >= 0 && aDamage < GregTechAPI.METATILEENTITIES.length
            && GregTechAPI.METATILEENTITIES[aDamage] != null) {
            Materials aMaterial = null;
            if (GregTechAPI.METATILEENTITIES[aDamage] instanceof MTEItemPipe itemPipe) {
                aMaterial = itemPipe.mMaterial;
            } else if (GregTechAPI.METATILEENTITIES[aDamage] instanceof GTPPMTEFluidPipe gtppFluidPipe) {
                aName = gtppFluidPipe.pipeStats.getLocalizedNameForItem(aName);
            } else if (GregTechAPI.METATILEENTITIES[aDamage] instanceof MTEFluidPipe fluidPipe) {
                aMaterial = fluidPipe.mMaterial;
            } else if (GregTechAPI.METATILEENTITIES[aDamage] instanceof MTECable cable) {
                aMaterial = cable.mMaterial;
            } else if (GregTechAPI.METATILEENTITIES[aDamage] instanceof MTEFrame frame) {
                aMaterial = frame.mMaterial;
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
        if ((tDamage < 0)
            || ((tDamage >= GregTechAPI.METATILEENTITIES.length) && (GregTechAPI.METATILEENTITIES[tDamage] != null))) {
            GregTechAPI.METATILEENTITIES[tDamage].onCreated(aStack, aWorld, aPlayer);
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int ordinalSide, float hitX, float hitY, float hitZ, int aMeta) {
        final ForgeDirection side = ForgeDirection.getOrientation(ordinalSide);
        final short tDamage = (short) getDamage(aStack);
        if (tDamage > 0) {
            if (GregTechAPI.METATILEENTITIES[tDamage] == null) {
                return false;
            }
            final int tMetaData = GregTechAPI.METATILEENTITIES[tDamage].getTileEntityBaseType();
            if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, tMetaData, 3)) {
                return false;
            }
            if (aWorld.getBlock(aX, aY, aZ) != this.field_150939_a) {
                throw new GTItsNotMyFaultException(
                    "Failed to place Block even though World.setBlock returned true. It COULD be MCPC/Bukkit causing that. In case you really have that installed, don't report this Bug to me, I don't know how to fix it.");
            }
            if (aWorld.getBlockMetadata(aX, aY, aZ) != tMetaData) {
                throw new GTItsNotMyFaultException(
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
        if (GregTechAPI.METATILEENTITIES[tDamage] instanceof MTESuperChest
            || GregTechAPI.METATILEENTITIES[tDamage] instanceof MTESuperTank) {
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
            final IMetaTileEntity tMetaTile = GregTechAPI.METATILEENTITIES[tDamage];
            if (tMetaTile != null) return tMetaTile.getCapacity();
        }
        return 0;
    }

    @Nullable
    private Fluid getLockedFluid(@Nonnull ItemStack container) {
        final NBTTagCompound tag = container.stackTagCompound;
        if (tag == null) return null;
        String lockedName = tag.getString("lockedFluidName");
        if (GTUtility.isStringInvalid(lockedName)) return null;
        return FluidRegistry.getFluid(lockedName);
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (container != null && resource != null) {
            final int tDamage = container.getItemDamage();
            final IMetaTileEntity tMetaTile = GregTechAPI.METATILEENTITIES[tDamage];
            if (!(tMetaTile instanceof MTEDigitalTankBase)) {
                return 0;
            }
            if (container.stackSize > 1) {
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
            final IMetaTileEntity tMetaTile = GregTechAPI.METATILEENTITIES[tDamage];
            if (!(tMetaTile instanceof MTEDigitalTankBase)) {
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
