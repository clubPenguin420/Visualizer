package com.fall_sem.set_calculator;

import com.fall_sem.set_calculator.utils.SetOps;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.Function;

import static com.fall_sem.set_calculator.utils.CheckForCalculationErrors.*;

public class Evaluator implements Expression.Visitor<Double> {
    private Environment env;
    private Stack<TreeSet<Double>> sets = new Stack<>();
    private boolean print;

    public Evaluator(Environment env) {
        this.env = env;
        print = true;
    }

    public void solve(Expression expr) {
        print = true;
        try{
            TreeSet<Double> result = sets.pop();
            if(print) {
                System.out.println(result);
//                if (String.valueOf(result).endsWith(".0")) {
//                    System.out.println(sets.pop());
//                } else {
//                    System.out.println(result);
//                }
                env.setPreviousResult(result);
            }
        } catch(Error e) {
            System.out.println(e.getMessage());
        }
    }

    public double evaluate(Expression expr) {
        return expr.accept(this);
    }

    @Override
    public Double visitNullNode(Expression.Null expr) {
        print = false;
        return 0.0;
    }

    @Override
    public Double visitLiteralNode(Expression.Literal expr) { return Double.parseDouble(expr.getValue()); }


    public Double visitFunctionNode(Expression.Function expr) {
        ArrayList<TreeSet<Double>> args = new ArrayList<>();
//        for(Expression arg : expr.getArguments()){
//            args.add(evaluate(arg));
//        }
        args.add(sets.pop());
        args.add(sets.pop());

        Function<ArrayList<TreeSet<Double>>, TreeSet<Double>> result = SetOps.setFunctions.get(expr.getFunction().getType());
        if(result == null) throw new Error("You used a function that isn't implemented yet");
        sets.push(result.apply(args));
        return 0.0;
    }

    @Override
    public Double visitUnaryNode(Expression.Unary expr) {
        double sideExpr = evaluate(expr.getSideExpr());

        switch (expr.getOperator().getType()) {
            case MINUS:
                return -1.0 * sideExpr;
            case FACTORIAL:
                return SetOps.factorial(String.valueOf(sideExpr));
            default:
                throw new Error("Failure in a Unary Node");
        }


    }


    @Override
    public Double visitGroupingNode(Expression.Grouping expr) {
        if(expr.getType().equals("grouping")) {
            return evaluate(expr.getExpression());
        }
        else if(expr.getType().equals("abs")) {
            return Math.abs(evaluate(expr.getExpression()));
        }

        //Unreachable
        throw new Error("Failure in a Grouping Node");
    }


    @Override
    public Double visitBinaryNode(Expression.Binary expr) {
        double left = evaluate(expr.getLeft());
        double right = evaluate(expr.getRight());
        checkArithmeticErrors(expr.getOperator(), left, right);
        switch (expr.getOperator().getType()) {
            case MINUS:
                return left - right;
            case PLUS:
                return left + right;
            case SLASH:
                return left / right;
            case STAR:
                return left * right;
            case EXP:
                return Math.pow(left, right);
            case MODULO:
                return left % right;
        }
        // Unreachable.
        throw new Error("Failure in a Binary Node");
    }


    @Override
    public Double visitSetNode(Expression.Set expr) {
        TreeSet<Double> set = new TreeSet<>();
        for(Expression exp : expr.getValues()) {
            set.add(evaluate(exp));
        }

        sets.push(set);
        return null;
    }

    @Override
    public Double visitAssignNode(Expression.Assign expr) {
        env.variables.put(expr.getName(), evaluate(expr.getExpression()));
        print = false;
        return 0.0;
    }
}