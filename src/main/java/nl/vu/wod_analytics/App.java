package nl.vu.wod_analytics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

/**
 * Hello world!
 * 
 */
public class App {
	final static String DATA = "/var/data/btc-2010-chunk-123.gz";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println("Hello World!");

		InputStream is = new GZIPInputStream(new FileInputStream(DATA));
		NxParser nxp = new NxParser(is, false);

		while (nxp.hasNext()) {
			Node[] ns = nxp.next();
			
			// Ignore literals
			if (ns[2] instanceof Literal)
				continue;
			
			// Ignore blank nodes
			if (ns[0] instanceof BNode || ns[2] instanceof BNode)
				continue;
			
			for (Node n : ns) {
				System.out.print(n.toN3());
				System.out.print(" ");
			}
			System.out.println(".");
		}
	}
}
