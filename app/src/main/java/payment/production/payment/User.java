package payment.production.payment;

public class User {
    public String uname,uid,phone,balance,email,type,password;

    public User(){

    }

    public User(String uname, String uid, String phone, String balance, String email, String type, String password) {
        this.uname = uname;
        this.uid = uid;
        this.phone = phone;
        this.balance = balance;
        this.email = email;
        this.type = type;
        this.password = password;
    }
}
