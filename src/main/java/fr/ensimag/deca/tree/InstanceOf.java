package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import java.io.PrintStream;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class InstanceOf extends AbstractExpr {
    private AbstractExpr nom;
    private AbstractIdentifier type;


    public InstanceOf(AbstractExpr nom, AbstractIdentifier type) {
        this.nom=nom;
        this.type=type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                throw new UnsupportedOperationException("verifyExpr() not implemented yet for InstanceOf.");
    }
    


    @Override
    public void decompile(IndentPrintStream s) {
        nom.decompile(s);
        s.print(" instanceof ");
        type.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        nom.iter(f);
        type.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        nom.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("codeGenExpr() not implemented yet for InstanceOf.");
    }

}
