import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.BorderUIResource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class FamilyCalendar extends JFrame implements ActionListener{
    // North
    JPanel topPanel = new JPanel();
	JButton prevBtn = new JButton("◀");
    JButton nextBtn = new JButton("▶");
    JLabel yearLbl = new JLabel("YEAR");
    JLabel monthLbl = new JLabel("MONTH");
	JButton loginBtn = new JButton("Login");
	JButton signUpBtn = new JButton("Sign Up");
	JButton modifyBtn = new JButton("Modify");
	JButton addBtn = new JButton("+");
	JButton searchBtn = new JButton("Search");
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

	private String uid;
	private String eid;
	private String rid;

	public FamilyCalendar() {
		super("Family Calendar");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		now = Calendar.getInstance();
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH)+1;
		date = now.get(Calendar.DATE);
		topPanel.add(signUpBtn);
		topPanel.add(modifyBtn);
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
		topPanel.add(searchBtn);
		topPanel.add(monthBtn);
		topPanel.add(weekBtn);
		topPanel.add(dayBtn);

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
		modifyBtn.addActionListener(this);
		signUpBtn.addActionListener(this);

		addBtn.addActionListener(this);
		searchBtn.addActionListener(this);
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

		if (obj == modifyBtn){
			if (loginBtn.getText().equals("Logout")){
				modifyWindow();
			}
			else if(loginBtn.getText().equals("Login")){
				JOptionPane.showMessageDialog(null, "Please Login First", "Login Error", JOptionPane.ERROR_MESSAGE);
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

		if(obj == searchBtn){
			if(loginBtn.getText().equals("Logout")){
				searchSchedule();
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
		JPanel inputPanel = new JPanel(new GridLayout(3,2));

		JTextField nameField = new JTextField(20);
		JTextField idField = new JTextField(20);
		JPasswordField pwField = new JPasswordField(20);

		inputPanel.add(new JLabel("Name: "));
		inputPanel.add(nameField);
		inputPanel.add(new JLabel("ID: "));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("Password: "));
		inputPanel.add(pwField);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton signUpButton = new JButton("Sign Up");
		buttonPanel.add(signUpButton);

		signUpPanel.add(inputPanel, BorderLayout.CENTER);
		signUpPanel.add(buttonPanel, BorderLayout.SOUTH);

		signUpFrame.add(signUpPanel);
		signUpFrame.pack();
		signUpFrame.setSize(500, 200);
    	signUpFrame.setVisible(true);

		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				String sqlSignUp = "insert into Users (uid, pw, name) values (?, ?, ?)";
				try(Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
					PreparedStatement preparedStatement = conn.prepareStatement(sqlSignUp)) {
						preparedStatement.setString(1, idField.getText());
            			preparedStatement.setString(2, new String(pwField.getPassword()));
						preparedStatement.setString(3, nameField.getText());
						preparedStatement.executeUpdate();
						System.out.print("record inserted successfully\n:");
					} catch (SQLException e) {
						System.out.print(e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				signUpFrame.dispose();
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
		loginFrame.setSize(500, 190);
    	loginFrame.setVisible(true);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText();
				char[] pw = pwField.getPassword();
				String sqlLogin = "select uid, pw from Users where uid=? and pw=?";

				try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
					PreparedStatement preparedStatement = connection.prepareStatement(sqlLogin)) {
					preparedStatement.setString(1, id);
					preparedStatement.setString(2, new String(pw));

					try(ResultSet resultSet = preparedStatement.executeQuery()) {
						if (resultSet.next()) {
							uid = resultSet.getString("uid");
							loginBtn.setText("Logout");
							loginFrame.dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Failed Login!", "Login Error", JOptionPane.ERROR_MESSAGE);
					    }
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public void modifyWindow() {
		JFrame modifyFrame = new JFrame("Modify Your Password");
		modifyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel modifyPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new GridLayout(2,2));
		 
		JTextField idField = new JTextField(20);
		JPasswordField newPwField = new JPasswordField(20);

		inputPanel.add(new JLabel("Your ID: "));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("New Password: "));
		inputPanel.add(newPwField);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton modifyButton = new JButton("Modify");
		buttonPanel.add(modifyButton);

		modifyPanel.add(inputPanel, BorderLayout.CENTER);
		modifyPanel.add(buttonPanel, BorderLayout.SOUTH);

		modifyFrame.add(modifyPanel);
    	modifyFrame.pack();
		modifyFrame.setSize(500, 190);
    	modifyFrame.setVisible(true);

		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = idField.getText();
				char[] newPw = newPwField.getPassword();
				String sqlModify = "update users set pw=? where uid=?";

				if (!id.equals(uid)) {
					JOptionPane.showMessageDialog(null, "Wrong ID!", "Login Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
					PreparedStatement preparedStatement = connection.prepareStatement(sqlModify)) {
					preparedStatement.setString(1, new String(newPw));
					preparedStatement.setString(2, uid);
					preparedStatement.executeLargeUpdate();
					loginBtn.setText("Login");
					modifyFrame.dispose();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public void addSchedule(){
		JFrame scheduleFrame = new JFrame("Add Schedule");
		scheduleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel schedulePanel = new JPanel(new BorderLayout());

		JPanel addPanel = new JPanel(new GridLayout(8, 2));
		JTextField titleField = new JTextField(20);
		JTextField s_DateField = new JTextField(20);
		JTextField e_DateField = new JTextField(20);
		JTextField s_TimeField = new JTextField(20);
		JTextField e_TimeField = new JTextField(20);
		JTextField locationField = new JTextField(20);
		JTextField timeFrameField = new JTextField(5);
		JTextField intervalField = new JTextField(5);

		addPanel.add(new JLabel("Title: "));
		addPanel.add(titleField);
		addPanel.add(new JLabel("Start Date (yyyy-MM-dd): "));
		addPanel.add(s_DateField);
		addPanel.add(new JLabel("End Date (yyyy-MM-dd): "));
		addPanel.add(e_DateField);
		addPanel.add(new JLabel("Start Time (hh:mm:ss): "));
		addPanel.add(s_TimeField);
		addPanel.add(new JLabel("End Time (hh:mm:ss): "));
		addPanel.add(e_TimeField);
		addPanel.add(new JLabel("Location: "));
		addPanel.add(locationField);
		addPanel.add(new JLabel("Remind Time Frame: "));
		addPanel.add(timeFrameField);
		addPanel.add(new JLabel("Remind Interval: "));
		addPanel.add(intervalField);

		JButton saveBtn = new JButton("Save");

		schedulePanel.add(addPanel);
		schedulePanel.add(saveBtn, BorderLayout.SOUTH);
		scheduleFrame.add(schedulePanel);
		scheduleFrame.pack();
		scheduleFrame.setVisible(true);

		saveBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String title = titleField.getText();
				String s_DateStr = s_DateField.getText();
				String e_DateStr = e_DateField.getText();
				String s_TimeStr = s_TimeField.getText();
				String e_TimeStr = e_TimeField.getText();
				String location = locationField.getText();
				String timeFrameStr = timeFrameField.getText();
				String intervalStr = intervalField.getText();

				Date s_Date = Date.valueOf(s_DateStr);
				Date e_Date = Date.valueOf(e_DateStr);
				Time s_Time = Time.valueOf(s_TimeStr);
				Time e_Time = Time.valueOf(e_TimeStr);

				Integer timeFrame = Integer.valueOf(timeFrameStr);
				Integer interval = Integer.valueOf(intervalStr);

				String sqlAddEvent = "insert into Events (title, s_date, e_date, s_time, e_time, location) values (?, ?, ?, ?, ?, ?)";
				String sqlAddRemind = "insert into Reminds (frame, interval, eid) values (?, ?, ?)";
				String sqlEid = "select eid from events where title=? and s_date=?";
				String sqlAddGenerates = "insert into Generates (uid, eid) values (?, ?)";

				try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
					PreparedStatement preparedStatement = connection.prepareStatement(sqlAddEvent)) {				
					preparedStatement.setString(1, title);
					preparedStatement.setDate(2, s_Date);
					preparedStatement.setDate(3, e_Date);
					preparedStatement.setTime(4, s_Time);
					preparedStatement.setTime(5, e_Time);
					preparedStatement.setString(6, location);
					preparedStatement.executeUpdate();

					try (PreparedStatement eidStatement = connection.prepareStatement(sqlEid)) {
						eidStatement.setString(1, title);
						eidStatement.setDate(2, s_Date);

						try (ResultSet generatedKeys = eidStatement.executeQuery()) {
							if (generatedKeys.next()) {
								eid = generatedKeys.getString("eid");

								try (PreparedStatement remindsStatement = connection.prepareStatement(sqlAddRemind)) {
									remindsStatement.setInt(1, timeFrame);
									remindsStatement.setInt(2, interval);
									remindsStatement.setString(3, eid);
									remindsStatement.executeUpdate();
								} catch (SQLException ex) {
									ex.printStackTrace();
								}

								try (PreparedStatement generatesStatement = connection.prepareStatement(sqlAddGenerates)) {
									generatesStatement.setString(1, uid);
									generatesStatement.setString(2, eid);
									generatesStatement.executeUpdate();
								} catch (SQLException ex) {
									ex.printStackTrace();
								}								
							}
						}
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				scheduleFrame.dispose();
			}
		});
	}

	public void searchSchedule(){
		JFrame searchFrame = new JFrame("Search Schedule");
		searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel searchPanel = new JPanel(new BorderLayout());

		JPanel lookUpPanel = new JPanel(new GridLayout(1,3));
		JTextField searchField = new JTextField();
		JButton searchButton = new JButton("Search");
		JPanel resultPanel = new JPanel(new GridLayout(6,1));

		lookUpPanel.add(new JLabel("Enter Date (yyyy-MM-dd): "));
		lookUpPanel.add(searchField);
		lookUpPanel.add(searchButton);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchDateStr = searchField.getText();
				Date searchDate = Date.valueOf(searchDateStr);

				String sqlSearch = "select title, s_date, s_time, e_date, e_time, location from events e, generates g where e.eid=g.eid and uid=? and s_date=?";
				
				try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
				    PreparedStatement preparedStatement = connection.prepareStatement(sqlSearch)){
					preparedStatement.setString(1, uid);
					preparedStatement.setDate(2, searchDate);

					try (ResultSet resultSet = preparedStatement.executeQuery()) {
						if (resultSet.next()) {
							String title = resultSet.getString("title");
							Date s_Date = resultSet.getDate("s_date");
							Time s_Time = resultSet.getTime("s_time");
							Date e_Date = resultSet.getDate("e_date");
							Time e_Time = resultSet.getTime("e_time");
							String location = resultSet.getString("location");

							String s_DateStr = s_Date.toString();
							String s_TimeStr = s_Time.toString();
							String e_DateStr = e_Date.toString();
							String e_TimeStr = e_Time.toString();

							resultPanel.removeAll();
							resultPanel.add(new JLabel("Title: " + title));
							resultPanel.add(new JLabel("Start Date: " + s_DateStr));
							resultPanel.add(new JLabel("Start Time: " + s_TimeStr));
							resultPanel.add(new JLabel("End Date: " + e_DateStr));
							resultPanel.add(new JLabel("End Time: " + e_TimeStr));
							resultPanel.add(new JLabel("Location: " + location));
							resultPanel.revalidate();
							resultPanel.repaint();

							System.out.println(title);
						} else {
							resultPanel.removeAll();
							resultPanel.add(new JLabel("No Event"));
							resultPanel.revalidate();
							resultPanel.repaint();
						} 
					}catch (SQLException ex){
						ex.printStackTrace();
					}
				}catch (SQLException ex){
						ex.printStackTrace();
				}
			}
		});

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				searchFrame.dispose();
			}
		});

		searchPanel.add(lookUpPanel, BorderLayout.NORTH);
		searchPanel.add(resultPanel, BorderLayout.CENTER);
		searchFrame.add(okButton, BorderLayout.SOUTH);
		searchFrame.add(searchPanel);
		searchFrame.setSize(700,500);
		searchFrame.setVisible(true);
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

			String yStr = Integer.toString(y);
			String mStr = Integer.toString(m);
			String btnDayStr = yStr + '-' + mStr + '-' + String.valueOf(i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date utilDay = null;
			try {
				utilDay = sdf.parse(btnDayStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			java.sql.Date btnDay = new java.sql.Date(utilDay.getTime());

			dayBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
					if(loginBtn.getText().equals("Login")){
						JOptionPane.showMessageDialog(null, "Please Login First", "Login Error", JOptionPane.ERROR_MESSAGE);
					}else if(loginBtn.getText().equals("Logout")){
						JFrame schFrame = new JFrame("Schedule");
		        		schFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        		JPanel schPanel = new JPanel(new BorderLayout());
						JPanel schAddPanel = new JPanel(new GridLayout(6,1));
						String sqlSchedule = "select title, s_date, s_time, e_date, e_time, location from events, generates where events.eid=generates.eid and uid=? and s_date=?";
						
						try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
							PreparedStatement preparedStatement = connection.prepareStatement(sqlSchedule)) {
								preparedStatement.setString(1, uid);
								preparedStatement.setDate(2, btnDay);
						
								try (ResultSet resultSet = preparedStatement.executeQuery()) {
									if (resultSet.next()) {
										String title = resultSet.getString("title");
										Date s_Date = resultSet.getDate("s_date");
										Time s_Time = resultSet.getTime("s_time");
										Date e_Date = resultSet.getDate("e_date");
										Time e_Time = resultSet.getTime("e_time");
										String location = resultSet.getString("location");

										String s_DateStr = s_Date.toString();
										String s_TimeStr = s_Time.toString();
										String e_DateStr = e_Date.toString();
										String e_TimeStr = e_Time.toString();

										if (btnDay.equals(s_Date)) {
											schAddPanel.add(new JLabel("Title: " + title));
											schAddPanel.add(new JLabel("Start Date: " + s_DateStr));
											schAddPanel.add(new JLabel("Start Time: " + s_TimeStr));
											schAddPanel.add(new JLabel("End Date: " + e_DateStr));
											schAddPanel.add(new JLabel("End Time: " + e_TimeStr));
											schAddPanel.add(new JLabel("Location: " + location));
										}
									}
								} catch (SQLException ex) {
									ex.printStackTrace();
								}
							} catch (SQLException ex) {
								ex.printStackTrace();
							}

						JPanel btnPanel = new JPanel();
						JButton updateBtn = new JButton("Update");
						updateBtn.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								schFrame.dispose();
								JFrame updateFrame = new JFrame("Update Schedule");
								updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								JPanel modifyPanel = new JPanel(new BorderLayout());
								JPanel updatePanel = new JPanel(new GridLayout(8,2));
								JTextField titleField = new JTextField(20);
								JTextField s_DateField = new JTextField(20);
								JTextField e_DateField = new JTextField(20);
								JTextField s_TimeField = new JTextField(20);
								JTextField e_TimeField = new JTextField(20);
								JTextField locationField = new JTextField(20);
								JTextField timeFrameField = new JTextField(5);
								JTextField intervalField = new JTextField(5);

								updatePanel.add(new JLabel("Title: "));
								updatePanel.add(titleField);
								updatePanel.add(new JLabel("Start Date (yyyy-MM-dd): "));
								updatePanel.add(s_DateField);
								updatePanel.add(new JLabel("End Date (yyyy-MM-dd): "));
								updatePanel.add(e_DateField);
								updatePanel.add(new JLabel("Start Time (hh:mm:ss): "));
								updatePanel.add(s_TimeField);
								updatePanel.add(new JLabel("End Time (hh:mm:ss): "));
								updatePanel.add(e_TimeField);
								updatePanel.add(new JLabel("Location: "));
								updatePanel.add(locationField);
								updatePanel.add(new JLabel("Remind Time Frame: "));
								updatePanel.add(timeFrameField);
								updatePanel.add(new JLabel("Remind Interval: "));
								updatePanel.add(intervalField);

								JButton saveBtn = new JButton("Save");
								modifyPanel.add(updatePanel);
								modifyPanel.add(saveBtn, BorderLayout.SOUTH);
								updateFrame.add(modifyPanel);
								updateFrame.pack();
								updateFrame.setVisible(true);

								saveBtn.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										String up_title = titleField.getText();
										String up_s_DateStr = s_DateField.getText();
										String up_e_DateStr = e_DateField.getText();
										String up_s_TimeStr = s_TimeField.getText();
										String up_e_TimeStr = e_TimeField.getText();
										String up_location = locationField.getText();
										String up_timeFrameStr = timeFrameField.getText();
										String up_intervalStr = intervalField.getText();

										Date up_s_Date = Date.valueOf(up_s_DateStr);
										Date up_e_Date = Date.valueOf(up_e_DateStr);
										Time up_s_Time = Time.valueOf(up_s_TimeStr);
										Time up_e_Time = Time.valueOf(up_e_TimeStr);

										Integer up_timeFrame = Integer.valueOf(up_timeFrameStr);
										Integer up_interval = Integer.valueOf(up_intervalStr);

										String sqlUpdateE = "update events set title=?, s_date=?, e_date=?, s_time=?, e_time=?, location=? where eid in (select e.eid from events e, generates g where e.eid=g.eid and uid=? and s_date=?)";
										String sqlUpdateR = "update reminds set frame=?, interval=? where eid in (select e.eid from events e, generates g where e.eid=g.eid and uid=? and s_date=?)";

										try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
											PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateE)) {
											preparedStatement.setString(1, up_title);
											preparedStatement.setDate(2, up_s_Date);
											preparedStatement.setDate(3, up_e_Date);
											preparedStatement.setTime(4, up_s_Time);
											preparedStatement.setTime(5, up_e_Time);
											preparedStatement.setString(6, up_location);
											preparedStatement.setString(7, uid);
											preparedStatement.setDate(8, btnDay);
											preparedStatement.executeUpdate();

											try(PreparedStatement preparedStatement2 = connection.prepareStatement(sqlUpdateR)) {
												preparedStatement2.setInt(1, up_timeFrame);
												preparedStatement2.setInt(2, up_interval);
												preparedStatement2.setString(3, uid);
												preparedStatement2.setDate(4, btnDay);
												preparedStatement2.executeUpdate();
											} catch (SQLException ex) {
												ex.printStackTrace();
											}
										}catch (SQLException ex) {
											ex.printStackTrace();
										}
										updateFrame.dispose();
									}
								});
							}
						});

						JButton deleteBtn = new JButton("Delete");
						deleteBtn.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e){
								String sqlSelectDel = "select events.eid, generates.uid from events join generates on events.eid=generates.eid join reminds on events.eid=reminds.eid where generates.uid=? and events.eid in (select eid from events where s_date=?)";
								String sqlGeneratesDel = "delete from generates where uid=? and eid=?";
								String sqlRemindsDel = "delete from reminds where eid=?";
								String sqlEventsDel = "delete from events where eid=?";
								try(Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice");
									PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectDel);
									PreparedStatement preparedStatement2 = connection.prepareStatement(sqlGeneratesDel);
									PreparedStatement preparedStatement3 = connection.prepareStatement(sqlRemindsDel);
									PreparedStatement preparedStatement4 = connection.prepareStatement(sqlEventsDel)) {
									preparedStatement.setString(1, uid);
									preparedStatement.setDate(2, btnDay);

									ResultSet resultSet = preparedStatement.executeQuery();

									if (resultSet.next()) {
										String eid = resultSet.getString("eid");
										String delUid = resultSet.getString("uid");

										preparedStatement2.setString(1, delUid);
										preparedStatement2.setString(2, eid);
										preparedStatement2.executeUpdate();

										preparedStatement3.setString(1, eid);
										preparedStatement3.executeUpdate();

										preparedStatement4.setString(1, eid);
										preparedStatement4.executeUpdate();
									}
								JOptionPane.showMessageDialog(null, "The schedule is deleted!", "Delete Schedule", JOptionPane.INFORMATION_MESSAGE);
								}catch (SQLException ex) {
									ex.printStackTrace();
								}
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
						schPanel.add(schAddPanel);
						schPanel.add(btnPanel, BorderLayout.SOUTH);
		        		schFrame.add(schPanel);
		        		schFrame.setSize(400, 500);
    	        		schFrame.setVisible(true);
					}
				}
			});
			datePane.add(dayBtn);
		}
	}

	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "dbms_practice", "dbms_practice")){
			System.out.println("Connection established succesfully");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		new FamilyCalendar();
	}
}