package gregtech.common.tileentities.automation;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SpecialFilter;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_TYPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_TYPEFILTER_GLOW;

public class GT_MetaTileEntity_TypeFilter extends GT_MetaTileEntity_SpecialFilter {
    public int mRotationIndex = 0;
    public OrePrefixes mPrefix = OrePrefixes.ore;

    public GT_MetaTileEntity_TypeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, new String[]{
                "Filters 1 Item Type",
                "Use Screwdriver to regulate output stack size",
                "Does not consume energy to move Item"});
    }

    public GT_MetaTileEntity_TypeFilter(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_TypeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TypeFilter(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
                TextureFactory.of(AUTOMATION_TYPEFILTER),
                TextureFactory.builder().addIcon(AUTOMATION_TYPEFILTER_GLOW).glow().build());
    }

    public void clickTypeIcon(boolean aRightClick, ItemStack aHandStack) {
        if (getBaseMetaTileEntity().isServerSide()) {
            ItemData data = GT_OreDictUnificator.getAssociation(aHandStack);
            if (data != null && data.hasValidPrefixData()) {
                this.mPrefix = data.mPrefix;
                this.mRotationIndex = -1;
                return;
            }
            for (int i = 0; i < OrePrefixes.values().length; i++) {
                if (this.mPrefix == OrePrefixes.values()[i]) {
                    for (this.mPrefix = null; this.mPrefix == null; this.mPrefix = OrePrefixes.values()[i]) {
                        if (aRightClick) {
                            do {
                                i--;
                                if (i < 0) {
                                    i = OrePrefixes.values().length - 1;
                                }
                            } while (OrePrefixes.values()[i].mPrefixedItems.isEmpty());
                        } else {
                            do {
                                i++;
                                if (i >= OrePrefixes.values().length) {
                                    i = 0;
                                }
                            } while (OrePrefixes.values()[i].mPrefixedItems.isEmpty());
                        }
                        if (!OrePrefixes.values()[i].mPrefixedItems.isEmpty() && OrePrefixes.values()[i].mPrefixInto == OrePrefixes.values()[i])
                            mPrefix = OrePrefixes.values()[i];
                    }
                }
            }
            this.mRotationIndex = -1;
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((!getBaseMetaTileEntity().isServerSide()) || ((aTick % 8L != 0L) && mRotationIndex != -1)) return;
        if (this.mPrefix.mPrefixedItems.isEmpty()) {
            this.mInventory[SPECIAL_SLOT_INDEX] = null;
            return;
        }
        this.mInventory[SPECIAL_SLOT_INDEX] = GT_Utility.copyAmount(1L, this.mPrefix.mPrefixedItems.get(this.mRotationIndex = (this.mRotationIndex + 1) % this.mPrefix.mPrefixedItems.size()));
        if (this.mInventory[SPECIAL_SLOT_INDEX] == null) return;
        if (this.mInventory[SPECIAL_SLOT_INDEX].getItemDamage() == W) this.mInventory[9].setItemDamage(0);
        this.mInventory[SPECIAL_SLOT_INDEX].setStackDisplayName(this.mPrefix.toString());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("mPrefix", this.mPrefix.toString());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mPrefix = OrePrefixes.getPrefix(aNBT.getString("mPrefix"), this.mPrefix);
    }

    @Override
    protected boolean isStackAllowed(ItemStack aStack) {
        boolean tAllowPrefix = this.mPrefix.contains(aStack);
        if (this.mPrefix == OrePrefixes.ore) {
            ItemData tData = GT_OreDictUnificator.getItemData(aStack);
            if (tData != null && tData.mPrefix != null) {
                OrePrefixes tFix = tData.mPrefix;
                if (tFix == OrePrefixes.oreBlackgranite ||
                        tFix == OrePrefixes.oreDense ||
                        tFix == OrePrefixes.oreEnd ||
                        tFix == OrePrefixes.oreEndstone ||
                        tFix == OrePrefixes.oreNether ||
                        tFix == OrePrefixes.oreNetherrack ||
                        tFix == OrePrefixes.oreNormal ||
                        tFix == OrePrefixes.orePoor ||
                        tFix == OrePrefixes.oreRedgranite ||
                        tFix == OrePrefixes.oreRich ||
                        tFix == OrePrefixes.oreSmall ||
                        tFix == OrePrefixes.oreBasalt ||
                        tFix == OrePrefixes.oreMarble) tAllowPrefix = true;
            }
        }
        return tAllowPrefix;
    }
}
