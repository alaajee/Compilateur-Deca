package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

import java.util.LinkedList;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
        this.expression = "instruction";
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {

        boolean assign = compiler.isAssign;
        boolean var = compiler.isVar;
        compiler.isDiv = true;
        GPRegister reg = compiler.associerReg();
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new fr.ensimag.ima.pseudocode.instructions.PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        if (getRightOperand().getType().isInt()){
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        }
        else {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
        }
        compiler.addInstruction(new CMP(rightOperand, Register.R0));

        if (!compiler.getCompilerOptions().getNoCHeck()){
            compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        }
       // compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        if (assign) {

            if (compiler.typeAssign.equals("float")) {

                constructeurDIV constructeurDIV = new constructeurDIV();
                codeGen gen = new codeGen();
                if (rightOperand instanceof GPRegister) {
                    if (getRightOperand().getType().isInt()){
                        compiler.addInstruction(new FLOAT(rightOperand, (GPRegister) rightOperand));
                    }
                    compiler.addInstruction(new LOAD(leftOperand, Register.R0));
                    if (getRightOperand().getType().isInt()){
                        compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    }
                    gen.codeGenPrint(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    regis.name = "float";
                    compiler.isAssign = false;
                    compiler.init = false;
                    return regis;
                } else {
                    compiler.addInstruction(new LOAD(rightOperand, Register.R0));
                    if (getRightOperand().getType().isInt()){
                        compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    }
                    compiler.addInstruction(new LOAD(leftOperand, Register.R1));
                    if (getLeftOperand().getType().isInt()){
                        compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
                    }
                    //gen.codeGenPrint(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    regis.name = "float";
                    compiler.etatDivide = true;
                    return regis;
                }
            } else {
                constructeurQUO constructeurQUO = new constructeurQUO();
                codeGen gen = new codeGen();
                DVal regis = gen.codeGen(leftOperand, rightOperand, reg, constructeurQUO, compiler);
                return regis;
            }
        } else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }


    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExprARM(compiler);
        DVal rightOperand = getRightOperand().codeGenExprARM(compiler);
        ARMGPRegister regResult;
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            regResult = compiler.associerRegARMD();
            ARMconstructeur constructeur = new ARMconstructeurVDIV();
            codeGenARM gen = new codeGenARM();
            regResult = gen.codeGenARMFloat(leftOperand, rightOperand, regResult, constructeur, compiler, getLeftOperand().getType().isFloat(), getRightOperand().getType().isFloat());
        }
        else {
            if (leftOperand instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R0, leftOperand));
                compiler.addInstruction(new LDR(ARMRegister.R0, new ARMImmediateString("[R0]")));
            } else  {
                compiler.addInstruction(new MOV(ARMRegister.R0, leftOperand));
            }
            if (rightOperand instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R1, rightOperand));
                compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
            } else  {
                compiler.addInstruction(new MOV(ARMRegister.R1, leftOperand));
            }
            regResult = compiler.associerRegARM();
            compiler.addInstruction(new BL(new ARMImmediateString("__aeabi_idiv")));
            compiler.addInstruction(new MOV(regResult, ARMRegister.R0));
        }
        return regResult;
    }
    
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        if (leftOperand.isOffSet){
            compiler.addInstruction(new fr.ensimag.ima.pseudocode.instructions.PUSH((GPRegister)leftOperand));
        }
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");


        if (typeRight.isInt() && typeLeft.isInt()) {
            constructeurQUO constructeurQUO = new constructeurQUO();
            codeGen gen = new codeGen();
            gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurQUO, compiler);
            compiler.addInstruction(new WINT());
        } else if (typeRight.isFloat() || typeLeft.isFloat()) {
            constructeurDIV constructeurDIV = new constructeurDIV();
            codeGen gen = new codeGen();
            gen.codeGenPrint(leftOperand, rightOperand, reg, constructeurDIV, compiler);
            compiler.addInstruction(new WFLOAT());
        } else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
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
            ARMconstructeur constructeur = new ARMconstructeurVDIV();
            codeGenARM gen = new codeGenARM();
            regResult = gen.codeGenARMFloat(leftOperand, rightOperand, regResult, constructeur, compiler, getLeftOperand().getType().isFloat(), getRightOperand().getType().isFloat());
            if(!compiler.printfloat){
                String line = "formatfloat" + ": .asciz " + "\"%f\"";
                compiler.addFirstComment(line);
                compiler.printfloat = true;
            };
            compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatfloat")));
            compiler.addInstruction(new VMOV(ARMRegister.R3,ARMRegister.R3, regResult));
        }
        else {
            if (leftOperand instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R0, leftOperand));
                compiler.addInstruction(new LDR(ARMRegister.R0, new ARMImmediateString("[R0]")));
            } else  {
                compiler.addInstruction(new MOV(ARMRegister.R0, leftOperand));
            }
            if (rightOperand instanceof DAddr) {
                compiler.addInstruction(new LDR(ARMRegister.R1, rightOperand));
                compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
            } else  {
                compiler.addInstruction(new MOV(ARMRegister.R1, rightOperand));
            }
            regResult = compiler.associerRegARM();
            compiler.addInstruction(new BL(new ARMImmediateString("__aeabi_idiv")));
            compiler.addInstruction(new MOV(regResult, ARMRegister.R0));
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


    public DVal codeGenInit(DecacCompiler compiler) {
        boolean assign = compiler.isAssign;
        boolean var = compiler.isVar;
        compiler.isDiv = true;
        GPRegister reg = compiler.associerReg();
        DVal rightOperand = getRightOperand().codeGenInit(compiler);
        if (getRightOperand().getType().isInt()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        }
        else if (getRightOperand().getType().isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
        }
        compiler.addInstruction(new CMP(rightOperand, Register.R0));
        if (!compiler.getCompilerOptions().getNoCHeck()){
            compiler.addInstruction(new BEQ(compiler.labelMap.get("division_by_zero_error")));
        }
        DVal leftOperand = getLeftOperand().codeGenInit(compiler);
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        DAddr adresse = compiler.getCurrentAdresse();

        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        if (assign) {
            if (compiler.typeAssign.equals("float")) {
                constructeurDIV constructeurDIV = new constructeurDIV();
                codeGen gen = new codeGen();
                if (rightOperand instanceof GPRegister) {
                    if (getRightOperand().getType().isInt()) {
                        compiler.addInstruction(new FLOAT(rightOperand, (GPRegister) rightOperand));
                    }
                    compiler.addInstruction(new LOAD(leftOperand, Register.R0));
                    if (getRightOperand().getType().isInt()) {
                        compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    }
                    gen.codeGenPrint(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R0, rightOperand, reg, constructeurDIV, compiler);
                    compiler.addInstruction(new STORE((GPRegister)regis, adresse));
                    return regis;
                } else {
                    compiler.addInstruction(new LOAD(rightOperand, Register.R0));
                    if (getRightOperand().getType().isInt()){
                        compiler.addInstruction(new FLOAT(Register.R0, Register.R0));
                    }
                    compiler.addInstruction(new LOAD(leftOperand, Register.R1));
                    if (getLeftOperand().getType().isInt()) {
                        compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
                    }
                    gen.codeGenPrint(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    DVal regis = gen.codeGen(Register.R1, Register.R0, reg, constructeurDIV, compiler);
                    compiler.addInstruction(new STORE((GPRegister)regis, adresse));
                    return regis;
                }
            } else {

                constructeurQUO constructeurQUO = new constructeurQUO();
                codeGen gen = new codeGen();
                DVal regis = gen.codeGen(leftOperand, rightOperand, reg, constructeurQUO, compiler);
                compiler.addInstruction(new STORE((GPRegister)regis, adresse));
                return regis;
            }
        } else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }

    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines, GPRegister register){
        GPRegister reg = compiler.associerReg();

        // LeftOperand et RightOperand ...
        DVal leftOperand = getLeftOperand().codeGenInstClass(compiler,lines,reg);
        DVal rightOperand = getRightOperand().codeGenInstClass(compiler,lines,reg);

        if (!compiler.registeres.contains(reg)) {
            compiler.registeres.add(reg);
        }

        lines.add(new LOAD(new RegisterOffset(-2,Register.LB),reg));
        lines.add(new LOAD(leftOperand,reg));
        lines.add(new QUO(rightOperand,reg));
        compiler.libererReg(reg.getNumber());
        return reg;
    }

}

