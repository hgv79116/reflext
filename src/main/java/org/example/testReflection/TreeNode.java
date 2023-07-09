package org.example.testReflection;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class TreeNode {
    public void invoke() {
        System.out.println("" + this.getClass() + " invoked");
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields) {
            try {
                System.out.println("haha " + field.getType().getTypeName());
                ((TreeNode1) field.get(this)).invoke();
            } catch (Exception e) {
                System.out.println(e);
            }
//            field.get(this).invoke();
        }
    }
}


