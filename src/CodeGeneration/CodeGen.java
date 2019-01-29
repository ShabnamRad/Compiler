package CodeGeneration;

import Scanner.Token;
import SymbolTable.SymbolTable;

public class CodeGen {

    private static int index = 0, temp = 1000, addr = 1500;
    public static String[] ProgramBlock = new String[2000];

    private static int[] ss_while = new int[100];
    private static int top_while = -1;

    private static int[] ss_if = new int[100];
    private static int top_if = -1;

    private static int[] ss_switch = new int[100];
    private static int top_switch = -1;

    private static String[] ss_expr = new String[100];
    private static int top_expr = -1;

    private static String[] ss_opr = new String[100];
    private static int top_opr = -1;


    private static int getTemp() {
        temp += 4;
        return temp - 4;
    }

    private static int getAddr() {
        addr += 4;
        return addr - 4;
    }

    private static void push_while(int i) {
        top_while ++;
        ss_while[top_while] = i;
        System.out.println("pushed " + i + " in ss_while");
    }
    
    private static int pop_while() {
        top_while --;
        System.out.println("popped from ss_while");
        return ss_while[top_while + 1];
    }
    
    private static void pop_while(int n) {
        for (int i = 0; i < n; i++) {
            pop_while();
        }
    }

    private static void push_if(int i) {
        top_if ++;
        ss_if[top_if] = i;
        System.out.println("pushed " + i + " in ss_if");
    }

    private static int pop_if() {
        top_if --;
        System.out.println("popped from ss_if");
        return ss_if[top_if + 1];
    }

    private static void push_switch(int i) {
        top_switch ++;
        ss_switch[top_switch] = i;
        System.out.println("pushed " + i + " in ss_switch");
    }

    private static int pop_switch() {
        top_switch --;
        System.out.println("popped from ss_switch");
        return ss_switch[top_switch + 1];
    }

    private static void pop_switch(int n) {
        for (int i = 0; i < n; i++) {
            pop_switch();
        }
    }

    private static void push_expr(String s) {
        top_expr ++;
        ss_expr[top_expr] = s;
        System.out.println("pushed " + s + " in ss_expr");
    }

    private static String pop_expr() {
        top_expr --;
        System.out.println("popped from ss_expr");
        return ss_expr[top_expr + 1];
    }

    private static void pop_expr(int n) {
        for (int i = 0; i < n; i++) {
            pop_expr();
        }
    }

    private static void push_opr(String s) {
        top_opr ++;
        ss_opr[top_opr] = s;
        System.out.println("pushed " + s + " in ss_opr");
    }

    private static String pop_opr() {
        top_opr --;
        System.out.println("popped from ss_opr");
        return ss_opr[top_opr + 1];
    }

    /*
    Code generation procedures start below
     */

    //TODO: X is for expression, and must be popped after usage

    public void while_label() {
        System.out.println("#while_label");
        ProgramBlock[index] = "(JP, " + (index + 2) + ", , )";
        index ++;
        push_while(index);
        index ++;
        push_while(index);
    }

    public void while_save() {
        System.out.println("#while_save");
        push_while(index);
        index ++;
    }

    public void whileEnd() {
        System.out.println("#whileEnd");
        ProgramBlock[index] = "(JP, " + ss_while[top_while - 1] + ", , )";
        index ++;
        String X = pop_expr();
        ProgramBlock[ss_while[top_while]] = "(JPF, " + X + ", " + index + ", )";
        ProgramBlock[ss_while[top_while - 2]] = "(JP, " + index + ", , )";
        pop_while(3);
    }

    public void if_save() {
        System.out.println("#if_save");
        push_if(index);
        index ++;
    }

    public void if_jpfSave() {
        System.out.println("#if_jpfSave");
        String X = pop_expr();
        ProgramBlock[ss_if[top_if]] = "(JPF, " + X + ", " + (index + 1) + ", )";
        pop_if();
        push_if(index);
        index ++;
    }

    public void if_jp() {
        System.out.println("#if_jp");
        ProgramBlock[ss_if[top_if]] = "(JP, " + index + ", , )";
        pop_if();
    }

    public void switchInit() {
        System.out.println("#switchInit");
        ProgramBlock[index] = "(JP, " + (index + 2) + ", , )";
        index ++;
        ProgramBlock[index] = "__";
        push_switch(index);
        push_switch(-1);
        push_switch(index);
        index ++;
    }

    public void switch_matchCaseJpf() {
        System.out.println("#switch_matchCaseJpf");
        int D = getTemp();
        String X = ss_expr[top_expr - 1];
        String caseNum = pop_expr();
        ProgramBlock[index] = "(EQ, X, " + caseNum + ", " + D + ")";
        ProgramBlock[ss_switch[top_switch]] = "(JPF, " + ss_switch[top_switch - 1] + ", " + index + ", )";
        pop_switch(2);
        index ++;
        push_switch(D);
    }

    public void switch_save() {
        System.out.println("#switch_save");
        ProgramBlock[index] = "__";
        push_switch(index);
        index ++;
    }

    public void switch_jpf() {
        System.out.println("#switch_jpf");
        ProgramBlock[ss_switch[top_switch]] = "(JPF, " + ss_switch[top_switch - 1] + ", " + index + ", )";
        pop_switch(2);
    }

    public void switchEnd(boolean hasDefault) {
        if (hasDefault) {
            System.out.println("#switchEnd_withDefault");
            ProgramBlock[ss_switch[top_switch]] = "(JP, " + index + ", , )";
        } else {
            System.out.println("#switchEnd_withoutDefault");
            ProgramBlock[ss_switch[top_switch]] = "(JPF, " + ss_switch[top_switch - 1] + ", " + index + ", )";
            pop_switch(2);
            ProgramBlock[ss_switch[top_switch]] = "(JP, " + index + ", , )";
        }
    }

    public void jpTop(int switchOrWhile) {
        if (switchOrWhile == 0) {
            System.out.println("#while_jpTop");
            ProgramBlock[index] = "(JP, " + ss_while[top_while - 2] + ", , )";
            index ++;
        } else if (switchOrWhile == 1) {
            System.out.println("#switch_jpTop");
            ProgramBlock[index] = "(JP, " + ss_switch[top_switch - 2] + " , , )";
            index ++;
        } else System.err.println("Parse Error (break outside of while or switch)"); // Semantic Check
    }

    public void do_opr() {
        System.out.println("#do_opr");
        String opr = pop_opr();
        String a = pop_expr();
        String b = pop_expr();
        int D = getTemp();
        String code = "";
        switch(opr) {
            case "+":
                code = "(ADD, " + a + ", " + b + ", " + D + ")";
                break;
            case "-":
                code = "(SUB, " + a + ", " + b + ", " + D + ")";
                break;
            case "*":
                code = "(MULT, " + a + ", " + b + ", " + D + ")";
                break;
            case "<":
                code = "(LT, " + a + ", " + b + ", " + D + ")";
                break;
            case "==":
                code = "(EQ, " + a + ", " + b + ", " + D + ")";
                break;
        }
        ProgramBlock[index] = code;
        index ++;
        push_expr(Integer.toString(D));
    }

    public void pOpr(String s) {
        System.out.println("#pOpr");
        push_opr(s);
    }

    public void pNum(String lexeme) {
        System.out.println("pNum");
        push_expr("#" + lexeme);
    }

    public void pID(String lexeme) {
        Token t = SymbolTable.get(lexeme);
        int addr = t.getAddr();
        push_expr(Integer.toString(addr));
    }

    public void pIndex() {
        String expr = pop_expr();
        String addrID = pop_expr();
        push_expr("@" + addrID);
        if(expr.startsWith("#")) {
            push_opr("+");
            push_expr(Integer.toString(Integer.parseInt(expr.substring(1)) * 4));
            do_opr();
        } else {
            push_opr("+");
            push_expr(expr);
            push_opr("*");
            push_expr("#4");
            do_opr();
            do_opr();
        }
    }
}
