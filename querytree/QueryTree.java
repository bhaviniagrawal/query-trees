package querytree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class QueryTree {
    private QueryTreeNode[] tree;
    private int size = 0;
    private int capacity;
    private int max_i = 0;
    private Map<String, Integer> opCounts = new HashMap<>();

    public static int getL(int i){
        return 2 * i + 1;
    }
    public static int getR(int i){
        return 2 * i + 2;
    }

    public QueryTreeNode[] get_all_nodes(){
        return tree;
    }

    public QueryTree(int c) {
        tree = new QueryTreeNode[c];
        capacity = c;
    }

    // Copy constructor
    public QueryTree(QueryTree other) {
        // Copy basic fields
        this.size = other.size;
        this.capacity = other.capacity;
        this.max_i = other.max_i;
        this.opCounts = new HashMap<>(other.opCounts);

        // Deep copy the tree array
        this.tree = new QueryTreeNode[this.capacity];
        for (int i = 0; i <= this.max_i; i++) {
            if (other.tree[i] != null) {
                this.tree[i] = new QueryTreeNode(other.tree[i]);
            }
        }
    }

    public QueryTreeNode get(int i){
        if(i < 0 || i > max_i){
            return null;
        }
        return tree[i];
    }

    public QueryTreeNode set(int i, QueryTreeNode node){
        max_i = Math.max(max_i, i);
        QueryTreeNode old = tree[i];
        // if(old == null && node != null){
        //     size++;
        // }
        // if(old != null && node == null){
        //     size--;
        // }
        
        // Update opCounts
        if (old != null) {
            String oldOpName = old.getOperator();
            opCounts.put(oldOpName, opCounts.get(oldOpName) - 1);
            size--;
        }
        if (node != null) {
            String newOpName = node.getOperator();
            opCounts.put(newOpName, opCounts.getOrDefault(newOpName, 0) + 1);
            size++;
        }
        tree[i] = node;
        return old;
    }

    public boolean have_node(int i){
        return tree[i] != null;
    }


    public QueryTreeNode getRoot() {
        return tree[0];
    }

    // public QueryTreeNode getLeftChild(QueryTreeNode node) {
    //     int index = getIndex(node);
    //     int leftChildIndex = 2 * index + 1;
    //     if (leftChildIndex >= size) {
    //         return null;
    //     }
    //     return tree[leftChildIndex];
    // }

    // public QueryTreeNode getRightChild(QueryTreeNode node) {
    //     int index = getIndex(node);
    //     int rightChildIndex = 2 * index + 2;
    //     if (rightChildIndex >= size) {
    //         return null;
    //     }
    //     return tree[rightChildIndex];
    // }

    public int getLeftChildIndex(int i){
        int leftChildIndex = 2 * i + 1;
        // if (leftChildIndex > max_i) {
        //     return -1;
        // }
        return leftChildIndex;
    }

    public int getRightChildIndex(int i){
        int rightChildIndex = 2 * i + 2;
        // if (rightChildIndex > max_i) {
        //     return -1;
        // }
        return rightChildIndex;
    }

    public int getParentIndex(int i){
        if(i == 0){
            return -1;
        }
        return (i - 1) / 2;
    }

    public QueryTreeNode getLeft(int i){
        int left_i = getLeftChildIndex(i);
        if(left_i < 0) return null;
        return get(left_i);
    }

    public QueryTreeNode getRight(int i){
        int right_i = getRightChildIndex(i);
        if(right_i < 0) return null;
        return get(right_i);
    }

    /**
     * Number of children of the note at index i
     * @param i
     * @return -1 if the node at index i is not in the tree, else 0, 1, or 2
     */
    public int getNumChildren(int i){
        int num = 0;
        if(get(i) == null) return -1;
        if(getLeft(i) != null) num++;
        if(getRight(i) != null) num++;
        return num;
    }

    /**
     * Maximum index of the tree, this is useful to iterate when the tree is not full.
     * @return
     */
    public int getMaxIndex(){
        return max_i;
    }
    
    public int getSize() {
        return size;
    }


    public Map<String,Integer> getOpCounts() {
        return opCounts;
    }

    // Higer-level Tree operations

    /**
     * For the syntax for the query remain sound, need to make sure both nodes have same nunber of children.
     * @param i
     * @param j
     */
    public void swapNodes(int i, int j) {
        
        // SAFETY CHECK can remove in future.
        if(getNumChildren(i) != getNumChildren(j)){
            throw new IllegalArgumentException("Invalid swap: nodes have different number of children");
        }

        QueryTreeNode temp = tree[i];
        set(i, tree[j]);
        set(j, temp);
    }
    /**
     * Move the node at fromIndex to toIndex, and move all its children recursively.
     * @param fromIndex
     * @param toIndex Note that tree's node at this index should be empty else it would be overwritten.
     */
    public void moveUp(int fromIndex, int toIndex) {
        if (fromIndex < 0  || toIndex < 0 || fromIndex >= capacity || toIndex >= capacity) {
            System.out.println(toIndex);
            throw new IllegalArgumentException("Invalid index or node not found");
        }

        if (tree[toIndex] != null) {
            System.out.println("Cauthious: QureryTree.moveUp() intend to overwrite some existig nodes at index " + toIndex);
        }
        set(toIndex, tree[fromIndex]);
        set(fromIndex, null);
        if (getLeft(fromIndex) != null) {
            moveUp(2*fromIndex + 1, 2*toIndex + 1);
        }
        if (getRight(fromIndex) != null) {
            moveUp(2*fromIndex + 2, 2*toIndex + 2);
        }
    }
    
    /**
 * Copy the subtree at fromIndex to toIndex, and copy all its children recursively.
 * @param fromIndex The index of the root of the subtree to copy.
 * @param toIndex The index where the root of the copied subtree should be placed.
 * Note that the tree's node at this index should be empty else it would be overwritten.
 */
    public void copyAndAppend(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= capacity || toIndex >= capacity) {
            throw new IllegalArgumentException("Invalid index or node not found");
        }

    if (get(toIndex) != null) {
        System.out.println("Caution: QueryTree.copyAndAppend() intends to overwrite some existing nodes at index " + toIndex);
        return;
    }

    // Copy the node from fromIndex to toIndex.
    set(toIndex, get(fromIndex));

    // Recursively copy left and right children.
    int leftChildIndex = 2 * fromIndex + 1;
    int rightChildIndex = 2 * fromIndex + 2;
    int newLeftChildIndex = 2 * toIndex + 1;
    int newRightChildIndex = 2 * toIndex + 2;

    if (leftChildIndex < capacity && get(leftChildIndex) != null) {
        copyAndAppend(leftChildIndex, newLeftChildIndex);
    }
    if (rightChildIndex < capacity && get(rightChildIndex) != null) {
        copyAndAppend(rightChildIndex, newRightChildIndex);
    }
}

    /**
     * Remove the node at index i, and move all its children recursively. Can pick Left or Right child as successor.
     * If the node at i have 2 children, then the successor will be the right child if useRightAsSuccessor is true, else the left child.
     * If the node at i have 1 child, then the successor will be the left child and useRightAsSuccessor will be ignored.
     * If the node at i have 0 child, then that leave node will be removed and useRightAsSuccessor will be ignored.
     * @param i
     * @param useRightAsSuccessor
     */
    public void removeNode(int i, boolean useRightAsSuccessor) {
        if (i < 0 || i >= capacity || tree[i] == null) {
            throw new IllegalArgumentException("Invalid index or node not found");
        }

        set(i, null);
        if (getLeft(i) != null && getRight(i) != null) {
            if (useRightAsSuccessor) {
                // Succ R
                removeAll(2*i+1);
                moveUp(2*i + 2, i);
            } else {
                // Succ L
                removeAll(2*i + 2);
                moveUp(2*i + 1, i);
            }
        } else if (getLeft(i) != null) {
            // i is 1-child Succ L
            
            moveUp(2*i + 1, i);
        } 
    }
    /**
     * Remove all nodes in the subtree rooted at i.
     * @param i
     */
    public void removeAll(int i) {
        if (i < 0 || i >= capacity || tree[i] == null) {
            return;
        }
        removeAll(2*i + 1);
        removeAll(2*i + 2);
        set(i, null);
    }



    
    private int getIndex(QueryTreeNode node) {
        for (int i = 0; i < capacity; i++) {
            if (tree[i] == node) {
                return i;
            }
        }
        throw new IllegalArgumentException("Node not found in tree");
    }

    public ArrayList<Integer> get1ChildPtrs(){
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i <= max_i; i++){
            if(getNumChildren(i) == 1){
                result.add(i);
            }
        }
        return result;
    }

    public ArrayList<Integer> get2ChildPtrs(){
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i <= max_i; i++){
            if(getNumChildren(i) == 2){
                result.add(i);
            }
        }
        return result;
    }

    /**
     * This tree is output into the form of python array which is suitable to be graphed using python
     */
    public void printArray(){
        System.out.print("[");
        for(int i = 0; i <= max_i; i++){
            if(tree[i] == null){
                System.out.print("None, ");
            }else{
                System.out.print("\"" + tree[i].toString() + "\", ");
            }
        }
        System.out.println("]");
    }

    public void printOpCounts() {
        System.out.println(opCounts);
    }


    public String toQuery(){
        return toQueryHelper(0);
    }

    public String toQueryHelper(int n_i){
        QueryTreeNode n = get(n_i);
        StringBuilder sb = new StringBuilder();
        sb.append(n.getOperator());
        sb.append("<");
        for(int i = 0; i < n.num_parameters; i++){
            sb.append(n.parameters[i]);
            sb.append(" ");
        }
        // Remove tail space
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ') {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(">");
        if(n.num_children == 0){
            return sb.toString();
        }else if(n.num_children == 1){
            sb.append("(");
            sb.append(toQueryHelper(getLeftChildIndex(n_i)));
            sb.append(")");
            return sb.toString();
        }else{
            sb.append("(");
            sb.append(toQueryHelper(getLeftChildIndex(n_i)));
            sb.append(",");
            sb.append(toQueryHelper(getRightChildIndex(n_i)));
            sb.append(")");
            return sb.toString();
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QueryTree other = (QueryTree) obj;
        for (int i = 0; i <= Math.max(this.max_i, other.max_i); i++) {
            if (this.tree[i] == null && other.tree[i] != null || this.tree[i] != null && !this.tree[i].opertaror.equals(other.tree[i].opertaror)) {
                return false;
            }
        }
        return true;
    }
}
