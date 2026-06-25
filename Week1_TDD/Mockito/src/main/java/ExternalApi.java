public interface ExternalApi {
    String getData();
    String getDataById(int id);
    void sendNotification(String message);
    void deleteRecord(int id);
}
