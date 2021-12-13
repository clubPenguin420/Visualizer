package com.fall_sem.set_calculator.utils;

import com.fall_sem.set_calculator.TokenType;
import sun.reflect.generics.tree.Tree;

import static com.fall_sem.set_calculator.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;

public class SetOps {
    public static Map<TokenType, Function<ArrayList<TreeSet<Double>>, TreeSet<Double>>> setFunctions = new HashMap<>();
    private final static double RECTANGLES = 100000000;
    public static final double PHI = 1.618033988749894;

    static {
        setFunctions.put(UNION, SetOps::union);
    }

    private static TreeSet<Double> union(ArrayList<TreeSet<Double>> args) {
        TreeSet<Double> union = new TreeSet<>();
        union.addAll(args.get(0));
        union.addAll(args.get(1));
        return union;
    }
    /*
    If it gets a whole number then it will evaluate it normally and return the value, but if it gets a decimal number
    then it will evaluate the gamma function(sterling's approximation) and return the value
     */
    public static double factorial(String n) {
        if (n.endsWith(".0")) {
            int num = Integer.parseInt(n.substring(0, n.length() - 2));
            double sum = 1;
            for (int i = num; i > 0; i--) {
                sum *= i;
            }
            return sum;
        } else {
            double num = Double.parseDouble(n);
            return Math.sqrt(2 * Math.PI * num) * Math.pow(num / Math.E, num);
        }
    }


    //    public static double getZAtHeight(double mean, double std, double height) {
//        return Math.sqrt(-2.0 * Math.log(height * (std * Math.sqrt(2.0 * Math.PI)))) * std + mean;
//    }
}