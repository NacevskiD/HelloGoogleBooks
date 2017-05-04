package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 5/3/17.
 */
public class BookGUI extends JFrame{
    private JTextField isbnTextHere;
    private JTextArea bookTitleText;
    private JPanel mainPanel;
    private JButton searchButton;

    BookGUI() {
        setContentPane(mainPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = isbnTextHere.getText(); //todo validation
                System.out.println(isbn);
                GetBook.getTitleByISBN(isbn, BookGUI.this);

            }
        });
        pack();
        setVisible(true);
    }

    public void titleFetched(String title) {
        System.out.println("title fetched");
        bookTitleText.setText("The title of this book is "+ title);
    }
}
