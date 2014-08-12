package org.personal.mason.ass.common.jpa;

import java.util.NoSuchElementException;

/**
 * Created by mason on 8/9/14.
 */
public enum  OrderDirection {
    ASC('+'),
    DESC('-');

    private char symbal;

    private OrderDirection(char symbal) {
        this.symbal = symbal;
    }

    public char getSymbal() {
        return symbal;
    }

    public static OrderDirection get(char symbal){
        for (OrderDirection direction : values()){
            if(direction.getSymbal() == symbal){
                return direction;
            }
        }
        return ASC;
    }
}
