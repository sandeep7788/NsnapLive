package chatroom.snap.snaplive.Notification;

public class DeleteMessage {
 String   to,message,nType = "deleteMess" ,senderId,Mid;

    public DeleteMessage() {
    }

    public DeleteMessage(String to, String message, String senderId, String mid) {
        this.to = to;
        this.message = message;
        this.senderId = senderId;
        Mid = mid;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMid() {
        return Mid;
    }

    public void setMid(String mid) {
        Mid = mid;
    }
}
