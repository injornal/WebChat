package Components.Message;

public class Person {
    
    private String name;
    private boolean loggedIn;

    public Person(String name, boolean status){
        this.name = name;
        this.loggedIn = status;
    }

    public boolean isLoggedIn(){
        if(loggedIn == true){
            return true;
        } else {
            return false;
        }
    }

    public void active(){
        loggedIn = true;
    }

    public void inactive(){
        loggedIn = false;
    }

    public String getName(){
        return name;
    }


}
