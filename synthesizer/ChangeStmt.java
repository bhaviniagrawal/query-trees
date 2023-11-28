package synthesizer;


import querytree.QueryTreeNode;
import querytree.QueryTree;


public class ChangeStmt extends Stmt {

    int numChildren;
    String operator;


    /**
     * Need to make sure npr_1 and the new operator both have numChildren (1or2) childrens
     * @param nptr_1
     * @param numChildren
     * @param operator New operator changed to, shall support same number of children as the original operator
     */
    public ChangeStmt(int nptr_1, int numChildren, String operator) {
        this.nptr_1 = nptr_1;
        this.numChildren = numChildren;
        this.operator = operator;

        if(QueryTreeNode.OperatorToNumChildren.get(operator) != numChildren){
            throw new IllegalArgumentException("Wrong ChangeStmt: Operator " + operator + " does not have " + numChildren + " children");
        }
    }

    @Override
    public void transform_tree(QueryTree tree) {
        QueryTreeNode new_node = new QueryTreeNode(tree.get(nptr_1));
        new_node.opertaror = operator;
        tree.set(nptr_1, new_node);
    }

    @Override
    public String toString() {
        if(numChildren == 2){
            return "Change_2(" + findPath(nptr_1) + "," + operator + ");";   
        }else{
            return "Change_1(" + findPath(nptr_1) + "," + operator + ");";
        }
    }
}
