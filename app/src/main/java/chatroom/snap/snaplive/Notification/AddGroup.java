package chatroom.snap.snaplive.Notification;

public class AddGroup {
   String senderName,senderAva,to,message,nType="addG",senderId,Mid;

    public AddGroup() {
    }

    public AddGroup(String senderName, String senderAva, String to, String message, String senderId, String mid) {
        this.senderName = senderName;
        this.senderAva = senderAva;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAva() {
        return senderAva;
    }

    public void setSenderAva(String senderAva) {
        this.senderAva = senderAva;
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
