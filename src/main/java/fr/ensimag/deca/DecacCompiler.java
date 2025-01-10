package fr.ensimag.deca;

import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl02
 * @date 01/01/2025
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public final SymbolTable symbolTable;
    public final EnvironmentType environmentType;

    public int adresseReg;
    public boolean isVar;
    private int adressVar;
    private int Overflow;
    private Map<String, VariableDefinition> varTab = new HashMap<>();
    private Map<Location,String> nameVal = new HashMap<>();
    private Map<String ,DAddr> regUn = new HashMap<>();
    private Boolean [] GP;
    public Boolean Offset;
    public int spVal;
    public int OverflowVal;

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.Offset = false;
        // Initialisation de symbolTable
        this.symbolTable = new SymbolTable();
        this.spVal = 0;
        this.OverflowVal = 2;
        // Initialisation de environmentType après symbolTable
        this.environmentType = new EnvironmentType(this);
        this.GP = new Boolean[OverflowVal+1];
        for(int i = 0 ; i < OverflowVal+1 ; i++){
            GP[i] = false;
        }
        GP[OverflowVal] = true;
        this.Overflow = 15;
        this.adressVar = 2;
        this.adresseReg = 2;
        this.isVar = false;


    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 
    /** The global environment for types (and the symbolTable) */
    // public final EnvironmentType environmentType = new EnvironmentType(this);
    // public final SymbolTable symbolTable = new SymbolTable();

    public Symbol createSymbol(String name) {
        return symbolTable.create(name);
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.substring(0, sourceFile.length()-4) + "ass";
        PrintStream err = System.err;
        PrintStream out = System.out;


        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());


        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    /**
     * Cette fonction permet d'associer une valeur à chaque variable et modifier setOperand pour cette variable
     * */

    public DAddr associerAdresse(){
        this.adressVar++;
        DAddr adresse = new RegisterOffset(adressVar,Register.GB);
        return adresse;
    }

    public void libererReg(int adresseReg){
        GP[adresseReg] = false;
    }

    public GPRegister associerReg() {
        for (int i = 2; i < OverflowVal+1; i++) {
            if (!GP[i]) {
                GP[i] = true;
                this.adresseReg = i;
                return Register.getR(i);
            } else {
                continue;
            }
        }
        return associerRegOffset();

    }

    public GPRegister associerRegOffset(){
        DVal reg = Register.getR(this.adresseReg);
        reg.isOffSet = true;
        return  (GPRegister) reg;
    }

    public int getAdresseReg(){
        return this.adresseReg;
    }
    public DAddr getCurrentAdresse(){
        return new RegisterOffset(adressVar,Register.GB);
    }

    public void libererReg(){
        this.adresseReg = this.adresseReg--;
    }

    public void libererAdresse(){
        this.adressVar = this.adressVar--;
    }

    public void addVar(VariableDefinition variableDefinition , String name){
        this.varTab.put(name,variableDefinition);
    }

    public Map<String,VariableDefinition> getVarTab(){
        return varTab;
    }

    public VariableDefinition getVar(String name){
        return this.varTab.get(name);
    }

    public void addNameVal(Location location, String name){
        this.nameVal.put(location,name);
    }

    public String getNameVal(String name){
        return this.nameVal.get(name).toString();
    }

    public Map<Location,String> getNameValTab(){
        return nameVal;
    }

    public Map<String , DAddr> getRegUn(){
        return regUn;
    }


    public DAddr getRegUn(String name){
        return this.regUn.get(name);
    }

    public void addRegUn(String name, DAddr reg){
        this.regUn.put(name,reg);
    }

    public void modifierRegun(DAddr reg, String name){
        this.regUn.put(name,reg);
    }

    public int getOverflow(){
        return this.Overflow;
    }

    public GPRegister getRegister(int adresse){
        return Register.getR(adresse);
    }



}
