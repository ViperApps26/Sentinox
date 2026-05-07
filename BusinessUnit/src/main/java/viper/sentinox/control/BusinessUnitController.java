package viper.sentinox.control;

import viper.sentinox.model.MedicineDataMart;
import viper.sentinox.subscriber.ActiveMQBusinessSubscriber;
import viper.sentinox.view.ConsoleView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BusinessUnitController {

    private final ActiveMQBusinessSubscriber subscriber;
    private final MedicineDataMart dataMart;
    private final ConsoleView view;

    public BusinessUnitController(ActiveMQBusinessSubscriber subscriber,
                                  MedicineDataMart dataMart,
                                  ConsoleView view) {
        this.subscriber = subscriber;
        this.dataMart = dataMart;
        this.view = view;
    }

    public void start() {
        view.show("Starting Business Unit...");
        startSummaryPrinter();
        subscriber.start();
    }

    private void startSummaryPrinter() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(
                () -> view.show(dataMart.getSummary()),
                30,
                30,
                TimeUnit.SECONDS
        );
    }
}