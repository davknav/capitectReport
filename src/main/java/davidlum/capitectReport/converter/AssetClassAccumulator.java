package davidlum.capitectReport.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetClassAccumulator {

  public String client;
  public List<ClientBean> beans;

  public Map<String,Double> assetClassNameToTotal = new HashMap<>();
  public double totalClientValue = 0.0;
  public double stockTotal = 0.0;
  public double bondsPlusCashTotal = 0.0;
  public double uncategorizedTotal = 0.0;

  public AssetClassAccumulator(String client, List<ClientBean> beans) {
    this.client = client;
    this.beans = beans;
    for (ClientBean bean : beans) {
      add(bean);
    }
  }

  public void add(ClientBean bean) {
    String assetClass = bean.getAssetClass();
    double clientValue = bean.getClientValue();
    totalClientValue += clientValue;
    assetClassNameToTotal.put(assetClass, assetClassNameToTotal.getOrDefault(assetClass, 0.0) + clientValue);
    if (assetClass.endsWith("Stock")) {
      stockTotal += clientValue;
    } else if (assetClass.endsWith("Bond")) {
      bondsPlusCashTotal += clientValue;
    } else if (assetClass.endsWith("Cash")) {
      bondsPlusCashTotal += clientValue;
    } else {
      uncategorizedTotal += clientValue;
    }
  }

  @Override
  public String toString() {
    return "AssetClassAccumulator{" +
            "client='" + client + '\'' +
            ", beans=" + beans +
            ", assetClassNameToTotal=" + assetClassNameToTotal +
            ", totalClientValue=" + totalClientValue +
            ", stockTotal=" + stockTotal +
            ", bondsPlusCashTotal=" + bondsPlusCashTotal +
            ", uncategorizedTotal=" + uncategorizedTotal +
            '}';
  }
}
