package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ARMconstructeur;
import fr.ensimag.deca.codegen.ARMconstructeurADD;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.codeGenARM;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.deca.codegen.constructeurADD;
import fr.ensimag.deca.codegen.constructeurMUL;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;


import java.util.LinkedList;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
        this.expression = "instruction";
    }
    
    @Override
    protected String getOperatorName() {
        return "+";
    }


    public String setExpr(){
        this.expression = "instruction";
        return expression;
    }

    public String getExpr(){
        return expression;
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
            compiler.incrementTsto();
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        if (leftOperand instanceof GPRegister){
            constructeur constructeur = new constructeurADD();
            codeGen gen = new codeGen();
            DVal register = gen.codeGen(leftOperand,rightOperand,(GPRegister) leftOperand,constructeur,compiler);
            gen.finalizeAndPush((GPRegister) register, compiler);
            return leftOperand;
        }
        else {
            GPRegister reg = compiler.associerReg();
            constructeur constructeur = new constructeurADD();
            codeGen gen = new codeGen();
            DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
            gen.finalizeAndPush((GPRegister) register, compiler);
            return register;
        }
    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExprARM(compiler);
        DVal rightOperand = getRightOperand().codeGenExprARM(compiler);
        ARMGPRegister regLeft = compiler.associerRegARM();
        ARMGPRegister regRight = compiler.associerRegARM();
        ARMGPRegister regResult = compiler.associerRegARM();
        if (leftOperand instanceof DAddr) {
            compiler.addInstruction(new LDR(regLeft, leftOperand));
            compiler.addInstruction(new LDR(regLeft, new ARMImmediateString("[R"+regLeft.getNumber()+"]")));
        } else {
            compiler.addInstruction(new MOV(regLeft, leftOperand));
        }
    
        if (rightOperand instanceof DAddr) {
            compiler.addInstruction(new LDR(regRight, rightOperand));
            compiler.addInstruction(new LDR(regRight, new ARMImmediateString("[R"+regRight.getNumber()+"]")));
        } else {
            compiler.addInstruction(new MOV(regRight, rightOperand));
        }
        compiler.addInstruction(new fr.ensimag.arm.pseudocode.instructions.ADD(regResult, regLeft, regRight));
        compiler.libererRegARM(regLeft.getNumber());
        compiler.libererRegARM(regRight.getNumber());
        return regResult;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new PUSH((GPRegister)leftOperand));
            compiler.incrementTsto();
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        constructeur constructeurADD = new constructeurADD();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurADD,compiler);
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }
    }

    public DVal codeGenInit(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenInit(compiler);
        DVal rightOperand = getRightOperand().codeGenInit(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurADD();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        gen.finalizeAndPush(reg, compiler);
        DAddr adresse = compiler.getCurrentAdresse();
        compiler.addInstruction(new STORE((GPRegister)register, adresse));
        return register;
    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines,GPRegister register){
        GPRegister reg = compiler.associerReg();

        // LeftOperand et RightOperand ...
        DVal leftOperand = getLeftOperand().codeGenInstClass(compiler,lines,reg);
        DVal rightOperand = getRightOperand().codeGenInstClass(compiler,lines,reg);

        if (!compiler.registeres.contains(reg)) {
            compiler.registeres.add(reg);
        }

        lines.add(new LOAD(new RegisterOffset(-2,Register.LB),reg));
        lines.add(new LOAD(leftOperand,reg));
        lines.add(new fr.ensimag.ima.pseudocode.instructions.ADD(rightOperand,reg));
        // DVal dval = getRightOperand().codeGenExpr(compiler);
        compiler.libererReg(reg.getNumber());
        return reg;
    }
}
