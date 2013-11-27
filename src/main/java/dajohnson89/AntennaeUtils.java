package dajohnson89;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dajohnson89.Operator.ABS;
import static dajohnson89.Operator.SUBTRACT;
import static dajohnson89.Operator.ADD;
import static dajohnson89.Operator.MULTIPLY;

public class AntennaeUtils {

    private static final Set<String> operatorNames = new HashSet<>(Operator.values().length);
    private static final Pattern integerPattern = Pattern.compile("\\d+");
    static {
        for (Operator operator : Operator.values()) {
            operatorNames.add(operator.toString());
        }
    }

    //don't construct me! I'm just a utils class!
    private AntennaeUtils(){}

    public static Long evaluateExpression(String expression) {
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

            if(operatorNames.contains(token)) {
                operatorStack.push(Operator.valueOf(token.toUpperCase()));
            }

            //todo: Consider extracting this into a method
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

    /**
     *
     * @param expr A String representing an arithmetic operation
     * @return A list consisting of the significant pieces of the expression
     */
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
                default: {
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
    /**
     * Return the (ordered) list of nodes comprising the shortest path from startID to goalID.
     *
     * @param graph
     * @param startID
     * @param goalID
     * @return
     */
    public static LinkedList<Long> calculateShortestPath(Graph graph, Long startID, Long goalID) {
        //Map<Child, Parent> used to trace the path obtained during BFS
        Map<Long, Long> parents = new HashMap<>(graph.getPageSet().size());
        LinkedList<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();

        queue.offer(startID);
        visited.add(startID);
        parents.put(startID,null);

        while(!queue.isEmpty()) {
            Long u = queue.poll();
            if (u.equals(goalID)) {
                System.out.println("Goal reached");
                LinkedList<Long> shortestPath = new LinkedList<>();
                shortestPath.add(goalID);
                Long parent = parents.get(goalID);
                while(!parent.equals(startID)) {
                    shortestPath.add(parent);
                    parent = parents.get(parent);
                }
                shortestPath.add(startID);
                Collections.reverse(shortestPath);
                return shortestPath;
            }

            for (Link l : graph.getPageFromLong(u).getOutgoingList()) {
                Long v = l.getDestinationID();
                if (!visited.contains(v)) {
                    parents.put(v,u);
                    visited.add(v);
                    queue.offer(v);
                    //distances.put(v, (distances.get(u)+1));
                }
            }
        }
        String.format("No shortest path found from %s to %s", startID, goalID);
        throw new IllegalStateException("No shortest path found.");
    }
}