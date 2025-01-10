package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier var;
    final private AbstractInitialization init;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier var, AbstractInitialization init) {
        this.type = type;
        this.var = var;
        this.init = init;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        var.decompile(s);
        if (init != null) {         
            s.print(" = ");
            init.decompile(s);     
        }

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        var.iter(f);
        init.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        var.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, true);
    }
}
