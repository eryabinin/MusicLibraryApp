package com.elena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;

// this class is to work with the GUI form used for Catalog records update.
// this code is based on the MovieRatings example provided by Clara. Modified by Elena R.
public class MusicLibraryForm extends JFrame implements WindowListener{
    private JTable musicDataTable;
    private JPanel rootPanel;
    private JTextField barcodeTextField;
    private JButton addNewMusicItemButton;
    private JButton quitButton;
    private JButton deleteMusicItemButton;
    private JButton BGMButton;
    private JComboBox typeComboBox;
    private JTextField authorTextField;
    private JTextField priceTextField;
    private JComboBox statusComboBox;
    private JTextField titleTextField;
    private JButton clearButton;

    MusicLibraryForm(final MusicCatalogDataModel musicDataModel){

        setContentPane(rootPanel);
        pack();
        setTitle("Music Database Application");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //Set up JTable for catalog records
        musicDataTable.setGridColor(Color.BLUE);
        musicDataTable.setModel(musicDataModel);

        //Event handlers for add, delete and quit buttons
        addNewMusicItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Get music barcode, make sure it's not blank
                String barcodeData = barcodeTextField.getText();
                int l = barcodeData.length();

                // validate: check if barcode entered with digits only
                boolean isInteger = false;
                if (containsOnlyNumbers(barcodeData)) {
                    isInteger = true;
                }

                if (barcodeData == null ||
                        barcodeData.trim().equals("") ||
                            !(l==10) ||
                                (isInteger == false))
                {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a valid barcode for a music item (10-digit number)");
                    return;
                }

                int bcd = Integer.parseInt(barcodeData); // ready to be added with a new record

                //Get type, make sure it's selected
                String typeData = typeComboBox.getSelectedItem().toString();

                if (typeData == null ||
                        typeData.trim().equals("") ||
                            (typeComboBox.getSelectedIndex() == 0)) {
                    JOptionPane.showMessageDialog(rootPane, "Please select a type for the music item");
                    return;
                }

                //Get music title, make sure it's not blank
                String titleData = titleTextField.getText();

                if (titleData == null || titleData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a title for the new music item");
                    return;
                }

                //Get author, make sure it's not blank
                String authorData = authorTextField.getText();

                if (authorData == null || authorData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter an author for the new music item");
                    return;
                }


                //Get status, make sure it's selected
                String statusData = statusComboBox.getSelectedItem().toString();

                if (statusData == null ||
                        statusData.trim().equals("") ||
                            (statusComboBox.getSelectedIndex() == 0)) {
                    JOptionPane.showMessageDialog(rootPane, "Please select a status of the music item");
                    return;
                }

                // 0 code for UserID means items is not assigned to any user yet
                String userId = Integer.toString(0);  //TODO implement another table Users allowing check out materials to users


                //Get price
                String priceData;
                double itemPrice;

                priceData = priceTextField.getText();

                try {
                    itemPrice = Double.parseDouble(priceData);
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(rootPane,
                            "Price needs to be a number.");
                    return;
                }

                double pr = Double.parseDouble(priceData); // ready to be added with a new record
                System.out.println("Adding a music item " + titleData + " ");

                // add data to a new record
                boolean insertedRow = musicDataModel.insertRow(bcd,
                        typeData,
                        titleData,
                        authorData,
                        statusData,
                        pr,
                        0);

                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new item");
                }
            }

        });

        // quit the app
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MusicDatabase.shutdown();
                System.exit(0);   //Should probably be a call back to Main class so all the System.exit(0) calls are in one place.
            }
        });


        // delete selected record
        deleteMusicItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = musicDataTable.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a record to delete");
                }
                boolean deleted = musicDataModel.deleteRow(currentRow);
                if (deleted) {
                    MusicDatabase.loadAllCatalogItems();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting record");
                }
            }
        });

        // background music -- played in loops ---- not working yet (not sure why)
        BGMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound sound = new Sound();
            }
        });

        // clear fields; ready to start a new record
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                barcodeTextField.setText("");
                typeComboBox.setSelectedIndex(0);
                titleTextField.setText("");
                authorTextField.setText("");
                priceTextField.setText("");
                statusComboBox.setSelectedIndex(0);
            }
        });
    }

    //windowListener methods. Only need one of them, but are required to implement the others anyway
    //WindowClosing will call DB shutdown code, which is important, so the DB is in a consistent state however the application is closed.

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        MusicDatabase.shutdown();}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    // code is based on the method at http://www.java2s.com/Code/Java/Data-Type/ValidateifaStringcontainsonlynumbers.htm
    public static boolean containsOnlyNumbers(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }
}
