package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_TYPEFILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_TYPEFILTER_GLOW;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTESpecialFilter;
import gregtech.api.objects.ItemData;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class MTETypeFilter extends MTESpecialFilter {

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

    public MTETypeFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Filters 1 Item Type", "Use Screwdriver to regulate output stack size" });
    }

    public MTETypeFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETypeFilter(
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
        ItemData data = GTOreDictUnificator.getAssociation(handStack);
        if (data != null && data.hasValidPrefixData()) {
            this.mPrefix = data.mPrefix;
            this.mRotationIndex = -1;
        }
    }

    private void cyclePrefix(boolean rightClick) {
        mRotationIndex = -1;

        final int start = IntStream.range(0, OrePrefixes.VALUES.length)
            .filter(i -> mPrefix == OrePrefixes.VALUES[i])
            .findFirst()
            .orElse(0);

        // spotless:off
        mPrefix = IntStream.range(1, OrePrefixes.VALUES.length)
            .map(offset -> start + (rightClick ? -offset : offset))                        // search up/down from start
            .map(index -> (index + OrePrefixes.VALUES.length) % OrePrefixes.VALUES.length) // wrap around
            .mapToObj(index -> OrePrefixes.VALUES[index])                                  // map to prefix
            .filter(prefix -> !prefix.mPrefixedItems.isEmpty())                            // only prefixes with items
            .findFirst()
            .orElse(mPrefix);                                                              // fallback to current prefix
        // spotless:on
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((!getBaseMetaTileEntity().isServerSide()) || ((aTick % 8L != 0L) && mRotationIndex != -1)) return;
        if (this.mPrefix.mPrefixedItems.isEmpty()) {
            this.mInventory[FILTER_SLOT_INDEX] = null;
            return;
        }
        this.mInventory[FILTER_SLOT_INDEX] = GTUtility.copyAmount(
            1,
            this.mPrefix.mPrefixedItems
                .get(this.mRotationIndex = (this.mRotationIndex + 1) % this.mPrefix.mPrefixedItems.size()));
        if (this.mInventory[FILTER_SLOT_INDEX] == null) return;
        if (this.mInventory[FILTER_SLOT_INDEX].getItemDamage() == WILDCARD) this.mInventory[9].setItemDamage(0);
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
            ItemData data = GTOreDictUnificator.getItemData(aStack);
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
            replacementTooltip.add(
                StatCollector
                    .translateToLocalFormatted("GT5U.tooltip.typefilter.set_to", mPrefix.getDefaultLocalName()));
            replacementTooltip.add(
                StatCollector.translateToLocalFormatted("GT5U.tooltip.typefilter.ore_prefix", "§e" + mPrefix + "§r"));
            replacementTooltip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.tooltip.typefilter.size",
                    "§e" + mPrefix.mPrefixedItems.size() + "§r"));
            replacementTooltip.addAll(mTooltipCache.getData(REPRESENTATION_SLOT_TOOLTIP).text);
            return replacementTooltip;
        };
    }

    @Override
    protected SlotWidget createFilterIconSlot(BaseSlot slot) {
        return new TypeFilterIconSlotWidget(slot);
    }

    private class TypeFilterIconSlotWidget extends FilterIconSlotWidget {

        public TypeFilterIconSlotWidget(BaseSlot slot) {
            super(slot);
        }

        @Override
        protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
            clickTypeIcon(clickData.mouseButton != 0, cursorStack);
        }
    }
}
