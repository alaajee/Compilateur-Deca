package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
       
        EnvironmentExp envExp = new EnvironmentExp(null);
        this.declVariables.verifyListDeclVariable(compiler,envExp,null);
        this.insts.verifyListInst(compiler,envExp,null,null);
    }


    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        compiler.addComment("Beginning of main instructions:");
        declVariables.codeGen(compiler);
        insts.codeGenListInst(compiler);
        int maximum = compiler.getAdressVar() + compiler.nbrVar;
        compiler.addFirst(new ADDSP(new ImmediateInteger(maximum)));
        Label stackOverflowLabel = new Label("stack_overflow_error");
        if (!compiler.getCompilerOptions().getNoCHeck()){
            compiler.addFirst(new BOV(stackOverflowLabel));
        }
        compiler.addFirst(new TSTO(compiler.getMaxTsto()+compiler.nbrVar));

    }

    @Override
    protected void codeGenMainARM(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        compiler.addFirstComment("main:");
        compiler.addFirstComment(".global main");
        compiler.addFirstComment(".section .text");
        compiler.addFirstComment("");
        declVariables.codeGenARM(compiler);
        insts.codeGenListInstARM(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}