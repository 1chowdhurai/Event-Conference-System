package Controller;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import UseCase.UserManager;
import Controller.LoginSystem;

// Allows current user to message some other given user.
// Returns a list of message contents from a specific user and sends it to MessagePresenter
// Gives option to user to reply to some message.

public class MessengerSystem {

    private UserManager user;

    public void MessageUser() {

        // ask them who they want to send it to
        // check to see if this user is in their contact list
        // ask them what they want to send (the content of the message)
        // access the current date and time
        // call user.message();

        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the username you want to send a message to:");
        String receiver = myObj.nextLine();
        if (!user.getContactList().contains(receiver)) {
            System.out.println("You do not have this user in your contact list.");
            return;
        }
        System.out.println("Enter the content of your message:");
        String content = myObj.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        user.message(user.username, receiver, content, dateFormat, timeFormat);
    }

    public void getSpecificMessages() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter a username to see the messages you have had with them.");
        String receiver = myObj.nextLine();
        if (!user.getContactList().contains(receiver)) {
            System.out.println("You do not have this user in your contact list.");
            return;
        for (String msg : user.getMessages(user.username(), receiver)) {
            System.out.println(msg);
        }
        }
    }

    public void replyToMsg() {

        // how would we reply to the msg?
        // we cannot use the message method because then the user would be able to reply to anyone without having
        // received a message from them

    }


}
