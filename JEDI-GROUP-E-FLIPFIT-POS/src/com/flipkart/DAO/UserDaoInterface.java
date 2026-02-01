package com.flipkart.DAO;

import com.flipkart.bean.UserRole;
import com.flipkart.exceptions.UserNotFoundException;

public interface UserDaoInterface {

	UserRole getUser(String userEmail) throws UserNotFoundException;

	public void addUserRole(UserRole userRole);

}
