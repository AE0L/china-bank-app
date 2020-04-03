package app.chinabank.model.exceptions;


public class RecordSearchException extends Exception {

  private static final long serialVersionUID = 6194462630328152252L;

  public RecordSearchException(String msg) {
    super(msg);
  }

  public RecordSearchException() {
    super("No record/s was found or the Database didn't return any result");
  }

}
