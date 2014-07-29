package org.personal.mason.ass.utils;

/**
 * Created by mason on 6/29/14.
 */
public class UserNumberUtils {

    public static String createNumber(String prefix, long value) {
        if(prefix == null || prefix.isEmpty()){
            prefix = "U";
        }
        StringBuilder builder = new StringBuilder(prefix);
        builder.append(String.format("%012d", value));
        return builder.toString();
}

    public static void main(String... args){
        System.out.println(createNumber("AB", -3l));
    }
}
