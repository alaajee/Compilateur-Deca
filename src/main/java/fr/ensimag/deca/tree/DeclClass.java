package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Map;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class DeclClass extends AbstractDeclClass {

    private AbstractIdentifier className;      // Nom de la classe
    private AbstractIdentifier superClassName; // Nom de la super-classe
    private ListDeclField fields;        // Map des champs
    private ListMethod methods;      // Map des méthodes 

    /**
     * Constructeur de DeclClass
     * 
     * @param className       Identifiant de la classe
     * @param superClassName  Identifiant de la super-classe
     * @param methods         Liste des méthodes de la classe
     * @param fields          Liste des champs de la classe
     */

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClassName,ListDeclField fields,ListMethod methods) {
        this.className = className;
        this.superClassName = superClassName;
        this.fields = fields;
        this.methods = methods;
    }

    // Getter pour le nom de la classe
    public AbstractIdentifier getClassName() {
        return className;
    }

    // Getter pour le nom de la super-classe
    
    public AbstractIdentifier getSuperClassName() {
        return superClassName;
    }
        

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        className.decompile(s);
        if (!superClassName.getName().getName().equals("Object")) {
            s.print(" extends ");
            superClassName.decompile(s);
        }
        
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }
    

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {

        Symbol classSymbol = this.className.getName();
        Symbol superClassSymbol = this.superClassName.getName();
        
        Map<Symbol,TypeDefinition> envTypes = compiler.environmentType.getEnvtypes();
        
        if (!envTypes.containsKey(superClassSymbol)) {
            throw new ContextualError("Super-class " + superClassName + " is not declared", this.getLocation());
        }

        if (envTypes.containsKey(classSymbol)) {
            throw new ContextualError("class " + className + " is already declared", this.getLocation());
        }
        
        ClassDefinition superClassDef = superClassName.getClassDefinition();
        ClassDefinition classDef = new ClassDefinition(
            new ClassType(classSymbol, this.getLocation(), superClassDef),
            this.getLocation(),superClassDef);
        
        envTypes.put(this.className.getName(), classDef);
    }


    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
            className.prettyPrint(s, prefix, false);
            superClassName.prettyPrint(s, prefix, false);
            fields.prettyPrint(s, prefix, false);
            methods.prettyPrint(s, prefix, true); 
    }
    

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        superClassName.iter(f);
        fields.iter(f);
        methods.iter(f);  
    }



}

