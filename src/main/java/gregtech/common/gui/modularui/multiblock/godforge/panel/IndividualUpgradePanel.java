package gregtech.common.gui.modularui.multiblock.godforge.panel;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.Expandable;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.data.UpgradeColor;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import gregtech.common.gui.modularui.widget.InterpolatedUITexture;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;

public class IndividualUpgradePanel {

    private static final int SIZE_LARGE = 300;
    private static final int SIZE_SMALL = 250;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.INDIVIDUAL_UPGRADE);

        registerSyncValues(hypervisor);

        panel.coverChildren()
            .background(IDrawable.EMPTY)
            .disableHoverBackground();

        // registered on the Upgrade Tree panel, look up from there
        EnumSyncValue<ForgeOfGodsUpgrade> upgradeSyncer = SyncValues.UPGRADE_CLICKED
            .lookupFrom(Panels.UPGRADE_TREE, hypervisor);

        // todo testing
        InterpolatedUITexture background = new InterpolatedUITexture(
            UpgradeColor.BLUE.getBackground(),
            UpgradeColor.RED.getBackground()).fadeOver();
        InterpolatedUITexture image = new InterpolatedUITexture(
            UpgradeColor.BLUE.getOverlay(),
            UpgradeColor.RED.getOverlay()).fadeOutThenIn();

        // todo figure out how to make the panel not "move" and resize as if its from the center
        Expandable resizer = new Expandable() {

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                // To make the panel resize on a specific action rather
                // than clicking anywhere in the expandable widget's area
                return Result.ACCEPT;
            }

            @Override
            public Expandable expanded(boolean expanded) {
                if (expanded != this.isExpanded()) {
                    background.toggle(100);
                    image.toggle(100);
                }
                return super.expanded(expanded);
            }
        }.name(Panels.INDIVIDUAL_UPGRADE.getExpandableId())
            .background(background)
            .collapsedView(
                new ParentWidget<>().size(SIZE_SMALL)
                    .child(
                        image.asWidget()
                            .size(50)
                            .align(Alignment.CENTER)))
            .expandedView(
                new ParentWidget<>().size(SIZE_LARGE)
                    .child(
                        image.asWidget()
                            .size(50)
                            .align(Alignment.CENTER)))
            // start with the initial sync value, afterward it will be dynamically resized
            .expanded(
                upgradeSyncer.getValue()
                    .isLargePanel())
            .animationDuration(500)
            .stencilTransform((r, expanded) -> {
                r.width = Math.max(20, r.width - 5);
                r.height = Math.max(20, r.height - 5);
            });
        panel.child(resizer);

        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.AVAILABLE_GRAVITON_SHARDS.registerFor(Panels.INDIVIDUAL_UPGRADE, hypervisor);
    }

    private static ParentWidget<?> buildSmallPanel(EnumSyncValue<ForgeOfGodsUpgrade> upgradeSyncer,
        SyncHypervisor hypervisor) {
        ParentWidget<?> parent = new ParentWidget<>().size(SIZE_SMALL);
        return parent;
    }

    private static ParentWidget<?> buildLargePanel(EnumSyncValue<ForgeOfGodsUpgrade> upgradeSyncer,
        SyncHypervisor hypervisor) {
        ParentWidget<?> parent = new ParentWidget<>().size(SIZE_LARGE);
        return parent;
    }
}
