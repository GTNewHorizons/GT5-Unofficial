package com.cleanroommc.modularui.widgets.layout;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.layout.ILayoutWidget;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.ReversedList;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.sizer.Box;
import com.cleanroommc.modularui.widget.sizer.ExpanderResizer;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

public class Flow extends ParentWidget<Flow> implements ILayoutWidget {

    public static Flow row() {
        return new Flow(GuiAxis.X);
    }

    public static Flow column() {
        return new Flow(GuiAxis.Y);
    }

    public static Flow col() {
        return new Flow(GuiAxis.Y);
    }

    /**
     * The main axis on which to align children.
     */
    private final GuiAxis axis;
    /**
     * How the children should be laid out on the main axis.
     */
    private Alignment.MainAxis maa = Alignment.MainAxis.START;
    /**
     * How the children should be laid out on the cross axis.
     */
    private Alignment.CrossAxis caa = Alignment.CrossAxis.CENTER;
    /**
     * Additional space between each child on main axis.
     * Does not work with {@link Alignment.MainAxis#SPACE_BETWEEN} and {@link Alignment.MainAxis#SPACE_AROUND}.
     */
    private int childPadding = 0;
    private int crossAxisChildPadding = 0;
    /**
     * Whether disabled child widgets should be collapsed for display.
     */
    private boolean collapseDisabledChild = false;
    /**
     * Whether the children list should be layout in reverse.
     */
    private boolean reverseLayout = false;

    private boolean wrap = false;
    private final List<IWidget> ignoredWidgets = new ArrayList<>();
    private final List<SimpleFlow> layoutWidgets = new ArrayList<>();

    public Flow(GuiAxis axis) {
        this.axis = axis;
        resizer(new ExpanderResizer(this, axis));
        sizeRel(1f, 1f);
    }

    @Override
    public int getDefaultHeight() {
        return this.axis.isHorizontal() ? getDefaultCrossAxisSize() : getDefaultMainAxisSize();
    }

    @Override
    public int getDefaultWidth() {
        return this.axis.isHorizontal() ? getDefaultMainAxisSize() : getDefaultCrossAxisSize();
    }

    public int getDefaultMainAxisSize() {
        if (!hasChildren()) return 18;
        GuiAxis axis = this.axis;
        int total = getArea().getPadding().getTotal(axis);
        for (IWidget widget : getChildren()) {
            if (shouldIgnoreChildSize(widget) || widget.resizer().hasPos(axis)) continue;
            if (widget.resizer().isExpanded() || !widget.resizer().isSizeCalculated(axis)) {
                total += axis.isHorizontal() ? widget.getDefaultWidth() : widget.getDefaultHeight();
            } else {
                total += widget.getArea().getSize(axis);
            }
            total += widget.getArea().getMargin().getTotal(axis);
        }
        return total;
    }

    public int getDefaultCrossAxisSize() {
        if (!hasChildren()) return 18;
        GuiAxis axis = this.axis.getOther();
        int max = 0;
        for (IWidget widget : getChildren()) {
            if (shouldIgnoreChildSize(widget)) continue;
            int s = widget.getArea().getMargin().getTotal(axis);
            if (!widget.resizer().isSizeCalculated(axis)) {
                s += axis.isHorizontal() ? widget.getDefaultWidth() : widget.getDefaultHeight();
            } else {
                s += widget.getArea().getSize(axis);
            }
            max = Math.max(max, s);
        }
        return max + getArea().getPadding().getTotal(axis);
    }

    private boolean buildWrappedFlows(List<IWidget> children, int size, boolean wrap) {
        this.layoutWidgets.clear();
        this.ignoredWidgets.clear();
        SimpleFlow currentFlow = new SimpleFlow();
        for (IWidget widget : children) {
            // ignore disabled child if configured as such and
            // exclude children whose position of main axis is fixed
            if (shouldIgnoreChildSize(widget) || widget.resizer().hasPos(this.axis)) {
                this.ignoredWidgets.add(widget);
                continue;
            }

            boolean isEmpty = currentFlow.widgets.isEmpty();

            if (widget.resizer().isExpanded()) {
                currentFlow.expanderCount++;
                // expanded widget size will be calculated later, but we still need to consider its margin
                currentFlow.size += widget.getArea().getMargin().getTotal(this.axis);
                if (!isEmpty) currentFlow.size += this.childPadding;
                currentFlow.widgets.add(widget);
                if (wrap) {
                    // if wrapping is enabled, then create a new row/col after every expanded
                    // TODO: is this desirable?
                    this.layoutWidgets.add(currentFlow);
                    currentFlow = new SimpleFlow();
                }
                continue;
            }
            // if the size of a widget is not calculated we can't continue
            if (!widget.resizer().isSizeCalculated(this.axis)) return false;
            int s = widget.getArea().requestedSize(this.axis);
            if (!isEmpty) s += this.childPadding;
            if (wrap && !isEmpty && currentFlow.size + s > size) {
                // test if the widget with padding fits and create a new row/col if not
                this.layoutWidgets.add(currentFlow);
                currentFlow = new SimpleFlow();
                s -= this.childPadding;
            }
            currentFlow.size += s;
            currentFlow.widgets.add(widget);
        }
        if (!currentFlow.widgets.isEmpty()) this.layoutWidgets.add(currentFlow);
        return true;
    }


    @Override
    public boolean layoutWidgets() {
        if (!hasChildren()) return true;
        final boolean coverChildren = resizer().dependsOnChildren(this.axis);
        boolean wrap = this.wrap;
        if (coverChildren && wrap) {
            ModularUI.LOGGER.warn("Flow can't coverChildren along its main axis and wrap at the same time. Offending widget: {}", this);
            wrap = false;
        }
        final boolean hasSize = resizer().isSizeCalculated(this.axis);
        Alignment.MainAxis maa = this.maa;
        if (!hasSize) {
            if (wrap) {
                return false;
            }
            if (maa != Alignment.MainAxis.START) {
                if (resizer().dependsOnChildren(this.axis)) {
                    // if this flow covers the children, we can assume start
                    maa = Alignment.MainAxis.START;
                } else {
                    // for anything else than start we need the size to be known
                    return false;
                }
            }
        }

        final Box padding = getArea().getPadding();
        final int size = hasSize ? getArea().paddedSize(this.axis) : 0;
        List<IWidget> childrenList = this.reverseLayout ? new ReversedList<>(getChildren()) : getChildren();

        if (!buildWrappedFlows(childrenList, size, wrap)) return false;
        for (SimpleFlow flow : this.layoutWidgets) {
            flow.layout(this.axis, size, padding, maa, this.childPadding);
        }
        for (IWidget widget : this.ignoredWidgets) {
            widget.resizer().updateResized();
            if (shouldIgnoreChildSize(widget)) {
                widget.resizer().setMarginPaddingApplied(true);
            }
        }
        return true;
    }

    @Override
    public boolean postLayoutWidgets() {
        return layoutCrossAxisListLike(this, this.layoutWidgets, this.axis, this.caa, this.crossAxisChildPadding);
    }

    public static boolean layoutCrossAxisListLike(IWidget parent, List<SimpleFlow> flows, GuiAxis axis, Alignment.CrossAxis caa, int crossAxisSpaceBetween) {
        if (flows.isEmpty()) return true;
        GuiAxis other = axis.getOther();
        boolean isWrapped = flows.size() > 1;
        // padding is applied in layoutCrossAxis()
        int availableSize = parent.resizer().isSizeCalculated(other) ? parent.getArea().getSize(other) : -1;
        Box padding = parent.getArea().getPadding();
        if (!isWrapped) {
            // simplified logic for non-wrapped
            flows.get(0).calculateCrossAxisSize(axis);
            // starting pos is 0 and use parents padding
            return flows.get(0).layoutCrossAxis(parent, axis, caa, availableSize, 0, padding);
        }
        if (parent.resizer().dependsOnChildren(other)) {
            // when covering children we can assume START
            caa = Alignment.CrossAxis.START;
        }
        if (caa != Alignment.CrossAxis.START && !parent.resizer().isSizeCalculated(other)) return false;
        int total = (flows.size() - 1) * crossAxisSpaceBetween; // start with cross axis child padding for total size
        for (SimpleFlow flow : flows) {
            flow.calculateCrossAxisSize(axis);
            total += flow.crossSize;
        }
        // calculate starting pos
        // TODO center padding
        int p = parent.getArea().getPadding().getStart(other);
        if (caa == Alignment.CrossAxis.END) {
            p = availableSize - total - parent.getArea().getMargin().getEnd(other);
        } else if (caa == Alignment.CrossAxis.CENTER) {
            p = (availableSize - total) / 2;
        }
        caa = Alignment.CrossAxis.CENTER;

        for (SimpleFlow flow : flows) {
            // use calculated pos and ignore parent padding
            if (!flow.layoutCrossAxis(parent, axis, caa, flow.crossSize, p, Box.ZERO)) return false;
            p += flow.crossSize + crossAxisSpaceBetween;
        }
        return true;
    }

    @Override
    public boolean canCoverByDefaultSize(GuiAxis axis) {
        return axis.getOther() == this.axis;
    }

    @Override
    public boolean shouldIgnoreChildSize(IWidget child) {
        return this.collapseDisabledChild && !child.isEnabled();
    }

    @Override
    public void onChildChangeEnabled(IWidget child, boolean enabled) {
        if (this.collapseDisabledChild) {
            ILayoutWidget.super.onChildChangeEnabled(child, enabled);
        }
    }

    public boolean isRow() {
        return getAxis().isHorizontal();
    }

    public boolean isColumn() {
        return getAxis().isVertical();
    }

    public Flow children(Iterable<IWidget> widgets) {
        for (IWidget widget : widgets) {
            child(widget);
        }
        return getThis();
    }

    public Flow children(int amount, IntFunction<IWidget> widgetCreator) {
        for (int i = 0; i < amount; i++) {
            child(widgetCreator.apply(i));
        }
        return getThis();
    }

    public <T> Flow children(Iterable<T> it, Function<T, IWidget> widgetCreator) {
        for (T t : it) {
            child(widgetCreator.apply(t));
        }
        return getThis();
    }

    public Alignment.MainAxis getMaa() {
        return maa;
    }

    public Alignment.CrossAxis getCaa() {
        return caa;
    }

    public int getChildPadding() {
        return childPadding;
    }

    public int getCrossAxisChildPadding() {
        return crossAxisChildPadding;
    }

    public boolean isCollapseDisabledChild() {
        return collapseDisabledChild;
    }

    public boolean isReverseLayout() {
        return reverseLayout;
    }

    public boolean isWrap() {
        return wrap;
    }

    /**
     * Sets the main axis alignment of this flow. This determines how multiple widgets are laid out along the main axis in this flow.
     *
     * @param maa main axis alignment
     * @return this
     * @see com.cleanroommc.modularui.utils.Alignment.MainAxis
     */
    public Flow mainAxisAlignment(Alignment.MainAxis maa) {
        this.maa = maa;
        return this;
    }

    /**
     * Sets the cross axis alignment of this flow. This determines how multiple widgets are laid out along the cross axis in this flow.
     *
     * @param caa cross axis alignment
     * @return this
     * @see com.cleanroommc.modularui.utils.Alignment.CrossAxis
     */
    public Flow crossAxisAlignment(Alignment.CrossAxis caa) {
        this.caa = caa;
        return this;
    }

    /**
     * Sets a fixed pixel size padding between all children widgets.
     *
     * @param spaceBetween pixel size padding between children
     * @return this
     */
    public Flow childPadding(int spaceBetween) {
        this.childPadding = spaceBetween;
        return this;
    }

    /**
     * Sets if disabled children should be collapsed.
     */
    public Flow collapseDisabledChild() {
        this.collapseDisabledChild = true;
        return this;
    }

    /**
     * Sets if disabled children should be collapsed. This means that if a child changes enabled state, this widget gets notified and
     * re-layouts its children. Children which are disabled will not be considered during layout, so that the flow will not appear to have
     * empty spots. This is disabled by default on Flow.
     *
     * @param collapse true if disabled children should be collapsed
     * @return this
     */
    public Flow collapseDisabledChild(boolean collapse) {
        this.collapseDisabledChild = collapse;
        return this;
    }

    /**
     * Sets if the children list should be layout in reversed or not (Default is false).
     *
     * @param reverseLayout true if the children list should be layout in reverse
     * @return this
     */
    public Flow reverseLayout(boolean reverseLayout) {
        this.reverseLayout = reverseLayout;
        return this;
    }

    public Flow reverseLayout() {
        return reverseLayout(true);
    }

    /**
     * This causes the Flow to create multiple rows/columns when widgets overflow the main axis size.
     * This my causes some unexpected behavior in layout. This feature is experimental.
     *
     * @param wrap if overflowing widgets should be wrapped to a next row/column
     * @return this
     */
    @ApiStatus.Experimental
    public Flow wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    @ApiStatus.Experimental
    public Flow wrap() {
        return wrap(true);
    }

    /**
     * Sets the cross axis child padding. It is a fixed pixel size between rows/columnd.
     * This only used if {@link #wrap()} is used.
     *
     * @param crossAxisChildPadding pixel space between rows/columns when wrapping is active
     * @return this
     */
    @ApiStatus.Experimental
    public Flow crossAxisChildPadding(int crossAxisChildPadding) {
        this.crossAxisChildPadding = crossAxisChildPadding;
        return this;
    }

    public GuiAxis getAxis() {
        return axis;
    }

    @Override
    protected String getTypeName() {
        return this.axis.isHorizontal() ? "Row" : "Column";
    }
}
