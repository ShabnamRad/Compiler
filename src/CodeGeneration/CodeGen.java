package CodeGeneration;

public class CodeGen {

    private static int index = 0;
    public static String[] ProgramBlock = new String[2000];

    private static int[] ss_while = new int[100];
    private static int top_while = -1;

    private static int[] ss_if = new int[100];
    private static int top_if = -1;


    
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

    private static void pop_if(int n) {
        for (int i = 0; i < n; i++) {
            pop_if();
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
        ProgramBlock[index] = "__";
        push_while(index);
        index ++;
        ProgramBlock[index] = "__";
        push_while(index);
    }

    public void while_save() {
        System.out.println("#while_save");
        ProgramBlock[index] = "__";
        push_while(index);
        index ++;
    }

    public void whileEnd() {
        System.out.println("#whileEnd");
        ProgramBlock[index] = "(JP, " + ss_while[top_while - 1] + ", , )";
        index ++;
        ProgramBlock[ss_while[top_while]] = "(JPF, " + "X, " + index + ", )";
        ProgramBlock[ss_while[top_while - 2]] = "(JP, " + index + ", , )";
        index ++;
        pop_while(3);

    }

    public void if_save() {
        System.out.println("#if_save");
        ProgramBlock[index] = "__";
        push_if(index);
        index ++;
    }

    public void if_jpfSave() {
        System.out.println("#if_jpfSave");
        ProgramBlock[ss_if[top_if]] = "(JPF, " + "X, " + (index + 1) + ", )";
        pop_if();
        ProgramBlock[index] = "__";
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

    }

    public void switch_matchCase() {
        System.out.println("#switch_matchCase");
    }

    public void switch_jpfSave() {
        System.out.println("#switch_jpfSave");
    }

    public void switchEnd() {
        System.out.println("#switchEnd");
    }

    public void jpTop(int switchOrWhile) {
        if (switchOrWhile == 0) {
            System.out.println("#while_jpTop");
            ProgramBlock[index] = "(JP, " + ss_while[top_while - 2] + ", , )";
            index ++;
        } else if (switchOrWhile == 1) {
            System.out.println("#switch_jpTop");
        } else System.err.println("Parse Error (break outside of while or switch)"); // Semantic Check
    }

}
