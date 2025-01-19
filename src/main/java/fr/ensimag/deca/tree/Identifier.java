package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.LinkedList;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Identifier extends AbstractIdentifier {
    private static final Logger log = Logger.getLogger(Identifier.class);

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public 
    Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                ExpDefinition definitionExp=localEnv.get(name);
                if(definitionExp == null){
                    throw new ContextualError("Identifier  '" + this.name + "' is not defined", this.getLocation());
                }


                this.setDefinition(definitionExp);
                this.setType(definitionExp.getType());
                return definitionExp.getType();            
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        TypeDefinition definitionT = compiler.environmentType.getEnvtypes().get(name);
        if (definitionT == null)
        {
            throw new ContextualError("Identifier ' " + this.name + " ' is not defined", this.getLocation());
        }
        this.setDefinition(definitionT);
        this.setType(definitionT.getType());
        return definitionT.getType();
        
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }

    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        String name = getName().toString();
        DAddr reg = compiler.getRegUn(name);
        if (compiler.isVar){
            GPRegister register = compiler.associerReg();
            compiler.addInstruction(new LOAD(reg,register));
            if (this.isParam){
                System.out.println(name);
            }
            compiler.addInstruction(new STORE(register,compiler.getCurrentAdresse()));
            compiler.isVar = false;
            compiler.libererReg(register.getNumber());
        }
        if (this.getExpDefinition().isField()){
            GPRegister register = compiler.associerReg();
            System.out.println(compiler.getFieldNombre());
            compiler.addInstruction(new LOAD(compiler.getCurrentAdresse(),register));
            compiler.addInstruction(new LOAD(new RegisterOffset(compiler.getTableNombreField(name),register),register));
            compiler.addInstruction(new STORE(register,compiler.getCurrentAdresse()));
            compiler.libererReg(register.getNumber());
        }
        return reg;

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        String name = getName().toString();
        DAddr register = compiler.getRegUn(name);
        compiler.addInstruction(new LOAD(register, Register.R1));
        if (getType().isInt()){
            compiler.addInstruction(new WINT());
        }
        else if (getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler) {
        String name = getName().toString();
        VariableDefinition variableDefinition = compiler.getVarTab().get(name);
        DAddr adresse = variableDefinition.getOperand();
        return adresse;
    }

    @Override
    protected void codeGenPrintx(DecacCompiler compiler) {
        String name = getName().toString();
        DAddr register = compiler.getRegUn(name);
        compiler.addInstruction(new LOAD(register, Register.R1));
        compiler.addInstruction(new WFLOATX());
    }

    public DVal codeGenInstrCond(DecacCompiler compiler, Label endLabel, Label bodyLabel) {
        // Récupère le nom et l'adresse du registre
        String name = getName().toString();
        DAddr register = compiler.getRegUn(name);

        // Charge la valeur du registre dans R1
        compiler.addInstruction(new LOAD(register, Register.R1));

        // Compare R1 avec 0
        compiler.addInstruction(new CMP(0, Register.R1));  // Comparer R1 à 0

        if (compiler.notCond){
            compiler.addInstruction(new BNE(endLabel));  // Si res == 0, saute à endLabel
            //compiler.addInstruction(new BRA(bodyLabel));  // Sinon, saute à bodyLabel
            compiler.notCond = false;
        }// Si res == 1, sauter à bodyLabel, sinon sauter à endLabel

        else {
            if (compiler.or){
                if (compiler.compteurOr == 1){
                    if (compiler.notCond){
                        compiler.addInstruction(new BNE(endLabel));
                        compiler.notCond = false;
                    }
                    else {
                        compiler.addInstruction(new BEQ(endLabel));
                    }
                }
                else {
                    compiler.addInstruction(new BEQ(endLabel));
                }
                compiler.compteurOr--;
            }
            else {
                compiler.addInstruction(new BEQ(endLabel));  // Si res == 0, saute à endLabel
            }
            //compiler.addInstruction(new BRA(bodyLabel));  // Sinon, saute à bodyLabel
        }
        return register;
    }


    @Override
    protected  DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines,GPRegister register){
        String name = getName().toString();
        int i = compiler.getRegisterOffset(name);
        if (i < 0){
            DAddr adresse = new RegisterOffset(i,Register.LB);
            return adresse;
        }
        else {
            DAddr registre = new RegisterOffset(i,register);
            return registre;
        }
    }

    @Override
    protected DVal codeGenClassPrint(DecacCompiler compiler, LinkedList<Instruction> lines){
        String name = getName().toString();
        int i = compiler.getRegisterOffset(name);
        GPRegister register = compiler.associerReg();
        lines.add(new LOAD(new RegisterOffset(-2,Register.LB),register));
        lines.add(new LOAD(new RegisterOffset(i,register),Register.R1));
        lines.add(new WINT());
        return register;
    }
}