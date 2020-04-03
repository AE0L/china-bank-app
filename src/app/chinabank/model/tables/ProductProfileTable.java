package app.chinabank.model.tables;


public class ProductProfileTable extends Table {

  private static ProductProfileTable INSTANCE = null;

  private ProductProfileTable() {
    super("PRODUCT_PROFILE");

    this.metadata.put("ProductType", Type.STRING);
    this.metadata.put("InterestRate", Type.DOUBLE);
    this.metadata.put("InitialRequirement", Type.DOUBLE);
    this.metadata.put("ProductCurrency", Type.STRING);
  }

  public static ProductProfileTable getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ProductProfileTable();
    }

    return INSTANCE;
  }

}
