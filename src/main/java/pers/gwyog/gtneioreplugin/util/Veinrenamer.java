package pers.gwyog.gtneioreplugin.util;

import com.opencsv.bean.AbstractBeanField;

public class Veinrenamer<T> extends AbstractBeanField<T> {

    @Override
    protected Object convert(String value) {
        String ret;
        CharSequence s = "/";

        if (value.contains(s)) {
            ret = value.split("/")[1];
            ret = ret.replaceAll("&", "");
            ret = ret.replaceAll(" ", "");
            ret = ret.replaceAll("\\.", "");
            ret = ret.toLowerCase();
        } else ret = value;
        ret = ret.replaceAll(" ", "");
        ret = ret.toLowerCase();
        return ret;
    }
}
