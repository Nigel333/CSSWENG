import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    JTextField searchField;
    JPanel logoAndTimePanel, accountPanel;
    JLabel clock, setting;
    JButton account;
    ImageIcon accountIcon;
    JPanel stepsPanel, cartsList, rightDisplay, displayScreen;
    JPanel cartPanel, checkoutPanel, paymentPanel, paymentBtnsPanel, receiptPanel, cancelBackPanel;
    ArrayList<JPanel> cartViews = new ArrayList<>();
    ArrayList<ArrayList<PartLabel>> listsOfParts = new ArrayList<>();
    JPanel checkoutView, paymentView, receiptView;
    JScrollPane cartViewScroll;
    StepButton cart, checkout, payment, receipt;
    JTextField finalPrice;

    CartButton proceedButton, proceed2PayButton, calculateButton, payButton, printButton, cancelOrderButton, backButtonChk, backButtonPay;
    ArrayList<CartButton> cartButtons = new ArrayList<>();
    int[] cartNum = new int[5];
    ArrayList<Double> partPrices = new ArrayList<>();
    Double sum = 0.0;
    int receiptCtr = 1;
    String date;
    public Frame(Main model) {
        super("Item Database");
        this.model = model;

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

        for (int i = 0; i < 6; i++)
            listsOfParts.add(new ArrayList<>());

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
                    JTextField textField = new JTextField("1", 5);
                    JPanel midPanel = new JPanel(new GridLayout(2,1));
                    JButton buyButton = new JButton("Add to Cart");
                    JButton cancelButton = new JButton("Cancel");
                    midPanel.add(label);
                    midPanel.add(textField);
                    panel.add(midPanel, BorderLayout.CENTER);
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
                            String text = textField.getText();
                            int quantity = Integer.parseInt(text);
                            Part part = new Part((String) partTable.getValueAt(row,0), (String) partTable.getValueAt(row,1), (String) partTable.getValueAt(row,2), (Integer) partTable.getValueAt(row,3), quantity, (Double) partTable.getValueAt(row,5), (Boolean) partTable.getValueAt(row,6), (String) partTable.getValueAt(row,7));

                            if (model.shoppingCarts.get(model.currCart).parts.contains(part))
                            {
                                int choice = JOptionPane.showConfirmDialog(null, "This is already in the current shopping cart. Would you like to increase the amount of the parts in the shopping cart instead?", "Confirmation", JOptionPane.YES_NO_OPTION);

                                if (choice == JOptionPane.YES_OPTION)
                                {
                                    model.shoppingCarts.get(model.currCart).parts.get(model.shoppingCarts.get(model.currCart).parts.indexOf(part)).quantity++;
                                    part = model.shoppingCarts.get(model.currCart).parts.get(model.shoppingCarts.get(model.currCart).parts.indexOf(part));
                                    for (PartLabel label: listsOfParts.get(model.currCart))
                                    {
                                        if (label.part.equals(part))
                                        {
                                            label.update(part);
                                            break;
                                        }
                                    }
                                    JOptionPane.showMessageDialog(null, "Increased the amount");
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Did not increase the amount");
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(dialog, "Added to Cart!");
                                model.shoppingCarts.get(model.currCart).parts.add(part);
                                addToCart(model.currCart, part);
                            }
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
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clock.setText("<html><p style=\"color: white; padding:3px; font-family: Verdana; font-size: 12px;border:solid\">" + new SimpleDateFormat("MMM dd, yyyy").format(new Date())+
                        "<br>" +  new SimpleDateFormat("hh:mm:ss a").format(new Date()) + "</p>");
            }
        });
        t.start();
        clock.setBorder(new EmptyBorder(0, 0, 0, 5));
        date = new Date().toLocaleString().split(", ")[0];

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

        for(int i = 0; i < 4; i++){
            cartNum[i] = 0;
        }
        cart = new StepButton("Cart");
        cart.setPreferredSize(new Dimension(77,50));
        cart.setFont(new Font("Verdana", Font.BOLD, 11));
        cart.setEnabled(true);
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
        cartButtons.get(model.currCart).setEnabled(false);

        cartsList.setBorder(new EmptyBorder(0,0,0,0));

        rightDisplay = new JPanel(new BorderLayout());

        cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(new JLabel("This is the CART. ヾ(≧▽≦*)o"), BorderLayout.NORTH);

        for(int i = 0; i < 5; i++)
        {
            cartViews.add(new JPanel());
            cartViews.get(i).setLayout(new BoxLayout(cartViews.get(i), BoxLayout.Y_AXIS));
        }
        cartViewScroll = new JScrollPane(cartViews.get(model.currCart), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cartViewScroll.getVerticalScrollBar().setUnitIncrement(16);
        cartPanel.add(cartViewScroll, BorderLayout.CENTER);
        proceedButton = new CartButton("Proceed to Checkout");
        cancelOrderButton = new CartButton();
        java.net.URL imageURL = getClass().getClassLoader().getResource("images/cancel_button.png");
        if (imageURL != null) {
            ImageIcon originalIcon = new ImageIcon(imageURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            cancelOrderButton.setIcon(new ImageIcon(scaledImage));
        } else {
            //System.err.println("Error: Unable to load cancel button icon");
            cancelOrderButton.setText("Cancel");
            cancelOrderButton.setFont(new Font("Verdana", Font.BOLD, 11));
        }
        cancelBackPanel = new JPanel(new GridLayout(1,5));
        cancelBackPanel.add(cancelOrderButton);
        cancelBackPanel.add(new JPanel());
        cancelBackPanel.add(new JPanel());
        cancelBackPanel.add(new JPanel());
        cancelBackPanel.add(new JPanel());
        cartPanel.add(cancelBackPanel, BorderLayout.NORTH);
        cartPanel.add(proceedButton, BorderLayout.SOUTH);

        checkoutPanel = new JPanel(new BorderLayout());
        checkoutView = new JPanel();
        checkoutView.setLayout(new BoxLayout(checkoutView, BoxLayout.Y_AXIS));
        JScrollPane checkoutViewScroll = new JScrollPane(checkoutView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        checkoutViewScroll.getVerticalScrollBar().setUnitIncrement(16);
        checkoutPanel.add(checkoutViewScroll, BorderLayout.CENTER);
        proceed2PayButton = new CartButton("Proceed to Payment");
        checkoutPanel.add(proceed2PayButton, BorderLayout.SOUTH);
        backButtonChk = new CartButton();


        paymentPanel = new JPanel(new BorderLayout());
        paymentView = new JPanel();
        paymentView.setLayout(new BoxLayout(paymentView, BoxLayout.Y_AXIS));
        JScrollPane paymentViewScroll = new JScrollPane(paymentView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        paymentViewScroll.getVerticalScrollBar().setUnitIncrement(16);
        paymentPanel.add(paymentViewScroll, BorderLayout.CENTER);
        payButton = new CartButton("Pay");
        /* FOR SPRINT 3
        calculateButton = new CartButton("Calculate");
         */
        paymentBtnsPanel = new JPanel(new BorderLayout());

        finalPrice = new JTextField();
        paymentBtnsPanel.add(finalPrice, BorderLayout.NORTH);
        paymentBtnsPanel.add(payButton,BorderLayout.CENTER);
        paymentPanel.add(paymentBtnsPanel, BorderLayout.SOUTH);
        backButtonPay = new CartButton();

        receiptPanel = new JPanel(new BorderLayout());
        receiptView = new JPanel();
        receiptView.setLayout(new BoxLayout(receiptView, BoxLayout.Y_AXIS));
        JScrollPane receiptViewScroll = new JScrollPane(receiptView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        receiptViewScroll.getVerticalScrollBar().setUnitIncrement(16);
        receiptPanel.add(receiptViewScroll, BorderLayout.CENTER);
        printButton = new CartButton("Print Receipt");
        receiptPanel.add(printButton, BorderLayout.SOUTH);

        displayScreen = cartPanel;
        rightDisplay.add(displayScreen, BorderLayout.CENTER);

        rightPanel.add(stepsPanel,BorderLayout.NORTH);
        rightPanel.add(cartsList, BorderLayout.WEST);
        rightPanel.add(rightDisplay, BorderLayout.CENTER);
    }
    public void addToCart(int i, Part part)
    {
        PartLabel label = new PartLabel(part);
        listsOfParts.get(i ).add(label);
        cartViews.get(i).add(label);
        cartViews.get(i).repaint();
        cartViews.get(i).revalidate();
    }
    public void checkoutList(Part part)
    {
        JLabel label = new JLabel();
        label.setText("<html>\n" +
                "    <body style=\"background-color: cyan; border-radius: 4px; border-style: solid;\">\n" +
                "        <p>"+ part.carBrand + "</p>\n" +
                "        <p>" + part.carModel + " | " + part.name  + "(" + part.year + ")</p>\n" +
                "        <p>" + part.authenticity + " | " + part.isNew + "</p>\n" +
                "        <p>" + "P" + part.price + " | QTY: " + part.quantity + "</p>\n" +
                "    </body>\n" +
                "</html>");
        checkoutView.add(label);
        checkoutView.repaint();
        checkoutView.revalidate();
    }
    public void paymentList(Part part)
    {
        /* FOR SPRINT 3
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        */
        JLabel label = new JLabel();

        label.setText("<html>\n" +
                "    <body style=\"background-color: cyan; border-radius: 4px; border-style: solid;\">\n" +
                "        <p>"+ part.carBrand + "</p>" +
                "        <p>" + part.carModel + "</p>" +
                "        <p>"+ part.name  + " (" + part.year + ")" + "</p>" +
                "        <p>" + "QTY: " + part.quantity + "  |  P" + part.price + " | " + "</p></p>\n" +
                "    </body>\n" +
                "</html>");
        /* FOR SPRINT 3
        panel.add(label);
        JTextField textField = new JTextField(5);
        panel.add(textField);
         */


        paymentView.add(label);
        paymentView.repaint();
        paymentView.revalidate();
    }

    public void receiptList(Part part)
    {
        JLabel label = new JLabel();
        label.setText("<html>\n" +
                "    <body style=\"background-color: cyan; border-radius: 4px; border-style: solid;\">\n" +
                "        <p>"+ part.carBrand + "</p>" +
                "        <p>" + part.carModel + "</p>" +
                "        <p>" + part.name  + " (" + part.year + ")" + "  |  QTY: " + part.quantity + "  |  P" + part.price + " | " + "</p></p>\n" +
                "    </body>\n" +
                "</html>");
        receiptView.add(label);
        receiptView.repaint();
        receiptView.revalidate();
    }
    public void receiptListTotal(Double sum)
    {
        JLabel label = new JLabel();
        label.setText("<html>\n" +
                "    <body style=\"background-color: cyan; border-radius: 6px; border-style: solid;\">\n" +
                "        <p>"+ "TOTAL: P" +  sum + "</p>" +
                "    </body>\n" +
                "</html>");
        label.setFont(new Font("Times New Roman", Font.BOLD, 16));
        receiptView.add(label);
        receiptView.repaint();
        receiptView.revalidate();
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

    public CartButton(String text)
    {
        this.setText(text);
        this.setPreferredSize(new Dimension(50,40));
        this.setFont(new Font("Verdana", Font.BOLD, 16));
        this.setFocusPainted(false);
    }

    public CartButton()
    {
        this.setPreferredSize(new Dimension(30,30));
        this.setFocusPainted(false);
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

class PartLabel extends JLabel {
    Part part;
    public PartLabel(Part part){
        this.part = part;
        this.setText();
    }

    public void update(Part part)
    {
        this.part = part;
        this.setText();
    }

    public void setText()
    {
        this.setText("<html>\n" +
                "    <body style=\"background-color: cyan; border-radius: 4px; border-style: solid;\">\n" +
                "        <p>"+ part.carBrand + "</p>\n" +
                "        <p>" + part.carModel + " | " + part.name  + "(" + part.year + ")</p>\n" +
                "        <p>" + part.authenticity + " | " + part.isNew + "</p>\n" +
                "        <p>" + "P" + part.price + " | QTY: " + part.quantity + "</p>\n" +
                "    </body>\n" +
                "</html>");
    }
}
