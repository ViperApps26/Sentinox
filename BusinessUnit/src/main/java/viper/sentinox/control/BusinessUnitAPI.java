package viper.sentinox.control;

import io.javalin.Javalin;

public class BusinessUnitAPI {

    private final MedicineDataMart dataMart;

    public BusinessUnitAPI(MedicineDataMart dataMart) {
        this.dataMart = dataMart;
    }

    public void start() {
        Thread apiThread = createApiThread();
        apiThread.setDaemon(false);
        apiThread.start();
    }

    private Thread createApiThread() {
        return new Thread(() -> {
            Javalin app = Javalin.create();
            routes(app);
            app.start(8080);
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ignored) {
            }
        });
    }

    private void routes(Javalin app) {
        app.get("/summary", ctx -> ctx.result(dataMart.getMedicinesSummary()));
        app.get("/medicine/{name}", ctx -> ctx.result(dataMart.getMedicineDetails(ctx.pathParam("name"))));
        app.get("/medicine/{name}/reactions", ctx -> ctx.result(dataMart.getMedicineReactions(ctx.pathParam("name"))));
        app.get("/medicine/{name}/comments", ctx -> ctx.result(dataMart.getMedicineComments(ctx.pathParam("name"))));
        app.get("/medicine/{name}/sentiment", ctx -> ctx.result(dataMart.getMedicineSentiment(ctx.pathParam("name"))));
    }
}
