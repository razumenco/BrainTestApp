package com.example.samsungapp1;

import java.util.Stack;
import java.util.StringTokenizer;

public class DoubleStackAlgorithm {

    /*
    Класс для решения математических примеров используя двухстековый алгоритм Дейкстры.
     */

    private static boolean correctExpression(String expression) { // Проверка примера на правильную расстановку скобок

        int count = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(expression);

        while (stringTokenizer.hasMoreTokens()) {
            String cur = stringTokenizer.nextToken();
            if (cur.equals("("))
                count++;
            else if (cur.equals(")"))
                count--;
        }

        return (count == 0);
    }

    public static int calculate(String expression) { // Непосредственно метод, возвращающий решение примера

        if (!correctExpression(expression))
            throw new IllegalArgumentException("------ Incorrect input expression to DoubleStackAlgorithm ------");

        Stack<Character> operations = new Stack<>();
        Stack<Integer> operands = new Stack<>();
        StringTokenizer stringTokenizer = new StringTokenizer(expression);

        try {

            while (stringTokenizer.hasMoreTokens()) {
                String c = stringTokenizer.nextToken();

                if (c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/"))
                    operations.push(c.toCharArray()[0]);

                else if (c.equals(")")) {

                    int op1 = operands.pop(), op2 = operands.pop();
                    char operation = operations.pop();

                    switch (operation) {
                        case '+':
                            operands.push(op1 + op2);
                            break;
                        case '-':
                            operands.push(op2 - op1);
                            break;
                        case '*':
                            operands.push(op1 * op2);
                            break;
                        case '/':
                            operands.push(op2 / op1);
                    }

                }

                else {
                    int op;
                    try {
                        op = Integer.parseInt(c);
                    } catch (NumberFormatException n) {
                        continue;
                    }
                    operands.push(op);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    return operands.pop();
    }
}
