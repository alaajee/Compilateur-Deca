// Generated from /home/ensimag/ensimag/GL/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaParser.g4 by ANTLR 4.13.2

    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DecaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DecaParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DecaParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(DecaParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(DecaParser.MainContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(DecaParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_decl(DecaParser.List_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#decl_var_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_var_set(DecaParser.Decl_var_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_decl_var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_decl_var(DecaParser.List_decl_varContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#decl_var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_var(DecaParser.Decl_varContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_inst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_inst(DecaParser.List_instContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#inst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInst(DecaParser.InstContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#if_then_else}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_then_else(DecaParser.If_then_elseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_expr(DecaParser.List_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(DecaParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#assign_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_expr(DecaParser.Assign_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#or_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_expr(DecaParser.Or_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#and_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_expr(DecaParser.And_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#eq_neq_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq_neq_expr(DecaParser.Eq_neq_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#inequality_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInequality_expr(DecaParser.Inequality_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#sum_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSum_expr(DecaParser.Sum_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#mult_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult_expr(DecaParser.Mult_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#unary_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_expr(DecaParser.Unary_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#select_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_expr(DecaParser.Select_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#primary_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary_expr(DecaParser.Primary_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(DecaParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(DecaParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(DecaParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_classes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_classes(DecaParser.List_classesContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#class_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_decl(DecaParser.Class_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#class_extension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_extension(DecaParser.Class_extensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#class_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_body(DecaParser.Class_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#decl_field_set}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_field_set(DecaParser.Decl_field_setContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#visibility}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVisibility(DecaParser.VisibilityContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_decl_field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_decl_field(DecaParser.List_decl_fieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#decl_field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_field(DecaParser.Decl_fieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#decl_method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl_method(DecaParser.Decl_methodContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#list_params}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList_params(DecaParser.List_paramsContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#multi_line_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulti_line_string(DecaParser.Multi_line_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link DecaParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(DecaParser.ParamContext ctx);
}