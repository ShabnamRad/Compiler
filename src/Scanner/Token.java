package Scanner;

/**
 * Created by Shabnam on 1/21/2019
 */
public class Token {
    private String lexeme = "";
    private TokenID tokenID = null;

    public void setTokenID(TokenID tokenID) {
        this.tokenID = tokenID;
    }

    public void extendLexeme(char c) {
        this.lexeme += c;
    }

    public TokenID getTokenID() {
        return tokenID;
    }

    public String getLexeme() {
        return lexeme;
    }
}

enum TokenID {
    EOF("EOF"),
    ID("ID"),
    LBR("["),
    RBR("]"),
    NUM("NUM"),
    SEMICOLON(";"),
    INT("int"),
    VOID("void"),
    LPR("("),
    RPR(")"),
    COMMA(","),
    LCB("{"),
    RCB("}"),
    CONTINUE("continue"),
    BREAK("break"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    RETURN("return"),
    SWITCH("switch"),
    CASE("case"),
    DEFAULT("default"),
    ASSIGN("="),
    LT("<"),
    EQ("=="),
    ADD("+"),
    SUB("-"),
    MULT("*"),
    IDENTIFIER(null),
    INTEGER(null),
    ERROR(null);

    String regexStr;

    TokenID(String regexStr) {
        this.regexStr = regexStr;
    }
}
