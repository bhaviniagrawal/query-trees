package querytree;

public class QueryTree {
    private QueryTreeNode[] tree;
    private int capacity;
    private int max_i = 0;

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


    public QueryTreeNode get(int i){
        if(i < 0 || i > max_i){
            return null;
        }
        return tree[i];
    }

    public QueryTreeNode set(int i, QueryTreeNode node){
        max_i = Math.max(max_i, i);
        QueryTreeNode old = tree[i];
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



    private int getIndex(QueryTreeNode node) {
        for (int i = 0; i < capacity; i++) {
            if (tree[i] == node) {
                return i;
            }
        }
        throw new IllegalArgumentException("Node not found in tree");
    }
}
