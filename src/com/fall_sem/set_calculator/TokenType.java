package com.fall_sem.set_calculator;

public enum TokenType {
    // Groupings
    LEFT_CB, RIGHT_CB, LEFT_PAREN, RIGHT_PAREN, ABS_BRACK,
    // Set Operators
    MINUS, PLUS, SLASH, STAR, MODULO, EXP, FACTORIAL,

    EQUAL,
    // Values
    NUMBER, ANS,

    // Misc.
    EOL, COMMA, VARIABLE, ARG,

    //Functions
    UNION, INTER, DIFF, SIZE,

    IMPORT
}
