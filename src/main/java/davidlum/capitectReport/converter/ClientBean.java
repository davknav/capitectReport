package davidlum.capitectReport.converter;

public class ClientBean {
  public long id;
  public String name;
  public String ticker;
  public String type;
  public String category;
  public String assetClass;
  public double price;
  public double totalShares;
  public double totalValue;
  public double weight;
  public String client;
  public double clientShares;
  public double clientValue;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getAssetClass() {
    return assetClass;
  }

  public void setAssetClass(String assetClass) {
    this.assetClass = assetClass;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getTotalShares() {
    return totalShares;
  }

  public void setTotalShares(double totalShares) {
    this.totalShares = totalShares;
  }

  public double getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(double totalValue) {
    this.totalValue = totalValue;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public double getClientShares() {
    return clientShares;
  }

  public void setClientShares(double clientShares) {
    this.clientShares = clientShares;
  }

  public double getClientValue() {
    return clientValue;
  }

  public void setClientValue(double clientValue) {
    this.clientValue = clientValue;
  }

  @Override
  public String toString() {
    return "ClientBean{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", ticker='" + ticker + '\'' +
            ", type='" + type + '\'' +
            ", category='" + category + '\'' +
            ", assetClass='" + assetClass + '\'' +
            ", price=" + price +
            ", totalShares=" + totalShares +
            ", totalValue=" + totalValue +
            ", weight=" + weight +
            ", client='" + client + '\'' +
            ", clientShares=" + clientShares +
            ", clientValue=" + clientValue +
            '}';
  }
}
