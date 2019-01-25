package Scanner;

/**
 * Created by Shabnam on 1/21/2019.
 */
public class Token {

    private String lexeme, tokenName, type;

    Token(String inp) {
        if (!checkSymbols(inp) && !checkReservedWord(inp)) {
            throw new Error("Token is identifier");
        }
    }

    Token(String inp, String name, String type) {
        this.lexeme = inp;
        this.tokenName = name;
        this.type = type;
    }

    public String getLexeme() { return lexeme; }
    public String getTokenName() { return tokenName; }
    public String getType() { return type; }

    public boolean match(String inp) {
        return this.lexeme.equals(inp);
    }

    private boolean checkSymbols(String inp) {
        boolean val = true;
        this.lexeme = inp;
        this.type = "special";
        switch (inp) {
            case ";":
                this.tokenName = "semi-colon";
                break;
            case ":":
                this.tokenName = "colon";
                break;
            case ",":
                this.tokenName = "comma";
                break;
            case "+":
                this.tokenName = "summation";
                break;
            case "-":
                this.tokenName = "subtraction";
                break;
            case "*":
                this.tokenName = "multiplication";
                break;
            case "=":
                this.tokenName = "assign";
                break;
            case "(":
                this.tokenName = "parentheses-open";
                break;
            case ")":
                this.tokenName = "parentheses-close";
                break;
            case "[":
                this.tokenName = "brackets-open";
                break;
            case "]":
                this.tokenName = "brackets-close";
                break;
            case "{":
                this.tokenName = "curly-braces-open";
                break;
            case "}":
                this.tokenName = "curly-braces-close";
                break;
            case "<":
                this.tokenName = "less-than";
                this.type = "relop";
                break;
            case "==":
                this.tokenName = "equals";
                this.type = "relop";
                break;
            case "\uFFFF":
                this.tokenName = "EOF";
                break;
            case "$":
                this.tokenName = "dollar";
                break;
            case "\\eps":
                this.tokenName = "epsilon";
                break;
            default:
                val = false;
                this.lexeme = null;
                this.type = null;
        }
        return val;
    }

    private boolean checkReservedWord(String inp) {
        boolean val = true;
        switch (inp) {
            case "int":
                this.tokenName = "Integer";
                this.type = "type-specifier";
                break;
            case "void":
                this.tokenName = "Void";
                this.type = "type-specifier";
                break;
            case "continue":
                this.tokenName = "Continue";
                this.type = "loop-operator";
                break;
            case "break":
                this.tokenName = "Break";
                this.type = "loop-operator";
                break;
            case "while":
                this.tokenName = "While";
                this.type = "loop-operator";
                break;
            case "if":
                this.tokenName = "If";
                this.type = "conditional-operator";
                break;
            case "else":
                this.tokenName = "Else";
                this.type = "conditional-operator";
                break;
            case "switch":
                this.tokenName = "Switch";
                this.type = "conditional-operator";
                break;
            case "case":
                this.tokenName = "Case";
                this.type = "conditional-operator";
                break;
            case "default":
                this.tokenName = "Default";
                this.type = "conditional-operator";
                break;
            default:
                val = false;
        }
        if (val) {
            this.lexeme = inp;
        }
        return val;
    }
}
