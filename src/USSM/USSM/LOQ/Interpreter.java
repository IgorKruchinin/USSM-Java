package USSM.USSM.LOQ;

import java.util.Scanner;

public class Interpreter {
    public static void main(String[] args) {
        LOQ lang = new LOQ();
        while(true) {
            System.out.println(lang.getProfName() + " >");
            Scanner scanner = new Scanner(System.in);
            try {
                lang.parseQuery(scanner.nextLine());
            } catch (LOQNoProfileException e) {
                System.out.println("No profiles with this name");
            }
        }
    }
}
