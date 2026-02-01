package ir;

import java.util.*;
import temp.*;

public class Liveness {

    // -------------------------------------------------------------------------
    // Inner Class: Liveness CFG Node
    // -------------------------------------------------------------------------
    private static class LiveNode {
        IrCommand command;
        int id;

        // CFG Edges
        List<LiveNode> succ = new ArrayList<>();
        List<LiveNode> pred = new ArrayList<>();

        // Dataflow Sets
        Set<Temp> in = new HashSet<>();
        Set<Temp> out = new HashSet<>();
        Set<Temp> gen = new HashSet<>();
        Set<Temp> kill = new HashSet<>();

        public LiveNode(int id, IrCommand command) {
            this.id = id;
            this.command = command;
        }
    }

    // -------------------------------------------------------------------------
    // Main Analysis Method
    // -------------------------------------------------------------------------
    public static Map<IrCommand, Set<Temp>> analyze(Ir ir) {
        if (ir == null) return new HashMap<>();

        // 1. Flatten IR to Nodes
        List<LiveNode> nodes = buildNodeList(ir);

        // 2. Build CFG (Edges)
        buildControlFlowEdges(nodes);

        // 3. Compute Gen/Kill Sets
        for (LiveNode node : nodes) {
            computeGenKill(node);
        }

        // 4. Run Backward Liveness Analysis (Fixed Point Iteration)
        runDataflow(nodes);

        // 5. Map results back to IrCommands (We return OUT sets usually,
        //    but for register allocation, we typically care about 'Interference'
        //    which is defined by the Liveness at the specific point.
        //    Here we map Command -> The Set of Temps live *Immediately After* it (Out)
        //    OR *Immediately Before* it (In).
        //    Standard Register Allocation uses the 'In' set of the *next* instruction
        //    (which is the 'Out' of current) + the defined variable of current.
        //    For simplicity, we return the 'Out' set: variables live AFTER this command.
        Map<IrCommand, Set<Temp>> result = new HashMap<>();
        for (LiveNode node : nodes) {
            result.put(node.command, node.out);
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Helper: Linearize IR
    // -------------------------------------------------------------------------
    private static List<LiveNode> buildNodeList(Ir ir) {
        List<LiveNode> nodes = new ArrayList<>();
        int idCounter = 0;

        // Handle head
        if (ir.head != null) {
            nodes.add(new LiveNode(idCounter++, ir.head));
        }

        // Handle tail list
        IrCommandList current = ir.tail;
        while (current != null) {
            if (current.head != null) {
                nodes.add(new LiveNode(idCounter++, current.head));
            }
            current = current.tail;
        }
        return nodes;
    }

    // -------------------------------------------------------------------------
    // Helper: Build Edges
    // -------------------------------------------------------------------------
    private static void buildControlFlowEdges(List<LiveNode> nodes) {
        Map<String, LiveNode> labelMap = new HashMap<>();

        // 1. Map Labels
        for (LiveNode node : nodes) {
            if (node.command instanceof IrCommandLabel) {
                labelMap.put(((IrCommandLabel) node.command).labelName, node);
            }
        }

        // 2. Add Edges
        for (int i = 0; i < nodes.size(); i++) {
            LiveNode node = nodes.get(i);
            IrCommand cmd = node.command;
            boolean fallsThrough = true;

            // Handle Branching
            if (cmd instanceof IrCommandBranch) {
                String target = ((IrCommandBranch) cmd).labelName;
                LiveNode targetNode = labelMap.get(target);
                if (targetNode != null) addEdge(node, targetNode);
                fallsThrough = false;
            }
            else if (cmd instanceof IrCommandBranchIfZero) {
                String target = ((IrCommandBranchIfZero) cmd).labelName;
                LiveNode targetNode = labelMap.get(target);
                if (targetNode != null) addEdge(node, targetNode);
                fallsThrough = true;
            }
            else if (cmd instanceof IrCommandBranchIfEquals) {
                String target = ((IrCommandBranchIfEquals) cmd).labelName;
                LiveNode targetNode = labelMap.get(target);
                if (targetNode != null) addEdge(node, targetNode);
                fallsThrough = true;
            }
            else if (cmd instanceof IrCommandBranchIfLess) {
                String target = ((IrCommandBranchIfLess) cmd).labelName;
                LiveNode targetNode = labelMap.get(target);
                if (targetNode != null) addEdge(node, targetNode);
                fallsThrough = true;
            }
            else if (cmd instanceof IrCommandBranchIfEqualsStrings) {
                String target = ((IrCommandBranchIfEqualsStrings) cmd).labelName;
                LiveNode targetNode = labelMap.get(target);
                if (targetNode != null) addEdge(node, targetNode);
                fallsThrough = true;
            }
            else if (cmd instanceof IrCommandReturn) {
                fallsThrough = false;
            }

            // Handle Sequential Flow
            if (fallsThrough && i + 1 < nodes.size()) {
                addEdge(node, nodes.get(i + 1));
            }
        }
    }

    private static void addEdge(LiveNode from, LiveNode to) {
        from.succ.add(to);
        to.pred.add(from);
    }

    // -------------------------------------------------------------------------
    // Helper: Compute Gen/Kill
    // -------------------------------------------------------------------------
    private static void computeGenKill(LiveNode node) {
        IrCommand cmd = node.command;
        Set<Temp> gen = node.gen;
        Set<Temp> kill = node.kill;

        // Note: We use specific checks for every IR type

        if (cmd instanceof IrCommandBinopAddIntegers) {
            kill.add(((IrCommandBinopAddIntegers) cmd).dst);
            gen.add(((IrCommandBinopAddIntegers) cmd).t1);
            gen.add(((IrCommandBinopAddIntegers) cmd).t2);
        }
        else if (cmd instanceof IrCommandBinopAddStrings) {
            kill.add(((IrCommandBinopAddStrings) cmd).dst);
            gen.add(((IrCommandBinopAddStrings) cmd).t1);
            gen.add(((IrCommandBinopAddStrings) cmd).t2);
        }
        else if (cmd instanceof IrCommandBinopSubIntegers) {
            kill.add(((IrCommandBinopSubIntegers) cmd).dst);
            gen.add(((IrCommandBinopSubIntegers) cmd).t1);
            gen.add(((IrCommandBinopSubIntegers) cmd).t2);
        }
        else if (cmd instanceof IrCommandBinopMulIntegers) {
            kill.add(((IrCommandBinopMulIntegers) cmd).dst);
            gen.add(((IrCommandBinopMulIntegers) cmd).t1);
            gen.add(((IrCommandBinopMulIntegers) cmd).t2);
        }
        else if (cmd instanceof IrCommandBinopDivIntegers) {
            kill.add(((IrCommandBinopDivIntegers) cmd).dst);
            gen.add(((IrCommandBinopDivIntegers) cmd).t1);
            gen.add(((IrCommandBinopDivIntegers) cmd).t2);
        }
        else if (cmd instanceof IrCommandConstInt) {
            kill.add(((IrCommandConstInt) cmd).t);
        }
        else if (cmd instanceof IrCommandReadVar) {
            kill.add(((IrCommandReadVar) cmd).dst);
        }
        else if (cmd instanceof IrCommandWriteVar) {
            gen.add(((IrCommandWriteVar) cmd).src);
        }
        else if (cmd instanceof IrCommandBranchIfZero) {
            gen.add(((IrCommandBranchIfZero) cmd).t);
        }
        else if (cmd instanceof IrCommandBranchIfEquals) {
            gen.add(((IrCommandBranchIfEquals) cmd).t1);
            gen.add(((IrCommandBranchIfEquals) cmd).t2);
        }
        else if (cmd instanceof IrCommandBranchIfLess) {
            gen.add(((IrCommandBranchIfLess) cmd).t1);
            gen.add(((IrCommandBranchIfLess) cmd).t2);
        }
        else if (cmd instanceof IrCommandBranchIfEqualsStrings) {
            gen.add(((IrCommandBranchIfEqualsStrings) cmd).t1);
            gen.add(((IrCommandBranchIfEqualsStrings) cmd).t2);
        }
        // Branch (unconditional) has no gen/kill
        else if (cmd instanceof IrCommandCall) {
            kill.add(((IrCommandCall) cmd).dst);
            addTempListToSet(((IrCommandCall) cmd).args, gen);
        }
        else if (cmd instanceof IrCommandCallVoid) {
            addTempListToSet(((IrCommandCallVoid) cmd).args, gen);
        }
        else if (cmd instanceof IrCommandVirtualCall) {
            kill.add(((IrCommandVirtualCall) cmd).dst);
            gen.add(((IrCommandVirtualCall) cmd).inst);
            addTempListToSet(((IrCommandVirtualCall) cmd).args, gen);
        }
        else if (cmd instanceof IrCommandVirtualCallVoid) {
            gen.add(((IrCommandVirtualCallVoid) cmd).inst);
            addTempListToSet(((IrCommandVirtualCallVoid) cmd).args, gen);
        }
        else if (cmd instanceof IrCommandReturn) {
            if (((IrCommandReturn) cmd).src != null) {
                gen.add(((IrCommandReturn) cmd).src);
            }
        }
        else if (cmd instanceof IrCommandArrayAccess) {
            kill.add(((IrCommandArrayAccess) cmd).dst);
            gen.add(((IrCommandArrayAccess) cmd).arr);
            gen.add(((IrCommandArrayAccess) cmd).index);
        }
        else if (cmd instanceof IrCommandArraySet) {
            gen.add(((IrCommandArraySet) cmd).src);
            gen.add(((IrCommandArraySet) cmd).arr);
            gen.add(((IrCommandArraySet) cmd).index);
        }
        else if (cmd instanceof IrCommandFieldAccess) {
            kill.add(((IrCommandFieldAccess) cmd).dst);
            gen.add(((IrCommandFieldAccess) cmd).inst);
        }
        else if (cmd instanceof IrCommandFieldSet) {
            gen.add(((IrCommandFieldSet) cmd).src);
            gen.add(((IrCommandFieldSet) cmd).inst);
        }
        else if (cmd instanceof IrCommandNewArray) {
            kill.add(((IrCommandNewArray) cmd).dst);
            gen.add(((IrCommandNewArray) cmd).size);
        }
        else if (cmd instanceof IrCommandNewClass) {
            kill.add(((IrCommandNewClass) cmd).dst);
        }
    }

    private static void addTempListToSet(TempList list, Set<Temp> set) {
        TempList current = list;
        while (current != null) {
            if (current.head != null) {
                set.add(current.head);
            }
            current = current.tail;
        }
    }

    // -------------------------------------------------------------------------
    // Helper: Fixed Point Iteration
    // -------------------------------------------------------------------------
    private static void runDataflow(List<LiveNode> nodes) {
        boolean changed = true;

        // Loop until convergence
        while (changed) {
            changed = false;

            // Iterate in reverse (bottom-up) order for faster convergence
            for (int i = nodes.size() - 1; i >= 0; i--) {
                LiveNode node = nodes.get(i);

                // 1. Compute OUT = Union of IN of all successors
                Set<Temp> newOut = new HashSet<>();
                for (LiveNode succ : node.succ) {
                    newOut.addAll(succ.in);
                }

                // 2. Compute IN = Gen U (Out - Kill)
                Set<Temp> newIn = new HashSet<>(newOut);
                newIn.removeAll(node.kill);
                newIn.addAll(node.gen);

                // 3. Check for change (only checking IN is sufficient for liveness)
                // However, we update both to keep state consistent
                if (!newIn.equals(node.in) || !newOut.equals(node.out)) {
                    node.in = newIn;
                    node.out = newOut;
                    changed = true;
                }
            }
        }
    }
}