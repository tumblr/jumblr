package com.tumblr.jumblr.types;

import java.util.Map;

/**
 * Common test helpers
 * @author jc
 */
public class TypeTest {

    protected String flatSerialize(Map<String, ?> options) {
        String str = "{";
        boolean first = true;
        for (String key : options.keySet()) {
            if (!first) { str += ","; }
            str += "\"" + key + "\":";
            if (options.get(key).toString().charAt(0) != '[') {
                if (options.get(key) instanceof Number) {
                    str += options.get(key);
                } else {
                    str += "\"" + options.get(key).toString() + "\"";
                }
            } else {
                str += options.get(key).toString();
            }
            first = false;
        }
        str += "}";
        return str;
    }

}
