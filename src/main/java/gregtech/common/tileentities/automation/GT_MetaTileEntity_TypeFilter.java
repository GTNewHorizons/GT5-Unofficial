package gregtech.common.tileentities.automation;

import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_TYPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_TYPEFILTER_GLOW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SpecialFilter;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_TypeFilter extends GT_MetaTileEntity_SpecialFilter {

    private static final String REPRESENTATION_SLOT_TOOLTIP = "GT5U.type_filter.representation_slot.tooltip";
    public int mRotationIndex = 0;
    public OrePrefixes mPrefix = OrePrefixes.ore;

    public static ImmutableList<OrePrefixes> OREBLOCK_PREFIXES = ImmutableList.of(
        OrePrefixes.oreBlackgranite,
        OrePrefixes.oreDense,
        OrePrefixes.oreEnd,
        OrePrefixes.oreEndstone,
        OrePrefixes.oreNether,
        OrePrefixes.oreNetherrack,
        OrePrefixes.oreNormal,
        OrePrefixes.orePoor,
        OrePrefixes.oreRedgranite,
        OrePrefixes.oreRich,
        OrePrefixes.oreSmall,
        OrePrefixes.oreBasalt,
        OrePrefixes.oreMarble);

    public GT_MetaTileEntity_TypeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Filters 1 Item Type", "Use Screwdriver to regulate output stack size" });
    }

    public GT_MetaTileEntity_TypeFilter(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_TypeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TypeFilter(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_TYPEFILTER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_TYPEFILTER_GLOW)
                .glow()
                .build());
    }

    @Override
    public void clickTypeIcon(boolean aRightClick, ItemStack aHandStack) {
        if (getBaseMetaTileEntity().isServerSide()) {
            if (aHandStack != null) {
                copyHeldItemPrefix(aHandStack);
            } else {
                cyclePrefix(aRightClick);
            }
        }
    }

    private void copyHeldItemPrefix(ItemStack handStack) {
        ItemData data = GT_OreDictUnificator.getAssociation(handStack);
        if (data != null && data.hasValidPrefixData()) {
            this.mPrefix = data.mPrefix;
            this.mRotationIndex = -1;
        }
    }

    private void cyclePrefix(boolean aRightClick) {
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
                    if (!OrePrefixes.values()[i].mPrefixedItems.isEmpty()
                        && OrePrefixes.values()[i].mPrefixInto == OrePrefixes.values()[i])
                        mPrefix = OrePrefixes.values()[i];
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
            this.mInventory[FILTER_SLOT_INDEX] = null;
            return;
        }
        this.mInventory[FILTER_SLOT_INDEX] = GT_Utility.copyAmount(
            1L,
            this.mPrefix.mPrefixedItems
                .get(this.mRotationIndex = (this.mRotationIndex + 1) % this.mPrefix.mPrefixedItems.size()));
        if (this.mInventory[FILTER_SLOT_INDEX] == null) return;
        if (this.mInventory[FILTER_SLOT_INDEX].getItemDamage() == W) this.mInventory[9].setItemDamage(0);
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
        if (this.mPrefix == OrePrefixes.ore) {
            ItemData data = GT_OreDictUnificator.getItemData(aStack);
            if (data != null && data.mPrefix != null && OREBLOCK_PREFIXES.contains(data.mPrefix)) {
                return true;
            }
        }
        return this.mPrefix.contains(aStack);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new FakeSyncWidget.StringSyncer(
                () -> this.mPrefix.toString(),
                (prefix) -> this.mPrefix = OrePrefixes.getPrefix(prefix, this.mPrefix)));
    }

    @Override
    protected Function<List<String>, List<String>> getItemStackReplacementTooltip() {
        return (itemTooltip) -> {
            List<String> replacementTooltip = new ArrayList<>();
            replacementTooltip.add("Filter set to " + mPrefix.mRegularLocalName);
            replacementTooltip.add("Ore prefix: §e" + mPrefix + "§r");
            replacementTooltip.add("Filter size: §e" + mPrefix.mPrefixedItems.size() + "§r");
            replacementTooltip.addAll(mTooltipCache.getData(REPRESENTATION_SLOT_TOOLTIP).text);
            return replacementTooltip;
        };
    }
}
