package edu.wpi.MochaManticores.messaging;

import edu.wpi.MochaManticores.App;
import edu.wpi.MochaManticores.Nodes.EdgeMapSuper;
import edu.wpi.MochaManticores.Nodes.MapSuper;
import edu.wpi.MochaManticores.database.DatabaseManager;
import edu.wpi.MochaManticores.database.sel;
import edu.wpi.MochaManticores.views.HomePage;
import edu.wpi.MochaManticores.views.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.util.Iterator;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class clientReader extends SceneController implements Runnable{
    public HashMap<String,LinkedList<Message>> messageHistory = new HashMap<>();
    private Socket socket;
    private messageClient client = null;
    private messageClientPage clientPage = null;
    private DataInputStream input = null;
    private boolean GUIconnected = false;
    private String username = null;

    //Constructor
    public clientReader(Socket socket, String username, messageClient client){
        this.client = client;
        this.socket = socket;
        this.username = username;
    }

    public void startGUI(messageClientPage page){
        this.clientPage = page;
        this.GUIconnected = true;
    }

    public void stopGUI(){
        this.clientPage = null;
        this.GUIconnected = false;
    }

    public HashMap<String,LinkedList<Message>> getHistory(){
        return messageHistory;
    }

    @Override
    public void run() {
        boolean running = true;
        while(running){
            try{
                input = new DataInputStream(socket.getInputStream());
                Message msg = new Message(input.readUTF());


                if(msg.TYPE == Message.msgType.SHUTDOWN){
                    if(msg.sender.equals("SERVER")){
                        msg.sender = username;
                        client.sendMsg(msg);
                    }
                    running = false;
                    break;
                }else if(msg.TYPE == Message.msgType.UPDATE){
                    DatabaseManager.refreshTable(DatabaseManager.getManagerFromString(msg.target));
                    continue;
                }

                store(msg);

                if(this.GUIconnected){
                    postMessage(msg);
                }else if(msg.TYPE != Message.msgType.DATAGRAB){
                    postNotif(msg);
                }
                Platform.runLater(() -> {

                    try {
                        clientPage.updateConvos();
                    } catch (NullPointerException ignore) {}
                });

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void postMessage(Message msg){
        // don't delete
        Platform.runLater(() -> {
            if (msg.target.equals(App.getCurrentUsername())){
                clientPage.loadConversation(msg.sender);
            } else {
                clientPage.loadConversation(msg.target);
            }
        });
    }

    public void postNotif(Message msg){
        //post to some text feild
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                Notifications notifications = Notifications.create()
                        .owner(App.getPrimaryStage())
                        .title(msg.sender)
                        .text(msg.body)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(event -> {
                            changeSceneTo("landingPage");
                            final HomePage home = new HomePage();
                            try {
                                home.goToChatPage(null);
                                final messageClientPage messagePage = new messageClientPage();
                                clientPage.updateConvos();
                                //clientPage.highlightConvo(msg.sender);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                notifications.show();
            }
        });
    }


    private void store(Message msg){
        // add messages to hashmap
        // store messages by conversation -> aka hash by whatever user we are not
        String hash = "";
        if(!msg.sender.equals(username)){
            //hash by sender
            hash = msg.sender;
        }else if(!msg.target.equals(username)){
            // hash by target
            hash = msg.target;
        }

        //if nothing matches throw it out
        if(hash.equals("")){
            return;
        }

        //hash the message into the linked list stored at that key value location
        if(messageHistory.containsKey(hash)){
            messageHistory.get(hash).add(msg);
        }else{
            LinkedList<Message> list = new LinkedList<>();
            list.add(msg);
            messageHistory.put(hash,list);
        }
    }
}
