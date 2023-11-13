import querytree.QueryTreeNode;
import querytree.QueryTreeParser;

import java.util.*;



import lib.TreePrinter;





public class test {

    public static void main(String[] args) {

        QueryTreeNode node = new QueryTreeNode("Sel", "a1 a2");

        //QueryTreeParser.parse("Sel<p r.a0>(Proj<r.a1>(Input<r>))");

        QueryTreeNode t = QueryTreeParser.parse("Proj<r0.a2>(IJoin<r0.a0 r.a>(Input<r0>,Dedup<>(Proj<r1.a1>(Input<r1>))))");
        System.out.println(t);

        QueryTreeNode t_out = QueryTreeParser.parse("Proj<r0.a2>(InSub<r0.a0>(Input<r0>,Proj<r1.a1>(Input<r1>)))");

        TreePrinter.print(t);
        TreePrinter.print(t_out);
        
        
        //System.out.println(node);
    }




}
