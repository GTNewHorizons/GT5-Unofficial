package gregtech.common.items.matterManipulator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.gson.JsonElement;
import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.api.AEApi;
import appeng.api.implementations.items.IUpgradeModule;
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

    public PortableItemStack[] mAEUpgrades = null, mConfig = null;
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
            mSettings = settings.hasNoTags() ? null : NBTState.toJsonObject(settings);
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
                mConfig = GTUtility.streamInventory(config)
                    .filter(x -> x != null)
                    .map(PortableItemStack::withoutStackSize)
                    .distinct()
                    .toArray(PortableItemStack[]::new);
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
                : (NBTTagCompound) NBTState.toNbt(mSettings);
            configurable.getConfigManager()
                .readFromNBT(settings);
        }

        if (part instanceof ISegmentedInventory segmentedInventory) {
            if (segmentedInventory.getInventoryByName("upgrades") instanceof UpgradeInventory upgradeInv) {
                ItemStackMap<Long> targetMap = GTUtility.getItemStackHistogram(
                    Arrays.stream(mAEUpgrades)
                        .map(PortableItemStack::toStack)
                        .toArray(ItemStack[]::new));
                ItemStackMap<Long> actualMap = GTUtility.getItemStackHistogram(
                    GTUtility.streamInventory(upgradeInv)
                        .filter(x -> x != null)
                        .toArray(ItemStack[]::new));

                if (!targetMap.equals(actualMap)) {
                    emptyInventory(context, upgradeInv);

                    targetMap.replaceAll((item, amount) -> {
                        if (item.getItem() instanceof IUpgradeModule upgrade) {
                            int max = upgradeInv.getMaxInstalled(upgrade.getType(item));

                            return Math.min(max, amount);
                        } else {
                            return 0l;
                        }
                    });

                    List<ItemStack> upgradeList = GTUtility.getStacksOfSize(targetMap, 1);

                    ItemStack[] upgrades = upgradeList
                        .subList(0, Math.min(upgradeList.size(), upgradeInv.getSizeInventory()))
                        .toArray(new ItemStack[0]);

                    if (context.tryConsumeItems(upgrades)) {
                        for (int i = 0; i < upgrades.length; i++) {
                            upgradeInv.setInventorySlotContents(i, upgrades[i]);
                        }
                    }
                }
            }

            IInventory config = segmentedInventory.getInventoryByName("config");

            if (config != null) {
                for (int i = 0; i < config.getSizeInventory(); i++) {
                    config.setInventorySlotContents(i, null);
                }

                if (mConfig != null) {
                    int n = Math.min(config.getSizeInventory(), mConfig.length);
                    for (int i = 0; i < n; i++) {
                        config.setInventorySlotContents(i, mConfig[i] == null ? null : mConfig[i].toStack());
                    }
                }

                config.markDirty();
            }
        }

        if (part instanceof IOreFilterable filterable) {
            filterable.setFilter(mOreDict == null ? "" : mOreDict);
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
        result = prime * result + Arrays.hashCode(mAEUpgrades);
        result = prime * result + Arrays.hashCode(mConfig);
        result = prime * result + ((mOreDict == null) ? 0 : mOreDict.hashCode());
        result = prime * result + ((mCustomName == null) ? 0 : mCustomName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
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
        if (!Arrays.equals(mAEUpgrades, other.mAEUpgrades)) return false;
        if (!Arrays.equals(mConfig, other.mConfig)) return false;
        if (mOreDict == null) {
            if (other.mOreDict != null) return false;
        } else if (!mOreDict.equals(other.mOreDict)) return false;
        if (mCustomName == null) {
            if (other.mCustomName != null) return false;
        } else if (!mCustomName.equals(other.mCustomName)) return false;
        return true;
    }

    static void emptyInventory(IBlockApplyContext context, IInventory inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                inv.setInventorySlotContents(i, null);

                context.givePlayerItems(stack);
            }
        }

        inv.markDirty();
    }
}
