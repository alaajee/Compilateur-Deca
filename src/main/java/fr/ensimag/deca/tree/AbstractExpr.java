package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.LinkedList;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public abstract class AbstractExpr extends AbstractInst {

    private Type type;
    public String expression = "general";

    public String getExpr() {
        return expression;
    }

    public String setExpr() {
        this.expression = "instruction";
        return expression;
    }

    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    /**
     * Verify the expression for contextual error.
     * Implements non-terminals "expr" and "lvalue" of [SyntaxeContextuelle] in pass 3.
     *
     * @param compiler contains the "env_types" attribute
     * @param localEnv Environment in which the expression should be checked
     * @param currentClass Definition of the class containing the expression, or null in the main block
     * @return the Type of the expression
     */
    public abstract Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments.
     * Implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3.
     *
     * @param compiler contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
                                     EnvironmentExp localEnv, ClassDefinition currentClass,
                                     Type expectedType) throws ContextualError {
        Type typeExp = this.verifyExpr(compiler, localEnv, currentClass);

        if (expectedType.sameType(typeExp)) {
            return this;
        }

        if (typeExp.isInt() && expectedType.isFloat()) {
            AbstractExpr convExpr = new ConvFloat(this);
            convExpr.setType(convExpr.verifyExpr(compiler, localEnv, currentClass));
            return convExpr;
        }

        if (typeExp.isClass() && expectedType.isClass()) {
            ClassType classTypeExp = (ClassType) typeExp;
            ClassType classExpectedType = (ClassType) expectedType;
            if (classTypeExp.isSubClassOf(classExpectedType)) {
                return this;
            }
        }

        if (typeExp.isNull() && expectedType.isClass()) {
            return this;
        }

        throw new ContextualError("Type incompatible : attendu " + expectedType.getName() +
                ", trouvé " + typeExp.getName(), getLocation());
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is boolean.
     *
     * @param localEnv Environment in which the condition should be checked.
     * @param currentClass Definition of the class containing the expression, or null in the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
                         ClassDefinition currentClass) throws ContextualError {
        Type typeCondition = this.verifyExpr(compiler, localEnv, currentClass);

        if (!typeCondition.isBoolean()) {
            throw new ContextualError("The type of the condition must be boolean", this.getLocation());
        }
    }

    /**
     * Generate code to print the expression.
     *
     * @param compiler the compiler instance
     */
    protected void codeGenPrint(DecacCompiler compiler) {
    }

    protected void codeGenPrintARM(DecacCompiler compiler){
        return;
    } ;

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExpr(compiler);
    }

    @Override
    protected void codeGenInstARM(DecacCompiler compiler) {
        codeGenExprARM(compiler);
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

    protected abstract DVal codeGenExprARM(DecacCompiler compiler);

    public DVal codeGenInit(DecacCompiler compiler){
        return null;
    }

    public DVal codeGenInitARM(DecacCompiler compiler){
        return null;
    }

    protected void codeGenPrintx(DecacCompiler compiler){
        return;
    }

    protected void codeGenPrintxARM(DecacCompiler compiler){
        return;
    }

    public DVal codeGenInstrCond(DecacCompiler compiler,Label endLabel,Label bodyLabel){
        return null;
    }

    protected DVal codeGenInstClass(DecacCompiler compiler) {
        return null;
    }

    public void codeGenField(DecacCompiler compiler) {
    }

    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines, GPRegister register) {
        return null;
    }

    protected DVal codeGenClassPrint(DecacCompiler compiler, LinkedList<Instruction> lines) {
        return null;
    }

    public DVal codeGenInitParam(DecacCompiler compiler, int i) {
        return null;
    }
}
