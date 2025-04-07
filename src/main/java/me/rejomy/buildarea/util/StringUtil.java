package me.rejomy.buildarea.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public boolean contains(String targetString, String... words) {
        targetString = targetString.toLowerCase();

        for (String word : words) {
            if (targetString.contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
