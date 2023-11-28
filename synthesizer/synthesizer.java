package synthesizer;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import querytree.QueryTreeNode;
import querytree.QueryTreeParser;
import querytree.QueryTree;


/**
 * searchEntry
 */
class SearchEntry {
    int d;
    Program p;
    QueryTree T;

    SearchEntry(int d, Program p, QueryTree T){
        this.d = d;
        this.p = p;
        this.T = T;
    }
}


public class synthesizer {

    Comparator<SearchEntry> comparator = new Comparator<SearchEntry>() {
        @Override
        public int compare(SearchEntry o1, SearchEntry o2) {
            return Integer.compare(o1.d, o2.d);
        }
    };

    PriorityQueue<SearchEntry> queue;
    QueryTree T_in;
    QueryTree T_out;
    int max_i;
    ArrayList<Integer> struct_diff_in = new ArrayList<Integer>();
    ArrayList<Integer> struct_diff_out = new ArrayList<Integer>();
    // op_to_change: op_name -> op_count stored the number of an operator in the target tree that outnumbered the number of the same operator in the input tree
    Map<String, Integer> op_to_change = new HashMap<String, Integer>();
    //ArrayList<String> op_to_change = new ArrayList<String>();
    Map<String, Integer> T_out_opcount;

    public synthesizer(QueryTree T_in, QueryTree T_out) {
        this.T_in = T_in;
        this.T_out = T_out;

        max_i = Math.max(T_in.getMaxIndex(), T_out.getMaxIndex());
        for(int i=0;i<=max_i;i++){
            if(T_in.get(i) == null && T_out.get(i) != null){
                struct_diff_out.add(i);
            }
            if(T_in.get(i) != null && T_out.get(i) == null){
                struct_diff_in.add(i);
            }
        }

        T_out_opcount = T_out.getOpCounts();

        for (Map.Entry<String, Integer> op_entry : T_out_opcount.entrySet()) {
            if (op_entry.getValue() > T_in.getOpCounts().getOrDefault(op_entry.getKey(),0)) {
                // op_entry.getKey() -> Tout op name
                // op_entry.getValue() -> Tout op count
                op_to_change.put(op_entry.getKey(), op_entry.getValue() - T_in.getOpCounts().getOrDefault(op_entry.getKey(),0));
            }
        }
    }

    public synthesizer(String T_in_s, String T_out_s) {
        this(QueryTreeParser.parse_ArrayBasedTree(T_in_s), QueryTreeParser.parse_ArrayBasedTree(T_out_s));
    }

    /**
     * Get the distance metric between T and T_out (the target tree)
     * @param T
     * @return
     */
    public int computeCost(QueryTree T, Program p, boolean verbose){
        int cost = 0;

        // Calculuate Structural Cost
        for(int i : struct_diff_in){
            if(T.get(i)!=null){cost++;}
        }
        for(int i : struct_diff_out){
            if(T.get(i)==null){cost++;}
        }

        int struct_cost = cost;
       

        // Calculate Operator Cost
        Map<String,Integer> T_op_count = T.getOpCounts();
        for (Map.Entry<String, Integer> op_entry : T_op_count.entrySet()) {
            cost += Math.abs(op_entry.getValue() - T_out_opcount.getOrDefault(op_entry.getKey(),0));
        }

        int program_cost = (p == null) ? 0 : p.getProgramCost();

        if(verbose){
            System.out.println("Query: " + T.toQuery() + ", Structural Cost: " + struct_cost + ", Operator Cost: " + (cost - struct_cost) + ", Total Cost: " + cost + ", Program Cost: " + program_cost);
        }

        // Calculate Program Cost
        cost += program_cost;


        return cost;
    }
    

    /**
     * Enumerate all possible stmts for current given tree, stmts are promised to be valid
     * @param T
     * @param p existing stmts so far. This could make sure the number of change operations in the whole solution wouldn't be over the limit. And need it to limit size of "swap"
     * @return
     */
    public ArrayList<Stmt> enumerate(QueryTree T, Program p){
        ArrayList<Stmt> stmts = new ArrayList<Stmt>();
        // Enumerate
        ArrayList<Integer> T_1ChildPtrs = T.get1ChildPtrs();
        ArrayList<Integer> T_2ChildPtrs = T.get2ChildPtrs();

        // Count op already changed, need to make sure the number of change operations in the whole solution wouldn't be over the limit (op_to_change);
        // Map<String, Integer> changed_op = new HashMap<String, Integer>();
        // int num_swap = 0;
        // for(Stmt s : p){
        //     if(s instanceof ChangeStmt){
        //         ChangeStmt cs = (ChangeStmt) s;
        //         changed_op.put(cs.operator, changed_op.getOrDefault(cs.operator, 0) + 1);
        //     }
        //     if(s instanceof SwapStmt){
        //         num_swap++;
        //     }
        // }

        Map<String, Integer> changed_op = p.getChanged_op();

        // Check if there is any op that has been changed too many times, remove them and the ramaining op are valid to change.
        Set<String> valid_op_to_change = op_to_change.keySet();
        for (Map.Entry<String, Integer> op_entry : changed_op.entrySet()) {
            if (op_entry.getValue() >= op_to_change.getOrDefault(op_entry.getKey(),0)) {
                valid_op_to_change.remove(op_entry.getKey());
            }
        }
    
        // Enumerate Change
        // Change_1
        for(int ptr : T_1ChildPtrs){
            for(String op : valid_op_to_change){
                if(QueryTreeNode.OperatorToNumChildren.get(op) != 1){
                    continue;
                }
                Stmt s = new ChangeStmt(ptr, 1, op);
                if(s.validCheck(T)){
                    stmts.add(s);
                }
            }
        }
        // Change_2
        for(int ptr : T_2ChildPtrs){
            for(String op : valid_op_to_change){
                if(QueryTreeNode.OperatorToNumChildren.get(op) != 2){
                    continue;
                }
                Stmt s = new ChangeStmt(ptr, 2, op);
                if(s.validCheck(T)){
                    stmts.add(s);
                }
            }
        }

        // Enumerate Remove
        for(int ptr : T_1ChildPtrs){
            // Remove_L
            Stmt s = new RemoveStmt(ptr, false);
            if(s.validCheck(T)){
                stmts.add(s);
            }
        }
        for(int ptr : T_2ChildPtrs){
            // Remove_L
            Stmt s = new RemoveStmt(ptr, false);
            if(s.validCheck(T)){
                stmts.add(s);
            }
            // Remove_R
            s = new RemoveStmt(ptr, true);
            if(s.validCheck(T)){
                stmts.add(s);
            }
        }

        // Enumerate Swap when there isn't too many
        if(p.getNum_swap() <= T_out.getSize()){
            // Swap_1
            for (int ptr_1: T_1ChildPtrs){
                for (int ptr_2: T_1ChildPtrs){
                    Stmt s = new SwapStmt(ptr_1, ptr_2, 1);
                    if(s.validCheck(T)){
                        stmts.add(s);
                    }
                }
            }
            // Swap_2
            for(int ptr_1 : T_2ChildPtrs){
                for (int ptr_2: T_2ChildPtrs){
                    Stmt s = new SwapStmt(ptr_1, ptr_2, 2);
                    if(s.validCheck(T)){
                        stmts.add(s);
                    }
                }
            }
        }
       
    
        return stmts;
    }  
    
    
    public Program synthesize(boolean verbose){

        queue = new PriorityQueue<SearchEntry>(comparator);
        int num_d_0 = 0;
        int num_loop = 0;
    
        int large_cost = computeCost(T_in, null, verbose);

        SearchEntry init_entry = new SearchEntry(large_cost, new Program(large_cost), T_in);

        queue.add(init_entry);

        while(!queue.isEmpty()){
            num_loop++;
            SearchEntry entry = queue.poll();

            if(entry.d == 0){
                // d = 0, (maybe) find a solution (not include the last most stmt in this program), but still need to check if the entire tree is equal.
                num_d_0++;
                if(entry.T.equals(T_out)) {
                    // Append the end stmt
                    if(!entry.p.isEmpty()){
                        entry.p.removeLastAndUpdate();
                    }
                    entry.p.addAndUpdate(new EndStmt(num_d_0, num_loop));
                    return entry.p;
                }
            }

            if(!entry.p.isEmpty()){
                Stmt s_new = entry.p.getLast();
                s_new.transform_tree(entry.T);

                if(verbose){
                    System.out.println("Executed Stmt: " + entry.p.toString() + ", -> " + entry.T.toQuery());
                }
            }
                
            if(entry.T.getSize() < T_out.getSize()){
                // Prune away, this tree is too small cannot form the target tree
                continue;
            }

            extend(entry,queue,verbose);
        }

        // No Solution found;
        //System.out.println("No Solution Found");
        return null;
    }

    public void extend(SearchEntry entry, PriorityQueue<SearchEntry> queue, boolean verbose){
        int d = computeCost(entry.T, entry.p, verbose);
        ArrayList<Stmt> stmts = enumerate(entry.T, entry.p);

        for (Stmt stmt : stmts) {
            // ArrayList<Stmt> new_stmts = new ArrayList<Stmt>(entry.stmts);        new_stmts.add(stmt);
            Program new_p = new Program(entry.p);
            new_p.addAndUpdate(stmt);
            QueryTree new_T = new QueryTree(entry.T);
            queue.add(new SearchEntry(d, new_p, new_T));
        }
    }





    
}
