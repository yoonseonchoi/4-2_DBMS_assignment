import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class WeekCalendar extends JFrame {
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
    JPanel titlePanel = new JPanel(new GridLayout(1, 7));
    String titleStr[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    JPanel weekPane = new JPanel(new GridLayout(0, 7));

    Calendar now;
    int year, month, date;

    WeekCalendar() {
        super("Weekly Calendar");
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

        titlePanel.setBackground(Color.white);

        for (int i = 0; i < titleStr.length; i++) {
            JLabel lbl = new JLabel(titleStr[i], JLabel.CENTER);
            if (i == 0) {
                lbl.setForeground(Color.red);
            } else if (i == 6) {
                lbl.setForeground(Color.blue);
            }
            titlePanel.add(lbl);
        }
        centerPanel.add(titlePanel, "North");

        weekPrint(year, month, date);
        centerPanel.add(weekPane, "Center");
        add(centerPanel, "Center");

        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now.add(Calendar.DATE, -7); // 현재 날짜에서 7일 이전으로 이동
                updateWeek();
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now.add(Calendar.DATE, 7); // 현재 날짜에서 7일 다음으로 이동
                updateWeek();
            }
        });

        yearCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedYear = (Integer) yearCombo.getSelectedItem();
                now.set(Calendar.YEAR, selectedYear);
                updateWeek();
            }
        });

        monthCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedMonth = (Integer) monthCombo.getSelectedItem();
                now.set(Calendar.MONTH, selectedMonth - 1); 
                updateWeek();
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

        dayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new DayCalendar();
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

        setSize(1200, 900);
        setVisible(true);
    }

    private void updateWeek() {
        yearCombo.setSelectedItem(now.get(Calendar.YEAR));
        monthCombo.setSelectedItem(now.get(Calendar.MONTH) + 1);
        weekPrint(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
    }

    public void weekPrint(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, y);
        cal.set(Calendar.MONTH, m - 1);
        cal.set(Calendar.DAY_OF_MONTH, d);
    
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
    
        weekPane.removeAll();
    
        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
            int day = cal.get(Calendar.DAY_OF_MONTH);
            JLabel dayLabel = new JLabel(Integer.toString(day), JLabel.CENTER);
            dayPanel.add(dayLabel, BorderLayout.NORTH);
    
            dayPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);
    
            weekPane.add(dayPanel);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        weekPane.revalidate();
        weekPane.repaint();
    }
}