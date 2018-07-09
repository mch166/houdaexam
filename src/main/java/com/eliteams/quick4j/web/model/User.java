package com.eliteams.quick4j.web.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户模型
 * 
 * @author StarZou
 * @since 2014年7月5日 下午12:07:20
 **/
public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String username;
    
    private String name;

    private String password;

    private String state;

    private String createTime;
    
    private int type;
    
    private String sjid;
    
    private String phone;
    
    //用户更改密码使用
    private String newpassword;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}
	
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", state=" + state + ", createTime=" + createTime + "]";
    }

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}


}