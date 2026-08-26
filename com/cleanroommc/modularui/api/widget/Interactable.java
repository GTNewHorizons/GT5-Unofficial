package com.cleanroommc.modularui.api.widget;

import com.cleanroommc.modularui.api.UpOrDown;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

/**
 * An interface that handles user interactions on {@link IWidget} objects.
 * These methods get called on the client
 */
public interface Interactable {

    /**
     * Called when this widget is pressed.
     *
     * @param mouseButton mouse button that was pressed.
     * @return result that determines what happens to other widgets
     * {@link #onMouseTapped(int)} is only called if this returns {@link Result#ACCEPT} or {@link Result#SUCCESS}
     */
    @NotNull
    default Result onMousePressed(int mouseButton) {
        return Result.ACCEPT;
    }

    /**
     * Called when a mouse button was released over this widget.
     *
     * @param mouseButton mouse button that was released.
     * @return whether other widgets should get called to. If this returns false, {@link #onMouseTapped(int)} will NOT be called.
     */
    default boolean onMouseRelease(int mouseButton) {
        return false;
    }

    /**
     * Called when this widget was pressed and then released within a certain time frame.
     *
     * @param mouseButton mouse button that was pressed.
     * @return result that determines if other widgets should get tapped to
     * {@link Result#IGNORE} and {@link Result#ACCEPT} will both "ignore" the result and {@link Result#STOP} and {@link Result#SUCCESS} will both stop other widgets from getting tapped.
     */
    @NotNull
    default Result onMouseTapped(int mouseButton) {
        return Result.IGNORE;
    }

    /**
     * Called when a key over this widget is pressed.
     *
     * @param typedChar character that was typed
     * @param keyCode   key that was pressed.
     * @return result that determines what happens to other widgets
     * {@link #onKeyTapped(char, int)} is only called if this returns {@link Result#ACCEPT} or {@link Result#SUCCESS}
     */
    @NotNull
    default Result onKeyPressed(char typedChar, int keyCode) {
        return Result.IGNORE;
    }

    /**
     * Called when a key was released over this widget.
     *
     * @param typedChar character that was typed
     * @param keyCode   key that was pressed.
     * @return whether other widgets should get called to. If this returns false, {@link #onKeyTapped(char, int)} will NOT be called.
     */
    default boolean onKeyRelease(char typedChar, int keyCode) {
        return false;
    }

    /**
     * Called when this widget was pressed and then released within a certain time frame.
     *
     * @param typedChar character that was typed
     * @param keyCode   key that was pressed.
     * @return result that determines if other widgets should get tapped to
     * {@link Result#IGNORE} and {@link Result#ACCEPT} will both "ignore" the result and {@link Result#STOP} and {@link Result#SUCCESS} will both stop other widgets from getting tapped.
     */
    @NotNull
    default Result onKeyTapped(char typedChar, int keyCode) {
        return Result.IGNORE;
    }

    /**
     * Called when this widget is focused or when the mouse is above this widget.
     * This method should return true if it can scroll at all and not if it scrolled right now.
     * If this scroll view scrolled to the end and this returns false, the scroll will get passed through another scroll view below this.
     *
     * @param scrollDirection up or down
     * @param amount          amount scrolled by (usually irrelevant)
     * @return true if this widget can be scrolled at all
     */
    default boolean onMouseScroll(UpOrDown scrollDirection, int amount) {
        return false;
    }

    /**
     * Called when this widget was clicked and mouse is now dragging.
     *
     * @param mouseButton    mouse button that drags
     * @param timeSinceClick time since drag began
     */
    default void onMouseDrag(int mouseButton, long timeSinceClick) {}

    /**
     * @return if left or right ctrl/cmd is pressed
     */
    @SideOnly(Side.CLIENT)
    static boolean hasControlDown() {
        return GuiScreen.isCtrlKeyDown();
    }

    /**
     * @return if left or right shift is pressed
     */
    @SideOnly(Side.CLIENT)
    static boolean hasShiftDown() {
        return GuiScreen.isShiftKeyDown();
    }

    /**
     * @return if alt or alt gr is pressed
     */
    @SideOnly(Side.CLIENT)
    static boolean hasAltDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    static boolean isKeyComboCtrlX(int keyID) {
        return keyID == Keyboard.KEY_X && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    static boolean isKeyComboCtrlV(int keyID) {
        return keyID == Keyboard.KEY_V && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    static boolean isKeyComboCtrlC(int keyID) {
        return keyID == Keyboard.KEY_C && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    static boolean isKeyComboCtrlA(int keyID) {
        return keyID == Keyboard.KEY_A && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    /**
     * @param key key id, see {@link Keyboard}
     * @return if the key is pressed
     */
    @SideOnly(Side.CLIENT)
    static boolean isKeyPressed(int key) {
        return Keyboard.isKeyDown(key);
    }

    ResourceLocation PRESS_SOUND = new ResourceLocation("gui.button.press");

    /**
     * Plays the default button click sound
     */
    @SideOnly(Side.CLIENT)
    static void playButtonClickSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(PRESS_SOUND, 1.0F));
    }

    enum Result {

        /**
         * Nothing happens.
         */
        IGNORE(false, false),
        /**
         * Interaction is accepted, but other widgets will get checked.
         */
        ACCEPT(true, false),
        /**
         * Interaction is rejected and no other widgets will get checked.
         */
        STOP(false, true),
        /**
         * Interaction is accepted and no other widgets will get checked.
         */
        SUCCESS(true, true);

        public final boolean accepts;
        public final boolean stops;

        Result(boolean accepts, boolean stops) {
            this.accepts = accepts;
            this.stops = stops;
        }
    }
}
