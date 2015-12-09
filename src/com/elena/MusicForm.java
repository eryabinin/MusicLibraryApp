package com.elena;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicForm {
    private JPanel root;
    private JPanel welcomePanel;
    private JPanel staffCatalog;
    private JButton quitButton;
    private JButton addNewRecordButton;
    private JButton editButton;
    private JTextField itemBarcodeTextField;
    private JButton staffCatalogButton;
    private JButton checkoutMaterialsButton;
    private JButton quitButton1;
    private JButton playMusicButton;
    private JTextField textField1;
    private JTextField textField2;

    public MusicForm() {
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MusicDatabase.shutdown();
                System.exit(0);   //Should probably be a call back to Main class so all the System.exit(0) calls are in one place.
            }
        });


        quitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MusicDatabase.shutdown();
                System.exit(0);   //Should probably be a call back to Main class so all the System.exit(0) calls are in one place.
            }
        });
        addNewRecordButton.addActionListener(new ActionListener() {//TODO open a new JPane similar to movie frm
        });


        //added music button to play background music
        playMusicButton.addActionListener(new ActionListener() {
            @Override
            public final void actionPerformed(ActionEvent e) {
                sound();//supposed to use method sound from Sound class
            }
        });
        staffCatalogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO need to open a new JFRAME with catalog editing
                //TODO want to add password to open catalog editing
            }
        });
        checkoutMaterialsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}