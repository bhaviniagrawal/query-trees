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
    
    /**
     * Parser for the ArrayBasedTree.
     * This is the recursive helper function.
     * @param expression
     * @param tree
     * @param root_ptr
     * @return
     */
    public static void parse_ArrayBasedTree_help(String expression, QueryTree tree, int root_ptr){
        //QueryTree tree = new QueryTree(300);

    

        int left_ptr = tree.getLeftChildIndex(root_ptr);
        int right_ptr = tree.getRightChildIndex(root_ptr);


        expression = expression.trim();
        if (expression == "") {
            return;
        }
        int i = 0;

        QueryTreeNode curr_node;

        // PARSE PARAMETERS IN "<>"
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
        tree.set(root_ptr, curr_node);
        
        while (expression.charAt(i) == ' ') i++;
        // Now i start right before '( if there is one'

        // PARSE CHILD EXPRESSION IN "()"
        if(curr_node.num_children == 0){
            return;
        }else if (curr_node.num_children == 1){
            parse_ArrayBasedTree_help(expression.substring(i+2, expression.length()-1), tree, left_ptr);
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
                    parse_ArrayBasedTree_help(expression.substring(left_start,i), tree, left_ptr);
                    parse_ArrayBasedTree_help(expression.substring(i+1, expression.length()-1), tree, right_ptr);
                    return;
                }
            }
        }
        return;
    }
    /**
     * @param expression
     * @return an array based - parse query tree for the expression
     */
    public static QueryTree parse_ArrayBasedTree(String expression){
        QueryTree tree = new QueryTree(300);
        parse_ArrayBasedTree_help(expression, tree, 0);
        return tree;
    }

}