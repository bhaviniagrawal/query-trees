import querytree.QueryTreeNode;
import querytree.QueryTreeParser;
import querytree.QueryTree;

import java.util.*;



import lib.TreePrinter;





public class test {


    public static void draw_both(String[] src_tgt_template){
       String src = src_tgt_template[0];
       String tgt = src_tgt_template[1];

       QueryTree src_tree = QueryTreeParser.parse_ArrayBasedTree(src);
       QueryTree tgt_tree = QueryTreeParser.parse_ArrayBasedTree(tgt);

        TreePrinter.print(src_tree);
        TreePrinter.print(tgt_tree);
    }

    public static void main(String[] args) {

        QueryTreeNode node = new QueryTreeNode("Sel", "a1 a2");

        //QueryTreeParser.parse("Sel<p r.a0>(Proj<r.a1>(Input<r>))");

        QueryTreeNode t = QueryTreeParser.parse("Proj<r0.a2>(IJoin<r0.a0 r.a>(Input<r0>,Dedup<>(Proj<r1.a1>(Input<r1>))))");

        QueryTreeNode t_out = QueryTreeParser.parse("Proj<r0.a2>(InSub<r0.a0>(Input<r0>,Proj<r1.a1>(Input<r1>)))");
        QueryTree q = QueryTreeParser.parse_ArrayBasedTree("Proj<r0.a2>(InSub<r0.a0>(Input<r0>,Proj<r1.a1>(Input<r1>)))");
        //TreePrinter.print(q);

        // Sample Wetune Rules.
        String[][] wetune_rules = {
            {"",""},
            {"Sel<p r.a0>(Proj<r.a1>(Input<r>))","Proj<r.a1>(Sel<p r.a0>(Input<r>))"},
            {"Dedup(Proj<r.a>(Input<r>))", "Proj<r.a>(Input<r>)"},
            {"Sel<p r.a>(Sel<p r.a>(Input<r>))", "Sel<p r.a>(Input<r>)"},
            {"InSub<r0.a0>(InSub<r0.a0>(Input<r0>, Input<r1>), Input<r1>)", "InSub<r0.a0>(Input<r0>, Input<r1>)"},
            {"Proj<r.a0>(Sel<p r.a1>(Proj<r.a2>(Input<r>)))", "Proj<r.a0>(Sel<p r.a1>(Input<r>))"},
            {"LJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>)", "IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>)"},
            {"Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))", "Proj<r0.a2>(Input<r0>)"},
            {"Proj<r0.a2>(Sel<p r0.a3>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>)))", "Proj<r0.a2>(Sel<p r0.a3>(Input<r0>))"},
            {"Dedup(Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>)))", "Dedup(Proj<r0.a2>(Input<r0>))"},
            {"Dedup(Proj<r0.a2>(Sel<p r0.a3>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))))", "Dedup(Proj<r0.a2>(Sel<p r0.a3>(Input<r0>)))"},
            {"Proj<r0.a2>(LJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))", "Proj<r0.a2>(Input<a0>)"},
            {"Proj<r0.a3>(Sel<p r0.a2>(LJoin<r0.a0 r1.a1>(Input<r0>,Input<r1>)))", "Proj<r0.a3>(Sel<p r0.a2>(Input<r0>))"},
            {"Dedup(Proj<r0.a2>(LJoin<r0.a0 t1.r2>(Input<r0>, Input<r1>)))", "Dedup(Proj<r0.a2>(Input<a0>))"},
            {"Dedup(Proj<r0.a3>(Sel<p r0.a2>(LJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))))", "Dedup(Proj<r0.a3>(Sel<p r0.a2>(Input<r0>)))"},
            {"InSub<r.a>(Input<r>, Proj<r'.a>(Input<r'>))", "Input<r>"},
            {"Proj<r.a>(IJoin<r.a r'.a>(Input<r>, Input<r'>))", "Proj<r.a>(Input<r>)"},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"IJoin<r0.a0 r1.a1>(Input<r0>, IJoin<r1.a2 r2.a3>(Input<r1>, Input<r2>))", "IJoin<r1.a2 r2.a3>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>), Input<r2>)"},
            {"Proj<r0.a2>(InSub<r0.a0>(Input<r0>, Proj<r1.a1>(Input<r1>)))", "Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))"},
            {"Proj<r0.a2>(IJoin<r0.a0 r.a>(Input<r0>,Dedup(Proj<r1.a1>(Input<r1>))))", "Proj<r0.a2>(InSub<r0.a0>(Input<r0>,Proj<r1.a1>(Input<r1>)))"},
            {"Dedup(Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Dedup(Input<r1>))))", "Dedup(Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>)))"},
            {"IJoin<r0.a0 r1.a1>(Input<r0>, Sel<p r1.a2>(Input<r1>))", "Sel<p r1.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))"},
            {"Sel<p r1.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>))", "IJoin<r0.a0 r1.a1>(Input<r0>, Sel<p r1.a2>(Input<r1>))"},
            {"Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Proj<r1.a1>(Input<r1>))))", "Proj<r0.a2>(IJoin<r0.a0 r1.a1>(Input<r0>, Input<r1>)))"},
            {"Sel<p r.a0>(IJoin<r.a1 r'.a1>(Input<r>, Input<r'>))", "Sel<p r'.a0>(IJoin<r.a1 r'.a1>(Input<r>, Input<r'>))"},
            {"Proj<r0.a0>(LJoin<r0.a1 r1.a2>(Proj<r0.a3>(Input<r0>), Input<r1>))", "Proj<r0.a0>(LJoin<r0.a1 r1.a2>(Input<r0>, Input<r1>))"},
            {"Proj<r0.a0>(LJoin<r0.a1 r1.a2>(Input<r0>, Proj<r1.a3>(Input<r1>)))", "Proj<r0.a0>(LJoin<r0.a1 r1.a2>(Input<r0>, Input<r1>))"}
        };

        // for(String[] rule : wetune_rules){
        //     draw_both(rule);
        // }

        draw_both(wetune_rules[32]);


    }




}
