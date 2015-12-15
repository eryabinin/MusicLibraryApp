package com.elena;

import javax.swing.*;

public class LibraryGUI extends JFrame{
//example was presented in class by Margaret. I would like to use it in the future for updated version.
    private JPanel rootPanel;

    //Not created in GUI designer
    private JTabbedPane tabbedPane;

    //Note that this fails with a NullPointer if the default layoutmanager (GridLayoutManager) for this form is used
    //Since all it does is hold the JTabbedPane, set the layout manager to something (probably anything) else.

    public LibraryGUI() {
        setContentPane(rootPanel);

        //Create a a JTabbedPanel, add to JPanel, add tabs to JTabbedPane.
        tabbedPane = new JTabbedPane();
        rootPanel.add(tabbedPane);
        //tabbedPane.add("Catalog", new MusicLibraryForm().getPanel());
        //tabbedPane.add("Users", new UsersGUI().getPanel());
        //tabbedPane.add("Check out Materials", new CKOGUI().getPanel());

        setVisible(true);
        pack();

    }
}