import java.util.*;

/**
 *
 * @author DIVYESH
 */
class InvalidExpression extends Exception {

    @Override
    public String toString() {
        return "Invalid Expression :";
    }
}

public class EvaluateExp {

    public static double math(double a, double b, char c) {
        double result = 0.0;
        switch (c) {
            case '*':
                result = a * b;
                break;
            case '/':
                result = a / b;
                break;
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
        }
        return result;
    }

    public static void create(ArrayDeque<Character> opSet, ArrayDeque<String> valueSet) throws Exception {
        char c;
        double b = 0;
        double a = 0;
        if (opSet.peek() == null) {
            c = '+';
        } else {
            c = opSet.pop();
        }
        if (valueSet.peek() != null) {
            b = Double.valueOf(valueSet.pop());
        }
        if (opSet.peek() != null && opSet.peek() == '-' && valueSet.peek() != null) {
            a = (Double.valueOf(valueSet.pop())) * -1;
            opSet.pop();
        } else {
            a = Double.valueOf(valueSet.pop());
        }

        valueSet.push(String.valueOf(math(a, b, c)));
    }

    public static void evaluateExp(String str, ArrayDeque<Character> opSet, ArrayDeque<String> valueSet) throws InvalidExpression {

        boolean isLastOp = true;
        char ch;
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);

            switch (ch) {

                //Handle Parentheses..    
                case '(':
                case ')':
                    if ((i == 0 && ch == ')') || (i == (str.length() - 1) && ch == '(')) {
                        InvalidExpression e = new InvalidExpression();
                        throw e;
                    } else if (ch == ')') {
                        if (opSet.peek() != null && opSet.peek() == '(') {
                            InvalidExpression e = new InvalidExpression();
                            throw e;
                        } else {
                            try {
                                create(opSet, valueSet);
                            } catch (Exception e) {
                                System.out.println("Invalid Expression :");
                            }

                            opSet.pop();
                            count--;
                        }
                    } else {
                        opSet.push(ch);
                        count++;
                        isLastOp = true;
                    }
                    break;

                //Handle only four Operators..
                case '*':
                case '/':
                case '+':
                case '-':
                    if ((i == 0 || i == (str.length() - 1) || isLastOp) && (ch == '/' || ch == '*')) {
                        InvalidExpression e = new InvalidExpression();
                        throw e;
                    } else {
                        if ((ch == '+' || ch == '-') && (opSet.peekFirst() != null)
                                && ('*' == opSet.getFirst() || '/' == opSet.getFirst())) {
                            try {
                                create(opSet, valueSet);
                            } catch (Exception e) {
                                System.out.println("Invalid Expression :");
                            }
                        }
                        opSet.push(ch);
                        isLastOp = true;
                    }
                    break;

                //Handle numbers..        
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    String num = "";
                    if (!isLastOp) {
                        num += valueSet.pop();
                    }
                    num += ch;
                    valueSet.push(num);
                    isLastOp = false;
                    break;

                default:
                    InvalidExpression e = new InvalidExpression();
                    throw e;
            }
        }

        if (count != 0) {
            InvalidExpression e = new InvalidExpression();
            throw e;
        } else {
            while (opSet.peekFirst() != null || valueSet.size() == 2) {
                try {
                    create(opSet, valueSet);
                } catch (Exception e) {
                    System.out.println("Invalid Expression :");
                }
            }
        }

    }

    public static void main(String[] args) {

        ArrayDeque<Character> opSet = new ArrayDeque<>();
        ArrayDeque<String> valueSet = new ArrayDeque<>();

        String str = "20-(5*2-2)+(34-(5+3))";

        try {
            evaluateExp(str, opSet, valueSet);
            System.out.println(valueSet);
        } catch (InvalidExpression e) {
            System.out.println(e);
        }

    }

}
