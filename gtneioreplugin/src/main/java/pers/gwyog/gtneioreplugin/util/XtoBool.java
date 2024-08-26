package pers.gwyog.gtneioreplugin.util;

import java.util.ResourceBundle;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BooleanConverter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class XtoBool<T> extends AbstractBeanField<T> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        if (value.isEmpty()) {
            return null;
        }
        String[] trueStrings = { "x", "X" };
        String[] falseStrings = { "" };
        Converter bc = new BooleanConverter(trueStrings, falseStrings);
        try {
            return bc.convert(Boolean.class, value.trim());
        } catch (ConversionException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                    value,
                    field.getType(),
                    ResourceBundle.getBundle("convertGermanToBoolean", errorLocale).getString("input.not.boolean"));
            csve.initCause(e);
            throw csve;
        }
    }
}
