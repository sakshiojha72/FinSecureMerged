package com.ds.app.service;

import com.ds.app.entity.AppUser;

public interface AppUserService {

	public AppUser registerAppUser(AppUser user);

	public AppUser findByUsername(String username);
}
