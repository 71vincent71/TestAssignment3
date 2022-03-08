import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TitleEntry extends JFrame
{

    private Connection connection;
    private PreparedStatement statement;

    public TitleEntry(String driver, String url, String user, String password)
    {
        super("Add a New Title");
        super.setLocation(960, 540);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        try
        {
            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, password);

            JLabel titleNameLabel = new JLabel("Title Name: ");

            JTextField titleNameField = new JTextField(20);

            JButton submitButton = new JButton("Submit");

            Box titleNameEntry = Box.createHorizontalBox();
            titleNameEntry.add(titleNameLabel);
            titleNameEntry.add(titleNameField);

            Container frameContent = getContentPane();
            frameContent.add(titleNameEntry, BorderLayout.NORTH);
            frameContent.add(submitButton);

            setSize(360, 200);
            setVisible(true);

            submitButton.addActionListener(e ->
                    {
                        try
                        {
                            String titles = titleNameField.getText();

                            statement =
                                    connection.prepareStatement(
                                            "INSERT INTO titles values (NULL,'" + titles + "');"
                                    );
                            statement.executeUpdate();
                        }
                        catch (SQLException sqlException)
                        {
                            JOptionPane.showMessageDialog(null, sqlException.toString(),
                                    "Database error", JOptionPane.ERROR_MESSAGE);
                        }
                        finally
                        {
                            dispose();
                        }

                    }
            );
        }
        catch (ClassNotFoundException classNotFound)
        {
            JOptionPane.showMessageDialog(null,
                    "Cloudscape driver not found", "Driver not found",
                    JOptionPane.ERROR_MESSAGE);
        }

        catch (SQLException sqlException)
        {
            JOptionPane.showMessageDialog(null,
                    sqlException.toString(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



