import java.io.*;

import java_cup.runtime.Symbol;
import ast.*;

public class ControlFlow {
    private static class ControlFlowNode {
        IrCommand command;
        ControlFlowNode neighbor0;
        ControlFlowNode neighbor1;

        public ControlFlowNode(IrCommand command) {
            this.command = command;
        }
    }

    public static void controlFlow(IrCommandList irCommands) {
        ControlFlowNode head = null;
        ControlFlowNode prevNode = null;

        // Create control flow nodes for each IR command
        for (IrCommandList list = irCommands; list != null; list = list.tail) {
            IrCommand command = list.head;
            ControlFlowNode currentNode = new ControlFlowNode(command);
            if (head == null) {
                head = currentNode;
            }
            if (prevNode != null) {
                prevNode.neighbor0 = currentNode; // Sequential flow
            }
            prevNode = currentNode;

            // Handle branching commands
            if (cmd instanceof IrCommandBranch) {
                String targetLabel = ((IrCommandBranch) command).labelName;
                // Find the target node and set it as neighbor1
                ControlFlowNode targetNode = findTargetNode(head, targetLabel);
                if (targetNode != null) {
                    currentNode.neighbor1 = targetNode;
                }
            }
        }

        // Optionally, print or process the control flow graph starting from head
        
    }
}