package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.ClassDefinition;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.Tree;




public abstract class AbstractBlock extends Tree {
    public abstract void verifyBlock(DecacCompiler compiler, EnvironmentExp localEnv,ClassDefinition currentClass,Type t)
            throws ContextualError;

}