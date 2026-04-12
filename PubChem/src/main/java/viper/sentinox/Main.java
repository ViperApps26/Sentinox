package viper.sentinox;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String databaseURL = args[2];

        PubChemConnect pubChemConnect = new PubChemConnect();
        PubChemGet pubChemGet = new PubChemGet(pubChemConnect);
        PubChemPrint pubChemPrint = new PubChemPrint(pubChemGet);

        PubChemControl pubChemControl = new PubChemControl(pubChemConnect, pubChemPrint);


        //DatabaseCreator.createDatabases(databaseURL);

        Scanner scanner = new Scanner(System.in);
        String command = pubChemControl.askCommand(scanner);

        while (!command.equals("exit")) {
            pubChemControl.processCommand(command, databaseURL);
            command = pubChemControl.askCommand(scanner);
        }
    }
}
