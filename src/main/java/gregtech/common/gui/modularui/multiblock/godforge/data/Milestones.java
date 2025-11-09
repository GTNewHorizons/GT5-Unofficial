package gregtech.common.gui.modularui.multiblock.godforge.data;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;

import gregtech.api.modularui2.GTGuiTextures;

public enum Milestones {

    // spotless:off
    CHARGE(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CHARGE_GLOW,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RED,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RED_INVERTED,
        Alignment.TopLeft,
        "gt.blockmachines.multimachine.FOG.powermilestone",
        80, 100,
        Syncers.MILESTONE_CHARGE_PROGRESS,
        Syncers.MILESTONE_CHARGE_PROGRESS_INVERTED),
    CONVERSION(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CONVERSION_GLOW,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_PURPLE,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_PURPLE_INVERTED,
        Alignment.TopRight,
        "gt.blockmachines.multimachine.FOG.recipemilestone",
        70, 98,
        Syncers.MILESTONE_CONVERSION_PROGRESS,
        Syncers.MILESTONE_CONVERSION_PROGRESS_INVERTED),
    CATALYST(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_CATALYST_GLOW,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_BLUE,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_BLUE_INVERTED,
        Alignment.BottomLeft,
        "gt.blockmachines.multimachine.FOG.fuelmilestone",
        100, 100,
        Syncers.MILESTONE_CATALYST_PROGRESS,
        Syncers.MILESTONE_CATALYST_PROGRESS_INVERTED),
    COMPOSITION(
        GTGuiTextures.PICTURE_GODFORGE_MILESTONE_COMPOSITION_GLOW,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW,
        GTGuiTextures.PROGRESSBAR_GODFORGE_MILESTONE_RAINBOW_INVERTED,
        Alignment.BottomRight,
        "gt.blockmachines.multimachine.FOG.purchasablemilestone",
        100, 100,
        Syncers.MILESTONE_COMPOSITION_PROGRESS,
        Syncers.MILESTONE_COMPOSITION_PROGRESS_INVERTED),

    ;
    // spotless:on

    public static final Milestones[] VALUES = values();

    private final UITexture mainBackground;
    private final UITexture progressBarMainOverlay;
    private final UITexture progressBarInvertedOverlay;
    private final Alignment position;
    private final String titleLangKey;

    private final int width;
    private final int height;

    private final Syncers<DoubleSyncValue> progressSyncer;
    private final Syncers<DoubleSyncValue> progressInvertedSyncer;

    Milestones(UITexture mainBackground, UITexture progressBarMainOverlay, UITexture progressBarInvertedOverlay,
        Alignment position, String titleLangKey, int width, int height, Syncers<DoubleSyncValue> progressSyncer,
        Syncers<DoubleSyncValue> progressInvertedSyncer) {
        this.mainBackground = mainBackground;
        this.progressBarMainOverlay = progressBarMainOverlay;
        this.progressBarInvertedOverlay = progressBarInvertedOverlay;
        this.position = position;
        this.titleLangKey = titleLangKey;
        this.width = width;
        this.height = height;
        this.progressSyncer = progressSyncer;
        this.progressInvertedSyncer = progressInvertedSyncer;
    }

    public UITexture getMainBackground() {
        return mainBackground;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Syncers<DoubleSyncValue> getProgressSyncer() {
        return progressSyncer;
    }

    public Syncers<DoubleSyncValue> getProgressInvertedSyncer() {
        return progressInvertedSyncer;
    }
}
