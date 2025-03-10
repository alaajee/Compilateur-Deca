package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {

        try {
            this.classes.verifyListClass(compiler);
            this.classes.verifyListClassMembers(compiler);
            this.classes.verifyListClassBody(compiler);
            this.main.verifyMain(compiler);


        } catch (ContextualError e) {
            throw e;
        }
    }


    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        classes.codeGenClasses(compiler);
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        classes.codeGenMethod(compiler);
        compiler.addLabel(new Label("code.Object.equals"));
        compiler.addInstruction(new HALT());
        compiler.addComment("end main program");
        if (compiler.label && !compiler.getCompilerOptions().getNoCHeck()){
            compiler.addLabel(compiler.labelMap.get("io_error"));
            compiler.addInstruction(new WSTR("Error: Input/Output error"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        if (compiler.isArith && !compiler.getCompilerOptions().getNoCHeck()){
            compiler.addLabel(compiler.labelMap.get("overflow_error"));
            compiler.addInstruction(new WSTR("Error: Overflow during arithmetic operation"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        if (compiler.isDiv && !compiler.getCompilerOptions().getNoCHeck()){
            compiler.addLabel(compiler.labelMap.get("division_by_zero_error"));
            compiler.addInstruction(new WSTR("Error: Division by zero"));
            compiler.addInstruction(new WNL());
        }
        if (!compiler.getCompilerOptions().getNoCHeck()){
            Label stackOverflowLabel = new Label("stack_overflow_error");
            compiler.addLabel(stackOverflowLabel);
            compiler.addInstruction(new WSTR(new ImmediateString("Error: Stack Overflow")));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }

        if (compiler.tas_plein && !compiler.getCompilerOptions().getNoCHeck()){
            compiler.addLabel(new Label("Tas_plein"));
            compiler.addInstruction(new WSTR(new ImmediateString("Error: Stack Overflow")));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }

        if (!classes.isEmpty() && !compiler.getCompilerOptions().getNoCHeck()){
            compiler.addLabel(new Label("dereferencement_null"));
            compiler.addInstruction(new WSTR(new ImmediateString("Error: Null pointer dereference")));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }

    @Override
    public void codeGenProgramARM(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        main.codeGenMainARM(compiler);
        if(compiler.print){
            compiler.addInstruction(new MOV(ARMRegister.R0, new ARMImmediateInteger(0)));
            compiler.addInstruction(new BL(new ARMImmediateString("fflush")));
        }
        compiler.addInstruction(new MOV(ARMRegister.R7, new ARMImmediateInteger(1)));
        compiler.addInstruction(new SVC(0));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
