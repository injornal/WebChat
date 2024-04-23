package Server;

public class Password {
    private String passwordCyphered;

    public Password(String password) {
        this.passwordCyphered = password; // TODO cypher password
    }

    public boolean equals(Password psw) {
        return psw.getPasswordCyphered().equals(this.passwordCyphered);
    }

    private String getPasswordCyphered() {
        return passwordCyphered;
    }
}
