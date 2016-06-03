package swiftanalysis;

import swiftanalysis.analyzers.Analyzer;
import swiftanalysis.analyzers.EmptyCatchBlockCounter;

import java.io.File;
import java.util.stream.Stream;

/**
 * Analyzes given Swift projects with the analyzer of choice.
 */
public class SwiftAnalyzer {

    /**
     * Analyzer of choice to analyze the project(s)
     */
    private static Analyzer analyzer = new EmptyCatchBlockCounter();
    //private static Analyzer analyzer = new MscrMetricsProjectAnalyzer();
    
    /**
     * Main method accepts one or more project paths to analyze.
     *
     * @param args array of project paths
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Specify one or more project paths");
            return;
        }

        ProjectParser parser = new ProjectParser();
        for (String projectPath : args) {
            File directory = new File(projectPath);
            Stream<AST> treeStream = parser.parseProject(directory);
            analyzer.analyzeProject(directory, treeStream);
        }
        analyzer.allProjectsDone();
    }

}