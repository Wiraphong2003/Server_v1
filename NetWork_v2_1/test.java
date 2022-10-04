public class test {
    public static void main(String[]args){
        Person fim = new Person("fim");
        ClientManager c = new ClientManager("192.168.100.154",50101);
        c.sendObjectToServer(fim);

    }
}

class Person{
    String name;
    Person(String name){
        this.name = name;
    }
    public String toString(){
        return name;
    }
}
