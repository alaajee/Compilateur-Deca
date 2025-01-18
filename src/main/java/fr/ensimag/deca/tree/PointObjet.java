package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class PointObjet extends AbstractExpr {
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
    public Type verifyExpr(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decompile(IndentPrintStream s){
        instance.decompile(s);
        s.print(".");
        method.decompile(s);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String name){
        instance.prettyPrint(s, name,false);
        method.prettyPrint(s,name,false);
    }

 @Override
 protected void iterChildren(TreeFunction f) {
 if (instance != null) {
 instance.iter(f);
 }
 if (method != null) {
 method.iter(f);
 }
 }


    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        String name = this.instance.getExpr();
        DAddr adresse = compiler.getRegUn(name);
        GPRegister reg  = compiler.associerReg();
        compiler.addInstruction(new LOAD(adresse,reg));
        compiler.addInstruction(new STORE(reg,new RegisterOffset(0,Register.SP)));
        // Il faut s'assurer des arguments ici
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.SP),reg));
        method.codeGenExpr(compiler,reg);
        compiler.libererReg();
        return adresse;
    }
}