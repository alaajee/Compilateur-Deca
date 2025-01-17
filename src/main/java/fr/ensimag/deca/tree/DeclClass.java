package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Map;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
    
        Map<Symbol, TypeDefinition> envTypes = compiler.environmentType.getEnvtypes();
    
        if (!envTypes.containsKey(superClassSymbol)) {
            throw new ContextualError("Super-class '" + superClassSymbol.getName() + "' is not declared", this.getLocation());
        }
    
        TypeDefinition superClassDef = envTypes.get(superClassSymbol);
    
        if (!(superClassDef instanceof ClassDefinition)) {
            if (superClassSymbol.getName().equals("Object")) {
                ClassType objectType = new ClassType(superClassSymbol, Location.BUILTIN, null);
                superClassDef = new ClassDefinition(objectType, Location.BUILTIN, null);
                envTypes.put(superClassSymbol, superClassDef);
            } else {
                throw new ContextualError("Super-class '" + superClassSymbol.getName() + "' is not a valid class", this.getLocation());
            }
        }
    
        if (envTypes.containsKey(classSymbol)) {
            throw new ContextualError("Class '" + classSymbol.getName() + "' is already declared", this.getLocation());
        }
    
        ClassDefinition classDef = new ClassDefinition(
            new ClassType(classSymbol, this.getLocation(), (ClassDefinition) superClassDef),
            this.getLocation(),
            (ClassDefinition) superClassDef
        );
    
        envTypes.put(classSymbol, classDef);
    
        this.superClassName.setDefinition(superClassDef);
        this.className.setDefinition(classDef);
        this.className.setType(classDef.getType());
    }
    

@Override
protected void verifyClassMembers(DecacCompiler compiler)
        throws ContextualError {
    ClassDefinition currentClass = className.getClassDefinition();
    EnvironmentExp localEnv = currentClass.getMembers();
    this.fields.verifyListDeclField(compiler, localEnv, currentClass);
    this.methods.verifyListDeclMethod(compiler, localEnv, currentClass);
}

    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition currentClass = className.getClassDefinition();
        EnvironmentExp localEnv = currentClass.getMembers();
        this.fields.verifyListinitField(compiler, currentClass);
        this.methods.verifyListBlockMethod(compiler, localEnv, currentClass);

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

