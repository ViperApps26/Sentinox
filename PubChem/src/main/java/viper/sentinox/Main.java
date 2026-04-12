package viper.sentinox;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        String databaseURL = args[0];

        PubChemConnect pubChemConnect = new PubChemConnect();
        PubChemGet pubChemGet = new PubChemGet(pubChemConnect);
        PubChemPrint pubChemPrint = new PubChemPrint(pubChemGet);

        PubChemDatabaseCreator pubChemDatabaseCreator = new PubChemDatabaseCreator();
        PubChemInsert pubChemInsert = new PubChemInsert(pubChemConnect, pubChemGet);
        PubChemDataViewer pubChemDataViewer = new PubChemDataViewer();
        PubChemFeeder pubChemFeeder = new PubChemFeeder(pubChemInsert);

        PubChemControl pubChemControl = new PubChemControl(
                pubChemConnect,
                pubChemPrint,
                pubChemFeeder,
                pubChemDataViewer
        );

        pubChemDatabaseCreator.createDatabase(databaseURL);

        Scanner scanner = new Scanner(System.in);
        String command = pubChemControl.askCommand(scanner);

        while (!command.equals("exit")) {
            pubChemControl.processCommand(command, databaseURL);
            command = pubChemControl.askCommand(scanner);
        }
    }
}