package app.chinabank.model.schemas;

import java.util.LinkedHashMap;

public class ProductProfile implements Schema {

  public String  type;
  public String  currency;
  @SuppressWarnings("unused")
  private Double interestRate;

  public ProductProfile(LinkedHashMap<String, Object> profile) {
    this.type         = (String) profile.get("ProductType");
    this.currency     = (String) profile.get("ProductCurrency");
    this.interestRate = (Double) profile.get("InterestRate");
  }

  @Override
  public String toJSON() {
    return null;
  }

}
