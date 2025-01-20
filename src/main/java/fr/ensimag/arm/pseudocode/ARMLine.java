package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

public class ARMLine extends ARMAbstractLine {
    public ARMLine(ARMLabel label, ARMInstruction instruction, String comment) {
        super();
        checkComment(comment);
        this.label = label;
        this.instruction = instruction;
        this.comment = comment;
    }

    public ARMLine(ARMInstruction instruction) {
        super();
        this.instruction = instruction;
    }

    public ARMLine(String comment) {
        super();
        this.comment = comment;
    }

    public ARMLine(ARMLabel label) {
        super();
        this.label = label;
    }

    private void checkComment(final String s) {
        if (s == null) {
            return;
        }
        if (s.contains("\n")) {
            throw new ARMInternalError("Comment '" + s + "'contains newline character");
        }
        if (s.contains("\r")) {
            throw new ARMInternalError("Comment '" + s + "'contains carriage return character");
        }
    }
    private ARMInstruction instruction;
    private String comment;
    private ARMLabel label;

    @Override
    void display(PrintStream s) {
        boolean tab = false;
        if (label != null) {
            s.print(label);
                        s.print(":");
            tab = true;
        }
        if (instruction != null) {
            s.print("\t");
            instruction.display(s);
            tab = true;
        }
        if (comment != null) {
            if (tab) {
                            s.print("\t");
                        }
            s.print(comment);
        }
        s.println();
    }

    public void setInstruction(ARMInstruction instruction) {
        this.instruction = instruction;
    }

    public ARMInstruction getInstruction() {
        return instruction;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setLabel(ARMLabel label) {
        this.label = label;
    }

    public ARMLabel getLabel() {
        return label;
    }
}
