package fr.ensimag.deca.tree;

import java.io.DataInput;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
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
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        SymbolTable.Symbol methodSymbol = this.methodName.getName();
        Map<SymbolTable.Symbol,TypeDefinition> envTypes = compiler.environmentType.getEnvtypes();

        if (envTypes.containsKey(methodSymbol)) {
            throw new ContextualError("La méthode " + methodName.getName() + " existe déjà dans la classe.", methodName.getLocation());
        }

        MethodDefinition overriddenMethod = null;
        ClassDefinition superClass = currentClass.getSuperClass();
        while (superClass != null) {
            ExpDefinition superMethod = superClass.getMembers().get(methodSymbol);
            if (superMethod!=null)
            {
                if (superMethod instanceof MethodDefinition) {
                    overriddenMethod = (MethodDefinition) superMethod;
                    break;
                }
            }
            superClass = superClass.getSuperClass();
        }

        if (overriddenMethod!=null)
        {
            if(!t.sameType(overriddenMethod.getType()))
            {
                throw new ContextualError("Le type de retour de la méthode " + methodName.getName() +
                " n'est pas compatible avec celui de la méthode redéfinie dans une superclasse.", methodName.getLocation());
            }

            if (!overriddenMethod.getSignature().equals(this.listParam.verifyListParam(compiler, localEnv, currentClass)))
            {
                throw new ContextualError(
                    "La signature des paramètres de la méthode " + methodName.getName() +
                    " n'est pas compatible avec celle de la méthode redéfinie dans une superclasse.",
                    methodName.getLocation()
                );
            }
            methodName.setDefinition(overriddenMethod);

        }




        Signature methodSignature = this.listParam.verifyListParam(compiler, localEnv,currentClass);
        MethodDefinition methodDefinition = new MethodDefinition(
            t,
            methodName.getLocation(),
            methodSignature,
            currentClass.incNumberOfMethods()
        );
        methodName.setDefinition(methodDefinition);
    
        try {
            currentClass.getMembers().declare(methodSymbol, methodDefinition);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(
                "La méthode " + methodName.getName() + " est déjà définie dans l'environnement local.",
                methodName.getLocation()
            );
        }
        

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
        listParam.codeGen(compiler);
        block.codeGen(compiler,lines);


        for (Iterator<Register> it = compiler.registeres.descendingIterator(); it.hasNext(); ) {
            Register reg = it.next();
            lines.addFirst(new PUSH(reg));
            lines.add(new POP((GPRegister) reg));
        }


        for (Instruction i : lines) {
            compiler.addInstruction(i);
        }
    }
}