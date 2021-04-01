package payment.production.payment;

public class Payment {

    public String number,amount,transection_type,payment_type,from_number,from_email,from_name,from_id,service,date,time;

    public Payment(){

    }

    public Payment(String number, String amount, String transection_type, String payment_type, String from_number, String from_email, String from_name, String from_id, String service, String date, String time) {
        this.number = number;
        this.amount = amount;
        this.transection_type = transection_type;
        this.payment_type = payment_type;
        this.from_number = from_number;
        this.from_email = from_email;
        this.from_name = from_name;
        this.from_id = from_id;
        this.service = service;
        this.date = date;
        this.time = time;
    }
}
