package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

public class IntegerParameter extends Parameter<Integer> {

    public IntegerParameter(Integer value, String langKey, Supplier<Integer> min, Supplier<Integer> max) {
        super(value, langKey, min, max);
    }

    @Override
    public Widget<?> createInputWidget() {
        return new TextFieldWidget().value(new IntSyncValue(this::getValue, this::setValue))
            .setNumbers(this::getMin, this::getMax);
    }
}
