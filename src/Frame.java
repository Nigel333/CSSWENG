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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Frame extends JFrame {
    Main model;
    JTable partTable;
    JPanel marginPanelTable, marginPanelSearch, tablePanel, leftPanel, rightPanel, middlePanel;
    JPanel filterPanel,
            firstFilter, secondFilter, thirdFilter, fourthFilter, fifthFilter,
            sixthFilter, seventhFilter, eighthFilter, ninthFilter, tenthFilter;
    JPanel sortingLabel, filterLabel;
    JComboBox sortBy, order, brandFilter, modelFilter, newFilter, authenticityFilter;;
    JTextField fromYear, toYear, fromPrice, toPrice;
    JCheckBox fromYearCheck, toYearCheck, fromPriceCheck, toPriceCheck;
    JTextField searchField;
    JPanel logoAndTimePanel, accountPanel;
    JLabel clock, setting;
    JButton account;
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
                jc.setBorder(new MatteBorder(0, 0, 10, 0, Color.decode("#92D050")));
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
            partTable.setRowHeight(i, 60);

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
        marginPanelTable.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        clock.setFont(new Font("Verdana",  Font.BOLD, 15));
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clock.setText("<html><h2 style=\"color: white; padding:5px; \">" + new Date().toLocaleString().split(", ")[0]
                            + "<br>" + new Date().toLocaleString().split(", ")[1] + "</h2>");
            }
        });
        t.start();
        clock.setBorder(new EmptyBorder(0, 0, 0, 5));

        ImageIcon logoIconOriginal = new ImageIcon("resources/KSClogo.png");
        Image image = logoIconOriginal.getImage();
        Image newImg = image.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);

        ImageIcon logoIcon = new ImageIcon(newImg);
        JLabel logo = new JLabel(logoIcon);
        logo.setBorder(new EmptyBorder(0, 10, 0, 0));
        logoAndTimePanel.add(logo, BorderLayout.WEST);
        logoAndTimePanel.add(clock, BorderLayout.EAST);

        // Filters and Stuff
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(Color.GRAY);
        filterPanel.setPreferredSize(new Dimension(0,0));

        // sortBy, order
        sortingLabel = createLabel("Sorting");
        filterPanel.add(sortingLabel);

        // Sort By filter
        firstFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        firstFilter.add(new JLabel("Sort By:"));
        sortBy = new JComboBox<>(new String[]{"Car Brand", "Car Model", "Name", "Year", "Quantity", "Price", "isNew", "Authenticity"});
        sortBy.setPreferredSize(new Dimension(120, 30));
        sortBy.setBackground(Color.WHITE);
        firstFilter.add(sortBy);
        filterPanel.add(firstFilter);

        // Order Filter
        secondFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        secondFilter.add(new JLabel("Order:"));
        order = new JComboBox<>(new String[]{"Ascending", "Descending"});
        order.setPreferredSize(new Dimension(120, 30));
        order.setBackground(Color.WHITE);
        secondFilter.add(order);
        filterPanel.add(secondFilter);

        // brandFilter, modelFilter, fromYear, toYear, fromPrice, toPrice, newFilter, authenticityFilter;
        filterLabel = createLabel("Filtering");
        filterPanel.add(filterLabel);

        // Brand Filter
        thirdFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        thirdFilter.add(new JLabel("Car Brand:"));
        brandFilter = new JComboBox(new String[]{"All"});
        for (String carBrand: model.carBrands) {
            brandFilter.addItem(carBrand);
        }
        brandFilter.setPreferredSize(new Dimension(120, 30));
        brandFilter.setBackground(Color.WHITE);
        thirdFilter.add(brandFilter);
        filterPanel.add(thirdFilter);

        // Model Filter
        fourthFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fourthFilter.add(new JLabel("Car Model:"));
        modelFilter = new JComboBox(new String[]{"All"});
        Set<String> models = new HashSet<>();
        for (Part part: model.parts)
            models.add(part.carModel);
        for (String str: models)
            modelFilter.addItem(str);
        modelFilter.setPreferredSize(new Dimension(120, 30));
        modelFilter.setBackground(Color.WHITE);
        fourthFilter.add(modelFilter);
        filterPanel.add(fourthFilter);

        // newFilter
        ninthFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ninthFilter.add(new JLabel("Condition"));
        newFilter = new JComboBox<>(new String[]{"All", "NEW", "OLD"});
        newFilter.setPreferredSize(new Dimension(120, 30));
        newFilter.setBackground(Color.WHITE);
        ninthFilter.add(newFilter);
        filterPanel.add(ninthFilter);

        // authenticityFilter
        tenthFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tenthFilter.add(new JLabel("Authenticity"));
        authenticityFilter = new JComboBox<>(new String[]{"All", "ORIGINAL", "CLASS A", "OTHERS"});
        authenticityFilter.setPreferredSize(new Dimension(120, 30));
        authenticityFilter.setBackground(Color.WHITE);
        tenthFilter.add(authenticityFilter);
        filterPanel.add(tenthFilter);

        // Year Range
        filterLabel = createLabel("Year Range");
        filterPanel.add(filterLabel);

        // From Year
        fifthFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fifthFilter.add(new JLabel("From:"));
        fromYear = new JTextField();
        fromYear.setPreferredSize(new Dimension(120, 30));
        fifthFilter.add(fromYear);
        filterPanel.add(fifthFilter);

        // To Year
        sixthFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sixthFilter.add(new JLabel("To:"));
        toYear = new JTextField();
        toYear.setPreferredSize(new Dimension(120, 30));
        sixthFilter.add(toYear);
        filterPanel.add(sixthFilter);

        // Price Range
        filterLabel = createLabel("Price Range");
        filterPanel.add(filterLabel);

        // From Price
        seventhFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        seventhFilter.add(new JLabel("From:"));
        fromPrice = new JTextField();
        fromPrice.setPreferredSize(new Dimension(120, 30));
        seventhFilter.add(fromPrice);
        filterPanel.add(seventhFilter);

        // To Price
        eighthFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eighthFilter.add(new JLabel("To:"));
        toPrice = new JTextField();
        toPrice.setPreferredSize(new Dimension(120, 30));
        eighthFilter.add(toPrice);
        filterPanel.add(eighthFilter);

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

    public JPanel createLabel(String labelText) {
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.setBackground(Color.GRAY);
        JLabel createLabel = new JLabel(labelText);
        createLabel.setForeground(Color.WHITE);
        createLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        createLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        labelPanel.add(createLabel);

        return labelPanel;
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