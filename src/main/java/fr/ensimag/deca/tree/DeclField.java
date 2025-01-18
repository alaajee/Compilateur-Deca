package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;


public class DeclField extends AbstractDeclField{


    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;
    final private Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization,Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        Validate.notNull(visibility);
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
        this.visibility = visibility;
    }



    @Override
    public void decompile(IndentPrintStream s) {

    s.print(visibility + " ");
    type.decompile(s);
    s.print(" ");
    fieldName.decompile(s);
    initialization.decompile(s);
    s.println(";");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);     
        fieldName.prettyPrint(s, prefix, false); 
        initialization.prettyPrint(s, prefix, true); 
    }
    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);    
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass, int index) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Cannot declare a field with type void", this.getLocation());
        }

        Symbol fieldSymbol = fieldName.getName();
        EnvironmentExp localEnv = currentClass.getMembers();

        if (localEnv.getEnvExp().containsKey(fieldSymbol)) {
            throw new ContextualError("Field " + fieldName.getName() + " already defined in the current class", this.getLocation());
        }

        ClassDefinition curr = currentClass.getSuperClass();
        ClassDefinition originClass = null;
        while (curr != null) {
            EnvironmentExp superEnv = curr.getMembers();
            if (superEnv.get(fieldSymbol) != null) {
                originClass = curr;
            }
            curr = curr.getSuperClass();
        }

        if (originClass != null) {
            throw new ContextualError("Field " + fieldName.getName() + " already defined in superclass "
                                      + originClass.getType().getName(), this.getLocation());
        }

        FieldDefinition fieldDef = new FieldDefinition(t, getLocation(), visibility, currentClass, index);
        try {
            localEnv.declare(fieldSymbol, fieldDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Field " + fieldName.getName() + " already defined in the current class", this.getLocation());
        }

        fieldName.setDefinition(fieldDef);
    }
    @Override
    protected void verifyinitField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {

            Type t = this.type.verifyType(compiler);
            if (t.isVoid()) {
                throw new ContextualError("Cannot declare a field with type void", this.getLocation());
            }
            EnvironmentExp localEnv = currentClass.getMembers();
            initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        }



    @Override
    protected DVal codeGenField(DecacCompiler compiler) {
        compiler.setNbreField();
        RegisterOffset reg = compiler.getRegisterClass();
       // System.out.println("je suis ici" + reg);
        // System.out.println(reg);
        FieldDefinition variable = new FieldDefinition(this.type.getDefinition().getType(), this.getLocation(),null,null,0);
        variable.setOperand(reg);
        compiler.setRegisterOffsets(this.fieldName.getName().getName(), reg.getOffset());
        // System.out.println(compiler.getRegUn());
        RegisterOffset reg2 = new RegisterOffset(-2,Register.LB);
        if (this.initialization.initialization()) {
            // Ici traiter l'initialisation
            this.initialization.codeGenField(compiler);
        }
        else {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        }
        compiler.addInstruction(new LOAD(reg2, Register.R1));
        compiler.addInstruction(new STORE(Register.R0,reg));
        return reg;
    }
}