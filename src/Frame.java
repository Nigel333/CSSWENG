import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Frame extends JFrame {
    JTable partTable;
    JPanel leftPanel, rightPanel, middlePanel;
    JPanel filterPanel;
    JTextField searchField;

    JLabel clock, account, setting;
    ImageIcon accountIcon;

    public Frame(Main model) {
        super("Item Database");

        PartTableModel tableModel = new PartTableModel(model.filteredParts);
        partTable = new JTable(tableModel);

        // Set the layout manager for the frame
        setLayout(new BorderLayout());

        middlePanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        middlePanel.add(searchField, BorderLayout.NORTH);
        middlePanel.add(new JScrollPane(partTable), BorderLayout.CENTER);
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
                clock.setText("<html><h2 style=\"color:\"white\"\"; padding-right: 50px>" + new Date().toLocaleString().split(", ")[0] + ", " + new Date().toLocaleString().split(", ")[1] + "<br>" + new Date().toLocaleString().split(", ")[2] + "</h2>");
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
        rightPanel.add(new JLabel("Shopping Cart Side"),BorderLayout.CENTER);
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
