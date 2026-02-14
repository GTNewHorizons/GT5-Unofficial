package gregtech.common.gui.modularui.widget.radialmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import org.apache.commons.lang3.mutable.MutableInt;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenu.RadialMenuClickHandler;
import gregtech.common.gui.modularui.widget.radialmenu.RadialMenu.RadialMenuOption;

/**
 * A builder that significantly helps with making radial menus.
 */
public class RadialMenuBuilder implements BranchableRadialMenu {

    private final String syncActionPrefix;
    private final PanelSyncManager syncManager;

    private final MutableInt ids = new MutableInt(0);

    public final List<RadialMenuOptionBuilder<RadialMenuBuilder>> options = new ArrayList<>();
    public float innerRadius = 0.25f, outerRadius = 0.60f;
    public IIcon innerIcon;
    public RadialMenuTheme theme = RadialMenu.DEFAULT_THEME;

    public RadialMenuBuilder(String syncActionPrefix, PanelSyncManager syncManager) {
        this.syncActionPrefix = syncActionPrefix;
        this.syncManager = syncManager;
    }

    /** Sets the inner icon */
    public RadialMenuBuilder innerIcon(IDrawable icon) {
        this.innerIcon = icon.asIcon()
            .marginLeft(-16)
            .marginRight(-16)
            .size(32, 32);
        return this;
    }

    /** Sets the inner icon */
    public RadialMenuBuilder innerIcon(ItemStack item) {
        innerIcon(new ItemDrawable(item));
        return this;
    }

    /** Sets the inner radius */
    public RadialMenuBuilder innerRadius(float radius) {
        this.innerRadius = radius;
        return this;
    }

    /** Sets the outer radius */
    public RadialMenuBuilder outerRadius(float radius) {
        this.outerRadius = radius;
        return this;
    }

    public RadialMenuBuilder theme(RadialMenuTheme theme) {
        this.theme = theme;
        return this;
    }

    /** Adds an externally-built option. */
    public RadialMenuBuilder option(RadialMenuOptionBuilder<RadialMenuBuilder> option) {
        options.add(option);
        return this;
    }

    /**
     * Adds a new option to this builder.
     * Call {@link RadialMenuOptionBuilderLeaf#done()} once done to finish adding the option.
     */
    @Override
    public RadialMenuOptionBuilderLeaf<RadialMenuBuilder> option() {
        var leaf = new RadialMenuOptionBuilderLeaf<>(syncManager::getPlayer, this, ids);
        options.add(leaf);
        return leaf;
    }

    /**
     * Adds a new sub-menu to this builder.
     * Call {@link RadialMenuOptionBuilderBranch#done()} once done to finish adding the sub menu.
     */
    @Override
    public RadialMenuOptionBuilderBranch<RadialMenuBuilder> branch() {
        var branch = new RadialMenuOptionBuilderBranch<>(syncManager::getPlayer, this, ids);
        options.add(branch);
        return branch;
    }

    /**
     * Lets you apply a custom function without needing a local variable.
     */
    public RadialMenuBuilder pipe(Consumer<RadialMenuBuilder> fn) {
        fn.accept(this);
        return this;
    }

    public RadialMenu build() {
        RadialMenu menu = new RadialMenu();

        menu.innerIcon = this.innerIcon;
        menu.innerRadius = this.innerRadius;
        menu.outerRadius = this.outerRadius;
        menu.theme = this.theme;

        for (var option : options) {
            option.registerActions(menu, syncManager, syncActionPrefix + ":");
            option.apply(menu, syncManager, syncActionPrefix + ":");
        }

        return menu;
    }

    /**
     * The base data for branch and leaf builders.
     */
    public static abstract class RadialMenuOptionBuilder<Parent> {

        public final Supplier<EntityPlayer> player;
        public final Parent parent;

        public IIcon label;
        public double weight = 1;
        public BooleanSupplier hidden = () -> false;

        public RadialMenuOptionBuilder(Supplier<EntityPlayer> player, Parent parent) {
            this.player = player;
            this.parent = parent;
        }

        public abstract void registerActions(RadialMenu menu, PanelSyncManager syncManager, String baseName);

        public abstract void apply(RadialMenu menu, PanelSyncManager syncManager, String baseName);

        /**
         * Finishes constructing this option/sub menu and returns to the parent builder.
         */
        public Parent done() {
            return parent;
        }
    }

    /**
     * A builder for radial menu options.
     */
    public static class RadialMenuOptionBuilderLeaf<Parent> extends RadialMenuOptionBuilder<Parent> {

        public RadialMenuClickHandler onClicked;
        public String actionId;

        public RadialMenuOptionBuilderLeaf(Supplier<EntityPlayer> player, Parent parent, MutableInt ids) {
            super(player, parent);
            ids.add(1);
            this.actionId = ids.toString();
        }

        public RadialMenuOptionBuilderLeaf<Parent> label(IIcon icon) {
            this.label = icon;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> label(IKey label) {
            this.label = label.asIcon()
                .width(60);
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> weight(double weight) {
            this.weight = weight;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> hidden(boolean hidden) {
            this.hidden = () -> hidden;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> hidden(BooleanSupplier hidden) {
            this.hidden = hidden;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> actionId(String actionId) {
            this.actionId = actionId;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(RadialMenuClickHandler onClicked) {
            this.onClicked = onClicked;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(Runnable onClicked) {
            this.onClicked = (menu, option, mouseButton, side) -> {
                onClicked.run();
                player.get()
                    .closeScreen();
            };
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(BooleanSupplier onClicked) {
            this.onClicked = (menu, option, mouseButton, side) -> {
                if (onClicked.getAsBoolean()) {
                    player.get()
                        .closeScreen();
                }
            };
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(int buttonId, Runnable onClicked) {
            this.onClicked = (menu, option, mouseButton, side) -> {
                if (mouseButton == buttonId) {
                    onClicked.run();
                    player.get()
                        .closeScreen();
                }
            };

            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> pipe(Consumer<RadialMenuOptionBuilderLeaf<Parent>> fn) {
            fn.accept(this);
            return this;
        }

        /// This variable is fucked, but I can't think of a better way to do it
        private RadialMenuOption option;

        @Override
        public void registerActions(RadialMenu menu, PanelSyncManager syncManager, String baseName) {
            syncManager.registerSyncedAction(baseName + actionId, packet -> {
                packet = new PacketBuffer(packet.copy());
                this.onClicked
                    .onClick(menu, this.option, packet.readInt(), syncManager.isClient() ? Side.CLIENT : Side.SERVER);
            });
        }

        @Override
        public void apply(RadialMenu menu, PanelSyncManager syncManager, String baseName) {
            this.option = new RadialMenuOption();

            this.option.label = this.label;
            this.option.weight = this.weight;
            this.option.hidden = this.hidden;
            this.option.onClick = (menu2, option, mouseButton, side) -> {
                syncManager.callSyncedAction(baseName + actionId, buffer -> { buffer.writeInt(mouseButton); });
            };

            menu.options.add(this.option);
        }
    }

    /**
     * A builder for radial menu sub menus.
     */
    public static class RadialMenuOptionBuilderBranch<Parent> extends RadialMenuOptionBuilder<Parent>
        implements BranchableRadialMenu {

        public final List<RadialMenuOptionBuilder<RadialMenuOptionBuilderBranch<Parent>>> children = new ArrayList<>();
        protected final MutableInt ids;

        public RadialMenuOptionBuilderBranch(Supplier<EntityPlayer> player, Parent parent, MutableInt ids) {
            super(player, parent);
            this.ids = ids;
        }

        public RadialMenuOptionBuilderBranch<Parent> label(IIcon icon) {
            this.label = icon;
            return this;
        }

        public RadialMenuOptionBuilderBranch<Parent> label(IKey label) {
            this.label = label.asIcon()
                .width(60);
            return this;
        }

        public RadialMenuOptionBuilderBranch<Parent> weight(double weight) {
            this.weight = weight;
            return this;
        }

        public RadialMenuOptionBuilderBranch<Parent> hidden(boolean hidden) {
            this.hidden = () -> hidden;
            return this;
        }

        public RadialMenuOptionBuilderBranch<Parent> hidden(BooleanSupplier hidden) {
            this.hidden = hidden;
            return this;
        }

        public RadialMenuOptionBuilderBranch<Parent> option(
            RadialMenuOptionBuilder<RadialMenuOptionBuilderBranch<Parent>> option) {
            children.add(option);
            return this;
        }

        @Override
        public RadialMenuOptionBuilderLeaf<RadialMenuOptionBuilderBranch<Parent>> option() {
            var leaf = new RadialMenuOptionBuilderLeaf<>(player, this, ids);
            children.add(leaf);
            return leaf;
        }

        @Override
        public RadialMenuOptionBuilderBranch<RadialMenuOptionBuilderBranch<Parent>> branch() {
            var branch = new RadialMenuOptionBuilderBranch<>(player, this, ids);
            children.add(branch);
            return branch;
        }

        public RadialMenuOptionBuilderBranch<Parent> pipe(Consumer<RadialMenuOptionBuilderBranch<Parent>> fn) {
            fn.accept(this);
            return this;
        }

        @Override
        public void registerActions(RadialMenu menu, PanelSyncManager syncManager, String baseName) {
            for (var child : children) {
                child.registerActions(menu, syncManager, baseName);
            }
        }

        @Override
        public void apply(RadialMenu menu, PanelSyncManager syncManager, String baseName) {
            RadialMenuOption opt = new RadialMenuOption();

            opt.label = this.label;
            opt.weight = this.weight;
            opt.hidden = this.hidden;
            opt.onClick = (_1, option, mouseButton, side) -> {
                menu.options.clear();

                for (var child : children) {
                    child.apply(menu, syncManager, baseName);
                }
            };

            menu.options.add(opt);
        }
    }
}
