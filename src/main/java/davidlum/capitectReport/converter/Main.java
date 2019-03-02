package davidlum.capitectReport.converter;

import com.google.common.io.Files;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A command-line utility to parse an Asset Allocation CSV from Capitect and produce another CSV
 * in the format described here: https://docs.google.com/spreadsheets/d/1tPeZp3eHJ_zynI8YgKmTkyuft2mb_2sBQhHzZmR2IPc/edit
 */
public class Main {
  private static final String OPT_INPUT_CSV = "input-csv";
  private static final String OPT_HELP = "help";
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH_mm");

  public static void main(String[] args) throws Exception {
    CommandLineParser cmdLineParser = new DefaultParser();
    Options opts = makeOptions();
    CommandLine commandLine = null;
    try {
      commandLine = cmdLineParser.parse(opts, args);
    } catch (MissingOptionException moe) {
      printUsage(opts);
      System.out.println("\n" + moe.getMessage());
      System.exit(1);
    }

    if (commandLine.hasOption(OPT_HELP)) {
      printHelp(opts);
      System.exit(0);
    }

    String inputCsvPath = commandLine.getOptionValue(OPT_INPUT_CSV);
    File inputCsv = new File(inputCsvPath);
    File outputCsv = makeOutputFileFromInputFile(inputCsv);
    CsvConverter converter = new CsvConverter(inputCsv, outputCsv);
    converter.process();
  }

  public static File makeOutputFileFromInputFile(File inputCsv) {
    String inputCsvPath = inputCsv.getAbsolutePath();
    String ext = Files.getFileExtension(inputCsvPath);
    String withoutExt = Files.getNameWithoutExtension(inputCsvPath);
    String dir = inputCsv.getParent();
    return new File(dir + "/" + withoutExt + "_PROCESSED_" + DATE_FORMAT.format(new Date()) + "." + ext);
  }

  private static Options makeOptions() {
    Options options = new Options();
    options.addOption(Option.builder("i")
            .required(true)
            .hasArg(true)
            .argName("inputCsv")
            .longOpt(OPT_INPUT_CSV)
            .desc("The Capitect CSV file to process")
            .build());
    options.addOption(Option.builder("h")
            .required(false)
            .hasArg(false)
            .longOpt(OPT_HELP)
            .desc("Show a help message")
            .build());
    return options;
  }

  private static void printUsage(Options options)
  {
    HelpFormatter formatter = new HelpFormatter();
    String syntax = Main.class.getName();
    System.out.println("\n=====");
    System.out.println("USAGE");
    System.out.println("=====");
    PrintWriter pw  = new PrintWriter(System.out);
    formatter.printUsage(pw, 80, syntax, options);
    pw.flush();
  }

  private static void printHelp(Options options)
  {
    HelpFormatter formatter = new HelpFormatter();
    String syntax = Main.class.getName();
    String usageHeader = "Running the Capitect Report Mangler";
    System.out.println("\n====");
    System.out.println("HELP");
    System.out.println("====");
    formatter.printHelp(syntax, usageHeader, options, "");
  }
}
