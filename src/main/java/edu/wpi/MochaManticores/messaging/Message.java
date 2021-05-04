package edu.wpi.MochaManticores.messaging;

public class Message {
    enum msgType {DATAGRAB,UPDATE,MSGPOST};

    public String sender;
    public String target;
    public String body;
    public msgType TYPE;
    private String delim = "[|]+";

    public Message(String sender, String target, String body){
        this.sender = sender;
        this.target = target;
        this.body = body;
        this.TYPE = msgType.MSGPOST;
    }

    public Message(String sender, String target, String body, msgType type){
        this.sender = sender;
        this.target = target;
        this.body = body;
        this.TYPE = type;
    }

    public Message(String writeFormat){
        // converts message from writeformat to a message object
        String[] tokens = writeFormat.split(delim);
        this.sender = tokens[0];
        this.target = tokens[1];
        this.TYPE = typeConverter(tokens[2]);
        this.body = tokens[3];
    }

    public String toWriteFormat(){
        return sender +"|"+target+"|"+stringConverter(TYPE)+"|"+body;
    }

    public msgType typeConverter(String type){
        switch(type){
            case "DATAGRAB":
                return msgType.DATAGRAB;
            case "UPDATE":
                return msgType.UPDATE;
            default:
                return msgType.MSGPOST;
        }
    }

    public String stringConverter(msgType type){
        switch(type){
            case DATAGRAB:
                return "DATAGRAB";
            case  UPDATE:
                return "UPDATE";
            default:
                return "MSGPOST";
        }
    }
}