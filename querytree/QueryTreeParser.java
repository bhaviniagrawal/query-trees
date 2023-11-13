package querytree;

import java.util.List;



public class QueryTreeParser {

    /**
     * Expressions shall look like: "Sel<p r.a0>(Proj<r.a1>(Input<r>))"; "Proj<r0.a2>(IJoin<r0.a0 r.a>(Input<r0>,Dedup(Proj<r1.a1>(Input<r1>))))"
     * 
     *  */ 
    public static QueryTreeNode parse(String expression){

        expression = expression.trim();

        int i = 0;

        QueryTreeNode curr_node;

        if (expression.startsWith("Dedup")) {
            // If the operator is Dedup, we allow no appearance of <> in the expression
            while (expression.charAt(i) != '(') i++;
            i--;
            curr_node = new QueryTreeNode("Dedup","");
        }else{
            while (expression.charAt(i) != '<') i++;
            String operator = expression.substring(0, i);
            int param_start = i;
            while (expression.charAt(i) != '>') i++;
            curr_node = new QueryTreeNode(operator, expression.substring(param_start + 1, i));
        }

        while (expression.charAt(i) == ' ') i++;
        // Now i start right before '( if there is one'

        if(curr_node.num_children == 0){
            return curr_node;
        }else if (curr_node.num_children == 1){
            curr_node.left = parse(expression.substring(i + 2, expression.length() - 1));
            return curr_node;
        }else{
            //System.out.println(operator);
            //System.out.println(expression.substring(i+2, expression.length() - 1));
            int level = 0;
            int left_start = i + 2;
            for(i=left_start; i < expression.length(); i++){
                if(expression.charAt(i) == '('){
                    level++;
                }else if(expression.charAt(i) == ')'){
                    level--;
                }
                if(level == 0 && expression.charAt(i) == ','){
                    curr_node.left = parse(expression.substring(left_start, i));
                    curr_node.right = parse(expression.substring(i + 1, expression.length() - 1));
                    return curr_node;
                }
            }
        }

        return curr_node;
    }
    

    public static void printTree(QueryTreeNode node, int level, StringBuilder prefix) {
        if (node == null) {
            return;
        }

        // Indentation for each level
        for (int i = 0; i < level; i++) {
            prefix.append("   ");
        }

        printTree(node.right, level + 1, new StringBuilder(prefix.toString() + "    "));
        System.out.println(prefix + "-> " + node);
        printTree(node.left, level + 1, new StringBuilder(prefix.toString() + "|   "));
    }

    public static void printTree(QueryTreeNode node) {
        printTree(node, 0, new StringBuilder());
    }

}