package com.fall_sem.set_calculator.utils;

import com.fall_sem.set_calculator.TokenType;
import static com.fall_sem.set_calculator.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SetOps {

    public static Map<TokenType, Function<Double, Double>> singleParamFunctions = new HashMap<>();
    public static Map<TokenType, Function<ArrayList<Double>, Double>> multiParamFunctions = new HashMap<>();
    private final static double RECTANGLES = 100000000;
    public static final double PHI = 1.618033988749894;

    static {

        //These functions only take in the parameters it wants and if you give it more than it takes in it will ignore the extras
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

    private static double binomialpdf(double trials, double x_value, double prob_of_success) {
        double bin_coeff = factorial(String.valueOf(trials)) / (factorial(String.valueOf(x_value)) * factorial(String.valueOf(trials - x_value)));
        return bin_coeff * Math.pow(prob_of_success, x_value) * Math.pow((1 - prob_of_success), (trials - x_value));
    }

    private static double binomialcdf(double trials, double x_value, double prob_of_success) {
        double prob = 0;
        for(int i = (int) x_value; i >= 0; i--) {
            prob += binomialpdf(trials, i, prob_of_success);
        }
        return prob;
    }

    public static double getHeightAtZ(double mean, double std, double z_value) {
        return Math.exp((-1.0 / 2.0) * Math.pow( (z_value - mean) / std, 2) ) / (std * Math.sqrt(2 * Math.PI));
    }

    public static double normalpdf(double mean, double std, double x_value) {
        double area = 0;
        double width = (x_value - 6) / RECTANGLES;
        for(int i = 0; i < RECTANGLES; i++) {
            area += width * getHeightAtZ(mean, std, width * i + x_value);
        }
        return Math.abs(area);
    }

    public static double normalcdf(double mean, double std, double l_bound, double u_bound) {
        double area = 0;
        double width = (u_bound - l_bound) / RECTANGLES;
        for(int i = 0; i < RECTANGLES; i++) {
            area += width * getHeightAtZ(mean, std, width * i + l_bound);
        }
        return Math.abs(area);
    }

    public static double inverseNorm(double mean, double std, double prob) {
        double[] a = {2.50662823884, -18.61500062529, 41.39119773534, -25.44106049637};
        double[] b = {-8.47351093090, 23.08336743743, -21.06224101826, 3.13082909833};

        double y = prob - 0.5;
        double r = y * y;
        double z_score = y * (((a[3] * r + a[2]) * r + a[1]) * r + a[0]) / ((((b[3] * r + b[2]) * r + b[1]) * r + b[0]) * r + 1);
        return z_score * std + mean;
    }


    //    public static double getZAtHeight(double mean, double std, double height) {
//        return Math.sqrt(-2.0 * Math.log(height * (std * Math.sqrt(2.0 * Math.PI)))) * std + mean;
//    }
}