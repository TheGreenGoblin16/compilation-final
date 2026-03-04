import java.io.*;
import java.util.*;
import ir.*;
import temp.*;
import symboltable.SymbolTableEntry;
import java.lang.reflect.Field; 

public class controlFlow {

    // -------------------------------------------------------------------------
    // Inner Classes
    // -------------------------------------------------------------------------

    private static class CfgNode {
        int id;
        IrCommand command;
        List<CfgNode> succ = new ArrayList<>();
        List<CfgNode> pred = new ArrayList<>();
        
        Set<String> in = new HashSet<>();
        Set<String> out = new HashSet<>();
        
        public CfgNode(int id, IrCommand command) {
            this.id = id;
            this.command = command;
        }
    }

    // -------------------------------------------------------------------------
    // Main Analysis Entry Point
    // -------------------------------------------------------------------------
    
    // Note: Parameter signature matches Main.java call: controlFlow(IrCommand, String)
    public static void controlFlow(IrCommand headCmd, PrintWriter writer) {
        
        // 1. Flatten IR into a List of Nodes
        // ---------------------------------------------------------------------
        List<CfgNode> nodes = new ArrayList<>();
        int nodeId = 0;

        // Add the first command (stored separately in Ir.java)
        if (headCmd != null) {
            nodes.add(new CfgNode(nodeId++, headCmd));
        }

        // Add the rest of the commands (stored in the 'tail' list in Ir.java)
        // Accessing Ir.getInstance().tail via reflection or assuming public if modified.
        // Since I cannot guarantee Ir.java visibility change, I will use reflection 
        // to be safe, or just assume the standard skeleton 'tail' field.
        // However, based on the provided files, 'tail' in Ir is private.
        // I will use reflection to get the list.
        
        try {
            Field tailField = Ir.class.getDeclaredField("tail");
            tailField.setAccessible(true);
            IrCommandList list = (IrCommandList) tailField.get(Ir.getInstance());
            
            while (list != null) {
                nodes.add(new CfgNode(nodeId++, list.head));
                list = list.tail;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (nodes.isEmpty()) {
            writeOutput(writer, new ArrayList<>());
            return;
        }

        // 2. Build Control Flow Graph (Edges)
        // ---------------------------------------------------------------------
        Map<String, CfgNode> labelMap = new HashMap<>();
        
        // 2a. Map Labels
        for (CfgNode node : nodes) {
            if (node.command instanceof IrCommandLabel) {
                String lbl = ((IrCommandLabel) node.command).labelName;
                labelMap.put(lbl, node);
            }
        }

        // 2b. Add Edges
        for (int i = 0; i < nodes.size(); i++) {
            CfgNode node = nodes.get(i);
            IrCommand cmd = node.command;
            
            boolean fallsThrough = true;

            // Handle Jumps
            if (cmd instanceof IrCommandBranch) {
                String target = ((IrCommandBranch) cmd).labelName;
                CfgNode targetNode = labelMap.get(target);
                if (targetNode != null) {
                    addEdge(node, targetNode);
                }
                fallsThrough = false; // Unconditional jump
            } 
            else if (cmd instanceof IrCommandBranchIfZero) {
                String target = ((IrCommandBranchIfZero) cmd).labelName;
                CfgNode targetNode = labelMap.get(target);
                if (targetNode != null) {
                    addEdge(node, targetNode);
                }
                fallsThrough = true; // Conditional falls through
            }
            else if (cmd instanceof IrCommandBranchIfEquals) {
                String target = ((IrCommandBranchIfEquals) cmd).labelName;
                CfgNode targetNode = labelMap.get(target);
                if (targetNode != null) {
                    addEdge(node, targetNode);
                }
                fallsThrough = true;
            }
            else if (cmd instanceof IrCommandBranchIfLess) {
                String target = ((IrCommandBranchIfLess) cmd).labelName;
                CfgNode targetNode = labelMap.get(target);
                if (targetNode != null) {
                    addEdge(node, targetNode);
                }
                fallsThrough = true;
            }
            else if (cmd instanceof IrCommandBranchIfEqualsStrings) {
                String target = ((IrCommandBranchIfEqualsStrings) cmd).labelName;
                CfgNode targetNode = labelMap.get(target);
                if (targetNode != null) {
                    addEdge(node, targetNode);
                }
                fallsThrough = true;
            } else if (cmd instanceof IrCommandReturn) {
                fallsThrough = false;
            } else if (cmd instanceof IrCommandCallVoid) {
                 // Check if it's an exit call? No, standard L semantics assume return.
            }

            // Handle Fallthrough
            if (fallsThrough && i + 1 < nodes.size()) {
                addEdge(node, nodes.get(i + 1));
            }
        }

        // 3. Initialize Dataflow Analysis
        // ---------------------------------------------------------------------
        // Universe: All variables in the program (source vars + temps)
        Set<String> universe = new HashSet<>();
        for (CfgNode node : nodes) {
            getUses(node.command, universe); // Just to populate universe
            getDef(node.command, universe);  // Just to populate universe
        }

        // Initialize sets
        // OUT[Entry] = Empty (or Global inits if handled linearly). 
        // Since global inits are linearly at the start of the IR list, we start with Empty.
        // OUT[Other] = Universe (for intersection convergence)
        for (CfgNode node : nodes) {
            node.in.clear(); // IN starts empty
            if (node == nodes.get(0)) {
                node.out.clear();
            } else {
                node.out.addAll(universe);
            }
        }

        // 4. Chaotic Iteration (Worklist Algorithm)
        // ---------------------------------------------------------------------
        Queue<CfgNode> worklist = new LinkedList<>(nodes);
        
        while (!worklist.isEmpty()) {
            CfgNode node = worklist.poll();

            // Compute IN = Intersection of Predecessors' OUT
            Set<String> newIn;
            if (node.pred.isEmpty()) {
                newIn = new HashSet<>(); // Entry node behavior
            } else {
                // Start with Universe and intersect
                newIn = new HashSet<>(universe);
                for (CfgNode p : node.pred) {
                    newIn.retainAll(p.out);
                }
            }

            // Save old OUT to check for changes
            Set<String> oldOut = new HashSet<>(node.out);

            // Compute Transfer Function: OUT = GEN U (IN - KILL)
            // But we use the specific logic: 
            // - Identify uses. If use not in IN, it's an error (tracked later), 
            //   but conceptually the var is "not valid" for definition.
            
            Set<String> newOut = new HashSet<>(newIn);
            
            boolean errorInInstruction = false;
            Set<String> uses = new HashSet<>();
            getUses(node.command, uses);
            
            for (String u : uses) {
                if (!newIn.contains(u)) {
                    errorInInstruction = true;
                }
            }

            String def = getDef(node.command, null);
            
            if (def != null) {
                if (errorInInstruction) {
                    // "invalid definition": remove var from OUT
                    newOut.remove(def);
                } else {
                    // "valid definition": add var to OUT
                    newOut.add(def);
                }
            }

            // Update State
            node.in = newIn;
            node.out = newOut;

            // If OUT changed, add successors to worklist
            if (!node.out.equals(oldOut)) {
                for (CfgNode s : node.succ) {
                    if (!worklist.contains(s)) {
                        worklist.add(s);
                    }
                }
            }
        }

        // 5. Final Pass: Collect Errors
        // ---------------------------------------------------------------------
        Set<String> detectedErrors = new HashSet<>();

        for (CfgNode node : nodes) {
            Set<String> uses = new HashSet<>();
            getUses(node.command, uses);
            
            for (String u : uses) {
                if (!node.in.contains(u)) {
                    // Filter: Only report high-level variables (not starting with "Temp_")
                    // Temps from TempFactory are integers usually, but converted to string?
                    // Temp.toString() isn't defined in skeleton. 
                    // Ir commands use Temp.toString() implicitly or explicit mapping.
                    // We need to distinguish.
                    // High-level vars match [a-zA-Z]...
                    // Temps are usually t0, t1...
                    // The skeleton TempFactory uses a counter.
                    // Assumption: Source variables don't look like temps?
                    // Actually, the prompt says "remove compiler-generated temporary variables".
                    // Best heuristic: If it matches strict Source ID rules. 
                    // But easier: Temps in IR are usually objects, Var names are Strings.
                    // In my set<String>, I mix them.
                    // How did I convert Temp to String?
                    // In getUses/getDef, I need to handle Temp vs String.
                    
                    if (isHighLevelVar(u)) {
                        detectedErrors.add(u);
                    }
                }
            }
        }

        // 6. Write Output
        // ---------------------------------------------------------------------
        List<String> sortedErrors = new ArrayList<>(detectedErrors);
        Collections.sort(sortedErrors);
        System.out.println("Detected uninitialized variable uses: " + sortedErrors); // y added for debug
        writeOutput(writer, sortedErrors);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void addEdge(CfgNode from, CfgNode to) {
        if (!from.succ.contains(to)) from.succ.add(to);
        if (!to.pred.contains(from)) to.pred.add(from);
    }

    // Update helper to use the existing writer
    private static void writeOutput(PrintWriter writer, List<String> errors) {
        if (errors.isEmpty()) {
            writer.print("!OK");
        } else {
            for (int i = 0; i < errors.size(); i++) {
                writer.print(errors.get(i).split(";")[0]); // Print only variable name
                if (i < errors.size() - 1) {
                    writer.print("\n"); // Ensure strictly one error per line
                }
            }
        }
        // Do NOT close the writer here; Main.java handles that in 'finally'
    }

    // Helper to extract string representation of Temp or String
    private static String str(Object o) {
        if (o instanceof Temp) {
            return "Temp_" + ((Temp) o).getSerialNumber();
        }
        if (o instanceof SymbolTableEntry) {
            return ((SymbolTableEntry) o).name + ";SymbolTableEntry_"+ ((SymbolTableEntry) o).id;
        }
        return o.toString();
    }
    
    private static boolean isHighLevelVar(String s) {
        return !s.startsWith("Temp_");
    }

    // Populate the set with variables used in the command
    private static void getUses(IrCommand cmd, Set<String> set) {
        if (cmd instanceof IrCommandBinopAddIntegers) {
            set.add(str(((IrCommandBinopAddIntegers)cmd).t1));
            set.add(str(((IrCommandBinopAddIntegers)cmd).t2));
        } else if (cmd instanceof IrCommandBinopAddStrings) {
            set.add(str(((IrCommandBinopAddStrings)cmd).t1));
            set.add(str(((IrCommandBinopAddStrings)cmd).t2));
        } else if (cmd instanceof IrCommandBinopSubIntegers) {
            set.add(str(((IrCommandBinopSubIntegers)cmd).t1));
            set.add(str(((IrCommandBinopSubIntegers)cmd).t2));
        } else if (cmd instanceof IrCommandBinopMulIntegers) {
            set.add(str(((IrCommandBinopMulIntegers)cmd).t1));
            set.add(str(((IrCommandBinopMulIntegers)cmd).t2));
        } else if (cmd instanceof IrCommandBinopDivIntegers) {
            set.add(str(((IrCommandBinopDivIntegers)cmd).t1));
            set.add(str(((IrCommandBinopDivIntegers)cmd).t2));
        } else if (cmd instanceof IrCommandBranchIfZero) {
            set.add(str(((IrCommandBranchIfZero)cmd).t));
        } else if (cmd instanceof IrCommandBranchIfEquals) {
            set.add(str(((IrCommandBranchIfEquals)cmd).t1));
            set.add(str(((IrCommandBranchIfEquals)cmd).t2));
        } else if (cmd instanceof IrCommandBranchIfLess) {
            set.add(str(((IrCommandBranchIfLess)cmd).t1));
            set.add(str(((IrCommandBranchIfLess)cmd).t2));
        } else if (cmd instanceof IrCommandBranchIfEqualsStrings) {
            set.add(str(((IrCommandBranchIfEqualsStrings)cmd).t1));
            set.add(str(((IrCommandBranchIfEqualsStrings)cmd).t2));
        } else if (cmd instanceof IrCommandCall) {
            TempList args = ((IrCommandCall)cmd).args;
            while (args != null) { set.add(str(args.head)); args = args.tail; }
        } else if (cmd instanceof IrCommandCallVoid) {
            TempList args = ((IrCommandCallVoid)cmd).args;
            while (args != null) { set.add(str(args.head)); args = args.tail; }
        } else if (cmd instanceof IrCommandVirtualCall) {
            set.add(str(((IrCommandVirtualCall)cmd).inst));
            TempList args = ((IrCommandVirtualCall)cmd).args;
            while (args != null) { set.add(str(args.head)); args = args.tail; }
        } else if (cmd instanceof IrCommandVirtualCallVoid) {
            set.add(str(((IrCommandVirtualCallVoid)cmd).inst));
            TempList args = ((IrCommandVirtualCallVoid)cmd).args;
            while (args != null) { set.add(str(args.head)); args = args.tail; }
        } else if (cmd instanceof IrCommandReturn) {
            if (((IrCommandReturn)cmd).src != null) 
                 set.add(str(((IrCommandReturn)cmd).src));
        } else if (cmd instanceof IrCommandWriteVar) {
            set.add(str(((IrCommandWriteVar)cmd).src));
        } else if (cmd instanceof IrCommandArraySet) {
            set.add(str(((IrCommandArraySet)cmd).src));
            set.add(str(((IrCommandArraySet)cmd).arr));
            set.add(str(((IrCommandArraySet)cmd).index));
        } else if (cmd instanceof IrCommandArrayAccess) {
            set.add(str(((IrCommandArrayAccess)cmd).arr));
            set.add(str(((IrCommandArrayAccess)cmd).index));
        } else if (cmd instanceof IrCommandFieldAccess) {
            set.add(str(((IrCommandFieldAccess)cmd).inst));
        } else if (cmd instanceof IrCommandFieldSet) {
            set.add(str(((IrCommandFieldSet)cmd).src));
            set.add(str(((IrCommandFieldSet)cmd).inst));
        } else if (cmd instanceof IrCommandNewArray) {
            set.add(str(((IrCommandNewArray)cmd).size));
        } else if (cmd instanceof IrCommandReadVar) {
            set.add(str(((IrCommandReadVar)cmd).varEntry)); //////////// check
        }
        // ConstInt, NewClass, Label, Branch have no uses (or handled in Def)
    }

    // Return the variable defined by the command (or null)
    private static String getDef(IrCommand cmd, Set<String> addToUniverse) {
        String def = null;
        if (cmd instanceof IrCommandBinopAddIntegers) def = str(((IrCommandBinopAddIntegers)cmd).dst);
        else if (cmd instanceof IrCommandBinopAddStrings) def = str(((IrCommandBinopAddStrings)cmd).dst);
        else if (cmd instanceof IrCommandBinopSubIntegers) def = str(((IrCommandBinopSubIntegers)cmd).dst);
        else if (cmd instanceof IrCommandBinopMulIntegers) def = str(((IrCommandBinopMulIntegers)cmd).dst);
        else if (cmd instanceof IrCommandBinopDivIntegers) def = str(((IrCommandBinopDivIntegers)cmd).dst);
        else if (cmd instanceof IrCommandCall) def = str(((IrCommandCall)cmd).dst);
        else if (cmd instanceof IrCommandVirtualCall) def = str(((IrCommandVirtualCall)cmd).dst);
        else if (cmd instanceof IrCommandConstInt) def = str(((IrCommandConstInt)cmd).t);
        else if (cmd instanceof IrCommandReadVar) def = str(((IrCommandReadVar)cmd).dst);
        else if (cmd instanceof IrCommandWriteVar) def = str(((IrCommandWriteVar)cmd).varEntry); // The only String def
        else if (cmd instanceof IrCommandArrayAccess) def = str(((IrCommandArrayAccess)cmd).dst);
        else if (cmd instanceof IrCommandFieldAccess) def = str(((IrCommandFieldAccess)cmd).dst);
        else if (cmd instanceof IrCommandNewArray) def = str(((IrCommandNewArray)cmd).dst);
        else if (cmd instanceof IrCommandNewClass) def = str(((IrCommandNewClass)cmd).dst);
        
        if (def != null && addToUniverse != null) {
            addToUniverse.add(def);
        }
        return def;
    }
}