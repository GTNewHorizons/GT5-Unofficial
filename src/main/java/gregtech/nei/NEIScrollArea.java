package gregtech.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.scroll.ScrollBar;
import codechicken.nei.scroll.ScrollContainer;

/**
 * A grid of {@link PositionedStack}s with NEI's scroll bar, for recipes that have more stacks than fit on the page.
 * Frontends create one in {@code RecipeMapFrontend#prepareRecipe} and register it with
 * {@link GTNEIDefaultHandler.CachedDefaultRecipe#addScrollArea}. Stacks are laid out row by row and whole rows scroll.
 * Stacks outside the visible rows are moved off-screen, so NEI still has all of them for searching.
 */
public class NEIScrollArea {

    private static final int SLOT_SIZE = 18;
    private static final int TRACK_GAP = 2;
    private static final int HIDDEN_POSITION = -10_000;

    private final int x;
    private final int y;
    private final int columns;
    private final int visibleRows;
    private final List<PositionedStack> stacks = new ArrayList<>();
    private final AreaContainer container;

    private int totalRows;
    private int firstRow;

    /**
     * @param x top left corner of the first slot, in the coordinates frontends use for slot positions
     */
    public NEIScrollArea(int x, int y, int columns, int visibleRows) {
        this.x = x + GTNEIDefaultHandler.WINDOW_OFFSET.x;
        this.y = y + GTNEIDefaultHandler.WINDOW_OFFSET.y;
        this.columns = columns;
        this.visibleRows = visibleRows;
        this.container = new AreaContainer(this.x, this.y, columns * SLOT_SIZE, visibleRows * SLOT_SIZE);
    }

    public NEIScrollArea setStacks(Collection<? extends PositionedStack> newStacks) {
        stacks.clear();
        stacks.addAll(newStacks);
        totalRows = (stacks.size() + columns - 1) / columns;
        container.setActualHeight(totalRows * SLOT_SIZE);
        firstRow = 0;
        container.setVerticalScrollOffset(0);
        layout();
        return this;
    }

    /** Whether the stack is not hidden by a scroll area. */
    public static boolean isShown(PositionedStack stack) {
        return stack.rely != HIDDEN_POSITION;
    }

    private boolean isScrollable() {
        return totalRows > visibleRows;
    }

    private boolean contains(int mouseX, int mouseY) {
        int width = columns * SLOT_SIZE + TRACK_GAP
            + container.getVerticalScroll()
                .getTrackWidth();
        return isScrollable() && new Rectangle(x, y, width, visibleRows * SLOT_SIZE).contains(mouseX, mouseY);
    }

    public boolean onMouseWheel(int direction, int mouseX, int mouseY) {
        if (!contains(mouseX, mouseY) || direction == 0) return false;
        setFirstRow(firstRow - Integer.signum(direction));
        return true;
    }

    public boolean onMouseClicked(int mouseX, int mouseY, int button) {
        if (!isScrollable() || button != 0) return false;
        if (!container.getVerticalScroll()
            .onClick(container, mouseX, mouseY, button)) return false;
        syncFromContainer();
        return true;
    }

    /** NEI does not forward drags to recipe handlers, so this is called every frame. */
    public void updateDrag(int mouseX, int mouseY, boolean leftButtonDown) {
        ScrollBar bar = container.getVerticalScroll();
        if (!bar.isMouseDragged()) return;

        if (leftButtonDown) {
            bar.onMouseDragged(container, mouseX, mouseY, 0, 0L);
            syncFromContainer();
        } else {
            bar.onClickReleased(container, mouseX, mouseY, 0);
        }
    }

    public void draw(int mouseX, int mouseY) {
        if (!isScrollable()) return;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
        container.getVerticalScroll()
            .draw(container, mouseX, mouseY, 1f);
        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
    }

    private void setFirstRow(int row) {
        row = Math.max(0, Math.min(totalRows - visibleRows, row));
        container.setVerticalScrollOffset(row * SLOT_SIZE);
        if (row == firstRow) return;
        firstRow = row;
        layout();
    }

    private void syncFromContainer() {
        setFirstRow(Math.round(container.getVerticalScrollOffset() / (float) SLOT_SIZE));
    }

    private void layout() {
        for (int i = 0; i < stacks.size(); i++) {
            PositionedStack stack = stacks.get(i);
            int visibleRow = i / columns - firstRow;
            if (visibleRow >= 0 && visibleRow < visibleRows) {
                stack.relx = x + (i % columns) * SLOT_SIZE + 1;
                stack.rely = y + visibleRow * SLOT_SIZE + 1;
            } else {
                // NEI cycles between stacks at the same position, so hidden stacks need distinct ones
                stack.relx = HIDDEN_POSITION - i;
                stack.rely = HIDDEN_POSITION;
            }
        }
    }

    /** Gives NEI's scroll bar the sizes and the scroll offset it needs. */
    private static final class AreaContainer extends ScrollContainer {

        AreaContainer(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = height;
            setVerticalScroll(
                ScrollBar.defaultVerticalBar()
                    .setOverflowType(ScrollBar.OverflowType.AUTO)
                    .setTrackPadding(TRACK_GAP, 0, 0, 0));
        }

        void setActualHeight(int height) {
            this.actualHeight = height;
        }
    }
}
