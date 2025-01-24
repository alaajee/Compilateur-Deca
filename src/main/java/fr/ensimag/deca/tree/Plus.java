package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
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
            compiler.addInstruction(new fr.ensimag.ima.pseudocode.instructions.PUSH((GPRegister)leftOperand));
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
        ARMGPRegister regResult;
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            regResult = compiler.associerRegARMD();
            ARMconstructeur constructeur = new ARMconstructeurVADD();
            codeGenARM gen = new codeGenARM();
            regResult = gen.codeGenARMFloat(leftOperand, rightOperand, regResult, constructeur, compiler, getLeftOperand().getType().isFloat(), getRightOperand().getType().isFloat());
        }
        else {
            regResult = compiler.associerRegARM();
            ARMconstructeur constructeur = new ARMconstructeurADD();
            codeGenARM gen = new codeGenARM();
            regResult = gen.codeGenARM(leftOperand, rightOperand, regResult, constructeur, compiler);
            }
        return regResult;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new fr.ensimag.ima.pseudocode.instructions.PUSH((GPRegister)leftOperand));
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

    @Override
    protected void codeGenPrintARM(DecacCompiler compiler){
        compiler.print = true;
        DVal leftOperand = getLeftOperand().codeGenExprARM(compiler);
        DVal rightOperand = getRightOperand().codeGenExprARM(compiler);
        ARMGPRegister regResult;
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            regResult = compiler.associerRegARMD();
            ARMconstructeur constructeur = new ARMconstructeurVADD();
            codeGenARM gen = new codeGenARM();
            regResult = gen.codeGenARMFloat(leftOperand, rightOperand, regResult, constructeur, compiler, getLeftOperand().getType().isFloat(), getRightOperand().getType().isFloat());
            if(!compiler.printfloat){
                String line = "formatfloat" + ": .asciz " + "\"%f\"";
                compiler.addFirstComment(line);
                compiler.printfloat = true;
            };
            compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatfloat")));
            compiler.addInstruction(new VMOV(ARMRegister.R2,ARMRegister.R3, regResult));
        }
        else {
            regResult = compiler.associerRegARM();
            ARMconstructeur constructeur = new ARMconstructeurADD();
            codeGenARM gen = new codeGenARM();
            regResult = gen.codeGenARM(leftOperand, rightOperand, regResult, constructeur, compiler);
            if(!compiler.printint){
                String line = "formatint" + ": .asciz " + "\"%d\"";
                compiler.addFirstComment(line);
                compiler.printint = true;
            }
            compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatint")));    
            compiler.addInstruction(new MOV(ARMRegister.R1, regResult));
        }
        compiler.addInstruction(new BL(new ARMImmediateString("printf")));
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
