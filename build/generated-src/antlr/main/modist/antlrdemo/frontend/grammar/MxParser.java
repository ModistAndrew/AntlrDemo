// Generated from modist/antlrdemo/frontend/grammar/Mx.g4 by ANTLR 4.13.1

    package modist.antlrdemo.frontend.grammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VOID=1, BOOL=2, INT=3, STRING=4, NEW=5, CLASS=6, NULL=7, THIS=8, IF=9, 
		ELSE=10, FOR=11, WHILE=12, BREAK=13, CONTINUE=14, RETURN=15, IntegerLiteral=16, 
		BooleanLiteral=17, StringLiteral=18, FormatStringBegin=19, FormatStringEnd=20, 
		FormatStringMiddle=21, FormatStringAtom=22, Identifier=23, ADD=24, SUB=25, 
		MUL=26, DIV=27, MOD=28, GT=29, LT=30, GE=31, LE=32, NE=33, EQ=34, LOGICAL_AND=35, 
		LOGICAL_OR=36, LOGICAL_NOT=37, SHL=38, SHR=39, AND=40, OR=41, XOR=42, 
		NOT=43, ASSIGN=44, INC=45, DEC=46, DOT=47, LBRACK=48, RBRACK=49, LPAREN=50, 
		RPAREN=51, QUESTION=52, COLON=53, SEMI=54, COMMA=55, LBRACE=56, RBRACE=57, 
		WhiteSpace=58, LineComment=59, BlockComment=60;
	public static final int
		RULE_program = 0, RULE_classDefinition = 1, RULE_functionDefinition = 2, 
		RULE_constructorDefinition = 3, RULE_parameterDefinition = 4, RULE_block = 5, 
		RULE_statement = 6, RULE_variableDefinitionsBody = 7, RULE_variableDefinitions = 8, 
		RULE_variableDeclarator = 9, RULE_forInitialization = 10, RULE_expression = 11, 
		RULE_arrayCreator = 12, RULE_expressionOrArray = 13, RULE_array = 14, 
		RULE_formatString = 15, RULE_literal = 16, RULE_argumentList = 17, RULE_condition = 18, 
		RULE_type = 19, RULE_typeName = 20, RULE_emptyBracketPair = 21, RULE_expressionBracketPair = 22, 
		RULE_possibleBracketPair = 23, RULE_emptyParenthesisPair = 24;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "classDefinition", "functionDefinition", "constructorDefinition", 
			"parameterDefinition", "block", "statement", "variableDefinitionsBody", 
			"variableDefinitions", "variableDeclarator", "forInitialization", "expression", 
			"arrayCreator", "expressionOrArray", "array", "formatString", "literal", 
			"argumentList", "condition", "type", "typeName", "emptyBracketPair", 
			"expressionBracketPair", "possibleBracketPair", "emptyParenthesisPair"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'void'", "'bool'", "'int'", "'string'", "'new'", "'class'", "'null'", 
			"'this'", "'if'", "'else'", "'for'", "'while'", "'break'", "'continue'", 
			"'return'", null, null, null, null, null, null, null, null, "'+'", "'-'", 
			"'*'", "'/'", "'%'", "'>'", "'<'", "'>='", "'<='", "'!='", "'=='", "'&&'", 
			"'||'", "'!'", "'<<'", "'>>'", "'&'", "'|'", "'^'", "'~'", "'='", "'++'", 
			"'--'", "'.'", "'['", "']'", "'('", "')'", "'?'", "':'", "';'", "','", 
			"'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "VOID", "BOOL", "INT", "STRING", "NEW", "CLASS", "NULL", "THIS", 
			"IF", "ELSE", "FOR", "WHILE", "BREAK", "CONTINUE", "RETURN", "IntegerLiteral", 
			"BooleanLiteral", "StringLiteral", "FormatStringBegin", "FormatStringEnd", 
			"FormatStringMiddle", "FormatStringAtom", "Identifier", "ADD", "SUB", 
			"MUL", "DIV", "MOD", "GT", "LT", "GE", "LE", "NE", "EQ", "LOGICAL_AND", 
			"LOGICAL_OR", "LOGICAL_NOT", "SHL", "SHR", "AND", "OR", "XOR", "NOT", 
			"ASSIGN", "INC", "DEC", "DOT", "LBRACK", "RBRACK", "LPAREN", "RPAREN", 
			"QUESTION", "COLON", "SEMI", "COMMA", "LBRACE", "RBRACE", "WhiteSpace", 
			"LineComment", "BlockComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public List<ClassDefinitionContext> classDefinition() {
			return getRuleContexts(ClassDefinitionContext.class);
		}
		public ClassDefinitionContext classDefinition(int i) {
			return getRuleContext(ClassDefinitionContext.class,i);
		}
		public List<VariableDefinitionsContext> variableDefinitions() {
			return getRuleContexts(VariableDefinitionsContext.class);
		}
		public VariableDefinitionsContext variableDefinitions(int i) {
			return getRuleContext(VariableDefinitionsContext.class,i);
		}
		public List<FunctionDefinitionContext> functionDefinition() {
			return getRuleContexts(FunctionDefinitionContext.class);
		}
		public FunctionDefinitionContext functionDefinition(int i) {
			return getRuleContext(FunctionDefinitionContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8388702L) != 0)) {
				{
				setState(53);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(50);
					classDefinition();
					}
					break;
				case 2:
					{
					setState(51);
					variableDefinitions();
					}
					break;
				case 3:
					{
					setState(52);
					functionDefinition();
					}
					break;
				}
				}
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassDefinitionContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(MxParser.CLASS, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LBRACE() { return getToken(MxParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxParser.RBRACE, 0); }
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public List<VariableDefinitionsContext> variableDefinitions() {
			return getRuleContexts(VariableDefinitionsContext.class);
		}
		public VariableDefinitionsContext variableDefinitions(int i) {
			return getRuleContext(VariableDefinitionsContext.class,i);
		}
		public List<FunctionDefinitionContext> functionDefinition() {
			return getRuleContexts(FunctionDefinitionContext.class);
		}
		public FunctionDefinitionContext functionDefinition(int i) {
			return getRuleContext(FunctionDefinitionContext.class,i);
		}
		public List<ConstructorDefinitionContext> constructorDefinition() {
			return getRuleContexts(ConstructorDefinitionContext.class);
		}
		public ConstructorDefinitionContext constructorDefinition(int i) {
			return getRuleContext(ConstructorDefinitionContext.class,i);
		}
		public ClassDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDefinitionContext classDefinition() throws RecognitionException {
		ClassDefinitionContext _localctx = new ClassDefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_classDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(CLASS);
			setState(59);
			match(Identifier);
			setState(60);
			match(LBRACE);
			setState(66);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8388638L) != 0)) {
				{
				setState(64);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(61);
					variableDefinitions();
					}
					break;
				case 2:
					{
					setState(62);
					functionDefinition();
					}
					break;
				case 3:
					{
					setState(63);
					constructorDefinition();
					}
					break;
				}
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(69);
			match(RBRACE);
			setState(70);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDefinitionContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<ParameterDefinitionContext> parameterDefinition() {
			return getRuleContexts(ParameterDefinitionContext.class);
		}
		public ParameterDefinitionContext parameterDefinition(int i) {
			return getRuleContext(ParameterDefinitionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxParser.COMMA, i);
		}
		public FunctionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDefinitionContext functionDefinition() throws RecognitionException {
		FunctionDefinitionContext _localctx = new FunctionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_functionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			type();
			setState(73);
			match(Identifier);
			setState(74);
			match(LPAREN);
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8388638L) != 0)) {
				{
				setState(75);
				parameterDefinition();
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(76);
					match(COMMA);
					setState(77);
					parameterDefinition();
					}
					}
					setState(82);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(85);
			match(RPAREN);
			setState(86);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorDefinitionContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public EmptyParenthesisPairContext emptyParenthesisPair() {
			return getRuleContext(EmptyParenthesisPairContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ConstructorDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstructorDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorDefinitionContext constructorDefinition() throws RecognitionException {
		ConstructorDefinitionContext _localctx = new ConstructorDefinitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_constructorDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(Identifier);
			setState(89);
			emptyParenthesisPair();
			setState(90);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterDefinitionContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParameterDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDefinition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParameterDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDefinitionContext parameterDefinition() throws RecognitionException {
		ParameterDefinitionContext _localctx = new ParameterDefinitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_parameterDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			type();
			setState(93);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(MxParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(LBRACE);
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 91312379149679550L) != 0)) {
				{
				{
				setState(96);
				statement();
				}
				}
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(102);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStmtContext extends StatementContext {
		public ForInitializationContext forInit;
		public ExpressionContext forCondition;
		public ExpressionContext forUpdate;
		public TerminalNode FOR() { return getToken(MxParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public List<TerminalNode> SEMI() { return getTokens(MxParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(MxParser.SEMI, i);
		}
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ForInitializationContext forInitialization() {
			return getRuleContext(ForInitializationContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileStmtContext extends StatementContext {
		public TerminalNode WHILE() { return getToken(MxParser.WHILE, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public WhileStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStmtContext extends StatementContext {
		public StatementContext ifThenStmt;
		public StatementContext ifElseStmt;
		public TerminalNode IF() { return getToken(MxParser.IF, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MxParser.ELSE, 0); }
		public IfStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockStmtContext extends StatementContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BlockStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BreakStmtContext extends StatementContext {
		public TerminalNode BREAK() { return getToken(MxParser.BREAK, 0); }
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public BreakStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreakStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStmtContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public ExpressionStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExpressionStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableDefinitionsStmtContext extends StatementContext {
		public VariableDefinitionsContext variableDefinitions() {
			return getRuleContext(VariableDefinitionsContext.class,0);
		}
		public VariableDefinitionsStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDefinitionsStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyStmtContext extends StatementContext {
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public EmptyStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitEmptyStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStmtContext extends StatementContext {
		public TerminalNode RETURN() { return getToken(MxParser.RETURN, 0); }
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public ExpressionOrArrayContext expressionOrArray() {
			return getRuleContext(ExpressionOrArrayContext.class,0);
		}
		public ReturnStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStmtContext extends StatementContext {
		public TerminalNode CONTINUE() { return getToken(MxParser.CONTINUE, 0); }
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public ContinueStmtContext(StatementContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinueStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_statement);
		int _la;
		try {
			setState(145);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(104);
				block();
				}
				break;
			case 2:
				_localctx = new VariableDefinitionsStmtContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(105);
				variableDefinitions();
				}
				break;
			case 3:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(106);
				match(IF);
				setState(107);
				condition();
				setState(108);
				((IfStmtContext)_localctx).ifThenStmt = statement();
				setState(111);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(109);
					match(ELSE);
					setState(110);
					((IfStmtContext)_localctx).ifElseStmt = statement();
					}
					break;
				}
				}
				break;
			case 4:
				_localctx = new ForStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(113);
				match(FOR);
				setState(114);
				match(LPAREN);
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1240386602205630L) != 0)) {
					{
					setState(115);
					((ForStmtContext)_localctx).forInit = forInitialization();
					}
				}

				setState(118);
				match(SEMI);
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1240386602205600L) != 0)) {
					{
					setState(119);
					((ForStmtContext)_localctx).forCondition = expression(0);
					}
				}

				setState(122);
				match(SEMI);
				setState(124);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1240386602205600L) != 0)) {
					{
					setState(123);
					((ForStmtContext)_localctx).forUpdate = expression(0);
					}
				}

				setState(126);
				match(RPAREN);
				setState(127);
				statement();
				}
				break;
			case 5:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(128);
				match(WHILE);
				setState(129);
				condition();
				setState(130);
				statement();
				}
				break;
			case 6:
				_localctx = new BreakStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(132);
				match(BREAK);
				setState(133);
				match(SEMI);
				}
				break;
			case 7:
				_localctx = new ContinueStmtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(134);
				match(CONTINUE);
				setState(135);
				match(SEMI);
				}
				break;
			case 8:
				_localctx = new ReturnStmtContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(136);
				match(RETURN);
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 73297980640133536L) != 0)) {
					{
					setState(137);
					expressionOrArray();
					}
				}

				setState(140);
				match(SEMI);
				}
				break;
			case 9:
				_localctx = new ExpressionStmtContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(141);
				expression(0);
				setState(142);
				match(SEMI);
				}
				break;
			case 10:
				_localctx = new EmptyStmtContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(144);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDefinitionsBodyContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<VariableDeclaratorContext> variableDeclarator() {
			return getRuleContexts(VariableDeclaratorContext.class);
		}
		public VariableDeclaratorContext variableDeclarator(int i) {
			return getRuleContext(VariableDeclaratorContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxParser.COMMA, i);
		}
		public VariableDefinitionsBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDefinitionsBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDefinitionsBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDefinitionsBodyContext variableDefinitionsBody() throws RecognitionException {
		VariableDefinitionsBodyContext _localctx = new VariableDefinitionsBodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_variableDefinitionsBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			type();
			setState(148);
			variableDeclarator();
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(149);
				match(COMMA);
				setState(150);
				variableDeclarator();
				}
				}
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDefinitionsContext extends ParserRuleContext {
		public VariableDefinitionsBodyContext variableDefinitionsBody() {
			return getRuleContext(VariableDefinitionsBodyContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public VariableDefinitionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDefinitions; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDefinitions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDefinitionsContext variableDefinitions() throws RecognitionException {
		VariableDefinitionsContext _localctx = new VariableDefinitionsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_variableDefinitions);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			variableDefinitionsBody();
			setState(157);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclaratorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode ASSIGN() { return getToken(MxParser.ASSIGN, 0); }
		public ExpressionOrArrayContext expressionOrArray() {
			return getRuleContext(ExpressionOrArrayContext.class,0);
		}
		public VariableDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclarator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclaratorContext variableDeclarator() throws RecognitionException {
		VariableDeclaratorContext _localctx = new VariableDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variableDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(Identifier);
			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(160);
				match(ASSIGN);
				setState(161);
				expressionOrArray();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForInitializationContext extends ParserRuleContext {
		public VariableDefinitionsBodyContext variableDefinitionsBody() {
			return getRuleContext(VariableDefinitionsBodyContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ForInitializationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInitialization; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForInitialization(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitializationContext forInitialization() throws RecognitionException {
		ForInitializationContext _localctx = new ForInitializationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_forInitialization);
		try {
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(164);
				variableDefinitionsBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				expression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PreUnaryAssignExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxParser.DEC, 0); }
		public PreUnaryAssignExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPreUnaryAssignExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ThisExprContext extends ExpressionContext {
		public TerminalNode THIS() { return getToken(MxParser.THIS, 0); }
		public ThisExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitThisExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubscriptExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionBracketPairContext expressionBracketPair() {
			return getRuleContext(ExpressionBracketPairContext.class,0);
		}
		public SubscriptExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSubscriptExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryExprContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MUL() { return getToken(MxParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MxParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MxParser.MOD, 0); }
		public TerminalNode ADD() { return getToken(MxParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxParser.SUB, 0); }
		public TerminalNode SHL() { return getToken(MxParser.SHL, 0); }
		public TerminalNode SHR() { return getToken(MxParser.SHR, 0); }
		public TerminalNode GT() { return getToken(MxParser.GT, 0); }
		public TerminalNode LT() { return getToken(MxParser.LT, 0); }
		public TerminalNode GE() { return getToken(MxParser.GE, 0); }
		public TerminalNode LE() { return getToken(MxParser.LE, 0); }
		public TerminalNode NE() { return getToken(MxParser.NE, 0); }
		public TerminalNode EQ() { return getToken(MxParser.EQ, 0); }
		public TerminalNode AND() { return getToken(MxParser.AND, 0); }
		public TerminalNode XOR() { return getToken(MxParser.XOR, 0); }
		public TerminalNode OR() { return getToken(MxParser.OR, 0); }
		public TerminalNode LOGICAL_AND() { return getToken(MxParser.LOGICAL_AND, 0); }
		public TerminalNode LOGICAL_OR() { return getToken(MxParser.LOGICAL_OR, 0); }
		public BinaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PostUnaryAssignExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxParser.DEC, 0); }
		public PostUnaryAssignExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPostUnaryAssignExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExprContext extends ExpressionContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public ParenExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParenExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionExprContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DOT() { return getToken(MxParser.DOT, 0); }
		public FunctionExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableExprContext extends ExpressionContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DOT() { return getToken(MxParser.DOT, 0); }
		public VariableExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PreUnaryExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SUB() { return getToken(MxParser.SUB, 0); }
		public TerminalNode NOT() { return getToken(MxParser.NOT, 0); }
		public TerminalNode LOGICAL_NOT() { return getToken(MxParser.LOGICAL_NOT, 0); }
		public PreUnaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPreUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExprContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FormatStringExprContext extends ExpressionContext {
		public FormatStringContext formatString() {
			return getRuleContext(FormatStringContext.class,0);
		}
		public FormatStringExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFormatStringExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CreatorExprContext extends ExpressionContext {
		public TerminalNode NEW() { return getToken(MxParser.NEW, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public ArrayCreatorContext arrayCreator() {
			return getRuleContext(ArrayCreatorContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public EmptyParenthesisPairContext emptyParenthesisPair() {
			return getRuleContext(EmptyParenthesisPairContext.class,0);
		}
		public CreatorExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCreatorExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(MxParser.ASSIGN, 0); }
		public ExpressionOrArrayContext expressionOrArray() {
			return getRuleContext(ExpressionOrArrayContext.class,0);
		}
		public AssignExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitAssignExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConditionalExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode QUESTION() { return getToken(MxParser.QUESTION, 0); }
		public TerminalNode COLON() { return getToken(MxParser.COLON, 0); }
		public ConditionalExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConditionalExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(169);
				match(LPAREN);
				setState(170);
				expression(0);
				setState(171);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new ThisExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(173);
				match(THIS);
				}
				break;
			case 3:
				{
				_localctx = new LiteralExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(174);
				literal();
				}
				break;
			case 4:
				{
				_localctx = new FormatStringExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(175);
				formatString();
				}
				break;
			case 5:
				{
				_localctx = new CreatorExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(176);
				match(NEW);
				setState(177);
				typeName();
				setState(178);
				arrayCreator();
				}
				break;
			case 6:
				{
				_localctx = new CreatorExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(180);
				match(NEW);
				setState(181);
				match(Identifier);
				setState(183);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
				case 1:
					{
					setState(182);
					emptyParenthesisPair();
					}
					break;
				}
				}
				break;
			case 7:
				{
				_localctx = new VariableExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(185);
				match(Identifier);
				}
				break;
			case 8:
				{
				_localctx = new FunctionExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(186);
				match(Identifier);
				setState(187);
				argumentList();
				}
				break;
			case 9:
				{
				_localctx = new PreUnaryAssignExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(188);
				((PreUnaryAssignExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==INC || _la==DEC) ) {
					((PreUnaryAssignExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(189);
				expression(14);
				}
				break;
			case 10:
				{
				_localctx = new PreUnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(190);
				((PreUnaryExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8933565530112L) != 0)) ) {
					((PreUnaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(191);
				expression(13);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(246);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(244);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(194);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(195);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 469762048L) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(196);
						expression(13);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(197);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(198);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(199);
						expression(12);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(200);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(201);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SHL || _la==SHR) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(202);
						expression(11);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(203);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(204);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8053063680L) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(205);
						expression(10);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(206);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(207);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==NE || _la==EQ) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(208);
						expression(9);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(209);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(210);
						((BinaryExprContext)_localctx).op = match(AND);
						setState(211);
						expression(8);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(212);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(213);
						((BinaryExprContext)_localctx).op = match(XOR);
						setState(214);
						expression(7);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(215);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(216);
						((BinaryExprContext)_localctx).op = match(OR);
						setState(217);
						expression(6);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(218);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(219);
						((BinaryExprContext)_localctx).op = match(LOGICAL_AND);
						setState(220);
						expression(5);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(221);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(222);
						((BinaryExprContext)_localctx).op = match(LOGICAL_OR);
						setState(223);
						expression(4);
						}
						break;
					case 11:
						{
						_localctx = new ConditionalExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(224);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(225);
						match(QUESTION);
						setState(226);
						expression(0);
						setState(227);
						match(COLON);
						setState(228);
						expression(2);
						}
						break;
					case 12:
						{
						_localctx = new SubscriptExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(230);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(231);
						expressionBracketPair();
						}
						break;
					case 13:
						{
						_localctx = new VariableExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(232);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(233);
						match(DOT);
						setState(234);
						match(Identifier);
						}
						break;
					case 14:
						{
						_localctx = new FunctionExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(235);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(236);
						match(DOT);
						setState(237);
						match(Identifier);
						setState(238);
						argumentList();
						}
						break;
					case 15:
						{
						_localctx = new PostUnaryAssignExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(239);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(240);
						((PostUnaryAssignExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((PostUnaryAssignExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 16:
						{
						_localctx = new AssignExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(241);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(242);
						match(ASSIGN);
						setState(243);
						expressionOrArray();
						}
						break;
					}
					} 
				}
				setState(248);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayCreatorContext extends ParserRuleContext {
		public List<PossibleBracketPairContext> possibleBracketPair() {
			return getRuleContexts(PossibleBracketPairContext.class);
		}
		public PossibleBracketPairContext possibleBracketPair(int i) {
			return getRuleContext(PossibleBracketPairContext.class,i);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ArrayCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreatorContext arrayCreator() throws RecognitionException {
		ArrayCreatorContext _localctx = new ArrayCreatorContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_arrayCreator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(250); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(249);
					possibleBracketPair();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(252); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(255);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(254);
				array();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionOrArrayContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public ExpressionOrArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionOrArray; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExpressionOrArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionOrArrayContext expressionOrArray() throws RecognitionException {
		ExpressionOrArrayContext _localctx = new ExpressionOrArrayContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expressionOrArray);
		try {
			setState(259);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NEW:
			case NULL:
			case THIS:
			case IntegerLiteral:
			case BooleanLiteral:
			case StringLiteral:
			case FormatStringBegin:
			case FormatStringAtom:
			case Identifier:
			case SUB:
			case LOGICAL_NOT:
			case NOT:
			case INC:
			case DEC:
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				expression(0);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(258);
				array();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(MxParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxParser.RBRACE, 0); }
		public List<ExpressionOrArrayContext> expressionOrArray() {
			return getRuleContexts(ExpressionOrArrayContext.class);
		}
		public ExpressionOrArrayContext expressionOrArray(int i) {
			return getRuleContext(ExpressionOrArrayContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxParser.COMMA, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(LBRACE);
			setState(270);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 73297980640133536L) != 0)) {
				{
				setState(262);
				expressionOrArray();
				setState(267);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(263);
					match(COMMA);
					setState(264);
					expressionOrArray();
					}
					}
					setState(269);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(272);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FormatStringContext extends ParserRuleContext {
		public Token FormatStringAtom;
		public List<Token> formatStringText = new ArrayList<Token>();
		public Token FormatStringBegin;
		public Token FormatStringMiddle;
		public Token FormatStringEnd;
		public TerminalNode FormatStringAtom() { return getToken(MxParser.FormatStringAtom, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode FormatStringBegin() { return getToken(MxParser.FormatStringBegin, 0); }
		public TerminalNode FormatStringEnd() { return getToken(MxParser.FormatStringEnd, 0); }
		public List<TerminalNode> FormatStringMiddle() { return getTokens(MxParser.FormatStringMiddle); }
		public TerminalNode FormatStringMiddle(int i) {
			return getToken(MxParser.FormatStringMiddle, i);
		}
		public FormatStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formatString; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFormatString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormatStringContext formatString() throws RecognitionException {
		FormatStringContext _localctx = new FormatStringContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_formatString);
		int _la;
		try {
			setState(286);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FormatStringAtom:
				enterOuterAlt(_localctx, 1);
				{
				setState(274);
				((FormatStringContext)_localctx).FormatStringAtom = match(FormatStringAtom);
				((FormatStringContext)_localctx).formatStringText.add(((FormatStringContext)_localctx).FormatStringAtom);
				}
				break;
			case FormatStringBegin:
				enterOuterAlt(_localctx, 2);
				{
				setState(275);
				((FormatStringContext)_localctx).FormatStringBegin = match(FormatStringBegin);
				((FormatStringContext)_localctx).formatStringText.add(((FormatStringContext)_localctx).FormatStringBegin);
				setState(276);
				expression(0);
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==FormatStringMiddle) {
					{
					{
					setState(277);
					((FormatStringContext)_localctx).FormatStringMiddle = match(FormatStringMiddle);
					((FormatStringContext)_localctx).formatStringText.add(((FormatStringContext)_localctx).FormatStringMiddle);
					setState(278);
					expression(0);
					}
					}
					setState(283);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(284);
				((FormatStringContext)_localctx).FormatStringEnd = match(FormatStringEnd);
				((FormatStringContext)_localctx).formatStringText.add(((FormatStringContext)_localctx).FormatStringEnd);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode IntegerLiteral() { return getToken(MxParser.IntegerLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(MxParser.BooleanLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(MxParser.StringLiteral, 0); }
		public TerminalNode NULL() { return getToken(MxParser.NULL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 458880L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public List<ExpressionOrArrayContext> expressionOrArray() {
			return getRuleContexts(ExpressionOrArrayContext.class);
		}
		public ExpressionOrArrayContext expressionOrArray(int i) {
			return getRuleContext(ExpressionOrArrayContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			match(LPAREN);
			setState(299);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 73297980640133536L) != 0)) {
				{
				setState(291);
				expressionOrArray();
				setState(296);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(292);
					match(COMMA);
					setState(293);
					expressionOrArray();
					}
					}
					setState(298);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(301);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_condition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(LPAREN);
			setState(304);
			expression(0);
			setState(305);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(MxParser.VOID, 0); }
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public List<EmptyBracketPairContext> emptyBracketPair() {
			return getRuleContexts(EmptyBracketPairContext.class);
		}
		public EmptyBracketPairContext emptyBracketPair(int i) {
			return getRuleContext(EmptyBracketPairContext.class,i);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_type);
		int _la;
		try {
			setState(315);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(307);
				match(VOID);
				}
				break;
			case BOOL:
			case INT:
			case STRING:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(308);
				typeName();
				setState(312);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LBRACK) {
					{
					{
					setState(309);
					emptyBracketPair();
					}
					}
					setState(314);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeNameContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(MxParser.INT, 0); }
		public TerminalNode BOOL() { return getToken(MxParser.BOOL, 0); }
		public TerminalNode STRING() { return getToken(MxParser.STRING, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_typeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8388636L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyBracketPairContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(MxParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MxParser.RBRACK, 0); }
		public EmptyBracketPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyBracketPair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitEmptyBracketPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyBracketPairContext emptyBracketPair() throws RecognitionException {
		EmptyBracketPairContext _localctx = new EmptyBracketPairContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_emptyBracketPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			match(LBRACK);
			setState(320);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionBracketPairContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(MxParser.LBRACK, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBRACK() { return getToken(MxParser.RBRACK, 0); }
		public ExpressionBracketPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionBracketPair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExpressionBracketPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionBracketPairContext expressionBracketPair() throws RecognitionException {
		ExpressionBracketPairContext _localctx = new ExpressionBracketPairContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_expressionBracketPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			match(LBRACK);
			setState(323);
			expression(0);
			setState(324);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PossibleBracketPairContext extends ParserRuleContext {
		public TerminalNode LBRACK() { return getToken(MxParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MxParser.RBRACK, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PossibleBracketPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_possibleBracketPair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPossibleBracketPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PossibleBracketPairContext possibleBracketPair() throws RecognitionException {
		PossibleBracketPairContext _localctx = new PossibleBracketPairContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_possibleBracketPair);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			match(LBRACK);
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1240386602205600L) != 0)) {
				{
				setState(327);
				expression(0);
				}
			}

			setState(330);
			match(RBRACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyParenthesisPairContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public EmptyParenthesisPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyParenthesisPair; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitEmptyParenthesisPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyParenthesisPairContext emptyParenthesisPair() throws RecognitionException {
		EmptyParenthesisPairContext _localctx = new EmptyParenthesisPairContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_emptyParenthesisPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(LPAREN);
			setState(333);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		case 10:
			return precpred(_ctx, 2);
		case 11:
			return precpred(_ctx, 20);
		case 12:
			return precpred(_ctx, 18);
		case 13:
			return precpred(_ctx, 16);
		case 14:
			return precpred(_ctx, 15);
		case 15:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001<\u0150\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u00006\b\u0000\n\u0000\f\u0000"+
		"9\t\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001A\b\u0001\n\u0001\f\u0001D\t\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0005\u0002O\b\u0002\n\u0002\f\u0002R\t\u0002"+
		"\u0003\u0002T\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0005\u0005b\b\u0005\n\u0005\f\u0005e\t\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006p\b\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0003\u0006u\b\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006y\b\u0006\u0001\u0006\u0001\u0006\u0003\u0006}\b\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006\u008b\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006\u0092\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0005\u0007\u0098\b\u0007\n\u0007\f\u0007\u009b\t\u0007\u0001\b"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0003\t\u00a3\b\t\u0001\n\u0001"+
		"\n\u0003\n\u00a7\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00b8"+
		"\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u00c1\b\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005"+
		"\u000b\u00f5\b\u000b\n\u000b\f\u000b\u00f8\t\u000b\u0001\f\u0004\f\u00fb"+
		"\b\f\u000b\f\f\f\u00fc\u0001\f\u0003\f\u0100\b\f\u0001\r\u0001\r\u0003"+
		"\r\u0104\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e"+
		"\u010a\b\u000e\n\u000e\f\u000e\u010d\t\u000e\u0003\u000e\u010f\b\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0005\u000f\u0118\b\u000f\n\u000f\f\u000f\u011b\t\u000f\u0001"+
		"\u000f\u0001\u000f\u0003\u000f\u011f\b\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u0127\b\u0011\n"+
		"\u0011\f\u0011\u012a\t\u0011\u0003\u0011\u012c\b\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0005\u0013\u0137\b\u0013\n\u0013\f\u0013\u013a\t\u0013"+
		"\u0003\u0013\u013c\b\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017"+
		"\u0001\u0017\u0003\u0017\u0149\b\u0017\u0001\u0017\u0001\u0017\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0000\u0001\u0016\u0019\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		" \"$&(*,.0\u0000\t\u0001\u0000-.\u0003\u0000\u0019\u0019%%++\u0001\u0000"+
		"\u001a\u001c\u0001\u0000\u0018\u0019\u0001\u0000&\'\u0001\u0000\u001d"+
		" \u0001\u0000!\"\u0002\u0000\u0007\u0007\u0010\u0012\u0002\u0000\u0002"+
		"\u0004\u0017\u0017\u0176\u00007\u0001\u0000\u0000\u0000\u0002:\u0001\u0000"+
		"\u0000\u0000\u0004H\u0001\u0000\u0000\u0000\u0006X\u0001\u0000\u0000\u0000"+
		"\b\\\u0001\u0000\u0000\u0000\n_\u0001\u0000\u0000\u0000\f\u0091\u0001"+
		"\u0000\u0000\u0000\u000e\u0093\u0001\u0000\u0000\u0000\u0010\u009c\u0001"+
		"\u0000\u0000\u0000\u0012\u009f\u0001\u0000\u0000\u0000\u0014\u00a6\u0001"+
		"\u0000\u0000\u0000\u0016\u00c0\u0001\u0000\u0000\u0000\u0018\u00fa\u0001"+
		"\u0000\u0000\u0000\u001a\u0103\u0001\u0000\u0000\u0000\u001c\u0105\u0001"+
		"\u0000\u0000\u0000\u001e\u011e\u0001\u0000\u0000\u0000 \u0120\u0001\u0000"+
		"\u0000\u0000\"\u0122\u0001\u0000\u0000\u0000$\u012f\u0001\u0000\u0000"+
		"\u0000&\u013b\u0001\u0000\u0000\u0000(\u013d\u0001\u0000\u0000\u0000*"+
		"\u013f\u0001\u0000\u0000\u0000,\u0142\u0001\u0000\u0000\u0000.\u0146\u0001"+
		"\u0000\u0000\u00000\u014c\u0001\u0000\u0000\u000026\u0003\u0002\u0001"+
		"\u000036\u0003\u0010\b\u000046\u0003\u0004\u0002\u000052\u0001\u0000\u0000"+
		"\u000053\u0001\u0000\u0000\u000054\u0001\u0000\u0000\u000069\u0001\u0000"+
		"\u0000\u000075\u0001\u0000\u0000\u000078\u0001\u0000\u0000\u00008\u0001"+
		"\u0001\u0000\u0000\u000097\u0001\u0000\u0000\u0000:;\u0005\u0006\u0000"+
		"\u0000;<\u0005\u0017\u0000\u0000<B\u00058\u0000\u0000=A\u0003\u0010\b"+
		"\u0000>A\u0003\u0004\u0002\u0000?A\u0003\u0006\u0003\u0000@=\u0001\u0000"+
		"\u0000\u0000@>\u0001\u0000\u0000\u0000@?\u0001\u0000\u0000\u0000AD\u0001"+
		"\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000"+
		"CE\u0001\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000EF\u00059\u0000\u0000"+
		"FG\u00056\u0000\u0000G\u0003\u0001\u0000\u0000\u0000HI\u0003&\u0013\u0000"+
		"IJ\u0005\u0017\u0000\u0000JS\u00052\u0000\u0000KP\u0003\b\u0004\u0000"+
		"LM\u00057\u0000\u0000MO\u0003\b\u0004\u0000NL\u0001\u0000\u0000\u0000"+
		"OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000\u0000PQ\u0001\u0000\u0000"+
		"\u0000QT\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000SK\u0001\u0000"+
		"\u0000\u0000ST\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000UV\u0005"+
		"3\u0000\u0000VW\u0003\n\u0005\u0000W\u0005\u0001\u0000\u0000\u0000XY\u0005"+
		"\u0017\u0000\u0000YZ\u00030\u0018\u0000Z[\u0003\n\u0005\u0000[\u0007\u0001"+
		"\u0000\u0000\u0000\\]\u0003&\u0013\u0000]^\u0005\u0017\u0000\u0000^\t"+
		"\u0001\u0000\u0000\u0000_c\u00058\u0000\u0000`b\u0003\f\u0006\u0000a`"+
		"\u0001\u0000\u0000\u0000be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000"+
		"\u0000cd\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000ec\u0001\u0000"+
		"\u0000\u0000fg\u00059\u0000\u0000g\u000b\u0001\u0000\u0000\u0000h\u0092"+
		"\u0003\n\u0005\u0000i\u0092\u0003\u0010\b\u0000jk\u0005\t\u0000\u0000"+
		"kl\u0003$\u0012\u0000lo\u0003\f\u0006\u0000mn\u0005\n\u0000\u0000np\u0003"+
		"\f\u0006\u0000om\u0001\u0000\u0000\u0000op\u0001\u0000\u0000\u0000p\u0092"+
		"\u0001\u0000\u0000\u0000qr\u0005\u000b\u0000\u0000rt\u00052\u0000\u0000"+
		"su\u0003\u0014\n\u0000ts\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000"+
		"uv\u0001\u0000\u0000\u0000vx\u00056\u0000\u0000wy\u0003\u0016\u000b\u0000"+
		"xw\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000"+
		"\u0000z|\u00056\u0000\u0000{}\u0003\u0016\u000b\u0000|{\u0001\u0000\u0000"+
		"\u0000|}\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u007f\u0005"+
		"3\u0000\u0000\u007f\u0092\u0003\f\u0006\u0000\u0080\u0081\u0005\f\u0000"+
		"\u0000\u0081\u0082\u0003$\u0012\u0000\u0082\u0083\u0003\f\u0006\u0000"+
		"\u0083\u0092\u0001\u0000\u0000\u0000\u0084\u0085\u0005\r\u0000\u0000\u0085"+
		"\u0092\u00056\u0000\u0000\u0086\u0087\u0005\u000e\u0000\u0000\u0087\u0092"+
		"\u00056\u0000\u0000\u0088\u008a\u0005\u000f\u0000\u0000\u0089\u008b\u0003"+
		"\u001a\r\u0000\u008a\u0089\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u0092\u00056\u0000"+
		"\u0000\u008d\u008e\u0003\u0016\u000b\u0000\u008e\u008f\u00056\u0000\u0000"+
		"\u008f\u0092\u0001\u0000\u0000\u0000\u0090\u0092\u00056\u0000\u0000\u0091"+
		"h\u0001\u0000\u0000\u0000\u0091i\u0001\u0000\u0000\u0000\u0091j\u0001"+
		"\u0000\u0000\u0000\u0091q\u0001\u0000\u0000\u0000\u0091\u0080\u0001\u0000"+
		"\u0000\u0000\u0091\u0084\u0001\u0000\u0000\u0000\u0091\u0086\u0001\u0000"+
		"\u0000\u0000\u0091\u0088\u0001\u0000\u0000\u0000\u0091\u008d\u0001\u0000"+
		"\u0000\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0092\r\u0001\u0000\u0000"+
		"\u0000\u0093\u0094\u0003&\u0013\u0000\u0094\u0099\u0003\u0012\t\u0000"+
		"\u0095\u0096\u00057\u0000\u0000\u0096\u0098\u0003\u0012\t\u0000\u0097"+
		"\u0095\u0001\u0000\u0000\u0000\u0098\u009b\u0001\u0000\u0000\u0000\u0099"+
		"\u0097\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a"+
		"\u000f\u0001\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009c"+
		"\u009d\u0003\u000e\u0007\u0000\u009d\u009e\u00056\u0000\u0000\u009e\u0011"+
		"\u0001\u0000\u0000\u0000\u009f\u00a2\u0005\u0017\u0000\u0000\u00a0\u00a1"+
		"\u0005,\u0000\u0000\u00a1\u00a3\u0003\u001a\r\u0000\u00a2\u00a0\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u0013\u0001"+
		"\u0000\u0000\u0000\u00a4\u00a7\u0003\u000e\u0007\u0000\u00a5\u00a7\u0003"+
		"\u0016\u000b\u0000\u00a6\u00a4\u0001\u0000\u0000\u0000\u00a6\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a7\u0015\u0001\u0000\u0000\u0000\u00a8\u00a9\u0006"+
		"\u000b\uffff\uffff\u0000\u00a9\u00aa\u00052\u0000\u0000\u00aa\u00ab\u0003"+
		"\u0016\u000b\u0000\u00ab\u00ac\u00053\u0000\u0000\u00ac\u00c1\u0001\u0000"+
		"\u0000\u0000\u00ad\u00c1\u0005\b\u0000\u0000\u00ae\u00c1\u0003 \u0010"+
		"\u0000\u00af\u00c1\u0003\u001e\u000f\u0000\u00b0\u00b1\u0005\u0005\u0000"+
		"\u0000\u00b1\u00b2\u0003(\u0014\u0000\u00b2\u00b3\u0003\u0018\f\u0000"+
		"\u00b3\u00c1\u0001\u0000\u0000\u0000\u00b4\u00b5\u0005\u0005\u0000\u0000"+
		"\u00b5\u00b7\u0005\u0017\u0000\u0000\u00b6\u00b8\u00030\u0018\u0000\u00b7"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8"+
		"\u00c1\u0001\u0000\u0000\u0000\u00b9\u00c1\u0005\u0017\u0000\u0000\u00ba"+
		"\u00bb\u0005\u0017\u0000\u0000\u00bb\u00c1\u0003\"\u0011\u0000\u00bc\u00bd"+
		"\u0007\u0000\u0000\u0000\u00bd\u00c1\u0003\u0016\u000b\u000e\u00be\u00bf"+
		"\u0007\u0001\u0000\u0000\u00bf\u00c1\u0003\u0016\u000b\r\u00c0\u00a8\u0001"+
		"\u0000\u0000\u0000\u00c0\u00ad\u0001\u0000\u0000\u0000\u00c0\u00ae\u0001"+
		"\u0000\u0000\u0000\u00c0\u00af\u0001\u0000\u0000\u0000\u00c0\u00b0\u0001"+
		"\u0000\u0000\u0000\u00c0\u00b4\u0001\u0000\u0000\u0000\u00c0\u00b9\u0001"+
		"\u0000\u0000\u0000\u00c0\u00ba\u0001\u0000\u0000\u0000\u00c0\u00bc\u0001"+
		"\u0000\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c1\u00f6\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c3\n\f\u0000\u0000\u00c3\u00c4\u0007\u0002"+
		"\u0000\u0000\u00c4\u00f5\u0003\u0016\u000b\r\u00c5\u00c6\n\u000b\u0000"+
		"\u0000\u00c6\u00c7\u0007\u0003\u0000\u0000\u00c7\u00f5\u0003\u0016\u000b"+
		"\f\u00c8\u00c9\n\n\u0000\u0000\u00c9\u00ca\u0007\u0004\u0000\u0000\u00ca"+
		"\u00f5\u0003\u0016\u000b\u000b\u00cb\u00cc\n\t\u0000\u0000\u00cc\u00cd"+
		"\u0007\u0005\u0000\u0000\u00cd\u00f5\u0003\u0016\u000b\n\u00ce\u00cf\n"+
		"\b\u0000\u0000\u00cf\u00d0\u0007\u0006\u0000\u0000\u00d0\u00f5\u0003\u0016"+
		"\u000b\t\u00d1\u00d2\n\u0007\u0000\u0000\u00d2\u00d3\u0005(\u0000\u0000"+
		"\u00d3\u00f5\u0003\u0016\u000b\b\u00d4\u00d5\n\u0006\u0000\u0000\u00d5"+
		"\u00d6\u0005*\u0000\u0000\u00d6\u00f5\u0003\u0016\u000b\u0007\u00d7\u00d8"+
		"\n\u0005\u0000\u0000\u00d8\u00d9\u0005)\u0000\u0000\u00d9\u00f5\u0003"+
		"\u0016\u000b\u0006\u00da\u00db\n\u0004\u0000\u0000\u00db\u00dc\u0005#"+
		"\u0000\u0000\u00dc\u00f5\u0003\u0016\u000b\u0005\u00dd\u00de\n\u0003\u0000"+
		"\u0000\u00de\u00df\u0005$\u0000\u0000\u00df\u00f5\u0003\u0016\u000b\u0004"+
		"\u00e0\u00e1\n\u0002\u0000\u0000\u00e1\u00e2\u00054\u0000\u0000\u00e2"+
		"\u00e3\u0003\u0016\u000b\u0000\u00e3\u00e4\u00055\u0000\u0000\u00e4\u00e5"+
		"\u0003\u0016\u000b\u0002\u00e5\u00f5\u0001\u0000\u0000\u0000\u00e6\u00e7"+
		"\n\u0014\u0000\u0000\u00e7\u00f5\u0003,\u0016\u0000\u00e8\u00e9\n\u0012"+
		"\u0000\u0000\u00e9\u00ea\u0005/\u0000\u0000\u00ea\u00f5\u0005\u0017\u0000"+
		"\u0000\u00eb\u00ec\n\u0010\u0000\u0000\u00ec\u00ed\u0005/\u0000\u0000"+
		"\u00ed\u00ee\u0005\u0017\u0000\u0000\u00ee\u00f5\u0003\"\u0011\u0000\u00ef"+
		"\u00f0\n\u000f\u0000\u0000\u00f0\u00f5\u0007\u0000\u0000\u0000\u00f1\u00f2"+
		"\n\u0001\u0000\u0000\u00f2\u00f3\u0005,\u0000\u0000\u00f3\u00f5\u0003"+
		"\u001a\r\u0000\u00f4\u00c2\u0001\u0000\u0000\u0000\u00f4\u00c5\u0001\u0000"+
		"\u0000\u0000\u00f4\u00c8\u0001\u0000\u0000\u0000\u00f4\u00cb\u0001\u0000"+
		"\u0000\u0000\u00f4\u00ce\u0001\u0000\u0000\u0000\u00f4\u00d1\u0001\u0000"+
		"\u0000\u0000\u00f4\u00d4\u0001\u0000\u0000\u0000\u00f4\u00d7\u0001\u0000"+
		"\u0000\u0000\u00f4\u00da\u0001\u0000\u0000\u0000\u00f4\u00dd\u0001\u0000"+
		"\u0000\u0000\u00f4\u00e0\u0001\u0000\u0000\u0000\u00f4\u00e6\u0001\u0000"+
		"\u0000\u0000\u00f4\u00e8\u0001\u0000\u0000\u0000\u00f4\u00eb\u0001\u0000"+
		"\u0000\u0000\u00f4\u00ef\u0001\u0000\u0000\u0000\u00f4\u00f1\u0001\u0000"+
		"\u0000\u0000\u00f5\u00f8\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000"+
		"\u0000\u0000\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f7\u0017\u0001\u0000"+
		"\u0000\u0000\u00f8\u00f6\u0001\u0000\u0000\u0000\u00f9\u00fb\u0003.\u0017"+
		"\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000"+
		"\u0000\u00fc\u00fa\u0001\u0000\u0000\u0000\u00fc\u00fd\u0001\u0000\u0000"+
		"\u0000\u00fd\u00ff\u0001\u0000\u0000\u0000\u00fe\u0100\u0003\u001c\u000e"+
		"\u0000\u00ff\u00fe\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000\u0000"+
		"\u0000\u0100\u0019\u0001\u0000\u0000\u0000\u0101\u0104\u0003\u0016\u000b"+
		"\u0000\u0102\u0104\u0003\u001c\u000e\u0000\u0103\u0101\u0001\u0000\u0000"+
		"\u0000\u0103\u0102\u0001\u0000\u0000\u0000\u0104\u001b\u0001\u0000\u0000"+
		"\u0000\u0105\u010e\u00058\u0000\u0000\u0106\u010b\u0003\u001a\r\u0000"+
		"\u0107\u0108\u00057\u0000\u0000\u0108\u010a\u0003\u001a\r\u0000\u0109"+
		"\u0107\u0001\u0000\u0000\u0000\u010a\u010d\u0001\u0000\u0000\u0000\u010b"+
		"\u0109\u0001\u0000\u0000\u0000\u010b\u010c\u0001\u0000\u0000\u0000\u010c"+
		"\u010f\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010e"+
		"\u0106\u0001\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f"+
		"\u0110\u0001\u0000\u0000\u0000\u0110\u0111\u00059\u0000\u0000\u0111\u001d"+
		"\u0001\u0000\u0000\u0000\u0112\u011f\u0005\u0016\u0000\u0000\u0113\u0114"+
		"\u0005\u0013\u0000\u0000\u0114\u0119\u0003\u0016\u000b\u0000\u0115\u0116"+
		"\u0005\u0015\u0000\u0000\u0116\u0118\u0003\u0016\u000b\u0000\u0117\u0115"+
		"\u0001\u0000\u0000\u0000\u0118\u011b\u0001\u0000\u0000\u0000\u0119\u0117"+
		"\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000\u0000\u0000\u011a\u011c"+
		"\u0001\u0000\u0000\u0000\u011b\u0119\u0001\u0000\u0000\u0000\u011c\u011d"+
		"\u0005\u0014\u0000\u0000\u011d\u011f\u0001\u0000\u0000\u0000\u011e\u0112"+
		"\u0001\u0000\u0000\u0000\u011e\u0113\u0001\u0000\u0000\u0000\u011f\u001f"+
		"\u0001\u0000\u0000\u0000\u0120\u0121\u0007\u0007\u0000\u0000\u0121!\u0001"+
		"\u0000\u0000\u0000\u0122\u012b\u00052\u0000\u0000\u0123\u0128\u0003\u001a"+
		"\r\u0000\u0124\u0125\u00057\u0000\u0000\u0125\u0127\u0003\u001a\r\u0000"+
		"\u0126\u0124\u0001\u0000\u0000\u0000\u0127\u012a\u0001\u0000\u0000\u0000"+
		"\u0128\u0126\u0001\u0000\u0000\u0000\u0128\u0129\u0001\u0000\u0000\u0000"+
		"\u0129\u012c\u0001\u0000\u0000\u0000\u012a\u0128\u0001\u0000\u0000\u0000"+
		"\u012b\u0123\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000\u0000"+
		"\u012c\u012d\u0001\u0000\u0000\u0000\u012d\u012e\u00053\u0000\u0000\u012e"+
		"#\u0001\u0000\u0000\u0000\u012f\u0130\u00052\u0000\u0000\u0130\u0131\u0003"+
		"\u0016\u000b\u0000\u0131\u0132\u00053\u0000\u0000\u0132%\u0001\u0000\u0000"+
		"\u0000\u0133\u013c\u0005\u0001\u0000\u0000\u0134\u0138\u0003(\u0014\u0000"+
		"\u0135\u0137\u0003*\u0015\u0000\u0136\u0135\u0001\u0000\u0000\u0000\u0137"+
		"\u013a\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0138"+
		"\u0139\u0001\u0000\u0000\u0000\u0139\u013c\u0001\u0000\u0000\u0000\u013a"+
		"\u0138\u0001\u0000\u0000\u0000\u013b\u0133\u0001\u0000\u0000\u0000\u013b"+
		"\u0134\u0001\u0000\u0000\u0000\u013c\'\u0001\u0000\u0000\u0000\u013d\u013e"+
		"\u0007\b\u0000\u0000\u013e)\u0001\u0000\u0000\u0000\u013f\u0140\u0005"+
		"0\u0000\u0000\u0140\u0141\u00051\u0000\u0000\u0141+\u0001\u0000\u0000"+
		"\u0000\u0142\u0143\u00050\u0000\u0000\u0143\u0144\u0003\u0016\u000b\u0000"+
		"\u0144\u0145\u00051\u0000\u0000\u0145-\u0001\u0000\u0000\u0000\u0146\u0148"+
		"\u00050\u0000\u0000\u0147\u0149\u0003\u0016\u000b\u0000\u0148\u0147\u0001"+
		"\u0000\u0000\u0000\u0148\u0149\u0001\u0000\u0000\u0000\u0149\u014a\u0001"+
		"\u0000\u0000\u0000\u014a\u014b\u00051\u0000\u0000\u014b/\u0001\u0000\u0000"+
		"\u0000\u014c\u014d\u00052\u0000\u0000\u014d\u014e\u00053\u0000\u0000\u014e"+
		"1\u0001\u0000\u0000\u0000 57@BPScotx|\u008a\u0091\u0099\u00a2\u00a6\u00b7"+
		"\u00c0\u00f4\u00f6\u00fc\u00ff\u0103\u010b\u010e\u0119\u011e\u0128\u012b"+
		"\u0138\u013b\u0148";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}