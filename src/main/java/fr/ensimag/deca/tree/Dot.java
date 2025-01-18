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
    public Type verifyExpr(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass)
    {
        throw new UnsupportedOperationException();
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
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler){
        return null;
    }
}