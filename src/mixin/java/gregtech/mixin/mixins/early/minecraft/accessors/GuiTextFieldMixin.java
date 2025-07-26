package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.client.gui.GuiTextField;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.GuiTextFieldAccessor;

@Mixin(GuiTextField.class)
public class GuiTextFieldMixin implements GuiTextFieldAccessor {

    @Shadow
    private int lineScrollOffset;

    @Override
    public int gt5u$getLineScrollOffset() {
        return lineScrollOffset;
    }
}
