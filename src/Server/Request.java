package Server;

public class Request {
    private String[] data;

    public enum Type {
        GET_ALL, GET_UNREAD, SEND, LOGIN, SIGNUP, LOGOUT
    }

    Type type;

    Request(String req) {
        this.data = req.split(" ");
        this.type = switch (data[0]) {
            case "GET_ALL" -> this.type = Type.GET_ALL;
            case "GET_UNREAD" -> Type.GET_UNREAD;
            case "SEND" -> Type.SEND;
            case "LOGIN" -> Type.LOGIN;
            case "SIGNUP" -> Type.SIGNUP;
            case "LOGOUT" -> Type.LOGOUT;
            default -> throw new IllegalArgumentException();
        };
    }

    public String[] getData() {
        return data;
    }
}
