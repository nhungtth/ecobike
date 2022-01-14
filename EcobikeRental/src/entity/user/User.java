package entity.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import entity.db.EcobikeDB;
import entity.rentbike.RentBike;
import utils.Utils;

public class User {
	private static Logger LOGGER = Utils.getLogger(User.class.getName());
    private String id;
    private String name;
    private String email;
    private String address;
    private String phone;

    public User(String id, String name){
        this.id = id;
        this.name = name;
    }
    
    // getter and setter
    
    public String getName() {
        return this.name;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public static User getCurrentUser() {
		try {
			Connection con = EcobikeDB.getConnection();
			String sql = "SELECT * FROM user";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet rs = statement.executeQuery(sql);

			if (rs.next()) {
				User user = new User(rs.getString("user_id"), rs.getString("name"));
				LOGGER.info("Current user: " + user.getName());
				return user;
			} else {
				LOGGER.info("There is no user logged in.");
			}
			statement.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
    
}
