package com.customer;

import java.sql.Blob;

/**
 * User.java
 * This is a model class represents a User entity
 * @author Ramesh Fadatare
 *
 */
// 只有純粹的建構子 以及set get 的Java檔 統稱 POJO
//User 是 網頁 與 數據庫 之間的 數據傳遞的橋樑。 透過創新User對象初始化來存取資料， DAO 則提取初始化的新對象 User資料，傳送到數據庫
public class User {
	protected int id;
	protected String mail;
	protected String time;
	protected String images;
	protected Blob image;
	
	public User(String images) {
		super();
		this.images = images;
	}	
	
	
	public User(String time,Blob image) {
		super();
		this.time=time;
		this.image = image;
	}	
	
	public User(String time, String images) {
		super();
		this.time=time;
		this.images=images;
	}
	
	public User(int id, String time, String images) {
		super();
		this.id = id;
		this.time=time;
		this.images = images;
	}
	
	public User(int id, String time, Blob image) {
		super();
		this.id = id;
		this.time=time;
		this.image = image;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
    
    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}