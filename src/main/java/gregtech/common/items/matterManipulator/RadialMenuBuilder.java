package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.common.items.matterManipulator.RadialMenu.RadialMenuClickHandler;
import gregtech.common.items.matterManipulator.RadialMenu.RadialMenuOption;

/**
 * A builder that significantly helps with making radial menus.
 */
public class RadialMenuBuilder {

    public final UIBuildContext buildContext;
    public final List<RadialMenuOptionBuilder<RadialMenuBuilder>> options = new ArrayList<>();
    public float innerRadius = 0.25f, outerRadius = 0.60f;
    public IDrawable innerIcon;

    public RadialMenuBuilder(UIBuildContext buildContext) {
        this.buildContext = buildContext;
    }

    /** Sets the inner icon */
    public RadialMenuBuilder innerIcon(IDrawable icon) {
        this.innerIcon = icon;
        return this;
    }

    /** Sets the inner icon */
    public RadialMenuBuilder innerIcon(ItemStack item) {
        this.innerIcon = new ItemDrawable(item).withOffset(-8, -8)
            .withFixedSize(32, 32);
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

    /** Adds an externally-built option. */
    public RadialMenuBuilder option(RadialMenuOptionBuilder<RadialMenuBuilder> option) {
        options.add(option);
        return this;
    }

    /**
     * Adds a new option to this builder.
     * Call {@link RadialMenuOptionBuilderLeaf#done()} once done to finish adding the option.
     */
    public RadialMenuOptionBuilderLeaf<RadialMenuBuilder> option() {
        var leaf = new RadialMenuOptionBuilderLeaf<>(buildContext, this);
        options.add(leaf);
        return leaf;
    }

    /**
     * Adds a new sub-menu to this builder.
     * Call {@link RadialMenuOptionBuilderBranch#done()} once done to finish adding the sub menu.
     */
    public RadialMenuOptionBuilderBranch<RadialMenuBuilder> branch() {
        var branch = new RadialMenuOptionBuilderBranch<>(buildContext, this);
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

        for (var option : options) {
            option.apply(menu);
        }

        return menu;
    }

    /**
     * The base data for branch and leaf builders.
     */
    public static abstract class RadialMenuOptionBuilder<Parent> {

        public final UIBuildContext buildContext;
        public final Parent parent;

        public Supplier<String> label;
        public double weight = 1;
        public BooleanSupplier hidden = () -> false;

        public RadialMenuOptionBuilder(UIBuildContext buildContext, Parent parent) {
            this.buildContext = buildContext;
            this.parent = parent;
        }

        public abstract void apply(RadialMenu menu);

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

        public RadialMenuOptionBuilderLeaf(UIBuildContext buildContext, Parent parent) {
            super(buildContext, parent);
        }

        public RadialMenuOptionBuilderLeaf<Parent> label(Supplier<String> label) {
            this.label = label;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> label(String label) {
            this.label = () -> label;
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

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(RadialMenuClickHandler onClicked) {
            this.onClicked = onClicked;
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(Runnable onClicked) {
            this.onClicked = (menu, option, mouseButton, doubleClicked) -> {
                onClicked.run();
                buildContext.getPlayer()
                    .closeScreen();
            };
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(BooleanSupplier onClicked) {
            this.onClicked = (menu, option, mouseButton, doubleClicked) -> {
                if (onClicked.getAsBoolean()) {
                    buildContext.getPlayer()
                        .closeScreen();
                }
            };
            return this;
        }

        public RadialMenuOptionBuilderLeaf<Parent> onClicked(int buttonId, Runnable onClicked) {
            this.onClicked = (menu, option, mouseButton, doubleClicked) -> {
                if (mouseButton == buttonId) {
                    onClicked.run();
                    buildContext.getPlayer()
                        .closeScreen();
                }
            };

            return this;
        }

        @Override
        public void apply(RadialMenu menu) {
            RadialMenuOption opt = new RadialMenuOption();

            opt.label = this.label;
            opt.weight = this.weight;
            opt.hidden = this.hidden;
            opt.onClick = onClicked;

            menu.options.add(opt);
        }
    }

    /**
     * A builder for radial menu sub menus.
     */
    public static class RadialMenuOptionBuilderBranch<Parent> extends RadialMenuOptionBuilder<Parent> {

        public final List<RadialMenuOptionBuilder<RadialMenuOptionBuilderBranch<Parent>>> children = new ArrayList<>();

        public RadialMenuOptionBuilderBranch(UIBuildContext buildContext, Parent parent) {
            super(buildContext, parent);
        }

        public RadialMenuOptionBuilderBranch<Parent> label(Supplier<String> label) {
            this.label = label;
            return this;
        }

        public RadialMenuOptionBuilderBranch<Parent> label(String label) {
            this.label = () -> label;
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

        public RadialMenuOptionBuilderLeaf<RadialMenuOptionBuilderBranch<Parent>> option() {
            var leaf = new RadialMenuOptionBuilderLeaf<>(buildContext, this);
            children.add(leaf);
            return leaf;
        }

        public RadialMenuOptionBuilderBranch<RadialMenuOptionBuilderBranch<Parent>> branch() {
            var branch = new RadialMenuOptionBuilderBranch<>(buildContext, this);
            children.add(branch);
            return branch;
        }

        @Override
        public void apply(RadialMenu menu) {
            RadialMenuOption opt = new RadialMenuOption();

            opt.label = this.label;
            opt.weight = this.weight;
            opt.hidden = this.hidden;
            opt.onClick = (_1, option, mouseButton, doubleClicked) -> {
                menu.options.clear();

                for (var child : children) {
                    child.apply(menu);
                }
            };

            menu.options.add(opt);
        }
    }
}
