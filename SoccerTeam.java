/*
 2.	使用某种方式存储一个足球队的3个人员(包括教练和队员)的信息：
•	人员信息包括：姓名，ID，在球队中的角色(队员及教练等); 
•	要求至少包含球队类(class)及人员类(class)；
•	打印出所有人员的信息。
 */
package Class3_18;

public class SoccerTeam {
	private Person A;
	private Person b;
	private Person c;
	//Person members[0]("A","1111","教练");
	public SoccerTeam() {
		A=new Person("A","1111","教练");
		b=new Person("b","2222","队员");
		c=new Person("c","3333","队员");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SoccerTeam Team=new SoccerTeam();
		System.out.println(">>人员信息：");
		System.out.println("name:"+Team.A.getname()+" ID:"+Team.A.getID()+" role:"+Team.A.getrole());
		System.out.println("name:"+Team.b.getname()+" ID:"+Team.b.getID()+" role:"+Team.b.getrole());
		System.out.println("name:"+Team.c.getname()+" ID:"+Team.c.getID()+" role:"+Team.c.getrole());
	}

}
class Person{        //把public去掉后不报错
	private String name;//添加private后  public class person仍报错
	private String ID;
	private String role;
	public Person(String name,String ID,String role) {
		this.name=name;
		this.ID=ID;
		this.role=role;
	}
	public String getname() {
		return name;
	}
	public String getID() {
		return ID;
	}
	public String getrole() {
		return role;
	}
}


//在Java中，一个源文件中只能有一个public类，而且该类的名称必须与源文件的名称相同。
//如果在同一个源文件中定义了多个public类，编译器将会报错。










