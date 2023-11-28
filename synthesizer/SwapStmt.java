package synthesizer;


import querytree.QueryTree;


public class SwapStmt extends Stmt {

    // numChildren is the number of children of the node to be swapped, must be consistent, either 1 or 2
    int numChildren;

    /**
     * Need to make sure npr_1 and nptr_2 both have numChildren childrens, the constructor would not check this
     * @param nptr_1
     * @param nptr_2
     * @param numChildren
     */
    public SwapStmt(int nptr_1,int nptr_2, int numChildren) {
        this.nptr_1 = nptr_1;
        this.nptr_2 = nptr_2;
        this.numChildren = numChildren;
    }

    @Override
    public void transform_tree(QueryTree tree) {
        tree.swapNodes(nptr_2, nptr_1);
    }

    @Override
    public String toString() {
        if(numChildren == 2){
            return "Swap_2(" + findPath(nptr_1) + "," + findPath(nptr_2) +");";   
        }else{
            return "Swap_1(" + findPath(nptr_1) + "," + findPath(nptr_2) +");";
        }
    }

    @Override
    public boolean validCheck(QueryTree tree) {
        if(nptr_1 == nptr_2) return false; // No meaning to swap with itself.
        
        return true;
    }

}
