package viper.sentinox;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Event Store Builder...");
        EventStoreBuilder builder = new EventStoreBuilder();
        builder.store();
    }
}