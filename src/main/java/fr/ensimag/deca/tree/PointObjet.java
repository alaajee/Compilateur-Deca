package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.*;


import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class PointObjet extends AbstractExpr {
    private static final Logger log = Logger.getLogger(PointObjet.class);
    private AbstractExpr instance;
    private CallMethod method;

    public PointObjet(AbstractExpr instance, CallMethod method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public String toString() {
        return "PointObjet [instance=" + instance + ", method=" + method + "]";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type instanceType = instance.verifyExpr(compiler, localEnv, currentClass);

        if (!instanceType.isClass()) {
            throw new ContextualError("The left part of '.' must be an object", instance.getLocation());
        }

        ClassDefinition instanceClass = (ClassDefinition) compiler.environmentType.getEnvtypes().get(instanceType.getName());
        if (instanceClass == null) {
            throw new ContextualError("Class " + instanceType.getName() + " is not defined", instance.getLocation());
        }

        Type methodType = method.verifyExpr(compiler, localEnv, instanceClass);
        this.setType(methodType);
        return methodType;
    }



@Override
    public void decompile(IndentPrintStream s) {
        
        method.decompile(s);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String prefix) {
        if (method != null) {
            method.prettyPrint(s, prefix, true);
        }
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        if (method != null) {
            method.iter(f);
        }
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        Identifier identifier = (Identifier) this.instance;
        String name = identifier.getName().getName();
        DAddr adresse = compiler.getRegUn(name);
        GPRegister reg  = compiler.associerReg();
        int i = method.getNbreFields();
        compiler.addInstruction(new ADDSP(1+i));
        compiler.addInstruction(new LOAD(adresse,reg));
        compiler.addInstruction(new STORE(reg,new RegisterOffset(0,Register.SP)));
        // Il faut s'assurer des arguments ici
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.SP),reg));
        method.codeGenExpr(compiler,reg);
        compiler.libererReg();
        return adresse;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        System.out.println("iciiiiiiiiiiii");
       instance.codeGenInst(compiler);
       method.codeGenInst(compiler);
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }
}