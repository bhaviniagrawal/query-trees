package synthesizer;

import querytree.QueryTree;

public class EndStmt extends Stmt {
    int num_d_0;
    int num_loop;

    EndStmt(int num_d_0, int num_loop){
        this.num_d_0 = num_d_0;
        this.num_loop = num_loop;
    }

    @Override
    public void transform_tree(QueryTree tree) {
        return;
    }


    @Override
    public String toString() {
        return "This synthesis tree did (" + num_d_0 + ") guess check of 0 cost and (" + num_loop + ") loop(s)";
    }

}
