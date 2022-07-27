package com.klapeks.libs.exceptions;

@SuppressWarnings("serial")
public class NoPermsException extends NoCatchException {
	
	String perms;
	public NoPermsException(String perms) {
		super("§cNo perms");
		this.perms = perms;
	}
	
	public String getPerms() {
		return perms;
	}
	
}
