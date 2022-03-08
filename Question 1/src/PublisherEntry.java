import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PublisherEntry extends JFrame{

    private Connection connection;
    private PreparedStatement statement;

    public PublisherEntry(String driver, String url, String user, String password) {
        super("Add a New Publisher");
        super.setLocation(960, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        try {
            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, password);

            JLabel publisherNameLabel = new JLabel("Publisher Name: ");

            JTextField publisherNameField = new JTextField(20);

            JButton submitButton = new JButton("Submit");

            Box publisherNameEntry = Box.createHorizontalBox();
            publisherNameEntry.add(publisherNameLabel);
            publisherNameEntry.add(publisherNameField);

            Container frameContent = getContentPane();
            frameContent.add(publisherNameEntry, BorderLayout.NORTH);
            frameContent.add(submitButton);

            setSize(360, 200);
            setVisible(true);

            submitButton.addActionListener(
                    e -> {
                        try {
                            String publisher = publisherNameField.getText();

                            statement =
                                    connection.prepareStatement(
                                            "INSERT INTO publishers values (NULL,'" + publisher + "');"
                                    );
                            statement.executeUpdate();
                        } catch (SQLException sqlException) {
                            JOptionPane.showMessageDialog(null, sqlException.toString(),
                                    "Database error", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            dispose();
                        }

                    }
            );
        }

        catch (ClassNotFoundException classNotFound) {
            JOptionPane.showMessageDialog(null,
                    "Cloudscape driver not found", "Driver not found",
                    JOptionPane.ERROR_MESSAGE);
        }

        catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null,
                    sqlException.toString(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


