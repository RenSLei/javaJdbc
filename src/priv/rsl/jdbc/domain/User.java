package priv.rsl.jdbc.domain;

import java.util.Date;

/** 
* @ClassName: User 
* @Description: TODO  
* ���һ��user ��domain����
* һ��������ֻҪ�������ݷ��ʲ��Date��һ����util���߰��е�Date����
* @author rsl
* @date 2018��3��18��
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
