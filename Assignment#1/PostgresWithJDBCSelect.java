import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

class Student {
	int rollID;
	String name;
	String section;
	LocalDateTime createdDate;

	public Student () {
		rollID = -1;
		name = "";
		section = "";
	}

	public void setRoll(int rollID) {
		this.rollID = rollID;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSection(String section) {
		this.section = section;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public int getRoll() {
		return this.rollID;
	}

	public String getName() {
		return this.name;
	}

	public String getSection() {
		return this.section;
	}

	public LocalDateTime getCreatedDate() {
		return this.createdDate;
	}
}

public class PostgresWithJDBCSelect {
	public static void main(String[] args) {
		List < Student > studentList = new ArrayList < > ();
		String SQL_SELECT = "Select * from STUDENT";
		// establishes database connection
		// auto closes connection and preparedStatement
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/yschoi", "yschoi", "1008");
			PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int rollId = resultSet.getInt("ROLL");
				String name = resultSet.getString("NAME");
				String section = resultSet.getString("SECTION");
				Timestamp createdDate = resultSet.getTimestamp("CREATED_DATE");
				Student student = new Student();
				student.setRoll(rollId);
				student.setName(name);
				student.setSection(section);
				student.setCreatedDate(createdDate.toLocalDateTime());
				studentList.add(student);
			}
			for (Student student: studentList) {
				System.out.println("Roll No: " + student.getRoll());
				System.out.println("Name: " + student.getName());
				System.out.println("Section: " + student.getSection());
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
