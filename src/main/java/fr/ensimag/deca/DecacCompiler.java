package fr.ensimag.deca;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.TypeDefinition;
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
import fr.ensimag.arm.pseudocode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUB;
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

    public  SymbolTable symbolTable;
    public  EnvironmentType environmentType;
    public Map<Symbol, TypeDefinition> envTypes;

    private int uniqueIDCounter = 0;
    private int uniqueDataID = 0;
    public boolean print = false;
    public boolean println = false;
    public boolean printint = false;
    public boolean printfloat = false;
    public boolean zeroARM = false;

    public int adresseReg;
    public int adresseRegS;
    public int adresseRegD;
    public boolean isVar;
    private int adressVar;
    private Map<String, VariableDefinition> varTab = new HashMap<>();
    private Map<Location,String> nameVal = new HashMap<>();
    private Map<String ,DAddr> regUn = new HashMap<>();
    public Map<String ,DAddr> regUnARM = new HashMap<>();
    private Boolean [] GP;
    public Boolean Offset;
    public int spVal;
    public int OverflowVal;
    public int OverflowValARM;
    public boolean isAssign;
    public String typeAssign;
    public boolean needToPush;
    public boolean label;
    public Map<String,Label> labelMap ;
    public boolean isArith;
    public boolean isDiv;
    public int nbrVar = 0;
    private int currentTsto = 0;
    private int maxTsto =0;
    private int nbreField = 0;
    public boolean isHex;
    private Boolean [] RegistersARM;
    private Boolean [] RegistersARMS;
    private Boolean [] RegistersARMD;

    public boolean and;
    public boolean or;
    public boolean condition;
    public int compteurOr = 1 ;
    public boolean ifcond = false;
    public boolean notCond = false;
    public Label endIfLabel;
    public boolean weAreinWhile;
    public DAddr adresseClasse;
    public int RegisterOffset = 1;
    public int paramReg = -3;
    public int regPush = 0;
    public LinkedList<Register> registeres = new LinkedList<Register>();;

    private Map<String ,Integer> registerOffsets = new HashMap<>();

    private Map<String ,DAddr> tableClassees = new HashMap<>();

    private Map<String ,Integer> tableFields = new HashMap<>();

    private Map<String ,Integer> AdresseMethode = new HashMap<>();

    private Map<String  ,Integer> FieldNombre= new HashMap<>();


    public int AdresseMethodeOffset = 2;
    public Label nouvLabel = new Label("And");
    public boolean compteurAnd = false;
    public int compterLabel = 0;

    public boolean tas_plein = false;
    public Label labelClasses = new Label("Tables.DES.Methodes");
    public boolean init = false;
    public boolean Print = false;
    public int LabelFinWhile = 2;
    public int debut = 0;
    public boolean etatDivide = false;

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.Offset = false;
        this.symbolTable = new SymbolTable();
        this.environmentType = new EnvironmentType(this);
        this.envTypes = environmentType.getEnvtypes();
        this.spVal = 0;
        this.OverflowVal = 15;
        this.OverflowValARM = 12;
        if (compilerOptions != null){
            this.OverflowVal = compilerOptions.getRegistreLimitValue();
        }
        this.GP = new Boolean[OverflowVal+1];
        for(int i = 0 ; i < OverflowVal+1 ; i++){
            GP[i] = false;
        }
        this.RegistersARM = new Boolean[OverflowValARM+1];
        for(int i = 0 ; i < OverflowValARM+1 ; i++){
            RegistersARM[i] = false;
        }

        this.RegistersARMS = new Boolean[32];
        for(int i = 0 ; i < 32 ; i++){
            RegistersARMS[i] = false;
        }

        this.RegistersARMD = new Boolean[16];
        for(int i = 0 ; i < 16 ; i++){
            RegistersARMD[i] = false;
        }
        DVal reg = Register.getR(this.OverflowVal);
        reg.isOffSet = true;
        this.adressVar = 2;
        this.adresseReg = 2;
        this.isVar = false;
        this.isAssign = false;
        this.needToPush = false;
        this.label = false;
        this.labelMap = new HashMap<>();
        Label label = new Label("io_error");
        this.labelMap.put("io_error",label);
        label = new Label("overflow_error");
        this.labelMap.put("overflow_error",label);
        this.isArith = false;
        label = new Label("division_by_zero_error");
        this.labelMap.put("division_by_zero_error",label);
        this.isDiv = false;
        this.endIfLabel = new Label("end_if_" + getUniqueID());
        this.weAreinWhile = false;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    public int getUniqueDataID(){
        return uniqueDataID++;
    }

    public int getUniqueID() {
        return uniqueIDCounter++;
    }
    public void incrementTsto() {
        currentTsto++;
        if (currentTsto > maxTsto) {
            maxTsto = currentTsto; // Mettre à jour le maximum
        }
    }

    public void decrementTsto() {
        currentTsto--; // Décrémenter la taille actuelle
    }

    public int getMaxTsto() {
        return maxTsto; // Récupérer la taille maximale atteinte
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

    public void addFirst(Instruction instruction) {
        program.addFirst(instruction);
    }


    public void addFirst(Instruction instruction, String comment) {
        program.addFirst(instruction, comment);
    }
    
    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    public void addInstruction(ARMInstruction instruction) {
        ARMprogram.addInstruction(instruction);
    }
    
    public void addFirstComment(String comment){
        ARMprogram.addFirstComment(comment);
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

    private final ARMProgram ARMprogram = new ARMProgram();
 
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
            if (compilerOptions.getParse())
            {
                AbstractProgram program = doLexingAndParsing(sourceFile, err);
                program.decompile(out);
                return false;

            }

            if (compilerOptions.getVerify())
            {
                AbstractProgram program = doLexingAndParsing(sourceFile, err);
                program.verifyProgram(this);
                /*
                 * pour afficher l'arbre syntaxique decoree
                 * decommenter la ligne suivante
                 */

                //System.out.println(program.prettyPrint());
                return false;

            }




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
//        if (this.adressVar == 4) {
//            this.adressVar++;
//        }
//        else if (this.adressVar == 3) {
//            this.adressVar++;
//            this.adressVar++;
//        }
        DAddr adresse = new RegisterOffset(adressVar,Register.GB);
        return adresse;
    }

    public DAddr associerAdresseARM(){
        DAddr adresse = new ARMRegisterOffset(uniqueDataID);
        return adresse;
    }

    public void libererReg(int adresseReg){
        GP[adresseReg] = false;
    }

    public void libererRegARM(int adresseReg){
        RegistersARM[adresseReg] = false;
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

    public ARMGPRegister associerRegARMD() {
        for (int i = 2; i < 15; i++) {
            if (!RegistersARMD[i]) {
                RegistersARMD[i] = true;
                this.adresseRegD = i;
                return ARMRegister.getD(i);
            } else {
                continue;
            }
        }
        return associerRegOffsetARMD();

    }

    public ARMGPRegister associerRegARMS() {
        for (int i = 2; i < 32; i++) {
            if (!RegistersARMS[i]) {
                RegistersARMS[i] = true;
                this.adresseRegS = i;
                return ARMRegister.getS(i);
            } else {
                continue;
            }
        }
        return associerRegOffsetARMS();

    }

    public ARMGPRegister associerRegARM(){
        for (int i = 4; i < OverflowValARM+1; i++){
            if(!RegistersARM[i]){
                RegistersARM[i] = true;
                this.adresseReg = i;
                return ARMRegister.getR(i);
            }else{
                continue;
            }
        }
        return associerRegOffsetARM();
    }

    public boolean hasAvailableregARM(){
        for (int i = 4; i < OverflowValARM+1; i++){
            if(!RegistersARM[i]){
                return true;
            }
        }
        return false;

    }

    public GPRegister associerRegOffset(){
        DVal reg = Register.getR(this.adresseReg);
        reg.isOffSet = true;
        return  (GPRegister) reg;
    }

    public ARMGPRegister associerRegOffsetARM(){
        DVal reg = ARMRegister.getR(this.adresseReg);
        reg.isOffSet = true;
        return  (ARMGPRegister) reg;
    }

    public ARMGPRegister associerRegOffsetARMS(){
        DVal reg = ARMRegister.getS(this.adresseRegS);
        reg.isOffSet = true;
        return  (ARMGPRegister) reg;
    }

    public ARMGPRegister associerRegOffsetARMD(){
        DVal reg = ARMRegister.getD(this.adresseRegD);
        reg.isOffSet = true;
        return  (ARMGPRegister) reg;
    }


    public int getAdresseReg(){
        return this.adresseReg;
    }
    public DAddr getCurrentAdresse(){
        return new RegisterOffset(adressVar,Register.GB);
    }
    public DAddr getCurrentAdresseARM(){
        // return new ARMRegisterOffset(adressVar, )
        return null;
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

    public DAddr getRegUnARM(String name){
        return this.regUnARM.get(name);
    }

    public void addRegUn(String name, DAddr reg){
        this.regUn.put(name,reg);
    }

    public void addRegUnARM(String name, DAddr reg){
        this.regUnARM.put(name,reg);
    }

    public void modifierRegun(DAddr reg, String name){
        this.regUn.put(name,reg);
    }

    public int getOverflowVal(){
        return this.OverflowVal;
    }

    public GPRegister getRegister(int adresse){
        return Register.getR(adresse);
    }

    public RegisterOffset getRegisterClass(){

        RegisterOffset reg = new RegisterOffset(this.RegisterOffset,Register.R1);
        this.RegisterOffset++;
        //System.out.println(this.RegisterOffset);
        return reg;
    }

    public RegisterOffset getRegisterParam(){
        RegisterOffset reg = new RegisterOffset(this.paramReg,Register.LB);
        this.paramReg--;
        return reg;
    }

    public int getAdressVar(){
        return this.adressVar;
    }

    public int getNbreField(){
        return this.nbreField;
    }

    public void setNbreField(){
        this.nbreField++;
    }

    public Map<String,Integer> getRegisterOffsets(){
        return registerOffsets;
    }

    public Integer getRegisterOffset(String name){
        return this.registerOffsets.get(name);
    }

    public void setRegisterOffsets(String name,Integer offset){
         this.registerOffsets.put(name,offset);
    }

    public int getCurrentId(){
        return uniqueIDCounter;
    }
    public Map<String,DAddr> getTableClassees(){
        return tableClassees;
    }

    public DAddr getTableClassee(String name){
        return tableClassees.get(name);
    }

    public void setTableClassee(String name , DAddr tableClassee) {
        this.tableClassees.put(name,tableClassee);
    }

    public void addInstructionAfterLabel(Label label,  Instruction instruction) {
        program.addInstructionAfterLabel(label,instruction);
    }

    public Integer getTableFields(String name){
        return tableFields.get(name);
    }

    public void setTableFields(String name , int fields) {
        this.tableFields.put(name,fields);
    }

    public Integer getTableAdresseMethode(String name){
        return AdresseMethode.get(name);
    }

    public void setTableAdresseMethode(String name , int fields) {
        this.AdresseMethode.put(name,fields);
    }

    public void setTableNombreField(String name, int fields) {
        this.FieldNombre.put(name,fields);
    }

    public int getTableNombreField(String name){
        return this.FieldNombre.get(name);
    }

    public Map<String ,Integer> getFieldNombre(){
        return FieldNombre;
    }
    
    public boolean compileARM() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.substring(0, sourceFile.length()-4) + "s";
        PrintStream err = System.err;
        PrintStream out = System.out;


        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            if (compilerOptions.getParse())
            {   
                AbstractProgram ARMprogram = doLexingAndParsing(sourceFile, err);
                ARMprogram.decompile(out);
                return false;

            }

            if (compilerOptions.getVerify())
            {
                AbstractProgram ARMprogram = doLexingAndParsing(sourceFile, err);
                ARMprogram.verifyProgram(this);
                /*
                * pour afficher l'arbre syntaxique decoree
                * decommenter la ligne suivante
                */

                //System.out.println(program.prettyPrint());
                return false;

            }
            return doCompileARM(sourceFile, destFile, out, err);
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
    private boolean doCompileARM(String sourceName, String destName,
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
        prog.codeGenProgramARM(this);
        LOG.debug("Generated assembly code:" + nl + ARMprogram.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        ARMprogram.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }
}


