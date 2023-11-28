package synthesizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Program {
    ArrayList<Stmt> stmts;
    // Being the unit of the large cost of a statement, typically the cost of input Tree.
    int largeCost;
    int num_swap = 0;
    // Count op already changed, need to make sure the number of change operations in the whole solution wouldn't be over the limit (op_to_change);
    Map<String, Integer> changed_op = new HashMap<String, Integer>();
    
    /** 
     * Construct an empty program with large cost.
     * @param largeCost is the unit of large cost for program cost, typically the cost of input Tree.
     */
    public Program(int largeCost) {
        stmts = new ArrayList<Stmt>();
        this.largeCost = largeCost;
        this.num_swap = 0;
        this.changed_op = new HashMap<String, Integer>();
    }

    public Program(ArrayList <Stmt> stmts, int largeCost) {
        this.stmts = stmts;
        this.largeCost = largeCost;
        compute();
    }

    public Program(Program otProgram) {
        stmts = new ArrayList<>(otProgram.stmts);
        this.largeCost = otProgram.largeCost;
        this.num_swap = otProgram.num_swap;
        this.changed_op = new HashMap<>(otProgram.changed_op);
    }

    public boolean isEmpty() {
        return stmts.isEmpty();
    }

    public Stmt get(int index) {
        return stmts.get(index);
    }

    public Stmt getLast() {
        return stmts.get(stmts.size() - 1);
    }

    public void add(Stmt stmt) {
        stmts.add(stmt);
    }

    /**
     * Recompute the cost and op changed for this program so far.
     */
    public void compute(){
        num_swap = 0;
        // Define Program Cost as adding up the large cost for each SwapStmt.
        for(Stmt stmt: stmts){
            if(stmt instanceof SwapStmt){
                num_swap += largeCost;
            }
            // Compute ChageStmt already changed
            if(stmt instanceof ChangeStmt){
                ChangeStmt cs = (ChangeStmt) stmt;
                changed_op.put(cs.operator, changed_op.getOrDefault(cs.operator, 0) + 1);
            }
        }
    }

    public void addAndUpdate(Stmt stmt){
        stmts.add(stmt);
        if(stmt instanceof SwapStmt){
            num_swap ++;
        }
        if(stmt instanceof ChangeStmt){
            ChangeStmt cs = (ChangeStmt) stmt;
            changed_op.put(cs.operator, changed_op.getOrDefault(cs.operator, 0) + 1);
        }
    }

    public void removeLastAndUpdate(){
        Stmt stmt = stmts.remove(stmts.size()-1);
        if(stmt instanceof SwapStmt){
            num_swap --;
        }
        if(stmt instanceof ChangeStmt){
            ChangeStmt cs = (ChangeStmt) stmt;
            changed_op.put(cs.operator, changed_op.getOrDefault(cs.operator, 0) - 1);
        }
    }

    public int getProgramCost(){
        return num_swap * largeCost;
    }

    public int getNum_swap() {
        return num_swap;
    }

    public Map<String, Integer> getChanged_op() {
        return changed_op;
    }

    @Override
    public String toString() {
        String s = "{";
        for (Stmt stmt : stmts) {
            s += (stmt.toString() + " ");
        }
        s.trim();
        s+="}";
        return s;
    }






}
