package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.AbstractLValue;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class CallMethod extends AbstractExpr {

    private AbstractExpr object;
    private AbstractIdentifier methodName;
    private ListExpr args;

    public CallMethod(AbstractExpr object, AbstractIdentifier methodName, ListExpr args) {
        this.object = object;
        this.methodName = methodName;
        this.args = args;
    }

    public CallMethod( AbstractIdentifier methodName, ListExpr args) {
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new ContextualError("pas encore implementey", getLocation());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        object.decompile(s);
        s.print(".");
        methodName.decompile(s);
        s.print("(");
        args.decompile(s);
        s.print(")");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        object.iter(f);
        methodName.iter(f);
        args.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        object.prettyPrint(s, prefix,false);
        methodName.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

    protected DVal codeGenExpr(DecacCompiler compiler, GPRegister register){
        compiler.addInstruction(new LOAD(new RegisterOffset(0,register),register));
        // 2 pour la premiere methode 3 pour la suivante etcc
        compiler.addInstruction(new BSR(new RegisterOffset(2,register)));
        return register;
    }

}
