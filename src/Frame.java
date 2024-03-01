import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Frame extends JFrame {
    Main model;
    JTable partTable;
    JPanel marginPanelTable, marginPanelSearch, tablePanel, leftPanel, rightPanel, middlePanel;
    JPanel filterPanel;
    JComboBox sortBy, order, brandFilter, modelFilter, newFilter, authenticityFilter;;
    JTextField fromYear, toYear, fromPrice, toPrice;
    JCheckBox fromYearCheck, toYearCheck, fromPriceCheck, toPriceCheck;

    JTextField searchField;

    JPanel logoAndTimePanel, accountPanel;

    JLabel clock, setting;
    JButton account;
    ImageIcon accountIcon;
    JPanel stepsPanel, cartsList, rightDisplay, displayScreen;
    JPanel cartPanel, checkoutPanel, paymentPanel, receiptPanel;
    StepButton cart, checkout, payment, receipt;
    ArrayList<CartButton> cartButtons = new ArrayList<>();

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

        partTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    JTable target = (JTable) me.getSource();
                    int row = target.getSelectedRow();
                    JPanel panel = new JPanel();
                    JPanel panelBot = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.setPreferredSize(new Dimension(250,100));
                    JLabel label = new JLabel("Buy " + partTable.getValueAt(row,1) + " " + partTable.getValueAt(row,2) + "?");
                    label.setHorizontalAlignment(JLabel.CENTER);
                    JButton buyButton = new JButton("Add to Cart");
                    JButton cancelButton = new JButton("Cancel");
                    panel.add(label, BorderLayout.CENTER);
                    panelBot.add(buyButton, BorderLayout.WEST);
                    panelBot.add(cancelButton, BorderLayout.EAST);
                    panel.add(panelBot, BorderLayout.SOUTH);

                    JDialog dialog = new JDialog();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.getContentPane().add(panel);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);

                    buyButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(dialog, "Added to Cart!");
                            dialog.dispose();
                        }
                    });

                    cancelButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            dialog.dispose();
                        }
                    });

                }
            }
        });

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
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.GRAY);
        filterPanel.setPreferredSize(new Dimension(0,0));
        sortBy = new JComboBox<>(new String[]{"Car Brand", "Car Model", "Name", "Year", "Quantity", "Price", "isNew", "Authenticity"});
        order = new JComboBox<>(new String[]{"Asc", "Desc"});
        filterPanel.add(new JLabel("Sort By:"));
        filterPanel.add(sortBy);
        filterPanel.add(order);
        // brandFilter, modelFilter, fromYear, toYear, fromPrice, toPrice, newFilter, authenticityFilter;

        brandFilter = new JComboBox(new String[]{"---"});
        for (String brand: model.carBrands)
            brandFilter.addItem(brand);
        modelFilter = new JComboBox(new String[]{"---"});
        Set<String> models = new HashSet<>();
        for (Part part: model.parts)
            models.add(part.carModel);
        for (String str: models)
            modelFilter.addItem(str);

        fromYear = new JTextField(10);
        fromYear.setEnabled(false);
        fromYear.setBackground(Color.decode("#C6C6C6"));
        toYear = new JTextField(10);
        toYear.setEnabled(false);
        toYear.setBackground(Color.decode("#C6C6C6"));
        fromPrice = new JTextField(10);
        fromPrice.setEnabled(false);
        fromPrice.setBackground(Color.decode("#C6C6C6"));
        toPrice = new JTextField(10);
        toPrice.setEnabled(false);
        toPrice.setBackground(Color.decode("#C6C6C6"));

        fromYearCheck = new JCheckBox();
        toYearCheck = new JCheckBox();
        fromPriceCheck = new JCheckBox();
        toPriceCheck = new JCheckBox();

        newFilter = new JComboBox<>(new String[]{"---", "NEW", "OLD"});
        authenticityFilter = new JComboBox<>(new String[]{"---", "ORIGINAL", "CLASS A", "OTHERS"});

        filterPanel.add(new JLabel("Filter"));
        filterPanel.add(new JLabel("Brand:"));

        filterPanel.add(new JLabel("Car Brand"));
        filterPanel.add(brandFilter);
        filterPanel.add(new JLabel("Car Model"));
        filterPanel.add(modelFilter);
        filterPanel.add(new JLabel("Year Range"));
        filterPanel.add(fromYearCheck);
        filterPanel.add(fromYear);
        filterPanel.add(new JLabel("to"));
        filterPanel.add(toYearCheck);
        filterPanel.add(toYear);
        filterPanel.add(new JLabel("Price Range"));
        filterPanel.add(fromPriceCheck);
        filterPanel.add(fromPrice);
        filterPanel.add(new JLabel("to"));
        filterPanel.add(toPriceCheck);
        filterPanel.add(toPrice);
        filterPanel.add(new JLabel("Condition"));
        filterPanel.add(newFilter);
        filterPanel.add(new JLabel("Authenticity"));
        filterPanel.add(authenticityFilter);

        accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBackground(Color.DARK_GRAY);
        accountPanel.setPreferredSize(new Dimension(0, 60));
        accountPanel.add(new JLabel());

        accountIcon = new ImageIcon("resources/regular.png");
        account = new JButton(accountIcon);
        account.setBorderPainted(false);
        account.setMargin(new Insets(0, 0, 0, 0));
        account.setContentAreaFilled(false);
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

        for(int i = 1; i < 6; i++)
            cartButtons.add(new CartButton(i));
        for(int i = 0; i < cartButtons.size(); i++)
            cartsList.add(cartButtons.get(i));
        cartButtons.get(model.currCart - 1).setEnabled(false);

        cartsList.setBorder(new EmptyBorder(0,0,0,0));

        rightDisplay = new JPanel(new BorderLayout());

        cartPanel = new JPanel();
        cartPanel.add(new JLabel("This is the CART. ヾ(≧▽≦*)o"));

        checkoutPanel = new JPanel();
        checkoutPanel.add(new JLabel("This is the CHECKOUT. ヾ(≧▽≦*)o"));

        paymentPanel = new JPanel();
        paymentPanel.add(new JLabel("This is the PAYMENT. ヾ(≧▽≦*)o"));

        receiptPanel = new JPanel();
        receiptPanel.add(new JLabel("This is the RECEIPT. ヾ(≧▽≦*)o"));

        displayScreen = cartPanel;
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
    int num;
    public CartButton(int num)
    {
        this.setText("<html><body style=\"margin:15px 0px;color:white\">" + num + "</body></html>");
        this.setBackground(Color.decode("#203864"));
        this.setPreferredSize(new Dimension(60, 100));
        this.setFocusPainted(false);
        this.num = num;
    }

    @Override
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        if (b)
        {
            this.setBackground(Color.decode("#203864"));
        }
        else
        {
            this.setBackground(Color.decode("#7AA4CC"));
        }
    }
}