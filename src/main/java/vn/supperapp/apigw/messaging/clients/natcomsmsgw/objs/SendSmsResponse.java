package vn.supperapp.apigw.messaging.clients.natcomsmsgw.objs;

public class SendSmsResponse {
    private int status;
    private String message;
    private String response;

    public SendSmsResponse() {
    }

    public SendSmsResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
