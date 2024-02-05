import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

public class Frame extends JFrame {
    JTable partTable;
    JPanel marginPanel, leftPanel, rightPanel, middlePanel;
    JPanel filterPanel;

    JTextField searchField;

    JPanel logoAndTimePanel, accountPanel;

    JLabel clock, account, setting;
    ImageIcon accountIcon;

    JPanel stepsPanel, cartsList, rightDisplay, displayScreen;
    StepButton cart, checkout, payment, receipt;

    public Frame(Main model) {
        super("Item Database");

        // initializing JTable with custom column size
        PartTableModel tableModel = new PartTableModel(model.filteredParts);
        partTable = new JTable(tableModel);
        partTable.setPreferredScrollableViewportSize(new Dimension(200,400));
        partTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(1).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(2).setPreferredWidth(20);
        partTable.getColumnModel().getColumn(3).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(5).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(6).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(7).setPreferredWidth(10);
        // Set the layout manager for the frame
        setLayout(new BorderLayout());

        marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        marginPanel.setBackground(Color.GREEN);

        middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(Color.GREEN);
        middlePanel.setPreferredSize(new Dimension(300,300));
        searchField = new JTextField();
        middlePanel.add(searchField, BorderLayout.NORTH);
        marginPanel.add(new JScrollPane(partTable), BorderLayout.CENTER);
        middlePanel.add(marginPanel, BorderLayout.CENTER);
        add(middlePanel, BorderLayout.CENTER);
        initializeLeft();
        add(leftPanel, BorderLayout.WEST);
        initializeRight();
        add(rightPanel, BorderLayout.EAST);


        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    protected void initializeLeft()
    {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0));

        logoAndTimePanel = new JPanel(new BorderLayout());
        logoAndTimePanel.setBackground(Color.DARK_GRAY);
        logoAndTimePanel.setPreferredSize(new Dimension(0, 100));

        clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(new Font("Arial", 1, 15));
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clock.setText("<html><h2 style=\"color: white; padding:5px; border-style: solid; border-color: white\">" + new Date().toLocaleString().split(", ")[0] + ", " + new Date().toLocaleString().split(", ")[1] + "<br>" + new Date().toLocaleString().split(", ")[2] + "</h2>");
            }
        });
        t.start();
        clock.setBorder(new EmptyBorder(0, 0, 0, 10));

        ImageIcon logoIcon = new ImageIcon("resources/logo.png");
        JLabel logo = new JLabel(logoIcon);
        logo.setBorder(new EmptyBorder(0, 10, 0, 0));
        logoAndTimePanel.add(logo, BorderLayout.WEST);

        logoAndTimePanel.add(clock, BorderLayout.EAST);

        filterPanel = new JPanel();
        filterPanel.setBackground(Color.GRAY);
        filterPanel.setPreferredSize(new Dimension(0,0));

        accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBackground(Color.DARK_GRAY);
        accountPanel.setPreferredSize(new Dimension(0, 60));
        accountPanel.add(new JLabel());

        accountIcon = new ImageIcon("resources/regular.png");
        account = new JLabel(accountIcon);
        account.setBorder(new EmptyBorder(0, 10, 0, 0));
        accountPanel.add(account, BorderLayout.WEST);

        ImageIcon settingIcon = new ImageIcon("resources/settings.png");
        setting = new JLabel(settingIcon);
        setting.setBorder(new EmptyBorder(0, 0, 0, 10));
        accountPanel.add(setting, BorderLayout.EAST);

        leftPanel.add(logoAndTimePanel, BorderLayout.NORTH);
        leftPanel.add(accountPanel, BorderLayout.SOUTH);
        leftPanel.add(filterPanel, BorderLayout.CENTER);
    }
    protected void initializeRight()
    {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.decode("#4472C4"));
        stepsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cart = new StepButton("Cart");
        cart.setEnabled(true);
        checkout = new StepButton("Checkout");
        payment = new StepButton("Payment");
        receipt = new StepButton("Receipt");

        stepsPanel.add(cart);
        stepsPanel.add(checkout);
        stepsPanel.add(payment);
        stepsPanel.add(receipt);
        stepsPanel.setBackground(Color.GREEN);

        cartsList = new JPanel();
        cartsList.setBackground(Color.decode("#4472C4"));
        cartsList.setLayout(new BoxLayout(cartsList, BoxLayout.Y_AXIS));
        cartsList.add(new CartButton());
        cartsList.add(new CartButton());
        cartsList.setBorder(new EmptyBorder(50,0,0,0));

        rightDisplay = new JPanel(new BorderLayout());
        displayScreen = new JPanel();
        displayScreen.setBorder(new EmptyBorder(40, 40, 40, 40));
        displayScreen.setBackground(Color.CYAN);
        rightDisplay.add(displayScreen, BorderLayout.CENTER);

        rightPanel.add(stepsPanel,BorderLayout.NORTH);
        rightPanel.add(cartsList, BorderLayout.WEST);
        rightPanel.add(rightDisplay, BorderLayout.CENTER);
    }
}
class StepButton extends JButton {
    public StepButton(String text)
    {
        this.setText(text);
        this.setBackground(Color.decode("#0080FF"));
        this.setForeground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 50));
        this.setEnabled(false);
    }

    @Override
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        if (b)
        {
            this.setBackground(Color.decode("#0080FF"));
        }
        else
        {
            this.setBackground(Color.decode("#C5CDD6"));
        }
    }
}

class CartButton extends JButton {
    public CartButton()
    {
        this.setText("<html><body style = margin:20px 35px></body></html>");
        this.setBackground(Color.decode("#1B5489"));
        this.setPreferredSize(new Dimension(40, 70));
    }
}

class PartTableModel extends AbstractTableModel {
    private ArrayList<Part> parts;
    private String[] columnNames = {"Car Brand", "Car Model", "Name", "Year", "Quantity", "Price", "Is New", "Authenticity"};

    public PartTableModel(ArrayList<Part> parts) {
        this.parts = parts;
    }

    @Override
    public int getRowCount() {
        return parts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Part part = parts.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return part.carBrand;
            case 1:
                return part.carModel;
            case 2:
                return part.name;
            case 3:
                return part.year;
            case 4:
                return part.quantity;
            case 5:
                return part.price;
            case 6:
                return part.isNew;
            case 7:
                return part.authenticity;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Return the appropriate class for each column
        if (columnIndex == 6) {
            return Boolean.class; // "Is New" column is of boolean type
        } else {
            return super.getColumnClass(columnIndex);
        }
    }
}
