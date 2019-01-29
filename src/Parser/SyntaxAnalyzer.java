package Parser;

import Scanner.LexicalAnalyzer;
import Scanner.Token;
import CodeGeneration.CodeGen;
import SymbolTable.SymbolTable;

import java.util.HashSet;
import java.util.Stack;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private CodeGen codeGen;
    private Grammar grammar = Grammar.getInstance();

    private static Stack<Integer> switchOrWhile = new Stack<>();

    //Semantics
    private static Stack<String> SemanticStack = new Stack<>();
    private static boolean main_exists = false;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.codeGen = new CodeGen();

        if (program() && main_exists) {
            System.out.print((char) 27 + "[36m");
            System.out.println("\ninput parsed successfully\n");
        }
        else if (!main_exists)
            throw new Error("There's no main function in code");
        else
            System.out.println("parsing encountered an error");
    }

    @SuppressWarnings("all")
    private boolean program() {
        int state = 0;
        while (true) {
            if (state == 2)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    Token withEpsilon = new Token(null, "declaration-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon).contains(token) || grammar.follow(withEpsilon).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (declaration_list())
                            state = 1;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 1:
                    switch (tokenName) {
                        case "EOF":
                            state = 2;
                            break;
                        default:
                            System.out.println("missing EOF token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 2;
                    }
                    break;
                default:
                    return false;
            }
        }
    }

    //Semantics
    private static boolean type_void = false;

    @SuppressWarnings("all")
    private boolean declaration_list() {
        Token diagramNonTerminal = new Token(null, "declaration-list", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            if (state == 6)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "int":
                            state = 1;
                            break;
                        case "void":
                            type_void = true;
                            state = 1;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 6;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 1:
                    switch (tokenName) {
                        case "ID":
                            //Semantics
                            if (!token.getType().equals("unknown")) {
                                throw new Error("ID already used");
                            }
                            if (type_void && token.getLexeme().equals("main") && SymbolTable.scope == 0) {
                                main_exists = true;
                            }
                            // Updating ID type
                            if (type_void) token.setType("void");
                            else token.setType("int");
//                            System.out.println("^^^^^^^^^^^ " + token.getLexeme() + " " + token.getTokenName() + " " + token.getType() + " " + token.getScope());
                            state = 2;
                            break;
                        default:
                            System.out.println("missing identifier. First possible id from symbol table was added to input."); //TODO
                            lexicalAnalyzer.setRepeatToken();
                            state = 2;
                    }
                    break;
                case 2:
                    switch (tokenName) {
                        case ";":
                            if (type_void) {
                                throw new Error("Variables can't be of type void");
                            }
                            state = 0;
                            break;
                        case "[":
                            if (type_void) {
                                throw new Error("Variables can't be of type void");
                            }
                            state = 3;
                            break;
                        case "(":
                            type_void = false;
                            state = 7;
                            break;
                        default:
                            System.out.println("missing ; token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 0;
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case "NUM":
                            codeGen.pNum(token.getLexeme());
                            state = 4;
                            break;
                        default:
                            System.out.println("missing number. 0 was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 4;
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case "]": // CODE GEN: initiate array
                            state = 5;
                            break;
                        default:
                            System.out.println("missing ] token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 5;
                    }
                    break;
                case 5:
                    switch (tokenName) {
                        case ";":
                            state = 0;
                            break;
                        default:
                            System.out.println("missing ; token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 0;
                    }
                    break;
                case 7:
                    if (grammar.first(new Token(null, "params", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (params()) {
                            state = 8;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 8:
                    switch (tokenName) {
                        case ")": // CODE GEN: put function address in the variable
                            state = 9;
                            break;
                        default:
                            System.out.println("missing ) token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 9;
                    }
                    break;
                case 9:
                    switch (tokenName) {
                        case "{":
                            //Semantics
                            SymbolTable.scope += 1;

                            state = 10;
                            break;
                        default:
                            System.out.println("missing ( token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 10;
                    }
                    break;
                case 10:
                    Token withEpsilon = new Token(null, "declaration-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon).contains(token) || grammar.follow(withEpsilon).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (declaration_list()) {
                            state = 12;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 11:
                    switch (tokenName) {
                        case "}":
                            //Semantics
                            SymbolTable.scope -= 1;

                            state = 0;
                            break;
                        default:
                            System.out.println("missing } token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 0;
                    }
                    break;
                case 12:
                    Token withEpsilon2 = new Token(null, "statement-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon2).contains(token) || grammar.follow(withEpsilon2).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 11;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean params() {
        Token diagramNonTerminal = new Token(null, "params", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            if (state == 7)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "int":
                            state = 1;
                            break;
                        case "void":
                            state = 6;
                            break;
                        default:
                            System.out.println("missing type-specifier. int token was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 1;
                    }
                    break;
                case 1:
                    switch (tokenName) {
                        case "ID":
                            //Semantics
                            token.setType("int");

                            state = 2;
                            break;
                        default:
                            System.out.println("missing identifier. First possible id from symbol table was added to input."); //TODO
                            lexicalAnalyzer.setRepeatToken();
                            state = 2;
                    }
                    break;
                case 2:
                    switch (tokenName) {
                        case "[":
                            state = 3;
                            break;
                        case ",":
                            state = 5;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 7;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case "]": // CODE GEN: initialize array parameter
                            state = 4;
                            break;
                        default:
                            System.out.println("missing ] token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 4;
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case ",":
                            state = 5;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 7;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 5:
                    switch (tokenName) {
                        case "int":
                        case "void":
                            state = 1;
                            break;
                        default:
                            System.out.println("missing type-specifier. int token was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 1;
                    }
                    break;
                case 6:
                    switch (tokenName) {
                        case "ID":
                            //Semantics
                            token.setType("void");

                            state = 2;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 7;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean statement() {
        int state = 0;
        while (true) {
            if (state == 2)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case ";":
                            state = 2;
                            break;
                        case "continue":
                            codeGen.while_continue();
                            state = 1;
                            break;
                        case "break":
                            codeGen.jpTop(switchOrWhile.peek());
                            state = 1;
                            break;
                        case "if":
                            state = 3;
                            break;
                        case "while":
                            switchOrWhile.push(0);
                            codeGen.while_label();
                            state = 9;
                            break;
                        case "return":
                            state = 13;
                            break;
                        case "switch":
                            switchOrWhile.push(1);
                            codeGen.switchInit();
                            state = 15;
                            break;
                        case "{":
                            //Semantics
                            SymbolTable.scope += 1;

                            state = 26;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression()) {
                                    state = 1;
                                } else
                                    return false;
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                            break;
                    }
                    break;
                case 1:
                case 14:
                    switch (tokenName) {
                        case ";":
                            state = 2;
                            break;
                        default:
                            System.out.println("missing ; token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 2;
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case "(":
                            state = 4;
                            break;
                        default:
                            System.out.println("missing ( token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 4;
                    }
                    break;
                case 4:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression()) {
                            state = 5;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 5:
                    // CODE GEN: evaluate expression and jmpsave if false
                    switch (tokenName) {
                        case ")":
                            codeGen.if_save();
                            state = 6;
                            break;
                        default:
                            System.out.println("missing ) token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 6;
                    }
                    break;
                case 6:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement()) {
                            state = 7;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 7:
                    switch (tokenName) {
                        case "else":
                            codeGen.if_jpfSave();
                            state = 8;
                            break;
                        default:
                            System.out.println("missing else token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 8;
                    }
                    break;
                case 8:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement()) {
                            codeGen.if_jp();
                            state = 2;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 9:
                    switch (tokenName) {
                        case "(":
                            state = 10;
                            break;
                        default:
                            System.out.println("missing ( token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 10;
                    }
                    break;
                case 10:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression()) {
                            state = 11;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 11:
                    switch (tokenName) {
                        case ")":
                            codeGen.while_save();
                            state = 12;
                            break;
                        default:
                            System.out.println("missing ) token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 12;
                    }
                    break;
                case 12:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement()) {
                            switchOrWhile.pop();
                            codeGen.whileEnd();
                            state = 2;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 13:
                    switch (tokenName) {
                        case ";":
                            state = 2;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression()) {
                                    state = 14;
                                } else
                                    return false;
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                            break;
                    }
                    break;
                case 15:
                    switch (tokenName) {
                        case "(":
                            state = 16;
                            break;
                        default:
                            System.out.println("missing ( token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 16;
                    }
                    break;
                case 16:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression()) {
                            state = 17;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 17:
                    switch (tokenName) {
                        case ")":
                            state = 18;
                            break;
                        default:
                            System.out.println("missing ) token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 18;
                    }
                    break;
                case 18:
                    switch (tokenName) {
                        case "{":
                            //Semantics
                            SymbolTable.scope += 1;

                            state = 19;
                            break;
                        default:
                            System.out.println("missing { token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 19;
                    }
                    break;
                case 19:
                    switch (tokenName) {
                        case "}":
                            //Semantics
                            SymbolTable.scope -= 1;
                            switchOrWhile.pop();

                            codeGen.switchEnd(false);
                            state = 2;
                            break;
                        case "default":
                            codeGen.switch_jpf();
                            state = 20;
                            break;
                        case "case":
                            state = 23;
                            break;
                        default:
                            System.out.println("missing case token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 23;
                    }
                    break;
                case 20:
                    switch (tokenName) {
                        case ":":
                            state = 21;
                            break;
                        default:
                            System.out.println("missing : token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 21;
                    }
                    break;
                case 21:
                    Token withEpsilon2 = new Token(null, "statement-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon2).contains(token) || grammar.follow(withEpsilon2).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 22;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 22:
                    switch (tokenName) {
                        case "}":
                            //Semantics
                            SymbolTable.scope -= 1;
                            switchOrWhile.pop();

                            codeGen.switchEnd(true);
                            state = 2;
                            break;
                        default:
                            System.out.println("missing } token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 2;
                    }
                    break;
                case 23:
                    switch (tokenName) {
                        case "NUM":
                            codeGen.pNum(token.getLexeme());
                            codeGen.switch_matchCaseJpf();
                            codeGen.switch_save();
                            state = 24;
                            break;
                        default:
                            System.out.println("missing number. 0 was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 24;
                    }
                    break;
                case 24:
                    switch (tokenName) {
                        case ":":
                            state = 25;
                            break;
                        default:
                            System.out.println("missing : token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 25;
                    }
                    break;
                case 25:
                    Token withEpsilon3 = new Token(null, "statement-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon3).contains(token) || grammar.follow(withEpsilon3).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 19;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 26:
                    Token withEpsilon = new Token(null, "declaration-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon).contains(token) || grammar.follow(withEpsilon).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (declaration_list())
                            state = 27;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 27:
                    Token withEpsilon4 = new Token(null, "statement-list", "NONTERMINAL");
                    if (grammar.first(withEpsilon4).contains(token) || grammar.follow(withEpsilon4).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 28;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 28:
                    switch (tokenName) {
                        case "}":
                            //Semantics
                            SymbolTable.scope -= 1;

                            state = 2;
                            break;
                        default:
                            System.out.println("missing } token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 2;
                    }
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean expression() {
        Token diagramNonTerminal = new Token(null, "expression", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            if (state == 15)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "(":
                            state = 1;
                            break;
                        case "NUM":
                            codeGen.pNum(token.getLexeme());
                            state = 9;
                            break;
                        case "ID":
                            //Semantics
                            if (token.getType().equals("unknown")) {
                                int s = SymbolTable.scope;
                                Token t;
                                while (s >= 0) {
                                    try {
                                        t = SymbolTable.get(token.getLexeme(), s);
                                        if (t.getType().equals("unknown"))
                                            throw new Error("ID not defined");
                                        codeGen.pID(t);
                                        break;
                                    } catch (Error e){
                                        s --;
                                    }
                                }
                                if (s < 0) {
                                    SymbolTable.show();
                                    throw new Error("ID not defined");
                                }
                            }

                            state = 4;
                            break;
                        default:
                            System.out.println("missing ( token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 1;
                    }
                    break;
                case 1:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 2;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 2:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        default:
                            System.out.println("missing ) token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 9;
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression())
                                    state = 8;
                                else
                                    return false;
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case "=":
                            state = 0;
                            break;
                        case "(":
                            state = 3;
                            break;
                        case "[":
                            state = 5;
                            break;
                        case "*":
                            codeGen.pOpr(tokenName);
                            state = 10;
                            break;
                        case "+":
                        case "-":
                            codeGen.pOpr(tokenName);
                            state = 16;
                            break;
                        case "<":
                        case "==":
                            codeGen.pOpr(tokenName);
                            state = 13;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 5:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 6;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 6:
                    switch (tokenName) {
                        case "]":
                            codeGen.pIndex();
                            state = 7;
                            break;
                        default:
                            System.out.println("missing ] token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 7;
                    }
                    break;
                case 7:
                    switch (tokenName) {
                        case "=":
                            state = 0;
                            break;
                        case "*":
                            codeGen.pOpr(tokenName);
                            state = 10;
                            break;
                        case "+":
                        case "-":
                            codeGen.pOpr(tokenName);
                            state = 16;
                            break;
                        case "<":
                        case "==":
                            codeGen.pOpr(tokenName);
                            state = 13;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 8:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        case ",":
                            state = 11;
                            break;
                        default:
                            System.out.println("missing , token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 11;
                    }
                    break;
                case 9:
                    switch (tokenName) {
                        case "*":
                            codeGen.pOpr(tokenName);
                            state = 10;
                            break;
                        case "+":
                        case "-":
                            codeGen.pOpr(tokenName);
                            state = 16;
                            break;
                        case "<":
                        case "==":
                            codeGen.pOpr(tokenName);
                            state = 13;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 10:
                    if (grammar.first(new Token(null, "factor", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (factor()) {
                            codeGen.do_opr();
                            state = 9;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 11:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 8;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 12:
                    switch (tokenName) {
                        case "<":
                        case "==":
                            codeGen.pOpr(tokenName);
                            state = 13;
                            break;
                        case "+":
                        case "-":
                            codeGen.pOpr(tokenName);
                            state = 16;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 13:
                    if (grammar.first(new Token(null, "term", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (term()) {
                            codeGen.do_opr();
                            state = 14;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 14:
                    switch (tokenName) {
                        case "+":
                        case "-":
                            codeGen.pOpr(tokenName);
                            state = 17;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 16:
                    if (grammar.first(new Token(null, "term", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (term()) {
                            codeGen.do_opr();
                            state = 12;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 17:
                    if (grammar.first(new Token(null, "term", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (term()) {
                            codeGen.do_opr();
                            state = 14;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean term() {
        Token diagramNonTerminal = new Token(null, "term", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            if (state == 2)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    if (grammar.first(new Token(null, "factor", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (factor())
                            state = 1;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 1:
                    switch (tokenName) {
                        case "*":
                            codeGen.pOpr(tokenName);
                            state = 3;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 2;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 3:
                    if (grammar.first(new Token(null, "factor", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (factor()) {
                            codeGen.do_opr();
                            state = 1;
                        } else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean factor() {
        Token diagramNonTerminal = new Token(null, "factor", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            if (state == 3)
                return true;
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "(":
                            state = 1;
                            break;
                        case "NUM":
                            codeGen.pNum(token.getLexeme());
                            state = 3;
                            break;
                        case "ID":
                            //Semantics
                            if (token.getType().equals("unknown")) {
                                int s = SymbolTable.scope;
                                Token t;
                                while (s >= 0) {
                                    try {
                                        t = SymbolTable.get(token.getLexeme(), s);
                                        if (t.getType().equals("unknown"))
                                            throw new Error("ID not defined");
                                        codeGen.pID(t);
                                        break;
                                    } catch (Error e){
                                        s --;
                                        System.out.println("oof");
                                    }
                                }
                                if (s < 0) {
                                    SymbolTable.show();
                                    throw new Error("ID not defined");
                                }
                            }

                            state = 4;
                            break;
                        default:
                            System.out.println("missing ( token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 1;
                    }
                    break;
                case 1:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 2;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 2:
                    switch (tokenName) {
                        case ")":
                            state = 3;
                            break;
                        default:
                            System.out.println("missing ) token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 3;
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case "[":
                            state = 5;
                            break;
                        case "(":
                            state = 7;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 3;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 5:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 6;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 6:
                    switch (tokenName) {
                        case "]":
                            codeGen.pIndex();
                            state = 3;
                            break;
                        default:
                            System.out.println("missing ] token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 3;
                    }
                    break;
                case 7:
                    switch (tokenName) {
                        case ")":
                            state = 3;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression())
                                    state = 8;
                                else
                                    return false;
                            } else
                                System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    }
                    break;
                case 8:
                    switch (tokenName) {
                        case ",":
                            state = 9;
                            break;
                        case ")":
                            state = 3;
                            break;
                        default:
                            System.out.println("missing , token. It was added to input.");
                            lexicalAnalyzer.setRepeatToken();
                            state = 9;
                    }
                    break;
                case 9:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 8;
                        else
                            return false;
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                default:
                    return false;
            }
        }
    }

    private boolean statement_list() {
        Token diagramNonTerminal = new Token(null, "statement-list", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            if (state == 2)
                return true;
            Token token = lexicalAnalyzer.getToken();
            switch (state) {
                case 0:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement())
                            state = 1;
                        else
                            return false;
                    } else if (follow.contains(token)) {
                        state = 2;
                        lexicalAnalyzer.setRepeatToken();
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                case 1:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement())
                            state = 0;
                        else
                            return false;
                    } else if (follow.contains(token)) {
                        state = 2;
                        lexicalAnalyzer.setRepeatToken();
                    } else
                        System.out.println("unexpected token " + token.getLexeme() + " in input! skipping.");
                    break;
                default:
                    return false;
            }
        }
    }
}
