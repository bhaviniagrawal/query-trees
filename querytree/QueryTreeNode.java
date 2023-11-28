package querytree;
import java.util.Map;

import lib.TreePrinter.PrintableNode;

/**
 * 
 * Note: This TreeNode currently support pointer based binary tree options, but should mainly be used as array-based binary tree in this project.
 */
public class QueryTreeNode implements PrintableNode{

    public String opertaror;
    public String[] parameters;
    public int num_parameters;
    public int num_children;

    public QueryTreeNode left;
    public QueryTreeNode right;

    public static final Map<String, Integer> OperatorToNumParameters = Map.of(
        "Sel",2,
        "Proj",1,
        "Input",1,
        "Dedup",1,
        "InSub",1,
        "LJoin",2,
        "RJoin",2,
        "IJoin",2
    );


    public static final Map<String, Integer> OperatorToNumChildren = Map.of(
        "Sel",1,
        "Proj",1,
        "Input",0,
        "Dedup",1,
        "InSub",2,
        "LJoin",2,
        "RJoin",2,
        "IJoin",2
    );

    // Empty Constructor might not be used
    public QueryTreeNode(){
        opertaror = "";
        parameters = new String[0];
        num_parameters = 0;
        num_children = 0;
        left = null;
        right = null;
    }

    // Shallow copy constructor
    public QueryTreeNode(QueryTreeNode other) {
        this.opertaror = other.opertaror;
        this.parameters = other.parameters.clone(); 
        this.num_parameters = other.num_parameters;
        this.num_children = other.num_children;
        this.left = other.left; 
        this.right = other.right;
    }

    /**
     * Constructor for QueryTreeNode.
     * This constructor won'd initialize the left and right child.
     * @param operator
     * @param all_parameters in the form like "a1 a2" seperated by space, can have 0,1,2 parameters according to the operator type
     */
    public QueryTreeNode(String operator, String all_parameters){
        this.opertaror = operator;
        this.parameters = all_parameters.split(" ");
        this.num_parameters = this.parameters.length;

        if (OperatorToNumParameters.containsKey(operator)) {
            this.num_parameters = OperatorToNumParameters.get(operator);
        } else {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        
        if (OperatorToNumChildren.containsKey(operator)) {
            this.num_children = OperatorToNumChildren.get(operator);
        } else {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        
        this.left = null;
        this.right = null;
    }

    public void set_left(QueryTreeNode left){
        this.left = left;
    }

    public void set_right(QueryTreeNode right){
        this.right = right;
    }

    
    

    public String toString(){
        String result = "";
        result += this.opertaror + "<";
        for (String parameter : this.parameters) {
            result += parameter + " ";
        }
        result = result.trim();
        result += ">";
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QueryTreeNode other = (QueryTreeNode) obj;
        return this.opertaror.equals(other.opertaror);
    }


    
    @Override
    public PrintableNode getLeft() {
        return left;
    }

    @Override
    public PrintableNode getRight() {
        return right;
    }

    @Override
    public String getText() {
        return this.toString();
    }

    public String getOperator(){
        return this.opertaror;
    }

}