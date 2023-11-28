package synthesizer;

import querytree.QueryTree;
import querytree.QueryTreeNode;

public class ChangeAppendStmt extends Stmt {
    
    String operator;
    
    /**
     * ChangeE (Change with extendsion) statement change operator of a (1-chold) node and extend it to a 2-child node 
     * while taking another node as root of a subtree and attach it to the new node's right child.
     * @param nptr_1 Should have only one (left) child
     * @param nptr_copy
     * @param operator Should have 2 children
     */
    public ChangeAppendStmt(int nptr_1, int nptr_copy, String operator) {
        this.nptr_1 = nptr_1;
        this.nptr_2 = nptr_copy;
        this.operator = operator;

        if(QueryTreeNode.OperatorToNumChildren.get(operator) != 2){
            throw new IllegalArgumentException("Wrong ChangeEStmt: Operator " + operator + " does not have 2 children");
        }
    }

    @Override
    public void transform_tree(QueryTree tree) {
        QueryTreeNode new_node = new QueryTreeNode(tree.get(nptr_1));
        new_node.opertaror = operator;
        tree.set(nptr_1, new_node);
        tree.copyAndAppend(nptr_2, nptr_1);
    }

    @Override
    public String toString() {
            return "ChangeAppend(" + findPath(nptr_1) + "," + findPath(nptr_2) + "," + operator + ");";   
    }

}
