package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass, int index) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Cannot declare a field with type void", this.getLocation());
        }
    
        Symbol fieldSymbol = fieldName.getName();
        EnvironmentExp localEnv = currentClass.getMembers();
        if (localEnv.get(fieldSymbol) != null) {
            throw new ContextualError("Field " + fieldName.getName() + " already defined in the current class", this.getLocation());
        }
    
        ClassDefinition curr = currentClass.getSuperClass(); 
    
        while (curr != null) {
            EnvironmentExp superEnv = curr.getMembers(); 
            if (superEnv.get(fieldSymbol) != null) {
                throw new ContextualError("Field " + fieldName.getName() + " already defined in superclass " 
                                          + curr.getType().getName(), this.getLocation());
            }
            curr = curr.getSuperClass(); 
        }
    
        FieldDefinition fieldDef = new FieldDefinition(t, getLocation(), visibility, currentClass, index);
        try {
            localEnv.declare(fieldSymbol, fieldDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Field " + fieldName.getName() + " already defined", this.getLocation());
        }
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        fieldName.setDefinition(fieldDef);
      
    }

    

    @Override
    protected void codeGenField(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



   


    
}