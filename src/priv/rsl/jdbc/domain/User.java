package priv.rsl.jdbc.domain;

import java.util.Date;

/** 
* @ClassName: User 
* @Description: TODO  
* 设计一个user 的domain对象
* 一般来讲，只要不是数据访问层的Date，一般用util工具包中的Date对象
* @author rsl
* @date 2018年3月18日
*  
*/
public class User {
    private int id;
    private String name;
    private String passWord;
    private Date birthday;
    private float money;
    
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public float getMoney() {
        return money;
    }
    public void setMoney(float money) {
        this.money = money;
    }
    public String getPassword() {
	return passWord;
    }
    public void setPassword(String passWord) {
	this.passWord = passWord;
    }
    @Override
    public String toString() {
	return "User [id=" + this.id + ", name=" + this.name + ", passWord=" + this.passWord + ", birthday=" + this.birthday + ", money="
		+ this.money + "]";
    }
    
    
}
