package davidlum.capitectReport.converter;

import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class CsvConverter {
  private File inputCsv;
  private File outputCsv;

  public CsvConverter(File inputCsv, File outputCsv) {
    this.inputCsv = inputCsv;
    this.outputCsv = outputCsv;
  }

  public void process() throws Exception {
    List<ClientBean> clientBeans = readClientBeans();
    Map<String, List<ClientBean>> beansByClient = clientBeans.stream()
            .collect(groupingBy(ClientBean::getClient, TreeMap::new, Collectors.toList()));
    List<AssetClassAccumulator> assetClassAccumulators = new ArrayList<>();
    beansByClient.forEach((client, bean) -> {
      assetClassAccumulators.add(new AssetClassAccumulator(client, bean));
    });
    List<String> sortedAssetClasses = getSortedAssetClasses(assetClassAccumulators);
    writeProcessedOutput(assetClassAccumulators, sortedAssetClasses);
  }

  private List<ClientBean> readClientBeans() throws Exception {
    List<ClientBean> clientBeans = new ArrayList<>();
    try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(inputCsv), CsvPreference.STANDARD_PREFERENCE)) {
      String[] header = beanReader.getHeader(true);
      for (int i=0; i<header.length; i++) {
        header[i] = header[i].replace(" ", "");
      }
      final CellProcessor[] processors = getProcessors();
      ClientBean clientBean;
      while( (clientBean = beanReader.read(ClientBean.class, header, processors)) != null ) {
        clientBeans.add(clientBean);
      }
    }
    return clientBeans;
  }

  private static CellProcessor[] getProcessors() {
    final CellProcessor[] processors = new CellProcessor[] {
      new ParseLong(), // id
      new Optional(), //name,
      new Optional(), // ticker,
      new Optional(), // type,
      new Optional(), // category,
      new Optional(), // assetClass
      new ParseDouble(), // price,
      new ParseDouble(), // totalShares,
      new ParseDouble(), // totalValue,
      new ParseDouble(), // weight,
      new NotNull(), // client,
      new ParseDouble(), // clientShares,
      new ParseDouble() // clientValue;
    };
    return processors;
  }

  private void writeProcessedOutput(List<AssetClassAccumulator> assetClassAccumulators, List<String> sortedAssetClasses)
          throws Exception {
    try (ICsvListWriter listWriter = new CsvListWriter(new FileWriter(outputCsv),
              CsvPreference.STANDARD_PREFERENCE)) {

      FmtNumber fmtNumber = new FmtNumber(new DecimalFormat());

      List<String> headers = new ArrayList<>();
      List<CellProcessor> processors = new ArrayList<>();

      headers.add("Client");
      processors.add(new NotNull());

      headers.add("Client Value");
      processors.add(fmtNumber);

      for (String assetClass : sortedAssetClasses) {
        headers.add(assetClass);
        processors.add(fmtNumber);
      }
      for (String assetClass : sortedAssetClasses) {
        headers.add(assetClass + " %");
        processors.add(fmtNumber);
      }

      headers.add("ALL Stock %");
      processors.add(fmtNumber);

      headers.add("ALL Bonds + Cash %");
      processors.add(fmtNumber);

      headers.add("Uncategorized/Other %");
      processors.add(fmtNumber);

      // write the header
      listWriter.writeHeader(headers.toArray(new String[0]));

      // write the beans
      for(AssetClassAccumulator acc : assetClassAccumulators) {
        List<Object> data = new ArrayList<>();
        data.add(acc.client);
        data.add(acc.totalClientValue);
        for (String assetClass : sortedAssetClasses) {
          data.add(acc.assetClassNameToTotal.getOrDefault(assetClass, 0.0));
        }
        for (String assetClass : sortedAssetClasses) {
          data.add(acc.assetClassNameToTotal.getOrDefault(assetClass, 0.0) / acc.totalClientValue);
        }
        data.add(acc.stockTotal / acc.totalClientValue);
        data.add(acc.bondsPlusCashTotal / acc.totalClientValue);
        data.add(acc.uncategorizedTotal / acc.totalClientValue);

        listWriter.write(data, processors.toArray(new CellProcessor[0]));
      }
    }
  }

  private static List<String> getSortedAssetClasses(List<AssetClassAccumulator> assetClassAccumulators) {
    Set<String> assetClasses = new TreeSet<>(new AssetClassComparator());
    for (AssetClassAccumulator acc : assetClassAccumulators) {
      assetClasses.addAll(acc.assetClassNameToTotal.keySet());
    }
    return new ArrayList<>(assetClasses);
  }

}
