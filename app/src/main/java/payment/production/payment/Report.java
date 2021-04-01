package payment.production.payment;

public class Report {
    public String text,email,name,userid,number;

    public Report() {
    }

    public Report(String text, String email, String name, String userid, String number) {
        this.text = text;
        this.email = email;
        this.name = name;
        this.userid = userid;
        this.number = number;
    }
}
