package GUI.MessageMenus;

import Controller.MessengerSystem;
import GUI.Main.PanelStack;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContactsGUI implements IMessageView{
    private MessagePanel panelHelper = new MessagePanel();
    private MessengerSystem messenger;
    private PanelStack panelStack;

    private MessagePanelBuilder contactBuilder;
    private MessagePanelBuilder sendMsgBuilder;
    private MessagePanelBuilder viewAllBuilder;
    private MessagePanelBuilder addContactBuilder;

    private JPanel contactPanel = new JPanel();
    private JPanel sendMsgPanel = new JPanel();
    private JPanel viewAllPanel = new JPanel();
    private JPanel addContactPanel = new JPanel();

    private JButton backButton;

    //contactPanel elements
    private JList contactsJList;
    private JScrollPane contactPane;
    private DefaultListModel<String> contactListModel = new DefaultListModel<>();
    private JButton addButton;
    private int currContactIndex;
    private String currSelectedContact;
    private List<JButton> contactOptions;

    //sendMsgPanel
    private JScrollPane currMessagePane;
    private JTextArea currMessageText;
    private JButton sendMsgButton;

    //viewAllPanel
    private JScrollPane currViewMessagePane;


    //addContactPanel
    private JButton internalAddButton;
    private JScrollPane addContactPane;
    private JList addContactJList;
    private DefaultListModel<String> addContactListModel = new DefaultListModel<>();
    private JTextField addUserTextField;



    public ContactsGUI(MessengerSystem messenger, PanelStack panelStack){
        this.messenger = messenger;
        this.panelStack = panelStack;
        contactBuilder = new MessagePanelBuilder(contactPanel);
        sendMsgBuilder = new MessagePanelBuilder(sendMsgPanel);
        viewAllBuilder = new MessagePanelBuilder(viewAllPanel);
        addContactBuilder = new MessagePanelBuilder(addContactPanel);

    }

    private void contactListListener(List<JButton> contactOptions){
        contactsJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currContactIndex = contactsJList.getSelectedIndex();
                if (currContactIndex != -1) {
                    currSelectedContact = contactListModel.get(currContactIndex);
                    panelHelper.enableButtons(contactOptions);
                }
            }
        });

        //send msg
        contactOptions.get(0).addActionListener( e -> panelStack.loadPanel(buildsSendMsgPanel()));

        // remove
        contactOptions.get(1).addActionListener(e -> {
            messenger.removeUser(currSelectedContact);
            contactListModel.removeElement(currSelectedContact);


        });

        // view msgs
        contactOptions.get(2).addActionListener(e -> panelStack.loadPanel(buildViewAllPanel()));
    }

    /**
     * Constructs the main contact page
     * @return the main contact page
     */
    public JPanel mainPage() {
        return buildContactPanel();
    }

    private void loadContacts(DefaultListModel listModel){
        listModel.clear();
        for (String s: messenger.getContacts()){
            if (!listModel.contains(s))
                listModel.addElement(s);
        }
    }

    private JPanel buildContactPanel(){
        loadContacts(contactListModel);
        contactsJList = contactBuilder.buildJList(contactListModel);
        contactPane = contactBuilder.buildMainPane(new JScrollPane(contactsJList), "contacts");
        contactPanel.add(contactPane);
        JButton contactBackButton = contactBuilder.makeBackButton();
        contactPanel.add(contactBackButton);
        panelHelper.mainBackListener(panelStack, contactBackButton);
        addButton = contactBuilder.buildButton("add", 310, 80);
        contactPanel.add(addButton);

        contactOptions = contactBuilder.buildOptions(new String[]{"send msg", "remove", "view msgs"}, 300);

        for (JButton button: contactOptions){
            contactPanel.add(button);
        }
        panelHelper.disableButtons(contactOptions);
        contactListListener(contactOptions);
        addButtonListener();
        return contactPanel;
    }


    private JPanel addContactPanel(){
        loadContacts(addContactListModel);
        addContactJList = addContactBuilder.buildJList(addContactListModel);
        addContactPane = addContactBuilder.buildMainPane(new JScrollPane(addContactJList), "add");

        addContactPanel.add(addContactPane);

        JButton addContactBackButton = addContactBuilder.makeBackButton();
        addContactPanel.add(addContactBackButton);
        panelHelper.mainBackListener(panelStack, addContactBackButton);

        internalAddButton = contactBuilder.buildButton("add", 310, 80);
        addContactPanel.add(internalAddButton);

        addUserTextField = addContactBuilder.buildTextField(310, 130);
        addContactPanel.add(addUserTextField);

        internalAddListener();
        return addContactPanel;
    }

    private void addButtonListener(){
        addButton.addActionListener(e -> panelStack.loadPanel(addContactPanel()));
    }

    private void internalAddListener(){
        internalAddButton.addActionListener(e -> {
            if (!messenger.addUser(addUserTextField.getText())) {
                JOptionPane.showMessageDialog(panelStack.getMainFrame(), "User does not exist or is already in your contacts!");
            }
            else {
                JOptionPane.showMessageDialog(panelStack.getMainFrame(), "User successfully added!");
                addContactListModel.addElement(addUserTextField.getText());
                contactListModel.addElement(addUserTextField.getText());
            }
            addUserTextField.setText("");
        });

    }


    private JPanel buildsSendMsgPanel(){
        Component[] elements = sendMsgBuilder.prepareEditablePane("send msg");
        sendMsgButton = sendMsgBuilder.buildButton("send", 310, 300);
        currMessageText = (JTextArea) elements[0];
        currMessagePane = (JScrollPane) elements[1];
        currMessagePane.setVisible(true);
        sendMsgPanel.add(currMessagePane);
        JButton sendMgsBackButton = sendMsgBuilder.makeBackButton();
        sendMsgPanel.add(sendMgsBackButton);
        panelHelper.mainBackListener(panelStack, sendMgsBackButton);
        sendMsgButton.addActionListener(e -> {
            if (!currMessageText.getText().equals(""))
                messenger.messageUser(currSelectedContact,
                currMessageText.getText());
            currMessageText.setText("");
        });
        sendMsgPanel.add(sendMsgButton);
        return sendMsgPanel;
    }

    private JPanel buildViewAllPanel(){
        JButton viewAllBackButton = viewAllBuilder.makeBackButton();
        viewAllPanel.add(viewAllBackButton);
        panelHelper.mainBackListener(panelStack, viewAllBackButton);
        DefaultListModel<String> list = new DefaultListModel<>();
        for (String s: messenger.viewMessages(currSelectedContact)){
            list.addElement(s);
        }
        JList messages = viewAllBuilder.buildJList(list);
        currViewMessagePane = viewAllBuilder.buildMainPane(new JScrollPane(messages), "view all");
        viewAllPanel.add(currViewMessagePane);
        return viewAllPanel;
    }

}