package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.AbstractLValue;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.LinkedList;

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
        ClassDefinition objectClassDef;
        System.out.println(currentClass);
        if (object != null) {

            Type objectType = object.verifyExpr(compiler, localEnv, currentClass);

            if (!objectType.isClass()) {
                throw new ContextualError("The expression before '.' must be an object.", object.getLocation());
            }
            objectClassDef = objectType.asClassType("Invalid object type for method call.", getLocation()).getDefinition();
        } else {
            if (currentClass == null) {
                throw new ContextualError("Cannot call method without an object outside a class context.", getLocation());
            }
            objectClassDef = currentClass;
        }

        ExpDefinition methodDef = currentClass.getMembers().get(methodName.getName());
        if (methodDef == null || !(methodDef instanceof MethodDefinition)) {
            throw new ContextualError("Method " + methodName.getName() + " is not defined in class " + objectClassDef.getType().getName(), methodName.getLocation());
        }

        MethodDefinition method = (MethodDefinition) methodDef;
        methodName.setDefinition(method);
        Signature methodSignature = method.getSignature();
        if (args.size() != methodSignature.size()) {
            throw new ContextualError(
                    "Invalid number of arguments for method " + methodName.getName() +
                            ". Expected: " + methodSignature.size() + ", Provided: " + args.size(),
                    getLocation()
            );
        }

        for (int i = 0; i < args.size(); i++) {
            Type expectedType = methodSignature.paramNumber(i);
            Type actualType = args.getList().get(i).verifyExpr(compiler, localEnv, currentClass);
            if (!actualType.sameType(expectedType)) {
                throw new ContextualError(
                        "Invalid type for argument " + (i + 1) + " of method " + methodName.getName() +
                                ". Expected: " + expectedType + ", Provided: " + actualType,
                        args.getList().get(i).getLocation()
                );
            }
        }

        this.setType(method.getType());
        return method.getType();
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
        compiler.addInstruction(new LOAD(Register.R0,Register.R1));
        compiler.addInstruction(new WINT());
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (object != null) {
            object.iter(f);
        }
        methodName.iter(f);
        args.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (object != null) {
            object.prettyPrint(s, prefix, false);
        }
        methodName.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
    }



    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }

}
