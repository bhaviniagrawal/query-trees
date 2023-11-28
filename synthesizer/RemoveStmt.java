package synthesizer;


import querytree.QueryTreeNode;
import querytree.QueryTreeParser;
import querytree.QueryTree;

public class RemoveStmt extends Stmt {

    boolean isRight;

    public RemoveStmt(int nptr_1, boolean isRight) {
        this.nptr_1 = nptr_1;
        this.isRight = isRight;     
    }

    @Override
    public void transform_tree(QueryTree tree) {
        tree.removeNode(nptr_1, isRight);
    }

    @Override
    public String toString() {
        if(isRight){
            return "Remove(Right," + findPath(nptr_1) + ");";
        }else{
            return "Remove(Left," + findPath(nptr_1) + ");";
        }
    }

    @Override
    public boolean validCheck(QueryTree tree) {
        if(tree.getNumChildren(nptr_1)==0) return false;
        // if(tree.getNumChildren(nptr_1)==2){
        //     if(isRight){
        //         if(tree.getNumChildren(tree.getRightChildIndex(nptr_1))!=1) return false;
        //     }else{
        //         if(tree.getNumChildren(tree.getLeftChildIndex(nptr_1))!=1) return false;
        //     }

        // }
        return true;
    }
    
}
