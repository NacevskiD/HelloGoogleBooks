package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by admin on 5/3/17.
 */
public class BookGUI extends JFrame{
    private JTextField searchText;
    private JTextArea bookDescriptionText;
    private JPanel mainPanel;
    private JButton searchButton;
    private JTextField ratingText;
    private JTextField authorText;
    private JTextField titleTextField;

    BookGUI() {
        setContentPane(mainPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchText.getText(); //todo validation
                System.out.println(query);
                GetBook.searchBook(query, BookGUI.this);

            }
        });
        pack();
        setVisible(true);
    }

    public void titleFetched(String title) {
        System.out.println("title fetched");
        bookDescriptionText.setText("The title of this book is "+ title);
    }

    public void firstResultIs(Book book) {
        bookDescriptionText.setText(book.description);
        ratingText.setText(book.googleRating + " out of 5");
        authorText.setText(book.author);
        titleTextField.setText(book.title);

    }
}
