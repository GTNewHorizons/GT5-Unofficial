package pers.gwyog.gtneioreplugin.util;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class Veinrenamer<T> extends AbstractBeanField<T> {

	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		String ret = null;
		CharSequence s = "/";

		if (value.contains(s)) {
			ret = value.split("/")[1];
			ret =ret.replaceAll("&", "");
			ret =ret.replaceAll(" ", "");
			ret =ret.replaceAll("\\.", "");
			ret = ret.toLowerCase();
		}
		else
			ret=value;
			ret =ret.replaceAll(" ", "");
			ret = ret.toLowerCase();
		return ret;
	}

}
