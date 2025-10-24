package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

public class DoubleParameter extends NumericParameter<Double> {

    public DoubleParameter(Double value, String langKey, Supplier<Double> min, Supplier<Double> max) {
        super(value, langKey, min, max);
    }

    @Override
    public Widget<?> createInputWidget() {
        return new TextFieldWidget().value(new DoubleSyncValue(this::getValue, this::setValue))
            .setNumbersDouble(this::validateValue);
    }

    private double validateValue(double num) {
        return Math.max(min.get(), Math.min(num, max.get()));
    }
}
