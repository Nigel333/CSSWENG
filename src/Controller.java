import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Controller {
    Main model;
    Frame view;

    public Controller(Frame view)
    {
        this.view = view;
        this.model = this.view.model;

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                model.overwriteFile();
            }
        });

        this.view.searchField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {filter();}
            @Override
            public void removeUpdate(DocumentEvent e) {filter();}
            @Override
            public void changedUpdate(DocumentEvent e) {filter();}
        });

        this.view.sortBy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.brandFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.modelFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {filter();}
        });

        this.view.fromYear.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.fromYear.getText().isEmpty()) {
                    try {
                        int num = Integer.parseInt(view.fromYear.getText());
                        if (num >= 9999) {
                            view.fromYear.setBackground(Color.decode("#FFCCCC"));
                        } else {
                            view.fromYear.setBackground(Color.WHITE);
                        }
                    } catch(Exception exception) {
                        view.fromYear.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.fromYear.setBackground(Color.WHITE);
                }
                filter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.toYear.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.toYear.getText().isEmpty()) {
                    try {
                        int num = Integer.parseInt(view.toYear.getText());
                        if (num >= 9999) {
                            view.toYear.setBackground(Color.decode("#FFCCCC"));
                        } else {
                            view.toYear.setBackground(Color.WHITE);
                        }
                    } catch(Exception exception) {
                        view.toYear.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.toYear.setBackground(Color.WHITE);
                }
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.fromPrice.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.fromPrice.getText().isEmpty()) {
                    String input = view.fromPrice.getText();
                    try
                    {
                        Double.parseDouble(input);
                        view.fromPrice.setBackground(Color.WHITE);
                    } catch (Exception exception)
                    {
                        view.fromPrice.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.fromPrice.setBackground(Color.WHITE);
                }
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.toPrice.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                if(!view.toPrice.getText().isEmpty()) {
                    String input = view.toPrice.getText();
                    try
                    {
                        Double.parseDouble(input);
                        view.toPrice.setBackground(Color.WHITE);
                    } catch (Exception exception)
                    {
                        view.toPrice.setBackground(Color.decode("#FFCCCC"));
                    }
                } else {
                    view.toPrice.setBackground(Color.WHITE);
                }
                filter();
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        this.view.newFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        this.view.authenticityFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        this.view.account.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] option = {"Enter"};
                JPasswordField passwordField = new JPasswordField();
                boolean exited = false;
                if(JOptionPane.showOptionDialog(null, passwordField, "Please Enter the Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]) == JOptionPane.CLOSED_OPTION)
                    exited = true;
                if(!exited && passwordField.getText().equals("password"))
                {
                    view.account.setIcon(new ImageIcon("resources/manager.png"));

                    JDialog dialog = new JDialog(view, "Manager Settings", true);
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.setBackground(Color.ORANGE);
                    JPanel optionPanel = new JPanel(new FlowLayout());
                    optionPanel.setBackground(Color.ORANGE);
                    JButton add = new JButton("ADD");
                    add.setPreferredSize(new Dimension(200, 30));
                    JButton update = new JButton("UPDATE");
                    update.setPreferredSize(new Dimension(200, 30));
                    JButton delete = new JButton("DELETE");
                    delete.setPreferredSize(new Dimension(200, 30));
                    optionPanel.add(add);
                    optionPanel.add(update);
                    optionPanel.add(delete);
                    panel.add(optionPanel, BorderLayout.NORTH);
                    dialog.getContentPane().add(panel);

                    JPanel addPanel;
                    final JPanel[] updatePanel = new JPanel[1];
                    JPanel deletePanel;
                    final JPanel[] currPanel = new JPanel[1];

                    addPanel = new JPanel(new GridBagLayout());
                    addPanel.setBackground(Color.ORANGE);
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.insets = new Insets(5, 5, 5, 5);
                    JButton addButton = new JButton("ADD");
                    JTextField carBrand = new JTextField(20);
                    JTextField carModel = new JTextField(20);
                    JTextField name = new JTextField(20);
                    JTextField year = new JTextField(20);
                    JTextField quantity = new JTextField(20);
                    JTextField price = new JTextField(20);
                    JComboBox condition = new JComboBox(new String[]{"NEW", "OLD"});
                    JComboBox authenticity1 = new JComboBox(new String[]{"ORIGINAL", "CLASS A", "OTHERS"});
                    JTextField authenticity2 = new JTextField(15);
                    authenticity2.setEnabled(false);
                    authenticity1.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(authenticity1.getSelectedIndex() == 2)
                                authenticity2.setEnabled(true);
                            else
                                authenticity2.setEnabled(false);
                        }
                    });

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Car Brand: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(carBrand, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Car Model: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(carModel, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 2;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Name: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(name, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Car Brand: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(carBrand, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Car Model: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(carModel, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 2;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Name: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(name, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 3;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Year: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(year, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 4;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Quantity: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(quantity, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 5;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Price: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(price, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 6;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Condition: "), gbc);
                    gbc.gridx = 1;
                    gbc.gridwidth = 2;
                    addPanel.add(condition, gbc);

                    gbc.gridx = 0;
                    gbc.gridy = 7;
                    gbc.gridwidth = 1;
                    addPanel.add(new JLabel("Authenticity: "), gbc);
                    gbc.gridx = 1;
                    addPanel.add(authenticity1, gbc);
                    gbc.gridx = 2;
                    addPanel.add(authenticity2, gbc);

                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean error = false;
                            try
                            {
                                if(Integer.parseInt(year.getText()) > 9999 || Integer.parseInt(year.getText()) < 0)
                                {
                                    year.setBackground(Color.decode("#FFCCCC"));
                                    error = true;
                                }
                                else
                                    year.setBackground(Color.WHITE);
                            } catch (Exception err)
                            {
                                year.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }

                            try
                            {
                                Integer.parseInt(quantity.getText());
                                quantity.setBackground(Color.WHITE);
                            } catch (Exception err)
                            {
                                quantity.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }

                            try
                            {
                                Double.parseDouble(price.getText());
                                price.setBackground(Color.WHITE);
                            } catch (Exception err)
                            {
                                price.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }

                            if(carBrand.getText().equals(""))
                            {
                                carBrand.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }
                            else
                                carBrand.setBackground(Color.WHITE);
                            if(carModel.getText().equals(""))
                            {
                                carModel.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }
                            else
                                carModel.setBackground(Color.WHITE);
                            if(name.getText().equals(""))
                            {
                                name.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }
                            else
                                name.setBackground(Color.WHITE);
                            if(authenticity1.getSelectedIndex() == 2 && authenticity2.getText().equals(""))
                            {
                                authenticity2.setBackground(Color.decode("#FFCCCC"));
                                error = true;
                            }
                            else
                                authenticity2.setBackground(Color.WHITE);

                            if(error)
                                JOptionPane.showMessageDialog(dialog, "Error Occurred!");
                            else
                            {
                                String auth;
                                if(authenticity1.getSelectedIndex() != 2)
                                    auth = authenticity1.getSelectedItem().toString();
                                else
                                    auth = authenticity2.getText();
                                Part part = new Part(carBrand.getText().toUpperCase(), carModel.getText().toUpperCase(), name.getText().toUpperCase(), Integer.parseInt(year.getText()), Integer.parseInt(quantity.getText()), Double.parseDouble(price.getText()), condition.getSelectedIndex() == 0, auth.toUpperCase());
                                if(model.parts.contains(part))
                                {
                                    if(JOptionPane.showConfirmDialog(null, "This is already in the database already. Would you like to increase the quantity of the parts in the database instead?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                                    {
                                        model.parts.get(model.parts.indexOf(part)).quantity += part.quantity;
                                        model.tableModel.changeParts(model.parts);
                                        if (model.tableModel.getRowCount() != 0)
                                            model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                                        JOptionPane.showMessageDialog(dialog, "Successfully Saved to Database!");
                                    }
                                    else
                                        JOptionPane.showMessageDialog(dialog, "Did Not Do Anything");
                                }
                                else
                                {
                                    model.parts.add(part);
                                    model.tableModel.changeParts(model.parts);
                                    if (model.tableModel.getRowCount() != 0)
                                        model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                                    JOptionPane.showMessageDialog(dialog, "Part Successfully Added to Database!");

                                    if(!model.carBrands.contains(part.carBrand))
                                    {
                                        model.carBrands.add(part.carBrand);
                                        view.brandFilter.addItem(part.carBrand);
                                    }
                                    boolean exists = false;
                                    for(int i = 0; i < view.modelFilter.getItemCount(); i++)
                                        if(view.modelFilter.getItemAt(i).equals(part.carModel))
                                            exists = true;
                                    if(!exists)
                                        view.modelFilter.addItem(part.carModel);
                                }
                            }
                            filter();
                        }
                    });
                    gbc.gridx = 0;
                    gbc.gridy = 8;
                    gbc.gridwidth = 3;
                    addPanel.add(addButton, gbc);

                    JPanel update1 = new JPanel(new BorderLayout());
                    JTable updateTable = new JTable(model.tableModel)
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
                    updateTable.setPreferredScrollableViewportSize(new Dimension(300, 750));
                    updateTable.getColumnModel().getColumn(0).setPreferredWidth(10);
                    updateTable.getColumnModel().getColumn(1).setPreferredWidth(30);
                    updateTable.getColumnModel().getColumn(2).setPreferredWidth(100);
                    updateTable.getColumnModel().getColumn(3).setPreferredWidth(8);
                    updateTable.getColumnModel().getColumn(4).setPreferredWidth(5);
                    updateTable.getColumnModel().getColumn(5).setPreferredWidth(10);
                    updateTable.getColumnModel().getColumn(6).setPreferredWidth(3);
                    updateTable.getColumnModel().getColumn(7).setPreferredWidth(10);
                    for (int i = 0; i < updateTable.getRowCount(); i ++)
                        updateTable.setRowHeight(i, 60);
                    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
                    cellRenderer.setHorizontalAlignment(JLabel.CENTER);
                    for (int i = 0; i < updateTable.getColumnCount(); i ++)
                        if(i != 6)
                            updateTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                    updateTable.addMouseListener(new MouseAdapter()
                    {
                        public void mouseClicked(MouseEvent me)
                        {
                            if (me.getClickCount() == 2)
                            {
                                JTable target = (JTable) me.getSource();
                                int row = target.getSelectedRow();
                                JButton backButton = new JButton("<");
                                JButton updateButton = new JButton("Update");
                                JTextField carBrand = new JTextField(20);
                                JTextField carModel = new JTextField(20);
                                JTextField name = new JTextField(20);
                                JTextField year = new JTextField(20);
                                JTextField quantity = new JTextField(20);
                                JTextField price = new JTextField(20);
                                JComboBox condition = new JComboBox(new String[]{"NEW", "OLD"});
                                JComboBox authenticity1 = new JComboBox(new String[]{"ORIGINAL", "CLASS A", "OTHERS"});
                                JTextField authenticity2 = new JTextField(15);
                                Part part = new Part((String) updateTable.getValueAt(row,0), (String) updateTable.getValueAt(row,1), (String) updateTable.getValueAt(row,2), (Integer) updateTable.getValueAt(row,3), (Integer) updateTable.getValueAt(row,4), (Double) updateTable.getValueAt(row,5), (Boolean) updateTable.getValueAt(row,6), (String) updateTable.getValueAt(row,7));
                                carBrand.setText(part.carBrand);
                                carModel.setText(part.carModel);
                                name.setText(part.name);
                                year.setText(String.valueOf(part.year));
                                quantity.setText(String.valueOf(part.quantity));
                                price.setText(String.valueOf(part.price));
                                condition.setSelectedIndex(part.isNew ? 0 : 1);
                                authenticity2.setEnabled(false);
                                if(part.authenticity.equals("ORIGINAL") || part.authenticity.equals("CLASS A"))
                                    authenticity1.setSelectedItem(part.authenticity);
                                else
                                {
                                    authenticity2.setEnabled(true);
                                    authenticity1.setSelectedIndex(2);
                                    authenticity2.setText(part.authenticity);
                                }
                                authenticity1.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if(authenticity1.getSelectedIndex() == 2)
                                            authenticity2.setEnabled(true);
                                        else
                                            authenticity2.setEnabled(false);
                                    }
                                });
                                panel.remove(currPanel[0]);
                                updatePanel[0] = new JPanel(new GridBagLayout());
                                currPanel[0] = updatePanel[0];
                                panel.add(currPanel[0]);

                                gbc.gridx = 0;
                                gbc.gridy = 0;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Car Brand: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(carBrand, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 1;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Car Model: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(carModel, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 2;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Name: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(name, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 0;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Car Brand: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(carBrand, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 1;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Car Model: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(carModel, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 2;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Name: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(name, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 3;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Year: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(year, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 4;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Quantity: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(quantity, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 5;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Price: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(price, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 6;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Condition: "), gbc);
                                gbc.gridx = 1;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(condition, gbc);

                                gbc.gridx = 0;
                                gbc.gridy = 7;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(new JLabel("Authenticity: "), gbc);
                                gbc.gridx = 1;
                                updatePanel[0].add(authenticity1, gbc);
                                gbc.gridx = 2;
                                updatePanel[0].add(authenticity2, gbc);

                                backButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        panel.remove(currPanel[0]);
                                        updatePanel[0] = update1;
                                        currPanel[0] = updatePanel[0];
                                        panel.add(currPanel[0]);
                                        panel.revalidate();
                                        panel.repaint();
                                    }
                                });
                                gbc.gridx = 0;
                                gbc.gridy = 8;
                                gbc.gridwidth = 1;
                                updatePanel[0].add(backButton, gbc);

                                updateButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int row = updateTable.getSelectedRow();
                                        Part oldPart = new Part((String) updateTable.getValueAt(row,0), (String) updateTable.getValueAt(row,1), (String) updateTable.getValueAt(row,2), (Integer) updateTable.getValueAt(row,3), (Integer) updateTable.getValueAt(row,4), (Double) updateTable.getValueAt(row,5), (Boolean) updateTable.getValueAt(row,6), (String) updateTable.getValueAt(row,7));
                                        boolean error = false;
                                        try
                                        {
                                            if(Integer.parseInt(year.getText()) > 9999 || Integer.parseInt(year.getText()) < 0)
                                            {
                                                year.setBackground(Color.decode("#FFCCCC"));
                                                error = true;
                                            }
                                            else
                                                year.setBackground(Color.WHITE);
                                        } catch (Exception err)
                                        {
                                            year.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }

                                        try
                                        {
                                            Integer.parseInt(quantity.getText());
                                            quantity.setBackground(Color.WHITE);
                                        } catch (Exception err)
                                        {
                                            quantity.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }

                                        try
                                        {
                                            Double.parseDouble(price.getText());
                                            price.setBackground(Color.WHITE);
                                        } catch (Exception err)
                                        {
                                            price.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }

                                        if(carBrand.getText().equals(""))
                                        {
                                            carBrand.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }
                                        else
                                            carBrand.setBackground(Color.WHITE);
                                        if(carModel.getText().equals(""))
                                        {
                                            carModel.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }
                                        else
                                            carModel.setBackground(Color.WHITE);
                                        if(name.getText().equals(""))
                                        {
                                            name.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }
                                        else
                                            name.setBackground(Color.WHITE);
                                        if(authenticity1.getSelectedIndex() == 2 && authenticity2.getText().equals(""))
                                        {
                                            authenticity2.setBackground(Color.decode("#FFCCCC"));
                                            error = true;
                                        }
                                        else
                                            authenticity2.setBackground(Color.WHITE);

                                        if(error)
                                            JOptionPane.showMessageDialog(dialog, "Error Occurred!");
                                        else
                                        {
                                            String auth;
                                            if(authenticity1.getSelectedIndex() != 2)
                                                auth = authenticity1.getSelectedItem().toString();
                                            else
                                                auth = authenticity2.getText();
                                            Part part = new Part(carBrand.getText().toUpperCase(), carModel.getText().toUpperCase(), name.getText().toUpperCase(), Integer.parseInt(year.getText()), Integer.parseInt(quantity.getText()), Double.parseDouble(price.getText()), condition.getSelectedIndex() == 0, auth.toUpperCase());
                                            if(oldPart.equals(part))
                                            {
                                                if(oldPart.quantity == part.quantity && oldPart.price == part.price)
                                                    JOptionPane.showMessageDialog(dialog,"There has been no change made.");
                                                else
                                                {
                                                    model.parts.remove(oldPart);
                                                    model.parts.add(part);
                                                    model.tableModel.changeParts(model.parts);
                                                    if (model.tableModel.getRowCount() != 0)
                                                        model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                                                    JOptionPane.showMessageDialog(dialog, "Part Successfully Updated!");
                                                }
                                            }
                                            else if(model.parts.contains(part))
                                            {
                                                if(JOptionPane.showConfirmDialog(dialog, "This is already in the database already. Would you like to increase the quantity of the other part in the database instead?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                                                {
                                                    model.parts.remove(oldPart);
                                                    model.parts.get(model.parts.indexOf(part)).quantity += part.quantity;
                                                    model.tableModel.changeParts(model.parts);
                                                    if (model.tableModel.getRowCount() != 0)
                                                        model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                                                    JOptionPane.showMessageDialog(dialog, "Successfully Saved to Database!");
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(dialog, "Did Not Do Anything");
                                            }
                                            else
                                            {
                                                model.parts.remove(oldPart);
                                                model.parts.add(part);
                                                model.tableModel.changeParts(model.parts);
                                                if (model.tableModel.getRowCount() != 0)
                                                    model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                                                JOptionPane.showMessageDialog(dialog, "Part Successfully Updated!");
                                            }
                                            if(!model.carBrands.contains(part.carBrand))
                                            {
                                                model.carBrands.add(part.carBrand);
                                                view.brandFilter.addItem(part.carBrand);
                                            }
                                            boolean exists = false;
                                            for(int i = 0; i < view.modelFilter.getItemCount(); i++)
                                                if(view.modelFilter.getItemAt(i).equals(part.carModel))
                                                    exists = true;
                                            if(!exists)
                                                view.modelFilter.addItem(part.carModel);

                                        }
                                        filter();
                                        updatePanel[0] = update1;
                                        panel.remove(currPanel[0]);
                                        currPanel[0] = updatePanel[0];
                                        panel.add(currPanel[0]);
                                        panel.revalidate();
                                        panel.repaint();
                                    }
                                });
                                gbc.gridx = 1;
                                gbc.gridy = 8;
                                gbc.gridwidth = 2;
                                updatePanel[0].add(updateButton, gbc);

                                panel.revalidate();
                                panel.repaint();
                            }
                        }
                    });
                    JScrollPane scrollPane = new JScrollPane(updateTable);
                    scrollPane.setBackground(Color.decode("#92D050"));
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    update1.add(scrollPane, BorderLayout.CENTER);

                    JTextField searchBar = new JTextField();
                    searchBar.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            filter();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            filter();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            filter();
                        }

                        private void filter()
                        {
                            ArrayList<Part> filtered = new ArrayList();
                            String text = searchBar.getText().toUpperCase();
                            for(Part part : model.parts)
                            {
                                boolean has = true;
                                for (String input: text.split(" "))
                                {
                                    if (!part.toString().toUpperCase().contains(input))
                                    {
                                        has = false;
                                        break;
                                    }
                                }
                                if (has)
                                    filtered.add(part);
                            }
                            model.tableModel.changeParts(filtered);
                            if (model.tableModel.getRowCount() != 0)
                                model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                            updateTable.revalidate();
                        }
                    });
                    update1.add(searchBar, BorderLayout.NORTH);
                    updatePanel[0] = update1;

                    deletePanel = new JPanel(new BorderLayout());
                    JTable deleteTable = new JTable(model.tableModel)
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
                    deleteTable.setPreferredScrollableViewportSize(new Dimension(300, 750));
                    deleteTable.getColumnModel().getColumn(0).setPreferredWidth(10);
                    deleteTable.getColumnModel().getColumn(1).setPreferredWidth(30);
                    deleteTable.getColumnModel().getColumn(2).setPreferredWidth(100);
                    deleteTable.getColumnModel().getColumn(3).setPreferredWidth(8);
                    deleteTable.getColumnModel().getColumn(4).setPreferredWidth(5);
                    deleteTable.getColumnModel().getColumn(5).setPreferredWidth(10);
                    deleteTable.getColumnModel().getColumn(6).setPreferredWidth(3);
                    deleteTable.getColumnModel().getColumn(7).setPreferredWidth(10);
                    for (int i = 0; i < deleteTable.getRowCount(); i ++)
                        deleteTable.setRowHeight(i, 60);
                    for (int i = 0; i < deleteTable.getColumnCount(); i ++)
                        if(i != 6)
                            deleteTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                    deleteTable.addMouseListener(new MouseAdapter()
                    {
                        public void mouseClicked(MouseEvent me)
                        {
                            if (me.getClickCount() == 2)
                            {
                                JTable target = (JTable) me.getSource();
                                int row = target.getSelectedRow();
                                int choice = JOptionPane.showConfirmDialog(
                                        null,
                                        "Delete " + deleteTable.getValueAt(row,1) + " " + deleteTable.getValueAt(row,2) + "?",
                                        "Delete Selected Item?",
                                        JOptionPane.YES_NO_OPTION);

                                if (choice == JOptionPane.YES_OPTION)
                                {
                                    Part part = new Part((String) deleteTable.getValueAt(row,0), (String) deleteTable.getValueAt(row,1), (String) deleteTable.getValueAt(row,2), (Integer) deleteTable.getValueAt(row,3), (Integer) deleteTable.getValueAt(row,4), (Double) deleteTable.getValueAt(row,5), (Boolean) deleteTable.getValueAt(row,6), (String) deleteTable.getValueAt(row,7));
                                    model.parts.remove(part);
                                    model.tableModel.changeParts(model.parts);
                                    if (model.tableModel.getRowCount() != 0)
                                        model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                                    JOptionPane.showMessageDialog(dialog, "Part Successfully Deleted From Database");
                                }
                                else
                                    JOptionPane.showMessageDialog(dialog, "Part Was Not Deleted From Database");
                            }
                        }
                    });

                    scrollPane = new JScrollPane(deleteTable);
                    scrollPane.setBackground(Color.decode("#92D050"));
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    JTextField searchBar2 = new JTextField();
                    searchBar2.getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            filter();
                        }

                        @Override
                        public void removeUpdate(DocumentEvent e) {
                            filter();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent e) {
                            filter();
                        }

                        private void filter()
                        {
                            ArrayList<Part> filtered = new ArrayList();
                            String text = searchBar2.getText().toUpperCase();
                            for(Part part : model.parts)
                            {
                                boolean has = true;
                                for (String input: text.split(" "))
                                {
                                    if (!part.toString().toUpperCase().contains(input))
                                    {
                                        has = false;
                                        break;
                                    }
                                }
                                if (has)
                                    filtered.add(part);
                            }
                            model.tableModel.changeParts(filtered);
                            if (model.tableModel.getRowCount() != 0)
                                model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
                            deleteTable.revalidate();
                        }
                    });
                    deletePanel.add(searchBar2, BorderLayout.NORTH);
                    deletePanel.add(scrollPane, BorderLayout.CENTER);

                    currPanel[0] = addPanel;
                    panel.add(currPanel[0], BorderLayout.CENTER);

                    add.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            panel.remove(currPanel[0]);
                            currPanel[0] = addPanel;
                            panel.add(currPanel[0], BorderLayout.CENTER); // Specify layout constraints
                            panel.revalidate(); // Refresh layout
                            panel.repaint(); // Repaint panel
                        }
                    });

                    update.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            panel.remove(currPanel[0]);
                            currPanel[0] = updatePanel[0];
                            panel.add(currPanel[0], BorderLayout.CENTER);
                            filter(searchBar, updateTable);
                            panel.revalidate();
                            panel.repaint();
                        }
                    });

                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            panel.remove(currPanel[0]);
                            currPanel[0] = deletePanel;
                            panel.add(currPanel[0], BorderLayout.CENTER);
                            filter(searchBar2, deleteTable);
                            panel.revalidate();
                            panel.repaint();
                        }
                    });

                    panel.add(addPanel, BorderLayout.CENTER);

                    dialog.setSize(800, 500);
                    dialog.setLocationRelativeTo(view);
                    dialog.setVisible(true);

                    dialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            view.account.setIcon(new ImageIcon("resources/regular.png"));
                        }
                    });
                }
                else if (!exited)
                    JOptionPane.showMessageDialog(null, "Wrong Password Inputted. Please Try Again.", "Error", JOptionPane.ERROR_MESSAGE);

                view.account.setIcon(new ImageIcon("resources/regular.png"));
            }
        });


        for (CartButton cartButton :  view.cartButtons)
        {
            cartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    view.cartButtons.get(model.currCart).setEnabled(true);
                    view.cartPanel.remove(view.cartViewScroll);
                    model.currCart = cartButton.num - 1;
                    view.cartViewScroll = new JScrollPane(view.cartViews.get(model.currCart), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    view.cartViewScroll.getVerticalScrollBar().setUnitIncrement(16);
                    view.cartPanel.add(view.cartViewScroll, BorderLayout.CENTER);
                    cartButton.setEnabled(false);

                    switch(view.cartNum[model.currCart]){
                        case 0: view.rightDisplay.remove(view.displayScreen);
                            view.cancelBackPanel.removeAll();
                            view.cancelBackPanel.add(view.cancelOrderButton);
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cartPanel.add(view.cancelBackPanel, BorderLayout.NORTH);
                            view.displayScreen = view.cartPanel;
                            view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                            view.rightDisplay.repaint();
                            view.rightDisplay.revalidate();

                            view.cart.setEnabled(true);
                            view.checkout.setEnabled(false);
                            view.payment.setEnabled(false);
                            view.receipt.setEnabled(false);
                            break;
                        case 1: view.rightDisplay.remove(view.displayScreen);
                            view.checkoutView.removeAll();
                            view.checkoutView.repaint();
                            view.checkoutView.revalidate();
                            for (Part part : model.shoppingCarts.get(model.currCart).parts) {
                                view.checkoutList(part);
                            }
                            view.cancelBackPanel.removeAll();
                            java.net.URL imageURL = getClass().getClassLoader().getResource("images/back_button.png");
                            if (imageURL != null) {
                                ImageIcon originalIcon = new ImageIcon(imageURL);
                                Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                                view.backButtonChk.setIcon(new ImageIcon(scaledImage));
                            } else {
                                //System.err.println("Error: Unable to load back button icon");
                                view.backButtonChk.setText("Back");
                                view.backButtonChk.setFont(new Font("Verdana", Font.BOLD, 11));
                            }
                            view.cancelBackPanel.add(view.backButtonChk);
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.checkoutPanel.add(view.cancelBackPanel, BorderLayout.NORTH);


                            view.displayScreen = view.checkoutPanel;
                            view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                            view.rightDisplay.repaint();
                            view.rightDisplay.revalidate();

                            view.cart.setEnabled(false);
                            view.checkout.setEnabled(true);
                            view.payment.setEnabled(false);
                            view.receipt.setEnabled(false);
                            break;
                        case 2: view.rightDisplay.remove(view.displayScreen);
                            view.paymentView.removeAll();
                            view.paymentView.repaint();
                            view.paymentView.revalidate();
                            view.partPrices.clear();
                            for (Part part : model.shoppingCarts.get(model.currCart).parts) {
                                view.paymentList(part);
                                view.partPrices.add(part.price);
                            }
                            double tempPrice = 0;
                            for (Double price : view.partPrices) {
                                tempPrice += price;
                            }
                            view.sum[model.currCart] = tempPrice;
                            view.finalPrice.setText(Double.toString(view.sum[model.currCart]));
                            view.cancelBackPanel.removeAll();
                            imageURL = getClass().getClassLoader().getResource("images/back_button.png");
                            if (imageURL != null) {
                                ImageIcon originalIcon = new ImageIcon(imageURL);
                                Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                                view.backButtonPay.setIcon(new ImageIcon(scaledImage));
                            } else {
                                //System.err.println("Error: Unable to load back button icon");
                                view.backButtonPay.setText("Back");
                                view.backButtonPay.setFont(new Font("Verdana", Font.BOLD, 11));
                            }
                            view.cancelBackPanel.add(view.backButtonPay);
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.cancelBackPanel.add(new JPanel());
                            view.paymentPanel.add(view.cancelBackPanel, BorderLayout.NORTH);

                            view.displayScreen = view.paymentPanel;
                            view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                            view.rightDisplay.repaint();
                            view.rightDisplay.revalidate();

                            view.cart.setEnabled(false);
                            view.checkout.setEnabled(false);
                            view.payment.setEnabled(true);
                            view.receipt.setEnabled(false);
                            break;
                        case 3: view.rightDisplay.remove(view.displayScreen);
                            view.receiptView.removeAll();
                            view.receiptView.repaint();
                            view.receiptView.revalidate();
                            for (Part part : model.shoppingCarts.get(model.currCart).parts) {
                                view.receiptList(part);
                                for (Part data: model.parts)
                                    if(data.equals(part)){
                                        view.finalPrice.getText();

                                        view.cancelBackPanel.removeAll();
                                        view.receiptPanel.add(view.cancelBackPanel, BorderLayout.NORTH);

                                        view.displayScreen = view.receiptPanel;
                                        view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                                        view.rightDisplay.repaint();
                                        view.rightDisplay.revalidate();

                                        view.cart.setEnabled(false);
                                        view.checkout.setEnabled(false);
                                        view.payment.setEnabled(false);
                                        view.receipt.setEnabled(true);
                                    }
                            }
                            view.receiptListTotal(model.shoppingCarts.get(model.currCart).finalPrice);
                            break;
                    }

                }
            });
        }


        view.proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.shoppingCarts.get(model.currCart).parts.size() != 0) {
                    view.cartNum[model.currCart] = 1;
                    view.rightDisplay.remove(view.displayScreen);
                    view.checkoutView.removeAll();
                    view.checkoutView.repaint();
                    view.checkoutView.revalidate();
                    for (Part part : model.shoppingCarts.get(model.currCart).parts) {
                        view.checkoutList(part);
                    }
                    view.cancelBackPanel.removeAll();
                    java.net.URL imageURL = getClass().getClassLoader().getResource("images/back_button.png");
                    if (imageURL != null) {
                        ImageIcon originalIcon = new ImageIcon(imageURL);
                        Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                        view.backButtonChk.setIcon(new ImageIcon(scaledImage));
                    } else {
                        //System.err.println("Error: Unable to load back button icon");
                        view.backButtonChk.setText("Back");
                        view.backButtonChk.setFont(new Font("Verdana", Font.BOLD, 11));
                    }
                    view.cancelBackPanel.add(view.backButtonChk);
                    view.cancelBackPanel.add(new JPanel());
                    view.cancelBackPanel.add(new JPanel());
                    view.cancelBackPanel.add(new JPanel());
                    view.cancelBackPanel.add(new JPanel());
                    view.checkoutPanel.add(view.cancelBackPanel, BorderLayout.NORTH);


                    view.displayScreen = view.checkoutPanel;
                    view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                    view.rightDisplay.repaint();
                    view.rightDisplay.revalidate();

                    view.cart.setEnabled(false);
                    view.checkout.setEnabled(true);
                    view.payment.setEnabled(false);
                    view.receipt.setEnabled(false);
                } else
                    JOptionPane.showMessageDialog(null, "The cart is empty. Please fill it up first!");
            }
        });
        view.proceed2PayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.cartNum[model.currCart] = 2;
                view.rightDisplay.remove(view.displayScreen);
                view.paymentView.removeAll();
                view.paymentView.repaint();
                view.paymentView.revalidate();
                view.partPrices.clear();
                for (Part part : model.shoppingCarts.get(model.currCart).parts) {
                    view.paymentList(part);
                    view.partPrices.add(part.price);
                }
                double tempPrice = 0;
                for (Double price : view.partPrices) {
                    tempPrice += price;
                }
                view.sum[model.currCart] = tempPrice;
                view.finalPrice.setText(Double.toString(view.sum[model.currCart]));
                view.cancelBackPanel.removeAll();
                java.net.URL imageURL = getClass().getClassLoader().getResource("images/back_button.png");
                if (imageURL != null) {
                    ImageIcon originalIcon = new ImageIcon(imageURL);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                    view.backButtonPay.setIcon(new ImageIcon(scaledImage));
                } else {
                    //System.err.println("Error: Unable to load back button icon");
                    view.backButtonPay.setText("Back");
                    view.backButtonPay.setFont(new Font("Verdana", Font.BOLD, 11));
                }
                view.cancelBackPanel.add(view.backButtonPay);
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.paymentPanel.add(view.cancelBackPanel, BorderLayout.NORTH);

                view.displayScreen = view.paymentPanel;
                view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                view.rightDisplay.repaint();
                view.rightDisplay.revalidate();

                view.cart.setEnabled(false);
                view.checkout.setEnabled(false);
                view.payment.setEnabled(true);
                view.receipt.setEnabled(false);
            }
        });
        this.view.finalPrice.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                String input = view.finalPrice.getText();
                try
                {
                    Double.parseDouble(input);
                    view.finalPrice.setBackground(Color.WHITE);
                } catch (Exception exception)
                {
                    view.finalPrice.setBackground(Color.decode("#FFCCCC"));
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) { update(); }
            @Override
            public void removeUpdate(DocumentEvent e) { update(); }
            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });
        view.payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!view.finalPrice.getBackground().equals(Color.WHITE))
                    JOptionPane.showMessageDialog(null, "Error Occured");
                else
                {
                    model.shoppingCarts.get(model.currCart).finalPrice = Double.parseDouble(view.finalPrice.getText());
                    view.cartNum[model.currCart] = 3;
                    view.rightDisplay.remove(view.displayScreen);
                    view.receiptView.removeAll();
                    view.receiptView.repaint();
                    view.receiptView.revalidate();
                    for (Part part : model.shoppingCarts.get(model.currCart).parts) {
                        view.receiptList(part);
                        for (Part data: model.parts)
                            if(data.equals(part))
                                if(data.quantity >= part.quantity){
                                    data.quantity -= part.quantity;
                                    if(data.quantity == 0) {
                                        JOptionPane.showMessageDialog(view, data.name + " has run out, please resupply");
                                    }
                                    model.tableModel.changeParts(model.parts);
                                    if (model.tableModel.getRowCount() != 0)
                                        model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);

                                    view.finalPrice.getText();

                                    view.cancelBackPanel.removeAll();
                                    view.receiptPanel.add(view.cancelBackPanel, BorderLayout.NORTH);

                                    view.displayScreen = view.receiptPanel;
                                    view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                                    view.rightDisplay.repaint();
                                    view.rightDisplay.revalidate();

                                    view.cart.setEnabled(false);
                                    view.checkout.setEnabled(false);
                                    view.payment.setEnabled(false);
                                    view.receipt.setEnabled(true);
                                }
                    }
                    view.receiptListTotal(model.shoppingCarts.get(model.currCart).finalPrice);                }
            }
        });
        view.printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.cartNum[model.currCart] = 0;
                view.rightDisplay.remove(view.displayScreen);
                view.receiptView.removeAll();
                view.receiptView.repaint();
                view.receiptView.revalidate();
                /* test
                for(int i = 0; i < 5; i++){
                    System.out.println(model.shoppingCarts.get(i).parts);
                }
                */
                CreateFile();
                WriteToFile(view.receiptCtr, model.shoppingCarts.get(model.currCart).parts, Double.parseDouble(view.finalPrice.getText()));

                ShoppingCart currentCart = model.shoppingCarts.get(model.currCart);
                currentCart.parts.clear();
                view.partPrices.clear();
                view.cartViews.get(model.currCart).removeAll();
                view.cancelBackPanel.removeAll();
                java.net.URL imageURL = getClass().getClassLoader().getResource("images/cancel_button.png");
                if (imageURL != null) {
                    ImageIcon originalIcon = new ImageIcon(imageURL);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                    view.cancelOrderButton.setIcon(new ImageIcon(scaledImage));
                } else {
                    //System.err.println("Error: Unable to load cancel button icon");
                    view.cancelOrderButton.setText("Cancel");
                    view.cancelOrderButton.setFont(new Font("Verdana", Font.BOLD, 11));
                }
                view.cancelBackPanel = new JPanel(new GridLayout(1,5));
                view.cancelBackPanel.add(view.cancelOrderButton);
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cartPanel.add(view.cancelBackPanel, BorderLayout.NORTH);

                view.displayScreen = view.cartPanel;
                view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                view.rightDisplay.repaint();
                view.rightDisplay.revalidate();

                view.cart.setEnabled(true);
                view.checkout.setEnabled(false);
                view.payment.setEnabled(false);
                view.receipt.setEnabled(false);


            }
        });
        view.cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.shoppingCarts.get(model.currCart).parts.clear();
                view.cartViews.get(model.currCart).removeAll();
                view.cartViews.get(model.currCart).revalidate();
                view.cartViews.get(model.currCart).repaint();
                //System.out.println("testing");
            }
        });
        view.backButtonChk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.cartNum[model.currCart] = 0;
                //System.out.print("TESTING");
                view.rightDisplay.remove(view.displayScreen);
                view.cancelBackPanel.removeAll();
                view.cancelBackPanel.add(view.cancelOrderButton);
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cartPanel.add(view.cancelBackPanel, BorderLayout.NORTH);
                view.displayScreen = view.cartPanel;
                view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                view.rightDisplay.repaint();
                view.rightDisplay.revalidate();

                view.cart.setEnabled(true);
                view.checkout.setEnabled(false);
                view.payment.setEnabled(false);
                view.receipt.setEnabled(false);
            }
        });

        view.backButtonPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.partPrices.clear();
                view.cartNum[model.currCart] = 1;
                System.out.print("TESTING");
                view.rightDisplay.remove(view.displayScreen);
                view.cancelBackPanel.removeAll();
                view.cancelBackPanel.add(view.backButtonChk);
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.cancelBackPanel.add(new JPanel());
                view.checkoutPanel.add(view.cancelBackPanel, BorderLayout.NORTH);
                view.displayScreen = view.checkoutPanel;
                view.rightDisplay.add(view.displayScreen, BorderLayout.CENTER);
                view.rightDisplay.repaint();
                view.rightDisplay.revalidate();

                view.cart.setEnabled(false);
                view.checkout.setEnabled(true);
                view.payment.setEnabled(false);
                view.receipt.setEnabled(false);
            }
        });

    }
    public void filter() {
        try {
            ArrayList<Part> filtered = new ArrayList<>();
            String text = this.view.searchField.getText().toLowerCase();

            for(Part part : model.parts)
            {
                boolean has = true;
                for (String input: text.split(" "))
                {
                    if (!part.toString().toLowerCase().contains(input))
                    {
                        has = false;
                        break;
                    }
                }
                if (has)
                {
                    filtered.add(part);
                }
            }

            filtered.sort(new Comparator<Part>() {
                @Override
                public int compare(Part o1, Part o2) {
                    int order;
                    if (Objects.requireNonNull(view.order.getSelectedItem()).toString().equals("Ascending"))
                        order = 1;
                    else
                        order = -1;
                    String sortBy = Objects.requireNonNull(view.sortBy.getSelectedItem()).toString();
                    if ("Car Brand".equals(sortBy)) {
                        return o1.carBrand.toLowerCase().compareTo(o2.carBrand.toLowerCase()) * order;
                    } else if ("Car Model".equals(sortBy)) {
                        return o1.carModel.toLowerCase().compareTo(o2.carModel.toLowerCase()) * order;
                    } else if ("Name".equals(sortBy)) {
                        return o1.name.toLowerCase().compareTo(o2.name.toLowerCase()) * order;
                    } else if ("Year".equals(sortBy)) {
                        return Integer.compare(o1.year, o2.year) * order;
                    } else if ("Quantity".equals(sortBy)) {
                        return Integer.compare(o1.quantity, o2.quantity) * order;
                    } else if ("Price".equals(sortBy)) {
                        return Double.compare(o1.price, o2.price) * order;
                    } else if ("isNew".equals(sortBy)) {
                        return Boolean.compare(o1.isNew, o2.isNew) * order;
                    } else {
                        String a = o1.authenticity;
                        String b = o2.authenticity;
                        int result;

                        if (a.equals("ORIGINAL")) {
                            if (b.equals("ORIGINAL")) {
                                result = 0;
                            } else if (b.equals("CLASS A")) {
                                result = 1;
                            } else {
                                result = 1;
                            }
                        } else if (a.equals("CLASS A")) {
                            if (b.equals("ORIGINAL")) {
                                result = -1;
                            } else if (b.equals("CLASS A")) {
                                result = 0;
                            } else {
                                result = 1;
                            }
                        } else {
                            if (b.equals("ORIGINAL") || b.equals("CLASS A")) {
                                result = -1;
                            } else {
                                result = a.toLowerCase().compareTo(b.toLowerCase());
                            }
                        }
                        return result * order;
                    }
                }
            });

            String brand = Objects.requireNonNull(this.view.brandFilter.getSelectedItem()).toString();
            if(!brand.equals("All"))
            {
                ArrayList<Part> filter = new ArrayList<>();
                for (Part part: filtered)
                {
                    if (part.carBrand.equals(brand))
                        filter.add(part);
                }
                filtered = filter;
            }

            String carModel = Objects.requireNonNull(this.view.modelFilter.getSelectedItem()).toString();
            if(!carModel.equals("All"))
            {
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part: filtered)
                {
                    if (!part.carModel.equals(carModel))
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            if ((!this.view.fromYear.getText().isEmpty()) && this.view.fromYear.getBackground().equals(Color.WHITE))
            {
                int year = Integer.parseInt(this.view.fromYear.getText());
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part : filtered)
                {
                    if (part.year < year)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }
            if ((!this.view.toYear.getText().isEmpty()) && this.view.toYear.getBackground().equals(Color.WHITE))
            {
                int year = Integer.parseInt(this.view.toYear.getText());
                ArrayList<Part> filterOut = new ArrayList();
                for (Part part : filtered)
                {
                    if (part.year > year)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            if ((!this.view.fromPrice.getText().isEmpty()) && this.view.fromPrice.getBackground().equals(Color.WHITE))
            {
                double price = Double.parseDouble(this.view.fromPrice.getText());
                ArrayList<Part> filterOut = new ArrayList();
                for (Part part : filtered)
                {
                    if (part.price < price)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            if ((!this.view.toPrice.getText().isEmpty()) && this.view.toPrice.getBackground().equals(Color.WHITE))
            {
                double price = Double.parseDouble(this.view.toPrice.getText());
                ArrayList<Part> filterOut = new ArrayList();
                for (Part part : filtered)
                {
                    if (part.price > price)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            String condition = this.view.newFilter.getSelectedItem().toString();
            if(!condition.equals("All"))
            {
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part: filtered)
                {
                    if(condition.equals("NEW"))
                    {
                        if(part.isNew != true)
                            filterOut.add(part);
                    }
                    else
                    if(part.isNew != false)
                        filterOut.add(part);
                }
                filtered.removeAll(filterOut);
            }

            String authenticity = this.view.authenticityFilter.getSelectedItem().toString();
            if(!authenticity.equals("All"))
            {
                ArrayList<Part> filterOut = new ArrayList<>();
                for (Part part: filtered)
                {
                    if(authenticity.equals("OTHERS"))
                    {
                        if(part.authenticity.equals("ORIGINAL") || part.authenticity.equals("CLASS A"))
                            filterOut.add(part);
                    }
                    else if (authenticity.equals("ORIGINAL"))
                    {
                        if (!part.authenticity.equals("ORIGINAL"))
                            filterOut.add(part);
                    }
                    else
                    {
                        if (!part.authenticity.equals("CLASS A"))
                            filterOut.add(part);
                    }
                }
                filtered.removeAll(filterOut);
            }

            model.tableModel.changeParts(filtered);
            if (model.tableModel.getRowCount() != 0)
                model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
            view.partTable.revalidate();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public void CreateFile ()
    {
        try {
            String directoryPath = "./Receipts/" + view.date + "/";
            File directory = new File(directoryPath);


            //this will make directory for receipts of a new day
            if (!directory.exists()) {
                directory.mkdirs(); // mkdirs() will create parent directories if they don't exist
                System.out.println("Directory created: " + directory.getAbsolutePath());
            }else {
                File[] files = directory.listFiles();
                if (files != null && files.length > 0) {
                    int lastNum = 0;
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".txt")) {
                            String fileName = file.getName().replace(".txt", "");
                            int num = Integer.parseInt(fileName);
                            if (num > lastNum) {
                                lastNum = num;
                            }
                        }
                    }
                    view.receiptCtr = lastNum + 1; // Set the counter to the next number
                }
            }

            File receipt = new File(directory, view.receiptCtr + ".txt");
            if (receipt.createNewFile()) {
                System.out.println("File created: " + receipt.getAbsolutePath());
                view.receiptCtr++;
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    public void WriteToFile(int fileNum,ArrayList<Part> parts, double total ) {
        try {
            FileWriter myWriter = new FileWriter("./Receipts/" + view.date + "/" +  (fileNum-1) + ".txt");
            myWriter.write("---------------------------------------------------------------------\n");
            for(Part part : parts){
                myWriter.write(part + "\n");
            }
            myWriter.write("---------------------------------------------------------------------\n");
            myWriter.write("Total: P" + total);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    public void filter(JTextField searchBar, JTable table)
    {
        ArrayList<Part> filtered = new ArrayList();
        String text = searchBar.getText().toUpperCase();
        for(Part part : model.parts)
        {
            boolean has = true;
            for (String input: text.split(" "))
            {
                if (!part.toString().toUpperCase().contains(input))
                {
                    has = false;
                    break;
                }
            }
            if (has)
                filtered.add(part);
        }
        model.tableModel.changeParts(filtered);
        if (model.tableModel.getRowCount() != 0)
            model.tableModel.fireTableRowsUpdated(0, model.tableModel.getRowCount() - 1);
        table.revalidate();
    }
}