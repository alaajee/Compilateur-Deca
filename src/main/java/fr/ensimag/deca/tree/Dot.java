package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class Dot extends AbstractLValue{
    private AbstractExpr left;
    private AbstractIdentifier right;


    public Dot(AbstractExpr expr, AbstractIdentifier name) {
        this.left=expr;
        this.right=name;
    }

    @Override
    public String toString() {
        return left.toString()+" "+right.toString();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type leftType = left.verifyExpr(compiler, localEnv, currentClass);
        if (!leftType.isClass()) {
            throw new ContextualError("The left-hand side of '.' must be a class type.", left.getLocation());
        }
    
        ClassDefinition leftClassDef = (ClassDefinition) compiler.environmentType.getEnvtypes().get(leftType.getName());
        if (leftClassDef == null) {
            throw new ContextualError("The class '" + leftType.getName() + "' is not defined.", left.getLocation());
        }
    
        ExpDefinition memberDef = leftClassDef.getMembers().getEnvExp().get(right.getName());
        if (memberDef == null) {
            throw new ContextualError(
                "The member '" + right.getName() + "' is not defined in class '" 
                + leftClassDef.getType().getName() + "'.",
                right.getLocation()
            );
        }
    
        if (!(memberDef instanceof FieldDefinition)) {
            throw new ContextualError(
                "The member '" + right.getName() + "' is not a field but a method or another type.",
                right.getLocation()
            );
        }
    
        right.setDefinition(memberDef);
        right.setType(memberDef.getType());
        this.setType(memberDef.getType());
        return memberDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s){

    }

    @Override
    public void prettyPrintChildren(PrintStream s, String name){
        left.prettyPrint(s,name,false);
        right.prettyPrint(s,name,false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        left.iterChildren(f);
        right.iterChildren(f);
    }


    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler){
        return null;
    }
}