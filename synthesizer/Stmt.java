package synthesizer;


import querytree.QueryTreeNode;
import querytree.QueryTreeParser;
import querytree.QueryTree;



public abstract class Stmt {
    protected int nptr_1;
    protected int nptr_2;

    abstract public void transform_tree(QueryTree tree);

    public boolean validCheck(QueryTree tree) {
        return true;
    }

    public static String findPath(int index) {
        
        index++;
        if (index == 1) {
            return "X"; // Root node, no path needed
        }

        StringBuilder path = new StringBuilder();
        while (index > 1) {
            if (index % 2 == 0) {
                path.insert(0, "L"); // Left child
            } else {
                path.insert(0, "R"); // Right child
            }
            index /= 2; // Move to parent
        }

        return path.toString();
    }

    @Override
    public String toString() {
        return "Stmt(" +
               "nptr_1=" + findPath(nptr_1) +
               ", nptr_2=" + findPath(nptr_2) +
               ')';
    }
}
