package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.TreeFunction;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class Cast extends AbstractExpr {
    private AbstractIdentifier type;
    private AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return type.toString() + " " + expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type exprType = expr.verifyExpr(compiler, localEnv, currentClass);
        Type targetType = type.verifyType(compiler);

        if (exprType.isVoid()) {
            throw new ContextualError("Cannot cast a void type.", getLocation());
        }

        if (!exprType.isClass() && !targetType.isClass()) {
            if (exprType.isInt() && targetType.isFloat()) {
                this.expr = this.expr.verifyRValue(compiler, localEnv, currentClass, targetType);
                this.expr.setType(targetType);
                return targetType;
            }
        
            if (exprType.isFloat() && targetType.isInt()) {
                this.expr.setType(targetType);
                return targetType;
            }
        
            if (exprType.sameType(targetType)) {
                this.setType(targetType);
                return targetType;
            }
        
            throw new ContextualError("Incompatible types for explicit cast: " +
                exprType + " to " + targetType, getLocation());
        }
    else if (exprType.isClass() && targetType.isClass()) {
        ClassType exprClassType = exprType.asClassType("Invalid class type for casting.", getLocation());
        ClassType targetClassType = targetType.asClassType("Invalid target class type for casting.", getLocation());

        // Vérification si exprType est une sous-classe de targetType
        if (exprClassType.isSubClassOf(targetClassType)) {
            this.expr.setType(targetClassType);
            return targetClassType;
        }

        // Vérification si targetType est une sous-classe de exprType
        if (targetClassType.isSubClassOf(exprClassType)) {
            this.expr.setType(targetClassType);
            return targetClassType;
        }

        // Si aucune des conditions ci-dessus n'est vraie, le cast est invalide
        throw new ContextualError(
            "Cannot cast from type " + exprType.getName() + " to type " + targetType.getName(),
            getLocation()
        );
    }


        return targetType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(")(");
        expr.decompile(s);
        s.print(")");
    }
    
    @Override
    public void prettyPrintChildren(PrintStream s, String name) {
        type.prettyPrint(s, name + "type", false);
        expr.prettyPrint(s, name + "expr", true);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
    

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler) {
        return null;
    }
}
