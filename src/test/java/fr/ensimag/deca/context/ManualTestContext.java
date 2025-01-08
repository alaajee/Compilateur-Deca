package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Assign;
import fr.ensimag.deca.tree.DeclVar;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.deca.tree.ListDeclVar;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.NoInitialization;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.deca.tree.ReadInt;
import fr.ensimag.deca.tree.Main;
/**
 * Driver to test only code generation from a manually built AST.
 */
public class ManualTestContext {
    public static void main(String[] args) throws IOException {

        // Création du compilateur
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        // Récupération de l'environnement des types prédéfinis
        EnvironmentType envTypes = compiler.environmentType;
        ListDeclClass listClasses = new ListDeclClass();

        // Liste des déclarations de variables
        ListDeclVar listDeclVar = new ListDeclVar();

        // Création de la table des symboles et des identifiants
        SymbolTable symbolTable = compiler.symbolTable;
        Symbol symbolX = symbolTable.create("x");

        // Création de l'identifiant x
        Identifier idX = new Identifier(symbolX);

        // Utilisation du type prédéfini int
        Symbol name = envTypes.INT.getName();
        Identifier intType = new Identifier(name);

        // Déclaration de la variable : int x;
        listDeclVar.add(new DeclVar(intType, idX, new NoInitialization()));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Instruction : x = readInt();
        ReadInt readInt = new ReadInt();
        listInst.add(new Assign(idX, readInt));

        // Création du bloc main avec les déclarations et les instructions
        Main mainBlock = new Main(listDeclVar, listInst);

        // Création du programme complet
        AbstractProgram source = new Program(listClasses, mainBlock);

        // Génération du code
        source.codeGenProgram(compiler);

        // Affichage du programme généré
        source.prettyPrint(System.out);
    }
}
