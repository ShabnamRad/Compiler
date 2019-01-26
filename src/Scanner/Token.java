package Scanner;

/**
 * Created by Shabnam on 1/21/2019.
 */
public class Token {

    private String lexeme, tokenName, type;

    public Token(String inp) {
        if (!checkSymbols(inp) && !checkReservedWord(inp)) {
            throw new Error("Token is identifier");
        }
    }

    public Token(String inp, String name, String type) {
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
                this.tokenName = ";";
                break;
            case ":":
                this.tokenName = ":";
                break;
            case ",":
                this.tokenName = ",";
                break;
            case "+":
                this.tokenName = "+";
                break;
            case "-":
                this.tokenName = "-";
                break;
            case "*":
                this.tokenName = "*";
                break;
            case "=":
                this.tokenName = "=";
                break;
            case "(":
                this.tokenName = "(";
                break;
            case ")":
                this.tokenName = ")";
                break;
            case "[":
                this.tokenName = "[";
                break;
            case "]":
                this.tokenName = "]";
                break;
            case "{":
                this.tokenName = "{";
                break;
            case "}":
                this.tokenName = "}";
                break;
            case "<":
                this.tokenName = "<";
                this.type = "relop";
                break;
            case "==":
                this.tokenName = "==";
                this.type = "relop";
                break;
            case "\uFFFF":
                this.tokenName = "EOF";
                break;
            case "$":
                this.tokenName = "$";
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
                this.tokenName = "int";
                this.type = "type-specifier";
                break;
            case "void":
                this.tokenName = "void";
                this.type = "type-specifier";
                break;
            case "return":
                this.tokenName = "return";
                this.type = "function-operator";
                break;
            case "continue":
                this.tokenName = "continue";
                this.type = "loop-operator";
                break;
            case "break":
                this.tokenName = "break";
                this.type = "loop-operator";
                break;
            case "while":
                this.tokenName = "while";
                this.type = "loop-operator";
                break;
            case "if":
                this.tokenName = "if";
                this.type = "conditional-operator";
                break;
            case "else":
                this.tokenName = "else";
                this.type = "conditional-operator";
                break;
            case "switch":
                this.tokenName = "switch";
                this.type = "conditional-operator";
                break;
            case "case":
                this.tokenName = "case";
                this.type = "conditional-operator";
                break;
            case "default":
                this.tokenName = "default";
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

    @Override
    public boolean equals(Object obj) {
        try{
            Token token = (Token) obj;
            return token.tokenName.equals(this.tokenName) && token.type.equals(this.type);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return tokenName.hashCode();
    }

    @Override
    public String toString() {
        return "( " + lexeme + ", " + tokenName + ", " + type + " )";
    }
}
