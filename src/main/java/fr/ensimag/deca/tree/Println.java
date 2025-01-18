package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

import java.util.LinkedList;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
        compiler.addInstruction(new WNL());
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        super.codeGenInstARM(compiler);
        if(!compiler.println){
            compiler.addFirstComment("line: .asciz \"\\n\"");
            compiler.println = true;
        }
        compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"line")));
        compiler.addInstruction(new BL(new ARMImmediateString("printf")));
    }

    @Override
    String getSuffix() {
        return "ln";
    }


    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines){
        super.codeGenInst(compiler);
        compiler.addInstruction(new WNL());
        return null;
    }
}
