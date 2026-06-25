public class MyService {

    private ExternalApi api;

    public MyService(ExternalApi api) {
        this.api = api;
    }

    public String fetchData() {
        return api.getData();
    }

    public String fetchDataById(int id) {
        return api.getDataById(id);
    }

    public void notifyUser(String message) {
        api.sendNotification(message);
    }

    public void removeRecord(int id) {
        api.deleteRecord(id);
    }

    public void processAll() {
        api.getData();
        api.sendNotification("Processing done");
    }
}
