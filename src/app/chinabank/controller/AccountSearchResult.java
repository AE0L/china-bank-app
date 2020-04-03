package app.chinabank.controller;

import app.chinabank.model.schemas.Schema;
import app.chinabank.utils.TimeUtil;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import org.json.simple.JSONObject;

final class AccountSearchResult implements Schema {

  private String    accountNo;
  private String    accountHolder;
  private String    accountType;
  private String    productType;
  private Timestamp dateOpen;

  AccountSearchResult(LinkedHashMap<String, Object> record) {
    accountNo     = (String) record.get("AccountNo");
    accountHolder = (String) record.get("AccountHolder");
    accountType   = (String) record.get("AccountType");
    productType   = (String) record.get("ProductType");
    dateOpen      = (Timestamp) record.get("DateOpen");
  }

  @Override
  @SuppressWarnings("unchecked")
  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("account_no", accountNo);
    json.put("account_holder", accountHolder);
    json.put("account_type", accountType);
    json.put("product_type", productType);
    json.put("date_opened", TimeUtil.dateFormat(dateOpen));

    return json.toJSONString();
  }
}
