package ir;

import java.util.*;
import temp.*;

public class RegisterAllocator {

    public void allocate(Ir ir) {
        // 1. Run Liveness Analysis
        // liveMap maps: IrCommand -> Set of Temps alive immediately AFTER it
        Map<IrCommand, Set<Temp>> liveMap = Liveness.analyze(ir);

        // 2. Build Interference Graph
        InterferenceGraph graph = new InterferenceGraph(ir, liveMap);

        // 3. Stack for Simplification
        Stack<Temp> stack = new Stack<>();

        // --- SIMPLIFY ---
        while (!graph.isEmpty()) {
            Temp node = graph.getNodeWithDegreeLessThan(10);
            if (node != null) {
                stack.push(node);
                graph.removeNode(node);
            } else {
                // Spill detected
                System.out.println("Register Allocation Failed1");
                System.exit(0);
            }
        }

        // --- SELECT (Coloring) ---
        // We maintain a local map of assigned colors during selection
        // to check neighbors, then commit to Temp object.
        Map<Temp, Integer> assignedColors = new HashMap<>();

        while (!stack.isEmpty()) {
            Temp t = stack.pop();
            Set<Temp> neighbors = graph.getOriginalNeighbors(t);

            // Find used colors by neighbors
            Set<Integer> usedColors = new HashSet<>();
            for (Temp n : neighbors) {
                if (assignedColors.containsKey(n)) {
                    usedColors.add(assignedColors.get(n));
                }
            }

            // Pick lowest available color 0-9
            int chosen = -1;
            for (int i = 0; i < 10; i++) {
                if (!usedColors.contains(i)) {
                    chosen = i;
                    break;
                }
            }

            if (chosen == -1) {
                System.out.println("Register Allocation Failed2");
                System.exit(0);
            }

            assignedColors.put(t, chosen);

            // UPDATE THE TEMP OBJECT DIRECTLY
            t.setRegIndex(chosen);
        }
    }

    // -------------------------------------------------------------------------
    // Inner Class: Interference Graph
    // -------------------------------------------------------------------------
    private static class InterferenceGraph {
        private Map<Temp, Set<Temp>> adj = new HashMap<>();
        private Map<Temp, Set<Temp>> originalAdj = new HashMap<>();
        private Map<Temp, Integer> degrees = new HashMap<>();

        public InterferenceGraph(Ir ir, Map<IrCommand, Set<Temp>> liveMap) {
            // Initialize graph nodes for ALL temps appearing in the program
            // We scan the IR manually to ensure even temps that are never live (dead code)
            // exist in the graph (degree 0) so they get allocated a register.
            collectAllTemps(ir);

            // Build edges based on Liveness
            // Rule: If temp D is defined at instruction I, it interferes with
            // all temps currently live (OUT[I]).
            // Note: Since Liveness.analyze returns OUT sets, we iterate commands
            // and find what is DEFINED (Killed) there.

            IrCommandList list = ir.tail;
            List<IrCommand> commands = new ArrayList<>();
            if (ir.head != null) commands.add(ir.head);
            while(list != null) { if(list.head!=null) commands.add(list.head); list=list.tail; }

            for (IrCommand cmd : commands) {
                Temp def = getDef(cmd);
                if (def != null) {
                    Set<Temp> liveOut = liveMap.get(cmd);
                    if (liveOut != null) {
                        for (Temp live : liveOut) {
                            // A variable does not interfere with itself
                            if (def != live) {
                                addEdge(def, live);
                            }
                        }
                    }
                }
            }
        }

        private void collectAllTemps(Ir ir) {
            // 1. Flatten the IR list to get all commands
            List<IrCommand> commands = new ArrayList<>();
            if (ir.head != null) {
                commands.add(ir.head);
            }
            
            IrCommandList current = ir.tail;
            while (current != null) {
                if (current.head != null) {
                    commands.add(current.head);
                }
                current = current.tail;
            }

            // 2. Iterate over every command and ensure every Temp is in the graph
            for (IrCommand cmd : commands) {
                // Add Definition (Write)
                Temp def = getDef(cmd);
                if (def != null) {
                    initNode(def);
                }

                // Add Uses (Reads)
                Set<Temp> uses = getUses(cmd);
                for (Temp t : uses) {
                    initNode(t);
                }
            }
        }

        // Helper to extract all used variables from a command
        private Set<Temp> getUses(IrCommand cmd) {
            Set<Temp> uses = new HashSet<>();
            
            if (cmd instanceof IrCommandBinopAddIntegers) {
                uses.add(((IrCommandBinopAddIntegers) cmd).t1);
                uses.add(((IrCommandBinopAddIntegers) cmd).t2);
            }
            else if (cmd instanceof IrCommandBinopAddStrings) {
                uses.add(((IrCommandBinopAddStrings) cmd).t1);
                uses.add(((IrCommandBinopAddStrings) cmd).t2);
            }
            else if (cmd instanceof IrCommandBinopSubIntegers) {
                uses.add(((IrCommandBinopSubIntegers) cmd).t1);
                uses.add(((IrCommandBinopSubIntegers) cmd).t2);
            }
            else if (cmd instanceof IrCommandBinopMulIntegers) {
                uses.add(((IrCommandBinopMulIntegers) cmd).t1);
                uses.add(((IrCommandBinopMulIntegers) cmd).t2);
            }
            else if (cmd instanceof IrCommandBinopDivIntegers) {
                uses.add(((IrCommandBinopDivIntegers) cmd).t1);
                uses.add(((IrCommandBinopDivIntegers) cmd).t2);
            }
            else if (cmd instanceof IrCommandWriteVar) {
                uses.add(((IrCommandWriteVar) cmd).src);
            }
            else if (cmd instanceof IrCommandBranchIfZero) {
                uses.add(((IrCommandBranchIfZero) cmd).t);
            }
            else if (cmd instanceof IrCommandBranchIfEquals) {
                uses.add(((IrCommandBranchIfEquals) cmd).t1);
                uses.add(((IrCommandBranchIfEquals) cmd).t2);
            }
            else if (cmd instanceof IrCommandBranchIfLess) {
                uses.add(((IrCommandBranchIfLess) cmd).t1);
                uses.add(((IrCommandBranchIfLess) cmd).t2);
            }
            else if (cmd instanceof IrCommandBranchIfEqualsStrings) {
                uses.add(((IrCommandBranchIfEqualsStrings) cmd).t1);
                uses.add(((IrCommandBranchIfEqualsStrings) cmd).t2);
            }
            else if (cmd instanceof IrCommandCall) {
                addTempListToSet(((IrCommandCall) cmd).args, uses);
            }
            else if (cmd instanceof IrCommandCallVoid) {
                addTempListToSet(((IrCommandCallVoid) cmd).args, uses);
            }
            else if (cmd instanceof IrCommandVirtualCall) {
                uses.add(((IrCommandVirtualCall) cmd).inst);
                addTempListToSet(((IrCommandVirtualCall) cmd).args, uses);
            }
            else if (cmd instanceof IrCommandVirtualCallVoid) {
                uses.add(((IrCommandVirtualCallVoid) cmd).inst);
                addTempListToSet(((IrCommandVirtualCallVoid) cmd).args, uses);
            }
            else if (cmd instanceof IrCommandReturn) {
                if (((IrCommandReturn) cmd).src != null) uses.add(((IrCommandReturn) cmd).src);
            }
            else if (cmd instanceof IrCommandArrayAccess) {
                uses.add(((IrCommandArrayAccess) cmd).arr);
                uses.add(((IrCommandArrayAccess) cmd).index);
            }
            else if (cmd instanceof IrCommandArraySet) {
                uses.add(((IrCommandArraySet) cmd).src);
                uses.add(((IrCommandArraySet) cmd).arr);
                uses.add(((IrCommandArraySet) cmd).index);
            }
            else if (cmd instanceof IrCommandFieldAccess) {
                uses.add(((IrCommandFieldAccess) cmd).inst);
            }
            else if (cmd instanceof IrCommandFieldSet) {
                uses.add(((IrCommandFieldSet) cmd).src);
                uses.add(((IrCommandFieldSet) cmd).inst);
            }
            else if (cmd instanceof IrCommandNewArray) {
                uses.add(((IrCommandNewArray) cmd).size);
            }
            else if (cmd instanceof IrCommandGetThis){
                uses.add(((IrCommandGetThis) cmd).dst);
            }
            // ConstInt, ReadVar, NewClass, Labels, Branch have no uses.
            
            return uses;
        }

        private void addTempListToSet(TempList list, Set<Temp> set) {
            TempList current = list;
            while (current != null) {
                if (current.head != null) {
                    set.add(current.head);
                }
                current = current.tail;
            }
        }

        private void addEdge(Temp u, Temp v) {
            if (!adj.containsKey(u)) initNode(u);
            if (!adj.containsKey(v)) initNode(v);

            if (adj.get(u).add(v)) {
                originalAdj.get(u).add(v);
                degrees.put(u, degrees.get(u) + 1);
            }
            if (adj.get(v).add(u)) {
                originalAdj.get(v).add(u);
                degrees.put(v, degrees.get(v) + 1);
            }
        }

        private void initNode(Temp t) {
            adj.putIfAbsent(t, new HashSet<>());
            originalAdj.putIfAbsent(t, new HashSet<>());
            degrees.putIfAbsent(t, 0);
        }

        public boolean isEmpty() {
            return adj.isEmpty();
        }

        public Temp getNodeWithDegreeLessThan(int k) {
            for (Map.Entry<Temp, Integer> entry : degrees.entrySet()) {
                if (entry.getValue() < k) return entry.getKey();
            }
            return null;
        }

        public void removeNode(Temp t) {
            Set<Temp> neighbors = adj.get(t);
            if (neighbors != null) {
                for (Temp n : neighbors) {
                    Set<Temp> nAdj = adj.get(n);
                    if (nAdj != null && nAdj.remove(t)) {
                        degrees.put(n, degrees.get(n) - 1);
                    }
                }
            }
            adj.remove(t);
            degrees.remove(t);
        }

        public Set<Temp> getOriginalNeighbors(Temp t) {
            return originalAdj.getOrDefault(t, Collections.emptySet());
        }

        private Temp getDef(IrCommand cmd) {
            if (cmd instanceof IrCommandBinopAddIntegers) return ((IrCommandBinopAddIntegers)cmd).dst;
            if (cmd instanceof IrCommandBinopAddStrings) return ((IrCommandBinopAddStrings)cmd).dst;
            if (cmd instanceof IrCommandBinopSubIntegers) return ((IrCommandBinopSubIntegers)cmd).dst;
            if (cmd instanceof IrCommandBinopMulIntegers) return ((IrCommandBinopMulIntegers)cmd).dst;
            if (cmd instanceof IrCommandBinopDivIntegers) return ((IrCommandBinopDivIntegers)cmd).dst;
            if (cmd instanceof IrCommandConstInt) return ((IrCommandConstInt)cmd).t;
            if (cmd instanceof IrCommandLoadString) return ((IrCommandLoadString)cmd).t;
            if (cmd instanceof IrCommandReadVar) return ((IrCommandReadVar)cmd).dst;
            if (cmd instanceof IrCommandCall) return ((IrCommandCall)cmd).dst;
            if (cmd instanceof IrCommandVirtualCall) return ((IrCommandVirtualCall)cmd).dst;
            if (cmd instanceof IrCommandArrayAccess) return ((IrCommandArrayAccess)cmd).dst;
            if (cmd instanceof IrCommandFieldAccess) return ((IrCommandFieldAccess)cmd).dst;
            if (cmd instanceof IrCommandNewArray) return ((IrCommandNewArray)cmd).dst;
            if (cmd instanceof IrCommandNewClass) return ((IrCommandNewClass)cmd).dst;
            else if (cmd instanceof IrCommandGetThis) return ((IrCommandGetThis) cmd).dst;

            return null;
        }
    }
}