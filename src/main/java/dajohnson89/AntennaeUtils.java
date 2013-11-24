package dajohnson89;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dajohnson89.Operator.ABS;
import static dajohnson89.Operator.SUBTRACT;
import static dajohnson89.Operator.ADD;
import static dajohnson89.Operator.MULTIPLY;


public class AntennaeUtils {

    public static final String DEADEND = "DEADEND";
    public static final String END = "END";
    private static final Set<String> operatorNames = new HashSet<>(Operator.values().length);

    static {
        for (Operator operator : Operator.values()) {
            operatorNames.add(operator.toString());
        }
    }

    private AntennaeUtils(){}

    public static Long evaluateExpression(String expression) {
        /*
        abs(add(multiply(-1,abs(26881)),add(multiply(-1,43945),40)))
         */
        //create a List from a minified version of the expression
        List<String> tokens = tokenizeExpression(expression.replaceAll("\\s", "").toLowerCase());

        Stack<String> tokenStack = new Stack<>();
        Stack<Operator> operatorStack = new Stack<>();
        Operator operator = null;
        Long firstEncounteredArgument = null;
        Long secondEcounteredArgument = null;
        List<Long> args;
        Long calculatedIntermediateValue = null;
        for (String token : tokens) {
            tokenStack.push(token);

            //this is our stopping criterion
            if (operatorStack.size() == 0) {

            }


            if(operatorNames.contains(token)) {
                operatorStack.push(Operator.valueOf(token.toUpperCase()));
            }

            //todo: Consider extracting this into a method
            //todo: How do we deal with removing parentheses from the stack?
            if (token.equals(")")) {
                operator = operatorStack.pop();

                //if abs, we only want to supply one number to the operator!
                if (ABS.equals(operator)) {
                    //pop the rpr
                    tokenStack.pop();
                    firstEncounteredArgument = Long.parseLong(tokenStack.pop());
                    //pop the lpr
                    tokenStack.pop();
                    //pop the opr
                    tokenStack.pop();
                } else //we must supply 2 numbers
                {
                    //pop the rpr
                    tokenStack.pop();
                    secondEcounteredArgument = Long.parseLong(tokenStack.pop());
                    firstEncounteredArgument = Long.parseLong(tokenStack.pop());
                    //pop the lpr
                    tokenStack.pop();
                    //pop the opr
                    tokenStack.pop();
                }
                args = new ArrayList<>(2);
                args.add(firstEncounteredArgument);
                args.add(secondEcounteredArgument);
                calculatedIntermediateValue = operator.apply(args.toArray(new Long[2]));

                tokenStack.push(Long.toString(calculatedIntermediateValue));

                //reset the values
                firstEncounteredArgument = null;
                secondEcounteredArgument = null;
                operator = null;
            }
        }
        return calculatedIntermediateValue;
    }

    private static final List<String> tokenizeExpression(String expr) {

        List<String> tokens= new ArrayList<>();
        boolean failed = false;
        boolean numIsNeg = false;

        int i = 0;
        while (i < expr.length()) {
            numIsNeg = false;
            switch (expr.charAt(i)) {
                case 'a': {
                    if (expr.substring(i, i+ ABS.toString().length()).equals(ABS.toString())) {
                        tokens.add(ABS.toString());
                        i+= ABS.toString().length();
                        break;
                    } else if (expr.substring(i, i+ ADD.toString().length()).equals(ADD.toString())) {
                        tokens.add(ADD.toString());
                        i+= ADD.toString().length();
                        break;
                    } else {
                        failed = true;
                        break;
                    }
                }
                case 'm': {
                    if (expr.substring(i, i + MULTIPLY.toString().length()).equals(MULTIPLY.toString())) {
                        tokens.add(MULTIPLY.toString());
                        i+= MULTIPLY.toString().length();
                        break;
                    } else {
                        failed = true;
                        break;
                    }
                }
                case 's': {
                    if (expr.substring(i, i+ SUBTRACT.toString().length()).equals(SUBTRACT.toString())) {
                        tokens.add(SUBTRACT.toString());
                        i+= SUBTRACT.toString().length();
                        break;
                    }
                }
                case '(': {
                    tokens.add("(");
                    i++;
                    break;
                }
                case ')': {
                    tokens.add(")");
                    i++;
                    break;
                }
                case ',': {
                    //ignore commas
                    i++;
                    break;
                }
                case '-': {
                    numIsNeg = true;
                }
                //the only other valid possibility is an integer.
                //todo[DJ]: Optimize this
                //todo[DJ]: Consider extracting this as a method
                default: {
                    Pattern integerPattern = Pattern.compile("\\d+");
                    Matcher integerMatcher = integerPattern.matcher(expr.substring(i, expr.length()));
                    if (integerMatcher.find()) {
                        String rawLongToAdd = integerMatcher.group();
                        if (numIsNeg) {
                            tokens.add("-"+rawLongToAdd);
                            i+=(rawLongToAdd.length()+1);
                        } else {
                            tokens.add(rawLongToAdd);
                            i+=rawLongToAdd.length();
                        }
                        break;
                    } else {
                        failed = true;
                        break;
                    }
                }
            }
            if (failed) {
                throw new IllegalArgumentException
                        ("Failed to parse the following expression at index "+ i +':'+ expr);
            }
        }
        return tokens;
    }

    private static Long calculate(Operator operator, Long... args) {
        return operator.apply(args);
    }
}
