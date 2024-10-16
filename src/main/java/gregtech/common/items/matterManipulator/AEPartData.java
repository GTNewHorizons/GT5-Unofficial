package gregtech.common.items.matterManipulator;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.gson.JsonElement;
import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.api.AEApi;
import appeng.api.implementations.tiles.ISegmentedInventory;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.IPartItem;
import appeng.api.parts.PartItemStack;
import appeng.api.util.IConfigurableObject;
import appeng.helpers.ICustomNameObject;
import appeng.helpers.IOreFilterable;
import appeng.me.GridAccessException;
import appeng.me.cache.P2PCache;
import appeng.parts.automation.UpgradeInventory;
import appeng.parts.p2p.PartP2PTunnel;
import appeng.parts.p2p.PartP2PTunnelNormal;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;

public class AEPartData {

    public PortableItemStack mPart;
    public String mSettingsName = null;
    public JsonElement mSettings = null;
    public String mCustomName = null;

    public PortableItemStack[] mAEUpgrades = null;
    public InventoryAnalysis mConfig = null;
    public String mOreDict = null;

    public Boolean mP2POutput = null;
    public Long mP2PFreq = null;

    private transient Optional<Class<? extends IPart>> mPartClass;

    public AEPartData(IPart part) {
        mPart = new PortableItemStack(part.getItemStack(PartItemStack.Wrench));

        if (part instanceof ICustomNameObject customName) {
            mCustomName = customName.hasCustomName() ? customName.getCustomName() : null;
        }

        if (part instanceof IOreFilterable filterable) {
            mOreDict = filterable.getFilter();

            if ("".equals(mOreDict)) mOreDict = null;
        }

        if (part instanceof IConfigurableObject configurable && configurable.getConfigManager() != null) {
            NBTTagCompound settings = new NBTTagCompound();
            configurable.getConfigManager()
                .writeToNBT(settings);
            mSettings = settings.hasNoTags() ? null : MMUtils.toJsonObject(settings);
        }

        if (part instanceof PartP2PTunnel tunnel) {
            mP2POutput = tunnel.isOutput();
            mP2PFreq = tunnel.getFrequency();
        }

        if (part instanceof ISegmentedInventory segmentedInventory) {
            IInventory upgrades = segmentedInventory.getInventoryByName("upgrades");

            if (upgrades != null) {
                mAEUpgrades = GTUtility.streamInventory(upgrades)
                    .filter(x -> x != null)
                    .map(PortableItemStack::new)
                    .toArray(PortableItemStack[]::new);
            }

            IInventory config = segmentedInventory.getInventoryByName("config");

            if (config != null) {
                mConfig = InventoryAnalysis.fromInventory(config, false);
            }
        }
    }

    public Class<? extends IPart> getPartClass() {
        if (mPartClass == null) {
            if (mPart.toStack()
                .getItem() instanceof IPartItem partItem) {
                IPart part = partItem.createPartFromItemStack(mPart.toStack());

                mPartClass = Optional.ofNullable(part)
                    .map(IPart::getClass);
            }
        }

        return mPartClass.orElse(null);
    }

    public boolean isPartSubclassOf(Class<? extends IPart> superclass) {
        return superclass.isAssignableFrom(getPartClass());
    }

    public boolean isP2P() {
        return isPartSubclassOf(PartP2PTunnel.class);
    }

    public boolean isAttunable() {
        return isPartSubclassOf(PartP2PTunnelNormal.class);
    }

    public boolean updatePart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side) {
        IPart part = partHost.getPart(side);

        if (part instanceof PartP2PTunnelNormal && isAttunable()) {
            partHost.removePart(side, true);

            partHost.addPart(mPart.toStack(), side, context.getRealPlayer());
            part = partHost.getPart(side);
        }

        if (part instanceof PartP2PTunnel<?>tunnel) {
            long freq = mP2PFreq == null ? 0 : mP2PFreq;

            tunnel.output = true;

            try {
                final P2PCache p2p = tunnel.getProxy()
                    .getP2P();
                p2p.updateFreq(tunnel, freq);
            } catch (final GridAccessException e) {
                tunnel.setFrequency(freq);
            }

            tunnel.onTunnelConfigChange();
        }

        if (part instanceof ICustomNameObject customName) {
            if (mCustomName != null) customName.setCustomName(mCustomName);
        }

        if (part instanceof IConfigurableObject configurable && configurable.getConfigManager() != null) {
            NBTTagCompound settings = mSettings == null ? new NBTTagCompound()
                : (NBTTagCompound) MMUtils.toNbt(mSettings);
            configurable.getConfigManager()
                .readFromNBT(settings);
        }

        if (part instanceof ISegmentedInventory segmentedInventory) {
            if (segmentedInventory.getInventoryByName("upgrades") instanceof UpgradeInventory upgradeInv) {
                ItemStackMap<Long> targetMap = GTUtility.getItemStackHistogram(
                    Arrays.stream(mAEUpgrades)
                        .map(PortableItemStack::toStack)
                        .collect(Collectors.toList()));
                ItemStackMap<Long> actualMap = GTUtility
                    .getItemStackHistogram(Arrays.asList(GTUtility.inventoryToArray(upgradeInv)));

                if (!targetMap.equals(actualMap)) {
                    if (!MMUtils.installUpgrades(context, upgradeInv, mAEUpgrades, true, false)) {
                        return false;
                    }
                }
            }

            IInventory config = segmentedInventory.getInventoryByName("config");

            if (config != null) {
                mConfig.apply(context, config, false, false);
            }
        }

        if (part instanceof IOreFilterable filterable) {
            filterable.setFilter(mOreDict == null ? "" : mOreDict);
        }

        return true;
    }

    public boolean getRequiredItemsForExistingPart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side) {
        IPart part = partHost.getPart(side);

        if (part instanceof ISegmentedInventory segmentedInventory) {
            if (segmentedInventory.getInventoryByName("upgrades") instanceof UpgradeInventory upgradeInv) {
                ItemStackMap<Long> targetMap = GTUtility.getItemStackHistogram(
                    Arrays.stream(mAEUpgrades)
                        .map(PortableItemStack::toStack)
                        .collect(Collectors.toList()));
                ItemStackMap<Long> actualMap = GTUtility
                    .getItemStackHistogram(Arrays.asList(GTUtility.inventoryToArray(upgradeInv)));

                if (!targetMap.equals(actualMap)) {
                    if (!MMUtils.installUpgrades(context, upgradeInv, mAEUpgrades, true, true)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean getRequiredItemsForNewPart(IBlockApplyContext context) {
        if (mAEUpgrades != null) {
            for (PortableItemStack upgrade : mAEUpgrades) {
                context.tryConsumeItems(upgrade.toStack());
            }
        }

        return true;
    }

    public ItemStack getEffectivePartStack() {
        if (isAttunable() && isP2P()) {
            return AEApi.instance()
                .definitions()
                .parts()
                .p2PTunnelME()
                .maybeStack(1)
                .get();
        } else {
            return mPart.toStack();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mPart == null) ? 0 : mPart.hashCode());
        result = prime * result + ((mSettingsName == null) ? 0 : mSettingsName.hashCode());
        result = prime * result + ((mSettings == null) ? 0 : mSettings.hashCode());
        result = prime * result + ((mCustomName == null) ? 0 : mCustomName.hashCode());
        result = prime * result + Arrays.hashCode(mAEUpgrades);
        result = prime * result + ((mConfig == null) ? 0 : mConfig.hashCode());
        result = prime * result + ((mOreDict == null) ? 0 : mOreDict.hashCode());
        result = prime * result + ((mP2POutput == null) ? 0 : mP2POutput.hashCode());
        result = prime * result + ((mP2PFreq == null) ? 0 : mP2PFreq.hashCode());
        result = prime * result + ((mPartClass == null) ? 0 : mPartClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AEPartData other = (AEPartData) obj;
        if (mPart == null) {
            if (other.mPart != null) return false;
        } else if (!mPart.equals(other.mPart)) return false;
        if (mSettingsName == null) {
            if (other.mSettingsName != null) return false;
        } else if (!mSettingsName.equals(other.mSettingsName)) return false;
        if (mSettings == null) {
            if (other.mSettings != null) return false;
        } else if (!mSettings.equals(other.mSettings)) return false;
        if (mCustomName == null) {
            if (other.mCustomName != null) return false;
        } else if (!mCustomName.equals(other.mCustomName)) return false;
        if (!Arrays.equals(mAEUpgrades, other.mAEUpgrades)) return false;
        if (mConfig == null) {
            if (other.mConfig != null) return false;
        } else if (!mConfig.equals(other.mConfig)) return false;
        if (mOreDict == null) {
            if (other.mOreDict != null) return false;
        } else if (!mOreDict.equals(other.mOreDict)) return false;
        if (mP2POutput == null) {
            if (other.mP2POutput != null) return false;
        } else if (!mP2POutput.equals(other.mP2POutput)) return false;
        if (mP2PFreq == null) {
            if (other.mP2PFreq != null) return false;
        } else if (!mP2PFreq.equals(other.mP2PFreq)) return false;
        if (mPartClass == null) {
            if (other.mPartClass != null) return false;
        } else if (!mPartClass.equals(other.mPartClass)) return false;
        return true;
    }
}
