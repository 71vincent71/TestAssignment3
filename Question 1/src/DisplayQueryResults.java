
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DisplayQueryResults extends JFrame {
    private ResultSetTableModel tableModel;

    public DisplayQueryResults() {
        super("Displaying Query Results");

        String driver = "com.mysql.cj.jdbc.Driver";

        String url = "jdbc:mysql://localhost:3306/books";
        String user = "root";
        String password = "root";

        String queryAuthors = "SELECT * FROM authors";
        String queryPublishers = "SELECT * FROM publisher";
        String queryTitles = "SELECT * FROM authors";
        String createQuery = "";




        try
        {
            tableModel = new ResultSetTableModel(driver, url, user, password, queryAuthors);

            JTextArea queryArea = new JTextArea(queryAuthors, 3, 35);
            queryArea.setWrapStyleWord(true);
            queryArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(queryArea,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JButton submitButton = new JButton("Submit Query");
            JButton showAuthorsButton = new JButton("Show Authors");
            JButton showPublishersButton = new JButton("Show Publishers");
            JButton showTitlesButton = new JButton("Show Titles");
            JButton showISBNButton = new JButton("Show Author ISBNs");

            Box showBox = Box.createHorizontalBox();
            showBox.add(showAuthorsButton);
            showBox.add(showPublishersButton);
            showBox.add(showTitlesButton);
            showBox.add(showISBNButton);

            Box queryBox = Box.createHorizontalBox();
            queryBox.add(scrollPane);
            queryBox.add(submitButton);
            queryBox.setMinimumSize(queryBox.getPreferredSize());

            JTable resultTable = new JTable(tableModel)
            {
                @Override
                public Dimension getPreferredScrollableViewportSize()
                {
                    return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
                }
            };

            JButton addAuthorButton = new JButton("Add Author");
            JButton addPublisherButton = new JButton("Add Publisher");
            JButton addTitleButton = new JButton("Add Title");

            Box createBox = Box.createHorizontalBox();
            createBox.add(addAuthorButton);
            createBox.add(addPublisherButton);
            createBox.add(addTitleButton);

            Container c = getContentPane();
            c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
            c.add(showBox, BorderLayout.BEFORE_FIRST_LINE);
            c.add(queryBox, BorderLayout.NORTH);
            c.add(new JScrollPane(resultTable), BorderLayout.CENTER);
            c.add(createBox, BorderLayout.SOUTH);

            submitButton.addActionListener( new assignQuery(queryArea.getText()) );

            showAuthorsButton.addActionListener( new assignQuery(queryAuthors) );

            showPublishersButton.addActionListener( new assignQuery("SELECT * FROM publishers") );

            showTitlesButton.addActionListener( new assignQuery("SELECT * FROM titles") );

            showISBNButton.addActionListener( new assignQuery("SELECT * FROM authorisbn") );

            addAuthorButton.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new AuthorEntry(driver, url, user, password);
                        }
                    }
            );

            addPublisherButton.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            new PublisherEntry(driver, url, user, password);
                        }
                    }
            );

            addTitleButton.addActionListener(
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            new TitleEntry(driver, url, user, password);
                        }
                    }
            );
            setMinimumSize(c.getPreferredSize());
        }

        catch (ClassNotFoundException classNotFound) {
            JOptionPane.showMessageDialog(null,
                    "Cloudscape driver not found", "Driver not found",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null,
                    sqlException.toString(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    private class assignQuery implements ActionListener
    {
        private String query;

        private assignQuery(String query) {
            this.query = query;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try {
                tableModel.setQuery(query);
            }
            catch (SQLException sqlException) {
                JOptionPane.showMessageDialog(null, sqlException.toString(), "Database error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}



