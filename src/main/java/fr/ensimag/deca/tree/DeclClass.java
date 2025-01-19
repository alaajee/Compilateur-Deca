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
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.mockito.exceptions.misusing.CannotVerifyStubOnlyMock;

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
     * @param className      Identifiant de la classe
     * @param superClassName Identifiant de la super-classe
     * @param methods        Liste des méthodes de la classe
     * @param fields         Liste des champs de la classe
     */

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClassName, ListDeclField fields, ListMethod methods) {
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
        this.fields.verifyListDeclField(compiler,localEnv,currentClass);
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


    @Override
    protected void codeGenclasse(DecacCompiler compiler) {
        String className = this.className.getName().getName();
        String name = "Construction de la table des methodes de " + className;
        compiler.addComment(name);
        DVal Object = new classeNom("Object", "equals");
        DVal dval = new classeNom(className, "equals");
        DAddr adresse = compiler.associerAdresse();
        compiler.addInstruction(new LEA(compiler.adresseClasse, Register.R0));
        compiler.adresseClasse = adresse;
        compiler.addInstruction(new STORE(Register.R0, adresse));
        compiler.addInstruction(new LOAD(Object, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, compiler.associerAdresse()));
        compiler.setTableClassee(className,adresse);
        int somme =  fields.size();
        if (!superClassName.getName().getName().equals("Object")){
            int i = compiler.getTableFields(superClassName.getName().getName());
            somme += i ;

        }
        compiler.setTableFields(className, somme);
        for (AbstractDeclMethod method : methods.getList()) {
            method.codeGenMethod(compiler, className);
        }
        int x = 1;
        for (AbstractDeclField field : fields.getList()) {
            compiler.setTableNombreField(field.getName(),x);
            x +=1;
        }

    }

    protected void codeGenMethod(DecacCompiler compiler){

    }

//        protected void codeGenMethod(DecacCompiler compiler){
//            String className = this.className.getName().getName();
//            for (AbstractDeclMethod method : methods.getList()) {
//                method.codeGenMethod(compiler, className);
//            }
//        }

        @Override
        protected void initClass(DecacCompiler compiler){
            String className = this.className.getName().getName();
            compiler.addLabel(new Label("init" + className));
            Label label = new Label("init" + superClassName.getName().getName());

            if (!superClassName.getName().getName().equals("Object")) {
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
                compiler.addInstruction(new PUSH(Register.R0));
                compiler.addInstruction(new BSR(label));
                compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
            }

            for (AbstractDeclField field : fields.getList()) {
                field.codeGenField(compiler);

            }
            //System.out.println(className + compiler.RegisterOffset);
            compiler.addInstruction(new RTS());
            for (AbstractDeclMethod method : methods.getList()) {
                method.codeGenBlock(compiler, className);
            }
        }
    }
