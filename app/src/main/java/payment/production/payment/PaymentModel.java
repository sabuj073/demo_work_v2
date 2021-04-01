package payment.production.payment;

public class PaymentModel {
    public String number,amount,transection_type,payment_type,from_number,from_email,from_name,from_id,key;

    public PaymentModel(String number, String amount, String transection_type, String payment_type, String from_number, String from_email, String from_name, String from_id, String key) {
        this.number = number;
        this.amount = amount;
        this.transection_type = transection_type;
        this.payment_type = payment_type;
        this.from_number = from_number;
        this.from_email = from_email;
        this.from_name = from_name;
        this.from_id = from_id;
        this.key = key;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransection_type() {
        return transection_type;
    }

    public void setTransection_type(String transection_type) {
        this.transection_type = transection_type;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getFrom_number() {
        return from_number;
    }

    public void setFrom_number(String from_number) {
        this.from_number = from_number;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
