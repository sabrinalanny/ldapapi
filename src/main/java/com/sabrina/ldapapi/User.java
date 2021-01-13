package com.sabrina.ldapapi;

/**
 * @author Sabrina
 *
 */
public class User {

	private String uid;
	private String cn;
	private String sn;

	public User(String uid, String cn, String sn) {
		super();
		this.uid = uid;
		this.cn = cn;
		this.sn = sn;
	}

	public User() {
		super();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", cn=" + cn + ", sn=" + sn + "]";
	}	

}
