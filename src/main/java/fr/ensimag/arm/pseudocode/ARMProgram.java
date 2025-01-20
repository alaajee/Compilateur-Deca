package fr.ensimag.arm.pseudocode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

public class ARMProgram {
    private final LinkedList<ARMAbstractLine> lines = new LinkedList<ARMAbstractLine>();

    public void add(ARMAbstractLine line) {
        lines.add(line);
    }

    public void addComment(String s) {
        lines.add(new ARMLine(s));
    }

    public void addLabel(ARMLabel l) {
        lines.add(new ARMLine(l));
    }

    public void addInstruction(ARMInstruction i) {
        lines.add(new ARMLine(i));
    }

    public void addInstruction(ARMInstruction i, String s) {
        lines.add(new ARMLine(null, i, s));
    }

    /**
     * Append the content of program p to the current program. The new program
     * and p may or may not share content with this program, so p should not be
     * used anymore after calling this function.
     */
    public void append(ARMProgram p) {
        lines.addAll(p.lines);
    }
    
    /**
     * Add a line at the front of the program.
     */
    public void addFirst(ARMLine l) {
        lines.addFirst(l);
    }

    public void addFirstComment(String comment){
        lines.addFirst(new ARMLine(comment));
    }

    /**
     * Display the program in a textual form readable by ARM to stream s.
     */
    public void display(PrintStream s) {
        for (ARMAbstractLine l: lines) {
            l.display(s);
        }
    }

    /**
     * Return the program in a textual form readable by ARM as a String.
     */
    public String display() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream s = new PrintStream(out);
        display(s);
        return out.toString();
    }

    public void addFirst(ARMInstruction i) {
        addFirst(new ARMLine(i));
    }
    
    public void addFirst(ARMInstruction i, String comment) {
        addFirst(new ARMLine(null, i, comment));
    }
}
