package priv.rsl.jdbc;

public class ORMBean {
    private int id;
    private String name;
    private String passWord;
    
    @Override
    public String toString() {
	return "ORMBean [id=" + id + ", name=" + name + ", passWord=" + passWord + "]";
    }
    
    
    public ORMBean() {
	
    }


    public ORMBean(int id, String name, String passWord) {
	super();
	this.id = id;
	this.name = name;
	this.passWord = passWord;
    }


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

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String passWord) {
        this.passWord = passWord;
    }
    
}
