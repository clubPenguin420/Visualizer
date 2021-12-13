package com.fall_sem.set_calculator;

import com.fall_sem.set_calculator.utils.Error;
import com.fall_sem.set_calculator.utils.SetOps;
import static com.fall_sem.set_calculator.TokenType.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private final ArrayList<Token> tokens;
    private int current = 0;
    private Environment env;

    public Parser(ArrayList<Token> tokens, Environment env) {
        this.tokens = tokens;
        this.env = env;
    }

    public Expression parse() throws IOException {
        return assignment();
    }

    // going from smallest layer to highest layer

    /**
     * literal :>  NUMBER | '(' expression ')' ;
     */
    private Expression literal() throws IOException {
        if(match(NUMBER)) return new Expression.Literal(previous().getLexme());
        if(match(VARIABLE)){
            return new Expression.Literal(env.variables.get(String.valueOf(previous().getLexme())));
        }
        if(match(ANS)) return new Expression.Literal(env.getPreviousResult());
        if(match(LEFT_PAREN)) {
            try {
                Expression expr = expression();
                consume(RIGHT_PAREN);
                return new Expression.Grouping(expr, "grouping");
            } catch(RuntimeException | IOException ignored) {
            }
        }
        if(match(ABS_BRACK)) {
            Expression expr = expression();
            try {
                consume(ABS_BRACK);
                return new Expression.Grouping(expr, "abs");
            } catch(RuntimeException ignored) {
            }
        }
        if(match(IMPORT)) {
            consume(LEFT_PAREN);
            String path = String.valueOf(advance().getLexme());

            if (match(COMMA)) {
                boolean override = Boolean.parseBoolean(String.valueOf(advance().getLexme()));
                consume(RIGHT_PAREN);
                env.importVariablesFromFile(path, override);
            } else {
                consume(RIGHT_PAREN);
                env.importVariablesFromFile(path, false);
            }
            return new Expression.Null();
        }
        throw new Error("Not a valid expression :(");
    }

    /*
     * function :> func_name '(' expression ')' | literal ;
     * func_name is all the functions valid, I can't be bothered to write all of them down
     */
    private Expression function() throws IOException {
        ArrayList<TokenType> setFuncs = new ArrayList<>(Arrays.asList(UNION, INTER, DIFF, SIZE));
        if(match(setFuncs)){
            Token function = previous();
            consume(LEFT_PAREN);
            ArrayList<Expression> args = new ArrayList<>();
            args.add(set());
            while(match(COMMA)){
                args.add(set());
            }
            consume(RIGHT_PAREN);
            return new Expression.Function(function, args);
        }
        return literal();

    }

    /*
     * factorial :>  function '!' | function ;
     */
    private Expression factorial() throws IOException {
        Expression left = function();
        if(match(FACTORIAL)) {
            Token fac = previous();
            return new Expression.Unary(fac, left);
        }

        return left;
    }

    /**
     * negate :>  '-' negate | factorial ;
     */
    private Expression negate() throws IOException {
        if(match(MINUS)) {
            Token minus = previous();
            Expression right = factorial();
            return new Expression.Unary(minus, right);
        }

        return factorial();
    }

    /**
     * power :>  negate [ ( '^' ) negate ] ;
     */
    private Expression power() throws IOException {
        Expression expr = negate();

        while(match(EXP)) {
            Token op = previous();
            Expression right = power();
            expr = new Expression.Binary(expr, op, right);
        }
        return expr;
    }


    /**
     * multidivimod :>  power [ ( '*' | '/' | '%' ) power ] ;
     */
    private Expression multidivimod() throws IOException {
        Expression expr = power();

        while(match(STAR, SLASH, MODULO)) {
            Token op = previous();
            Expression right = power();
            expr = new Expression.Binary(expr, op, right);
        }
        return expr;
    }

    /**
     * addsub :>  multidivimod [ ( '+' | '-' ) multidivimod ] ;
     */
    private Expression addsub() throws IOException {
        Expression expr = multidivimod();

        while(match(PLUS, MINUS)) {
            Token op = previous();
            Expression right = multidivimod();
            expr = new Expression.Binary(expr, op, right);
        }
        return expr;
    }

    /**
     * expression :>  addsub ;
     */
    private Expression expression() throws IOException {
        return addsub();
    }

    private Expression set() throws IOException {
        if(match(LEFT_CB)) {
            ArrayList<Expression> values = new ArrayList<>();
            values.add(expression());
            while(match(COMMA)) {
                values.add(expression());
            }
            System.out.println(values);
            consume(RIGHT_CB);
            return new Expression.Set(values);
        }
        return expression();
    }

    /**
     *assignment :> ( variable = set ) | set ;
     */
    private Expression assignment() throws IOException {
        if(match(VARIABLE)){
            Token var_name = previous();
            if(match(EQUAL)){
                Expression expr = expression();
                return new Expression.Assign(String.valueOf(var_name.getLexme()), expr);
            }
            else {
                stepBack(1);
            }

        }
        return set();
    }


    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (checkType(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean match(ArrayList<TokenType> types) {
        for (TokenType type : types) {
            if (checkType(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private void consume(TokenType type) {
        if (checkType(type)) {
            advance();
            return;
        }


//        System.err.println(message);
        throw new Error(String.format("Missing Token %s :(", type.toString()));
    }

    private boolean checkType(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == EOL;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
    private void stepBack(int steps) { current -= steps; }
}
