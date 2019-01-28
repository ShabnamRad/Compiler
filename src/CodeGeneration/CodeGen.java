package CodeGeneration;

import Scanner.Token;

public class CodeGen {

    private static int index = 0, temp = 1000, addr = 1500;
    public static String[] ProgramBlock = new String[2000];

    private static int[] ss_while = new int[100];
    private static int top_while = -1;

    private static int[] ss_if = new int[100];
    private static int top_if = -1;

    private static int[] ss_switch = new int[100];
    private static int top_switch = -1;


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
        ProgramBlock[ss_while[top_while]] = "(JPF, " + "X, " + index + ", )";
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
        ProgramBlock[ss_if[top_if]] = "(JPF, " + "X, " + (index + 1) + ", )";
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

    public void switch_matchCaseJpf(Token t) {
        System.out.println("#switch_matchCaseJpf");
        int D = getTemp();
        ProgramBlock[index] = "(EQ, X, #" + t.getLexeme() + ", " + D + ")";
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
}
