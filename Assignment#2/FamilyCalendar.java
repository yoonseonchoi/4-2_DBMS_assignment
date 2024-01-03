import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.text.ParseException;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FamilyCalendar extends JFrame implements ActionListener{
    // North
    JPanel topPanel = new JPanel();
	JButton prevBtn = new JButton("◀");
    JButton nextBtn = new JButton("▶");
    JLabel yearLbl = new JLabel("YEAR");
    JLabel monthLbl = new JLabel("MONTH");
	JButton loginBtn = new JButton("Login");
	JButton signUpBtn = new JButton("Sign Up");
	JButton addBtn = new JButton("+");
	JButton dayBtn = new JButton("Day");
	JButton weekBtn = new JButton("Week");
	JButton monthBtn = new JButton("Month");

    JComboBox<Integer> yearCombo = new JComboBox<Integer>();
	DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<Integer>();
	JComboBox<Integer> monthCombo = new JComboBox<Integer>();
	DefaultComboBoxModel<Integer> monthModel = new DefaultComboBoxModel<Integer>();
	
    //Center
    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel titlePanel = new JPanel(new GridLayout(1, 7));
	String titleStr[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	JPanel datePane = new JPanel(new GridLayout(0, 7));		

	Calendar now;
	int year, month, date;

	public FamilyCalendar() {
		super("Family Calendar");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH)+1;
		date = now.get(Calendar.DATE);
		topPanel.add(signUpBtn);
		topPanel.add(loginBtn);
		topPanel.add(prevBtn);
    
		for(int i=year-100; i<=year+50; i++){
			yearModel.addElement(i);
		}

		yearCombo.setModel(yearModel);
		yearCombo.setSelectedItem(year);
		topPanel.add(yearLbl);
		topPanel.add(yearCombo);	

		for(int i=1; i<=12; i++){
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

		//Center
		titlePanel.setBackground(Color.white);

		for(int i=0; i<titleStr.length; i++){
			JLabel lbl = new JLabel(titleStr[i], JLabel.CENTER);
			if(i == 0){
				lbl.setForeground(Color.red);
			}else if(i == 6){
				lbl.setForeground(Color.blue);
			}
			titlePanel.add(lbl);
		}

		centerPanel.add(titlePanel, "North");

		//날짜 출력
		dayPrint(year, month);
		centerPanel.add(datePane, "Center");
		add(centerPanel, "Center");
		setSize(1200, 900);
        setVisible(true);		

		prevBtn.addActionListener(this);
		nextBtn.addActionListener(this);
		yearCombo.addActionListener(this);
		monthCombo.addActionListener(this);

		loginBtn.addActionListener(this);
		signUpBtn.addActionListener(this);

		addBtn.addActionListener(this);
		dayBtn.addActionListener(this);
		weekBtn.addActionListener(this);
		monthBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();

		if(obj instanceof JButton){
			JButton eventBtn = (JButton)obj;
			int yy = (Integer)yearCombo.getSelectedItem();
			int mm = (Integer)monthCombo.getSelectedItem();
			if(eventBtn.equals(prevBtn)){
				if(mm==1){
					yy--; mm=12;
				}else{
					mm--;
				}				
			}else if(eventBtn.equals(nextBtn)){
				if(mm==12){
					yy++; mm=1;
				}else{
					mm++;
				}
			}
			yearCombo.setSelectedItem(yy);
			monthCombo.setSelectedItem(mm);
		}else if(obj instanceof JComboBox){
			createDayStart();
		}
		
		if(obj == loginBtn){
	 		if(loginBtn.getText().equals("Login")){
				loginWindow();
				loginBtn.setText("Logout");
			}else if(loginBtn.getText().equals("Logout")){
				loginBtn.setText("Login");
			}
	 	}

		if(obj == signUpBtn){
			if(loginBtn.getText().equals("Login")){
				signUpWindow();
			}
			else if(loginBtn.getText().equals("Logout")){
				JOptionPane.showMessageDialog(null, "Please Logout First", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if(obj == addBtn){
			if(loginBtn.getText().equals("Logout")){
				addSchedule();
			}
			else if(loginBtn.getText().equals("Login")){
				JOptionPane.showMessageDialog(null, "Please Login First", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if(obj == dayBtn){
			if(loginBtn.getText().equals("Logout")){
				new DayCalendar();
				setVisible(false);
			}
			else if(loginBtn.getText().equals("Login")){
				JOptionPane.showMessageDialog(null, "Please Login First", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if(obj == weekBtn){
			if(loginBtn.getText().equals("Logout")){
				new WeekCalendar();
				setVisible(false);
			}
			else if(loginBtn.getText().equals("Login")){
				JOptionPane.showMessageDialog(null, "Please Login First", "Login Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void signUpWindow(){
		JFrame signUpFrame = new JFrame("Sign Up");
		signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel signUpPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new GridLayout(4,2));

		JTextField nameField = new JTextField(20);
		JTextField idField = new JTextField(20);
		JPasswordField pwField = new JPasswordField(20);
		JPasswordField pwConfirmField = new JPasswordField(20);

		inputPanel.add(new JLabel("Name: "));
		inputPanel.add(nameField);
		inputPanel.add(new JLabel("ID: "));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("Password: "));
		inputPanel.add(pwField);
		inputPanel.add(new JLabel("Confirm Password: "));
		inputPanel.add(pwConfirmField);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton signUpButton = new JButton("Sign Up");
		buttonPanel.add(signUpButton);

		signUpPanel.add(inputPanel, BorderLayout.CENTER);
		signUpPanel.add(buttonPanel, BorderLayout.SOUTH);

		signUpFrame.add(signUpPanel);
		signUpFrame.pack();
		signUpFrame. setSize(500, 600);
    	signUpFrame.setVisible(true);

		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				char[] pw = pwField.getPassword();
				char[] pwConfirm = pwConfirmField.getPassword();

				if(new String(pw).equals(new String(pwConfirm))){
					signUpFrame.dispose();
				}
				else{
					JOptionPane.showMessageDialog(null, "Confirm Password Again!", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public void loginWindow(){
		JFrame loginFrame = new JFrame("Login");
		loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel loginPanel = new JPanel(new BorderLayout());

		JPanel inputPanel = new JPanel(new GridLayout(2,2));
		JTextField idField = new JTextField(20);
    	JPasswordField pwField = new JPasswordField(20);
		inputPanel.add(new JLabel("ID:"));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("Password:"));
		inputPanel.add(pwField);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton loginButton = new JButton("Login");
		buttonPanel.add(loginButton);

		loginPanel.add(inputPanel, BorderLayout.CENTER);
    	loginPanel.add(buttonPanel, BorderLayout.SOUTH);

    	loginFrame.add(loginPanel);
    	loginFrame.pack();
		loginFrame. setSize(500, 200);
    	loginFrame.setVisible(true);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText();
				char[] pw = pwField.getPassword();

				if(id.equals("1234") && new String(pw).equals("1234")){
					loginFrame.dispose();
				}
				else{
					JOptionPane.showMessageDialog(null, "Failed Login!", "Login Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public void addSchedule(){
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

	public void createDayStart(){
		datePane.setVisible(false);
		datePane.removeAll();
		dayPrint((Integer)yearCombo.getSelectedItem(), (Integer)monthCombo.getSelectedItem());
		datePane.setVisible(true);			
	}	

	public void dayPrint(int y, int m){
		Calendar cal = Calendar.getInstance();
		cal.set(y, m-1, 1);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		for(int i=1; i<week; i++){
			datePane.add(new JLabel(" "));
		}

		for(int i=1; i<=lastDate; i++){
			JButton dayBtn = new JButton(String.valueOf(i));
			dayBtn.setHorizontalAlignment(JLabel.LEFT);
			dayBtn.setVerticalAlignment(JLabel.TOP);
			dayBtn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
			cal.set(y, m-1, i);
			int outWeek = cal.get(Calendar.DAY_OF_WEEK);
			if (outWeek == 1){
				dayBtn.setForeground(Color.red);
			}else if(outWeek==7){
				dayBtn.setForeground(Color.BLUE);
			}

			dayBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					if(loginBtn.getText().equals("Login")){
						JOptionPane.showMessageDialog(null, "Please Login First", "Login Error", JOptionPane.ERROR_MESSAGE);
					}else if(loginBtn.getText().equals("Logout")){
						JFrame schFrame = new JFrame("Schedule");
		        		schFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        		JPanel schPanel = new JPanel(new BorderLayout());					
						JPanel btnPanel = new JPanel();

						JButton updateBtn = new JButton("Update");
						updateBtn.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								addSchedule();
							}
						});

						JButton deleteBtn = new JButton("Delete");
						deleteBtn.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								schFrame.dispose();
							}
						});

		        		JButton okBtn = new JButton("OK");
		        		okBtn.addActionListener(new ActionListener() {
			        		@Override
			        		public void actionPerformed(ActionEvent e){
				        		schFrame.dispose();
			        		}
						});
						
						btnPanel.add(updateBtn);
						btnPanel.add(deleteBtn);
						btnPanel.add(okBtn);
						schPanel.add(btnPanel, BorderLayout.SOUTH);
		        		schFrame.add(schPanel);
		        		schFrame. setSize(400, 500);
    	        		schFrame.setVisible(true);
					}
				}
			});
			datePane.add(dayBtn);
		}
	}

	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "yschoi", "1008")){
			System.out.println("Connection established succesfully");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		new FamilyCalendar();
	}
}