package fr.ensimag.deca.tree;

import java.io.DataInput;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;


public  class DeclMethod extends AbstractDeclMethod{


    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListParam listParam;
    final private AbstractBlock block;



    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName,ListParam listParam,AbstractBlock block){
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(listParam);
        Validate.notNull(block);
        this.type = type;
        this.methodName = methodName;
        this.listParam = listParam;
        this.block = block;
    }

    public AbstractIdentifier gettype()
    {
        return type;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        listParam.decompile(s);
        s.print(") ");
        block.decompile(s);
    }
    

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        listParam.prettyPrint(s, prefix, false);
        block.prettyPrint(s, prefix, true);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        listParam.iter(f);
        block.iter(f);
    }
    

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass)  throws ContextualError{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void codeGenMethod(DecacCompiler compiler,String className) {
        String method = methodName.getName().getName();
        DAddr adresse = compiler.associerAdresse();
        DVal dval = new classeNom(className,method);
        compiler.addInstruction(new LOAD(dval, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, adresse));
    }

    @Override
    protected void verifyBlockMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        this.block.verifyBlock(compiler, localEnv, currentClass, type.getType());
    }

    @Override
    protected void codeGenBlock(DecacCompiler compiler,String className){
        String method = methodName.getName().getName();
        DVal dval  = new classeNom(className,method);
        String dval1 = dval.toString();
        compiler.addLabel(new Label(dval1));

        LinkedList<Instruction> lines = new LinkedList<Instruction>();
        block.codeGen(compiler,lines);

        for (Register reg : compiler.registeres) {
            lines.addFirst(new PUSH(reg));
            lines.add(new POP((GPRegister) reg));
        }

        for (Instruction i : lines) {
            compiler.addInstruction(i);
        }
    }
}