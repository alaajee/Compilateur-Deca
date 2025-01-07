// package fr.ensimag.deca.tree;

// import fr.ensimag.deca.DecacCompiler;
// import fr.ensimag.deca.context.*;
// import fr.ensimag.ima.pseudocode.*;
// import fr.ensimag.ima.pseudocode.instructions.*;
// import fr.ensimag.ima.pseudocode.instructionsARM.*;
// import fr.ensimag.deca.tools.SymbolTable;
// import java.util.List;

// public class CallMethod extends AbstractExpr {

//     private AbstractExpr object;
//     private AbstractIdentifier methodName;
//     private ListExpr params;

//     public CallMethod(AbstractExpr object, AbstractIdentifier methodName, ListExpr params) {
//         this.object = object;
//         this.methodName = methodName;
//         this.params = params;
//     }

//     @Override
//     protected void verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
//         // Verify the object
//         object.verifyExpr(compiler, localEnv, currentClass);
//         Type objectType = object.getType();
        
//         if (!objectType.isClass()) {
//             throw new ContextualError("Method can only be called on an object of a class type (rule 3.41)", getLocation());
//         }
        
//         ClassType classType = (ClassType) objectType;
//         EnvironmentExp classEnv = classType.getDefinition().getMembers();

//         // Verify the method exists in the class environment
//         ExpDefinition methodDef = classEnv.get(methodName.getName());
//         if (methodDef == null || !methodDef.isMethod()) {
//             throw new ContextualError("Method " + methodName.getName() + " not found in class " + classType.getName() + " (rule 3.41)", getLocation());
//         }
        
//         MethodDefinition methodDefinition = methodDef.asMethodDefinition("Expected a method definition");
//         methodName.setDefinition(methodDefinition);
//         methodName.setType(methodDefinition.getType());

//         // Verify parameters
//         List<Type> expectedParams = methodDefinition.getSignature().getArgs();
//         params.verifyListExpr(compiler, localEnv, currentClass, expectedParams);

//         // Set the type of the method call to the return type of the method
//         this.setType(methodDefinition.getType());
//     }

//     @Override
//     public void decompile(IndentPrintStream s) {
//         object.decompile(s);
//         s.print(".");
//         methodName.decompile(s);
//         s.print("(");
//         params.decompile(s);
//         s.print(")");
//     }

//     @Override
//     protected void codeGenInst(DecacCompiler compiler) {
//         // Reserve space for parameters on the stack
//         int paramCount = params.size();
//         if (paramCount > 0) {
//             compiler.addInstruction(new ADDSP(paramCount));
//         }

//         // Generate code for the parameters in reverse order
//         for (int i = paramCount - 1; i >= 0; i--) {
//             params.getList().get(i).codeGenInst(compiler);
//             compiler.addInstruction(new PUSH(Register.getR(0))); // Push each parameter
//         }

//         // Generate code for the object (receiver of the method)
//         object.codeGenInst(compiler);
//         compiler.addInstruction(new PUSH(Register.getR(0))); // Push the object reference

//         // Call the method
//         MethodDefinition methodDef = methodName.getMethodDefinition();
//         int methodIndex = methodDef.getIndex();
//         compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(0)), Register.getR(0))); // Load vtable
//         compiler.addInstruction(new BSR(new RegisterOffset(methodIndex, Register.getR(0)))); // Call method

//         // Adjust stack pointer back
//         compiler.addInstruction(new SUBSP(paramCount + 1)); // Parameters + object reference
//     }

//     @Override
//     protected void iterChildren(TreeFunction f) {
//         object.iter(f);
//         methodName.iter(f);
//         params.iter(f);
//     }

//     @Override
//     protected void prettyPrintChildren(PrintStream s, String prefix) {
//         object.prettyPrint(s, prefix, false);
//         methodName.prettyPrint(s, prefix, false);
//         params.prettyPrint(s, prefix, true);
//     }
// }
