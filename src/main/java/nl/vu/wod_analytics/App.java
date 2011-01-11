package nl.vu.wod_analytics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.semanticweb.yars.nx.BNode;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

// Check if a rel exist: http://wiki.neo4j.org/content/FAQ
// TODO read http://wiki.neo4j.org/content/Design_Guide
public class App {
	final static String DATA = "/var/data/btc-2010-chunk-123.gz";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println("Hello World!");

		GraphDatabaseService graphDb = new EmbeddedGraphDatabase("/tmp/graphdb");
		graphDb.shutdown();

		
		InputStream is = new GZIPInputStream(new FileInputStream(DATA));
		NxParser nxp = new NxParser(is, false);

		while (nxp.hasNext()) {
			Node[] ns = nxp.next();

			// Ignore dumb triples (just in case)
			if (!(ns[1] instanceof Literal))
				continue;
			
			// Ignore literals
			if (ns[2] instanceof Literal)
				continue;

			// Ignore blank nodes
			if (ns[0] instanceof BNode || ns[2] instanceof BNode)
				continue;

			// Get the namespaces
			String ns1 = getNameSpace(ns[0].toN3());
			String ns2 = getNameSpace(ns[2].toN3());
			
			// Ignore inner connections
			if (ns1.equals(ns2))
				continue;
			
			for (Node n : ns) {
				System.out.print(n.toN3());
				System.out.print(" ");
			}
			System.out.println(".");
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private static String getNameSpace(String string) {
		// Detect '#'
		String[] p = string.split("#");
		if (p.length == 2)
			return p[0];
		
		// Fall back at cutting on the last '/'
		int pos = string.lastIndexOf('/');
		return string.substring(0, pos-1);
	}
}
