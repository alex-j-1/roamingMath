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

import static dajohnson89.Operator.*;

/**
 * Various utlity methods for analyzing a graph.
 */
public class MathUtils {

    private static final Set<String> operatorNames = new HashSet<>(Operator.values().length);
    private static final Pattern integerPattern = Pattern.compile("\\d+");
    static {
        for (Operator operator : Operator.values()) {
            operatorNames.add(operator.toString());
        }
    }

    //don't construct me! I'm just a utils class!
    private MathUtils(){}

    /**
     * Evaluate an expression using {+,-,*, abs}.
     * Example input: abs(add(multiply(-1,abs(26881)),add(multiply(-1,43945),40)))
     * Example output: 70786
     * @param expression
     * @return unsigned 64-bit integer representing the evaluated expression
     */
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
     * Helper method. Breaks expr into significant tokens.
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
     * Implementation reference: http://en.wikipedia.org/wiki/Breadth-first_search
     *
     * @param graph The graph we're searching over
     * @param startID The starting point of the desired shortest path
     * @param goalID The ending point of the desired shortest path
     * @return
     */
    public static final LinkedList<Long> calculateShortestPath(Graph graph, Long startID, Long goalID) {
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
                return getPathList(startID, goalID, parents);
            }

            for (Link l : graph.getPageFromLong(u).getOutgoingList()) {
                Long v = l.getDestinationID();
                if (!visited.contains(v)) {
                    //v is a successor of u. We'll need this information for reconstructing the path.
                    parents.put(v,u);
                    visited.add(v);
                    queue.offer(v);
                }
            }
        }
        String.format("No shortest path found from %s to %s", startID, goalID);
        throw new IllegalStateException("No shortest path found.");
    }

    /**
     * Using DFS, Tarjan's algorithm finds all strongly connected components of a graph.
     * Technically, an SCC could be the intersection of several simple cycles.
     *
     * Implementation reference:
     * http://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
     * http://www.logarithmic.net/pfh-files/blog/01208083168/tarjan.py
     *
     * Count how many cycles are in the graph.
     * @param graph
     * @return The # of cycles in the graph.
     */
    public static final int countCycles(Graph graph) {

        Integer indexCounter = 0;
        Stack<Long> stack = new Stack<>();
        Map<Long, Integer> lowlinks = new HashMap<>();
        Map<Long, Integer> index = new HashMap<>();
        Set<Set<Long>> result = new HashSet<>();

        /*initialize the maps to some silly value, so we can keep track of which nodes
         haven't been analyzed.
         */
        for (Page p : graph.getPageSet()) {
            lowlinks.put(p.getNumericValue(), Integer.MAX_VALUE);
            index.put(p.getNumericValue(), Integer.MAX_VALUE);
        }
        for(Page p : graph.getPageSet()) {
            if (lowlinks.get(p.getNumericValue()).equals(Integer.MAX_VALUE))
            tarjanAlgorithm(graph, p.getNumericValue(), indexCounter, stack, lowlinks, index, result);
        }
        return result.size();
    }

    /**
     * Helper method. Based on DFS
     * @param graph
     * @param node
     * @param indexCounter
     * @param stack
     * @param lowlinks
     * @param index
     * @param result
     */
    private static final void tarjanAlgorithm(Graph graph, Long node, Integer indexCounter, Stack<Long> stack, Map<Long, Integer> lowlinks, Map<Long, Integer> index, Set<Set<Long>> result) {
        index.put(node, indexCounter);
        lowlinks.put(node, indexCounter);
        indexCounter+=1;
        stack.push(node);

        //for every successor of node,
        for (Link l : graph.getPageFromLong(node).getOutgoingList()) {
            Long successor = l.getDestinationID();
            if((lowlinks.get(successor).equals(Integer.MAX_VALUE))) {
                //successor hasn't been visited, let's recurse on it.
                tarjanAlgorithm(graph, successor, indexCounter, stack, lowlinks, index, result);
                lowlinks.put(node, Math.min(lowlinks.get(node), lowlinks.get(successor)));
            } else if (stack.contains(successor)) {
                lowlinks.put(node, Math.min(lowlinks.get(node), index.get(successor)));
            }
        }
        //if we have a directed cycle, let's add it to the result set.
        if (lowlinks.get(node).equals(index.get(node))) {
            Set<Long> connectedComponent = new HashSet<>();
            while (true) {
                Long successor = stack.pop();
                connectedComponent.add(successor);
                if (successor.equals(node)) {
                    break;
                }
            }
            //todo: this is a hack to avoid inserting trivial cycles. Can't figure out why my code does this. :-(
            if(connectedComponent.size() != 1) {
                result.add(connectedComponent);
            }
        }
    }

    /**
     * Helper method to reconstruct the shortest path obtained from BFS.
     * @param startID The starting point of BFS
     * @param goalID  The targe node of the search
     * @param parents A map associating every traversed node with its predecessor.
     * @return
     */
    private static final LinkedList<Long> getPathList(Long startID, Long goalID, Map<Long, Long> parents) {
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
}