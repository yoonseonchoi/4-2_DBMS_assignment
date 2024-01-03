import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Font;

import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DayCalendar extends JFrame{
	JPanel topPanel = new JPanel();
    JButton prevBtn = new JButton("◀");
    JButton nextBtn = new JButton("▶");
    JLabel yearLbl = new JLabel("YEAR");
    JLabel monthLbl = new JLabel("MONTH");
    JButton logoutBtn = new JButton("Logout");
    JButton addBtn = new JButton("+");
    JButton dayBtn = new JButton("Day");
    JButton weekBtn = new JButton("Week");
    JButton monthBtn = new JButton("Month");

    JComboBox<Integer> yearCombo = new JComboBox<Integer>();
    DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<Integer>();
    JComboBox<Integer> monthCombo = new JComboBox<Integer>();
    DefaultComboBoxModel<Integer> monthModel = new DefaultComboBoxModel<Integer>();

    JPanel centerPanel = new JPanel(new BorderLayout());
	JPanel dayPanel = new JPanel();

    private Calendar now;
    private int year, month, date;

    JPanel leftPanel = new JPanel(new GridLayout(26,1));
    JLabel currentDateLabel = new JLabel();
    JLabel[] timeLabel = new JLabel[26];

	DayCalendar(){
		super("Daily Calendar");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH) + 1;
        date = now.get(Calendar.DATE);
        topPanel.add(logoutBtn);
        topPanel.add(prevBtn);

		for (int i = year - 100; i <= year + 50; i++) {
            yearModel.addElement(i);
        }

        yearCombo.setModel(yearModel);
        yearCombo.setSelectedItem(year);
        topPanel.add(yearLbl);
        topPanel.add(yearCombo);

        for (int i = 1; i <= 12; i++) {
            monthModel.addElement(i);
        }

		monthCombo.setModel(monthModel);
        monthCombo.setSelectedItem(month);
        topPanel.add(monthLbl);
        topPanel.add(monthCombo);
        topPanel.add(nextBtn);
        topPanel.add(addBtn);
        topPanel.add(dayBtn);
        topPanel.add(weekBtn);
        topPanel.add(monthBtn);

		topPanel.setBackground(new Color(204, 204, 255));
        add(topPanel, "North");
    
        timeLabel[0] = new JLabel("<html><body><br></body></html>");
        leftPanel.add(timeLabel[0]);
        for (int i=1; i<26; i++){
            timeLabel[i] = new JLabel(i-1+" :00");
            leftPanel.add(timeLabel[i]);
        }
        centerPanel.add(leftPanel, "West");

        dayPrint(year, month, date);
        centerPanel.add(dayPanel, "Center");
        add(centerPanel, "Center");

		prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now.add(Calendar.DATE, -1);
                updateDate();
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now.add(Calendar.DATE, 1);
                updateDate();
            }
        });

		yearCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedYear = (Integer) yearCombo.getSelectedItem();
                now.set(Calendar.YEAR, selectedYear);
                updateDate();
            }
        });

        monthCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedMonth = (Integer) monthCombo.getSelectedItem();
                now.set(Calendar.MONTH, selectedMonth - 1);
                updateDate();
            }
        });

		monthBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                FamilyCalendar fc = new FamilyCalendar();
                fc.loginBtn.setText("Logout");
                setVisible(false);
            }
        });

        weekBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new WeekCalendar();
                setVisible(false);
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                logoutBtn.setText("Login");
                new FamilyCalendar();
                setVisible(false);
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JFrame scheduleFrame = new JFrame("Add Schedule");
		        scheduleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        JPanel schedulePanel = new JPanel(new BorderLayout());

		        JPanel addPanel = new JPanel(new GridLayout(3, 2));
		        JTextField contentField = new JTextField(20);
		        JTextField startDateField = new JTextField(20);
		        JTextField endDateField = new JTextField(20);

		        addPanel.add(new JLabel("Content: "));
		        addPanel.add(contentField);
		        addPanel.add(new JLabel("Start: "));
		        addPanel.add(startDateField);
		        addPanel.add(new JLabel("End: "));
		        addPanel.add(endDateField);

		        JButton saveBtn = new JButton("Save");
		        saveBtn.addActionListener(new ActionListener(){
			        @Override
			        public void actionPerformed(ActionEvent e){
				        String content = contentField.getText();
				        String startDateStr = startDateField.getText();
				        String endDateStr = endDateField.getText();

				        try{
					        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					        Date startDate = dateFormat.parse(startDateStr);
					        Date endDate = dateFormat.parse(endDateStr);
					        scheduleFrame.dispose();
				        } catch (ParseException ex){
					        ex.printStackTrace();
					        JOptionPane.showMessageDialog(scheduleFrame, "Invalid Date Format. Please enter 'yyyy-MM-dd HH:mm' format.");
				        }
			        }              
		        });

		        schedulePanel.add(addPanel);
		        schedulePanel.add(saveBtn, BorderLayout.SOUTH);
		        scheduleFrame.add(schedulePanel);
		        scheduleFrame.pack();
		        scheduleFrame.setVisible(true);
            }
        });

        setSize(1200,900);
        setVisible(true);
    }

    private void updateDate() {
        yearCombo.setSelectedItem(now.get(Calendar.YEAR));
        monthCombo.setSelectedItem(now.get(Calendar.MONTH) + 1);
        dayPrint(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
    }

    public void dayPrint(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.MONTH, m - 1);
        cal.set(Calendar.DAY_OF_MONTH, d);

        dayPanel.removeAll();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        JLabel dayLabel = new JLabel(Integer.toString(day), JLabel.CENTER);
        dayLabel.setFont(new Font("Serif", Font.BOLD, 24));
        dayPanel.add(dayLabel, BorderLayout.NORTH);
        dayPanel.revalidate();
        dayPanel.repaint();
    }
}