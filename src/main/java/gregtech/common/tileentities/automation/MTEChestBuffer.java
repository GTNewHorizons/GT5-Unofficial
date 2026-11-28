package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_CHESTBUFFER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_CHESTBUFFER_GLOW;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBuffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.singleblock.MTEChestBufferGui;

@IMetaTileEntity.SkipGenerateDescription
@IMetaTileEntity.SkipGenerateName
public class MTEChestBuffer extends MTEBuffer {

    private static final int[] tickRate = { 400, 200, 100, 20, 4, 1, 1, 1, 1, 1, 1, 1, 1 };
    private static final int[] maxStacks = { 1, 1, 1, 1, 1, 1, 2, 4, 8, 16, 32, 64, 128 };

    public MTEChestBuffer(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 28, GTValues.emptyStringArray);
    }

    public MTEChestBuffer(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public MTEChestBuffer(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedFormatted(
            "gt.blockmachines.automation.buffer.desc",
            getMaxItemStacks(),
            StatCollector.translateToLocalFormatted(
                maxStacks[mTier] > 1 ? "gt.blockmachines.automation.buffer.rate.desc"
                    : "gt.blockmachines.automation.buffer.rate.single.desc",
                maxStacks[mTier],
                formatSeconds(getTickRate(mTier))));
    }

    @Override
    public String getLocalName() {
        if (!hasOwnLocalName()) return super.getLocalName();
        return StatCollector.translateToLocalFormatted(
            "gt.blockmachines.automation.chestbuffer.name",
            GTValues.getLocalizedLongVoltageName(mTier),
            GTValues.VN[mTier]);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEChestBuffer(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_CHESTBUFFER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_CHESTBUFFER_GLOW)
                .glow()
                .build());
    }

    @Override
    protected void moveItems(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aTimer % tickRate[mTier] > 0) return;

        // mSuccess will be negative if the call is caused by the %200 aTimer, always try to push. Otherwise it will be
        // positive.
        // For the first 6 ticks after a successful move (49->44), push every tick. Then go to every 5 ticks.
        if ((mSuccess <= 0) || (mSuccess > 43) || ((mSuccess % 5) == 0)) {
            super.moveItems(aBaseMetaTileEntity, aTimer, Math.min(MAX, maxStacks[mTier]));
        }

        if (mSuccess < 0) {
            mSuccess = 0;
        }
    }

    /**
     * Item stacks this buffer holds, one slot being reserved for the output stack size setting.
     */
    protected int getMaxItemStacks() {
        return mInventory.length - 1;
    }

    /**
     * Formats a tick rate as seconds, e.g. 400 ticks as 20 and 1 tick as 0.05.
     */
    protected static String formatSeconds(int tickRate) {
        final double seconds = tickRate / 20.0D;
        return seconds >= 1.0D ? String.valueOf((int) seconds) : String.valueOf(seconds);
    }

    protected static int getTickRate(int tier) {
        if (tier > 9) return 1;
        return tickRate[tier];
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEChestBufferGui(this).build(guiData, syncManager, uiSettings);
    }
}
