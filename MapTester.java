import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MapTester extends JFrame {

    private final HashMap<String, String> phoneBook = new HashMap<>();

    private JTextField nameField;
    private JTextField numberField;
    private JTextField searchField;
    private JTextArea resultArea;
    private DefaultTableModel tableModel;
    private StarryBackground background;

    public MapTester() {
        setTitle("Moonlight Phone Book");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(createBackground());
    }

    private JPanel createBackground() {
        background = new StarryBackground();
        background.setLayout(new BorderLayout(15, 15));
        background.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("☾  MOONLIGHT PHONE BOOK  ☽",
                SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(225, 235, 255));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        background.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(15, 15));
        center.setOpaque(false);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 12, 12));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(130, 160, 220)),
                "Contact Manager",
                0, 0,
                new Font("SansSerif", Font.BOLD, 15),
                Color.WHITE));

        nameField = createField();
        numberField = createField();
        searchField = createField();

        inputPanel.add(createLabel("Contact Name:"));
        inputPanel.add(nameField);
        inputPanel.add(createLabel("Phone Number:"));
        inputPanel.add(numberField);

        JButton addButton = createButton("✚ Add Contact");
        JButton searchButton = createButton("⌕ Search");
        JButton showAllButton = createButton("☷ Show All");
        JButton deleteButton = createButton("✖ Delete Contact");

        inputPanel.add(addButton);
        inputPanel.add(searchButton);
        inputPanel.add(createLabel("Search Name:"));
        inputPanel.add(searchField);
        inputPanel.add(showAllButton);
        inputPanel.add(deleteButton);

        center.add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"Name", "Phone Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(27);
        table.setBackground(new Color(20, 30, 65));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(80, 100, 150));
        table.setSelectionBackground(new Color(70, 95, 155));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(45, 60, 105));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(20, 30, 65));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(130, 160, 220)),
                "Saved Contacts",
                0, 0,
                new Font("SansSerif", Font.BOLD, 15),
                Color.WHITE));

        center.add(scrollPane, BorderLayout.CENTER);

        resultArea = new JTextArea(2, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultArea.setForeground(new Color(225, 235, 255));
        resultArea.setOpaque(false);
        resultArea.setText("Welcome! Add a contact under the moonlight.");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        bottom.add(resultArea, BorderLayout.CENTER);

        background.add(center, BorderLayout.CENTER);
        background.add(bottom, BorderLayout.SOUTH);

        addButton.addActionListener(e -> enterNumber());
        searchButton.addActionListener(e -> lookupNumber());
        showAllButton.addActionListener(e -> showAllContacts());
        deleteButton.addActionListener(e -> deleteContact());

        nameField.addActionListener(e -> numberField.requestFocusInWindow());
        numberField.addActionListener(e -> enterNumber());
        searchField.addActionListener(e -> lookupNumber());

        refreshTable();

        return background;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBackground(new Color(235, 240, 255));
        field.setForeground(new Color(20, 30, 60));
        field.setCaretColor(Color.BLUE);
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(new Color(65, 85, 145));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(105, 130, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(65, 85, 145));
            }
        });

        return button;
    }

    // Add or update a contact using HashMap.put()
    public void enterNumber(String name, String number) {
        phoneBook.put(name, number);
    }

    // Retrieve a contact using HashMap.get()
    public String lookupNumber(String name) {
        return phoneBook.get(name);
    }

    private void enterNumber() {
        String name = nameField.getText().trim();
        String number = numberField.getText().trim();

        if (name.isEmpty() || number.isEmpty()) {
            showMessage("Please enter both a contact name and phone number.");
            return;
        }

        if (!number.matches("[+0-9() -]+")) {
            showMessage("Please enter a valid phone number.");
            return;
        }

        boolean updated = phoneBook.containsKey(name);

        enterNumber(name, number);
        refreshTable();

        if (updated) {
            showMessage("Contact updated: " + name);
        } else {
            showMessage("Contact added successfully: " + name);
        }

        nameField.setText("");
        numberField.setText("");
        nameField.requestFocusInWindow();
    }

    private void lookupNumber() {
        String name = searchField.getText().trim();

        if (name.isEmpty()) {
            showMessage("Please enter a name to search.");
            return;
        }

        String number = lookupNumber(name);

        if (number != null) {
            showMessage("Found " + name + " — Phone: " + number);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString()
                        .equalsIgnoreCase(name)) {
                    // Display the matching row in the table.
                    resultArea.setText(
                            "Found " + name + " — Phone: " + number);
                    break;
                }
            }
        } else {
            showMessage("No contact found for: " + name);
        }
    }

    private void showAllContacts() {
        refreshTable();
        showMessage("Total saved contacts: " + phoneBook.size());
    }

    private void deleteContact() {
        String name = searchField.getText().trim();

        if (name.isEmpty()) {
            showMessage("Enter the contact name in the Search Name field.");
            return;
        }

        if (phoneBook.remove(name) != null) {
            refreshTable();
            showMessage("Contact deleted: " + name);
        } else {
            showMessage("No contact found for: " + name);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
            tableModel.addRow(new Object[]{
                    entry.getKey(),
                    entry.getValue()
            });
        }
    }

    private void showMessage(String message) {
        resultArea.setText(message);
    }

    // Animated night sky with a glowing crescent moon and twinkling stars.
    private static class StarryBackground extends JPanel {

        private static class Star {
            int x;
            int y;
            int size;
            float brightness;
            float speed;
            boolean increasing;

            Star(int x, int y, int size, Random random) {
                this.x = x;
                this.y = y;
                this.size = size;
                this.brightness = random.nextFloat();
                this.speed = 0.01f + random.nextFloat() * 0.035f;
                this.increasing = random.nextBoolean();
            }

            void twinkle() {
                brightness += increasing ? speed : -speed;

                if (brightness >= 1.0f) {
                    brightness = 1.0f;
                    increasing = false;
                } else if (brightness <= 0.15f) {
                    brightness = 0.15f;
                    increasing = true;
                }
            }
        }

        private final Star[] stars;
        private final Timer animationTimer;
        private float moonGlow = 0.5f;
        private boolean glowIncreasing = true;

        StarryBackground() {
            setBackground(new Color(8, 14, 40));

            Random random = new Random();
            stars = new Star[140];

            for (int i = 0; i < stars.length; i++) {
                stars[i] = new Star(
                        random.nextInt(900),
                        random.nextInt(700),
                        1 + random.nextInt(3),
                        random
                );
            }

            animationTimer = new Timer(40, e -> {
                for (Star star : stars) {
                    star.twinkle();
                }

                moonGlow += glowIncreasing ? 0.012f : -0.012f;

                if (moonGlow >= 1.0f) {
                    moonGlow = 1.0f;
                    glowIncreasing = false;
                } else if (moonGlow <= 0.4f) {
                    moonGlow = 0.4f;
                    glowIncreasing = true;
                }

                repaint();
            });

            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            // Gradient night sky
            GradientPaint sky = new GradientPaint(
                    0, 0, new Color(5, 10, 32),
                    width, height, new Color(35, 42, 88)
            );

            g2.setPaint(sky);
            g2.fillRect(0, 0, width, height);

            // Twinkling stars
            for (Star star : stars) {
                int x = star.x % Math.max(width, 1);
                int y = star.y % Math.max(height, 1);

                int alpha = (int) (star.brightness * 255);

                g2.setColor(new Color(220, 230, 255, alpha));
                g2.fillOval(x, y, star.size, star.size);

                if (star.size == 3 && star.brightness > 0.75f) {
                    g2.drawLine(x - 3, y + 1, x + 5, y + 1);
                    g2.drawLine(x + 1, y - 3, x + 1, y + 5);
                }
            }

            // Moon glow
            int moonX = width - 125;
            int moonY = 90;
            int moonSize = 85;

            for (int i = 5; i >= 1; i--) {
                int alpha = (int) (12 * moonGlow);
                g2.setColor(new Color(180, 205, 255, alpha));
                int glowSize = moonSize + i * 18;
                g2.fillOval(
                        moonX - (glowSize - moonSize) / 2,
                        moonY - (glowSize - moonSize) / 2,
                        glowSize,
                        glowSize
                );
            }

            // Crescent moon
            g2.setColor(new Color(255, 246, 205));
            g2.fillOval(moonX, moonY, moonSize, moonSize);

            g2.setColor(new Color(17, 24, 57));
            g2.fillOval(moonX + 25, moonY - 12, moonSize, moonSize);

            // Small decorative stars around the moon
            g2.setColor(new Color(255, 240, 190));
            g2.fillOval(moonX - 20, moonY + 25, 4, 4);
            g2.fillOval(moonX + 90, moonY + 65, 3, 3);
            g2.fillOval(moonX + 20, moonY - 15, 3, 3);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MapTester app = new MapTester();
            app.setVisible(true);
        });
    }
}