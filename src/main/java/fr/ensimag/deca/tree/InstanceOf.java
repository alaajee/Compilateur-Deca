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
        Type exprType = nom.verifyExpr(compiler, localEnv, currentClass);
        if (!exprType.isClassOrNull()) {
            throw new ContextualError("The left-hand side of 'instanceof' must be a class type or null.", nom.getLocation());
        }
        Type targetType = type.verifyType(compiler);
        if (!targetType.isClass()) {
            throw new ContextualError("The target type in 'instanceof' must be a class type.", type.getLocation());
        }
        this.setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
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

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler) {
        throw new UnsupportedOperationException("codeGenExpr() not implemented yet for InstanceOf.");
    }

}
