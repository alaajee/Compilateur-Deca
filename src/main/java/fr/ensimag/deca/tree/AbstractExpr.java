package fr.ensimag.deca.tree;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl02
 * @date 01/01/2025
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */

    public String expression = "general";
    public boolean continuePush = false;

    public String getExpr(){
        return expression;
    }

    public String setExpr(){
        this.expression = "instruction";
        return expression;
    }

    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

//    @Override
//    protected void checkDecoration() {
//        if (getType() == null) {
//            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
//        }
//    }

    /**
     * Verify the expression for contextual error.
     *
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     *
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
                                     EnvironmentExp localEnv, ClassDefinition currentClass,
                                     Type expectedType)
            throws ContextualError {
        Type TypeExp=this.verifyExpr(compiler, localEnv, currentClass);
        if(expectedType.sameType(TypeExp)){
            return this;
        }

        if(TypeExp.isInt() && expectedType.isFloat()){
            AbstractExpr convExpr = new ConvFloat(this);
            Type convExprType = convExpr.verifyExpr(compiler, localEnv, currentClass);
            convExpr.setType(convExprType);
            this.setType(convExprType);
            return convExpr;
        }
        throw new ContextualError("Type incompatible : attendu " + expectedType.getName() +
                ", trouvé " + TypeExp.getName(), getLocation());

    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.verifyExpr(compiler, localEnv, currentClass);

    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */

    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
                         ClassDefinition currentClass) throws ContextualError {
        Type typeCondition = this.verifyExpr(compiler, localEnv, currentClass);

        if (!typeCondition.isBoolean())
        {
            throw new ContextualError("The type of the consdition must be boolean", this.getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */

    protected void codeGenPrint(DecacCompiler compiler){
        return;
    } ;

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }


    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    protected abstract DVal codeGenExpr(DecacCompiler compiler);


    public DVal codeGenInit(DecacCompiler compiler){
        return null;
    }

    protected void codeGenPrintx(DecacCompiler compiler){
        return;
    }

    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel){
        return null;
    }

    protected void codeGenInstClass(DecacCompiler compiler){};

    public void codeGenField(DecacCompiler compiler){};
}