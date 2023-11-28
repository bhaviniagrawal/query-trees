import querytree.QueryTreeNode;
import querytree.QueryTreeParser;
import querytree.QueryTree;
import synthesizer.synthesizer;
import synthesizer.Stmt;
import synthesizer.ChangeStmt;
import synthesizer.Program;
import synthesizer.RemoveStmt;

import java.util.*;

import javax.swing.plaf.synth.SynthStyle;

import lib.TreePrinter;





public class test {

    static String[][] wetune_rules = {
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


    public static void draw_both(String[] src_tgt_template){
       String src = src_tgt_template[0];
       String tgt = src_tgt_template[1];

       
       QueryTree src_tree = QueryTreeParser.parse_ArrayBasedTree(src);
       QueryTree tgt_tree = QueryTreeParser.parse_ArrayBasedTree(tgt);

        System.out.println("src: ");
        TreePrinter.print(src_tree);
                System.out.println("");
        System.out.println("tgt: ");
        TreePrinter.print(tgt_tree);
    }


    public static void array_both(String[] src_tgt_template){
       String src = src_tgt_template[0];
       String tgt = src_tgt_template[1];

       
       QueryTree src_tree = QueryTreeParser.parse_ArrayBasedTree(src);
       QueryTree tgt_tree = QueryTreeParser.parse_ArrayBasedTree(tgt);

        System.out.println("src: ");
        src_tree.printArray();
                System.out.println("");
        System.out.println("tgt: ");
        tgt_tree.printArray();
    }


    public static void main(String[] args) {

        QueryTreeNode node = new QueryTreeNode("Sel", "a1 a2");

        //QueryTreeParser.parse("Sel<p r.a0>(Proj<r.a1>(Input<r>))");

        //TreePrinter.print(q);

    



        //draw_both(wetune_rules[24]);
        


        // QueryTree gpt_tree = QueryTreeParser.parse_ArrayBasedTree("IJoin<r0.a0 r3.a2>(LJoin<r0.a2 r0.a0>(InSub<r1.a0>(Input<r2.a2>, Input<r2.a3>), Proj<r3.a1>(Input<r2.a0>)), RJoin<r3.a1 r2.a3>(Dedup<r3.a2>(Input<r0.a0>), IJoin<r0.a1 r3.a2>(Input<r3.a3>, Input<r3.a3>)))");
        // System.out.println(gpt_tree.toQuery());
        // QueryTree gpt_tree_2 = QueryTreeParser.parse_ArrayBasedTree("IJoin<r0.a0 r3.a2>(LJoin<r0.a2 r0.a0>(InSub<r1.a0>(Input<r2.a2>, Input<r2.a3>), Proj<r3.a1>(Input<r2.a0>)), RJoin<r3.a1 r2.a3>(Dedup<r3.a2>(Input<r0.a0>), IJoin<r0.a1 r3.a2>(Input<r3.a3>, Input<r3.a3>)))");
       
        // //TreePrinter.print(gpt_tree);
        // gpt_tree.printArray();
        // System.out.println(gpt_tree.getSize());
        // gpt_tree.moveUp(6,10);
        // System.out.println(gpt_tree.getSize());
        // gpt_tree.printArray();

        // ChangeStmt change_stmt = new ChangeStmt(1, 2, "IJoin");


        // change_stmt.transform_tree(gpt_tree);
        // gpt_tree.printArray();

        // array_both(wetune_rules[24]);

        
     
   

       for(int i = 0; i < wetune_rules.length; i++){
            syn_test(i, false);
        }

    
    
    }

    public static void syn_test(int i,boolean verbose){
        if(wetune_rules[i][0].equals("")) return;


        synthesizer syn = new synthesizer(wetune_rules[i][0], wetune_rules[i][1]);
        Program p = syn.synthesize(verbose);
        System.out.print(i + ": ");
        if(p == null){
            System.out.println("no solution found");
            return;
        }else{
            System.out.println(p);
        }

    }



}
