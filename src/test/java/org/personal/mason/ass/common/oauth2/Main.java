package org.personal.mason.ass.common.oauth2;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Created by mason on 8/10/14.
 */
public class Main {
    public String string;
    public List<Integer> integers;
    public Collection<Double> doubles;


    public static void main(String... args){
        Field[] declaredFields = Main.class.getDeclaredFields();
        for (Field field : declaredFields){
            Type genericType = field.getGenericType();
        }
    }
}
