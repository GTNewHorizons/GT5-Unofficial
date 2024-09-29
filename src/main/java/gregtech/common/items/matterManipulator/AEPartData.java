package gregtech.common.items.matterManipulator;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonElement;
import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

import appeng.api.implementations.items.IMemoryCard;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.implementations.items.MemoryCardMessages;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.helpers.IOreFilterable;
import appeng.parts.AEBasePart;
import appeng.parts.automation.UpgradeInventory;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public class AEPartData {

    public PortableItemStack mPart;
    public String mSettingsName = null;
    public JsonElement mSettings = null, mMemoryCardData = null;
    public PortableItemStack[] mAEUpgrades = null, mConfig = null;
    public String mOreDict = null;
    public String mCustomName = null;

    public transient ItemStack partStack;

    public AEPartData(EntityPlayer fakePlayer, AEBasePart part) {
        mPart = new PortableItemStack(part.getItemStack(PartItemStack.Break));

        mCustomName = part.hasCustomName() ? part.getCustomName() : null;

        if (part instanceof IOreFilterable filterable) {
            mOreDict = filterable.getFilter();

            if ("".equals(mOreDict)) mOreDict = null;
        }

        fakePlayer.inventory.mainInventory[0] = new ItemStack(new FakeMemoryCard());

        part.onShiftActivate(fakePlayer, Vec3.createVectorHelper(0, 0, 0));

        fakePlayer.inventory.mainInventory[0] = null;

        IInventory upgrades = part.getInventoryByName("upgrades");

        if (upgrades != null) {
            mAEUpgrades = GTUtility.streamInventory(upgrades)
                .filter(x -> x != null)
                .map(PortableItemStack::new)
                .toArray(PortableItemStack[]::new);
        }

        IInventory config = part.getInventoryByName("config");

        if (config != null) {
            mConfig = GTUtility.streamInventory(config)
                .filter(x -> x != null)
                .map(PortableItemStack::withoutStackSize)
                .distinct()
                .toArray(PortableItemStack[]::new);
        }
    }

    public boolean updatePart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side) {
        if (partHost.getPart(side) instanceof AEBasePart part) {
            if (mCustomName != null) part.setCustomName(mCustomName);

            EntityPlayer fakePlayer = context.getFakePlayer().get();

            fakePlayer.inventory.mainInventory[0] = new ItemStack(new FakeMemoryCard());

            part.onActivate(fakePlayer, Vec3.createVectorHelper(0, 0, 0));

            fakePlayer.inventory.mainInventory[0] = null;
            
            UpgradeInventory upgradeInv = (UpgradeInventory) part.getInventoryByName("upgrades");

            if (upgradeInv != null) {
                ItemStackMap<Long> targetMap = GTUtility.getItemStackHistogram(
                    Arrays.stream(mAEUpgrades)
                        .map(PortableItemStack::toStack)
                        .toArray(ItemStack[]::new));
                ItemStackMap<Long> actualMap = GTUtility.getItemStackHistogram(
                    GTUtility.streamInventory(upgradeInv)
                        .filter(x -> x != null)
                        .toArray(ItemStack[]::new));

                if (!targetMap.equals(actualMap)) {
                    BlockAnalyzer.emptyInventory(context, upgradeInv);

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

            IInventory config = part.getInventoryByName("config");

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

            if (part instanceof IOreFilterable filterable) {
                filterable.setFilter(mOreDict == null ? "" : mOreDict);
            }
        }

        return true;
    }

    public ItemStack getPartStack() {
        return mPart.toStack();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mPart == null) ? 0 : mPart.hashCode());
        result = prime * result + ((mSettingsName == null) ? 0 : mSettingsName.hashCode());
        result = prime * result + ((mSettings == null) ? 0 : mSettings.hashCode());
        result = prime * result + ((mMemoryCardData == null) ? 0 : mMemoryCardData.hashCode());
        result = prime * result + Arrays.hashCode(mAEUpgrades);
        result = prime * result + Arrays.hashCode(mConfig);
        result = prime * result + ((mOreDict == null) ? 0 : mOreDict.hashCode());
        result = prime * result + ((mCustomName == null) ? 0 : mCustomName.hashCode());
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
            if (other.mPart != null)
                return false;
        } else if (!mPart.equals(other.mPart))
            return false;
        if (mSettingsName == null) {
            if (other.mSettingsName != null)
                return false;
        } else if (!mSettingsName.equals(other.mSettingsName))
            return false;
        if (mSettings == null) {
            if (other.mSettings != null)
                return false;
        } else if (!mSettings.equals(other.mSettings))
            return false;
        if (mMemoryCardData == null) {
            if (other.mMemoryCardData != null)
                return false;
        } else if (!mMemoryCardData.equals(other.mMemoryCardData))
            return false;
        if (!Arrays.equals(mAEUpgrades, other.mAEUpgrades))
            return false;
        if (!Arrays.equals(mConfig, other.mConfig))
            return false;
        if (mOreDict == null) {
            if (other.mOreDict != null)
                return false;
        } else if (!mOreDict.equals(other.mOreDict))
            return false;
        if (mCustomName == null) {
            if (other.mCustomName != null)
                return false;
        } else if (!mCustomName.equals(other.mCustomName))
            return false;
        return true;
    }

    private class FakeMemoryCard extends Item implements IMemoryCard {
        @Override
        public void setMemoryCardContents(ItemStack is, String SettingsName, NBTTagCompound data) {
            AEPartData.this.mSettingsName = SettingsName;
            AEPartData.this.mMemoryCardData = NBTState.toJsonObject(data);
        }

        @Override
        public String getSettingsName(ItemStack is) {
            return AEPartData.this.mSettingsName;
        }

        @Override
        public NBTTagCompound getData(ItemStack is) {
            return (NBTTagCompound) NBTState.toNbt(AEPartData.this.mMemoryCardData);
        }

        @Override
        public void notifyUser(EntityPlayer player, MemoryCardMessages msg) {
            
        }
    }
}