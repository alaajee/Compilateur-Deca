package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author gl02
 * @date 01/01/2025
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        Iterator<AbstractExpr> iterator = arguments.getList().iterator(); 
    
        while (iterator.hasNext()) {
            AbstractExpr argument = iterator.next(); 
            Type argType = argument.verifyExpr(compiler, localEnv, currentClass);
    
            if (!argType.isString() && !argType.isInt() && !argType.isFloat()) {
                throw new ContextualError("illegal argument for print", argument.getLocation());
            }
        }
    
    }
    

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler);
        }
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            if (printHex) {
                a.codeGenPrintxARM(compiler);
            }
            else {
                a.codeGenPrintARM(compiler);
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // Choisir le type d'instruction : print ou println
        s.print("print" + getSuffix() + "(");
        
        // Décompiler chaque argument de la liste
        Iterator<AbstractExpr> iterator = arguments.iterator();
        while (iterator.hasNext()) {
            AbstractExpr argument = iterator.next();
            argument.decompile(s);
            if (iterator.hasNext()) {
                s.print(", ");  // Ajouter une virgule entre les arguments
            }
        }
        
        s.print(");");
    }
    

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
