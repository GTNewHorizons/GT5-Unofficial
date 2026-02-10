package gregtech.common.gui.modularui.multiblock.godforge.data;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValue.ForgeOfGodsSyncValue;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public enum Milestones {

    // spotless:off
    CHARGE(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW,
        80, 100,
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CHARGE,
        60, 75,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RED,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RED_INVERTED,
        Alignment.TopLeft,
        "gt.blockmachines.multimachine.FOG.powermilestone",
        "gt.blockmachines.multimachine.FOG.power",
        SyncValues.TOTAL_POWER_CONSUMED,
        SyncValues.MILESTONE_CHARGE_LEVEL,
        SyncValues.MILESTONE_CHARGE_PROGRESS,
        SyncValues.MILESTONE_CHARGE_PROGRESS_INVERTED),
    CONVERSION(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CONVERSION_GLOW,
        70, 98,
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CONVERSION,
        54, 75,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_PURPLE,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_PURPLE_INVERTED,
        Alignment.TopRight,
        "gt.blockmachines.multimachine.FOG.recipemilestone",
        "gt.blockmachines.multimachine.FOG.recipes",
        SyncValues.TOTAL_RECIPES_PROCESSED,
        SyncValues.MILESTONE_CONVERSION_LEVEL,
        SyncValues.MILESTONE_CONVERSION_PROGRESS,
        SyncValues.MILESTONE_CONVERSION_PROGRESS_INVERTED),
    CATALYST(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CATALYST_GLOW,
        100, 100,
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CATALYST,
        75, 75,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_BLUE,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_BLUE_INVERTED,
        Alignment.BottomLeft,
        "gt.blockmachines.multimachine.FOG.fuelmilestone",
        "gt.blockmachines.multimachine.FOG.fuelconsumed",
        SyncValues.TOTAL_FUEL_CONSUMED,
        SyncValues.MILESTONE_CATALYST_LEVEL,
        SyncValues.MILESTONE_CATALYST_PROGRESS,
        SyncValues.MILESTONE_CATALYST_PROGRESS_INVERTED),
    COMPOSITION(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION_GLOW,
        100, 100,
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION,
        75, 75,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW_INVERTED,
        Alignment.BottomRight,
        "gt.blockmachines.multimachine.FOG.purchasablemilestone",
        "gt.blockmachines.multimachine.FOG.extensions",
        SyncValues.MILESTONE_COMPOSITION_LEVEL,
        SyncValues.MILESTONE_COMPOSITION_LEVEL,
        SyncValues.MILESTONE_COMPOSITION_PROGRESS,
        SyncValues.MILESTONE_COMPOSITION_PROGRESS_INVERTED),

    ;
    // spotless:on

    public static final Milestones[] VALUES = values();

    private final UITexture mainBackground;
    private final int mainWidth;
    private final int mainHeight;

    private final UITexture symbolBackground;
    private final int symbolWidth;
    private final int symbolHeight;

    private final UITexture progressBarMainOverlay;
    private final UITexture progressBarInvertedOverlay;
    private final Alignment position;

    private final String titleLangKey;
    private final String progressLangKey;

    private final ForgeOfGodsSyncValue<? extends ValueSyncHandler<? extends Number>> totalSyncer;
    private final ForgeOfGodsSyncValue<IntSyncValue> levelSyncer;
    private final ForgeOfGodsSyncValue<FloatSyncValue> progressSyncer;
    private final ForgeOfGodsSyncValue<FloatSyncValue> progressInvertedSyncer;

    Milestones(UITexture mainBackground, int mainWidth, int mainHeight, UITexture symbolBackground, int symbolWidth,
        int symbolHeight, UITexture progressBarMainOverlay, UITexture progressBarInvertedOverlay, Alignment position,
        String titleLangKey, String progressLangKey,
        ForgeOfGodsSyncValue<? extends ValueSyncHandler<? extends Number>> totalSyncer,
        ForgeOfGodsSyncValue<IntSyncValue> levelSyncer, ForgeOfGodsSyncValue<FloatSyncValue> progressSyncer,
        ForgeOfGodsSyncValue<FloatSyncValue> progressInvertedSyncer) {
        this.mainBackground = mainBackground;
        this.mainWidth = mainWidth;
        this.mainHeight = mainHeight;
        this.symbolBackground = symbolBackground;
        this.symbolWidth = symbolWidth;
        this.symbolHeight = symbolHeight;
        this.progressBarMainOverlay = progressBarMainOverlay;
        this.progressBarInvertedOverlay = progressBarInvertedOverlay;
        this.position = position;
        this.titleLangKey = titleLangKey;
        this.progressLangKey = progressLangKey;
        this.totalSyncer = totalSyncer;
        this.levelSyncer = levelSyncer;
        this.progressSyncer = progressSyncer;
        this.progressInvertedSyncer = progressInvertedSyncer;
    }

    public UITexture getMainBackground() {
        return mainBackground;
    }

    public int getMainWidth() {
        return mainWidth;
    }

    public int getMainHeight() {
        return mainHeight;
    }

    public UITexture getSymbolBackground() {
        return symbolBackground;
    }

    public int getSymbolWidth() {
        return symbolWidth;
    }

    public int getSymbolHeight() {
        return symbolHeight;
    }

    public float getSymbolWidthRatio() {
        return 1.0f * symbolWidth / symbolHeight;
    }

    public UITexture getProgressBarMainOverlay() {
        return progressBarMainOverlay;
    }

    public UITexture getProgressBarInvertedOverlay() {
        return progressBarInvertedOverlay;
    }

    public Alignment getPosition() {
        return position;
    }

    public String getTitleLangKey() {
        return titleLangKey;
    }

    public String getProgressLangKey() {
        return progressLangKey;
    }

    public ForgeOfGodsSyncValue<? extends ValueSyncHandler<? extends Number>> getTotalSyncer() {
        return totalSyncer;
    }

    public ForgeOfGodsSyncValue<IntSyncValue> getLevelSyncer() {
        return levelSyncer;
    }

    public ForgeOfGodsSyncValue<FloatSyncValue> getProgressSyncer() {
        return progressSyncer;
    }

    public ForgeOfGodsSyncValue<FloatSyncValue> getProgressInvertedSyncer() {
        return progressInvertedSyncer;
    }
}
