import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Frame extends JFrame {
    Main model;
    JTable partTable;
    JPanel marginPanelTable, marginPanelSearch, tablePanel, leftPanel, rightPanel, middlePanel;
    JPanel filterPanel;
    JComboBox sortBy, order;

    JTextField searchField;

    JPanel logoAndTimePanel, accountPanel;

    JLabel clock, account, setting;
    ImageIcon accountIcon;

    JPanel stepsPanel, cartsList, rightDisplay, displayScreen;
    StepButton cart, checkout, payment, receipt;

    public Frame(Main model) {
        super("Item Database");
        this.model = model;

        // initializing JTable with custom column size

        partTable = new JTable(model.tableModel)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)c;
                jc.setBorder(new MatteBorder(0, 0, 40, 0, Color.decode("#92D050")));
                jc.setBackground(Color.decode("#385723"));
                jc.setForeground(Color.WHITE);
                jc.setFont(new Font("Verdana", Font.BOLD, 11));

                return c;
            }
        };
        partTable.setPreferredScrollableViewportSize(new Dimension(300, 750));
        partTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        partTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        partTable.getColumnModel().getColumn(3).setPreferredWidth(8);
        partTable.getColumnModel().getColumn(4).setPreferredWidth(5);
        partTable.getColumnModel().getColumn(5).setPreferredWidth(10);
        partTable.getColumnModel().getColumn(6).setPreferredWidth(3);
        partTable.getColumnModel().getColumn(7).setPreferredWidth(10);

        partTable.setShowGrid(false);
        partTable.setBackground(Color.decode("#92D050"));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);


        for (int i = 0; i < partTable.getRowCount(); i ++)
            partTable.setRowHeight(i, 80);

        for (int i = 0; i < partTable.getColumnCount(); i ++)
            if(i != 6)
                partTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);

        // Set the layout manager for the frame
        setLayout(new BorderLayout());

        marginPanelTable = new JPanel(new BorderLayout());
        marginPanelTable.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        marginPanelTable.setBackground(Color.decode("#92D050"));

        marginPanelSearch = new JPanel(new BorderLayout());
        marginPanelSearch.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));
        marginPanelSearch.setBackground(Color.decode("#92D050"));

        middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(Color.decode("#92D050"));
        searchField = new JTextField();
        searchField.setFont(new Font("Verdana", Font.BOLD, 18));
        middlePanel.add(searchField, BorderLayout.CENTER);
        marginPanelSearch.add(searchField, BorderLayout.CENTER);
        marginPanelTable.add(marginPanelSearch, BorderLayout.NORTH);

        tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(partTable);
        scrollPane.setBackground(Color.decode("#92D050"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tablePanel.setBackground(Color.decode("#92D050"));
        tablePanel.add(scrollPane, BorderLayout.NORTH);
        marginPanelTable.add(tablePanel, BorderLayout.CENTER);
        middlePanel.add(marginPanelTable, BorderLayout.CENTER);

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
                clock.setText("<html><h2 style=\"color: white; padding:5px; border-style: solid; border-color: white\">" + new Date().toLocaleString().split(", ")[0]
                            + "<br>" + new Date().toLocaleString().split(", ")[1] + "</h2>");
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
        sortBy = new JComboBox<>(new String[]{"Car Brand", "Car Model", "Name", "Year", "Quantity", "Price", "isNew", "Authenticity"});
        order = new JComboBox<>(new String[]{"Asc", "Desc"});
        filterPanel.add(new JLabel("Sort By:"));
        filterPanel.add(sortBy);
        filterPanel.add(order);

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
        rightPanel.setPreferredSize(new Dimension(345,0));
        stepsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cart = new StepButton("Cart");
        cart.setPreferredSize(new Dimension(77,50));
        cart.setFont(new Font("Verdana", Font.BOLD, 11));
        //cart.setEnabled(true);
        checkout = new StepButton("Checkout");
        checkout.setPreferredSize(new Dimension(93,50));
        checkout.setFont(new Font("Verdana", Font.BOLD, 11));

        payment = new StepButton("Payment");
        payment.setPreferredSize(new Dimension(93,50));
        payment.setFont(new Font("Verdana", Font.BOLD, 11));
        receipt = new StepButton("Receipt");
        receipt.setPreferredSize(new Dimension(82,50));
        receipt.setFont(new Font("Verdana", Font.BOLD, 11));

        stepsPanel.add(cart);
        stepsPanel.add(checkout);
        stepsPanel.add(payment);
        stepsPanel.add(receipt);
        stepsPanel.setBackground(Color.GREEN);

        cartsList = new JPanel();
        cartsList.setBackground(Color.decode("#4472C4"));
        cartsList.setLayout(new BoxLayout(cartsList, BoxLayout.Y_AXIS));
        CartButton firstCart = new CartButton("1");
        firstCart.setEnabled(true);
        firstCart.setForeground(Color.WHITE);
        cartsList.add(firstCart);
        cartsList.add(new CartButton("2"));
        cartsList.add(new CartButton("3"));
        cartsList.add(new CartButton("4"));
        cartsList.add(new CartButton("5"));
        cartsList.setBorder(new EmptyBorder(0,0,0,0));

        rightDisplay = new JPanel(new BorderLayout());
        displayScreen = new JPanel();
        JLabel tempLabel = new JLabel("This is the cart panel");
        displayScreen.add(tempLabel);
        displayScreen.setBorder(new EmptyBorder(40, 40, 40, 40));
        displayScreen.setBackground(Color.CYAN);
        rightDisplay.add(displayScreen, BorderLayout.CENTER);

        rightPanel.add(stepsPanel,BorderLayout.NORTH);
        rightPanel.add(cartsList, BorderLayout.WEST);
        rightPanel.add(rightDisplay, BorderLayout.CENTER);

        cart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rightDisplay.remove(displayScreen);
                JPanel displayScreen = new JPanel();
                displayScreen.setBackground(Color.CYAN); 
                JLabel label = new JLabel("This is the cart panel");
                displayScreen.add(label);
                rightDisplay.add(displayScreen, BorderLayout.CENTER);

            }
        });
        checkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rightDisplay.remove(displayScreen);
                JPanel displayScreen = new JPanel();
                displayScreen.setBackground(Color.CYAN);
                JLabel label = new JLabel("This is the checkout panel");
                displayScreen.add(label);
                rightDisplay.add(displayScreen, BorderLayout.CENTER);

            }
        });
        payment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rightDisplay.remove(displayScreen);
                JPanel displayScreen = new JPanel();
                displayScreen.setBackground(Color.CYAN);
                JLabel label = new JLabel("This is the payment panel");
                displayScreen.add(label);
                rightDisplay.add(displayScreen, BorderLayout.CENTER);

            }
        });
        receipt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rightDisplay.remove(displayScreen);
                JPanel displayScreen = new JPanel();
                displayScreen.setBackground(Color.CYAN);
                JLabel label = new JLabel("This is the receipt panel my guy");
                displayScreen.add(label);
                rightDisplay.add(displayScreen, BorderLayout.CENTER);

            }
        });
    }
}
class StepButton extends JButton {
    public StepButton(String text)
    {
        this.setText(text);
        this.setBackground(Color.decode("#0080FF"));
        this.setForeground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 50));
        this.setEnabled(true);
        this.setFocusPainted(false);
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
    private static JButton currentButton = null;
    public CartButton(String text)
    {

        this.setText("<html><body style = margin:15px 0px 15px 150px>"+ text +"</body></html>");
        this.setBackground(Color.decode("#1B5489"));
        this.setPreferredSize(new Dimension(77, 100));
        this.setFocusPainted(false);

        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentButton != null) {
                    currentButton.setForeground(null);
                }
                currentButton = CartButton.this;
                    CartButton.this.setForeground(Color.WHITE);
            }
        });
    }
}